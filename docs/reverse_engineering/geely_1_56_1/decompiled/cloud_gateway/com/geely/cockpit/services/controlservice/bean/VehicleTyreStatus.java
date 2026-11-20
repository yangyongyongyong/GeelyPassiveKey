package com.geely.cockpit.services.controlservice.bean;

/* JADX INFO: compiled from: PlatformVehicleStatus.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\bH\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BÓ\u0001\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0014J\u000b\u00109\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010@\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010D\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010E\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010F\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010G\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010I\u001a\u0004\u0018\u00010\u0003HÆ\u0003JÕ\u0001\u0010J\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010K\u001a\u00020L2\b\u0010M\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010N\u001a\u00020OHÖ\u0001J\t\u0010P\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0016\"\u0004\b\u001a\u0010\u0018R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0016\"\u0004\b\u001c\u0010\u0018R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0016\"\u0004\b\u001e\u0010\u0018R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0016\"\u0004\b \u0010\u0018R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0016\"\u0004\b\"\u0010\u0018R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0016\"\u0004\b$\u0010\u0018R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0016\"\u0004\b&\u0010\u0018R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0016\"\u0004\b(\u0010\u0018R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0016\"\u0004\b*\u0010\u0018R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0016\"\u0004\b,\u0010\u0018R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0016\"\u0004\b.\u0010\u0018R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0016\"\u0004\b0\u0010\u0018R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0016\"\u0004\b2\u0010\u0018R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0016\"\u0004\b4\u0010\u0018R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0016\"\u0004\b6\u0010\u0018R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0016\"\u0004\b8\u0010\u0018¨\u0006Q"}, d2 = {"Lcom/geely/cockpit/services/controlservice/bean/VehicleTyreStatus;", "", "tyrePreWarningDriver", "", "tyrePreWarningDriverRear", "tyrePreWarningPassenger", "tyrePreWarningPassengerRear", "tyreStatusDriver", "tyreStatusDriverRear", "tyreStatusPassenger", "tyreStatusPassengerRear", "tyreUnit", "tyreTempDriver", "tyreTempDriverRear", "tyreTempPassenger", "tyreTempPassengerRear", "tyreTempWarningDriver", "tyreTempWarningPassenger", "tyreTempWarningDriverRear", "tyreTempWarningPassengerRear", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getTyrePreWarningDriver", "()Ljava/lang/String;", "setTyrePreWarningDriver", "(Ljava/lang/String;)V", "getTyrePreWarningDriverRear", "setTyrePreWarningDriverRear", "getTyrePreWarningPassenger", "setTyrePreWarningPassenger", "getTyrePreWarningPassengerRear", "setTyrePreWarningPassengerRear", "getTyreStatusDriver", "setTyreStatusDriver", "getTyreStatusDriverRear", "setTyreStatusDriverRear", "getTyreStatusPassenger", "setTyreStatusPassenger", "getTyreStatusPassengerRear", "setTyreStatusPassengerRear", "getTyreTempDriver", "setTyreTempDriver", "getTyreTempDriverRear", "setTyreTempDriverRear", "getTyreTempPassenger", "setTyreTempPassenger", "getTyreTempPassengerRear", "setTyreTempPassengerRear", "getTyreTempWarningDriver", "setTyreTempWarningDriver", "getTyreTempWarningDriverRear", "setTyreTempWarningDriverRear", "getTyreTempWarningPassenger", "setTyreTempWarningPassenger", "getTyreTempWarningPassengerRear", "setTyreTempWarningPassengerRear", "getTyreUnit", "setTyreUnit", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class VehicleTyreStatus {
    private java.lang.String tyrePreWarningDriver;
    private java.lang.String tyrePreWarningDriverRear;
    private java.lang.String tyrePreWarningPassenger;
    private java.lang.String tyrePreWarningPassengerRear;
    private java.lang.String tyreStatusDriver;
    private java.lang.String tyreStatusDriverRear;
    private java.lang.String tyreStatusPassenger;
    private java.lang.String tyreStatusPassengerRear;
    private java.lang.String tyreTempDriver;
    private java.lang.String tyreTempDriverRear;
    private java.lang.String tyreTempPassenger;
    private java.lang.String tyreTempPassengerRear;
    private java.lang.String tyreTempWarningDriver;
    private java.lang.String tyreTempWarningDriverRear;
    private java.lang.String tyreTempWarningPassenger;
    private java.lang.String tyreTempWarningPassengerRear;
    private java.lang.String tyreUnit;

    public VehicleTyreStatus() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 131071, null);
    }

    public VehicleTyreStatus(java.lang.String str) {
        this(str, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 131070, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2) {
        this(str, str2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 131068, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        this(str, str2, str3, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 131064, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
        this(str, str2, str3, str4, null, null, null, null, null, null, null, null, null, null, null, null, null, 131056, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5) {
        this(str, str2, str3, str4, str5, null, null, null, null, null, null, null, null, null, null, null, null, 131040, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6) {
        this(str, str2, str3, str4, str5, str6, null, null, null, null, null, null, null, null, null, null, null, 131008, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7) {
        this(str, str2, str3, str4, str5, str6, str7, null, null, null, null, null, null, null, null, null, null, 130944, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8) {
        this(str, str2, str3, str4, str5, str6, str7, str8, null, null, null, null, null, null, null, null, null, 130816, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, null, null, null, null, null, null, null, null, 130560, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, null, null, null, null, null, null, null, 130048, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, null, null, null, null, null, null, 129024, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, null, null, null, null, null, 126976, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, null, null, null, null, 122880, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, null, null, null, 114688, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, null, null, 98304, null);
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16) {
        this(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, null, 65536, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getTyrePreWarningDriver() {
        return this.tyrePreWarningDriver;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final java.lang.String getTyreTempDriver() {
        return this.tyreTempDriver;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final java.lang.String getTyreTempDriverRear() {
        return this.tyreTempDriverRear;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final java.lang.String getTyreTempPassenger() {
        return this.tyreTempPassenger;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final java.lang.String getTyreTempPassengerRear() {
        return this.tyreTempPassengerRear;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final java.lang.String getTyreTempWarningDriver() {
        return this.tyreTempWarningDriver;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final java.lang.String getTyreTempWarningPassenger() {
        return this.tyreTempWarningPassenger;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final java.lang.String getTyreTempWarningDriverRear() {
        return this.tyreTempWarningDriverRear;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final java.lang.String getTyreTempWarningPassengerRear() {
        return this.tyreTempWarningPassengerRear;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getTyrePreWarningDriverRear() {
        return this.tyrePreWarningDriverRear;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.String getTyrePreWarningPassenger() {
        return this.tyrePreWarningPassenger;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final java.lang.String getTyrePreWarningPassengerRear() {
        return this.tyrePreWarningPassengerRear;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final java.lang.String getTyreStatusDriver() {
        return this.tyreStatusDriver;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final java.lang.String getTyreStatusDriverRear() {
        return this.tyreStatusDriverRear;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final java.lang.String getTyreStatusPassenger() {
        return this.tyreStatusPassenger;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final java.lang.String getTyreStatusPassengerRear() {
        return this.tyreStatusPassengerRear;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final java.lang.String getTyreUnit() {
        return this.tyreUnit;
    }

    public final com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus copy(java.lang.String tyrePreWarningDriver, java.lang.String tyrePreWarningDriverRear, java.lang.String tyrePreWarningPassenger, java.lang.String tyrePreWarningPassengerRear, java.lang.String tyreStatusDriver, java.lang.String tyreStatusDriverRear, java.lang.String tyreStatusPassenger, java.lang.String tyreStatusPassengerRear, java.lang.String tyreUnit, java.lang.String tyreTempDriver, java.lang.String tyreTempDriverRear, java.lang.String tyreTempPassenger, java.lang.String tyreTempPassengerRear, java.lang.String tyreTempWarningDriver, java.lang.String tyreTempWarningPassenger, java.lang.String tyreTempWarningDriverRear, java.lang.String tyreTempWarningPassengerRear) {
        return new com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus(tyrePreWarningDriver, tyrePreWarningDriverRear, tyrePreWarningPassenger, tyrePreWarningPassengerRear, tyreStatusDriver, tyreStatusDriverRear, tyreStatusPassenger, tyreStatusPassengerRear, tyreUnit, tyreTempDriver, tyreTempDriverRear, tyreTempPassenger, tyreTempPassengerRear, tyreTempWarningDriver, tyreTempWarningPassenger, tyreTempWarningDriverRear, tyreTempWarningPassengerRear);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus vehicleTyreStatus = (com.geely.cockpit.services.controlservice.bean.VehicleTyreStatus) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.tyrePreWarningDriver, vehicleTyreStatus.tyrePreWarningDriver) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyrePreWarningDriverRear, vehicleTyreStatus.tyrePreWarningDriverRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyrePreWarningPassenger, vehicleTyreStatus.tyrePreWarningPassenger) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyrePreWarningPassengerRear, vehicleTyreStatus.tyrePreWarningPassengerRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreStatusDriver, vehicleTyreStatus.tyreStatusDriver) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreStatusDriverRear, vehicleTyreStatus.tyreStatusDriverRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreStatusPassenger, vehicleTyreStatus.tyreStatusPassenger) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreStatusPassengerRear, vehicleTyreStatus.tyreStatusPassengerRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreUnit, vehicleTyreStatus.tyreUnit) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempDriver, vehicleTyreStatus.tyreTempDriver) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempDriverRear, vehicleTyreStatus.tyreTempDriverRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempPassenger, vehicleTyreStatus.tyreTempPassenger) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempPassengerRear, vehicleTyreStatus.tyreTempPassengerRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempWarningDriver, vehicleTyreStatus.tyreTempWarningDriver) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempWarningPassenger, vehicleTyreStatus.tyreTempWarningPassenger) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempWarningDriverRear, vehicleTyreStatus.tyreTempWarningDriverRear) && kotlin.jvm.internal.Intrinsics.areEqual(this.tyreTempWarningPassengerRear, vehicleTyreStatus.tyreTempWarningPassengerRear);
    }

    public int hashCode() {
        java.lang.String str = this.tyrePreWarningDriver;
        int iHashCode = (str == null ? 0 : str.hashCode()) * 31;
        java.lang.String str2 = this.tyrePreWarningDriverRear;
        int iHashCode2 = (iHashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.String str3 = this.tyrePreWarningPassenger;
        int iHashCode3 = (iHashCode2 + (str3 == null ? 0 : str3.hashCode())) * 31;
        java.lang.String str4 = this.tyrePreWarningPassengerRear;
        int iHashCode4 = (iHashCode3 + (str4 == null ? 0 : str4.hashCode())) * 31;
        java.lang.String str5 = this.tyreStatusDriver;
        int iHashCode5 = (iHashCode4 + (str5 == null ? 0 : str5.hashCode())) * 31;
        java.lang.String str6 = this.tyreStatusDriverRear;
        int iHashCode6 = (iHashCode5 + (str6 == null ? 0 : str6.hashCode())) * 31;
        java.lang.String str7 = this.tyreStatusPassenger;
        int iHashCode7 = (iHashCode6 + (str7 == null ? 0 : str7.hashCode())) * 31;
        java.lang.String str8 = this.tyreStatusPassengerRear;
        int iHashCode8 = (iHashCode7 + (str8 == null ? 0 : str8.hashCode())) * 31;
        java.lang.String str9 = this.tyreUnit;
        int iHashCode9 = (iHashCode8 + (str9 == null ? 0 : str9.hashCode())) * 31;
        java.lang.String str10 = this.tyreTempDriver;
        int iHashCode10 = (iHashCode9 + (str10 == null ? 0 : str10.hashCode())) * 31;
        java.lang.String str11 = this.tyreTempDriverRear;
        int iHashCode11 = (iHashCode10 + (str11 == null ? 0 : str11.hashCode())) * 31;
        java.lang.String str12 = this.tyreTempPassenger;
        int iHashCode12 = (iHashCode11 + (str12 == null ? 0 : str12.hashCode())) * 31;
        java.lang.String str13 = this.tyreTempPassengerRear;
        int iHashCode13 = (iHashCode12 + (str13 == null ? 0 : str13.hashCode())) * 31;
        java.lang.String str14 = this.tyreTempWarningDriver;
        int iHashCode14 = (iHashCode13 + (str14 == null ? 0 : str14.hashCode())) * 31;
        java.lang.String str15 = this.tyreTempWarningPassenger;
        int iHashCode15 = (iHashCode14 + (str15 == null ? 0 : str15.hashCode())) * 31;
        java.lang.String str16 = this.tyreTempWarningDriverRear;
        int iHashCode16 = (iHashCode15 + (str16 == null ? 0 : str16.hashCode())) * 31;
        java.lang.String str17 = this.tyreTempWarningPassengerRear;
        return iHashCode16 + (str17 != null ? str17.hashCode() : 0);
    }

    public java.lang.String toString() {
        return "VehicleTyreStatus(tyrePreWarningDriver=" + this.tyrePreWarningDriver + ", tyrePreWarningDriverRear=" + this.tyrePreWarningDriverRear + ", tyrePreWarningPassenger=" + this.tyrePreWarningPassenger + ", tyrePreWarningPassengerRear=" + this.tyrePreWarningPassengerRear + ", tyreStatusDriver=" + this.tyreStatusDriver + ", tyreStatusDriverRear=" + this.tyreStatusDriverRear + ", tyreStatusPassenger=" + this.tyreStatusPassenger + ", tyreStatusPassengerRear=" + this.tyreStatusPassengerRear + ", tyreUnit=" + this.tyreUnit + ", tyreTempDriver=" + this.tyreTempDriver + ", tyreTempDriverRear=" + this.tyreTempDriverRear + ", tyreTempPassenger=" + this.tyreTempPassenger + ", tyreTempPassengerRear=" + this.tyreTempPassengerRear + ", tyreTempWarningDriver=" + this.tyreTempWarningDriver + ", tyreTempWarningPassenger=" + this.tyreTempWarningPassenger + ", tyreTempWarningDriverRear=" + this.tyreTempWarningDriverRear + ", tyreTempWarningPassengerRear=" + this.tyreTempWarningPassengerRear + ")";
    }

    public VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17) {
        this.tyrePreWarningDriver = str;
        this.tyrePreWarningDriverRear = str2;
        this.tyrePreWarningPassenger = str3;
        this.tyrePreWarningPassengerRear = str4;
        this.tyreStatusDriver = str5;
        this.tyreStatusDriverRear = str6;
        this.tyreStatusPassenger = str7;
        this.tyreStatusPassengerRear = str8;
        this.tyreUnit = str9;
        this.tyreTempDriver = str10;
        this.tyreTempDriverRear = str11;
        this.tyreTempPassenger = str12;
        this.tyreTempPassengerRear = str13;
        this.tyreTempWarningDriver = str14;
        this.tyreTempWarningPassenger = str15;
        this.tyreTempWarningDriverRear = str16;
        this.tyreTempWarningPassengerRear = str17;
    }

    public /* synthetic */ VehicleTyreStatus(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, java.lang.String str9, java.lang.String str10, java.lang.String str11, java.lang.String str12, java.lang.String str13, java.lang.String str14, java.lang.String str15, java.lang.String str16, java.lang.String str17, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6, (i & 64) != 0 ? null : str7, (i & 128) != 0 ? null : str8, (i & com.ecarx.iwallsdk.crypto.param.CryptParam.ECC_KEY_LEN) != 0 ? "0" : str9, (i & 512) != 0 ? null : str10, (i & com.carlinx.module_show.ui.fragment.ImportMusicFragment.VIDEO_REQUEST_CODE) != 0 ? null : str11, (i & 2048) != 0 ? null : str12, (i & 4096) != 0 ? null : str13, (i & 8192) != 0 ? null : str14, (i & 16384) != 0 ? null : str15, (i & 32768) != 0 ? null : str16, (i & 65536) != 0 ? null : str17);
    }

    public final java.lang.String getTyrePreWarningDriver() {
        return this.tyrePreWarningDriver;
    }

    public final void setTyrePreWarningDriver(java.lang.String str) {
        this.tyrePreWarningDriver = str;
    }

    public final java.lang.String getTyrePreWarningDriverRear() {
        return this.tyrePreWarningDriverRear;
    }

    public final void setTyrePreWarningDriverRear(java.lang.String str) {
        this.tyrePreWarningDriverRear = str;
    }

    public final java.lang.String getTyrePreWarningPassenger() {
        return this.tyrePreWarningPassenger;
    }

    public final void setTyrePreWarningPassenger(java.lang.String str) {
        this.tyrePreWarningPassenger = str;
    }

    public final java.lang.String getTyrePreWarningPassengerRear() {
        return this.tyrePreWarningPassengerRear;
    }

    public final void setTyrePreWarningPassengerRear(java.lang.String str) {
        this.tyrePreWarningPassengerRear = str;
    }

    public final java.lang.String getTyreStatusDriver() {
        return this.tyreStatusDriver;
    }

    public final void setTyreStatusDriver(java.lang.String str) {
        this.tyreStatusDriver = str;
    }

    public final java.lang.String getTyreStatusDriverRear() {
        return this.tyreStatusDriverRear;
    }

    public final void setTyreStatusDriverRear(java.lang.String str) {
        this.tyreStatusDriverRear = str;
    }

    public final java.lang.String getTyreStatusPassenger() {
        return this.tyreStatusPassenger;
    }

    public final void setTyreStatusPassenger(java.lang.String str) {
        this.tyreStatusPassenger = str;
    }

    public final java.lang.String getTyreStatusPassengerRear() {
        return this.tyreStatusPassengerRear;
    }

    public final void setTyreStatusPassengerRear(java.lang.String str) {
        this.tyreStatusPassengerRear = str;
    }

    public final java.lang.String getTyreUnit() {
        return this.tyreUnit;
    }

    public final void setTyreUnit(java.lang.String str) {
        this.tyreUnit = str;
    }

    public final java.lang.String getTyreTempDriver() {
        return this.tyreTempDriver;
    }

    public final void setTyreTempDriver(java.lang.String str) {
        this.tyreTempDriver = str;
    }

    public final java.lang.String getTyreTempDriverRear() {
        return this.tyreTempDriverRear;
    }

    public final void setTyreTempDriverRear(java.lang.String str) {
        this.tyreTempDriverRear = str;
    }

    public final java.lang.String getTyreTempPassenger() {
        return this.tyreTempPassenger;
    }

    public final void setTyreTempPassenger(java.lang.String str) {
        this.tyreTempPassenger = str;
    }

    public final java.lang.String getTyreTempPassengerRear() {
        return this.tyreTempPassengerRear;
    }

    public final void setTyreTempPassengerRear(java.lang.String str) {
        this.tyreTempPassengerRear = str;
    }

    public final java.lang.String getTyreTempWarningDriver() {
        return this.tyreTempWarningDriver;
    }

    public final void setTyreTempWarningDriver(java.lang.String str) {
        this.tyreTempWarningDriver = str;
    }

    public final java.lang.String getTyreTempWarningPassenger() {
        return this.tyreTempWarningPassenger;
    }

    public final void setTyreTempWarningPassenger(java.lang.String str) {
        this.tyreTempWarningPassenger = str;
    }

    public final java.lang.String getTyreTempWarningDriverRear() {
        return this.tyreTempWarningDriverRear;
    }

    public final void setTyreTempWarningDriverRear(java.lang.String str) {
        this.tyreTempWarningDriverRear = str;
    }

    public final java.lang.String getTyreTempWarningPassengerRear() {
        return this.tyreTempWarningPassengerRear;
    }

    public final void setTyreTempWarningPassengerRear(java.lang.String str) {
        this.tyreTempWarningPassengerRear = str;
    }
}
