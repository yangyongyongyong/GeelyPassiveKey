package com.luca.geelykeyradar;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * 官方银河桌面小组件 token 桥接。
 *
 * WgProviderMiddle 本身仍由官方 App 创建 PendingIntent；本类只负责通过 AppWidgetHost
 * 取得官方 RemoteViews，并按官方 LOCK 图标识别控件。它不是 BLE/GATT 连接器，也不伪造
 * 官方 token。
 */
final class WidgetTokenBridge {
    private static final String TAG = "WidgetTokenBridge";
    private static volatile String activeOfficialPkg = OfficialBrandPolicy.PKG_GALAXY;
    private static final int HOST_ID = 0x4752;
    private static final long CONFIRM_FIRST_DELAY_MS = 600L;
    private static final long CONFIRM_SECOND_DELAY_MS = 1600L;

    private static WidgetRemoteViewsSpec remoteViewsSpec;

    private static final Object STATE_LOCK = new Object();
    private static volatile ComponentName PROVIDER = new ComponentName(
            OfficialBrandPolicy.PKG_GALAXY, OfficialBrandPolicy.WIDGET_PROVIDER_GALAXY);

    private static Context appContext;
    private static AppWidgetManager appWidgetManager;
    private static AppWidgetProviderInfo providerInfo;
    private static AppWidgetHost host;
    private static AppWidgetHostView hostView;
    private static Handler mainHandler;
    private static int widgetId = -1;
    private static OfficialControlToken lockClickToken;
    private static OfficialControlToken lockConfirmToken;
    private static PendingIntent configLaunchIntent;
    private static String observedLockHint = "";
    private static long observedLockHintAtMs;
    private static long lastRemoteViewsIdentity;
    private static long lastConfirmAttemptForClickAtMs;
    private static boolean started;
    private static String status = "未启用（可选）";
    private static String lastDiagnostic = "widget=not_started";

    private WidgetTokenBridge() {
    }

