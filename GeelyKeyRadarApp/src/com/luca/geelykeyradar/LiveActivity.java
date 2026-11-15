package com.luca.geelykeyradar;

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
        String pendingWaitReason = "";
        String sampleDecision = "";
        String zone = "";
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
            return result(Stage.NOTIFICATION_ACCESS, "权限", "缺少通知使用权",
                    "需要读取银河通知按钮才能发送解锁或上锁", Tone.ERROR);
        }
        if (!value.listenerConnected) {
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
            return value.dispatchAction == PassiveKeyLogic.Action.LOCK
                    ? result(Stage.SENDING_LOCK, "上锁", "正在发送上锁指令",
                    "正在通过银河官方通知按钮发送", Tone.PROGRESS)
                    : result(Stage.SENDING_UNLOCK, "解锁", "正在发送解锁指令",
                    "正在通过银河官方通知按钮发送", Tone.PROGRESS);
        }
        if (value.lastSentAgeMs >= 0L && value.lastSentAgeMs <= RESULT_VISIBLE_MS) {
            if ("UNKNOWN".equals(value.lastControlResult)) {
                String action = value.lastSentAction == PassiveKeyLogic.Action.LOCK ? "上锁" : "解锁";
                return result(Stage.RESULT_UNKNOWN, "待核实", action + "结果不确定",
                        "已交给银河但未收到车锁回执，请观察车辆确认", Tone.WARNING);
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
            if (!value.gattConnected) {
                return result(Stage.CONNECTING_GATT, "测距", "银河已连接，正在建立测距通道",
                        "正在读取连接态 RSSI", Tone.PROGRESS);
            }
            return result(Stage.PREPARING_UNLOCK, "解锁", "已确认靠近，正在准备解锁",
                    "距离条件已满足，准备交给银河执行", Tone.PROGRESS);
        }
        if (value.pendingLock) {
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

    private static String signal(int rssi) {
        return rssi > -127 && rssi < 0 ? rssi + " dBm" : "无信号";
    }

    private static String zoneSuffix(String zone) {
        if (zone == null || zone.trim().isEmpty()) return "";
        return " · " + zone.replace(" (无信号)", "").replace("（无信号）", "").trim();
    }

    private static Result result(Stage stage, String badge, String title, String detail, Tone tone) {
        return new Result(stage, badge, title, detail, tone);
    }

    private LiveActivity() {
    }
}
