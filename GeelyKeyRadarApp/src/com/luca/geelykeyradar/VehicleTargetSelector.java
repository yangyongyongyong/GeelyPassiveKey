package com.luca.geelykeyradar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 服务主线程内按物理地址管理有界候选，只让一个可信目标供给RSSI；未知候选有固定尝试预算。 */
final class VehicleTargetSelector<T extends VehicleBeacon> {
    static final long CANDIDATE_TTL_MS = 20000L;
    static final long OBSERVATION_MS = 1500L;
    static final long ATTEMPT_MS = 12000L;
    static final long FAILED_COOLDOWN_MS = 30000L;
    static final long TARGET_FRESH_MS = 3000L;
    static final long NAME_CONFIRM_MS = 600L;
    static final int MAX_CANDIDATES = 32;

    private final Map<String, Entry<T>> entries = new LinkedHashMap<>();
    private String selectedAddress = "";
    private long selectedAtMs;
    private long discoveryStartedAtMs = -1L;
    private String lastReason = "NO_CANDIDATE";

    private static final class Entry<T extends VehicleBeacon> {
        T latest;
        T namedEvidence;
        long firstSeenAtMs;
        int sightings;
        long firstNamedAtMs;
        int namedSightings;
        long failedUntilMs;
        long connectionStartedAtMs = -1L;
        boolean firstObservationLogged;
        String namedResetReason = "NO_NAME";

        /** 一个MAC仅有一个槽位，重复时间戳不累积观察或名称确认。 */
        Entry(T ad) {
            firstSeenAtMs = ad.seenAtMs;
            update(ad);
        }

        /** 更新新鲜观测，保留近期名称，防止无名帧抹掉已观察到的冲突名称。 */
        void update(T ad) {
            latest = ad;
            sightings++;
            if (VehicleBeacon.isGeelyVehicleName(ad.name)) {
                if (namedEvidence == null || !namedEvidence.name.equalsIgnoreCase(ad.name)
                        || ad.seenAtMs - namedEvidence.seenAtMs > TARGET_FRESH_MS) {
                    namedResetReason = namedEvidence == null ? "FIRST_NAME"
                            : (!namedEvidence.name.equalsIgnoreCase(ad.name) ? "NAME_CHANGED" : "NAME_GAP");
                    firstNamedAtMs = ad.seenAtMs;
                    namedSightings = 0;
                }
                namedEvidence = ad;
                namedSightings++;
            }
        }
    }

    /** 拒绝重复与过期观测；容量满时只淘汰非主目标，避免停车场内存随设备数增长。 */
    boolean observe(T ad, long nowMs) {
        prune(nowMs);
        if (ad == null || !VehicleLearningState.hasAddress(ad.address) || !fresh(ad, nowMs)
                || ad.rssi <= -105 || ad.rssi >= 0) return false;
        Entry<T> entry = entries.get(ad.stableKey());
        if (entry != null && ad.seenAtNanos <= entry.latest.seenAtNanos) return false;
        if (entry != null && ad.seenAtMs - entry.latest.seenAtMs > CANDIDATE_TTL_MS) {
            long failedUntil = entry.failedUntilMs;
            entry = new Entry<>(ad);
            entry.failedUntilMs = failedUntil;
            entries.put(ad.stableKey(), entry);
            return true;
        }
        if (entry == null) {
            if (entries.size() >= MAX_CANDIDATES) evictNonPrimary();
            entry = new Entry<>(ad);
            entries.put(ad.stableKey(), entry);
        } else {
            entry.update(ad);
        }
        if (discoveryStartedAtMs < 0L) discoveryStartedAtMs = nowMs;
        return true;
    }

