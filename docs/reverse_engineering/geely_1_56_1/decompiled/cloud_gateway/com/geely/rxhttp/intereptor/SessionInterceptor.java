package com.geely.rxhttp.intereptor;

/* JADX INFO: compiled from: SessionInterceptor.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00132\u00020\u0001:\u0002\u0013\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0002J\"\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\f2\b\u0010\u0012\u001a\u0004\u0018\u00010\fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/geely/rxhttp/intereptor/SessionInterceptor;", "Lokhttp3/Interceptor;", "tokenProvider", "Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;", "(Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "safeParseBoolean", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "requireToken", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "saveAddHeader", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "builder", "Lokhttp3/Request$Builder;", "key", "value", "Companion", "ITokenProvider", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SessionInterceptor implements okhttp3.Interceptor {
    public static final java.lang.String HEADER_PARAM_REQUIRE_SESSION = "x-requireToken";
    public static final java.lang.String HEADER_PARAM_SESSION_ID = "token";
    public static final java.lang.String HEADER_PARAM_SVCS_ID = "svcsid";
    private final com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider tokenProvider;

    /* JADX INFO: compiled from: SessionInterceptor.kt */
    @kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H&J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0005H&J \u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH&J\u001a\u0010\f\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0018\u00010\tH&J\n\u0010\r\u001a\u0004\u0018\u00010\u0005H&J\n\u0010\u000e\u001a\u0004\u0018\u00010\u0005H&J\u001c\u0010\u000f\u001a\u00020\u00102\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0011\u001a\u0004\u0018\u00010\u0005H&¨\u0006\u0012"}, d2 = {"Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "handleResponseCode", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "url", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "code", "provideBaseUrl", "provideHeader", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "request", "Lokhttp3/Request;", "provideHeaders", "provideSvcsid", "provideToken", "refreshToken", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "data", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public interface ITokenProvider {
        void handleResponseCode(java.lang.String url, java.lang.String code);

        java.lang.String provideBaseUrl();

        java.util.Map<java.lang.String, java.lang.String> provideHeader(okhttp3.Request request);

        java.util.Map<java.lang.String, java.lang.String> provideHeaders();

        java.lang.String provideSvcsid();

        java.lang.String provideToken();

        boolean refreshToken(java.lang.String url, java.lang.String data);
    }

    public SessionInterceptor(com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider iTokenProvider) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iTokenProvider, "tokenProvider");
        this.tokenProvider = iTokenProvider;
    }

    public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chain, "chain");
        okhttp3.Request request = chain.request();
        okhttp3.Request.Builder builderNewBuilder = request.newBuilder();
        if (safeParseBoolean(request.header("x-requireToken"))) {
            java.lang.String strHeader = request.header("token");
            if (strHeader == null || strHeader.length() == 0) {
                java.lang.String strProvideToken = this.tokenProvider.provideToken();
                java.lang.String str = strProvideToken;
                if (!(str == null || str.length() == 0)) {
                    kotlin.jvm.internal.Intrinsics.checkNotNull(strProvideToken);
                    builderNewBuilder.addHeader("token", strProvideToken);
                }
            }
            java.lang.String strProvideSvcsid = this.tokenProvider.provideSvcsid();
            if (strProvideSvcsid != null) {
                builderNewBuilder.addHeader(HEADER_PARAM_SVCS_ID, strProvideSvcsid);
            }
            builderNewBuilder.removeHeader("x-requireToken");
        }
        okhttp3.Response responseProceed = chain.proceed(builderNewBuilder.build());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseProceed, "chain.proceed(builder.build())");
        return responseProceed;
    }

    private final void saveAddHeader(okhttp3.Request.Builder builder, java.lang.String key, java.lang.String value) {
        try {
            java.lang.String str = value;
            if (str == null || str.length() == 0) {
                return;
            }
            builder.removeHeader(key);
            builder.addHeader(key, value);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    private final boolean safeParseBoolean(java.lang.String requireToken) {
        java.lang.Boolean boolValueOf;
        if (requireToken == null) {
            boolValueOf = null;
        } else {
            try {
                boolValueOf = java.lang.Boolean.valueOf(java.lang.Boolean.parseBoolean(requireToken));
            } catch (java.lang.Exception unused) {
                return false;
            }
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(boolValueOf);
        return boolValueOf.booleanValue();
    }
}
