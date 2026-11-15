package com.luca.geelykeyradar;

/**
 * 吉利 V2 厂商数据的稳定发现掩码。
 *
 * 历史官方实现优先匹配 vendor、antenna 和 VIN 后两位；antenna 也可能变化时只保留
 * vendor 与 VIN 后两位。版本和设备状态字节可变化，不能作为永久身份条件。
 * 本类只服务于发现，不授予地址、RSSI、鉴权或车控权限。
 */
final class VehicleDiscoverySignature {
    private static final int PREFIX_BYTES = 6;
    private static final byte[] PREFERRED_MASK = new byte[]{
            (byte) 0xFF, 0, (byte) 0xFF, 0, (byte) 0xFF, (byte) 0xFF
    };
    private static final byte[] FALLBACK_MASK = new byte[]{
            (byte) 0xFF, 0, 0, 0, (byte) 0xFF, (byte) 0xFF
    };

    /** 返回优先和兜底两份掩码副本，调用方修改数组不会污染稳定发现规则。 */
    static byte[][] masks() {
        return new byte[][]{PREFERRED_MASK.clone(), FALLBACK_MASK.clone()};
    }

    /** 按厂商 ID 裁出 Android 厂商 payload 前缀，兼容旧 15FF 签名携带 Company ID 的布局。 */
    static byte[] payloadPrefix(int manufacturerId, String signature) {
        if (manufacturerId != VehicleBeacon.COMPANY_ID_GEELY
                && manufacturerId != VehicleBeacon.COMPANY_ID_COMPAT
                && manufacturerId != VehicleBeacon.COMPANY_ID_LEGACY) return new byte[0];
        String value = signature == null ? "" : signature.trim();
        String prefix = manufacturerId == VehicleBeacon.COMPANY_ID_LEGACY && value.startsWith("15FF")
                ? value.substring(4) : value;
        return VehicleBeacon.hexToBytes(prefix);
    }

    /** 判断前缀是否具备当前可识别的 vendor/version 与两位 VIN 字符。 */
    static boolean isUsablePrefix(byte[] prefix) {
        return prefix != null && prefix.length >= PREFIX_BYTES
                && (prefix[0] & 0xFF) == 6 && (prefix[1] & 0xFF) > 0
                && isVinCharacter(prefix[4]) && isVinCharacter(prefix[5]);
    }

    /** 校验前缀后两位与已学习尾号完全一致，防止硬件计划放行另一台同厂商车辆。 */
    static boolean isUsableForTail(byte[] prefix, String tail2) {
        if (!isUsablePrefix(prefix) || tail2 == null || tail2.length() != 2) return false;
        byte first = asciiUpper((byte) tail2.charAt(0));
        byte second = asciiUpper((byte) tail2.charAt(1));
        return prefix[4] == first && prefix[5] == second;
    }

    /** 优先匹配 vendor+antenna+VIN2；若天线位变化则退回 vendor+VIN2 的官方两字节分支。 */
    static boolean matches(byte[] learned, byte[] observed) {
        if (!isUsablePrefix(learned) || !isUsablePrefix(observed)) return false;
        return maskedEquals(learned, observed, PREFERRED_MASK)
                || maskedEquals(learned, observed, FALLBACK_MASK);
    }

    /** 只比较掩码声明为身份的位，版本、devices 或兜底下的 antenna 不参与。 */
    private static boolean maskedEquals(byte[] learned, byte[] observed, byte[] mask) {
        for (int index = 0; index < PREFIX_BYTES; index++) {
            if ((learned[index] & mask[index]) != (observed[index] & mask[index])) return false;
        }
        return true;
    }

    /** 与 VehicleBeacon 的 VIN 字符校验保持一致，拒绝把状态字节当作尾号。 */
    private static boolean isVinCharacter(byte value) {
        int character = value & 0xFF;
        return (character >= '0' && character <= '9') || (character >= 'A' && character <= 'Z')
                || (character >= 'a' && character <= 'z');
    }

    /** 将两位尾号统一成广播前缀使用的大写ASCII，避免持久化大小写差异误删有效规则。 */
    private static byte asciiUpper(byte value) {
        return value >= 'a' && value <= 'z' ? (byte) (value - ('a' - 'A')) : value;
    }
}
