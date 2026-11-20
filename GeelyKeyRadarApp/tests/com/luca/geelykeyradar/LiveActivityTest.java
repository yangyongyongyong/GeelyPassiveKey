package com.luca.geelykeyradar;

/** 顶部实时行为的纯状态回归，不依赖 Android 服务或真实蓝牙。 */
public final class LiveActivityTest {
    /** 覆盖空闲、发现、鉴权、待发、发送结果、测距和上锁确认。 */
    public static void main(String[] args) {
        idleAndDiscovery();
        wakeAndAuthentication();
        pendingAndSending();
        widgetFallbackStates();
        connectedMonitoringAndDeparture();
        safetyBlocksTakePriority();
        tracePreservesShortStages();
        System.out.println("LiveActivityTest passed");
    }

    private static void idleAndDiscovery() {
        LiveActivity.Input idle = new LiveActivity.Input();
        LiveActivity.Result waiting = LiveActivity.resolve(idle);
        check(waiting.stage == LiveActivity.Stage.WAITING_SIGNAL
                && waiting.title.contains("等待车辆蓝牙信号"), "空闲时明确显示等待车辆信号");

        idle.signalFresh = true;
        idle.rssi = -84;
        check(LiveActivity.resolve(idle).stage == LiveActivity.Stage.WAKING_OFFICIAL,
                "达到连接阈值后显示准备连接");

        idle.wakeActive = true;
        idle.wakeAgeMs = 500L;
        LiveActivity.Result waking = LiveActivity.resolve(idle);
        check(waking.stage == LiveActivity.Stage.WAKING_OFFICIAL
                && waking.title.contains("正在唤醒银河"), "收到广播后显示唤醒银河");
    }

    private static void wakeAndAuthentication() {
        LiveActivity.Input input = new LiveActivity.Input();
        input.wakeActive = true;
        input.aclRecent = true;
        LiveActivity.Result auth = LiveActivity.resolve(input);
        check(auth.stage == LiveActivity.Stage.WAITING_AUTH
                && auth.detail.contains("V2"), "ACL建立后显示等待官方鉴权");
    }

    private static void pendingAndSending() {
        LiveActivity.Input input = new LiveActivity.Input();
        input.pendingUnlock = true;
        input.geelyConnected = true;
        input.gattConnected = true;
        input.pendingWaitReason = "WAIT_BUTTON";
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.WAITING_UNLOCK_BUTTON,
                "官方在线但按钮未就绪时显示等待解锁按钮");

        input.dispatchAgeMs = 100L;
        input.dispatchAction = PassiveKeyLogic.Action.UNLOCK;
        LiveActivity.Result sendingUnlock = LiveActivity.resolve(input);
        check(sendingUnlock.stage == LiveActivity.Stage.SENDING_UNLOCK
                && sendingUnlock.title.contains("正在发送解锁"), "发送中优先显示正在解锁");

