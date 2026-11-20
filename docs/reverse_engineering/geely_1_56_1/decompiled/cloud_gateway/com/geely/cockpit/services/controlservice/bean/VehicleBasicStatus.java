package com.geely.cockpit.services.controlservice.bean;

/* JADX INFO: compiled from: PlatformVehicleStatus.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\bQ\b\u0086\b\u0018\u00002\u00020\u0001B÷\u0001\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u001aJ\u000b\u0010M\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010N\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010.J\u000b\u0010O\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010P\u001a\u0004\u0018\u00010\u0010HÆ\u0003¢\u0006\u0002\u0010CJ\u000b\u0010Q\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010R\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010.J\u000b\u0010S\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u0016HÆ\u0003J\u000b\u0010V\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010W\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010X\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010Y\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010Z\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010[\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\\\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010]\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010.J\u000b\u0010^\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0003HÆ\u0003Jþ\u0001\u0010a\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010bJ\u0013\u0010c\u001a\u00020\u00102\b\u0010d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010e\u001a\u00020\tHÖ\u0001J\t\u0010f\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001eR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001c\"\u0004\b\"\u0010\u001eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001c\"\u0004\b$\u0010\u001eR\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u001c\"\u0004\b&\u0010\u001eR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u001c\"\u0004\b(\u0010\u001eR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001c\"\u0004\b*\u0010\u001eR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001c\"\u0004\b,\u0010\u001eR\u001e\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u0010\n\u0002\u00101\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u001c\"\u0004\b3\u0010\u001eR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u001c\"\u0004\b5\u0010\u001eR\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u001c\"\u0004\b7\u0010\u001eR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u001c\"\u0004\b9\u0010\u001eR\u001e\u0010\r\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u0010\n\u0002\u00101\u001a\u0004\b:\u0010.\"\u0004\b;\u00100R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\u001c\"\u0004\b=\u0010\u001eR\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u001e\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e¢\u0006\u0010\n\u0002\u0010F\u001a\u0004\bB\u0010C\"\u0004\bD\u0010ER\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bG\u0010\u001c\"\u0004\bH\u0010\u001eR\u001e\u0010\u0012\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u0010\n\u0002\u00101\u001a\u0004\bI\u0010.\"\u0004\bJ\u00100R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bK\u0010\u001c\"\u0004\bL\u0010\u001e¨\u0006g"}, d2 = {"Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;", "", "chargeLevel", "", "chargeLevelPct", "distanceToEmpty", "distanceToEmptyOnBatteryOnly", "distanceToEmptyOnBattery100Soc", "doorLockStatus", "", "fuelLevel", "fuelType", "fuelLevelPct", "lightHornStatus", "odometer", "preClimateActive", "", "tripMeter", "trunkLockStatus", "usageMode", "fuelLevelStatus", "position", "Lcom/geely/cockpit/services/controlservice/bean/VehiclePosition;", "crSetTemp", "carRefrigeratorStatus", "chargeCapDCACStatus", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Lcom/geely/cockpit/services/controlservice/bean/VehiclePosition;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getCarRefrigeratorStatus", "()Ljava/lang/String;", "setCarRefrigeratorStatus", "(Ljava/lang/String;)V", "getChargeCapDCACStatus", "setChargeCapDCACStatus", "getChargeLevel", "setChargeLevel", "getChargeLevelPct", "setChargeLevelPct", "getCrSetTemp", "setCrSetTemp", "getDistanceToEmpty", "setDistanceToEmpty", "getDistanceToEmptyOnBattery100Soc", "setDistanceToEmptyOnBattery100Soc", "getDistanceToEmptyOnBatteryOnly", "setDistanceToEmptyOnBatteryOnly", "getDoorLockStatus", "()Ljava/lang/Integer;", "setDoorLockStatus", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getFuelLevel", "setFuelLevel", "getFuelLevelPct", "setFuelLevelPct", "getFuelLevelStatus", "setFuelLevelStatus", "getFuelType", "setFuelType", "getLightHornStatus", "setLightHornStatus", "getOdometer", "setOdometer", "getPosition", "()Lcom/geely/cockpit/services/controlservice/bean/VehiclePosition;", "setPosition", "(Lcom/geely/cockpit/services/controlservice/bean/VehiclePosition;)V", "getPreClimateActive", "()Ljava/lang/Boolean;", "setPreClimateActive", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getTripMeter", "setTripMeter", "getTrunkLockStatus", "setTrunkLockStatus", "getUsageMode", "setUsageMode", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Lcom/geely/cockpit/services/controlservice/bean/VehiclePosition;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/geely/cockpit/services/controlservice/bean/VehicleBasicStatus;", "equals", "other", "hashCode", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class VehicleBasicStatus {
    private java.lang.String carRefrigeratorStatus;
    private java.lang.String chargeCapDCACStatus;
    private java.lang.String chargeLevel;
    private java.lang.String chargeLevelPct;
    private java.lang.String crSetTemp;
    private java.lang.String distanceToEmpty;
    private java.lang.String distanceToEmptyOnBattery100Soc;
    private java.lang.String distanceToEmptyOnBatteryOnly;
    private java.lang.Integer doorLockStatus;
    private java.lang.String fuelLevel;
    private java.lang.String fuelLevelPct;
    private java.lang.String fuelLevelStatus;
    private java.lang.String fuelType;
    private java.lang.Integer lightHornStatus;
    private java.lang.String odometer;
    private com.geely.cockpit.services.controlservice.bean.VehiclePosition position;
    private java.lang.Boolean preClimateActive;
    private java.lang.String tripMeter;
    private java.lang.Integer trunkLockStatus;
    private java.lang.String usageMode;

    public VehicleBasicStatus() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048575, null);
    }

    public VehicleBasicStatus(java.lang.String str) {
        this(str, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048574, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2) {
        this(str, str2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048572, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        this(str, str2, str3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048568, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        this(str, str2, str3, str4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048560, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5) {
        this(str, str2, str3, str4, str5, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048544, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num) {
        this(str, str2, str3, str4, str5, num, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048512, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6) {
        this(str, str2, str3, str4, str5, num, str6, null, null, null, null, null, null, null, null, null, null, null, null, null, 1048448, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7) {
        this(str, str2, str3, str4, str5, num, str6, str7, null, null, null, null, null, null, null, null, null, null, null, null, 1048320, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, null, null, null, null, null, null, null, null, null, null, null, 1048064, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, null, null, null, null, null, null, null, null, null, null, 1047552, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, null, null, null, null, null, null, null, null, null, 1046528, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, null, null, null, null, null, null, null, null, 1044480, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, null, null, null, null, null, null, null, 1040384, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, null, null, null, null, null, null, 1032192, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, str11, null, null, null, null, null, 1015808, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, str11, str12, null, null, null, null, 983040, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12, com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, str11, str12, vehiclePosition, null, null, null, 917504, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12, com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition, java.lang.String str13) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, str11, str12, vehiclePosition, str13, null, null, 786432, null);
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12, com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition, java.lang.String str13, java.lang.String str14) {
        this(str, str2, str3, str4, str5, num, str6, str7, str8, num2, str9, bool, str10, num3, str11, str12, vehiclePosition, str13, str14, null, 524288, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getChargeLevel() {
        return this.chargeLevel;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final java.lang.Integer getLightHornStatus() {
        return this.lightHornStatus;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final java.lang.String getOdometer() {
        return this.odometer;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final java.lang.Boolean getPreClimateActive() {
        return this.preClimateActive;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final java.lang.String getTripMeter() {
        return this.tripMeter;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final java.lang.Integer getTrunkLockStatus() {
        return this.trunkLockStatus;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final java.lang.String getUsageMode() {
        return this.usageMode;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final java.lang.String getFuelLevelStatus() {
        return this.fuelLevelStatus;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final com.geely.cockpit.services.controlservice.bean.VehiclePosition getPosition() {
        return this.position;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final java.lang.String getCrSetTemp() {
        return this.crSetTemp;
    }

    /* JADX INFO: renamed from: component19, reason: from getter */
    public final java.lang.String getCarRefrigeratorStatus() {
        return this.carRefrigeratorStatus;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getChargeLevelPct() {
        return this.chargeLevelPct;
    }

    /* JADX INFO: renamed from: component20, reason: from getter */
    public final java.lang.String getChargeCapDCACStatus() {
        return this.chargeCapDCACStatus;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.String getDistanceToEmpty() {
        return this.distanceToEmpty;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final java.lang.String getDistanceToEmptyOnBatteryOnly() {
        return this.distanceToEmptyOnBatteryOnly;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final java.lang.String getDistanceToEmptyOnBattery100Soc() {
        return this.distanceToEmptyOnBattery100Soc;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final java.lang.Integer getDoorLockStatus() {
        return this.doorLockStatus;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final java.lang.String getFuelLevel() {
        return this.fuelLevel;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final java.lang.String getFuelType() {
        return this.fuelType;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final java.lang.String getFuelLevelPct() {
        return this.fuelLevelPct;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus copy(java.lang.String chargeLevel, java.lang.String chargeLevelPct, java.lang.String distanceToEmpty, java.lang.String distanceToEmptyOnBatteryOnly, java.lang.String distanceToEmptyOnBattery100Soc, java.lang.Integer doorLockStatus, java.lang.String fuelLevel, java.lang.String fuelType, java.lang.String fuelLevelPct, java.lang.Integer lightHornStatus, java.lang.String odometer, java.lang.Boolean preClimateActive, java.lang.String tripMeter, java.lang.Integer trunkLockStatus, java.lang.String usageMode, java.lang.String fuelLevelStatus, com.geely.cockpit.services.controlservice.bean.VehiclePosition position, java.lang.String crSetTemp, java.lang.String carRefrigeratorStatus, java.lang.String chargeCapDCACStatus) {
        return new com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus(chargeLevel, chargeLevelPct, distanceToEmpty, distanceToEmptyOnBatteryOnly, distanceToEmptyOnBattery100Soc, doorLockStatus, fuelLevel, fuelType, fuelLevelPct, lightHornStatus, odometer, preClimateActive, tripMeter, trunkLockStatus, usageMode, fuelLevelStatus, position, crSetTemp, carRefrigeratorStatus, chargeCapDCACStatus);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus vehicleBasicStatus = (com.geely.cockpit.services.controlservice.bean.VehicleBasicStatus) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.chargeLevel, vehicleBasicStatus.chargeLevel) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeLevelPct, vehicleBasicStatus.chargeLevelPct) && kotlin.jvm.internal.Intrinsics.areEqual(this.distanceToEmpty, vehicleBasicStatus.distanceToEmpty) && kotlin.jvm.internal.Intrinsics.areEqual(this.distanceToEmptyOnBatteryOnly, vehicleBasicStatus.distanceToEmptyOnBatteryOnly) && kotlin.jvm.internal.Intrinsics.areEqual(this.distanceToEmptyOnBattery100Soc, vehicleBasicStatus.distanceToEmptyOnBattery100Soc) && kotlin.jvm.internal.Intrinsics.areEqual(this.doorLockStatus, vehicleBasicStatus.doorLockStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.fuelLevel, vehicleBasicStatus.fuelLevel) && kotlin.jvm.internal.Intrinsics.areEqual(this.fuelType, vehicleBasicStatus.fuelType) && kotlin.jvm.internal.Intrinsics.areEqual(this.fuelLevelPct, vehicleBasicStatus.fuelLevelPct) && kotlin.jvm.internal.Intrinsics.areEqual(this.lightHornStatus, vehicleBasicStatus.lightHornStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.odometer, vehicleBasicStatus.odometer) && kotlin.jvm.internal.Intrinsics.areEqual(this.preClimateActive, vehicleBasicStatus.preClimateActive) && kotlin.jvm.internal.Intrinsics.areEqual(this.tripMeter, vehicleBasicStatus.tripMeter) && kotlin.jvm.internal.Intrinsics.areEqual(this.trunkLockStatus, vehicleBasicStatus.trunkLockStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.usageMode, vehicleBasicStatus.usageMode) && kotlin.jvm.internal.Intrinsics.areEqual(this.fuelLevelStatus, vehicleBasicStatus.fuelLevelStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.position, vehicleBasicStatus.position) && kotlin.jvm.internal.Intrinsics.areEqual(this.crSetTemp, vehicleBasicStatus.crSetTemp) && kotlin.jvm.internal.Intrinsics.areEqual(this.carRefrigeratorStatus, vehicleBasicStatus.carRefrigeratorStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.chargeCapDCACStatus, vehicleBasicStatus.chargeCapDCACStatus);
    }

    public int hashCode() {
        java.lang.String str = this.chargeLevel;
        int iHashCode = (str == null ? 0 : str.hashCode()) * 31;
        java.lang.String str2 = this.chargeLevelPct;
        int iHashCode2 = (iHashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.String str3 = this.distanceToEmpty;
        int iHashCode3 = (iHashCode2 + (str3 == null ? 0 : str3.hashCode())) * 31;
        java.lang.String str4 = this.distanceToEmptyOnBatteryOnly;
        int iHashCode4 = (iHashCode3 + (str4 == null ? 0 : str4.hashCode())) * 31;
        java.lang.String str5 = this.distanceToEmptyOnBattery100Soc;
        int iHashCode5 = (iHashCode4 + (str5 == null ? 0 : str5.hashCode())) * 31;
        java.lang.Integer num = this.doorLockStatus;
        int iHashCode6 = (iHashCode5 + (num == null ? 0 : num.hashCode())) * 31;
        java.lang.String str6 = this.fuelLevel;
        int iHashCode7 = (iHashCode6 + (str6 == null ? 0 : str6.hashCode())) * 31;
        java.lang.String str7 = this.fuelType;
        int iHashCode8 = (iHashCode7 + (str7 == null ? 0 : str7.hashCode())) * 31;
        java.lang.String str8 = this.fuelLevelPct;
        int iHashCode9 = (iHashCode8 + (str8 == null ? 0 : str8.hashCode())) * 31;
        java.lang.Integer num2 = this.lightHornStatus;
        int iHashCode10 = (iHashCode9 + (num2 == null ? 0 : num2.hashCode())) * 31;
        java.lang.String str9 = this.odometer;
        int iHashCode11 = (iHashCode10 + (str9 == null ? 0 : str9.hashCode())) * 31;
        java.lang.Boolean bool = this.preClimateActive;
        int iHashCode12 = (iHashCode11 + (bool == null ? 0 : bool.hashCode())) * 31;
        java.lang.String str10 = this.tripMeter;
        int iHashCode13 = (iHashCode12 + (str10 == null ? 0 : str10.hashCode())) * 31;
        java.lang.Integer num3 = this.trunkLockStatus;
        int iHashCode14 = (iHashCode13 + (num3 == null ? 0 : num3.hashCode())) * 31;
        java.lang.String str11 = this.usageMode;
        int iHashCode15 = (iHashCode14 + (str11 == null ? 0 : str11.hashCode())) * 31;
        java.lang.String str12 = this.fuelLevelStatus;
        int iHashCode16 = (iHashCode15 + (str12 == null ? 0 : str12.hashCode())) * 31;
        com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition = this.position;
        int iHashCode17 = (iHashCode16 + (vehiclePosition == null ? 0 : vehiclePosition.hashCode())) * 31;
        java.lang.String str13 = this.crSetTemp;
        int iHashCode18 = (iHashCode17 + (str13 == null ? 0 : str13.hashCode())) * 31;
        java.lang.String str14 = this.carRefrigeratorStatus;
        int iHashCode19 = (iHashCode18 + (str14 == null ? 0 : str14.hashCode())) * 31;
        java.lang.String str15 = this.chargeCapDCACStatus;
        return iHashCode19 + (str15 != null ? str15.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "VehicleBasicStatus(chargeLevel=" + this.chargeLevel + ", chargeLevelPct=" + this.chargeLevelPct + ", distanceToEmpty=" + this.distanceToEmpty + ", distanceToEmptyOnBatteryOnly=" + this.distanceToEmptyOnBatteryOnly + ", distanceToEmptyOnBattery100Soc=" + this.distanceToEmptyOnBattery100Soc + ", doorLockStatus=" + this.doorLockStatus + ", fuelLevel=" + this.fuelLevel + ", fuelType=" + this.fuelType + ", fuelLevelPct=" + this.fuelLevelPct + ", lightHornStatus=" + this.lightHornStatus + ", odometer=" + this.odometer + ", preClimateActive=" + this.preClimateActive + ", tripMeter=" + this.tripMeter + ", trunkLockStatus=" + this.trunkLockStatus + ", usageMode=" + this.usageMode + ", fuelLevelStatus=" + this.fuelLevelStatus + ", position=" + this.position + ", crSetTemp=" + this.crSetTemp + ", carRefrigeratorStatus=" + this.carRefrigeratorStatus + ", chargeCapDCACStatus=" + this.chargeCapDCACStatus + ")";
    }

    public VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12, com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition, java.lang.String str13, java.lang.String str14, java.lang.String str15) {
        this.chargeLevel = str;
        this.chargeLevelPct = str2;
        this.distanceToEmpty = str3;
        this.distanceToEmptyOnBatteryOnly = str4;
        this.distanceToEmptyOnBattery100Soc = str5;
        this.doorLockStatus = num;
        this.fuelLevel = str6;
        this.fuelType = str7;
        this.fuelLevelPct = str8;
        this.lightHornStatus = num2;
        this.odometer = str9;
        this.preClimateActive = bool;
        this.tripMeter = str10;
        this.trunkLockStatus = num3;
        this.usageMode = str11;
        this.fuelLevelStatus = str12;
        this.position = vehiclePosition;
        this.crSetTemp = str13;
        this.carRefrigeratorStatus = str14;
        this.chargeCapDCACStatus = str15;
    }

    public /* synthetic */ VehicleBasicStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.Integer num, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.Integer num2, java.lang.String str9, java.lang.Boolean bool, java.lang.String str10, java.lang.Integer num3, java.lang.String str11, java.lang.String str12, com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition, java.lang.String str13, java.lang.String str14, java.lang.String str15, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : num, (i & 64) != 0 ? null : str6, (i & 128) != 0 ? null : str7, (i & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? null : str8, (i & 512) != 0 ? null : num2, (i & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? null : str9, (i & 2048) != 0 ? null : bool, (i & 4096) != 0 ? null : str10, (i & 8192) != 0 ? null : num3, (i & 16384) != 0 ? null : str11, (i & 32768) != 0 ? null : str12, (i & 65536) != 0 ? null : vehiclePosition, (i & 131072) != 0 ? null : str13, (i & 262144) != 0 ? null : str14, (i & 524288) != 0 ? null : str15);
    }

    public final java.lang.String getChargeLevel() {
        return this.chargeLevel;
    }

    public final void setChargeLevel(java.lang.String str) {
        this.chargeLevel = str;
    }

    public final java.lang.String getChargeLevelPct() {
        return this.chargeLevelPct;
    }

    public final void setChargeLevelPct(java.lang.String str) {
        this.chargeLevelPct = str;
    }

    public final java.lang.String getDistanceToEmpty() {
        return this.distanceToEmpty;
    }

    public final void setDistanceToEmpty(java.lang.String str) {
        this.distanceToEmpty = str;
    }

    public final java.lang.String getDistanceToEmptyOnBatteryOnly() {
        return this.distanceToEmptyOnBatteryOnly;
    }

    public final void setDistanceToEmptyOnBatteryOnly(java.lang.String str) {
        this.distanceToEmptyOnBatteryOnly = str;
    }

    public final java.lang.String getDistanceToEmptyOnBattery100Soc() {
        return this.distanceToEmptyOnBattery100Soc;
    }

    public final void setDistanceToEmptyOnBattery100Soc(java.lang.String str) {
        this.distanceToEmptyOnBattery100Soc = str;
    }

    public final java.lang.Integer getDoorLockStatus() {
        return this.doorLockStatus;
    }

    public final void setDoorLockStatus(java.lang.Integer num) {
        this.doorLockStatus = num;
    }

    public final java.lang.String getFuelLevel() {
        return this.fuelLevel;
    }

    public final void setFuelLevel(java.lang.String str) {
        this.fuelLevel = str;
    }

    public final java.lang.String getFuelType() {
        return this.fuelType;
    }

    public final void setFuelType(java.lang.String str) {
        this.fuelType = str;
    }

    public final java.lang.String getFuelLevelPct() {
        return this.fuelLevelPct;
    }

    public final void setFuelLevelPct(java.lang.String str) {
        this.fuelLevelPct = str;
    }

    public final java.lang.Integer getLightHornStatus() {
        return this.lightHornStatus;
    }

    public final void setLightHornStatus(java.lang.Integer num) {
        this.lightHornStatus = num;
    }

    public final java.lang.String getOdometer() {
        return this.odometer;
    }

    public final void setOdometer(java.lang.String str) {
        this.odometer = str;
    }

    public final java.lang.Boolean getPreClimateActive() {
        return this.preClimateActive;
    }

    public final void setPreClimateActive(java.lang.Boolean bool) {
        this.preClimateActive = bool;
    }

    public final java.lang.String getTripMeter() {
        return this.tripMeter;
    }

    public final void setTripMeter(java.lang.String str) {
        this.tripMeter = str;
    }

    public final java.lang.Integer getTrunkLockStatus() {
        return this.trunkLockStatus;
    }

    public final void setTrunkLockStatus(java.lang.Integer num) {
        this.trunkLockStatus = num;
    }

    public final java.lang.String getUsageMode() {
        return this.usageMode;
    }

    public final void setUsageMode(java.lang.String str) {
        this.usageMode = str;
    }

    public final java.lang.String getFuelLevelStatus() {
        return this.fuelLevelStatus;
    }

    public final void setFuelLevelStatus(java.lang.String str) {
        this.fuelLevelStatus = str;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehiclePosition getPosition() {
        return this.position;
    }

    public final void setPosition(com.geely.cockpit.services.controlservice.bean.VehiclePosition vehiclePosition) {
        this.position = vehiclePosition;
    }

    public final java.lang.String getCrSetTemp() {
        return this.crSetTemp;
    }

    public final void setCrSetTemp(java.lang.String str) {
        this.crSetTemp = str;
    }

    public final java.lang.String getCarRefrigeratorStatus() {
        return this.carRefrigeratorStatus;
    }

    public final void setCarRefrigeratorStatus(java.lang.String str) {
        this.carRefrigeratorStatus = str;
    }

    public final java.lang.String getChargeCapDCACStatus() {
        return this.chargeCapDCACStatus;
    }

    public final void setChargeCapDCACStatus(java.lang.String str) {
        this.chargeCapDCACStatus = str;
    }
}
