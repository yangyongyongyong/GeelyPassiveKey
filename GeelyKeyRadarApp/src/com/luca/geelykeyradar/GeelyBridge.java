package com.luca.geelykeyradar;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对吉利汽车（com.geely.consumer）与吉利银河（com.geelygalaxy.customer）双官方 App 的最小桥接。
 *
 * 只使用官方 App 已经导出的组件：
 * - MiuiService：把本应用扫到的 ScanResult 交给对应的官方钥匙 SDK；
 * - DigitalKeyBgService：保活绑定；
 * - 通知栏控制广播：作为通知按钮点击失败时的兜底。
 *
 * 不读取官方 App 私有数据，不依赖 adb/REA，也不写死 VIN/MAC。
 */
final class GeelyBridge {

    private static final String TAG = "GeelyBridge";

    static final String GEELY_PKG = OfficialBrandPolicy.PKG_GALAXY;
    static final String GEELY_DK_SERVICE = OfficialBrandPolicy.DK_BG_SERVICE_CLASS;
    static final String GEELY_MIUI_SERVICE = OfficialBrandPolicy.MIUI_SERVICE_CLASS;

    static final String ACTION_DK_START = OfficialBrandPolicy.ACTION_DK_START;
    static final String ACTION_MIUI_SERVICE = OfficialBrandPolicy.ACTION_MIUI_GALAXY;
    static final String ACTION_MIUI_CONSUMER = OfficialBrandPolicy.ACTION_MIUI_CONSUMER;
    static final String ACTION_NOTIFICATION_CTRL = "com.snc.dk.rv.notification.ctrl";
    static final String ACTION_REMOTE_VIEW_CLICK = "com.geely.cockpit.ble.RemoteViewClick";
    static final String EXTRA_CMD = "com.extra.cmd";

    static final int CTRL_QUERY = 0;
    static final int CTRL_UNLOCK = 1;
    static final int CTRL_LOCK = 2;

    static int toDk15Cmd(int logicalCtrlType) {
        if (logicalCtrlType == CTRL_UNLOCK) return 2;
        if (logicalCtrlType == CTRL_LOCK) return 1;
        return logicalCtrlType;
    }

    private static final Set<String> boundPackages = new LinkedHashSet<>();
    private static long wakeSequence = 0L;

