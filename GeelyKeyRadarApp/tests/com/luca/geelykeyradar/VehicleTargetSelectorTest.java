package com.luca.geelykeyradar;

/** 多车候选和RSSI隔离回归，覆盖已学习、匿名发现及首次学习预算路径。 */
public final class VehicleTargetSelectorTest {
    /** 执行物理候选去重、轮换确认、固定预算、冷却与完整信号流回放。 */
    public static void main(String[] args) {
        sameMacDoesNotCreateMultipleCars();
        learnedAddressWinsAndOfficialConnectionPins();
        namedRotationNeedsConfirmation();
        firstPacketDoesNotBindAndBudgetDoesNotRenew();
        anonymousHintNeverFeedsRssi();
        stableMaskHintNeverFeedsRssi();
        boundedCandidatesAndIdlePrune();
        targetChangeClearsDistanceNotControlLatch();
        unknownNearCandidateDoesNotArmControls();
        subMillisecondResponseAndCooldownPersist();
        System.out.println("VehicleTargetSelectorTest passed");
    }

    /** 名称补全与签名变化不改变物理候选数，同时间戳回调不累计样本。 */
    private static void sameMacDoesNotCreateMultipleCars() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleBeacon anonymous = beacon("AA:00:00:00:00:01", "", -80, 1000);
        check(selector.observe(anonymous, 1000), "第一条广播记录候选");
        check(!selector.observe(anonymous, 1100), "重复时间戳不构造第二个新样本");
        selector.observe(beacon(anonymous.address, "GeelyVehicle245", -70, 1300), 1300);
        check(selector.size(1300) == 1 && selector.evidenceFor(anonymous.address, 1300).name.equals("GeelyVehicle245"),
                "ScanResponse名称不制造多个候选");
        selector.observe(beacon(anonymous.address, "", -72, 1600), 1600);
        check(selector.evidenceFor(anonymous.address, 1600).name.equals("GeelyVehicle245"), "后续无名帧保留近期命名证据");
    }

    /** 已知MAC立即可用；旧目标仍新鲜或官方在线时，不抢到另一同名/同尾号地址。 */
    private static void learnedAddressWinsAndOfficialConnectionPins() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleBeacon own = beacon("AA:00:00:00:00:01", "GeelyVehicle245", -85, 1000);
        VehicleIdentity identity = VehicleIdentity.from(own);
        selector.observe(own, 1000);
        check(selector.select(identity, false, 1000).address.equals(own.address), "已学习MAC第一包立即唤醒，不增加观察延迟");
        for (long now = 1200; now <= 3000; now += 600) {
            selector.observe(beacon("BB:00:00:00:00:02", own.name, -50, now), now);
            selector.observe(beacon(own.address, own.name, -85, now), now);
            check(selector.select(identity, false, now).address.equals(own.address), "同名更强车不能抢新鲜旧MAC");
        }
        selector.observe(beacon("BB:00:00:00:00:02", own.name, -50, 7000), 7000);
        check(selector.select(identity, true, 7000) == null && selector.selectedAddress().equals(own.address),
                "官方在线且本车停止广播时保持真实连接目标");
    }

    /** 完整同名新地址必须多个样本跨600ms确认，同时等旧地址停更；冲突车名被记忆拒绝。 */
    private static void namedRotationNeedsConfirmation() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleBeacon old = beacon("AA:00:00:00:00:01", "GeelyVehicle245", -70, 1000);
        VehicleIdentity identity = VehicleIdentity.from(old);
        selector.observe(old, 1000);
        selector.select(identity, false, 1000);
        VehicleBeacon rotated = beacon("CC:00:00:00:00:03", old.name, -65, 4500);
        selector.observe(rotated, 4500);
        check(selector.select(identity, false, 4500) == null, "新名称首包不覆盖旧MAC");
        selector.observe(beacon(rotated.address, old.name, -65, 5100), 5100);
        check(selector.select(identity, false, 5100).address.equals(rotated.address), "旧地址停更后选择新名称轮换候选");
        check(!selector.authorizesRssi(identity, rotated) && identity.mergeWith(rotated) == identity,
                "同名轮换候选只能唤醒官方，不能直接覆盖MAC或测距");
        VehicleTargetSelector<VehicleBeacon> conflict = new VehicleTargetSelector<>();
        conflict.observe(beacon(old.address, "GeelyVehicle345", -50, 2000), 2000);
        conflict.observe(beacon(old.address, "", -50, 2300), 2300);
        check(conflict.select(identity, false, 2300) == null, "已见冲突车名不能用随后的匿名帧洗掉");
    }

    /** 未学习先观察1.5秒；选中后12秒硬预算，持续广播不续期，失败地址进入30秒冷却。 */
    private static void firstPacketDoesNotBindAndBudgetDoesNotRenew() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        String other = "BB:00:00:00:00:02", own = "AA:00:00:00:00:01";
        selector.observe(beacon(other, "GeelyVehicle345", -60, 1000), 1000);
        check(selector.select(null, false, 1000) == null, "第一包不能自动绑定或立即认定唯一车辆");
        selector.observe(beacon(other, "GeelyVehicle345", -60, 1600), 1600);
        check(selector.select(null, false, 1600) == null, "观察预算未结束");
        selector.observe(beacon(other, "GeelyVehicle345", -60, 2500), 2500);
        check(selector.select(null, false, 2500).address.equals(other), "观察后跟随一个主候选");
        for (long now = 3000; now < 14500; now += 500) {
            selector.observe(beacon(other, "GeelyVehicle345", -89, now), now);
            selector.observe(beacon(own, "GeelyVehicle245", -55, now), now);
            check(selector.select(null, false, now).address.equals(other), "尝试预算内不混流");
        }
        selector.observe(beacon(other, "GeelyVehicle345", -89, 14500), 14500);
        selector.observe(beacon(own, "GeelyVehicle245", -55, 14500), 14500);
        check(selector.select(null, false, 14500).address.equals(own), "持续广播的错目标12秒后也必须切换");
        check(selector.selectionAgeMs(14500) == 0, "新目标预算从真实选择时刻开始");
    }

    /** 匿名同短前缀即使能发现也不得进入滤波；已学习单车在邻车弱广播中仍保持响应。 */
    private static void anonymousHintNeverFeedsRssi() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01", "GeelyVehicle245", -88, 1000));
        RssiFilter filter = new RssiFilter();
        long crossing = -1L;
        for (long now = 1000; now <= 13000; now += 500) {
            VehicleBeacon own = beacon(identity.address, identity.name, now < 2500 ? -88 : -70, now);
            selector.observe(own, now);
            VehicleBeacon selected = selector.select(identity, false, now);
            if (selected != null && selector.authorizesRssi(identity, own)) {
                float smooth = filter.update(own.rssi, own.seenAtMs);
                if (crossing < 0L && smooth >= -75) crossing = now;
            }
            for (long offset : new long[]{160, 320}) {
                VehicleBeacon anonymous = beacon("BB:00:00:00:00:02", "", -86, now + offset);
                selector.observe(anonymous, now + offset);
                selector.select(identity, false, now + offset);
                check(!selector.isSelected(anonymous), "已学习本车新鲜时匿名邻车不成为主目标");
                check(!identity.matches(anonymous), "匿名邻车不会送入RSSI");
            }
        }
        check(crossing == 3000, "本车-70信号出现500ms后达到原-75阈值，不被两条弱邻车广播拖住");
        VehicleTargetSelector<VehicleBeacon> hint = new VehicleTargetSelector<>();
        hint.observe(beacon("BB:00:00:00:00:02", "", -60, 20000), 20000);
        hint.observe(beacon("BB:00:00:00:00:02", "", -60, 21500), 21500);
        VehicleBeacon selected = hint.select(identity, false, 21500);
        check(selected != null && !hint.authorizesRssi(identity, selected), "旧MAC失效后允许匿名窄线索尝试官方，不改身份也不测距");
    }

    /** 官方稳定掩码发现的新MAC只进入候选，跨antenna/version变化也不能供给RSSI或直接绑定。 */
    private static void stableMaskHintNeverFeedsRssi() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleIdentity identity = VehicleIdentity.from(beacon("AA:00:00:00:00:01",
                "GeelyVehicle245", -88, 1000));
        String rotated = "BB:00:00:00:00:02";
        selector.observe(beacon(rotated, "", -62, 20000, "060300993435"), 20000);
        selector.observe(beacon(rotated, "", -60, 21500, "060201143435"), 21500);
        VehicleBeacon selected = selector.select(identity, false, 21500);
        check(selected != null && identity.match(selected) == VehicleIdentity.Match.HINT,
                "version/devices与antenna变化均可通过稳定掩码进入发现候选");
        check(!selector.authorizesRssi(identity, selected), "稳定掩码匿名HINT不得进入RSSI滤波");
        check(identity.mergeWith(selected) == identity && identity.address.equals("AA:00:00:00:00:01"),
                "稳定掩码匿名HINT不得覆盖已学地址");
    }

    /** 停车场设备上限32个，长时间无新广播时计数归零，快照不主动修改集合。 */
    private static void boundedCandidatesAndIdlePrune() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        for (int index = 0; index < 80; index++) {
            String address = String.format(java.util.Locale.ROOT, "AA:00:00:00:00:%02X", index);
            selector.observe(beacon(address, "GeelyVehicle245", -60, 1000 + index), 1000 + index);
        }
        check(selector.size(1080) == 32, "物理候选上限固定32，不随广播量无限增长");
        check(selector.size(22000) == 0, "只读快照不把旧候选算成附近车辆");
        selector.prune(22000);
        selector.observe(beacon("AA:00:00:00:00:01", "GeelyVehicle245", -60, 23000), 23000);
        check(selector.select(null, false, 23000) == null, "空闲后新会话重新观察，不继承旧单车预算");
    }

    /** 换物理测距目标清理旧驻留与滤波证据，已交付或手动锁存继续阻止重复反向动作。 */
    private static void targetChangeClearsDistanceNotControlLatch() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60, 1000, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK, "建立已解锁会话");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1000);
        logic.onMeasurementTargetChanged(2000);
        check(logic.getLastSampleAtMs() == 0L && logic.getLockDwellStartMs() == 0L, "切换清旧距离与驻留");
        check(logic.onSample(-60, 2500, true, false, true, true) == PassiveKeyLogic.Action.NONE, "换地址不重复解锁");
        PassiveKeyLogic manual = new PassiveKeyLogic();
        manual.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1000);
        manual.onMeasurementTargetChanged(2000);
        check(manual.onSample(-60, 2500, true, false, true, true) == PassiveKeyLogic.Action.NONE, "手动上锁锁存不能被目标切换撤销");
        check(manual.getLastSampleDecision().equals("MANUAL_LOCK_LATCHED"), "手动锁原因仍有效");
    }

    /** 未学习主候选只唤醒官方，不参与状态机，强邻车也不能制造解锁或离开上锁资格。 */
    private static void unknownNearCandidateDoesNotArmControls() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        PassiveKeyLogic logic = new PassiveKeyLogic();
        selector.observe(beacon("BB:00:00:00:00:02", "GeelyVehicle345", -50, 1000), 1000);
        selector.observe(beacon("BB:00:00:00:00:02", "GeelyVehicle345", -50, 2500), 2500);
        VehicleBeacon selected = selector.select(null, false, 2500);
        VehicleIdentity identity = null;
        if (selector.authorizesRssi(identity, selected)) {
            logic.onSample(selected.rssi, selected.seenAtMs, false, false, true, true);
        }
        check(selected != null && logic.getLastSampleAtMs() == 0L && !logic.hasBeenNear(),
                "未知近场主候选不计RSSI、不武装上锁资格");
    }

    /** 同毫秒不同纳秒结果可以补名，失败候选消失后仍保持三十秒冷却。 */
    private static void subMillisecondResponseAndCooldownPersist() {
        VehicleTargetSelector<VehicleBeacon> selector = new VehicleTargetSelector<>();
        VehicleBeacon first = beacon("AA:00:00:00:00:01", "", -60, 1000);
        selector.observe(first, 1000);
        VehicleBeacon named = new VehicleBeacon(first.address, "GeelyVehicle245", -60, 1000,
                first.seenAtNanos + 500000L, "45", "245", first.mfgSignature, first.manufacturerId, first.sourceTag);
        check(selector.observe(named, 1001), "同毫秒不同纳秒的ScanResponse应补全名称");
        check(selector.evidenceFor(first.address, 1001).name.equals(named.name), "纳秒去重不能丢名称");
        VehicleTargetSelector<VehicleBeacon> failed = new VehicleTargetSelector<>();
        failed.observe(beacon(first.address, named.name, -60, 1000), 1000);
        failed.observe(beacon(first.address, named.name, -60, 2500), 2500);
        check(failed.select(null, false, 2500) != null, "观察后选中未知主候选");
        failed.observe(beacon(first.address, named.name, -60, 14500), 14500);
        check(failed.select(null, false, 14500) == null, "十二秒预算到期并进入三十秒冷却");
        failed.prune(35500);
        failed.observe(beacon(first.address, named.name, -60, 35500), 35500);
        failed.observe(beacon(first.address, named.name, -60, 37500), 37500);
        check(failed.select(null, false, 37500) == null, "消失20秒后回来仍不能重置未结束的失败冷却");
        failed.observe(beacon(first.address, named.name, -60, 44500), 44500);
        check(failed.select(null, false, 44500) != null, "冷却期结束后允许再次尝试");
    }

    /** 合成厂商前缀与尾号一致的车辆，名称245与345共享后两位用于碰撞测试。 */
    private static VehicleBeacon beacon(String address, String name, int rssi, long now) {
        return beacon(address, name, rssi, now, "060200143435");
    }

    /** 构造可指定厂商前缀的合成广播，名称仍必须与VIN后两位一致。 */
    private static VehicleBeacon beacon(String address, String name, int rssi, long now, String prefix) {
        return VehicleBeacon.parse(address, name, rssi, now, VehicleBeacon.hexToBytes(prefix), null, null, false);
    }

    /** 断言失败时输出具体候选隔离规则。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
