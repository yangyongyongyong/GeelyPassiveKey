package com.geely.rxhttp.base;

/* JADX INFO: compiled from: SWGateWayConfig.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0010B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u0004\u0018\u00010\u0004J\b\u0010\f\u001a\u0004\u0018\u00010\u0004J\u0011\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007¢\u0006\u0002\u0010\u000eJ\u0006\u0010\u000f\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/geely/rxhttp/base/SWGateWayConfig;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "appKey", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "appSecret", "extraHeaderToSign", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "[Ljava/lang/String;", "printConsole", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "getAppKey", "getAppSecureKey", "getExtraHeaderToSign", "()[Ljava/lang/String;", "isPrintConsole", "Builder", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SWGateWayConfig {
    private java.lang.String appKey;
    private java.lang.String appSecret;
    private java.lang.String[] extraHeaderToSign = new java.lang.String[0];
    private boolean printConsole = true;

    public final java.lang.String getAppKey() {
        return this.appKey;
    }

    /* JADX INFO: renamed from: getAppSecureKey, reason: from getter */
    public final java.lang.String getAppSecret() {
        return this.appSecret;
    }

    public final java.lang.String[] getExtraHeaderToSign() {
        return this.extraHeaderToSign;
    }

    /* JADX INFO: renamed from: isPrintConsole, reason: from getter */
    public final boolean getPrintConsole() {
        return this.printConsole;
    }

    /* JADX INFO: compiled from: SWGateWayConfig.kt */
    @kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0015\u0010\u000f\u001a\u00020\u00002\b\u0010\u0010\u001a\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\u00002\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\b¢\u0006\u0002\u0010\u0013J\u001a\u0010\u0014\u001a\u00020\u00002\b\u0010\u0015\u001a\u0004\u0018\u00010\u00042\b\u0010\u0016\u001a\u0004\u0018\u00010\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/geely/rxhttp/base/SWGateWayConfig$Builder;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "appKey", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "appSecret", "endPoint", "extraHeaderToSign", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "[Ljava/lang/String;", "printConsole", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "store", "create", "Lcom/geely/rxhttp/base/SWGateWayConfig;", "openPrintConsole", "enable", "(Ljava/lang/Boolean;)Lcom/geely/rxhttp/base/SWGateWayConfig$Builder;", "setExtraHeader", "([Ljava/lang/String;)Lcom/geely/rxhttp/base/SWGateWayConfig$Builder;", "setKeyEntry", "ak", "sk", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Builder {
        private java.lang.String appKey;
        private java.lang.String appSecret;
        private java.lang.String endPoint;
        private java.lang.String[] extraHeaderToSign = new java.lang.String[0];
        private boolean printConsole = true;
        private java.lang.String store;

        public final com.geely.rxhttp.base.SWGateWayConfig.Builder setKeyEntry(java.lang.String ak, java.lang.String sk) {
            this.appKey = ak;
            this.appSecret = sk;
            return this;
        }

        public final com.geely.rxhttp.base.SWGateWayConfig.Builder setExtraHeader(java.lang.String[] extraHeaderToSign) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(extraHeaderToSign, "extraHeaderToSign");
            this.extraHeaderToSign = extraHeaderToSign;
            return this;
        }

        public final com.geely.rxhttp.base.SWGateWayConfig.Builder openPrintConsole(java.lang.Boolean enable) {
            this.printConsole = enable == null ? true : enable.booleanValue();
            return this;
        }

        public final com.geely.rxhttp.base.SWGateWayConfig create() {
            com.geely.rxhttp.base.SWGateWayConfig sWGateWayConfig = new com.geely.rxhttp.base.SWGateWayConfig();
            sWGateWayConfig.appKey = this.appKey;
            sWGateWayConfig.appSecret = this.appSecret;
            sWGateWayConfig.extraHeaderToSign = this.extraHeaderToSign;
            sWGateWayConfig.printConsole = this.printConsole;
            return sWGateWayConfig;
        }
    }
}