    /** 根据可信身份选择唯一目标；名称轮换需旧目标停更及多个新名称观测，匿名线索仍不可信。 */
    // 普通发现仍按原证据门槛；首包预连接是独立路径，不放宽RSSI资格。
    T select(VehicleIdentity known, boolean officialConnected, long nowMs) {
        prune(nowMs);
        if (known != null && VehicleLearningState.hasAddress(known.address)) {
            Entry<T> saved = entries.get(known.address);
            if (saved != null && fresh(saved.latest, nowMs) && match(known, saved, nowMs) == VehicleIdentity.Match.ADDRESS) {
                choose(known.address, nowMs, "LEARNED_ADDRESS");
                return saved.latest;
            }
            // 官方已有连接时不能将另一个同名或同尾号MAC抢成测距目标。
            if (officialConnected) {
                lastReason = "OFFICIAL_CONNECTION_PINNED";
                return null;
            }
        }

        Entry<T> current = entries.get(selectedAddress);
        if (current != null && fresh(current.latest, nowMs) && eligible(known, current, nowMs)) {
            if (officialConnected || nowMs - selectedAtMs < ATTEMPT_MS
                    || (known != null && match(known, current, nowMs) == VehicleIdentity.Match.ADDRESS)) {
                lastReason = known == null ? "CANDIDATE_IN_BUDGET" : "DISCOVERY_IN_BUDGET";
                return current.latest;
            }
            // 十二秒从选中时起算，持续广播不会续期；失败MAC冷却后才能重试。
            // 已先进行首包预连接的候选从更早的实际尝试起算，不因名称确认获得第二份预算。
            current.failedUntilMs = nowMs + FAILED_COOLDOWN_MS;
            current.connectionStartedAtMs = -1L;
            selectedAddress = "";
            selectedAtMs = 0L;
            lastReason = "CANDIDATE_BUDGET_EXPIRED";
        } else if (!selectedAddress.isEmpty()) {
            selectedAddress = "";
            selectedAtMs = 0L;
        }
        if (officialConnected) {
            lastReason = "WAIT_CONNECTED_PEER";
            return null;
        }

        Entry<T> best = null;
        for (Entry<T> entry : entries.values()) {
            if (!fresh(entry.latest, nowMs) || entry.failedUntilMs > nowMs || !eligible(known, entry, nowMs)) continue;
            if (best == null || entry.latest.rssi > best.latest.rssi
                    || (entry.latest.rssi == best.latest.rssi && entry.latest.seenAtMs > best.latest.seenAtMs)) best = entry;
        }
        if (best == null) {
            lastReason = entries.isEmpty() ? "NO_CANDIDATE" : "OBSERVING_OR_COOLDOWN";
            return null;
        }
        choose(best.latest.address, nowMs, known == null ? "OBSERVED_PRIMARY" : "ROTATION_DISCOVERY");
        return best.latest;
    }

    /** 确认系统真实连接后立即固定地址，广播停止也不会让同品牌邻车取代它。 */
    void pinConnected(String address, long nowMs) {
        if (!VehicleLearningState.hasAddress(address)) return;
        choose(address.toUpperCase(java.util.Locale.ROOT), nowMs, "VERIFIED_CONNECTED_PEER");
        Entry<T> selected = entries.get(selectedAddress);
        if (selected != null) {
            selectedAtMs = nowMs;
            selected.failedUntilMs = 0L;
            selected.connectionStartedAtMs = -1L;
        }
    }

    /** 返回某条广播是否仍属于唯一主目标，不在每个matches分支各自选择RSSI来源。 */
    boolean isSelected(VehicleBeacon ad) {
        return ad != null && !selectedAddress.isEmpty() && selectedAddress.equalsIgnoreCase(ad.address);
    }

    /** RSSI必须来自已验证地址和当前唯一主目标；NAME/HINT发现不具有测距资格。 */
    boolean authorizesRssi(VehicleIdentity known, VehicleBeacon ad) {
        return known != null && VehicleLearningState.hasAddress(known.address) && isSelected(ad) && known.matches(ad);
    }

