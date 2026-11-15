package com.luca.geelykeyradar;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;

/** 把 Android 当前权限、监听和保活事实读成 PermissionAudit.Input。 */
final class PermissionStatus {
    static final String PREFS = "permission_guide_prefs";
    static final String KEY_VENDOR_BACKGROUND_CONFIRMED = "vendor_background_confirmed";
    static final String KEY_ACTIVE_STEP = "active_step";
    static final String KEY_SKIPPED_STEPS = "skipped_steps";
    static final String KEY_NOTIFICATION_REQUESTED = "notification_requested";

    private PermissionStatus() {
    }

    static PermissionAudit.Input read(Context context, PassiveKeyService.Status status) {
        PermissionAudit.Input input = new PermissionAudit.Input();
        if (context == null) return input;
        Context app = context.getApplicationContext();
        input.sdkInt = Build.VERSION.SDK_INT;
        input.bluetoothPermission = hasBluetoothPermissions(app);
        BluetoothManager manager = (BluetoothManager) app.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = manager != null ? manager.getAdapter() : BluetoothAdapter.getDefaultAdapter();
        input.bluetoothOn = adapter != null && adapter.isEnabled();
        input.notificationRuntimePermission = hasNotificationRuntimePermission(app);
        input.listenerGranted = KeyNotificationBridgeService.isNotificationListenerEnabled(app);
        input.listenerConnected = KeyNotificationBridgeService.isListenerConnected();
        input.listenerDownMs = KeyNotificationBridgeService.getListenerDownDurationMs();
        input.geelyInstalled = GeelyBridge.isGeelyInstalled(app);

        VehicleIdentityStore store = new VehicleIdentityStore(app);
        input.serviceEnabled = status != null ? status.serviceEnabled : store.isServiceEnabled();
        input.serviceRunning = PassiveKeyService.isRunning();
        input.batteryWhitelisted = isBatteryWhitelisted(app);
        SharedPreferences prefs = app.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        input.vendorBackgroundConfirmed = prefs.getBoolean(KEY_VENDOR_BACKGROUND_CONFIRMED, false);
        return input;
    }

    static boolean hasBluetoothPermissions(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    static boolean hasNotificationRuntimePermission(Context context) {
        return context == null || Build.VERSION.SDK_INT < 33
                || context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    static boolean isBatteryWhitelisted(Context context) {
        if (context == null) return false;
        try {
            PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return manager != null && manager.isIgnoringBatteryOptimizations(context.getPackageName());
        } catch (Throwable ignored) {
            return false;
        }
    }

    static boolean vendorBackgroundConfirmed(Context context) {
        return context != null && context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY_VENDOR_BACKGROUND_CONFIRMED, false);
    }

    static void setVendorBackgroundConfirmed(Context context, boolean confirmed) {
        if (context == null) return;
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_VENDOR_BACKGROUND_CONFIRMED, confirmed).apply();
    }
}
