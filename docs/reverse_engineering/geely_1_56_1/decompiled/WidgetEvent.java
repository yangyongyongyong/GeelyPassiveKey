package com.geely.galaxy.car.weight.appwidget.state;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.geely.core.common.R2;
import com.geely.galaxy.car.weight.appwidget.WgProviderMiddle;
import com.geely.galaxy.car.weight.appwidget.WidgetLauncher;
import com.geely.galaxy.car.weight.appwidget.dialog.WgConfigSettingActivity;
import com.geely.swmodulemanager.base.SWBaseApplication;
import com.jaeger.library.BuildConfig;
import com.sensorsdata.analytics.android.sdk.aop.push.PushAutoTrackHelper;
import java.util.Map;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WidgetAction.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u001a\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J.\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0014\b\u0002\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0015H\u0002J.\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0014\b\u0002\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0015H\u0002J\u0006\u0010\u0017\u001a\u00020\bJ\b\u0010\u0018\u001a\u00020\u0019H\u0007J\u000e\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u0004\u0018\u00010\bJ\b\u0010\u001e\u001a\u0004\u0018\u00010\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/geely/galaxy/car/weight/appwidget/state/WidgetEvent;", BuildConfig.FLAVOR, "()V", "CONFIRM", BuildConfig.FLAVOR, "WIDGET_CLICK", "WIDGET_REFRESH_START", "buildFunClickIntent", "Landroid/app/PendingIntent;", "widgetFunType", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetFunType;", "buildFunConfirmCancelIntent", "buildFunConfirmIntent", "buildLaunchIntent", "requestCode", BuildConfig.FLAVOR, "routeType", "Lcom/geely/galaxy/car/weight/appwidget/WidgetLauncher$WgRouteType;", "buildPendingIntent", "actionName", "extraMap", BuildConfig.FLAVOR, "buildPendingIntentNew", "buildPullNewStatusIntent", "refreshData", BuildConfig.FLAVOR, "routeToConfigActivity", "isFromSmallWidget", BuildConfig.FLAVOR, "routeToMainActivity", "routeToVehicleListActivity", "app_release"}, k = 1, mv = {1, 8, 0}, xi = R2.attr.actionBarStyle)
public final class WidgetEvent {
    public static final String CONFIRM = "com.geely.galaxy.customer.widget.event.CONFIRM";
    public static final WidgetEvent INSTANCE = new WidgetEvent();
    public static final String WIDGET_CLICK = "com.geely.galaxy.customer.widget.event.WIDGET_CLICK";
    public static final String WIDGET_REFRESH_START = "com.geely.galaxy.customer.widget.event.WIDGET_REFRESH_START";

    private WidgetEvent() {
    }

    @Deprecated(message = "暂未使用")
    public final void refreshData() {
        try {
            Result.Companion companion = Result.Companion;
            WidgetEvent widgetEvent = this;
            Object systemService = SWBaseApplication.getInstance().getSystemService("alarm");
            AlarmManager alarmManager = systemService instanceof AlarmManager ? (AlarmManager) systemService : null;
            if (alarmManager == null) {
                return;
            }
            alarmManager.set(0, System.currentTimeMillis() + 100, buildPullNewStatusIntent());
            Result.constructor-impl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            Result.constructor-impl(ResultKt.createFailure(th));
        }
    }

    public final PendingIntent buildFunClickIntent(WidgetFunType widgetFunType) {
        Intrinsics.checkNotNullParameter(widgetFunType, "widgetFunType");
        return buildPendingIntent(WIDGET_CLICK, widgetFunType.ordinal() + 10000, MapsKt.mapOf(TuplesKt.to(WidgetFunType.BundleName, widgetFunType.name())));
    }

    public final PendingIntent buildPullNewStatusIntent() {
        return buildPendingIntent(WIDGET_CLICK, 11000, MapsKt.mapOf(TuplesKt.to(WidgetActionKt.WIDGET_EVENT_TYPE_KEY_NAME, WidgetActionKt.WIDGET_EVENT_TYPE_NAME)));
    }

    public final PendingIntent buildFunConfirmIntent(WidgetFunType widgetFunType) {
        Intrinsics.checkNotNullParameter(widgetFunType, "widgetFunType");
        return buildPendingIntent(CONFIRM, 20001, MapsKt.mapOf(new Pair[]{TuplesKt.to(WidgetConfirmType.BundleName, WidgetConfirmType.CONFIRM.getBundleValue()), TuplesKt.to(WidgetFunType.BundleName, widgetFunType.name())}));
    }

