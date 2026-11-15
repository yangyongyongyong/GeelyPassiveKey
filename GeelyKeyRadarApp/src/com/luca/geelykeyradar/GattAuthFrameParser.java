package com.luca.geelykeyradar;

/** DK1.5 GATT通知帧的只读头部解析器，不解析或保存鉴权凭证。 */
final class GattAuthFrameParser {
    static final int DK15_MAGIC = 0xFE;
    static final int CMD_SESSION_RSP15 = 5;
    static final int CMD_APPROACH_LOCK_NOTIFY15 = 345;

    /** 只保留判断鉴权完成所需的明文头字段。 */
    static final class Frame {
        final int magic;
        final int version;
        final int length;
        final int cmdId;
        final int sequence;

        /** 保存8字节明文头，不复制后续可能包含鉴权数据的payload。 */
        Frame(int magic, int version, int length, int cmdId, int sequence) {
            this.magic = magic;
            this.version = version;
            this.length = length;
            this.cmdId = cmdId;
            this.sequence = sequence;
        }

        /** 官方DK1.5收到Session响应后才进入EVENT_DK_AUTH_SUCCESS。 */
        boolean isDk15AuthSuccess() {
            return isDk15Header(CMD_SESSION_RSP15);
        }

        /** 0x0159是车机原生PE接近落锁通知，可用于识别仍在近场却被原厂逻辑误锁。 */
        boolean isApproachLockNotice() {
            return isDk15Header(CMD_APPROACH_LOCK_NOTIFY15);
        }

        private boolean isDk15Header(int expectedCmdId) {
            return magic == DK15_MAGIC && version > 0 && length >= 8 && cmdId == expectedCmdId;
        }
    }

    /** 解析至少8字节的通知头；长度不足或为空时返回null，绝不根据短帧猜鉴权成功。 */
    static Frame parse(byte[] data) {
        if (data == null || data.length < 8) return null;
        return new Frame(data[0] & 0xFF, data[1] & 0xFF,
                ((data[2] & 0xFF) << 8) | (data[3] & 0xFF),
                ((data[4] & 0xFF) << 8) | (data[5] & 0xFF),
                ((data[6] & 0xFF) << 8) | (data[7] & 0xFF));
    }

    /** 禁止实例化纯静态解析器。 */
    private GattAuthFrameParser() {
    }
}
