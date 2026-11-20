package com.geely.galaxy.uc.http;

/* JADX INFO: compiled from: TokenProvider.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, d2 = {"Lcom/geely/galaxy/uc/http/TokenProvider;", "", "()V", "Companion", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class TokenProvider {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.uc.http.TokenProvider.Companion INSTANCE;
    private static java.lang.String mBaseUrl;
    private static com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider tokenProvider;

    /* JADX INFO: compiled from: TokenProvider.kt */
    @kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bH\u0002J\b\u0010\n\u001a\u00020\u0004H\u0002J\u0006\u0010\u000b\u001a\u00020\u0006J\n\u0010\f\u001a\u0004\u0018\u00010\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/geely/galaxy/uc/http/TokenProvider$Companion;", "", "()V", "mBaseUrl", "", "tokenProvider", "Lcom/geely/rxhttp/intereptor/SessionInterceptor$ITokenProvider;", "getHeaderInterpolators", "", "Lcom/geely/sweet/lib/token/IHeaderInterpolator;", "getHostByAppEnv", "getProvider", "getTokenProtocol", "Lcom/geely/sweet/lib/token/ITokenProtocol;", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Companion {

        /* JADX INFO: compiled from: TokenProvider.kt */
        @kotlin.Metadata(k = 3, mv = {1, 8, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[com.geely.swannotation.base.AppEnvKit.values().length];
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.TRA.ordinal()] = 1;
                } catch (java.lang.NoSuchFieldError unused) {
                }
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.UAT.ordinal()] = 2;
                } catch (java.lang.NoSuchFieldError unused2) {
                }
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.SIT.ordinal()] = 3;
                } catch (java.lang.NoSuchFieldError unused3) {
                }
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.Dev.ordinal()] = 4;
                } catch (java.lang.NoSuchFieldError unused4) {
                }
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.Product.ordinal()] = 5;
                } catch (java.lang.NoSuchFieldError unused5) {
                }
                try {
                    iArr[com.geely.swannotation.base.AppEnvKit.PRE.ordinal()] = 6;
                } catch (java.lang.NoSuchFieldError unused6) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider getProvider() {
            return com.geely.galaxy.uc.http.TokenProvider.tokenProvider;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final java.lang.String getHostByAppEnv() {
            com.geely.swannotation.base.AppEnvKit currentEnv = com.geely.core.common.env.AppEnvProxy.Companion.getInstance().getCurrentEnv();
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"AutoRefreshInteceptor", "env:" + currentEnv});
            switch (com.geely.galaxy.uc.http.TokenProvider.Companion.WhenMappings.$EnumSwitchMapping$0[currentEnv.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return com.geely.galaxy.uc.http.GalaxyUserServiceApi.HOST.UAT_HOST.getIpAndPort();
                case 5:
                    return com.geely.galaxy.uc.http.GalaxyUserServiceApi.HOST.PRODUCT_HOST.getIpAndPort();
                case 6:
                    return com.geely.galaxy.uc.http.GalaxyUserServiceApi.HOST.PRE_HOST.getIpAndPort();
                default:
                    return com.geely.galaxy.uc.http.GalaxyUserServiceApi.HOST.PRODUCT_HOST.getIpAndPort();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final java.util.List<com.geely.sweet.lib.token.IHeaderInterpolator> getHeaderInterpolators() {
            return com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().fetchInterpolators();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final com.geely.sweet.lib.token.ITokenProtocol getTokenProtocol() {
            return com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().fetchProtocol();
        }
    }

    static {
        com.geely.galaxy.uc.http.TokenProvider.Companion companion = new com.geely.galaxy.uc.http.TokenProvider.Companion(null);
        INSTANCE = companion;
        mBaseUrl = companion.getHostByAppEnv();
        tokenProvider = new com.geely.rxhttp.intereptor.SessionInterceptor.ITokenProvider() { // from class: com.geely.galaxy.uc.http.TokenProvider$Companion$tokenProvider$1
            public java.util.Map<java.lang.String, java.lang.String> provideHeaders() {
                java.util.List<com.geely.sweet.lib.token.IHeaderInterpolator> headerInterpolators = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getHeaderInterpolators();
                java.util.List list = headerInterpolators;
                if (list == null || list.isEmpty()) {
                    return null;
                }
                java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
                for (com.geely.sweet.lib.token.IHeaderInterpolator iHeaderInterpolator : headerInterpolators) {
                    java.util.Map mapProvideHeaders = iHeaderInterpolator != null ? iHeaderInterpolator.provideHeaders() : null;
                    if (mapProvideHeaders != null && (mapProvideHeaders.isEmpty() ^ true)) {
                        linkedHashMap.putAll(mapProvideHeaders);
                    }
                }
                return linkedHashMap;
            }

            public java.util.Map<java.lang.String, java.lang.String> provideHeader(okhttp3.Request request) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
                com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getTokenProtocol();
                if (tokenProtocol != null) {
                    return tokenProtocol.provideHeader(request);
                }
                return null;
            }

            public boolean refreshToken(java.lang.String url, java.lang.String data) {
                com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getTokenProtocol();
                if (tokenProtocol != null) {
                    return tokenProtocol.tryRefreshToken(url, data);
                }
                return false;
            }

            public void handleResponseCode(java.lang.String url, java.lang.String code) {
                com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getTokenProtocol();
                if (tokenProtocol != null) {
                    tokenProtocol.handleResponseCode(url, code);
                }
            }

            public java.lang.String provideBaseUrl() {
                return com.geely.galaxy.uc.http.TokenProvider.mBaseUrl;
            }

            public java.lang.String provideToken() {
                com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getTokenProtocol();
                if (tokenProtocol != null) {
                    return tokenProtocol.getToken();
                }
                return null;
            }

            public java.lang.String provideSvcsid() {
                com.geely.sweet.lib.token.ITokenProtocol tokenProtocol = com.geely.galaxy.uc.http.TokenProvider.INSTANCE.getTokenProtocol();
                if (tokenProtocol != null) {
                    return tokenProtocol.getVcsid();
                }
                return null;
            }
        };
    }
}
