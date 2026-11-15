package com.luca.geelykeyradar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 授权与运行全检的纯逻辑模型。
 *
 * Android 权限、通知监听和厂商后台开关的读取放在 PermissionStatus 中；
 * 这里只负责把事实映射成稳定顺序、状态和待处理项，便于单测与界面复用。
 */
final class PermissionAudit {

    /** vivo 覆盖安装/强停后，给 NotificationListenerService 自动重绑的观察窗口。 */
    static final long LISTENER_REBIND_GRACE_MS = 10000L;

    enum Priority {
        REQUIRED,
        RECOMMENDED
    }

    enum Item {
        BLUETOOTH_PERMISSION(Priority.REQUIRED, "蓝牙扫描与连接",
                "扫描车辆广播，并读取连接态 RSSI"),
        BLUETOOTH_ON(Priority.REQUIRED, "手机蓝牙",
                "蓝牙关闭时无法发现车辆"),
        NOTIFICATIONS(Priority.REQUIRED, "系统通知权限",
                "显示前台服务状态，系统事件也需要它"),
        LISTENER(Priority.REQUIRED, "通知使用权",
                "读取银河连接状态，并复用官方解锁/上锁按钮"),
        SERVICE(Priority.REQUIRED, "无感服务",
                "扫描、判定和自动解闭锁的总开关"),
        GEELY_APP(Priority.REQUIRED, "银河 App",
                "钥匙凭证、鉴权和车控按钮都在官方 App 内"),
        BATTERY(Priority.RECOMMENDED, "电池白名单",
                "锁屏后减少被省电策略冻结或清理"),
        VENDOR_BACKGROUND(Priority.RECOMMENDED, "厂商后台与自启动",
                "OriginOS 的后台高耗电和自启动需要人工确认");

        final Priority priority;
        final String title;
        final String description;

        Item(Priority priority, String title, String description) {
            this.priority = priority;
            this.title = title;
            this.description = description;
        }

        boolean isRequired() {
            return priority == Priority.REQUIRED;
        }
    }

    enum State {
        READY,
        ACTION_REQUIRED,
        WAITING,
        MANUAL
    }

    static final class Input {
        int sdkInt = 29;
        boolean bluetoothPermission;
        boolean bluetoothOn;
        boolean notificationRuntimePermission;
        boolean listenerGranted;
        boolean listenerConnected;
        long listenerDownMs;
        boolean geelyInstalled;
        boolean serviceEnabled;
        boolean serviceRunning;
        boolean batteryWhitelisted;
        boolean vendorBackgroundConfirmed;
    }

    static final class Row {
        final Item item;
        final State state;

        Row(Item item, State state) {
            this.item = item;
            this.state = state;
        }

        boolean isReady() {
            return state == State.READY;
        }

        boolean isWaiting() {
            return state == State.WAITING;
        }

        boolean isBlocking() {
            return item.isRequired() && !isReady();
        }

        String statusLabel() {
            switch (state) {
                case READY:
                    return "已就绪";
                case WAITING:
                    return "正在重连";
                case MANUAL:
                    return "需手动确认";
                case ACTION_REQUIRED:
                default:
                    return "待处理";
            }
        }
    }

    static final class Result {
        final List<Row> rows;
        final int requiredReady;
        final int requiredTotal;
        final int recommendedReady;
        final int recommendedTotal;
        final Row nextRequired;
        final Row nextRecommended;

        Result(List<Row> rows, int requiredReady, int requiredTotal,
               int recommendedReady, int recommendedTotal,
               Row nextRequired, Row nextRecommended) {
            this.rows = Collections.unmodifiableList(rows);
            this.requiredReady = requiredReady;
            this.requiredTotal = requiredTotal;
            this.recommendedReady = recommendedReady;
            this.recommendedTotal = recommendedTotal;
            this.nextRequired = nextRequired;
            this.nextRecommended = nextRecommended;
        }

        Row row(Item item) {
            for (Row row : rows) {
                if (row.item == item) return row;
            }
            return null;
        }

        int pendingRequired() {
            return Math.max(0, requiredTotal - requiredReady);
        }

        int pendingRecommended() {
            return Math.max(0, recommendedTotal - recommendedReady);
        }

        boolean essentialReady() {
            return pendingRequired() == 0;
        }

        boolean allReady() {
            return essentialReady() && pendingRecommended() == 0;
        }

        String headline() {
            if (!essentialReady()) return "还需处理 " + pendingRequired() + " 项必要检查";
            if (!allReady()) return "必要项已就绪";
            return "全部检查通过";
        }

        String summaryLine() {
            return "必要项 " + requiredReady + "/" + requiredTotal
                    + " · 保活建议 " + recommendedReady + "/" + recommendedTotal;
        }
    }

    private PermissionAudit() {
    }

    /** 按固定顺序评估全部检查项；必要的通知监听掉绑会先显示自动恢复窗口。 */
    static Result evaluate(Input input) {
        Input value = input == null ? new Input() : input;
        List<Row> rows = new ArrayList<>();
        rows.add(new Row(Item.BLUETOOTH_PERMISSION,
                value.bluetoothPermission ? State.READY : State.ACTION_REQUIRED));
        rows.add(new Row(Item.BLUETOOTH_ON,
                value.bluetoothOn ? State.READY : State.ACTION_REQUIRED));
        rows.add(new Row(Item.NOTIFICATIONS,
                value.sdkInt < 33 || value.notificationRuntimePermission
                        ? State.READY : State.ACTION_REQUIRED));

        State listener;
        if (!value.listenerGranted) {
            listener = State.ACTION_REQUIRED;
        } else if (value.listenerConnected) {
            listener = State.READY;
        } else if (value.listenerDownMs < LISTENER_REBIND_GRACE_MS) {
            listener = State.WAITING;
        } else {
            listener = State.ACTION_REQUIRED;
        }
        rows.add(new Row(Item.LISTENER, listener));

        rows.add(new Row(Item.SERVICE,
                value.serviceEnabled && value.serviceRunning ? State.READY : State.ACTION_REQUIRED));
        rows.add(new Row(Item.GEELY_APP,
                value.geelyInstalled ? State.READY : State.ACTION_REQUIRED));
        rows.add(new Row(Item.BATTERY,
                value.batteryWhitelisted ? State.READY : State.ACTION_REQUIRED));
        rows.add(new Row(Item.VENDOR_BACKGROUND,
                value.vendorBackgroundConfirmed ? State.READY : State.MANUAL));

        int requiredReady = 0;
        int requiredTotal = 0;
        int recommendedReady = 0;
        int recommendedTotal = 0;
        Row nextRequired = null;
        Row nextRecommended = null;
        for (Row row : rows) {
            if (row.item.isRequired()) {
                requiredTotal++;
                if (row.isReady()) requiredReady++;
                else if (nextRequired == null) nextRequired = row;
            } else {
                recommendedTotal++;
                if (row.isReady()) recommendedReady++;
                else if (nextRecommended == null) nextRecommended = row;
            }
        }
        return new Result(rows, requiredReady, requiredTotal, recommendedReady,
                recommendedTotal, nextRequired, nextRecommended);
    }
}
