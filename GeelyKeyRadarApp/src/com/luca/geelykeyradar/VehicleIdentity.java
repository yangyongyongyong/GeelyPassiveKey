package com.luca.geelykeyradar;

import java.util.Locale;

/**
 * 本车蓝牙钥匙身份。
 *
 * 这些字段全部在运行时从广播/官方 App 连接事件学习，不依赖 adb、REA 或用户手填 VIN。
 */
final class VehicleIdentity {
    enum Match {
        ADDRESS, NAME, HINT, CONFLICT, NONE
    }

    final String address;
    final String name;
    final String vinTail2;
    final String vinTail3;
    final String mfgSignature;
    final int manufacturerId;
    final long learnedAtMs;

    /** 兼容旧版存储与调用，旧厂商前缀仅用于限制发现范围，不当作完整车辆身份。 */
    VehicleIdentity(String address, String name, String vinTail2, String vinTail3,
                    String mfgSignature, long learnedAtMs) {
        this(address, name, vinTail2, vinTail3, mfgSignature,
                inferManufacturerId(mfgSignature), learnedAtMs);
    }

    /** 保存可信地址与匹配提示，厂商 ID 随前缀一起保存，避免跨协议误用硬件过滤。 */
    VehicleIdentity(String address, String name, String vinTail2, String vinTail3,
                    String mfgSignature, int manufacturerId, long learnedAtMs) {
        this.address = clean(address).toUpperCase(Locale.ROOT);
        this.name = clean(name);
        this.vinTail2 = clean(vinTail2);
        this.vinTail3 = clean(vinTail3);
        this.mfgSignature = clean(mfgSignature).toUpperCase(Locale.ROOT);
        this.manufacturerId = manufacturerId;
        this.learnedAtMs = learnedAtMs;
    }

    /** 从有实际连接证据的广播生成身份，不能仅凭扫描第一包调用此方法完成学习。 */
    static VehicleIdentity from(VehicleBeacon ad) {
        if (ad == null) return null;
        String tail2 = !ad.vinTail2.isEmpty() ? ad.vinTail2 : tail2(ad.vinTail3);
        return new VehicleIdentity(ad.address, VehicleBeacon.isRecognizableVehicleName(ad.name) ? ad.name : "",
                tail2, ad.vinTail3, ad.mfgSignature, ad.manufacturerId, System.currentTimeMillis());
    }

    /**
     * 判断一条广播是否属于本车。
     *
     * 地址可能随车机蓝牙模块重启而变化，所以名称/VIN 尾号/厂商数据签名都是备用身份。
     * v4.1.1将这些备用字段分级：明确名称冲突优先排除；仅已验证地址命中可参与测距。
     * VIN 后两位及稳定厂商掩码只有发现线索资格，不再单独将匿名广播判为本车。
     */
    boolean matches(VehicleBeacon ad) {
        return match(ad) == Match.ADDRESS;
    }

    /** 返回实际匹配依据；弱尾号/签名不能覆盖已有地址，MAC命中也不能跳过车名冲突。 */
    Match match(VehicleBeacon ad) {
        if (ad == null) return Match.NONE;
        if (conflicts(ad.name, ad.vinTail2, ad.vinTail3)) return Match.CONFLICT;
        if (!address.isEmpty() && address.equalsIgnoreCase(ad.address)) return Match.ADDRESS;
        if (matchesName(ad.name)) return Match.NAME;
        if (VehicleBeacon.isGeelyAutoName(name) && VehicleBeacon.isGeelyAutoName(ad.name)) return Match.HINT;
        boolean tailMatches = !effectiveTail2().isEmpty() && effectiveTail2().equalsIgnoreCase(ad.vinTail2);
        boolean signatureMatches = hasUsableSignature() && manufacturerId == ad.manufacturerId
                && mfgSignature.equalsIgnoreCase(ad.mfgSignature);
        if (tailMatches && (mfgSignature.isEmpty() || signatureMatches || stableDiscoveryMatches(ad))) {
            return Match.HINT;
        }
        return Match.NONE;
    }

