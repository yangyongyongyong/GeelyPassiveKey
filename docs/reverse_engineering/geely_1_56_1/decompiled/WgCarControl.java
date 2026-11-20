package com.geely.galaxy.car.weight.appwidget.control;

import android.widget.RemoteViews;
import com.geely.core.common.R2;
import com.geely.galaxy.car.R;
import com.geely.galaxy.car.vehicle.carcontrol.CarControlBusiness;
import com.geely.galaxy.car.vehicle.carcontrol.SourceType;
import com.geely.galaxy.car.weight.appwidget.state.WidgetFunType;
import com.jaeger.library.BuildConfig;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WgCarControl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bH\u0002¨\u0006\f"}, d2 = {"Lcom/geely/galaxy/car/weight/appwidget/control/WgCarControl;", BuildConfig.FLAVOR, "()V", "carControl", BuildConfig.FLAVOR, "remoteViews", "Landroid/widget/RemoteViews;", "carControlType", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetFunType;", "updateControlView", "cmdRequest", "Lkotlin/Function0;", "app_release"}, k = 1, mv = {1, 8, 0}, xi = R2.attr.actionBarStyle)
public final class WgCarControl {
    public static final WgCarControl INSTANCE = new WgCarControl();

    private WgCarControl() {
    }

    public final void carControl(RemoteViews remoteViews, final WidgetFunType carControlType) {
        Intrinsics.checkNotNullParameter(remoteViews, "remoteViews");
        Intrinsics.checkNotNullParameter(carControlType, "carControlType");
        updateControlView(remoteViews, new Function0<Unit>() { // from class: com.geely.galaxy.car.weight.appwidget.control.WgCarControl.carControl.1
            {
                super(0);
            }

            public /* bridge */ /* synthetic */ Object invoke() {
                m1076invoke();
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: collision with other method in class */
            public final void m1076invoke() {
                CarControlBusiness.toCarControl$default(CarControlBusiness.INSTANCE, carControlType.getBinFunType(), null, null, SourceType.Widget, 6, null);
            }
        });
    }

    private final void updateControlView(RemoteViews remoteViews, Function0<Unit> cmdRequest) {
        remoteViews.setViewVisibility(R.id.wg_middle_layout_confirm, 8);
        cmdRequest.invoke();
    }
}
