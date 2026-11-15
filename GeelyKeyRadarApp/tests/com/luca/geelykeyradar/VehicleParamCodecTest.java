package com.luca.geelykeyradar;

/** 车机只读回执解码回归，覆盖DK1.5 PE状态和异常长度边界。 */
public final class VehicleParamCodecTest {
    /** 执行PE查询回执、失败码、短帧和十六进制摘要检查。 */
    public static void main(String[] args) {
        decodePeSwitchStates();
        rejectIncompletePayload();
        allowOnlyReadRequests();
        compactHexLimitsOutput();
        System.out.println("VehicleParamCodecTest passed");
    }

    /** 0x0501回执第0字节是请求类型，第1字节是真实PE状态，第2..3字节是结果码。 */
    private static void decodePeSwitchStates() {
        VehicleParamCodec.PeSwitchResult enabled = VehicleParamCodec.decodePeSwitch(
                new byte[]{2, 1, 0, 0}, 99);
        check(enabled != null && enabled.request == 2 && enabled.state == 1
                && enabled.resultCode == 0 && enabled.success, "PE开启回执解析");
        VehicleParamCodec.PeSwitchResult disabled = VehicleParamCodec.decodePeSwitch(
                new byte[]{2, 0, 0, 0}, 99);
        check(disabled != null && disabled.state == 0 && disabled.success, "PE关闭回执解析");
        VehicleParamCodec.PeSwitchResult failed = VehicleParamCodec.decodePeSwitch(
                new byte[]{2, 1, 0, 7}, 99);
        check(failed != null && failed.resultCode == 7 && !failed.success, "非零结果码不能报成功");
    }

    /** 少于两字节或空回执不能猜测出车机开关状态。 */
    private static void rejectIncompletePayload() {
        check(VehicleParamCodec.decodePeSwitch(null, 0) == null, "空回执拒绝");
        check(VehicleParamCodec.decodePeSwitch(new byte[]{2}, 0) == null, "单字节回执拒绝");
    }

    /** 只读白名单必须同时校验feature、action和value，PE开关0/1与标定写入405都要拒绝。 */
    private static void allowOnlyReadRequests() {
        check(VehicleParamCodec.isReadOnlyRequest("feat_dk_configuration", 5392, 2), "PE查询放行");
        check(!VehicleParamCodec.isReadOnlyRequest("feat_dk_configuration", 5392, 0), "PE关闭写入拒绝");
        check(!VehicleParamCodec.isReadOnlyRequest("feat_dk_configuration", 5392, 1), "PE开启写入拒绝");
        check(VehicleParamCodec.isReadOnlyRequest("feat_dk_fast_ctrl", 272, 0), "车况查询放行");
        check(VehicleParamCodec.isReadOnlyRequest("feat_calibration", 305, 0), "标定读取请求放行");
        check(!VehicleParamCodec.isReadOnlyRequest("feat_calibration", 405, 0), "标定写入拒绝");
        check(!VehicleParamCodec.isReadOnlyRequest("feat_calibration", 368, 2), "距离档位写入拒绝");
    }

    /** 日志和界面摘要必须限制长度，避免异常广播撑大持久化内容。 */
    private static void compactHexLimitsOutput() {
        byte[] data = new byte[]{0, 1, 2, 3, 4, 5};
        String compact = VehicleParamCodec.compactHex(data, 3);
        check(compact.startsWith("000102...") && compact.endsWith("(6B)"), "十六进制摘要限制长度");
        check(VehicleParamCodec.compactHex(new byte[0], 3).equals("empty (0B)"), "空摘要明确标记");
    }

    /** 断言失败保留具体只读参数语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
