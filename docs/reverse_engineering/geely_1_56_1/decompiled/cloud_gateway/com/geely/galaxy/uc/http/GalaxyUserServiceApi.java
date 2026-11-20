package com.geely.galaxy.uc.http;

/* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\bf\u0018\u0000 Y2\u00020\u0001:\u0006YZ[\\]^J,\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H'J\u001c\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\n\u001a\u00020\u0005H'J\u001e\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010\f\u001a\u0004\u0018\u00010\u0001H'J\u001e\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H'J\u001c\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0010\u001a\u00020\u0005H'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u001c\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0013\u001a\u00020\u0005H'J\u001c\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0015\u001a\u00020\u0005H'J\u001e\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H'J$\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005H'J\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u001b\u001a\u00020\u001cH'J\b\u0010\u001d\u001a\u00020\u0005H&J\u0014\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0014\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J6\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u00052\b\u0010$\u001a\u0004\u0018\u00010\u0005H'J\u0014\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00040\u0003H'J\u001c\u0010'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\u00040\u00032\u0006\u0010)\u001a\u00020\u0005H'J\u0014\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0016\u0010+\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010-0,0\u0003H'J\u001c\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010/\u001a\u00020\u0005H'J$\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H'J\u001e\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u00102\u001a\u0004\u0018\u00010\u0001H'J\u001c\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\n\u001a\u00020\u0005H'J$\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u00105\u001a\u00020\u00052\u0006\u00106\u001a\u00020\u0005H'J\u001e\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u00102\u001a\u0004\u0018\u00010\u0001H'J.\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u00109\u001a\u00020\u00052\b\u0010:\u001a\u0004\u0018\u00010\u0005H'J\u001c\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010/\u001a\u00020\u0005H'J\u001c\u0010;\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010!\u001a\u00020\u0005H'J\u001e\u0010<\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H'J\u001c\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010!\u001a\u00020\u0005H'J\u001e\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010:\u001a\u0004\u0018\u00010\u0005H'J\u001c\u0010?\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\b\u001a\u00020\u0005H'J\u001e\u0010@\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010:\u001a\u0004\u0018\u00010\u0005H'J\u001c\u0010A\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\b\u001a\u00020\u0005H'J$\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H'J&\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\b\u0010:\u001a\u0004\u0018\u00010\u0005H'J&\u0010D\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\b\u0010:\u001a\u0004\u0018\u00010\u0005H'J \u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\n\b\u0002\u0010F\u001a\u0004\u0018\u00010\u0005H'J&\u0010G\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\b\u0010F\u001a\u0004\u0018\u00010\u0005H'J\u0014\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J$\u0010I\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H'J$\u0010J\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u0005H'J\u001e\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\u0010\f\u001a\u0004\u0018\u00010\u0001H'J\u001c\u0010L\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010M\u001a\u00020NH'J(\u0010L\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0012\u0010O\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050PH'J\"\u0010Q\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00050SH'J\u001e\u0010T\u001a\b\u0012\u0004\u0012\u00020U0\u00032\u0006\u0010V\u001a\u00020W2\u0006\u0010X\u001a\u00020&H'¨\u0006_"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi;", "", "CancellationWithReasons", "Lio/reactivex/Observable;", "Lcom/geely/rxhttp/base/IHttpResponse;", "", "cancellationCause", "causeDescription", "authCode", "OneKeyLogin", "accessToken", "addAddress", "address", "bindDevice", "accountDevice", "changePassword", "newPassword", "clientDetail", "deleteAddressByID", "address_id", "deletetLoginDevices", "device_id", "execThirdPartyLogin", "bindReq", "forgetPassword", "phoneNumber", "getAddressList", "pageNo", "", "getApiBaseUrl", "getCancellationReasons", "getLoginDevices", "getOAuth2Code", com.ecarx.open.http.utils.iovgoto.iovdo, "scope", "responseType", "state", "getOssToken", "Lcom/geely/galaxy/uc/beans/OssToken;", "getProtocolDetail", "Lcom/geely/galaxy/uc/beans/UserProtocol;", "protocolType", "getRemoteUserInfo", "getUserSecuritySettings", "Lcom/geely/galaxy/uc/http/BaseResponce;", "Lcom/geely/galaxy/uc/beans/UserSecurity;", "logout", "refreshToken", "mobileCodeLogin", "modifyMobile", "reqPwd", "oneKeyModifyMobileVerify", "oneOSAuthorize", com.elynkco.common.utils.CacheUtil.uuid, "action", "onekeyModifyMobile", "passwordLogin", "password", "certifyId", "removeAuth", "removeDevice", "scopeDetail", "sendChangePasswordSMS", "sendChangePasswordSMSVerify", "sendDeregisterSMS", "sendDeregisterSMSVerify", "sendFogetPasswordSMSVerify", "sendForgetPasswordSMS", "sendLoginSMS", "sendModifyMobile", "challenge", "sendNewModifyMobile", "setUserInforDefault", "smsModifyMobileVerify", "thirdAppOAuth2Authorize", "updateAddress", "updateUser", "user", "Lcom/geely/galaxy/uc/beans/User;", "map", "", "uploadDeregisterAgreement", com.geely.galaxy.mine.constant.RouterParams.codes, "", "uploadUserAvatar", "Lcom/alibaba/sdk/android/oss/OSSClient;", "context", "Landroid/content/Context;", "ossToken", "Companion", "HOST", "MAIN_DOMAIN", "PROTOCOL", "SCHEME", "USER_DOMAIN", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public interface GalaxyUserServiceApi {
    public static final java.lang.String ADD_ADDRESS = "address/create";
    public static final java.lang.String API_URL_BIND_DEVICE = "device/bind";
    public static final java.lang.String API_URL_DELETE_LOGIN_DEVICES = "hw/device/del";
    public static final java.lang.String API_URL_GET_LOGIN_DEVICES = "hw/device/list";
    public static final java.lang.String API_URL_GET_USER_SECURITY_SETTINGS = "user/securitySettings";
    public static final java.lang.String API_URL_REMOVE_DEVICE = "device/remove";
    public static final java.lang.String API_URL_THIRD_PARTY_LOGIN = "/login/thirdparty/login";
    public static final java.lang.String AUTH_PATH = "api/v1";
    public static final java.lang.String CANCELLATION_WITH_REASON = "user/cancellation";
    public static final java.lang.String CHANGE_PASSWORD = "user/modify/password";
    public static final java.lang.String CHANGE_PASSWORD_AUTH_CODE_VERIFY = "user/modify/smsVerify";
    public static final java.lang.String CHANGE_PASSWORD_SEND_AUTH_CODE = "user/modify/password/sendSms";
    public static final java.lang.String CLIENT_DETAIL = "oauth2/authorize/clientDetail";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final com.geely.galaxy.uc.http.GalaxyUserServiceApi.Companion INSTANCE = com.geely.galaxy.uc.http.GalaxyUserServiceApi.Companion.$$INSTANCE;
    public static final java.lang.String DELETE_ADDRESS = "address/delete";
    public static final java.lang.String DEREGISTER_AUTH_CODE_VERIFY = "user/cancellation/smsVerify";
    public static final java.lang.String DEREGISTER_SEND_AUTH_CODE = "user/cancellation/sendCancellationCode";
    public static final java.lang.String DEREGISTER_UPLOAD_AGREEMENT = "agreement/uploadAgreement";
    public static final java.lang.String FORGET_PASSWORD = "user/forgot/password";
    public static final java.lang.String FORGET_PASSWORD_AUTH_CODE_VERIFY = "user/forget/smsVerify";
    public static final java.lang.String FORGET_PASSWORD_SEND_AUTH_CODE = "user/forget/password/sendSms";
    public static final java.lang.String GET_ADDRESS = "address/paging";
    public static final java.lang.String GET_ADDRESS_DETAIL = "address/detail";
    public static final java.lang.String GET_CANCELLATION_REASON = "user/cancellation/cause";
    public static final java.lang.String GET_OAUTH2_AUTHORIZE = "oauth2/authorize";
    public static final java.lang.String GET_OAUTH2_CODE = "oauth2/code";
    public static final java.lang.String GET_ONEOS_OAUTH_CODE = "oneos/qrlogin/authorize";
    public static final java.lang.String LOGIN_SEND_AUTH_CODE = "login/sendSms";
    public static final java.lang.String LOGOUT = "login/logout";
    public static final java.lang.String MOBILE_CODE_LOGIN = "login/mobileCodeLogin";
    public static final java.lang.String MODIFY_PHONE = "user/modifyMobile";
    public static final java.lang.String ONEKEY_MODIFY_PHONE = "user/oneKeyModifyMobile";
    public static final java.lang.String ONEKEY_VERIFYMOBILE = "user/verifyMobile";
    public static final java.lang.String ONE_KEY_LOGIN = "login/onekey/mobileLogin";
    public static final java.lang.String OSS_TOKEN = "oss/security/token";
    public static final java.lang.String PASSWORD_LOGIN = "login/pwdLogin";
    public static final java.lang.String PROTOCOL_DETAIL = "agreement/getUserAgreementDetail";
    public static final java.lang.String REFRESH_TOKEN = "login/refresh";
    public static final java.lang.String REMOVE_AUTH = "oauth2/authorize/cancel";
    public static final java.lang.String SCOPE_DETAIL = "oauth2/authorize/scopeDetail";
    public static final java.lang.String SEND_MODIFY_MOBILE = "user/sendModifyMobile";
    public static final java.lang.String SEND_NEW_MODIFY_MOBILE = "user/sendNewModifyMobile";
    public static final java.lang.String SMS_MODIFY_MOBILE_VERIFY = "user/smsModifyMobileVerify";
    public static final java.lang.String UPDATE_ADDRESS = "address/update";
    public static final java.lang.String UPDATE_USER = "user/update";
    public static final java.lang.String USER_DEFAULT = "user/resetAccountInfo";
    public static final java.lang.String USER_INFO = "user/info";

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> CancellationWithReasons(java.lang.String cancellationCause, java.lang.String causeDescription, java.lang.String authCode);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> OneKeyLogin(java.lang.String accessToken);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> addAddress(java.lang.Object address);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> bindDevice(java.lang.Object accountDevice);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> changePassword(java.lang.String newPassword);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> clientDetail();

    @retrofit2.http.DELETE
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> deleteAddressByID(java.lang.String address_id);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> deletetLoginDevices(java.lang.String device_id);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> execThirdPartyLogin(java.lang.Object bindReq);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> forgetPassword(java.lang.String phoneNumber, java.lang.String newPassword);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getAddressList(int pageNo);

    java.lang.String getApiBaseUrl();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getCancellationReasons();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getLoginDevices();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getOAuth2Code(java.lang.String clientId, java.lang.String scope, java.lang.String responseType, java.lang.String state);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.OssToken>> getOssToken();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<com.geely.galaxy.uc.beans.UserProtocol>> getProtocolDetail(java.lang.String protocolType);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> getRemoteUserInfo();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.galaxy.uc.http.BaseResponce<com.geely.galaxy.uc.beans.UserSecurity>> getUserSecuritySettings();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> logout(java.lang.String refreshToken);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> mobileCodeLogin(java.lang.String phoneNumber, java.lang.String authCode);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> modifyMobile(java.lang.Object reqPwd);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> oneKeyModifyMobileVerify(java.lang.String accessToken);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> oneOSAuthorize(java.lang.String uuid, java.lang.String action);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> onekeyModifyMobile(java.lang.Object reqPwd);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> passwordLogin(java.lang.String phoneNumber, java.lang.String password, java.lang.String certifyId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> refreshToken(java.lang.String refreshToken);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> removeAuth(java.lang.String clientId);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> removeDevice(java.lang.Object accountDevice);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> scopeDetail(java.lang.String clientId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendChangePasswordSMS(java.lang.String certifyId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendChangePasswordSMSVerify(java.lang.String authCode);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendDeregisterSMS(java.lang.String certifyId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendDeregisterSMSVerify(java.lang.String authCode);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendFogetPasswordSMSVerify(java.lang.String phoneNumber, java.lang.String authCode);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendForgetPasswordSMS(java.lang.String phoneNumber, java.lang.String certifyId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendLoginSMS(java.lang.String phoneNumber, java.lang.String certifyId);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendModifyMobile(java.lang.String challenge);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> sendNewModifyMobile(java.lang.String phoneNumber, java.lang.String challenge);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> setUserInforDefault();

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> smsModifyMobileVerify(java.lang.String phoneNumber, java.lang.String authCode);

    @retrofit2.http.GET
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> thirdAppOAuth2Authorize(java.lang.String clientId, java.lang.String scope);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateAddress(java.lang.Object address);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateUser(com.geely.galaxy.uc.beans.User user);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> updateUser(java.util.Map<java.lang.String, java.lang.String> map);

    @retrofit2.http.POST
    io.reactivex.Observable<com.geely.rxhttp.base.IHttpResponse<java.lang.String>> uploadDeregisterAgreement(java.util.List<java.lang.String> codes);

    @retrofit2.http.PUT
    io.reactivex.Observable<com.alibaba.sdk.android.oss.OSSClient> uploadUserAvatar(android.content.Context context, com.geely.galaxy.uc.beans.OssToken ossToken);

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\r"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$HOST;", "", "ipAndPort", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getIpAndPort", "()Ljava/lang/String;", "setIpAndPort", "(Ljava/lang/String;)V", "UAT_HOST", "PRE_HOST", "PRODUCT_HOST", "UNKNOWN_HOST", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum HOST {
        UAT_HOST("galaxy-user-api.geely-test.com"),
        PRE_HOST("galaxy-user-api-pre.geely-test.com"),
        PRODUCT_HOST("galaxy-user-api.geely.com"),
        UNKNOWN_HOST("");

        private java.lang.String ipAndPort;

        HOST(java.lang.String str) {
            this.ipAndPort = str;
        }

        public final java.lang.String getIpAndPort() {
            return this.ipAndPort;
        }

        public final void setIpAndPort(java.lang.String str) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.ipAndPort = str;
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$SCHEME;", "", "value", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValue", "()Ljava/lang/String;", "setValue", "(Ljava/lang/String;)V", "HTTP", "HTTPS", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum SCHEME {
        HTTP("http://"),
        HTTPS("https://");

        private java.lang.String value;

        SCHEME(java.lang.String str) {
            this.value = str;
        }

        public final java.lang.String getValue() {
            return this.value;
        }

        public final void setValue(java.lang.String str) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.value = str;
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$PROTOCOL;", "", "type", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getType", "()Ljava/lang/String;", "setType", "(Ljava/lang/String;)V", "REGISTER", "PRIVACY_POLICE", "DEREGISTER", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum PROTOCOL {
        REGISTER("galaxy_user_agreement"),
        PRIVACY_POLICE("galaxy_privacy_agreement"),
        DEREGISTER("galaxy_cancellation");

        private java.lang.String type;

        PROTOCOL(java.lang.String str) {
            this.type = str;
        }

        public final java.lang.String getType() {
            return this.type;
        }

        public final void setType(java.lang.String str) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.type = str;
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\r"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$USER_DOMAIN;", "", "value", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValue", "()Ljava/lang/String;", "setValue", "(Ljava/lang/String;)V", "GALAXY_USERMODULE_HOST_DEV", "GALAXY_USERMODULE_HOST_UAT", "GALAXY_USERMODULE_HOST_PRE", "GALAXY_USERMODULE_HOST_PRO", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum USER_DOMAIN {
        GALAXY_USERMODULE_HOST_DEV("galaxy-user-api.geely-test.com"),
        GALAXY_USERMODULE_HOST_UAT("galaxy-user-api.geely-test.com"),
        GALAXY_USERMODULE_HOST_PRE("galaxy-user-api-pre.geely-test.com"),
        GALAXY_USERMODULE_HOST_PRO("galaxy-user-api.geely.com");

        private java.lang.String value;

        USER_DOMAIN(java.lang.String str) {
            this.value = str;
        }

        public final java.lang.String getValue() {
            return this.value;
        }

        public final void setValue(java.lang.String str) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.value = str;
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\r"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$MAIN_DOMAIN;", "", "value", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValue", "()Ljava/lang/String;", "setValue", "(Ljava/lang/String;)V", "GALAXY_MAIN_HOST_DEV", "GALAXY_MAIN_HOST_UAT", "GALAXY_MAIN_HOST_PRE", "GALAXY_MAIN_HOST_PRO", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public enum MAIN_DOMAIN {
        GALAXY_MAIN_HOST_DEV("galaxy-app-test.geely-test.com"),
        GALAXY_MAIN_HOST_UAT("galaxy-app-uat.geely-test.com"),
        GALAXY_MAIN_HOST_PRE("galaxy-app-pre.geely-test.com"),
        GALAXY_MAIN_HOST_PRO("galaxy-app.geely.com");

        private java.lang.String value;

        MAIN_DOMAIN(java.lang.String str) {
            this.value = str;
        }

        public final java.lang.String getValue() {
            return this.value;
        }

        public final void setValue(java.lang.String str) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.value = str;
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b.\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u00062"}, d2 = {"Lcom/geely/galaxy/uc/http/GalaxyUserServiceApi$Companion;", "", "()V", "ADD_ADDRESS", "", "API_URL_BIND_DEVICE", "API_URL_DELETE_LOGIN_DEVICES", "API_URL_GET_LOGIN_DEVICES", "API_URL_GET_USER_SECURITY_SETTINGS", "API_URL_REMOVE_DEVICE", "API_URL_THIRD_PARTY_LOGIN", "AUTH_PATH", "CANCELLATION_WITH_REASON", "CHANGE_PASSWORD", "CHANGE_PASSWORD_AUTH_CODE_VERIFY", "CHANGE_PASSWORD_SEND_AUTH_CODE", "CLIENT_DETAIL", "DELETE_ADDRESS", "DEREGISTER_AUTH_CODE_VERIFY", "DEREGISTER_SEND_AUTH_CODE", "DEREGISTER_UPLOAD_AGREEMENT", "FORGET_PASSWORD", "FORGET_PASSWORD_AUTH_CODE_VERIFY", "FORGET_PASSWORD_SEND_AUTH_CODE", "GET_ADDRESS", "GET_ADDRESS_DETAIL", "GET_CANCELLATION_REASON", "GET_OAUTH2_AUTHORIZE", "GET_OAUTH2_CODE", "GET_ONEOS_OAUTH_CODE", "LOGIN_SEND_AUTH_CODE", "LOGOUT", "MOBILE_CODE_LOGIN", "MODIFY_PHONE", "ONEKEY_MODIFY_PHONE", "ONEKEY_VERIFYMOBILE", "ONE_KEY_LOGIN", "OSS_TOKEN", "PASSWORD_LOGIN", "PROTOCOL_DETAIL", "REFRESH_TOKEN", "REMOVE_AUTH", "SCOPE_DETAIL", "SEND_MODIFY_MOBILE", "SEND_NEW_MODIFY_MOBILE", "SMS_MODIFY_MOBILE_VERIFY", "UPDATE_ADDRESS", "UPDATE_USER", "USER_DEFAULT", "USER_INFO", "app_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final class Companion {
        static final /* synthetic */ com.geely.galaxy.uc.http.GalaxyUserServiceApi.Companion $$INSTANCE = new com.geely.galaxy.uc.http.GalaxyUserServiceApi.Companion();
        public static final java.lang.String ADD_ADDRESS = "address/create";
        public static final java.lang.String API_URL_BIND_DEVICE = "device/bind";
        public static final java.lang.String API_URL_DELETE_LOGIN_DEVICES = "hw/device/del";
        public static final java.lang.String API_URL_GET_LOGIN_DEVICES = "hw/device/list";
        public static final java.lang.String API_URL_GET_USER_SECURITY_SETTINGS = "user/securitySettings";
        public static final java.lang.String API_URL_REMOVE_DEVICE = "device/remove";
        public static final java.lang.String API_URL_THIRD_PARTY_LOGIN = "/login/thirdparty/login";
        public static final java.lang.String AUTH_PATH = "api/v1";
        public static final java.lang.String CANCELLATION_WITH_REASON = "user/cancellation";
        public static final java.lang.String CHANGE_PASSWORD = "user/modify/password";
        public static final java.lang.String CHANGE_PASSWORD_AUTH_CODE_VERIFY = "user/modify/smsVerify";
        public static final java.lang.String CHANGE_PASSWORD_SEND_AUTH_CODE = "user/modify/password/sendSms";
        public static final java.lang.String CLIENT_DETAIL = "oauth2/authorize/clientDetail";
        public static final java.lang.String DELETE_ADDRESS = "address/delete";
        public static final java.lang.String DEREGISTER_AUTH_CODE_VERIFY = "user/cancellation/smsVerify";
        public static final java.lang.String DEREGISTER_SEND_AUTH_CODE = "user/cancellation/sendCancellationCode";
        public static final java.lang.String DEREGISTER_UPLOAD_AGREEMENT = "agreement/uploadAgreement";
        public static final java.lang.String FORGET_PASSWORD = "user/forgot/password";
        public static final java.lang.String FORGET_PASSWORD_AUTH_CODE_VERIFY = "user/forget/smsVerify";
        public static final java.lang.String FORGET_PASSWORD_SEND_AUTH_CODE = "user/forget/password/sendSms";
        public static final java.lang.String GET_ADDRESS = "address/paging";
        public static final java.lang.String GET_ADDRESS_DETAIL = "address/detail";
        public static final java.lang.String GET_CANCELLATION_REASON = "user/cancellation/cause";
        public static final java.lang.String GET_OAUTH2_AUTHORIZE = "oauth2/authorize";
        public static final java.lang.String GET_OAUTH2_CODE = "oauth2/code";
        public static final java.lang.String GET_ONEOS_OAUTH_CODE = "oneos/qrlogin/authorize";
        public static final java.lang.String LOGIN_SEND_AUTH_CODE = "login/sendSms";
        public static final java.lang.String LOGOUT = "login/logout";
        public static final java.lang.String MOBILE_CODE_LOGIN = "login/mobileCodeLogin";
        public static final java.lang.String MODIFY_PHONE = "user/modifyMobile";
        public static final java.lang.String ONEKEY_MODIFY_PHONE = "user/oneKeyModifyMobile";
        public static final java.lang.String ONEKEY_VERIFYMOBILE = "user/verifyMobile";
        public static final java.lang.String ONE_KEY_LOGIN = "login/onekey/mobileLogin";
        public static final java.lang.String OSS_TOKEN = "oss/security/token";
        public static final java.lang.String PASSWORD_LOGIN = "login/pwdLogin";
        public static final java.lang.String PROTOCOL_DETAIL = "agreement/getUserAgreementDetail";
        public static final java.lang.String REFRESH_TOKEN = "login/refresh";
        public static final java.lang.String REMOVE_AUTH = "oauth2/authorize/cancel";
        public static final java.lang.String SCOPE_DETAIL = "oauth2/authorize/scopeDetail";
        public static final java.lang.String SEND_MODIFY_MOBILE = "user/sendModifyMobile";
        public static final java.lang.String SEND_NEW_MODIFY_MOBILE = "user/sendNewModifyMobile";
        public static final java.lang.String SMS_MODIFY_MOBILE_VERIFY = "user/smsModifyMobileVerify";
        public static final java.lang.String UPDATE_ADDRESS = "address/update";
        public static final java.lang.String UPDATE_USER = "user/update";
        public static final java.lang.String USER_DEFAULT = "user/resetAccountInfo";
        public static final java.lang.String USER_INFO = "user/info";

        private Companion() {
        }
    }

    /* JADX INFO: compiled from: GalaxyUserServiceApi.kt */
    @kotlin.Metadata(k = 3, mv = {1, 8, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ io.reactivex.Observable sendModifyMobile$default(com.geely.galaxy.uc.http.GalaxyUserServiceApi galaxyUserServiceApi, java.lang.String str, int i, java.lang.Object obj) {
            if (obj != null) {
                throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: sendModifyMobile");
            }
            if ((i & 1) != 0) {
                str = null;
            }
            return galaxyUserServiceApi.sendModifyMobile(str);
        }
    }
}
