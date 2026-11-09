package com.luca.geelykeyradar;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 吉利银河通知栏桥接。
 *
 * 极简版只保留两件事：
 * 1. 读取官方通知栏的“已连接/未连接”状态，用于判断蓝牙钥匙是否在线；
 * 2. 直接触发通知栏里官方的“解锁/上锁”按钮，等价于用户手动点击，不依赖车机被动 PE 开关。
 *
 * 旧版注释中关于 DK1.5 指令反转、RemoteViews.apply 兼容 Android 14-16 的逆向结论仍然有效。
 */
public class KeyNotificationBridgeService extends NotificationListenerService {

    /** 区分已交付、明确未发送和结果未知，未知时不得自动重试。 */
    enum ControlResult {
        SENT,
        NOT_SENT,
        UNKNOWN
    }

    private static final String TAG = "KeyNotifBridge";
    public static final String GEELY_PKG = "com.geelygalaxy.customer";
    public static final int GEELY_DK_NOTIFY_ID = 5;

    // Resource IDs from com.geely.snc.digitalkey.base.R$id (verified via dexdump of DkRemoteView.buildRemoteView @ 0x5011e8)
    public static final int ID_NOTIF_LAYOUT_LOCK_UNLOCK = 0x7f0a10c6;
    public static final int ID_NOTIF_TV_LOCK            = 0x7f0a10c7;
    public static final int ID_NOTIF_TV_STATUS_CONN     = 0x7f0a10c8;
    public static final int ID_NOTIF_TV_STATUS_DISCONN  = 0x7f0a10c9;
    public static final int ID_NOTIF_TV_STATUS_NO       = 0x7f0a10ca;
    public static final int ID_NOTIF_TV_UNLOCK          = 0x7f0a10cb;

    private static volatile KeyNotificationBridgeService instance;
    private static volatile boolean geelyBleConnected = false;
    private static volatile boolean geelyNotificationPresent = false;
    private static volatile PendingIntent cachedLockPi = null;
    private static volatile PendingIntent cachedUnlockPi = null;
    private static volatile PendingIntent cachedContentPi = null;
    private static volatile View cachedLockView = null;
    private static volatile View cachedUnlockView = null;
    private static volatile long lastUpdateTimestamp = 0L;
    private static volatile String lastObservedParkStatus = "";
    private static volatile long lastObservedParkStatusAtMs = 0L;
    private static volatile String lastObservedLockHint = "";
    private static volatile String lastButtonCaptureDiag = "等待解析吉利银河通知栏...";
    private static volatile Boolean textConnectionHint = null;
    private static volatile String lastControlDiagnostic = "transport=NONE result=NONE";
    private static volatile String lastParseDiagnostic = "NO_NOTIFICATION";
    private static final long REBIND_REQUEST_INTERVAL_MS = 2000L;
    private static final long MISSING_NOTIFICATION_RESCAN_MS = 1500L;
    private static long listenerDownSinceMs = 0L;
    private static long lastRebindRequestAtMs = 0L;
    private static long lastMissingRescanAtMs = 0L;
    private static volatile String lastRecoveryDiagnostic = "listener_recovery=waiting";

    // 官方通知从未连接切换到已连接时通知主服务；待发动作统一由主服务管理，避免两套队列重复点击。
    private static volatile Runnable stateListener;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final NotificationEpoch notificationEpoch = new NotificationEpoch();
    private final DiagnosticCounters diagnosticCounters = new DiagnosticCounters();
    private long lastPostedAtMs;
    private long lastSeenAtMs;
    private long lastParsedAtMs;
    private long lastNotificationLogAtMs;
    private volatile boolean listenerConnected = false;
    private Context geelyPkgContext = null;

    public static KeyNotificationBridgeService getInstance() {
        return instance;
    }

    /** 区分实例仍存活与系统通知监听已真正绑定，支持失联后的周期性重绑。 */
    public static boolean isListenerConnected() {
        KeyNotificationBridgeService service = instance;
        return service != null && service.listenerConnected;
    }

    public static String getLastObservedParkStatus() {
        return lastObservedParkStatus;
    }

    public static long getLastObservedParkStatusAtMs() {
        return lastObservedParkStatusAtMs;
    }

    public static String getLastObservedLockHint() {
        return lastObservedLockHint;
    }

    public static String getLastButtonCaptureDiag() {
        return lastButtonCaptureDiag;
    }

    /** 主服务在SEND_RESULT中加入实际发送通道和异常类别，与同一req_id对应。 */
    public static String getLastControlDiagnostic() {
        return lastControlDiagnostic;
    }

    /** 返回监听恢复状态，便于界面与导出日志区分“未授权”和“系统掉绑”。 */
    public static String getLastRecoveryDiagnostic() {
        return lastRecoveryDiagnostic;
    }

