package com.luca.geelykeyradar;

/** RSSI 二次靠近解锁只允许建立在新鲜、明确的“已上锁”证据上。 */
final class UnlockRecoveryPolicy {
    static final long MAX_CONFIRMED_LOCK_AGE_MS = 90_000L;

    private UnlockRecoveryPolicy() {
    }

    /**
     * 官方通知/小组件文本是当前唯一可信锁态来源。
     * UNKNOWN、UNLOCKED、空值或过期 LOCKED 都不能作为补发第二次解锁的依据。
     */
    static boolean hasFreshConfirmedLock(String lockHint, long lockHintAtMs, long nowMs) {
        return "LOCKED".equals(lockHint)
                && lockHintAtMs > 0L
                && nowMs >= lockHintAtMs
                && (nowMs - lockHintAtMs) <= MAX_CONFIRMED_LOCK_AGE_MS;
    }
}
