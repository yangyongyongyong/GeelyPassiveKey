package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/** 不依赖 Android 的吉利广播数据与解析规则，便于对真实匹配路径做多车回放。 */
class VehicleBeacon {
    static final int COMPANY_ID_GEELY = 0x01FE;
    static final int COMPANY_ID_COMPAT = 0x06FE;
    static final int COMPANY_ID_LEGACY = 0xFF15;
    static final String SERVICE_UUID = "0000fdfd-0000-1000-8000-00805f9b34fb";
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    final String address;
    final String name;
    final int rssi;
    final long seenAtMs;
    final long seenAtNanos;
    final String vinTail2;
    final String vinTail3;
    final String mfgSignature;
    final int manufacturerId;
    final String sourceTag;

    /** 保存实际采集时间和公开广播字段；名称、尾号与协议前缀均不能当作钥匙凭证。 */
    VehicleBeacon(String address, String name, int rssi, long seenAtMs, String vinTail2,
                  String vinTail3, String mfgSignature, int manufacturerId, String sourceTag) {
        this(address, name, rssi, seenAtMs, seenAtMs * 1000000L, vinTail2, vinTail3,
                mfgSignature, manufacturerId, sourceTag);
    }

    /** 保留系统纳秒采集时间，毫秒内不同广播仍是不同观测，完全重复结果才会被拒绝。 */
    VehicleBeacon(String address, String name, int rssi, long seenAtMs, long seenAtNanos, String vinTail2,
                  String vinTail3, String mfgSignature, int manufacturerId, String sourceTag) {
        this.address = clean(address).toUpperCase(Locale.ROOT);
        this.name = clean(name);
        this.rssi = rssi;
        this.seenAtMs = seenAtMs;
        this.seenAtNanos = seenAtNanos;
        this.vinTail2 = clean(vinTail2);
        this.vinTail3 = clean(vinTail3);
        this.mfgSignature = clean(mfgSignature).toUpperCase(Locale.ROOT);
        this.manufacturerId = manufacturerId;
        this.sourceTag = clean(sourceTag);
    }

    /** 复制解析结果供 Android 扫描包装使用，不重新生成采集时间。 */
    VehicleBeacon(VehicleBeacon data, long sampledNanos) {
        this(data.address, data.name, data.rssi, data.seenAtMs, sampledNanos, data.vinTail2, data.vinTail3,
                data.mfgSignature, data.manufacturerId, data.sourceTag);
    }

    /** 校验厂商字段或精确吉利设备名；裸 FDFD 加任意设备名不再冒充车辆。 */
    static VehicleBeacon parse(String address, String name, int rssi, long seenAtMs,
                               byte[] geelyPayload, byte[] compatPayload, byte[] legacyPayload,
                               boolean hasFdfd) {
        String safeName = clean(name);
        boolean geelyName = isRecognizableVehicleName(safeName);
        byte[] payload = null;
        int company = -1;
        String source = "";
        if (isVendorPayload(geelyPayload)) {
            payload = geelyPayload;
            company = COMPANY_ID_GEELY;
            source = "01FE";
        } else if (isVendorPayload(compatPayload)) {
            payload = compatPayload;
            company = COMPANY_ID_COMPAT;
            source = "06FE";
        } else if (isVendorPayload(legacyPayload)) {
            payload = legacyPayload;
            company = COMPANY_ID_LEGACY;
            source = "15FF v" + (payload[0] & 0xFF) + "." + (payload[1] & 0xFF);
        }
        if (payload == null && !geelyName) return null;

        String tail2 = "";
        String signature = "";
        if (payload != null) {
            if (payload.length >= 6 && isVinCharacter(payload[4]) && isVinCharacter(payload[5])) {
                tail2 = new String(payload, 4, 2, StandardCharsets.US_ASCII);
            }
            // 0x01FE 的前 6 字节是 vendor/version/固定位 + VIN 尾号；后面的字节可能随状态变化。
            int prefixLength = Math.min(6, payload.length);
            signature = bytesToHex(payload, 0, prefixLength);
            if (company == COMPANY_ID_LEGACY) signature = "15FF" + signature;
        } else {
            source = hasFdfd ? "FDFD+NAME" : "NAME";
        }
        String tail3 = vinTailFromName(safeName);
        if (!tail2.isEmpty() && tail3.length() >= 2
                && !tail2.equalsIgnoreCase(tail3.substring(tail3.length() - 2))) return null;
        if (!VehicleLearningState.hasAddress(address)) return null;
        return new VehicleBeacon(address, safeName, rssi, seenAtMs, tail2, tail3,
                signature, company, source);
    }

    /** 厂商 ID 本身不是车辆证据；匿名帧还需可识别的 vendor/version 字段。 */
    private static boolean isVendorPayload(byte[] payload) {
        return payload != null && payload.length >= 2 && (payload[0] & 0xFF) == 6
                && (payload[1] & 0xFF) > 0;
    }

