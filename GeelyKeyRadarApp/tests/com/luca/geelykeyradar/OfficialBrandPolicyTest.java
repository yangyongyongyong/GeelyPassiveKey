package com.luca.geelykeyradar;

/** 双官方客户端（吉利汽车 / 吉利银河）路由、兜底与通知隔离纯 Java 回归。 */
public final class OfficialBrandPolicyTest {
    public static void main(String[] args) {
        vehicleBrandOverridesGlobalAutoMode();
        autoModeFallsBackToInstalledAndDualWake();
        notificationFilterIsolatesWhenPinned();
        presetsCoverBothBrands();
        System.out.println("OfficialBrandPolicyTest passed");
    }

    private static void vehicleBrandOverridesGlobalAutoMode() {
        check(OfficialBrandPolicy.resolveTargetBrand(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.GALAXY, true, true)
                == OfficialBrandPolicy.Brand.GALAXY,
                "双端都装时，银河车型应直连银河 App");
        check(OfficialBrandPolicy.resolveTargetBrand(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.CONSUMER, true, true)
                == OfficialBrandPolicy.Brand.CONSUMER,
                "双端都装时，吉利汽车车型应直连吉利汽车 App");
        check(OfficialBrandPolicy.ACTION_MIUI_CONSUMER.equals(
                        OfficialBrandPolicy.miuiActionForPackage(OfficialBrandPolicy.PKG_CONSUMER)),
                "吉利汽车包名应匹配 consumer action");
        check(OfficialBrandPolicy.ACTION_MIUI_GALAXY.equals(
                        OfficialBrandPolicy.miuiActionForPackage(OfficialBrandPolicy.PKG_GALAXY)),
                "银河包名应匹配 geometry action");
    }

    private static void autoModeFallsBackToInstalledAndDualWake() {
        check(OfficialBrandPolicy.resolveTargetBrand(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.AUTO, false, true)
                == OfficialBrandPolicy.Brand.GALAXY,
                "只装银河时 AUTO 应自动选用银河 App");
        check(OfficialBrandPolicy.resolveTargetBrand(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.AUTO, true, false)
                == OfficialBrandPolicy.Brand.CONSUMER,
                "只装吉利汽车时 AUTO 应自动选用吉利汽车 App");
        OfficialBrandPolicy.Brand primary = OfficialBrandPolicy.resolveTargetBrand(
                OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.AUTO, true, true);
        OfficialBrandPolicy.Brand secondary = OfficialBrandPolicy.secondaryAutoBrand(
                primary, OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.Brand.AUTO, true, true);
        check(primary == OfficialBrandPolicy.Brand.CONSUMER && secondary == OfficialBrandPolicy.Brand.GALAXY,
                "双端都装且未标定车型品牌时，1.2s 无 ACL 兜底应能补拉另一端");
        check(OfficialBrandPolicy.secondaryAutoBrand(
                        OfficialBrandPolicy.Brand.GALAXY, OfficialBrandPolicy.Brand.AUTO,
                        OfficialBrandPolicy.Brand.GALAXY, true, true) == null,
                "已标定为银河的车辆不得盲目补拉吉利汽车");
    }

    private static void notificationFilterIsolatesWhenPinned() {
        check(OfficialBrandPolicy.shouldAcceptNotification(
                        OfficialBrandPolicy.Brand.CONSUMER, OfficialBrandPolicy.PKG_CONSUMER),
                "锁定吉利汽车时接受吉利汽车通知");
        check(!OfficialBrandPolicy.shouldAcceptNotification(
                        OfficialBrandPolicy.Brand.CONSUMER, OfficialBrandPolicy.PKG_GALAXY),
                "锁定吉利汽车时屏蔽银河通知避免未连接覆盖");
        check(OfficialBrandPolicy.shouldAcceptNotification(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.PKG_CONSUMER)
                && OfficialBrandPolicy.shouldAcceptNotification(
                        OfficialBrandPolicy.Brand.AUTO, OfficialBrandPolicy.PKG_GALAXY),
                "AUTO 多车模式下两家官方通知都应能识别");
        check(!OfficialBrandPolicy.shouldAcceptNotification(
                        OfficialBrandPolicy.Brand.AUTO, "com.example.other"),
                "非官方包名不得被当作钥匙通知");
    }

    private static void presetsCoverBothBrands() {
        boolean hasConsumer = false;
        boolean hasGalaxy = false;
        for (OfficialBrandPolicy.ModelPreset preset : OfficialBrandPolicy.PRESETS) {
            if (preset.brand == OfficialBrandPolicy.Brand.CONSUMER) hasConsumer = true;
            if (preset.brand == OfficialBrandPolicy.Brand.GALAXY) hasGalaxy = true;
        }
        check(hasConsumer && hasGalaxy, "车型预设必须同时包含吉利汽车与吉利银河");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
