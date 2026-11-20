package com.geely.galaxy.car.init;

/* JADX INFO: compiled from: ParseEnvConfigUtils.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes13.dex */
@kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014H\u0003J\b\u0010\u0015\u001a\u0004\u0018\u00010\u0004J\b\u0010\u0016\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u0004\u0018\u00010\n8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u000f\u001a\u0004\u0018\u00010\n8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0011\u0010\u000e\u001a\u0004\b\u0010\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/geely/galaxy/car/init/ParseEnvConfigUtils;", com.jaeger.library.BuildConfig.FLAVOR, "()V", "CAR_ENVIRONMENT_ASSET_PATH", com.jaeger.library.BuildConfig.FLAVOR, "CAR_ENVIRONMENT_ASSET_PATH_NEW", "KEY_NAME_API_BASE_URL", "KEY_NAME_ENVIRONMENT", "KEY_NAME_PRODUCTION", "currentEnvConfig", "Lcom/google/gson/JsonObject;", "getCurrentEnvConfig", "()Lcom/google/gson/JsonObject;", "currentEnvConfig$delegate", "Lkotlin/Lazy;", "currentEnvConfigNew", "getCurrentEnvConfigNew", "currentEnvConfigNew$delegate", "getAppCurrentEnv", com.geely.galaxy.car.utils.CarEnvConst.MODULE_ENV, "Lcom/geely/swannotation/base/AppEnvKit;", "getBaseUrlOta", "getBaseUrlOtaNew", "app_release"}, k = 1, mv = {1, 8, 0}, xi = com.geely.core.common.R2.attr.actionBarStyle)
public final class ParseEnvConfigUtils {
    private static final java.lang.String CAR_ENVIRONMENT_ASSET_PATH = "CarEnvConf.json";
    private static final java.lang.String CAR_ENVIRONMENT_ASSET_PATH_NEW = "CarEnvConfNew.json";
    private static final java.lang.String KEY_NAME_API_BASE_URL = "otaApiBaseUrl";
    private static final java.lang.String KEY_NAME_ENVIRONMENT = "environment";
    private static final java.lang.String KEY_NAME_PRODUCTION = "production";
    public static final com.geely.galaxy.car.init.ParseEnvConfigUtils INSTANCE = new com.geely.galaxy.car.init.ParseEnvConfigUtils();

    /* JADX INFO: renamed from: currentEnvConfig$delegate, reason: from kotlin metadata */
    private static final kotlin.Lazy currentEnvConfig = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.google.gson.JsonObject>() { // from class: com.geely.galaxy.car.init.ParseEnvConfigUtils$currentEnvConfig$2
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final com.google.gson.JsonObject m375invoke() {
            try {
                return ((com.google.gson.JsonObject) com.blankj.utilcode.util.GsonUtils.fromJson(com.geely.core.common.util.AssetUtil.readJson("CarEnvConf.json"), com.google.gson.JsonObject.class)).getAsJsonObject("environment").getAsJsonObject(com.geely.galaxy.car.init.ParseEnvConfigUtils.INSTANCE.getAppCurrentEnv(com.geely.core.common.env.AppEnvProxy.INSTANCE.getInstance().getCurrentEnv()));
            } catch (java.lang.Exception unused) {
                return null;
            }
        }
    });

    /* JADX INFO: renamed from: currentEnvConfigNew$delegate, reason: from kotlin metadata */
    private static final kotlin.Lazy currentEnvConfigNew = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<com.google.gson.JsonObject>() { // from class: com.geely.galaxy.car.init.ParseEnvConfigUtils$currentEnvConfigNew$2
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final com.google.gson.JsonObject m376invoke() {
            try {
                return ((com.google.gson.JsonObject) com.blankj.utilcode.util.GsonUtils.fromJson(com.geely.core.common.util.AssetUtil.readJson("CarEnvConfNew.json"), com.google.gson.JsonObject.class)).getAsJsonObject("environment").getAsJsonObject(com.geely.galaxy.car.init.ParseEnvConfigUtils.INSTANCE.getAppCurrentEnv(com.geely.core.common.env.AppEnvProxy.INSTANCE.getInstance().getCurrentEnv()));
            } catch (java.lang.Exception unused) {
                return null;
            }
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    @com.geely.core.common.protect.LDPProtect
    public final native java.lang.String getAppCurrentEnv(com.geely.swannotation.base.AppEnvKit env);

    private ParseEnvConfigUtils() {
    }

    private final com.google.gson.JsonObject getCurrentEnvConfig() {
        return (com.google.gson.JsonObject) currentEnvConfig.getValue();
    }

    private final com.google.gson.JsonObject getCurrentEnvConfigNew() {
        return (com.google.gson.JsonObject) currentEnvConfigNew.getValue();
    }

    public final java.lang.String getBaseUrlOta() {
        com.google.gson.JsonElement jsonElement;
        try {
            com.google.gson.JsonObject currentEnvConfig2 = getCurrentEnvConfig();
            if (currentEnvConfig2 == null || (jsonElement = currentEnvConfig2.get(KEY_NAME_API_BASE_URL)) == null) {
                return null;
            }
            return jsonElement.getAsString();
        } catch (java.lang.Exception unused) {
            return null;
        }
    }

    public final java.lang.String getBaseUrlOtaNew() {
        com.google.gson.JsonElement jsonElement;
        try {
            com.google.gson.JsonObject currentEnvConfigNew2 = getCurrentEnvConfigNew();
            if (currentEnvConfigNew2 == null || (jsonElement = currentEnvConfigNew2.get(KEY_NAME_API_BASE_URL)) == null) {
                return null;
            }
            return jsonElement.getAsString();
        } catch (java.lang.Exception unused) {
            return null;
        }
    }
}
