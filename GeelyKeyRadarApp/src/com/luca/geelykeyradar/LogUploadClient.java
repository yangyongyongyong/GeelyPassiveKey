package com.luca.geelykeyradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

/** 用户明确点击后才上传脱敏诊断ZIP；不联网检查日志、不自动上传。 */
final class LogUploadClient {
    static final String ENDPOINT = "https://geelylog.mq.lucadesign.uk/v1/logs";
    private static final String PREFS = "log_upload_prefs";
    private static final String KEY_INSTALL_ID = "install_id";
    private static final String KEY_LAST_ID = "last_log_id";
    private static final String KEY_LAST_AT = "last_upload_at";
    private static final String KEY_LAST_STATUS = "last_status";
    private static final int CONNECT_TIMEOUT_MS = 15_000;
    private static final int READ_TIMEOUT_MS = 30_000;

    static final class Result {
        final boolean ok;
        final String logId;
        final String message;
        final int httpStatus;

        Result(boolean ok, String logId, String message, int httpStatus) {
            this.ok = ok;
            this.logId = logId == null ? "" : logId;
            this.message = message == null ? "" : message;
            this.httpStatus = httpStatus;
        }
    }

    private LogUploadClient() {
    }

    static String installId(Context context) {
        SharedPreferences prefs = prefs(context);
        String current = prefs.getString(KEY_INSTALL_ID, "");
        if (LogUploadPolicy.validInstallId(current)) return current;
        String created = LogUploadPolicy.newInstallId();
        prefs.edit().putString(KEY_INSTALL_ID, created).apply();
        return created;
    }

    static String lastLogId(Context context) {
        return prefs(context).getString(KEY_LAST_ID, "");
    }

    static long lastUploadAt(Context context) {
        return prefs(context).getLong(KEY_LAST_AT, 0L);
    }

    static String lastStatus(Context context) {
        return prefs(context).getString(KEY_LAST_STATUS, "");
    }

    /** 在后台线程调用；读取用户明确选择的ZIP并完成一次HTTP请求。 */
    static Result upload(Context context, File archive, String note) {
        if (context == null || archive == null || !archive.isFile()) {
            return new Result(false, "", "日志文件不存在", 0);
        }
        try {
            byte[] bytes = readFile(archive);
            LogUploadPolicy.validateArchive(bytes);
            String sha256 = sha256(bytes);
            String appVersion = "unknown";
            int versionCode = 0;
            try {
                android.content.pm.PackageInfo info = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0);
                appVersion = info.versionName;
                versionCode = (int) info.getLongVersionCode();
            } catch (Exception ignored) {
            }
            String deviceUid = DeviceUid.get(context);
            String json = LogUploadPolicy.buildRequestJson(installId(context), deviceUid,
                    note, appVersion, versionCode, Build.VERSION.RELEASE, Build.BRAND, Build.MODEL,
                    archive.getName(), bytes, sha256);
            byte[] body = json.getBytes(StandardCharsets.UTF_8);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(ENDPOINT).openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
                connection.setReadTimeout(READ_TIMEOUT_MS);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "GeelyPassiveKey/" + appVersion);
                connection.setFixedLengthStreamingMode(body.length);
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(body);
                }
                int status = connection.getResponseCode();
                String response = readResponse(status >= 200 && status < 300
                        ? connection.getInputStream() : connection.getErrorStream());
                JSONObject parsed = response.isEmpty() ? new JSONObject() : new JSONObject(response);
                if (status >= 200 && status < 300 && parsed.optBoolean("ok", false)) {
                    String id = parsed.optString("logId", "");
                    String returnedUid = parsed.optString("deviceUid", deviceUid);
                    String message = "日志已上传\nUID：" + returnedUid + "\n编号：" + id
                            + "\n保留 " + parsed.optInt("retentionDays", 90) + " 天";
                    saveSuccess(context, id, message);
                    DiagnosticLog.event("Upload", "UPLOAD_SENT", "log_id=" + id
                            + " device_uid=" + returnedUid + " bytes=" + bytes.length + " http=" + status);
                    return new Result(true, id, message, status);
                }
                String code = parsed.optString("code", "HTTP_" + status);
                String serverMessage = parsed.optString("message", "上传失败");
                String message = friendlyError(status, code, serverMessage);
                saveFailure(context, message);
                DiagnosticLog.event("Upload", "UPLOAD_REJECTED", "http=" + status
                        + " code=" + LogUploadPolicy.sanitizeMeta(code)
                        + " bytes=" + bytes.length);
                return new Result(false, "", message, status);
            } finally {
                if (connection != null) connection.disconnect();
            }
        } catch (IllegalArgumentException invalid) {
            saveFailure(context, invalid.getMessage());
            DiagnosticLog.event("Upload", "UPLOAD_LOCAL_INVALID", "reason="
                    + LogUploadPolicy.sanitizeMeta(invalid.getMessage()));
            return new Result(false, "", invalid.getMessage(), 0);
        } catch (Throwable error) {
            String reason = DiagnosticText.errorSummary(error);
            saveFailure(context, "网络或服务暂不可用，请稍后重试");
            DiagnosticLog.bridgeError("Upload", "UPLOAD_FAILED", error);
            return new Result(false, "", "网络或服务暂不可用，请稍后重试\n" + reason, 0);
        }
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private static void saveSuccess(Context context, String id, String message) {
        prefs(context).edit()
                .putString(KEY_LAST_ID, id)
                .putLong(KEY_LAST_AT, System.currentTimeMillis())
                .putString(KEY_LAST_STATUS, message)
                .apply();
    }

    private static void saveFailure(Context context, String message) {
        prefs(context).edit()
                .remove(KEY_LAST_ID)
                .putLong(KEY_LAST_AT, System.currentTimeMillis())
                .putString(KEY_LAST_STATUS, "上次上传失败：" + message)
                .apply();
    }

    private static String friendlyError(int status, String code, String serverMessage) {
        if (status == 429) return "今天上传次数过多，请明天再试";
        if (status == 413) return "日志包超过服务端限制（1.8 MB）";
        if (status == 401 || status == 403) return "上传服务拒绝请求";
        if (status >= 500) return "上传服务暂不可用，请稍后重试";
        return "上传失败：" + serverMessage + " (" + code + ")";
    }

    private static byte[] readFile(File file) throws Exception {
        try (InputStream input = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream output = new ByteArrayOutputStream((int) Math.min(file.length(), 2_000_000))) {
            byte[] buffer = new byte[16384];
            int count;
            while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
            return output.toByteArray();
        }
    }

    private static String readResponse(InputStream input) throws Exception {
        if (input == null) return "";
        try (InputStream stream = input;
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int total = 0;
            int count;
            while ((count = stream.read(buffer)) != -1 && total < 65536) {
                int accepted = Math.min(count, 65536 - total);
                output.write(buffer, 0, accepted);
                total += accepted;
            }
            return output.toString(StandardCharsets.UTF_8.name());
        }
    }

    private static String sha256(byte[] data) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(data);
        StringBuilder output = new StringBuilder(64);
        for (byte value : digest) output.append(String.format(Locale.ROOT, "%02x", value & 0xFF));
        return output.toString();
    }
}
