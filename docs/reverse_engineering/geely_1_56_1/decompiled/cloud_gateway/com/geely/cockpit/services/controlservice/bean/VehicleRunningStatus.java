package com.geely.cockpit.services.controlservice.bean;

/* JADX INFO: compiled from: PlatformVehicleStatus.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\bL\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bß\u0001\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0015J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010D\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010E\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010F\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010G\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010I\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010K\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010L\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010M\u001a\u0004\u0018\u00010\u0003HÆ\u0003Já\u0001\u0010N\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010O\u001a\u00020P2\b\u0010Q\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010R\u001a\u00020SHÖ\u0001J\t\u0010T\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0017\"\u0004\b\u001b\u0010\u0019R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0017\"\u0004\b\u001d\u0010\u0019R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0017\"\u0004\b\u001f\u0010\u0019R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0017\"\u0004\b!\u0010\u0019R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0017\"\u0004\b#\u0010\u0019R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0017\"\u0004\b%\u0010\u0019R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0017\"\u0004\b'\u0010\u0019R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0017\"\u0004\b)\u0010\u0019R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0017\"\u0004\b+\u0010\u0019R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0017\"\u0004\b-\u0010\u0019R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0017\"\u0004\b/\u0010\u0019R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010\u0017\"\u0004\b1\u0010\u0019R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u0017\"\u0004\b3\u0010\u0019R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0017\"\u0004\b5\u0010\u0019R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0017\"\u0004\b7\u0010\u0019R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0017\"\u0004\b9\u0010\u0019R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0017\"\u0004\b;\u0010\u0019¨\u0006U"}, d2 = {"Lcom/geely/cockpit/services/controlservice/bean/VehicleRunningStatus;", "", "aveFuelConsumption", "", "averPowerConsumption", "avgSpeed", "electricParkBrakeStatus", "engineSpeed", "indPowerConsumption", "indFuelConsumption", "odometer", "gearAutoStatus", "speed", "tripMeter1", "tripMeter2", "aveTraFuelConsumption", "averTraPowerConsumption", "aveTraAlcoholConsumption", "mmiPwrcnsavgtabsts", "mmiPwrcnsavg50", "mmiPwrcnsavg100", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAveFuelConsumption", "()Ljava/lang/String;", "setAveFuelConsumption", "(Ljava/lang/String;)V", "getAveTraAlcoholConsumption", "setAveTraAlcoholConsumption", "getAveTraFuelConsumption", "setAveTraFuelConsumption", "getAverPowerConsumption", "setAverPowerConsumption", "getAverTraPowerConsumption", "setAverTraPowerConsumption", "getAvgSpeed", "setAvgSpeed", "getElectricParkBrakeStatus", "setElectricParkBrakeStatus", "getEngineSpeed", "setEngineSpeed", "getGearAutoStatus", "setGearAutoStatus", "getIndFuelConsumption", "setIndFuelConsumption", "getIndPowerConsumption", "setIndPowerConsumption", "getMmiPwrcnsavg100", "setMmiPwrcnsavg100", "getMmiPwrcnsavg50", "setMmiPwrcnsavg50", "getMmiPwrcnsavgtabsts", "setMmiPwrcnsavgtabsts", "getOdometer", "setOdometer", "getSpeed", "setSpeed", "getTripMeter1", "setTripMeter1", "getTripMeter2", "setTripMeter2", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class VehicleRunningStatus {
    private java.lang.String aveFuelConsumption;
    private java.lang.String aveTraAlcoholConsumption;
    private java.lang.String aveTraFuelConsumption;
    private java.lang.String averPowerConsumption;
    private java.lang.String averTraPowerConsumption;
    private java.lang.String avgSpeed;
    private java.lang.String electricParkBrakeStatus;
    private java.lang.String engineSpeed;
    private java.lang.String gearAutoStatus;
    private java.lang.String indFuelConsumption;
    private java.lang.String indPowerConsumption;
    private java.lang.String mmiPwrcnsavg100;
    private java.lang.String mmiPwrcnsavg50;
    private java.lang.String mmiPwrcnsavgtabsts;
    private java.lang.String odometer;
    private java.lang.String speed;
    private java.lang.String tripMeter1;
    private java.lang.String tripMeter2;

    public VehicleRunningStatus() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 262143, null);
    }

    public VehicleRunningStatus(java.lang.String str) {
        this(str, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 262142, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2) {
        this(str, str2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 262140, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        this(str, str2, str3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 262136, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        this(str, str2, str3, str4, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 262128, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5) {
        this(str, str2, str3, str4, str5, null, null, null, null, null, null, null, null, null, null, null, null, null, 262112, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6) {
        this(str, str2, str3, str4, str5, str6, null, null, null, null, null, null, null, null, null, null, null, null, 262080, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7) {
        this(str, str2, str3, str4, str5, str6, str7, null, null, null, null, null, null, null, null, null, null, null, 262016, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8) {
        this(str, str2, str3, str4, str5, str6, str7, str8, null, null, null, null, null, null, null, null, null, null, 261888, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, null, null, null, null, null, null, null, null, null, 261632, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, null, null, null, null, null, null, null, null, 261120, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, null, null, null, null, null, null, null, 260096, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, null, null, null, null, null, null, 258048, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, null, null, null, null, null, 253952, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, null, null, null, null, 245760, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, null, null, null, 229376, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, null, null, 196608, null);
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, str17, null, 131072, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getAveFuelConsumption() {
        return this.aveFuelConsumption;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final java.lang.String getSpeed() {
        return this.speed;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final java.lang.String getTripMeter1() {
        return this.tripMeter1;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final java.lang.String getTripMeter2() {
        return this.tripMeter2;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final java.lang.String getAveTraFuelConsumption() {
        return this.aveTraFuelConsumption;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final java.lang.String getAverTraPowerConsumption() {
        return this.averTraPowerConsumption;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final java.lang.String getAveTraAlcoholConsumption() {
        return this.aveTraAlcoholConsumption;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final java.lang.String getMmiPwrcnsavgtabsts() {
        return this.mmiPwrcnsavgtabsts;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final java.lang.String getMmiPwrcnsavg50() {
        return this.mmiPwrcnsavg50;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final java.lang.String getMmiPwrcnsavg100() {
        return this.mmiPwrcnsavg100;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getAverPowerConsumption() {
        return this.averPowerConsumption;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.String getAvgSpeed() {
        return this.avgSpeed;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final java.lang.String getElectricParkBrakeStatus() {
        return this.electricParkBrakeStatus;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final java.lang.String getEngineSpeed() {
        return this.engineSpeed;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final java.lang.String getIndPowerConsumption() {
        return this.indPowerConsumption;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final java.lang.String getIndFuelConsumption() {
        return this.indFuelConsumption;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final java.lang.String getOdometer() {
        return this.odometer;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final java.lang.String getGearAutoStatus() {
        return this.gearAutoStatus;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus copy(java.lang.String aveFuelConsumption, java.lang.String averPowerConsumption, java.lang.String avgSpeed, java.lang.String electricParkBrakeStatus, java.lang.String engineSpeed, java.lang.String indPowerConsumption, java.lang.String indFuelConsumption, java.lang.String odometer, java.lang.String gearAutoStatus, java.lang.String speed, java.lang.String tripMeter1, java.lang.String tripMeter2, java.lang.String aveTraFuelConsumption, java.lang.String averTraPowerConsumption, java.lang.String aveTraAlcoholConsumption, java.lang.String mmiPwrcnsavgtabsts, java.lang.String mmiPwrcnsavg50, java.lang.String mmiPwrcnsavg100) {
        return new com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus(aveFuelConsumption, averPowerConsumption, avgSpeed, electricParkBrakeStatus, engineSpeed, indPowerConsumption, indFuelConsumption, odometer, gearAutoStatus, speed, tripMeter1, tripMeter2, aveTraFuelConsumption, averTraPowerConsumption, aveTraAlcoholConsumption, mmiPwrcnsavgtabsts, mmiPwrcnsavg50, mmiPwrcnsavg100);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus vehicleRunningStatus = (com.geely.cockpit.services.controlservice.bean.VehicleRunningStatus) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.aveFuelConsumption, vehicleRunningStatus.aveFuelConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.averPowerConsumption, vehicleRunningStatus.averPowerConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.avgSpeed, vehicleRunningStatus.avgSpeed) && kotlin.jvm.internal.Intrinsics.areEqual(this.electricParkBrakeStatus, vehicleRunningStatus.electricParkBrakeStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.engineSpeed, vehicleRunningStatus.engineSpeed) && kotlin.jvm.internal.Intrinsics.areEqual(this.indPowerConsumption, vehicleRunningStatus.indPowerConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.indFuelConsumption, vehicleRunningStatus.indFuelConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.odometer, vehicleRunningStatus.odometer) && kotlin.jvm.internal.Intrinsics.areEqual(this.gearAutoStatus, vehicleRunningStatus.gearAutoStatus) && kotlin.jvm.internal.Intrinsics.areEqual(this.speed, vehicleRunningStatus.speed) && kotlin.jvm.internal.Intrinsics.areEqual(this.tripMeter1, vehicleRunningStatus.tripMeter1) && kotlin.jvm.internal.Intrinsics.areEqual(this.tripMeter2, vehicleRunningStatus.tripMeter2) && kotlin.jvm.internal.Intrinsics.areEqual(this.aveTraFuelConsumption, vehicleRunningStatus.aveTraFuelConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.averTraPowerConsumption, vehicleRunningStatus.averTraPowerConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.aveTraAlcoholConsumption, vehicleRunningStatus.aveTraAlcoholConsumption) && kotlin.jvm.internal.Intrinsics.areEqual(this.mmiPwrcnsavgtabsts, vehicleRunningStatus.mmiPwrcnsavgtabsts) && kotlin.jvm.internal.Intrinsics.areEqual(this.mmiPwrcnsavg50, vehicleRunningStatus.mmiPwrcnsavg50) && kotlin.jvm.internal.Intrinsics.areEqual(this.mmiPwrcnsavg100, vehicleRunningStatus.mmiPwrcnsavg100);
    }

    public int hashCode() {
        java.lang.String str = this.aveFuelConsumption;
        int iHashCode = (str == null ? 0 : str.hashCode()) * 31;
        java.lang.String str2 = this.averPowerConsumption;
        int iHashCode2 = (iHashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.String str3 = this.avgSpeed;
        int iHashCode3 = (iHashCode2 + (str3 == null ? 0 : str3.hashCode())) * 31;
        java.lang.String str4 = this.electricParkBrakeStatus;
        int iHashCode4 = (iHashCode3 + (str4 == null ? 0 : str4.hashCode())) * 31;
        java.lang.String str5 = this.engineSpeed;
        int iHashCode5 = (iHashCode4 + (str5 == null ? 0 : str5.hashCode())) * 31;
        java.lang.String str6 = this.indPowerConsumption;
        int iHashCode6 = (iHashCode5 + (str6 == null ? 0 : str6.hashCode())) * 31;
        java.lang.String str7 = this.indFuelConsumption;
        int iHashCode7 = (iHashCode6 + (str7 == null ? 0 : str7.hashCode())) * 31;
        java.lang.String str8 = this.odometer;
        int iHashCode8 = (iHashCode7 + (str8 == null ? 0 : str8.hashCode())) * 31;
        java.lang.String str9 = this.gearAutoStatus;
        int iHashCode9 = (iHashCode8 + (str9 == null ? 0 : str9.hashCode())) * 31;
        java.lang.String str10 = this.speed;
        int iHashCode10 = (iHashCode9 + (str10 == null ? 0 : str10.hashCode())) * 31;
        java.lang.String str11 = this.tripMeter1;
        int iHashCode11 = (iHashCode10 + (str11 == null ? 0 : str11.hashCode())) * 31;
        java.lang.String str12 = this.tripMeter2;
        int iHashCode12 = (iHashCode11 + (str12 == null ? 0 : str12.hashCode())) * 31;
        java.lang.String str13 = this.aveTraFuelConsumption;
        int iHashCode13 = (iHashCode12 + (str13 == null ? 0 : str13.hashCode())) * 31;
        java.lang.String str14 = this.averTraPowerConsumption;
        int iHashCode14 = (iHashCode13 + (str14 == null ? 0 : str14.hashCode())) * 31;
        java.lang.String str15 = this.aveTraAlcoholConsumption;
        int iHashCode15 = (iHashCode14 + (str15 == null ? 0 : str15.hashCode())) * 31;
        java.lang.String str16 = this.mmiPwrcnsavgtabsts;
        int iHashCode16 = (iHashCode15 + (str16 == null ? 0 : str16.hashCode())) * 31;
        java.lang.String str17 = this.mmiPwrcnsavg50;
        int iHashCode17 = (iHashCode16 + (str17 == null ? 0 : str17.hashCode())) * 31;
        java.lang.String str18 = this.mmiPwrcnsavg100;
        return iHashCode17 + (str18 != null ? str18.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "VehicleRunningStatus(aveFuelConsumption=" + this.aveFuelConsumption + ", averPowerConsumption=" + this.averPowerConsumption + ", avgSpeed=" + this.avgSpeed + ", electricParkBrakeStatus=" + this.electricParkBrakeStatus + ", engineSpeed=" + this.engineSpeed + ", indPowerConsumption=" + this.indPowerConsumption + ", indFuelConsumption=" + this.indFuelConsumption + ", odometer=" + this.odometer + ", gearAutoStatus=" + this.gearAutoStatus + ", speed=" + this.speed + ", tripMeter1=" + this.tripMeter1 + ", tripMeter2=" + this.tripMeter2 + ", aveTraFuelConsumption=" + this.aveTraFuelConsumption + ", averTraPowerConsumption=" + this.averTraPowerConsumption + ", aveTraAlcoholConsumption=" + this.aveTraAlcoholConsumption + ", mmiPwrcnsavgtabsts=" + this.mmiPwrcnsavgtabsts + ", mmiPwrcnsavg50=" + this.mmiPwrcnsavg50 + ", mmiPwrcnsavg100=" + this.mmiPwrcnsavg100 + ")";
    }

    public VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18) {
        this.aveFuelConsumption = str;
        this.averPowerConsumption = str2;
        this.avgSpeed = str3;
        this.electricParkBrakeStatus = str4;
        this.engineSpeed = str5;
        this.indPowerConsumption = str6;
        this.indFuelConsumption = str7;
        this.odometer = str8;
        this.gearAutoStatus = str9;
        this.speed = str10;
        this.tripMeter1 = str11;
        this.tripMeter2 = str12;
        this.aveTraFuelConsumption = str13;
        this.averTraPowerConsumption = str14;
        this.aveTraAlcoholConsumption = str15;
        this.mmiPwrcnsavgtabsts = str16;
        this.mmiPwrcnsavg50 = str17;
        this.mmiPwrcnsavg100 = str18;
    }

    public /* synthetic */ VehicleRunningStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, java.lang.String str18, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6, (i & 64) != 0 ? null : str7, (i & 128) != 0 ? null : str8, (i & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? null : str9, (i & 512) != 0 ? null : str10, (i & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? null : str11, (i & 2048) != 0 ? null : str12, (i & 4096) != 0 ? null : str13, (i & 8192) != 0 ? null : str14, (i & 16384) != 0 ? null : str15, (i & 32768) != 0 ? null : str16, (i & 65536) != 0 ? null : str17, (i & 131072) != 0 ? null : str18);
    }

    public final java.lang.String getAveFuelConsumption() {
        return this.aveFuelConsumption;
    }

    public final void setAveFuelConsumption(java.lang.String str) {
        this.aveFuelConsumption = str;
    }

    public final java.lang.String getAverPowerConsumption() {
        return this.averPowerConsumption;
    }

    public final void setAverPowerConsumption(java.lang.String str) {
        this.averPowerConsumption = str;
    }

    public final java.lang.String getAvgSpeed() {
        return this.avgSpeed;
    }

    public final void setAvgSpeed(java.lang.String str) {
        this.avgSpeed = str;
    }

    public final java.lang.String getElectricParkBrakeStatus() {
        return this.electricParkBrakeStatus;
    }

    public final void setElectricParkBrakeStatus(java.lang.String str) {
        this.electricParkBrakeStatus = str;
    }

    public final java.lang.String getEngineSpeed() {
        return this.engineSpeed;
    }

    public final void setEngineSpeed(java.lang.String str) {
        this.engineSpeed = str;
    }

    public final java.lang.String getIndPowerConsumption() {
        return this.indPowerConsumption;
    }

    public final void setIndPowerConsumption(java.lang.String str) {
        this.indPowerConsumption = str;
    }

    public final java.lang.String getIndFuelConsumption() {
        return this.indFuelConsumption;
    }

    public final void setIndFuelConsumption(java.lang.String str) {
        this.indFuelConsumption = str;
    }

    public final java.lang.String getOdometer() {
        return this.odometer;
    }

    public final void setOdometer(java.lang.String str) {
        this.odometer = str;
    }

    public final java.lang.String getGearAutoStatus() {
        return this.gearAutoStatus;
    }

    public final void setGearAutoStatus(java.lang.String str) {
        this.gearAutoStatus = str;
    }

    public final java.lang.String getSpeed() {
        return this.speed;
    }

    public final void setSpeed(java.lang.String str) {
        this.speed = str;
    }

    public final java.lang.String getTripMeter1() {
        return this.tripMeter1;
    }

    public final void setTripMeter1(java.lang.String str) {
        this.tripMeter1 = str;
    }

    public final java.lang.String getTripMeter2() {
        return this.tripMeter2;
    }

    public final void setTripMeter2(java.lang.String str) {
        this.tripMeter2 = str;
    }

    public final java.lang.String getAveTraFuelConsumption() {
        return this.aveTraFuelConsumption;
    }

    public final void setAveTraFuelConsumption(java.lang.String str) {
        this.aveTraFuelConsumption = str;
    }

    public final java.lang.String getAverTraPowerConsumption() {
        return this.averTraPowerConsumption;
    }

    public final void setAverTraPowerConsumption(java.lang.String str) {
        this.averTraPowerConsumption = str;
    }

    public final java.lang.String getAveTraAlcoholConsumption() {
        return this.aveTraAlcoholConsumption;
    }

    public final void setAveTraAlcoholConsumption(java.lang.String str) {
        this.aveTraAlcoholConsumption = str;
    }

    public final java.lang.String getMmiPwrcnsavgtabsts() {
        return this.mmiPwrcnsavgtabsts;
    }

    public final void setMmiPwrcnsavgtabsts(java.lang.String str) {
        this.mmiPwrcnsavgtabsts = str;
    }

    public final java.lang.String getMmiPwrcnsavg50() {
        return this.mmiPwrcnsavg50;
    }

    public final void setMmiPwrcnsavg50(java.lang.String str) {
        this.mmiPwrcnsavg50 = str;
    }

    public final java.lang.String getMmiPwrcnsavg100() {
        return this.mmiPwrcnsavg100;
    }

    public final void setMmiPwrcnsavg100(java.lang.String str) {
        this.mmiPwrcnsavg100 = str;
    }
}