    public final PendingIntent buildFunConfirmCancelIntent(WidgetFunType widgetFunType) {
        Intrinsics.checkNotNullParameter(widgetFunType, "widgetFunType");
        return buildPendingIntent(CONFIRM, 20002, MapsKt.mapOf(new Pair[]{TuplesKt.to(WidgetConfirmType.BundleName, WidgetConfirmType.CANCEL.getBundleValue()), TuplesKt.to(WidgetFunType.BundleName, widgetFunType.name())}));
    }

    public final PendingIntent routeToMainActivity() {
        return buildLaunchIntent(40000, WidgetLauncher.WgRouteType.MAIN_ACTIVITY);
    }

    public final PendingIntent routeToVehicleListActivity() {
        return buildLaunchIntent(40001, WidgetLauncher.WgRouteType.CAR_LIST_ACTIVITY);
    }

    public final PendingIntent routeToConfigActivity(boolean isFromSmallWidget) {
        Context sWBaseApplication = SWBaseApplication.getInstance();
        Intent intent = new Intent(sWBaseApplication, (Class<?>) WgConfigSettingActivity.class);
        intent.putExtra("isFromSmallWidget", isFromSmallWidget);
        int i = isFromSmallWidget ? 1002 : 1003;
        PushAutoTrackHelper.hookIntentGetActivity(sWBaseApplication, i, intent, 201326592);
        PendingIntent activity = PendingIntent.getActivity(sWBaseApplication, i, intent, 201326592);
        PushAutoTrackHelper.hookPendingIntentGetActivity(activity, sWBaseApplication, i, intent, 201326592);
        Intrinsics.checkNotNullExpressionValue(activity, "getActivity(context, if …ingIntent.FLAG_IMMUTABLE)");
        return activity;
    }

    private final PendingIntent buildLaunchIntent(int requestCode, WidgetLauncher.WgRouteType routeType) {
        Context sWBaseApplication = SWBaseApplication.getInstance();
        Intent launchIntentForPackage = SWBaseApplication.getInstance().getPackageManager().getLaunchIntentForPackage(sWBaseApplication.getPackageName());
        if (launchIntentForPackage == null) {
            return null;
        }
        launchIntentForPackage.putExtra(WidgetLauncher.WgRouteType.BundleName, routeType.name());
        launchIntentForPackage.putExtra(WidgetActionKt.WIDGET_LAUNCH_FLAG, true);
        launchIntentForPackage.addFlags(268435456);
        Context context = sWBaseApplication;
        PushAutoTrackHelper.hookIntentGetActivity(context, requestCode, launchIntentForPackage, 201326592);
        PendingIntent activity = PendingIntent.getActivity(context, requestCode, launchIntentForPackage, 201326592);
        PushAutoTrackHelper.hookPendingIntentGetActivity(activity, context, requestCode, launchIntentForPackage, 201326592);
        return activity;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ PendingIntent buildPendingIntent$default(WidgetEvent widgetEvent, String str, int i, Map map, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        return widgetEvent.buildPendingIntent(str, i, map);
    }

    private final PendingIntent buildPendingIntent(String actionName, int requestCode, Map<String, String> extraMap) {
        return buildPendingIntentNew(actionName, requestCode, extraMap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ PendingIntent buildPendingIntentNew$default(WidgetEvent widgetEvent, String str, int i, Map map, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        return widgetEvent.buildPendingIntentNew(str, i, map);
    }

    private final PendingIntent buildPendingIntentNew(String actionName, int requestCode, Map<String, String> extraMap) {
        Intent intent = new Intent(SWBaseApplication.getInstance(), (Class<?>) WgProviderMiddle.class);
        intent.setAction(actionName);
        intent.putExtra(WidgetActionKt.WIDGET_EVENT_FLAG, true);
        for (Map.Entry<String, String> entry : extraMap.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        Context sWBaseApplication = SWBaseApplication.getInstance();
        PushAutoTrackHelper.hookIntentGetBroadcast(sWBaseApplication, requestCode, intent, 201326592);
        PendingIntent broadcast = PendingIntent.getBroadcast(sWBaseApplication, requestCode, intent, 201326592);
        PushAutoTrackHelper.hookPendingIntentGetBroadcast(broadcast, sWBaseApplication, requestCode, intent, 201326592);
        Intrinsics.checkNotNullExpressionValue(broadcast, "Intent(SWBaseApplication…FLAG_IMMUTABLE)\n        }");
        return broadcast;
    }
}
