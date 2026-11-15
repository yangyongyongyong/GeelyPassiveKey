package com.luca.geelykeyradar;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * 对吉利银河官方 App 的最小桥接。
 *
 * 只使用官方 App 已经导出的组件：
 * - MiuiService / MiuiReceiver：把本应用扫到的 ScanResult 交给官方钥匙 SDK；
 * - GLDkConnectionService / DigitalKeyBgService：唤醒官方后台钥匙进程；
 * - 通知栏控制广播：作为通知按钮点击失败时的兜底。
 *
 * 不读取官方 App 私有数据，不依赖 adb/REA，也不写死 VIN/MAC。
 */
final class GeelyBridge {

    private static final String TAG = "GeelyBridge";

    static final String GEELY_PKG = "com.geelygalaxy.customer";
    static final String GEELY_DK_SERVICE = "com.geely.snc.digitalkey.base.service.DigitalKeyBgService";
    static final String GEELY_MIUI_SERVICE = "com.ecarx.blekey.thirdadapter.service.MiuiService";
    static final String GEELY_MIUI_RECEIVER = "com.ecarx.blekey.thirdadapter.receiver.MiuiReceiver";
    static final String GEELY_GLDK_SERVICE = "com.geely.lib.dk.ble.GLDkConnectionService";

    static final String ACTION_DK_START = "com.zeekr.snc.service.action.DK_START";
    static final String ACTION_PENDING_SCAN = "com.geely.pendintent.scanresult";
    static final String ACTION_MIUI_SERVICE = "com.geely.geometry.miuiservice.action";
    static final String ACTION_MIUI_CONSUMER = "com.geely.consumer.miuiservice.action";
    static final String ACTION_NOTIFICATION_CTRL = "com.snc.dk.rv.notification.ctrl";
    static final String ACTION_REMOTE_VIEW_CLICK = "com.geely.cockpit.ble.RemoteViewClick";
    static final String EXTRA_CMD = "com.extra.cmd";

    // 逻辑控制类型
    static final int CTRL_QUERY = 0;
    static final int CTRL_UNLOCK = 1;
    static final int CTRL_LOCK = 2;

    /**
     * 将逻辑控制类型转换为吉利银河 DK 1.5 (DkControlType15 / DkCmdType15) 的真实广播命令值：
     * 在 DkRemoteView.buildRemoteView 中：
     * - DkControlType15.Lock   = CMD_CLOSE_DOOR15 = 1 (上锁)
     * - DkControlType15.Unlock = CMD_OPEN_DOOR15  = 2 (解锁)
     */
    static int toDk15Cmd(int logicalCtrlType) {
        if (logicalCtrlType == CTRL_UNLOCK) {
            return 2;
        }
        if (logicalCtrlType == CTRL_LOCK) {
            return 1;
        }
        return logicalCtrlType;
    }

    private static volatile boolean keepAliveBound = false;
    private static long wakeSequence = 0L;

    private static final ServiceConnection KEEP_ALIVE_CONNECTION = new ServiceConnection() {
        /** 记录系统实际完成绑定的回调，区分调用bindService与绑定就绪。 */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_CONNECTED", "component=" + name.getClassName());
            Log.i(TAG, "官方钥匙服务保活绑定成功: " + name);
        }