    /** 分离通知到达、解析和就绪时间，listener活着不代表官方持续更新。 */
    public static String diagnosticSnapshot(long nowMs) {
        KeyNotificationBridgeService service = instance;
        return "listener_connected=" + isListenerConnected() + " notification_present=" + geelyNotificationPresent
                + " official_connected=" + geelyBleConnected
                + " posted_callback_age_ms=" + (service == null || service.lastPostedAtMs == 0L ? -1L : nowMs - service.lastPostedAtMs)
                + " notification_seen_age_ms=" + (service == null || service.lastSeenAtMs == 0L ? -1L : nowMs - service.lastSeenAtMs)
                + " notification_parse_age_ms=" + (service == null || service.lastParsedAtMs == 0L ? -1L : nowMs - service.lastParsedAtMs)
                + " notification_parse=" + lastParseDiagnostic
                + " unlock_button=" + (cachedUnlockPi != null ? "PI" : cachedUnlockView != null ? "VIEW" : "NONE")
                + " lock_button=" + (cachedLockPi != null ? "PI" : cachedLockView != null ? "VIEW" : "NONE")
                + " park=" + lastObservedParkStatus
                + " park_age_ms=" + (lastObservedParkStatusAtMs == 0L ? -1L : nowMs - lastObservedParkStatusAtMs)
                + " " + lastRecoveryDiagnostic
                + (service == null ? " epoch=unavailable" : " " + service.notificationEpoch.diagnosticSnapshot()
                + " " + service.diagnosticCounters.snapshot(false));
    }

    public static boolean isNotificationListenerEnabled(Context context) {
        if (context == null) return false;
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (flat == null || flat.isEmpty()) return false;
        ComponentName cn = new ComponentName(context, KeyNotificationBridgeService.class);
        return flat.contains(cn.flattenToString()) || flat.contains(cn.flattenToShortString());
    }

    /**
     * Ensures Android's NotificationManagerService actively binds KeyNotificationBridgeService
     * (recovering from any prior force-stop or package reinstall) and immediately scans Geely's notification.
     */
    public static void ensureRebound(Context context) {
        if (context == null) return;
        DiagnosticLog.initialize(context);
        try {
            KeyNotificationBridgeService cur = instance;
            if (cur != null && cur.listenerConnected) {
                cur.scanExistingNotifications();
                return;
            }
            if (!isNotificationListenerEnabled(context)) {
                return;
            }
            requestRebindThrottled(new ComponentName(context, KeyNotificationBridgeService.class),
                    "ensure_rebound");
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Notification", "LISTENER_REBIND_ERROR", t);
            Log.w(TAG, "ensureRebound warning: " + t.getMessage());
        }
    }

    /**
     * 周期性自愈：持续请求系统重绑；部分 ROM 会拒绝无图形界面的重绑，
     * 此时明确标记需要用户到系统通知使用权页重新开关，已连接时补扫现有通知。
     */
    public static void recoverIfNeeded(Context context) {
        if (context == null) return;
        if (!isNotificationListenerEnabled(context)) {
            listenerDownSinceMs = 0L;
            lastRecoveryDiagnostic = "listener_recovery=access_disabled";
            return;
        }
        long now = SystemClock.elapsedRealtime();
        if (isListenerConnected()) {
            listenerDownSinceMs = 0L;
            lastRecoveryDiagnostic = "listener_recovery=connected";
            rescanIfMissing(now);
            return;
        }
        if (listenerDownSinceMs == 0L) {
            listenerDownSinceMs = now;
            DiagnosticLog.event("Notification", "LISTENER_DOWN", "recovery_started=true");
        }
        long downMs = Math.max(0L, now - listenerDownSinceMs);
        if (downMs >= 10000L) {
            lastRecoveryDiagnostic = "listener_recovery=needs_user_access_toggle";
        }
        ComponentName cn = new ComponentName(context, KeyNotificationBridgeService.class);
        requestRebindThrottled(cn, "down_ms=" + downMs);
    }

    /** 手动测试发现监听掉绑时立即再请求一次重绑，随后由界面引导系统授权页修复。 */
    public static void requestImmediateRecovery(Context context) {
        if (context == null || !isNotificationListenerEnabled(context)) return;
        lastRebindRequestAtMs = 0L;
        requestRebindThrottled(new ComponentName(context, KeyNotificationBridgeService.class),
                "manual_request");
    }

