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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 官方银河 / 吉利桌面小组件 token 桥接。
 *
 * WgProviderMiddle / Wg_Middle 本身仍由官方 App 创建 PendingIntent；
 * 本类通过 AppWidgetHostView.updateAppWidget 公开回调取得官方 RemoteViews，
 * 优先直接遍历已膨胀的公开 View 树并从系统 pending_intent_tag 提取 PendingIntent，
 * 避免 Android 14-17 隐藏 API（mNestedViews / mResponse）被系统拦截。
 */
final class WidgetTokenBridge {
    private static final String TAG = "WidgetTokenBridge";
    private static final int HOST_ID = 0x4752;
    private static final long CONFIRM_FIRST_DELAY_MS = 600L;
    private static final long CONFIRM_SECOND_DELAY_MS = 1600L;

    private static volatile String activeOfficialPkg = OfficialBrandPolicy.PKG_GALAXY;
    private static WidgetRemoteViewsSpec remoteViewsSpec;
    private static IconReferenceCache iconReferenceCache;

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
    private static View configLaunchView;
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

    static void stop() {
        stopInternal(false);
    }

    static void stopAndDelete(Context context) {
        if (context != null) {
            new VehicleIdentityStore(context.getApplicationContext()).setWidgetFallbackEnabled(false);
        }
        stopInternal(true);
    }

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

