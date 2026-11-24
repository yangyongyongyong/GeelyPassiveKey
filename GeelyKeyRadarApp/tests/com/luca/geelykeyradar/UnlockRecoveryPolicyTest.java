package com.luca.geelykeyradar;

/** RSSI 二次解锁必须有新鲜且明确的锁态证据。 */
public final class UnlockRecoveryPolicyTest {
    public static void main(String[] args) {
        requiresExplicitFreshLocked();
        rejectsStaleOrMalformedClock();
        System.out.println("UnlockRecoveryPolicyTest passed");
    }

    private static void requiresExplicitFreshLocked() {
        check(UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "LOCKED", 1_000L, 2_000L),
                "新鲜 LOCKED 应允许纠偏解锁");
        check(!UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "UNLOCKED", 1_000L, 2_000L),
                "明确已解锁时不得重复解锁");
        check(!UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "UNKNOWN", 1_000L, 2_000L),
                "锁态未知时不得重复解锁");
        check(!UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "", 1_000L, 2_000L),
                "无锁态文本时不得重复解锁");
    }

    private static void rejectsStaleOrMalformedClock() {
        check(!UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "LOCKED", 1_000L, 1_000L + UnlockRecoveryPolicy.MAX_CONFIRMED_LOCK_AGE_MS + 1L),
                "过期 LOCKED 不得作为补解锁依据");
        check(UnlockRecoveryPolicy.hasFreshConfirmedLock(
                        "LOCKED", 1_000L, 1_000L + UnlockRecoveryPolicy.MAX_CONFIRMED_LOCK_AGE_MS),
                "边界内 LOCKED 仍可使用");
        check(!UnlockRecoveryPolicy.hasFreshConfirmedLock("LOCKED", 2_000L, 1_999L),
                "时间倒退或未知观测时间必须拒绝");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
