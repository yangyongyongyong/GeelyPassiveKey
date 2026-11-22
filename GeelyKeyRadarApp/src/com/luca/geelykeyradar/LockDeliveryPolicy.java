package com.luca.geelykeyradar;

/**
 * 自动上锁交付策略。
 *
 * 旧通知 PendingIntent 只能证明 Android 接收了投递，不能证明车辆执行。
 * 当本次上锁只能复用旧缓存 token 时，允许在仍处于远场、官方仍连接且没有锁态确认的情况下，
 * 额外执行一次显式上锁重试。解锁永远不走该策略。
 */
final class LockDeliveryPolicy {
    static final long FALLBACK_TOKEN_STALE_MS = 60_000L;
    static final long RETRY_DELAY_MS = 6_000L;

    private LockDeliveryPolicy() {
    }

    /** 当前通知没有按钮、只能使用历史缓存时，超过一分钟或年龄未知即视为旧 token。 */
    static boolean isStaleFallback(boolean currentTokenAvailable, long fallbackTokenAgeMs) {
        return !currentTokenAvailable
                && (fallbackTokenAgeMs < 0L || fallbackTokenAgeMs >= FALLBACK_TOKEN_STALE_MS);
    }

    /** 只有自动上锁、明确交付、旧 token 且没有已上锁证据时才安排一次确认重试。 */
    static boolean shouldScheduleRetry(PassiveKeyLogic.Action action, boolean delivered,
                                       boolean currentTokenAvailable, long fallbackTokenAgeMs,
                                       boolean autoLock, String lockHint) {
        return action == PassiveKeyLogic.Action.LOCK
                && delivered
                && autoLock
                && !"LOCKED".equals(lockHint)
                && isStaleFallback(currentTokenAvailable, fallbackTokenAgeMs);
    }

    /** 到达重试时刻后，必须仍满足安全条件；任何条件变化都放弃，不累计第三次发送。 */
    static boolean canRetryNow(PassiveKeyLogic.Action action, boolean retryUsed,
                               boolean serviceEnabled, boolean autoLock, boolean blocked,
                               boolean officialConnected, boolean signalFresh,
                               float distanceDbm, float lockDbm, String lockHint,
                               long elapsedSinceDeliveryMs) {
        return action == PassiveKeyLogic.Action.LOCK
                && !retryUsed
                && serviceEnabled
                && autoLock
                && !blocked
                && officialConnected
                && signalFresh
                && !"LOCKED".equals(lockHint)
                && elapsedSinceDeliveryMs >= RETRY_DELAY_MS
                && Float.isFinite(distanceDbm)
                && Float.isFinite(lockDbm)
                && distanceDbm <= lockDbm;
    }

    static boolean hasLockedConfirmation(String lockHint) {
        return "LOCKED".equals(lockHint);
    }
}
