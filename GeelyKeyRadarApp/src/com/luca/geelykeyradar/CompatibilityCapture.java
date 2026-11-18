package com.luca.geelykeyradar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** 车型兼容专用原始 BLE 抓取；只在用户明确点击上报时短暂运行，不参与身份或车控。 */
final class CompatibilityCapture {
    static final long DEFAULT_DURATION_MS = 10_000L;
    static final int MAX_DEVICES = 256;
    static final int MAX_UNIQUE_OBSERVATIONS = 1_500;
    static final int MAX_TEXT_CHARS = 1_200_000;

    private CompatibilityCapture() {
    }

    static final class Result {
        final boolean completed;
        final String text;
        final int deviceCount;
        final int observationCount;
        final int uniqueObservationCount;
        final String error;
        final long elapsedMs;

        Result(boolean completed, String text, int deviceCount, int observationCount,
               int uniqueObservationCount, String error, long elapsedMs) {
            this.completed = completed;
            this.text = text == null ? "" : text;
            this.deviceCount = deviceCount;
            this.observationCount = observationCount;
            this.uniqueObservationCount = uniqueObservationCount;
            this.error = error == null ? "" : error;
            this.elapsedMs = elapsedMs;
        }

        String diagnosticSummary() {
            return "completed=" + completed + " devices=" + deviceCount
                    + " observations=" + observationCount + " unique=" + uniqueObservationCount
                    + " elapsed_ms=" + elapsedMs + " error=" + error;
        }
    }

    private static final class DeviceSummary {
        final String address;
        String latestName = "";
        long firstWallMs;
        long lastWallMs;
        int observations;
        int uniqueObservations;
        int rssiFirst;
        int rssiMin = Integer.MAX_VALUE;
        int rssiMax = Integer.MIN_VALUE;
        int rssiLast;

        DeviceSummary(String address) {
            this.address = address;
        }

        void observe(String name, int rssi, long wallMs, boolean unique) {
            if (observations == 0) {
                firstWallMs = wallMs;
                rssiFirst = rssi;
            }
            observations++;
            if (unique) uniqueObservations++;
            if (name != null && !name.isEmpty()) latestName = name;
            lastWallMs = wallMs;
            rssiLast = rssi;
            rssiMin = Math.min(rssiMin, rssi);
            rssiMax = Math.max(rssiMax, rssi);
        }
    }

    private static final class Observation {
        final int sequence;
        final int callbackType;
        final long scanTimestampNanos;
        final long scanAgeMs;
        final String address;
        final String deviceName;
        final String recordName;
        final String rawAdHex;
        final int rssiFirst;
        final int txPowerLevel;
        final int txPower;
        final int advertiseFlags;
        final int advertisingSid;
        final int primaryPhy;
        final int secondaryPhy;
        final int periodicAdvertisingInterval;
        final int dataStatus;
        final int deviceType;
        final int addressType;
        final int bondState;
        final String bluetoothClass;
        final String deviceUuids;
        final boolean connectable;
        final String serviceUuids;
        final String manufacturerData;
        final String serviceData;
        final List<CompatibilityAdDecoder.Structure> structures;
        final long firstWallMs;
        final long firstMonoMs;
        int seenCount = 1;
        int rssiMin;
        int rssiMax;
        int rssiLast;
        long lastWallMs;
        long lastMonoMs;

