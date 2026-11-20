package com.geely.galaxy.network.impl;

/* JADX INFO: compiled from: GetHttpConfigImpl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/geely/galaxy/network/impl/GetHttpConfigImpl;", "Lcom/geely/galaxy/network/config/BaseHttpConfig;", "()V", "Builder", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class GetHttpConfigImpl extends com.geely.galaxy.network.config.BaseHttpConfig {

    /* JADX INFO: compiled from: GetHttpConfigImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u00042\b\u0010\t\u001a\u0004\u0018\u00010\u0001J\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\fJ\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0002J\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0004J\u0010\u0010\u0012\u001a\u00020\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u0004J.\u0010\u0014\u001a\u00020\u00002&\u0010\u0015\u001a\"\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0016j\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004`\u0017J\u0012\u0010\u0018\u001a\u00020\u00002\b\u0010\u0019\u001a\u0004\u0018\u00010\u0004H\u0002J\u0012\u0010\u001a\u001a\u00020\u00002\b\u0010\u001b\u001a\u0004\u0018\u00010\u0004H\u0002J\u001c\u0010\u001c\u001a\u00020\u00002\u0014\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001eJ\u0012\u0010\u001f\u001a\u00020\u00002\b\u0010 \u001a\u0004\u0018\u00010!H\u0002J\u0010\u0010\"\u001a\u00020\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u0004J\u000e\u0010#\u001a\u00020\u00002\u0006\u0010#\u001a\u00020$J\u000e\u0010%\u001a\u00020\u00002\u0006\u0010&\u001a\u00020$R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/geely/galaxy/network/impl/GetHttpConfigImpl$Builder;", "", "()V", "customUrl", "", "httpConfig", "Lcom/geely/galaxy/network/impl/GetHttpConfigImpl;", "addHeader", "name", "value", "addQueryParam", "build", "Lcom/geely/rxhttp/base/JsonRequest;", "ensureHeaders", "", "ensureQueryParams", "setAppCode", "appCode", "setCustomUrl", "url", "setHeaders", "headers", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "setHost", "host", "setPath", "path", "setQueryParams", "queryParams", "", "setScheme", "scheme", "Lcom/geely/rxhttp/SchemeType;", "setUrl", "useCache", "", "useGateWay", "useGateway", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Builder {
        private java.lang.String customUrl;
        private com.geely.galaxy.network.impl.GetHttpConfigImpl httpConfig = new com.geely.galaxy.network.impl.GetHttpConfigImpl();

        private final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setScheme(com.geely.rxhttp.SchemeType scheme) {
            this.httpConfig.setScheme(scheme);
            return this;
        }

        private final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setHost(java.lang.String host) {
            this.httpConfig.setHost(host);
            return this;
        }

        private final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setPath(java.lang.String path) {
            this.httpConfig.setPath(path);
            return this;
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setUrl(java.lang.String url) {
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

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setCustomUrl(java.lang.String url) {
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

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setHeaders(java.util.HashMap<java.lang.String, java.lang.String> headers) {
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

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder addHeader(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            ensureHeaders();
            java.util.HashMap headers = this.httpConfig.getHeaders();
            kotlin.jvm.internal.Intrinsics.checkNotNull(headers);
            headers.put(name, value);
            return this;
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder useGateWay(boolean useGateway) {
            this.httpConfig.setUseGateWay(useGateway);
            return this;
        }

        private final void ensureQueryParams() {
            if (this.httpConfig.getQueryParams() == null) {
                this.httpConfig.setQueryParams(new java.util.HashMap());
            }
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setQueryParams(java.util.Map<java.lang.String, ? extends java.lang.Object> queryParams) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(queryParams, "queryParams");
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(queryParams)) {
                this.httpConfig.setQueryParams((java.util.HashMap) queryParams);
            }
            return this;
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder addQueryParam(java.lang.String name, java.lang.Object value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            ensureQueryParams();
            java.util.HashMap queryParams = this.httpConfig.getQueryParams();
            kotlin.jvm.internal.Intrinsics.checkNotNull(queryParams);
            queryParams.put(name, value);
            return this;
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder useCache(boolean useCache) {
            this.httpConfig.setUseCache(useCache);
            return this;
        }

        public final com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder setAppCode(java.lang.String appCode) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(appCode, "appCode");
            this.httpConfig.setAppcode(appCode);
            return this;
        }

        public final com.geely.rxhttp.base.JsonRequest<java.lang.String> build() {
            java.lang.String url = this.httpConfig.getUrl();
            if (url == null || url.length() == 0) {
                java.lang.String str = this.customUrl;
                if (str == null || str.length() == 0) {
                    throw new java.lang.NullPointerException("you should invoke setUrl or setCustomUrl first");
                }
            }
            java.lang.String str2 = this.customUrl;
            java.lang.String url2 = str2 == null || str2.length() == 0 ? this.httpConfig.getUrl() : this.customUrl;
            com.geely.rxhttp.impl.SimpleJsonRequest.Builder url3 = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).useGateWay(this.httpConfig.getUseGateWay()).useCache(this.httpConfig.getUseCache()).setUrl(url2);
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
            return url3.build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class));
        }
    }
}
