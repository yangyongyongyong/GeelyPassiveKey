package com.luca.geelykeyradar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

/**
 * 前台服务看门狗：服务被系统回收后，无需用户打开界面也能尝试恢复扫描。
 */
public final class ServiceWatchdogReceiver extends BroadcastReceiver {
    private static final String ACTION_WATCHDOG = "com.luca.geelykeyradar.action.SERVICE_WATCHDOG";
    private static final int REQUEST_CODE = 0x5757;
    private static final long INTERVAL_MS = 2 * 60 * 1000L;

    /** 安排下一次检查；重复调用会覆盖旧闹钟。 */
    static void schedule(Context context) {
        if (context == null) return;
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager == null) return;
            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + INTERVAL_MS,
                    pendingIntent(context)
            );
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Lifecycle", "WATCHDOG_SCHEDULE_ERROR", error);
        }
    }

    /** 用户关闭无感进出总开关时停止看门狗，避免无意义唤醒。 */
    static void cancel(Context context) {
        if (context == null) return;
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) alarmManager.cancel(pendingIntent(context));
        } catch (Throwable ignored) {
        }
    }

    /** 显式广播，不依赖外部应用或动态注册。 */
    private static PendingIntent pendingIntent(Context context) {
        Intent intent = new Intent(context, ServiceWatchdogReceiver.class);
        intent.setAction(ACTION_WATCHDOG);
        return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) return;
        DiagnosticLog.initialize(context);
        if (!new VehicleIdentityStore(context).isServiceEnabled()) {
            DiagnosticLog.event("Lifecycle", "WATCHDOG_DISABLED", "service_enabled=false");
            return;
        }
        schedule(context);
        if (PassiveKeyService.isRunning()) {
            DiagnosticLog.detail("Lifecycle", "WATCHDOG_ALIVE", "service_running=true");
            return;
        }
        DiagnosticLog.event("Lifecycle", "WATCHDOG_RESTART", "service_running=false");
        try {
            Intent service = new Intent(context, PassiveKeyService.class);
            service.setAction(PassiveKeyService.ACTION_START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service);
            } else {
                context.startService(service);
            }
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Lifecycle", "WATCHDOG_START_ERROR", error);
        }
    }
}
