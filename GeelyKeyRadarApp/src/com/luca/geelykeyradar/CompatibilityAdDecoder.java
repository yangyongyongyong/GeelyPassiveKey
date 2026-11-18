package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 将原始 BLE AD 结构解码成兼容性排查可直接阅读的字段，不参与车辆身份或车控判定。 */
final class CompatibilityAdDecoder {
    private CompatibilityAdDecoder() {
    }

    static final class Structure {
        final int offset;
        final int length;
        final int type;
        final byte[] value;
        final boolean malformed;

        Structure(int offset, int length, int type, byte[] value, boolean malformed) {
            this.offset = offset;
            this.length = length;
            this.type = type;
            this.value = value == null ? new byte[0] : value;
            this.malformed = malformed;
        }

        String describe() {
            if (malformed) {
                return "offset=" + offset + " malformed=true declared_length=" + length
                        + " remaining_bytes=" + value.length + " raw=" + toHex(value);
            }
            return "offset=" + offset + " len=" + (length + 1) + " payload_len=" + value.length
                    + " type=" + String.format(Locale.ROOT, "0x%02X", type)
                    + "(" + typeName(type) + ") value=" + toHex(value)
                    + " decoded=" + interpret(type, value);
        }
    }

    /** 按 AD length/type 边界解码；遇到损坏长度仍保留剩余原始字节并以 malformed 标记结束。 */
    static List<Structure> decode(byte[] raw) {
        List<Structure> result = new ArrayList<>();
        if (raw == null || raw.length == 0) return result;
        int index = 0;
        while (index < raw.length) {
            int length = raw[index] & 0xFF;
            if (length == 0) break;
            if (length < 1 || index + 1 >= raw.length || index + 1 + length > raw.length) {
                byte[] remaining = new byte[raw.length - index];
                System.arraycopy(raw, index, remaining, 0, remaining.length);
                result.add(new Structure(index, length, -1, remaining, true));
                break;
            }
            int type = raw[index + 1] & 0xFF;
            byte[] value = new byte[length - 1];
            System.arraycopy(raw, index + 2, value, 0, value.length);
            result.add(new Structure(index, length, type, value, false));
            index += length + 1;
        }
        return result;
    }

    static String typeName(int type) {
        switch (type) {
            case 0x01: return "FLAGS";
            case 0x02: return "INCOMPLETE_16BIT_UUIDS";
            case 0x03: return "COMPLETE_16BIT_UUIDS";
            case 0x04: return "INCOMPLETE_32BIT_UUIDS";
            case 0x05: return "COMPLETE_32BIT_UUIDS";
            case 0x06: return "INCOMPLETE_128BIT_UUIDS";
            case 0x07: return "COMPLETE_128BIT_UUIDS";
            case 0x08: return "SHORTENED_LOCAL_NAME";
            case 0x09: return "COMPLETE_LOCAL_NAME";
            case 0x0A: return "TX_POWER_LEVEL";
            case 0x0D: return "CLASS_OF_DEVICE";
            case 0x16: return "SERVICE_DATA_16BIT_UUID";
            case 0x19: return "APPEARANCE";
            case 0x20: return "SERVICE_DATA_32BIT_UUID";
            case 0x21: return "SERVICE_DATA_128BIT_UUID";
            case 0x24: return "URI";
            case 0x2A: return "TRANSPORT_DISCOVERY_DATA";
            case 0xFF: return "MANUFACTURER_SPECIFIC_DATA";
            default: return "UNKNOWN";
        }
    }

    static String interpret(int type, byte[] value) {
        if (value == null || value.length == 0) return "\"\"";
        switch (type) {
            case 0x01:
                return String.format(Locale.ROOT, "0x%02X", value[0] & 0xFF);
            case 0x02:
            case 0x03:
                return decodeUuids(value, 2);
            case 0x04:
            case 0x05:
                return decodeUuids(value, 4);
            case 0x06:
            case 0x07:
                return decodeUuids(value, 16);
            case 0x08:
            case 0x09:
                return quote(printableUtf8(value));
            case 0x0A:
                return Integer.toString(value[0]);
            case 0x0D:
                if (value.length < 3) return toHex(value);
                int cod = (value[0] & 0xFF) | ((value[1] & 0xFF) << 8) | ((value[2] & 0xFF) << 16);
                return String.format(Locale.ROOT, "0x%06X", cod);
            case 0x16:
                return serviceData(value, 2);
            case 0x20:
                return serviceData(value, 4);
            case 0x21:
                return serviceData(value, 16);
            case 0x19:
                if (value.length < 2) return toHex(value);
                return String.format(Locale.ROOT, "0x%04X",
                        (value[0] & 0xFF) | ((value[1] & 0xFF) << 8));
            case 0x24:
                return quote(printableUtf8(value));
            case 0xFF:
                if (value.length < 2) return toHex(value);
                int company = (value[0] & 0xFF) | ((value[1] & 0xFF) << 8);
                byte[] payload = new byte[value.length - 2];
                System.arraycopy(value, 2, payload, 0, payload.length);
                return "company_id=0x" + String.format(Locale.ROOT, "%04X", company)
                        + " payload=" + toHex(payload);
            default:
                return toHex(value);
        }
    }

    private static String decodeUuids(byte[] value, int width) {
        if (width <= 0 || value.length < width) return toHex(value);
        StringBuilder result = new StringBuilder();
        for (int offset = 0; offset + width <= value.length; offset += width) {
            if (result.length() > 0) result.append(',');
            long low = 0L;
            for (int index = width - 1; index >= 0; index--) {
                low = (low << 8) | (value[offset + index] & 0xFFL);
            }
            if (width == 2) {
                result.append(String.format(Locale.ROOT, "0000%04X-0000-1000-8000-00805F9B34FB", low));
            } else if (width == 4) {
                result.append(String.format(Locale.ROOT, "%08X-0000-1000-8000-00805F9B34FB", low));
            } else {
                result.append(formatUuid128(value, offset));
            }
        }
        return result.toString();
    }

    private static String formatUuid128(byte[] value, int offset) {
        StringBuilder result = new StringBuilder(36);
        for (int index = 15; index >= 0; index--) {
            result.append(String.format(Locale.ROOT, "%02X", value[offset + index] & 0xFF));
            if (index == 12 || index == 10 || index == 8 || index == 6) result.append('-');
        }
        return result.toString();
    }

    private static String serviceData(byte[] value, int uuidBytes) {
        if (value.length < uuidBytes) return toHex(value);
        byte[] uuid = new byte[uuidBytes];
        byte[] data = new byte[value.length - uuidBytes];
        System.arraycopy(value, 0, uuid, 0, uuidBytes);
        System.arraycopy(value, uuidBytes, data, 0, data.length);
        return "uuid=" + decodeUuids(uuid, uuidBytes) + " data=" + toHex(data);
    }

    private static String printableUtf8(byte[] value) {
        String decoded = new String(value, StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder(decoded.length());
        for (int index = 0; index < decoded.length(); index++) {
            char character = decoded.charAt(index);
            result.append(character < 0x20 || character == 0x7F ? '.' : character);
        }
        return result.toString();
    }

    static String quote(String value) {
        if (value == null) return "\"\"";
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t") + '"';
    }

    static String toHex(byte[] value) {
        if (value == null || value.length == 0) return "";
        char[] digits = "0123456789ABCDEF".toCharArray();
        char[] result = new char[value.length * 2];
        int offset = 0;
        for (byte item : value) {
            int unsigned = item & 0xFF;
            result[offset++] = digits[unsigned >>> 4];
            result[offset++] = digits[unsigned & 0x0F];
        }
        return new String(result);
    }
}
