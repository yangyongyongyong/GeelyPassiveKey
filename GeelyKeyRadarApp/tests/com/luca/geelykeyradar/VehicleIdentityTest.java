package com.luca.geelykeyradar;

/** 多车身份回归：测试数据均为合成广播，不包含真实车辆凭证或完整VIN。 */
public final class VehicleIdentityTest {
    /** 验证解析、弱身份拒绝、连接证据及旧前缀迁移。 */
    public static void main(String[] args) {
        anonymousTailCannotReplaceAddress();
        stableDiscoveryMasksAllowDynamicFields();
        conflictBeforeAddress();
        nameAndAddressSupplement();
        connectedEvidenceKeepsManualHint();
        strictParserAndStructuredPayload();
        legacyPrefixAndHexValidation();
        System.out.println("VehicleIdentityTest passed");
    }

    /** 相同尾号和稳定厂商掩码也只到HINT，不能把发现线索伪装成全局唯一身份。 */
    private static void anonymousTailCannotReplaceAddress() {
        VehicleBeacon own = beacon("AA:00:00:00:00:01", "GeelyVehicle245", "060200143435", 1000);
        VehicleIdentity identity = VehicleIdentity.from(own);
        VehicleBeacon differentSignature = beacon("BB:00:00:00:00:02", "", "060300993435", 1100);
        check(!identity.matches(differentSignature), "匿名同尾号但签名不同不得命中本车");
        check(identity.mergeWith(differentSignature) == identity, "弱尾号不得覆盖MAC与签名");
        VehicleBeacon samePrefix = beacon("BB:00:00:00:00:02", "", "060200143435", 1200);
        check(identity.match(samePrefix) == VehicleIdentity.Match.HINT, "六字节相同也只能作为找车线索");
        check(!identity.matches(samePrefix), "同短前缀匿名邻车不得参与RSSI");
        check(identity.mergeWith(samePrefix) == identity, "匿名短前缀不能换本车地址");
    }

