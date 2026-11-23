package com.luca.geelykeyradar;

/** 判断 BLE 扫描器是否只剩“已启动”状态而实际回调已经停滞。 */
final class ScanHealthPolicy {
    enum Action {
        NONE,
        RESTART,
        HARD_RESET
    }

    static final long STALE_CALLBACK_MS = 45_000L;
    static final long RESTART_THROTTLE_MS = 90_000L;
    static final int HARD_RESET_AFTER_RESTARTS = 2;

    private ScanHealthPolicy() {
    }

    /**
     * 扫描启动本身不能证明 BLE 仍在工作，只以本次启动后的真实回调年龄判断。
     * 第一次停滞重建扫描器；连续第二次仍无任何回调时重新获取 Adapter/Scanner。
     */
    static Action evaluate(boolean scanning, boolean officialConnected,
                           boolean compatibilityCapture, long nowMs,
                           long scanStartedAtMs, long lastCallbackAtMs,
                           long lastRestartAtMs, int consecutiveStaleRestarts) {
        if (!scanning || officialConnected || compatibilityCapture) return Action.NONE;
        long lastEvidenceAtMs = Math.max(scanStartedAtMs, lastCallbackAtMs);
        if (lastEvidenceAtMs <= 0L || nowMs - lastEvidenceAtMs < STALE_CALLBACK_MS) {
            return Action.NONE;
        }
        if (lastRestartAtMs > 0L && nowMs - lastRestartAtMs < RESTART_THROTTLE_MS) {
            return Action.NONE;
        }
        return consecutiveStaleRestarts + 1 >= HARD_RESET_AFTER_RESTARTS
                ? Action.HARD_RESET : Action.RESTART;
    }
}
