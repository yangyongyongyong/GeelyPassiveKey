package com.luca.geelykeyradar;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/** 主服务与通知/银河桥接共用的日志入口，观测失败不会中断车控。 */
final class DiagnosticLog {
    private static volatile DiagnosticLogStore store;
    private static String environment = "environment=not_initialized";
    private static volatile String traceRun = "none";
    private static volatile String deviceUid = "unavailable";
    private static long lastBridgeErrorAtMs;

    /** 每个进程初始化一次随机会话和本地脱敏盐，不写出偏好原文。 */
    static synchronized void initialize(Context context) {
        if (store != null || context == null) return;
        try {
            Context application = context.getApplicationContext();
            SharedPreferences settings = application.getSharedPreferences("diagnostic_log", Context.MODE_PRIVATE);
            String salt = settings.getString("local_salt", "");
            if (salt.isEmpty()) {
                salt = UUID.randomUUID().toString();
                settings.edit().putString("local_salt", salt).apply();
            }
            String session = UUID.randomUUID().toString().substring(0, 8);
            store = new DiagnosticLogStore(application.getFilesDir(), session, Process.myPid(), salt);
            deviceUid = DeviceUid.get(application);
            environment = "device_uid=" + deviceUid
                    + " app=" + packageVersion(application, application.getPackageName())
                    + " official_app=" + packageVersion(application, GeelyBridge.GEELY_PKG)
                    + " android=" + Build.VERSION.RELEASE + " sdk=" + Build.VERSION.SDK_INT
                    + " brand=" + Build.BRAND + " model=" + Build.MODEL;
            event("Lifecycle", "PROCESS_START", environment);
            recordPreviousExits(application);
        } catch (RuntimeException unavailable) {
            Log.w("PassiveDiagnostics", "日志初始化失败 " + unavailable.getClass().getSimpleName());
        }
    }

    /** 关键生命周期、请求和结果落入独立事件日志及最近十条列表。 */
    static void event(String source, String event, String fields) {
        write(true, source, event, fields);
    }

    /** 采样、计数和等待细节只进入固定容量诊断文件。 */
    static void detail(String source, String event, String fields) {
        write(false, source, event, fields);
    }

    /** 桥接异常全部进入诊断文件，关键日志每五秒至多提升一次，避免后台重试淹没现场。 */
    static void bridgeError(String source, String event, Throwable error) {
        String fields = DiagnosticText.errorSummary(error);
        detail(source, event, fields);
        long now = SystemClock.elapsedRealtime();
        synchronized (DiagnosticLog.class) {
            if (lastBridgeErrorAtMs > 0L && now - lastBridgeErrorAtMs < 5000L) return;
            lastBridgeErrorAtMs = now;
        }
        event(source, event, fields);
    }

    /** 对任何观测异常隔离，原始消息不作为失败兜底输出，防止隐私泄露。 */
    private static void write(boolean important, String source, String event, String fields) {
        DiagnosticLogStore current = store;
        if (current == null) return;
        try {
            String line = current.log(important, source, event,
                    "uid=" + deviceUid + " trace_run=" + traceRun + " " + fields,
                    System.currentTimeMillis(), SystemClock.elapsedRealtime());
            if (important) Log.i("PassiveDiagnostics", line);
            else Log.d("PassiveDiagnostics", line);
        } catch (RuntimeException unavailable) {
            Log.w("PassiveDiagnostics", "日志观测失败 " + unavailable.getClass().getSimpleName());
        }
    }

    /** 输出旧服务实例是否正常结束，并返回当前运行号。 */
    static String serviceStarted() {
        String run = UUID.randomUUID().toString().substring(0, 8);
        traceRun = run;
        DiagnosticLogStore current = store;
        if (current != null) current.serviceStarted(run, System.currentTimeMillis(), SystemClock.elapsedRealtime());
        return run;
    }

    /** 记录正常服务停止，进程共享的日志线程保留供通知服务继续写入。 */
    static void serviceStopped(String run) {
        DiagnosticLogStore current = store;
        if (current != null) current.serviceStopped(run, System.currentTimeMillis(), SystemClock.elapsedRealtime());
        if (run.equals(traceRun)) traceRun = "none";
    }

    /** 界面只显示最近十条脱敏关键事件。 */
    static String recentText() {
        DiagnosticLogStore current = store;
        if (current == null) return "暂无日志";
        // UID已在维护页单独显示；日志正文仍保留，仅避免最近日志每行重复。
        return current.recentText().replaceAll("uid=GPK-[0-9A-F-]+ ", "");
    }

    /** 返回日志队列、丢失计数和容量。 */
    static String health() {
        DiagnosticLogStore current = store;
        return current == null ? "logging=unavailable" : current.health();
    }

