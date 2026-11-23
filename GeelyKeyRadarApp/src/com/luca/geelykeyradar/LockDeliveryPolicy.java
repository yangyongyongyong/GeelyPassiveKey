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

    /**
     * 重新解析一条旧通知并不等于获得了新鲜按钮；当前通知本身超过一分钟时仍按旧 token 处理。
     * 历史 token 年龄与通知快照年龄取较大值，避免每轮重读把年龄重置为零。
     */
    static boolean isStaleFallback(boolean currentTokenAvailable, long fallbackTokenAgeMs,
                                   long notificationAgeMs) {
        if (currentTokenAvailable) {
            return notificationAgeMs < 0L || notificationAgeMs >= FALLBACK_TOKEN_STALE_MS;
        }
        long effectiveAgeMs = effectiveAgeMs(fallbackTokenAgeMs, notificationAgeMs);
        return effectiveAgeMs < 0L || effectiveAgeMs >= FALLBACK_TOKEN_STALE_MS;
    }

    /** 合并两个可未知的单调/墙钟年龄，任一未知时使用另一个。 */
    static long effectiveAgeMs(long tokenAgeMs, long notificationAgeMs) {
        if (tokenAgeMs < 0L) return notificationAgeMs;
        if (notificationAgeMs < 0L) return tokenAgeMs;
        return Math.max(tokenAgeMs, notificationAgeMs);
    }

    /** 只有自动上锁、明确交付、旧 token 且没有已上锁证据时才安排一次确认重试。 */
    static boolean shouldScheduleRetry(PassiveKeyLogic.Action action, boolean delivered,
                                       boolean currentTokenAvailable, long fallbackTokenAgeMs,
                                       boolean autoLock, String lockHint) {
        boolean staleToken = isStaleFallback(currentTokenAvailable, fallbackTokenAgeMs);
        return shouldScheduleRetry(action, delivered, staleToken, autoLock, lockHint);
    }

    /** 发送结果已判定 token 新鲜度时直接复用同一结论，避免第二次重读把旧通知改成“当前”。 */
    static boolean shouldScheduleRetry(PassiveKeyLogic.Action action, boolean delivered,
                                       boolean staleToken, boolean autoLock, String lockHint) {
        return action == PassiveKeyLogic.Action.LOCK
                && delivered
                && autoLock
                && !"LOCKED".equals(lockHint)
                && staleToken;
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