        Observation(int sequence, int callbackType, String address, String deviceName, String recordName,
                    ScanResult result, long wallMs, long monoMs) {
            this.sequence = sequence;
            this.callbackType = callbackType;
            this.scanTimestampNanos = result.getTimestampNanos();
            long nowNanos = SystemClock.elapsedRealtimeNanos();
            this.scanAgeMs = scanTimestampNanos > 0L && nowNanos >= scanTimestampNanos
                    ? (nowNanos - scanTimestampNanos) / 1_000_000L : -1L;
            this.address = address;
            this.deviceName = deviceName;
            this.recordName = recordName;
            ScanRecord record = result.getScanRecord();
            byte[] raw = record == null ? null : record.getBytes();
            this.rawAdHex = CompatibilityAdDecoder.toHex(raw);
            this.rssiFirst = result.getRssi();
            this.rssiMin = result.getRssi();
            this.rssiMax = result.getRssi();
            this.rssiLast = result.getRssi();
            this.txPowerLevel = record == null ? 0 : record.getTxPowerLevel();
            this.txPower = result.getTxPower();
            this.advertiseFlags = record == null ? 0 : record.getAdvertiseFlags();
            this.advertisingSid = result.getAdvertisingSid();
            this.primaryPhy = result.getPrimaryPhy();
            this.secondaryPhy = result.getSecondaryPhy();
            this.periodicAdvertisingInterval = result.getPeriodicAdvertisingInterval();
            this.dataStatus = result.getDataStatus();
            this.deviceType = result.getDevice() == null ? 0 : result.getDevice().getType();
            this.addressType = deviceAddressType(result.getDevice());
            this.bondState = result.getDevice() == null ? 0 : result.getDevice().getBondState();
            this.bluetoothClass = bluetoothClass(result.getDevice());
            this.deviceUuids = bondedUuids(result.getDevice());
            this.connectable = result.isConnectable();
            this.serviceUuids = serviceUuids(record);
            this.manufacturerData = manufacturerData(record);
            this.serviceData = serviceData(record);
            this.structures = CompatibilityAdDecoder.decode(raw);
            this.firstWallMs = wallMs;
            this.firstMonoMs = monoMs;
            this.lastWallMs = wallMs;
            this.lastMonoMs = monoMs;
        }

        void repeated(int rssi, long wallMs, long monoMs) {
            seenCount++;
            rssiLast = rssi;
            rssiMin = Math.min(rssiMin, rssi);
            rssiMax = Math.max(rssiMax, rssi);
            lastWallMs = wallMs;
            lastMonoMs = monoMs;
        }


        String toText() {
            StringBuilder out = new StringBuilder();
            out.append("# OBS ").append(sequence).append('\n');
            out.append("addr=").append(address).append('\n');
            out.append("device_name=").append(CompatibilityAdDecoder.quote(deviceName)).append('\n');
            out.append("record_name=").append(CompatibilityAdDecoder.quote(recordName)).append('\n');
            out.append("callback_type=").append(callbackType).append('\n');
            out.append("scan_timestamp_nanos=").append(scanTimestampNanos).append('\n');
            out.append("scan_age_ms=").append(scanAgeMs).append('\n');
            out.append("first_wall_ms=").append(firstWallMs).append('\n');
            out.append("first_mono_ms=").append(firstMonoMs).append('\n');
            out.append("last_wall_ms=").append(lastWallMs).append('\n');
            out.append("last_mono_ms=").append(lastMonoMs).append('\n');
            out.append("seen=").append(seenCount).append('\n');
            out.append("rssi_first=").append(rssiFirst)
                    .append(" rssi_min=").append(rssiMin)
                    .append(" rssi_max=").append(rssiMax)
                    .append(" rssi_last=").append(rssiLast).append('\n');
            out.append("connectable=").append(connectable)
                    .append(" tx_power_level=").append(txPowerLevel)
                    .append(" tx_power=").append(txPower)
                    .append(" adv_flags=").append(String.format(Locale.ROOT, "0x%02X", advertiseFlags))
                    .append(" advertising_sid=").append(advertisingSid)
                    .append(" primary_phy=").append(primaryPhy)
                    .append(" secondary_phy=").append(secondaryPhy)
                    .append(" periodic_interval=").append(periodicAdvertisingInterval)
                    .append(" data_status=").append(dataStatus).append('\n');
            out.append("device_type=").append(deviceType)
                    .append(" address_type=").append(addressType)
                    .append(" bond_state=").append(bondStateName(bondState))
                    .append(" device_class=").append(CompatibilityAdDecoder.quote(bluetoothClass)).append('\n');
            out.append("device_uuids=").append(deviceUuids).append('\n');
            out.append("service_uuids=").append(serviceUuids).append('\n');
            out.append("manufacturer_data=").append(manufacturerData).append('\n');
            out.append("service_data=").append(serviceData).append('\n');
            out.append("raw_ad_hex=").append(rawAdHex).append('\n');
            if (structures.isEmpty()) {
                out.append("ad_structures=<none>\n");
            } else {
                for (int index = 0; index < structures.size(); index++) {
                    out.append("ad[").append(index).append("] ")
                            .append(structures.get(index).describe()).append('\n');
                }
            }
            out.append('\n');
            return out.toString();
        }
    }

