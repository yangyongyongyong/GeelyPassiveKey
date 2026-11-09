package com.luca.geelykeyradar;

import java.util.Collection;
import java.util.Locale;

/** 本车学习的资格和可见状态，区分尾号提示、已有配置与真实蓝牙地址。 */
final class VehicleLearningState {
    static final String UNKNOWN = "unknown";
    static final String LEGACY = "legacy_migration";
    static final String MANUAL = "manual_suffix";
    static final String SCAN = "advertisement";
    static final String CONNECTED = "connected_gatt";
    static final String ACL = "acl_event";
    static final String GATT_EVENT = "gatt_event";

    /** 只有实际获得合法蓝牙地址，才算完成地址学习；尾号或设备名不能推算MAC。 */
    static boolean hasAddress(String address) {
        return address != null && address.matches("(?i)(?:[0-9a-f]{2}:){5}[0-9a-f]{2}");
    }

    /** 等待新连接只限制猜测路径，不能挡住当前真实且可验证的已连接设备。 */
    static boolean canLearnConnected(boolean permission, boolean officialConnected,
                                     boolean waitingForReconnect, boolean verifiedPeer) {
        return permission && officialConnected && verifiedPeer;
    }

    /** 多个符合条件的连接不能任取一个，重复地址仅视为同一个设备。 */
    static String uniqueAddress(Collection<String> addresses) {
        String selected = "";
        for (String address : addresses) {
            if (!hasAddress(address)) continue;
            if (!selected.isEmpty() && !selected.equalsIgnoreCase(address)) return "";
            selected = address.toUpperCase(Locale.ROOT);
        }
        return selected;
    }

    /** 提供具体的下一步，缺权限优先于广播停止和等待重连提示。 */
    static String message(boolean enabled, boolean bluetoothPermission, boolean notificationAccess,
                          boolean complete, boolean hasHint, boolean officialConnected, boolean waitingForReconnect) {
        if (!enabled) return "服务已关闭，开启无感进出服务后才能扫描学习";
        if (!bluetoothPermission) return "缺少蓝牙扫描/连接权限，请先点“授予蓝牙权限”";
        if (!notificationAccess) return "缺少通知使用权，请先点“开启通知使用权”，才能确认银河连接状态";
        if (complete) return "本车蓝牙地址已获得";
        if (officialConnected || waitingForReconnect) {
            return "银河已连接，但系统未提供可识别的本车地址；请在银河先断开蓝牙钥匙再连接，保持本应用运行";
        }
        return hasHint ? "仅设置了匹配特征，尚未获取蓝牙地址；等待本车广播或连接事件"
                : "等待本车广播或连接事件；请保持本应用运行，再用银河连接车辆";
    }

    /** 旧配置和手填提示明确标明来源，不将它们冒充首次自动识别验证。 */
    static String sourceLabel(String source) {
        if (SCAN.equals(source)) return "实际蓝牙广播";
        if (CONNECTED.equals(source)) return "当前已连接的蓝牙设备";
        if (ACL.equals(source)) return "本车蓝牙连接事件";
        if (GATT_EVENT.equals(source)) return "本车GATT连接事件";
        if (MANUAL.equals(source)) return "手动尾号匹配提示";
        if (LEGACY.equals(source)) return "本机旧版配置迁移";
        return "已有配置，来源未记录";
    }
}
