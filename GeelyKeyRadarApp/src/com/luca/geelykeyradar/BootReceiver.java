package com.luca.geelykeyradar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 开机、应用更新后自动拉起极简无感钥匙服务。
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;
        if (!new VehicleIdentityStore(context).isServiceEnabled()) {
            return;
        }
        if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())
                && KeyNotificationBridgeService.isNotificationListenerEnabled(context)) {
            KeyNotificationBridgeService.requestImmediateRecovery(context);
        }
        ServiceWatchdogReceiver.schedule(context);
        try {
            Intent service = new Intent(context, PassiveKeyService.class);
            service.setAction(PassiveKeyService.ACTION_START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service);
            } else {
                context.startService(service);
            }
        } catch (Throwable ignored) {
        }
    }
}
