package com.luca.geelykeyradar;

/** 通知监听失联恢复退避与用户介入状态回归。 */
public final class NotificationRecoveryPolicyTest {
    public static void main(String[] args) {
        retriesAreBoundedAndBackedOff();
        statusRequestsUserActionAfterTenSeconds();
        connectionAndAccessGatesWin();
        System.out.println("NotificationRecoveryPolicyTest passed");
    }

    private static void retriesAreBoundedAndBackedOff() {
        long now = 0L;
        long lastRequest = -1L;
        int attempts = 0;
        int requests = 0;
        for (int tick = 0; tick <= 360; tick++, now += 1_000L) {
            long sinceLast = lastRequest < 0L ? -1L : now - lastRequest;
            if (NotificationRecoveryPolicy.shouldRequestRebind(true, false, now, sinceLast, attempts)) {
                requests++;
                attempts++;
                lastRequest = now;
            }
        }
        check(requests >= 5, "六分钟内应保留若干次自动恢复机会");
        check(requests <= 7, "六分钟内不得像两秒轮询一样发出数十次 requestRebind");
        check(NotificationRecoveryPolicy.retryDelayMs(1) == 2_000L,
                "第一次重绑失败后应等待两秒");
        check(NotificationRecoveryPolicy.retryDelayMs(6) == NotificationRecoveryPolicy.LONG_RETRY_MS,
                "长期失败后退避到五分钟");
    }

    private static void statusRequestsUserActionAfterTenSeconds() {
        check("listener_recovery=rebinding".equals(
                        NotificationRecoveryPolicy.recoveryStatus(true, false, 9_999L)),
                "十秒内保持自动重绑状态");
        check("listener_recovery=needs_user_access_toggle".equals(
                        NotificationRecoveryPolicy.recoveryStatus(true, false, 10_000L)),
                "十秒后明确提示用户重新开关通知使用权");
    }

    private static void connectionAndAccessGatesWin() {
        check(!NotificationRecoveryPolicy.shouldRequestRebind(false, false, 60_000L, -1L, 0),
                "系统设置未授权时不应继续 requestRebind");
        check(!NotificationRecoveryPolicy.shouldRequestRebind(true, true, 60_000L, -1L, 0),
                "监听已连接时不得再重绑");
        check("listener_recovery=access_disabled".equals(
                        NotificationRecoveryPolicy.recoveryStatus(false, false, 60_000L)),
                "未授权状态必须可诊断");
        check("listener_recovery=connected".equals(
                        NotificationRecoveryPolicy.recoveryStatus(true, true, 60_000L)),
                "连接状态必须可诊断");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