    /** 只接收 GeelyVehicle 加两到三位尾号，避免把公共前缀或相似设备名认作车辆。 */
    static boolean isGeelyVehicleName(String value) {
        String name = clean(value);
        String prefix = "GeelyVehicle";
        if (!name.regionMatches(true, 0, prefix, 0, prefix.length())) return false;
        String suffix = name.substring(prefix.length());
        if (suffix.length() < 2 || suffix.length() > 3) return false;
        for (int index = 0; index < suffix.length(); index++) {
            char character = suffix.charAt(index);
            if (!((character >= '0' && character <= '9') || (character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z'))) return false;
        }
        return true;
    }

    /** 星愿不同层可能显示 Geely_Auto 或 GeelyAuto；它只可作为发现线索，不能证明唯一车辆。 */
    static boolean isGeelyAutoName(String value) {
        String name = clean(value);
        return "Geely_Auto".equalsIgnoreCase(name) || "GeelyAuto".equalsIgnoreCase(name);
    }

    /** 可参与找车的明确名称；唯一身份仍需 GeelyVehicle 尾号或真实连接地址。 */
    static boolean isRecognizableVehicleName(String value) {
        return isGeelyVehicleName(value) || isGeelyAutoName(value);
    }

    /** 从精确设备名提取尾号，保留合法字母，不从任意名称尾部猜测数字。 */
    static String vinTailFromName(String value) {
        return isGeelyVehicleName(value) ? clean(value).substring("GeelyVehicle".length()) : "";
    }

    /** 同一物理地址的广播与 ScanResponse 共用一个候选，名称补全不会制造第二辆车。 */
    String stableKey() {
        return address;
    }

    /**
     * 传给银河 MiuiService 的非空 carId。
     *
     * 官方 MiuiService 只用它做日志/回执标识，真正鉴权使用 ScanResult 和吉利自己的钥匙配置；
     * 因此这里不需要、也不应该写死用户 VIN。
     */
    String carId() {
        if (!name.isEmpty()) return name;
        if (!vinTail2.isEmpty()) return "geely-vin-" + vinTail2;
        return "geely-ble";
    }

    /** 无明确设备名时仅显示不完整尾号，不宣称已完成本车学习。 */
    String displayName() {
        if (!name.isEmpty()) return name;
        if (!vinTail2.isEmpty()) return "GeelyVehicle??" + vinTail2;
        return address;
    }

    /** 仅供界面展示公开广播信息，日志入口应继续使用脱敏指纹。 */
    String displayDetail() {
        StringBuilder result = new StringBuilder(displayName());
        if (!address.isEmpty()) result.append(" · ").append(address);
        if (!sourceTag.isEmpty()) result.append(" · ").append(sourceTag);
        return result.toString();
    }

    /** 判断 VIN 尾号字节为 ASCII 字母或数字，避免把状态字节当作尾号。 */
    private static boolean isVinCharacter(byte value) {
        int character = value & 0xFF;
        return (character >= '0' && character <= '9') || (character >= 'A' && character <= 'Z')
                || (character >= 'a' && character <= 'z');
    }

    /** 编码整个短前缀，不在每个字节上调用高开销的 String.format。 */
    static String bytesToHex(byte[] data) {
        return data == null ? "" : bytesToHex(data, 0, data.length);
    }

    /** 仅编码需要的 vendor/version/VIN 前缀，不逐包把整个广播转换为十六进制字符串。 */
    static String bytesToHex(byte[] data, int offset, int length) {
        if (data == null || length <= 0 || offset < 0 || offset >= data.length) return "";
        int count = Math.min(length, data.length - offset);
        char[] output = new char[count * 2];
        for (int index = 0; index < count; index++) {
            int value = data[offset + index] & 0xFF;
            output[index * 2] = HEX[value >>> 4];
            output[index * 2 + 1] = HEX[value & 0x0F];
        }
        return new String(output);
    }

    /** 严格解码持久化前缀，损坏或奇数长度的字符串不能形成硬件过滤。 */
    static byte[] hexToBytes(String hex) {
        if (hex == null || hex.length() < 2 || (hex.length() & 1) != 0) return new byte[0];
        byte[] output = new byte[hex.length() / 2];
        for (int index = 0; index < output.length; index++) {
            int high = Character.digit(hex.charAt(index * 2), 16);
            int low = Character.digit(hex.charAt(index * 2 + 1), 16);
            if (high < 0 || low < 0) return new byte[0];
            output[index] = (byte) ((high << 4) | low);
        }
        return output;
    }

    /** 将空字段归一化，避免匹配与过滤使用不同的大小写或首尾空白。 */
    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