    static final class Session {
        private final Context context;
        private final long durationMs;
        private final String trigger;
        private final long startedWallMs;
        private final long startedMonoMs;
        private final LinkedHashMap<String, DeviceSummary> devices = new LinkedHashMap<>();
        private final LinkedHashMap<String, Observation> observations = new LinkedHashMap<>();
        private final List<String> errors = new ArrayList<>();
        private int totalObservations;
        private int skippedDevices;
        private int skippedUniqueObservations;
        private boolean finished;

        long durationMs() {
            return durationMs;
        }

        Session(Context context, long durationMs, String trigger) {
            this.context = context.getApplicationContext();
            this.durationMs = Math.max(1_000L, Math.min(30_000L, durationMs));
            this.trigger = trigger == null ? "unknown" : trigger;
            this.startedWallMs = System.currentTimeMillis();
            this.startedMonoMs = SystemClock.elapsedRealtime();
        }

        synchronized void record(int callbackType, ScanResult result, long wallMs, long monoMs) {
            if (finished || result == null || result.getDevice() == null) return;
            totalObservations++;
            try {
                BluetoothDevice device = result.getDevice();
                String address = safeAddress(device);
                String deviceName = safeName(device);
                ScanRecord record = result.getScanRecord();
                String recordName = record == null ? "" : safe(record.getDeviceName());
                DeviceSummary summary = devices.get(address);
                if (summary == null) {
                    if (devices.size() >= MAX_DEVICES) {
                        skippedDevices++;
                        return;
                    }
                    summary = new DeviceSummary(address);
                    devices.put(address, summary);
                }
                ScanRecord scanRecord = result.getScanRecord();
                byte[] raw = scanRecord == null ? null : scanRecord.getBytes();
                String key = callbackType + "|" + address + "|" + deviceName + "|" + recordName + "|"
                        + CompatibilityAdDecoder.toHex(raw) + "|" + result.isConnectable()
                        + "|" + result.getTxPower()
                        + "|" + (scanRecord == null ? 0 : scanRecord.getTxPowerLevel())
                        + "|" + (scanRecord == null ? 0 : scanRecord.getAdvertiseFlags())
                        + "|" + result.getAdvertisingSid() + "|" + result.getPrimaryPhy()
                        + "|" + result.getSecondaryPhy() + "|" + result.getDataStatus()
                        + "|" + result.getDevice().getType() + "|" + deviceAddressType(result.getDevice());
                Observation observation = observations.get(key);
                boolean unique = false;
                if (observation == null) {
                    if (observations.size() >= MAX_UNIQUE_OBSERVATIONS) {
                        skippedUniqueObservations++;
                    } else {
                        observation = new Observation(observations.size() + 1, callbackType, address,
                                !recordName.isEmpty() ? recordName : deviceName, recordName,
                                result, wallMs, monoMs);
                        observations.put(key, observation);
                        unique = true;
                    }
                } else {
                    observation.repeated(result.getRssi(), wallMs, monoMs);
                }
                summary.observe(!recordName.isEmpty() ? recordName : deviceName,
                        result.getRssi(), wallMs, unique);
            } catch (Throwable error) {
                addError("record", error);
            }
        }

        synchronized void scanFailed(int errorCode) {
            errors.add("scan_failed error_code=" + errorCode + " label=" + scanFailureName(errorCode));
        }

        synchronized void addError(String phase, Throwable error) {
            if (error == null) {
                errors.add("phase=" + phase + " error=unknown");
                return;
            }
            errors.add("phase=" + phase + " type=" + error.getClass().getSimpleName()
                    + " message=" + safe(error.getMessage()));
        }

        synchronized Result finish(String reason, BluetoothGatt activeGatt) {
            if (finished) return buildResult(reason, activeGatt, false);
            finished = true;
            return buildResult(reason, activeGatt, true);
        }

