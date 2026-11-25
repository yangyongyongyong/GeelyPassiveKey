package com.luca.geelykeyradar;

/** 亮屏/解锁补拉与未安装银河 App 时的唤醒拦截策略。 */
final class InteractiveWakePolicy {
    static final long MAX_AD_AGE_MS = 30_000L;
    static final long MIN_RETRY_GAP_MS = 5_000L;
    static final long UNAVAILABLE_LOG_GAP_MS = 60_000L;

    private InteractiveWakePolicy() {
    }

    static boolean shouldWake(boolean serviceEnabled, boolean officialConnected,
                              boolean vehicleAdvertisementAvailable, boolean aclSeen,
                              long advertisementAgeMs, long sinceLastAttemptMs) {
        return shouldWake(serviceEnabled, true, officialConnected, vehicleAdvertisementAvailable,
                aclSeen, advertisementAgeMs, sinceLastAttemptMs);
    }

    static boolean shouldWake(boolean serviceEnabled, boolean officialAppInstalled,
                              boolean officialConnected, boolean vehicleAdvertisementAvailable,
                              boolean aclSeen, long advertisementAgeMs, long sinceLastAttemptMs) {
        if (!serviceEnabled || !officialAppInstalled || officialConnected
                || !vehicleAdvertisementAvailable || aclSeen) {
            return false;
        }
        if (advertisementAgeMs < 0L || advertisementAgeMs > MAX_AD_AGE_MS) return false;
        return sinceLastAttemptMs < 0L || sinceLastAttemptMs >= MIN_RETRY_GAP_MS;
    }

    /** 未检测到银河 App 时限频记录诊断，不再每轮重复注入与起快扫。 */
    static boolean shouldLogUnavailable(long lastLoggedAtMs, long nowMs) {
        return lastLoggedAtMs <= 0L || nowMs < lastLoggedAtMs
                || (nowMs - lastLoggedAtMs) >= UNAVAILABLE_LOG_GAP_MS;
    }
}
