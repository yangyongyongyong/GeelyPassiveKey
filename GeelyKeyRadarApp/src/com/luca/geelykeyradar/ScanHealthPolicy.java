package com.luca.geelykeyradar;

/** 判断 BLE 扫描器是否只剩“已启动”状态而实际回调已经停滞，并防止离车期间过频重启触发系统扫描限频。 */
final class ScanHealthPolicy {
    enum Action {
        NONE,
        RESTART,
        HARD_RESET
    }

    /** 后台首次停滞检查放宽到 30 秒，确保 30 秒滑窗内最多重建一次，远离 Android 5次/30秒 限频阈值。 */
    static final long STALE_CALLBACK_MS = 30_000L;
    /** 连续 RESTART+HARD_RESET 仍零回调说明大概率离车，后台退避到 60 秒一次。 */
    static final long IDLE_BACKOFF_STALE_MS = 60_000L;
    /** 亮屏/解锁后若当前扫描已静默满 8 秒才重建；刚启动不足 8 秒的扫描不再被打断。 */
    static final long INTERACTIVE_STALE_CALLBACK_MS = 8_000L;
    static final long RESTART_THROTTLE_MS = 25_000L;
    static final long INTERACTIVE_FORCE_THROTTLE_MS = 8_000L;
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
     * 后台首次停滞先 RESTART，连续第二次升级 HARD_RESET；若仍零回调则进入 60 秒离车退避，
     * 只在真正收到扫描回调后才退出退避。亮屏强制恢复也遵守 8 秒冷却与新鲜扫描保护。
     */
    static Action evaluate(boolean scanning, boolean officialConnected,
                           boolean compatibilityCapture, boolean interactive,
                           boolean forceInteractive, long nowMs,
                           long scanStartedAtMs, long lastCallbackAtMs,
                           long lastRestartAtMs, int consecutiveStaleRestarts) {
        if (!scanning || officialConnected || compatibilityCapture) return Action.NONE;
        long lastEvidenceAtMs = Math.max(scanStartedAtMs, lastCallbackAtMs);
        boolean idleBackedOff = consecutiveStaleRestarts >= HARD_RESET_AFTER_RESTARTS;
        long staleMs;
        if (forceInteractive) {
            staleMs = INTERACTIVE_STALE_CALLBACK_MS;
        } else if (idleBackedOff) {
            staleMs = IDLE_BACKOFF_STALE_MS;
        } else if (interactive) {
            staleMs = INTERACTIVE_STALE_CALLBACK_MS;
        } else {
            staleMs = STALE_CALLBACK_MS;
        }
        if (lastEvidenceAtMs <= 0L || nowMs - lastEvidenceAtMs < staleMs) {
            return Action.NONE;
        }
        long throttleMs = forceInteractive
                ? INTERACTIVE_FORCE_THROTTLE_MS
                : (idleBackedOff ? IDLE_BACKOFF_STALE_MS : RESTART_THROTTLE_MS);
        if (lastRestartAtMs > 0L && nowMs - lastRestartAtMs < throttleMs) {
            return Action.NONE;
        }
        if (forceInteractive) return Action.HARD_RESET;
        return ((consecutiveStaleRestarts + 1) % HARD_RESET_AFTER_RESTARTS) == 0
                ? Action.HARD_RESET : Action.RESTART;
    }
}
