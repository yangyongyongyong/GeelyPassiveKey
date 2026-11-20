package com.geely.rxhttp;

/* JADX INFO: compiled from: SWGatewayComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000¸\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J(\u0010\r\u001a\u00020\u000e\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013J\u0010\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0015H\u0002J(\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00182\u000e\u0010\u0019\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001aH\u0002J\"\u0010\u001c\u001a\u0004\u0018\u00010\u0015\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011H\u0002J$\u0010\u001d\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\t0\b2\u0006\u0010\u001e\u001a\u00020\u00072\b\u0010\u001f\u001a\u0004\u0018\u00010\u0003H\u0002J\u0018\u0010 \u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0003H\u0002J \u0010!\u001a\u0004\u0018\u00010\u0015\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J\u0018\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u0015H\u0002J&\u0010$\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J\u0012\u0010%\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010&\u0018\u00010\u001aH\u0002J&\u0010'\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J&\u0010(\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J&\u0010)\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J\n\u0010*\u001a\u0004\u0018\u00010+H\u0002J\u001c\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u00010-H\u0002J\u001a\u00101\u001a\u0004\u0018\u00010/2\u0006\u0010.\u001a\u0002022\u0006\u00103\u001a\u000204H\u0002J\"\u00105\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00152\b\u0010\u0012\u001a\u0004\u0018\u000106H\u0002J#\u00107\u001a\u0004\u0018\u0001H\u000f\"\u0004\b\u0000\u0010\u000f2\f\u00108\u001a\b\u0012\u0004\u0012\u0002H\u000f09H\u0002¢\u0006\u0002\u0010:J\u001a\u0010;\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u00012\b\u0010#\u001a\u0004\u0018\u00010<H\u0002J\u001a\u0010=\u001a\u0004\u0018\u00010>2\u0006\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u0015H\u0002J\u001c\u0010?\u001a\u00020\u000e2\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f\u0018\u00010\u000bJ\u0012\u0010@\u001a\u00020\u000e2\b\u0010A\u001a\u0004\u0018\u00010BH\u0002J\u0012\u0010C\u001a\u00020\u000e2\b\u0010D\u001a\u0004\u0018\u00010EH\u0002J$\u0010F\u001a\b\u0012\u0004\u0012\u0002H\u000f0G\"\b\b\u0000\u0010\u000f*\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0011J\u0006\u0010H\u001a\u00020\u000eR0\u0010\u0005\u001a$\u0012\u0004\u0012\u00020\u0003\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u00020\u0007\u0012\u000e\u0012\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\t0\b0\u00060\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006I"}, d2 = {"Lcom/geely/rxhttp/SWAPIClientHost;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "host", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "(Ljava/lang/String;)V", "apiClientMap", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "Lcom/geely/rxhttp/SchemeType;", "Ljava/lang/ref/WeakReference;", "Lcom/alibaba/cloudapi/sdk/client/BaseApiClient;", "provider", "Lcom/geely/rxhttp/IProvider;", "Lcom/alibaba/cloudapi/sdk/model/BaseClientInitialParam;", "asyncRequest", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "T", "request", "Lcom/geely/rxhttp/base/IBaseRequest;", "callback", "Lcom/geely/rxhttp/OnGatewayRequestCallback;", "attachCommonParam", "Lcom/alibaba/cloudapi/sdk/model/ApiRequest;", "attachParam", "type", "Lcom/geely/rxhttp/ParamType;", "list", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "Lcom/geely/lynkco/kit/gateway/Entry;", "buildApiRequest", "createInstanceRef", "scheme", "appcode", "ensureApiClient", "getAsyncRequest", "instance", "param", "getBodies", "getHeaderInterpolators", "Lcom/geely/sweet/lib/token/IHeaderInterpolator;", "getHeaders", "getPaths", "getQueries", "getTokenProtocol", "Lcom/geely/sweet/lib/token/ITokenProtocol;", "makeBody", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "method", "Lcom/alibaba/cloudapi/sdk/enums/HttpMethod;", "body", "parseHttpMethod", "Lcom/geely/rxhttp/entity/HttpResponse$HttpMethod;", com.geely.lynkco.kit.gateway.base.Constants.KEY_NAME_CONTENT_TYPE, "Lcom/geely/rxhttp/ContentType;", "reflectAysncSend", "Lcom/alibaba/cloudapi/sdk/model/ApiCallback;", "reflectConstructor", "clazz", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "reflectInvokeInit", "Lcom/alibaba/cloudapi/sdk/model/WebSocketClientBuilderParams;", "reflectSyncSend", "Lcom/alibaba/cloudapi/sdk/model/ApiResponse;", "registerParamProvider", "replaceHttpClient", "apiClient", "Lcom/alibaba/cloudapi/sdk/client/HttpApiClient;", "replaceWebSocketClient", "wsClient", "Lcom/alibaba/cloudapi/sdk/client/WebSocketApiClient;", "syncRequest", "Lcom/geely/rxhttp/base/IHttpResponse;", "unRegisterProvider", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SWAPIClientHost {
    private final java.util.Map<java.lang.String, java.util.Map<com.geely.rxhttp.SchemeType, java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient>>> apiClientMap;
    private final java.lang.String host;
    private com.geely.rxhttp.IProvider<com.geely.rxhttp.SchemeType, com.alibaba.cloudapi.sdk.model.BaseClientInitialParam> provider;

    /* JADX INFO: compiled from: SWGatewayComponent.kt */
    @kotlin.Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;
        public static final /* synthetic */ int[] $EnumSwitchMapping$2;

        static {
            int[] iArr = new int[com.geely.rxhttp.SchemeType.values().length];
            iArr[com.geely.rxhttp.SchemeType.Http.ordinal()] = 1;
            iArr[com.geely.rxhttp.SchemeType.Https.ordinal()] = 2;
            iArr[com.geely.rxhttp.SchemeType.Websocket.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[com.geely.rxhttp.ParamType.values().length];
            iArr2[com.geely.rxhttp.ParamType.Path.ordinal()] = 1;
            iArr2[com.geely.rxhttp.ParamType.Query.ordinal()] = 2;
            iArr2[com.geely.rxhttp.ParamType.Header.ordinal()] = 3;
            $EnumSwitchMapping$1 = iArr2;
            int[] iArr3 = new int[com.geely.rxhttp.ContentType.values().length];
            iArr3[com.geely.rxhttp.ContentType.Json.ordinal()] = 1;
            iArr3[com.geely.rxhttp.ContentType.Form.ordinal()] = 2;
            $EnumSwitchMapping$2 = iArr3;
        }
    }

    public SWAPIClientHost(java.lang.String str) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "host");
        this.host = str;
        this.apiClientMap = new java.util.concurrent.ConcurrentHashMap();
    }

    public final void registerParamProvider(com.geely.rxhttp.IProvider<com.geely.rxhttp.SchemeType, com.alibaba.cloudapi.sdk.model.BaseClientInitialParam> provider) {
        this.provider = provider;
    }

    public final void unRegisterProvider() {
        this.provider = null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    private final java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient> createInstanceRef(com.geely.rxhttp.SchemeType scheme, java.lang.String appcode) throws kotlin.NoWhenBranchMatchedException {
        com.geely.rxhttp.IProvider<com.geely.rxhttp.SchemeType, com.alibaba.cloudapi.sdk.model.BaseClientInitialParam> iProvider = this.provider;
        kotlin.jvm.internal.Intrinsics.checkNotNull(iProvider);
        com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams httpClientBuilderParams = (com.alibaba.cloudapi.sdk.model.BaseClientInitialParam) iProvider.provide(scheme, appcode);
        int i = com.geely.rxhttp.SWAPIClientHost.WhenMappings.$EnumSwitchMapping$0[scheme.ordinal()];
        if (i == 1 || i == 2) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(httpClientBuilderParams);
            httpClientBuilderParams.setHost(this.host);
            httpClientBuilderParams.setScheme(scheme.convertApiScheme());
            com.alibaba.cloudapi.sdk.client.HttpApiClient httpApiClient = (com.alibaba.cloudapi.sdk.client.HttpApiClient) reflectConstructor(com.alibaba.cloudapi.sdk.client.HttpApiClient.class);
            if (httpApiClient != null) {
                httpApiClient.init(httpClientBuilderParams);
            }
            replaceHttpClient(httpApiClient);
            return new java.lang.ref.WeakReference<>(httpApiClient);
        }
        if (i != 3) {
            throw new kotlin.NoWhenBranchMatchedException();
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(httpClientBuilderParams);
        httpClientBuilderParams.setHost(this.host);
        httpClientBuilderParams.setScheme(scheme.convertApiScheme());
        com.alibaba.cloudapi.sdk.client.WebSocketApiClient webSocketApiClient = (com.alibaba.cloudapi.sdk.client.WebSocketApiClient) reflectConstructor(com.alibaba.cloudapi.sdk.client.WebSocketApiClient.class);
        if (webSocketApiClient != null) {
            reflectInvokeInit(webSocketApiClient, (com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams) httpClientBuilderParams);
        }
        replaceWebSocketClient(webSocketApiClient);
        return new java.lang.ref.WeakReference<>(webSocketApiClient);
    }

    private final void replaceWebSocketClient(com.alibaba.cloudapi.sdk.client.WebSocketApiClient wsClient) {
        if (wsClient != null) {
            try {
                java.lang.reflect.Field declaredField = wsClient.getClass().getDeclaredField("client");
                declaredField.setAccessible(true);
                declaredField.set(wsClient, com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().getMOkHttpClient());
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final void replaceHttpClient(com.alibaba.cloudapi.sdk.client.HttpApiClient apiClient) {
        if (apiClient != null) {
            try {
                java.lang.reflect.Field declaredField = apiClient.getClass().getDeclaredField("client");
                declaredField.setAccessible(true);
                declaredField.set(apiClient, com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().getMOkHttpClient());
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final <T> T reflectConstructor(java.lang.Class<T> clazz) {
        try {
            java.lang.reflect.Constructor<T> declaredConstructor = clazz.getDeclaredConstructor(new java.lang.Class[0]);
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(new java.lang.Object[0]);
        } catch (java.lang.Exception unused) {
            return null;
        }
    }

    private final void reflectInvokeInit(java.lang.Object instance, com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams param) {
        try {
            java.lang.reflect.Method declaredMethod = instance.getClass().getDeclaredMethod("init", com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(instance, param);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    private final com.alibaba.cloudapi.sdk.model.ApiResponse reflectSyncSend(java.lang.Object instance, com.alibaba.cloudapi.sdk.model.ApiRequest param) {
        try {
            java.lang.reflect.Method declaredMethod = instance.getClass().getDeclaredMethod("sendSyncRequest", com.alibaba.cloudapi.sdk.model.ApiRequest.class);
            declaredMethod.setAccessible(true);
            java.lang.Object objInvoke = declaredMethod.invoke(instance, param);
            if (objInvoke != null) {
                return (com.alibaba.cloudapi.sdk.model.ApiResponse) objInvoke;
            }
            throw new java.lang.NullPointerException("null cannot be cast to non-null type com.alibaba.cloudapi.sdk.model.ApiResponse");
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final void reflectAysncSend(java.lang.Object instance, com.alibaba.cloudapi.sdk.model.ApiRequest param, com.alibaba.cloudapi.sdk.model.ApiCallback callback) {
        try {
            java.lang.reflect.Method declaredMethod = instance.getClass().getDeclaredMethod("sendAsyncRequest", com.alibaba.cloudapi.sdk.model.ApiRequest.class, com.alibaba.cloudapi.sdk.model.ApiCallback.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(instance, param, callback);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    private final com.alibaba.cloudapi.sdk.client.BaseApiClient ensureApiClient(com.geely.rxhttp.SchemeType scheme, java.lang.String appcode) throws kotlin.NoWhenBranchMatchedException {
        java.util.Map<com.geely.rxhttp.SchemeType, java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient>> map = this.apiClientMap.get(appcode);
        if (map != null) {
            java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient> weakReference = map.get(scheme);
            kotlin.jvm.internal.Intrinsics.checkNotNull(weakReference);
            com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClient = weakReference.get();
            if (baseApiClient != null) {
                return baseApiClient;
            }
            java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient> weakReferenceCreateInstanceRef = createInstanceRef(scheme, appcode);
            map.put(scheme, weakReferenceCreateInstanceRef);
            this.apiClientMap.put(appcode, map);
            com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClient2 = weakReferenceCreateInstanceRef.get();
            kotlin.jvm.internal.Intrinsics.checkNotNull(baseApiClient2);
            return baseApiClient2;
        }
        java.lang.ref.WeakReference<? extends com.alibaba.cloudapi.sdk.client.BaseApiClient> weakReferenceCreateInstanceRef2 = createInstanceRef(scheme, appcode);
        java.util.concurrent.ConcurrentHashMap concurrentHashMap = new java.util.concurrent.ConcurrentHashMap();
        concurrentHashMap.put(scheme, weakReferenceCreateInstanceRef2);
        this.apiClientMap.put(appcode, concurrentHashMap);
        com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClient3 = weakReferenceCreateInstanceRef2.get();
        kotlin.jvm.internal.Intrinsics.checkNotNull(baseApiClient3);
        return baseApiClient3;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final <T> void asyncRequest(final com.geely.rxhttp.base.IBaseRequest<T> request, final com.geely.rxhttp.OnGatewayRequestCallback callback) throws kotlin.NoWhenBranchMatchedException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        com.geely.rxhttp.SchemeType schemeType = request.get$scheme();
        java.lang.String str = request.get$appCode();
        if (str == null) {
            str = com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(schemeType);
        com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClientEnsureApiClient = ensureApiClient(schemeType, str);
        com.alibaba.cloudapi.sdk.model.ApiRequest apiRequestBuildApiRequest = buildApiRequest(request);
        if (apiRequestBuildApiRequest != null) {
            reflectAysncSend(baseApiClientEnsureApiClient, apiRequestBuildApiRequest, new com.alibaba.cloudapi.sdk.model.ApiCallback() { // from class: com.geely.rxhttp.SWAPIClientHost.asyncRequest.1
                public void onFailure(com.alibaba.cloudapi.sdk.model.ApiRequest apiRequest, java.lang.Exception ex) {
                    com.geely.rxhttp.OnGatewayRequestCallback onGatewayRequestCallback = callback;
                    if (onGatewayRequestCallback == null) {
                        return;
                    }
                    com.geely.rxhttp.GatewayError gatewayError = com.geely.rxhttp.GatewayError.IOException;
                    kotlin.reflect.KClass expectedCls = request.getExpectedCls();
                    kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
                    onGatewayRequestCallback.onResponse(gatewayError.convert2Response(expectedCls, ex));
                }

                public void onResponse(com.alibaba.cloudapi.sdk.model.ApiRequest apiRequest, com.alibaba.cloudapi.sdk.model.ApiResponse response) {
                    com.geely.rxhttp.OnGatewayRequestCallback onGatewayRequestCallback = callback;
                    if (onGatewayRequestCallback == null) {
                        return;
                    }
                    kotlin.reflect.KClass expectedCls = request.getExpectedCls();
                    kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
                    onGatewayRequestCallback.onResponse(com.geely.rxhttp.SWGatewayComponentKt.buildExecResponse(expectedCls, response));
                }
            });
        } else {
            if (callback == null) {
                return;
            }
            com.geely.rxhttp.GatewayError gatewayError = com.geely.rxhttp.GatewayError.BadRequest;
            kotlin.reflect.KClass<T> expectedCls = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
            callback.onResponse(gatewayError.convert2Response(expectedCls));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final <T> com.alibaba.cloudapi.sdk.model.ApiRequest getAsyncRequest(com.geely.rxhttp.base.IBaseRequest<T> request) throws kotlin.NoWhenBranchMatchedException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        com.geely.rxhttp.SchemeType schemeType = request.get$scheme();
        java.lang.String str = request.get$appCode();
        if (str == null) {
            str = com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(schemeType);
        com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClientEnsureApiClient = ensureApiClient(schemeType, str);
        com.alibaba.cloudapi.sdk.model.ApiRequest apiRequestBuildApiRequest = buildApiRequest(request);
        if (apiRequestBuildApiRequest != null) {
            getAsyncRequest(baseApiClientEnsureApiClient, apiRequestBuildApiRequest);
        }
        return apiRequestBuildApiRequest;
    }

    private final void getAsyncRequest(java.lang.Object instance, com.alibaba.cloudapi.sdk.model.ApiRequest param) {
        try {
            java.lang.reflect.Method declaredMethod = instance.getClass().getDeclaredMethod("getAsyncRequest", com.alibaba.cloudapi.sdk.model.ApiRequest.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(instance, param);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NoWhenBranchMatchedException */
    public final <T> com.geely.rxhttp.base.IHttpResponse<T> syncRequest(com.geely.rxhttp.base.IBaseRequest<T> request) throws kotlin.NoWhenBranchMatchedException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        com.geely.rxhttp.SchemeType schemeType = request.get$scheme();
        java.lang.String str = request.get$appCode();
        if (str == null) {
            str = com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(schemeType);
        com.alibaba.cloudapi.sdk.client.BaseApiClient baseApiClientEnsureApiClient = ensureApiClient(schemeType, str);
        com.alibaba.cloudapi.sdk.model.ApiRequest apiRequestBuildApiRequest = buildApiRequest(request);
        if (apiRequestBuildApiRequest == null) {
            com.geely.rxhttp.GatewayError gatewayError = com.geely.rxhttp.GatewayError.IOException;
            kotlin.reflect.KClass<T> expectedCls = request.getExpectedCls();
            kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls);
            return gatewayError.convert2Response(expectedCls);
        }
        com.alibaba.cloudapi.sdk.model.ApiResponse apiResponseReflectSyncSend = reflectSyncSend(baseApiClientEnsureApiClient, apiRequestBuildApiRequest);
        if (apiResponseReflectSyncSend != null) {
            return com.geely.rxhttp.SWGatewayComponentKt.buildExecResponse(request.getExpectedCls(), apiResponseReflectSyncSend);
        }
        com.geely.rxhttp.GatewayError gatewayError2 = com.geely.rxhttp.GatewayError.IOException;
        kotlin.reflect.KClass<T> expectedCls2 = request.getExpectedCls();
        kotlin.jvm.internal.Intrinsics.checkNotNull(expectedCls2);
        return gatewayError2.convert2Response(expectedCls2);
    }

    private final com.geely.sweet.lib.token.ITokenProtocol getTokenProtocol() {
        return com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().fetchProtocol();
    }

    private final <T> com.alibaba.cloudapi.sdk.model.ApiRequest buildApiRequest(com.geely.rxhttp.base.IBaseRequest<T> request) {
        byte[] bytes;
        com.geely.rxhttp.entity.HttpResponse.HttpMethod method = request.getMethod();
        java.lang.String str = request.get$path();
        java.util.HashMap<java.lang.String, java.lang.String> headers = request.getHeaders();
        if (headers != null) {
            headers.remove(com.geely.rxhttp.base.Constants.KEY_NAME_CONTENT_TYPE);
        }
        com.geely.rxhttp.ContentType contentType = request.get$contentType();
        kotlin.jvm.internal.Intrinsics.checkNotNull(method);
        com.alibaba.cloudapi.sdk.enums.HttpMethod httpMethod = parseHttpMethod(method, contentType);
        if (httpMethod == null) {
            return null;
        }
        java.lang.String str2 = request instanceof com.geely.rxhttp.base.JsonRequest ? ((com.geely.rxhttp.base.JsonRequest) request).get$json() : org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE;
        if (str2 == null) {
            bytes = null;
        } else {
            bytes = str2.getBytes(kotlin.text.Charsets.UTF_8);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
        }
        com.alibaba.cloudapi.sdk.model.ApiRequest apiRequest = new com.alibaba.cloudapi.sdk.model.ApiRequest(httpMethod, str, makeBody(httpMethod, bytes), request.get$requestBody());
        attachParam(apiRequest, com.geely.rxhttp.ParamType.Path, getPaths(request));
        attachParam(apiRequest, com.geely.rxhttp.ParamType.Header, getHeaders(request));
        attachParam(apiRequest, com.geely.rxhttp.ParamType.Query, getQueries(request));
        attachParam(apiRequest, com.geely.rxhttp.ParamType.Body, getBodies(request));
        attachCommonParam(apiRequest);
        java.lang.String str3 = request.get$appCode();
        boolean z = true;
        if (!(str3 == null || str3.length() == 0)) {
            apiRequest.addParam("x-ca-appcode", request.get$appCode(), com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
        } else {
            apiRequest.addParam("x-ca-appcode", com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE, com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
        }
        java.util.HashMap<java.lang.String, java.lang.String> headers2 = request.getHeaders();
        if (headers2 != null && headers2.containsKey("token")) {
            java.util.HashMap<java.lang.String, java.lang.String> headers3 = request.getHeaders();
            java.lang.String str4 = headers3 == null ? null : headers3.get("token");
            if (!(str4 == null || str4.length() == 0)) {
                apiRequest.addParam("x-requireToken", "false", com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
            } else {
                java.lang.Boolean bool = request.get$requireToken();
                kotlin.jvm.internal.Intrinsics.checkNotNull(bool);
                if (bool.booleanValue()) {
                    apiRequest.addParam("x-requireToken", "true", com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
                    if (getTokenProtocol() != null) {
                        com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = getTokenProtocol();
                        java.lang.String token = tokenProtocol == null ? null : tokenProtocol.getToken();
                        if (!(token == null || token.length() == 0)) {
                            com.geely.sweet.lib.token.ITokenProtocol tokenProtocol2 = getTokenProtocol();
                            apiRequest.addHeader("token", tokenProtocol2 != null ? tokenProtocol2.getToken() : null);
                        }
                    }
                }
            }
        } else {
            java.lang.Boolean bool2 = request.get$requireToken();
            kotlin.jvm.internal.Intrinsics.checkNotNull(bool2);
            if (bool2.booleanValue()) {
                apiRequest.addParam("x-requireToken", "true", com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
                if (getTokenProtocol() != null) {
                    com.geely.sweet.lib.token.ITokenProtocol tokenProtocol3 = getTokenProtocol();
                    java.lang.String token2 = tokenProtocol3 == null ? null : tokenProtocol3.getToken();
                    if (!(token2 == null || token2.length() == 0)) {
                        com.geely.sweet.lib.token.ITokenProtocol tokenProtocol4 = getTokenProtocol();
                        apiRequest.addHeader("token", tokenProtocol4 != null ? tokenProtocol4.getToken() : null);
                    }
                }
            }
        }
        java.lang.String str5 = request.get$appCode();
        if (str5 != null && str5.length() != 0) {
            z = false;
        }
        if (z) {
            apiRequest.addParam(com.geely.rxhttp.intereptor.AutoRefreshInterceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true", com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
        }
        return apiRequest;
    }

    private final byte[] makeBody(com.alibaba.cloudapi.sdk.enums.HttpMethod method, byte[] body) {
        if (method == com.alibaba.cloudapi.sdk.enums.HttpMethod.HEAD || method == com.alibaba.cloudapi.sdk.enums.HttpMethod.GET) {
            return null;
        }
        if (body != null) {
            return body;
        }
        byte[] bytes = " ".getBytes(kotlin.text.Charsets.UTF_8);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    private final void attachCommonParam(com.alibaba.cloudapi.sdk.model.ApiRequest request) {
        java.util.List<com.geely.sweet.lib.token.IHeaderInterpolator> headerInterpolators = getHeaderInterpolators();
        java.util.List<com.geely.sweet.lib.token.IHeaderInterpolator> list = headerInterpolators;
        if (list == null || list.isEmpty()) {
            return;
        }
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        java.util.Iterator<com.geely.sweet.lib.token.IHeaderInterpolator> it = headerInterpolators.iterator();
        while (it.hasNext()) {
            com.geely.sweet.lib.token.IHeaderInterpolator next = it.next();
            java.util.Map mapProvideHeaders = next == null ? null : next.provideHeaders();
            if (mapProvideHeaders != null && (mapProvideHeaders.isEmpty() ^ true)) {
                linkedHashMap.putAll(mapProvideHeaders);
            }
        }
        if (!linkedHashMap.isEmpty()) {
            for (java.util.Map.Entry entry : linkedHashMap.entrySet()) {
                java.lang.String str = (java.lang.String) entry.getKey();
                java.lang.String str2 = (java.lang.String) entry.getValue();
                java.lang.String str3 = str;
                if (!(str3 == null || str3.length() == 0)) {
                    java.lang.String str4 = str2;
                    if (!(str4 == null || str4.length() == 0)) {
                        request.addParam(str, str2, com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD, false);
                    }
                }
            }
        }
    }

    private final java.util.List<com.geely.sweet.lib.token.IHeaderInterpolator> getHeaderInterpolators() {
        return com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().fetchInterpolators();
    }

    private final void attachParam(com.alibaba.cloudapi.sdk.model.ApiRequest request, com.geely.rxhttp.ParamType type, java.util.List<com.geely.lynkco.kit.gateway.Entry> list) {
        com.alibaba.cloudapi.sdk.enums.ParamPosition paramPosition;
        java.util.List<com.geely.lynkco.kit.gateway.Entry> list2 = list;
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        int i = com.geely.rxhttp.SWAPIClientHost.WhenMappings.$EnumSwitchMapping$1[type.ordinal()];
        if (i == 1) {
            paramPosition = com.alibaba.cloudapi.sdk.enums.ParamPosition.PATH;
        } else if (i == 2) {
            paramPosition = com.alibaba.cloudapi.sdk.enums.ParamPosition.QUERY;
        } else if (i == 3) {
            paramPosition = com.alibaba.cloudapi.sdk.enums.ParamPosition.HEAD;
        } else {
            paramPosition = com.alibaba.cloudapi.sdk.enums.ParamPosition.BODY;
        }
        for (com.geely.lynkco.kit.gateway.Entry entry : list) {
            if (!(entry.getKey().length() == 0)) {
                request.addParam(entry.getKey(), entry.getValue(), paramPosition, false);
            }
        }
    }

    public final <T> java.util.List<com.geely.lynkco.kit.gateway.Entry> getHeaders(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        java.util.HashMap<java.lang.String, java.lang.String> headers = request.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.HashMap<java.lang.String, java.lang.String> headers2 = request.getHeaders();
        kotlin.jvm.internal.Intrinsics.checkNotNull(headers2);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers2.entrySet()) {
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(entry, "request.getHeaders()!!.entries");
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            java.lang.String str = key;
            if (!(str == null || str.length() == 0)) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(key);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(key, "key!!");
                arrayList.add(new com.geely.lynkco.kit.gateway.Entry(key, value));
            }
        }
        return arrayList;
    }

    public final <T> java.util.List<com.geely.lynkco.kit.gateway.Entry> getPaths(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        java.util.HashMap<java.lang.String, java.lang.String> queryPaths = request.getQueryPaths();
        if (queryPaths == null || queryPaths.isEmpty()) {
            return null;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.HashMap<java.lang.String, java.lang.String> queryPaths2 = request.getQueryPaths();
        kotlin.jvm.internal.Intrinsics.checkNotNull(queryPaths2);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : queryPaths2.entrySet()) {
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(entry, "request.getQueryPaths()!!.entries");
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            java.lang.String str = key;
            if (!(str == null || str.length() == 0)) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(key);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(key, "key!!");
                arrayList.add(new com.geely.lynkco.kit.gateway.Entry(key, value));
            }
        }
        return arrayList;
    }

    public final <T> java.util.List<com.geely.lynkco.kit.gateway.Entry> getQueries(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        java.util.HashMap<java.lang.String, java.lang.String> queryParams = request.getQueryParams();
        if (queryParams == null || queryParams.isEmpty()) {
            return null;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.HashMap<java.lang.String, java.lang.String> queryParams2 = request.getQueryParams();
        kotlin.jvm.internal.Intrinsics.checkNotNull(queryParams2);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : queryParams2.entrySet()) {
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(entry, "request.getQueryParams()!!.entries");
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            java.lang.String str = key;
            if (!(str == null || str.length() == 0)) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(key);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(key, "key!!");
                arrayList.add(new com.geely.lynkco.kit.gateway.Entry(key, value));
            }
        }
        return arrayList;
    }

    public final <T> java.util.List<com.geely.lynkco.kit.gateway.Entry> getBodies(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        java.util.Map<java.lang.String, java.lang.String> bodies = request.getBodies();
        if (bodies == null || bodies.isEmpty()) {
            return null;
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        java.util.Map<java.lang.String, java.lang.String> bodies2 = request.getBodies();
        kotlin.jvm.internal.Intrinsics.checkNotNull(bodies2);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : bodies2.entrySet()) {
            java.lang.String key = entry.getKey();
            java.lang.String value = entry.getValue();
            java.lang.String str = key;
            if (!(str == null || str.length() == 0)) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(key);
                arrayList.add(new com.geely.lynkco.kit.gateway.Entry(key, value));
            }
        }
        return arrayList;
    }

    private final com.alibaba.cloudapi.sdk.enums.HttpMethod parseHttpMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod method, com.geely.rxhttp.ContentType contentType) {
        if (method == com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET) {
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.GET;
        }
        if (method == com.geely.rxhttp.entity.HttpResponse.HttpMethod.HEAD) {
            if (contentType == com.geely.rxhttp.ContentType.Default) {
                return com.alibaba.cloudapi.sdk.enums.HttpMethod.HEAD;
            }
            return null;
        }
        if (method == com.geely.rxhttp.entity.HttpResponse.HttpMethod.DELETE) {
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.DELETE;
        }
        if (method == com.geely.rxhttp.entity.HttpResponse.HttpMethod.PUT) {
            int i = com.geely.rxhttp.SWAPIClientHost.WhenMappings.$EnumSwitchMapping$2[contentType.ordinal()];
            if (i == 1) {
                return com.alibaba.cloudapi.sdk.enums.HttpMethod.PUT_BODY;
            }
            if (i == 2) {
                return com.alibaba.cloudapi.sdk.enums.HttpMethod.PUT_FORM;
            }
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.PUT_BODY;
        }
        if (method == com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST) {
            int i2 = com.geely.rxhttp.SWAPIClientHost.WhenMappings.$EnumSwitchMapping$2[contentType.ordinal()];
            if (i2 == 1) {
                return com.alibaba.cloudapi.sdk.enums.HttpMethod.POST_BODY;
            }
            if (i2 == 2) {
                return com.alibaba.cloudapi.sdk.enums.HttpMethod.POST_FORM;
            }
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.POST_BODY;
        }
        if (method != com.geely.rxhttp.entity.HttpResponse.HttpMethod.PATCH) {
            return null;
        }
        int i3 = com.geely.rxhttp.SWAPIClientHost.WhenMappings.$EnumSwitchMapping$2[contentType.ordinal()];
        if (i3 == 1) {
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.PATCH_BODY;
        }
        if (i3 == 2) {
            return com.alibaba.cloudapi.sdk.enums.HttpMethod.PATCH_FORM;
        }
        return com.alibaba.cloudapi.sdk.enums.HttpMethod.PATCH_BODY;
    }
}
