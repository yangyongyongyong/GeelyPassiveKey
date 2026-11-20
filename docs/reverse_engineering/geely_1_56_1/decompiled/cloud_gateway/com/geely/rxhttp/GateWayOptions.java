package com.geely.rxhttp;

/* JADX INFO: compiled from: SWGatewayComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\"\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0012\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u0013"}, d2 = {"Lcom/geely/rxhttp/GateWayOptions;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "appKey", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "getAppKey", "()Ljava/lang/String;", "setAppKey", "(Ljava/lang/String;)V", "appSecret", "getAppSecret", "setAppSecret", "extraHeaderToSign", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "getExtraHeaderToSign", "()[Ljava/lang/String;", "setExtraHeaderToSign", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class GateWayOptions {
    private java.lang.String appKey;
    private java.lang.String appSecret;
    private java.lang.String[] extraHeaderToSign = new java.lang.String[0];

    public final java.lang.String getAppKey() {
        return this.appKey;
    }

    public final void setAppKey(java.lang.String str) {
        this.appKey = str;
    }

    public final java.lang.String getAppSecret() {
        return this.appSecret;
    }

    public final void setAppSecret(java.lang.String str) {
        this.appSecret = str;
    }

    public final java.lang.String[] getExtraHeaderToSign() {
        return this.extraHeaderToSign;
    }

    public final void setExtraHeaderToSign(java.lang.String[] strArr) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(strArr, "<set-?>");
        this.extraHeaderToSign = strArr;
    }
}
