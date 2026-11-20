package com.geely.galaxy.car.init;

/* JADX INFO: compiled from: SWCarModuleMainInitializer.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0002J\u0018\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016¨\u0006\u0013"}, d2 = {"Lcom/geely/galaxy/car/init/SWCarModuleMainInitializer;", "Lcom/geely/swmodulemanager/module/IModule;", "()V", "getExposedServices", com.jaeger.library.BuildConfig.FLAVOR, "Lcom/geely/swmodulemanager/service/ServiceDescriptor;", "getName", com.jaeger.library.BuildConfig.FLAVOR, "getPriority", com.jaeger.library.BuildConfig.FLAVOR, "getStage", "Lcom/geely/swmodulemanager/lifecycle/Stage;", "initUspGateWay", com.jaeger.library.BuildConfig.FLAVOR, "onStage", "stage", "stageContext", "Lcom/geely/swmodulemanager/lifecycle/StageContext;", "Companion", "app_release"}, k = 1, mv = {1, 8, 0}, xi = com.geely.core.common.R2.attr.actionBarStyle)
public final class SWCarModuleMainInitializer implements com.geely.swmodulemanager.module.IModule {
    private static boolean goBackground;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.car.init.SWCarModuleMainInitializer.Companion INSTANCE = new com.geely.galaxy.car.init.SWCarModuleMainInitializer.Companion(null);
    private static final java.lang.String LOG_TAG = "SWCarModuleMainInitializer";
    private static final java.util.concurrent.atomic.AtomicBoolean initialized = new java.util.concurrent.atomic.AtomicBoolean(false);

    public java.util.List<com.geely.swmodulemanager.service.ServiceDescriptor> getExposedServices() {
        return null;
    }

    public int getPriority() {
        return com.geely.core.common.R2.attr.autoSizeStepGranularity;
    }

    /* JADX INFO: compiled from: SWCarModuleMainInitializer.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/geely/galaxy/car/init/SWCarModuleMainInitializer$Companion;", com.jaeger.library.BuildConfig.FLAVOR, "()V", "LOG_TAG", com.jaeger.library.BuildConfig.FLAVOR, "kotlin.jvm.PlatformType", "getLOG_TAG", "()Ljava/lang/String;", "goBackground", com.jaeger.library.BuildConfig.FLAVOR, "getGoBackground", "()Z", "setGoBackground", "(Z)V", "initialized", "Ljava/util/concurrent/atomic/AtomicBoolean;", "app_release"}, k = 1, mv = {1, 8, 0}, xi = com.geely.core.common.R2.attr.actionBarStyle)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final java.lang.String getLOG_TAG() {
            return com.geely.galaxy.car.init.SWCarModuleMainInitializer.LOG_TAG;
        }

        public final boolean getGoBackground() {
            return com.geely.galaxy.car.init.SWCarModuleMainInitializer.goBackground;
        }

        public final void setGoBackground(boolean z) {
            com.geely.galaxy.car.init.SWCarModuleMainInitializer.goBackground = z;
        }
    }

    public java.lang.String getName() {
        java.lang.String str = LOG_TAG;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "LOG_TAG");
        return str;
    }

    public com.geely.swmodulemanager.lifecycle.Stage getStage() {
        return com.geely.swmodulemanager.lifecycle.Stage.MAIN_THREAD;
    }

    public void onStage(com.geely.swmodulemanager.lifecycle.Stage stage, com.geely.swmodulemanager.lifecycle.StageContext stageContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stage, "stage");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stageContext, "stageContext");
        com.geely.galaxy.car.vehicle.utils.CarOrderUtil.Companion companion = com.geely.galaxy.car.vehicle.utils.CarOrderUtil.INSTANCE;
        com.geely.swmodulemanager.base.SWBaseApplication context = stageContext.getContext();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(context, "stageContext.context");
        if (companion.isMainProcess((android.content.Context) context)) {
            com.geely.galaxy.carcontrol.core.utils.Ge_LogUtils.d("SWCarModuleMainInitializer", "SWCarModuleMainInitializer:MainProcessStart");
            android.util.Log.d("galaxyAANewWidget[Pid: " + android.os.Process.myPid() + "]", "onStage: SWCarModuleMainInitializer-->" + com.geely.galaxy.car.utils.KaptExpendKt.checkUserIsLogin());
            com.geely.galaxy.car.sdkadapter.CarControlSDKInit.INSTANCE.initLogAdapter();
            com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
            java.lang.String str = LOG_TAG;
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "LOG_TAG");
            cloudLogUtils.releaseLog(str, (24 & 2) != 0 ? null : "onStage:start", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : null);
            if (com.geely.galaxy.car.utils.DeviceUtil.isVivo() || com.geely.icc.common.business.CommonApiImpl.getAliveInstance().isMeiZuPhone() || com.geely.icc.common.business.CommonApiImpl.getAliveInstance().isHonorPhone()) {
                com.geely.galaxy.car.utils.Ge_BlueKeepAliveUtil.INSTANCE.getInstance().beforeInitDo();
            }
            initUspGateWay();
            boolean zCheckPrivacyAgree = com.geely.galaxy.car.utils.KaptExpendKt.checkPrivacyAgree();
            com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils2 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "LOG_TAG");
            cloudLogUtils2.releaseLog(str, (24 & 2) != 0 ? null : "checkPrivacyAgree", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : java.lang.Boolean.valueOf(zCheckPrivacyAgree));
            if (zCheckPrivacyAgree) {
                try {
                    com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils3 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "LOG_TAG");
                    cloudLogUtils3.releaseLog(str, (24 & 2) != 0 ? null : "initAppConfig", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : null);
                    com.geely.icc.common.business.CommonApiImpl commonApiImpl = com.geely.icc.common.business.CommonApiImpl.INSTANCE;
                    android.content.Context context2 = stageContext.getContext();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(context2, "stageContext.context");
                    com.geely.icc.common.business.config.ComConfig.Builder builder = new com.geely.icc.common.business.config.ComConfig.Builder();
                    java.lang.String excarBleEnv = com.geely.galaxy.car.init.CarInnerEnvApiConfig.getExcarBleEnv();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(excarBleEnv, "getExcarBleEnv()");
                    commonApiImpl.initAppConfig(context2, builder.setEnv(excarBleEnv).build(), com.geely.galaxy.car.utils.LogConfigInfo.INSTANCE);
                } catch (java.lang.Exception e) {
                    com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils4 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
                    java.lang.String str2 = LOG_TAG;
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str2, "LOG_TAG");
                    cloudLogUtils4.releaseLog(str2, (24 & 2) != 0 ? null : "initAppConfig:Exception", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : e);
                }
            }
            com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils5 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
            java.lang.String str3 = LOG_TAG;
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str3, "LOG_TAG");
            cloudLogUtils5.releaseLog(str3, (24 & 2) != 0 ? null : "onStage:end", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : null);
            try {
                kotlin.Result.Companion companion2 = kotlin.Result.Companion;
                androidx.lifecycle.ProcessLifecycleOwner.Companion.get().getLifecycle().addObserver(new androidx.lifecycle.DefaultLifecycleObserver() { // from class: com.geely.galaxy.car.init.SWCarModuleMainInitializer$onStage$1$1
                    public void onStart(androidx.lifecycle.LifecycleOwner owner) {
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
                        super.onStart(owner);
                        com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils6 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
                        java.lang.String log_tag = com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.getLOG_TAG();
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(log_tag, "LOG_TAG");
                        cloudLogUtils6.releaseLog(log_tag, (24 & 2) != 0 ? null : "onStart", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : null);
                        if (com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.getGoBackground() && com.geely.cockpit.services.controlservice.runtime.CurVehicle.get() != null && com.geely.cockpit.services.controlservice.runtime.CurVehicle.getCapabilityService().getSupportBluetoothKey()) {
                            com.geely.galaxy.car.business.CarOrderBusiness.carControlBind$default(com.geely.galaxy.car.business.CarOrderBusiness.INSTANCE, null, 1, null);
                            com.geely.galaxy.car.business.CarBluetoothService.INSTANCE.hotStartAutoConnectCheck();
                        }
                        com.geely.galaxy.car.business.CarOrderBusiness.INSTANCE.setMainFront(true);
                        com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.setGoBackground(false);
                    }

                    public void onStop(androidx.lifecycle.LifecycleOwner owner) {
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
                        super.onStop(owner);
                        com.geely.galaxy.car.utils.CloudLogUtils cloudLogUtils6 = com.geely.galaxy.car.utils.CloudLogUtils.INSTANCE;
                        java.lang.String log_tag = com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.getLOG_TAG();
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(log_tag, "LOG_TAG");
                        cloudLogUtils6.releaseLog(log_tag, (24 & 2) != 0 ? null : "onStop", (24 & 4) != 0 ? null : null, (24 & 8) != 0 ? null : null, (24 & 16) != 0 ? null : null);
                        if (com.geely.cockpit.services.controlservice.runtime.CurVehicle.get() != null) {
                            if (com.geely.cockpit.services.controlservice.runtime.CurVehicle.getCapabilityService().getSupportBluetoothKey()) {
                                com.geely.galaxy.car.business.CarOrderBusiness.INSTANCE.setAutoConnect(true);
                                android.util.Log.i(com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.getLOG_TAG(), "--->setAutoConnect(true)");
                                com.geely.galaxy.car.business.CarBluetoothService.INSTANCE.notifyWalletBind();
                            }
                            com.geely.cockpit.services.controlservice.runtime.CurVehicle.INSTANCE.setGoBackground(true);
                        }
                        com.geely.galaxy.car.business.CarOrderBusiness.INSTANCE.setMainFront(false);
                        com.geely.galaxy.car.init.SWCarModuleMainInitializer.INSTANCE.setGoBackground(true);
                    }
                });
                kotlin.Result.constructor-impl(kotlin.Unit.INSTANCE);
            } catch (java.lang.Throwable th) {
                kotlin.Result.Companion companion3 = kotlin.Result.Companion;
                kotlin.Result.constructor-impl(kotlin.ResultKt.createFailure(th));
            }
        }
    }

    private final void initUspGateWay() {
        java.lang.String uspGateway = com.geely.galaxy.car.utils.LDPProtectUtils.getUspGateway();
        java.lang.String uspGatewayNew = com.geely.galaxy.car.utils.LDPProtectUtils.getUspGatewayNew();
        java.lang.String baseUrlOta = com.geely.galaxy.car.init.ParseEnvConfigUtils.INSTANCE.getBaseUrlOta();
        java.lang.String str = com.jaeger.library.BuildConfig.FLAVOR;
        if (baseUrlOta == null) {
            baseUrlOta = com.jaeger.library.BuildConfig.FLAVOR;
        }
        java.lang.String baseUrlOtaNew = com.geely.galaxy.car.init.ParseEnvConfigUtils.INSTANCE.getBaseUrlOtaNew();
        if (baseUrlOtaNew != null) {
            str = baseUrlOtaNew;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(uspGateway, "uspGateway");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(uspGatewayNew, "uspGatewayNew");
        com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl.setupUrl(baseUrlOta, str, uspGateway, uspGatewayNew);
        com.geely.galaxy.network.HttpExecutor.Companion.initGateWay(com.geely.galaxy.car.utils.LDPProtectUtils.getUspAppid(), com.geely.galaxy.car.utils.LDPProtectUtils.getUspAppSecret(), uspGateway);
        com.geely.galaxy.network.HttpExecutor.Companion.initGateWay(com.geely.galaxy.car.utils.LDPProtectUtils.getUspAppidNew(), com.geely.galaxy.car.utils.LDPProtectUtils.getUspAppSecretNew(), uspGatewayNew);
    }
}