        private Result buildResult(String reason, BluetoothGatt activeGatt, boolean completed) {
            long finishedWallMs = System.currentTimeMillis();
            long finishedMonoMs = SystemClock.elapsedRealtime();
            StringBuilder out = new StringBuilder();
            out.append("schema=1\n");
            out.append("capture_type=compatibility_raw_ble\n");
            out.append("raw_data_intentional=true\n");
            out.append("trigger=").append(trigger).append('\n');
            out.append("finish_reason=").append(safe(reason)).append('\n');
            out.append("started_wall_ms=").append(startedWallMs).append('\n');
            out.append("started_mono_ms=").append(startedMonoMs).append('\n');
            out.append("finished_wall_ms=").append(finishedWallMs).append('\n');
            out.append("finished_mono_ms=").append(finishedMonoMs).append('\n');
            out.append("duration_requested_ms=").append(durationMs).append('\n');
            out.append("duration_actual_ms=").append(Math.max(0L, finishedMonoMs - startedMonoMs)).append('\n');
            out.append("device_count=").append(devices.size()).append('\n');
            out.append("observation_count=").append(totalObservations).append('\n');
            out.append("unique_observation_count=").append(observations.size()).append('\n');
            out.append("skipped_devices=").append(skippedDevices).append('\n');
            out.append("skipped_unique_observations=").append(skippedUniqueObservations).append('\n');
            out.append("max_devices=").append(MAX_DEVICES).append('\n');
            out.append("max_unique_observations=").append(MAX_UNIQUE_OBSERVATIONS).append('\n');
            appendEnvironment(out);
            appendSystemConnections(out, activeGatt);
            appendBondedDevices(out);

            out.append("# ERRORS\n");
            if (errors.isEmpty()) out.append("errors=none\n");
            for (String error : errors) out.append(error).append('\n');


            out.append("\n# DEVICE SUMMARY\n");
            for (DeviceSummary device : devices.values()) {
                out.append("device addr=").append(device.address)
                        .append(" name=").append(CompatibilityAdDecoder.quote(device.latestName))
                        .append(" observations=").append(device.observations)
                        .append(" unique=").append(device.uniqueObservations)
                        .append(" first_wall_ms=").append(device.firstWallMs)
                        .append(" last_wall_ms=").append(device.lastWallMs)
                        .append(" rssi_first=").append(device.rssiFirst)
                        .append(" rssi_min=").append(device.rssiMin)
                        .append(" rssi_max=").append(device.rssiMax)
                        .append(" rssi_last=").append(device.rssiLast).append('\n');
            }

            out.append("\n# RAW ADVERTISEMENT OBSERVATIONS\n");
            for (Observation observation : observations.values()) out.append(observation.toText());

            String text = out.toString();
            if (text.length() > MAX_TEXT_CHARS) {
                int cut = text.lastIndexOf('\n', MAX_TEXT_CHARS);
                if (cut <= 0) cut = MAX_TEXT_CHARS;
                text = text.substring(0, cut)
                        + "\ntruncated=true original_chars=" + out.length()
                        + " kept_chars=" + cut + "\n";
            }
            String error = errors.isEmpty() ? "" : errors.get(0);
            return new Result(completed, text, devices.size(), totalObservations,
                    observations.size(), error, Math.max(0L, finishedMonoMs - startedMonoMs));
        }

