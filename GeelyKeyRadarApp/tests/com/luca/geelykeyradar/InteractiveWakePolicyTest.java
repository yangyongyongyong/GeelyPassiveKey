package com.luca.geelykeyradar;

/** 亮屏补拉官方连接与未安装银河拦截边界回归。 */
public final class InteractiveWakePolicyTest {
    public static void main(String[] args) {
        wakesOnlyForFreshUnconnectedCar();
        missingOfficialAppBlocksWakeAndThrottlesDiagnostic();
        honorsRetryGap();
        System.out.println("InteractiveWakePolicyTest passed");
    }

    private static void wakesOnlyForFreshUnconnectedCar() {
        check(InteractiveWakePolicy.shouldWake(true, true, false, true, false, 1_000L, -1L),
                "刚看到本车、银河已安装且未建立 ACL 时应补拉");
        check(!InteractiveWakePolicy.shouldWake(false, true, false, true, false, 1_000L, -1L),
                "服务关闭时不得唤醒官方 App");
        check(!InteractiveWakePolicy.shouldWake(true, true, true, true, false, 1_000L, -1L),
                "银河已连接时不得重复唤醒");
        check(!InteractiveWakePolicy.shouldWake(true, true, false, false, false, 1_000L, -1L),
                "没有本车广播时不得盲拉官方 App");
        check(!InteractiveWakePolicy.shouldWake(true, true, false, true, true, 1_000L, -1L),
                "ACL 已出现时应等待鉴权而不是重复拉起");
        check(!InteractiveWakePolicy.shouldWake(
                        true, true, false, true, false, InteractiveWakePolicy.MAX_AD_AGE_MS + 1L, -1L),
                "过期广播不得用于亮屏补拉");
    }

    private static void missingOfficialAppBlocksWakeAndThrottlesDiagnostic() {
        check(!InteractiveWakePolicy.shouldWake(true, false, false, true, false, 1_000L, -1L),
                "当前 Android 容器未检测到银河 App 时不得触发唤醒");
        check(InteractiveWakePolicy.shouldLogUnavailable(0L, 1_000L),
                "首次未安装拦截应记录诊断");
        check(!InteractiveWakePolicy.shouldLogUnavailable(
                        1_000L, 1_000L + InteractiveWakePolicy.UNAVAILABLE_LOG_GAP_MS - 1L),
                "一分钟内不应刷屏 WAKE_UNAVAILABLE");
        check(InteractiveWakePolicy.shouldLogUnavailable(
                        1_000L, 1_000L + InteractiveWakePolicy.UNAVAILABLE_LOG_GAP_MS),
                "满一分钟后允许再次记录诊断");
    }

    private static void honorsRetryGap() {
        check(!InteractiveWakePolicy.shouldWake(
                        true, true, false, true, false, 1_000L, InteractiveWakePolicy.MIN_RETRY_GAP_MS - 1L),
                "五秒节流内不得重复补拉");
        check(InteractiveWakePolicy.shouldWake(
                        true, true, false, true, false, 1_000L, InteractiveWakePolicy.MIN_RETRY_GAP_MS),
                "达到五秒后允许下一次补拉");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
