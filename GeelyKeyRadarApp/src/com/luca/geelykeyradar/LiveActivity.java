package com.luca.geelykeyradar;

import java.util.List;
import java.util.Locale;

/** 把服务内部阶段映射成用户能直接看懂的“现在正在做什么”。 */
final class LiveActivity {
    static final long SENDING_VISIBLE_MS = 800L;
    static final long RESULT_VISIBLE_MS = 5000L;
    static final long WAKE_VISIBLE_MS = 15000L;

    enum Tone {
        IDLE,
        PROGRESS,
        SUCCESS,
        WARNING,
        ERROR
    }

    enum Stage {
        STARTING,
        PAUSED,
        BLUETOOTH_OFF,
        NOTIFICATION_ACCESS,
        LISTENER_RECONNECTING,
        CHARGING_BLOCKED,
        DRIVING_BLOCKED,
        SENDING_UNLOCK,
        SENDING_LOCK,
        UNLOCK_SENT,
        LOCK_SENT,
        RESULT_UNKNOWN,
        CONNECTING_OFFICIAL,
        WAITING_AUTH,
        WAITING_UNLOCK_BUTTON,
        WAITING_WIDGET_TOKEN,
        WAITING_WIDGET_STATE,
        CONNECTING_GATT,
        PREPARING_UNLOCK,
        PREPARING_LOCK,
        CONFIRMING_DEPARTURE,
        MONITORING_DEPARTURE,
        MEASURING,
        WAKING_OFFICIAL,
        WAITING_STRONGER_SIGNAL,
        LEARNING_VEHICLE,
        WAITING_SIGNAL,
        RECOVERING_SCAN
    }

    static final class Input {
        boolean running = true;
        boolean serviceEnabled = true;
        boolean bluetoothOn = true;
        boolean notificationAccess = true;
        boolean listenerConnected = true;
        boolean chargingBlocked;
        boolean driving;
        boolean identityComplete = true;
        boolean scanning = true;
        boolean geelyConnected;
        boolean gattConnected;
        boolean wakeActive;
        boolean aclRecent;
        boolean signalFresh;
        boolean pendingUnlock;
        boolean pendingLock;
        boolean widgetFallbackEnabled;
        boolean widgetReady;
        int rssi = -127;
        float connectDbm = PassiveKeyDefaults.CONNECT_DBM;
        long wakeAgeMs = -1L;
        long lastSentAgeMs = -1L;
        long dispatchAgeMs = -1L;
        long lockDwellElapsedMs = -1L;
        long lockDwellMs = PassiveKeyDefaults.LOCK_DWELL_MS;
        int lockDwellSamples;
        PassiveKeyLogic.Action lastSentAction = PassiveKeyLogic.Action.NONE;
        PassiveKeyLogic.Action dispatchAction = PassiveKeyLogic.Action.NONE;
        String lastControlResult = "NONE";
        String lastControlSource = "NONE";
        String widgetStatus = "";
        String widgetDiagnostic = "";
        String pendingWaitReason = "";
        String sampleDecision = "";
        String zone = "";
        List<LiveActivityTrace.Entry> trace = java.util.Collections.emptyList();
        long traceNowMs;
    }

    static final class Result {
        final Stage stage;
        final String badge;
        final String title;
        final String detail;
        final Tone tone;

        Result(Stage stage, String badge, String title, String detail, Tone tone) {
            this.stage = stage;
            this.badge = badge;
            this.title = title;
            this.detail = detail;
            this.tone = tone;
        }
    }

