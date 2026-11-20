package com.geely.galaxy.uc.http;

/* JADX INFO: compiled from: SWUserSessionComponent.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000ð\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 £\u00012\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0004£\u0001¤\u0001B\u0005¢\u0006\u0002\u0010\u0005J\u0006\u0010*\u001a\u00020+J\u0018\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020/H\u0016J\b\u00100\u001a\u00020+H\u0002J\u0006\u00101\u001a\u00020+J\b\u00102\u001a\u00020+H\u0002J\u0014\u00103\u001a\u0004\u0018\u00010\u00072\b\u00104\u001a\u0004\u0018\u00010\u0007H\u0003J\b\u00105\u001a\u00020+H\u0016J\u0014\u00106\u001a\u0004\u0018\u00010\u00072\b\u00107\u001a\u0004\u0018\u00010\u0007H\u0002J\u0010\u00108\u001a\u00020\u001a2\u0006\u00109\u001a\u00020\u0007H\u0002J\u0014\u0010:\u001a\u0004\u0018\u00010\u00072\b\u0010;\u001a\u0004\u0018\u00010\u0007H\u0003J\u0010\u0010<\u001a\u00020+2\u0006\u0010=\u001a\u00020>H\u0016J\u0016\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00070@2\u0006\u0010A\u001a\u00020BH\u0002J\u0006\u0010C\u001a\u00020\u0007J2\u0010D\u001a\u00020+2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020\u00072\u0006\u0010G\u001a\u00020\u00072\b\u0010H\u001a\u0004\u0018\u00010\u00072\u0006\u0010=\u001a\u00020>H\u0016J<\u0010D\u001a\u00020+2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020\u00072\u0006\u0010G\u001a\u00020\u00072\b\u0010H\u001a\u0004\u0018\u00010\u00072\u0012\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0IJ6\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0L2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020\u00072\u0006\u0010G\u001a\u00020\u00072\b\u0010H\u001a\u0004\u0018\u00010\u0007H\u0016J\n\u0010M\u001a\u0004\u0018\u00010\u0007H\u0002J\u001e\u0010N\u001a\u00020+2\u0014\u0010O\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\u0004\u0012\u00020+0PH\u0017J\n\u0010Q\u001a\u0004\u0018\u00010\u0007H\u0016J\u0014\u0010R\u001a\u0004\u0018\u00010\u00072\b\u0010S\u001a\u0004\u0018\u00010\u0007H\u0002J\n\u0010T\u001a\u0004\u0018\u00010\u0007H\u0016J\b\u0010U\u001a\u0004\u0018\u00010\"J\u0010\u0010V\u001a\u00020+2\u0006\u0010=\u001a\u00020WH\u0016J\u000e\u0010X\u001a\u00020+2\u0006\u0010=\u001a\u00020WJ\n\u0010Y\u001a\u0004\u0018\u00010\u0007H\u0016J\u001c\u0010Z\u001a\u00020+2\b\u0010S\u001a\u0004\u0018\u00010\u00072\b\u0010[\u001a\u0004\u0018\u00010\u0007H\u0016J\u0012\u0010\\\u001a\u00020+2\b\u0010]\u001a\u0004\u0018\u00010^H\u0016J\b\u0010_\u001a\u00020\u000eH\u0016JO\u0010`\u001a\u00020+\"\b\b\u0000\u0010a*\u00020b2\b\u0010c\u001a\u0004\u0018\u00010\u00072\f\u0010d\u001a\b\u0012\u0004\u0012\u0002Ha0e2#\u0010=\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u0001Ha¢\u0006\f\bf\u0012\b\bg\u0012\u0004\b\b(h\u0012\u0004\u0012\u00020+0PH\u0016J&\u0010i\u001a\u00020+2\u0006\u0010]\u001a\u00020^2\u0014\u0010=\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J\u0018\u00010IH\u0016J$\u0010j\u001a\u00020+2\u0014\u0010=\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J\u0018\u00010I2\u0006\u0010k\u001a\u00020\u000eJ\u0010\u0010l\u001a\u00020\u000e2\u0006\u00109\u001a\u00020\u0007H\u0016J\b\u0010m\u001a\u00020+H\u0016J\b\u0010n\u001a\u00020\u000eH\u0016J\u000e\u0010n\u001a\u00020\u000e2\u0006\u0010k\u001a\u00020\u000eJ\b\u0010o\u001a\u00020+H\u0016J\u0012\u0010p\u001a\u00020+2\b\u0010q\u001a\u0004\u0018\u00010\u0007H\u0016J\u001e\u0010r\u001a\u00020+2\u0014\u0010=\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J\u0018\u00010IH\u0016J\u001a\u0010s\u001a\u00020+2\u0012\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0IJ&\u0010t\u001a\u0004\u0018\u00010\u00072\u0006\u0010u\u001a\u00020v2\u0006\u0010w\u001a\u00020\u00072\n\b\u0002\u0010x\u001a\u0004\u0018\u00010\u0007H\u0002J\u0010\u0010y\u001a\u00020+2\u0006\u0010z\u001a\u00020{H\u0016J\b\u0010|\u001a\u00020\u000eH\u0016J\u001a\u0010}\u001a\u00020+2\b\u0010~\u001a\u0004\u0018\u00010b2\u0006\u0010=\u001a\u00020\u007fH\u0016J-\u0010\u0080\u0001\u001a\u00020+2\u0007\u0010\u0081\u0001\u001a\u00020\u00072\u0007\u0010\u0082\u0001\u001a\u00020\u00072\u0012\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0IJ\u000b\u0010\u0083\u0001\u001a\u0004\u0018\u00010)H\u0003J\"\u0010\u0084\u0001\u001a\u0013\u0012\u0004\u0012\u00020\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0018\u00010\u0085\u00012\u0006\u0010A\u001a\u00020BH\u0016J\u0015\u0010\u0086\u0001\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0LH\u0016J\u0011\u0010\u0086\u0001\u001a\u00020+2\u0006\u0010=\u001a\u00020WH\u0016J\u001a\u0010\u0087\u0001\u001a\u00020+2\u0006\u0010-\u001a\u00020\u00072\u0007\u0010.\u001a\u00030\u0088\u0001H\u0016J3\u0010\u0089\u0001\u001a\u0004\u0018\u0001Ha\"\b\b\u0000\u0010a*\u00020b2\t\u0010\u008a\u0001\u001a\u0004\u0018\u00010b2\r\u0010\u008b\u0001\u001a\b\u0012\u0004\u0012\u0002Ha0e¢\u0006\u0003\u0010\u008c\u0001J\u0015\u0010\u008d\u0001\u001a\u0004\u0018\u00010\u00072\b\u0010S\u001a\u0004\u0018\u00010\u0007H\u0003J5\u0010\u008e\u0001\u001a\u0004\u0018\u0001Ha\"\b\b\u0000\u0010a*\u00020b2\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\u00072\r\u0010\u0090\u0001\u001a\b\u0012\u0004\u0012\u0002Ha0eH\u0002¢\u0006\u0003\u0010\u0091\u0001J\t\u0010\u0092\u0001\u001a\u00020+H\u0002J\u0014\u0010\u0093\u0001\u001a\u00020\u000e2\t\u0010\u0094\u0001\u001a\u0004\u0018\u00010)H\u0016J\u0013\u0010\u0095\u0001\u001a\u00020\u000e2\b\u0010h\u001a\u0004\u0018\u00010\u0007H\u0002J5\u0010\u0096\u0001\u001a\u00020+2\u0006\u0010]\u001a\u00020^2\u0010\u0010\u0097\u0001\u001a\u000b\u0012\u0004\u0012\u00020+\u0018\u00010\u0098\u00012\u0010\u0010\u0099\u0001\u001a\u000b\u0012\u0004\u0012\u00020+\u0018\u00010\u0098\u0001H\u0016J\u000f\u0010\u009a\u0001\u001a\u00020+2\u0006\u00109\u001a\u00020\u0007J'\u0010\u009b\u0001\u001a\u00020+2\u0006\u0010]\u001a\u00020^2\t\u0010\u009c\u0001\u001a\u0004\u0018\u00010\u00072\t\u0010=\u001a\u0005\u0018\u00010\u009d\u0001H\u0016J-\u0010\u009e\u0001\u001a\u00020+2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020\u00072\u0012\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070J0IH\u0016J\t\u0010\u009f\u0001\u001a\u00020+H\u0016J\u001d\u0010 \u0001\u001a\u00020+2\b\u0010]\u001a\u0004\u0018\u00010^2\b\u0010=\u001a\u0004\u0018\u00010\u0018H\u0016J\u000f\u0010 \u0001\u001a\u00020+2\u0006\u0010=\u001a\u00020\u0018J\t\u0010¡\u0001\u001a\u00020+H\u0016J\u001d\u0010¢\u0001\u001a\u00020\u000e2\b\u0010S\u001a\u0004\u0018\u00010\u00072\b\u0010[\u001a\u0004\u0018\u00010\u0007H\u0016R\u0014\u0010\u0006\u001a\u00020\u0007X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u0007X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\u0007X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\u001b\u001a\u00020\u001c8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001d\u0010\u001eR\u0010\u0010 \u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u001b\u0010#\u001a\u00020$8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b'\u0010\u0014\u001a\u0004\b%\u0010&R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006¥\u0001"}, d2 = {"Lcom/geely/galaxy/uc/http/SWUserSessionComponent;", "Lcom/geely/sweet/lib/token/ITokenProtocol;", "Lcom/geely/galaxy/uc/http/TokenManager;", "Lcom/geely/galaxy/uc/http/UserLoginObserver;", "Lcom/geely/galaxy/router/service/IGalaxyUserService;", "()V", "LAST_LOGIN_NEW_ACCOUNT", "", "getLAST_LOGIN_NEW_ACCOUNT", "()Ljava/lang/String;", "LAST_LOGIN_PHONE_NUM", "getLAST_LOGIN_PHONE_NUM", "TAG", "bNotifyLogin", "", "defaultCharset", "Ljava/nio/charset/Charset;", "getDefaultCharset", "()Ljava/nio/charset/Charset;", "defaultCharset$delegate", "Lkotlin/Lazy;", "isCrowdedOutDialogShow", "isModifyPasswordNeedBackToMain", "loginCallback", "Lcom/geely/galaxy/router/service/IGalaxyUserService$LoginCallback;", "mPromptDialog", "Lcom/geely/market/energy/common/dialog/impl/RxDialogFragment;", "prefs", "Lcom/geely/market/energy/common/prefs/PrefsManager;", "getPrefs", "()Lcom/geely/market/energy/common/prefs/PrefsManager;", "prefs$delegate", "remoteUserInfo", "userLogin", "Lcom/geely/galaxy/uc/beans/UserLogin;", "userRepository", "Lcom/geely/galaxy/uc/repository/UserRepository;", "getUserRepository", "()Lcom/geely/galaxy/uc/repository/UserRepository;", "userRepository$delegate", "userToken", "Lcom/geely/galaxy/uc/beans/UserToken;", "backToMain", "", "bindDevice", "deviceId", "callBack", "Lcom/geely/galaxy/router/service/IGalaxyUserService$BindDeviceCallBack;", "clearUserInfo", "closeCordovaActivity", "closeCrowedOut", "decrypt", "cipherText", "deleteToken", "encrypt", "text", "ensurePromptDialog", "tokenCode", "formatPath", "path", "getDefaultDeliveryAddress", "callback", "Lcom/geely/galaxy/router/service/IGalaxyUserService$HttpCallBack;", "getGatewayRequest", "Lcom/geely/rxhttp/base/IBaseRequest;", "request", "Lokhttp3/Request;", "getLastLoginPhoneNumer", "getOAuthCode", com.ecarx.open.http.utils.iovgoto.iovdo, "scope", "responseType", "state", "Lio/reactivex/Observer;", "Lcom/geely/rxhttp/base/IHttpResponse;", "getOAuthCodeErupt", "Lio/reactivex/Observable;", "getRefreshToken", "getRemoteUserInfo", "block", "Lkotlin/Function1;", "getToken", "getURLPath", "url", "getUserInfo", "getUserInfo2", "getUserToken", "Lcom/geely/galaxy/router/service/IGalaxyUserService$UserTokenCallback;", "getUserTokenForOtherModule", "getVcsid", "handleResponseCode", "code", "init", "context", "Landroid/content/Context;", "isLogin", "listen", "T", "", "eventCode", "expectClass", "Lkotlin/reflect/KClass;", "Lkotlin/ParameterName;", "name", "data", "logout", "logoutWithLogin", "needLogin", "onCrowdedOut", "onExitLogin", "onLoginExpired", "onLoginFailed", "onLoginSuccess", "loginResult", "onLogout", "onLogoutWithOutNotify", "onPrefsCURD", "type", "Lcom/geely/galaxy/uc/http/SWUserSessionComponent$PrefsManagerType;", com.geely.galaxy.main.router.ITabMainServiceImpl.INTENT_KEY_SELECT_MALL, "value", "onProfileUpdate", "user", "Lcom/geely/galaxy/uc/beans/User;", "onRefreshToken", "onThirdLogin", "bindReq", "Lcom/geely/galaxy/uc/util/ThirdAppUtil$ThirdAppCallBack;", "oneOSAuthorize", com.elynkco.common.utils.CacheUtil.uuid, "action", "parseToken", "provideHeader", "", "refreshToken", "removeDevice", "Lcom/geely/galaxy/router/service/IGalaxyUserService$UnbindDeviceCallBack;", "safetyParse", "target", "clazz", "(Ljava/lang/Object;Lkotlin/reflect/KClass;)Ljava/lang/Object;", "safetyParsePath", "safetyParseResponse", "json", "expectedCls", "(Ljava/lang/String;Lkotlin/reflect/KClass;)Ljava/lang/Object;", "saveLoginPhoneNumer", "saveToken", com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_TOKEN, "saveUserInfo", "saveUserInfoLog", "success", "Lkotlin/Function0;", "failed", "showCrowdedOutDialog", "startAddressManager", "addressId", "Lcom/geely/galaxy/router/service/IGalaxyUserService$CommonCallback;", "thirdAppOAuth2Authorize", "toAccountSecurity", "toLogin", "toProfile", "tryRefreshToken", "Companion", "PrefsManagerType", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class SWUserSessionComponent implements com.geely.sweet.lib.token.ITokenProtocol, com.geely.galaxy.uc.http.TokenManager, com.geely.galaxy.uc.http.UserLoginObserver, com.geely.galaxy.router.service.IGalaxyUserService {
    private static final java.lang.String KEY_USER_DATA = "user_data";
    private static final java.lang.String KEY_USER_ID = "user_id";
    private static final java.lang.String KEY_USER_TOKEN = "user_token";
    private boolean bNotifyLogin;
    private boolean isCrowdedOutDialogShow;
    private boolean isModifyPasswordNeedBackToMain;
    private com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback loginCallback;
    private com.geely.market.energy.common.dialog.impl.RxDialogFragment mPromptDialog;
    private java.lang.String remoteUserInfo;
    private com.geely.galaxy.uc.beans.UserLogin userLogin;
    private com.geely.galaxy.uc.beans.UserToken userToken;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.uc.http.SWUserSessionComponent.Companion INSTANCE = new com.geely.galaxy.uc.http.SWUserSessionComponent.Companion(null);
    private static final com.geely.galaxy.uc.http.SWUserSessionComponent instance = new com.geely.galaxy.uc.http.SWUserSessionComponent();
    private final java.lang.String TAG = "SWUser-UConstants";
    private final java.lang.String LAST_LOGIN_PHONE_NUM = "LAST_LOGIN_PHONE_NUM";
    private final java.lang.String LAST_LOGIN_NEW_ACCOUNT = "LAST_LOGIN_NEW_ACCOUNT";

    /* JADX INFO: renamed from: userRepository$delegate, reason: from kotlin metadata */
    private final kotlin.Lazy userRepository = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.geely.galaxy.uc.repository.UserRepository>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$userRepository$2
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final com.geely.galaxy.uc.repository.UserRepository m1797invoke() {
            return com.geely.galaxy.uc.repository.UserRepositoryImpl.Companion.getInstance(com.geely.galaxy.uc.http.GalaxyUserServiceApi.SCHEME.HTTPS, true);
        }
    });

    /* JADX INFO: renamed from: prefs$delegate, reason: from kotlin metadata */
    private final kotlin.Lazy prefs = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.geely.market.energy.common.prefs.PrefsManager>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$prefs$2
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final com.geely.market.energy.common.prefs.PrefsManager m1796invoke() {
            return new com.geely.market.energy.common.prefs.PrefsManager("swx_galaxt_user_data");
        }
    });

    /* JADX INFO: renamed from: defaultCharset$delegate, reason: from kotlin metadata */
    private final kotlin.Lazy defaultCharset = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<java.nio.charset.Charset>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$defaultCharset$2
        public final java.nio.charset.Charset invoke() {
            return java.nio.charset.Charset.forName("utf-8");
        }
    });

    /* JADX INFO: compiled from: SWUserSessionComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/geely/galaxy/uc/http/SWUserSessionComponent$PrefsManagerType;", "", "(Ljava/lang/String;I)V", "PARSE", "SAVE", "REMOVE", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum PrefsManagerType {
        PARSE,
        SAVE,
        REMOVE
    }

    /* JADX INFO: compiled from: SWUserSessionComponent.kt */
    @kotlin.Metadata(k = 3, mv = {1, 8, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.values().length];
            try {
                iArr[com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError unused) {
            }
            try {
                iArr[com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.SAVE.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError unused2) {
            }
            try {
                iArr[com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.REMOVE.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError unused3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @com.geely.market.energy.common.protect.LDPProtect
    private final native java.lang.String decrypt(java.lang.String cipherText);

    @com.geely.market.energy.common.protect.LDPProtect
    private final native java.lang.String formatPath(java.lang.String path);

    public static final com.geely.galaxy.uc.http.SWUserSessionComponent getInstance() {
        return INSTANCE.getInstance();
    }

    @com.geely.market.energy.common.protect.LDPProtect
    private final native com.geely.galaxy.uc.beans.UserToken parseToken();

    @com.geely.market.energy.common.protect.LDPProtect
    private final native java.lang.String safetyParsePath(java.lang.String url);

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void getOAuthCode(java.lang.String clientId, java.lang.String scope, java.lang.String responseType, java.lang.String state, com.geely.galaxy.router.service.IGalaxyUserService.HttpCallBack callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(responseType, "responseType");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void getUserToken(com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
    }

    public java.lang.String getVcsid() {
        return null;
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void toAccountSecurity() {
    }

    public SWUserSessionComponent() {
        com.geely.sweet.lib.token.SWTokenContract.Companion.getInstance().provideProtocol(this);
        java.lang.String strDecrypt = decrypt(onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE, KEY_USER_DATA, null, 4, null));
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"SWUser-UConstants", "userData:" + strDecrypt});
        this.userToken = parseToken();
        com.geely.galaxy.uc.beans.UserLogin userLogin = (com.geely.galaxy.uc.beans.UserLogin) safetyParse(strDecrypt, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserLogin.class));
        this.userLogin = userLogin;
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"SWUser-UConstants", "userLogin:" + userLogin});
        this.loginCallback = null;
    }

    public final java.lang.String getLAST_LOGIN_PHONE_NUM() {
        return this.LAST_LOGIN_PHONE_NUM;
    }

    public final java.lang.String getLAST_LOGIN_NEW_ACCOUNT() {
        return this.LAST_LOGIN_NEW_ACCOUNT;
    }

    /* JADX INFO: compiled from: SWUserSessionComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\t\u0010\u0002\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/geely/galaxy/uc/http/SWUserSessionComponent$Companion;", "", "()V", "KEY_USER_DATA", "", "KEY_USER_ID", "KEY_USER_TOKEN", "instance", "Lcom/geely/galaxy/uc/http/SWUserSessionComponent;", "getInstance$annotations", "getInstance", "()Lcom/geely/galaxy/uc/http/SWUserSessionComponent;", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @kotlin.jvm.JvmStatic
        public static /* synthetic */ void getInstance$annotations() {
        }

        private Companion() {
        }

        public final com.geely.galaxy.uc.http.SWUserSessionComponent getInstance() {
            return com.geely.galaxy.uc.http.SWUserSessionComponent.instance;
        }
    }

    private final com.geely.galaxy.uc.repository.UserRepository getUserRepository() {
        return (com.geely.galaxy.uc.repository.UserRepository) this.userRepository.getValue();
    }

    private final com.geely.market.energy.common.prefs.PrefsManager getPrefs() {
        return (com.geely.market.energy.common.prefs.PrefsManager) this.prefs.getValue();
    }

    private final java.nio.charset.Charset getDefaultCharset() {
        java.lang.Object value = this.defaultCharset.getValue();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(value, "<get-defaultCharset>(...)");
        return (java.nio.charset.Charset) value;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.NotImplementedError */
    public void init(android.content.Context context) throws kotlin.NotImplementedError {
        throw new kotlin.NotImplementedError("An operation is not implemented: Not yet implemented");
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public java.lang.String getToken() {
        try {
            com.geely.galaxy.uc.beans.UserToken userToken = this.userToken;
            java.lang.String token = userToken != null ? userToken.getToken() : null;
            java.lang.String str = token;
            if (!(str == null || str.length() == 0)) {
                return token;
            }
            com.geely.galaxy.uc.beans.UserToken token2 = parseToken();
            this.userToken = token2;
            if (token2 != null) {
                return token2.getToken();
            }
            return null;
        } catch (java.lang.Exception unused) {
            return null;
        }
    }

    private final java.lang.String getRefreshToken() {
        try {
            com.geely.galaxy.uc.beans.UserToken userToken = this.userToken;
            java.lang.String refreshToken = userToken != null ? userToken.getRefreshToken() : null;
            java.lang.String str = refreshToken;
            if (!(str == null || str.length() == 0)) {
                return refreshToken;
            }
            com.geely.galaxy.uc.beans.UserToken token = parseToken();
            this.userToken = token;
            if (token != null) {
                return token.getRefreshToken();
            }
            return null;
        } catch (java.lang.Exception unused) {
            return null;
        }
    }

    public void handleResponseCode(java.lang.String url, java.lang.String code) {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "handleResponseCode url:" + url + " code:" + code});
    }

    public java.util.Map<java.lang.String, java.lang.String> provideHeader(okhttp3.Request request) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(request, "request");
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "provideHeader request:" + com.alibaba.fastjson.JSON.toJSON(request)});
        com.alibaba.cloudapi.sdk.model.ApiRequest host = com.geely.rxhttp.SWGatewayComponent.Companion.getInstance().getHost(getGatewayRequest(request));
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "provideHeader requestHost:" + com.alibaba.fastjson.JSON.toJSON(host)});
        java.util.Map headers = host != null ? host.getHeaders() : null;
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "provideHeader apiRequest?.headers:$" + com.alibaba.fastjson.JSON.toJSON(headers)});
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        if (headers != null) {
            for (java.util.Map.Entry entry : headers.entrySet()) {
                java.lang.String str = (java.lang.String) entry.getKey();
                java.util.List list = (java.util.List) entry.getValue();
                if (!list.isEmpty()) {
                    linkedHashMap.put(str, list.get(0));
                }
            }
        }
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "provideHeader " + com.alibaba.fastjson.JSON.toJSON(linkedHashMap)});
        return linkedHashMap;
    }

    private final com.geely.rxhttp.base.IBaseRequest<java.lang.String> getGatewayRequest(okhttp3.Request request) {
        com.geely.rxhttp.entity.HttpResponse.HttpMethod httpMethod;
        java.lang.String string;
        if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "GET")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET;
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "PUT")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.PUT;
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "HEAD")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.HEAD;
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "POST")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST;
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "PATCH")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.PATCH;
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(request.method(), "DELETE")) {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.DELETE;
        } else {
            httpMethod = com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET;
        }
        com.geely.rxhttp.ContentType contentType = com.geely.rxhttp.ContentType.Json;
        if (httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.GET || httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.DELETE || httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.HEAD) {
            contentType = com.geely.rxhttp.ContentType.Default;
        }
        if (httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.POST || httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.PUT || httpMethod == com.geely.rxhttp.entity.HttpResponse.HttpMethod.PATCH) {
            contentType = com.geely.rxhttp.ContentType.Json;
        }
        java.lang.String strHeader = request.header("content-type");
        java.lang.String strSubstring = null;
        if (strHeader != null) {
            java.lang.String str = strHeader;
            if (kotlin.text.StringsKt.contains$default(str, "json", false, 2, (java.lang.Object) null)) {
                contentType = com.geely.rxhttp.ContentType.Json;
            } else if (kotlin.text.StringsKt.contains$default(str, "Default", false, 2, (java.lang.Object) null)) {
                contentType = com.geely.rxhttp.ContentType.Default;
            } else if (kotlin.text.StringsKt.contains$default(str, "Form", false, 2, (java.lang.Object) null)) {
                contentType = com.geely.rxhttp.ContentType.Form;
            }
        }
        okio.BufferedSink buffer = new okio.Buffer();
        okhttp3.RequestBody requestBodyBody = request.body();
        if (requestBodyBody != null) {
            requestBodyBody.writeTo(buffer);
            java.nio.charset.Charset charsetForName = java.nio.charset.Charset.forName(com.elynkco.common.utils.FileHelper.DEFAULT_CHARSET);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(\"UTF-8\")");
            okhttp3.MediaType mediaTypeContentType = requestBodyBody.contentType();
            if (mediaTypeContentType != null) {
                charsetForName = mediaTypeContentType.charset(com.alibaba.fastjson.util.IOUtils.UTF8);
                kotlin.jvm.internal.Intrinsics.checkNotNull(charsetForName);
            }
            string = buffer.readString(charsetForName);
        } else {
            string = null;
        }
        java.lang.String path = formatPath(getURLPath(request.url().toString()));
        java.lang.Integer numValueOf = path != null ? java.lang.Integer.valueOf(kotlin.text.StringsKt.indexOf$default(path, "?", -1, false, 4, (java.lang.Object) null)) : null;
        if (numValueOf == null || -1 != numValueOf.intValue()) {
            if (numValueOf != null) {
                int iIntValue = numValueOf.intValue();
                if (path != null) {
                    strSubstring = path.substring(0, iIntValue);
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "this as java.lang.String…ing(startIndex, endIndex)");
                }
            }
            path = strSubstring;
        }
        okhttp3.HttpUrl httpUrlUrl = request.url();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(httpUrlUrl, "request.url()");
        java.util.Set<java.lang.String> setQueryParameterNames = httpUrlUrl.queryParameterNames();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(setQueryParameterNames, "httpUrl.queryParameterNames()");
        java.util.HashMap map = new java.util.HashMap();
        for (java.lang.String str2 : setQueryParameterNames) {
            map.put(str2, httpUrlUrl.queryParameter(str2));
        }
        java.lang.String strHost = request.url().host();
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "getGatewayRequest Content-Type:" + com.alibaba.fastjson.JSON.toJSON(contentType)});
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "getGatewayRequest paramKeys:" + com.alibaba.fastjson.JSON.toJSON(setQueryParameterNames)});
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "getGatewayRequest path:" + ((java.lang.Object) path) + ",body:" + string});
        com.geely.rxhttp.impl.SimpleJsonRequest.Builder method = com.geely.rxhttp.impl.SimpleJsonRequest.Companion.newBuilder().setUrl(httpUrlUrl.toString()).setHost(strHost).setScheme(com.geely.rxhttp.SchemeType.Https).setRequireToken(true).setContentType(contentType).setQueryParams(map).setJson(string).setMethod(httpMethod);
        java.util.List listListOf = kotlin.collections.CollectionsKt.listOf(new java.lang.String[]{com.geely.galaxy.uc.http.GalaxyUserServiceApi.USER_DOMAIN.GALAXY_USERMODULE_HOST_DEV.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.USER_DOMAIN.GALAXY_USERMODULE_HOST_UAT.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.USER_DOMAIN.GALAXY_USERMODULE_HOST_PRE.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.USER_DOMAIN.GALAXY_USERMODULE_HOST_PRO.getValue()});
        java.util.List listListOf2 = kotlin.collections.CollectionsKt.listOf(new java.lang.String[]{com.geely.galaxy.uc.http.GalaxyUserServiceApi.MAIN_DOMAIN.GALAXY_MAIN_HOST_DEV.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.MAIN_DOMAIN.GALAXY_MAIN_HOST_UAT.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.MAIN_DOMAIN.GALAXY_MAIN_HOST_PRE.getValue(), com.geely.galaxy.uc.http.GalaxyUserServiceApi.MAIN_DOMAIN.GALAXY_MAIN_HOST_PRO.getValue()});
        if (listListOf.contains(strHost)) {
            java.lang.String strHeader2 = request.header("x-ca-appcode");
            if (strHeader2 != null) {
                method.setAppCode(strHeader2);
            }
            method.useCache(true);
        } else if (listListOf2.contains(strHost)) {
            method.useCache(true);
        } else {
            java.lang.String strHeader3 = request.header("x-ca-appcode");
            if (strHeader3 != null) {
                method.setAppCode(strHeader3);
            }
            method.useCache(false);
        }
        return method.build(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class));
    }

    private final java.lang.String getURLPath(java.lang.String url) {
        java.lang.String str = url;
        if (str == null || str.length() == 0) {
            return null;
        }
        int iIndexOf$default = kotlin.text.StringsKt.indexOf$default(str, "?", 0, false, 6, (java.lang.Object) null);
        if (iIndexOf$default != -1) {
            url.subSequence(0, iIndexOf$default);
        }
        return safetyParsePath(url);
    }

    public boolean tryRefreshToken(java.lang.String url, java.lang.String code) {
        java.lang.Object[] objArr = new java.lang.Object[2];
        objArr[0] = this.TAG;
        objArr[1] = "check refreshToken url:" + url + " ,code:" + code + ",code==null:" + (code == null);
        com.blankj.utilcode.util.LogUtils.d(objArr);
        java.lang.String str = code;
        if (str == null || kotlin.text.StringsKt.isBlank(str)) {
            return false;
        }
        try {
            if (!kotlin.jvm.internal.Intrinsics.areEqual(code, com.geely.galaxy.uc.http.SWUserSessionComponentKt.CODE_TOKEN_EXPIRED)) {
                org.json.JSONObject jSONObject = new org.json.JSONObject(code);
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "tryRefreshToken result:" + jSONObject});
                code = jSONObject.getString("code");
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(code, "result.getString(\"code\")");
            }
            if (!kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_THE_LOGIN_INFO_HAS_EXPIRED, code) && !kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_LOGIN_INVALID_EXPIRED, code) && !kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.CODE_TOKEN_EXPIRED, code)) {
                if (!kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_REFRESH_INVALID_EXPIRED, code) && !kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_NOT_LOGIN, code)) {
                    if (!kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_CROWDED_OUT, code) && !kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_UPDATE_PWD_KICK_OUT, code) && !kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_BLACKLIST_KICK_OUT, code)) {
                        return false;
                    }
                    return onCrowdedOut(code);
                }
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "mPromptDialog is null " + this.isCrowdedOutDialogShow});
                if (this.isCrowdedOutDialogShow) {
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "强制弹窗未关闭之前，不调登录页面"});
                    return false;
                }
                boolean zOnLoginExpired = onLoginExpired();
                android.app.Activity current = com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().getCurrent();
                if (current != null) {
                    current.runOnUiThread(new java.lang.Runnable() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.geely.galaxy.uc.http.SWUserSessionComponent.tryRefreshToken$lambda$3();
                        }
                    });
                }
                return zOnLoginExpired;
            }
            return onRefreshToken();
        } catch (org.json.JSONException unused) {
            return false;
        } catch (java.lang.Exception e) {
            com.geely.lynkco.kit.log.SWLogReport.t(this.TAG).report(4, "tryRefreshToken--" + e.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void tryRefreshToken$lambda$3() {
        com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGOUT.getEventName(), java.lang.Void.class));
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public boolean onCrowdedOut(final java.lang.String tokenCode) {
        android.app.Activity current;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tokenCode, "tokenCode");
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "onCrowdedOut -code:" + tokenCode});
        boolean z = this.isCrowdedOutDialogShow;
        if (z) {
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "onCrowdedOut -isCrowdedOutDialogShow:" + z});
            return false;
        }
        this.isCrowdedOutDialogShow = true;
        this.bNotifyLogin = false;
        deleteToken();
        clearUserInfo();
        try {
            kotlinx.coroutines.BuildersKt.launch$default(kotlinx.coroutines.GlobalScope.INSTANCE, kotlinx.coroutines.Dispatchers.getMain(), (kotlinx.coroutines.CoroutineStart) null, new com.geely.galaxy.uc.http.SWUserSessionComponent.C05711(null), 2, (java.lang.Object) null);
        } catch (java.lang.Exception unused) {
        }
        if (com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().contains("com.geely.galaxy.main.activity.GalaxyTabMainActivity") && (current = com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().getCurrent()) != null) {
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "onCrowdedOut runOnUiThread--" + current.getClass().getSimpleName()});
            current.runOnUiThread(new java.lang.Runnable() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    com.geely.galaxy.uc.http.SWUserSessionComponent.onCrowdedOut$lambda$5$lambda$4(this.f$0, tokenCode);
                }
            });
        }
        return false;
    }

    /* JADX INFO: renamed from: com.geely.galaxy.uc.http.SWUserSessionComponent$onCrowdedOut$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: SWUserSessionComponent.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 8, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.geely.galaxy.uc.http.SWUserSessionComponent$onCrowdedOut$1", f = "SWUserSessionComponent.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class C05711 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.CoroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        int label;

        C05711(kotlin.coroutines.Continuation<? super com.geely.galaxy.uc.http.SWUserSessionComponent.C05711> continuation) {
            super(2, continuation);
        }

        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new com.geely.galaxy.uc.http.SWUserSessionComponent.C05711(continuation);
        }

        public final java.lang.Object invoke(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return create(coroutineScope, continuation).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            kotlin.ResultKt.throwOnFailure(obj);
            com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build$default(com.geely.core.common.notification.base.INotificationEvent.Companion, com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGOUT.getEventName(), (java.lang.Object) null, 2, (java.lang.Object) null));
            return kotlin.Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void onCrowdedOut$lambda$5$lambda$4(com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent, java.lang.String str) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sWUserSessionComponent, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$tokenCode");
        sWUserSessionComponent.showCrowdedOutDialog(str);
    }

    public final void showCrowdedOutDialog(java.lang.String tokenCode) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tokenCode, "tokenCode");
        if (this.mPromptDialog == null) {
            this.mPromptDialog = ensurePromptDialog(tokenCode);
            com.geely.market.energy.common.dialog.DialogProxy.Companion.getInstance().show(this.mPromptDialog);
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "showCrowdedOutDialog"});
        }
    }

    private final com.geely.market.energy.common.dialog.impl.RxDialogFragment ensurePromptDialog(final java.lang.String tokenCode) {
        com.geely.market.energy.common.dialog.impl.RxDialogFragment.Builder builderNewBuilder = com.geely.market.energy.common.dialog.impl.RxDialogFragment.Builder.Companion.newBuilder();
        android.view.View viewInflate = android.view.LayoutInflater.from(com.geely.sweet.lib.common.util.ComponentProvider.getInstance().provideContext()).inflate(com.geely.galaxy.usercenter.R.layout.dialog_common_alter, (android.view.ViewGroup) null, false);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(viewInflate, "inflater.inflate(R.layou…ommon_alter, null, false)");
        builderNewBuilder.setContentView(viewInflate);
        builderNewBuilder.setCancelable(false);
        android.view.View viewFindViewById = viewInflate.findViewById(com.geely.galaxy.usercenter.R.id.btn_confirm);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(viewFindViewById, "contentView.findViewById(R.id.btn_confirm)");
        android.widget.TextView textView = (android.widget.TextView) viewFindViewById;
        android.view.View viewFindViewById2 = viewInflate.findViewById(com.geely.galaxy.usercenter.R.id.btn_cancel);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(viewFindViewById2, "contentView.findViewById(R.id.btn_cancel)");
        android.widget.TextView textView2 = (android.widget.TextView) viewFindViewById2;
        android.view.View viewFindViewById3 = viewInflate.findViewById(com.geely.galaxy.usercenter.R.id.tvContent);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(viewFindViewById3, "contentView.findViewById(R.id.tvContent)");
        android.widget.TextView textView3 = (android.widget.TextView) viewFindViewById3;
        android.view.View viewFindViewById4 = viewInflate.findViewById(com.geely.galaxy.usercenter.R.id.tvTitle);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(viewFindViewById4, "contentView.findViewById(R.id.tvTitle)");
        android.widget.TextView textView4 = (android.widget.TextView) viewFindViewById4;
        textView.setText(com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.login_device_login_again));
        textView2.setText(com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.cancel));
        java.lang.String string = com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.login_device_warn_content);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "getGlobalApplication().g…ogin_device_warn_content)");
        this.isModifyPasswordNeedBackToMain = false;
        if (kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_UPDATE_PWD_KICK_OUT, tokenCode)) {
            string = com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.titleChangePasswordLogin);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "getGlobalApplication().g…titleChangePasswordLogin)");
            this.isModifyPasswordNeedBackToMain = true;
        }
        if (kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_BLACKLIST_KICK_OUT, tokenCode)) {
            string = com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.titleChangeBlackList);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "getGlobalApplication()\n …ing.titleChangeBlackList)");
            textView2.setVisibility(8);
            textView.setText("知道了");
        }
        textView3.setText(string);
        textView4.setText(com.geely.sweet.core.base.HostApplicationLike.getGlobalApplication().getString(com.geely.galaxy.usercenter.R.string.titleChangePasswordwarn));
        textView.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view) {
                com.geely.galaxy.uc.http.SWUserSessionComponent.ensurePromptDialog$lambda$6(this.f$0, tokenCode, view);
            }
        });
        textView2.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view) {
                com.geely.galaxy.uc.http.SWUserSessionComponent.ensurePromptDialog$lambda$7(this.f$0, tokenCode, view);
            }
        });
        return builderNewBuilder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void ensurePromptDialog$lambda$6(com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent, java.lang.String str, android.view.View view) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sWUserSessionComponent, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$tokenCode");
        sWUserSessionComponent.closeCrowedOut();
        if (kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_BLACKLIST_KICK_OUT, str)) {
            com.sensorsdata.analytics.android.autotrack.aop.SensorsDataAutoTrackHelper.trackViewOnClick(view);
        } else {
            com.geely.galaxy.uc.config.LoginServiceImpl.getInstance().OnekeyLogin();
            com.sensorsdata.analytics.android.autotrack.aop.SensorsDataAutoTrackHelper.trackViewOnClick(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void ensurePromptDialog$lambda$7(com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent, java.lang.String str, android.view.View view) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sWUserSessionComponent, "this$0");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "$tokenCode");
        com.geely.market.energy.common.util.SharePreferenceUtils.getInstace().save(sWUserSessionComponent.LAST_LOGIN_NEW_ACCOUNT, "");
        sWUserSessionComponent.closeCrowedOut();
        if (kotlin.jvm.internal.Intrinsics.areEqual(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_SERVICE_UPDATE_PWD_KICK_OUT, str)) {
            sWUserSessionComponent.backToMain();
        } else {
            sWUserSessionComponent.closeCordovaActivity();
        }
        com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGIN_CANCEL.getEventName(), java.lang.Void.class));
        com.sensorsdata.analytics.android.autotrack.aop.SensorsDataAutoTrackHelper.trackViewOnClick(view);
    }

    private final boolean saveUserInfo(java.lang.String data) {
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"save user-info with " + data});
        if (onPrefsCURD(com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.SAVE, KEY_USER_DATA, encrypt(data)) == null) {
            return false;
        }
        com.geely.galaxy.uc.base.TrackUtil.INSTANCE.updateUserInfo();
        return true;
    }

    private final void saveLoginPhoneNumer() {
        java.lang.String string = com.geely.market.energy.common.util.SharePreferenceUtils.getInstace().getString(this.LAST_LOGIN_NEW_ACCOUNT, "");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "oldAccount");
        if (string.length() > 0) {
            com.geely.galaxy.uc.beans.UserLogin userLogin = this.userLogin;
            if (!kotlin.jvm.internal.Intrinsics.areEqual(userLogin != null ? userLogin.getMobile() : null, string)) {
                com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"账号强制登出后再登录，前后两次账号不一致时，需要返回到首页"});
                backToMain();
            }
        }
        com.geely.market.energy.common.util.SharePreferenceUtils instace = com.geely.market.energy.common.util.SharePreferenceUtils.getInstace();
        java.lang.String str = this.LAST_LOGIN_PHONE_NUM;
        com.geely.galaxy.uc.beans.UserLogin userLogin2 = this.userLogin;
        instace.save(str, userLogin2 != null ? userLogin2.getMobile() : null);
        com.geely.market.energy.common.util.SharePreferenceUtils instace2 = com.geely.market.energy.common.util.SharePreferenceUtils.getInstace();
        java.lang.String str2 = this.LAST_LOGIN_NEW_ACCOUNT;
        com.geely.galaxy.uc.beans.UserLogin userLogin3 = this.userLogin;
        instace2.save(str2, userLogin3 != null ? userLogin3.getMobile() : null);
    }

    public final java.lang.String getLastLoginPhoneNumer() {
        java.lang.String string = com.geely.market.energy.common.util.SharePreferenceUtils.getInstace().getString(this.LAST_LOGIN_PHONE_NUM, "");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "getInstace().getString(LAST_LOGIN_PHONE_NUM, \"\")");
        return string;
    }

    @Override // com.geely.galaxy.uc.http.TokenManager
    public boolean saveToken(com.geely.galaxy.uc.beans.UserToken token) {
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"save token:" + token});
        if (onPrefsCURD(com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.SAVE, KEY_USER_TOKEN, encrypt(com.alibaba.fastjson.JSON.toJSONString(token))) == null) {
            return false;
        }
        this.userToken = token;
        return true;
    }

    @Override // com.geely.galaxy.uc.http.TokenManager
    public void deleteToken() {
        try {
            com.geely.galaxy.uc.util.LogReportUtil.INSTANCE.report("deleteToken->" + new com.google.gson.Gson().toJson(java.lang.Thread.currentThread().getStackTrace()));
        } catch (java.lang.Exception unused) {
        }
        onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.REMOVE, KEY_USER_TOKEN, null, 4, null);
        this.userToken = null;
        com.geely.galaxy.uc.base.TrackUtil.INSTANCE.updateUserInfo();
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"deleteToken"});
    }

    @Override // com.geely.galaxy.uc.http.TokenManager
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> refreshToken() {
        com.geely.galaxy.uc.repository.UserRepository userRepository = getUserRepository();
        java.lang.String refreshToken = getRefreshToken();
        if (refreshToken == null) {
            refreshToken = "";
        }
        return userRepository.refreshToken(refreshToken);
    }

    private final java.lang.String encrypt(java.lang.String text) {
        if (text == null) {
            return null;
        }
        try {
            byte[] bytes = text.getBytes(getDefaultCharset());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
            return android.util.Base64.encodeToString(bytes, 0);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void clearUserInfo() {
        try {
            com.geely.galaxy.uc.util.LogReportUtil.INSTANCE.report("clearUserInfo->" + new com.google.gson.Gson().toJson(java.lang.Thread.currentThread().getStackTrace()));
        } catch (java.lang.Exception unused) {
        }
        this.remoteUserInfo = null;
        this.userLogin = null;
        onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.REMOVE, KEY_USER_DATA, null, 4, null);
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onLoginSuccess(java.lang.String loginResult) {
        com.geely.galaxy.uc.beans.LoginResult loginResult2 = (com.geely.galaxy.uc.beans.LoginResult) safetyParse(loginResult, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.LoginResult.class));
        com.geely.galaxy.uc.beans.UserToken centerTokenDto = loginResult2 != null ? loginResult2.getCenterTokenDto() : null;
        this.userLogin = loginResult2 != null ? loginResult2.getCenterUserInfoDto() : null;
        com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType prefsManagerType = com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.SAVE;
        com.geely.galaxy.uc.beans.UserLogin userLogin = this.userLogin;
        onPrefsCURD(prefsManagerType, KEY_USER_ID, userLogin != null ? userLogin.getId() : null);
        saveToken(centerTokenDto);
        saveUserInfo(new org.json.JSONObject(loginResult).getString("centerUserInfoDto"));
        saveLoginPhoneNumer();
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"loginCallback---onLoginSuccess"});
        com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback loginCallback = this.loginCallback;
        if (loginCallback != null) {
            loginCallback.onLoginSuccess();
        }
        if (!this.bNotifyLogin) {
            com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"onLoginSuccessAndNofity"});
            com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGIN.getEventName(), ""));
        }
        if (this.isModifyPasswordNeedBackToMain) {
            backToMain();
        }
        this.isModifyPasswordNeedBackToMain = false;
        closeCrowedOut();
        this.bNotifyLogin = true;
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"bNotifyLogin:true"});
        this.loginCallback = null;
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onLoginFailed() {
        com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback loginCallback = this.loginCallback;
        if (loginCallback != null) {
            loginCallback.onLoginFail();
        }
        this.loginCallback = null;
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onLogout(io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        logoutWithLogin(callback, callback == null);
        com.geely.market.energy.common.util.SharePreferenceUtils.getInstace().save(this.LAST_LOGIN_NEW_ACCOUNT, "");
    }

    public final void logoutWithLogin(final io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback, final boolean needLogin) {
        com.geely.galaxy.uc.repository.UserRepository userRepository = getUserRepository();
        java.lang.String refreshToken = getRefreshToken();
        if (refreshToken == null) {
            refreshToken = "e130fdeac54746f48259164931c19bcd";
        }
        userRepository.logout(refreshToken).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.logoutWithLogin.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("SWUserSessionComponent onLogout");
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> result) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(result, "result");
                com.geely.galaxy.uc.http.SWUserSessionComponent.this.onLoginExpired(needLogin);
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "onLoginOutAndNofity"});
                com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGOUT.getEventName(), java.lang.Void.class));
                io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observer = callback;
                if (observer != null) {
                    observer.onNext(result);
                }
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{getClass().getSimpleName(), "onFail: " + error});
                com.geely.galaxy.uc.http.SWUserSessionComponent.this.onLoginExpired(needLogin);
                com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGOUT.getEventName(), java.lang.Void.class));
                io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observer = callback;
                if (observer != null) {
                    observer.onComplete();
                }
            }
        });
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onThirdLogin(java.lang.Object bindReq, final com.geely.galaxy.uc.util.ThirdAppUtil.ThirdAppCallBack callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserRepository().execThirdPartyLogin(bindReq).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.onThirdLogin.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("SWUserSessionComponent onThirdLogin");
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> response) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(response, "response");
                com.geely.galaxy.uc.http.SWUserSessionComponent.this.onLoginSuccess((java.lang.String) response.getData());
                callback.resultOk();
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                com.geely.galaxy.uc.http.SWUserSessionComponent.this.onLoginFailed();
                callback.resultFaild(error);
            }
        });
    }

    public final void onLogoutWithOutNotify(final io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        com.geely.galaxy.uc.repository.UserRepository userRepository = getUserRepository();
        java.lang.String refreshToken = getRefreshToken();
        if (refreshToken == null) {
            refreshToken = "";
        }
        userRepository.logout(refreshToken).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.onLogoutWithOutNotify.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("SWUserSessionComponent onLogout");
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> result) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(result, "result");
                callback.onNext(result);
                this.deleteToken();
                this.clearUserInfo();
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserLogOutObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{getClass().getSimpleName(), "onFail: " + error});
                callback.onComplete();
                this.onLoginExpired();
                com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGOUT.getEventName(), java.lang.Void.class));
            }
        });
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onProfileUpdate(com.geely.galaxy.uc.beans.User user) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(user, "user");
        this.remoteUserInfo = null;
        com.geely.galaxy.uc.beans.UserLogin userLogin = this.userLogin;
        if (userLogin != null) {
            userLogin.setAvatarUrl(user.getAvatarUrl());
        }
        com.geely.galaxy.uc.beans.UserLogin userLogin2 = this.userLogin;
        if (userLogin2 != null) {
            userLogin2.setDisplayName(user.getDisplayName());
        }
        com.geely.galaxy.uc.beans.UserLogin userLogin3 = this.userLogin;
        if (userLogin3 != null) {
            userLogin3.setDescription(user.getDescription());
        }
        com.geely.galaxy.uc.beans.UserLogin userLogin4 = this.userLogin;
        if (userLogin4 != null) {
            userLogin4.setGender(java.lang.String.valueOf(user.getGender()));
        }
        if (saveUserInfo(com.geely.market.energy.common.util.JsonUtilKt.toJson(this.userLogin))) {
            com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.USER_INFO_UPDATE.getEventName(), (java.lang.Object) null));
        }
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public void onExitLogin() {
        com.geely.market.energy.common.util.SharePreferenceUtils.getInstace().save(this.LAST_LOGIN_NEW_ACCOUNT, "");
        com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback loginCallback = this.loginCallback;
        if (loginCallback == null) {
            if (this.isModifyPasswordNeedBackToMain) {
                backToMain();
            } else {
                closeCordovaActivity();
            }
        } else if (loginCallback != null) {
            loginCallback.onBtnCloseClick();
        }
        this.isModifyPasswordNeedBackToMain = false;
        this.loginCallback = null;
        com.geely.core.common.notification.RxEventNotification.Companion.getInstance().fireEvent(com.geely.core.common.notification.base.INotificationEvent.Companion.build(com.geely.galaxy.router.service.IGalaxyUserService.UserEvent.LOGIN_CANCEL.getEventName(), java.lang.Void.class));
    }

    public final void closeCordovaActivity() {
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "onExitLogin--关闭H5页面"});
        com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().popAll(new com.geely.core.common.stack.IStackPopFilter() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.closeCordovaActivity.1
            public boolean shouldPop(android.app.Activity activity) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(activity, "activity");
                java.lang.String name = activity.getClass().getName();
                boolean zAreEqual = kotlin.jvm.internal.Intrinsics.areEqual(name, "com.sweet.common.pack.SWCordovaActivity");
                com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "清栈---" + name + " ret:" + zAreEqual});
                return zAreEqual;
            }
        });
    }

    public final void backToMain() {
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "backToMain--返回到首页"});
        com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().popAll(new com.geely.core.common.stack.IStackPopFilter() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.backToMain.1
            public boolean shouldPop(android.app.Activity activity) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(activity, "activity");
                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "清栈--" + activity.getClass().getName()});
                return (kotlin.jvm.internal.Intrinsics.areEqual(activity.getClass().getName(), "com.geely.galaxy.main.activity.GalaxyTabMainActivity") || kotlin.jvm.internal.Intrinsics.areEqual(activity.getClass().getName(), "com.geely.galaxy.uc.user.activity.test.UserActivity")) ? false : true;
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:16:0x007b  */
    /* JADX WARN: Code duplicated, block: B:19:0x0092 A[Catch: Exception -> 0x00a1, TRY_LEAVE, TryCatch #2 {Exception -> 0x00a1, blocks: (B:17:0x007e, B:19:0x0092), top: B:29:0x007e }] */
    private final void closeCrowedOut() {
        kotlin.Unit unit;
        android.app.Activity activity;
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "closeCrowedOut"});
        this.isCrowdedOutDialogShow = false;
        com.geely.market.energy.common.dialog.impl.RxDialogFragment rxDialogFragment = this.mPromptDialog;
        if (rxDialogFragment != null) {
            try {
                rxDialogFragment.dismissAllowingStateLoss();
            } catch (java.lang.Exception e) {
                com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "closeCrowedOut--Exception:" + e.getMessage()});
            }
            try {
                android.app.Activity activity2 = rxDialogFragment.getActivity();
                if (activity2 != null) {
                    com.geely.core.common.stack.IActivityStack companion = com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance();
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(activity2, "it");
                    companion.pop(activity2, true);
                    unit = kotlin.Unit.INSTANCE;
                } else {
                    unit = null;
                }
            } catch (java.lang.Exception e2) {
                com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "closeCrowedOut--pop:" + e2.getMessage()});
                unit = kotlin.Unit.INSTANCE;
            }
            if (unit == null) {
                try {
                    activity = (com.geely.market.energy.common.dialog.impl.RxDialogActivity) com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().getTarget(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.market.energy.common.dialog.impl.RxDialogActivity.class));
                    if (activity != null) {
                        com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().pop(activity, true);
                        kotlin.Unit unit2 = kotlin.Unit.INSTANCE;
                    }
                } catch (java.lang.Exception e3) {
                    com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{this.TAG, "closeCrowedOut--getTarget:" + e3.getMessage()});
                    kotlin.Unit unit3 = kotlin.Unit.INSTANCE;
                }
            }
        } else {
            activity = (com.geely.market.energy.common.dialog.impl.RxDialogActivity) com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().getTarget(kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.market.energy.common.dialog.impl.RxDialogActivity.class));
            if (activity != null) {
                com.geely.core.common.stack.ActivityStackProxy.Companion.getInstance().pop(activity, true);
                kotlin.Unit unit4 = kotlin.Unit.INSTANCE;
            }
        }
        this.mPromptDialog = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final io.reactivex.ObservableSource onRefreshToken$lambda$16(kotlin.jvm.functions.Function1 function1, java.lang.Object obj) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "$tmp0");
        return (io.reactivex.ObservableSource) function1.invoke(obj);
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public boolean onRefreshToken() {
        try {
            io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> observableRefreshToken = refreshToken();
            final kotlin.jvm.functions.Function1<com.geely.rxhttp.base.IHttpResponse<java.lang.String>, io.reactivex.ObservableSource<? extends java.lang.Boolean>> function1 = new kotlin.jvm.functions.Function1<com.geely.rxhttp.base.IHttpResponse<java.lang.String>, io.reactivex.ObservableSource<? extends java.lang.Boolean>>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.onRefreshToken.1
                {
                    super(1);
                }

                public final io.reactivex.ObservableSource<? extends java.lang.Boolean> invoke(com.geely.rxhttp.base.IHttpResponse<java.lang.String> iHttpResponse) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iHttpResponse, "it");
                    if (iHttpResponse.isSuccessful()) {
                        com.geely.galaxy.uc.beans.LoginResult loginResult = (com.geely.galaxy.uc.beans.LoginResult) com.geely.galaxy.uc.http.SWUserSessionComponent.this.safetyParse(iHttpResponse.getData(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.LoginResult.class));
                        com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken = loginResult != null ? loginResult.getCenterTokenDto() : null;
                        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "after refresh userToken:" + com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken});
                        if (com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken != null) {
                            com.geely.galaxy.uc.beans.UserToken userToken = com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken;
                            if (!((userToken == null || userToken.isLegal()) ? false : true)) {
                                com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent = com.geely.galaxy.uc.http.SWUserSessionComponent.this;
                                if (sWUserSessionComponent.saveToken(sWUserSessionComponent.userToken)) {
                                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "onRefreshToken true"});
                                    return io.reactivex.Observable.just(true);
                                }
                            }
                        }
                        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "onRefreshToken fail"});
                        return io.reactivex.Observable.just(false);
                    }
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "onRefreshToken fail"});
                    java.lang.String body = iHttpResponse.getBody();
                    if (body != null) {
                        com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent2 = com.geely.galaxy.uc.http.SWUserSessionComponent.this;
                        if (kotlin.text.StringsKt.contains$default(body, com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_CROWDED_OUT, false, 2, (java.lang.Object) null)) {
                            sWUserSessionComponent2.onCrowdedOut(com.geely.galaxy.uc.http.SWUserSessionComponentKt.USER_CROWDED_OUT);
                        }
                    }
                    return io.reactivex.Observable.just(false);
                }
            };
            java.lang.Object objBlockingFirst = observableRefreshToken.flatMap(new io.reactivex.functions.Function() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda6
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.geely.galaxy.uc.http.SWUserSessionComponent.onRefreshToken$lambda$16(function1, obj);
                }
            }).blockingFirst();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objBlockingFirst, "override fun onRefreshTo…    false\n        }\n    }");
            return ((java.lang.Boolean) objBlockingFirst).booleanValue();
        } catch (java.lang.Exception e) {
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "Exception fail:" + e.getMessage()});
            com.geely.lynkco.kit.log.SWLogReport.t(this.TAG).report(4, "onRefreshToken--" + e.getMessage());
            return false;
        }
    }

    public final void getUserTokenForOtherModule(final com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "getUserTokenForOtherModule"});
        try {
            refreshToken().subscribe(new com.geely.galaxy.uc.http.rxObserver.UserObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.getUserTokenForOtherModule.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super("refreshToken");
                }

                @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
                public void onFail(java.lang.String error) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "getUserTokenForOtherModule onFail"});
                    callback.onGetTokenFail();
                }

                @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
                public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> response) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(response, "response");
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "getUserTokenForOtherModule onSuccess"});
                    com.geely.galaxy.uc.beans.LoginResult loginResult = (com.geely.galaxy.uc.beans.LoginResult) com.geely.galaxy.uc.http.SWUserSessionComponent.this.safetyParse(response.getData(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.LoginResult.class));
                    com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken = loginResult != null ? loginResult.getCenterTokenDto() : null;
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "after other module refresh userToken:" + com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken});
                    if (com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken != null) {
                        com.geely.galaxy.uc.beans.UserToken userToken = com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken;
                        if (!((userToken == null || userToken.isLegal()) ? false : true)) {
                            com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent = com.geely.galaxy.uc.http.SWUserSessionComponent.this;
                            if (sWUserSessionComponent.saveToken(sWUserSessionComponent.userToken)) {
                                com.geely.galaxy.uc.beans.UserToken userToken2 = com.geely.galaxy.uc.http.SWUserSessionComponent.this.userToken;
                                java.lang.String token = userToken2 != null ? userToken2.getToken() : null;
                                com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "getUserTokenForOtherModule " + token});
                                if (token != null) {
                                    callback.onGetTokenSuccess(token);
                                    return;
                                }
                                return;
                            }
                        }
                    }
                    com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{com.geely.galaxy.uc.http.SWUserSessionComponent.this.TAG, "other module onRefreshToken fail"});
                    callback.onGetTokenFail();
                }
            });
        } catch (java.lang.Exception e) {
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "getUserTokenForOtherModule onFail"});
            callback.onGetTokenFail();
            com.geely.lynkco.kit.log.SWLogReport.t(this.TAG).report(4, "getUserTokenForOtherModule--" + e.getMessage());
        }
    }

    @Override // com.geely.galaxy.uc.http.UserLoginObserver
    public boolean onLoginExpired() {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "onLoginExpired token"});
        onLoginExpired(true);
        return false;
    }

    public final boolean onLoginExpired(boolean needLogin) {
        com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{this.TAG, "onLoginExpired token"});
        this.bNotifyLogin = false;
        deleteToken();
        clearUserInfo();
        if (needLogin) {
            closeCrowedOut();
            com.geely.galaxy.uc.config.LoginServiceImpl.getInstance().OnekeyLogin();
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> T safetyParse(java.lang.Object target, kotlin.reflect.KClass<T> clazz) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clazz, "clazz");
        if (target == 0) {
            return null;
        }
        try {
            if (kotlin.jvm.internal.Intrinsics.areEqual(clazz, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class)) && (target instanceof java.lang.String)) {
                return target;
            }
            if (target instanceof java.lang.String) {
                return (T) new com.google.gson.Gson().fromJson((java.lang.String) target, kotlin.jvm.JvmClassMappingKt.getJavaClass(clazz));
            }
            return (T) new com.google.gson.Gson().fromJson(com.alibaba.fastjson.JSON.toJSONString(target), kotlin.jvm.JvmClassMappingKt.getJavaClass(clazz));
        } catch (java.lang.Exception e) {
            com.blankj.utilcode.util.LogUtils.w(new java.lang.Object[]{"safetyParse", e.getMessage()});
            return null;
        }
    }

    private final <T> T safetyParseResponse(java.lang.String json, kotlin.reflect.KClass<T> expectedCls) {
        java.lang.String str = json;
        if (str == null || str.length() == 0) {
            return null;
        }
        com.geely.market.energy.common.http.base.IHttpResponse iHttpResponse = com.geely.market.energy.common.util.HttpParseUtil.INSTANCE.parse2Response(json, expectedCls);
        if (iHttpResponse.isSuccessful()) {
            return (T) iHttpResponse.getData();
        }
        return null;
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void toLogin(android.content.Context context, com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback callback) {
        try {
            com.geely.galaxy.uc.util.LogReportUtil.INSTANCE.report("toLogin->" + new com.google.gson.Gson().toJson(java.lang.Thread.currentThread().getStackTrace()));
        } catch (java.lang.Exception unused) {
        }
        closeCrowedOut();
        com.geely.galaxy.uc.config.LoginServiceImpl.getInstance().OnekeyLogin();
        this.loginCallback = callback;
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void toProfile() {
        com.geely.galaxy.uc.ext.ActivityExtKt.route$default("/GeelyGalaxyUserCenter//profile", null, 2, null);
    }

    public final void toLogin(com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        this.loginCallback = callback;
        java.lang.String token = getToken();
        if (token == null || token.length() == 0) {
            onLoginExpired();
        } else {
            getUserTokenForOtherModule(new com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.toLogin.1
                @Override // com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback
                public void onGetTokenFail() {
                    com.geely.galaxy.uc.http.SWUserSessionComponent.this.onLoginExpired();
                }

                @Override // com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback
                public void onGetTokenSuccess(java.lang.String token2) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(token2, com.geely.galaxy.uc.http.AutoRefreshInteceptor.HEADER_PARAM_TOKEN);
                    com.geely.galaxy.router.service.IGalaxyUserService.LoginCallback loginCallback = com.geely.galaxy.uc.http.SWUserSessionComponent.this.loginCallback;
                    kotlin.jvm.internal.Intrinsics.checkNotNull(loginCallback);
                    loginCallback.onLoginSuccess();
                    com.geely.galaxy.uc.http.SWUserSessionComponent.this.loginCallback = null;
                }
            });
        }
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void logout(android.content.Context context, io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        onLogout(callback);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void refreshToken(com.geely.galaxy.router.service.IGalaxyUserService.UserTokenCallback callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserTokenForOtherModule(callback);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void bindDevice(java.lang.String deviceId, final com.geely.galaxy.router.service.IGalaxyUserService.BindDeviceCallBack callBack) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callBack, "callBack");
        java.lang.String strValueOf = java.lang.String.valueOf(onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE, KEY_USER_ID, null, 4, null));
        if (strValueOf.length() == 0) {
            callBack.onBindDeviceFail("userId is null");
            return;
        }
        com.geely.galaxy.uc.beans.AccountDevice accountDevice = new com.geely.galaxy.uc.beans.AccountDevice(null, null, null, null, 15, null);
        accountDevice.setAccountId(strValueOf);
        accountDevice.setDevice(deviceId);
        accountDevice.setDeviceType("Android");
        com.geely.galaxy.uc.beans.UserLogin userLogin = this.userLogin;
        accountDevice.setMobile(java.lang.String.valueOf(userLogin != null ? userLogin.getMobile() : null));
        getUserRepository().bindDevice(accountDevice).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.bindDevice.1
            {
                super("bindDevice");
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> response) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(response, "response");
                com.geely.galaxy.router.service.IGalaxyUserService.BindDeviceCallBack bindDeviceCallBack = callBack;
                if (bindDeviceCallBack != null) {
                    bindDeviceCallBack.onBindDeviceSuccess();
                }
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                com.geely.galaxy.router.service.IGalaxyUserService.BindDeviceCallBack bindDeviceCallBack = callBack;
                if (bindDeviceCallBack != null) {
                    bindDeviceCallBack.onBindDeviceFail(error);
                }
            }
        });
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void getDefaultDeliveryAddress(final com.geely.galaxy.router.service.IGalaxyUserService.HttpCallBack callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserRepository().getAddressList(0).observeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.getDefaultDeliveryAddress.1
            {
                super("AuthDetailActivity");
            }

            /* JADX WARN: Code duplicated, block: B:15:0x004b  */
            /* JADX WARN: Code duplicated, block: B:17:0x0051  */
            /* JADX WARN: Type inference failed for: r1v0, types: [com.geely.galaxy.uc.http.SWUserSessionComponent$getDefaultDeliveryAddress$1$onSuccess$result$1] */
            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> response) {
                java.util.ArrayList data;
                java.lang.Object next;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(response, "response");
                com.geely.galaxy.uc.beans.PageResult pageResult = (com.geely.galaxy.uc.beans.PageResult) new com.google.gson.Gson().fromJson((java.lang.String) response.getData(), new com.google.gson.reflect.TypeToken<com.geely.galaxy.uc.beans.PageResult<com.geely.galaxy.uc.beans.DeliveryAddressContent>>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$getDefaultDeliveryAddress$1$onSuccess$result$1
                }.getType());
                java.util.ArrayList data2 = pageResult.getData();
                com.geely.galaxy.uc.beans.DeliveryAddressContent deliveryAddressContent = null;
                if (data2 != null) {
                    java.util.Iterator it = data2.iterator();
                    do {
                        if (!it.hasNext()) {
                            next = null;
                            break;
                        }
                        next = it.next();
                    } while (!((com.geely.galaxy.uc.beans.DeliveryAddressContent) next).isDefaultAddress());
                    com.geely.galaxy.uc.beans.DeliveryAddressContent deliveryAddressContent2 = (com.geely.galaxy.uc.beans.DeliveryAddressContent) next;
                    if (deliveryAddressContent2 == null) {
                        data = pageResult.getData();
                        if (data != null) {
                            deliveryAddressContent = (com.geely.galaxy.uc.beans.DeliveryAddressContent) kotlin.collections.CollectionsKt.getOrNull(data, 0);
                        }
                    } else {
                        deliveryAddressContent = deliveryAddressContent2;
                    }
                } else {
                    data = pageResult.getData();
                    if (data != null) {
                        deliveryAddressContent = (com.geely.galaxy.uc.beans.DeliveryAddressContent) kotlin.collections.CollectionsKt.getOrNull(data, 0);
                    }
                }
                com.geely.galaxy.router.service.IGalaxyUserService.HttpCallBack httpCallBack = callback;
                java.lang.String json = deliveryAddressContent == null ? "" : new com.google.gson.Gson().toJson(deliveryAddressContent);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(json, "if(find == null) \"\" else Gson().toJson(find)");
                httpCallBack.requestSuccess(json);
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                callback.requestError(error);
            }
        });
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void removeDevice(java.lang.String deviceId, final com.geely.galaxy.router.service.IGalaxyUserService.UnbindDeviceCallBack callBack) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callBack, "callBack");
        com.geely.galaxy.uc.beans.AccountDevice accountDevice = new com.geely.galaxy.uc.beans.AccountDevice(null, null, null, null, 15, null);
        accountDevice.setAccountId(java.lang.String.valueOf(onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE, KEY_USER_ID, null, 4, null)));
        accountDevice.setDevice(deviceId);
        accountDevice.setDeviceType("Android");
        com.geely.galaxy.uc.beans.UserLogin userLogin = this.userLogin;
        accountDevice.setMobile(java.lang.String.valueOf(userLogin != null ? userLogin.getMobile() : null));
        getUserRepository().removeDevice(accountDevice).subscribe(new com.geely.galaxy.uc.http.rxObserver.UserObserver<java.lang.String>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.removeDevice.1
            {
                super("removeDevice");
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onSuccess(com.geely.rxhttp.base.IHttpResponse<java.lang.String> response) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(response, "response");
                com.geely.galaxy.router.service.IGalaxyUserService.UnbindDeviceCallBack unbindDeviceCallBack = callBack;
                if (unbindDeviceCallBack != null) {
                    unbindDeviceCallBack.onUnbindDeviceSuccess();
                }
            }

            @Override // com.geely.galaxy.uc.http.rxObserver.UserObserver
            public void onFail(java.lang.String error) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(error, "error");
                com.geely.galaxy.router.service.IGalaxyUserService.UnbindDeviceCallBack unbindDeviceCallBack = callBack;
                if (unbindDeviceCallBack != null) {
                    unbindDeviceCallBack.onUnbindDeviceFail(error);
                }
            }
        });
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void saveUserInfoLog(final android.content.Context context, final kotlin.jvm.functions.Function0<kotlin.Unit> success, final kotlin.jvm.functions.Function0<kotlin.Unit> failed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        getRemoteUserInfo(new kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.saveUserInfoLog.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Object obj) {
                invoke((java.lang.String) obj);
                return kotlin.Unit.INSTANCE;
            }

            public final void invoke(java.lang.String str) {
                java.lang.String strOrDefault;
                try {
                    com.geely.galaxy.uc.beans.User user = (com.geely.galaxy.uc.beans.User) new com.google.gson.Gson().fromJson(str, com.geely.galaxy.uc.beans.User.class);
                    try {
                        com.geely.galaxy.uc.beans.UserLogin userLogin = (com.geely.galaxy.uc.beans.UserLogin) com.geely.galaxy.uc.http.SWUserSessionComponent.INSTANCE.getInstance().safetyParse(com.geely.galaxy.uc.http.SWUserSessionComponent.INSTANCE.getInstance().getUserInfo(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.geely.galaxy.uc.beans.UserLogin.class));
                        if (userLogin != null) {
                            strOrDefault = java.lang.String.valueOf(userLogin.getMobile());
                        } else {
                            strOrDefault = com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getMobile(), "");
                        }
                    } catch (java.lang.Exception unused) {
                        strOrDefault = "";
                    }
                    if (user != null) {
                        java.lang.String str2 = "用户头像：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getAvatarUrl(), "") + "\n昵称：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getDisplayName(), "") + "\n简介：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getDescription(), "") + "\n性别：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getGenderText(), "") + "\n生日：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getBirthDayText(), "") + "\n地区：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(user.getProvinceAndCityText2(), "") + "\n手机号：" + com.geely.galaxy.commons.utils.ExtendUtil.orDefault(strOrDefault, "");
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(calendar, "getInstance()");
                        int i = calendar.get(1);
                        int i2 = calendar.get(2) + 1;
                        int i3 = calendar.get(5);
                        if (com.geely.galaxy.uc.util.FileUtil.INSTANCE.saveToExternalPrivate(context, "吉利银河个人信息导出" + (i + (i2 < 10 ? "0" : "") + i2 + (i3 < 10 ? "0" : "") + i3) + ".txt", str2)) {
                            kotlin.jvm.functions.Function0<kotlin.Unit> function0 = success;
                            if (function0 != null) {
                                function0.invoke();
                                return;
                            }
                            return;
                        }
                    }
                } catch (java.lang.Exception unused2) {
                }
                kotlin.jvm.functions.Function0<kotlin.Unit> function1 = failed;
                if (function1 != null) {
                    function1.invoke();
                }
            }
        });
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void startAddressManager(android.content.Context context, java.lang.String addressId, com.geely.galaxy.router.service.IGalaxyUserService.CommonCallback callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        com.geely.galaxy.uc.util.AddressManager.INSTANCE.startAddressManager(context, addressId, callback);
    }

    public final void getOAuthCode(java.lang.String clientId, java.lang.String scope, java.lang.String responseType, java.lang.String state, io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(responseType, "responseType");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserRepository().getOAuth2Code(clientId, scope, responseType, state).subscribe(callback);
    }

    public final void oneOSAuthorize(java.lang.String uuid, java.lang.String action, io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uuid, com.elynkco.common.utils.CacheUtil.uuid);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserRepository().oneOSAuthorize(uuid, action).subscribe(callback);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void thirdAppOAuth2Authorize(java.lang.String clientId, java.lang.String scope, io.reactivex.Observer<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        getUserRepository().thirdAppOAuth2Authorize(clientId, scope).subscribe(callback);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public boolean isLogin() {
        boolean z = this.userToken != null;
        com.blankj.utilcode.util.LogUtils.i(new java.lang.Object[]{"islogin " + z});
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void listen$lambda$17(kotlin.jvm.functions.Function1 function1, java.lang.Object obj) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "$tmp0");
        function1.invoke(obj);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public <T> void listen(java.lang.String eventCode, final kotlin.reflect.KClass<T> expectClass, final kotlin.jvm.functions.Function1<? super T, kotlin.Unit> callback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(expectClass, "expectClass");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(callback, "callback");
        io.reactivex.Observable observableListen = com.geely.core.common.notification.RxEventNotification.Companion.getInstance().listen(eventCode, expectClass);
        final kotlin.jvm.functions.Function1<com.geely.core.common.notification.base.IDataWrapper<T>, kotlin.Unit> function1 = new kotlin.jvm.functions.Function1<com.geely.core.common.notification.base.IDataWrapper<T>, kotlin.Unit>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.listen.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Object obj) {
                invoke((com.geely.core.common.notification.base.IDataWrapper) obj);
                return kotlin.Unit.INSTANCE;
            }

            public final void invoke(com.geely.core.common.notification.base.IDataWrapper<T> iDataWrapper) {
                callback.invoke(this.safetyParse(iDataWrapper.getData(), expectClass));
            }
        };
        observableListen.subscribe(new io.reactivex.functions.Consumer() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda3
            public final void accept(java.lang.Object obj) {
                com.geely.galaxy.uc.http.SWUserSessionComponent.listen$lambda$17(function1, obj);
            }
        });
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getOAuthCodeErupt(java.lang.String clientId, java.lang.String scope, java.lang.String responseType, java.lang.String state) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clientId, com.ecarx.open.http.utils.iovgoto.iovdo);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(scope, "scope");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(responseType, "responseType");
        return getUserRepository().getOAuth2Code(clientId, scope, responseType, state);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public void getRemoteUserInfo(final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        if (!isLogin()) {
            this.remoteUserInfo = null;
            block.invoke((java.lang.Object) null);
            return;
        }
        java.lang.String str = this.remoteUserInfo;
        if (!(str == null || str.length() == 0)) {
            block.invoke(this.remoteUserInfo);
            return;
        }
        io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> remoteUserInfo = getUserRepository().getRemoteUserInfo();
        final kotlin.jvm.functions.Function1<com.geely.rxhttp.base.IHttpResponse<java.lang.String>, kotlin.Unit> function1 = new kotlin.jvm.functions.Function1<com.geely.rxhttp.base.IHttpResponse<java.lang.String>, kotlin.Unit>() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent.getRemoteUserInfo.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Object obj) {
                invoke((com.geely.rxhttp.base.IHttpResponse<java.lang.String>) obj);
                return kotlin.Unit.INSTANCE;
            }

            public final void invoke(com.geely.rxhttp.base.IHttpResponse<java.lang.String> iHttpResponse) {
                if (iHttpResponse.isSuccessful()) {
                    com.geely.galaxy.uc.http.SWUserSessionComponent.this.remoteUserInfo = (java.lang.String) iHttpResponse.getData();
                }
                block.invoke(com.geely.galaxy.uc.http.SWUserSessionComponent.this.remoteUserInfo);
            }
        };
        remoteUserInfo.subscribe(new io.reactivex.functions.Consumer() { // from class: com.geely.galaxy.uc.http.SWUserSessionComponent$$ExternalSyntheticLambda4
            public final void accept(java.lang.Object obj) {
                com.geely.galaxy.uc.http.SWUserSessionComponent.getRemoteUserInfo$lambda$18(function1, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getRemoteUserInfo$lambda$18(kotlin.jvm.functions.Function1 function1, java.lang.Object obj) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "$tmp0");
        function1.invoke(obj);
    }

    @Override // com.geely.galaxy.router.service.IGalaxyUserService
    public java.lang.String getUserInfo() {
        return decrypt(onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE, KEY_USER_DATA, null, 4, null));
    }

    public final com.geely.galaxy.uc.beans.UserLogin getUserInfo2() {
        try {
            com.geely.galaxy.uc.beans.UserLogin userLogin = (com.geely.galaxy.uc.beans.UserLogin) new com.google.gson.Gson().fromJson(decrypt(onPrefsCURD$default(this, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType.PARSE, KEY_USER_DATA, null, 4, null)), com.geely.galaxy.uc.beans.UserLogin.class);
            if (userLogin == null) {
                return null;
            }
            return userLogin;
        } catch (com.google.gson.JsonParseException e) {
            e.printStackTrace();
            com.blankj.utilcode.util.LogUtils.d(new java.lang.Object[]{"getUserInfo", kotlin.Unit.INSTANCE.toString()});
            return null;
        }
    }

    static /* synthetic */ java.lang.String onPrefsCURD$default(com.geely.galaxy.uc.http.SWUserSessionComponent sWUserSessionComponent, com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType prefsManagerType, java.lang.String str, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        return sWUserSessionComponent.onPrefsCURD(prefsManagerType, str, str2);
    }

    private final synchronized java.lang.String onPrefsCURD(com.geely.galaxy.uc.http.SWUserSessionComponent.PrefsManagerType type, java.lang.String key, java.lang.String value) {
        java.lang.String str;
        int i = com.geely.galaxy.uc.http.SWUserSessionComponent.WhenMappings.$EnumSwitchMapping$0[type.ordinal()];
        if (i == 1) {
            str = (java.lang.String) getPrefs().parse(key, kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(java.lang.String.class), value);
        } else if (i == 2) {
            str = getPrefs().save(key, value) ? "" : null;
        } else if (i == 3) {
            getPrefs().remove(key);
            str = "";
        } else {
            throw new kotlin.NoWhenBranchMatchedException();
        }
        return str;
    }
}