    private static final ServiceConnection KEEP_ALIVE_CONNECTION = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_CONNECTED",
                    "pkg=" + name.getPackageName() + " component=" + name.getClassName());
            Log.i(TAG, "官方钥匙服务保活绑定成功: " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_DISCONNECTED",
                    "pkg=" + name.getPackageName() + " component=" + name.getClassName());
            Log.w(TAG, "官方钥匙服务保活绑定断开: " + name);
            synchronized (GeelyBridge.class) {
                boundPackages.remove(name.getPackageName());
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_DIED",
                    "pkg=" + name.getPackageName() + " component=" + name.getClassName());
            Log.w(TAG, "官方钥匙服务保活绑定失效: " + name);
            synchronized (GeelyBridge.class) {
                boundPackages.remove(name.getPackageName());
            }
        }
    };

    private GeelyBridge() {
    }

    static boolean isGeelyInstalled(Context context) {
        return OfficialTarget.isAnyInstalled(context);
    }

    static boolean isTargetInstalled(Context context, OfficialBrandPolicy.Brand userMode,
                                     OfficialBrandPolicy.Brand vehicleBrand) {
        if (context == null) return false;
        OfficialBrandPolicy.Brand resolved = OfficialTarget.resolveBrand(context, userMode, vehicleBrand);
        return OfficialTarget.isBrandInstalled(context, resolved);
    }

    static synchronized void bindKeepAlive(Context context) {
        if (context == null) return;
        VehicleGarage garage = new VehicleIdentityStore(context).loadGarage();
        bindKeepAlive(context, garage.brandMode);
    }

    static synchronized void bindKeepAlive(Context context, OfficialBrandPolicy.Brand brandMode) {
        DiagnosticLog.initialize(context);
        if (context == null) return;
        if (brandMode == OfficialBrandPolicy.Brand.CONSUMER) {
            bindKeepAlivePackage(context, OfficialBrandPolicy.PKG_CONSUMER);
        } else if (brandMode == OfficialBrandPolicy.Brand.GALAXY) {
            bindKeepAlivePackage(context, OfficialBrandPolicy.PKG_GALAXY);
        } else {
            if (OfficialTarget.isConsumerInstalled(context)) {
                bindKeepAlivePackage(context, OfficialBrandPolicy.PKG_CONSUMER);
            }
            if (OfficialTarget.isGalaxyInstalled(context)) {
                bindKeepAlivePackage(context, OfficialBrandPolicy.PKG_GALAXY);
            }
        }
    }

    private static synchronized void bindKeepAlivePackage(Context context, String pkg) {
        if (context == null || !OfficialTarget.isInstalled(context, pkg)) return;
        if (boundPackages.contains(pkg)) return;

        String action = OfficialBrandPolicy.miuiActionForPackage(pkg);
        boolean miuiAccepted = false;
        try {
            Intent miui = new Intent(action);
            miui.setComponent(new ComponentName(pkg, GEELY_MIUI_SERVICE));
            miuiAccepted = context.bindService(miui, KEEP_ALIVE_CONNECTION,
                    Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            if (miuiAccepted) boundPackages.add(pkg);
            DiagnosticLog.detail("Bridge", "KEEPALIVE_BIND_REQUEST",
                    "pkg=" + pkg + " component=MiuiService accepted=" + miuiAccepted);
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Bridge", "KEEPALIVE_BIND_ERROR", t);
            Log.w(TAG, "绑定 MiuiService(" + pkg + ") 失败: " + t.getMessage());
        }

        try {
            Intent dk = new Intent(ACTION_DK_START);
            dk.setComponent(new ComponentName(pkg, GEELY_DK_SERVICE));
            boolean accepted = context.bindService(dk, KEEP_ALIVE_CONNECTION,
                    Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            DiagnosticLog.detail("Bridge", "KEEPALIVE_BIND_REQUEST",
                    "pkg=" + pkg + " component=DigitalKeyBgService accepted=" + accepted);
        } catch (Throwable ignored) {
            DiagnosticLog.detail("Bridge", "OPTIONAL_KEEPALIVE_BIND_ERROR", DiagnosticText.errorSummary(ignored));
        }
    }

    static OfficialBrandPolicy.Brand wakeAndConnect(Context context, ScanResult scanResult, String carId,
                                                    OfficialBrandPolicy.Brand userMode,
                                                    OfficialBrandPolicy.Brand vehicleBrand) {
        if (context == null || scanResult == null) return OfficialBrandPolicy.Brand.CONSUMER;
        DiagnosticLog.initialize(context);
        long wakeId = ++wakeSequence;
        OfficialBrandPolicy.Brand targetBrand = OfficialTarget.resolveBrand(context, userMode, vehicleBrand);
        String pkg = OfficialBrandPolicy.packageForBrand(targetBrand);
        if (!OfficialTarget.isInstalled(context, pkg)) {
            DiagnosticLog.event("Bridge", "WAKE_UNAVAILABLE",
                    "wake_id=" + wakeId + " pkg=" + pkg + " official_app_installed=false");
            return targetBrand;
        }
        String action = OfficialBrandPolicy.miuiActionForPackage(pkg);
        String safeCarId = (carId == null || carId.trim().isEmpty()) ? "geely-ble" : carId.trim();
        String mac = (scanResult.getDevice() != null) ? scanResult.getDevice().getAddress() : "unknown";
        DiagnosticLog.detail("Bridge", "WAKE_BEGIN", "wake_id=" + wakeId
                + " brand=" + targetBrand + " pkg=" + pkg
                + " addr_id=" + DiagnosticLog.fingerprint(mac) + " rssi=" + scanResult.getRssi());

        bindKeepAlivePackage(context, pkg);
        sendMiuiService(context, pkg, action, safeCarId, scanResult, wakeId);
        return targetBrand;
    }

    static void wakeAndConnectFallback(Context context, ScanResult scanResult, String carId,
                                       OfficialBrandPolicy.Brand userMode,
                                       OfficialBrandPolicy.Brand vehicleBrand,
                                       OfficialBrandPolicy.Brand primaryUsed) {
        if (context == null || scanResult == null || !isGeelyInstalled(context)) return;
        DiagnosticLog.initialize(context);
        long wakeId = ++wakeSequence;
        String safeCarId = (carId == null || carId.trim().isEmpty()) ? "geely-ble" : carId.trim();

        boolean consumerInstalled = OfficialTarget.isConsumerInstalled(context);
        boolean galaxyInstalled = OfficialTarget.isGalaxyInstalled(context);
        OfficialBrandPolicy.Brand secondary = OfficialBrandPolicy.secondaryAutoBrand(
                primaryUsed, userMode, vehicleBrand, consumerInstalled, galaxyInstalled);
        if (secondary != null) {
            String secPkg = OfficialBrandPolicy.packageForBrand(secondary);
            String secAction = OfficialBrandPolicy.miuiActionForPackage(secPkg);
            DiagnosticLog.event("Bridge", "WAKE_FALLBACK",
                    "wake_id=" + wakeId + " brand=" + secondary + " pkg=" + secPkg + " action=" + secAction);
            bindKeepAlivePackage(context, secPkg);
            sendMiuiService(context, secPkg, secAction, safeCarId, scanResult, wakeId);
            return;
        }

        if (primaryUsed == OfficialBrandPolicy.Brand.GALAXY && galaxyInstalled) {
            DiagnosticLog.event("Bridge", "WAKE_FALLBACK",
                    "wake_id=" + wakeId + " pkg=" + OfficialBrandPolicy.PKG_GALAXY
                            + " action=" + ACTION_MIUI_CONSUMER);
            sendMiuiService(context, OfficialBrandPolicy.PKG_GALAXY, ACTION_MIUI_CONSUMER,
                    safeCarId, scanResult, wakeId);
        }
    }

    private static void sendMiuiService(Context context, String pkg, String action,
                                        String carId, ScanResult scanResult, long wakeId) {
        try {
            Intent intent = new Intent(action);
            intent.setComponent(new ComponentName(pkg, GEELY_MIUI_SERVICE));
            intent.putExtra("setCarId", carId);
            intent.putExtra("ScanResult", scanResult);
            ComponentName result = context.startService(intent);
            DiagnosticLog.detail("Bridge", "WAKE_SERVICE_RETURN", "wake_id=" + wakeId
                    + " pkg=" + pkg + " component=MiuiService action=" + action
                    + " accepted=" + (result != null));
        } catch (Throwable t) {
            DiagnosticLog.detail("Bridge", "WAKE_SERVICE_ERROR", "wake_id=" + wakeId
                    + " pkg=" + pkg + " component=MiuiService action=" + action
                    + " " + DiagnosticText.errorSummary(t));
            Log.w(TAG, "启动 MiuiService(" + pkg + "/" + action + ") 失败: " + t.getMessage());
        }
    }

    static void sendControlFallback(Context context, int controlType) {
        if (context == null) return;
        String activePkg = KeyNotificationBridgeService.getActiveOfficialPackage();
        String pkg = OfficialBrandPolicy.isOfficialPackage(activePkg)
                ? activePkg : OfficialTarget.resolveConfiguredPackage(context);
        int dk15Cmd = toDk15Cmd(controlType);
        try {
            Intent intent = new Intent(ACTION_NOTIFICATION_CTRL);
            intent.setPackage(pkg);
            intent.putExtra(EXTRA_CMD, dk15Cmd);
            context.sendBroadcast(intent);
            Log.i(TAG, "已发送通知控制兜底广播 pkg=" + pkg + " logical=" + controlType + " dk15Cmd=" + dk15Cmd);
        } catch (Throwable t) {
            Log.w(TAG, "通知控制兜底广播失败: " + t.getMessage());
        }
        try {
            Intent rvIntent = new Intent(ACTION_REMOTE_VIEW_CLICK);
            rvIntent.setPackage(pkg);
            rvIntent.putExtra(EXTRA_CMD, dk15Cmd);
            rvIntent.putExtra("ctrlType", dk15Cmd);
            context.sendBroadcast(rvIntent);
        } catch (Throwable ignored) {
        }
    }

    static List<BluetoothDevice> getConnectedGattDevices(Context context) {
        if (context == null) return Collections.emptyList();
        Map<String, BluetoothDevice> unique = new LinkedHashMap<>();
        try {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (manager == null) return Collections.emptyList();
            appendConnected(unique, manager.getConnectedDevices(BluetoothProfile.GATT));
            appendConnected(unique, manager.getConnectedDevices(BluetoothProfile.GATT_SERVER));
            return new ArrayList<>(unique.values());
        } catch (Throwable ignored) {
            return new ArrayList<>(unique.values());
        }
    }

    private static void appendConnected(Map<String, BluetoothDevice> unique, List<BluetoothDevice> devices) {
        if (devices == null) return;
        for (BluetoothDevice device : devices) {
            if (device == null) continue;
            try {
                String address = device.getAddress();
                if (address != null && !address.isEmpty()) unique.putIfAbsent(address.toUpperCase(java.util.Locale.ROOT), device);
            } catch (SecurityException ignored) {
            }
        }
    }
}
