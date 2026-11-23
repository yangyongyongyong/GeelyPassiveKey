package com.luca.geelykeyradar;

/** 旧通知 token 上锁确认重试的纯逻辑回归。 */
public final class LockDeliveryPolicyTest {
    public static void main(String[] args) {
        staleFallbackDetection();
        retryScheduling();
        retrySafetyGates();
        System.out.println("LockDeliveryPolicyTest passed");
    }

    private static void staleFallbackDetection() {
        check(LockDeliveryPolicy.isStaleFallback(false, -1L), "年龄未知的历史 token 必须视为旧 token");
        check(LockDeliveryPolicy.isStaleFallback(false, LockDeliveryPolicy.FALLBACK_TOKEN_STALE_MS),
                "一分钟及以上的历史 token 必须视为旧 token");
        check(!LockDeliveryPolicy.isStaleFallback(true, -1L), "当前通知提供的 token 不按历史年龄拦截");
        check(!LockDeliveryPolicy.isStaleFallback(false, 59_999L), "一分钟内的缓存 token 仍可直接交付");
        check(!LockDeliveryPolicy.isStaleFallback(true, 0L, 59_999L),
                "当前通知快照一分钟内仍是新鲜 token");
        check(LockDeliveryPolicy.isStaleFallback(true, 0L, LockDeliveryPolicy.FALLBACK_TOKEN_STALE_MS),
                "重新解析十二分钟前的通知不能把 token 年龄重置为零");
        check(LockDeliveryPolicy.isStaleFallback(false, 1_000L, 70_000L),
                "历史 token 与通知快照取较大年龄，任一过期都视为旧 token");
        check(LockDeliveryPolicy.isStaleFallback(false, 70_000L, 1_000L),
                "token 捕获年龄过期时通知刚重读也不能伪装成新鲜");
        check(LockDeliveryPolicy.effectiveAgeMs(4_800_000L, 720_000L) == 4_800_000L,
                "有效年龄必须取 token 与通知快照的较大值");
    }

    private static void retryScheduling() {
        check(LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, true, true, "UNKNOWN"),
                "旧缓存 token 且未确认锁态时应安排一次显式上锁重试");
        check(LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, true, true, ""),
                "持久化恢复的旧 token 也应安排重试");
        check(!LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.UNLOCK, true, true, true, true, "UNKNOWN"),
                "解锁绝不能重试");
        check(!LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, false, true, "UNKNOWN"),
                "当前 View/PI 成功交付时不得再次上锁");
        check(LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, true, true, "UNKNOWN"),
                "发送阶段已判定旧缓存 token 时应安排一次上锁复核");
        check(!LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, true, false, "UNKNOWN"),
                "用户关闭自动上锁后不得重试");
        check(!LockDeliveryPolicy.shouldScheduleRetry(
                        PassiveKeyLogic.Action.LOCK, true, true, true, true, "LOCKED"),
                "已观测到上锁状态时不得重试");
    }

    private static void retrySafetyGates() {
        check(LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        true, true, -93.0f, -85.0f, "UNKNOWN", LockDeliveryPolicy.RETRY_DELAY_MS),
                "仍远场、官方连接且信号新鲜时允许一次重试");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, true, true, true, false,
                        true, true, -93.0f, -85.0f, "UNKNOWN", 20_000L),
                "一次重试后不得继续发送");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        false, true, -93.0f, -85.0f, "UNKNOWN", 7_000L),
                "银河连接已断开时不得投递旧通知 token");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        true, false, -93.0f, -85.0f, "UNKNOWN", 7_000L),
                "信号已失效时不得重试");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        true, true, -70.0f, -85.0f, "UNKNOWN", 7_000L),
                "重新靠近车辆后不得补发上锁");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, true,
                        true, true, -93.0f, -85.0f, "UNKNOWN", 7_000L),
                "插线或行驶禁控时不得重试");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        true, true, -93.0f, -85.0f, "LOCKED", 7_000L),
                "出现已上锁确认后不得重试");
        check(!LockDeliveryPolicy.canRetryNow(
                        PassiveKeyLogic.Action.LOCK, false, true, true, false,
                        true, true, -93.0f, -85.0f, "UNKNOWN", 5_999L),
                "未到六秒确认窗口不得提前重试");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