    /**
     * 按官方 V2 稳定位判断匿名轮换广播是否可作为本车发现线索。
     *
     * 即使单独调用也先执行冲突拒绝；本方法只返回 HINT 资格，不能改变可信地址或 RSSI 授权。
     */
    boolean stableDiscoveryMatches(VehicleBeacon ad) {
        if (ad == null || conflicts(ad.name, ad.vinTail2, ad.vinTail3)) return false;
        if (!hasUsableSignature() || manufacturerId != ad.manufacturerId) return false;
        String tail = effectiveTail2();
        if (tail.length() != 2 || !tail.equalsIgnoreCase(ad.vinTail2)) return false;
        byte[] observed = VehicleDiscoverySignature.payloadPrefix(ad.manufacturerId, ad.mfgSignature);
        return VehicleDiscoverySignature.matches(manufacturerPrefix(), observed);
    }

    /** 判断完整名称是否匹配；公共 GeelyVehicle 前缀不是跨地址身份依据。 */
    boolean matchesName(String observedName) {
        if (!VehicleBeacon.isGeelyVehicleName(observedName)) return false;
        if (VehicleBeacon.isGeelyVehicleName(name)) return name.equalsIgnoreCase(observedName);
        return !vinTail3.isEmpty() && vinTail3.equalsIgnoreCase(VehicleBeacon.vinTailFromName(observedName));
    }

    /** 显式尾号或车名不一致时拒绝，防止先命中旧MAC再把邻车车名合并进来。 */
    boolean conflicts(String observedName, String observedTail2, String observedTail3) {
        if (VehicleBeacon.isGeelyVehicleName(observedName) && VehicleBeacon.isGeelyVehicleName(name)
                && !name.equalsIgnoreCase(observedName)) return true;
        if (!vinTail3.isEmpty() && observedTail3 != null && !observedTail3.isEmpty()
                && !vinTail3.equalsIgnoreCase(observedTail3)) return true;
        String observedTwo = observedTail2 == null || observedTail2.isEmpty()
                ? tail2(observedTail3) : observedTail2;
        return !effectiveTail2().isEmpty() && !observedTwo.isEmpty()
                && !effectiveTail2().equalsIgnoreCase(observedTwo);
    }

    /** 从系统可见的真实连接补全身份；调用方仍必须核验官方就绪以及连接设备唯一性。 */
    static VehicleIdentity fromConnectedPeer(String address, String observedName, VehicleBeacon evidence,
                                             VehicleIdentity known) {
        if (!VehicleLearningState.hasAddress(address)) return null;
        String peerName = clean(observedName);
        String peerTail = VehicleBeacon.vinTailFromName(peerName);
        boolean sameEvidenceAddress = evidence != null && address.equalsIgnoreCase(evidence.address);
        if (sameEvidenceAddress && from(evidence).conflicts(peerName, "", peerTail)) return null;
        if (known != null) {
            if (known.conflicts(peerName, "", peerTail)) return null;
            Match match = sameEvidenceAddress ? known.match(evidence) : Match.NONE;
            if (match == Match.CONFLICT) return null;
            boolean sameAddress = !known.address.isEmpty() && known.address.equalsIgnoreCase(address);
            boolean sameAutoFamily = VehicleBeacon.isGeelyAutoName(peerName)
                    && VehicleBeacon.isGeelyAutoName(known.name);
            if (!sameAddress && !known.matchesName(peerName) && !sameAutoFamily
                    && match != Match.ADDRESS && match != Match.NAME && match != Match.HINT) return null;
        } else if (!VehicleBeacon.isRecognizableVehicleName(peerName) && !sameEvidenceAddress) {
            return null;
        }
        VehicleIdentity base = known != null ? known : (sameEvidenceAddress ? from(evidence)
                : new VehicleIdentity(address, peerName, tail2(peerTail), peerTail, "", System.currentTimeMillis()));
        String newName = VehicleBeacon.isRecognizableVehicleName(peerName) ? peerName : base.name;
        if (newName.isEmpty() && sameEvidenceAddress && VehicleBeacon.isRecognizableVehicleName(evidence.name)) {
            newName = evidence.name;
        }
        String newTail3 = !base.vinTail3.isEmpty() ? base.vinTail3 : VehicleBeacon.vinTailFromName(newName);
        String newTail2 = !base.effectiveTail2().isEmpty() ? base.effectiveTail2()
                : (sameEvidenceAddress ? evidence.vinTail2 : tail2(newTail3));
        String signature = sameEvidenceAddress && !evidence.mfgSignature.isEmpty()
                ? evidence.mfgSignature : base.mfgSignature;
        int company = sameEvidenceAddress && !evidence.mfgSignature.isEmpty()
                ? evidence.manufacturerId : base.manufacturerId;
        return new VehicleIdentity(address, newName, newTail2, newTail3, signature, company, System.currentTimeMillis());
    }

