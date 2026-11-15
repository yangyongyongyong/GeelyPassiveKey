package com.luca.geelykeyradar;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** 独立管理提前连接、单个在途目标和ACL握手保护，绝不保存身份或供给测距/车控。 */
final class VehicleWakeGate<T extends VehicleBeacon> {
    static final long ACL_AUTH_GRACE_MS = 5000L;
    // 常规连接门槛；长时间无车后的首个明确候选可单独放宽，但仍不授予RSSI或车控资格。
    static final int NORMAL_CONNECT_MIN_RSSI = -95;
    static final int LONG_ABSENCE_CONNECT_MIN_RSSI = -102;
    static final long LONG_ABSENCE_MS = 5 * 60 * 1000L;

    enum Result {
        SEND_SELECTED, SEND_EARLY_NAME, SEND_EARLY_HINT, DISABLED, OFFICIAL_READY, TARGET_REJECTED,
        AD_STALE, WEAK_SIGNAL, NOT_SELECTED, CANDIDATE_COOLDOWN, CANDIDATE_IN_FLIGHT,
        WAIT_ACL_AUTH, THROTTLED, BACKOFF, DUPLICATE_EARLY_AD
    }

    private final VehicleTargetSelector<T> targets;
    private final Map<String, Long> aclConnectedAtMs = new LinkedHashMap<>();
    private String activeAddress = "";
    private String protectedAclAddress = "";
    private String knownAddress = "";
    private boolean waitVerifiedEarlyPeer;
    private long startedAtMs;
    private long lastAttemptAtMs;
    private long longAbsenceGrantedAtMs;
    private int attempts;
    private T lastWakeAd;
    private String lastReason = "NO_ATTEMPT";
    private String earlyReason = "NOT_CHECKED";

    /** 与普通候选共用未知连接预算和失败冷却，调用方只需维护一个唤醒门控。 */
    VehicleWakeGate(VehicleTargetSelector<T> targets) {
        this.targets = targets;
    }

    /** 原selected路径保留；只有已学完整名与当前广播厂商一致的非主候选允许首包提前连接。 */
    Result claim(T ad, VehicleIdentity known, boolean enabled, boolean officialConnected,
                 boolean advertisedName, long nowMs) {
        return claim(ad, known, enabled, officialConnected, advertisedName, nowMs, false);
    }

    /** 长时间无车后的首个明确本车候选可把连接门槛放宽到-102；仍只用于官方鉴权。 */
    Result claim(T ad, VehicleIdentity known, boolean enabled, boolean officialConnected,
                 boolean advertisedName, long nowMs, boolean longAbsenceFirstCandidate) {
        return claim(ad, known, enabled, officialConnected, advertisedName, nowMs, longAbsenceFirstCandidate, 0L);
    }

