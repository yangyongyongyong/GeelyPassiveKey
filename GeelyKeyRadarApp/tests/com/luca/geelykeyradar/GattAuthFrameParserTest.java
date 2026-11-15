package com.luca.geelykeyradar;

/** GATT鉴权完成帧解析回归，只验证明文头和cmdId=5。 */
public final class GattAuthFrameParserTest {
    /** 执行Session响应、其他命令、短帧和错误magic检查。 */
    public static void main(String[] args) {
        parseSessionResponse();
        rejectNonSuccessFrames();
        System.out.println("GattAuthFrameParserTest passed");
    }

    /** DK1.5 Session响应cmdId=5触发鉴权完成。 */
    private static void parseSessionResponse() {
        GattAuthFrameParser.Frame frame = GattAuthFrameParser.parse(new byte[]{
                (byte) 0xFE, 1, 0, 12, 0, 5, 0, 9, 1, 2, 3, 4
        });
        check(frame != null && frame.length == 12 && frame.cmdId == 5 && frame.sequence == 9,
                "Session响应头解析");
        check(frame.isDk15AuthSuccess(), "cmdId=5判为鉴权完成");
    }

    /** 其他cmdId、短帧和错误magic不能误触发解锁资格。 */
    private static void rejectNonSuccessFrames() {
        GattAuthFrameParser.Frame other = GattAuthFrameParser.parse(new byte[]{
                (byte) 0xFE, 1, 0, 8, 0, 4, 0, 1
        });
        check(other != null && !other.isDk15AuthSuccess(), "DK verify帧不是最终成功");
        check(GattAuthFrameParser.parse(new byte[]{1, 2, 3}) == null, "短帧拒绝");
        GattAuthFrameParser.Frame badMagic = GattAuthFrameParser.parse(new byte[]{
                1, 1, 0, 8, 0, 5, 0, 1
        });
        check(badMagic != null && !badMagic.isDk15AuthSuccess(), "错误magic拒绝");
    }

    /** 断言失败保留鉴权完成帧的安全边界。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