        input.dispatchAction = PassiveKeyLogic.Action.LOCK;
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.SENDING_LOCK,
                "发送中优先显示正在上锁");

        input.dispatchAgeMs = -1L;
        input.lastSentAction = PassiveKeyLogic.Action.UNLOCK;
        input.lastSentAgeMs = 500L;
        LiveActivity.Result sent = LiveActivity.resolve(input);
        check(sent.stage == LiveActivity.Stage.UNLOCK_SENT
                && sent.detail.contains("不是车锁执行回执"), "交付银河后不冒充车辆执行成功");

        input.lastControlResult = "UNKNOWN";
        LiveActivity.Result uncertain = LiveActivity.resolve(input);
        check(uncertain.stage == LiveActivity.Stage.RESULT_UNKNOWN
                && uncertain.title.contains("结果不确定") && uncertain.tone == LiveActivity.Tone.WARNING,
                "未知回执必须显示待核实而不是成功");
    }

    private static void connectedMonitoringAndDeparture() {
        LiveActivity.Input input = new LiveActivity.Input();
        input.geelyConnected = true;
        input.gattConnected = true;
        input.lastSentAction = PassiveKeyLogic.Action.UNLOCK;
        input.lastSentAgeMs = LiveActivity.RESULT_VISIBLE_MS + 1L;
        input.sampleDecision = "APPROACH_LATCHED";
        LiveActivity.Result monitoring = LiveActivity.resolve(input);
        check(monitoring.stage == LiveActivity.Stage.MONITORING_DEPARTURE
                && monitoring.title.contains("监测离开"), "解锁完成后显示监测离开");

        input.lockDwellSamples = 2;
        input.lockDwellElapsedMs = 1000L;
        input.lockDwellMs = 2000L;
        LiveActivity.Result departure = LiveActivity.resolve(input);
        check(departure.stage == LiveActivity.Stage.CONFIRMING_DEPARTURE
                && departure.detail.contains("1.0 / 2.0"), "远场驻留显示确认进度");
    }

    private static void widgetFallbackStates() {
        LiveActivity.Input input = new LiveActivity.Input();
        input.listenerConnected = false;
        input.widgetFallbackEnabled = true;
        LiveActivity.Result fallback = LiveActivity.resolve(input);
        check(fallback.stage == LiveActivity.Stage.LISTENER_RECONNECTING
                && fallback.title.contains("小组件备用"), "通知掉绑但启用Widget时显示受限备用状态");

        input = new LiveActivity.Input();
        input.pendingUnlock = true;
        input.geelyConnected = true;
        input.gattConnected = true;
        input.widgetFallbackEnabled = true;
        input.pendingWaitReason = "WAIT_WIDGET_STATE";
        LiveActivity.Result stale = LiveActivity.resolve(input);
        check(stale.stage == LiveActivity.Stage.WAITING_WIDGET_STATE
                && stale.tone == LiveActivity.Tone.WARNING, "Widget锁态未知或过期时明确等待");

        input.pendingWaitReason = "WAIT_WIDGET_TOKEN";
        input.widgetStatus = "等待官方车锁控件";
        LiveActivity.Result token = LiveActivity.resolve(input);
        check(token.stage == LiveActivity.Stage.WAITING_WIDGET_TOKEN
                && token.detail.contains("等待官方车锁控件"), "Widget token缺失时显示真实桥接状态");
    }

    private static void safetyBlocksTakePriority() {
        LiveActivity.Input input = new LiveActivity.Input();
        input.chargingBlocked = true;
        input.pendingUnlock = true;
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.CHARGING_BLOCKED,
                "插线禁控优先于待发解锁");

        input.chargingBlocked = false;
        input.listenerConnected = false;
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.LISTENER_RECONNECTING,
                "通知桥接掉绑优先显示重连状态");

        input.listenerConnected = true;
        input.driving = true;
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.DRIVING_BLOCKED,
                "行驶保护显示在实时状态顶部");
    }

    private static void tracePreservesShortStages() {
        LiveActivityTrace trace = new LiveActivityTrace();
        trace.add(LiveActivityTrace.Step.SCAN_FOUND, 1_000L, "rssi=-84");
        trace.add(LiveActivityTrace.Step.WAKE_REQUEST, 1_100L, "rssi=-84");
        trace.add(LiveActivityTrace.Step.ACL_CONNECTED, 2_500L, "acl");
        trace.add(LiveActivityTrace.Step.OFFICIAL_CONNECTED, 4_000L, "ready");
        trace.add(LiveActivityTrace.Step.UNLOCK_REQUESTED, 4_050L, "prepare");
        trace.add(LiveActivityTrace.Step.UNLOCK_SENT, 4_100L, "sent");
        LiveActivity.Input input = new LiveActivity.Input();
        input.trace = trace.snapshot();
        input.traceNowMs = 4_200L;
        LiveActivity.Result result = LiveActivity.resolve(input);
        check(result.stage == LiveActivity.Stage.UNLOCK_SENT, "短阶段轨迹应优先显示已发送");
        String recentTrace = LiveActivity.traceText(input.trace);
        check(recentTrace.contains("鉴权") && recentTrace.contains("解锁")
                && recentTrace.contains("已发送"), "轨迹文本应保留最近四步");
        input.traceNowMs = 40_000L;
        LiveActivity.Result stale = LiveActivity.resolve(input);
        check(stale.stage != LiveActivity.Stage.UNLOCK_SENT
                && stale.stage != LiveActivity.Stage.PREPARING_UNLOCK,
                "超过30秒的旧轨迹不能冒充当前动作");
        trace.add(LiveActivityTrace.Step.SCAN_FOUND, 40_001L, "新发现");
        trace.add(LiveActivityTrace.Step.WAKE_REQUEST, 40_100L, "新唤醒");
        input.trace = trace.snapshot();
        input.traceNowMs = 40_200L;
        check(LiveActivity.resolve(input).stage == LiveActivity.Stage.WAKING_OFFICIAL,
                "短阶段轨迹可显示发现后的唤醒步骤");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
