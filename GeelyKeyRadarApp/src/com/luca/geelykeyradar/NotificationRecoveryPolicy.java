package com.luca.geelykeyradar;

/** NotificationListenerService 失联后的有界退避，避免每两秒无限 requestRebind。 */
final class NotificationRecoveryPolicy {
    static final long USER_ACTION_AFTER_MS = 10_000L;
    static final long LONG_RETRY_MS = 300_000L;

    private static final long[] RETRY_DELAYS_MS = {
            0L, 2_000L, 5_000L, 10_000L, 30_000L, 60_000L, LONG_RETRY_MS
    };

    private NotificationRecoveryPolicy() {
    }

    /** attempts 是已经发出的重绑请求数；0 表示本次失联尚未请求。 */
    static long retryDelayMs(int attempts) {
        int index = Math.max(0, Math.min(RETRY_DELAYS_MS.length - 1, attempts));
        return RETRY_DELAYS_MS[index];
    }

    static boolean shouldRequestRebind(boolean accessEnabled, boolean listenerConnected,
                                       long downMs, long sinceLastRequestMs, int attempts) {
        if (!accessEnabled || listenerConnected || downMs < 0L) return false;
        if (attempts <= 0 || sinceLastRequestMs < 0L) return true;
        return sinceLastRequestMs >= retryDelayMs(attempts);
    }

    static String recoveryStatus(boolean accessEnabled, boolean listenerConnected, long downMs) {
        if (!accessEnabled) return "listener_recovery=access_disabled";
        if (listenerConnected) return "listener_recovery=connected";
        return downMs >= USER_ACTION_AFTER_MS
                ? "listener_recovery=needs_user_access_toggle"
                : "listener_recovery=rebinding";
    }
}
