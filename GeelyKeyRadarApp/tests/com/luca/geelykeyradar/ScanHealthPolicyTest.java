package com.luca.geelykeyradar;

/** BLE 扫描回调停滞、离车退避、亮屏恢复与重建升级策略回归。 */
public final class ScanHealthPolicyTest {
    public static void main(String[] args) {
        recentCallbackIsHealthy();
        startTimeCannotMaskMissingCallbacks();
        staleCallbackEscalatesAndBacksOffUntilRealCallback();
        interactiveRecoveryProtectsFreshScanAndAvoidsDoubleFire();
        throttleAndSafetyGatesAreHonored();
        System.out.println("ScanHealthPolicyTest passed");
    }

    private static void recentCallbackIsHealthy() {
        check(ScanHealthPolicy.evaluate(true, false, false, 100_000L,
                        10_000L, 95_000L, 0L, 0) == ScanHealthPolicy.Action.NONE,
                "三十秒内有真实扫描回调时不得重建");
    }

    private static void startTimeCannotMaskMissingCallbacks() {
        check(ScanHealthPolicy.evaluate(true, false, false, 210_000L,
                        190_000L, 20_000L, 0L, 0) == ScanHealthPolicy.Action.NONE,
                "新扫描启动后未满三十秒不得误判停滞");
        check(ScanHealthPolicy.evaluate(true, false, false,
                        190_000L + ScanHealthPolicy.STALE_CALLBACK_MS, 190_000L, 20_000L,
                        0L, 0) == ScanHealthPolicy.Action.RESTART,
                "新扫描启动满三十秒仍无回调时应先普通重建");
    }

    private static void staleCallbackEscalatesAndBacksOffUntilRealCallback() {
        check(ScanHealthPolicy.evaluate(true, false, false, 300_000L,
                        10_000L, 20_000L, 100_000L, 0) == ScanHealthPolicy.Action.RESTART,
                "第一次停滞应先普通重建扫描器");
        check(ScanHealthPolicy.evaluate(true, false, false, 300_000L,
                        190_000L, 20_000L, 100_000L, 1) == ScanHealthPolicy.Action.HARD_RESET,
                "连续第二次停滞应升级为 HARD_RESET 重新获取 Adapter/Scanner");
        // HARD_RESET 后若仍零回调（例如长期离车），后台进入 60 秒退避，不再每 10 秒重启触发系统限频。
        long afterHardReset = 300_000L;
        check(ScanHealthPolicy.evaluate(true, false, false,
                        afterHardReset + 35_000L, afterHardReset, 20_000L, afterHardReset, 2)
                        == ScanHealthPolicy.Action.NONE,
                "HARD_RESET 后仍零回调时未满 180 秒必须退避静默，避免触发限频或走近时撞上重建窗口");
        check(ScanHealthPolicy.evaluate(true, false, false,
                        afterHardReset + ScanHealthPolicy.IDLE_BACKOFF_STALE_MS,
                        afterHardReset, 20_000L, afterHardReset, 2)
                        == ScanHealthPolicy.Action.RESTART,
                "离车退避满 180 秒后允许一次轻量巡检");
        check(ScanHealthPolicy.evaluate(true, false, false,
                        afterHardReset + ScanHealthPolicy.IDLE_BACKOFF_STALE_MS * 2L,
                        afterHardReset + ScanHealthPolicy.IDLE_BACKOFF_STALE_MS,
                        20_000L,
                        afterHardReset + ScanHealthPolicy.IDLE_BACKOFF_STALE_MS, 3)
                        == ScanHealthPolicy.Action.HARD_RESET,
                "退避周期内继续交替执行 RESTART 与 HARD_RESET");
    }

    private static void interactiveRecoveryProtectsFreshScanAndAvoidsDoubleFire() {
        long now = 200_000L;
        check(ScanHealthPolicy.evaluate(true, false, false, true, true, now,
                        now - 6_400L, 20_000L, now - 20_000L, 2)
                        == ScanHealthPolicy.Action.NONE,
                "6.4 秒前刚启动的新鲜扫描不得在亮屏瞬间被打断，避免连环重启引发系统 6 秒空白");
        check(ScanHealthPolicy.evaluate(true, false, false, true, true, now,
                        now - ScanHealthPolicy.INTERACTIVE_STALE_CALLBACK_MS, 20_000L,
                        now - ScanHealthPolicy.INTERACTIVE_FORCE_THROTTLE_MS, 2)
                        == ScanHealthPolicy.Action.HARD_RESET,
                "当前扫描静默满 8 秒且冷却已过时，亮屏应立即硬重建");
        check(ScanHealthPolicy.evaluate(true, false, false, true, true, now,
                        100_000L, 20_000L,
                        now - ScanHealthPolicy.INTERACTIVE_FORCE_THROTTLE_MS + 1L, 0)
                        == ScanHealthPolicy.Action.NONE,
                "8 秒冷却期内重复亮屏/解锁不得连续触发硬重建");
        check(ScanHealthPolicy.evaluate(true, false, false, true, false, now,
                        10_000L, 20_000L, 0L, 0) == ScanHealthPolicy.Action.RESTART,
                "亮屏状态满 8 秒无回调且不在冷却期时应快速普通重建");
    }

    private static void throttleAndSafetyGatesAreHonored() {
        check(ScanHealthPolicy.evaluate(true, false, false, 150_000L,
                        10_000L, 20_000L, 140_000L, 0) == ScanHealthPolicy.Action.NONE,
                "25 秒后台重建节流期内不得反复重启");
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
