package com.luca.geelykeyradar;

import android.content.Context;

/**
 * 吉利汽车（com.geely.consumer）与吉利银河（com.geelygalaxy.customer）双官方客户端的 Android 运行时探测。
 */
final class OfficialTarget {
    private OfficialTarget() {
    }

    static boolean isInstalled(Context context, String pkg) {
        if (context == null || pkg == null || pkg.isEmpty()) return false;
        try {
            context.getPackageManager().getPackageInfo(pkg, 0);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    static boolean isConsumerInstalled(Context context) {
        return isInstalled(context, OfficialBrandPolicy.PKG_CONSUMER);
    }

    static boolean isGalaxyInstalled(Context context) {
        return isInstalled(context, OfficialBrandPolicy.PKG_GALAXY);
    }

    static boolean isAnyInstalled(Context context) {
        return isConsumerInstalled(context) || isGalaxyInstalled(context);
    }

    static boolean isBrandInstalled(Context context, OfficialBrandPolicy.Brand brand) {
        if (brand == OfficialBrandPolicy.Brand.CONSUMER) return isConsumerInstalled(context);
        if (brand == OfficialBrandPolicy.Brand.GALAXY) return isGalaxyInstalled(context);
        return isAnyInstalled(context);
    }

    static OfficialBrandPolicy.Brand resolveBrand(Context context,
                                                OfficialBrandPolicy.Brand userMode,
                                                OfficialBrandPolicy.Brand vehicleBrand) {
        return OfficialBrandPolicy.resolveTargetBrand(
                userMode, vehicleBrand, isConsumerInstalled(context), isGalaxyInstalled(context));
    }

    static String resolvePackage(Context context,
                                 OfficialBrandPolicy.Brand userMode,
                                 OfficialBrandPolicy.Brand vehicleBrand) {
        return OfficialBrandPolicy.packageForBrand(resolveBrand(context, userMode, vehicleBrand));
    }

    static String resolveConfiguredPackage(Context context) {
        if (context == null) return OfficialBrandPolicy.PKG_CONSUMER;
        VehicleIdentityStore store = new VehicleIdentityStore(context);
        VehicleGarage garage = store.loadGarage();
        VehicleProfile active = garage.findById(garage.pinnedProfileId);
        if (active == null && !garage.activeProfiles().isEmpty()) {
            active = garage.activeProfiles().get(0);
        }
        OfficialBrandPolicy.Brand vehicleBrand = active != null
                ? active.brand : OfficialBrandPolicy.Brand.AUTO;
        return resolvePackage(context, garage.brandMode, vehicleBrand);
    }

    static String displayName(Context context, OfficialBrandPolicy.Brand userMode,
                              OfficialBrandPolicy.Brand vehicleBrand) {
        if (!isConsumerInstalled(context) && !isGalaxyInstalled(context)) {
            if (vehicleBrand == OfficialBrandPolicy.Brand.GALAXY
                    || (vehicleBrand == OfficialBrandPolicy.Brand.AUTO
                    && userMode == OfficialBrandPolicy.Brand.GALAXY)) {
                return "银河 App";
            }
            if (vehicleBrand == OfficialBrandPolicy.Brand.CONSUMER
                    || userMode == OfficialBrandPolicy.Brand.CONSUMER) {
                return "吉利汽车 App";
            }
            return "官方 App（吉利汽车 / 银河）";
        }
        return OfficialBrandPolicy.displayNameForBrand(resolveBrand(context, userMode, vehicleBrand));
    }

    static String installedSummary(Context context) {
        boolean consumer = isConsumerInstalled(context);
        boolean galaxy = isGalaxyInstalled(context);
        return "吉利汽车 App：" + (consumer ? "已安装" : "未安装")
                + " · 银河 App：" + (galaxy ? "已安装" : "未安装");
    }
}
