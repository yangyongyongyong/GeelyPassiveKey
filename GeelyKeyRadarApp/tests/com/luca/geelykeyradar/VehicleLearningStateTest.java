package com.luca.geelykeyradar;

import java.util.List;

/** 首次使用、重学及尾号提示的回归；以下地址仅为合成测试数据。 */
public final class VehicleLearningStateTest {
    /** 验证权限、真实连接优先级、歧义设备及完成状态。 */
    public static void main(String[] args) {
        check(!VehicleLearningState.hasAddress("GeelyVehicle123"), "设备名不能当作已学习地址");
        check(!VehicleLearningState.hasAddress("123"), "VIN尾号不能推算MAC");
        check(!VehicleLearningState.hasAddress(null) && !VehicleLearningState.hasAddress(""), "空配置不算完成学习");
        check(VehicleLearningState.hasAddress("AA:BB:CC:11:22:33"), "合法观测地址才算完成");
        check(VehicleLearningState.canLearnConnected(true, true, true, true),
                "等待重连不能阻断已经验证的当前连接");
        check(!VehicleLearningState.canLearnConnected(false, true, false, true), "缺权限不能读当前设备");
        check(!VehicleLearningState.canLearnConnected(true, false, false, true), "官方离线不能借其他连接当作本车");
        check(!VehicleLearningState.canLearnConnected(true, true, true, false), "官方已连接但地址不可见时不能虚构地址");
        check(VehicleLearningState.uniqueAddress(List.of("AA:BB:CC:11:22:33", "aa:bb:cc:11:22:33"))
                .equals("AA:BB:CC:11:22:33"), "同一连接的重复观测应去重");
        check(VehicleLearningState.uniqueAddress(List.of("AA:BB:CC:11:22:33", "AA:BB:CC:44:55:66")).isEmpty(),
                "多个符合条件的连接不能任取一个");
        check(VehicleLearningState.uniqueAddress(List.of("123", "")).isEmpty(), "无有效地址不能学习成功");
        check(VehicleLearningState.message(true, false, false, false, false, true, true).contains("蓝牙扫描/连接权限"),
                "当前缺权限优先于停止广播提示");
        check(VehicleLearningState.message(true, true, false, false, false, true, true).contains("通知使用权"),
                "官方连接确认所需的特殊权限必须明确提示");
        check(VehicleLearningState.message(true, true, true, false, true, true, true).contains("先断开蓝牙钥匙再连接"),
                "银河已连且无地址时必须有可执行的重学步骤");
        check(VehicleLearningState.message(true, true, true, false, true, false, false).contains("尚未获取蓝牙地址"),
                "只填尾号不能显示成已学习");
        check(VehicleLearningState.sourceLabel(VehicleLearningState.LEGACY).contains("旧版配置"),
                "迁移数据不能冒充自动识别来源");
        check(VehicleLearningState.sourceLabel(VehicleLearningState.MANUAL).contains("匹配提示"),
                "手填数据须显示真实来源");
        check(VehicleLearningState.message(false, true, true, false, false, false, false).contains("服务已关闭"),
                "重学不能绕过服务总开关");
        System.out.println("VehicleLearningStateTest passed");
    }

    /** 断言失败时保留学习语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