    static Result resolve(Input in) {
        Input value = in == null ? new Input() : in;
        Result traced = resolveTrace(value);
        if (traced != null) return traced;
        if (!value.running) {
            return result(Stage.STARTING, "启动", "正在启动无感服务",
                    "正在初始化蓝牙、通知桥接与前台服务", Tone.PROGRESS);
        }
        if (!value.serviceEnabled) {
            return result(Stage.PAUSED, "暂停", "无感服务已暂停",
                    "打开自动运行后恢复扫描和自动解闭锁", Tone.WARNING);
        }
        if (!value.bluetoothOn) {
            return result(Stage.BLUETOOTH_OFF, "蓝牙", "手机蓝牙已关闭",
                    "打开蓝牙后才能发现车辆", Tone.WARNING);
        }
        if (!value.notificationAccess) {
            if (value.widgetFallbackEnabled) {
                return result(Stage.NOTIFICATION_ACCESS, "备用", "通知使用权不可用，小组件备用受限",
                        "只有官方锁态新鲜时才能用小组件；建议到维护页修复通知监听", Tone.WARNING);
            }
            return result(Stage.NOTIFICATION_ACCESS, "权限", "缺少通知使用权",
                    "需要读取银河通知按钮才能发送解锁或上锁", Tone.ERROR);
        }
        if (!value.listenerConnected) {
            if (value.widgetFallbackEnabled) {
                return result(Stage.LISTENER_RECONNECTING, "备用", "通知桥接正在重连，小组件备用待命",
                        value.widgetReady
                                ? "小组件 token 已捕获；仍需新鲜锁态且目标相反才会单次发送"
                                : "正在等待官方小组件 token；未就绪不会发送备用控制", Tone.WARNING);
            }
            return result(Stage.LISTENER_RECONNECTING, "重连", "通知桥接正在重新连接",
                    "系统正在恢复银河通知监听；持续失败请运行全检", Tone.WARNING);
        }
        if (value.chargingBlocked) {
            return result(Stage.CHARGING_BLOCKED, "禁控", "插线禁控中",
                    "拔掉数据线后恢复自动和手动解闭锁", Tone.WARNING);
        }
        if (value.driving) {
            return result(Stage.DRIVING_BLOCKED, "保护", "车辆行驶中",
                    "已暂停自动解闭锁，停车后自动恢复监测", Tone.WARNING);
        }
        if (value.dispatchAgeMs >= 0L && value.dispatchAgeMs <= SENDING_VISIBLE_MS
                && value.dispatchAction != PassiveKeyLogic.Action.NONE) {
            String transport = sourceText(value.lastControlSource);
            return value.dispatchAction == PassiveKeyLogic.Action.LOCK
                    ? result(Stage.SENDING_LOCK, "上锁", "正在发送上锁指令",
                    "正在通过" + transport + "发送", Tone.PROGRESS)
                    : result(Stage.SENDING_UNLOCK, "解锁", "正在发送解锁指令",
                    "正在通过" + transport + "发送", Tone.PROGRESS);
        }
        if (value.lastSentAgeMs >= 0L && value.lastSentAgeMs <= RESULT_VISIBLE_MS) {
            if ("UNKNOWN".equals(value.lastControlResult)) {
                String action = value.lastSentAction == PassiveKeyLogic.Action.LOCK ? "上锁" : "解锁";
                return result(Stage.RESULT_UNKNOWN, "待核实", action + "结果不确定",
                        "已交给" + sourceText(value.lastControlSource)
                                + "，未收到车锁回执，请观察车辆确认", Tone.WARNING);
            }
            if (value.lastSentAction == PassiveKeyLogic.Action.UNLOCK) {
                return result(Stage.UNLOCK_SENT, "已发送", "解锁指令已交给银河",
                        "等待车辆执行；这里不是车锁执行回执", Tone.SUCCESS);
            }
            if (value.lastSentAction == PassiveKeyLogic.Action.LOCK) {
                return result(Stage.LOCK_SENT, "已发送", "上锁指令已交给银河",
                        "等待车辆执行；这里不是车锁执行回执", Tone.SUCCESS);
            }
        }
        if (value.pendingUnlock) {
            if (!value.geelyConnected) {
                return value.aclRecent
                        ? result(Stage.WAITING_AUTH, "连接", "发现车辆，正在等待银河鉴权",
                        "蓝牙链路已建立，正在等待官方 V2 钥匙就绪", Tone.PROGRESS)
                        : result(Stage.CONNECTING_OFFICIAL, "连接", "已确认靠近，正在连接银河钥匙",
                        "已把车辆广播交给银河 MiuiService", Tone.PROGRESS);
            }
            if ("WAIT_BUTTON".equals(value.pendingWaitReason)) {
                return result(Stage.WAITING_UNLOCK_BUTTON, "就绪", "银河已连接，正在等待解锁按钮",
                        "官方通知已出现，正在等待可点击的解锁按钮", Tone.PROGRESS);
            }
            if ("WAIT_WIDGET_TOKEN".equals(value.pendingWaitReason)) {
                return result(Stage.WAITING_WIDGET_TOKEN, "备用", "银河已连接，等待小组件 token",
                        value.widgetStatus == null || value.widgetStatus.isEmpty()
                                ? "通知按钮暂不可用，正在等待官方小组件车锁 token" : value.widgetStatus,
                        Tone.PROGRESS);
            }
            if ("WAIT_WIDGET_STATE".equals(value.pendingWaitReason)) {
                return result(Stage.WAITING_WIDGET_STATE, "锁态", "小组件备用等待新鲜车锁状态",
                        "为防止 LOCK 切换反向执行，锁态未知或超过 45 秒时不会发送", Tone.WARNING);
            }
            if (!value.gattConnected) {
                return result(Stage.CONNECTING_GATT, "测距", "银河已连接，正在建立测距通道",
                        "正在读取连接态 RSSI", Tone.PROGRESS);
            }
            return result(Stage.PREPARING_UNLOCK, "解锁", "已确认靠近，正在准备解锁",
                    "距离条件已满足，准备交给银河执行", Tone.PROGRESS);
        }
        if (value.pendingLock) {
            if ("WAIT_WIDGET_STATE".equals(value.pendingWaitReason)) {
                return result(Stage.WAITING_WIDGET_STATE, "锁态", "小组件备用等待新鲜车锁状态",
                        "通知按钮不可用；锁态未知或过期时不会发送切换指令", Tone.WARNING);
            }
            if ("WAIT_WIDGET_TOKEN".equals(value.pendingWaitReason)) {
                return result(Stage.WAITING_WIDGET_TOKEN, "备用", "正在等待官方小组件 token",
                        value.widgetStatus == null || value.widgetStatus.isEmpty()
                                ? "通知按钮暂不可用，正在等待官方小组件车锁 token" : value.widgetStatus,
                        Tone.PROGRESS);
            }
            return result(Stage.PREPARING_LOCK, "上锁", "正在准备上锁指令",
                    "正在复核远离证据和官方通知按钮", Tone.PROGRESS);
        }
        if (value.wakeActive || (value.wakeAgeMs >= 0L && value.wakeAgeMs <= WAKE_VISIBLE_MS)) {
            return value.aclRecent
                    ? result(Stage.WAITING_AUTH, "连接", "发现车辆，正在等待银河鉴权",
                    "蓝牙链路已建立，正在等待 V2 钥匙连接完成", Tone.PROGRESS)
                    : result(Stage.WAKING_OFFICIAL, "发现", "发现车辆，正在唤醒银河",
                    "RSSI " + signal(value.rssi) + " · 已交给官方 MiuiService", Tone.PROGRESS);
        }
        if (value.geelyConnected) {
            if (value.lockDwellSamples > 0 && value.lockDwellElapsedMs >= 0L) {
                return result(Stage.CONFIRMING_DEPARTURE, "离开", "正在确认离开，准备上锁",
                        String.format(Locale.ROOT, "连续远场 %.1f / %.1f 秒",
                                value.lockDwellElapsedMs / 1000.0f, value.lockDwellMs / 1000.0f),
                        Tone.PROGRESS);
            }
            if (value.lastSentAction == PassiveKeyLogic.Action.UNLOCK
                    || "APPROACH_LATCHED".equals(value.sampleDecision)) {
                return result(Stage.MONITORING_DEPARTURE, "监测", "车辆已解锁，正在监测离开",
                        "本次靠近已处理；真正离开后自动上锁", Tone.SUCCESS);
            }
            if (value.lastSentAction == PassiveKeyLogic.Action.LOCK) {
                return result(Stage.MONITORING_DEPARTURE, "完成", "车辆已上锁，继续监测状态",
                        "等待银河断开或下一次接近", Tone.SUCCESS);
            }
            return value.gattConnected
                    ? result(Stage.MEASURING, "测距", "银河钥匙已连接，正在实时测距",
                    "RSSI " + signal(value.rssi) + zoneSuffix(value.zone), Tone.SUCCESS)
                    : result(Stage.CONNECTING_GATT, "测距", "银河钥匙已连接，正在建立测距通道",
                    "正在挂载连接态 RSSI", Tone.PROGRESS);
        }
        if (value.signalFresh) {
            if (!value.identityComplete) {
                return result(Stage.LEARNING_VEHICLE, "学习", "发现车辆，正在识别本车",
                        "首次连接成功后会自动记住本车蓝牙身份", Tone.PROGRESS);
            }
            if (value.rssi < value.connectDbm) {
                return result(Stage.WAITING_STRONGER_SIGNAL, "发现", "发现车辆，等待信号增强",
                        String.format(Locale.ROOT, "RSSI %s · 连接触发 ≥ %s dBm",
                                signal(value.rssi), signal((int) value.connectDbm)), Tone.IDLE);
            }
            return result(Stage.WAKING_OFFICIAL, "发现", "发现车辆，准备连接银河",
                    String.format(Locale.ROOT, "RSSI %s · 已达到连接阈值", signal(value.rssi)), Tone.PROGRESS);
        }
        if (value.scanning) {
            return result(Stage.WAITING_SIGNAL, "等待", "等待车辆蓝牙信号",
                    String.format(Locale.ROOT, "自动运行中 · 连接触发 ≥ %s dBm",
                            signal((int) value.connectDbm)), Tone.IDLE);
        }
        return result(Stage.RECOVERING_SCAN, "恢复", "正在恢复蓝牙扫描",
                "扫描器暂时停止，服务会自动重建", Tone.WARNING);
    }

