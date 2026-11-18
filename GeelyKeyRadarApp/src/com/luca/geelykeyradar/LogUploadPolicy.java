package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

/** 纯Java日志上传约束与请求体构造，便于离线回归隐私边界和序列化。 */
final class LogUploadPolicy {
    static final int MAX_ARCHIVE_BYTES = 1_800_000;
    static final int MAX_NOTE_CHARS = 800;
    static final int MIN_NOTE_CHARS = 4;
    private static final int MAX_META_CHARS = 80;

    private LogUploadPolicy() {
    }

    /** 安装ID仅用于同一设备的日志分组，不是 Android ID、序列号或账号。 */
    static String newInstallId() {
        return UUID.randomUUID().toString().toLowerCase(Locale.ROOT);
    }

    static boolean validInstallId(String value) {
        return value != null && value.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    static boolean validDeviceUid(String value) {
        return value != null && value.matches("GPK-[0-9A-F]{4}(?:-[0-9A-F]{4}){4}");
    }

    static boolean validIssueDescription(String value) {
        return sanitizeNote(value).length() >= MIN_NOTE_CHARS;
    }

    /** 用户描述保留中文和常见标点，只移除控制字符并限制长度。 */
    static String sanitizeNote(String value) {
        if (value == null) return "";
        String cleaned = value.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "")
                .replace("\r\n", "\n").replace('\r', '\n').trim();
        return cleaned.length() <= MAX_NOTE_CHARS ? cleaned : cleaned.substring(0, MAX_NOTE_CHARS);
    }

    static String sanitizeMeta(String value) {
        if (value == null) return "unknown";
        String cleaned = value.replaceAll("[\\p{Cntrl}]", " ").replaceAll("\\s+", " ").trim();
        if (cleaned.isEmpty()) return "unknown";
        return cleaned.length() <= MAX_META_CHARS ? cleaned : cleaned.substring(0, MAX_META_CHARS);
    }

    /** 本地先校验ZIP与大小，避免把非日志文件或超出D1单行限制的数据送到服务端。 */
    static void validateArchive(byte[] archive) {
        if (archive == null || archive.length < 22 || !isZip(archive)) {
            throw new IllegalArgumentException("日志包不是有效ZIP");
        }
        if (archive.length > MAX_ARCHIVE_BYTES) {
            throw new IllegalArgumentException("日志包超过1.8 MB，请先清理旧日志后重试");
        }
    }

    static boolean isZip(byte[] data) {
        return data != null && data.length >= 4 && data[0] == 0x50 && data[1] == 0x4B
                && (data[2] == 0x03 || data[2] == 0x05 || data[2] == 0x07)
                && (data[3] == 0x04 || data[3] == 0x06 || data[3] == 0x08);
    }

    /** 由硬件材料生成固定、不可逆的日志UID；同一材料始终得到同一UID。 */
    static String deriveDeviceUid(String material) {
        try {
            byte[] digest = java.security.MessageDigest.getInstance("SHA-256")
                    .digest((material == null ? "" : material).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(64);
            for (byte value : digest) hex.append(String.format(Locale.ROOT, "%02x", value & 0xFF));
            String prefix = hex.substring(0, 20).toUpperCase(Locale.ROOT);
            return "GPK-" + prefix.substring(0, 4) + "-" + prefix.substring(4, 8)
                    + "-" + prefix.substring(8, 12) + "-" + prefix.substring(12, 16)
                    + "-" + prefix.substring(16, 20);
        } catch (Exception impossible) {
            throw new IllegalStateException("无法生成本机日志UID", impossible);
        }
    }

    /** 构造固定schema JSON；Base64只编码已经由导出器脱敏的ZIP。 */
    static String buildRequestJson(String installId, String deviceUid, String note,
                                   String appVersion, int versionCode,
                                   String androidRelease, String brand, String model,
                                   String archiveName, byte[] archive, String archiveSha256) {
        validateArchive(archive);
        if (!validInstallId(installId)) throw new IllegalArgumentException("安装ID无效");
        if (!validDeviceUid(deviceUid)) throw new IllegalArgumentException("本机日志UID无效");
        if (!validIssueDescription(note)) throw new IllegalArgumentException("请填写至少4个字符的异常现象");
        String safeSha = archiveSha256 == null ? "" : archiveSha256.toLowerCase(Locale.ROOT);
        if (!safeSha.matches("[0-9a-f]{64}")) throw new IllegalArgumentException("日志校验值无效");
        return "{"
                + "\"schema\":1,"
                + "\"installId\":\"" + jsonEscape(installId) + "\","
                + "\"deviceUid\":\"" + jsonEscape(deviceUid) + "\","
                + "\"note\":\"" + jsonEscape(sanitizeNote(note)) + "\","
                + "\"appVersion\":\"" + jsonEscape(sanitizeMeta(appVersion)) + "\","
                + "\"versionCode\":" + Math.max(0, versionCode) + ","
                + "\"androidRelease\":\"" + jsonEscape(sanitizeMeta(androidRelease)) + "\","
                + "\"brand\":\"" + jsonEscape(sanitizeMeta(brand)) + "\","
                + "\"model\":\"" + jsonEscape(sanitizeMeta(model)) + "\","
                + "\"archiveName\":\"" + jsonEscape(sanitizeMeta(archiveName)) + "\","
                + "\"archiveSha256\":\"" + safeSha + "\","
                + "\"archiveBase64\":\"" + Base64.getEncoder().encodeToString(archive) + "\""
                + "}";
    }

    /** JSON字符串转义只处理协议字符，不改变日志包内已脱敏内容。 */
    static String jsonEscape(String value) {
        if (value == null) return "";
        StringBuilder output = new StringBuilder(value.length() + 16);
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            switch (character) {
                case '"': output.append("\\\""); break;
                case '\\': output.append("\\\\"); break;
                case '\b': output.append("\\b"); break;
                case '\f': output.append("\\f"); break;
                case '\n': output.append("\\n"); break;
                case '\r': output.append("\\r"); break;
                case '\t': output.append("\\t"); break;
                default:
                    if (character < 0x20) output.append(String.format(Locale.ROOT, "\\u%04x", (int) character));
                    else output.append(character);
            }
        }
        return output.toString();
    }

    static byte[] utf8(String value) {
        return value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8);
    }
}
