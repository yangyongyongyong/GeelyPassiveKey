package com.luca.geelykeyradar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本车蓝牙身份的本地持久化。
 *
 * 只保存广播里能观察到的信息，不保存 VIN 全码、账号、令牌或任何吉利私有数据。
 */
final class VehicleIdentityStore {

    private static final String PREFS = "passive_key_prefs";
    private static final String KEY_ADDRESS = "vehicle_address";
    private static final String KEY_NAME = "vehicle_name";
    private static final String KEY_TAIL2 = "vehicle_vin_tail2";
    private static final String KEY_TAIL3 = "vehicle_vin_tail3";
    private static final String KEY_SIG = "vehicle_mfg_signature";
    private static final String KEY_LEARNED_AT = "vehicle_learned_at";
    private static final String KEY_MANUAL_CLEARED = "vehicle_manual_cleared";
    private static final String KEY_LEARNING_SOURCE = "vehicle_learning_source";
    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    private static final String KEY_BLOCK_WHEN_CHARGING = "block_control_when_charging";
    private static final String KEY_UNLOCK_DBM = "unlock_dbm";
    private static final String KEY_LOCK_DBM = "lock_dbm";
    private static final String KEY_LOCK_DWELL_MS = "lock_dwell_ms";

    static final float DEFAULT_UNLOCK_DBM = -75.0f; // 实车验证最佳提前解锁阈值
    static final float DEFAULT_LOCK_DBM = -82.0f;   // 约 7~8 米
    static final long DEFAULT_LOCK_DWELL_MS = 4500L;

    private final Context appContext;
    private final SharedPreferences prefs;

    VehicleIdentityStore(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = this.appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /**
     * 加载已学习的本车蓝牙身份。
     * 若新版存储为空且用户未主动清除，则尝试从本机旧版 SharedPreferences 自动迁移。
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
        return new VehicleIdentity(address, name, tail2, tail3, signature, learnedAt);
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
    void save(VehicleIdentity identity, String source) {
        if (identity == null) return;
        prefs.edit()
                .putString(KEY_ADDRESS, identity.address)
                .putString(KEY_NAME, identity.name)
                .putString(KEY_TAIL2, identity.vinTail2)
                .putString(KEY_TAIL3, identity.vinTail3)
                .putString(KEY_SIG, identity.mfgSignature)
                .putLong(KEY_LEARNED_AT, identity.learnedAtMs)
                .putString(KEY_LEARNING_SOURCE, source)
                .putBoolean(KEY_MANUAL_CLEARED, false)
                .apply();
    }

    /** 只清除车辆及学习来源，保留服务开关和距离参数。 */
    void clear() {
        prefs.edit()
                .remove(KEY_ADDRESS)
                .remove(KEY_NAME)
                .remove(KEY_TAIL2)
                .remove(KEY_TAIL3)
                .remove(KEY_SIG)
                .remove(KEY_LEARNED_AT)
                .remove(KEY_LEARNING_SOURCE)
                .putBoolean(KEY_MANUAL_CLEARED, true)
                .apply();
    }

    /** 兼容旧存储，未知来源不宣称为自动识别。 */
    String getLearningSource() {
        return prefs.getString(KEY_LEARNING_SOURCE, VehicleLearningState.UNKNOWN);
    }

    boolean isAutoUnlockEnabled() {
        return prefs.getBoolean("auto_unlock", true);
    }

    boolean isAutoLockEnabled() {
        return prefs.getBoolean("auto_lock", true);
    }

    void setAutoUnlockEnabled(boolean enabled) {
        prefs.edit().putBoolean("auto_unlock", enabled).apply();
    }

    void setAutoLockEnabled(boolean enabled) {
        prefs.edit().putBoolean("auto_lock", enabled).apply();
    }

    boolean isServiceEnabled() {
        return prefs.getBoolean(KEY_SERVICE_ENABLED, true);
    }

    void setServiceEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply();
    }

    boolean isBlockWhenChargingEnabled() {
        return prefs.getBoolean(KEY_BLOCK_WHEN_CHARGING, true);
    }

    void setBlockWhenChargingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_BLOCK_WHEN_CHARGING, enabled).apply();
    }

    float getUnlockDbm() {
        return prefs.getFloat(KEY_UNLOCK_DBM, DEFAULT_UNLOCK_DBM);
    }

    float getLockDbm() {
        return prefs.getFloat(KEY_LOCK_DBM, DEFAULT_LOCK_DBM);
    }

    long getLockDwellMs() {
        return prefs.getLong(KEY_LOCK_DWELL_MS, DEFAULT_LOCK_DWELL_MS);
    }

    void saveThresholds(float unlockDbm, float lockDbm, long lockDwellMs) {
        prefs.edit()
                .putFloat(KEY_UNLOCK_DBM, unlockDbm)
                .putFloat(KEY_LOCK_DBM, lockDbm)
                .putLong(KEY_LOCK_DWELL_MS, lockDwellMs)
                .apply();
    }
}