    /** 首包允许完整名称，或强信号稳定厂商尾号HINT；两者都只用于官方鉴权，不授予RSSI资格。 */
    String earlyConnectionReason(VehicleIdentity known, T ad, boolean advertisedName, long nowMs) {
        if (known == null || !VehicleLearningState.hasAddress(known.address)) return "NO_LEARNED_ADDRESS";
        if (ad == null) return "NO_FRESH_FULL_NAME";
        Entry<T> entry = entries.get(ad.stableKey());
        if (entry == null || entry.latest.seenAtNanos != ad.seenAtNanos || !fresh(ad, nowMs)) return "STALE_OR_UNOBSERVED";
        if (entry.failedUntilMs > nowMs) return "CANDIDATE_COOLDOWN";
        Entry<T> saved = entries.get(known.address);
        if (saved != null && fresh(saved.latest, nowMs)
                && match(known, saved, nowMs) == VehicleIdentity.Match.ADDRESS) return "LEARNED_ADDRESS_FRESH";
        VehicleIdentity.Match currentMatch = match(known, entry, nowMs);
        if (!advertisedName) {
            if (currentMatch == VehicleIdentity.Match.HINT && ad.rssi >= -70
                    && known.stableDiscoveryMatches(ad)) return "STABLE_HINT_HIGH_RSSI";
            return "NO_FRESH_FULL_NAME";
        }
        if (!VehicleBeacon.isGeelyVehicleName(known.name) || !known.name.equalsIgnoreCase(ad.name)) {
            return "NO_FRESH_FULL_NAME";
        }
        if (currentMatch != VehicleIdentity.Match.NAME) return "NOT_NAMED_ROTATION";
        byte[] savedPrefix = known.manufacturerPrefix();
        String rawPrefix = ad.manufacturerId == VehicleBeacon.COMPANY_ID_LEGACY
                && ad.mfgSignature.startsWith("15FF") ? ad.mfgSignature.substring(4) : ad.mfgSignature;
        byte[] observedPrefix = VehicleBeacon.hexToBytes(rawPrefix);
        String tail = known.effectiveTail2();
        if (savedPrefix.length < 6 || observedPrefix.length < 6
                || known.manufacturerId != ad.manufacturerId
                || savedPrefix[0] != 6 || observedPrefix[0] != 6
                || (savedPrefix[1] & 0xFF) == 0 || (observedPrefix[1] & 0xFF) == 0
                || tail.length() != 2 || !tail.equalsIgnoreCase(ad.vinTail2)
                || !tail.equalsIgnoreCase(new String(savedPrefix, 4, 2, java.nio.charset.StandardCharsets.US_ASCII))
                || !tail.equalsIgnoreCase(new String(observedPrefix, 4, 2, java.nio.charset.StandardCharsets.US_ASCII))) {
            return "VENDOR_OR_TAIL_MISMATCH";
        }
        // 名称+Company/vendor/VIN仍只是公开发现线索；版本及中间状态字节可变化，最终由官方鉴权。
        return "FULL_NAME_VENDOR_TAIL";
    }

    /** 唤醒重试也复核近期冲突名称，避免匿名帧把已拒绝的同MAC重新送给官方。 */
    VehicleIdentity.Match connectionMatch(VehicleIdentity known, T ad, long nowMs) {
        if (known == null) return VehicleIdentity.Match.HINT;
        VehicleIdentity.Match direct = known.match(ad);
        if (direct == VehicleIdentity.Match.NONE || direct == VehicleIdentity.Match.CONFLICT) return direct;
        Entry<T> entry = ad == null ? null : entries.get(ad.stableKey());
        if (entry == null) return VehicleIdentity.Match.NONE;
        VehicleIdentity.Match latest = match(known, entry, nowMs);
        return latest == VehicleIdentity.Match.CONFLICT || latest == VehicleIdentity.Match.NONE ? latest : direct;
    }

    /** 首次实际预连接时间与后续发现选择共用预算，不因每次重试或补名而续期。 */
    long noteConnectionAttempt(String address, long nowMs) {
        Entry<T> entry = entries.get(address);
        if (entry == null) return nowMs;
        if (entry.connectionStartedAtMs < 0L) {
            entry.connectionStartedAtMs = address.equalsIgnoreCase(selectedAddress) ? selectedAtMs : nowMs;
        }
        if (address.equalsIgnoreCase(selectedAddress)) selectedAtMs = entry.connectionStartedAtMs;
        return entry.connectionStartedAtMs;
    }

    /** 单个未验证连接超时后同步撤销主候选并冷却，防止普通发现路径绕过预连接预算。 */
    void failConnectionAttempt(String address, long nowMs) {
        Entry<T> entry = entries.get(address);
        if (entry != null) {
            if (entry.failedUntilMs <= nowMs) entry.failedUntilMs = nowMs + FAILED_COOLDOWN_MS;
            entry.connectionStartedAtMs = -1L;
        }
        if (address.equalsIgnoreCase(selectedAddress)) {
            selectedAddress = "";
            selectedAtMs = 0L;
            lastReason = "CONNECTION_BUDGET_EXPIRED";
        }
    }

