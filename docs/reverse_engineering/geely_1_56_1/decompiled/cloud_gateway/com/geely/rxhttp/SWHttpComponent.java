package com.geely.rxhttp;

/* JADX INFO: compiled from: SWHttpComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000 (2\u00020\u0001:\u0001(B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u001a\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000bH\u0002J&\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00130\u0012\"\b\b\u0000\u0010\u0013*\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00130\u0016H\u0016J\"\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00122\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u000bH\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u000bJ&\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u000b2\b\u0010 \u001a\u0004\u0018\u00010\u000b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u0016J\u0012\u0010!\u001a\u00020\u001b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000bH\u0002J2\u0010\"\u001a\"\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u000b0#j\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u000b`$2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010%\u001a\u00020\u00042\u0006\u0010&\u001a\u00020'H\u0016¨\u0006)"}, d2 = {"Lcom/geely/rxhttp/SWHttpComponent;", "Lcom/geely/rxhttp/ISWHttpProxy;", "()V", "addDefaultTokenInterceptor", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "addInterceptor", "interceptor", "Lokhttp3/Interceptor;", "builderRequest", "Lcom/geely/rxhttp/impl/SimpleJsonRequest$Builder;", "url", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "obj", "Lcom/alibaba/fastjson/JSONObject;", "convert", "Lcom/geely/rxhttp/entity/HttpResponse$HttpMethod;", "str", "execRequest", "Lcom/geely/rxhttp/base/IHttpResponse;", "T", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "request", "Lcom/geely/rxhttp/base/IBaseRequest;", "json", "getHttpClient", "Lokhttp3/OkHttpClient;", "initGateWay", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "swGateWayConfig", "Lcom/geely/rxhttp/base/SWGateWayConfig;", com.geely.rxhttp.base.Constants.KEY_NAME_APPCODE, "appKey", "appSecret", "isRequestGateway", "parseParams", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "setConnectTimeout", "timeout", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "Companion", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SWHttpComponent implements com.geely.rxhttp.ISWHttpProxy {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.rxhttp.SWHttpComponent.Companion INSTANCE = new com.geely.rxhttp.SWHttpComponent.Companion(null);
    private static final com.geely.rxhttp.ISWHttpProxy instance = new com.geely.rxhttp.SWHttpComponent();

    public static final com.geely.rxhttp.ISWHttpProxy getInstance() {
        return INSTANCE.getInstance();
    }

    /* JADX INFO: compiled from: SWHttpComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/geely/rxhttp/SWHttpComponent$Companion;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "instance", "Lcom/geely/rxhttp/ISWHttpProxy;", "getInstance$annotations", "getInstance", "()Lcom/geely/rxhttp/ISWHttpProxy;", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @kotlin.jvm.JvmStatic
        public static /* synthetic */ void getInstance$annotations() {
        }

        private Companion() {
        }

        public final com.geely.rxhttp.ISWHttpProxy getInstance() {
            return com.geely.rxhttp.SWHttpComponent.instance;
        }
    }

    public final boolean initGateWay(com.geely.rxhttp.base.SWGateWayConfig swGateWayConfig, java.lang.String appCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(swGateWayConfig, "swGateWayConfig");
        java.lang.String str = appCode;
        if (str == null || str.length() == 0) {
            return com.geely.rxhttp.SWGatewayComponent.INSTANCE.getInstance().init(swGateWayConfig, com.geely.rxhttp.base.Constants.DEFAULT_APP_CODE);
        }
        return com.geely.rxhttp.SWGatewayComponent.INSTANCE.getInstance().init(swGateWayConfig, appCode);
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public boolean initGateWay(java.lang.String appKey, java.lang.String appSecret, java.lang.String appCode) {
        return initGateWay(new com.geely.rxhttp.base.SWGateWayConfig.Builder().setKeyEntry(appKey, appSecret).setExtraHeader(new java.lang.String[]{"token", "gl_user_id"}).create(), appCode);
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public com.geely.rxhttp.base.IHttpResponse<java.lang.String> execRequest(java.lang.String url, java.lang.String json) {
        if (isRequestGateway(json)) {
            return com.geely.rxhttp.SWGatewayComponent.INSTANCE.getInstance().execRequest(url, json);
        }
        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(json);
        com.geely.rxhttp.base.IHttpProxy companion = com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(object, "jsonObject");
        return companion.execRequest(builderRequest(url, object).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class)));
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public void addInterceptor(okhttp3.Interceptor interceptor) {
        if (interceptor != null) {
            com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().addInterceptor(interceptor);
        }
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public void addDefaultTokenInterceptor() {
        com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().addDefaultTokenInterceptor();
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public okhttp3.OkHttpClient getHttpClient() {
        return com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().getMOkHttpClient();
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public void setConnectTimeout(long timeout) {
        com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().setConnectTimeout(timeout);
    }

    @Override // com.geely.rxhttp.ISWHttpProxy
    public <T> com.geely.rxhttp.base.IHttpResponse<T> execRequest(com.geely.rxhttp.base.IBaseRequest<T> request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        if (request.get$useGateway()) {
            return com.geely.rxhttp.SWGatewayComponent.INSTANCE.getInstance().execRequest(request);
        }
        return com.geely.rxhttp.SWHttpProxy.INSTANCE.getInstance().execRequest(request);
    }

    private final boolean isRequestGateway(java.lang.String str) {
        com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSON.parseObject(str);
        if (!object.containsKey(com.geely.rxhttp.base.Constants.KEY_NAME_HOST_ENABLE)) {
            return false;
        }
        java.lang.Boolean bool = object.getBoolean(com.geely.rxhttp.base.Constants.KEY_NAME_HOST_ENABLE);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bool, "jsonObject.getBoolean(Co…nts.KEY_NAME_HOST_ENABLE)");
        return bool.booleanValue();
    }

    private final com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequest(java.lang.String url, com.alibaba.fastjson.JSONObject obj) {
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderNewBuilder = com.geely.rxhttp.impl.SimpleJsonRequest.INSTANCE.newBuilder();
        java.lang.String string = obj.getString("method");
        com.alibaba.fastjson.JSONObject jSONObject = obj.getJSONObject(com.geely.rxhttp.base.Constants.KEY_NAME_HEADERS);
        java.lang.String string2 = obj.getString("body");
        if (string2 == null) {
            string2 = org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE;
        }
        if (string != null) {
            builderNewBuilder.setMethod(convert(string));
        } else {
            builderNewBuilder.setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST);
        }
        builderNewBuilder.setUrl(url);
        if (jSONObject != null && obj.size() != 0) {
            builderNewBuilder.setHeaders(parseParams(jSONObject));
        }
        builderNewBuilder.addHeader(com.geely.rxhttp.base.Constants.KEY_HEADER_WEEX_REQUEST, "true");
        builderNewBuilder.setJsonBody(string2);
        return builderNewBuilder;
    }

    private final com.geely.rxhttp.entity.HttpResponse.HttpMethod convert(java.lang.String str) {
        java.lang.String str2 = str;
        int i = 0;
        if (str2 == null || str2.length() == 0) {
            return com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST;
        }
        com.geely.rxhttp.entity.HttpResponse.HttpMethod[] httpMethodArrValues = com.geely.rxhttp.entity.HttpResponse.HttpMethod.values();
        int length = httpMethodArrValues.length;
        while (i < length) {
            com.geely.rxhttp.entity.HttpResponse.HttpMethod httpMethod = httpMethodArrValues[i];
            i++;
            if (kotlin.text.StringsKt.equals(httpMethod.name(), str, true)) {
                return httpMethod;
            }
        }
        return com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST;
    }

    private final java.util.HashMap<java.lang.String, java.lang.String> parseParams(com.alibaba.fastjson.JSONObject obj) {
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        if (obj != null) {
            for (java.util.Map.Entry entry : obj.entrySet()) {
                java.lang.CharSequence charSequence = (java.lang.CharSequence) entry.getKey();
                if (!(charSequence == null || charSequence.length() == 0)) {
                    java.lang.Object value = entry.getValue();
                    java.util.HashMap<java.lang.String, java.lang.String> map2 = map;
                    java.lang.Object key = entry.getKey();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(key, "it.key");
                    map2.put(key, value == null ? null : value.toString());
                }
            }
        }
        return map;
    }
}