    /** 弱信号宽限只在长时间无车会话中首次通过，单地址预算结束后不能无限使用。 */
    Result claim(T ad, VehicleIdentity known, boolean enabled, boolean officialConnected,
                 boolean advertisedName, long nowMs, boolean longAbsenceFirstCandidate, long absenceSinceMs) {
        if (!enabled) return result(Result.DISABLED);
        if (officialConnected) return result(Result.OFFICIAL_READY);
        knownAddress = known == null ? "" : known.address;
        maintain(known, nowMs);
        if (ad == null || !VehicleLearningState.hasAddress(ad.address)) return result(Result.TARGET_REJECTED);
        if (ad.seenAtMs <= 0L || ad.seenAtMs > nowMs
                || nowMs - ad.seenAtMs > VehicleTargetSelector.TARGET_FRESH_MS) return result(Result.AD_STALE);
        VehicleIdentity.Match match = targets.connectionMatch(known, ad, nowMs);
        if (match == VehicleIdentity.Match.NONE || match == VehicleIdentity.Match.CONFLICT) return result(Result.TARGET_REJECTED);
        boolean strongIdentity = known != null && VehicleLearningState.hasAddress(known.address)
                && (match == VehicleIdentity.Match.ADDRESS || match == VehicleIdentity.Match.NAME);
        boolean absenceGrant = longAbsenceFirstCandidate && absenceSinceMs > 0L
                && nowMs >= absenceSinceMs && nowMs - absenceSinceMs >= LONG_ABSENCE_MS
                && (longAbsenceGrantedAtMs == 0L || nowMs - longAbsenceGrantedAtMs >= LONG_ABSENCE_MS);
        // 首次学习阶段没有已学地址，按历史协议宽过滤观察后仍允许走官方鉴权；不产生RSSI或车控资格。
        boolean firstLearning = known == null && longAbsenceFirstCandidate && absenceSinceMs > 0L
                && nowMs >= absenceSinceMs && nowMs - absenceSinceMs >= LONG_ABSENCE_MS
                && ad.rssi >= NORMAL_CONNECT_MIN_RSSI;
        int minimumRssi = strongIdentity && absenceGrant ? LONG_ABSENCE_CONNECT_MIN_RSSI : NORMAL_CONNECT_MIN_RSSI;
        if (targets.isConnectionCooling(ad.address, nowMs)) return result(Result.CANDIDATE_COOLDOWN);
        boolean selected = targets.isSelected(ad);
        if (ad.rssi < minimumRssi || ad.rssi >= 0) return result(Result.WEAK_SIGNAL);
        earlyReason = targets.earlyConnectionReason(known, ad, advertisedName, nowMs);
        // 当前MAC已被普通选择，已学习本车主路径可立即连接，不应被-95强加第二道等待。
        if (selected && strongIdentity && "LEARNED_ADDRESS_FRESH".equals(earlyReason)) {
            earlyReason = "SELECTED_LEARNED_ADDRESS";
        }
        if (absenceGrant && strongIdentity && ad.rssi < NORMAL_CONNECT_MIN_RSSI
                && "NO_FRESH_FULL_NAME".equals(earlyReason)) {
            // 长时间无车后的第一个已验证MAC候选不需要再等普通选择；弱包只负责提前叫醒官方。
            earlyReason = "LONG_ABSENCE_STRONG_IDENTITY";
        }
        boolean earlyName = "FULL_NAME_VENDOR_TAIL".equals(earlyReason);
        boolean earlyHint = "STABLE_HINT_HIGH_RSSI".equals(earlyReason);
        boolean longAbsenceBridge = "LONG_ABSENCE_STRONG_IDENTITY".equals(earlyReason)
                || "SELECTED_LEARNED_ADDRESS".equals(earlyReason);
        if (firstLearning) {
            earlyReason = "FIRST_LEARNING_SELECTED";
            longAbsenceBridge = true;
        }
        if (!selected && !earlyName && !earlyHint && !longAbsenceBridge) return result(Result.NOT_SELECTED);
        if (hasRecentAcl(ad.address, nowMs)) {
            protectedAclAddress = ad.address;
            return result(Result.WAIT_ACL_AUTH);
        }
        if (hasRecentAcl(protectedAclAddress, nowMs)
                || (!activeAddress.isEmpty() && hasRecentAcl(activeAddress, nowMs))
                || (known != null && hasRecentAcl(known.address, nowMs))) return result(Result.WAIT_ACL_AUTH);
        boolean sameAddress = ad.address.equalsIgnoreCase(activeAddress);
        boolean activeUnverified = !activeAddress.isEmpty()
                && (known == null || !activeAddress.equalsIgnoreCase(known.address));
        boolean learnedAddress = known != null && ad.address.equalsIgnoreCase(known.address)
                && match == VehicleIdentity.Match.ADDRESS && selected;
        if (!sameAddress && activeUnverified && !learnedAddress) return result(Result.CANDIDATE_IN_FLIGHT);
        if (!sameAddress && activeUnverified) {
            // 无ACL握手时让位给真正已验证的MAC，不能被一个误中的同名首包拖住十二秒。
            targets.failConnectionAttempt(activeAddress, nowMs);
        }
        if (!selected && sameAddress && lastWakeAd != null
                && ad.seenAtNanos <= lastWakeAd.seenAtNanos) return result(Result.DUPLICATE_EARLY_AD);
        if (sameAddress) {
            long throttle = ad.rssi >= -88 ? 1500L : 2500L;
            if (lastAttemptAtMs > 0L && nowMs - lastAttemptAtMs < throttle) return result(Result.THROTTLED);
            if (attempts >= 6 && nowMs - lastAttemptAtMs < 5000L) return result(Result.BACKOFF);
        } else {
            activeAddress = ad.address;
            startedAtMs = targets.noteConnectionAttempt(ad.address, nowMs);
            attempts = 0;
        }
        lastAttemptAtMs = nowMs;
        lastWakeAd = ad;
        attempts++;
        // 一旦真正使用了-102弱信号宽限，就记录时间；同一长时间无车会话不会再次续用。
        if ((absenceGrant && ad.rssi < NORMAL_CONNECT_MIN_RSSI) || firstLearning) {
            longAbsenceGrantedAtMs = nowMs;
        }
        if (!selected) waitVerifiedEarlyPeer = true;
        if (selected) return result(Result.SEND_SELECTED);
        return result(earlyHint || longAbsenceBridge ? Result.SEND_EARLY_HINT : Result.SEND_EARLY_NAME);
    }

