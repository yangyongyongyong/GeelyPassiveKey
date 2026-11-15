package com.luca.geelykeyradar;

/** 通知就绪与真实地址交错回归，验证首次学习不漏解锁且重复通知不重复车控。 */
public final class OfficialReadyGateTest {
    /** 运行两种证据到达顺序、重复通知、启停以及重连测试。 */
    public static void main(String[] args) {
        delayedIdentityCanClaimOnce();
        existingIdentityAndDuplicateNotifications();
        switchAndReconnectResetBudget();
        System.out.println("OfficialReadyGateTest passed");
    }

    /** 官方通知先到而地址还不可验证，不消耗机会，稍后连接证据到达时仍可处理。 */
    private static void delayedIdentityCanClaimOnce() {
        OfficialReadyGate gate = new OfficialReadyGate();
        gate.onConnectionChanged(true);
        check(!gate.claim(false, true), "未验证地址时不执行连接即刻解锁");
        check(gate.claim(true, true), "真实地址晚到仍能补处理本次唯一机会");
        check(!gate.claim(true, true), "反复采用同一连接证据不重复处理");
    }

    /** 已学习身份在真实连接边沿立即可用，重复通知不重置门控。 */
    private static void existingIdentityAndDuplicateNotifications() {
        OfficialReadyGate gate = new OfficialReadyGate();
        check(!gate.claim(true, true), "官方未连接不能使用旧身份执行");
        gate.onConnectionChanged(true);
        check(gate.claim(true, true), "已有身份首次官方就绪立即处理");
        gate.onConnectionChanged(true);
        check(!gate.claim(true, true), "重复已连接通知不产生第二次就绪机会");
    }

    /** 总开关关闭不能消耗机会，实际断连再重连后为下一次连接重置预算。 */
    private static void switchAndReconnectResetBudget() {
        OfficialReadyGate gate = new OfficialReadyGate();
        gate.onConnectionChanged(true);
        check(!gate.claim(true, false), "服务关闭时不执行车控");
        check(gate.claim(true, true), "仍是本次真实连接，开启后只可处理一次");
        gate.onConnectionChanged(false);
        check(!gate.claim(true, true), "断连状态拒绝旧通知机会");
        gate.onConnectionChanged(true);
        check(gate.claim(true, true), "下一次真实连接有新的单次机会");
    }

    /** 断言失败时输出连接时序含义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