    /** 官方连接完成只结束临时尝试预算，不因此将候选提升为已验证地址。 */
    void finishConnectionAttempt(String address, long nowMs) {
        Entry<T> entry = entries.get(address);
        if (entry == null) return;
        entry.connectionStartedAtMs = -1L;
        if (address.equalsIgnoreCase(selectedAddress)) selectedAtMs = nowMs;
    }

    /** 已失败地址的冷却由同一候选槽位持有，停止广播或转入普通发现都不能绕过。 */
    boolean isConnectionCooling(String address, long nowMs) {
        Entry<T> entry = entries.get(address);
        return entry != null && entry.failedUntilMs > nowMs;
    }

    /** 每个有界物理候选只记录一次首次出现，名称补全不会逐包重复事件。 */
    boolean claimFirstObservation(T ad) {
        Entry<T> entry = ad == null ? null : entries.get(ad.stableKey());
        if (entry == null || entry.firstObservationLogged) return false;
        entry.firstObservationLogged = true;
        return true;
    }

    /** 返回候选的真实第一包采集时间，用于计算先观察后选择的耗时。 */
    long firstSeenAtMs(String address) {
        Entry<T> entry = entries.get(address);
        return entry == null ? 0L : entry.firstSeenAtMs;
    }

    /** 只读输出命名观察数、跨度及重置原因，不泄露名称或改动候选状态。 */
    String observationDiagnostic(T ad, long nowMs) {
        Entry<T> entry = ad == null ? null : entries.get(ad.stableKey());
        if (entry == null) return "candidate_age_ms=-1 sightings=0 named_sightings=0 named_span_ms=0 name_reset=NO_ENTRY";
        long span = entry.namedEvidence == null ? 0L : entry.namedEvidence.seenAtMs - entry.firstNamedAtMs;
        return "candidate_age_ms=" + Math.max(0L, nowMs - entry.firstSeenAtMs)
                + " sightings=" + entry.sightings + " named_sightings=" + entry.namedSightings
                + " named_span_ms=" + span + " name_reset=" + entry.namedResetReason;
    }

    /** 按最新观测及近期名称判断，已见到的明确名称冲突不能被紧随其后的匿名帧洗掉。 */
    private VehicleIdentity.Match match(VehicleIdentity known, Entry<T> entry, long nowMs) {
        if (entry.namedEvidence != null && nowMs - entry.namedEvidence.seenAtMs <= CANDIDATE_TTL_MS
                && known.conflicts(entry.namedEvidence.name, entry.namedEvidence.vinTail2, entry.namedEvidence.vinTail3)) {
            return VehicleIdentity.Match.CONFLICT;
        }
        return known.match(entry.latest);
    }

    /** 首次找车先观察；地址轮换的完整名称需确认，弱线索只允许尝试官方鉴权。 */
    private boolean eligible(VehicleIdentity known, Entry<T> entry, long nowMs) {
        if (entry.latest.rssi <= -105 || entry.latest.rssi >= 0) return false;
        VehicleIdentity.Match match = known == null ? VehicleIdentity.Match.HINT : match(known, entry, nowMs);
        // 已学地址允许-105..-95进入候选，仅用于长时无车后的官方连接宽限；RSSI授权仍单独要求已知身份。
        boolean learnedAddress = known != null && match == VehicleIdentity.Match.ADDRESS;
        if (!learnedAddress && match == VehicleIdentity.Match.HINT
                && entry.latest.rssi < VehicleWakeGate.NORMAL_CONNECT_MIN_RSSI) return false;
        if (known == null) {
            return discoveryStartedAtMs >= 0L && nowMs - discoveryStartedAtMs >= OBSERVATION_MS && entry.sightings >= 2;
        }
        if (learnedAddress) return true;
        if (match == VehicleIdentity.Match.NAME) {
            return entry.namedSightings >= 2 && entry.namedEvidence != null
                    && entry.namedEvidence.seenAtMs - entry.firstNamedAtMs >= NAME_CONFIRM_MS;
        }
        return match == VehicleIdentity.Match.HINT && entry.sightings >= 2
                && entry.latest.seenAtMs - entry.firstSeenAtMs >= OBSERVATION_MS;
    }

