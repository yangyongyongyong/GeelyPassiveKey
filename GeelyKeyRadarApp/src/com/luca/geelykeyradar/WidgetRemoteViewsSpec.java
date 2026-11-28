package com.luca.geelykeyradar;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 官方银河/吉利小组件 RemoteViews 控件与锁态图标规格。
 * 同时保留 1.56.1 静态资源 ID 与运行时按官方资源名动态解析出来的 ID，
 * 并提供不依赖隐藏 API 的标准化图标像素匹配，兼容 Android 14-17。
 */
final class WidgetRemoteViewsSpec {
    static final int LEGACY_VIEW_FUN_LOADING = 0x7f0a2123;
    static final int LEGACY_VIEW_FUN_STATUS = 0x7f0a2124;
    static final int LEGACY_CONFIRM_OK = 0x7f0a20fd;
    static final int LEGACY_BTN_SETTING = 0x7f0a20da;
    static final int LEGACY_LL_APP_ADDITION = 0x7f0a20f7;
    static final int LEGACY_ICON_LOCK_OFF_BLACK = 0x7f0f039e;
    static final int LEGACY_ICON_LOCK_OFF_WHITE = 0x7f0f039f;
    static final int LEGACY_ICON_LOCK_ON = 0x7f0f03a0;

    static final String NAME_VIEW_FUN_LOADING = "wg_view_fun_loading";
    static final String NAME_VIEW_FUN_STATUS = "wg_view_fun_status";
    static final String NAME_CONFIRM_OK = "wg_middle_confirm_btn_ok";
    static final String NAME_BTN_SETTING = "wg_btn_setting";
    static final String NAME_LL_APP_ADDITION = "wg_ll_app_addition";
    static final String NAME_ICON_LOCK_OFF_BLACK = "car_wg_funicon_door_lock_off_black";
    static final String NAME_ICON_LOCK_OFF_WHITE = "car_wg_funicon_door_lock_off_white";
    static final String NAME_ICON_LOCK_ON = "car_wg_funicon_door_lock_on";

    static final int ICON_GRID_SIZE = 16;

    final int dynamicLoadingId;
    final int dynamicStatusId;
    final int dynamicConfirmOkId;
    final int dynamicSettingId;
    final int dynamicAppAdditionId;
    final int dynamicLockOffBlackId;
    final int dynamicLockOffWhiteId;
    final int dynamicLockOnId;

    final Set<Integer> funStatusIds;
    final Set<Integer> confirmOkIds;
    final Set<Integer> settingIds;
    final Set<Integer> appAdditionIds;
    final Set<Integer> lockOffIconIds;
    final Set<Integer> lockOnIconIds;

    private WidgetRemoteViewsSpec(int dynamicLoadingId, int dynamicStatusId, int dynamicConfirmOkId,
                                  int dynamicSettingId, int dynamicAppAdditionId,
                                  int dynamicLockOffBlackId, int dynamicLockOffWhiteId, int dynamicLockOnId,
                                  Set<Integer> funStatusIds, Set<Integer> confirmOkIds,
                                  Set<Integer> settingIds, Set<Integer> appAdditionIds,
                                  Set<Integer> lockOffIconIds, Set<Integer> lockOnIconIds) {
        this.dynamicLoadingId = dynamicLoadingId;
        this.dynamicStatusId = dynamicStatusId;
        this.dynamicConfirmOkId = dynamicConfirmOkId;
        this.dynamicSettingId = dynamicSettingId;
        this.dynamicAppAdditionId = dynamicAppAdditionId;
        this.dynamicLockOffBlackId = dynamicLockOffBlackId;
        this.dynamicLockOffWhiteId = dynamicLockOffWhiteId;
        this.dynamicLockOnId = dynamicLockOnId;
        this.funStatusIds = funStatusIds;
        this.confirmOkIds = confirmOkIds;
        this.settingIds = settingIds;
        this.appAdditionIds = appAdditionIds;
        this.lockOffIconIds = lockOffIconIds;
        this.lockOnIconIds = lockOnIconIds;
    }

    static WidgetRemoteViewsSpec defaults() {
        return create(0, 0, 0, 0, 0, 0, 0, 0);
    }

