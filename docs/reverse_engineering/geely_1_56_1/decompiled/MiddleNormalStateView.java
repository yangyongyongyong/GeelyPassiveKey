package com.geely.galaxy.car.weight.appwidget.stateview;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus;
import com.geely.cockpit.services.controlservice.bean.bizextend.PlatformVehicleGeneralBizExtentKt;
import com.geely.cockpit.services.controlservice.dataprovider.DataProvider;
import com.geely.cockpit.services.controlservice.service.FunType;
import com.geely.core.common.R2;
import com.geely.galaxy.car.R;
import com.geely.galaxy.car.weight.appwidget.WgProviderMiddle;
import com.geely.galaxy.car.weight.appwidget.WgTools;
import com.geely.galaxy.car.weight.appwidget.WidgetStatusManager;
import com.geely.galaxy.car.weight.appwidget.control.WgCarControl;
import com.geely.galaxy.car.weight.appwidget.state.WidgetAction;
import com.geely.galaxy.car.weight.appwidget.state.WidgetConfirmType;
import com.geely.galaxy.car.weight.appwidget.state.WidgetEvent;
import com.geely.galaxy.car.weight.appwidget.state.WidgetFunType;
import com.geely.galaxy.car.weight.appwidget.state.WidgetStyle;
import com.geely.galaxy.car.weight.appwidget.state.WidgetUiStatus;
import com.geely.galaxy.car.weight.appwidget.stateview.part.WgControlView;
import com.geely.galaxy.car.weight.appwidget.stateview.part.WgEnergyBarView;
import com.geely.galaxy.car.weight.appwidget.stateview.part.WgToastView;
import com.jaeger.library.BuildConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MiddleStateView.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\t\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0002J*\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0002J\u0010\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J(\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J(\u0010\u0017\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u0014\u0010\u0005\u001a\u00020\u0006X\u0094D¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u001e"}, d2 = {"Lcom/geely/galaxy/car/weight/appwidget/stateview/MiddleNormalStateView;", "Lcom/geely/galaxy/car/weight/appwidget/stateview/AbstractStateView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "dynamicCarModel", BuildConfig.FLAVOR, "getDynamicCarModel", "()Z", "onClickAction", BuildConfig.FLAVOR, "remoteViews", "Landroid/widget/RemoteViews;", "clickFunType", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetFunType;", "onConfirmClickAction", "confirmType", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetConfirmType;", "setupCommonClickListener", "transToConfirmStatus", "carControlType", "confirmMsg", BuildConfig.FLAVOR, "updateView", "status", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetUiStatus;", "intent", "Landroid/content/Intent;", "style", "Lcom/geely/galaxy/car/weight/appwidget/state/WidgetStyle;", "app_release"}, k = 1, mv = {1, 8, 0}, xi = R2.attr.actionBarStyle)
public final class MiddleNormalStateView extends AbstractStateView {
    private final boolean dynamicCarModel;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MiddleNormalStateView(Context context) {
        super(context, WgProviderMiddle.class, null);
        Intrinsics.checkNotNullParameter(context, "context");
        this.dynamicCarModel = true;
    }

    @Override // com.geely.galaxy.car.weight.appwidget.stateview.AbstractStateView
    protected boolean getDynamicCarModel() {
        return this.dynamicCarModel;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.geely.galaxy.car.weight.appwidget.stateview.AbstractStateView
    public void updateView(RemoteViews remoteViews, WidgetUiStatus status, Intent intent, WidgetStyle style) {
        Intrinsics.checkNotNullParameter(remoteViews, "remoteViews");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(style, "style");
        super.updateView(remoteViews, status, intent, style);
        String action = intent.getAction();
        if (action != null) {
            switch (action.hashCode()) {
                case -563206839:
                    if (action.equals(WidgetAction.WIDGET_TOAST_SHOW)) {
                        WgToastView.INSTANCE.showWidgetToast(getContext(), remoteViews, intent);
                    }
                    break;
                case -294200052:
                    if (action.equals(WidgetAction.WIDGET_TOAST_CLOSE)) {
                        WgToastView.INSTANCE.tryCloseWidgetToast();
                    }
                    break;
                case 19869691:
                    if (action.equals(WidgetEvent.CONFIRM)) {
                        onConfirmClickAction(getContext(), remoteViews, WidgetConfirmType.INSTANCE.toConfirmType(intent), WidgetFunType.INSTANCE.toClickFunType(intent));
                    }
                    break;
                case 1459887922:
                    if (action.equals(WidgetEvent.WIDGET_CLICK)) {
                        onClickAction(getContext(), remoteViews, WidgetFunType.INSTANCE.toClickFunType(intent));
                    }
                    break;
            }
        }
        WgEnergyBarView.INSTANCE.updateEnergyBar(remoteViews, status, style);
        WgToastView.INSTANCE.updateToastView(remoteViews, status);
        WgControlView.INSTANCE.updateControlPanelView(remoteViews, status, getWidgetStyle());
    }

    @Override // com.geely.galaxy.car.weight.appwidget.stateview.AbstractStateView
    public void setupCommonClickListener(RemoteViews remoteViews) {
        Intrinsics.checkNotNullParameter(remoteViews, "remoteViews");
        super.setupCommonClickListener(remoteViews);
        remoteViews.setOnClickPendingIntent(R.id.wg_btn_setting, WidgetEvent.INSTANCE.routeToConfigActivity(false));
    }

    private final void onClickAction(Context context, RemoteViews remoteViews, WidgetFunType clickFunType) {
        WgTools.INSTANCE.log(getTAG(), "Wg_Middle performClick clickFunType:" + clickFunType);
        if (clickFunType == null) {
            return;
        }
        WidgetUiStatus.WgFunStatus funStatus = clickFunType.getFunStatus(WidgetStatusManager.INSTANCE.getLastStatus().getWgControlStatus());
        String confirmMessage = clickFunType.getConfirmMessage(!(funStatus != null ? funStatus.getOnStatus() : false));
        if (clickFunType.getBinFunType() == FunType.AIR_PURIFICATION) {
            if (PlatformVehicleGeneralBizExtentKt.isClimateOn((PlatformVehicleStatus) DataProvider.INSTANCE.getVehicleStatus().getValue())) {
                WidgetAction widgetAction = WidgetAction.INSTANCE;
                String string = context.getString(R.string.car_wg_purification_climate_on);
                Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.stri…_purification_climate_on)");
                widgetAction.notifyShowToast(string);
                return;
            }
            if (PlatformVehicleGeneralBizExtentKt.isDefrostOn((PlatformVehicleStatus) DataProvider.INSTANCE.getVehicleStatus().getValue())) {
                WidgetAction widgetAction2 = WidgetAction.INSTANCE;
                String string2 = context.getString(R.string.car_wg_purification_defrost_on);
                Intrinsics.checkNotNullExpressionValue(string2, "context.getString(R.stri…_purification_defrost_on)");
                widgetAction2.notifyShowToast(string2);
                return;
            }
        }
        if (getNeedReconfirm()) {
            if (confirmMessage.length() > 0) {
                transToConfirmStatus(context, remoteViews, clickFunType, confirmMessage);
                return;
            }
        }
        WgCarControl.INSTANCE.carControl(remoteViews, clickFunType);
    }

    private final void onConfirmClickAction(Context context, RemoteViews remoteViews, WidgetConfirmType confirmType, WidgetFunType clickFunType) {
        WgTools.INSTANCE.log(getTAG(), "Wg_Middle onReceive== WIDGET_CONFIRM_CLICK:confirmType:" + confirmType + ", funType:" + clickFunType);
        if (clickFunType != null && confirmType == WidgetConfirmType.CONFIRM) {
            WgCarControl.INSTANCE.carControl(remoteViews, clickFunType);
        }
        remoteViews.setViewVisibility(R.id.wg_middle_layout_confirm, 8);
    }

    private final void transToConfirmStatus(Context context, RemoteViews remoteViews, WidgetFunType carControlType, String confirmMsg) {
        remoteViews.setTextViewText(R.id.wg_middle_confirm_title, confirmMsg);
        remoteViews.setViewVisibility(R.id.wg_middle_layout_confirm, 0);
        remoteViews.setOnClickPendingIntent(R.id.wg_middle_confirm_btn_ok, WidgetEvent.INSTANCE.buildFunConfirmIntent(carControlType));
        remoteViews.setOnClickPendingIntent(R.id.wg_middle_confirm_btn_cancel, WidgetEvent.INSTANCE.buildFunConfirmCancelIntent(carControlType));
    }
}