    /** 只有实际切换地址才重置尝试预算，同地址持续广播绝不改变selectedAtMs。 */
    private void choose(String address, long nowMs, String reason) {
        if (!address.equalsIgnoreCase(selectedAddress)) {
            selectedAddress = address;
            Entry<T> entry = entries.get(address);
            // 首包预连接已经花掉的时间不能因稍后名称确认而重新获得十二秒预算。
            selectedAtMs = entry != null && entry.connectionStartedAtMs >= 0L
                    ? entry.connectionStartedAtMs : nowMs;
        }
        lastReason = reason;
    }

    /** 使用广播采集时间检查新鲜度，不能把Handler排队时间当作刚收到的样本。 */
    private boolean fresh(VehicleBeacon ad, long nowMs) {
        return ad.seenAtMs > 0L && ad.seenAtMs <= nowMs && nowMs - ad.seenAtMs <= TARGET_FRESH_MS;
    }

    /** 定时淘汰陈旧候选，即使之后完全没有回调也不保留虚假的多车计数。 */
    void prune(long nowMs) {
        Iterator<Entry<T>> iterator = entries.values().iterator();
        while (iterator.hasNext()) {
            Entry<T> entry = iterator.next();
            if (nowMs - entry.latest.seenAtMs > CANDIDATE_TTL_MS && entry.failedUntilMs <= nowMs) iterator.remove();
        }
        if (entries.isEmpty()) discoveryStartedAtMs = -1L;
    }

    /** 容量满时优先删除最旧的非主候选，主目标不受大量路过设备挤占。 */
    private void evictNonPrimary() {
        String oldest = "";
        long oldestSeen = Long.MAX_VALUE;
        for (Map.Entry<String, Entry<T>> item : entries.entrySet()) {
            // 首包预连接尚未通过普通名称确认时，也保留它的近期真实证据供连接交叉校验。
            if (!item.getKey().equals(selectedAddress) && item.getValue().connectionStartedAtMs < 0L
                    && item.getValue().latest.seenAtMs < oldestSeen) {
                oldest = item.getKey();
                oldestSeen = item.getValue().latest.seenAtMs;
            }
        }
        if (!oldest.isEmpty()) entries.remove(oldest);
    }

    /** 查找近期命名证据供系统连接校验，不返回其他MAC的名称或无限期缓存。 */
    T evidenceFor(String address, long nowMs) {
        Entry<T> entry = entries.get(address == null ? "" : address.toUpperCase(java.util.Locale.ROOT));
        if (entry == null || nowMs - entry.latest.seenAtMs > CANDIDATE_TTL_MS) return null;
        return entry.namedEvidence != null && nowMs - entry.namedEvidence.seenAtMs <= CANDIDATE_TTL_MS
                ? entry.namedEvidence : entry.latest;
    }

    /** 快照按物理MAC返回最新广告，外部遍历不会改变候选集合。 */
    List<T> snapshot(long nowMs) {
        List<T> result = new ArrayList<>();
        for (Entry<T> entry : entries.values()) {
            if (nowMs - entry.latest.seenAtMs <= CANDIDATE_TTL_MS) result.add(entry.latest);
        }
        return result;
    }

    /** 返回仍在保留窗口内的物理地址数，不把同MAC名称补全算成多辆车。 */
    int size(long nowMs) {
        return snapshot(nowMs).size();
    }

    /** 返回唯一目标地址供脱敏日志和会话关联。 */
    String selectedAddress() {
        return selectedAddress;
    }

    /** 返回从实际选择时刻起算的预算年龄。 */
    long selectionAgeMs(long nowMs) {
        return selectedAddress.isEmpty() ? -1L : Math.max(0L, nowMs - selectedAtMs);
    }

    /** 返回最新选择分支原因，不在只读日志中重新运行选择算法。 */
    String lastReason() {
        return lastReason;
    }

    /** 重学、更换提示或关闭服务时清除候选、失败预算和旧选择。 */
    void clear() {
        entries.clear();
        selectedAddress = "";
        selectedAtMs = 0L;
        discoveryStartedAtMs = -1L;
        lastReason = "NO_CANDIDATE";
    }
}