        private void appendEnvironment(StringBuilder out) {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter adapter = manager == null ? BluetoothAdapter.getDefaultAdapter() : manager.getAdapter();
            out.append("\n# ENVIRONMENT\n");
            out.append("android_release=").append(safe(Build.VERSION.RELEASE)).append('\n');
            out.append("android_sdk=").append(Build.VERSION.SDK_INT).append('\n');
            out.append("manufacturer=").append(safe(Build.MANUFACTURER)).append('\n');
            out.append("brand=").append(safe(Build.BRAND)).append('\n');
            out.append("model=").append(safe(Build.MODEL)).append('\n');
            out.append("device=").append(safe(Build.DEVICE)).append('\n');
            out.append("timezone=").append(TimeZone.getDefault().getID()).append('\n');
            if (adapter == null) {
                out.append("adapter=unavailable\n");
                return;
            }
            out.append("adapter_name=").append(CompatibilityAdDecoder.quote(safe(adapter.getName()))).append('\n');
            out.append("adapter_address=").append(safeAddress(adapter)).append('\n');
            out.append("adapter_enabled=").append(adapter.isEnabled()).append('\n');
            out.append("adapter_state=").append(adapter.getState()).append('\n');
            out.append("adapter_scan_mode=").append(adapter.getScanMode()).append('\n');
            out.append("adapter_le_2m_phy=").append(adapter.isLe2MPhySupported()).append('\n');
            out.append("adapter_le_coded_phy=").append(adapter.isLeCodedPhySupported()).append('\n');
            out.append("adapter_le_extended_advertising=")
                    .append(adapter.isLeExtendedAdvertisingSupported()).append('\n');
            out.append("adapter_le_periodic_advertising=")
                    .append(adapter.isLePeriodicAdvertisingSupported()).append('\n');
            out.append("adapter_multiple_advertising=").append(adapter.isMultipleAdvertisementSupported()).append('\n');
        }

        private void appendSystemConnections(StringBuilder out, BluetoothGatt activeGatt) {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            out.append("\n# SYSTEM GATT CONNECTIONS\n");
            if (manager == null) {
                out.append("manager=unavailable\n");
            } else {
                appendConnectedProfile(out, manager, BluetoothProfile.GATT, "GATT");
                appendConnectedProfile(out, manager, BluetoothProfile.GATT_SERVER, "GATT_SERVER");
            }
            out.append("\n# APP ACTIVE GATT\n");
            if (activeGatt == null) {
                out.append("active_gatt=null\n");
                return;
            }
            try {
                BluetoothDevice device = activeGatt.getDevice();
                out.append("active_gatt_device_addr=").append(safeAddress(device)).append('\n');
                out.append("active_gatt_device_name=")
                        .append(CompatibilityAdDecoder.quote(safeName(device))).append('\n');
                List<BluetoothGattService> services = activeGatt.getServices();
                if (services == null) {
                    out.append("active_gatt_services=null\n");
                    return;
                }
                if (services.isEmpty()) out.append("active_gatt_services=<empty>\n");
                for (BluetoothGattService service : services) {
                    out.append("service uuid=").append(service.getUuid())
                            .append(" type=").append(service.getType()).append('\n');
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    if (characteristics == null) continue;
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        out.append("  characteristic uuid=").append(characteristic.getUuid())
                                .append(" properties=0x")
                                .append(String.format(Locale.ROOT, "%08X", characteristic.getProperties()))
                                .append(" permissions=0x")
                                .append(String.format(Locale.ROOT, "%08X", characteristic.getPermissions()))
                                .append('\n');
                        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                        if (descriptors == null) continue;
                        for (BluetoothGattDescriptor descriptor : descriptors) {
                            out.append("    descriptor uuid=").append(descriptor.getUuid())
                                    .append(" permissions=0x")
                                    .append(String.format(Locale.ROOT, "%08X", descriptor.getPermissions()))
                                    .append('\n');
                        }
                    }
                }
            } catch (Throwable error) {
                out.append("active_gatt_error=")
                        .append(CompatibilityAdDecoder.quote(error.getClass().getSimpleName()
                                + ": " + safe(error.getMessage()))).append('\n');
            }
        }

        private void appendConnectedProfile(StringBuilder out, BluetoothManager manager, int profile,
                                            String label) {
            try {
                List<BluetoothDevice> connected = manager.getConnectedDevices(profile);
                out.append(label).append(" count=").append(connected == null ? 0 : connected.size()).append('\n');
                if (connected == null) return;
                for (BluetoothDevice device : connected) {
                    out.append(label).append(" device addr=").append(safeAddress(device))
                            .append(" name=").append(CompatibilityAdDecoder.quote(safeName(device)))
                            .append(" type=").append(device.getType())
                            .append(" address_type=").append(deviceAddressType(device))
                            .append(" bond=").append(bondStateName(device.getBondState()))
                            .append(" uuids=").append(bondedUuids(device)).append('\n');
                }
            } catch (Throwable error) {
                out.append(label).append(" error=")
                        .append(CompatibilityAdDecoder.quote(error.getClass().getSimpleName()
                                + ": " + safe(error.getMessage()))).append('\n');
            }
        }

