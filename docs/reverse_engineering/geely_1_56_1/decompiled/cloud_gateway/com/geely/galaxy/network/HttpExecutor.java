package com.geely.galaxy.network;

/* JADX INFO: compiled from: HttpExcutor.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/geely/galaxy/network/HttpExecutor;", "", "()V", "Companion", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class HttpExecutor {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.network.HttpExecutor.Companion INSTANCE = new com.geely.galaxy.network.HttpExecutor.Companion(null);

    @kotlin.jvm.JvmStatic
    public static final void addInterceptor(okhttp3.Interceptor interceptor) {
        INSTANCE.addInterceptor(interceptor);
    }

    @kotlin.jvm.JvmStatic
    public static final <T> io.reactivex.Observable<T> execute(com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequest, java.lang.reflect.Type type) {
        return INSTANCE.execute(iBaseRequest, type);
    }

    @kotlin.jvm.JvmStatic
    public static final <T> com.geely.rxhttp.base.IHttpResponse<T> executeSync(com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequest, java.lang.reflect.Type type) {
        return INSTANCE.executeSync(iBaseRequest, type);
    }

    @kotlin.jvm.JvmStatic
    public static final void initGateWay(java.lang.String str, java.lang.String str2, java.lang.String str3) {
        INSTANCE.initGateWay(str, str2, str3);
    }

    @kotlin.jvm.JvmStatic
    public static final void initialize(java.lang.String str) {
        INSTANCE.initialize(str);
    }

    /* JADX INFO: compiled from: HttpExcutor.kt */
    @kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J'\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\b\"\u0006\b\u0000\u0010\t\u0018\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0087\bJ,\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0007J'\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\u0010\"\u0006\b\u0000\u0010\t\u0018\u00012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0087\bJ,\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\u0010\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0007J&\u0010\u0011\u001a\u00020\u00042\b\u0010\u0012\u001a\u0004\u0018\u00010\f2\b\u0010\u0013\u001a\u0004\u0018\u00010\f2\b\u0010\u0014\u001a\u0004\u0018\u00010\fH\u0007J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\fH\u0007¨\u0006\u0017"}, d2 = {"Lcom/geely/galaxy/network/HttpExecutor$Companion;", "", "()V", "addInterceptor", "", "interceptor", "Lokhttp3/Interceptor;", "execute", "Lio/reactivex/Observable;", "T", "request", "Lcom/geely/rxhttp/base/IBaseRequest;", "", "typeOfT", "Ljava/lang/reflect/Type;", "executeSync", "Lcom/geely/rxhttp/base/IHttpResponse;", "initGateWay", "appKey", "appSecret", "appCode", "initialize", "baseUrl", "com.geely.galaxy.network"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @kotlin.jvm.JvmStatic
        public final /* synthetic */ <T> io.reactivex.Observable<T> execute(com.geely.rxhttp.base.IBaseRequest<java.lang.String> request) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
            com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
            kotlin.jvm.internal.Intrinsics.needClassReification();
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<T>() { // from class: com.geely.galaxy.network.HttpExecutor$Companion$execute$1
            }.getType();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
            return httpExecutorProxy.execute(request, type);
        }

        @kotlin.jvm.JvmStatic
        public final <T> io.reactivex.Observable<T> execute(com.geely.rxhttp.base.IBaseRequest<java.lang.String> request, java.lang.reflect.Type typeOfT) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeOfT, "typeOfT");
            return com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE.execute(request, typeOfT);
        }

        @kotlin.jvm.JvmStatic
        public final /* synthetic */ <T> com.geely.rxhttp.base.IHttpResponse<T> executeSync(com.geely.rxhttp.base.IBaseRequest<java.lang.String> request) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
            com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
            kotlin.jvm.internal.Intrinsics.needClassReification();
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<T>() { // from class: com.geely.galaxy.network.HttpExecutor$Companion$executeSync$1
            }.getType();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
            return httpExecutorProxy.executeSync(request, type);
        }

        @kotlin.jvm.JvmStatic
        public final <T> com.geely.rxhttp.base.IHttpResponse<T> executeSync(com.geely.rxhttp.base.IBaseRequest<java.lang.String> request, java.lang.reflect.Type typeOfT) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeOfT, "typeOfT");
            return com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE.executeSync(request, typeOfT);
        }

        @kotlin.jvm.JvmStatic
        public final void initialize(java.lang.String baseUrl) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(baseUrl, "baseUrl");
            com.geely.galaxy.network.config.NetWorkConfigKt.setBaseHttpUrl(baseUrl);
        }

        @kotlin.jvm.JvmStatic
        public final void addInterceptor(okhttp3.Interceptor interceptor) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(interceptor, "interceptor");
            com.geely.rxhttp.SWHttpComponent.Companion.getInstance().addInterceptor(interceptor);
        }

        @kotlin.jvm.JvmStatic
        public final void initGateWay(java.lang.String appKey, java.lang.String appSecret, java.lang.String appCode) {
            com.geely.rxhttp.SWHttpComponent.Companion.getInstance().initGateWay(appKey, appSecret, appCode);
        }
    }
}