    static WidgetRemoteViewsSpec create(int loadingId, int statusId, int confirmOkId,
                                        int settingId, int appAdditionId,
                                        int lockOffBlackId, int lockOffWhiteId, int lockOnId) {
        return new WidgetRemoteViewsSpec(
                loadingId, statusId, confirmOkId, settingId, appAdditionId,
                lockOffBlackId, lockOffWhiteId, lockOnId,
                idSet(LEGACY_VIEW_FUN_STATUS, LEGACY_VIEW_FUN_LOADING, statusId, loadingId),
                idSet(LEGACY_CONFIRM_OK, confirmOkId),
                idSet(LEGACY_BTN_SETTING, settingId),
                idSet(LEGACY_LL_APP_ADDITION, appAdditionId),
                idSet(LEGACY_ICON_LOCK_OFF_BLACK, LEGACY_ICON_LOCK_OFF_WHITE,
                        lockOffBlackId, lockOffWhiteId),
                idSet(LEGACY_ICON_LOCK_ON, lockOnId));
    }

    boolean isFunStatusView(int viewId) {
        return viewId != 0 && funStatusIds.contains(viewId);
    }

    boolean isConfirmOkView(int viewId) {
        return viewId != 0 && confirmOkIds.contains(viewId);
    }

    boolean isSettingView(int viewId) {
        return viewId != 0 && settingIds.contains(viewId);
    }

    boolean isAppAdditionView(int viewId) {
        return viewId != 0 && appAdditionIds.contains(viewId);
    }

    String lockHintForDrawable(int drawableId) {
        if (drawableId == 0) return null;
        if (lockOnIconIds.contains(drawableId)) return "UNLOCKED";
        if (lockOffIconIds.contains(drawableId)) return "LOCKED";
        return null;
    }

    /**
     * 在隐藏 API 屏蔽 mNestedViews / mResponse 时，通过公开 ImageView Drawable 归一化像素轮廓识别锁态图标。
     * 只在命中明显高置信度（>=92%）且无平局歧义时返回 LOCKED / UNLOCKED，否则返回 null。
     */
    static String matchNormalizedIconPixels(int[] candidate, int[] refOffBlack,
                                            int[] refOffWhite, int[] refOn) {
        if (candidate == null || candidate.length == 0 || isAllTransparent(candidate)) return null;
        int onScore = similarityPercent(candidate, refOn);
        int offScore = Math.max(similarityPercent(candidate, refOffBlack),
                similarityPercent(candidate, refOffWhite));
        if (onScore < 92 && offScore < 92) return null;
        if (Math.abs(onScore - offScore) < 6) return null;
        return onScore > offScore ? "UNLOCKED" : "LOCKED";
    }

    private static boolean isAllTransparent(int[] pixels) {
        for (int p : pixels) {
            if (((p >>> 24) & 0xFF) > 16) return false;
        }
        return true;
    }

    private static int similarityPercent(int[] a, int[] b) {
        if (a == null || b == null || a.length == 0 || a.length != b.length) return 0;
        if (isAllTransparent(b)) return 0;
        int matched = 0;
        for (int i = 0; i < a.length; i++) {
            int pa = a[i];
            int pb = b[i];
            int aa = (pa >>> 24) & 0xFF;
            int ab = (pb >>> 24) & 0xFF;
            boolean aVis = aa > 48;
            boolean bVis = ab > 48;
            if (aVis != bVis) continue;
            if (!aVis) {
                matched++;
                continue;
            }
            int dr = Math.abs(((pa >>> 16) & 0xFF) - ((pb >>> 16) & 0xFF));
            int dg = Math.abs(((pa >>> 8) & 0xFF) - ((pb >>> 8) & 0xFF));
            int db = Math.abs((pa & 0xFF) - (pb & 0xFF));
            if (dr <= 42 && dg <= 42 && db <= 42) {
                matched++;
            }
        }
        return (matched * 100) / a.length;
    }

    private static Set<Integer> idSet(int... ids) {
        Set<Integer> result = new LinkedHashSet<>();
        for (int id : ids) {
            if (id != 0) result.add(id);
        }
        return java.util.Collections.unmodifiableSet(result);
    }
}
