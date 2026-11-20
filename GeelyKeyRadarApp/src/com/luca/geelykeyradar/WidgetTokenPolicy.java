package com.luca.geelykeyradar;

/**
 * 小组件 token 的安全判定：官方 WgProviderMiddle 的 LOCK 控件是状态切换，
 * 不能在不了解当前车锁状态时直接发送。
 */
final class WidgetTokenPolicy {
    static final long MAX_LOCK_HINT_AGE_MS = 45000L;

    /** 单次车控只选择一个 token 源，通知路径永远优先。 */
    enum Route {
        NONE,
        NOTIFICATION,
        WIDGET
    }

    private WidgetTokenPolicy() {
    }

    /** Widget token 只有在锁态新鲜且与目标动作相反时才允许发送。 */
    static boolean canUseToggle(PassiveKeyLogic.Action action, String lockHint,
                                long lockHintAtMs, long nowMs) {
        return reason(action, lockHint, lockHintAtMs, nowMs).isEmpty();
    }

    /**
     * 在发送前选择唯一通道。通知 token 可用时不再检查或发送 Widget；
     * Widget 只有显式启用、token 已捕获且锁态门控通过时才可选。
     */
    static Route selectRoute(PassiveKeyLogic.Action action, boolean notificationAvailable,
                             boolean widgetEnabled, boolean widgetTokenAvailable,
                             String lockHint, long lockHintAtMs, long nowMs) {
        if (notificationAvailable) return Route.NOTIFICATION;
        if (!widgetEnabled || !widgetTokenAvailable) return Route.NONE;
        return canUseToggle(action, lockHint, lockHintAtMs, nowMs)
                ? Route.WIDGET : Route.NONE;
    }

    /** 返回通知优先路由被拒绝时的可记录原因；空字符串表示可选。 */
    static String routeReason(PassiveKeyLogic.Action action, boolean notificationAvailable,
                              boolean widgetEnabled, boolean widgetTokenAvailable,
                              String lockHint, long lockHintAtMs, long nowMs) {
        if (notificationAvailable) return "";
        if (!widgetEnabled) return "WIDGET_DISABLED";
        if (!widgetTokenAvailable) return "WIDGET_TOKEN_MISSING";
        return reason(action, lockHint, lockHintAtMs, nowMs);
    }

    /** 返回拒绝原因；空字符串表示允许。 */
    static String reason(PassiveKeyLogic.Action action, String lockHint,
                         long lockHintAtMs, long nowMs) {
        if (action != PassiveKeyLogic.Action.UNLOCK && action != PassiveKeyLogic.Action.LOCK) {
            return "INVALID_ACTION";
        }
        if (lockHintAtMs <= 0L || nowMs - lockHintAtMs > MAX_LOCK_HINT_AGE_MS) {
            return "LOCK_STATE_STALE";
        }
        if ("LOCKED".equals(lockHint)) {
            return action == PassiveKeyLogic.Action.UNLOCK ? "" : "ALREADY_LOCKED";
        }
        if ("UNLOCKED".equals(lockHint)) {
            return action == PassiveKeyLogic.Action.LOCK ? "" : "ALREADY_UNLOCKED";
        }
        return "LOCK_STATE_UNKNOWN";
    }

    /** 供日志/界面使用的拒绝原因中文摘要。 */
    static String displayReason(String reason) {
        if ("LOCK_STATE_STALE".equals(reason)) return "官方锁态已过期";
        if ("LOCK_STATE_UNKNOWN".equals(reason)) return "官方锁态未知";
        if ("ALREADY_LOCKED".equals(reason)) return "车辆已经上锁";
        if ("ALREADY_UNLOCKED".equals(reason)) return "车辆已经解锁";
        if ("WIDGET_DISABLED".equals(reason)) return "小组件备用控制未启用";
        if ("WIDGET_TOKEN_MISSING".equals(reason)) return "尚未捕获官方小组件车锁 token";
        if ("NOTIFICATION_TOKEN_MISSING".equals(reason)) return "通知按钮和小组件 token 均不可用";
        if ("INVALID_ACTION".equals(reason)) return "无效动作";
        return "";
    }
}
