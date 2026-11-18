package com.luca.geelykeyradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;


/**
 * 本机固定日志UID。
 *
 * 使用 Android ID 与应用签名/用户范围绑定，再混入硬件型号字段做应用命名空间哈希；
 * 不读取IMEI、序列号、手机号，也不把 Android ID 原文写入日志或上传。
 */
final class DeviceUid {
    private static final String PREFS = "device_uid_prefs";
    private static final String KEY_UID = "device_uid";
    private static final String KEY_FALLBACK = "fallback_random";

    private DeviceUid() {
    }

    static String get(Context context) {
        Context app = context.getApplicationContext();
        SharedPreferences prefs = app.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String cached = prefs.getString(KEY_UID, "");
        if (valid(cached)) return cached;
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (RuntimeException ignored) {
        }
        String fallback = prefs.getString(KEY_FALLBACK, "");
        if (androidId == null || androidId.trim().isEmpty()) {
            if (fallback == null || fallback.isEmpty()) {
                fallback = LogUploadPolicy.newInstallId();
                prefs.edit().putString(KEY_FALLBACK, fallback).apply();
            }
            androidId = "fallback:" + fallback;
        }
        String material = "GeelyPassiveKey|" + app.getPackageName() + "|" + androidId
                + "|" + Build.MANUFACTURER + "|" + Build.BRAND + "|" + Build.MODEL
                + "|" + Build.DEVICE + "|" + Build.HARDWARE + "|" + Build.BOARD
                + "|" + socFields();
        String uid = derive(material);
        prefs.edit().putString(KEY_UID, uid).apply();
        return uid;
    }

    private static String socFields() {
        if (Build.VERSION.SDK_INT < 31) return "unknown|unknown";
        return Build.SOC_MANUFACTURER + "|" + Build.SOC_MODEL;
    }

    /** 只输出哈希后的公开UID，不保留 Android ID 或硬件原文。 */
    static String derive(String material) {
        return LogUploadPolicy.deriveDeviceUid(material);
    }

    static boolean valid(String value) {
        return value != null && value.matches("GPK-[0-9A-F]{4}(?:-[0-9A-F]{4}){4}");
    }
}
