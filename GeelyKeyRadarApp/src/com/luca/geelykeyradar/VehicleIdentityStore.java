package com.luca.geelykeyradar;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 本车及多车车库蓝牙身份的本地持久化。
 *
 * 只保存广播里能观察到的公开信息，不保存 VIN 全码、账号、令牌或任何吉利私有数据。
 */
final class VehicleIdentityStore {

    private static final String PREFS = "passive_key_prefs";
    private static final String KEY_ADDRESS = "vehicle_address";
    private static final String KEY_NAME = "vehicle_name";
    private static final String KEY_TAIL2 = "vehicle_vin_tail2";
    private static final String KEY_TAIL3 = "vehicle_vin_tail3";
    private static final String KEY_SIG = "vehicle_mfg_signature";
    private static final String KEY_COMPANY = "vehicle_manufacturer_id";
    private static final String KEY_LEARNED_AT = "vehicle_learned_at";
    private static final String KEY_MANUAL_CLEARED = "vehicle_manual_cleared";
    private static final String KEY_LEARNING_SOURCE = "vehicle_learning_source";

    private static final String KEY_GARAGE_JSON = "vehicle_garage_json";
    private static final String KEY_PINNED_PROFILE_ID = "vehicle_pinned_profile_id";
    private static final String KEY_LEARNING_PROFILE_ID = "vehicle_learning_profile_id";
    private static final String KEY_BRAND_MODE = "official_brand_mode";

    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    private static final String KEY_BLOCK_WHEN_CHARGING = "block_control_when_charging";
    private static final String KEY_UNLOCK_DBM = "unlock_dbm";
    private static final String KEY_LOCK_DBM = "lock_dbm";
    private static final String KEY_LOCK_DWELL_MS = "lock_dwell_ms";
    private static final String KEY_CONNECT_DBM = "connect_dbm";
    private static final String KEY_WIDGET_FALLBACK_ENABLED = "widget_fallback_enabled";

    static final float DEFAULT_UNLOCK_DBM = PassiveKeyDefaults.UNLOCK_DBM;
    static final float DEFAULT_LOCK_DBM = PassiveKeyDefaults.LOCK_DBM;
    static final long DEFAULT_LOCK_DWELL_MS = PassiveKeyDefaults.LOCK_DWELL_MS;
    static final float DEFAULT_CONNECT_DBM = PassiveKeyDefaults.CONNECT_DBM;

    private final Context appContext;
    private final SharedPreferences prefs;

