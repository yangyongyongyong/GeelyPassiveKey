package com.geely.galaxy.uc.repository;

/* JADX INFO: compiled from: UserRepository.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 n2\u00020\u0001:\u0001nB!\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ,\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J\u001e\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u001e\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010\u001b\u001a\u0004\u0018\u00010\u0019H\u0016J\u001c\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u001d\u001a\u00020\u0005H\u0016J\u0014\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H\u0016J\u001c\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010 \u001a\u00020\u0005H\u0016J\u001c\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\"\u001a\u00020\u0005H\u0016J\u001e\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010$\u001a\u0004\u0018\u00010\u0019H\u0016J$\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005H\u0016J\u001c\u0010'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010(\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020\u0005H\u0016J\u0014\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H\u0016J$\u0010,\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050-j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`.H\u0002J\u0014\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H\u0016J6\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00052\u0006\u00103\u001a\u00020\u00052\b\u00104\u001a\u0004\u0018\u00010\u0005H\u0016J\n\u00105\u001a\u0004\u0018\u00010\u0005H\u0002J\u0014\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002070\u00110\u0010H\u0016J\u001c\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002090\u00110\u00102\u0006\u0010:\u001a\u00020\u0005H\u0016J\u0014\u0010;\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H\u0016J\u0016\u0010<\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010>0=0\u0010H\u0016J\u001c\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010@\u001a\u00020\u0005H\u0016J$\u0010A\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J\u001e\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010C\u001a\u0004\u0018\u00010\u0019H\u0016J\u001c\u0010D\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J$\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010F\u001a\u00020\u00052\u0006\u0010G\u001a\u00020\u0005H\u0016J\u001e\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010C\u001a\u0004\u0018\u00010\u0019H\u0016J.\u0010I\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\u0006\u0010J\u001a\u00020\u00052\b\u0010K\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010@\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010@\u001a\u00020\u0005H\u0016J\u001c\u0010L\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u00101\u001a\u00020\u0005H\u0016J\u001e\u0010M\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010\u001b\u001a\u0004\u0018\u00010\u0019H\u0016J\u0018\u0010N\u001a\u00020O2\u0006\u0010P\u001a\u00020\u00052\u0006\u0010Q\u001a\u00020\u0007H\u0002J\u001c\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u00101\u001a\u00020\u0005H\u0016J\u001e\u0010S\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010K\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010T\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J\u001e\u0010U\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010K\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010V\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J$\u0010W\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J&\u0010X\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\b\u0010K\u001a\u0004\u0018\u00010\u0005H\u0016J&\u0010Y\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\b\u0010K\u001a\u0004\u0018\u00010\u0005H\u0016J\u001e\u0010Z\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010[\u001a\u0004\u0018\u00010\u0005H\u0016J&\u0010\\\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\b\u0010[\u001a\u0004\u0018\u00010\u0005H\u0016J\u0014\u0010]\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u0010H\u0016J$\u0010^\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J$\u0010_\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0016J\u001e\u0010`\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u001c\u0010a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0006\u0010b\u001a\u00020cH\u0016J(\u0010a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\u0012\u0010d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050eH\u0016J\"\u0010f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00110\u00102\f\u0010g\u001a\b\u0012\u0004\u0012\u00020\u00050hH\u0016J\u001e\u0010i\u001a\b\u0012\u0004\u0012\u00020j0\u00102\u0006\u0010k\u001a\u00020l2\u0006\u0010m\u001a\u000207H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006o"}, d2 = {"Lcom/geely/galaxy/uc/repository/UserRepositoryImpl;", "Lcom/geely/galaxy/uc/repository/UserRepository;", "scheme", "Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$SCHEME;", "host", "", "useGateway", "", "(Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$SCHEME;Ljava/lang/String;Z)V", "httpProxy", "Lcom/geely/rxhttp/ISWHttpProxy;", "getHttpProxy", "()Lcom/geely/rxhttp/ISWHttpProxy;", "httpProxy$delegate", "Lkotlin/Lazy;", "CancellationWithReasons", "Lio/reactivex/Observable;", "Lcom/geely/rxhttp/base/IHttpResponse;", "cancellationCause", "causeDescription", "authCode", "OneKeyLogin", "accessToken", "addAddress", "address", "", "bindDevice", "accountDevice", "changePassword", "newPassword", "clientDetail", "deleteAddressByID", "address_id", "deletetLoginDevices", "device_id", "execThirdPartyLogin", "bindReq", "forgetPassword", "phoneNumber", "getAddressList", "pageNo", "", "getApiBaseUrl", "getCancellationReasons", "getDeviceInfo", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getLoginDevices", "getOAuth2Code", com.ecarx.open.http.utils.iovgoto.iovdo, "scope", "responseType", "state", "getOsVersion", "getOssToken", "Lcom/geely/galaxy/uc/beans/OssToken;", "getProtocolDetail", "Lcom/geely/galaxy/uc/beans/UserProtocol;", "protocolType", "getRemoteUserInfo", "getUserSecuritySettings", "Lcom/geely/galaxy/uc/http/BaseResponce;", "Lcom/geely/galaxy/uc/beans/UserSecurity;", "logout", "refreshToken", "mobileCodeLogin", "modifyMobile", "reqPwd", "oneKeyModifyMobileVerify", "oneOSAuthorize", com.elynkco.common.utils.CacheUtil.uuid, "action", "onekeyModifyMobile", "passwordLogin", "password", "certifyId", "removeAuth", "removeDevice", "requestBuilder", "Lcom/geely/rxhttp/impl/SimpleJsonRequest$Builder;", "url", "requireToken", "scopeDetail", "sendChangePasswordSMS", "sendChangePasswordSMSVerify", "sendDeregisterSMS", "sendDeregisterSMSVerify", "sendFogetPasswordSMSVerify", "sendForgetPasswordSMS", "sendLoginSMS", "sendModifyMobile", "challenge", "sendNewModifyMobile", "setUserInforDefault", "smsModifyMobileVerify", "thirdAppOAuth2Authorize", "updateAddress", "updateUser", "user", "Lcom/geely/galaxy/uc/beans/User;", "map", "", "uploadDeregisterAgreement", com.geely.galaxy.mine.constant.RouterParams.codes, "", "uploadUserAvatar", "Lcom/alibaba/sdk/android/oss/OSSClient;", "context", "Landroid/content/Context;", "ossToken", "Companion", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class UserRepositoryImpl implements com.geely.galaxy.uc.repository.UserRepository {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion INSTANCE = new com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion(null);
    private static final java.lang.String TAG = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.repository.UserRepositoryImpl.class).getSimpleName();
    private static volatile com.geely.galaxy.uc.repository.UserRepository instance;
    private java.lang.String host;

    /* JADX INFO: renamed from: httpProxy$delegate, reason: from kotlin metadata */
    private final kotlin.Lazy httpProxy;
    private com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME scheme;
    private boolean useGateway;

    public UserRepositoryImpl(com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME scheme, java.lang.String str, boolean z) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scheme, "scheme");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "host");
        this.scheme = scheme;
        this.host = str;
        this.useGateway = z;
        this.httpProxy = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.geely.rxhttp.ISWHttpProxy>() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$httpProxy$2
            {
                super(0);
            }

            /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
            public final com.geely.rxhttp.ISWHttpProxy m1816invoke() {
                com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl = this.this$0;
                userRepositoryImpl.useGateway = com.geely.galaxy.network.GateWayUtils.useGateWay(userRepositoryImpl.host);
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.repository.UserRepositoryImpl.TAG, "getGateWayUseEnable: " + this.this$0.useGateway});
                return com.geely.rxhttp.SWHttpComponent.Companion.getInstance();
            }
        });
    }

    public /* synthetic */ UserRepositoryImpl(com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME scheme, java.lang.String str, boolean z, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(scheme, str, (i & 4) != 0 ? false : z);
    }

    /* JADX INFO: compiled from: UserRepository.kt */
    @kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\u0004H\u0002J\u0016\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/geely/galaxy/uc/repository/UserRepositoryImpl$Companion;", "", "()V", "TAG", "", "instance", "Lcom/geely/galaxy/uc/repository/UserRepository;", "getHostByAppEnv", "getInstance", "scheme", "Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$SCHEME;", "useGateway", "", "getSchemeByAppEnv", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Companion {

        /* JADX INFO: compiled from: UserRepository.kt */
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

        public final com.geely.galaxy.uc.repository.UserRepository getInstance(com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME scheme, boolean useGateway) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scheme, "scheme");
            if (com.geely.galaxy.uc.repository.UserRepositoryImpl.instance == null) {
                synchronized (com.geely.galaxy.uc.repository.UserRepository.class) {
                    if (com.geely.galaxy.uc.repository.UserRepositoryImpl.instance == null) {
                        com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion companion = com.geely.galaxy.uc.repository.UserRepositoryImpl.INSTANCE;
                        com.geely.galaxy.uc.repository.UserRepositoryImpl.instance = new com.geely.galaxy.uc.repository.UserRepositoryImpl(scheme, com.geely.galaxy.uc.repository.UserRepositoryImpl.INSTANCE.getHostByAppEnv(), useGateway);
                    }
                    kotlin.Unit unit = kotlin.Unit.INSTANCE;
                }
            }
            com.geely.galaxy.uc.repository.UserRepository userRepository = com.geely.galaxy.uc.repository.UserRepositoryImpl.instance;
            kotlin.jvm.internal.Intrinsics.checkNotNull(userRepository);
            return userRepository;
        }

        private final java.lang.String getHostByAppEnv() {
            com.geely.swannotation.base.AppEnvKit currentEnv = com.geely.core.common.env.AppEnvProxy.Companion.getInstance().getCurrentEnv();
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.repository.UserRepositoryImpl.TAG, "env:" + currentEnv});
            switch (com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion.WhenMappings.$EnumSwitchMapping$0[currentEnv.ordinal()]) {
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

        public final com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME getSchemeByAppEnv() {
            int i = com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion.WhenMappings.$EnumSwitchMapping$0[com.geely.core.common.env.AppEnvProxy.Companion.getInstance().getCurrentEnv().ordinal()];
            if (i == 2) {
                return com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME.HTTPS;
            }
            if (i == 5) {
                return com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME.HTTP;
            }
            if (i == 6) {
                return com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME.HTTPS;
            }
            return com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME.HTTPS;
        }
    }

    private final com.geely.rxhttp.ISWHttpProxy getHttpProxy() {
        return (com.geely.rxhttp.ISWHttpProxy) this.httpProxy.getValue();
    }

    private final com.geely.rxhttp.impl.SimpleJsonRequest.Builder requestBuilder(java.lang.String url, boolean requireToken) {
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder appCode = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setHost(this.host).setUrl(url).setRequireToken(requireToken).useGateWay(this.useGateway).addHeader("appId", com.geely.galaxy.usercenter.BuildConfig.APP_ID).addHeader("tenantId", com.geely.galaxy.usercenter.BuildConfig.TENANT_ID).setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE);
        if (requireToken) {
            appCode.addHeader(com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true");
        }
        return appCode;
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendLoginSMS(final java.lang.String phoneNumber, final java.lang.String certifyId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda30
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendLoginSMS$lambda$2(this.f$0, phoneNumber, certifyId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendLoginSMS$lambda$2(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/sendSms", false).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true");
        java.util.HashMap map = new java.util.HashMap();
        map.put("mobile", str);
        java.lang.String str3 = str2;
        if (!(str3 == null || str3.length() == 0)) {
            map.put("certifyId", str2);
        }
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader.setJsonBody(map).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendDeregisterSMS(final java.lang.String certifyId) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda6
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendDeregisterSMS$lambda$4(this.f$0, certifyId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendDeregisterSMS$lambda$4(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        boolean z = true;
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderUseGateWay = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/cancellation/sendCancellationCode", true).useGateWay(userRepositoryImpl.useGateway);
        java.lang.String str2 = str;
        if (str2 != null && str2.length() != 0) {
            z = false;
        }
        if (!z) {
            builderUseGateWay.addQueryParam("certifyId", str);
        }
        observableEmitter.onNext(httpProxy.execRequest(builderUseGateWay.addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendDeregisterSMSVerify(final java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda9
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendDeregisterSMSVerify$lambda$5(this.f$0, authCode, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendDeregisterSMSVerify$lambda$5(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$authCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/cancellation/smsVerify", true).addQueryParam("smsCode", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendChangePasswordSMSVerify(final java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda39
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendChangePasswordSMSVerify$lambda$6(this.f$0, authCode, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendChangePasswordSMSVerify$lambda$6(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$authCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/modify/smsVerify", true).addQueryParam("smsCode", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendFogetPasswordSMSVerify(final java.lang.String phoneNumber, final java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda7
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendFogetPasswordSMSVerify$lambda$8(this.f$0, phoneNumber, authCode, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendFogetPasswordSMSVerify$lambda$8(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$authCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequestBuilder = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/forget/smsVerify", false);
        java.util.HashMap map = new java.util.HashMap();
        map.put("mobile", str);
        map.put("smsCode", str2);
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(builderRequestBuilder.setJsonBody(map).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> mobileCodeLogin(final java.lang.String phoneNumber, final java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        final java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = getDeviceInfo();
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda20
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.mobileCodeLogin$lambda$9(this.f$0, deviceInfo, phoneNumber, authCode, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void mobileCodeLogin$lambda$9(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.util.HashMap map, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$deviceInfo");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$authCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequestBuilder = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/mobileCodeLogin", false);
        java.lang.String str3 = (java.lang.String) map.get(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION);
        if (str3 == null) {
            str3 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = builderRequestBuilder.addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, str3);
        java.lang.String str4 = (java.lang.String) map.get("deviceId");
        if (str4 == null) {
            str4 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader2 = builderAddHeader.addHeader("deviceId", str4);
        java.lang.String str5 = (java.lang.String) map.get("deviceModel");
        if (str5 == null) {
            str5 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader3 = builderAddHeader2.addHeader("deviceModel", str5).addHeader("deviceType", "android");
        java.lang.String str6 = (java.lang.String) map.get("hardwareDeviceId");
        if (str6 == null) {
            str6 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader4 = builderAddHeader3.addHeader("hardwareDeviceId", str6).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true");
        java.lang.String currentCertifyId = com.geely.galaxy.uc.util.SecuritySdkManager.INSTANCE.getCurrentCertifyId();
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader4.addHeader("certifyId", currentCertifyId != null ? currentCertifyId : "").addQueryParam("mobile", str).addQueryParam("verificationCode", str2).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> passwordLogin(final java.lang.String phoneNumber, final java.lang.String password, final java.lang.String certifyId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(password, "password");
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"Login", "passwordLogin phoneNumber:" + phoneNumber + ",password:" + password});
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda10
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.passwordLogin$lambda$11(this.f$0, phoneNumber, password, certifyId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void passwordLogin$lambda$11(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, java.lang.String str3, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$password");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = userRepositoryImpl.getDeviceInfo();
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/pwdLogin", false).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true");
        java.lang.String currentCertifyId = com.geely.galaxy.uc.util.SecuritySdkManager.INSTANCE.getCurrentCertifyId();
        if (currentCertifyId == null) {
            currentCertifyId = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader2 = builderAddHeader.addHeader("certifyId", currentCertifyId);
        java.lang.String str4 = deviceInfo.get("hardwareDeviceId");
        if (str4 == null) {
            str4 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader3 = builderAddHeader2.addHeader("hardwareDeviceId", str4);
        java.util.HashMap map = new java.util.HashMap();
        java.lang.String str5 = deviceInfo.get(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION);
        if (str5 == null) {
            str5 = "";
        }
        map.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, str5);
        java.lang.String str6 = deviceInfo.get("deviceId");
        if (str6 == null) {
            str6 = "";
        }
        map.put("deviceId", str6);
        java.lang.String str7 = deviceInfo.get("deviceModel");
        map.put("deviceModel", str7 != null ? str7 : "");
        map.put("deviceType", "android");
        map.put("mobile", str);
        map.put("password", str2);
        if (str3 != null) {
            map.put("certifyId", str3);
        }
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader3.setJsonBody(map).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> execThirdPartyLogin(final java.lang.Object bindReq) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda31
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.execThirdPartyLogin$lambda$12(bindReq, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void execThirdPartyLogin$lambda$12(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.UserThirdPartyLoginReq userThirdPartyLoginReq = (com.geely.galaxy.uc.beans.UserThirdPartyLoginReq) com.geely.core.common.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserThirdPartyLoginReq.class));
        if (userThirdPartyLoginReq != null) {
            userThirdPartyLoginReq.setDeviceId(android.provider.Settings.Secure.getString(com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getContentResolver(), "android_id"));
        }
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1//login/thirdparty/login", false).addQueryParam("verificationCode", userThirdPartyLoginReq != null ? userThirdPartyLoginReq.getVerificationCode() : null).addQueryParam("type", userThirdPartyLoginReq != null ? userThirdPartyLoginReq.getType() : null).addQueryParam("identityToken", userThirdPartyLoginReq != null ? userThirdPartyLoginReq.getIdentityToken() : null).addQueryParam("deviceType", "ANDROID").addQueryParam("deviceId", userThirdPartyLoginReq != null ? userThirdPartyLoginReq.getDeviceId() : null).setJsonBody(userThirdPartyLoginReq != null ? userThirdPartyLoginReq.getSwThirdPartyInfo() : null).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    private final java.util.HashMap<java.lang.String, java.lang.String> getDeviceInfo() {
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        java.lang.String string = java.util.UUID.randomUUID().toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "randomUUID().toString()");
        java.util.HashMap<java.lang.String, java.lang.String> map2 = map;
        map2.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, "");
        map2.put("deviceId", string);
        map2.put("hardwareDeviceId", "");
        map2.put("deviceModel", "");
        map2.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_OS_VERSION, "");
        try {
            java.lang.String versionName = com.geely.sweet.lib.common.dev.AppManager.getInstance().getVersionName();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(versionName, "getInstance().versionName");
            map.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, versionName);
            java.lang.String deviceId = com.alibaba.sdk.android.push.noonesdk.PushServiceFactory.getCloudPushService().getDeviceId();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(deviceId, "getCloudPushService().deviceId");
            map.put("deviceId", deviceId);
            java.lang.String string2 = android.provider.Settings.Secure.getString(com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getContentResolver(), "android_id");
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string2, "getString(HostApplicatio…ttings.Secure.ANDROID_ID)");
            map.put("hardwareDeviceId", string2);
            java.lang.String model = com.geely.sweet.lib.common.dev.DeviceManager.getInstance().getModel();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(model, "getInstance().model");
            map.put("deviceModel", model);
            java.lang.String osVersion = getOsVersion();
            kotlin.jvm.internal.Intrinsics.checkNotNull(osVersion);
            map.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_OS_VERSION, osVersion);
        } catch (java.lang.Throwable th) {
            th.printStackTrace();
        }
        return map;
    }

    private final java.lang.String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendChangePasswordSMS(final java.lang.String certifyId) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda22
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendChangePasswordSMS$lambda$15(this.f$0, certifyId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendChangePasswordSMS$lambda$15(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        boolean z = true;
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequestBuilder = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/modify/password/sendSms", true);
        java.lang.String str2 = str;
        if (str2 != null && str2.length() != 0) {
            z = false;
        }
        if (!z) {
            builderRequestBuilder.addQueryParam("certifyId", str);
        }
        observableEmitter.onNext(httpProxy.execRequest(builderRequestBuilder.addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> changePassword(final java.lang.String newPassword) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newPassword, "newPassword");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda8
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.changePassword$lambda$16(this.f$0, newPassword, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void changePassword$lambda$16(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$newPassword");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/modify/password", true).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").addQueryParam("newPassword", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendForgetPasswordSMS(final java.lang.String phoneNumber, final java.lang.String certifyId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda44
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendForgetPasswordSMS$lambda$18(this.f$0, phoneNumber, certifyId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendForgetPasswordSMS$lambda$18(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/forget/password/sendSms", false).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true");
        java.util.HashMap map = new java.util.HashMap();
        map.put("mobile", str);
        java.lang.String str3 = str2;
        if (!(str3 == null || str3.length() == 0)) {
            map.put("certifyId", str2);
        }
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader.setJsonBody(map).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> forgetPassword(final java.lang.String phoneNumber, final java.lang.String newPassword) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newPassword, "newPassword");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda45
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.forgetPassword$lambda$19(this.f$0, phoneNumber, newPassword, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void forgetPassword$lambda$19(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$newPassword");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/forgot/password", false).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").addQueryParam("mobile", str).addQueryParam("newPassword", str2).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getRemoteUserInfo() {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"UserRepository", "getUserInfo"});
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda3
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getRemoteUserInfo$lambda$20(this.f$0, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getRemoteUserInfo$lambda$20(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/info", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> refreshToken(final java.lang.String refreshToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(refreshToken, "refreshToken");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda40
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.refreshToken$lambda$21(this.f$0, refreshToken, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void refreshToken$lambda$21(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$refreshToken");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/refresh", false).addQueryParam("refreshToken", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateUser(final com.geely.galaxy.uc.beans.User user) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(user, "user");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda41
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.updateUser$lambda$23(this.f$0, user, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updateUser$lambda$23(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, com.geely.galaxy.uc.beans.User user, io.reactivex.ObservableEmitter observableEmitter) {
        java.lang.String city;
        java.lang.String cityCode;
        java.lang.String province;
        java.lang.String provinceCode;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(user, "$user");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder appCode = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setHost(userRepositoryImpl.host).setUrl(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/update").setRequireToken(true).useGateWay(userRepositoryImpl.useGateway).addHeader("appId", com.geely.galaxy.usercenter.BuildConfig.APP_ID).addHeader("tenantId", com.geely.galaxy.usercenter.BuildConfig.TENANT_ID).addHeader(com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true").setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE);
        java.util.HashMap map = new java.util.HashMap();
        map.put("addressId", "");
        java.lang.String avatarUrl = user.getAvatarUrl();
        if (avatarUrl == null) {
            avatarUrl = "";
        }
        map.put("avatarUrl", avatarUrl);
        java.lang.String displayName = user.getDisplayName();
        if (displayName == null) {
            displayName = "";
        }
        map.put("displayName", displayName);
        java.lang.String birthday = user.getBirthday();
        if (birthday == null) {
            birthday = "";
        }
        map.put("birthday", birthday);
        com.geely.galaxy.uc.beans.User.addrssContent addrs = user.getAddrs();
        if (addrs == null || (city = addrs.getCity()) == null) {
            city = "";
        }
        map.put("city", city);
        com.geely.galaxy.uc.beans.User.addrssContent addrs2 = user.getAddrs();
        if (addrs2 == null || (cityCode = addrs2.getCityCode()) == null) {
            cityCode = "";
        }
        map.put("cityCode", cityCode);
        com.geely.galaxy.uc.beans.User.addrssContent addrs3 = user.getAddrs();
        if (addrs3 == null || (province = addrs3.getProvince()) == null) {
            province = "";
        }
        map.put("province", province);
        com.geely.galaxy.uc.beans.User.addrssContent addrs4 = user.getAddrs();
        if (addrs4 == null || (provinceCode = addrs4.getProvinceCode()) == null) {
            provinceCode = "";
        }
        map.put("provinceCode", provinceCode);
        java.lang.String description = user.getDescription();
        if (description == null) {
            description = "";
        }
        map.put("description", description);
        map.put("gender", java.lang.String.valueOf(user.getGender()));
        java.lang.String mobile = user.getMobile();
        map.put("mobile", mobile != null ? mobile : "");
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(appCode.setJsonBody(map).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateUser(final java.util.Map<java.lang.String, java.lang.String> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda29
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.updateUser$lambda$24(this.f$0, map, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updateUser$lambda$24(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.util.Map map, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$map");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setHost(userRepositoryImpl.host).setUrl(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/update").setRequireToken(true).useGateWay(userRepositoryImpl.useGateway).addHeader("appId", com.geely.galaxy.usercenter.BuildConfig.APP_ID).addHeader("tenantId", com.geely.galaxy.usercenter.BuildConfig.TENANT_ID).addHeader(com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true").setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE).setJsonBody(map).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> setUserInforDefault() {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"UserRepository", "getUserInfo"});
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda28
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.setUserInforDefault$lambda$25(this.f$0, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setUserInforDefault$lambda$25(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/resetAccountInfo", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.OssToken>> getOssToken() {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.OssToken>> observableSubscribeOn = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda42
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getOssToken$lambda$26(this.f$0, observableEmitter);
            }
        }).observeOn(io.reactivex.schedulers.Schedulers.io()).subscribeOn(io.reactivex.schedulers.Schedulers.io());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableSubscribeOn, "create(ObservableOnSubsc…scribeOn(Schedulers.io())");
        return observableSubscribeOn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getOssToken$lambda$26(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oss/security/token", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.OssToken.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getLoginDevices() {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda2
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getLoginDevices$lambda$27(this.f$0, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getLoginDevices$lambda$27(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/hw/device/list", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.geely.galaxy.uc.repository.UserRepositoryImpl$getUserSecuritySettings$$inlined$execute$1] */
    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.galaxy.uc.http.BaseResponce<com.geely.galaxy.uc.beans.UserSecurity>> getUserSecuritySettings() {
        com.geely.galaxy.network.HttpExecutor.Companion companion = com.geely.galaxy.network.HttpExecutor.INSTANCE;
        com.geely.rxhttp.base.IBaseRequest<java.lang.String> iBaseRequestBuild = new com.geely.galaxy.network.impl.GetHttpConfigImpl.Builder().setCustomUrl(this.scheme.getValue() + this.host + "/api/v1/user/securitySettings").setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE).useGateWay(true).build();
        com.geely.galaxy.network.proxy.HttpExecutorProxy httpExecutorProxy = com.geely.galaxy.network.proxy.HttpExecutorProxy.INSTANCE;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<com.geely.galaxy.uc.http.BaseResponce<com.geely.galaxy.uc.beans.UserSecurity>>() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$getUserSecuritySettings$$inlined$execute$1
        }.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T?>() {}.type");
        io.reactivex.Observable<com.geely.galaxy.uc.http.BaseResponce<com.geely.galaxy.uc.beans.UserSecurity>> observableCompose = httpExecutorProxy.execute(iBaseRequestBuild, type).compose(com.geely.galaxy.network.util.RxTransform.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "HttpExecutor.execute<Bas…ransform.httpTransform())");
        return observableCompose;
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> deletetLoginDevices(final java.lang.String device_id) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(device_id, "device_id");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda35
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.deletetLoginDevices$lambda$28(this.f$0, device_id, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void deletetLoginDevices$lambda$28(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$device_id");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/hw/device/del", true).addQueryParam("id", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.alibaba.sdk.android.oss.OSSClient> uploadUserAvatar(android.content.Context context, final com.geely.galaxy.uc.beans.OssToken ossToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(ossToken, "ossToken");
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"UserRepository", "endPoint:https://oss-cn-hangzhou.aliyuncs.com"});
        com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider oSSCredentialProvider = new com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda36
            public final com.alibaba.sdk.android.oss.common.auth.OSSFederationToken getFederationToken() {
                return com.geely.galaxy.uc.repository.UserRepositoryImpl.uploadUserAvatar$lambda$29(ossToken);
            }
        };
        com.alibaba.sdk.android.oss.ClientConfiguration clientConfiguration = new com.alibaba.sdk.android.oss.ClientConfiguration();
        clientConfiguration.setConnectionTimeout(15000);
        clientConfiguration.setSocketTimeout(15000);
        clientConfiguration.setMaxConcurrentRequest(10);
        clientConfiguration.setMaxErrorRetry(2);
        final com.alibaba.sdk.android.oss.OSSClient oSSClient = new com.alibaba.sdk.android.oss.OSSClient(context, "https://oss-cn-hangzhou.aliyuncs.com", oSSCredentialProvider, clientConfiguration);
        io.reactivex.Observable<com.alibaba.sdk.android.oss.OSSClient> observableCreate = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda37
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.uploadUserAvatar$lambda$31(oSSClient, observableEmitter);
            }
        });
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCreate, "create {\n            it.…Next(ossClient)\n        }");
        return observableCreate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final com.alibaba.sdk.android.oss.common.auth.OSSFederationToken uploadUserAvatar$lambda$29(com.geely.galaxy.uc.beans.OssToken ossToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(ossToken, "$ossToken");
        return new com.alibaba.sdk.android.oss.common.auth.OSSFederationToken(ossToken.getAccessKeyId(), ossToken.getAccessKeySecret(), ossToken.getSecurityToken(), ossToken.getExpiration());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void uploadUserAvatar$lambda$31(com.alibaba.sdk.android.oss.OSSClient oSSClient, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(oSSClient, "$ossClient");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(oSSClient);
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> clientDetail() {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"UserRepository", "clientDetail url https://" + this.host + "/api/v1/oauth2/authorize/clientDetail"});
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda4
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.clientDetail$lambda$32(this.f$0, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void clientDetail$lambda$32(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oauth2/authorize/clientDetail", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> scopeDetail(final java.lang.String clientId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda32
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.scopeDetail$lambda$33(this.f$0, clientId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void scopeDetail$lambda$33(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$clientId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oauth2/authorize/scopeDetail", true).addQueryParam(com.ecarx.open.http.utils.iovgoto.iovdo, str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> logout(final java.lang.String refreshToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(refreshToken, "refreshToken");
        final java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = getDeviceInfo();
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda21
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.logout$lambda$34(this.f$0, deviceInfo, refreshToken, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void logout$lambda$34(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.util.HashMap map, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$deviceInfo");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$refreshToken");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequestBuilder = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/logout", true);
        java.lang.String str2 = (java.lang.String) map.get(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION);
        if (str2 == null) {
            str2 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = builderRequestBuilder.addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, str2);
        java.lang.String str3 = (java.lang.String) map.get("deviceId");
        if (str3 == null) {
            str3 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader2 = builderAddHeader.addHeader("deviceId", str3);
        java.lang.String str4 = (java.lang.String) map.get("deviceModel");
        if (str4 == null) {
            str4 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader3 = builderAddHeader2.addHeader("deviceModel", str4).addHeader("deviceType", "android");
        java.lang.String str5 = (java.lang.String) map.get("hardwareDeviceId");
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader3.addHeader("hardwareDeviceId", str5 != null ? str5 : "").addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").addQueryParam("refreshToken", str).setContentType(com.geely.rxhttp.ContentType.Default).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> removeAuth(final java.lang.String clientId) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda1
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.removeAuth$lambda$35(this.f$0, clientId, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void removeAuth$lambda$35(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$clientId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oauth2/authorize/cancel", true).addQueryParam(com.ecarx.open.http.utils.iovgoto.iovdo, str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getOAuth2Code(final java.lang.String clientId, final java.lang.String scope, final java.lang.String responseType, final java.lang.String state) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(responseType, "responseType");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda25
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getOAuth2Code$lambda$37(this.f$0, clientId, scope, responseType, state, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getOAuth2Code$lambda$37(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$clientId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str3, "$responseType");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        boolean z = true;
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddQueryParam = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oauth2/code", true).addQueryParam("client_id", str).addQueryParam("scope", str2).addQueryParam("response_type", str3).addQueryParam("isDestruction", "false");
        java.lang.String str5 = str4;
        if (str5 != null && str5.length() != 0) {
            z = false;
        }
        if (!z) {
            builderAddQueryParam.addQueryParam("state", str4);
        }
        observableEmitter.onNext(httpProxy.execRequest(builderAddQueryParam.setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> oneOSAuthorize(final java.lang.String uuid, final java.lang.String action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uuid, com.elynkco.common.utils.CacheUtil.uuid);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        final java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = getDeviceInfo();
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda5
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.oneOSAuthorize$lambda$38(this.f$0, uuid, action, deviceInfo, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void oneOSAuthorize$lambda$38(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, java.util.HashMap map, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$uuid");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$action");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$deviceInfo");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddQueryParam = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oneos/qrlogin/authorize", true).addQueryParam(com.elynkco.common.utils.CacheUtil.uuid, str).addQueryParam("action", str2);
        java.lang.String str3 = (java.lang.String) map.get("deviceId");
        if (str3 == null) {
            str3 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = builderAddQueryParam.addHeader("deviceId", str3);
        java.lang.String str4 = (java.lang.String) map.get("deviceModel");
        if (str4 == null) {
            str4 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader2 = builderAddHeader.addHeader("deviceModel", str4);
        java.lang.String str5 = (java.lang.String) map.get("hardwareDeviceId");
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader2.addHeader("hardwareDeviceId", str5 != null ? str5 : "").setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> thirdAppOAuth2Authorize(final java.lang.String clientId, final java.lang.String scope) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda17
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.thirdAppOAuth2Authorize$lambda$39(this.f$0, clientId, scope, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void thirdAppOAuth2Authorize$lambda$39(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$clientId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/oauth2/authorize", true).addQueryParam(com.ecarx.open.http.utils.iovgoto.iovdo, str).addQueryParam("scope", str2).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.UserProtocol>> getProtocolDetail(final java.lang.String protocolType) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(protocolType, "protocolType");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.UserProtocol>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda24
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getProtocolDetail$lambda$40(this.f$0, protocolType, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getProtocolDetail$lambda$40(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$protocolType");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/agreement/getUserAgreementDetail", false).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).addQueryParam("code", str).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserProtocol.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> OneKeyLogin(final java.lang.String accessToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(accessToken, "accessToken");
        final java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = getDeviceInfo();
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda14
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.OneKeyLogin$lambda$42(this.f$0, deviceInfo, accessToken, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void OneKeyLogin$lambda$42(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.util.HashMap map, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$deviceInfo");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$accessToken");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/login/onekey/mobileLogin", false).addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true");
        java.lang.String currentCertifyId = com.geely.galaxy.uc.util.SecuritySdkManager.INSTANCE.getCurrentCertifyId();
        if (currentCertifyId == null) {
            currentCertifyId = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader2 = builderAddHeader.addHeader("certifyId", currentCertifyId);
        java.lang.String str2 = (java.lang.String) map.get("hardwareDeviceId");
        if (str2 == null) {
            str2 = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader3 = builderAddHeader2.addHeader("hardwareDeviceId", str2);
        java.util.HashMap map2 = new java.util.HashMap();
        java.lang.String str3 = (java.lang.String) map.get(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION);
        if (str3 == null) {
            str3 = "";
        }
        map2.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_APP_VERSION, str3);
        java.lang.String str4 = (java.lang.String) map.get("deviceId");
        if (str4 == null) {
            str4 = "";
        }
        map2.put("deviceId", str4);
        java.lang.String str5 = (java.lang.String) map.get("deviceModel");
        if (str5 == null) {
            str5 = "";
        }
        map2.put("deviceModel", str5);
        map2.put("deviceType", "android");
        map2.put("accessToken", str);
        java.lang.String str6 = (java.lang.String) map.get("hardwareDeviceId");
        if (str6 == null) {
            str6 = "";
        }
        map2.put("hardwareDeviceId", str6);
        java.lang.String str7 = (java.lang.String) map.get(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_OS_VERSION);
        map2.put(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_OS_VERSION, str7 != null ? str7 : "");
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader3.setJsonBody(map2).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> onekeyModifyMobile(final java.lang.Object reqPwd) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda27
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.onekeyModifyMobile$lambda$43(reqPwd, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onekeyModifyMobile$lambda$43(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.UserModifyMobileReq userModifyMobileReq = (com.geely.galaxy.uc.beans.UserModifyMobileReq) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserModifyMobileReq.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/oneKeyModifyMobile", true).setJsonBody(userModifyMobileReq).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> modifyMobile(final java.lang.Object reqPwd) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda26
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.modifyMobile$lambda$44(reqPwd, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void modifyMobile$lambda$44(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.UserNewMobileReq userNewMobileReq = (com.geely.galaxy.uc.beans.UserNewMobileReq) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserNewMobileReq.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/modifyMobile", true).setJsonBody(userNewMobileReq).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendModifyMobile(final java.lang.String challenge) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda43
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendModifyMobile$lambda$46(this.f$0, challenge, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendModifyMobile$lambda$46(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        boolean z = true;
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderRequestBuilder = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/sendModifyMobile", true);
        java.lang.String str2 = str;
        if (str2 != null && str2.length() != 0) {
            z = false;
        }
        if (!z) {
            builderRequestBuilder.addQueryParam("challenge", str);
        }
        observableEmitter.onNext(httpProxy.execRequest(builderRequestBuilder.setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendNewModifyMobile(final java.lang.String phoneNumber, final java.lang.String challenge) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda18
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.sendNewModifyMobile$lambda$48(this.f$0, phoneNumber, challenge, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void sendNewModifyMobile$lambda$48(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        boolean z = true;
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddQueryParam = userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/sendNewModifyMobile", true).addQueryParam("mobile", str);
        java.lang.String str3 = str2;
        if (str3 != null && str3.length() != 0) {
            z = false;
        }
        if (!z) {
            builderAddQueryParam.addQueryParam("challenge", str2);
        }
        observableEmitter.onNext(httpProxy.execRequest(builderAddQueryParam.setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> smsModifyMobileVerify(final java.lang.String phoneNumber, final java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(phoneNumber, "phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda16
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.smsModifyMobileVerify$lambda$49(this.f$0, phoneNumber, authCode, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void smsModifyMobileVerify$lambda$49(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$phoneNumber");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$authCode");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/smsModifyMobileVerify", true).addQueryParam("mobile", str).addQueryParam("code", str2).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> oneKeyModifyMobileVerify(final java.lang.String accessToken) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(accessToken, "accessToken");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda15
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.oneKeyModifyMobileVerify$lambda$50(this.f$0, accessToken, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void oneKeyModifyMobileVerify$lambda$50(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$accessToken");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/verifyMobile", true).addQueryParam("accessToken", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> bindDevice(final java.lang.Object accountDevice) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda0
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.bindDevice$lambda$51(accountDevice, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void bindDevice$lambda$51(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.AccountDevice accountDevice = (com.geely.galaxy.uc.beans.AccountDevice) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.AccountDevice.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/device/bind", true).setJsonBody(accountDevice).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> removeDevice(final java.lang.Object accountDevice) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda38
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.removeDevice$lambda$52(accountDevice, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void removeDevice$lambda$52(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.AccountDevice accountDevice = (com.geely.galaxy.uc.beans.AccountDevice) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.AccountDevice.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/device/remove", true).setJsonBody(accountDevice).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getAddressList(final int pageNo) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda33
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getAddressList$lambda$53(this.f$0, pageNo, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getAddressList$lambda$53(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, int i, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/address/paging", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).addQueryParam("pageNo", java.lang.String.valueOf(i)).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> addAddress(final java.lang.Object address) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda13
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.addAddress$lambda$54(address, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void addAddress$lambda$54(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.DeliveryAddressContent deliveryAddressContent = (com.geely.galaxy.uc.beans.DeliveryAddressContent) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.DeliveryAddressContent.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/address/create", true).setJsonBody(deliveryAddressContent).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateAddress(final java.lang.Object address) {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda23
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.updateAddress$lambda$55(address, this, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updateAddress$lambda$55(java.lang.Object obj, com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.galaxy.uc.beans.DeliveryAddressContent deliveryAddressContent = (com.geely.galaxy.uc.beans.DeliveryAddressContent) com.geely.rxhttp.util.JsonUtil.INSTANCE.convert(obj, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.DeliveryAddressContent.class));
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/address/update", true).setJsonBody(deliveryAddressContent).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.PUT).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> deleteAddressByID(final java.lang.String address_id) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(address_id, "address_id");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda34
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.deleteAddressByID$lambda$56(this.f$0, address_id, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void deleteAddressByID$lambda$56(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$address_id");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/address/delete", true).addQueryParam("addressId", str).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.DELETE).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getCancellationReasons() {
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda11
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.getCancellationReasons$lambda$57(this.f$0, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getCancellationReasons$lambda$57(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        observableEmitter.onNext(userRepositoryImpl.getHttpProxy().execRequest(userRepositoryImpl.requestBuilder(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/cancellation/cause", true).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> CancellationWithReasons(final java.lang.String cancellationCause, final java.lang.String causeDescription, java.lang.String authCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cancellationCause, "cancellationCause");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(causeDescription, "causeDescription");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(authCode, "authCode");
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda19
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.CancellationWithReasons$lambda$59(this.f$0, cancellationCause, causeDescription, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void CancellationWithReasons$lambda$59(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.lang.String str, java.lang.String str2, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$cancellationCause");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "$causeDescription");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "emitter");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder appCode = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setHost(userRepositoryImpl.host).setUrl(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/user/cancellation").setRequireToken(true).useGateWay(userRepositoryImpl.useGateway).addHeader("appId", com.geely.galaxy.usercenter.BuildConfig.APP_ID).addHeader("tenantId", com.geely.galaxy.usercenter.BuildConfig.TENANT_ID).addHeader(com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true").addHeader(com.geely.aliyun.security.intereptor.SecurityInterceptor.HEADER_PARAM_REQUIRE_SECURITY, "true").setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE);
        java.util.HashMap map = new java.util.HashMap();
        map.put("cancellationCause", str);
        map.put("causeDescription", str2);
        map.put("isOpenVerify", true);
        kotlin.Unit unit = kotlin.Unit.INSTANCE;
        observableEmitter.onNext(httpProxy.execRequest(appCode.setJsonBody(map).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> uploadDeregisterAgreement(final java.util.List<java.lang.String> codes) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(codes, com.geely.galaxy.mine.constant.RouterParams.codes);
        final java.util.HashMap<java.lang.String, java.lang.String> deviceInfo = getDeviceInfo();
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableCompose = io.reactivex.Observable.create(new io.reactivex.ObservableOnSubscribe() { // from class: com.geely.galaxy.uc.repository.UserRepositoryImpl$$ExternalSyntheticLambda12
            public final void subscribe(io.reactivex.ObservableEmitter observableEmitter) {
                com.geely.galaxy.uc.repository.UserRepositoryImpl.uploadDeregisterAgreement$lambda$60(this.f$0, deviceInfo, codes, observableEmitter);
            }
        }).compose(com.geely.swevent.common.util.RxUtil.INSTANCE.httpTransform());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(observableCompose, "create(ObservableOnSubsc…e(RxUtil.httpTransform())");
        return observableCompose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void uploadDeregisterAgreement$lambda$60(com.geely.galaxy.uc.repository.UserRepositoryImpl userRepositoryImpl, java.util.HashMap map, java.util.List list, io.reactivex.ObservableEmitter observableEmitter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userRepositoryImpl, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "$deviceInfo");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "$codes");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(observableEmitter, "it");
        com.geely.rxhttp.ISWHttpProxy httpProxy = userRepositoryImpl.getHttpProxy();
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder appCode = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setHost(userRepositoryImpl.host).setUrl(userRepositoryImpl.scheme.getValue() + userRepositoryImpl.host + "/api/v1/agreement/uploadAgreement").setRequireToken(true).useGateWay(userRepositoryImpl.useGateway).addHeader("appId", com.geely.galaxy.usercenter.BuildConfig.APP_ID).addHeader("tenantId", com.geely.galaxy.usercenter.BuildConfig.TENANT_ID).addHeader(com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_IS_REFRESH_TOKEN, "true").setAppCode(com.geely.galaxy.usercenter.BuildConfig.APP_CODE);
        java.lang.String str = (java.lang.String) map.get("deviceId");
        if (str == null) {
            str = "";
        }
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder builderAddHeader = appCode.addHeader("deviceId", str);
        java.lang.String str2 = (java.lang.String) map.get("deviceModel");
        observableEmitter.onNext(httpProxy.execRequest(builderAddHeader.addHeader("deviceModel", str2 != null ? str2 : "").setJsonBody(list).setContentType(com.geely.rxhttp.ContentType.Json).setMethod(com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST).build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class))));
    }

    @Override // com.geely.galaxy.uc.http.GalaxyUserServiceApi
    public java.lang.String getApiBaseUrl() {
        return this.scheme.getValue() + this.host + "/";
    }
}