        private void appendBondedDevices(StringBuilder out) {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter adapter = manager == null ? BluetoothAdapter.getDefaultAdapter() : manager.getAdapter();
            out.append("\n# BONDED DEVICES\n");
            if (adapter == null) {
                out.append("adapter=unavailable\n");
                return;
            }
            try {
                java.util.Set<BluetoothDevice> bonded = adapter.getBondedDevices();
                out.append("count=").append(bonded == null ? 0 : bonded.size()).append('\n');
                if (bonded == null) return;
                for (BluetoothDevice device : bonded) {
                    out.append("bonded addr=").append(safeAddress(device))
                            .append(" name=").append(CompatibilityAdDecoder.quote(safeName(device)))
                            .append(" alias=").append(CompatibilityAdDecoder.quote(safeAlias(device)))
                            .append(" type=").append(device.getType())
                            .append(" address_type=").append(deviceAddressType(device))
                            .append(" bond=").append(bondStateName(device.getBondState()))
                            .append(" class=").append(CompatibilityAdDecoder.quote(bluetoothClass(device)))
                            .append(" uuids=").append(bondedUuids(device)).append('\n');
                }
            } catch (Throwable error) {
                out.append("error=").append(CompatibilityAdDecoder.quote(error.getClass().getSimpleName()
                        + ": " + safe(error.getMessage()))).append('\n');
            }
        }
    }

    /** 服务或权限不可用时仍生成一份可上传的明确失败说明，避免上传流程被卡死。 */
    static Result unavailable(String reason) {
        String text = "schema=1\n"
                + "capture_type=compatibility_raw_ble\n"
                + "raw_data_intentional=true\n"
                + "completed=false\n"
                + "finish_reason=" + safe(reason) + "\n"
                + "errors=" + safe(reason) + "\n";
        return new Result(false, text, 0, 0, 0, safe(reason), 0L);
    }

    /** 服务不可用时在后台直接抓取；仅作兜底，不主动连接任何设备。 */
    static Result captureStandalone(Context context, long durationMs) {
        Session session = new Session(context, durationMs, "standalone_fallback");
        CountDownLatch finished = new CountDownLatch(1);
        BluetoothLeScanner scanner = null;
        ScanCallback callback = null;
        try {
            BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter adapter = manager == null ? BluetoothAdapter.getDefaultAdapter() : manager.getAdapter();
            if (adapter == null || !adapter.isEnabled()) {
                session.addError("precondition", new IllegalStateException("bluetooth_disabled_or_unavailable"));
                return session.finish("precondition_failed", null);
            }
            scanner = adapter.getBluetoothLeScanner();
            if (scanner == null) {
                session.addError("precondition", new IllegalStateException("scanner_unavailable"));
                return session.finish("precondition_failed", null);
            }
            callback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    session.record(callbackType, result, System.currentTimeMillis(), SystemClock.elapsedRealtime());
                }

                @Override
                public void onScanFailed(int errorCode) {
                    session.scanFailed(errorCode);
                    finished.countDown();
                }
            };
            scanner.startScan(null, scanSettings(), callback);
            finished.await(session.durationMs, TimeUnit.MILLISECONDS);
            return session.finish("duration_elapsed", null);
        } catch (Throwable error) {
            session.addError("standalone_capture", error);
            return session.finish("capture_failed", null);
        } finally {
            if (scanner != null && callback != null) {
                try {
                    scanner.stopScan(callback);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    static ScanSettings scanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setReportDelay(0L)
                .build();
    }

    static String scanFailureName(int errorCode) {
        switch (errorCode) {
            case ScanCallback.SCAN_FAILED_ALREADY_STARTED: return "ALREADY_STARTED";
            case ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                return "APPLICATION_REGISTRATION_FAILED";
            case ScanCallback.SCAN_FAILED_INTERNAL_ERROR: return "INTERNAL_ERROR";
            case ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED: return "FEATURE_UNSUPPORTED";
            case ScanCallback.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES: return "OUT_OF_HARDWARE_RESOURCES";
            case ScanCallback.SCAN_FAILED_SCANNING_TOO_FREQUENTLY: return "SCANNING_TOO_FREQUENTLY";
            default: return "UNKNOWN";
        }
    }

    private static int deviceAddressType(BluetoothDevice device) {
        if (device == null) return -1;
        try {
            java.lang.reflect.Method method = device.getClass().getMethod("getAddressType");
            Object value = method.invoke(device);
            return value instanceof Integer ? (Integer) value : -1;
        } catch (Throwable ignored) {
            return -1;
        }
    }

    private static String safeAddress(BluetoothDevice device) {
        if (device == null) return "unknown";
        try {
            return safe(device.getAddress());
        } catch (Throwable ignored) {
            return "unavailable";
        }
    }

    private static String safeAddress(BluetoothAdapter adapter) {
        if (adapter == null) return "unavailable";
        try {
            return safe(adapter.getAddress());
        } catch (Throwable ignored) {
            return "unavailable";
        }
    }

    private static String safeName(BluetoothDevice device) {
        if (device == null) return "";
        try {
            return safe(device.getName());
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String safeAlias(BluetoothDevice device) {
        if (device == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return "";
        try {
            java.lang.reflect.Method method = device.getClass().getMethod("getAliasName");
            Object value = method.invoke(device);
            return value instanceof String ? safe((String) value) : "";
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String bluetoothClass(BluetoothDevice device) {
        if (device == null) return "";
        try {
            return device.getBluetoothClass() == null ? "" : device.getBluetoothClass().toString();
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String bondedUuids(BluetoothDevice device) {
        if (device == null) return "";
        try {
            ParcelUuid[] uuids = device.getUuids();
            if (uuids == null || uuids.length == 0) return "";
            List<String> values = new ArrayList<>();
            for (ParcelUuid uuid : uuids) if (uuid != null) values.add(uuid.toString());
            Collections.sort(values);
            return String.join(",", values);
        } catch (Throwable ignored) {
            return "";
        }
    }

    private static String serviceUuids(ScanRecord record) {
        if (record == null) return "";
        List<ParcelUuid> uuids = record.getServiceUuids();
        if (uuids == null || uuids.isEmpty()) return "";
        List<String> values = new ArrayList<>();
        for (ParcelUuid uuid : uuids) if (uuid != null) values.add(uuid.toString());
        Collections.sort(values);
        return String.join(",", values);
    }

    private static String manufacturerData(ScanRecord record) {
        if (record == null) return "";
        SparseArray<byte[]> data = record.getManufacturerSpecificData();
        if (data == null || data.size() == 0) return "";
        List<String> values = new ArrayList<>();
        for (int index = 0; index < data.size(); index++) {
            int company = data.keyAt(index);
            values.add(String.format(Locale.ROOT, "0x%04X:%s", company,
                    CompatibilityAdDecoder.toHex(data.valueAt(index))));
        }
        Collections.sort(values);
        return String.join(",", values);
    }

    private static String serviceData(ScanRecord record) {
        if (record == null) return "";
        Map<ParcelUuid, byte[]> data = record.getServiceData();
        if (data == null || data.isEmpty()) return "";
        List<String> values = new ArrayList<>();
        for (Map.Entry<ParcelUuid, byte[]> entry : data.entrySet()) {
            if (entry.getKey() == null) continue;
            values.add(entry.getKey() + ":" + CompatibilityAdDecoder.toHex(entry.getValue()));
        }
        Collections.sort(values);
        return String.join(",", values);
    }

    static String bondStateName(int state) {
        switch (state) {
            case BluetoothDevice.BOND_NONE: return "NONE";
            case BluetoothDevice.BOND_BONDING: return "BONDING";
            case BluetoothDevice.BOND_BONDED: return "BONDED";
            default: return Integer.toString(state);
        }
    }

    private static String safe(String value) {
        if (value == null) return "";
        return value.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
    }


}
