package com.geely.cockpit.services.controlservice.runtime;

/* JADX INFO: compiled from: CurUspBaseUrl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004H\u0007R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u001e\u0010\n\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u001e\u0010\f\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/geely/cockpit/services/controlservice/runtime/CurUspBaseUrl;", "", "()V", "<set-?>", "", "USP_GATEWAY_CODE", "getUSP_GATEWAY_CODE$LibCarControlNew_release", "()Ljava/lang/String;", "USP_GATEWAY_CODE_NEW", "getUSP_GATEWAY_CODE_NEW$LibCarControlNew_release", "baseUrl", "getBaseUrl$LibCarControlNew_release", "baseUrlNew", "getBaseUrlNew$LibCarControlNew_release", "initSuccess", "Ljava/util/concurrent/atomic/AtomicBoolean;", "setupUrl", "", "uspGatewayCode", "uspGatewayCodeNew", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class CurUspBaseUrl {
    public static final com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl INSTANCE = new com.geely.cockpit.services.controlservice.runtime.CurUspBaseUrl();
    private static java.util.concurrent.atomic.AtomicBoolean initSuccess = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static java.lang.String baseUrl = "";
    private static java.lang.String baseUrlNew = "";
    private static java.lang.String USP_GATEWAY_CODE = "";
    private static java.lang.String USP_GATEWAY_CODE_NEW = "";

    private CurUspBaseUrl() {
    }

    public final java.lang.String getBaseUrl$LibCarControlNew_release() {
        return baseUrl;
    }

    public final java.lang.String getBaseUrlNew$LibCarControlNew_release() {
        return baseUrlNew;
    }

    public final java.lang.String getUSP_GATEWAY_CODE$LibCarControlNew_release() {
        return USP_GATEWAY_CODE;
    }

    public final java.lang.String getUSP_GATEWAY_CODE_NEW$LibCarControlNew_release() {
        return USP_GATEWAY_CODE_NEW;
    }

    @kotlin.jvm.JvmStatic
    public static final void setupUrl(java.lang.String baseUrl2, java.lang.String baseUrlNew2, java.lang.String uspGatewayCode, java.lang.String uspGatewayCodeNew) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseUrl2, "baseUrl");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseUrlNew2, "baseUrlNew");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uspGatewayCode, "uspGatewayCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uspGatewayCodeNew, "uspGatewayCodeNew");
        if (!(!initSuccess.get())) {
            throw new java.lang.IllegalStateException("you can initialize EnvType for once!!!".toString());
        }
        baseUrl = baseUrl2;
        baseUrlNew = baseUrlNew2;
        USP_GATEWAY_CODE = uspGatewayCode;
        USP_GATEWAY_CODE_NEW = uspGatewayCodeNew;
        initSuccess.set(true);
        com.geely.cockpit.services.controlservice.CarControlLogger.INSTANCE.d("环境配置", "CurUspBaseUrl::initialize: " + baseUrl2);
    }
}
