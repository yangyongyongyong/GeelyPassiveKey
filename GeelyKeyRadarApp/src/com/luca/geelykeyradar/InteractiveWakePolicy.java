package com.luca.geelykeyradar;

/** 亮屏/解锁时，仅对刚看到且尚未建立 ACL 的本车广播补拉一次官方连接。 */
final class InteractiveWakePolicy {
    static final long MAX_AD_AGE_MS = 30_000L;
    static final long MIN_RETRY_GAP_MS = 5_000L;

    private InteractiveWakePolicy() {
    }

    static boolean shouldWake(boolean serviceEnabled, boolean officialConnected,
                              boolean vehicleAdvertisementAvailable, boolean aclSeen,
                              long advertisementAgeMs, long sinceLastAttemptMs) {
        if (!serviceEnabled || officialConnected || !vehicleAdvertisementAvailable || aclSeen) return false;
        if (advertisementAgeMs < 0L || advertisementAgeMs > MAX_AD_AGE_MS) return false;
        return sinceLastAttemptMs < 0L || sinceLastAttemptMs >= MIN_RETRY_GAP_MS;
    }
}
