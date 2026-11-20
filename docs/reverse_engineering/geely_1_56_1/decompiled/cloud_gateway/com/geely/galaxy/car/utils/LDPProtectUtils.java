package com.geely.galaxy.car.utils;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
public class LDPProtectUtils {
    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getChargeSdkAppSecret();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getChargeSdkAppid();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getChargeSdkClientId();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspAppSecret();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspAppSecretNew();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspAppid();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspAppidNew();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspGateway();

    @com.geely.core.common.protect.LDPProtect
    public static native java.lang.String getUspGatewayNew();

    /* JADX INFO: renamed from: com.geely.galaxy.car.utils.LDPProtectUtils$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$geely$swannotation$base$AppEnvKit;

        static {
            int[] iArr = new int[com.geely.swannotation.base.AppEnvKit.values().length];
            $SwitchMap$com$geely$swannotation$base$AppEnvKit = iArr;
            try {
                iArr[com.geely.swannotation.base.AppEnvKit.Release.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$geely$swannotation$base$AppEnvKit[com.geely.swannotation.base.AppEnvKit.Product.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$geely$swannotation$base$AppEnvKit[com.geely.swannotation.base.AppEnvKit.Dev.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$geely$swannotation$base$AppEnvKit[com.geely.swannotation.base.AppEnvKit.SIT.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$geely$swannotation$base$AppEnvKit[com.geely.swannotation.base.AppEnvKit.TRA.ordinal()] = 5;
            } catch (java.lang.NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$geely$swannotation$base$AppEnvKit[com.geely.swannotation.base.AppEnvKit.UAT.ordinal()] = 6;
            } catch (java.lang.NoSuchFieldError unused6) {
            }
        }
    }
}
