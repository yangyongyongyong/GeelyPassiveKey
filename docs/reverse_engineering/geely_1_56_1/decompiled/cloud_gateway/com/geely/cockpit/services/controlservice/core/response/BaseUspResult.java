package com.geely.cockpit.services.controlservice.core.response;

/* JADX INFO: compiled from: BaseUspResult.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B=\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0007\u001a\u0004\u0018\u00018\u0000\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00018\u0000HÆ\u0003¢\u0006\u0002\u0010\u000eJ\t\u0010\u001c\u001a\u00020\tHÆ\u0003JN\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00018\u00002\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001¢\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"HÖ\u0003J\t\u0010#\u001a\u00020$HÖ\u0001J\b\u0010%\u001a\u00020 H\u0016J\b\u0010&\u001a\u00020\u0004H\u0016R\u0016\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0018\u0010\u0007\u001a\u0004\u0018\u00018\u0000X\u0096\u0004¢\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\fR\u0014\u0010\u0012\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\fR\u001a\u0010\b\u001a\u00020\tX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006'"}, d2 = {"Lcom/geely/cockpit/services/controlservice/core/response/BaseUspResult;", "T", "Lcom/geely/cockpit/services/controlservice/core/response/ICarControlResult;", "code", "", "galaxyMessage", "geelyMessage", "data", "tspPushResult", "Lcom/geely/cockpit/services/controlservice/core/support/push/ActionResult;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Lcom/geely/cockpit/services/controlservice/core/support/push/ActionResult;)V", "getCode", "()Ljava/lang/String;", "getData", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getGalaxyMessage", "getGeelyMessage", "msg", "getMsg", "getTspPushResult", "()Lcom/geely/cockpit/services/controlservice/core/support/push/ActionResult;", "setTspPushResult", "(Lcom/geely/cockpit/services/controlservice/core/support/push/ActionResult;)V", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Lcom/geely/cockpit/services/controlservice/core/support/push/ActionResult;)Lcom/geely/cockpit/services/controlservice/core/response/BaseUspResult;", "equals", "", "other", "", "hashCode", "", "isSucceed", "toString", "LibCarControlNew_release"}, k = 1, mv = {1, 8, 0}, xi = 48)
public final /* data */ class BaseUspResult<T> implements com.geely.cockpit.services.controlservice.core.response.ICarControlResult<T> {
    private final java.lang.String code;
    private final T data;

    @com.google.gson.annotations.SerializedName("msg")
    private final java.lang.String galaxyMessage;

    @com.google.gson.annotations.SerializedName("message")
    private final java.lang.String geelyMessage;
    private com.geely.cockpit.services.controlservice.core.support.push.ActionResult tspPushResult;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ com.geely.cockpit.services.controlservice.core.response.BaseUspResult copy$default(com.geely.cockpit.services.controlservice.core.response.BaseUspResult baseUspResult, java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.Object obj, com.geely.cockpit.services.controlservice.core.support.push.ActionResult actionResult, int i, java.lang.Object obj2) {
        if ((i & 1) != 0) {
            str = baseUspResult.code;
        }
        if ((i & 2) != 0) {
            str2 = baseUspResult.galaxyMessage;
        }
        java.lang.String str4 = str2;
        if ((i & 4) != 0) {
            str3 = baseUspResult.geelyMessage;
        }
        java.lang.String str5 = str3;
        if ((i & 8) != 0) {
            obj = baseUspResult.data;
        }
        java.lang.Object obj3 = obj;
        if ((i & 16) != 0) {
            actionResult = baseUspResult.tspPushResult;
        }
        return baseUspResult.copy(str, str4, str5, obj3, actionResult);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getCode() {
        return this.code;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final java.lang.String getGalaxyMessage() {
        return this.galaxyMessage;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final java.lang.String getGeelyMessage() {
        return this.geelyMessage;
    }

    public final T component4() {
        return this.data;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final com.geely.cockpit.services.controlservice.core.support.push.ActionResult getTspPushResult() {
        return this.tspPushResult;
    }

    public final com.geely.cockpit.services.controlservice.core.response.BaseUspResult<T> copy(java.lang.String code, java.lang.String galaxyMessage, java.lang.String geelyMessage, T data, com.geely.cockpit.services.controlservice.core.support.push.ActionResult tspPushResult) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tspPushResult, "tspPushResult");
        return new com.geely.cockpit.services.controlservice.core.response.BaseUspResult<>(code, galaxyMessage, geelyMessage, data, tspPushResult);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.geely.cockpit.services.controlservice.core.response.BaseUspResult)) {
            return false;
        }
        com.geely.cockpit.services.controlservice.core.response.BaseUspResult baseUspResult = (com.geely.cockpit.services.controlservice.core.response.BaseUspResult) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.code, baseUspResult.code) && kotlin.jvm.internal.Intrinsics.areEqual(this.galaxyMessage, baseUspResult.galaxyMessage) && kotlin.jvm.internal.Intrinsics.areEqual(this.geelyMessage, baseUspResult.geelyMessage) && kotlin.jvm.internal.Intrinsics.areEqual(this.data, baseUspResult.data) && kotlin.jvm.internal.Intrinsics.areEqual(this.tspPushResult, baseUspResult.tspPushResult);
    }

    public int hashCode() {
        java.lang.String str = this.code;
        int iHashCode = (str == null ? 0 : str.hashCode()) * 31;
        java.lang.String str2 = this.galaxyMessage;
        int iHashCode2 = (iHashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        java.lang.String str3 = this.geelyMessage;
        int iHashCode3 = (iHashCode2 + (str3 == null ? 0 : str3.hashCode())) * 31;
        T t = this.data;
        return ((iHashCode3 + (t != null ? t.hashCode() : 0)) * 31) + this.tspPushResult.hashCode();
    }

    public BaseUspResult(java.lang.String str, java.lang.String str2, java.lang.String str3, T t, com.geely.cockpit.services.controlservice.core.support.push.ActionResult actionResult) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(actionResult, "tspPushResult");
        this.code = str;
        this.galaxyMessage = str2;
        this.geelyMessage = str3;
        this.data = t;
        this.tspPushResult = actionResult;
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public com.geely.cockpit.services.controlservice.core.response.ICarControlResult<T> onFailure(kotlin.jvm.functions.Function1<? super com.geely.cockpit.services.controlservice.core.response.ICarControlResult<T>, kotlin.Unit> function1) {
        return com.geely.cockpit.services.controlservice.core.response.ICarControlResult.DefaultImpls.onFailure(this, function1);
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public com.geely.cockpit.services.controlservice.core.response.ICarControlResult<T> onSuccess(kotlin.jvm.functions.Function1<? super T, kotlin.Unit> function1) {
        return com.geely.cockpit.services.controlservice.core.response.ICarControlResult.DefaultImpls.onSuccess(this, function1);
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public java.lang.String getCode() {
        return this.code;
    }

    public final java.lang.String getGalaxyMessage() {
        return this.galaxyMessage;
    }

    public final java.lang.String getGeelyMessage() {
        return this.geelyMessage;
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public T getData() {
        return this.data;
    }

    public /* synthetic */ BaseUspResult(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.Object obj, com.geely.cockpit.services.controlservice.core.support.push.ActionResult actionResult, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "1001002" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? "" : str3, obj, (i & 16) != 0 ? new com.geely.cockpit.services.controlservice.core.support.push.ActionResult(null, null, null, 7, null) : actionResult);
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public com.geely.cockpit.services.controlservice.core.support.push.ActionResult getTspPushResult() {
        return this.tspPushResult;
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public void setTspPushResult(com.geely.cockpit.services.controlservice.core.support.push.ActionResult actionResult) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(actionResult, "<set-?>");
        this.tspPushResult = actionResult;
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public boolean isSucceed() {
        return kotlin.jvm.internal.Intrinsics.areEqual(getCode(), "0");
    }

    public java.lang.String toString() {
        return "BaseUspResult{msg='" + getMsg() + "', code='" + getCode() + "', data=" + getData() + ", galaxyMessage=" + this.galaxyMessage + ", geelyMessage=" + this.geelyMessage + "}";
    }

    @Override // com.geely.cockpit.services.controlservice.core.response.ICarControlResult
    public java.lang.String getMsg() {
        java.lang.String str;
        if (com.geely.cockpit.services.controlservice.runtime.RuntimeUtilsKt.isGeelyApp()) {
            str = this.geelyMessage;
            if (str == null) {
                return "";
            }
        } else {
            str = this.galaxyMessage;
            if (str == null) {
                return "";
            }
        }
        return str;
    }
}
