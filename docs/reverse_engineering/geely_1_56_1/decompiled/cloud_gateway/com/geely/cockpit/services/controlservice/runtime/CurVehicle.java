package com.geely.cockpit.services.controlservice.runtime;

/* JADX INFO: compiled from: CurVehicle.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\n\u0010\u001d\u001a\u0004\u0018\u00010\fH\u0007J\b\u0010\u001e\u001a\u00020\u001fH\u0007J'\u0010 \u001a\u0004\u0018\u0001H!\"\b\b\u0000\u0010!*\u00020\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0007¢\u0006\u0002\u0010%J\b\u0010&\u001a\u00020\u0016H\u0007J\b\u0010'\u001a\u00020(H\u0007J\b\u0010)\u001a\u00020*H\u0007J\b\u0010+\u001a\u00020\u0016H\u0007J\b\u0010,\u001a\u00020\u0016H\u0007J\u0006\u0010-\u001a\u00020\u000fJ\u0006\u0010.\u001a\u00020\u000fJ\u0012\u0010/\u001a\u00020\u000f2\n\b\u0002\u00100\u001a\u0004\u0018\u00010\u0016J\u0006\u00101\u001a\u00020\u000fJ\b\u00102\u001a\u00020\u000fH\u0007J\u0006\u00103\u001a\u00020\u000fJ\u0006\u00104\u001a\u00020\u000fJ\u0012\u00105\u001a\u00020\u000f2\n\b\u0002\u00100\u001a\u0004\u0018\u00010\u0016J\b\u00106\u001a\u00020\u000fH\u0007J\b\u00107\u001a\u00020\u000fH\u0007J\b\u00108\u001a\u000209H\u0007J\u0012\u0010:\u001a\u0002092\b\u0010;\u001a\u0004\u0018\u00010\"H\u0007J\u0012\u0010<\u001a\u0002092\b\u0010=\u001a\u0004\u0018\u00010>H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R&\u0010\b\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00048\u0006@BX\u0087\u000e¢\u0006\u000e\n\u0000\u0012\u0004\b\t\u0010\u0002\u001a\u0004\b\n\u0010\u0006R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f8\u0002@\u0002X\u0083\u000e¢\u0006\b\n\u0000\u0012\u0004\b\r\u0010\u0002R\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R2\u0010\u0014\u001a&\u0012\f\u0012\n \u0017*\u0004\u0018\u00010\u00160\u0016 \u0017*\u0012\u0012\f\u0012\n \u0017*\u0004\u0018\u00010\u00160\u0016\u0018\u00010\u00180\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0019\u001a\u00020\u001a8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001c¨\u0006?"}, d2 = {"Lcom/geely/cockpit/services/controlservice/runtime/CurVehicle;", "", "()V", "DEFAULT_CAPABILITY_SERVICE", "Lcom/geely/galaxy/carcontrol/core/vehicle/usp/capability/CapabilityService;", "getDEFAULT_CAPABILITY_SERVICE", "()Lcom/geely/galaxy/carcontrol/core/vehicle/usp/capability/CapabilityService;", "<set-?>", "capabilityService", "getCapabilityService$annotations", "getCapabilityService", "curPlatformVehicle", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicle;", "getCurPlatformVehicle$annotations", "goBackground", "", "getGoBackground", "()Z", "setGoBackground", "(Z)V", "levcCarList", "", "", "kotlin.jvm.PlatformType", "", "vehicleStatus", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "getVehicleStatus", "()Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "get", "getArea", "Lcom/baselinelibrary/enums/AREAType;", "getBizVehicle", "T", "Lcom/geely/cockpit/services/controlservice/interfaces/IPlatformVehicle;", "type", "Ljava/lang/Class;", "(Ljava/lang/Class;)Lcom/geely/cockpit/services/controlservice/interfaces/IPlatformVehicle;", "getCarUserId", "getPlatformType", "Lcom/geely/cockpit/services/controlservice/bean/PlatformType;", "getPlatformVersion", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVersion;", "getTermId", "getVin", "isDefaultCapService", "isE22H", "isE22HCode", "seriesCodeVs", "isLEVC", "isNotV2Car", "isOwner", "isPanda", "isPandaCode", "isV1Car", "isV2Car", "reset", "", "setup", "newVehicle", "setupCapabilityService", "carCapability", "Lcom/geely/snc/basevehicle/bean/GLCapabilityBean;", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class CurVehicle {
    private static final com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService DEFAULT_CAPABILITY_SERVICE;
    private static volatile com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService capabilityService;
    private static volatile com.geely.cockpit.services.controlservice.bean.PlatformVehicle curPlatformVehicle;
    private static boolean goBackground;
    public static final com.geely.cockpit.services.controlservice.runtime.CurVehicle INSTANCE = new com.geely.cockpit.services.controlservice.runtime.CurVehicle();
    private static final java.util.List<java.lang.String> levcCarList = java.util.Arrays.asList("XE08-TEST", "XE08");

    /* JADX INFO: compiled from: CurVehicle.kt */
    @kotlin.Metadata(k = 3, mv = {1, 8, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.geely.cockpit.services.controlservice.bean.PlatformType.values().length];
            try {
                iArr[com.geely.cockpit.services.controlservice.bean.PlatformType.USP.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError unused) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @kotlin.jvm.JvmStatic
    public static /* synthetic */ void getCapabilityService$annotations() {
    }

    @kotlin.jvm.JvmStatic
    private static /* synthetic */ void getCurPlatformVehicle$annotations() {
    }

    private CurVehicle() {
    }

    static {
        com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService capabilityService2 = new com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 0, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, (java.lang.Boolean) null, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, (com.geely.cockpit.services.controlservice.capability.model.EnvCapability) null, (com.geely.cockpit.services.controlservice.capability.model.EnvCombineCapability) null, (com.geely.cockpit.services.controlservice.capability.model.FragranceCapability) null, (com.geely.cockpit.services.controlservice.capability.model.SeatsModeCapability) null, (com.geely.cockpit.services.controlservice.capability.model.ChargingCapCapability) null, false, false, false, false, (java.lang.Boolean) null, (java.lang.Boolean) null, (java.lang.Boolean) null, (java.lang.Boolean) null, false, (com.geely.galaxy.carcontrol.core.vehicle.usp.capability.HoodCmdType) null, false, false, -1, -1, -1, 65535, (kotlin.jvm.internal.DefaultConstructorMarker) null);
        DEFAULT_CAPABILITY_SERVICE = capabilityService2;
        capabilityService = capabilityService2;
    }

    public final com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService getDEFAULT_CAPABILITY_SERVICE() {
        return DEFAULT_CAPABILITY_SERVICE;
    }

    public final boolean getGoBackground() {
        return goBackground;
    }

    public final void setGoBackground(boolean z) {
        goBackground = z;
    }

    public final com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus getVehicleStatus() {
        return com.geely.cockpit.services.controlservice.dataprovider.DataProvider.INSTANCE.getVehicleStatus().getValue();
    }

    public static final com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService getCapabilityService() {
        return capabilityService;
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.cockpit.services.controlservice.bean.PlatformVehicle get() {
        return curPlatformVehicle;
    }

    @kotlin.jvm.JvmStatic
    public static final <T extends com.geely.cockpit.services.controlservice.interfaces.IPlatformVehicle> T getBizVehicle(java.lang.Class<T> type) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(type, "type");
        return (T) com.geely.cockpit.services.controlservice.bean.PlatformVehicleKt.toBizVehicle(curPlatformVehicle, type);
    }

    @kotlin.jvm.JvmStatic
    public static final void setup(com.geely.cockpit.services.controlservice.interfaces.IPlatformVehicle newVehicle) {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = newVehicle != null ? com.geely.cockpit.services.controlservice.bean.PlatformVehicleKt.toPlatformVehicle(newVehicle) : null;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle2 = curPlatformVehicle;
        com.geely.cockpit.services.controlservice.CarControlLogger.INSTANCE.i("setup", java.lang.String.valueOf(platformVehicle));
        if (kotlin.jvm.internal.Intrinsics.areEqual(platformVehicle, platformVehicle2)) {
            return;
        }
        com.geely.galaxy.carcontrol.core.utils.Ge_LogUtils.d("galaxyNewSdk[" + java.lang.Thread.currentThread() + "]", "setup: " + platformVehicle);
        curPlatformVehicle = platformVehicle;
        if (kotlin.text.StringsKt.isBlank(getTermId()) && curPlatformVehicle != null) {
            java.lang.String temId = com.geely.cockpit.services.controlservice.cache.CarControlCache.INSTANCE.getTemId(getVin());
            com.geely.galaxy.carcontrol.core.utils.Ge_LogUtils.d("galaxyNewSdk[" + java.lang.Thread.currentThread() + "]", "getVehicleDetailsCache: temId: " + temId);
            com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle3 = curPlatformVehicle;
            curPlatformVehicle = platformVehicle3 != null ? platformVehicle3.copy((8187 & 1) != 0 ? platformVehicle3.vin : null, (8187 & 2) != 0 ? platformVehicle3.defaultVehicle : false, (8187 & 4) != 0 ? platformVehicle3.temId : temId, (8187 & 8) != 0 ? platformVehicle3.seriesCodeVs : null, (8187 & 16) != 0 ? platformVehicle3.seriesName : null, (8187 & 32) != 0 ? platformVehicle3.modelCode : null, (8187 & 64) != 0 ? platformVehicle3.modelName : null, (8187 & 128) != 0 ? platformVehicle3.carName : null, (8187 & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? platformVehicle3.areaType : null, (8187 & 512) != 0 ? platformVehicle3.platformVersion : null, (8187 & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? platformVehicle3.platformType : null, (8187 & 2048) != 0 ? platformVehicle3.isOwner : false, (8187 & 4096) != 0 ? platformVehicle3.bizVehicleJson : null) : null;
        }
        com.geely.cockpit.services.controlservice.cache.CarControlCache.INSTANCE.getDefaultCar().save(platformVehicle);
        setupCapabilityService(com.geely.snc.basevehicle.GLVehicleInfoService.INSTANCE.getInstance().getCapabilityFromCache());
        if (kotlin.jvm.internal.Intrinsics.areEqual(platformVehicle != null ? platformVehicle.getVin() : null, platformVehicle2 != null ? platformVehicle2.getVin() : null)) {
            return;
        }
        com.geely.cockpit.services.controlservice.dataprovider.DataProvider.INSTANCE.resetAndStop();
    }

    @kotlin.jvm.JvmStatic
    public static final void reset() {
        com.geely.galaxy.carcontrol.core.utils.Ge_LogUtils.d("galaxyNewSdk[" + java.lang.Thread.currentThread() + "]", "CurVehicle::reset: ");
        curPlatformVehicle = null;
        capabilityService = DEFAULT_CAPABILITY_SERVICE;
        com.geely.cockpit.services.controlservice.dataprovider.DataProvider.INSTANCE.resetAndStop();
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getCarUserId() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        com.geely.cockpit.services.controlservice.bean.PlatformType platformType = platformVehicle != null ? platformVehicle.getPlatformType() : null;
        if ((platformType == null ? -1 : com.geely.cockpit.services.controlservice.runtime.CurVehicle.WhenMappings.$EnumSwitchMapping$0[platformType.ordinal()]) == 1) {
            return com.geely.cockpit.services.controlservice.runtime.CurUserInfo.getUserId();
        }
        return com.geely.cockpit.services.controlservice.runtime.CurUserInfo.getIovUserId();
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getVin() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        java.lang.String vin = platformVehicle != null ? platformVehicle.getVin() : null;
        return vin == null ? "" : vin;
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getTermId() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        java.lang.String temId = platformVehicle != null ? platformVehicle.getTemId() : null;
        java.lang.String str = temId;
        if (!(str == null || kotlin.text.StringsKt.isBlank(str))) {
            return temId;
        }
        java.lang.String temId2 = com.geely.cockpit.services.controlservice.cache.CarControlCache.INSTANCE.getTemId(getVin());
        java.lang.String str2 = temId2;
        return !(str2 == null || kotlin.text.StringsKt.isBlank(str2)) ? temId2 : "";
    }

    @kotlin.jvm.JvmStatic
    public static final com.baselinelibrary.enums.AREAType getArea() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return com.geely.cockpit.services.controlservice.bean.parser.PlatformAreaTransformKt.toAreaType(platformVehicle != null ? platformVehicle.getAreaType() : null);
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.cockpit.services.controlservice.bean.PlatformType getPlatformType() {
        com.geely.cockpit.services.controlservice.bean.PlatformType platformType;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return (platformVehicle == null || (platformType = platformVehicle.getPlatformType()) == null) ? com.geely.cockpit.services.controlservice.bean.PlatformType.UNKNOWN : platformType;
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.cockpit.services.controlservice.bean.PlatformVersion getPlatformVersion() {
        com.geely.cockpit.services.controlservice.bean.PlatformVersion platformVersion;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return (platformVehicle == null || (platformVersion = platformVehicle.getPlatformVersion()) == null) ? com.geely.cockpit.services.controlservice.bean.PlatformVersion.V20 : platformVersion;
    }

    public final boolean isDefaultCapService() {
        return kotlin.jvm.internal.Intrinsics.areEqual(capabilityService, DEFAULT_CAPABILITY_SERVICE);
    }

    @kotlin.jvm.JvmStatic
    public static final void setupCapabilityService(com.geely.snc.basevehicle.bean.GLCapabilityBean carCapability) {
        com.geely.galaxy.carcontrol.core.vehicle.usp.capability.CapabilityService capabilityServiceBuild;
        com.geely.cockpit.services.controlservice.CarControlLogger.INSTANCE.i("setupCapabilityService", carCapability + " " + (carCapability != null ? carCapability.getOriginData() : null));
        if (carCapability == null || carCapability.getOriginData().isEmpty()) {
            capabilityServiceBuild = DEFAULT_CAPABILITY_SERVICE;
        } else {
            capabilityServiceBuild = com.geely.galaxy.carcontrol.core.vehicle.usp.capability.builder.CapabilityServiceBuilder.INSTANCE.build(getPlatformType(), getPlatformVersion(), carCapability);
        }
        capabilityService = capabilityServiceBuild;
    }

    @kotlin.jvm.JvmStatic
    public static final boolean isV2Car() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return (platformVehicle != null ? platformVehicle.getPlatformVersion() : null) == com.geely.cockpit.services.controlservice.bean.PlatformVersion.V20;
    }

    @kotlin.jvm.JvmStatic
    public static final boolean isV1Car() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return (platformVehicle != null ? platformVehicle.getPlatformVersion() : null) == com.geely.cockpit.services.controlservice.bean.PlatformVersion.V10;
    }

    public final boolean isPanda() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        if ((platformVehicle != null ? platformVehicle.getPlatformVersion() : null) == com.geely.cockpit.services.controlservice.bean.PlatformVersion.V10) {
            com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle2 = curPlatformVehicle;
            if (isPandaCode(platformVehicle2 != null ? platformVehicle2.getSeriesCodeVs() : null)) {
                return true;
            }
        }
        return false;
    }

    public static /* synthetic */ boolean isPandaCode$default(com.geely.cockpit.services.controlservice.runtime.CurVehicle curVehicle, java.lang.String str, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return curVehicle.isPandaCode(str);
    }

    public final boolean isPandaCode(java.lang.String seriesCodeVs) {
        if (seriesCodeVs != null && kotlin.text.StringsKt.startsWith$default(seriesCodeVs, "PA2A", false, 2, (java.lang.Object) null)) {
            return true;
        }
        return seriesCodeVs != null && kotlin.text.StringsKt.startsWith$default(seriesCodeVs, "E21H", false, 2, (java.lang.Object) null);
    }

    public static /* synthetic */ boolean isE22HCode$default(com.geely.cockpit.services.controlservice.runtime.CurVehicle curVehicle, java.lang.String str, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return curVehicle.isE22HCode(str);
    }

    public final boolean isE22HCode(java.lang.String seriesCodeVs) {
        return seriesCodeVs != null && kotlin.text.StringsKt.startsWith$default(seriesCodeVs, "E22H", false, 2, (java.lang.Object) null);
    }

    public final boolean isE22H() {
        java.lang.String seriesCodeVs;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return (platformVehicle == null || (seriesCodeVs = platformVehicle.getSeriesCodeVs()) == null || !kotlin.text.StringsKt.startsWith$default(seriesCodeVs, "E22H", false, 2, (java.lang.Object) null)) ? false : true;
    }

    public final boolean isLEVC() {
        if (curPlatformVehicle == null) {
            return false;
        }
        java.util.List<java.lang.String> list = levcCarList;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return list.contains(platformVehicle != null ? platformVehicle.getSeriesCodeVs() : null);
    }

    @kotlin.jvm.JvmStatic
    public static final boolean isNotV2Car() {
        return !isV2Car();
    }

    public final boolean isOwner() {
        com.geely.cockpit.services.controlservice.bean.PlatformVehicle platformVehicle = curPlatformVehicle;
        return platformVehicle != null && platformVehicle.isOwner();
    }
}
