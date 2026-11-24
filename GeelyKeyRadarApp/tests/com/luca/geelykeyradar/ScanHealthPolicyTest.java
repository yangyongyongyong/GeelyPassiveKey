package com.luca.geelykeyradar;

/** BLE 扫描回调停滞、亮屏恢复与重建升级策略回归。 */
public final class ScanHealthPolicyTest {
    public static void main(String[] args) {
        recentCallbackIsHealthy();
        startTimeCannotMaskMissingCallbacks();
        staleCallbackEscalatesToHardReset();
        interactiveRecoveryIsFastAndForceful();
        throttleAndSafetyGatesAreHonored();
        System.out.println("ScanHealthPolicyTest passed");
    }

    private static void recentCallbackIsHealthy() {
        check(ScanHealthPolicy.evaluate(true, false, false, 100_000L,
                        10_000L, 95_000L, 0L, 0) == ScanHealthPolicy.Action.NONE,
                "十秒内有真实扫描回调时不得重建");
    }

    private static void startTimeCannotMaskMissingCallbacks() {
        check(ScanHealthPolicy.evaluate(true, false, false, 195_000L,
                        190_000L, 20_000L, 0L, 0) == ScanHealthPolicy.Action.NONE,
                "新一轮扫描启动后应重新计算回调窗口");
        check(ScanHealthPolicy.evaluate(true, false, false,
                        190_000L + ScanHealthPolicy.STALE_CALLBACK_MS, 190_000L, 20_000L,
                        0L, 0) == ScanHealthPolicy.Action.RESTART,
                "新扫描启动满十秒仍无回调时必须重建");
    }

    private static void staleCallbackEscalatesToHardReset() {
        check(ScanHealthPolicy.evaluate(true, false, false, 300_000L,
                        10_000L, 20_000L, 100_000L, 0) == ScanHealthPolicy.Action.RESTART,
                "第一次停滞应先普通重建扫描器");
        check(ScanHealthPolicy.evaluate(true, false, false, 300_000L,
                        190_000L, 20_000L, 100_000L, 1) == ScanHealthPolicy.Action.HARD_RESET,
                "连续第二次停滞应重新获取 Adapter/Scanner");
    }

    private static void interactiveRecoveryIsFastAndForceful() {
        long now = 200_000L;
        check(ScanHealthPolicy.evaluate(true, false, false, true, false, now,
                        10_000L, 20_000L, 0L, 0) == ScanHealthPolicy.Action.RESTART,
                "亮屏四秒无回调应快速普通重建");
        check(ScanHealthPolicy.evaluate(true, false, false, true, true, now,
                        190_000L, 20_000L, now - 1_000L, 0)
                        == ScanHealthPolicy.Action.HARD_RESET,
                "亮屏/解锁强制恢复必须绕过八秒节流并硬重建");
        check(ScanHealthPolicy.evaluate(true, false, false, true, false,
                        now, now - ScanHealthPolicy.INTERACTIVE_STALE_CALLBACK_MS + 1L,
                        20_000L, 0L, 0) == ScanHealthPolicy.Action.NONE,
                "亮屏后不足四秒仍应等待回调");
    }

    private static void throttleAndSafetyGatesAreHonored() {
        check(ScanHealthPolicy.evaluate(true, false, false, 150_000L,
                        10_000L, 20_000L, 145_000L, 0) == ScanHealthPolicy.Action.NONE,
                "八秒重建节流期内不得反复重启");
        check(ScanHealthPolicy.evaluate(false, false, false, 300_000L,
                        10_000L, 20_000L, 0L, 1) == ScanHealthPolicy.Action.NONE,
                "扫描未运行时不处理");
        check(ScanHealthPolicy.evaluate(true, true, false, 300_000L,
                        10_000L, 20_000L, 0L, 1) == ScanHealthPolicy.Action.NONE,
                "官方钥匙连接态由 GATT 测距接管，不重建广播扫描");
        check(ScanHealthPolicy.evaluate(true, false, true, 300_000L,
                        10_000L, 20_000L, 0L, 1) == ScanHealthPolicy.Action.NONE,
                "车型兼容原始抓取期间不得被普通看门狗打断");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
