package com.luca.geelykeyradar;

/** Android ScanCallback 错误码的恢复强度与重试间隔。 */
final class ScanFailurePolicy {
    static final int ERROR_ALREADY_STARTED = 1;
    static final int ERROR_APPLICATION_REGISTRATION_FAILED = 2;
    static final int ERROR_INTERNAL = 3;
    static final int ERROR_OUT_OF_HARDWARE_RESOURCES = 5;
    static final int ERROR_SCANNING_TOO_FREQUENTLY = 6;
    static final long HARD_RESET_RETRY_MS = 3_000L;
    static final long NORMAL_RETRY_MS = 1_800L;
    static final long TOO_FREQUENT_RETRY_MS = 6_500L;

    private ScanFailurePolicy() {
    }

    /** 注册、内部错误和硬件资源耗尽必须重新取得 Adapter/Scanner 代理。 */
    static boolean requiresHardReset(int errorCode) {
        return errorCode == ERROR_APPLICATION_REGISTRATION_FAILED
                || errorCode == ERROR_INTERNAL
                || errorCode == ERROR_OUT_OF_HARDWARE_RESOURCES;
    }

    static long retryDelayMs(int errorCode) {
        if (errorCode == ERROR_SCANNING_TOO_FREQUENTLY) return TOO_FREQUENT_RETRY_MS;
        if (requiresHardReset(errorCode)) return HARD_RESET_RETRY_MS;
        return NORMAL_RETRY_MS;
    }
}