    /** 版本、devices或antenna变化仍可成为HINT，但不同尾号、Company或车名必须优先拒绝。 */
    private static void stableDiscoveryMasksAllowDynamicFields() {
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01",
                "GeelyVehicle245", "060200143435", 1000));
        VehicleBeacon versionAndDevices = beacon("BB:00:00:00:00:02", "", "060300993435", 1100);
        check(identity.match(versionAndDevices) == VehicleIdentity.Match.HINT,
                "version/devices变化由稳定四字节VIN掩码发现");
        VehicleBeacon antennaChanged = beacon("CC:00:00:00:00:03", "", "060201143435", 1200);
        check(identity.match(antennaChanged) == VehicleIdentity.Match.HINT,
                "antenna变化由两字节VIN兜底掩码发现");
        check(!identity.matches(versionAndDevices) && !identity.matches(antennaChanged),
                "稳定掩码HINT仍不得获得RSSI资格");
        check(identity.mergeWith(versionAndDevices) == identity && identity.mergeWith(antennaChanged) == identity,
                "稳定掩码HINT不得修改已学地址或身份");
        VehicleBeacon differentTail = beacon("DD:00:00:00:00:04", "", "060200143436", 1300);
        check(identity.match(differentTail) == VehicleIdentity.Match.CONFLICT && !identity.matches(differentTail),
                "不同尾号即使掩码形状相同也先拒绝");
        VehicleBeacon differentCompany = VehicleBeacon.parse("EE:00:00:00:00:05", "", -70, 1400,
                null, VehicleBeacon.hexToBytes("060200143435"), null, false);
        check(identity.match(differentCompany) == VehicleIdentity.Match.NONE,
                "不同Company ID不能借用稳定掩码");
        VehicleBeacon differentName = beacon("FF:00:00:00:00:06", "GeelyVehicle345", "060200143435", 1500);
        check(identity.match(differentName) == VehicleIdentity.Match.CONFLICT,
                "同尾号但完整车名冲突优先于稳定掩码");
    }

    /** 同MAC也必须先检查明确车名及尾号冲突，不能复现地址短路后的身份污染。 */
    private static void conflictBeforeAddress() {
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01", "GeelyVehicle245", "060200143435", 1000));
        VehicleBeacon conflictName = beacon(identity.address, "GeelyVehicle345", "060200143435", 1100);
        check(identity.match(conflictName) == VehicleIdentity.Match.CONFLICT, "明确不同车名先于MAC命中");
        check(identity.mergeWith(conflictName) == identity, "冲突车名不得合并");
        VehicleBeacon conflictTail = beacon(identity.address, "", "060200143436", 1200);
        check(identity.match(conflictTail) == VehicleIdentity.Match.CONFLICT, "同MAC但明确尾号冲突也拒绝");
    }

    /** 同地址匿名帧正常工作，完整名称提供MAC轮换路径，随状态变化的签名不会抹掉可信名称。 */
    private static void nameAndAddressSupplement() {
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01", "GeelyVehicle245", "060200143435", 1000));
        check(identity.matches(beacon(identity.address, "", "060200143435", 1100)), "本车同地址匿名广播正常供样");
        VehicleBeacon rotation = beacon("CC:00:00:00:00:03", identity.name, "060300993435", 1200);
        check(identity.match(rotation) == VehicleIdentity.Match.NAME, "完整名称保留地址轮换兼容");
        check(!identity.matches(rotation) && identity.mergeWith(rotation) == identity, "同名跨MAC仍不直接测距或更新身份");
        VehicleIdentity verified = VehicleIdentity.fromConnectedPeer(rotation.address, rotation.name, rotation, identity);
        check(verified != null && verified.address.equals(rotation.address), "实际连接与名称交叉验证后才更新MAC");
        VehicleIdentity anonymous = VehicleIdentity.from(beacon(identity.address, "", "060200143435", 1000));
        check(anonymous.mergeWith(beacon(identity.address, identity.name, "060200143435", 1300)).name.equals(identity.name),
                "同地址ScanResponse能够补全名称");
        VehicleBeacon arbitraryName = beacon(identity.address, "NearbyHeadUnit", "060200143435", 1400);
        check(identity.mergeWith(arbitraryName).name.equals(identity.name), "普通名称不能抹掉已学习的吉利名称");
    }

    /** 匿名弱线索只有在对应实际连接地址时才允许采用，并且不能继承另一车的提示或证据。 */
    private static void connectedEvidenceKeepsManualHint() {
        VehicleIdentity manual = new VehicleIdentity("", "GeelyVehicle245", "45", "245", "", 1000);
        VehicleBeacon anonymous = beacon("AA:00:00:00:00:01", "", "060200143435", 1100);
        check(manual.match(anonymous) == VehicleIdentity.Match.HINT && !manual.matches(anonymous), "手填尾号不自动变已学习");
        VehicleIdentity connected = VehicleIdentity.fromConnectedPeer(anonymous.address, "", anonymous, manual);
        check(connected != null && connected.name.equals(manual.name) && connected.vinTail3.equals("245"),
                "实际同MAC连接补全地址且保留用户手填后三位");
        check(VehicleIdentity.fromConnectedPeer("BB:00:00:00:00:02", "", anonymous, null) == null,
                "其他MAC不能借用当前候选广播学习");
        check(VehicleIdentity.fromConnectedPeer(anonymous.address, "GeelyVehicle345", anonymous, manual) == null,
                "连接设备名与手填提示冲突时拒绝");
        check(VehicleIdentity.fromConnectedPeer(anonymous.address, "", null, null) == null, "无名无广播的匿名连接不猜测");
        check(VehicleIdentity.fromConnectedPeer(anonymous.address, "GeelyVehicle345",
                beacon(anonymous.address, "GeelyVehicle245", "060200143435", 1200), null) == null,
                "首次学习也必须拒绝连接名与同MAC广播名互相冲突");
    }

    /** 裸FDFD、相似名字、无vendor的厂商字段均拒绝；名称/VIN矛盾的广播不成为候选。 */
    private static void strictParserAndStructuredPayload() {
        check(VehicleBeacon.parse("AA:00:00:00:00:01", "NotAGeelyCar", -60, 1000, null, null, null, true) == null,
                "FDFD加任意非空名字不能当吉利车");
        check(!VehicleBeacon.isGeelyVehicleName("GeelyVehicle") && !VehicleBeacon.isGeelyVehicleName("GeelyVehicle245Speaker"),
                "公共前缀及相似名字拒绝");
        check(VehicleBeacon.vinTailFromName("GeelyVehicleA45").equals("A45"), "字母尾号不被丢弃");
        VehicleBeacon named = VehicleBeacon.parse("AA:00:00:00:00:01", "GeelyVehicle245", -60, 1000, null, null, null, true);
        check(named != null && named.sourceTag.equals("FDFD+NAME"), "合法名字加FDFD正常识别");
        check(VehicleBeacon.parse("AA:00:00:00:00:01", "", -60, 1000, new byte[]{1, 2}, null, null, false) == null,
                "01FE厂商ID本身不构成车辆证据");
        check(VehicleBeacon.parse("AA:00:00:00:00:01", "GeelyVehicle245", -60, 1000,
                VehicleBeacon.hexToBytes("060200143436"), null, null, false) == null, "名字与载荷尾号矛盾不进入RSSI");
        check(VehicleBeacon.parse("", "GeelyVehicle245", -60, 1000, null, null, null, false) == null, "无MAC不生成物理候选");
    }

    /** 兼容15FF签名中的Company ID并严格处理损坏十六进制，不再搜索任意raw字节子串。 */
    private static void legacyPrefixAndHexValidation() {
        VehicleBeacon legacy = VehicleBeacon.parse("AA:00:00:00:00:01", "", -60, 1000,
                null, null, VehicleBeacon.hexToBytes("0602001434359988"), false);
        check(legacy != null && legacy.manufacturerId == VehicleBeacon.COMPANY_ID_LEGACY, "结构化15FF字段解析");
        check(legacy.mfgSignature.equals("15FF060200143435"), "保留旧15FF前缀布局");
        VehicleIdentity old = new VehicleIdentity("AA:00:00:00:00:01", "", "45", "", "15FF060200143435", 1000);
        check(VehicleBeacon.bytesToHex(old.manufacturerPrefix()).equals("060200143435"), "硬件数据前缀不包含company字节");
        check(VehicleBeacon.hexToBytes("123").length == 0 && VehicleBeacon.hexToBytes("GG").length == 0,
                "奇数长度与非法字符不能生成过滤");
        check(VehicleBeacon.bytesToHex(new byte[]{0, 15, (byte) 255}).equals("000FFF"), "低开销编码结果一致");
    }

    /** 生成只含短公开字段的合成01FE广播。 */
    private static VehicleBeacon beacon(String address, String name, String prefix, long now) {
        return VehicleBeacon.parse(address, name, -70, now, VehicleBeacon.hexToBytes(prefix), null, null, false);
    }

    /** 断言失败保留对应多车安全语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