    /**
     * 调用方已通过系统 GATT 增量/唯一连接证明该地址可信时，不再用车型名称做白名单。
     * 名称只保存用于诊断；严格车名冲突和同MAC广播冲突仍必须拒绝。
     */
    static VehicleIdentity fromTrustedConnectedPeer(String address, String observedName,
                                                    VehicleBeacon evidence, VehicleIdentity known) {
        if (!VehicleLearningState.hasAddress(address)) return null;
        String peerName = clean(observedName);
        String peerTail = VehicleBeacon.vinTailFromName(peerName);
        boolean sameEvidenceAddress = evidence != null && address.equalsIgnoreCase(evidence.address);
        VehicleIdentity evidenceIdentity = sameEvidenceAddress ? from(evidence) : null;
        if (evidenceIdentity != null && evidenceIdentity.conflicts(peerName, "", peerTail)) return null;
        if (known != null) {
            if (known.conflicts(peerName, "", peerTail)) return null;
            if (sameEvidenceAddress && known.match(evidence) == Match.CONFLICT) return null;
        }
        VehicleIdentity base = known != null ? known
                : (evidenceIdentity != null ? evidenceIdentity
                : new VehicleIdentity(address, "", "", "", "", System.currentTimeMillis()));
        String newName = base.name;
        if (VehicleBeacon.isRecognizableVehicleName(peerName)) {
            newName = peerName;
        } else if (newName.isEmpty() && !peerName.isEmpty()) {
            newName = peerName;
        }
        if (newName.isEmpty() && evidenceIdentity != null) newName = evidenceIdentity.name;
        String newTail3 = !base.vinTail3.isEmpty() ? base.vinTail3 : VehicleBeacon.vinTailFromName(newName);
        String newTail2 = !base.effectiveTail2().isEmpty() ? base.effectiveTail2()
                : (evidenceIdentity != null ? evidenceIdentity.vinTail2 : tail2(newTail3));
        String signature = evidenceIdentity != null && !evidenceIdentity.mfgSignature.isEmpty()
                ? evidenceIdentity.mfgSignature : base.mfgSignature;
        int company = evidenceIdentity != null && !evidenceIdentity.mfgSignature.isEmpty()
                ? evidenceIdentity.manufacturerId : base.manufacturerId;
        return new VehicleIdentity(address, newName, newTail2, newTail3, signature, company,
                System.currentTimeMillis());
    }