        /** 官方服务绑定断开时保留生命周期事件。 */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_DISCONNECTED", "component=" + name.getClassName());
            Log.w(TAG, "官方钥匙服务保活绑定断开: " + name);
            keepAliveBound = false;
        }

        /** 官方服务绑定失效时记录原因来源，沿用原恢复策略。 */
        @Override
        public void onBindingDied(ComponentName name) {
            DiagnosticLog.event("Bridge", "KEEPALIVE_DIED", "component=" + name.getClassName());
            Log.w(TAG, "官方钥匙服务保活绑定失效: " + name);
            keepAliveBound = false;
        }
    };

    private GeelyBridge() {
    }

    static boolean isGeelyInstalled(Context context) {
        if (context == null) return false;
        try {
            context.getPackageManager().getPackageInfo(GEELY_PKG, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * 绑定官方钥匙服务做保活；重复调用是安全的。
     */
    static synchronized void bindKeepAlive(Context context) {
        DiagnosticLog.initialize(context);
        if (context == null || !isGeelyInstalled(context)) return;
        if (keepAliveBound) return;

        try {
            Intent miui = new Intent(ACTION_MIUI_SERVICE);
            miui.setComponent(new ComponentName(GEELY_PKG, GEELY_MIUI_SERVICE));
            boolean accepted = context.bindService(miui, KEEP_ALIVE_CONNECTION,
                    Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            keepAliveBound = true;
            DiagnosticLog.detail("Bridge", "KEEPALIVE_BIND_REQUEST", "component=MiuiService accepted=" + accepted);
            Log.i(TAG, "已绑定银河 MiuiService 保活");
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Bridge", "KEEPALIVE_BIND_ERROR", t);
            Log.w(TAG, "绑定 MiuiService 失败: " + t.getMessage());
        }

        try {
            Intent dk = new Intent(ACTION_DK_START);
            dk.setComponent(new ComponentName(GEELY_PKG, GEELY_DK_SERVICE));
            boolean accepted = context.bindService(dk, KEEP_ALIVE_CONNECTION,
                    Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            DiagnosticLog.detail("Bridge", "KEEPALIVE_BIND_REQUEST", "component=DigitalKeyBgService accepted=" + accepted);
        } catch (Throwable ignored) {
            // DigitalKeyBgService 在新版清单里可能是非 exported，失败不影响 MiuiService 主链路。
            DiagnosticLog.detail("Bridge", "OPTIONAL_KEEPALIVE_BIND_ERROR", DiagnosticText.errorSummary(ignored));
        }
    }

    /**
     * 把一条真实扫描到的吉利车机广播交给官方 App，让官方 App 自己去完成 V2 鉴权。
     *
     * 注意：这里绝不主动 connectGatt 占车机唯一的连接槽位。
     */
    static void wakeAndConnect(Context context, ScanResult scanResult, String carId) {
        if (context == null || scanResult == null) return;
        DiagnosticLog.initialize(context);
        long wakeId = ++wakeSequence;
        if (!isGeelyInstalled(context)) {
            DiagnosticLog.event("Bridge", "WAKE_UNAVAILABLE", "wake_id=" + wakeId + " official_app_installed=false");
            Log.w(TAG, "未安装吉利银河 App，无法触发蓝牙钥匙");
            return;
        }

        String safeCarId = (carId == null || carId.trim().isEmpty()) ? "geely-ble" : carId.trim();
        String mac = (scanResult.getDevice() != null) ? scanResult.getDevice().getAddress() : "unknown";
        DiagnosticLog.detail("Bridge", "WAKE_BEGIN", "wake_id=" + wakeId
                + " addr_id=" + DiagnosticLog.fingerprint(mac) + " rssi=" + scanResult.getRssi());

        // 当前1.56.1只保留Manifest声明的单一MiuiService入口；旧Receiver/GLDk/未导出服务均由兜底另行处理。
        sendMiuiService(context, ACTION_MIUI_SERVICE, safeCarId, scanResult, wakeId);

        bindKeepAlive(context);
    }

    /** 主MiuiService约1秒仍无ACL时，仅补发一次未在Manifest声明的兼容action。 */
    static void wakeAndConnectFallback(Context context, ScanResult scanResult, String carId) {
        if (context == null || scanResult == null || !isGeelyInstalled(context)) return;
        DiagnosticLog.initialize(context);
        long wakeId = ++wakeSequence;
        String safeCarId = (carId == null || carId.trim().isEmpty()) ? "geely-ble" : carId.trim();
        DiagnosticLog.event("Bridge", "WAKE_FALLBACK", "wake_id=" + wakeId + " action=" + ACTION_MIUI_CONSUMER);
        sendMiuiService(context, ACTION_MIUI_CONSUMER, safeCarId, scanResult, wakeId);
    }

    /** 每个唤醒入口独立记录系统是否接纳；接纳不等于官方钥匙已鉴权。 */
    private static void sendMiuiService(Context context, String action, String carId, ScanResult scanResult, long wakeId) {
        try {
            Intent intent = new Intent(action);
            intent.setComponent(new ComponentName(GEELY_PKG, GEELY_MIUI_SERVICE));
            intent.putExtra("setCarId", carId);
            intent.putExtra("ScanResult", scanResult);
            ComponentName result = context.startService(intent);
            DiagnosticLog.detail("Bridge", "WAKE_SERVICE_RETURN", "wake_id=" + wakeId
                    + " component=MiuiService action=" + action + " accepted=" + (result != null));
        } catch (Throwable t) {
            DiagnosticLog.detail("Bridge", "WAKE_SERVICE_ERROR", "wake_id=" + wakeId
                    + " component=MiuiService action=" + action + " " + DiagnosticText.errorSummary(t));
            Log.w(TAG, "启动 MiuiService(" + action + ") 失败: " + t.getMessage());
        }
    }

    /**
     * 通知按钮点击失败时的兜底广播。不是主路径，避免重复下发。
     */
    static void sendControlFallback(Context context, int controlType) {
        if (context == null) return;
        int dk15Cmd = toDk15Cmd(controlType);
        try {
            Intent intent = new Intent(ACTION_NOTIFICATION_CTRL);
            intent.setPackage(GEELY_PKG);
            intent.putExtra(EXTRA_CMD, dk15Cmd);
            context.sendBroadcast(intent);
            Log.i(TAG, "已发送通知控制兜底广播 logical=" + controlType + " dk15Cmd=" + dk15Cmd);
        } catch (Throwable t) {
            Log.w(TAG, "通知控制兜底广播失败: " + t.getMessage());
        }
        try {
            Intent rvIntent = new Intent(ACTION_REMOTE_VIEW_CLICK);
            rvIntent.setPackage(GEELY_PKG);
            rvIntent.putExtra(EXTRA_CMD, dk15Cmd);
            rvIntent.putExtra("ctrlType", dk15Cmd);
            context.sendBroadcast(rvIntent);
        } catch (Throwable ignored) {
        }
    }

    /**
     * 尝试从系统 GATT 连接列表里找出银河 App 正在连接的车机。
     *
     * 某些 ROM 能返回官方 App 的 GATT 客户端；返回空也不影响广播学习路径。
     */
    static List<BluetoothDevice> getConnectedGattDevices(Context context) {
        if (context == null) return Collections.emptyList();
        try {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (manager == null) return Collections.emptyList();
            List<BluetoothDevice> devices = manager.getConnectedDevices(BluetoothProfile.GATT);
            return devices == null ? Collections.<BluetoothDevice>emptyList() : devices;
        } catch (Throwable ignored) {
            return Collections.emptyList();
        }
    }
}