    /** 打开本应用偏好，不读取官方App私有数据。 */
    VehicleIdentityStore(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = this.appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /**
     * 加载已学习的单车蓝牙身份（兼容旧版接口）。
     */
    VehicleIdentity load() {
        String address = prefs.getString(KEY_ADDRESS, "");
        String name = prefs.getString(KEY_NAME, "");
        String tail2 = prefs.getString(KEY_TAIL2, "");
        String tail3 = prefs.getString(KEY_TAIL3, "");
        String signature = prefs.getString(KEY_SIG, "");
        long learnedAt = prefs.getLong(KEY_LEARNED_AT, 0L);
        if (address.isEmpty() && name.isEmpty() && tail2.isEmpty() && signature.isEmpty()) {
            if (!prefs.getBoolean(KEY_MANUAL_CLEARED, false)) {
                VehicleIdentity migrated = migrateFromLegacyPrefs();
                if (migrated != null) {
                    save(migrated, VehicleLearningState.LEGACY);
                    return migrated;
                }
            }
            return null;
        }
        int company = prefs.getInt(KEY_COMPANY, VehicleIdentity.inferManufacturerId(signature));
        return new VehicleIdentity(address, name, tail2, tail3, signature, company, learnedAt);
    }

    /** 加载多车车库；首次升级自动将原有单车配置迁移为车库第一台车，保留用户全部已学信息。 */
    synchronized VehicleGarage loadGarage() {
        OfficialBrandPolicy.Brand brandMode = getBrandMode();
        String pinnedId = prefs.getString(KEY_PINNED_PROFILE_ID, "");
        String learningId = prefs.getString(KEY_LEARNING_PROFILE_ID, "");
        String raw = prefs.getString(KEY_GARAGE_JSON, "");
        List<VehicleProfile> profiles = new ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            try {
                JSONArray array = new JSONArray(raw);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    VehicleProfile parsed = decodeProfile(obj);
                    if (parsed != null) profiles.add(parsed);
                }
            } catch (Throwable ignored) {
            }
            return new VehicleGarage(profiles, pinnedId, learningId, brandMode);
        }
        VehicleIdentity legacy = load();
        if (legacy != null) {
            VehicleProfile migrated = new VehicleProfile(
                    "car-1",
                    "",
                    brandMode,
                    "",
                    legacy,
                    getLearningSource(),
                    true
            );
            profiles.add(migrated);
            VehicleGarage garage = new VehicleGarage(profiles, "", "", brandMode);
            saveGarage(garage);
            return garage;
        }
        return new VehicleGarage(profiles, pinnedId, learningId, brandMode);
    }

    /** 保存多车车库，并同步主/首台激活车辆到单车旧字段，保证诊断和升级兼容。 */
    synchronized void saveGarage(VehicleGarage garage) {
        if (garage == null) return;
        JSONArray array = new JSONArray();
        for (VehicleProfile profile : garage.profiles) {
            JSONObject obj = encodeProfile(profile);
            if (obj != null) array.put(obj);
        }
        SharedPreferences.Editor editor = prefs.edit()
                .putString(KEY_GARAGE_JSON, array.toString())
                .putString(KEY_PINNED_PROFILE_ID, garage.pinnedProfileId)
                .putString(KEY_LEARNING_PROFILE_ID, garage.learningProfileId)
                .putString(KEY_BRAND_MODE, garage.brandMode.name());
        VehicleProfile primary = garage.findById(garage.pinnedProfileId);
        if (primary == null) {
            for (VehicleProfile candidate : garage.activeProfiles()) {
                if (candidate.identity != null) {
                    primary = candidate;
                    break;
                }
            }
        }
        if (primary != null && primary.identity != null) {
            writeLegacyIdentityFields(editor, primary.identity, primary.learningSource);
        } else if (garage.profiles.isEmpty()) {
            clearLegacyIdentityFields(editor);
        }
        editor.apply();
    }

    synchronized String nextProfileId() {
        return "car-" + System.currentTimeMillis() + "-" + Integer.toHexString((int) (System.nanoTime() & 0xFFFF));
    }

    synchronized OfficialBrandPolicy.Brand getBrandMode() {
        return OfficialBrandPolicy.parseBrand(prefs.getString(KEY_BRAND_MODE, "AUTO"));
    }

    synchronized void setBrandMode(OfficialBrandPolicy.Brand mode) {
        OfficialBrandPolicy.Brand safe = mode == null ? OfficialBrandPolicy.Brand.AUTO : mode;
        VehicleGarage current = loadGarage();
        saveGarage(new VehicleGarage(current.profiles, current.pinnedProfileId, current.learningProfileId, safe));
    }

    synchronized void setPinnedProfileId(String profileId) {
        VehicleGarage current = loadGarage();
        saveGarage(new VehicleGarage(current.profiles, profileId, "", current.brandMode));
    }

    synchronized void setLearningProfileId(String profileId) {
        VehicleGarage current = loadGarage();
        saveGarage(new VehicleGarage(current.profiles, current.pinnedProfileId, profileId, current.brandMode));
    }

    synchronized VehicleGarage upsertProfile(VehicleProfile profile, boolean pinAsActive, boolean startLearning) {
        if (profile == null) return loadGarage();
        VehicleGarage current = loadGarage();
        List<VehicleProfile> next = new ArrayList<>();
        boolean replaced = false;
        for (VehicleProfile existing : current.profiles) {
            if (existing.id.equals(profile.id)) {
                next.add(profile);
                replaced = true;
            } else {
                next.add(existing);
            }
        }
        if (!replaced) next.add(profile);
        String pinned = pinAsActive ? profile.id : current.pinnedProfileId;
        String learning = startLearning ? profile.id
                : (current.learningProfileId.equals(profile.id) && profile.hasLearnedAddress() ? "" : current.learningProfileId);
        VehicleGarage updated = new VehicleGarage(next, pinned, learning, current.brandMode);
        saveGarage(updated);
        return updated;
    }

    synchronized VehicleGarage removeProfile(String profileId) {
        VehicleGarage current = loadGarage();
        List<VehicleProfile> next = new ArrayList<>();
        for (VehicleProfile existing : current.profiles) {
            if (!existing.id.equals(profileId)) next.add(existing);
        }
        String pinned = current.pinnedProfileId.equals(profileId) ? "" : current.pinnedProfileId;
        String learning = current.learningProfileId.equals(profileId) ? "" : current.learningProfileId;
        VehicleGarage updated = new VehicleGarage(next, pinned, learning, current.brandMode);
        saveGarage(updated);
        return updated;
    }

    private static JSONObject encodeProfile(VehicleProfile profile) {
        if (profile == null || profile.id.isEmpty()) return null;
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", profile.id);
            obj.put("alias", profile.alias);
            obj.put("brand", profile.brand.name());
            obj.put("modelHint", profile.modelHint);
            obj.put("learningSource", profile.learningSource);
            obj.put("enabled", profile.enabled);
            if (profile.identity != null) {
                obj.put("address", profile.identity.address);
                obj.put("name", profile.identity.name);
                obj.put("vinTail2", profile.identity.vinTail2);
                obj.put("vinTail3", profile.identity.vinTail3);
                obj.put("mfgSignature", profile.identity.mfgSignature);
                obj.put("manufacturerId", profile.identity.manufacturerId);
                obj.put("learnedAtMs", profile.identity.learnedAtMs);
            }
            return obj;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static VehicleProfile decodeProfile(JSONObject obj) {
        if (obj == null) return null;
        String id = obj.optString("id", "").trim();
        if (id.isEmpty()) return null;
        String alias = obj.optString("alias", "");
        OfficialBrandPolicy.Brand brand = OfficialBrandPolicy.parseBrand(obj.optString("brand", "AUTO"));
        String modelHint = obj.optString("modelHint", "");
        String source = obj.optString("learningSource", VehicleLearningState.UNKNOWN);
        boolean enabled = obj.optBoolean("enabled", true);
        String address = obj.optString("address", "");
        String name = obj.optString("name", "");
        String tail2 = obj.optString("vinTail2", "");
        String tail3 = obj.optString("vinTail3", "");
        String sig = obj.optString("mfgSignature", "");
        int company = obj.optInt("manufacturerId", VehicleIdentity.inferManufacturerId(sig));
        long learnedAt = obj.optLong("learnedAtMs", 0L);
        VehicleIdentity identity = (address.isEmpty() && name.isEmpty() && tail2.isEmpty() && tail3.isEmpty() && sig.isEmpty())
                ? null
                : new VehicleIdentity(address, name, tail2, tail3, sig, company, learnedAt);
        return new VehicleProfile(id, alias, brand, modelHint, identity, source, enabled);
    }

    /**
     * 从本机旧版存储中自动迁移已保存的车辆信息（不硬编码任何固定值）。
     */
    private VehicleIdentity migrateFromLegacyPrefs() {
        try {
            SharedPreferences legacyRadar = appContext.getSharedPreferences("geely_key_radar_prefs", Context.MODE_PRIVATE);
            SharedPreferences legacyBackup = appContext.getSharedPreferences("vehicle_param_backup_prefs", Context.MODE_PRIVATE);
            String vin = legacyRadar.getString("vin", "");
            if (vin == null || vin.trim().isEmpty()) {
                vin = legacyBackup.getString("orig_vin", "");
            }
            String mac = legacyBackup.getString("orig_ble_mac", "");
            vin = (vin == null) ? "" : vin.trim();
            mac = (mac == null) ? "" : mac.trim();
            String tail3 = vin.length() >= 3 ? vin.substring(vin.length() - 3) : "";
            String tail2 = vin.length() >= 2 ? vin.substring(vin.length() - 2) : "";
            String name = !tail3.isEmpty() ? ("GeelyVehicle" + tail3) : "";
            if (mac.isEmpty() && name.isEmpty() && tail2.isEmpty()) {
                return null;
            }
            return new VehicleIdentity(mac, name, tail2, tail3, "", System.currentTimeMillis());
        } catch (Throwable ignored) {
            return null;
        }
    }

    /** 补全或轮换地址时保留原来源，真实观测入口应显式传入来源。 */
    void save(VehicleIdentity identity) {
        save(identity, getLearningSource());
    }

    /** 将学习来源与身份同时持久化，界面可区分手填、旧配置和实际观测。 */
    synchronized void save(VehicleIdentity identity, String source) {
        if (identity == null) return;
        SharedPreferences.Editor editor = prefs.edit();
        writeLegacyIdentityFields(editor, identity, source);
        editor.apply();
    }

    private static void writeLegacyIdentityFields(SharedPreferences.Editor editor,
                                                  VehicleIdentity identity, String source) {
        editor.putString(KEY_ADDRESS, identity.address)
                .putString(KEY_NAME, identity.name)
                .putString(KEY_TAIL2, identity.vinTail2)
                .putString(KEY_TAIL3, identity.vinTail3)
                .putString(KEY_SIG, identity.mfgSignature)
                .putInt(KEY_COMPANY, identity.manufacturerId)
                .putLong(KEY_LEARNED_AT, identity.learnedAtMs)
                .putString(KEY_LEARNING_SOURCE, source)
                .putBoolean(KEY_MANUAL_CLEARED, false);
    }

    private static void clearLegacyIdentityFields(SharedPreferences.Editor editor) {
        editor.remove(KEY_ADDRESS)
                .remove(KEY_NAME)
                .remove(KEY_TAIL2)
                .remove(KEY_TAIL3)
                .remove(KEY_SIG)
                .remove(KEY_COMPANY)
                .remove(KEY_LEARNED_AT)
                .remove(KEY_LEARNING_SOURCE)
                .putBoolean(KEY_MANUAL_CLEARED, true);
    }

    /** 只清除车辆及学习来源，保留服务开关和距离参数。 */
    synchronized void clear() {
        SharedPreferences.Editor editor = prefs.edit();
        clearLegacyIdentityFields(editor);
        editor.apply();
    }

    /** 兼容旧存储，未知来源不宣称为自动识别。 */
    String getLearningSource() {
        return prefs.getString(KEY_LEARNING_SOURCE, VehicleLearningState.UNKNOWN);
    }

    /** 返回靠近自动解锁开关。 */
    boolean isAutoUnlockEnabled() {
        return prefs.getBoolean("auto_unlock", true);
    }

    /** 返回远离自动上锁开关。 */
    boolean isAutoLockEnabled() {
        return prefs.getBoolean("auto_lock", true);
    }

    /** 保存靠近自动解锁开关，不改变身份。 */
    void setAutoUnlockEnabled(boolean enabled) {
        prefs.edit().putBoolean("auto_unlock", enabled).apply();
    }

    /** 保存远离自动上锁开关，不改变阈值。 */
    void setAutoLockEnabled(boolean enabled) {
        prefs.edit().putBoolean("auto_lock", enabled).apply();
    }

    /** 返回无感服务总开关。 */
    boolean isServiceEnabled() {
        return prefs.getBoolean(KEY_SERVICE_ENABLED, true);
    }

    /** 持久化服务启停状态。 */
    void setServiceEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply();
    }

    /** 返回插线禁控开关。 */
    boolean isBlockWhenChargingEnabled() {
        return prefs.getBoolean(KEY_BLOCK_WHEN_CHARGING, true);
    }

    /** 保存插线禁控选择。 */
    void setBlockWhenChargingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_BLOCK_WHEN_CHARGING, enabled).apply();
    }

    /** 读取原有解锁阈值，升级不重置用户配置。 */
    float getUnlockDbm() {
        return prefs.getFloat(KEY_UNLOCK_DBM, DEFAULT_UNLOCK_DBM);
    }

    /** 读取原有上锁阈值，升级不重置用户配置。 */
    float getLockDbm() {
        return prefs.getFloat(KEY_LOCK_DBM, DEFAULT_LOCK_DBM);
    }

    /** 读取连续远场确认时长。 */
    long getLockDwellMs() {
        return prefs.getLong(KEY_LOCK_DWELL_MS, DEFAULT_LOCK_DWELL_MS);
    }

    /** 读取提前唤醒官方端的普通连接阈值；与解闭锁阈值相互独立。 */
    float getConnectDbm() {
        return prefs.getFloat(KEY_CONNECT_DBM, DEFAULT_CONNECT_DBM);
    }

    /** 判断用户是否保存过自定义触发参数；升级时只读，不写默认值。 */
    boolean hasCustomThresholds() {
        return prefs.contains(KEY_UNLOCK_DBM) || prefs.contains(KEY_LOCK_DBM)
                || prefs.contains(KEY_LOCK_DWELL_MS) || prefs.contains(KEY_CONNECT_DBM);
    }

    /** 官方小组件备用控制默认关闭，必须由用户在设置页显式启用。 */
    boolean isWidgetFallbackEnabled() {
        return prefs.getBoolean(KEY_WIDGET_FALLBACK_ENABLED, false);
    }

    /** 保存官方小组件备用控制开关，不改变四项触发参数。 */
    void setWidgetFallbackEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_WIDGET_FALLBACK_ENABLED, enabled).apply();
    }

    /** 同步持久化四项触发参数，不改变车辆身份或自动动作开关。 */
    void saveThresholds(float unlockDbm, float lockDbm, long lockDwellMs, float connectDbm) {
        prefs.edit()
                .putFloat(KEY_UNLOCK_DBM, unlockDbm)
                .putFloat(KEY_LOCK_DBM, lockDbm)
                .putLong(KEY_LOCK_DWELL_MS, lockDwellMs)
                .putFloat(KEY_CONNECT_DBM, connectDbm)
                .apply();
    }
}
