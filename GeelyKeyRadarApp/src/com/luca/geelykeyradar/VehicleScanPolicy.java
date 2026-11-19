package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 生成可离线验证的硬件过滤计划，学习后替换宽过滤而非将精确条件OR到宽过滤中。 */
final class VehicleScanPolicy {
    enum Kind { ADDRESS, NAME, MANUFACTURER, SERVICE }

    static final class Rule {
        final Kind kind;
        final String value;
        final int manufacturerId;
        final byte[] data;
        final byte[] mask;

        /** 每条硬件规则独立匹配，整份列表是OR；空厂商数据只能用于首次学习模式。 */
        Rule(Kind kind, String value, int manufacturerId, byte[] data, byte[] mask) {
            this.kind = kind;
            this.value = value;
            this.manufacturerId = manufacturerId;
            this.data = data.clone();
            this.mask = mask.clone();
        }

        /** 离线检验同厂商不同尾号是否被硬件计划排除，真实Android转换仍采用同一字段。 */
        boolean matches(VehicleBeacon ad) {
            if (ad == null) return false;
            if (kind == Kind.ADDRESS) return value.equalsIgnoreCase(ad.address);
            if (kind == Kind.NAME) return value.equalsIgnoreCase(ad.name);
            if (kind == Kind.SERVICE) return ad.sourceTag.startsWith("FDFD");
            if (manufacturerId != ad.manufacturerId) return false;
            if (data.length == 0) return true;
            byte[] candidate = VehicleDiscoverySignature.payloadPrefix(ad.manufacturerId, ad.mfgSignature);
            if (candidate.length < data.length) return false;
            for (int index = 0; index < data.length; index++) {
                if ((candidate[index] & mask[index]) != (data[index] & mask[index])) return false;
            }
            return true;
        }

        /** 比较规则实际内容而不是identity对象引用，状态补全不会逐包重启扫描。 */
        String key() {
            return kind + ":" + value + ":" + manufacturerId + ":" + VehicleBeacon.bytesToHex(data)
                    + ":" + VehicleBeacon.bytesToHex(mask);
        }
    }

    static final class Plan {
        final String mode;
        final List<Rule> rules;
        final String key;

        /** 固定过滤快照，应用该计划期间身份变化由节流重配置任务处理。 */
        Plan(String mode, List<Rule> rules) {
            this.mode = mode;
            this.rules = Collections.unmodifiableList(new ArrayList<>(rules));
            StringBuilder result = new StringBuilder(mode);
            for (Rule rule : rules) result.append('|').append(rule.key());
            key = result.toString();
        }

        /** 只供回放验证，运行时由Android ScanFilter执行硬件/平台匹配。 */
        boolean matches(VehicleBeacon ad) {
            for (Rule rule : rules) if (rule.matches(ad)) return true;
            return false;
        }
    }

    /** 可信地址优先，同时保留完整名称与稳定厂商掩码发现轮换地址；不保留裸厂商/UUID宽过滤。 */
    static Plan build(VehicleIdentity identity) {
        List<Rule> rules = new ArrayList<>();
        if (identity != null) {
            if (VehicleLearningState.hasAddress(identity.address)) {
                rules.add(simple(Kind.ADDRESS, identity.address));
            }
            String name = VehicleBeacon.isRecognizableVehicleName(identity.name) ? identity.name
                    : (identity.vinTail3.length() == 3 ? "GeelyVehicle" + identity.vinTail3 : "");
            if (VehicleBeacon.isRecognizableVehicleName(name)) rules.add(simple(Kind.NAME, name));
            if (identity.hasUsableSignature()) {
                byte[] prefix = identity.manufacturerPrefix();
                if (VehicleDiscoverySignature.isUsableForTail(prefix, identity.effectiveTail2())) {
                    // 官方四字节VIN与两字节VIN分支都保留；两掩码只做发现，软件仍要求HINT。
                    for (byte[] mask : VehicleDiscoverySignature.masks()) {
                        rules.add(new Rule(Kind.MANUFACTURER, "", identity.manufacturerId, prefix, mask));
                    }
                }
            } else if (!VehicleLearningState.hasAddress(identity.address) && identity.effectiveTail2().length() == 2) {
                // 手填尾号只限制发现范围，不完成学习，也不允许匿名同尾号直接供给RSSI。
                byte[] tail = identity.effectiveTail2().getBytes(StandardCharsets.US_ASCII);
                byte[] prefix = new byte[]{6, 0, 0, 0, tail[0], tail[1]};
                byte[] mask = new byte[]{(byte) 0xFF, 0, 0, 0, (byte) 0xFF, (byte) 0xFF};
                rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_GEELY, prefix, mask));
                rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_COMPAT, prefix, mask));
                rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_LEGACY, prefix, mask));
            }
        }
        if (!rules.isEmpty()) {
            return new Plan(identity != null && VehicleLearningState.hasAddress(identity.address)
                    ? "TRACKING" : "HINT_DISCOVERY", rules);
        }
        // 首次学习才能使用协议级宽过滤，仍保持非空ScanFilter以支持息屏扫描。
        rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_GEELY, new byte[0], new byte[0]));
        rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_COMPAT, new byte[0], new byte[0]));
        rules.add(new Rule(Kind.MANUFACTURER, "", VehicleBeacon.COMPANY_ID_LEGACY, new byte[0], new byte[0]));
        rules.add(simple(Kind.SERVICE, VehicleBeacon.SERVICE_UUID));
        rules.add(simple(Kind.NAME, "Geely_Auto"));
        rules.add(simple(Kind.NAME, "GeelyAuto"));
        return new Plan("LEARNING", rules);
    }

    /** 地址、名称及UUID规则不携带无关厂商字段，避免误用列表AND语义。 */
    private static Rule simple(Kind kind, String value) {
        return new Rule(kind, value, -1, new byte[0], new byte[0]);
    }

    static final class RestartBudget {
        static final long WINDOW_MS = 30000L;
        static final long MIN_GAP_MS = 1000L;
        static final int MAX_STARTS = 4;
        private final ArrayDeque<Long> starts = new ArrayDeque<>();

        /** 同一进程所有启动/恢复共用四次三十秒预算，避免反复补全身份触发系统扫描限频。 */
        long delayMs(long nowMs) {
            while (!starts.isEmpty() && nowMs - starts.peekFirst() >= WINDOW_MS) starts.removeFirst();
            long delay = starts.isEmpty() ? 0L : Math.max(0L, starts.peekLast() + MIN_GAP_MS - nowMs);
            if (starts.size() >= MAX_STARTS) delay = Math.max(delay, starts.peekFirst() + WINDOW_MS - nowMs);
            return delay;
        }

        /** 在真正调用startScan之前记录，异常启动也计入预算；调用者必须先检查delayMs。 */
        void started(long nowMs) {
            starts.addLast(nowMs);
        }
    }
}
