package com.luca.geelykeyradar;

/** 官方小组件静态、动态资源 ID 与公开 View Drawable 图标识别回归。 */
public final class WidgetRemoteViewsSpecTest {
    public static void main(String[] args) {
        legacyIdsAreSupported();
        dynamicIdsSurviveResourceShift();
        zeroAndUnrelatedIdsAreIgnored();
        normalizedIconPixelsDistinguishLockStates();
        System.out.println("WidgetRemoteViewsSpecTest passed");
    }

    private static void legacyIdsAreSupported() {
        WidgetRemoteViewsSpec spec = WidgetRemoteViewsSpec.defaults();
        check(spec.isFunStatusView(WidgetRemoteViewsSpec.LEGACY_VIEW_FUN_STATUS),
                "应支持 1.56.1 的 wg_view_fun_status");
        check(spec.isFunStatusView(WidgetRemoteViewsSpec.LEGACY_VIEW_FUN_LOADING),
                "应支持 1.56.1 的 wg_view_fun_loading");
        check(spec.isConfirmOkView(WidgetRemoteViewsSpec.LEGACY_CONFIRM_OK),
                "应支持 1.56.1 确认按钮 ID");
        check(spec.isSettingView(WidgetRemoteViewsSpec.LEGACY_BTN_SETTING),
                "应支持 1.56.1 设置按钮 ID");
        check(spec.isAppAdditionView(WidgetRemoteViewsSpec.LEGACY_LL_APP_ADDITION),
                "应支持 1.56.1 添加入口 ID");
        check("LOCKED".equals(spec.lockHintForDrawable(WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_OFF_BLACK)),
                "off_black 图标应识别为 LOCKED");
        check("LOCKED".equals(spec.lockHintForDrawable(WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_OFF_WHITE)),
                "off_white 图标应识别为 LOCKED");
        check("UNLOCKED".equals(spec.lockHintForDrawable(WidgetRemoteViewsSpec.LEGACY_ICON_LOCK_ON)),
                "lock_on 图标应识别为 UNLOCKED");
    }

    private static void dynamicIdsSurviveResourceShift() {
        WidgetRemoteViewsSpec shifted = WidgetRemoteViewsSpec.create(
                0x7f0b0001, 0x7f0b0002, 0x7f0b0003, 0x7f0b0004, 0x7f0b0005,
                0x7f100001, 0x7f100002, 0x7f100003);
        check(shifted.isFunStatusView(0x7f0b0001) && shifted.isFunStatusView(0x7f0b0002),
                "官方升级重排 ID 后按资源名解析仍应识别车锁控件");
        check(shifted.isConfirmOkView(0x7f0b0003), "重排后的确认按钮必须识别");
        check(shifted.isSettingView(0x7f0b0004) && shifted.isAppAdditionView(0x7f0b0005),
                "重排后的配置入口必须识别");
        check("LOCKED".equals(shifted.lockHintForDrawable(0x7f100001)),
                "重排后的锁车图标必须识别为 LOCKED");
        check("LOCKED".equals(shifted.lockHintForDrawable(0x7f100002)),
                "重排后的白色锁车图标必须识别为 LOCKED");
        check("UNLOCKED".equals(shifted.lockHintForDrawable(0x7f100003)),
                "重排后的开锁图标必须识别为 UNLOCKED");
        check(shifted.isFunStatusView(WidgetRemoteViewsSpec.LEGACY_VIEW_FUN_STATUS),
                "动态解析不得破坏既有 1.56.1 ID 兼容");
    }

    private static void zeroAndUnrelatedIdsAreIgnored() {
        WidgetRemoteViewsSpec spec = WidgetRemoteViewsSpec.defaults();
        check(!spec.isFunStatusView(0) && !spec.isConfirmOkView(0), "0 资源 ID 不得误匹配");
        check(spec.lockHintForDrawable(0) == null && spec.lockHintForDrawable(0x12345678) == null,
                "其他非车锁图标不得伪造锁态");
    }

    private static void normalizedIconPixelsDistinguishLockStates() {
        int[] offBlack = new int[256];
        int[] offWhite = new int[256];
        int[] onGreen = new int[256];
        int[] unrelated = new int[256];
        for (int i = 0; i < 256; i++) {
            int row = i / 16;
            int col = i % 16;
            boolean body = row >= 6 && row <= 13 && col >= 4 && col <= 11;
            boolean closedShackle = row >= 2 && row <= 5 && (col == 5 || col == 10 || (row == 2 && col >= 5 && col <= 10));
            boolean openShackle = row >= 2 && row <= 5 && (col == 9 || col == 13 || (row == 2 && col >= 9 && col <= 13));
            if (body || closedShackle) {
                offBlack[i] = 0xFF1E1E1E;
                offWhite[i] = 0xFFF5F5F5;
            }
            if (body || openShackle) {
                onGreen[i] = 0xFF2ED47A;
            }
            if (row >= 4 && row <= 12 && col >= 3 && col <= 12) {
                unrelated[i] = 0xFF4285F4;
            }
        }
        check("LOCKED".equals(WidgetRemoteViewsSpec.matchNormalizedIconPixels(offBlack, offBlack, offWhite, onGreen)),
                "黑色闭锁图标轮廓必须识别为 LOCKED");
        check("LOCKED".equals(WidgetRemoteViewsSpec.matchNormalizedIconPixels(offWhite, offBlack, offWhite, onGreen)),
                "白色闭锁图标轮廓必须识别为 LOCKED");
        check("UNLOCKED".equals(WidgetRemoteViewsSpec.matchNormalizedIconPixels(onGreen, offBlack, offWhite, onGreen)),
                "开锁图标轮廓必须识别为 UNLOCKED");
        check(WidgetRemoteViewsSpec.matchNormalizedIconPixels(unrelated, offBlack, offWhite, onGreen) == null,
                "其他车控图标不得误识别为车锁状态");
        check(WidgetRemoteViewsSpec.matchNormalizedIconPixels(new int[256], offBlack, offWhite, onGreen) == null,
                "全透明图标不得返回锁态");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
