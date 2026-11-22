package com.luca.geelykeyradar;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/** 安装包版本读取与文件名安全格式，确保日志不会混入错误版本。 */
final class AppBuildInfo {
    private AppBuildInfo() {
    }

    static String versionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionName == null ? "unknown" : info.versionName;
        } catch (Exception unavailable) {
            return "unknown";
        }
    }

    static long versionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.getLongVersionCode();
        } catch (Exception unavailable) {
            return 0L;
        }
    }

    static String label(Context context) {
        return versionName(context) + "(" + versionCode(context) + ")";
    }

    /** 文件或日志字段使用的短标签，只保留常见版本名字符。 */
    static String fileTag(Context context) {
        return LogUploadPolicy.archiveVersionTag(versionName(context), versionCode(context));
    }
}
