package com.geely.rxhttp;

/* JADX INFO: compiled from: SWGatewayComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 52\u00020\u00012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0002:\u00015B\u0005¢\u0006\u0002\u0010\u0005J\b\u0010\u000f\u001a\u00020\u000eH\u0002JT\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\b\b\u0000\u0010\u0012*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0018\u001a\u00020\u00152\u0016\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\b\u0018\u00010\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\bH\u0002J&\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u00120\u0011\"\b\b\u0000\u0010\u0012*\u00020\u00132\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00120\u001fH\u0016J\"\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u00112\b\u0010 \u001a\u0004\u0018\u00010\b2\b\u0010!\u001a\u0004\u0018\u00010\bH\u0016J1\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u00112\b\u0010 \u001a\u0004\u0018\u00010\b2\b\u0010!\u001a\u0004\u0018\u00010\b2\b\u0010\"\u001a\u0004\u0018\u00010\u000eH\u0016¢\u0006\u0002\u0010#J$\u0010$\u001a\u0004\u0018\u00010%\"\b\b\u0000\u0010\u0012*\u00020\u00132\u000e\u0010\u001e\u001a\n\u0012\u0004\u0012\u0002H\u0012\u0018\u00010\u001fH\u0016J\n\u0010&\u001a\u0004\u0018\u00010'H\u0002J\u001a\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\bH\u0016J\u001a\u0010,\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010\b2\u0006\u0010-\u001a\u00020\u0004H\u0002J\u0012\u0010.\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010\bH\u0002J\u0012\u0010/\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010\bH\u0002J\u0012\u00100\u001a\u00020\u00042\b\u0010+\u001a\u0004\u0018\u00010\bH\u0002J\u001e\u00101\u001a\u0004\u0018\u00010\u00042\b\u00102\u001a\u0004\u0018\u00010\u00032\b\u0010+\u001a\u0004\u0018\u00010\bH\u0016J,\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00120\u001103\"\b\b\u0000\u0010\u0012*\u00020\u00132\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H\u00120\u001fH\u0016J(\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0011032\b\u0010 \u001a\u0004\u0018\u00010\b2\b\u0010!\u001a\u0004\u0018\u00010\bH\u0016J7\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0011032\b\u0010 \u001a\u0004\u0018\u00010\b2\b\u0010!\u001a\u0004\u0018\u00010\b2\b\u0010\"\u001a\u0004\u0018\u00010\u000eH\u0016¢\u0006\u0002\u00104R$\u0010\u0006\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00066"}, d2 = {"Lcom/geely/rxhttp/SWGatewayComponent;", "Lcom/geely/rxhttp/ISWXGatewayProxy;", "Lcom/geely/rxhttp/IProvider;", "Lcom/geely/rxhttp/SchemeType;", "Lcom/alibaba/cloudapi/sdk/model/BaseClientInitialParam;", "()V", "apiClientHostMap", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "Ljava/lang/ref/WeakReference;", "Lcom/geely/rxhttp/SWAPIClientHost;", "gatewayMap", "Lcom/geely/rxhttp/GateWayOptions;", "initialized", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "assertInit", "buildSucessResponse", "Lcom/geely/rxhttp/base/IHttpResponse;", "T", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "code", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "message", "body", "httpCode", com.geely.rxhttp.base.Constants.KEY_NAME_HEADERS, org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "ensureAPIClientHost", "host", "execRequest", "request", "Lcom/geely/rxhttp/base/IBaseRequest;", "url", "json", "isWeex", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)Lcom/geely/rxhttp/base/IHttpResponse;", "getHost", "Lcom/alibaba/cloudapi/sdk/model/ApiRequest;", "getTokenProtocol", "Lcom/geely/sweet/lib/token/ITokenProtocol;", "init", "swGateWayConfig", "Lcom/geely/rxhttp/base/SWGateWayConfig;", "appcode", "obtainGateWayParams", "httpParam", "obtainNewHttpBuilderParam", "obtainNewHttpsBuilderParam", "obtainNewWsBuilderParam", "provide", "schemeType", "Lio/reactivex/Observable;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)Lio/reactivex/Observable;", "Companion", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SWGatewayComponent implements com.geely.rxhttp.ISWXGatewayProxy, com.geely.rxhttp.IProvider<com.geely.rxhttp.SchemeType, com.alibaba.cloudapi.sdk.model.BaseClientInitialParam> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.rxhttp.SWGatewayComponent.Companion INSTANCE = new com.geely.rxhttp.SWGatewayComponent.Companion(null);
    private static final com.geely.rxhttp.ISWXGatewayProxy instance = new com.geely.rxhttp.SWGatewayComponent();
    private final java.util.Map<java.lang.String, java.lang.ref.WeakReference<com.geely.rxhttp.SWAPIClientHost>> apiClientHostMap = new java.util.concurrent.ConcurrentHashMap();
    private final java.util.Map<java.lang.String, com.geely.rxhttp.GateWayOptions> gatewayMap = new java.util.concurrent.ConcurrentHashMap();
    private boolean initialized;

    /* JADX INFO: compiled from: SWGatewayComponent.kt */
    @kotlin.Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.geely.rxhttp.SchemeType.values().length];
            iArr[com.geely.rxhttp.SchemeType.Http.ordinal()] = 1;
            iArr[com.geely.rxhttp.SchemeType.Https.ordinal()] = 2;
            iArr[com.geely.rxhttp.SchemeType.Websocket.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final com.geely.rxhttp.ISWXGatewayProxy getInstance() {
        return INSTANCE.getInstance();
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.rxhttp.ISWXGatewayProxy obtainInstance() {
        return INSTANCE.obtainInstance();
    }

    private final com.geely.sweet.lib.token.ITokenProtocol getTokenProtocol() {
        return com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().fetchProtocol();
    }

    private final com.alibaba.cloudapi.sdk.model.BaseClientInitialParam obtainNewHttpBuilderParam(java.lang.String appcode) {
        return obtainGateWayParams(appcode, (com.alibaba.cloudapi.sdk.model.BaseClientInitialParam) new com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams());
    }

    private final com.alibaba.cloudapi.sdk.model.BaseClientInitialParam obtainNewHttpsBuilderParam(java.lang.String appcode) {
        com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams httpClientBuilderParams = new com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams();
        httpClientBuilderParams.setHostnameVerifier(com.geely.rxhttp.impl.SSLSocketClient.INSTANCE.getHostnameVerifier());
        httpClientBuilderParams.setX509TrustManager(com.geely.rxhttp.impl.SSLSocketClient.INSTANCE.getX509TrustManager());
        httpClientBuilderParams.setSslSocketFactory(com.geely.rxhttp.impl.SSLSocketClient.INSTANCE.getSSLSocketFactory());
        return obtainGateWayParams(appcode, (com.alibaba.cloudapi.sdk.model.BaseClientInitialParam) httpClientBuilderParams);
    }

    private final com.alibaba.cloudapi.sdk.model.BaseClientInitialParam obtainNewWsBuilderParam(java.lang.String appcode) {
        return obtainGateWayParams(appcode, (com.alibaba.cloudapi.sdk.model.BaseClientInitialParam) new com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams());
    }

    private final com.alibaba.cloudapi.sdk.model.BaseClientInitialParam obtainGateWayParams(java.lang.String appcode, com.alibaba.cloudapi.sdk.model.BaseClientInitialParam httpParam) {
        java.lang.String str = appcode;
        if (str == null || str.length() == 0) {
            appcode = com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE;
        }
        com.geely.rxhttp.GateWayOptions gateWayOptions = this.gatewayMap.get(appcode);
        if (gateWayOptions == null) {
            gateWayOptions = this.gatewayMap.get(com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE);
        }
        if ((gateWayOptions == null ? null : gateWayOptions.getAppKey()) != null && gateWayOptions.getAppSecret() != null) {
            httpParam.setAppKey(gateWayOptions.getAppKey());
            httpParam.setAppSecret(gateWayOptions.getAppSecret());
            httpParam.setExtraHeaderToSign(gateWayOptions.getExtraHeaderToSign());
        }
        return httpParam;
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public <T> io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<T>> request(final com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        if (assertInit()) {
            com.geely.rxhttp.GatewayError gatewayError = com.geely.rxhttp.GatewayError.InitializeError;
            kotlin.reflect.KClass<T> expectedCls = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
            io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<T>> observableCompose = io.reactivex.Observable.just(gatewayError.convert2Response(expectedCls)).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.asyncTransform());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "just(GatewayError.Initia…(RxUtil.asyncTransform())");
            return observableCompose;
        }
        if (!request.get$verify()) {
            com.geely.rxhttp.GatewayError gatewayError2 = com.geely.rxhttp.GatewayError.BadParams;
            kotlin.reflect.KClass<T> expectedCls2 = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls2);
            io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<T>> observableCompose2 = io.reactivex.Observable.just(gatewayError2.convert2Response(expectedCls2)).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.asyncTransform());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose2, "just(GatewayError.BadPar…(RxUtil.asyncTransform())");
            return observableCompose2;
        }
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<T>> observableCompose3 = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.rxhttp.SWGatewayComponent$$ExternalSyntheticLambda0
            /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) throws kotlin.NoWhenBranchMatchedException {
                com.geely.rxhttp.SWGatewayComponent.m638request$lambda0(this.f$0, request, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.asyncTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose3, "create(ObservableOnSubsc…(RxUtil.asyncTransform())");
        return observableCompose3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    /* JADX INFO: renamed from: request$lambda-0, reason: not valid java name */
    public static final void m638request$lambda0(final com.geely.rxhttp.SWGatewayComponent sWGatewayComponent, com.geely.rxhttp.base.IBaseRequest iBaseRequest, final io.reactivex.ObservableEmitter observableEmitter) throws kotlin.NoWhenBranchMatchedException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sWGatewayComponent, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iBaseRequest, "$request");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        java.lang.String str = iBaseRequest.get$host();
        kotlin.jvm.internal.Intrinsics.checkNotNull(str);
        sWGatewayComponent.ensureAPIClientHost(str).asyncRequest(iBaseRequest, new com.geely.rxhttp.OnGatewayRequestCallback() { // from class: com.geely.rxhttp.SWGatewayComponent$request$1$1
            @Override // com.geely.rxhttp.OnGatewayRequestCallback
            public <T> void onResponse(com.geely.rxhttp.base.IHttpResponse<T> reponse) {
                int iIntValue;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(reponse, "reponse");
                if (reponse.get$httpCode() != null) {
                    java.lang.Integer num = reponse.get$httpCode();
                    kotlin.jvm.internal.Intrinsics.checkNotNull(num);
                    iIntValue = num.intValue();
                } else {
                    iIntValue = reponse.get$code();
                }
                observableEmitter.onNext(sWGatewayComponent.buildSucessResponse(reponse.get$code(), reponse.get$message(), reponse.get$body(), iIntValue, reponse.getHeaders()));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final <T> com.geely.rxhttp.base.IHttpResponse<T> buildSucessResponse(final int code, final java.lang.String message, final java.lang.String body, final int httpCode, final java.util.Map<java.lang.String, java.lang.String> headers) {
        return new com.geely.rxhttp.base.IHttpResponse<T>() { // from class: com.geely.rxhttp.SWGatewayComponent.buildSucessResponse.1
            @Override // com.geely.rxhttp.base.IHttpResponse
            public T getData() {
                return null;
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            public boolean isSuccessful() {
                return com.geely.rxhttp.base.IHttpResponse.DefaultImpls.isSuccessful(this);
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
                java.util.HashMap map = new java.util.HashMap();
                java.util.Map<java.lang.String, java.lang.String> map2 = headers;
                if (map2 == null) {
                    return null;
                }
                if (map2.size() > 0) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
                        java.lang.String key = entry.getKey();
                        if (!(key == null || key.length() == 0)) {
                            java.lang.String value = entry.getValue();
                            map.put(entry.getKey(), value == null ? null : value.toString());
                        }
                    }
                }
                return map;
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            /* JADX INFO: renamed from: getCode, reason: from getter */
            public int get$code() {
                return code;
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            /* JADX INFO: renamed from: getMessage */
            public java.lang.String get$message() {
                return message;
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            /* JADX INFO: renamed from: getBody, reason: from getter */
            public java.lang.String get$body() {
                return body;
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            /* JADX INFO: renamed from: getHttpCode */
            public java.lang.Integer get$httpCode() {
                return java.lang.Integer.valueOf(httpCode);
            }

            @Override // com.geely.rxhttp.base.IHttpResponse
            /* JADX INFO: renamed from: getHttpMessage, reason: from getter */
            public java.lang.String get$message() {
                return message;
            }
        };
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public <T> com.geely.rxhttp.base.IHttpResponse<T> execRequest(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        if (assertInit()) {
            com.geely.rxhttp.GatewayError gatewayError = com.geely.rxhttp.GatewayError.InitializeError;
            kotlin.reflect.KClass<T> expectedCls = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
            return gatewayError.convert2Response(expectedCls);
        }
        if (!request.get$verify()) {
            com.geely.rxhttp.GatewayError gatewayError2 = com.geely.rxhttp.GatewayError.BadParams;
            kotlin.reflect.KClass<T> expectedCls2 = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls2);
            return gatewayError2.convert2Response(expectedCls2);
        }
        if (!com.geely.rxhttp.util.NetworkUtil.INSTANCE.isNetworkAvaliable()) {
            com.geely.rxhttp.GatewayError gatewayError3 = com.geely.rxhttp.GatewayError.ClientNoNetwork;
            kotlin.reflect.KClass<T> expectedCls3 = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls3);
            return gatewayError3.convert2Response(expectedCls3);
        }
        java.lang.String str = request.get$host();
        kotlin.jvm.internal.Intrinsics.checkNotNull(str);
        return ensureAPIClientHost(str).syncRequest(request);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public com.geely.rxhttp.base.IHttpResponse<java.lang.String> execRequest(java.lang.String url, java.lang.String json) {
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> request = com.geely.rxhttp.GatewayRequest.INSTANCE.parseRequest(url, (java.lang.Object) json, (java.lang.Boolean) false);
        kotlin.jvm.internal.Intrinsics.checkNotNull(request);
        return execRequest(request);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public com.geely.rxhttp.base.IHttpResponse<java.lang.String> execRequest(java.lang.String url, java.lang.String json, java.lang.Boolean isWeex) {
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> request = com.geely.rxhttp.GatewayRequest.INSTANCE.parseRequest(url, json, isWeex);
        kotlin.jvm.internal.Intrinsics.checkNotNull(request);
        return execRequest(request);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public <T> com.alibaba.cloudapi.sdk.model.ApiRequest getHost(com.geely.rxhttp.base.IBaseRequest<T> request) {
        java.lang.String str;
        if (request == null || (str = request.get$host()) == null) {
            return null;
        }
        return ensureAPIClientHost(str).getAsyncRequest(request);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> request(java.lang.String url, java.lang.String json, java.lang.Boolean isWeex) {
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> request = com.geely.rxhttp.GatewayRequest.INSTANCE.parseRequest(url, json, isWeex);
        kotlin.jvm.internal.Intrinsics.checkNotNull(request);
        return request(request);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> request(java.lang.String url, java.lang.String json) {
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> request = com.geely.rxhttp.GatewayRequest.INSTANCE.parseRequest(url, (java.lang.Object) json, (java.lang.Boolean) false);
        kotlin.jvm.internal.Intrinsics.checkNotNull(request);
        return request(request);
    }

    private final com.geely.rxhttp.SWAPIClientHost ensureAPIClientHost(java.lang.String host) {
        if (this.apiClientHostMap.containsKey(host)) {
            java.lang.ref.WeakReference<com.geely.rxhttp.SWAPIClientHost> weakReference = this.apiClientHostMap.get(host);
            if ((weakReference == null ? null : weakReference.get()) == null) {
                com.geely.rxhttp.SWAPIClientHost sWAPIClientHost = new com.geely.rxhttp.SWAPIClientHost(host);
                sWAPIClientHost.registerParamProvider(this);
                this.apiClientHostMap.put(host, new java.lang.ref.WeakReference<>(sWAPIClientHost));
                return sWAPIClientHost;
            }
            com.geely.rxhttp.SWAPIClientHost sWAPIClientHost2 = weakReference.get();
            kotlin.jvm.internal.Intrinsics.checkNotNull(sWAPIClientHost2);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sWAPIClientHost2, "ref.get()!!");
            return sWAPIClientHost2;
        }
        com.geely.rxhttp.SWAPIClientHost sWAPIClientHost3 = new com.geely.rxhttp.SWAPIClientHost(host);
        sWAPIClientHost3.registerParamProvider(this);
        this.apiClientHostMap.put(host, new java.lang.ref.WeakReference<>(sWAPIClientHost3));
        return sWAPIClientHost3;
    }

    @Override // com.geely.rxhttp.IProvider
    public com.alibaba.cloudapi.sdk.model.BaseClientInitialParam provide(com.geely.rxhttp.SchemeType schemeType, java.lang.String appcode) {
        int i = schemeType == null ? -1 : com.geely.rxhttp.SWGatewayComponent.WhenMappings.$EnumSwitchMapping$0[schemeType.ordinal()];
        if (i == 1) {
            return obtainNewHttpBuilderParam(appcode);
        }
        if (i == 2) {
            return obtainNewHttpsBuilderParam(appcode);
        }
        if (i != 3) {
            return null;
        }
        return obtainNewWsBuilderParam(appcode);
    }

    @Override // com.geely.rxhttp.ISWXGatewayProxy
    public boolean init(com.geely.rxhttp.base.SWGateWayConfig swGateWayConfig, java.lang.String appcode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(swGateWayConfig, "swGateWayConfig");
        java.lang.String appKey = swGateWayConfig.getAppKey();
        if (!(appKey == null || appKey.length() == 0)) {
            java.lang.String appSecret = swGateWayConfig.getAppSecret();
            if (!(appSecret == null || appSecret.length() == 0)) {
                java.lang.String str = appcode;
                if (str == null || str.length() == 0) {
                    appcode = com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE;
                }
                if (this.gatewayMap.get(appcode) == null) {
                    com.geely.rxhttp.GateWayOptions gateWayOptions = new com.geely.rxhttp.GateWayOptions();
                    gateWayOptions.setAppKey(swGateWayConfig.getAppKey());
                    gateWayOptions.setAppSecret(swGateWayConfig.getAppSecret());
                    gateWayOptions.setExtraHeaderToSign(swGateWayConfig.getExtraHeaderToSign());
                    this.gatewayMap.put(appcode, gateWayOptions);
                }
                this.initialized = true;
                com.geely.sweet.lib.common.log.LogUtil.t("SWXGateway").w(kotlin.jvm.internal.Intrinsics.stringPlus("init Gateway success and appcode is : ", appcode));
                return true;
            }
        }
        com.geely.sweet.lib.common.log.LogUtil.t("SWXGateway").e("init gateway failed : appKey or appSecret is null");
        return false;
    }

    private final boolean assertInit() {
        return !this.initialized;
    }

    /* JADX INFO: compiled from: SWGatewayComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\u0004H\u0007R\u001c\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007¨\u0006\t"}, d2 = {"Lcom/geely/rxhttp/SWGatewayComponent$Companion;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "instance", "Lcom/geely/rxhttp/ISWXGatewayProxy;", "getInstance$annotations", "getInstance", "()Lcom/geely/rxhttp/ISWXGatewayProxy;", "obtainInstance", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @kotlin.jvm.JvmStatic
        public static /* synthetic */ void getInstance$annotations() {
        }

        private Companion() {
        }

        public final com.geely.rxhttp.ISWXGatewayProxy getInstance() {
            return com.geely.rxhttp.SWGatewayComponent.instance;
        }

        @kotlin.jvm.JvmStatic
        public final com.geely.rxhttp.ISWXGatewayProxy obtainInstance() {
            return new com.geely.rxhttp.SWGatewayComponent();
        }
    }
}
