package com.luca.geelykeyradar;

/** BLE 扫描错误码恢复策略回归。 */
public final class ScanFailurePolicyTest {
    public static void main(String[] args) {
        hardFailuresResetProxy();
        frequencyFailureUsesLongBackoff();
        ordinaryFailureUsesShortRetry();
        System.out.println("ScanFailurePolicyTest passed");
    }

    private static void hardFailuresResetProxy() {
        int[] codes = {
                ScanFailurePolicy.ERROR_APPLICATION_REGISTRATION_FAILED,
                ScanFailurePolicy.ERROR_INTERNAL,
                ScanFailurePolicy.ERROR_OUT_OF_HARDWARE_RESOURCES
        };
        for (int code : codes) {
            check(ScanFailurePolicy.requiresHardReset(code),
                    "注册/内部/资源错误必须硬恢复 code=" + code);
            check(ScanFailurePolicy.retryDelayMs(code) == ScanFailurePolicy.HARD_RESET_RETRY_MS,
                    "硬恢复必须等待三秒 code=" + code);
        }
    }

    private static void frequencyFailureUsesLongBackoff() {
        check(!ScanFailurePolicy.requiresHardReset(
                        ScanFailurePolicy.ERROR_SCANNING_TOO_FREQUENTLY),
                "扫描过频不应立即硬恢复");
        check(ScanFailurePolicy.retryDelayMs(
                        ScanFailurePolicy.ERROR_SCANNING_TOO_FREQUENTLY)
                        == ScanFailurePolicy.TOO_FREQUENT_RETRY_MS,
                "扫描过频必须使用六点五秒退避");
    }

    private static void ordinaryFailureUsesShortRetry() {
        check(!ScanFailurePolicy.requiresHardReset(ScanFailurePolicy.ERROR_ALREADY_STARTED),
                "已启动/特性不支持等错误只做普通重试");
        check(ScanFailurePolicy.retryDelayMs(ScanFailurePolicy.ERROR_ALREADY_STARTED)
                        == ScanFailurePolicy.NORMAL_RETRY_MS,
                "普通错误保持一点八秒重试");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
