package com.luca.geelykeyradar;

/** 判断 BLE 扫描器是否只剩“已启动”状态而实际回调已经停滞。 */
final class ScanHealthPolicy {
    enum Action {
        NONE,
        RESTART,
        HARD_RESET
    }

    /** 后台扫描超过十秒仍无任何回调，说明系统扫描器可能已假在线。 */
    static final long STALE_CALLBACK_MS = 10_000L;
    /** 用户已亮屏时不再给后台静默停滞留十几秒，四秒即可重建。 */
    static final long INTERACTIVE_STALE_CALLBACK_MS = 4_000L;
    static final long RESTART_THROTTLE_MS = 8_000L;
    static final int HARD_RESET_AFTER_RESTARTS = 2;

    private ScanHealthPolicy() {
    }

    static Action evaluate(boolean scanning, boolean officialConnected,
                           boolean compatibilityCapture, long nowMs,
                           long scanStartedAtMs, long lastCallbackAtMs,
                           long lastRestartAtMs, int consecutiveStaleRestarts) {
        return evaluate(scanning, officialConnected, compatibilityCapture, false, false, nowMs,
                scanStartedAtMs, lastCallbackAtMs, lastRestartAtMs, consecutiveStaleRestarts);
    }

    /**
     * 扫描启动本身不能证明 BLE 仍在工作，只以本次启动后的真实回调年龄判断。
     * 普通后台先重建扫描器；连续第二次仍无回调时重新获取 Adapter/Scanner。
     * 亮屏/解锁是可恢复交互窗口：若四秒仍无回调，立即绕过节流做一次硬重建。
     */
    static Action evaluate(boolean scanning, boolean officialConnected,
                           boolean compatibilityCapture, boolean interactive,
                           boolean forceInteractive, long nowMs,
                           long scanStartedAtMs, long lastCallbackAtMs,
                           long lastRestartAtMs, int consecutiveStaleRestarts) {
        if (!scanning || officialConnected || compatibilityCapture) return Action.NONE;
        long lastEvidenceAtMs = Math.max(scanStartedAtMs, lastCallbackAtMs);
        long staleMs = interactive || forceInteractive
                ? INTERACTIVE_STALE_CALLBACK_MS : STALE_CALLBACK_MS;
        if (lastEvidenceAtMs <= 0L || nowMs - lastEvidenceAtMs < staleMs) {
            return Action.NONE;
        }
        if (!forceInteractive && lastRestartAtMs > 0L
                && nowMs - lastRestartAtMs < RESTART_THROTTLE_MS) {
            return Action.NONE;
        }
        if (forceInteractive) return Action.HARD_RESET;
        return consecutiveStaleRestarts + 1 >= HARD_RESET_AFTER_RESTARTS
                ? Action.HARD_RESET : Action.RESTART;
    }
}