    static boolean hasConfigEntry() {
        synchronized (STATE_LOCK) {
            return configLaunchIntent != null || configLaunchView != null
                    || (providerInfo != null && providerInfo.configure != null);
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

    static void refreshNow() {
        synchronized (STATE_LOCK) {
            if (!started || hostView == null) return;
        }
        inspectRemoteViewsIfPresent(true);
    }

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
                    status = "未找到官方小组件";
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
                    status = "未找到官方小组件";
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

    /** 用户点击后打开官方配置页；优先使用从官方小组件视图提取的 PendingIntent / 按钮视图。 */
    static boolean openConfig(Context context) {
        if (context == null) return false;
        refreshNow();
        PendingIntent official;
        View fallbackView;
        synchronized (STATE_LOCK) {
            official = configLaunchIntent;
            fallbackView = configLaunchView;
        }
        if (official != null) {
            try {
                sendPendingIntent(official);
                DiagnosticLog.event("Widget", "CONFIG_OPEN_PI", "source=inflated_view");
                return true;
            } catch (Throwable error) {
                DiagnosticLog.bridgeError("Widget", "CONFIG_OPEN_PI_ERROR", error);
            }
        }
        if (fallbackView != null) {
            try {
                if (fallbackView.performClick()) {
                    DiagnosticLog.event("Widget", "CONFIG_OPEN_VIEW_CLICK", "");
                    return true;
                }
            } catch (Throwable error) {
                DiagnosticLog.bridgeError("Widget", "CONFIG_OPEN_VIEW_ERROR", error);
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

    private static void attachOrRequestBinding() {
        synchronized (STATE_LOCK) {
            if (appWidgetManager == null) appWidgetManager = AppWidgetManager.getInstance(appContext);
            ensureHostLocked();
        }
        AppWidgetProviderInfo info = findProvider();
        if (info == null) {
            synchronized (STATE_LOCK) {
                status = "未找到官方小组件";
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
            iconReferenceCache = null;
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
            java.util.List<AppWidgetProviderInfo> providers = appWidgetManager.getInstalledProviders();
            AppWidgetProviderInfo match = findProvider(providers);
            if (match != null) return match;
        } catch (Throwable ignored) {
        }
        try {
            java.util.List<AppWidgetProviderInfo> scoped =
                    appWidgetManager.getInstalledProvidersForPackage(activeOfficialPkg, null);
            return findProvider(scoped);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static AppWidgetProviderInfo findProvider(java.util.List<AppWidgetProviderInfo> providers) {
        if (providers == null) return null;
        for (AppWidgetProviderInfo provider : providers) {
            if (provider != null && PROVIDER.equals(provider.provider)) return provider;
        }
        return null;
    }

    private static void inspectRemoteViewsIfPresent(boolean force) {
        AppWidgetHostView view;
        synchronized (STATE_LOCK) {
            if (!started || hostView == null) return;
            view = hostView;
        }
        try {
            RemoteViews views = findRemoteViews(view);
            if (views != null || view.getChildCount() > 0) {
                inspectHostAndRemoteViews(view, views, force);
            } else {
                synchronized (STATE_LOCK) {
                    lastDiagnostic = "widget=waiting_remoteviews";
                }
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
            iconReferenceCache = null;
            widgetId = -1;
            lockClickToken = null;
            lockConfirmToken = null;
            configLaunchIntent = null;
            configLaunchView = null;
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
            inspectHostAndRemoteViews(view, remoteViews, false);
        } catch (Throwable t) {
            synchronized (STATE_LOCK) {
                lastDiagnostic = "widget=remoteviews_error " + t.getClass().getSimpleName();
            }
        }
    }

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

    /**
     * 优先遍历公开膨胀后的 View 树（AppWidgetHostView / RemoteViews.apply）提取配置入口、
     * 车锁点击与确认 PendingIntent，规避 Android 14+ 隐藏 API 封锁。
     */
    private static void inspectHostAndRemoteViews(AppWidgetHostView view, RemoteViews root, boolean force) {
        long identity = root != null ? System.identityHashCode(root)
                : (view != null ? System.identityHashCode(view) : 0L);
        synchronized (STATE_LOCK) {
            if (!force && identity == lastRemoteViewsIdentity) return;
            lastRemoteViewsIdentity = identity;
        }
        WidgetRemoteViewsSpec spec = getOrResolveRemoteViewsSpec();
        IconReferenceCache icons = getOrBuildIconReferences(spec);
        Visit visit = new Visit();

        if (view != null && view.getChildCount() > 0) {
            scanInflatedViewTree(view, spec, icons, visit);
        }
        if (root != null && visit.config == null && visit.lockClick == null && visit.lockConfirm == null) {
            try {
                Context ctx = getOfficialPkgContext();
                FrameLayout container = new FrameLayout(ctx);
                View applied = root.apply(ctx, container);
                if (applied != null) {
                    scanInflatedViewTree(applied, spec, icons, visit);
                }
            } catch (Throwable ignored) {
            }
        }
        if (root != null && Build.VERSION.SDK_INT < 30
                && visit.config == null && visit.lockClick == null && visit.lockConfirm == null) {
            scanLegacyRemoteViews(root, visit, spec);
        }

        boolean click = visit.lockClick != null;
        boolean confirm = visit.lockConfirm != null;
        boolean hasConfig = visit.config != null || visit.configView != null;
        long now = SystemClock.elapsedRealtime();
        String prevDiag;
        String nextDiag;
        synchronized (STATE_LOCK) {
            prevDiag = lastDiagnostic;
            lockClickToken = !confirm && click
                    ? OfficialControlToken.widgetToggle(visit.lockClick, now) : null;
            lockConfirmToken = confirm ? OfficialControlToken.widgetToggle(visit.lockConfirm, now) : null;
            if (visit.config != null) configLaunchIntent = visit.config;
            if (visit.configView != null) configLaunchView = visit.configView;
            if (visit.lockHint != null) {
                observedLockHint = visit.lockHint;
                observedLockHintAtMs = now;
            }
            if (lockConfirmToken != null && lockClickToken == null) {
                status = "官方小组件等待确认";
                lastDiagnostic = "widget=confirm_pending confirm=PI config=" + hasConfig
                        + " lock_hint=" + (observedLockHint.isEmpty() ? "UNKNOWN" : observedLockHint);
            } else if (lockClickToken != null) {
                status = "官方小组件车锁 token 已就绪";
                lastDiagnostic = "widget=ready click=PI confirm=" + (lockConfirmToken != null ? "PI" : "pending")
                        + " config=" + hasConfig
                        + " lock_hint=" + (observedLockHint.isEmpty() ? "UNKNOWN" : observedLockHint)
                        + " token_age_ms=" + Math.max(0L, now - lockClickToken.capturedAtMs);
            } else {
                status = hasConfig
                        ? "已找到官方配置入口（请在官方配置页选择车辆与车锁）"
                        : "等待官方车锁控件（请在官方 App 完成车辆选择）";
                lastDiagnostic = "widget=waiting_lock_control click=false confirm=false config=" + hasConfig;
            }
            nextDiag = lastDiagnostic;
        }
        if (!nextDiag.equals(prevDiag) && (click || confirm || hasConfig)) {
            DiagnosticLog.event("Widget", "REMOTEVIEWS_READY", "click=" + click
                    + " confirm=" + confirm + " config=" + hasConfig + " " + nextDiag);
        }
    }

    private static void scanInflatedViewTree(View node, WidgetRemoteViewsSpec spec,
                                             IconReferenceCache icons, Visit visit) {
        if (node == null) return;
        int viewId = node.getId();
        if (viewId != View.NO_ID && viewId != 0) {
            if (spec.isSettingView(viewId)) {
                PendingIntent pi = extractPendingIntentFromInflatedView(node);
                if (pi != null) visit.config = pi;
                if (pi != null || node.hasOnClickListeners() || node.isClickable()) {
                    visit.configView = node;
                }
            } else if (spec.isAppAdditionView(viewId)) {
                PendingIntent pi = extractPendingIntentFromInflatedView(node);
                if (visit.config == null && pi != null) visit.config = pi;
                if (visit.configView == null && (pi != null || node.hasOnClickListeners() || node.isClickable())) {
                    visit.configView = node;
                }
            } else if (spec.isConfirmOkView(viewId)) {
                PendingIntent pi = extractPendingIntentFromInflatedView(node);
                if (pi != null) visit.lockConfirm = pi;
            } else if (spec.isFunStatusView(viewId)) {
                PendingIntent pi = extractPendingIntentFromInflatedView(node);
                if (pi != null) {
                    String hint = detectLockHintForItem(node, icons);
                    if (hint != null) {
                        visit.lockClick = pi;
                        visit.lockHint = hint;
                    }
                }
            }
        }
        if (node instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) node;
            int count = vg.getChildCount();
            for (int i = 0; i < count; i++) {
                scanInflatedViewTree(vg.getChildAt(i), spec, icons, visit);
            }
        }
    }

    private static String detectLockHintForItem(View funView, IconReferenceCache icons) {
        if (funView == null || icons == null || !icons.isUsable()) return null;
        String direct = matchImageDrawable(funView, icons);
        if (direct != null) return direct;
        if (funView.getParent() instanceof ViewGroup) {
            return searchImageDrawableInContainer((ViewGroup) funView.getParent(), icons, 0);
        }
        return null;
    }

    private static String searchImageDrawableInContainer(View node, IconReferenceCache icons, int depth) {
        if (node == null || depth > 3) return null;
        String matched = matchImageDrawable(node, icons);
        if (matched != null) return matched;
        if (node instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) node;
            for (int i = 0; i < vg.getChildCount(); i++) {
                String childMatch = searchImageDrawableInContainer(vg.getChildAt(i), icons, depth + 1);
                if (childMatch != null) return childMatch;
            }
        }
        return null;
    }

    private static String matchImageDrawable(View view, IconReferenceCache icons) {
        if (!(view instanceof ImageView)) return null;
        Drawable d = ((ImageView) view).getDrawable();
        if (d == null) return null;
        int[] pixels = rasterizeDrawable(d);
        return WidgetRemoteViewsSpec.matchNormalizedIconPixels(
                pixels, icons.offBlack, icons.offWhite, icons.on);
    }

    private static int[] rasterizeDrawable(Drawable drawable) {
        if (drawable == null) return null;
        int size = WidgetRemoteViewsSpec.ICON_GRID_SIZE;
        try {
            Bitmap bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Drawable copy = drawable.getConstantState() != null
                    ? drawable.getConstantState().newDrawable().mutate() : drawable.mutate();
            copy.setBounds(0, 0, size, size);
            copy.draw(canvas);
            int[] pixels = new int[size * size];
            bmp.getPixels(pixels, 0, size, 0, 0, size, size);
            bmp.recycle();
            return pixels;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 与 KeyNotificationBridgeService 相同的公开 PendingIntent 提取链：优先读取系统 pending_intent_tag。 */
    private static PendingIntent extractPendingIntentFromInflatedView(View view) {
        if (view == null) return null;
        try {
            int tagId = Resources.getSystem().getIdentifier("pending_intent_tag", "id", "android");
            if (tagId != 0) {
                PendingIntent pi = unwrapPendingIntent(view.getTag(tagId));
                if (pi != null) return pi;
            }
        } catch (Throwable ignored) {
        }
        try {
            PendingIntent pi = unwrapPendingIntent(view.getTag());
            if (pi != null) return pi;
        } catch (Throwable ignored) {
        }
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

    private static PendingIntent unwrapPendingIntent(Object obj) {
        if (obj == null) return null;
        if (obj instanceof PendingIntent) return (PendingIntent) obj;
        try {
            for (Class<?> cls = obj.getClass(); cls != null && cls != Object.class; cls = cls.getSuperclass()) {
                for (Field f : cls.getDeclaredFields()) {
                    try {
                        f.setAccessible(true);
                        Object v = f.get(obj);
                        if (v instanceof PendingIntent) return (PendingIntent) v;
                    } catch (Throwable ignored) {
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static Context getOfficialPkgContext() {
        Context base;
        String pkg;
        synchronized (STATE_LOCK) {
            base = appContext;
            pkg = activeOfficialPkg;
        }
        if (base == null) return null;
        try {
            return base.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
        } catch (Throwable ignored) {
            return base;
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

    private static IconReferenceCache getOrBuildIconReferences(WidgetRemoteViewsSpec spec) {
        Context ctx;
        String pkg;
        synchronized (STATE_LOCK) {
            if (iconReferenceCache != null) return iconReferenceCache;
            ctx = appContext;
            pkg = activeOfficialPkg;
        }
        IconReferenceCache built = buildIconReferences(ctx, pkg, spec);
        synchronized (STATE_LOCK) {
            if (iconReferenceCache == null) iconReferenceCache = built;
            return iconReferenceCache;
        }
    }

    private static IconReferenceCache buildIconReferences(Context context, String pkg,
                                                          WidgetRemoteViewsSpec spec) {
        if (context == null || spec == null) return new IconReferenceCache(null, null, null);
        try {
            Context pkgCtx = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            Resources res = pkgCtx.getResources();
            int offBlackId = spec.dynamicLockOffBlackId != 0
                    ? spec.dynamicLockOffBlackId : WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_OFF_BLACK;
            int offWhiteId = spec.dynamicLockOffWhiteId != 0
                    ? spec.dynamicLockOffWhiteId : WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_OFF_WHITE;
            int onId = spec.dynamicLockOnId != 0
                    ? spec.dynamicLockOnId : WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_ON;
            int[] offBlack = rasterizeDrawableId(res, pkgCtx.getTheme(), offBlackId);
            int[] offWhite = rasterizeDrawableId(res, pkgCtx.getTheme(), offWhiteId);
            int[] on = rasterizeDrawableId(res, pkgCtx.getTheme(), onId);
            return new IconReferenceCache(offBlack, offWhite, on);
        } catch (Throwable ignored) {
            return new IconReferenceCache(null, null, null);
        }
    }

    private static int[] rasterizeDrawableId(Resources res, Resources.Theme theme, int id) {
        if (res == null || id == 0) return null;
        try {
            Drawable d = res.getDrawable(id, theme);
            return rasterizeDrawable(d);
        } catch (Throwable ignored) {
            return null;
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

    /** 仅作为 Android 10 以下老系统的补充兜底，不用于 Android 11+ 以避免隐藏 API 拦截。 */
    private static void scanLegacyRemoteViews(RemoteViews rv, Visit visit, WidgetRemoteViewsSpec spec) {
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
                PendingIntent pi = readPendingIntent(action);
                if (spec.isSettingView(viewId) && pi != null) {
                    visit.config = pi;
                } else if (spec.isAppAdditionView(viewId) && visit.config == null && pi != null) {
                    visit.config = pi;
                }
                if (pi != null) {
                    if (spec.isConfirmOkView(viewId)) visit.lockConfirm = pi;
                    if (spec.isFunStatusView(viewId) && lockHint != null) {
                        visit.lockClick = pi;
                        visit.lockHint = lockHint;
                    }
                }
                Object nested = readField(action, "mNestedViews");
                if (nested instanceof RemoteViews) scanLegacyRemoteViews((RemoteViews) nested, visit, spec);
            }
        } catch (Throwable ignored) {
        }
    }

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

    private static final class IconReferenceCache {
        final int[] offBlack;
        final int[] offWhite;
        final int[] on;

        IconReferenceCache(int[] offBlack, int[] offWhite, int[] on) {
            this.offBlack = offBlack;
            this.offWhite = offWhite;
            this.on = on;
        }

        boolean isUsable() {
            return on != null && (offBlack != null || offWhite != null);
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
        View configView;
        String lockHint;
    }
}
