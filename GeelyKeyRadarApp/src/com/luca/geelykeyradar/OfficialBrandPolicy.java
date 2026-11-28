package com.luca.geelykeyradar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 吉利汽车（com.geely.consumer）与吉利银河（com.geelygalaxy.customer）双官方客户端路由与车型预设策略。
 * 纯 Java 逻辑，不依赖 Android Context，便于单测直接验证双端匹配、兜底唤醒和通知隔离规则。
 */
final class OfficialBrandPolicy {
    enum Brand {
        AUTO,
        CONSUMER,
        GALAXY
    }

    static final class ModelPreset {
        final Brand brand;
        final String modelName;

        ModelPreset(Brand brand, String modelName) {
            this.brand = brand;
            this.modelName = modelName;
        }
    }

    static final String PKG_CONSUMER = "com.geely.consumer";
    static final String PKG_GALAXY = "com.geelygalaxy.customer";

    static final String ACTION_MIUI_CONSUMER = "com.geely.consumer.miuiservice.action";
    static final String ACTION_MIUI_GALAXY = "com.geely.geometry.miuiservice.action";
    static final String ACTION_DK_START = "com.zeekr.snc.service.action.DK_START";

    static final String MIUI_SERVICE_CLASS = "com.ecarx.blekey.thirdadapter.service.MiuiService";
    static final String DK_BG_SERVICE_CLASS = "com.geely.snc.digitalkey.base.service.DigitalKeyBgService";

    static final String WIDGET_PROVIDER_GALAXY = "com.geely.galaxy.car.weight.appwidget.WgProviderMiddle";
    static final String WIDGET_PROVIDER_CONSUMER = "com.geely.consumer.carcontrol.widget.appwidget.Wg_Middle";

    static final List<ModelPreset> PRESETS = Collections.unmodifiableList(Arrays.asList(
            new ModelPreset(Brand.CONSUMER, "星越L"),
            new ModelPreset(Brand.CONSUMER, "博越L"),
            new ModelPreset(Brand.CONSUMER, "星瑞"),
            new ModelPreset(Brand.CONSUMER, "帝豪"),
            new ModelPreset(Brand.CONSUMER, "缤越"),
            new ModelPreset(Brand.CONSUMER, "豪越L"),
            new ModelPreset(Brand.GALAXY, "银河E5"),
            new ModelPreset(Brand.GALAXY, "银河E8"),
            new ModelPreset(Brand.GALAXY, "银河L7"),
            new ModelPreset(Brand.GALAXY, "银河L6"),
            new ModelPreset(Brand.GALAXY, "银河星舰7"),
            new ModelPreset(Brand.GALAXY, "银河A7"),
            new ModelPreset(Brand.GALAXY, "星愿"),
            new ModelPreset(Brand.GALAXY, "熊猫")
    ));

    private OfficialBrandPolicy() {
    }

    static Brand parseBrand(String raw) {
        if (raw == null) return Brand.AUTO;
        String norm = raw.trim().toUpperCase(Locale.ROOT);
        if ("CONSUMER".equals(norm) || "GEELY".equals(norm)) return Brand.CONSUMER;
        if ("GALAXY".equals(norm)) return Brand.GALAXY;
        return Brand.AUTO;
    }

    static boolean isOfficialPackage(String pkg) {
        return PKG_CONSUMER.equals(pkg) || PKG_GALAXY.equals(pkg);
    }

    static Brand brandForPackage(String pkg) {
        if (PKG_CONSUMER.equals(pkg)) return Brand.CONSUMER;
        if (PKG_GALAXY.equals(pkg)) return Brand.GALAXY;
        return Brand.AUTO;
    }

    static String packageForBrand(Brand brand) {
        if (brand == Brand.GALAXY) return PKG_GALAXY;
        return PKG_CONSUMER;
    }

    static String miuiActionForPackage(String pkg) {
        return PKG_GALAXY.equals(pkg) ? ACTION_MIUI_GALAXY : ACTION_MIUI_CONSUMER;
    }

    static String widgetProviderForPackage(String pkg) {
        return PKG_CONSUMER.equals(pkg) ? WIDGET_PROVIDER_CONSUMER : WIDGET_PROVIDER_GALAXY;
    }

    static String displayNameForBrand(Brand brand) {
        if (brand == Brand.CONSUMER) return "吉利汽车 App";
        if (brand == Brand.GALAXY) return "银河 App";
        return "双品牌自动（吉利 / 银河）";
    }

    static String shortLabel(Brand brand) {
        if (brand == Brand.CONSUMER) return "吉利汽车";
        if (brand == Brand.GALAXY) return "吉利银河";
        return "自动识别";
    }

    /**
     * 解析本次应唤醒的官方客户端：
     * 1) 已识别/选定车辆自带明确品牌时优先走车辆品牌；
     * 2) 否则参考全局模式；
     * 3) 若处于 AUTO 模式，优先选本机实际已安装的客户端（双装时先尝试吉利汽车，1.2s 无 ACL 再补唤醒银河）。
     */
    static Brand resolveTargetBrand(Brand userMode, Brand vehicleBrand,
                                    boolean consumerInstalled, boolean galaxyInstalled) {
        Brand mode = userMode == null ? Brand.AUTO : userMode;
        Brand car = vehicleBrand == null ? Brand.AUTO : vehicleBrand;
        if (car != Brand.AUTO) {
            if (car == Brand.CONSUMER && consumerInstalled) return Brand.CONSUMER;
            if (car == Brand.GALAXY && galaxyInstalled) return Brand.GALAXY;
            if (mode == Brand.AUTO) {
                if (consumerInstalled) return Brand.CONSUMER;
                if (galaxyInstalled) return Brand.GALAXY;
            }
            return car;
        }
        if (mode == Brand.CONSUMER) return Brand.CONSUMER;
        if (mode == Brand.GALAXY) return Brand.GALAXY;
        if (consumerInstalled) return Brand.CONSUMER;
        if (galaxyInstalled) return Brand.GALAXY;
        return Brand.CONSUMER;
    }

    /** 自动双品牌模式下且双端都已安装时，主唤醒无 ACL 后可补拉另一家官方端。 */
    static Brand secondaryAutoBrand(Brand primary, Brand userMode, Brand vehicleBrand,
                                    boolean consumerInstalled, boolean galaxyInstalled) {
        Brand mode = userMode == null ? Brand.AUTO : userMode;
        Brand car = vehicleBrand == null ? Brand.AUTO : vehicleBrand;
        if (mode != Brand.AUTO || car != Brand.AUTO) return null;
        if (!consumerInstalled || !galaxyInstalled) return null;
        return primary == Brand.CONSUMER ? Brand.GALAXY : Brand.CONSUMER;
    }

    /**
     * 决定通知监听是否应处理来自该包名的通知：
     * - 用户/当前会话锁定单品牌时，只接收该品牌通知，防止另一家常驻“未连接”覆盖；
     * - AUTO 模式下两家通知均可接收（实际优先级由活跃连接状态保护）。
     */
    static boolean shouldAcceptNotification(Brand filterBrand, String pkg) {
        if (!isOfficialPackage(pkg)) return false;
        if (filterBrand == Brand.CONSUMER) return PKG_CONSUMER.equals(pkg);
        if (filterBrand == Brand.GALAXY) return PKG_GALAXY.equals(pkg);
        return true;
    }
}