    /** 无回调时也从第一次尝试起算十二秒；名称确认、匿名补包和选择切换均不续期。 */
    String maintain(VehicleIdentity known, long nowMs) {
        pruneAcl(nowMs);
        if (activeAddress.isEmpty() || (known != null && activeAddress.equalsIgnoreCase(known.address))) return "";
        if (targets.isConnectionCooling(activeAddress, nowMs)
                || nowMs - startedAtMs >= VehicleTargetSelector.ATTEMPT_MS) {
            String expired = activeAddress;
            if (hasRecentAcl(expired, nowMs)) protectedAclAddress = expired;
            targets.failConnectionAttempt(expired, nowMs);
            clearAttempt();
            lastReason = "CONNECTION_BUDGET_EXPIRED";
            return expired;
        }
        return "";
    }

    /** 按MAC保存真实底层连接事实，不把邻车ACL或候选选择变化当作本车鉴权就绪。 */
    void onAclConnected(String address, long nowMs) {
        if (!VehicleLearningState.hasAddress(address)) return;
        pruneAcl(nowMs);
        String key = address.toUpperCase(Locale.ROOT);
        if (!aclConnectedAtMs.containsKey(key) && aclConnectedAtMs.size() >= VehicleTargetSelector.MAX_CANDIDATES) {
            Iterator<String> iterator = aclConnectedAtMs.keySet().iterator();
            iterator.next();
            iterator.remove();
        }
        aclConnectedAtMs.put(key, nowMs);
        if (key.equalsIgnoreCase(activeAddress)) protectedAclAddress = key;
    }

    /** 只清除同一物理地址的握手保护，旧地址迟到断连不解除新地址保护。 */
    void onAclDisconnected(String address) {
        if (address != null) aclConnectedAtMs.remove(address.toUpperCase(Locale.ROOT));
    }

    /** 官方真正就绪后停止当前注入会话；不改变RSSI状态机或已发送动作锁存。 */
    void onOfficialConnected(long nowMs) {
        targets.finishConnectionAttempt(activeAddress, nowMs);
        clearAttempt();
        protectedAclAddress = "";
    }

    /** 官方连接建立但鉴权/按钮未就绪时重置唤醒预算，防止同一接近会话被旧12秒预算挡住。 */
    void renewForPendingUnlock(long nowMs) {
        if (activeAddress.isEmpty()) return;
        startedAtMs = nowMs;
        lastAttemptAtMs = 0L;
        protectedAclAddress = "";
    }