    /** 普通重绑请求按两秒节流，避免每五百毫秒向系统重复请求。 */
    private static void requestRebindThrottled(ComponentName componentName, String reason) {
        long now = SystemClock.elapsedRealtime();
        if ((now - lastRebindRequestAtMs) < REBIND_REQUEST_INTERVAL_MS) return;
        lastRebindRequestAtMs = now;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DiagnosticLog.event("Notification", "LISTENER_REBIND_REQUEST", "reason=" + reason);
                NotificationListenerService.requestRebind(componentName);
            }
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Notification", "LISTENER_REBIND_REQUEST_ERROR", t);
        }
    }

    /** 监听已连接但尚未读到银河通知时周期重扫，覆盖系统刚绑定时的空列表竞态。 */
    private static void rescanIfMissing(long nowMs) {
        KeyNotificationBridgeService service = instance;
        if (service == null || !service.listenerConnected || geelyNotificationPresent) return;
        if ((nowMs - lastMissingRescanAtMs) < MISSING_NOTIFICATION_RESCAN_MS) return;
        lastMissingRescanAtMs = nowMs;
        service.scanExistingNotifications();
    }

    public static boolean isGeelyBleConnected() {
        return geelyBleConnected;
    }

    public static boolean isGeelyNotificationPresent() {
        return geelyNotificationPresent;
    }

    public static boolean hasControlPendingIntents() {
        return (cachedUnlockPi != null || cachedUnlockView != null)
                && (cachedLockPi != null || cachedLockView != null);
    }

    /** 只要求当前动作对应的按钮可用，另一个按钮不可用不阻塞本次请求。 */
    static boolean hasControlIntent(PassiveKeyLogic.Action action) {
        if (!geelyBleConnected || !geelyNotificationPresent) return false;
        if (action == PassiveKeyLogic.Action.UNLOCK) return cachedUnlockPi != null || cachedUnlockView != null;
        if (action == PassiveKeyLogic.Action.LOCK) return cachedLockPi != null || cachedLockView != null;
        return false;
    }

    public static long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    /** 注册官方状态就绪回调；回调只负责唤醒主服务的唯一待发队列。 */
    public static void setStateListener(Runnable listener) {
        stateListener = listener;
        KeyNotificationBridgeService service = instance;
        if (service != null) service.notifyStateListener();
    }

    /** 服务销毁时解除自己的回调，不影响随后创建的新服务。 */
    public static void clearStateListener(Runnable listener) {
        if (stateListener != listener) return;
        stateListener = null;
        KeyNotificationBridgeService service = instance;
        if (service != null) service.mainHandler.removeCallbacks(listener);
    }

    /** 将状态通知合并到主线程，避免通知解析过程中递归发送。 */
    private void notifyStateListener() {
        Runnable listener = stateListener;
        if (listener == null) return;
        mainHandler.removeCallbacks(listener);
        mainHandler.post(listener);
    }

    /**
     * 检查总开关、插线与行驶保护；手动测试只跳过对应自动动作分开关。
     */
    private static boolean isControlBlocked(boolean unlock, boolean manual) {
        KeyNotificationBridgeService svc = instance;
        if (svc == null || !svc.listenerConnected || Looper.myLooper() != Looper.getMainLooper()
                || !geelyBleConnected || !geelyNotificationPresent) return true;
        try {
            VehicleIdentityStore store = new VehicleIdentityStore(svc);
            if (!store.isServiceEnabled()
                    || (!manual && (unlock ? !store.isAutoUnlockEnabled() : !store.isAutoLockEnabled()))) {
                return true;
            }
            if ("行驶中".equals(lastObservedParkStatus)
                    && SystemClock.elapsedRealtime() - lastObservedParkStatusAtMs < 90000L) return true;
            if (!store.isBlockWhenChargingEnabled()) {
                return false;
            }
            Intent sticky = svc.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (sticky == null) return true;
            int plugged = sticky.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            return (plugged & (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB)) != 0;
        } catch (Throwable ignored) {
            return true;
        }
    }

    /**
     * 点击银河通知的“解锁”按钮（id=5, notif_tv_unlock=0x7f0a10cb）。
     * 旧版在这里挂起并在连接后自动发送；现在只返回本次发送结果，等待与取消由主服务统一处理。
     */
    public static ControlResult triggerUnlockViaNotification() {
        if (isControlBlocked(true, false)) return controlResult(ControlResult.NOT_SENT, "NONE", "BLOCKED", null);
        return fireNotificationButton(cachedUnlockPi, cachedUnlockView, "解锁");
    }

    /**
     * 点击银河通知的“上锁”按钮（id=5, notif_tv_lock=0x7f0a10c7），不创建第二套待发队列。
     */
    public static ControlResult triggerLockViaNotification() {
        if (isControlBlocked(false, false)) return controlResult(ControlResult.NOT_SENT, "NONE", "BLOCKED", null);
        return fireNotificationButton(cachedLockPi, cachedLockView, "上锁");
    }

    /** 手动测试复用同一个通知按钮发送通道，保留禁控检查且不创建延迟或重发任务。 */
    static ControlResult triggerManualControlViaNotification(PassiveKeyLogic.Action action) {
        if (action != PassiveKeyLogic.Action.UNLOCK && action != PassiveKeyLogic.Action.LOCK) {
            return controlResult(ControlResult.NOT_SENT, "NONE", "INVALID_MANUAL_ACTION", null);
        }
        boolean unlock = action == PassiveKeyLogic.Action.UNLOCK;
        if (isControlBlocked(unlock, true)) {
            return controlResult(ControlResult.NOT_SENT, "NONE", "MANUAL_BLOCKED", null);
        }
        return unlock ? fireNotificationButton(cachedUnlockPi, cachedUnlockView, "手动解锁")
                : fireNotificationButton(cachedLockPi, cachedLockView, "手动上锁");
    }

    /** 一次尝试只选 PI 或 View；出现不确定异常时停止，不冒险重复执行车控。 */
    private static ControlResult fireNotificationButton(final PendingIntent pi, final View buttonView, final String actionLabel) {
        // 1. Primary: Send the extracted PendingIntent directly (with Android 14-16 background activity/broadcast start options)
        if (pi != null) {
            try {
                if (Build.VERSION.SDK_INT >= 34) {
                    ActivityOptions opts = ActivityOptions.makeBasic();
                    opts.setPendingIntentBackgroundActivityStartMode(
                            ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED);
                    pi.send(null, 0, null, null, null, null, opts.toBundle());
                } else {
                    pi.send();
                }
                Log.i(TAG, "✓ 已通过通知栏 PendingIntent.send() 触发银河【" + actionLabel + "】按钮");
                return controlResult(ControlResult.SENT, "PI", actionLabel, null);
            } catch (PendingIntent.CanceledException cancelled) {
                Log.w(TAG, "通知 PendingIntent 已失效，等待新按钮: " + actionLabel);
                return controlResult(ControlResult.NOT_SENT, "PI", actionLabel, cancelled);
            } catch (Throwable uncertain) {
                Log.w(TAG, "通知发送结果未知，停止自动重试: " + actionLabel, uncertain);
                return controlResult(ControlResult.UNKNOWN, "PI", actionLabel, uncertain);
            }
        }

        // 2. Secondary fallback: 无法提取 PendingIntent 时才在主线程调用 RemoteViews 按钮。
        if (buttonView != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                try {
                    boolean clicked = buttonView.performClick();
                    Log.i(TAG, "✓ 已在主线程调用通知栏按钮 View.performClick() 触发银河【" + actionLabel + "】 -> " + clicked);
                    return controlResult(clicked ? ControlResult.SENT : ControlResult.NOT_SENT, "VIEW", actionLabel, null);
                } catch (Throwable uncertain) {
                    Log.w(TAG, "通知按钮执行结果未知，停止自动重试", uncertain);
                    return controlResult(ControlResult.UNKNOWN, "VIEW", actionLabel, uncertain);
                }
            }
        }
        return controlResult(ControlResult.NOT_SENT, "NONE", actionLabel, null);
    }

    /** 记录本次实际返回结果，不额外发指令、不把异常正文写入文件。 */
    private static ControlResult controlResult(ControlResult result, String transport, String action, Throwable error) {
        lastControlDiagnostic = "transport=" + transport + " bridge_result=" + result + " bridge_action=" + action
                + (error == null ? "" : " " + DiagnosticText.errorSummary(error));
        DiagnosticLog.detail("Notification", "BUTTON_RESULT", lastControlDiagnostic);
        return result;
    }

    public static boolean triggerContentIntentWakeup() {
        PendingIntent pi = cachedContentPi;
        if (pi != null) {
            try {
                pi.send();
                return true;
            } catch (Throwable ignored) {
            }
        }
        return false;
    }

    /** 新实例先清除上个监听实例的按钮，待系统正式绑定后重新捕获。 */
    @Override
    public void onCreate() {
        super.onCreate();
        DiagnosticLog.initialize(this);
        instance = this;
        clearOfficialNotification();
        Log.i(TAG, "KeyNotificationBridgeService onCreate");
        DiagnosticLog.event("Notification", "LISTENER_CREATE", "");
    }

    /** 通知监听实例销毁后不保留上次连接的可点击按钮。 */
    @Override
    public void onDestroy() {
        DiagnosticLog.event("Notification", "LISTENER_DESTROY", "current_instance=" + (instance == this));
        notificationEpoch.invalidate();
        listenerConnected = false;
        if (instance == this) {
            clearOfficialNotification();
            instance = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    /** 只允许当前监听实例接受通知，重绑不复用旧实例排队的解析。 */
    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        if (instance != this) return;
        listenerConnected = true;
        listenerDownSinceMs = 0L;
        lastRecoveryDiagnostic = "listener_recovery=connected";
        DiagnosticLog.event("Notification", "LISTENER_CONNECTED", diagnosticSnapshot(SystemClock.elapsedRealtime()));
        notificationEpoch.invalidate();
        Log.i(TAG, "KeyNotificationBridgeService onListenerConnected -> scanning active notifications");
        ensureRadarServiceRunning();
        scanExistingNotifications();
        mainHandler.postDelayed(this::scanExistingNotifications, 800L);
    }

    /** 监听失联时立即清理就绪状态，并请求系统重新绑定。 */
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        if (instance != this) return;
        listenerConnected = false;
        listenerDownSinceMs = SystemClock.elapsedRealtime();
        lastRecoveryDiagnostic = "listener_recovery=disconnected";
        DiagnosticLog.event("Notification", "LISTENER_DISCONNECTED", diagnosticSnapshot(SystemClock.elapsedRealtime()));
        notificationEpoch.invalidate();
        Log.w(TAG, "KeyNotificationBridgeService onListenerDisconnected -> requesting rebind");
        clearOfficialNotification();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                requestRebind(new ComponentName(this, KeyNotificationBridgeService.class));
            }
        } catch (Throwable ignored) {
        }
    }

    /** 只解析官方钥匙通知，普通业务通知不触发车控或重新拉起主服务。 */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (instance != this || !listenerConnected) return;
        if (sbn != null && GEELY_PKG.equals(sbn.getPackageName()) && sbn.getId() == GEELY_DK_NOTIFY_ID) {
            diagnosticCounters.increment("notification_posted");
            lastPostedAtMs = SystemClock.elapsedRealtime();
            lastSeenAtMs = lastPostedAtMs;
            ensureRadarServiceRunning();
            parseGeelyNotification(sbn);
        }
    }

    private void ensureRadarServiceRunning() {
        if (!new VehicleIdentityStore(this).isServiceEnabled()) {
            return;
        }
        if (!PassiveKeyService.isRunning()) {
            try {
                Intent svc = new Intent(this, PassiveKeyService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(svc);
                } else {
                    startService(svc);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    /** 聚合通知移除不代表钥匙断连，只处理真正的钥匙通知。 */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (instance != this || !listenerConnected) return;
        if (sbn != null && GEELY_PKG.equals(sbn.getPackageName()) && sbn.getId() == GEELY_DK_NOTIFY_ID) {
            diagnosticCounters.increment("notification_removed");
            long token = notificationEpoch.remove(sbn.getKey(), sbn.getPostTime());
            if (token < 0L) {
                diagnosticCounters.increment("notification_old_remove_ignored");
                return;
            }
            DiagnosticLog.event("Notification", "REMOVED", "post_ms=" + sbn.getPostTime()
                    + " " + notificationEpoch.diagnosticSnapshot());
            Runnable clear = () -> {
                if (instance != this || !notificationEpoch.isCurrent(token)) return;
                clearOfficialNotification();
                scanExistingNotifications();
            };
            if (Looper.myLooper() == Looper.getMainLooper()) clear.run();
            else mainHandler.post(clear);
        }
    }

    /** 重新绑定后读取当前钥匙通知，通知不存在时清空旧状态。 */
    public void scanExistingNotifications() {
        if (instance != this || !listenerConnected) return;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mainHandler.post(this::scanExistingNotifications);
            return;
        }
        try {
            StatusBarNotification[] active = getActiveNotifications();
            if (active == null) return;
            boolean found = false;
            for (StatusBarNotification sbn : active) {
                if (sbn != null && GEELY_PKG.equals(sbn.getPackageName()) && sbn.getId() == GEELY_DK_NOTIFY_ID) {
                    found = true;
                    lastSeenAtMs = SystemClock.elapsedRealtime();
                    parseGeelyNotification(sbn);
                }
            }
            if (!found) {
                notificationEpoch.removeCurrent();
                clearOfficialNotification();
            }
            diagnosticCounters.increment(found ? "notification_rescan_found" : "notification_rescan_empty");
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Notification", "RESCAN_ERROR", t);
            Log.w(TAG, "scanExistingNotifications error: " + t.getMessage());
        }
    }

    /** 清除失效的官方状态，通知主服务停止等待旧按钮。 */
    private void clearOfficialNotification() {
        if (instance != this) return;
        notificationEpoch.cancelPending();
        boolean changed = geelyNotificationPresent || geelyBleConnected || hasControlPendingIntents();
        geelyNotificationPresent = false;
        geelyBleConnected = false;
        textConnectionHint = null;
        cachedUnlockPi = null;
        cachedLockPi = null;
        cachedUnlockView = null;
        cachedLockView = null;
        cachedContentPi = null;
        lastButtonCaptureDiag = "官方钥匙通知未就绪";
        lastParseDiagnostic = "CLEARED";
        if (changed) DiagnosticLog.event("Notification", "STATE_CLEARED", diagnosticSnapshot(SystemClock.elapsedRealtime()));
        if (changed) notifyStateListener();
    }

    /** 标记通知版本并将整份快照串行提交，旧的后台解析不能复活已移除按钮。 */
    private void parseGeelyNotification(final StatusBarNotification sbn) {
        if (instance != this || !listenerConnected) return;
        long token = notificationEpoch.begin(sbn.getKey(), sbn.getPostTime());
        if (token < 0L) {
            diagnosticCounters.increment("notification_old_post_ignored");
            return;
        }
        Runnable parse = () -> {
            if (instance != this || !listenerConnected || !notificationEpoch.isCurrent(token)) {
                diagnosticCounters.increment("notification_old_parse_ignored");
                return;
            }
            applyGeelyNotification(sbn);
        };
        if (Looper.myLooper() == Looper.getMainLooper()) parse.run();
        else mainHandler.post(parse);
    }

    /** 在主线程内一次性解析官方通知，发布状态前不暴露半更新的全局字段。 */
    @SuppressWarnings("deprecation")
    private void applyGeelyNotification(final StatusBarNotification sbn) {
        final Notification notif = sbn.getNotification();
        if (notif == null) return;

        // Ignore empty group summary notifications (e.g. id=0 with FLAG_GROUP_SUMMARY and no RemoteViews)
        boolean isGroupSummary = (notif.flags & Notification.FLAG_GROUP_SUMMARY) != 0;
        if (isGroupSummary && notif.contentView == null && notif.bigContentView == null) {
            return;
        }
        if (sbn.getId() != GEELY_DK_NOTIFY_ID && notif.contentView == null && notif.bigContentView == null) {
            return;
        }

        geelyNotificationPresent = true;
        lastParseDiagnostic = "PARSING";
        lastUpdateTimestamp = SystemClock.elapsedRealtime();
        textConnectionHint = null;
        if (notif.contentIntent != null) {
            cachedContentPi = notif.contentIntent;
        }

        // Inspect standard notification extras for vehicle state keywords ("已驻车", "行驶中", "充电中", etc.)
        if (notif.extras != null) {
            for (String key : new String[]{
                    Notification.EXTRA_TITLE,
                    Notification.EXTRA_TEXT,
                    Notification.EXTRA_SUB_TEXT,
                    Notification.EXTRA_BIG_TEXT}) {
                CharSequence cs = notif.extras.getCharSequence(key);
                if (cs != null) {
                    inspectTextForVehicleStatus(cs.toString());
                }
            }
        }

        final RemoteViews rv = notif.bigContentView != null ? notif.bigContentView : notif.contentView;
        if (rv == null) {
            diagnosticCounters.increment("notification_no_remoteviews");
            DiagnosticLog.event("Notification", "NO_REMOTEVIEWS", "post_ms=" + sbn.getPostTime());
            clearOfficialNotification();
            return;
        }

        // 整份通知已在主线程串行校验，RemoteViews.apply 与按钮点击不会交叉更新。
        inflateAndExtractFromRemoteViews(rv);
    }

    private Context getOrCreateGeelyContext() {
        if (geelyPkgContext == null) {
            try {
                geelyPkgContext = createPackageContext(GEELY_PKG, Context.CONTEXT_IGNORE_SECURITY);
            } catch (Throwable ignored) {
            }
        }
        return geelyPkgContext != null ? geelyPkgContext : this;
    }

    /**
     * Inflates Geely Galaxy's RemoteViews (_aaa_ge_dk_notification_view = 0x7f0d0003) via public
     * RemoteViews.apply(context, parent) and extracts:
     * 1. Connection status (notif_tv_status_conn 0x7f0a10c8 / notif_layout_lock_unlock 0x7f0a10c6 vs notif_tv_status_disconn 0x7f0a10c9)
     * 2. The inflated '解锁' (0x7f0a10cb) and '上锁' (0x7f0a10c7) button Views + their attached PendingIntents
     * 仅采用本轮可见状态，状态改变时通知主服务，不在这里自动执行车控。
     */
    private void inflateAndExtractFromRemoteViews(RemoteViews rv) {
        String previousDiag = lastButtonCaptureDiag;
        long parseStarted = SystemClock.elapsedRealtime();
        String parsePath = "PUBLIC";
        Boolean connVisible = null;
        boolean foundUnlockBtn = false;
        boolean foundLockBtn = false;

        // Path 1: Official public RemoteViews.apply() inflation (Works on Android 14/15/16 without hidden API restrictions!)
        try {
            Context inflateCtx = getOrCreateGeelyContext();
            FrameLayout container = new FrameLayout(inflateCtx);
            View root = null;
            try {
                root = rv.apply(inflateCtx, container);
            } catch (Throwable t1) {
                root = rv.apply(this, new FrameLayout(this));
            }

            if (root != null) {
                View connView = root.findViewById(ID_NOTIF_TV_STATUS_CONN);
                View lockUnlockLayout = root.findViewById(ID_NOTIF_LAYOUT_LOCK_UNLOCK);
                View disconnView = root.findViewById(ID_NOTIF_TV_STATUS_DISCONN);

                if (isVisibleInTree(connView)) {
                    connVisible = true;
                } else if (isVisibleInTree(lockUnlockLayout)) {
                    connVisible = true;
                } else if (isVisibleInTree(disconnView)) {
                    connVisible = false;
                }

                View unlockView = root.findViewById(ID_NOTIF_TV_UNLOCK);
                View lockView = root.findViewById(ID_NOTIF_TV_LOCK);

                // Fallback: traverse tree by text ("解锁" / "上锁" / "已连接" / "未连接") in case resource IDs shift across Geely versions
                View[] discoveredByText = new View[2]; // [0]=unlock, [1]=lock
                Boolean[] connByText = new Boolean[1];
                traverseInflatedTree(root, discoveredByText, connByText);

                if (unlockView == null) unlockView = discoveredByText[0];
                if (lockView == null) lockView = discoveredByText[1];
                if (connVisible == null && connByText[0] != null) {
                    connVisible = connByText[0];
                }

                // Extract PendingIntent or OnClickListener from unlockView & lockView
                // IMPORTANT: DkRemoteView.buildRemoteView only calls setOnClickPendingIntent when isConnect == true!
                // When isConnect == false, unlockView.hasOnClickListeners() is false, so we MUST NOT overwrite
                // a previously captured cachedUnlockPi / cachedUnlockView with an unclickable view!
                if (unlockView != null) {
                    PendingIntent pi = extractPendingIntentFromInflatedView(unlockView);
                    if (pi != null) {
                        cachedUnlockPi = pi;
                        foundUnlockBtn = true;
                    }
                    if (unlockView.hasOnClickListeners() || pi != null) {
                        cachedUnlockView = unlockView;
                        foundUnlockBtn = true;
                    }
                }

                if (lockView != null) {
                    PendingIntent pi = extractPendingIntentFromInflatedView(lockView);
                    if (pi != null) {
                        cachedLockPi = pi;
                        foundLockBtn = true;
                    }
                    if (lockView.hasOnClickListeners() || pi != null) {
                        cachedLockView = lockView;
                        foundLockBtn = true;
                    }
                }
            }
        } catch (Throwable t) {
            parsePath = "PUBLIC_FAILED_" + t.getClass().getSimpleName();
            diagnosticCounters.increment("notification_public_parse_error");
            DiagnosticLog.bridgeError("Notification", "REMOTEVIEWS_PARSE_ERROR", t);
            Log.w(TAG, "RemoteViews.apply inflation warning: " + t.getMessage());
        }

        // Path 2: Also attempt mActions reflection as supplementary fallback for older Android versions
        try {
            Field actionsField = RemoteViews.class.getDeclaredField("mActions");
            actionsField.setAccessible(true);
            ArrayList<?> actions = (ArrayList<?>) actionsField.get(rv);
            if (actions != null) {
                for (Object action : actions) {
                    if (action == null) continue;
                    String className = action.getClass().getName();
                    int viewId = extractViewId(action);
                    // mActions 同时包含隐藏文案，不能把隐藏的“未连接/行驶中”当作当前状态。

                    if (viewId == ID_NOTIF_TV_STATUS_CONN || viewId == ID_NOTIF_LAYOUT_LOCK_UNLOCK) {
                        Integer vis = extractVisibilityValue(action);
                        if (vis != null && connVisible == null) {
                            connVisible = (vis == View.VISIBLE);
                        }
                    } else if (viewId == ID_NOTIF_TV_STATUS_DISCONN) {
                        Integer vis = extractVisibilityValue(action);
                        if (vis != null && vis == View.VISIBLE && connVisible == null) {
                            connVisible = false;
                        }
                    }

                    if (viewId == ID_NOTIF_TV_LOCK || viewId == ID_NOTIF_TV_UNLOCK
                            || className.contains("SetOnClickResponse") || className.contains("SetOnClickPendingIntent")) {
                        PendingIntent pi = extractPendingIntentFromAction(action);
                        if (pi != null) {
                            if (viewId == ID_NOTIF_TV_LOCK) {
                                cachedLockPi = pi;
                                foundLockBtn = true;
                            } else if (viewId == ID_NOTIF_TV_UNLOCK) {
                                cachedUnlockPi = pi;
                                foundUnlockBtn = true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
            diagnosticCounters.increment("notification_reflection_unavailable");
        }

        boolean prevConnected = geelyBleConnected;
        if (connVisible == null && textConnectionHint != null) {
            connVisible = textConnectionHint;
        }
        geelyBleConnected = Boolean.TRUE.equals(connVisible);
        // 未连接时银河通知里的按钮不可点；清掉上一轮连接留下的 PendingIntent，
        // 让 triggerUnlock/triggerLock 走“先排队、连接后立刻点击”的路径，
        // 避免把解锁指令提前发给尚未完成 V2 鉴权的车机。
        if (!geelyBleConnected) {
            cachedUnlockPi = null;
            cachedLockPi = null;
            cachedUnlockView = null;
            cachedLockView = null;
        }

        lastButtonCaptureDiag = String.format(
                "通知栏状态=%s | 解锁按钮=%s | 上锁按钮=%s",
                geelyBleConnected ? "已连接" : "未连接",
                (cachedUnlockPi != null ? "PI已捕获" : (cachedUnlockView != null ? "View已就绪" : "待连接捕获")),
                (cachedLockPi != null ? "PI已捕获" : (cachedLockView != null ? "View已就绪" : "待连接捕获"))
        );
        lastParsedAtMs = SystemClock.elapsedRealtime();
        lastParseDiagnostic = parsePath + "_" + (connVisible == null ? "CONNECTION_UNKNOWN" : "OK");
        diagnosticCounters.increment("notification_parsed");
        if (!lastButtonCaptureDiag.equals(previousDiag) || lastParsedAtMs - lastNotificationLogAtMs >= 30000L) {
            lastNotificationLogAtMs = lastParsedAtMs;
            DiagnosticLog.detail("Notification", "PARSED", "parse_ms=" + (lastParsedAtMs - parseStarted)
                    + " " + diagnosticSnapshot(lastParsedAtMs));
        }
        if (!lastButtonCaptureDiag.equals(previousDiag) || prevConnected != geelyBleConnected) {
            Log.i(TAG, "已解析银河通知栏: " + lastButtonCaptureDiag
                    + " (本次捕获: unlock=" + foundUnlockBtn + ", lock=" + foundLockBtn + ")");
            // 通知就绪立即唤醒唯一队列，不在通知服务中自行点击。
            notifyStateListener();
        }
    }

    /** 判断 View 及所有祖先是否可见；离屏 RemoteViews 不能使用要求挂窗的 isShown。 */
    private static boolean isVisibleInTree(View view) {
        if (view == null) return false;
        View current = view;
        while (current != null) {
            if (current.getVisibility() != View.VISIBLE) return false;
            current = current.getParent() instanceof View ? (View) current.getParent() : null;
        }
        return true;
    }

    /** 遍历可见通知文案，隐藏布局不会覆盖连接或行车状态。 */
    private void traverseInflatedTree(View view, View[] discoveredButtons, Boolean[] connByText) {
        if (view == null || view.getVisibility() != View.VISIBLE) return;
        if (view instanceof TextView) {
            CharSequence cs = ((TextView) view).getText();
            String text = (cs != null) ? cs.toString().trim() : "";
            if (!text.isEmpty()) {
                inspectTextForVehicleStatus(text);
                if ("解锁".equals(text)) {
                    discoveredButtons[0] = view;
                } else if ("上锁".equals(text) || "闭锁".equals(text)) {
                    discoveredButtons[1] = view;
                } else if (view.getVisibility() == View.VISIBLE) {
                    if (text.contains("已连接")) {
                        connByText[0] = true;
                    } else if (text.contains("未连接") && connByText[0] == null) {
                        connByText[0] = false;
                    }
                }
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                traverseInflatedTree(vg.getChildAt(i), discoveredButtons, connByText);
            }
        }
    }

    /**
     * Extracts the PendingIntent stored by RemoteViews.SetOnClickResponse on an inflated View:
     * 1. Checks View.getTag(com.android.internal.R.id.pending_intent_tag)
     * 2. Inspects View's mKeyedTags SparseArray for any PendingIntent or RemoteResponse
     * 3. Inspects View's ListenerInfo.mOnClickListener fields for any PendingIntent or RemoteResponse
     */
    private PendingIntent extractPendingIntentFromInflatedView(View view) {
        if (view == null) return null;

        // 1. Check standard Android system tag "pending_intent_tag"
        try {
            int tagId = Resources.getSystem().getIdentifier("pending_intent_tag", "id", "android");
            if (tagId != 0) {
                Object tagVal = view.getTag(tagId);
                PendingIntent pi = unwrapPendingIntent(tagVal);
                if (pi != null) return pi;
            }
        } catch (Throwable ignored) {
        }

        // 2. Check View.mKeyedTags SparseArray if accessible
        try {
            Field keyedTagsField = View.class.getDeclaredField("mKeyedTags");
            keyedTagsField.setAccessible(true);
            Object sparseObj = keyedTagsField.get(view);
            if (sparseObj instanceof android.util.SparseArray) {
                android.util.SparseArray<?> sa = (android.util.SparseArray<?>) sparseObj;
                for (int i = 0; i < sa.size(); i++) {
                    PendingIntent pi = unwrapPendingIntent(sa.valueAt(i));
                    if (pi != null) return pi;
                }
            }
        } catch (Throwable ignored) {
        }

        // 3. Check View's OnClickListener inside ListenerInfo
        try {
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object listenerInfo = getListenerInfo.invoke(view);
            if (listenerInfo != null) {
                Field onClickField = listenerInfo.getClass().getDeclaredField("mOnClickListener");
                onClickField.setAccessible(true);
                Object clickListener = onClickField.get(listenerInfo);
                if (clickListener != null) {
                    PendingIntent pi = unwrapPendingIntent(clickListener);
                    if (pi != null) return pi;
                    for (Field f : clickListener.getClass().getDeclaredFields()) {
                        try {
                            f.setAccessible(true);
                            PendingIntent innerPi = unwrapPendingIntent(f.get(clickListener));
                            if (innerPi != null) return innerPi;
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private PendingIntent unwrapPendingIntent(Object obj) {
        if (obj == null) return null;
        if (obj instanceof PendingIntent) {
            return (PendingIntent) obj;
        }
        try {
            Class<?> cls = obj.getClass();
            while (cls != null && cls != Object.class) {
                for (Field f : cls.getDeclaredFields()) {
                    try {
                        f.setAccessible(true);
                        Object v = f.get(obj);
                        if (v instanceof PendingIntent) {
                            return (PendingIntent) v;
                        }
                    } catch (Throwable ignored) {
                    }
                }
                cls = cls.getSuperclass();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private int extractViewId(Object action) {
        Class<?> cls = action.getClass();
        while (cls != null && cls != Object.class) {
            for (String fieldName : new String[]{"viewId", "mViewId"}) {
                try {
                    Field f = cls.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    return f.getInt(action);
                } catch (Throwable ignored) {
                }
            }
            cls = cls.getSuperclass();
        }
        return -1;
    }

    private Integer extractVisibilityValue(Object action) {
        Class<?> cls = action.getClass();
        while (cls != null && cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    String name = f.getName().toLowerCase();
                    if (name.contains("visib") && (f.getType() == int.class || f.getType() == Integer.class)) {
                        return (Integer) f.get(action);
                    }
                    if (name.equals("value") && (f.getType() == Object.class || f.getType() == Integer.class)) {
                        Object v = f.get(action);
                        if (v instanceof Integer) {
                            return (Integer) v;
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private PendingIntent extractPendingIntentFromAction(Object action) {
        Class<?> cls = action.getClass();
        while (cls != null && cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(action);
                    PendingIntent pi = unwrapPendingIntent(val);
                    if (pi != null) return pi;
                } catch (Throwable ignored) {
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private void extractAndInspectTextFromAction(Object action) {
        Class<?> cls = action.getClass();
        while (cls != null && cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    Object val = f.get(action);
                    if (val instanceof CharSequence) {
                        inspectTextForVehicleStatus(val.toString());
                    }
                } catch (Throwable ignored) {
                }
            }
            cls = cls.getSuperclass();
        }
    }

    /** 更新当前可见车辆状态，相同的行驶文案也刷新观测时间。 */
    private void inspectTextForVehicleStatus(String text) {
        if (text == null || text.isEmpty()) return;
        String newPark = null;
        if (text.contains("行驶中")) {
            newPark = "行驶中";
        } else if (text.contains("充电中")) {
            newPark = "充电中";
        } else if (text.contains("放电中")) {
            newPark = "放电中";
        } else if (text.contains("已插枪")) {
            newPark = "已插枪";
        } else if (text.contains("已驻车")) {
            newPark = "已驻车";
        }
        if (newPark != null) {
            lastObservedParkStatus = newPark;
            lastObservedParkStatusAtMs = SystemClock.elapsedRealtime();
        }
        if (text.contains("已上锁") || text.contains("已闭锁") || text.contains("已锁车")) {
            lastObservedLockHint = "LOCKED";
        } else if (text.contains("已解锁") || text.contains("未上锁") || text.contains("未闭锁")) {
            lastObservedLockHint = "UNLOCKED";
        }
        if (text.contains("未连接") || text.contains("已断开")) {
            textConnectionHint = false;
        } else if (text.contains("已连接")) {
            textConnectionHint = true;
        }
    }
}
