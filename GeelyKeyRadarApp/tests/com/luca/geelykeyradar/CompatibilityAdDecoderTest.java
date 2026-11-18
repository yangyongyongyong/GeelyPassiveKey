package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.List;

/** 原始 BLE AD 解码回归：名称、厂商数据、Service Data 与损坏长度都必须可追溯。 */
public final class CompatibilityAdDecoderTest {
    public static void main(String[] args) {
        decodesNameManufacturerAndServiceData();
        keepsMalformedTailVisible();
        System.out.println("CompatibilityAdDecoderTest passed");
    }

    private static void decodesNameManufacturerAndServiceData() {
        byte[] name = "Geely_Auto".getBytes(StandardCharsets.UTF_8);
        byte[] raw = concat(
                new byte[]{0x02, 0x01, 0x06},
                concat(new byte[]{(byte) (name.length + 1), 0x09}, name),
                new byte[]{0x07, (byte) 0xFF, (byte) 0xFE, 0x01, 0x06, 0x02, 0x34, 0x12},
                new byte[]{0x07, 0x16, (byte) 0xFD, (byte) 0xFD, 0x01, 0x02, 0x03, 0x04}
        );
        List<CompatibilityAdDecoder.Structure> structures = CompatibilityAdDecoder.decode(raw);
        check(structures.size() == 4, "应解出四个 AD 结构");
        check(structures.get(0).describe().contains("FLAGS"), "Flags 类型应可读");
        check(structures.get(1).describe().contains("Geely_Auto"), "完整本地名应保留");
        check(structures.get(2).describe().contains("company_id=0x01FE"), "厂商 ID 应按小端解码");
        check(structures.get(2).describe().contains("payload=06023412"), "厂商 payload 原始字节应保留");
        check(structures.get(3).describe().contains("0000FDFD-0000-1000-8000-00805F9B34FB"),
                "16-bit Service Data UUID 应展开");
        check(structures.get(3).describe().contains("data=01020304"), "Service Data payload 应保留");
    }

    private static void keepsMalformedTailVisible() {
        List<CompatibilityAdDecoder.Structure> structures = CompatibilityAdDecoder.decode(
                new byte[]{0x02, 0x01, 0x06, 0x09, 0x09, 0x41});
        check(structures.size() == 2, "损坏尾部应保留为独立结构");
        check(structures.get(1).malformed, "长度越界必须标记 malformed");
        check(structures.get(1).describe().contains("090941"), "损坏尾部原始字节不能丢失");
    }

    private static byte[] concat(byte[]... values) {
        int length = 0;
        for (byte[] value : values) length += value.length;
        byte[] result = new byte[length];
        int offset = 0;
        for (byte[] value : values) {
            System.arraycopy(value, 0, result, offset, value.length);
            offset += value.length;
        }
        return result;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
