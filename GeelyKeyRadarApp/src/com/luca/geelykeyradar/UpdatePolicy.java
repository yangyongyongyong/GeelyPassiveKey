package com.luca.geelykeyradar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 自动更新中不依赖 Android 的版本、周期、文件名与摘要规则。 */
final class UpdatePolicy {
    static final long CHECK_INTERVAL_MS = 60L * 60L * 1000L;
    static final int MAX_APK_BYTES = 50 * 1024 * 1024;
    static final String CLOUDFLARE_ORIGIN = "https://geelyupdate.mq.lucadesign.uk";
    static final String CLOUDFLARE_MANIFEST_URL = CLOUDFLARE_ORIGIN + "/update.json?refresh=1";
    static final String GITHUB_MANIFEST_URL =
            "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest/download/update.json";
    static final String GITHUB_APK_PREFIX =
            "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/";

    private static final Pattern VERSIONED_APK = Pattern.compile(
            "^GeelyPassiveKey-v(.+)-(\\d+)\\.apk$", Pattern.CASE_INSENSITIVE);

    static final class Asset {
        final String name;
        final String versionName;
        final int versionCode;
        final String downloadUrl;
        final String digest;
        final long size;

        Asset(String name, String versionName, int versionCode,
              String downloadUrl, String digest, long size) {
            this.name = name;
            this.versionName = versionName;
            this.versionCode = versionCode;
            this.downloadUrl = downloadUrl;
            this.digest = digest;
            this.size = size;
        }
    }

    private UpdatePolicy() {
    }

    /** 只接受 GitHub Actions 产生的版本化 APK 名称。 */
    static Asset parseAsset(String name, String downloadUrl, String digest, long size) {
        if (name == null || downloadUrl == null || downloadUrl.trim().isEmpty()) return null;
        Matcher matcher = VERSIONED_APK.matcher(name.trim());
        if (!matcher.matches()) return null;
        String versionName = matcher.group(1);
        int versionCode;
        try {
            versionCode = Integer.parseInt(matcher.group(2));
        } catch (NumberFormatException invalid) {
            return null;
        }
        if (versionName.isEmpty() || versionCode <= 0) return null;
        return new Asset(name.trim(), versionName, versionCode, downloadUrl.trim(),
                digest == null ? "" : digest.trim(), Math.max(0L, size));
    }

    /** 只允许项目 Cloudflare 镜像和本项目 GitHub Release 的版本化 APK 地址。 */
    static boolean isAllowedAssetLocation(Asset asset) {
        if (asset == null) return false;
        String cloudflare = CLOUDFLARE_ORIGIN + "/releases/" + asset.versionName + "/" + asset.name;
        String github = GITHUB_APK_PREFIX + "v" + asset.versionName + "/" + asset.name;
        return asset.downloadUrl.equals(cloudflare) || asset.downloadUrl.equals(github);
    }

    static boolean isAllowedManifestUrl(String url) {
        return CLOUDFLARE_MANIFEST_URL.equals(url) || GITHUB_MANIFEST_URL.equals(url);
    }

    static boolean isNewer(int remoteVersionCode, int installedVersionCode) {
        return remoteVersionCode > installedVersionCode;
    }

    /** 多渠道清单取最高构建号，旧镜像不能把用户推进多次升级。 */
    static Asset newest(Asset current, Asset candidate) {
        if (current == null) return candidate;
        if (candidate == null) return current;
        return candidate.versionCode > current.versionCode ? candidate : current;
    }

    /** 每小时最多自动检查一次；时钟回拨时允许重新检查。 */
    static boolean isCheckDue(long nowWallMs, long lastAttemptWallMs, boolean autoEnabled) {
        if (!autoEnabled || nowWallMs <= 0L) return false;
        if (lastAttemptWallMs <= 0L || nowWallMs < lastAttemptWallMs) return true;
        return nowWallMs - lastAttemptWallMs >= CHECK_INTERVAL_MS;
    }

    /** 去掉路径和危险字符，只保留单个 APK 文件名。 */
    static String safeFileName(String input) {
        if (input == null) return "GeelyPassiveKey-update.apk";
        String name = input.trim();
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) name = name.substring(slash + 1);
        name = name.replaceAll("[^A-Za-z0-9._-]", "_");
        if (name.isEmpty()) name = "GeelyPassiveKey-update.apk";
        if (!name.toLowerCase(java.util.Locale.ROOT).endsWith(".apk")) name += ".apk";
        return name.length() > 120 ? name.substring(name.length() - 120) : name;
    }

    /** GitHub API 的 digest 形如 sha256:abc...；比较时不区分大小写。 */
    static boolean digestMatches(String expected, String actual) {
        if (actual == null || actual.trim().isEmpty()) return false;
        if (expected == null || expected.trim().isEmpty()) return true;
        String normalized = expected.trim();
        if (normalized.regionMatches(true, 0, "sha256:", 0, 7)) {
            normalized = normalized.substring(7);
        }
        return normalized.equalsIgnoreCase(actual.trim());
    }
}
