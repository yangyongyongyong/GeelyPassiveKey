package com.geely.rxhttp;

/* JADX INFO: compiled from: SWHttpComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0012\u0010\u0004\u001a\u00020\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H&J&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\b\b\u0000\u0010\t*\u00020\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\u000bH&J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\f0\b2\b\u0010\r\u001a\u0004\u0018\u00010\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\fH&J\b\u0010\u000f\u001a\u00020\u0010H&J(\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\f2\b\u0010\u0014\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\fH&J\u0010\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0018H&¨\u0006\u0019"}, d2 = {"Lcom/geely/rxhttp/ISWHttpProxy;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "addDefaultTokenInterceptor", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "addInterceptor", "interceptor", "Lokhttp3/Interceptor;", "execRequest", "Lcom/geely/rxhttp/base/IHttpResponse;", "T", "request", "Lcom/geely/rxhttp/base/IBaseRequest;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "url", "json", "getHttpClient", "Lokhttp3/OkHttpClient;", "initGateWay", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "appKey", "appSecret", com.geely.rxhttp.base.Constants.KEY_NAME_APPCODE, "setConnectTimeout", "timeout", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public interface ISWHttpProxy {
    void addDefaultTokenInterceptor();

    void addInterceptor(okhttp3.Interceptor interceptor);

    <T> com.geely.rxhttp.base.IHttpResponse<T> execRequest(com.geely.rxhttp.base.IBaseRequest<T> request);

    com.geely.rxhttp.base.IHttpResponse<java.lang.String> execRequest(java.lang.String url, java.lang.String json);

    okhttp3.OkHttpClient getHttpClient();

    boolean initGateWay(java.lang.String appKey, java.lang.String appSecret, java.lang.String appCode);

    void setConnectTimeout(long timeout);

    /* JADX INFO: compiled from: SWHttpComponent.kt */
    @kotlin.Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ boolean initGateWay$default(com.geely.rxhttp.ISWHttpProxy iSWHttpProxy, java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
            if (obj != null) {
                throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: initGateWay");
            }
            if ((i & 4) != 0) {
                str3 = null;
            }
            return iSWHttpProxy.initGateWay(str, str2, str3);
        }
    }
}
