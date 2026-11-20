package com.geely.rxhttp.impl;

/* JADX INFO: compiled from: SimpleJsonRequest.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\u0018\u0000 \u00042\u00020\u0001:\u0002\u0003\u0004B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0005"}, d2 = {"Lcom/geely/rxhttp/impl/SimpleJsonRequest;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "Builder", "Companion", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class SimpleJsonRequest {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.rxhttp.impl.SimpleJsonRequest.Companion INSTANCE = new com.geely.rxhttp.impl.SimpleJsonRequest.Companion(null);

    /* JADX INFO: compiled from: SimpleJsonRequest.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, d2 = {"Lcom/geely/rxhttp/impl/SimpleJsonRequest$Companion;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "newBuilder", "Lcom/geely/rxhttp/impl/SimpleJsonRequest$Builder;", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder newBuilder() {
            return new com.geely.rxhttp.impl.SimpleJsonRequest.Builder();
        }
    }

    /* JADX INFO: compiled from: SimpleJsonRequest.kt */
    @kotlin.Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0015\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00042\b\u0010 \u001a\u0004\u0018\u00010\u0004J\u0016\u0010!\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u0010 \u001a\u00020\u0004J\u0016\u0010\"\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u0010 \u001a\u00020\u0004J\u0018\u0010#\u001a\u00020\u00002\u0006\u0010\u001f\u001a\u00020\u00042\b\u0010 \u001a\u0004\u0018\u00010\u0004J$\u0010$\u001a\b\u0012\u0004\u0012\u0002H&0%\"\b\b\u0000\u0010&*\u00020\u00012\f\u0010'\u001a\b\u0012\u0004\u0012\u0002H&0(J\b\u0010)\u001a\u00020*H\u0002J\b\u0010+\u001a\u00020*H\u0002J\b\u0010,\u001a\u00020*H\u0002J\b\u0010-\u001a\u00020*H\u0002J\u0010\u0010.\u001a\u00020\u00002\b\u0010/\u001a\u0004\u0018\u00010\u0004J\u0010\u00100\u001a\u00020\u00002\b\u0010\u0007\u001a\u0004\u0018\u00010\bJ\u001c\u00101\u001a\u00020\u00002\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00040\nJ\u0010\u00102\u001a\u00020\u00002\b\u0010\u000b\u001a\u0004\u0018\u00010\u0004J\u0010\u00103\u001a\u00020\u00002\b\u0010\f\u001a\u0004\u0018\u00010\u0004J\u001f\u00104\u001a\u00020\u0000\"\b\b\u0000\u0010&*\u00020\u00012\b\u00105\u001a\u0004\u0018\u0001H&¢\u0006\u0002\u00106J\u0010\u00107\u001a\u00020\u00002\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u0010\u00108\u001a\u00020\u00002\b\u0010\u0010\u001a\u0004\u0018\u00010\u0004J\u001c\u00109\u001a\u00020\u00002\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00040\nJ\u0010\u0010:\u001a\u00020\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014J\u000e\u0010;\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010<\u001a\u00020\u00002\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0010\u0010=\u001a\u00020\u00002\b\u0010\u0019\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u0016J\u000e\u0010\u001b\u001a\u00020\u00002\u0006\u0010>\u001a\u00020\u0016J\u001a\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u00162\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001d\u001a\u00020\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u0005\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006?"}, d2 = {"Lcom/geely/rxhttp/impl/SimpleJsonRequest$Builder;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "appcode", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "bodiesParams", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, com.geely.lynkco.kit.gateway.base.Constants.KEY_NAME_CONTENT_TYPE, "Lcom/geely/rxhttp/ContentType;", com.geely.rxhttp.base.Constants.KEY_NAME_HEADERS, "Ljava/util/HashMap;", "host", "json", "method", "Lcom/geely/rxhttp/entity/HttpResponse$HttpMethod;", "mockFile", "path", "queryParams", "queryPaths", com.geely.rxhttp.base.Constants.KEY_NAME_REQUEST_BODY, "Lokhttp3/RequestBody;", "requireToken", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "scheme", "Lcom/geely/rxhttp/SchemeType;", "url", "useCache", "useGateWay", "useMock", "useMultipart", "addBodiesParam", "name", "value", "addHeader", "addPathParams", "addQueryParam", "build", "Lcom/geely/rxhttp/base/JsonRequest;", "T", "expected", "Lkotlin/reflect/KClass;", "ensureBodiesParams", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "ensureHeaders", "ensurePathParams", "ensureQueryParams", "setAppCode", com.geely.rxhttp.base.Constants.KEY_NAME_APPCODE, "setContentType", "setHeaders", "setHost", "setJson", "setJsonBody", "jsonBody", "(Ljava/lang/Object;)Lcom/geely/rxhttp/impl/SimpleJsonRequest$Builder;", "setMethod", "setPath", "setQueryParams", "setRequestBody", "setRequireToken", "setScheme", "setUrl", "useGateway", "SWHttpKit_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Builder {
        private java.lang.String appcode;
        private java.util.Map<java.lang.String, java.lang.String> bodiesParams;
        private java.util.HashMap<java.lang.String, java.lang.String> headers;
        private java.lang.String host;
        private java.lang.String json;
        private java.lang.String mockFile;
        private java.lang.String path;
        private java.util.HashMap<java.lang.String, java.lang.String> queryParams;
        private java.util.HashMap<java.lang.String, java.lang.String> queryPaths;
        private okhttp3.RequestBody requestBody;
        private com.geely.rxhttp.SchemeType scheme;
        private java.lang.String url;
        private boolean useCache;
        private boolean useGateWay;
        private boolean useMock;
        private boolean useMultipart;
        private com.geely.rxhttp.ContentType contentType = com.geely.rxhttp.ContentType.Default;
        private com.geely.rxhttp.entity.HttpResponse.HttpMethod method = com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET;
        private boolean requireToken = true;

        public static /* synthetic */ com.geely.rxhttp.impl.SimpleJsonRequest.Builder useMock$default(com.geely.rxhttp.impl.SimpleJsonRequest.Builder builder, boolean z, java.lang.String str, int i, java.lang.Object obj) {
            if ((i & 2) != 0) {
                str = null;
            }
            return builder.useMock(z, str);
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder useMock(boolean useMock, java.lang.String mockFile) {
            this.useMock = useMock;
            this.mockFile = mockFile;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder useCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder useGateWay(boolean useGateway) {
            this.useGateWay = useGateway;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setHeaders(java.util.HashMap<java.lang.String, java.lang.String> headers) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(headers, com.geely.rxhttp.base.Constants.KEY_NAME_HEADERS);
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(headers)) {
                this.headers = headers;
            }
            return this;
        }

        private final void ensureHeaders() {
            if (this.headers == null) {
                this.headers = new java.util.HashMap<>();
            }
        }

        private final void ensurePathParams() {
            if (this.queryPaths == null) {
                this.queryPaths = new java.util.HashMap<>();
            }
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder addHeader(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            ensureHeaders();
            java.util.HashMap<java.lang.String, java.lang.String> map = this.headers;
            kotlin.jvm.internal.Intrinsics.checkNotNull(map);
            map.put(name, value);
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder addPathParams(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            ensurePathParams();
            java.util.HashMap<java.lang.String, java.lang.String> map = this.queryPaths;
            kotlin.jvm.internal.Intrinsics.checkNotNull(map);
            map.put(name, value);
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setUrl(java.lang.String url) {
            this.url = url;
            try {
                android.net.Uri uri = android.net.Uri.parse(url);
                setHost(uri.getHost());
                setPath(uri.getPath());
                setScheme(com.geely.rxhttp.SchemeType.INSTANCE.convert(uri.getScheme()));
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setAppCode(java.lang.String appCode) {
            this.appcode = appCode;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setScheme(com.geely.rxhttp.SchemeType scheme) {
            this.scheme = scheme;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setHost(java.lang.String host) {
            this.host = host;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setPath(java.lang.String path) {
            this.path = path;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setContentType(com.geely.rxhttp.ContentType contentType) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(contentType);
            this.contentType = contentType;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod method) {
            if (method == null) {
                this.method = com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET;
            } else {
                this.method = method;
            }
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setQueryParams(java.util.HashMap<java.lang.String, java.lang.String> queryParams) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(queryParams, "queryParams");
            if (!com.geely.rxhttp.util.CollectionUtil.isEmpty(queryParams)) {
                this.queryParams = queryParams;
            }
            return this;
        }

        private final void ensureBodiesParams() {
            if (this.bodiesParams == null) {
                this.bodiesParams = new java.util.HashMap();
            }
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder addBodiesParam(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            ensureBodiesParams();
            java.util.Map<java.lang.String, java.lang.String> map = this.bodiesParams;
            kotlin.jvm.internal.Intrinsics.checkNotNull(map);
            map.put(name, value);
            return this;
        }

        private final void ensureQueryParams() {
            if (this.queryParams == null) {
                this.queryParams = new java.util.HashMap<>();
            }
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder addQueryParam(java.lang.String name, java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
            ensureQueryParams();
            java.util.HashMap<java.lang.String, java.lang.String> map = this.queryParams;
            kotlin.jvm.internal.Intrinsics.checkNotNull(map);
            map.put(name, value);
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setRequireToken(boolean requireToken) {
            this.requireToken = requireToken;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setJson(java.lang.String json) {
            this.json = json;
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder setRequestBody(okhttp3.RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public final <T> com.geely.rxhttp.impl.SimpleJsonRequest.Builder setJsonBody(T jsonBody) {
            this.json = com.geely.rxhttp.util.JsonUtilKt.toJson(jsonBody);
            return this;
        }

        public final com.geely.rxhttp.impl.SimpleJsonRequest.Builder useMultipart(boolean useMultipart) {
            this.useMultipart = useMultipart;
            return this;
        }

        public final <T> com.geely.rxhttp.base.JsonRequest<T> build(final kotlin.reflect.KClass<T> expected) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(expected, "expected");
            return new com.geely.rxhttp.base.JsonRequest<T>() { // from class: com.geely.rxhttp.impl.SimpleJsonRequest$Builder$build$1
                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.Boolean isLegal() {
                    return com.geely.rxhttp.base.JsonRequest.DefaultImpls.isLegal(this);
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.String getMockFile() {
                    return this.this$0.mockFile;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public kotlin.reflect.KClass<T> getExpectedCls() {
                    return expected;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public com.geely.rxhttp.entity.HttpResponse.HttpMethod getMethod() {
                    return this.this$0.method;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.util.HashMap<java.lang.String, java.lang.String> getQueryParams() {
                    return this.this$0.queryParams;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.util.Map<java.lang.String, java.lang.String> getBodies() {
                    return this.this$0.bodiesParams;
                }

                @Override // com.geely.rxhttp.base.JsonRequest
                public java.lang.String getJson() {
                    return this.this$0.json;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.util.HashMap<java.lang.String, java.lang.String> getHeaders() {
                    return this.this$0.headers;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.String getUrl() {
                    try {
                        java.lang.String str = this.this$0.url;
                        if (!(str == null || str.length() == 0)) {
                            return this.this$0.url;
                        }
                        java.lang.String value = "HTTP://";
                        com.geely.rxhttp.SchemeType schemeType = this.this$0.scheme;
                        com.alibaba.cloudapi.sdk.enums.Scheme schemeConvertApiScheme = null;
                        if ((schemeType == null ? null : schemeType.convertApiScheme()) != null) {
                            com.geely.rxhttp.SchemeType schemeType2 = this.this$0.scheme;
                            if (schemeType2 != null) {
                                schemeConvertApiScheme = schemeType2.convertApiScheme();
                            }
                            kotlin.jvm.internal.Intrinsics.checkNotNull(schemeConvertApiScheme);
                            value = schemeConvertApiScheme.getValue();
                            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(value, "scheme?.convertApiScheme()!!.value");
                        }
                        java.lang.String str2 = this.this$0.host;
                        kotlin.jvm.internal.Intrinsics.checkNotNull(str2);
                        if (str2.length() > 0) {
                            java.lang.String str3 = this.this$0.path;
                            kotlin.jvm.internal.Intrinsics.checkNotNull(str3);
                            if (str3.length() > 0) {
                                return value + this.this$0.host + this.this$0.path;
                            }
                        }
                        return this.this$0.url;
                    } catch (java.lang.Exception unused) {
                        return this.this$0.url;
                    }
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public boolean useCache() {
                    return this.this$0.useCache;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.Boolean requireToken() {
                    return java.lang.Boolean.valueOf(this.this$0.requireToken);
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.Boolean useMock() {
                    return java.lang.Boolean.valueOf(this.this$0.useMock);
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.String getHost() {
                    return this.this$0.host;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.String getPath() {
                    return this.this$0.path;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public com.geely.rxhttp.SchemeType getScheme() {
                    return this.this$0.scheme;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.lang.String getAppCode() {
                    return this.this$0.appcode;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public com.geely.rxhttp.ContentType getContentType() {
                    return this.this$0.contentType;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public java.util.HashMap<java.lang.String, java.lang.String> getQueryPaths() {
                    return this.this$0.queryPaths;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public boolean useGateWay() {
                    return this.this$0.useGateWay;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public boolean useMultipart() {
                    return this.this$0.useMultipart;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public okhttp3.RequestBody getRequestBody() {
                    return this.this$0.requestBody;
                }

                @Override // com.geely.rxhttp.base.IBaseRequest
                public boolean verify() {
                    if (getScheme() != null && getHost() != null) {
                        java.lang.String path = getPath();
                        if (!(path == null || path.length() == 0) && ((getMethod() != com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET && getMethod() != com.geely.rxhttp.entity.HttpResponse.HttpMethod.DELETE && getMethod() != com.geely.rxhttp.entity.HttpResponse.HttpMethod.HEAD) || (getContentType() != com.geely.rxhttp.ContentType.Form && getContentType() != com.geely.rxhttp.ContentType.Json))) {
                            if (getMethod() == com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST || getMethod() == com.geely.rxhttp.entity.HttpResponse.HttpMethod.PUT || getMethod() == com.geely.rxhttp.entity.HttpResponse.HttpMethod.PATCH) {
                                getContentType();
                                com.geely.rxhttp.ContentType contentType = com.geely.rxhttp.ContentType.Default;
                            }
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
    }
}