    /** 服务启动时调用；未启用时保持关闭。 */
    static void start(Context context) {
        if (context == null) return;
        final Context app = context.getApplicationContext();
        VehicleIdentityStore store = new VehicleIdentityStore(app);
        if (!store.isWidgetFallbackEnabled()) {
            synchronized (STATE_LOCK) {
                if (!started) status = "未启用（可选）";
            }
            return;
        }
        synchronized (STATE_LOCK) {
            if (started) return;
            appContext = app;
            refreshProviderForContextLocked();
            if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
            status = "正在检查官方小组件…";
            lastDiagnostic = "widget=checking";
        }
        try {
            attachOrRequestBinding();
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                status = "绑定失败：" + t.getClass().getSimpleName();
                lastDiagnostic = "widget=bind_error " + t.getClass().getSimpleName();
                started = false;
            }
            DiagnosticLog.bridgeError("Widget", "BIND_ERROR", t);
            Log.w(TAG, "官方小组件绑定失败", t);
        }
    }

    /** 停止当前进程内的监听，保留系统组件；用户关闭通道时另行删除 host。 */
    static void stop() {
        stopInternal(false);
    }

    /** 用户关闭时删除本应用独占的 AppWidget host，避免残留不可见组件。 */
    static void stopAndDelete(Context context) {
        if (context != null) {
            new VehicleIdentityStore(context.getApplicationContext()).setWidgetFallbackEnabled(false);
        }
        stopInternal(true);
    }

    /** 设置页启用后调用；调用前应已写入 enabled=true。 */
    static void enableAndStart(Context context) {
        if (context == null) return;
        new VehicleIdentityStore(context.getApplicationContext()).setWidgetFallbackEnabled(true);
        stopInternal(false);
        start(context);
    }

    static boolean isStarted() {
        synchronized (STATE_LOCK) {
            return started;
        }
    }

    static boolean isBound() {
        synchronized (STATE_LOCK) {
            return started && hostView != null;
        }
    }

    static boolean hasLockToken() {
        synchronized (STATE_LOCK) {
            return lockClickToken != null && lockClickToken.isUsable();
        }
    }

    static long getLockTokenAgeMs(long nowMs) {
        synchronized (STATE_LOCK) {
            return lockClickToken == null || !lockClickToken.isUsable()
                    ? -1L : Math.max(0L, nowMs - lockClickToken.capturedAtMs);
        }
    }

    /** 官方小组件锁图标提供的新鲜锁态；与通知文字锁态按时间取最新。 */
    static String getObservedLockHint() {
        synchronized (STATE_LOCK) {
            return observedLockHint;
        }
    }

    static long getObservedLockHintAtMs() {
        synchronized (STATE_LOCK) {
            return observedLockHintAtMs;
        }
    }

    static AppWidgetHostView getHostView() {
        synchronized (STATE_LOCK) {
            return hostView;
        }
    }

    static PendingIntent getConfigLaunchIntent() {
        synchronized (STATE_LOCK) {
            return configLaunchIntent;
        }
    }

    static String getStatusText() {
        synchronized (STATE_LOCK) {
            return status;
        }
    }

    static String getLastDiagnostic() {
        synchronized (STATE_LOCK) {
            return lastDiagnostic;
        }
    }

    /** 当官方锁态新鲜且目标状态相反时，Widget LOCK 切换才可用。 */
    static boolean canUseFor(PassiveKeyLogic.Action action, String lockHint,
                             long lockHintAtMs, long nowMs) {
        return WidgetTokenPolicy.canUseToggle(action, lockHint, lockHintAtMs, nowMs)
                && hasLockToken();
    }

    static String rejectionReason(PassiveKeyLogic.Action action, String lockHint,
                                  long lockHintAtMs, long nowMs) {
        String reason = WidgetTokenPolicy.reason(action, lockHint, lockHintAtMs, nowMs);
        if (!reason.isEmpty()) return reason;
        return hasLockToken() ? "" : "WIDGET_TOKEN_MISSING";
    }

    /** 重新读取当前 host 的 RemoteViews；设置页可手动触发。 */
    static void refreshNow() {
        synchronized (STATE_LOCK) {
            if (!started || hostView == null) return;
        }
        inspectRemoteViewsIfPresent(true);
    }

    /**
     * 发送官方 LOCK 切换 token。先发送 WIDGET_CLICK；如果官方小组件随后更新出
     * 新鲜 CONFIRM token，则在短延迟内发送确认。没有观察到确认时，点击本身可能已经是
     * 官方直接执行路径，但也可能只是打开了二次确认；因此返回 UNKNOWN，绝不自动重发点击。
     */
    static KeyNotificationBridgeService.ControlResult triggerToggle(
            PassiveKeyLogic.Action action, String lockHint, long lockHintAtMs, long nowMs) {
        OfficialControlToken click;
        synchronized (STATE_LOCK) {
            String reason = rejectionReason(action, lockHint, lockHintAtMs, nowMs);
            if (!reason.isEmpty()) {
                lastDiagnostic = "widget=blocked reason=" + reason;
                DiagnosticLog.event("Widget", "TOGGLE_BLOCKED", "action=" + action
                        + " reason=" + reason + " " + status);
                return KeyNotificationBridgeService.ControlResult.NOT_SENT;
            }
            click = lockClickToken;
            lastConfirmAttemptForClickAtMs = 0L;
        }
        if (click == null || click.pendingIntent == null) {
            return KeyNotificationBridgeService.ControlResult.NOT_SENT;
        }
        try {
            sendPendingIntent(click.pendingIntent);
            final long sentAt = SystemClock.elapsedRealtime();
            synchronized (STATE_LOCK) {
                // 点击已消费当前 RemoteViews 的 token；同一快照不再重复发送。
                lockClickToken = null;
                lockConfirmToken = null;
                lastDiagnostic = "widget=click_sent confirm=pending";
                status = "官方小组件 LOCK 切换已发送";
                DiagnosticLog.event("Widget", "CLICK_SENT", "action=" + action
                        + " lock_hint=" + lockHint + " captured_age_ms=" + (nowMs - click.capturedAtMs));
            }
            if (mainHandler != null) {
                mainHandler.postDelayed(() -> sendConfirmIfUpdated(sentAt), CONFIRM_FIRST_DELAY_MS);
                mainHandler.postDelayed(() -> sendConfirmIfUpdated(sentAt), CONFIRM_SECOND_DELAY_MS);
            }
            // 只有点击 token；二次确认是否出现无法同步得知，不能报告确定成功。
            return KeyNotificationBridgeService.ControlResult.UNKNOWN;
        } catch (PendingIntent.CanceledException cancelled) {
            synchronized (STATE_LOCK) {
                lockClickToken = null;
                lockConfirmToken = null;
                lastDiagnostic = "widget=click_canceled";
                status = "官方小组件 token 已失效";
            }
            DiagnosticLog.event("Widget", "CLICK_CANCELED", "action=" + action);
            return KeyNotificationBridgeService.ControlResult.NOT_SENT;
        } catch (Throwable uncertain) {
            synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=click_unknown " + uncertain.getClass().getSimpleName();
                status = "官方小组件发送结果不确定";
            }
            DiagnosticLog.bridgeError("Widget", "CLICK_UNKNOWN", uncertain);
            return KeyNotificationBridgeService.ControlResult.UNKNOWN;
        }
    }

    /**
     * 首次绑定交给系统权限对话框，不在后台静默调用 bindAppWidgetIdIfAllowed 后假装成功。
     * 已获系统绑定授权时直接附着 host 并返回 null。
     */
    static Intent buildBindIntent(Context context) {
        if (context == null) return null;
        Context app = context.getApplicationContext();
        try {
            synchronized (STATE_LOCK) {
                appContext = app;
                refreshProviderForContextLocked();
                if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
                ensureHostLocked();
                if (host == null || widgetId < 0) return null;
            }
            AppWidgetProviderInfo info = findProvider();
            if (info == null) {
                synchronized (STATE_LOCK) {
                    status = "未找到银河官方小组件";
                    lastDiagnostic = "widget=provider_not_visible";
                }
                return null;
            }
            synchronized (STATE_LOCK) {
                providerInfo = info;
            }
            if (isWidgetBoundToProvider(appWidgetManager, widgetId)
                    || appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, PROVIDER)) {
                attachHost(info);
                return null;
            }
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_BIND);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, PROVIDER);
            synchronized (STATE_LOCK) {
                status = "需要在系统页允许绑定官方小组件";
                lastDiagnostic = "widget=bind_permission_required id=" + widgetId;
            }
            DiagnosticLog.event("Widget", "BIND_PERMISSION_REQUEST", "id=" + widgetId);
            return intent;
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                status = "无法准备官方小组件绑定";
                lastDiagnostic = "widget=bind_prepare_error " + t.getClass().getSimpleName();
            }
            DiagnosticLog.bridgeError("Widget", "BIND_PREPARE_ERROR", t);
            return null;
        }
    }

    /** 接收系统绑定结果；只有 RESULT_OK 且系统允许该 provider 时才附着。 */
    static boolean onBindResult(Context context, int resultCode, Intent data) {
        if (context == null) return false;
        if (resultCode != Activity.RESULT_OK) {
            synchronized (STATE_LOCK) {
                if (host != null && widgetId >= 0) {
                    try {
                        host.deleteAppWidgetId(widgetId);
                    } catch (Throwable ignored) {
                    }
                }
                widgetId = -1;
                status = "已取消官方小组件绑定";
                lastDiagnostic = "widget=bind_cancelled";
            }
            return false;
        }
        Context app = context.getApplicationContext();
        int resultId = data == null ? -1 : data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        try {
            synchronized (STATE_LOCK) {
                appContext = app;
                if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
                if (resultId >= 0) widgetId = resultId;
                ensureHostLocked();
            }
            AppWidgetProviderInfo info = findProvider();
            if (info == null) {
                synchronized (STATE_LOCK) {
                    status = "未找到银河官方小组件";
                    lastDiagnostic = "widget=provider_not_visible";
                }
                return false;
            }
            if (!isWidgetBoundToProvider(appWidgetManager, widgetId)
                    && !appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, PROVIDER)) {
                synchronized (STATE_LOCK) {
                    status = "系统未允许绑定官方小组件";
                    lastDiagnostic = "widget=bind_denied id=" + widgetId;
                }
                return false;
            }
            synchronized (STATE_LOCK) {
                providerInfo = info;
            }
            attachHost(info);
            return isBound();
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                status = "绑定结果处理失败";
                lastDiagnostic = "widget=bind_result_error " + t.getClass().getSimpleName();
            }
            DiagnosticLog.bridgeError("Widget", "BIND_RESULT_ERROR", t);
            return false;
        }
    }

    /** 官方小组件配置页；不自动替用户选择车辆或点击控件。 */
    static Intent buildConfigureIntent(Context context) {
        if (context == null) return null;
        try {
            synchronized (STATE_LOCK) {
                appContext = context.getApplicationContext();
                if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
                if (providerInfo == null) providerInfo = findProviderLocked();
            }
            AppWidgetProviderInfo info;
            int id;
            synchronized (STATE_LOCK) {
                info = providerInfo;
                id = widgetId;
            }
            if (info == null || info.configure == null || id < 0) return null;
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(info.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            return intent;
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Widget", "CONFIG_INTENT_ERROR", t);
            return null;
        }
    }

    /** 用户点击后打开官方配置页；优先使用 RemoteViews 中的官方 PendingIntent。 */
    static boolean openConfig(Context context) {
        if (context == null) return false;
        PendingIntent official = getConfigLaunchIntent();
        if (official != null) {
            try {
                sendPendingIntent(official);
                DiagnosticLog.event("Widget", "CONFIG_OPEN_PI", "source=remoteviews");
                return true;
            } catch (Throwable error) {
                DiagnosticLog.bridgeError("Widget", "CONFIG_OPEN_PI_ERROR", error);
            }
        }
        Intent configure = buildConfigureIntent(context);
        if (configure == null) return false;
        try {
            if (!(context instanceof Activity)) {
                configure.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(configure);
            DiagnosticLog.event("Widget", "CONFIG_OPEN_ACTIVITY", "component=" + configure.getComponent());
            return true;
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Widget", "CONFIG_OPEN_ACTIVITY_ERROR", error);
            return false;
        }
    }

    /** 查找当前官方 provider，不把其他车型同名组件混入。 */
    private static void attachOrRequestBinding() throws Exception {
        synchronized (STATE_LOCK) {
            if (appWidgetManager == null) appWidgetManager = AppWidgetManager.getInstance(appContext);
            ensureHostLocked();
        }
        AppWidgetProviderInfo info = findProvider();
        if (info == null) {
            synchronized (STATE_LOCK) {
                status = "未找到银河官方小组件";
                lastDiagnostic = "widget=provider_not_visible";
                started = false;
            }
            return;
        }
        synchronized (STATE_LOCK) {
            providerInfo = info;
        }
        boolean bound = isWidgetBoundToProvider(appWidgetManager, widgetId)
                || appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, PROVIDER);
        synchronized (STATE_LOCK) {
            status = bound ? "已绑定，等待官方车锁控件" : "需要允许绑定官方小组件";
            lastDiagnostic = "widget=" + (bound ? "bound id=" + widgetId : "bind_permission_required id=" + widgetId);
        }
        if (!bound) {
            return;
        }
        attachHost(info);
    }

    private static void attachHost(AppWidgetProviderInfo info) {
        if (info == null) return;
        AppWidgetHost activeHost;
        int activeId;
        synchronized (STATE_LOCK) {
            activeHost = host;
            activeId = widgetId;
            if (activeHost == null || activeId < 0) return;
        }
        try {
            activeHost.startListening();
            AppWidgetHostView view = activeHost.createView(appContext, activeId, info);
            if (view == null) {
                synchronized (STATE_LOCK) {
                    status = "官方小组件初始化失败";
                    lastDiagnostic = "widget=create_view_null";
                    started = false;
                }
                return;
            }
            synchronized (STATE_LOCK) {
                hostView = view;
                started = true;
                status = "已绑定，等待官方车锁控件";
                lastDiagnostic = "widget=host_started id=" + activeId;
            }
            inspectRemoteViewsIfPresent(true);
            schedulePoll();
            DiagnosticLog.event("Widget", "BOUND", "id=" + activeId + " provider=" + PROVIDER.getClassName());
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                status = "绑定后读取官方小组件失败";
                lastDiagnostic = "widget=attach_error " + t.getClass().getSimpleName();
                started = false;
            }
            DiagnosticLog.bridgeError("Widget", "ATTACH_ERROR", t);
            Log.w(TAG, "官方小组件附着失败", t);
        }
    }

    private static void refreshProviderForContextLocked() {
        if (appContext == null) return;
        String pkg = OfficialTarget.resolveConfiguredPackage(appContext);
        if (!pkg.equals(activeOfficialPkg)) {
            remoteViewsSpec = null;
        }
        activeOfficialPkg = pkg;
        PROVIDER = new ComponentName(pkg, OfficialBrandPolicy.widgetProviderForPackage(pkg));
    }

    private static void ensureHostLocked() {
        if (appContext == null) return;
        if (appWidgetManager == null) appWidgetManager = AppWidgetManager.getInstance(appContext);
        if (host == null) host = new BridgeHost(appContext, HOST_ID);
        if (widgetId < 0) {
            int[] existing = host.getAppWidgetIds();
            int chosen = -1;
            if (existing != null) {
                for (int candidate : existing) {
                    if (isWidgetBoundToProvider(appWidgetManager, candidate)) {
                        chosen = candidate;
                        break;
                    }
                }
                if (chosen < 0 && existing.length > 0) {
                    chosen = existing[0];
                }
                for (int candidate : existing) {
                    if (candidate != chosen) {
                        try {
                            host.deleteAppWidgetId(candidate);
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }
            widgetId = chosen >= 0 ? chosen : host.allocateAppWidgetId();
        }
    }

    private static boolean isWidgetBoundToProvider(AppWidgetManager manager, int id) {
        if (manager == null || id < 0) return false;
        try {
            AppWidgetProviderInfo existingInfo = manager.getAppWidgetInfo(id);
            return existingInfo != null && PROVIDER.equals(existingInfo.provider);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static AppWidgetProviderInfo findProvider() {
        synchronized (STATE_LOCK) {
            return findProviderLocked();
        }
    }

    private static AppWidgetProviderInfo findProviderLocked() {
        if (appWidgetManager == null) {
            if (appContext == null) return null;
            appWidgetManager = AppWidgetManager.getInstance(appContext);
        }
        try {
            List<AppWidgetProviderInfo> providers = appWidgetManager.getInstalledProviders();
            AppWidgetProviderInfo match = findProvider(providers);
            if (match != null) return match;
        } catch (Throwable ignored) {
        }
        try {
            List<AppWidgetProviderInfo> scoped =
                    appWidgetManager.getInstalledProvidersForPackage(activeOfficialPkg, null);
            return findProvider(scoped);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static AppWidgetProviderInfo findProvider(List<AppWidgetProviderInfo> providers) {
        if (providers == null) return null;
        for (AppWidgetProviderInfo provider : providers) {
            if (provider != null && PROVIDER.equals(provider.provider)) return provider;
        }
        return null;
    }

    private static void inspectRemoteViewsIfPresent(boolean force) {
        synchronized (STATE_LOCK) {
            if (!started || hostView == null) return;
        }
        try {
            AppWidgetHostView view;
            synchronized (STATE_LOCK) {
                view = hostView;
            }
            RemoteViews views = findRemoteViews(view);
            if (views != null) inspectRemoteViews(views, force);
            else synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=waiting_remoteviews";
            }
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=remoteviews_error " + t.getClass().getSimpleName();
            }
        }
    }

    private static void schedulePoll() {
        if (mainHandler == null) return;
        mainHandler.removeCallbacks(POLL_RUNNABLE);
        mainHandler.postDelayed(POLL_RUNNABLE, 800L);
    }

    private static final Runnable POLL_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            synchronized (STATE_LOCK) {
                if (!started) return;
            }
            inspectRemoteViewsIfPresent(false);
            synchronized (STATE_LOCK) {
                if (started && mainHandler != null) {
                    mainHandler.postDelayed(this, 1200L);
                }
            }
        }
    };

    private static void removePoll() {
        if (mainHandler != null) mainHandler.removeCallbacks(POLL_RUNNABLE);
    }

    private static void stopInternal(boolean deleteHost) {
        AppWidgetHost oldHost;
        synchronized (STATE_LOCK) {
            removePoll();
            oldHost = host;
            host = null;
            hostView = null;
            appWidgetManager = null;
            providerInfo = null;
            remoteViewsSpec = null;
            widgetId = -1;
            lockClickToken = null;
            lockConfirmToken = null;
            configLaunchIntent = null;
            observedLockHint = "";
            observedLockHintAtMs = 0L;
            lastRemoteViewsIdentity = 0L;
            lastConfirmAttemptForClickAtMs = 0L;
            started = false;
            status = deleteHost ? "未启用（可选）" : "已停止";
            lastDiagnostic = deleteHost ? "widget=disabled" : "widget=stopped";
        }
        if (oldHost == null && deleteHost && appContext != null) {
            try {
                oldHost = new AppWidgetHost(appContext, HOST_ID);
            } catch (Throwable ignored) {
            }
        }
        if (oldHost != null) {
            try {
                if (deleteHost) oldHost.deleteHost();
                else oldHost.stopListening();
            } catch (Throwable ignored) {
            }
        }
    }

    private static void onHostViewRemoteViewsUpdated(BridgeHostView view, RemoteViews remoteViews) {
        synchronized (STATE_LOCK) {
            if (!started || hostView != view) return;
        }
        try {
            inspectRemoteViews(remoteViews, false);
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=remoteviews_error " + t.getClass().getSimpleName();
            }
        }
    }

    /** 优先使用 AppWidgetHostView.updateAppWidget 公开回调捕获的 RemoteViews，失败时再回退私有字段反射。 */
    private static RemoteViews findRemoteViews(Object view) {
        if (view == null) return null;
        if (view instanceof BridgeHostView) {
            RemoteViews direct = ((BridgeHostView) view).capturedRemoteViews;
            if (direct != null) return direct;
        }
        for (Class<?> c = view.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            for (String fieldName : new String[]{"mLastInflatedRemoteViews", "mRemoteViews"}) {
                try {
                    Field field = c.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(view);
                    if (value instanceof RemoteViews) return (RemoteViews) value;
                } catch (NoSuchFieldException ignored) {
                } catch (Throwable ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    /** 递归扫描官方 RemoteViews；锁图标用于避免把其他车控图标误当成 LOCK。 */
    private static void inspectRemoteViews(RemoteViews root, boolean force) throws Exception {
        long identity = System.identityHashCode(root);
        synchronized (STATE_LOCK) {
            if (!force && identity == lastRemoteViewsIdentity) return;
            lastRemoteViewsIdentity = identity;
        }
        WidgetRemoteViewsSpec spec = getOrResolveRemoteViewsSpec();
        Visit visit = new Visit();
        scanRemoteViews(root, visit, spec);
        boolean click = visit.lockClick != null;
        boolean confirm = visit.lockConfirm != null;
        long now = SystemClock.elapsedRealtime();
        synchronized (STATE_LOCK) {
            // RemoteViews 是整份快照；新快照没有 LOCK 控件时必须撤销旧 token，
            // 避免官方控件正在 loading/切车时复用上一帧的点击。
            // 二次确认界面存在时禁止再次点击 LOCK，先完成/取消确认再由新快照恢复。
            lockClickToken = !confirm && click
                    ? OfficialControlToken.widgetToggle(visit.lockClick, now) : null;
            lockConfirmToken = confirm ? OfficialControlToken.widgetToggle(visit.lockConfirm, now) : null;
            if (visit.config != null) configLaunchIntent = visit.config;
            if (visit.lockHint != null) {
                observedLockHint = visit.lockHint;
                observedLockHintAtMs = now;
            }
            if (lockConfirmToken != null && lockClickToken == null) {
                status = "官方小组件等待确认";
                lastDiagnostic = "widget=confirm_pending confirm=PI lock_hint="
                        + (observedLockHint.isEmpty() ? "UNKNOWN" : observedLockHint);
            } else if (lockClickToken != null) {
                status = "官方小组件车锁 token 已就绪";
                lastDiagnostic = "widget=ready click=PI confirm=" + (lockConfirmToken != null ? "PI" : "pending")
                        + " lock_hint=" + (observedLockHint.isEmpty() ? "UNKNOWN" : observedLockHint)
                        + " token_age_ms=" + Math.max(0L, now - lockClickToken.capturedAtMs);
            } else {
                status = "等待官方车锁控件（请在银河完成车辆选择）";
                lastDiagnostic = "widget=waiting_lock_control click=false confirm=false";
            }
        }
        if (click || confirm) {
            DiagnosticLog.event("Widget", "REMOTEVIEWS_READY", "click=" + click
                    + " confirm=" + confirm + " " + lastDiagnostic);
        }
    }

    private static WidgetRemoteViewsSpec getOrResolveRemoteViewsSpec() {
        Context ctx;
        synchronized (STATE_LOCK) {
            if (remoteViewsSpec != null) return remoteViewsSpec;
            ctx = appContext;
        }
        WidgetRemoteViewsSpec resolved = resolveRemoteViewsSpec(ctx);
        synchronized (STATE_LOCK) {
            if (remoteViewsSpec == null) remoteViewsSpec = resolved;
            return remoteViewsSpec;
        }
    }

    private static WidgetRemoteViewsSpec resolveRemoteViewsSpec(Context context) {
        if (context == null) return WidgetRemoteViewsSpec.defaults();
        String pkg = activeOfficialPkg;
        try {
            Context pkgCtx = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            Resources res = pkgCtx.getResources();
            int loadingId = res.getIdentifier(WidgetRemoteViewsSpec.NAME_VIEW_FUN_LOADING, "id", pkg);
            int statusId = res.getIdentifier(WidgetRemoteViewsSpec.NAME_VIEW_FUN_STATUS, "id", pkg);
            int confirmOkId = res.getIdentifier(WidgetRemoteViewsSpec.NAME_CONFIRM_OK, "id", pkg);
            int settingId = res.getIdentifier(WidgetRemoteViewsSpec.NAME_BTN_SETTING, "id", pkg);
            int appAdditionId = res.getIdentifier(WidgetRemoteViewsSpec.NAME_LL_APP_ADDITION, "id", pkg);
            int lockOffBlackId = resolveDrawableId(res, pkg, WidgetRemoteViewsSpec.NAME_ICON_LOCK_OFF_BLACK);
            int lockOffWhiteId = resolveDrawableId(res, pkg, WidgetRemoteViewsSpec.NAME_ICON_LOCK_OFF_WHITE);
            int lockOnId = resolveDrawableId(res, pkg, WidgetRemoteViewsSpec.NAME_ICON_LOCK_ON);
            return WidgetRemoteViewsSpec.create(loadingId, statusId, confirmOkId, settingId,
                    appAdditionId, lockOffBlackId, lockOffWhiteId, lockOnId);
        } catch (Throwable ignored) {
            return WidgetRemoteViewsSpec.defaults();
        }
    }

    private static int resolveDrawableId(Resources res, String pkg, String name) {
        int id = res.getIdentifier(name, "drawable", pkg);
        return id != 0 ? id : res.getIdentifier(name, "mipmap", pkg);
    }

    private static void scanRemoteViews(RemoteViews rv, Visit visit, WidgetRemoteViewsSpec spec) {
        if (rv == null || !visit.visited.add(rv)) return;
        String lockHint = lockHintFromIcon(rv, spec);
        try {
            Field actionsField = RemoteViews.class.getDeclaredField("mActions");
            actionsField.setAccessible(true);
            Object raw = actionsField.get(rv);
            if (!(raw instanceof Iterable)) return;
            for (Object action : (Iterable<?>) raw) {
                if (action == null) continue;
                int viewId = readViewId(action);
                PendingIntent actionPendingIntent = readPendingIntent(action);
                if (spec.isSettingView(viewId) && actionPendingIntent != null) {
                    visit.config = actionPendingIntent;
                } else if (spec.isAppAdditionView(viewId) && visit.config == null
                        && actionPendingIntent != null) {
                    visit.config = actionPendingIntent;
                }
                PendingIntent pi = actionPendingIntent;
                if (pi != null) {
                    if (spec.isConfirmOkView(viewId)) visit.lockConfirm = pi;
                    if (spec.isFunStatusView(viewId) && lockHint != null) {
                        visit.lockClick = pi;
                        visit.lockHint = lockHint;
                    }
                }
                Object nested = readField(action, "mNestedViews");
                if (nested instanceof RemoteViews) scanRemoteViews((RemoteViews) nested, visit, spec);
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * 官方 WidgetUiStatus 对 LOCK 的 onStatus 等于 anyDoorUnLocked；
     * WidgetFunTypeIcon 在 onStatus 时显示 car_wg_funicon_door_lock_on（开锁图标）。
     * 因此 on 资源表示 UNLOCKED，两个 off 资源表示 LOCKED。
     */
    private static String lockHintFromIcon(RemoteViews rv, WidgetRemoteViewsSpec spec) {
        String result = null;
        try {
            Field actionsField = RemoteViews.class.getDeclaredField("mActions");
            actionsField.setAccessible(true);
            Object raw = actionsField.get(rv);
            if (!(raw instanceof Iterable)) return null;
            for (Object action : (Iterable<?>) raw) {
                if (action == null) continue;
                String method = String.valueOf(readField(action, "mMethodName"));
                if (!method.contains("setImageResource") && !method.contains("setImageViewResource")) continue;
                Object value = readField(action, "mValue");
                if (!(value instanceof Number)) continue;
                int resourceId = ((Number) value).intValue();
                String candidate = spec.lockHintForDrawable(resourceId);
                if (candidate == null) continue;
                if (result != null && !result.equals(candidate)) return null;
                result = candidate;
            }
        } catch (Throwable ignored) {
        }
        return result;
    }

    private static int readViewId(Object action) {
        int id = readInt(action, "mViewId");
        return id != 0 ? id : readInt(action, "viewId");
    }

    private static PendingIntent readPendingIntent(Object action) {
        Object direct = readField(action, "mPendingIntent");
        if (direct instanceof PendingIntent) return (PendingIntent) direct;
        Object response = readField(action, "mResponse");
        if (response != null) {
            Object nested = readField(response, "mPendingIntent");
            if (nested instanceof PendingIntent) return (PendingIntent) nested;
        }
        return null;
    }

    private static int readInt(Object object, String name) {
        Object value = readField(object, name);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    private static Object readField(Object object, String name) {
        if (object == null) return null;
        for (Class<?> c = object.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException ignored) {
            } catch (Throwable ignored) {
                return null;
            }
        }
        return null;
    }

    /** 只接受点击后新捕获的 CONFIRM token，旧确认 token 不得用于新命令。 */
    private static void sendConfirmIfUpdated(long sentAt) {
        OfficialControlToken confirm;
        synchronized (STATE_LOCK) {
            if (lastConfirmAttemptForClickAtMs == sentAt) return;
            confirm = lockConfirmToken;
            if (confirm == null || confirm.pendingIntent == null) {
                lastDiagnostic = "widget=click_sent confirm=not_seen";
                return;
            }
            if (confirm.capturedAtMs < sentAt) {
                lastDiagnostic = "widget=click_sent confirm=stale_ignored";
                return;
            }
            lastConfirmAttemptForClickAtMs = sentAt;
        }
        try {
            sendPendingIntent(confirm.pendingIntent);
            synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=click_sent confirm=sent age_ms="
                        + Math.max(0L, SystemClock.elapsedRealtime() - confirm.capturedAtMs);
                status = "官方小组件确认已发送";
            }
            DiagnosticLog.event("Widget", "CONFIRM_SENT", "age_ms="
                    + (SystemClock.elapsedRealtime() - confirm.capturedAtMs));
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                lastConfirmAttemptForClickAtMs = 0L;
                lastDiagnostic = "widget=click_sent confirm_error " + t.getClass().getSimpleName();
                status = "官方小组件确认发送失败";
            }
            DiagnosticLog.bridgeError("Widget", "CONFIRM_SEND_ERROR", t);
        }
    }

    /** 与通知桥接一致的 Android 14+ 后台 PendingIntent 发送参数。 */
    static void sendPendingIntent(PendingIntent pendingIntent) throws PendingIntent.CanceledException {
        if (Build.VERSION.SDK_INT >= 34) {
            ActivityOptions options = ActivityOptions.makeBasic();
            options.setPendingIntentBackgroundActivityStartMode(
                    ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED);
            pendingIntent.send(null, 0, null, null, null, null, options.toBundle());
        } else {
            pendingIntent.send();
        }
    }

    private static final class BridgeHost extends AppWidgetHost {
        BridgeHost(Context context, int hostId) {
            super(context, hostId);
        }

        @Override
        protected AppWidgetHostView onCreateView(Context context, int appWidgetId,
                                                 AppWidgetProviderInfo appWidget) {
            return new BridgeHostView(context);
        }
    }

    private static final class BridgeHostView extends AppWidgetHostView {
        volatile RemoteViews capturedRemoteViews;

        BridgeHostView(Context context) {
            super(context);
        }

        @Override
        public void updateAppWidget(RemoteViews remoteViews) {
            if (remoteViews != null) {
                capturedRemoteViews = remoteViews;
            }
            try {
                super.updateAppWidget(remoteViews);
            } catch (Throwable ignored) {
            }
            if (remoteViews != null) {
                onHostViewRemoteViewsUpdated(this, remoteViews);
            }
        }
    }

    private static final class Visit {
        final Set<RemoteViews> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        PendingIntent lockClick;
        PendingIntent lockConfirm;
        PendingIntent config;
        String lockHint;
    }
}
