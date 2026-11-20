package com.geely.snc.login;

/* JADX INFO: compiled from: GLLoginServiceImpl.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@com.geely.snc.login.utils.LDPProtect
@kotlin.Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0001\u0018\u0000 #2\u00020\u0001:\u0001#B\u0005¢\u0006\u0002\u0010\u0002J\n\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016J\n\u0010\b\u001a\u0004\u0018\u00010\u0006H\u0016J%\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n2\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J%\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000b0\n2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001d\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u000b0\nH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0019J%\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u000b0\n2\u0006\u0010\u001c\u001a\u00020\u001dH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ%\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u000b0\n2\u0006\u0010 \u001a\u00020!H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006$"}, d2 = {"Lcom/geely/snc/login/GLLoginServiceImpl;", "Lcom/geely/snc/login/IGLLoginService;", "()V", "loginApi", "Lcom/geely/snc/login/GLLoginServiceApi;", "getAccessToken", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "getRefreshToken", "getUserId", "hfScanLogin", "Lkotlinx/coroutines/flow/Flow;", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", "Lcom/geely/snc/login/model/rsp/HFScanLoginRspBean;", "hfScanLoginReqBean", "Lcom/geely/snc/login/model/req/HFScanLoginReqBean;", "(Lcom/geely/snc/login/model/req/HFScanLoginReqBean;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isLogin", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, com.geely.gpt.nfc.util.SpUtils.KEY_LOGIN, "Lcom/geely/snc/login/model/rsp/AuthLoginRspBean;", "authLoginReqBean", "Lcom/geely/snc/login/model/req/AuthLoginReqBean;", "(Lcom/geely/snc/login/model/req/AuthLoginReqBean;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshToken", "Lcom/geely/snc/login/model/rsp/RefreshTokenRspBean;", "refreshTokenReqBean", "Lcom/geely/snc/login/model/req/RefreshTokenReqBean;", "(Lcom/geely/snc/login/model/req/RefreshTokenReqBean;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scanCodeLogin", "scanCodeLoginReqBean", "Lcom/geely/snc/login/model/req/ScanCodeLoginReqBean;", "(Lcom/geely/snc/login/model/req/ScanCodeLoginReqBean;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "mod_login_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class GLLoginServiceImpl implements com.geely.snc.login.IGLLoginService {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.snc.login.GLLoginServiceImpl.Companion INSTANCE = new com.geely.snc.login.GLLoginServiceImpl.Companion(null);
    private static com.tencent.mmkv.MMKV mmkv;
    private static com.geely.snc.network.http.biz.manager.IGeelyUserManager userManager;
    private com.geely.snc.login.GLLoginServiceApi loginApi;

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.String getAccessToken();

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.String getRefreshToken();

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.String getUserId();

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.Object hfScanLogin(com.geely.snc.login.model.req.HFScanLoginReqBean hFScanLoginReqBean, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.HFScanLoginRspBean>>> continuation);

    @Override // com.geely.snc.login.IGLLoginService
    public native boolean isLogin();

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.Object login(com.geely.snc.login.model.req.AuthLoginReqBean authLoginReqBean, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>>> continuation);

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.Object logout(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<com.geely.snc.network.http.biz.model.http.BaseHttpResponse<java.lang.Object>>> continuation);

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.Object refreshToken(com.geely.snc.login.model.req.RefreshTokenReqBean refreshTokenReqBean, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.RefreshTokenRspBean>>> continuation);

    @Override // com.geely.snc.login.IGLLoginService
    public native java.lang.Object scanCodeLogin(com.geely.snc.login.model.req.ScanCodeLoginReqBean scanCodeLoginReqBean, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>>> continuation);

    public GLLoginServiceImpl() {
        com.geely.snc.network.http.IGLNetworkClient iGLNetworkClientCreateNetworkClient = com.geely.snc.network.http.biz.GLNetworkService.INSTANCE.createNetworkClient();
        iGLNetworkClientCreateNetworkClient.addInterceptor(kotlin.collections.CollectionsKt.listOf(new com.geely.snc.login.GLTokenInterceptor()));
        this.loginApi = (com.geely.snc.login.GLLoginServiceApi) iGLNetworkClientCreateNetworkClient.createApi(com.geely.snc.login.GLLoginServiceApi.class);
        userManager = new com.geely.snc.network.http.biz.manager.IGeelyUserManager() { // from class: com.geely.snc.login.GLLoginServiceImpl.1
            @Override // com.geely.snc.network.http.biz.manager.IGeelyUserManager
            public java.lang.String getAccessToken() {
                return com.geely.snc.login.GLLoginServiceImpl.INSTANCE.getMmkv().getString(com.geely.snc.login.GLLoginServiceImplKt.MODULE_LOGIN_ACCESS_TOKEN, (java.lang.String) null);
            }

            @Override // com.geely.snc.network.http.biz.manager.IGeelyUserManager
            public java.lang.String getRefreshToken() {
                return com.geely.snc.login.GLLoginServiceImpl.INSTANCE.getMmkv().getString(com.geely.snc.login.GLLoginServiceImplKt.MODULE_LOGIN_REFRESH_TOKEN, (java.lang.String) null);
            }
        };
        com.geely.snc.network.http.biz.GLNetworkService gLNetworkService = com.geely.snc.network.http.biz.GLNetworkService.INSTANCE;
        com.geely.snc.network.http.biz.manager.IGeelyUserManager iGeelyUserManager = userManager;
        kotlin.jvm.internal.Intrinsics.checkNotNull(iGeelyUserManager);
        gLNetworkService.setUserManager(iGeelyUserManager);
    }

    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lcom/geely/snc/login/GLLoginServiceImpl$Companion;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE, "()V", "mmkv", "Lcom/tencent/mmkv/MMKV;", "getMmkv", "()Lcom/tencent/mmkv/MMKV;", "setMmkv", "(Lcom/tencent/mmkv/MMKV;)V", "userManager", "Lcom/geely/snc/network/http/biz/manager/IGeelyUserManager;", "getUserManager", "()Lcom/geely/snc/network/http/biz/manager/IGeelyUserManager;", "setUserManager", "(Lcom/geely/snc/network/http/biz/manager/IGeelyUserManager;)V", "mod_login_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.geely.snc.network.http.biz.manager.IGeelyUserManager getUserManager() {
            return com.geely.snc.login.GLLoginServiceImpl.userManager;
        }

        public final void setUserManager(com.geely.snc.network.http.biz.manager.IGeelyUserManager iGeelyUserManager) {
            com.geely.snc.login.GLLoginServiceImpl.userManager = iGeelyUserManager;
        }

        public final com.tencent.mmkv.MMKV getMmkv() {
            return com.geely.snc.login.GLLoginServiceImpl.mmkv;
        }

        public final void setMmkv(com.tencent.mmkv.MMKV mmkv) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mmkv, "<set-?>");
            com.geely.snc.login.GLLoginServiceImpl.mmkv = mmkv;
        }
    }

    static {
        com.tencent.mmkv.MMKV mmkvMmkvWithID = com.tencent.mmkv.MMKV.mmkvWithID(com.geely.snc.login.GLLoginServiceImplKt.MODULE_LOGIN, 2);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(mmkvMmkvWithID, "mmkvWithID(MODULE_LOGIN, MMKV.MULTI_PROCESS_MODE)");
        mmkv = mmkvMmkvWithID;
    }

    /* JADX INFO: renamed from: com.geely.snc.login.GLLoginServiceImpl$login$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", "Lcom/geely/snc/login/model/rsp/AuthLoginRspBean;"}, k = 3, mv = {1, 5, 1}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.snc.login.GLLoginServiceImpl$login$2", f = "GLLoginServiceImpl.kt", i = {}, l = {com.geely.market.energy.common.R2.attr.actionMenuTextAppearance}, m = "invokeSuspend", n = {}, s = {})
    static final class C04042 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>>, java.lang.Object> {
        final /* synthetic */ com.geely.snc.login.model.req.AuthLoginReqBean $authLoginReqBean;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C04042(com.geely.snc.login.model.req.AuthLoginReqBean authLoginReqBean, kotlin.coroutines.Continuation<? super com.geely.snc.login.GLLoginServiceImpl.C04042> continuation) {
            super(1, continuation);
            this.$authLoginReqBean = authLoginReqBean;
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
            return com.geely.snc.login.GLLoginServiceImpl.this.new C04042(this.$authLoginReqBean, continuation);
        }

        public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>> continuation) {
            return create(continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                kotlin.ResultKt.throwOnFailure(obj);
                com.geely.snc.login.GLLoginServiceApi gLLoginServiceApi = com.geely.snc.login.GLLoginServiceImpl.this.loginApi;
                kotlin.jvm.internal.Intrinsics.checkNotNull(gLLoginServiceApi);
                this.label = 1;
                obj = gLLoginServiceApi.login(this.$authLoginReqBean, (kotlin.coroutines.Continuation) this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                kotlin.ResultKt.throwOnFailure(obj);
            }
            return obj;
        }
    }

    /* JADX INFO: renamed from: com.geely.snc.login.GLLoginServiceImpl$hfScanLogin$2, reason: invalid class name */
    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", "Lcom/geely/snc/login/model/rsp/HFScanLoginRspBean;"}, k = 3, mv = {1, 5, 1}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.snc.login.GLLoginServiceImpl$hfScanLogin$2", f = "GLLoginServiceImpl.kt", i = {}, l = {66}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.HFScanLoginRspBean>>, java.lang.Object> {
        final /* synthetic */ com.geely.snc.login.model.req.HFScanLoginReqBean $hfScanLoginReqBean;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(com.geely.snc.login.model.req.HFScanLoginReqBean hFScanLoginReqBean, kotlin.coroutines.Continuation<? super com.geely.snc.login.GLLoginServiceImpl.AnonymousClass2> continuation) {
            super(1, continuation);
            this.$hfScanLoginReqBean = hFScanLoginReqBean;
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
            return com.geely.snc.login.GLLoginServiceImpl.this.new AnonymousClass2(this.$hfScanLoginReqBean, continuation);
        }

        public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.HFScanLoginRspBean>> continuation) {
            return create(continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                kotlin.ResultKt.throwOnFailure(obj);
                com.geely.snc.login.GLLoginServiceApi gLLoginServiceApi = com.geely.snc.login.GLLoginServiceImpl.this.loginApi;
                kotlin.jvm.internal.Intrinsics.checkNotNull(gLLoginServiceApi);
                this.label = 1;
                obj = gLLoginServiceApi.hfScanLogin(this.$hfScanLoginReqBean, (kotlin.coroutines.Continuation) this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                kotlin.ResultKt.throwOnFailure(obj);
            }
            return obj;
        }
    }

    /* JADX INFO: renamed from: com.geely.snc.login.GLLoginServiceImpl$refreshToken$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", "Lcom/geely/snc/login/model/rsp/RefreshTokenRspBean;"}, k = 3, mv = {1, 5, 1}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.snc.login.GLLoginServiceImpl$refreshToken$2", f = "GLLoginServiceImpl.kt", i = {}, l = {com.geely.market.energy.common.R2.attr.actionModeSplitBackground}, m = "invokeSuspend", n = {}, s = {})
    static final class C04062 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.RefreshTokenRspBean>>, java.lang.Object> {
        final /* synthetic */ com.geely.snc.login.model.req.RefreshTokenReqBean $refreshTokenReqBean;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C04062(com.geely.snc.login.model.req.RefreshTokenReqBean refreshTokenReqBean, kotlin.coroutines.Continuation<? super com.geely.snc.login.GLLoginServiceImpl.C04062> continuation) {
            super(1, continuation);
            this.$refreshTokenReqBean = refreshTokenReqBean;
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
            return com.geely.snc.login.GLLoginServiceImpl.this.new C04062(this.$refreshTokenReqBean, continuation);
        }

        public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.RefreshTokenRspBean>> continuation) {
            return create(continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                kotlin.ResultKt.throwOnFailure(obj);
                com.geely.snc.login.GLLoginServiceApi gLLoginServiceApi = com.geely.snc.login.GLLoginServiceImpl.this.loginApi;
                kotlin.jvm.internal.Intrinsics.checkNotNull(gLLoginServiceApi);
                this.label = 1;
                obj = gLLoginServiceApi.refreshToken(this.$refreshTokenReqBean, (kotlin.coroutines.Continuation) this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                kotlin.ResultKt.throwOnFailure(obj);
            }
            return obj;
        }
    }

    /* JADX INFO: renamed from: com.geely.snc.login.GLLoginServiceImpl$logout$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", org.java_websocket.extensions.ExtensionRequestData.EMPTY_VALUE}, k = 3, mv = {1, 5, 1}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.snc.login.GLLoginServiceImpl$logout$2", f = "GLLoginServiceImpl.kt", i = {}, l = {com.geely.market.energy.common.R2.attr.actionOverflowButtonStyle}, m = "invokeSuspend", n = {}, s = {})
    static final class C04052 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<java.lang.Object>>, java.lang.Object> {
        int label;

        C04052(kotlin.coroutines.Continuation<? super com.geely.snc.login.GLLoginServiceImpl.C04052> continuation) {
            super(1, continuation);
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
            return com.geely.snc.login.GLLoginServiceImpl.this.new C04052(continuation);
        }

        public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<java.lang.Object>> continuation) {
            return create(continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                kotlin.ResultKt.throwOnFailure(obj);
                com.geely.snc.login.GLLoginServiceApi gLLoginServiceApi = com.geely.snc.login.GLLoginServiceImpl.this.loginApi;
                kotlin.jvm.internal.Intrinsics.checkNotNull(gLLoginServiceApi);
                this.label = 1;
                obj = gLLoginServiceApi.logout((kotlin.coroutines.Continuation) this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                kotlin.ResultKt.throwOnFailure(obj);
            }
            return obj;
        }
    }

    /* JADX INFO: renamed from: com.geely.snc.login.GLLoginServiceImpl$scanCodeLogin$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: GLLoginServiceImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "Lcom/geely/snc/network/http/biz/model/http/BaseHttpResponse;", "Lcom/geely/snc/login/model/rsp/AuthLoginRspBean;"}, k = 3, mv = {1, 5, 1}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.snc.login.GLLoginServiceImpl$scanCodeLogin$2", f = "GLLoginServiceImpl.kt", i = {}, l = {com.geely.market.energy.common.R2.attr.alertDialogCenterButtons}, m = "invokeSuspend", n = {}, s = {})
    static final class C04072 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function1<kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>>, java.lang.Object> {
        final /* synthetic */ com.geely.snc.login.model.req.ScanCodeLoginReqBean $scanCodeLoginReqBean;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C04072(com.geely.snc.login.model.req.ScanCodeLoginReqBean scanCodeLoginReqBean, kotlin.coroutines.Continuation<? super com.geely.snc.login.GLLoginServiceImpl.C04072> continuation) {
            super(1, continuation);
            this.$scanCodeLoginReqBean = scanCodeLoginReqBean;
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(kotlin.coroutines.Continuation<?> continuation) {
            return com.geely.snc.login.GLLoginServiceImpl.this.new C04072(this.$scanCodeLoginReqBean, continuation);
        }

        public final java.lang.Object invoke(kotlin.coroutines.Continuation<? super com.geely.snc.network.http.biz.model.http.BaseHttpResponse<com.geely.snc.login.model.rsp.AuthLoginRspBean>> continuation) {
            return create(continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                kotlin.ResultKt.throwOnFailure(obj);
                com.geely.snc.login.GLLoginServiceApi gLLoginServiceApi = com.geely.snc.login.GLLoginServiceImpl.this.loginApi;
                kotlin.jvm.internal.Intrinsics.checkNotNull(gLLoginServiceApi);
                this.label = 1;
                obj = gLLoginServiceApi.scanCodeLogin(this.$scanCodeLoginReqBean, (kotlin.coroutines.Continuation) this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                kotlin.ResultKt.throwOnFailure(obj);
            }
            return obj;
        }
    }
}
