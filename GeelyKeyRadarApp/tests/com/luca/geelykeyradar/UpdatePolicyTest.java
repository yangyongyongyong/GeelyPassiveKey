package com.luca.geelykeyradar;

/** 自动更新纯逻辑回归，不访问网络、不读取真实安装包。 */
public final class UpdatePolicyTest {
    public static void main(String[] args) {
        parsesOnlyVersionedApkNames();
        comparesVersionAndHourlyWindow();
        selectsHighestVersionAcrossSources();
        sanitizesAndMatchesDigest();
        allowsOnlyCloudflareAndGithubAssets();
        System.out.println("UpdatePolicyTest passed");
    }

    private static void parsesOnlyVersionedApkNames() {
        UpdatePolicy.Asset asset = UpdatePolicy.parseAsset(
                "GeelyPassiveKey-v4.4.0-min-400.apk",
                "https://example.test/update.apk",
                "sha256:ABCDEF", 1234L);
        check(asset != null, "版本化 APK 应被识别");
        check("4.4.0-min".equals(asset.versionName), "版本名应保留 -min");
        check(asset.versionCode == 400 && asset.size == 1234L, "构建号和大小应保留");
        check(UpdatePolicy.parseAsset("GeelyPassiveKey.apk", "https://example.test/a", "", 0) == null,
                "旧的无版本 APK 不能参与自动更新");
        check(UpdatePolicy.parseAsset("other-v4.4.0-400.apk", "https://example.test/a", "", 0) == null,
                "其他 APK 不能被误选");
        check(UpdatePolicy.parseAsset("GeelyPassiveKey-v4.4.0-min-0.apk", "https://example.test/a", "", 0) == null,
                "非正构建号应拒绝");
    }

    private static void comparesVersionAndHourlyWindow() {
        check(UpdatePolicy.isNewer(401, 400), "更高构建号应允许更新");
        check(!UpdatePolicy.isNewer(400, 400), "同版本不能重复提示更新");
        long now = 10_000_000L;
        check(UpdatePolicy.isCheckDue(now, 0L, true), "首次启用应立即检查");
        check(!UpdatePolicy.isCheckDue(now, now - UpdatePolicy.CHECK_INTERVAL_MS + 1L, true),
                "未满一小时不应自动检查");
        check(UpdatePolicy.isCheckDue(now, now - UpdatePolicy.CHECK_INTERVAL_MS, true),
                "满一小时应检查");
        check(UpdatePolicy.isCheckDue(now, now + 10L, true), "时钟回拨后允许重新检查");
        check(!UpdatePolicy.isCheckDue(now, 0L, false), "关闭自动更新后不得自动检查");
    }

    private static void selectsHighestVersionAcrossSources() {
        UpdatePolicy.Asset stale = UpdatePolicy.parseAsset("GeelyPassiveKey-v4.6.2-min-602.apk",
                "https://geelyupdate.mq.lucadesign.uk/releases/4.6.2-min/GeelyPassiveKey-v4.6.2-min-602.apk",
                "sha256:a", 100L);
        UpdatePolicy.Asset latest = UpdatePolicy.parseAsset("GeelyPassiveKey-v4.6.4-min-604.apk",
                "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/v4.6.4-min/GeelyPassiveKey-v4.6.4-min-604.apk",
                "sha256:b", 100L);
        check(UpdatePolicy.newest(stale, latest) == latest, "旧 Cloudflare 清单不能压过更高 GitHub 版本");
        check(UpdatePolicy.newest(latest, stale) == latest, "渠道顺序不能降低已选最高版本");
    }

    private static void sanitizesAndMatchesDigest() {
        check("evil.apk".equals(UpdatePolicy.safeFileName("../../evil.apk")),
                "下载文件名不能带路径");
        check("a_b.apk".equals(UpdatePolicy.safeFileName("a b")), "危险字符应替换并补 apk");
        check(UpdatePolicy.digestMatches("sha256:ABCDEF", "abcdef"), "摘要前缀和大小写不影响比较");
        check(UpdatePolicy.digestMatches("", "abcdef"), "旧 Release 没有 digest 时不凭空拒绝");
        check(!UpdatePolicy.digestMatches("sha256:123", "456"), "摘要不一致应拒绝");
        check(!UpdatePolicy.digestMatches("", ""), "实测摘要为空时不能视为通过");
    }


    private static void allowsOnlyCloudflareAndGithubAssets() {
        String name = "GeelyPassiveKey-v4.6.0-min-600.apk";
        UpdatePolicy.Asset cloudflare = UpdatePolicy.parseAsset(name,
                "https://geelyupdate.mq.lucadesign.uk/releases/4.6.0-min/" + name,
                "sha256:abc", 1234L);
        UpdatePolicy.Asset github = UpdatePolicy.parseAsset(name,
                "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/v4.6.0-min/" + name,
                "sha256:abc", 1234L);
        check(UpdatePolicy.isAllowedAssetLocation(cloudflare), "Cloudflare 版本化 APK 地址允许");
        check(UpdatePolicy.isAllowedAssetLocation(github), "本项目 GitHub Release 地址允许");
        check(!UpdatePolicy.isAllowedAssetLocation(UpdatePolicy.parseAsset(name,
                "https://evil.example/releases/4.6.0-min/" + name, "", 1234L)), "其他域名必须拒绝");
        check(!UpdatePolicy.isAllowedAssetLocation(UpdatePolicy.parseAsset(name,
                "https://geelyupdate.mq.lucadesign.uk/releases/4.5.9-min/" + name, "", 1234L)),
                "Cloudflare 路径版本必须匹配");
        check(UpdatePolicy.isAllowedManifestUrl(UpdatePolicy.CLOUDFLARE_MANIFEST_URL),
                "Cloudflare 清单地址允许");
        check(UpdatePolicy.isAllowedManifestUrl(UpdatePolicy.GITHUB_MANIFEST_URL),
                "GitHub 兜底清单地址允许");
        check(!UpdatePolicy.isAllowedManifestUrl("https://evil.example/update.json"),
                "其他清单域名拒绝");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
