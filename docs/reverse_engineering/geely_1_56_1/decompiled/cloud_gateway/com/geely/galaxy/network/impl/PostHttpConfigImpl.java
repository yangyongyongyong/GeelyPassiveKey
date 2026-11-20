package com.geely.galaxy.network.impl;

/* JADX INFO: compiled from: PostHttpConfigImpl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0005B\u0005¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/geely/galaxy/network/impl/PostHttpConfigImpl;", "Lcom/geely/galaxy/network/config/BaseHttpConfig;", "()V", "requestBody", "Lokhttp3/RequestBody;", "Builder", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class PostHttpConfigImpl extends com.geely.galaxy.network.config.BaseHttpConfig {
    private okhttp3.RequestBody requestBody;

    /* JADX INFO: compiled from: PostHttpConfigImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00042\b\u0010\t\u001a\u0004\u0018\u00010\u0001J\u0018\u0010\u000b\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00042\b\u0010\t\u001a\u0004\u0018\u00010\u0001J\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rJ\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\b\u0010\u0011\u001a\u00020\u000fH\u0002J\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0004J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\u00002\b\u0010\u0018\u001a\u0004\u0018\u00010\u0004J.\u0010\u0019\u001a\u00020\u00002&\u0010\u001a\u001a\"\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u001bj\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004`\u001cJ\u0012\u0010\u001d\u001a\u00020\u00002\b\u0010\u001e\u001a\u0004\u0018\u00010\u0004H\u0002J\u0010\u0010\u001f\u001a\u00020\u00002\b\u0010 \u001a\u0004\u0018\u00010\u0004J\u001f\u0010!\u001a\u00020\u0000\"\b\b\u0000\u0010\"*\u00020\u00012\b\u0010#\u001a\u0004\u0018\u0001H\"¢\u0006\u0002\u0010$J\u0012\u0010%\u001a\u00020\u00002\b\u0010&\u001a\u0004\u0018\u00010\u0004H\u0002J.\u0010'\u001a\u00020\u00002&\u0010(\u001a\"\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001bj\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0001`\u001cJ.\u0010)\u001a\u00020\u00002&\u0010*\u001a\"\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001bj\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0001`\u001cJ\u000e\u0010+\u001a\u00020\u00002\u0006\u0010,\u001a\u00020-J\u0012\u0010.\u001a\u00020\u00002\b\u0010/\u001a\u0004\u0018\u000100H\u0002J\u0010\u00101\u001a\u00020\u00002\b\u0010\u0018\u001a\u0004\u0018\u00010\u0004J\u000e\u00102\u001a\u00020\u00002\u0006\u00102\u001a\u000203J\u000e\u00104\u001a\u00020\u00002\u0006\u00105\u001a\u000203R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00066"}, d2 = {"Lcom/geely/galaxy/network/impl/PostHttpConfigImpl$Builder;", "", "()V", "customUrl", "", "httpConfig", "Lcom/geely/galaxy/network/impl/PostHttpConfigImpl;", "addHeader", "name", "value", "addPathParams", "addQueryParam", "build", "Lcom/geely/rxhttp/base/JsonRequest;", "ensureHeaders", "", "ensurePathParams", "ensureQueryParams", "setAppCode", "appCode", "setContentType", "contentType", "Lcom/geely/rxhttp/ContentType;", "setCustomUrl", "url", "setHeaders", "headers", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "setHost", "host", "setJson", "json", "setJsonBody", "T", "jsonBody", "(Ljava/lang/Object;)Lcom/geely/galaxy/network/impl/PostHttpConfigImpl$Builder;", "setPath", "path", "setPathParams", "pathParams", "setQueryParams", "queryParams", "setRequestBody", "requestBody", "Lokhttp3/RequestBody;", "setScheme", "scheme", "Lcom/geely/rxhttp/SchemeType;", "setUrl", "useCache", "", "useGateWay", "useGateway", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Builder {
        private java.lang.String customUrl;
        private com.geely.galaxy.network.impl.PostHttpConfigImpl httpConfig = new com.geely.galaxy.network.impl.PostHttpConfigImpl();

        private final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setScheme(com.geely.rxhttp.SchemeType scheme) {
            this.httpConfig.setScheme(scheme);
            return this;
        }

        private final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setHost(java.lang.String host) {
            this.httpConfig.setHost(host);
            return this;
        }

        private final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setPath(java.lang.String path) {
            this.httpConfig.setPath(path);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setUrl(java.lang.String url) {
            this.httpConfig.setUrl(com.geely.galaxy.network.config.NetWorkConfigKt.getBaseHttpUrl() + url);
            try {
                android.net.Uri uri = android.net.Uri.parse(url);
                setHost(uri.getHost());
                setPath(uri.getPath());
                setScheme(com.geely.rxhttp.SchemeType.Companion.convert(uri.getScheme()));
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setCustomUrl(java.lang.String url) {
            this.customUrl = url;
            try {
                android.net.Uri uri = android.net.Uri.parse(url);
                setHost(uri.getHost());
                setPath(uri.getPath());
                setScheme(com.geely.rxhttp.SchemeType.Companion.convert(uri.getScheme()));
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setHeaders(java.util.HashMap<java.lang.String, java.lang.String> headers) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(headers, "headers");
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(headers)) {
                this.httpConfig.setHeaders(headers);
            }
            return this;
        }

        private final void ensureHeaders() {
            if (this.httpConfig.getHeaders() == null) {
                this.httpConfig.setHeaders(new java.util.HashMap());
            }
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder addHeader(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            ensureHeaders();
            java.util.HashMap headers = this.httpConfig.getHeaders();
            kotlin.jvm.internal.Intrinsics.checkNotNull(headers);
            headers.put(name, value);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder useGateWay(boolean useGateway) {
            this.httpConfig.setUseGateWay(useGateway);
            return this;
        }

        private final void ensureQueryParams() {
            if (this.httpConfig.getQueryParams() == null) {
                this.httpConfig.setQueryParams(new java.util.HashMap());
            }
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setAppCode(java.lang.String appCode) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(appCode, "appCode");
            this.httpConfig.setAppcode(appCode);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setQueryParams(java.util.HashMap<java.lang.String, java.lang.Object> queryParams) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(queryParams, "queryParams");
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(queryParams)) {
                this.httpConfig.setQueryParams(queryParams);
            }
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder addQueryParam(java.lang.String name, java.lang.Object value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            ensureQueryParams();
            java.util.HashMap queryParams = this.httpConfig.getQueryParams();
            kotlin.jvm.internal.Intrinsics.checkNotNull(queryParams);
            queryParams.put(name, value);
            return this;
        }

        private final void ensurePathParams() {
            if (this.httpConfig.getQueryPaths() == null) {
                this.httpConfig.setQueryPaths(new java.util.HashMap());
            }
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setPathParams(java.util.HashMap<java.lang.String, java.lang.Object> pathParams) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pathParams, "pathParams");
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(pathParams)) {
                this.httpConfig.setQueryPaths(pathParams);
            }
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder addPathParams(java.lang.String name, java.lang.Object value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            ensurePathParams();
            java.util.HashMap queryPaths = this.httpConfig.getQueryPaths();
            kotlin.jvm.internal.Intrinsics.checkNotNull(queryPaths);
            queryPaths.put(name, value);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setContentType(com.geely.rxhttp.ContentType contentType) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(contentType, "contentType");
            this.httpConfig.setContentType(contentType);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setJson(java.lang.String json) {
            this.httpConfig.setJson(json);
            return this;
        }

        public final <T> com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setJsonBody(T jsonBody) {
            this.httpConfig.setJson(com.geely.rxhttp.util.JsonUtilKt.toJson(jsonBody));
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder useCache(boolean useCache) {
            this.httpConfig.setUseCache(useCache);
            return this;
        }

        public final com.geely.galaxy.network.impl.PostHttpConfigImpl.Builder setRequestBody(okhttp3.RequestBody requestBody) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(requestBody, "requestBody");
            this.httpConfig.requestBody = requestBody;
            return this;
        }

        public final com.geely.rxhttp.base.JsonRequest<java.lang.String> build() {
            java.util.HashMap queryPaths;
            java.lang.String url = this.httpConfig.getUrl();
            if (url == null || url.length() == 0) {
                java.lang.String str = this.customUrl;
                if (str == null || str.length() == 0) {
                    throw new java.lang.NullPointerException("you should invoke setUrl or setCustomUrl first");
                }
            }
            java.lang.String str2 = this.customUrl;
            java.lang.String url2 = str2 == null || str2.length() == 0 ? this.httpConfig.getUrl() : this.customUrl;
            com.geely.rxhttp.impl.SimpleJsonRequest.Builder url3 = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).setContentType(this.httpConfig.getContentType()).useGateWay(this.httpConfig.getUseGateWay()).useCache(this.httpConfig.getUseCache()).setUrl(url2);
            if (this.httpConfig.getAppcode() != null) {
                url3.setAppCode(this.httpConfig.getAppcode());
            }
            if (!this.httpConfig.getUseGateWay()) {
                if (url2 == null) {
                    url2 = "";
                }
                if (com.geely.galaxy.network.GateWayUtils.useGateWay(url2)) {
                    url3.useGateWay(true);
                }
            }
            if (this.httpConfig.getHeaders() != null) {
                java.util.HashMap headers = this.httpConfig.getHeaders();
                kotlin.jvm.internal.Intrinsics.checkNotNull(headers);
                url3.setHeaders(headers);
            }
            if (this.httpConfig.getQueryParams() != null) {
                java.util.HashMap map = new java.util.HashMap();
                java.util.HashMap queryParams = this.httpConfig.getQueryParams();
                if (queryParams != null) {
                    for (java.util.Map.Entry entry : queryParams.entrySet()) {
                        map.put(entry.getKey(), java.lang.String.valueOf(entry.getValue()));
                    }
                }
                url3.setQueryParams(map);
            }
            if (this.httpConfig.getQueryPaths() != null && (queryPaths = this.httpConfig.getQueryPaths()) != null) {
                for (java.util.Map.Entry entry2 : queryPaths.entrySet()) {
                    url3.addPathParams((java.lang.String) entry2.getKey(), java.lang.String.valueOf(entry2.getValue()));
                }
            }
            if (this.httpConfig.getJson() != null) {
                url3.setJson(this.httpConfig.getJson());
            }
            if (this.httpConfig.requestBody != null) {
                url3.setRequestBody(this.httpConfig.requestBody);
            }
            return url3.build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class));
        }
    }
}
