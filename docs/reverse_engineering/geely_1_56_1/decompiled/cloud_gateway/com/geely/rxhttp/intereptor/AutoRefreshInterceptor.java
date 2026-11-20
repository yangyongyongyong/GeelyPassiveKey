package com.geely.rxhttp.intereptor;

/* JADX INFO: compiled from: AutoRefreshInteceptor.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u0001:\u0002\u001c\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J(\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\t2\u0016\u0010\u000f\u001a\u0012\u0012\u0004\u0012\u00020\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u0011\u0018\u00010\u0010H\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0012\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0011H\u0002J\u0018\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0011H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/geely/rxhttp/intereptor/AutoRefreshInterceptor;", "Lokhttp3/Interceptor;", "tokenProvider", "Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;", "(Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;)V", "isRefreshing", "Ljava/util/concurrent/atomic/AtomicBoolean;", "mRefreshListenerList", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "Lokhttp3/Request$Builder;", "Lcom/geely/rxhttp/intereptor/AutoRefreshInterceptor$OnRefreshListener;", "responses", "addHeaders", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "builder", com.geely.rxhttp.base.Constants.KEY_NAME_HEADERS, "Ljava/util/HashMap;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "safeParseBoolean", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "requireToken", "triggerRefreshListener", "isSuccess", "accessToken", "Companion", "OnRefreshListener", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class AutoRefreshInterceptor implements okhttp3.Interceptor {
    public static final java.lang.String HEADER_PARAM_IS_REFRESH_TOKEN = "x-refresh-token";
    public static final java.lang.String REFRESHINTERCEPTOR_TAG = "sw_access_token";
    private java.util.concurrent.atomic.AtomicBoolean isRefreshing;
    private final java.util.Map<okhttp3.Request.Builder, com.geely.rxhttp.intereptor.AutoRefreshInterceptor.OnRefreshListener> mRefreshListenerList;
    private okhttp3.Request.Builder responses;
    private final com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider tokenProvider;

    /* JADX INFO: compiled from: AutoRefreshInteceptor.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H&¨\u0006\b"}, d2 = {"Lcom/geely/rxhttp/intereptor/AutoRefreshInterceptor$OnRefreshListener;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "onRefresh", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "isSuccess", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "accessToken", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public interface OnRefreshListener {
        void onRefresh(boolean isSuccess, java.lang.String accessToken);
    }

    public AutoRefreshInterceptor(com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider iTokenProvider) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iTokenProvider, "tokenProvider");
        this.tokenProvider = iTokenProvider;
        this.mRefreshListenerList = new java.util.LinkedHashMap();
        this.isRefreshing = new java.util.concurrent.atomic.AtomicBoolean(false);
    }

    /* JADX WARN: Type inference failed for: r2v10, types: [com.geely.rxhttp.intereptor.AutoRefreshInterceptor$intercept$request$1] */
    public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) {
        okhttp3.Response response;
        okhttp3.MediaType mediaType;
        okhttp3.Headers headers;
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String str4;
        okhttp3.Headers headers2;
        okhttp3.MediaType mediaType2;
        java.lang.String str5;
        java.lang.String str6;
        java.lang.String str7;
        java.lang.String str8;
        java.lang.String str9;
        java.lang.String json;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chain, "chain");
        okhttp3.Request.Builder builderNewBuilder = chain.request().newBuilder();
        okhttp3.Response responseProceed = chain.proceed(chain.request());
        okhttp3.ResponseBody responseBodyBody = responseProceed.body();
        okhttp3.MediaType mediaTypeContentType = responseBodyBody == null ? null : responseBodyBody.contentType();
        okhttp3.Headers headers3 = responseProceed.headers();
        okhttp3.ResponseBody responseBodyBody2 = responseProceed.body();
        kotlin.jvm.internal.Intrinsics.checkNotNull(responseBodyBody2);
        java.lang.String utf8 = responseBodyBody2.source().readUtf8();
        com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w("response code : " + responseProceed.code() + " , request url:  " + chain.request().url() + ' ');
        if (responseProceed.code() == com.geely.rxhttp.base.HttpError.SUCCESS.getCode()) {
            com.geely.rxhttp.util.HttpParseUtil httpParseUtil = com.geely.rxhttp.util.HttpParseUtil.INSTANCE;
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(utf8, "data");
            int i = httpParseUtil.parse2Response(utf8, responseProceed, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class), null).get$code();
            java.lang.String string = chain.request().url().toString();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "chain.request().url().toString()");
            okhttp3.Request request = chain.request();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(request, "chain.request()");
            java.util.List listHeaders = request.headers("OAUTH");
            java.lang.String str10 = listHeaders.size() > 0 ? (java.lang.String) listHeaders.get(0) : null;
            if (str10 != null && i == 602 && java.lang.Boolean.parseBoolean(str10)) {
                i = com.geely.market.energy.common.R2.attr.layout_editor_absoluteY;
            }
            int i2 = i;
            com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w("request url : " + string + " httpcode :" + i2);
            response = responseProceed;
            okhttp3.MediaType mediaType3 = mediaTypeContentType;
            if (i2 == 601) {
                str3 = "x-ca-key";
                if (!this.isRefreshing.get()) {
                    com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(kotlin.jvm.internal.Intrinsics.stringPlus("need refreshToken url : ", string));
                    this.isRefreshing.set(true);
                    this.tokenProvider.refreshToken(string, java.lang.String.valueOf(i2));
                    com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(kotlin.jvm.internal.Intrinsics.stringPlus("after refreshToken url : ", string));
                    this.isRefreshing.set(false);
                    java.lang.String strProvideToken = this.tokenProvider.provideToken();
                    java.util.Map<java.lang.String, java.lang.String> mapProvideHeader = this.tokenProvider.provideHeader(request);
                    if (strProvideToken != null && !kotlin.jvm.internal.Intrinsics.areEqual(strProvideToken, chain.request().header("token"))) {
                        triggerRefreshListener(true, strProvideToken);
                        builderNewBuilder.removeHeader("token");
                        builderNewBuilder.addHeader("token", strProvideToken);
                        builderNewBuilder.removeHeader("user-agent");
                        builderNewBuilder.removeHeader(com.geely.rxhttp.base.Constants.KEY_NAME_CONTENT_TYPE);
                        builderNewBuilder.removeHeader("x-ca-signature-headers");
                        builderNewBuilder.removeHeader("host");
                        builderNewBuilder.removeHeader("x-ca-timestamp");
                        builderNewBuilder.removeHeader("accept");
                        builderNewBuilder.removeHeader("ca_version");
                        builderNewBuilder.removeHeader(str3);
                        builderNewBuilder.removeHeader("x-ca-nonce");
                        builderNewBuilder.removeHeader(com.geely.rxhttp.intereptor.SessionInterceptor.HEADER_PARAM_SVCS_ID);
                        builderNewBuilder.removeHeader("x-ca-signature");
                        builderNewBuilder.removeHeader("date");
                        builderNewBuilder.removeHeader("content-md5");
                        com.geely.rxhttp.util.HttpParseUtil httpParseUtil2 = com.geely.rxhttp.util.HttpParseUtil.INSTANCE;
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(builderNewBuilder, "requestBuilder");
                        httpParseUtil2.addHeaders(builderNewBuilder, (java.util.HashMap) mapProvideHeader);
                        okhttp3.Response responseProceed2 = chain.proceed(builderNewBuilder.build());
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseProceed2, "chain.proceed(requestBuilder.build())");
                        return responseProceed2;
                    }
                    triggerRefreshListener(false, org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE);
                    okhttp3.Response responseBuild = new okhttp3.Response.Builder().body(okhttp3.ResponseBody.create(mediaType3, com.alibaba.fastjson.JSON.toJSONString(new com.geely.rxhttp.util.HttpParseUtil.HttpParse("user_refresh_invalid_expired", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "user_refresh_invalid_expired")))).headers(headers3).protocol(response.protocol()).code(response.code()).message(response.message()).request(builderNewBuilder.build()).build();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseBuild, "newResponse.build()");
                    return responseBuild;
                }
                str2 = "host";
                str4 = "newResponse.build()";
                headers2 = headers3;
                mediaType2 = mediaType3;
                str5 = "date";
                str6 = "x-ca-signature";
                str7 = org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE;
                str8 = "user_refresh_invalid_expired";
                com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(kotlin.jvm.internal.Intrinsics.stringPlus("wait refresh token url : ", string));
                final java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(1);
                java.util.Map<okhttp3.Request.Builder, com.geely.rxhttp.intereptor.AutoRefreshInterceptor.OnRefreshListener> map = this.mRefreshListenerList;
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(builderNewBuilder, "requestBuilder");
                map.put(builderNewBuilder, new com.geely.rxhttp.intereptor.AutoRefreshInterceptor.OnRefreshListener() { // from class: com.geely.rxhttp.intereptor.AutoRefreshInterceptor.intercept.1
                    @Override // com.geely.rxhttp.intereptor.AutoRefreshInterceptor.OnRefreshListener
                    public void onRefresh(boolean isSuccess, java.lang.String accessToken) {
                        countDownLatch.countDown();
                    }
                });
                try {
                    countDownLatch.await();
                } catch (java.lang.InterruptedException e) {
                    com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(kotlin.jvm.internal.Intrinsics.stringPlus("countDownLatch InterruptedException:", e.getMessage()));
                }
            } else {
                str2 = "host";
                str3 = "x-ca-key";
                str4 = "newResponse.build()";
                headers2 = headers3;
                mediaType2 = mediaType3;
                str5 = "date";
                str6 = "x-ca-signature";
                str7 = org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE;
                str8 = "user_refresh_invalid_expired";
            }
            if (i2 == 602 || i2 == 604 || i2 == 605) {
                this.tokenProvider.refreshToken(string, java.lang.String.valueOf(i2));
                try {
                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    str9 = utf8;
                    try {
                        com.geely.market.energy.common.data.RequestBean requestBean = (com.geely.market.energy.common.data.RequestBean) gson.fromJson(str9, new com.google.gson.reflect.TypeToken<com.geely.market.energy.common.data.RequestBean<java.lang.String>>() { // from class: com.geely.rxhttp.intereptor.AutoRefreshInterceptor$intercept$request$1
                        }.getType());
                        requestBean.setData(null);
                        requestBean.setMessage(null);
                        json = gson.toJson(requestBean);
                    } catch (java.lang.Exception unused) {
                        json = str9;
                    }
                } catch (java.lang.Exception unused2) {
                    str9 = utf8;
                }
            } else {
                str9 = utf8;
                json = str9;
            }
            if (this.mRefreshListenerList.get(builderNewBuilder) != null) {
                this.mRefreshListenerList.remove(builderNewBuilder);
                java.lang.String strProvideToken2 = this.tokenProvider.provideToken();
                if (strProvideToken2 != null && !kotlin.jvm.internal.Intrinsics.areEqual(strProvideToken2, chain.request().header("token"))) {
                    com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider iTokenProvider = this.tokenProvider;
                    okhttp3.Request requestBuild = builderNewBuilder.build();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(requestBuild, "requestBuilder.build()");
                    java.util.Map<java.lang.String, java.lang.String> mapProvideHeader2 = iTokenProvider.provideHeader(requestBuild);
                    builderNewBuilder.removeHeader("token");
                    builderNewBuilder.addHeader("token", strProvideToken2);
                    builderNewBuilder.removeHeader("user-agent");
                    builderNewBuilder.removeHeader(com.geely.rxhttp.base.Constants.KEY_NAME_CONTENT_TYPE);
                    builderNewBuilder.removeHeader("x-ca-signature-headers");
                    builderNewBuilder.removeHeader(str2);
                    builderNewBuilder.removeHeader("x-ca-timestamp");
                    builderNewBuilder.removeHeader("accept");
                    builderNewBuilder.removeHeader("ca_version");
                    builderNewBuilder.removeHeader(str3);
                    builderNewBuilder.removeHeader("x-ca-nonce");
                    builderNewBuilder.removeHeader(com.geely.rxhttp.intereptor.SessionInterceptor.HEADER_PARAM_SVCS_ID);
                    builderNewBuilder.removeHeader(str6);
                    builderNewBuilder.removeHeader(str5);
                    builderNewBuilder.removeHeader("content-md5");
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(builderNewBuilder, "requestBuilder");
                    addHeaders(builderNewBuilder, (java.util.HashMap) mapProvideHeader2);
                    okhttp3.Response responseProceed3 = chain.proceed(builderNewBuilder.build());
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseProceed3, "chain.proceed(requestBuilder.build())");
                    return responseProceed3;
                }
                java.lang.String str11 = str8;
                com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(str11);
                okhttp3.Response responseBuild2 = new okhttp3.Response.Builder().body(okhttp3.ResponseBody.create(mediaType2, com.alibaba.fastjson.JSON.toJSONString(new com.geely.rxhttp.util.HttpParseUtil.HttpParse(str11, str7, str11)))).headers(headers2).protocol(response.protocol()).code(response.code()).message(response.message()).request(builderNewBuilder.build()).build();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseBuild2, str4);
                return responseBuild2;
            }
            mediaType = mediaType2;
            headers = headers2;
            str = str4;
            com.geely.sweet.lib.common.log.LogUtil.t(REFRESHINTERCEPTOR_TAG).w(kotlin.jvm.internal.Intrinsics.stringPlus("tokenProvider-1111==", string));
            utf8 = json;
        } else {
            response = responseProceed;
            mediaType = mediaTypeContentType;
            headers = headers3;
            str = "newResponse.build()";
        }
        okhttp3.Response responseBuild3 = new okhttp3.Response.Builder().body(okhttp3.ResponseBody.create(mediaType, utf8)).headers(headers).protocol(response.protocol()).code(response.code()).message(response.message()).request(builderNewBuilder.build()).build();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(responseBuild3, str);
        return responseBuild3;
    }

    private final void addHeaders(okhttp3.Request.Builder builder, java.util.HashMap<java.lang.String, java.lang.String> headers) {
        java.util.HashMap<java.lang.String, java.lang.String> map = headers;
        if (!(map == null || map.isEmpty()) && map.containsKey("token")) {
            headers.remove("token");
        }
        if (headers == null || headers.size() <= 0) {
            return;
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(entry, "iterator.next()");
            java.util.Map.Entry<java.lang.String, java.lang.String> entry2 = entry;
            java.lang.String key = entry2.getKey();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(key, "item.key");
            java.lang.String str = key;
            java.lang.String value = entry2.getValue();
            if (value != null) {
                builder.addHeader(str, value);
            }
        }
    }

    private final void triggerRefreshListener(boolean isSuccess, java.lang.String accessToken) {
        try {
            synchronized (this.mRefreshListenerList) {
                java.util.Iterator<com.geely.rxhttp.intereptor.AutoRefreshInterceptor.OnRefreshListener> it = this.mRefreshListenerList.values().iterator();
                while (it.hasNext()) {
                    it.next().onRefresh(isSuccess, accessToken);
                }
                kotlin.Unit unit = kotlin.Unit.INSTANCE;
            }
        } catch (java.lang.Exception unused) {
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
