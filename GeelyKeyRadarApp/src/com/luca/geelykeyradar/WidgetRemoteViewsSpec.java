package com.luca.geelykeyradar;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 官方银河小组件 RemoteViews 控件与锁态图标规格。
 * 同时保留 1.56.1 静态资源 ID 与运行时按官方资源名动态解析出来的 ID，
 * 避免官方 App 升级后 R.id 重排导致小组件无法识别。
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

    final Set<Integer> funStatusIds;
    final Set<Integer> confirmOkIds;
    final Set<Integer> settingIds;
    final Set<Integer> appAdditionIds;
    final Set<Integer> lockOffIconIds;
    final Set<Integer> lockOnIconIds;

    private WidgetRemoteViewsSpec(Set<Integer> funStatusIds, Set<Integer> confirmOkIds,
                                  Set<Integer> settingIds, Set<Integer> appAdditionIds,
                                  Set<Integer> lockOffIconIds, Set<Integer> lockOnIconIds) {
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

    private static Set<Integer> idSet(int... ids) {
        Set<Integer> result = new LinkedHashSet<>();
        for (int id : ids) {
            if (id != 0) result.add(id);
        }
        return java.util.Collections.unmodifiableSet(result);
    }
}
