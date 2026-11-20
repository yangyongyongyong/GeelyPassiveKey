package com.geely.cockpit.services.controlservice.bean;

/* JADX INFO: compiled from: PlatformVehicleStatus.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000¤\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\bo\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0099\u0002\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001c\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 \u0012\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$\u0012\n\b\u0002\u0010%\u001a\u0004\u0018\u00010&\u0012\n\b\u0002\u0010'\u001a\u0004\u0018\u00010(\u0012\n\b\u0002\u0010)\u001a\u0004\u0018\u00010*\u0012\n\b\u0002\u0010+\u001a\u0004\u0018\u00010,\u0012\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010.J\f\u0010\u008e\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u008f\u0001\u001a\u0004\u0018\u00010\u0014HÆ\u0003J\f\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u0016HÆ\u0003J\f\u0010\u0091\u0001\u001a\u0004\u0018\u00010\u0018HÆ\u0003J\f\u0010\u0092\u0001\u001a\u0004\u0018\u00010\u001aHÆ\u0003J\f\u0010\u0093\u0001\u001a\u0004\u0018\u00010\u001cHÆ\u0003J\f\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u001eHÆ\u0003J\f\u0010\u0095\u0001\u001a\u0004\u0018\u00010 HÆ\u0003J\f\u0010\u0096\u0001\u001a\u0004\u0018\u00010\"HÆ\u0003J\f\u0010\u0097\u0001\u001a\u0004\u0018\u00010$HÆ\u0003J\f\u0010\u0098\u0001\u001a\u0004\u0018\u00010&HÆ\u0003J\f\u0010\u0099\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u009a\u0001\u001a\u0004\u0018\u00010(HÆ\u0003J\f\u0010\u009b\u0001\u001a\u0004\u0018\u00010*HÆ\u0003J\f\u0010\u009c\u0001\u001a\u0004\u0018\u00010,HÆ\u0003J\f\u0010\u009d\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0011\u0010\u009e\u0001\u001a\u0004\u0018\u00010\u0006HÆ\u0003¢\u0006\u0002\u0010@J\f\u0010\u009f\u0001\u001a\u0004\u0018\u00010\bHÆ\u0003J\f\u0010 \u0001\u001a\u0004\u0018\u00010\nHÆ\u0003J\f\u0010¡\u0001\u001a\u0004\u0018\u00010\fHÆ\u0003J\f\u0010¢\u0001\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\f\u0010£\u0001\u001a\u0004\u0018\u00010\u0010HÆ\u0003J\f\u0010¤\u0001\u001a\u0004\u0018\u00010\u0012HÆ\u0003J¤\u0002\u0010¥\u0001\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00182\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$2\n\b\u0002\u0010%\u001a\u0004\u0018\u00010&2\n\b\u0002\u0010'\u001a\u0004\u0018\u00010(2\n\b\u0002\u0010)\u001a\u0004\u0018\u00010*2\n\b\u0002\u0010+\u001a\u0004\u0018\u00010,2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0003\u0010¦\u0001J\u0016\u0010§\u0001\u001a\u00030¨\u00012\t\u0010©\u0001\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u000b\u0010ª\u0001\u001a\u00030«\u0001HÖ\u0001J\n\u0010¬\u0001\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001d\u00107\u001a\u0004\u0018\u0001088FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b;\u0010<\u001a\u0004\b9\u0010:R\u001c\u0010-\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u00104\"\u0004\b>\u00106R\u001e\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e¢\u0006\u0010\n\u0002\u0010C\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010E\"\u0004\bF\u0010GR\u001c\u0010)\u001a\u0004\u0018\u00010*X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010I\"\u0004\bJ\u0010KR\u001c\u0010'\u001a\u0004\u0018\u00010(X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010M\"\u0004\bN\u0010OR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010Q\"\u0004\bR\u0010SR\u001c\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bT\u0010U\"\u0004\bV\u0010WR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010Y\"\u0004\bZ\u0010[R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010]\"\u0004\b^\u0010_R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b`\u0010a\"\u0004\bb\u0010cR\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010e\"\u0004\bf\u0010gR\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bh\u0010i\"\u0004\bj\u0010kR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bl\u0010m\"\u0004\bn\u0010oR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bp\u0010q\"\u0004\br\u0010sR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bt\u0010u\"\u0004\bv\u0010wR\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bx\u0010y\"\u0004\bz\u0010{R\u001c\u0010+\u001a\u0004\u0018\u00010,X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b|\u0010}\"\u0004\b~\u0010\u007fR \u0010!\u001a\u0004\u0018\u00010\"X\u0086\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0080\u0001\u0010\u0081\u0001\"\u0006\b\u0082\u0001\u0010\u0083\u0001R \u0010#\u001a\u0004\u0018\u00010$X\u0086\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0084\u0001\u0010\u0085\u0001\"\u0006\b\u0086\u0001\u0010\u0087\u0001R \u0010%\u001a\u0004\u0018\u00010&X\u0086\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0088\u0001\u0010\u0089\u0001\"\u0006\b\u008a\u0001\u0010\u008b\u0001R\u001e\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u008c\u0001\u00104\"\u0005\b\u008d\u0001\u00106¨\u0006\u00ad\u0001"}, d2 = {"Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "", "vin", "", "carFromSource", "updateTime", "", "basicVehicleStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;", "vehicleBatteryStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;", "vehicleClimateStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleClimateStatus;", "vehicleDoorCoverStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleDoorCoverStatus;", "vehicleEngineStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleEngineStatus;", "vehicleEnvironmentStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleEnvironmentStatus;", "vehicleFragranceStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleFragranceStatus;", "vehicleHeadLightStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleHeadLightStatus;", "vehicleIndicatorLightStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleIndicatorLightStatus;", "vehicleLocationStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleLocationStatus;", "vehicleMaintainStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleMaintainStatus;", "vehicleRunningStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;", "vehicleSafeStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleSafeStatus;", "vehicleTcamStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleTcamStatus;", "vehicleTyreStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;", "vehicleWindowStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleWindowStatus;", "vehicleBleExtra", "Lcom/geely/cockpit/services/controlservice/bean/VehicleBleExtra;", "vehicleBigdataStatus", "Lcom/geely/cockpit/services/controlservice/bean/VehicleBigdataStatus;", "vehicleSwitch", "Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;", "shadowTspVehicleStatus", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleClimateStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleDoorCoverStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleEngineStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleEnvironmentStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleFragranceStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleHeadLightStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleIndicatorLightStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleLocationStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleMaintainStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleSafeStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleTcamStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleWindowStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleBleExtra;Lcom/geely/cockpit/services/controlservice/bean/VehicleBigdataStatus;Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;Ljava/lang/String;)V", "getBasicVehicleStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;", "setBasicVehicleStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;)V", "getCarFromSource", "()Ljava/lang/String;", "setCarFromSource", "(Ljava/lang/String;)V", "shadowTspStatus", "Lcom/ecarx/open/http/entity/VehicleStatus;", "getShadowTspStatus", "()Lcom/ecarx/open/http/entity/VehicleStatus;", "shadowTspStatus$delegate", "Lkotlin/Lazy;", "getShadowTspVehicleStatus", "setShadowTspVehicleStatus", "getUpdateTime", "()Ljava/lang/Long;", "setUpdateTime", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getVehicleBatteryStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;", "setVehicleBatteryStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;)V", "getVehicleBigdataStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleBigdataStatus;", "setVehicleBigdataStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleBigdataStatus;)V", "getVehicleBleExtra", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleBleExtra;", "setVehicleBleExtra", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleBleExtra;)V", "getVehicleClimateStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleClimateStatus;", "setVehicleClimateStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleClimateStatus;)V", "getVehicleDoorCoverStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleDoorCoverStatus;", "setVehicleDoorCoverStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleDoorCoverStatus;)V", "getVehicleEngineStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleEngineStatus;", "setVehicleEngineStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleEngineStatus;)V", "getVehicleEnvironmentStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleEnvironmentStatus;", "setVehicleEnvironmentStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleEnvironmentStatus;)V", "getVehicleFragranceStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleFragranceStatus;", "setVehicleFragranceStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleFragranceStatus;)V", "getVehicleHeadLightStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleHeadLightStatus;", "setVehicleHeadLightStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleHeadLightStatus;)V", "getVehicleIndicatorLightStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleIndicatorLightStatus;", "setVehicleIndicatorLightStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleIndicatorLightStatus;)V", "getVehicleLocationStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleLocationStatus;", "setVehicleLocationStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleLocationStatus;)V", "getVehicleMaintainStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleMaintainStatus;", "setVehicleMaintainStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleMaintainStatus;)V", "getVehicleRunningStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;", "setVehicleRunningStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;)V", "getVehicleSafeStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleSafeStatus;", "setVehicleSafeStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleSafeStatus;)V", "getVehicleSwitch", "()Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;", "setVehicleSwitch", "(Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;)V", "getVehicleTcamStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleTcamStatus;", "setVehicleTcamStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleTcamStatus;)V", "getVehicleTyreStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;", "setVehicleTyreStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;)V", "getVehicleWindowStatus", "()Lcom/geely/cockpit/services/controlservice/bean/VehicleWindowStatus;", "setVehicleWindowStatus", "(Lcom/geely/cockpit/services/controlservice/bean/VehicleWindowStatus;)V", "getVin", "setVin", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleClimateStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleDoorCoverStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleEngineStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleEnvironmentStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleFragranceStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleHeadLightStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleIndicatorLightStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleLocationStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleMaintainStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleSafeStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleTcamStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleWindowStatus;Lcom/geely/cockpit/services/controlservice/bean/VehicleBleExtra;Lcom/geely/cockpit/services/controlservice/bean/VehicleBigdataStatus;Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleSwitch;Ljava/lang/String;)Lcom/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus;", "equals", "", "other", "hashCode", "", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class PlatformVehicleStatus {
    private com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus basicVehicleStatus;
    private java.lang.String carFromSource;

    /* JADX INFO: renamed from: shadowTspStatus$delegate, reason: from kotlin metadata */
    private final transient kotlin.Lazy shadowTspStatus;
    private java.lang.String shadowTspVehicleStatus;
    private java.lang.Long updateTime;
    private com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra;
    private com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus;
    private com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch vehicleSwitch;
    private com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus;
    private com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus;
    private java.lang.String vin;

    public PlatformVehicleStatus() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 8388607, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getVin() {
        return this.vin;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus getVehicleFragranceStatus() {
        return this.vehicleFragranceStatus;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus getVehicleHeadLightStatus() {
        return this.vehicleHeadLightStatus;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus getVehicleIndicatorLightStatus() {
        return this.vehicleIndicatorLightStatus;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus getVehicleLocationStatus() {
        return this.vehicleLocationStatus;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus getVehicleMaintainStatus() {
        return this.vehicleMaintainStatus;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus getVehicleRunningStatus() {
        return this.vehicleRunningStatus;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus getVehicleSafeStatus() {
        return this.vehicleSafeStatus;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus getVehicleTcamStatus() {
        return this.vehicleTcamStatus;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus getVehicleTyreStatus() {
        return this.vehicleTyreStatus;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus getVehicleWindowStatus() {
        return this.vehicleWindowStatus;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getCarFromSource() {
        return this.carFromSource;
    }

    /* JADX INFO: renamed from: component20, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleBleExtra getVehicleBleExtra() {
        return this.vehicleBleExtra;
    }

    /* JADX INFO: renamed from: component21, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus getVehicleBigdataStatus() {
        return this.vehicleBigdataStatus;
    }

    /* JADX INFO: renamed from: component22, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch getVehicleSwitch() {
        return this.vehicleSwitch;
    }

    /* JADX INFO: renamed from: component23, reason: from getter */
    public final java.lang.String getShadowTspVehicleStatus() {
        return this.shadowTspVehicleStatus;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.Long getUpdateTime() {
        return this.updateTime;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus getBasicVehicleStatus() {
        return this.basicVehicleStatus;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus getVehicleBatteryStatus() {
        return this.vehicleBatteryStatus;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus getVehicleClimateStatus() {
        return this.vehicleClimateStatus;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus getVehicleDoorCoverStatus() {
        return this.vehicleDoorCoverStatus;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus getVehicleEngineStatus() {
        return this.vehicleEngineStatus;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus getVehicleEnvironmentStatus() {
        return this.vehicleEnvironmentStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus copy(java.lang.String vin, java.lang.String carFromSource, java.lang.Long updateTime, com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus basicVehicleStatus, com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus, com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus, com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus, com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus, com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus, com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus, com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus, com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus, com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus, com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus, com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus, com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus, com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus, com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra, com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus, com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch vehicleSwitch, java.lang.String shadowTspVehicleStatus) {
        return new com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus(vin, carFromSource, updateTime, basicVehicleStatus, vehicleBatteryStatus, vehicleClimateStatus, vehicleDoorCoverStatus, vehicleEngineStatus, vehicleEnvironmentStatus, vehicleFragranceStatus, vehicleHeadLightStatus, vehicleIndicatorLightStatus, vehicleLocationStatus, vehicleMaintainStatus, vehicleRunningStatus, vehicleSafeStatus, vehicleTcamStatus, vehicleTyreStatus, vehicleWindowStatus, vehicleBleExtra, vehicleBigdataStatus, vehicleSwitch, shadowTspVehicleStatus);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus platformVehicleStatus = (com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.vin, platformVehicleStatus.vin) && kotlin.jvm.internal.Intrinsics.areEqual(this.carFromSource, platformVehicleStatus.carFromSource) && kotlin.jvm.internal.Intrinsics.areEqual(this.updateTime, platformVehicleStatus.updateTime) && kotlin.jvm.internal.Intrinsics.areEqual(this.basicVehicleStatus, platformVehicleStatus.basicVehicleStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleBatteryStatus, platformVehicleStatus.vehicleBatteryStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleClimateStatus, platformVehicleStatus.vehicleClimateStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleDoorCoverStatus, platformVehicleStatus.vehicleDoorCoverStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleEngineStatus, platformVehicleStatus.vehicleEngineStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleEnvironmentStatus, platformVehicleStatus.vehicleEnvironmentStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleFragranceStatus, platformVehicleStatus.vehicleFragranceStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleHeadLightStatus, platformVehicleStatus.vehicleHeadLightStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleIndicatorLightStatus, platformVehicleStatus.vehicleIndicatorLightStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleLocationStatus, platformVehicleStatus.vehicleLocationStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleMaintainStatus, platformVehicleStatus.vehicleMaintainStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleRunningStatus, platformVehicleStatus.vehicleRunningStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleSafeStatus, platformVehicleStatus.vehicleSafeStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleTcamStatus, platformVehicleStatus.vehicleTcamStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleTyreStatus, platformVehicleStatus.vehicleTyreStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleWindowStatus, platformVehicleStatus.vehicleWindowStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleBleExtra, platformVehicleStatus.vehicleBleExtra) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleBigdataStatus, platformVehicleStatus.vehicleBigdataStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.vehicleSwitch, platformVehicleStatus.vehicleSwitch) && kotlin.jvm.internal.Intrinsics.areEqual(this.shadowTspVehicleStatus, platformVehicleStatus.shadowTspVehicleStatus);
    }

    public int hashCode() {
        java.lang.String str = this.vin;
        int iHashCode = (str == null ? 0 : str.hashCode()) * 31;
        java.lang.String str2 = this.carFromSource;
        int iHashCode2 = (iHashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.Long l = this.updateTime;
        int iHashCode3 = (iHashCode2 + (l == null ? 0 : l.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus vehicleBasicStatus = this.basicVehicleStatus;
        int iHashCode4 = (iHashCode3 + (vehicleBasicStatus == null ? 0 : vehicleBasicStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus = this.vehicleBatteryStatus;
        int iHashCode5 = (iHashCode4 + (vehicleBatteryStatus == null ? 0 : vehicleBatteryStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus = this.vehicleClimateStatus;
        int iHashCode6 = (iHashCode5 + (vehicleClimateStatus == null ? 0 : vehicleClimateStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus = this.vehicleDoorCoverStatus;
        int iHashCode7 = (iHashCode6 + (vehicleDoorCoverStatus == null ? 0 : vehicleDoorCoverStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus = this.vehicleEngineStatus;
        int iHashCode8 = (iHashCode7 + (vehicleEngineStatus == null ? 0 : vehicleEngineStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus = this.vehicleEnvironmentStatus;
        int iHashCode9 = (iHashCode8 + (vehicleEnvironmentStatus == null ? 0 : vehicleEnvironmentStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus = this.vehicleFragranceStatus;
        int iHashCode10 = (iHashCode9 + (vehicleFragranceStatus == null ? 0 : vehicleFragranceStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus = this.vehicleHeadLightStatus;
        int iHashCode11 = (iHashCode10 + (vehicleHeadLightStatus == null ? 0 : vehicleHeadLightStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus = this.vehicleIndicatorLightStatus;
        int iHashCode12 = (iHashCode11 + (vehicleIndicatorLightStatus == null ? 0 : vehicleIndicatorLightStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus = this.vehicleLocationStatus;
        int iHashCode13 = (iHashCode12 + (vehicleLocationStatus == null ? 0 : vehicleLocationStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus = this.vehicleMaintainStatus;
        int iHashCode14 = (iHashCode13 + (vehicleMaintainStatus == null ? 0 : vehicleMaintainStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus = this.vehicleRunningStatus;
        int iHashCode15 = (iHashCode14 + (vehicleRunningStatus == null ? 0 : vehicleRunningStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus = this.vehicleSafeStatus;
        int iHashCode16 = (iHashCode15 + (vehicleSafeStatus == null ? 0 : vehicleSafeStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus = this.vehicleTcamStatus;
        int iHashCode17 = (iHashCode16 + (vehicleTcamStatus == null ? 0 : vehicleTcamStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus = this.vehicleTyreStatus;
        int iHashCode18 = (iHashCode17 + (vehicleTyreStatus == null ? 0 : vehicleTyreStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus = this.vehicleWindowStatus;
        int iHashCode19 = (iHashCode18 + (vehicleWindowStatus == null ? 0 : vehicleWindowStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra = this.vehicleBleExtra;
        int iHashCode20 = (iHashCode19 + (vehicleBleExtra == null ? 0 : vehicleBleExtra.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus = this.vehicleBigdataStatus;
        int iHashCode21 = (iHashCode20 + (vehicleBigdataStatus == null ? 0 : vehicleBigdataStatus.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch platformVehicleSwitch = this.vehicleSwitch;
        int iHashCode22 = (iHashCode21 + (platformVehicleSwitch == null ? 0 : platformVehicleSwitch.hashCode())) * 31;
        java.lang.String str3 = this.shadowTspVehicleStatus;
        return iHashCode22 + (str3 != null ? str3.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "PlatformVehicleStatus(vin=" + this.vin + ", carFromSource=" + this.carFromSource + ", updateTime=" + this.updateTime + ", basicVehicleStatus=" + this.basicVehicleStatus + ", vehicleBatteryStatus=" + this.vehicleBatteryStatus + ", vehicleClimateStatus=" + this.vehicleClimateStatus + ", vehicleDoorCoverStatus=" + this.vehicleDoorCoverStatus + ", vehicleEngineStatus=" + this.vehicleEngineStatus + ", vehicleEnvironmentStatus=" + this.vehicleEnvironmentStatus + ", vehicleFragranceStatus=" + this.vehicleFragranceStatus + ", vehicleHeadLightStatus=" + this.vehicleHeadLightStatus + ", vehicleIndicatorLightStatus=" + this.vehicleIndicatorLightStatus + ", vehicleLocationStatus=" + this.vehicleLocationStatus + ", vehicleMaintainStatus=" + this.vehicleMaintainStatus + ", vehicleRunningStatus=" + this.vehicleRunningStatus + ", vehicleSafeStatus=" + this.vehicleSafeStatus + ", vehicleTcamStatus=" + this.vehicleTcamStatus + ", vehicleTyreStatus=" + this.vehicleTyreStatus + ", vehicleWindowStatus=" + this.vehicleWindowStatus + ", vehicleBleExtra=" + this.vehicleBleExtra + ", vehicleBigdataStatus=" + this.vehicleBigdataStatus + ", vehicleSwitch=" + this.vehicleSwitch + ", shadowTspVehicleStatus=" + this.shadowTspVehicleStatus + ")";
    }

    public PlatformVehicleStatus(java.lang.String str, java.lang.String str2, java.lang.Long l, com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus vehicleBasicStatus, com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus, com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus, com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus, com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus, com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus, com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus, com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus, com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus, com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus, com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus, com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus, com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus, com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus, com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra, com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus, com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch platformVehicleSwitch, java.lang.String str3) {
        this.vin = str;
        this.carFromSource = str2;
        this.updateTime = l;
        this.basicVehicleStatus = vehicleBasicStatus;
        this.vehicleBatteryStatus = vehicleBatteryStatus;
        this.vehicleClimateStatus = vehicleClimateStatus;
        this.vehicleDoorCoverStatus = vehicleDoorCoverStatus;
        this.vehicleEngineStatus = vehicleEngineStatus;
        this.vehicleEnvironmentStatus = vehicleEnvironmentStatus;
        this.vehicleFragranceStatus = vehicleFragranceStatus;
        this.vehicleHeadLightStatus = vehicleHeadLightStatus;
        this.vehicleIndicatorLightStatus = vehicleIndicatorLightStatus;
        this.vehicleLocationStatus = vehicleLocationStatus;
        this.vehicleMaintainStatus = vehicleMaintainStatus;
        this.vehicleRunningStatus = vehicleRunningStatus;
        this.vehicleSafeStatus = vehicleSafeStatus;
        this.vehicleTcamStatus = vehicleTcamStatus;
        this.vehicleTyreStatus = vehicleTyreStatus;
        this.vehicleWindowStatus = vehicleWindowStatus;
        this.vehicleBleExtra = vehicleBleExtra;
        this.vehicleBigdataStatus = vehicleBigdataStatus;
        this.vehicleSwitch = platformVehicleSwitch;
        this.shadowTspVehicleStatus = str3;
        this.shadowTspStatus = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.ecarx.open.http.entity.VehicleStatus>() { // from class: com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus$shadowTspStatus$2
            {
                super(0);
            }

            /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
            public final com.ecarx.open.http.entity.VehicleStatus m1217invoke() {
                java.lang.Object obj;
                com.geely.cockpit.services.controlservice.bean.PlatformVehicleStatus platformVehicleStatus = this.this$0;
                try {
                    kotlin.Result.Companion companion = kotlin.Result.Companion;
                    obj = kotlin.Result.constructor-impl((com.ecarx.open.http.entity.VehicleStatus) com.blankj.utilcode.util.GsonUtils.fromJson(platformVehicleStatus.getShadowTspVehicleStatus(), com.ecarx.open.http.entity.VehicleStatus.class));
                } catch (java.lang.Throwable th) {
                    kotlin.Result.Companion companion2 = kotlin.Result.Companion;
                    obj = kotlin.Result.constructor-impl(kotlin.ResultKt.createFailure(th));
                }
                if (kotlin.Result.isFailure-impl(obj)) {
                    obj = null;
                }
                return (com.ecarx.open.http.entity.VehicleStatus) obj;
            }
        });
    }

    public /* synthetic */ PlatformVehicleStatus(java.lang.String str, java.lang.String str2, java.lang.Long l, com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus vehicleBasicStatus, com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus, com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus, com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus, com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus, com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus, com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus, com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus, com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus, com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus, com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus, com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus, com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus, com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus, com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus, com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra, com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus, com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch platformVehicleSwitch, java.lang.String str3, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : l, (i & 8) != 0 ? null : vehicleBasicStatus, (i & 16) != 0 ? null : vehicleBatteryStatus, (i & 32) != 0 ? null : vehicleClimateStatus, (i & 64) != 0 ? null : vehicleDoorCoverStatus, (i & 128) != 0 ? null : vehicleEngineStatus, (i & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? null : vehicleEnvironmentStatus, (i & 512) != 0 ? null : vehicleFragranceStatus, (i & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? null : vehicleHeadLightStatus, (i & 2048) != 0 ? null : vehicleIndicatorLightStatus, (i & 4096) != 0 ? null : vehicleLocationStatus, (i & 8192) != 0 ? null : vehicleMaintainStatus, (i & 16384) != 0 ? null : vehicleRunningStatus, (i & 32768) != 0 ? null : vehicleSafeStatus, (i & 65536) != 0 ? null : vehicleTcamStatus, (i & 131072) != 0 ? null : vehicleTyreStatus, (i & 262144) != 0 ? null : vehicleWindowStatus, (i & 524288) != 0 ? null : vehicleBleExtra, (i & 1048576) != 0 ? null : vehicleBigdataStatus, (i & 2097152) != 0 ? null : platformVehicleSwitch, (i & 4194304) != 0 ? null : str3);
    }

    public final java.lang.String getVin() {
        return this.vin;
    }

    public final void setVin(java.lang.String str) {
        this.vin = str;
    }

    public final java.lang.String getCarFromSource() {
        return this.carFromSource;
    }

    public final void setCarFromSource(java.lang.String str) {
        this.carFromSource = str;
    }

    public final java.lang.Long getUpdateTime() {
        return this.updateTime;
    }

    public final void setUpdateTime(java.lang.Long l) {
        this.updateTime = l;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus getBasicVehicleStatus() {
        return this.basicVehicleStatus;
    }

    public final void setBasicVehicleStatus(com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus vehicleBasicStatus) {
        this.basicVehicleStatus = vehicleBasicStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus getVehicleBatteryStatus() {
        return this.vehicleBatteryStatus;
    }

    public final void setVehicleBatteryStatus(com.geely.cockpit.services.controlservice.bean.VehicleBatteryStatus vehicleBatteryStatus) {
        this.vehicleBatteryStatus = vehicleBatteryStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus getVehicleClimateStatus() {
        return this.vehicleClimateStatus;
    }

    public final void setVehicleClimateStatus(com.geely.cockpit.services.controlservice.bean.VehicleClimateStatus vehicleClimateStatus) {
        this.vehicleClimateStatus = vehicleClimateStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus getVehicleDoorCoverStatus() {
        return this.vehicleDoorCoverStatus;
    }

    public final void setVehicleDoorCoverStatus(com.geely.cockpit.services.controlservice.bean.VehicleDoorCoverStatus vehicleDoorCoverStatus) {
        this.vehicleDoorCoverStatus = vehicleDoorCoverStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus getVehicleEngineStatus() {
        return this.vehicleEngineStatus;
    }

    public final void setVehicleEngineStatus(com.geely.cockpit.services.controlservice.bean.VehicleEngineStatus vehicleEngineStatus) {
        this.vehicleEngineStatus = vehicleEngineStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus getVehicleEnvironmentStatus() {
        return this.vehicleEnvironmentStatus;
    }

    public final void setVehicleEnvironmentStatus(com.geely.cockpit.services.controlservice.bean.VehicleEnvironmentStatus vehicleEnvironmentStatus) {
        this.vehicleEnvironmentStatus = vehicleEnvironmentStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus getVehicleFragranceStatus() {
        return this.vehicleFragranceStatus;
    }

    public final void setVehicleFragranceStatus(com.geely.cockpit.services.controlservice.bean.VehicleFragranceStatus vehicleFragranceStatus) {
        this.vehicleFragranceStatus = vehicleFragranceStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus getVehicleHeadLightStatus() {
        return this.vehicleHeadLightStatus;
    }

    public final void setVehicleHeadLightStatus(com.geely.cockpit.services.controlservice.bean.VehicleHeadLightStatus vehicleHeadLightStatus) {
        this.vehicleHeadLightStatus = vehicleHeadLightStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus getVehicleIndicatorLightStatus() {
        return this.vehicleIndicatorLightStatus;
    }

    public final void setVehicleIndicatorLightStatus(com.geely.cockpit.services.controlservice.bean.VehicleIndicatorLightStatus vehicleIndicatorLightStatus) {
        this.vehicleIndicatorLightStatus = vehicleIndicatorLightStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus getVehicleLocationStatus() {
        return this.vehicleLocationStatus;
    }

    public final void setVehicleLocationStatus(com.geely.cockpit.services.controlservice.bean.VehicleLocationStatus vehicleLocationStatus) {
        this.vehicleLocationStatus = vehicleLocationStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus getVehicleMaintainStatus() {
        return this.vehicleMaintainStatus;
    }

    public final void setVehicleMaintainStatus(com.geely.cockpit.services.controlservice.bean.VehicleMaintainStatus vehicleMaintainStatus) {
        this.vehicleMaintainStatus = vehicleMaintainStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus getVehicleRunningStatus() {
        return this.vehicleRunningStatus;
    }

    public final void setVehicleRunningStatus(com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus) {
        this.vehicleRunningStatus = vehicleRunningStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus getVehicleSafeStatus() {
        return this.vehicleSafeStatus;
    }

    public final void setVehicleSafeStatus(com.geely.cockpit.services.controlservice.bean.VehicleSafeStatus vehicleSafeStatus) {
        this.vehicleSafeStatus = vehicleSafeStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus getVehicleTcamStatus() {
        return this.vehicleTcamStatus;
    }

    public final void setVehicleTcamStatus(com.geely.cockpit.services.controlservice.bean.VehicleTcamStatus vehicleTcamStatus) {
        this.vehicleTcamStatus = vehicleTcamStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus getVehicleTyreStatus() {
        return this.vehicleTyreStatus;
    }

    public final void setVehicleTyreStatus(com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus) {
        this.vehicleTyreStatus = vehicleTyreStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus getVehicleWindowStatus() {
        return this.vehicleWindowStatus;
    }

    public final void setVehicleWindowStatus(com.geely.cockpit.services.controlservice.bean.VehicleWindowStatus vehicleWindowStatus) {
        this.vehicleWindowStatus = vehicleWindowStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBleExtra getVehicleBleExtra() {
        return this.vehicleBleExtra;
    }

    public final void setVehicleBleExtra(com.geely.cockpit.services.controlservice.bean.VehicleBleExtra vehicleBleExtra) {
        this.vehicleBleExtra = vehicleBleExtra;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus getVehicleBigdataStatus() {
        return this.vehicleBigdataStatus;
    }

    public final void setVehicleBigdataStatus(com.geely.cockpit.services.controlservice.bean.VehicleBigdataStatus vehicleBigdataStatus) {
        this.vehicleBigdataStatus = vehicleBigdataStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch getVehicleSwitch() {
        return this.vehicleSwitch;
    }

    public final void setVehicleSwitch(com.geely.cockpit.services.controlservice.bean.PlatformVehicleSwitch platformVehicleSwitch) {
        this.vehicleSwitch = platformVehicleSwitch;
    }

    public final java.lang.String getShadowTspVehicleStatus() {
        return this.shadowTspVehicleStatus;
    }

    public final void setShadowTspVehicleStatus(java.lang.String str) {
        this.shadowTspVehicleStatus = str;
    }

    public final com.ecarx.open.http.entity.VehicleStatus getShadowTspStatus() {
        return (com.ecarx.open.http.entity.VehicleStatus) this.shadowTspStatus.getValue();
    }
}