    /** 最近30秒内，按已发生阶段优先显示短生命周期动作，避免页面轮询漏掉。 */
    private static Result resolveTrace(Input value) {
        if (value.trace == null || value.trace.isEmpty()) return null;
        long now = value.traceNowMs > 0L ? value.traceNowMs : Long.MAX_VALUE;
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.RESULT_UNKNOWN)) {
            return result(Stage.RESULT_UNKNOWN, "待核实", "车控发送结果不确定",
                    "已交给银河但未收到车锁回执，请观察车辆确认", Tone.WARNING);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.LOCK_CONFIRMED)) {
            return result(Stage.LOCK_SENT, "已确认", "车辆已确认上锁",
                    "官方锁态已变为已上锁", Tone.SUCCESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.LOCK_CONFIRMING)) {
            return result(Stage.LOCK_SENT, "确认", "上锁指令已交付，等待车辆确认",
                    "旧通知 token 最多执行一次显式上锁复核；不会重试解锁", Tone.WARNING);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.UNLOCK_SENT)) {
            return result(Stage.UNLOCK_SENT, "已发送", "解锁指令已交给银河",
                    "等待车辆执行；这里不是车锁执行回执", Tone.SUCCESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.LOCK_SENT)) {
            return result(Stage.LOCK_SENT, "已发送", "上锁指令已交给银河",
                    "等待车辆执行；这里不是车锁执行回执", Tone.SUCCESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.UNLOCK_REQUESTED)) {
            return result(Stage.PREPARING_UNLOCK, "解锁", "已确认靠近，准备解锁",
                    "解锁请求已建立，正在等待官方按钮或发送", Tone.PROGRESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.LOCK_REQUESTED)) {
            return result(Stage.PREPARING_LOCK, "上锁", "已确认离开，准备上锁",
                    "上锁请求已建立，正在等待官方按钮或发送", Tone.PROGRESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.OFFICIAL_CONNECTED)) {
            return result(Stage.WAITING_UNLOCK_BUTTON, "就绪", "银河钥匙已连接",
                    "已收到官方连接，正在等待解锁按钮或处理本次接近", Tone.PROGRESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.ACL_CONNECTED)) {
            return result(Stage.WAITING_AUTH, "连接", "车机链路已建立，等待银河鉴权",
                    "银河正在完成 V2 鉴权，完成后才允许解锁", Tone.PROGRESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.WAKE_REQUEST)) {
            return result(Stage.WAKING_OFFICIAL, "发现", "发现车辆，正在唤醒银河",
                    "已把真实 ScanResult 交给官方 MiuiService", Tone.PROGRESS);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.SCAN_LOST)) {
            return result(Stage.WAITING_SIGNAL, "等待", "等待车辆蓝牙信号",
                    "本车广播暂时消失，扫描仍在继续", Tone.IDLE);
        }
        if (hasRecent(value.trace, now, LiveActivityTrace.Step.SCAN_FOUND)) {
            return result(Stage.WAITING_STRONGER_SIGNAL, "发现", "发现本车蓝牙广播",
                    "正在确认连接触发条件与 RSSI", Tone.IDLE);
        }
        return null;
    }

    /** 查找最近一条指定阶段的轨迹，时间来自服务单调时钟。 */
    private static boolean hasRecent(List<LiveActivityTrace.Entry> trace, long now,
                                     LiveActivityTrace.Step step) {
        for (int index = trace.size() - 1; index >= 0; index--) {
            LiveActivityTrace.Entry entry = trace.get(index);
            if (entry.step == step) {
                return now >= entry.atMs && now - entry.atMs <= LiveActivityTrace.SUMMARY_VISIBLE_MS;
            }
        }
        return false;
    }

    /** 把轨迹压缩成便于阅读的最近四步。 */
    static String traceText(List<LiveActivityTrace.Entry> trace) {
        if (trace == null || trace.isEmpty()) return "";
        int start = Math.max(0, trace.size() - 4);
        StringBuilder result = new StringBuilder();
        for (int index = start; index < trace.size(); index++) {
            if (result.length() > 0) result.append(" → ");
            result.append(trace.get(index).step.badge);
        }
        return result.toString();
    }

    private static String signal(int rssi) {
        return rssi > -127 && rssi < 0 ? rssi + " dBm" : "无信号";
    }

    private static String zoneSuffix(String zone) {
        if (zone == null || zone.trim().isEmpty()) return "";
        return " · " + zone.replace(" (无信号)", "").replace("（无信号）", "").trim();
    }

    private static String sourceText(String source) {
        if ("WIDGET".equals(source) || "WIDGET_FALLBACK".equals(source)) return "官方小组件备用通道";
        if ("NOTIFICATION_FALLBACK".equals(source)) return "官方通知缓存按钮";
        return "银河官方通知按钮";
    }

    private static Result result(Stage stage, String badge, String title, String detail, Tone tone) {
        return new Result(stage, badge, title, detail, tone);
    }

    private LiveActivity() {
    }
}
