package com.geely.cockpit.services.controlservice.runtime;

/* JADX INFO: compiled from: CurUserInfo.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u0015B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0007J\n\u0010\b\u001a\u0004\u0018\u00010\tH\u0007J\b\u0010\n\u001a\u00020\u0007H\u0007J\b\u0010\u000b\u001a\u00020\u0004H\u0007J\n\u0010\f\u001a\u0004\u0018\u00010\u0007H\u0007J\b\u0010\r\u001a\u00020\u0007H\u0007J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u0004H\u0007J\f\u0010\u0013\u001a\u00020\u0007*\u00020\u0014H\u0002R\u0018\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002¨\u0006\u0016"}, d2 = {"Lcom/geely/cockpit/services/controlservice/runtime/CurUserInfo;", "", "()V", "userInfo", "Lcom/geely/cockpit/services/controlservice/runtime/CurUserInfo$PlatformUserInfo;", "getUserInfo$annotations", "getIovUserId", "", "getIovUserInfo", "Lcom/geely/snc/login/model/rsp/AuthLoginRspBean;", "getUserId", "getUserInfo", "getUserOpenId", "getUserToken", "isIovLogin", "", "resetAll", "", "setupUserInfo", "traceString", "Lcom/ecarx/open/http/response/LoginResponse;", "PlatformUserInfo", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final class CurUserInfo {
    public static final com.geely.cockpit.services.controlservice.runtime.CurUserInfo INSTANCE = new com.geely.cockpit.services.controlservice.runtime.CurUserInfo();
    private static volatile com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo userInfo = new com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo(null, null, null, null, 15, null);

    @kotlin.jvm.JvmStatic
    private static /* synthetic */ void getUserInfo$annotations() {
    }

    private CurUserInfo() {
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo getUserInfo() {
        return userInfo;
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getUserId() {
        return userInfo.getUserId();
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getUserToken() {
        return userInfo.getUserToken();
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getUserOpenId() {
        return userInfo.getOpenId();
    }

    @kotlin.jvm.JvmStatic
    public static final java.lang.String getIovUserId() {
        com.geely.snc.login.model.rsp.AuthLoginRspBean iovUserInfo = getIovUserInfo();
        java.lang.String userId = iovUserInfo != null ? iovUserInfo.getUserId() : null;
        return userId == null ? "" : userId;
    }

    @kotlin.jvm.JvmStatic
    public static final com.geely.snc.login.model.rsp.AuthLoginRspBean getIovUserInfo() {
        return com.geely.cockpit.services.controlservice.core.support.multiarea.MultiAreaManager.INSTANCE.getCurIovSDKState().getLoginResponse();
    }

    public final boolean isIovLogin() {
        java.lang.String userId;
        com.geely.snc.login.model.rsp.AuthLoginRspBean loginResponse = com.geely.cockpit.services.controlservice.core.support.multiarea.MultiAreaManager.INSTANCE.getCurIovSDKState().getLoginResponse();
        if (loginResponse == null || (userId = loginResponse.getUserId()) == null) {
            return false;
        }
        return !kotlin.text.StringsKt.isBlank(userId);
    }

    @kotlin.jvm.JvmStatic
    public static final void setupUserInfo(com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo userInfo2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userInfo2, "userInfo");
        com.geely.cockpit.services.controlservice.CarControlLogger.INSTANCE.d("用户ID", "setupUserId: " + userInfo2);
        userInfo = userInfo2;
    }

    @kotlin.jvm.JvmStatic
    public static final synchronized void resetAll() {
        if (userInfo.getUserId().length() > 0) {
            com.geely.cockpit.lib.watch.message.WearPhoneMessageCenter.INSTANCE.notifyLogout("");
            com.geely.cockpit.framework.event.LiteEventBus.INSTANCE.publish(new com.geely.cockpit.libs.interfaces.services.event.LogoutEvent(null, 1, null));
        }
        userInfo = new com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo(null, null, null, null, 15, null);
        com.geely.cockpit.services.controlservice.CarControlLogger.INSTANCE.d("用户ID", "reset UserInfo: " + userInfo);
    }

    private final java.lang.String traceString(com.ecarx.open.http.response.LoginResponse loginResponse) {
        return "LoginResponse(id=" + loginResponse.getUserId() + ", accessToken=" + loginResponse.accessToken + ", refreshToken=" + loginResponse.refreshToken + ")";
    }

    /* JADX INFO: compiled from: CurUserInfo.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J1\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0018"}, d2 = {"Lcom/geely/cockpit/services/controlservice/runtime/CurUserInfo$PlatformUserInfo;", "", com.geely.galaxy.mine.constant.RouterParams.personId, "", "mobile", "userToken", "openId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getMobile", "()Ljava/lang/String;", "getOpenId", "getUserId", "getUserToken", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
    public static final /* data */ class PlatformUserInfo {
        private final java.lang.String mobile;
        private final java.lang.String openId;
        private final java.lang.String userId;
        private final java.lang.String userToken;

        public PlatformUserInfo() {
            this(null, null, null, null, 15, null);
        }

        public static /* synthetic */ com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo copy$default(com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo platformUserInfo, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, java.lang.Object obj) {
            if ((i & 1) != 0) {
                str = platformUserInfo.userId;
            }
            if ((i & 2) != 0) {
                str2 = platformUserInfo.mobile;
            }
            if ((i & 4) != 0) {
                str3 = platformUserInfo.userToken;
            }
            if ((i & 8) != 0) {
                str4 = platformUserInfo.openId;
            }
            return platformUserInfo.copy(str, str2, str3, str4);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final java.lang.String getUserId() {
            return this.userId;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final java.lang.String getMobile() {
            return this.mobile;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final java.lang.String getUserToken() {
            return this.userToken;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final java.lang.String getOpenId() {
            return this.openId;
        }

        public final com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo copy(java.lang.String userId, java.lang.String mobile, java.lang.String userToken, java.lang.String openId) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userId, com.geely.galaxy.mine.constant.RouterParams.personId);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mobile, "mobile");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(userToken, "userToken");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openId, "openId");
            return new com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo(userId, mobile, userToken, openId);
        }

        public boolean equals(java.lang.Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo)) {
                return false;
            }
            com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo platformUserInfo = (com.geely.cockpit.services.controlservice.runtime.CurUserInfo.PlatformUserInfo) other;
            return kotlin.jvm.internal.Intrinsics.areEqual(this.userId, platformUserInfo.userId) && kotlin.jvm.internal.Intrinsics.areEqual(this.mobile, platformUserInfo.mobile) && kotlin.jvm.internal.Intrinsics.areEqual(this.userToken, platformUserInfo.userToken) && kotlin.jvm.internal.Intrinsics.areEqual(this.openId, platformUserInfo.openId);
        }

        public int hashCode() {
            return (((((this.userId.hashCode() * 31) + this.mobile.hashCode()) * 31) + this.userToken.hashCode()) * 31) + this.openId.hashCode();
        }

        public java.lang.String toString() {
            return "PlatformUserInfo(userId=" + this.userId + ", mobile=" + this.mobile + ", userToken=" + this.userToken + ", openId=" + this.openId + ")";
        }

        public PlatformUserInfo(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, com.geely.galaxy.mine.constant.RouterParams.personId);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str2, "mobile");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str3, "userToken");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str4, "openId");
            this.userId = str;
            this.mobile = str2;
            this.userToken = str3;
            this.openId = str4;
        }

        public /* synthetic */ PlatformUserInfo(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? "" : str3, (i & 8) != 0 ? "" : str4);
        }

        public final java.lang.String getMobile() {
            return this.mobile;
        }

        public final java.lang.String getOpenId() {
            return this.openId;
        }

        public final java.lang.String getUserId() {
            return this.userId;
        }

        public final java.lang.String getUserToken() {
            return this.userToken;
        }
    }
}