    /** 返回固定日志UID，供界面复制和上传元数据使用。 */
    static String deviceUid() {
        return deviceUid;
    }

    /** 返回仅供日志关联的本机短指纹。 */
    static String fingerprint(String value) {
        DiagnosticLogStore current = store;
        return current == null ? "unavailable" : current.fingerprint(value);
    }

    /** 导出包含版本和状态快照的ZIP，调用方在后台等待结果。 */
    static Future<File> export(File destination, String snapshot) {
        DiagnosticLogStore current = store;
        if (current == null) throw new IllegalStateException("日志尚未初始化");
        event("User", "EXPORT_REQUEST", "");
        return current.export(destination, "trace_run=" + traceRun + " " + environment + "\n" + snapshot,
                System.currentTimeMillis(), SystemClock.elapsedRealtime());
    }

    /** 动作交付后截取最近三分钟，写入私有动作快照目录并异步限数。 */
    static Future<File> captureActionSnapshot(String action, String snapshot) {
        DiagnosticLogStore current = store;
        if (current == null) throw new IllegalStateException("日志尚未初始化");
        return current.captureActionSnapshot(action, "trace_run=" + traceRun + " " + environment
                + "\n" + snapshot, System.currentTimeMillis(), SystemClock.elapsedRealtime());
    }

    /** 返回自动动作快照数量和占用。 */
    static String actionSnapshotHealth() {
        DiagnosticLogStore current = store;
        return current == null ? "action_snapshots=unavailable" : current.actionSnapshotHealth();
    }

    /** 手动打包现有动作快照，不改变留存数量。 */
    static Future<File> exportActionSnapshots(File destination, String snapshot) {
        DiagnosticLogStore current = store;
        if (current == null) throw new IllegalStateException("日志尚未初始化");
        return current.exportActionSnapshots(destination,
                "trace_run=" + traceRun + " " + environment + "\n" + snapshot,
                System.currentTimeMillis(), SystemClock.elapsedRealtime());
    }

    /** 清空两组日志分段，保持本地脱敏盐和生命周期标记。 */
    static void clear() {
        DiagnosticLogStore current = store;
        if (current != null) current.clear(System.currentTimeMillis(), SystemClock.elapsedRealtime());
    }

    /** 仅获取应用版本，不读取账号或官方私有存储。 */
    private static String packageVersion(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return info.versionName + "(" + info.getLongVersionCode() + ")";
        } catch (Exception unavailable) {
            return "unavailable";
        }
    }

    /** Android11及以上获取本应用最近三次退出原因，旧系统明确记录不支持。 */
    private static void recordPreviousExits(Context context) {
        if (Build.VERSION.SDK_INT < 30) {
            event("Lifecycle", "PROCESS_EXIT_HISTORY", "supported=false");
            return;
        }
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager == null) return;
            List<ApplicationExitInfo> exits = manager.getHistoricalProcessExitReasons(context.getPackageName(), 0, 3);
            if (exits.isEmpty()) event("Lifecycle", "PROCESS_EXIT_HISTORY", "records=0");
            for (ApplicationExitInfo exit : exits) {
                event("Lifecycle", "PREVIOUS_PROCESS_EXIT", "pid=" + exit.getPid()
                        + " at_ms=" + exit.getTimestamp() + " reason=" + exitReason(exit.getReason())
                        + " reason_code=" + exit.getReason() + " status=" + exit.getStatus()
                        + " importance=" + exit.getImportance() + " pss_kb=" + exit.getPss()
                        + " rss_kb=" + exit.getRss());
            }
        } catch (RuntimeException unavailable) {
            detail("Lifecycle", "PROCESS_EXIT_HISTORY_ERROR", DiagnosticText.errorSummary(unavailable));
        }
    }

    /** 退出原因同时保留数值，厂商未知原因也可继续关联。 */
    private static String exitReason(int reason) {
        switch (reason) {
            case ApplicationExitInfo.REASON_LOW_MEMORY: return "LOW_MEMORY";
            case ApplicationExitInfo.REASON_CRASH: return "CRASH";
            case ApplicationExitInfo.REASON_CRASH_NATIVE: return "CRASH_NATIVE";
            case ApplicationExitInfo.REASON_ANR: return "ANR";
            case ApplicationExitInfo.REASON_USER_REQUESTED: return "USER_REQUESTED";
            case ApplicationExitInfo.REASON_USER_STOPPED: return "USER_STOPPED";
            case ApplicationExitInfo.REASON_PERMISSION_CHANGE: return "PERMISSION_CHANGE";
            case ApplicationExitInfo.REASON_SIGNALED: return "SIGNALED";
            case ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE: return "RESOURCE_USAGE";
            default: return "OTHER_OR_UNKNOWN";
        }
    }
}