    /**
     * 将新广播中补全的字段（如 ScanResponse 名称、01FE 尾号、最新 MAC）合并到本车身份中。
     * 不同MAC的名称/尾号/短前缀一律不合并，必须先通过fromConnectedPeer验证真实连接。
     */
    VehicleIdentity mergeWith(VehicleBeacon ad) {
        if (!matches(ad)) return this;
        String newAddress = !ad.address.isEmpty() ? ad.address : address;
        String newName = VehicleBeacon.isRecognizableVehicleName(ad.name) ? ad.name : name;
        String newTail3 = !ad.vinTail3.isEmpty() ? ad.vinTail3 : vinTail3;
        String newTail2 = !ad.vinTail2.isEmpty() ? ad.vinTail2 : effectiveTail2();
        if (newTail2.isEmpty()) newTail2 = tail2(newTail3);
        String newSignature = !ad.mfgSignature.isEmpty() ? ad.mfgSignature : mfgSignature;
        int newCompany = !ad.mfgSignature.isEmpty() ? ad.manufacturerId : manufacturerId;
        if (newAddress.equalsIgnoreCase(address) && newName.equalsIgnoreCase(name)
                && newTail2.equalsIgnoreCase(vinTail2) && newTail3.equalsIgnoreCase(vinTail3)
                && newSignature.equalsIgnoreCase(mfgSignature) && newCompany == manufacturerId) return this;
        return new VehicleIdentity(newAddress, newName, newTail2, newTail3, newSignature,
                newCompany, System.currentTimeMillis());
    }

    /** 只在连接地址已经验证后调用，扫描弱线索不得绕过mergeWith直接换地址。 */
    VehicleIdentity withAddress(String newAddress) {
        if (!VehicleLearningState.hasAddress(newAddress) || newAddress.equalsIgnoreCase(address)) return this;
        return new VehicleIdentity(newAddress, name, vinTail2, vinTail3, mfgSignature,
                manufacturerId, System.currentTimeMillis());
    }

    /** 返回完整厂商前缀是否可用于限制发现范围，截断的两字节vendor不是车辆线索。 */
    boolean hasUsableSignature() {
        return VehicleDiscoverySignature.isUsablePrefix(manufacturerPrefix());
    }

    /** 去掉旧15FF签名中附带的Company ID，Android厂商过滤的数据不包含这两个字节。 */
    byte[] manufacturerPrefix() {
        return VehicleDiscoverySignature.payloadPrefix(manufacturerId, mfgSignature);
    }

    /** 兼容旧版六字节01FE及八字节15FF签名，其他损坏值不猜测厂商ID。 */
    static int inferManufacturerId(String signature) {
        String value = clean(signature);
        if (value.length() == 16 && value.regionMatches(true, 0, "15FF", 0, 4)) return VehicleBeacon.COMPANY_ID_LEGACY;
        return value.length() == 12 ? VehicleBeacon.COMPANY_ID_GEELY : -1;
    }

    /** 无完整后三位时保留手填两位提示，提示本身不等于已验证地址。 */
    String effectiveTail2() {
        return !vinTail2.isEmpty() ? vinTail2 : tail2(vinTail3);
    }

    /** 显示已学习名称或不完整尾号。 */
    String displayName() {
        if (!name.isEmpty()) return name;
        if (!vinTail2.isEmpty()) return "GeelyVehicle??" + vinTail2;
        if (!address.isEmpty()) return address;
        return "未知车辆";
    }

    /** 供界面显示地址和尾号；日志使用加盐指纹而非原文。 */
    String displayDetail() {
        StringBuilder result = new StringBuilder(displayName());
        if (!address.isEmpty()) result.append(" · ").append(address);
        if (!vinTail3.isEmpty()) result.append(" · VIN尾号 ").append(vinTail3);
        return result.toString();
    }

    /** 身份指纹包含稳定公开字段和厂商ID，不把候选对象数量误认作车辆数量。 */
    String stableKey() {
        return address + "|" + name + "|" + vinTail2 + "|" + mfgSignature + "|" + manufacturerId;
    }

    /** 提取完整尾号的后两位。 */
    private static String tail2(String value) {
        return value != null && value.length() >= 2 ? value.substring(value.length() - 2) : "";
    }

    /** 将持久化和观测字段统一去掉首尾空白。 */
    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
