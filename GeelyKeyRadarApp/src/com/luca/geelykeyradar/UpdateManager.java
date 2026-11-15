package com.luca.geelykeyradar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GitHub Releases 自动更新。
 *
 * 自动检查和下载在现有前台服务/界面后台线程执行；下载完成后发通知。
 * Android 不允许普通应用静默覆盖安装，最后一步始终由系统安装器确认。
 */
final class UpdateManager {
    private static final String TAG = "GeelyUpdate";
    private static final String UPDATE_MANIFEST_URL =
            "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest/download/update.json";
    private static final String PREFS = "app_update_prefs";
    private static final String UPDATE_DIR = "updates";
    private static final String KEY_AUTO_ENABLED = "auto_enabled";
    private static final String KEY_LAST_ATTEMPT_MS = "last_attempt_ms";
    private static final String KEY_LAST_SUCCESS_MS = "last_success_ms";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DOWNLOADED_PATH = "downloaded_path";
    private static final String KEY_DOWNLOADED_CODE = "downloaded_code";
    private static final String KEY_DOWNLOADED_NAME = "downloaded_name";
    private static final String KEY_DOWNLOADED_DIGEST = "downloaded_digest";
    private static final String CHANNEL_ID = "app_update_channel";
    private static final int NOTIFICATION_ID = 2028;
    static final String EXTRA_OPEN_UPDATE = "com.luca.geelykeyradar.OPEN_UPDATE";
    static final String ACTION_OPEN_UPDATE = "com.luca.geelykeyradar.action.OPEN_UPDATE";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "geely-update");
        thread.setDaemon(true);
        return thread;
    });
    private static final AtomicBoolean BUSY = new AtomicBoolean(false);
    private static volatile boolean checking;
    private static volatile boolean downloading;
    private static volatile String runtimeStatus = "";

    static final class Snapshot {
        final boolean autoEnabled;
        final boolean checking;
        final boolean downloading;
        final boolean updateReady;
        final int downloadedVersionCode;
        final String downloadedVersionName;
        final long lastCheckAtMs;
        final String status;

        Snapshot(boolean autoEnabled, boolean checking, boolean downloading, boolean updateReady,
                 int downloadedVersionCode, String downloadedVersionName, long lastCheckAtMs, String status) {
            this.autoEnabled = autoEnabled;
            this.checking = checking;
            this.downloading = downloading;
            this.updateReady = updateReady;
            this.downloadedVersionCode = downloadedVersionCode;
            this.downloadedVersionName = downloadedVersionName;
            this.lastCheckAtMs = lastCheckAtMs;
            this.status = status;
        }
    }

    private static final class UpdateException extends Exception {
        UpdateException(String message) {
            super(message);
        }

        UpdateException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private UpdateManager() {
    }

    static boolean isAutoEnabled(Context context) {
        return context != null && prefs(context).getBoolean(KEY_AUTO_ENABLED, true);
    }

    static void setAutoEnabled(Context context, boolean enabled) {
        if (context == null) return;
        prefs(context).edit().putBoolean(KEY_AUTO_ENABLED, enabled).apply();
        setStatus(context, enabled
                ? "自动检查已开启，有新版本时会自动下载"
                : "自动检查已关闭，仍可手动检查更新");
        DiagnosticLog.event("Update", enabled ? "AUTO_ENABLED" : "AUTO_DISABLED", "");
    }

    static Snapshot snapshot(Context context) {
        Context app = context == null ? null : context.getApplicationContext();
        boolean auto = isAutoEnabled(app);
        File downloaded = downloadedFile(app);
        int downloadedCode = app == null ? 0 : prefs(app).getInt(KEY_DOWNLOADED_CODE, 0);
        String downloadedName = app == null ? "" : prefs(app).getString(KEY_DOWNLOADED_NAME, "");
        int installedCode = installedVersionCode(app);
        boolean ready = downloaded != null && downloaded.isFile() && downloadedCode > installedCode;
        String status;
        if (downloadedCode > 0 && downloadedCode <= installedCode) {
            status = "已安装 v" + installedVersionName(app) + "（构建 " + installedCode + "）\n"
                    + (auto ? "自动检查：每 1 小时" : "自动检查：关闭");
        } else {
            status = statusText(app, ready, downloadedName, downloadedCode);
        }
        long last = app == null ? 0L : prefs(app).getLong(KEY_LAST_ATTEMPT_MS, 0L);
        return new Snapshot(auto, checking, downloading, ready, downloadedCode,
                downloadedName == null ? "" : downloadedName, last, status);
    }

    /** 前台服务和主页共用的小时检查入口；失败/无网络不占满一小时窗口。 */
    static void maybeAutoCheck(Context context) {
        if (context == null || !isAutoEnabled(context)) return;
        Context app = context.getApplicationContext();
        long now = System.currentTimeMillis();
        long last = prefs(app).getLong(KEY_LAST_ATTEMPT_MS, 0L);
        if (!UpdatePolicy.isCheckDue(now, last, true)) return;
        if (!hasNetwork(app)) {
            setStatus(app, "等待网络：自动更新将在网络恢复后重试");
            return;
        }
        startCheck(app, false);
    }

    /** 用户主动检查始终绕过一小时窗口；返回false表示已有任务在运行。 */
    static boolean checkNow(Context context, boolean manual) {
        if (context == null) return false;
        Context app = context.getApplicationContext();
        if (!hasNetwork(app)) {
            setStatus(app, "当前没有可用网络，无法检查 GitHub Releases");
            return false;
        }
        return startCheck(app, manual);
    }

    static boolean hasDownloadedUpdate(Context context) {
        return snapshot(context).updateReady;
    }

    static int downloadedVersionCode(Context context) {
        return snapshot(context).downloadedVersionCode;
    }

    static String downloadedVersionName(Context context) {
        return snapshot(context).downloadedVersionName;
    }

    static boolean canInstallPackages(Context context) {
        if (context == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true;
        return context.getPackageManager().canRequestPackageInstalls();
    }

    static Intent installPermissionIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        return intent;
    }

    /** 打开系统安装器；返回false时由界面提示权限或安装器异常。 */
    static boolean openInstaller(Context context) {
        if (context == null || !hasDownloadedUpdate(context) || !canInstallPackages(context)) return false;
        Context app = context.getApplicationContext();
        File apk = downloadedFile(app);
        if (apk == null || !apk.isFile()) return false;
        String authority = app.getPackageName() + ".updates";
        Uri uri = Uri.parse("content://" + authority + "/" + Uri.encode(apk.getName()));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClipData(ClipData.newRawUri("update.apk", uri));
        try {
            app.startActivity(intent);
            clearNotification(app);
            DiagnosticLog.event("Update", "INSTALLER_OPENED",
                    "version_code=" + prefs(app).getInt(KEY_DOWNLOADED_CODE, 0));
            return true;
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Update", "INSTALLER_OPEN_ERROR", error);
            return false;
        }
    }

    private static boolean startCheck(Context app, boolean manual) {
        if (!BUSY.compareAndSet(false, true)) return false;
        checking = true;
        downloading = false;
        long now = System.currentTimeMillis();
        prefs(app).edit().putLong(KEY_LAST_ATTEMPT_MS, now).apply();
        setStatus(app, manual ? "正在手动检查 GitHub Releases…" : "正在进行每小时自动检查…");
        DiagnosticLog.event("Update", "CHECK_START", "manual=" + manual);
        EXECUTOR.execute(() -> runCheck(app, manual));
        return true;
    }

    private static void runCheck(Context app, boolean manual) {
        try {
            UpdatePolicy.Asset asset = fetchLatestAsset(app);
            int installedCode = installedVersionCode(app);
            if (!UpdatePolicy.isNewer(asset.versionCode, installedCode)) {
                clearDownloaded(app);
                clearNotification(app);
                setStatus(app, "已是最新版本：v" + installedVersionName(app)
                        + "（构建 " + installedCode + "）");
                prefs(app).edit().putLong(KEY_LAST_SUCCESS_MS, System.currentTimeMillis()).apply();
                DiagnosticLog.event("Update", "NO_UPDATE", "installed_code=" + installedCode
                        + " remote_code=" + asset.versionCode);
                return;
            }

            File existing = downloadedFile(app);
            int existingCode = prefs(app).getInt(KEY_DOWNLOADED_CODE, 0);
            if (existing != null && existing.isFile() && existingCode == asset.versionCode) {
                setStatus(app, "v" + asset.versionName + " 已下载，等待安装");
                showUpdateNotification(app, asset.versionName, asset.versionCode);
                return;
            }

            downloading = true;
            setStatus(app, "发现新版本 v" + asset.versionName + "，正在下载…");
            DiagnosticLog.event("Update", "UPDATE_FOUND", "version_code=" + asset.versionCode
                    + " size=" + asset.size + " manual=" + manual);
            File target = downloadAsset(app, asset);
            downloading = false;
            verifyDownloadedApk(app, target, asset);
            prefs(app).edit()
                    .putString(KEY_DOWNLOADED_PATH, target.getAbsolutePath())
                    .putInt(KEY_DOWNLOADED_CODE, asset.versionCode)
                    .putString(KEY_DOWNLOADED_NAME, asset.versionName)
                    .putString(KEY_DOWNLOADED_DIGEST, asset.digest)
                    .putLong(KEY_LAST_SUCCESS_MS, System.currentTimeMillis())
                    .apply();
            cleanupOldDownloads(app, target);
            setStatus(app, "v" + asset.versionName + " 已下载，点“安装更新”打开系统安装器");
            showUpdateNotification(app, asset.versionName, asset.versionCode);
            DiagnosticLog.event("Update", "DOWNLOAD_VERIFIED", "version_code=" + asset.versionCode
                    + " bytes=" + target.length());
        } catch (Throwable error) {
            downloading = false;
            String message = friendlyError(error);
            setStatus(app, message);
            DiagnosticLog.bridgeError("Update", "CHECK_OR_DOWNLOAD_ERROR", error);
            Log.w(TAG, message, error);
        } finally {
            checking = false;
            downloading = false;
            BUSY.set(false);
        }
    }

    private static UpdatePolicy.Asset fetchLatestAsset(Context app) throws UpdateException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(UPDATE_MANIFEST_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "GeelyPassiveKey/" + installedVersionName(app));
            int code = connection.getResponseCode();
            if (code == 404) {
                throw new UpdateException("最新 Release 尚未上传 update.json，请等待 CI 完成");
            }
            if (code != 200) {
                throw new UpdateException("更新清单返回 HTTP " + code);
            }
            String body = readText(connection.getInputStream(), 2 * 1024 * 1024);
            JSONObject manifest = new JSONObject(body);
            UpdatePolicy.Asset asset = UpdatePolicy.parseAsset(
                    manifest.optString("apkName", ""),
                    manifest.optString("apkUrl", ""),
                    manifest.optString("sha256", ""),
                    manifest.optLong("size", 0L));
            if (asset == null) {
                throw new UpdateException("update.json 中的 APK 名称或下载地址无效");
            }
            String manifestVersion = manifest.optString("versionName", "");
            int manifestCode = manifest.optInt("versionCode", 0);
            if (!asset.versionName.equals(manifestVersion) || asset.versionCode != manifestCode) {
                throw new UpdateException("update.json 版本信息与 APK 文件名不一致");
            }
            if (!asset.downloadUrl.startsWith(
                    "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/")) {
                throw new UpdateException("update.json 下载地址不属于本项目 GitHub Release");
            }
            return asset;
        } catch (UpdateException error) {
            throw error;
        } catch (Throwable error) {
            throw new UpdateException("检查更新失败：" + errorSummary(error), error);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private static File downloadAsset(Context app, UpdatePolicy.Asset asset) throws UpdateException {
        if (asset.size > UpdatePolicy.MAX_APK_BYTES) {
            throw new UpdateException("更新包超过 50 MiB，已拒绝下载");
        }
        File dir = updateDir(app);
        if (!dir.isDirectory() && !dir.mkdirs()) {
            throw new UpdateException("无法创建更新缓存目录");
        }
        String safeName = UpdatePolicy.safeFileName(asset.name);
        File target = new File(dir, safeName);
        File part = new File(dir, safeName + ".part");
        if (part.exists() && !part.delete()) throw new UpdateException("无法清理旧下载临时文件");
        HttpURLConnection connection = null;
        long total = 0L;
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (Exception unavailable) {
            throw new UpdateException("设备不支持 SHA-256 校验", unavailable);
        }
        try {
            URL url = new URL(asset.downloadUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "GeelyPassiveKey/" + installedVersionName(app));
            int code = connection.getResponseCode();
            if (code != 200) throw new UpdateException("APK 下载返回 HTTP " + code);
            try (InputStream input = new BufferedInputStream(connection.getInputStream());
                 OutputStream output = new BufferedOutputStream(new FileOutputStream(part))) {
                byte[] buffer = new byte[32768];
                int count;
                while ((count = input.read(buffer)) != -1) {
                    total += count;
                    if (total > UpdatePolicy.MAX_APK_BYTES) {
                        throw new UpdateException("下载包超过 50 MiB，已中止");
                    }
                    output.write(buffer, 0, count);
                    sha256.update(buffer, 0, count);
                }
            }
            if (asset.size > 0L && total != asset.size) {
                throw new UpdateException("APK 大小不一致：期望 " + asset.size + "，实际 " + total);
            }
            String digest = toHex(sha256.digest());
            if (!UpdatePolicy.digestMatches(asset.digest, digest)) {
                throw new UpdateException("APK SHA-256 与 GitHub Release 不一致");
            }
            if (target.exists() && !target.delete()) throw new UpdateException("无法替换旧更新文件");
            if (!part.renameTo(target)) throw new UpdateException("无法保存下载的 APK");
            return target;
        } catch (UpdateException error) {
            if (part.exists()) part.delete();
            throw error;
        } catch (Throwable error) {
            if (part.exists()) part.delete();
            throw new UpdateException("下载更新失败：" + errorSummary(error), error);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    /** 包名、构建号和签名必须与当前应用完全一致，防止被替换成其他 APK。 */
    private static void verifyDownloadedApk(Context app, File file, UpdatePolicy.Asset asset) throws UpdateException {
        PackageManager manager = app.getPackageManager();
        PackageInfo installed;
        PackageInfo archive;
        try {
            installed = manager.getPackageInfo(app.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
            archive = manager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_SIGNING_CERTIFICATES);
        } catch (Throwable error) {
            throw new UpdateException("无法读取更新包信息", error);
        }
        if (archive == null || archive.packageName == null || archive.applicationInfo == null) {
            throw new UpdateException("下载文件不是有效 APK");
        }
        if (!app.getPackageName().equals(archive.packageName)) {
            throw new UpdateException("更新包包名与当前应用不一致");
        }
        long archiveCode = Build.VERSION.SDK_INT >= 28 ? archive.getLongVersionCode() : archive.versionCode;
        if (archiveCode != asset.versionCode) {
            throw new UpdateException("APK 构建号与 Release 文件名不一致");
        }
        String installedSigners = signerDigest(installed);
        String archiveSigners = signerDigest(archive);
        if (installedSigners.isEmpty() || archiveSigners.isEmpty() || !installedSigners.equals(archiveSigners)) {
            throw new UpdateException("更新包签名与当前应用不一致，已拒绝安装");
        }
    }

    private static String signerDigest(PackageInfo info) {
        if (info == null || info.signingInfo == null) return "";
        Signature[] signatures = info.signingInfo.hasMultipleSigners()
                ? info.signingInfo.getApkContentsSigners()
                : info.signingInfo.getSigningCertificateHistory();
        if (signatures == null || signatures.length == 0) return "";
        Set<String> digests = new TreeSet<>();
        for (Signature signature : signatures) {
            if (signature == null) continue;
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digests.add(toHex(digest.digest(signature.toByteArray())));
            } catch (Exception ignored) {
            }
        }
        return String.join(",", digests);
    }

    private static String statusText(Context app, boolean ready, String downloadedName, int downloadedCode) {
        if (checking) return runtimeStatus.isEmpty() ? "正在检查 GitHub Releases…" : runtimeStatus;
        if (downloading) return runtimeStatus.isEmpty() ? "正在下载更新…" : runtimeStatus;
        if (ready) return "新版本 v" + downloadedName + "（构建 " + downloadedCode
                + "）已下载，等待系统安装确认";
        String stored = app == null ? "" : prefs(app).getString(KEY_STATUS, "");
        boolean auto = isAutoEnabled(app);
        String schedule = auto ? "自动检查：每 1 小时" : "自动检查：关闭";
        return (stored == null || stored.isEmpty() ? "尚未检查更新" : stored) + "\n" + schedule;
    }

    private static void setStatus(Context context, String status) {
        if (context == null) return;
        runtimeStatus = status == null ? "" : status;
        prefs(context).edit().putString(KEY_STATUS, runtimeStatus).apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private static File updateDir(Context context) {
        return new File(context.getCacheDir(), UPDATE_DIR);
    }

    private static File downloadedFile(Context context) {
        if (context == null) return null;
        String path = prefs(context).getString(KEY_DOWNLOADED_PATH, "");
        return path == null || path.isEmpty() ? null : new File(path);
    }

    private static void clearDownloaded(Context context) {
        File file = downloadedFile(context);
        if (file != null && file.exists()) file.delete();
        prefs(context).edit()
                .remove(KEY_DOWNLOADED_PATH)
                .remove(KEY_DOWNLOADED_CODE)
                .remove(KEY_DOWNLOADED_NAME)
                .remove(KEY_DOWNLOADED_DIGEST)
                .apply();
    }

    private static void cleanupOldDownloads(Context context, File keep) {
        File[] files = updateDir(context).listFiles();
        if (files == null) return;
        String keepPath = keep == null ? "" : keep.getAbsolutePath();
        for (File file : files) {
            if (!file.getAbsolutePath().equals(keepPath)) file.delete();
        }
    }

    private static int installedVersionCode(Context context) {
        if (context == null) return 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return Build.VERSION.SDK_INT >= 28 ? (int) info.getLongVersionCode() : info.versionCode;
        } catch (Throwable ignored) {
            return 0;
        }
    }

    private static String installedVersionName(Context context) {
        if (context == null) return "unknown";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName == null ? "unknown" : info.versionName;
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private static boolean hasNetwork(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return false;
            Network network = manager.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            return capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static String readText(InputStream input, int limit) throws IOException {
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = stream.read(buffer)) != -1) {
                if (output.size() + count > limit) throw new IOException("GitHub 响应过大");
                output.write(buffer, 0, count);
            }
            return output.toString(StandardCharsets.UTF_8.name());
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder value = new StringBuilder(bytes.length * 2);
        for (byte item : bytes) value.append(String.format(Locale.ROOT, "%02x", item & 0xff));
        return value.toString();
    }

    private static String friendlyError(Throwable error) {
        String message = error == null ? "" : error.getMessage();
        if (message == null || message.trim().isEmpty()) message = errorSummary(error);
        if (message.contains("UnknownHost") || message.contains("Unable to resolve host")) {
            return "无法连接 GitHub，请检查网络后重试";
        }
        return message.length() > 180 ? message.substring(0, 180) : message;
    }

    private static String errorSummary(Throwable error) {
        return error == null ? "未知错误" : error.getClass().getSimpleName()
                + (error.getMessage() == null ? "" : ": " + error.getMessage());
    }

    private static void showUpdateNotification(Context context, String versionName, int versionCode) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "应用更新",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("检查并下载银河无感钥匙新版本");
                manager.createNotificationChannel(channel);
            }
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(ACTION_OPEN_UPDATE);
            intent.putExtra(EXTRA_OPEN_UPDATE, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pending = PendingIntent.getActivity(context, NOTIFICATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                    ? new Notification.Builder(context, CHANNEL_ID)
                    : new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("银河无感钥匙 v" + versionName)
                    .setContentText("新版本已下载，点此打开系统安装器")
                    .setStyle(new Notification.BigTextStyle().bigText(
                            "v" + versionName + "（构建 " + versionCode
                                    + "）已下载并完成 SHA-256、包名和签名校验。点此安装。"))
                    .setContentIntent(pending)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
            manager.notify(NOTIFICATION_ID, builder.build());
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Update", "NOTIFY_ERROR", error);
        }
    }

    private static void clearNotification(Context context) {
        try {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.cancel(NOTIFICATION_ID);
        } catch (Throwable ignored) {
        }
    }
}