    /** 快速路径不能拿旧学习MAC冒充本次连接；实际唯一连接采用后才解除车控/GATT门控。 */
    void onVerifiedConnectedPeer() {
        waitVerifiedEarlyPeer = false;
    }

    /** 只约束新增首包路径；原已验证MAC的测距、离开状态机和发送规则不变。 */
    boolean needsVerifiedEarlyPeer() {
        return waitVerifiedEarlyPeer;
    }

    /** 供主服务判断主MiuiService是否已在兜底窗口内建立同MAC ACL。 */
    boolean hasRecentAclHandshake(String address, long nowMs) {
        return hasRecentAcl(address, nowMs);
    }

    /** 官方断连后开启新的预连接会话，已失败候选的冷却仍由selector保留。 */
    void onOfficialDisconnected() {
        clearAttempt();
        aclConnectedAtMs.clear();
        protectedAclAddress = "";
        waitVerifiedEarlyPeer = false;
        lastReason = "OFFICIAL_DISCONNECTED";
    }

    /** 对某个MAC只保护真实ACL之后五秒，计时到期不等于声明鉴权成功。 */
    private boolean hasRecentAcl(String address, long nowMs) {
        Long connectedAt = aclConnectedAtMs.get(address == null ? "" : address.toUpperCase(Locale.ROOT));
        return connectedAt != null && nowMs >= connectedAt && nowMs - connectedAt < ACL_AUTH_GRACE_MS;
    }

    /** 移除已结束的握手观察窗口，长期多车环境下不会积累无限地址。 */
    private void pruneAcl(long nowMs) {
        Iterator<Long> iterator = aclConnectedAtMs.values().iterator();
        while (iterator.hasNext()) {
            long observedAt = iterator.next();
            if (nowMs - observedAt >= ACL_AUTH_GRACE_MS) iterator.remove();
        }
    }

    /** 记录实际返回分支，日志与计数不需要重新执行有副作用的判定。 */
    private Result result(Result value) {
        lastReason = value.name();
        return value;
    }

    /** 返回本次是否使用长时间无车后的弱信号连接门槛，供日志区分。 */
    String minimumRssiReason() {
        return earlyReason;
    }

    /** 返回实际注入次数，供原唤醒日志使用。 */
    int attempts() {
        return attempts;
    }

    /** 返回最后真实交给官方的广播，候选确认后仍保留同地址节流证据。 */
    T lastWakeAdvertisement() {
        return lastWakeAd;
    }

    /** 返回当前单一连接尝试地址；它没有身份或测距权限。 */
    String activeAddress() {
        return activeAddress;
    }

    /** 只输出原因、次数与年龄；地址由服务统一指纹化，不暴露车名或钥匙字段。 */
    String diagnosticSnapshot(long nowMs) {
        return "wake_reason=" + lastReason + " early_reason=" + earlyReason
                + " wake_attempts=" + attempts + " wake_attempt_age_ms="
                + (activeAddress.isEmpty() ? -1L : Math.max(0L, nowMs - startedAtMs))
                + " wake_last_age_ms=" + (lastAttemptAtMs <= 0L ? -1L : Math.max(0L, nowMs - lastAttemptAtMs))
                + " wake_wait_verified_peer=" + waitVerifiedEarlyPeer;
    }

    /** 清除连接尝试但不影响selector候选或测距，避免双份预算状态。 */
    private void clearAttempt() {
        activeAddress = "";
        startedAtMs = 0L;
        lastAttemptAtMs = 0L;
        attempts = 0;
        lastWakeAd = null;
        knownAddress = "";
    }

    /** 总开关关闭、重学、更换提示或蓝牙关闭时撤销全部临时唤醒证据。 */
    void clear() {
        clearAttempt();
        aclConnectedAtMs.clear();
        protectedAclAddress = "";
        waitVerifiedEarlyPeer = false;
        lastReason = "NO_ATTEMPT";
        earlyReason = "NOT_CHECKED";
        longAbsenceGrantedAtMs = 0L;
    }
}
