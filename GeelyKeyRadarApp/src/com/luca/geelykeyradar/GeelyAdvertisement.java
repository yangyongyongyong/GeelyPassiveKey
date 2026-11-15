package com.luca.geelykeyradar;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;

import java.util.List;

/**
 * 一条吉利数字钥匙 BLE 广播的 Android 包装。
 *
 * 兼容三代常见广播特征：
 * - Company ID 0x01FE（510）的 UAES/吉利 V2 厂商数据；
 * - 设备名 GeelyVehicle<VIN后三位>；
 * - Service UUID 0xFDFD + 吉利设备名；
 * - 原始 15FF 厂商帧（通过 ScanRecord 的 AD 结构解析，不再在任意字节中搜索 15FF）。
 *
 * 不写死任何 VIN、MAC 或车型。
 */
final class GeelyAdvertisement extends VehicleBeacon {
    final ScanResult scanResult;
    final boolean advertisedName;

    /** 保留原始 ScanResult 供官方 SDK 鉴权，纯数据字段来自可离线回放的解析器。 */
    private GeelyAdvertisement(ScanResult scanResult, VehicleBeacon data, boolean advertisedName) {
        super(data, scanResult.getTimestampNanos());
        this.scanResult = scanResult;
        this.advertisedName = advertisedName;
    }

    /** 从系统扫描结果解析；不是吉利钥匙广播时返回 null，采集时间不改成回调处理时间。 */
    static GeelyAdvertisement from(ScanResult result) {
        if (result == null || result.getDevice() == null) return null;
        ScanRecord record = result.getScanRecord();
        BluetoothDevice device = result.getDevice();
        String address = "";
        String name = "";
        boolean advertisedName = false;
        try {
            address = device.getAddress();
            String recordName = record == null ? null : record.getDeviceName();
            advertisedName = VehicleBeacon.isGeelyVehicleName(recordName);
            name = recordName != null && !recordName.trim().isEmpty() ? recordName : device.getName();
        } catch (SecurityException denied) {
            return null;
        }
        // 只需要 vendor/version/VIN 尾号所在的前 8 字节；不同车型的 15FF 帧长度不完全一致。
        // ScanRecord 按 AD 长度与类型拆帧，避免普通负载中的 15FF 子串被误认作厂商字段。
        VehicleBeacon data = VehicleBeacon.parse(address, name, result.getRssi(),
                result.getTimestampNanos() / 1000000L,
                manufacturerData(record, COMPANY_ID_GEELY), manufacturerData(record, COMPANY_ID_COMPAT),
                manufacturerData(record, COMPANY_ID_LEGACY), hasServiceUuid(record, SERVICE_UUID));
        return data == null ? null : new GeelyAdvertisement(result, data, advertisedName);
    }

    /** 读取结构化厂商字段，缺少字段或厂商解析失败时保留精确名称匹配路径。 */
    private static byte[] manufacturerData(ScanRecord record, int company) {
        if (record == null) return null;
        try {
            return record.getManufacturerSpecificData(company);
        } catch (RuntimeException malformed) {
            return null;
        }
    }

    /** 校验服务 UUID；裸 UUID 只是协议线索，仍需厂商字段或精确吉利名称。 */
    private static boolean hasServiceUuid(ScanRecord record, String uuid) {
        if (record == null) return false;
        List<ParcelUuid> uuids = record.getServiceUuids();
        if (uuids == null) return false;
        for (ParcelUuid item : uuids) {
            if (item != null && item.toString().equalsIgnoreCase(uuid)) return true;
        }
        return false;
    }
}
