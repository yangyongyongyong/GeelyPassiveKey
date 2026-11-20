package com.geely.galaxy.car.weight.appwidget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.geely.core.common.R2;
import com.geely.galaxy.car.R;
import com.geely.galaxy.car.weight.appwidget.state.WidgetUiStatus;
import com.geely.galaxy.car.weight.appwidget.stateview.MiddleInitStateView;
import com.geely.galaxy.car.weight.appwidget.stateview.MiddleNormalStateView;
import com.geely.galaxy.car.weight.appwidget.styleview.MiddleInitStyleView;
import com.geely.galaxy.car.weight.appwidget.styleview.MiddleNormalStyleView;
import com.jaeger.library.BuildConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WgProvider.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0014J \u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0014¨\u0006\f"}, d2 = {"Lcom/geely/galaxy/car/weight/appwidget/WgProviderMiddle;", "Lcom/geely/galaxy/car/weight/appwidget/WgBaseProvider;", "()V", "updateInitStatusAppWidget", BuildConfig.FLAVOR, "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "status", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetUiStatus;", "updateNormalStatusAppWidget", "app_release"}, k = 1, mv = {1, 8, 0}, xi = R2.attr.actionBarStyle)
public final class WgProviderMiddle extends WgBaseProvider {
    public WgProviderMiddle() {
        super(null);
    }

    @Override // com.geely.galaxy.car.weight.appwidget.WgBaseProvider
    protected void updateInitStatusAppWidget(Context context, Intent intent, WidgetUiStatus status) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(status, "status");
        Log.d(WgBaseProvider.TAG, "updateInitStatusAppWidget");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wg_middle_init);
        new MiddleInitStyleView(context).switchWidgetStyle(remoteViews);
        new MiddleInitStateView(context).handleIntent(remoteViews, intent, status);
    }

    @Override // com.geely.galaxy.car.weight.appwidget.WgBaseProvider
    protected void updateNormalStatusAppWidget(Context context, Intent intent, WidgetUiStatus status) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(status, "status");
        Log.d(WgBaseProvider.TAG, "updateNormalStatusAppWidget");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wg_middle_normal);
        new MiddleNormalStyleView(context).switchWidgetStyle(remoteViews);
        new MiddleNormalStateView(context).handleIntent(remoteViews, intent, status);
    }
}
