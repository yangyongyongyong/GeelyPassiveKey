package com.luca.geelykeyradar;

/** 官方 1.59 通知按钮发送优先级回归。 */
public final class NotificationTransportPolicyTest {
    public static void main(String[] args) {
        currentViewMustWin();
        pendingIntentFallbackOrder();
        noToken();
        System.out.println("NotificationTransportPolicyTest passed");
    }

    private static void currentViewMustWin() {
        check(NotificationTransportPolicy.select(true, true, true, true)
                        == NotificationTransportPolicy.Transport.CURRENT_VIEW,
                "当前通知按钮 View 必须优先于 PendingIntent，避免 Android 16 静默空投");
    }

    private static void pendingIntentFallbackOrder() {
        check(NotificationTransportPolicy.select(false, true, true, true)
                        == NotificationTransportPolicy.Transport.CURRENT_PENDING_INTENT,
                "当前 PendingIntent 优先于历史缓存");
        check(NotificationTransportPolicy.select(false, false, true, true)
                        == NotificationTransportPolicy.Transport.CACHED_PENDING_INTENT,
                "历史 PendingIntent 优先于历史 View");
        check(NotificationTransportPolicy.select(false, false, false, true)
                        == NotificationTransportPolicy.Transport.CACHED_VIEW,
                "仅保留历史 View 时才使用历史 View");
        check(NotificationTransportPolicy.pendingIntentAfterViewMiss(true, true)
                        == NotificationTransportPolicy.Transport.CURRENT_PENDING_INTENT,
                "当前 View 无监听器时应回到当前 PendingIntent");
        check(NotificationTransportPolicy.pendingIntentAfterViewMiss(false, true)
                        == NotificationTransportPolicy.Transport.CACHED_PENDING_INTENT,
                "无当前 PendingIntent 时回到历史缓存");
    }

    private static void noToken() {
        check(NotificationTransportPolicy.select(false, false, false, false)
                        == NotificationTransportPolicy.Transport.NONE,
                "没有按钮或 token 时不得发送");
        check(NotificationTransportPolicy.pendingIntentAfterViewMiss(false, false)
                        == NotificationTransportPolicy.Transport.NONE,
                "View 点击失败后没有 PendingIntent 时应停止");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
