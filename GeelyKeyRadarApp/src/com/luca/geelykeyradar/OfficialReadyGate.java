package com.luca.geelykeyradar;

/** 官方通知就绪与可信地址可能先后到达，每次真实连接只提供一次就绪机会。 */
final class OfficialReadyGate {
    private boolean connected;
    private boolean handled;

    /** 只有真实连接边沿才重置预算，重复通知不能伪造第二次靠近机会。 */
    void onConnectionChanged(boolean nowConnected) {
        if (connected == nowConnected) return;
        connected = nowConnected;
        handled = false;
    }

    /** 地址尚未验证时不消费机会；稍后实际连接证据到达即可补处理一次。 */
    boolean claim(boolean identityComplete, boolean serviceEnabled) {
        if (!connected || handled || !identityComplete || !serviceEnabled) return false;
        handled = true;
        return true;
    }
}
