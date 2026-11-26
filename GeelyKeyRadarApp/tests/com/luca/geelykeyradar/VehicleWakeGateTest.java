package com.luca.geelykeyradar;

/** 提前连接回归：合成广播只验证时序门控，不冒充真实缺失的扫描包或实车距离。 */
public final class VehicleWakeGateTest {
    private static final String OLD = "AA:00:00:00:00:01";
    private static final String NEW = "CC:00:00:00:00:03";
    private static final String OTHER = "BB:00:00:00:00:02";
    private static final String NAME = "GeelyVehicle245";
    private static final String PREFIX = "060200143435";
    private static final String CHANGED_PREFIX = "060300993435";

    /** 覆盖首包、稀疏名称、厂商状态、单目标预算、ACL保护及上锁输入隔离。 */
    public static void main(String[] args) {
        firstNamedPacketStartsConnectionNotMeasurement();
        sparseNamesNoLongerDelayFirstAttempt();
        anonymousAndCachedNamesKeepOriginalObservation();
        strongStableHintStartsEarlyWithoutRssi();
        conflictingOrIncompleteCluesCannotUseFastPath();
        staleDuplicateAndWeakPacketsCannotStartEarly();
        longAbsenceWeakPacketOnlyConnectsAndIsSingleUse();
        learnedAddressKeepsPriorityAndImmediateWake();
        verifiedMacPreemptsUnconnectedNameAndActiveEvidenceSurvivesCrowd();
        selectedConfirmationPreservesThrottleAndAcl();
        aclBeforeFirstAttemptIsAddressScoped();
        oneUnknownAttemptBudgetAndCooldownAcrossBothPaths();
        aclGraceSurvivesBudgetExpiry();
        earlyOfficialReadyWaitsForVerifiedPhysicalPeer();
        disabledOfficialReadyAndResetDoNotLeakAttempts();
        candidateDiagnosticsDescribeRealNameEvidence();
        configuredConnectionThresholdOnlyGatesWakeup();
        earlyNeighborDoesNotChangeLockPipeline();
        System.out.println("VehicleWakeGateTest passed");
    }

    /** 同名新MAC第一包-78可预连接，已保存身份、主目标和RSSI仍不变。 */
    private static void firstNamedPacketStartsConnectionNotMeasurement() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon ad = named(NEW, -78, 1000, CHANGED_PREFIX);
        targets.observe(ad, 1013);
        check(targets.select(known, false, 1013) == null, "普通轮换首包仍未确认");
        check(gate.claim(ad, known, true, false, true, 1013) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "真实首包完整名称加厂商/VIN线索，13ms扫描年龄即允许提前连接");
        check(gate.attempts() == 1 && gate.lastWakeAdvertisement() == ad, "只交付本条真实广播");
        check(known.address.equals(OLD) && known.mergeWith(ad) == known, "首包不覆写学习地址");
        check(!targets.authorizesRssi(known, ad) && targets.selectedAddress().isEmpty(), "预连接不选测距目标");
        PassiveKeyLogic logic = new PassiveKeyLogic();
        if (targets.authorizesRssi(known, ad)) logic.onSample(ad.rssi, ad.seenAtMs, false, false, true, true);
        check(logic.getLastSampleAtMs() == 0L && !logic.hasBeenNear(), "未鉴权首包不产生解锁或上锁资格");
        VehicleIdentity verified = VehicleIdentity.fromConnectedPeer(ad.address, ad.name, ad, known);
        targets.pinConnected(verified.address, 1800);
        check(targets.authorizesRssi(verified, ad), "官方就绪且唯一实际连接验证后才接受新地址RSSI");
    }

    /** 用合成缺名/补名解释四秒观察等待，不能将补包当作今早日志真实逐包还原。 */
    private static void sparseNamesNoLongerDelayFirstAttempt() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        long base = 548019520L;
        VehicleBeacon first = named(NEW, -78, base, CHANGED_PREFIX);
        targets.observe(first, base + 13);
        check(targets.select(known, false, base + 13) == null, "旧路径首包被确认门槛挡住");
        check(gate.claim(first, known, true, false, true, base + 13) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "新路径在首包同一回调中发起，不增加600ms或1.5s等待");
        VehicleBeacon anonymous = beacon(NEW, "", -76, base + 400, CHANGED_PREFIX);
        targets.observe(anonymous, base + 400);
        check(targets.select(known, false, base + 400) == null, "匿名弱帧不补足完整名确认");
        gate.onAclConnected(NEW, base + 791);
        VehicleBeacon laterName = named(NEW, -74, base + 3400, CHANGED_PREFIX);
        targets.observe(laterName, base + 3400);
        check(targets.select(known, false, base + 3400) == null, "大于三秒命名断档重新观察");
        VehicleBeacon confirmation = named(NEW, -72, base + 4026, CHANGED_PREFIX);
        targets.observe(confirmation, base + 4026);
        check(targets.select(known, false, base + 4026) != null, "合成第二段名称跨626ms才通过普通确认");
        check(gate.claim(confirmation, known, true, false, true, base + 4026) == VehicleWakeGate.Result.WAIT_ACL_AUTH,
                "确认目标不清掉早先同MAC的真实ACL握手记录");
        check(gate.attempts() == 1 && targets.selectionAgeMs(base + 4026) == 4013,
                "不重复注入，十二秒预算仍从首个实际预连接起算");
        System.out.println("synthetic_sparse_names normal_selection=4026ms early_request=13ms attempts=1");
    }

    /** 匿名同尾号、缓存BluetoothDevice名字以及未学习车辆都不能跳过原发现观察。 */
    private static void anonymousAndCachedNamesKeepOriginalObservation() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon anonymous = beacon(NEW, "", -78, 1000, CHANGED_PREFIX);
        targets.observe(anonymous, 1000);
        check(gate.claim(anonymous, known(), true, false, false, 1000) == VehicleWakeGate.Result.NOT_SELECTED,
                "稳定掩码匿名HINT首包仍不提前连接");
        VehicleTargetSelector<VehicleBeacon> observation = new VehicleTargetSelector<>();
        observation.observe(anonymous, 1000);
        check(observation.select(known(), false, 2000) == null, "匿名HINT观察不足1.5秒不得预连接");
        VehicleBeacon cached = named(NEW, -78, 1300, PREFIX);
        targets.observe(cached, 1300);
        check(gate.claim(cached, known(), true, false, false, 1300) == VehicleWakeGate.Result.NOT_SELECTED,
                "系统缓存名字不能冒充ScanRecord本包完整名称");
        VehicleBeacon anonLater = beacon(NEW, "", -78, 2500, "060201143435");
        targets.observe(anonLater, 2500);
        check(known().match(anonLater) == VehicleIdentity.Match.HINT,
                "antenna变化仍由稳定兜底掩码发现");
        check(targets.select(known(), false, 2500) != null, "匿名HINT保留两条跨度1.5秒普通路径");
        check(gate.claim(anonLater, known(), true, false, false, 2500) == VehicleWakeGate.Result.SEND_SELECTED,
                "确认后仍允许普通单目标官方鉴权");
        check(!targets.authorizesRssi(known(), anonLater), "稳定掩码匿名HINT确认后仍不得供给RSSI");
        VehicleTargetSelector<VehicleBeacon> learning = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> learningGate = new VehicleWakeGate<>(learning);
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        learning.observe(first, 1000);
        check(learningGate.claim(first, null, true, false, true, 1000) == VehicleWakeGate.Result.NOT_SELECTED,
                "首次学习不因新优化凭首包绑定或接管任意吉利");
        VehicleIdentity manual = new VehicleIdentity("", NAME, "45", "245", "", 1000);
        check(learningGate.claim(first, manual, true, false, true, 1000) == VehicleWakeGate.Result.NOT_SELECTED,
                "手填完整尾号不是已学地址和厂商证据");
    }

    /** 车多丢 SCAN_RSP 时，-76 首包或同MAC连续三包 -83 稳定掩码被动帧可提前唤醒官方，仍不得供RSSI。 */
    private static void strongStableHintStartsEarlyWithoutRssi() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon hint = beacon(NEW, "", -76, 1000, CHANGED_PREFIX);
        targets.observe(hint, 1000);
        check(gate.claim(hint, known, true, false, false, 1000)
                        == VehicleWakeGate.Result.SEND_EARLY_HINT,
                "-76稳定尾号匿名首包允许提前交给官方");
        check(!targets.authorizesRssi(known, hint), "匿名提前唤醒仍不得供给RSSI");
        check(known.mergeWith(hint) == known, "匿名提前唤醒不得覆盖地址");

        VehicleTargetSelector<VehicleBeacon> multiTargets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> multiGate = new VehicleWakeGate<>(multiTargets);
        VehicleBeacon p1 = beacon(NEW, "", -82, 1000, CHANGED_PREFIX);
        VehicleBeacon p2 = beacon(NEW, "", -81, 1300, CHANGED_PREFIX);
        VehicleBeacon p3 = beacon(NEW, "", -80, 1600, CHANGED_PREFIX);
        multiTargets.observe(p1, 1000);
        check(multiGate.claim(p1, known, true, false, false, 1000) == VehicleWakeGate.Result.NOT_SELECTED,
                "远距离单包匿名HINT不应盲目唤醒");
        multiTargets.observe(p2, 1300);
        check(multiGate.claim(p2, known, true, false, false, 1300) == VehicleWakeGate.Result.NOT_SELECTED,
                "两包匿名HINT在-81dBm仍需等待第三次确认");
        multiTargets.observe(p3, 1600);
        check(multiGate.claim(p3, known, true, false, false, 1600) == VehicleWakeGate.Result.SEND_EARLY_HINT,
                "车多丢失SCAN_RSP时，连续三包同MAC稳定01FE在-80dBm可提前唤醒官方");
        check(!multiTargets.authorizesRssi(known, p3), "三包稳定匿名唤醒仍不得供给RSSI");
    }

    /** 同尾不同全名、厂商ID不一致、短前缀或无厂商数据均不能走快速路径。 */
    private static void conflictingOrIncompleteCluesCannotUseFastPath() {
        check(firstResult(beacon(NEW, "GeelyVehicle345", -78, 1000, PREFIX), known())
                == VehicleWakeGate.Result.TARGET_REJECTED, "不同完整名称先拒绝");
        VehicleBeacon differentCompany = VehicleBeacon.parse(NEW, NAME, -78, 1000,
                null, VehicleBeacon.hexToBytes(PREFIX), null, false);
        check(firstResult(differentCompany, known()) == VehicleWakeGate.Result.NOT_SELECTED, "不同Company ID不得提前连接");
        check(firstResult(beacon(NEW, NAME, -78, 1000, "0602"), known())
                == VehicleWakeGate.Result.NOT_SELECTED, "两字节vendor不足以认定厂商加VIN线索");
        check(firstResult(VehicleBeacon.parse(NEW, NAME, -78, 1000, null, null, null, true), known())
                == VehicleWakeGate.Result.NOT_SELECTED, "只有完整名/FDFD没有厂商证据仍先观察");
        VehicleIdentity anonymousLearned = VehicleIdentity.from(beacon(OLD, "", -80, 100, PREFIX));
        check(firstResult(named(NEW, -78, 1000, PREFIX), anonymousLearned)
                == VehicleWakeGate.Result.NOT_SELECTED, "旧身份缺完整名不把新完整名当首包本车");
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon conflict = beacon(NEW, "GeelyVehicle345", -60, 1000, PREFIX);
        targets.observe(conflict, 1000);
        VehicleBeacon anon = beacon(NEW, "", -60, 1300, PREFIX);
        targets.observe(anon, 1300);
        check(gate.claim(anon, known(), true, false, false, 1300) == VehicleWakeGate.Result.TARGET_REJECTED,
                "已记录冲突名称不能被匿名帧洗掉后再次唤醒");
    }

    /** 快速路径的年龄与采集时间去重不放宽，-95边界与原预连接门槛一致。 */
    private static void staleDuplicateAndWeakPacketsCannotStartEarly() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1000);
        check(gate.claim(first, known(), true, false, true, 4001) == VehicleWakeGate.Result.AD_STALE, "三秒以上广告不重播");
        check(firstResult(named(NEW, -96, 1000, PREFIX), known()) == VehicleWakeGate.Result.WEAK_SIGNAL,
                "低于-95不因完整名而强行预连接");
        check(firstResult(named(NEW, -95, 1000, PREFIX), known()) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "-95边界可用，不把解锁阈值误加给预连接");
        VehicleTargetSelector<VehicleBeacon> duplicate = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> duplicateGate = new VehicleWakeGate<>(duplicate);
        duplicate.observe(first, 1000);
        check(duplicateGate.claim(first, known(), true, false, true, 1000) == VehicleWakeGate.Result.SEND_EARLY_NAME, "首次注入");
        check(!duplicate.observe(first, 2500), "上游拒绝重复系统时间戳");
        check(duplicateGate.claim(first, known(), true, false, true, 2500) == VehicleWakeGate.Result.DUPLICATE_EARLY_AD,
                "早期候选不能利用重复包在节流后重新注入");
        check(duplicateGate.attempts() == 1, "重复扫描包不增加实际尝试计数");
        check(!duplicate.observe(named(OTHER, -70, 6000, PREFIX), 5900), "未来采集时间不能成为候选");
    }

    /** 长时无车首个强身份弱包只提前连接，不产生RSSI；无长时无车条件或已用宽限仍退回-95。 */
    private static void longAbsenceWeakPacketOnlyConnectsAndIsSingleUse() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> normal = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> normalGate = new VehicleWakeGate<>(normal);
        VehicleBeacon normalWeak = beacon(OLD, NAME, -100, 1000, PREFIX);
        normal.observe(normalWeak, 1000);
        normal.select(known, false, 1000);
        check(normalGate.claim(normalWeak, known, true, false, true, 1000, true, 0L)
                        == VehicleWakeGate.Result.WEAK_SIGNAL,
                "没有五分钟无车会话时弱包不能放宽");

        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        long absence = 1000L;
        long firstAt = absence + VehicleWakeGate.LONG_ABSENCE_MS + 100L;
        VehicleBeacon weak = beacon(OLD, NAME, -100, firstAt, PREFIX);
        targets.observe(weak, firstAt);
        targets.select(known, false, firstAt);
        check(gate.claim(weak, known, true, false, true, firstAt, true, absence)
                        == VehicleWakeGate.Result.SEND_SELECTED,
                "长时无车后的已学强身份弱包允许提前连接");
        check(!targets.authorizesRssi(known, weak) || targets.isSelected(weak),
                "弱包连接不改变RSSI授权边界");

        VehicleTargetSelector<VehicleBeacon> secondTargets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> secondGate = new VehicleWakeGate<>(secondTargets);
        long secondAbsence = 100_000L;
        long before = secondAbsence + VehicleWakeGate.LONG_ABSENCE_MS - 1L;
        VehicleBeacon tooEarly = beacon(OLD, NAME, -100, before, PREFIX);
        secondTargets.observe(tooEarly, before);
        secondTargets.select(known, false, before);
        check(secondGate.claim(tooEarly, known, true, false, true, before, true, secondAbsence)
                        == VehicleWakeGate.Result.WEAK_SIGNAL,
                "无车时间不足五分钟仍保持-95门槛");

        long after = secondAbsence + VehicleWakeGate.LONG_ABSENCE_MS + 1L;
        VehicleBeacon firstWeak = beacon(OLD, NAME, -100, after, PREFIX);
        secondTargets.observe(firstWeak, after);
        secondTargets.select(known, false, after);
        check(secondGate.claim(firstWeak, known, true, false, true, after, true, secondAbsence)
                        == VehicleWakeGate.Result.SEND_SELECTED,
                "长时无车第一次允许弱包连接");
        long again = after + 2000L;
        VehicleBeacon secondWeak = beacon(OLD, NAME, -100, again, PREFIX);
        secondTargets.observe(secondWeak, again);
        secondTargets.select(known, false, again);
        check(secondGate.claim(secondWeak, known, true, false, true, again, true, secondAbsence)
                        == VehicleWakeGate.Result.THROTTLED || secondGate.claim(secondWeak, known, true, false, true, again, true, secondAbsence)
                        == VehicleWakeGate.Result.WEAK_SIGNAL,
                "弱包宽限不会在同一次会话中无限续用");
        System.out.println("synthetic_long_absence weak_connect=true no_direct_unlock=true");
    }

    /** 已验证MAC首包沿用原路径；更强同名新MAC不能抢尚新鲜的本车。 */
    private static void learnedAddressKeepsPriorityAndImmediateWake() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon own = named(OLD, -78, 1000, PREFIX);
        targets.observe(own, 1000);
        targets.select(known(), false, 1000);
        check(gate.claim(own, known(), true, false, true, 1000) == VehicleWakeGate.Result.SEND_SELECTED,
                "已学地址仍首包立即连接");
        VehicleBeacon neighbor = named(NEW, -50, 2000, PREFIX);
        targets.observe(neighbor, 2000);
        check(gate.claim(neighbor, known(), true, false, true, 2000) == VehicleWakeGate.Result.NOT_SELECTED,
                "新鲜可信本车优先，不把更强重名车发给官方");
        check(gate.maintain(known(), 14000).isEmpty(), "已学地址不受未知候选十二秒失败预算限制");
    }

    /** 真正已验证的MAC出现时不等错同名候选十二秒；人群设备洪水不会淘汰在途证据。 */
    private static void verifiedMacPreemptsUnconnectedNameAndActiveEvidenceSurvivesCrowd() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon nameOnly = named(NEW, -78, 1000, PREFIX);
        targets.observe(nameOnly, 1000);
        gate.claim(nameOnly, known, true, false, true, 1000);
        for (int index = 0; index < 80; index++) {
            VehicleBeacon other = named(String.format(java.util.Locale.ROOT, "DD:00:00:00:00:%02X", index),
                    -50, 1100 + index, PREFIX);
            targets.observe(other, 1100 + index);
        }
        check(targets.size(1180) == 32 && targets.evidenceFor(NEW, 1180) != null,
                "有界候选保留首包在途MAC命名证据，系统连接时仍可验证");
        VehicleBeacon actual = named(OLD, -80, 2000, PREFIX);
        targets.observe(actual, 2000);
        targets.select(known, false, 2000);
        check(gate.claim(actual, known, true, false, true, 2000) == VehicleWakeGate.Result.SEND_SELECTED,
                "没有ACL握手的同名误候选不阻挡已验证本车首包");
        check(targets.isConnectionCooling(NEW, 2000) && gate.activeAddress().equals(OLD),
                "错候选冷却，实际旧MAC立即使用唯一预连接通道");
        VehicleTargetSelector<VehicleBeacon> handshaking = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> handshakingGate = new VehicleWakeGate<>(handshaking);
        handshaking.observe(nameOnly, 1000);
        handshakingGate.claim(nameOnly, known, true, false, true, 1000);
        handshakingGate.onAclConnected(NEW, 1300);
        handshaking.observe(actual, 2000);
        handshaking.select(known, false, 2000);
        check(handshakingGate.claim(actual, known, true, false, true, 2000) == VehicleWakeGate.Result.WAIT_ACL_AUTH,
                "实际ACL正在鉴权时不抢槽位，可信MAC优先也不能打断握手");
    }

    /** 600ms普通名称确认不会重置早期节流；随后ACL出现不会再注入打断鉴权。 */
    private static void selectedConfirmationPreservesThrottleAndAcl() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1000);
        gate.claim(first, known(), true, false, true, 1000);
        VehicleBeacon confirmation = named(NEW, -75, 1600, PREFIX);
        targets.observe(confirmation, 1600);
        check(targets.select(known(), false, 1600) != null, "600ms名称确认");
        check(gate.claim(confirmation, known(), true, false, true, 1600) == VehicleWakeGate.Result.THROTTLED,
                "先wake后select仍保持1.5秒节流");
        gate.onAclConnected(NEW, 1791);
        VehicleBeacon later = named(NEW, -72, 5026, PREFIX);
        targets.observe(later, 5026);
        targets.select(known(), false, 5026);
        check(gate.claim(later, known(), true, false, true, 5026) == VehicleWakeGate.Result.WAIT_ACL_AUTH,
                "轮换候选确认或名称断档都不清除同地址握手保护");
        check(gate.attempts() == 1, "握手保护期间没有第二次广播注入");
    }

    /** 官方自己已建立ACL时首包也等待；无关和旧MAC迟到断连不能清新MAC的保护。 */
    private static void aclBeforeFirstAttemptIsAddressScoped() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon ad = named(NEW, -78, 1000, PREFIX);
        targets.observe(ad, 1000);
        gate.onAclConnected(OTHER, 900);
        check(gate.claim(ad, known(), true, false, true, 1000) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "无关MAC的ACL不是全局五秒延迟");
        VehicleTargetSelector<VehicleBeacon> waiting = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> waitingGate = new VehicleWakeGate<>(waiting);
        waiting.observe(ad, 1000);
        waitingGate.onAclConnected(NEW, 900);
        waitingGate.onAclDisconnected(OLD);
        check(waitingGate.claim(ad, known(), true, false, true, 1000) == VehicleWakeGate.Result.WAIT_ACL_AUTH,
                "同地址已握手时先观察官方就绪，不重连也不声明鉴权成功");
        VehicleBeacon newAd = named(NEW, -78, 5900, PREFIX);
        waiting.observe(newAd, 5900);
        check(waitingGate.claim(newAd, known(), true, false, true, 5900) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "五秒保护到期且有新鲜广告，才恢复尝试，不会无限等待ACL");
    }

    /** 同名多MAC只有一个在途；名称确认不续期，超时后普通发现也不能绕过冷却。 */
    private static void oneUnknownAttemptBudgetAndCooldownAcrossBothPaths() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1000);
        gate.claim(first, known, true, false, true, 1000);
        VehicleBeacon neighbor = named(OTHER, -50, 1800, PREFIX);
        targets.observe(neighbor, 1800);
        check(gate.claim(neighbor, known, true, false, true, 1800) == VehicleWakeGate.Result.CANDIDATE_IN_FLIGHT,
                "更强同名MAC也不能并行预连接多辆车");
        for (long now = 2000; now < 13000; now += 1000) {
            VehicleBeacon ad = named(NEW, -78, now, PREFIX);
            targets.observe(ad, now);
            targets.select(known, false, now);
            gate.claim(ad, known, true, false, true, now);
        }
        check(targets.selectionAgeMs(12000) == 11000, "后续命名包和选择从第一次实际尝试计预算");
        check(gate.maintain(known, 13000).equals(NEW), "首wake满十二秒结束在途，无新扫描也会失败");
        check(targets.isConnectionCooling(NEW, 13000) && gate.activeAddress().isEmpty(), "超时释放唯一通道并冷却30秒");
        VehicleBeacon cooled = named(NEW, -75, 14000, PREFIX);
        targets.observe(cooled, 14000);
        check(targets.select(known, false, 14000) == null, "普通select不能绕过同MAC冷却");
        check(gate.claim(cooled, known, true, false, true, 14000) == VehicleWakeGate.Result.CANDIDATE_COOLDOWN,
                "首包快速路径同样不能绕过冷却");
        targets.prune(35000);
        VehicleBeacon returning = named(NEW, -78, 35000, PREFIX);
        targets.observe(returning, 35000);
        check(gate.claim(returning, known, true, false, true, 35000) == VehicleWakeGate.Result.CANDIDATE_COOLDOWN,
                "广播消失20秒再出现，不重置还未结束的失败冷却");
        VehicleBeacon available = named(NEW, -78, 43000, PREFIX);
        targets.observe(available, 43000);
        check(gate.claim(available, known, true, false, true, 43000) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "真实冷却到期后新鲜强身份首包恢复尝试");
    }

    /** 十二秒预算恰好结束但同MAC刚进ACL握手时，不换另一辆车打断剩余保护窗口。 */
    private static void aclGraceSurvivesBudgetExpiry() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1000);
        gate.claim(first, known(), true, false, true, 1000);
        gate.onAclConnected(NEW, 12900);
        check(gate.maintain(known(), 13000).equals(NEW), "十二秒注入预算仍如期结束并冷却");
        VehicleBeacon neighbor = named(OTHER, -60, 13000, PREFIX);
        targets.observe(neighbor, 13000);
        check(gate.claim(neighbor, known(), true, false, true, 13000) == VehicleWakeGate.Result.WAIT_ACL_AUTH,
                "预算到期也保留刚建立ACL的五秒保护，不改连同名其他车");
        neighbor = named(OTHER, -60, 17900, PREFIX);
        targets.observe(neighbor, 17900);
        check(gate.claim(neighbor, known(), true, false, true, 17900) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "真实五秒保护结束后新鲜候选仍可尝试，不额外延迟");
    }

    /** 新首包路径官方通知先到但物理连接未可验证，不能凭旧学习MAC解锁或主动挂GATT。 */
    private static void earlyOfficialReadyWaitsForVerifiedPhysicalPeer() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleIdentity known = known();
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1000);
        gate.claim(first, known, true, false, true, 1000);
        check(gate.needsVerifiedEarlyPeer(), "新首包会话标记本次物理地址待验证");
        gate.onOfficialConnected(1500);
        OfficialReadyGate ready = new OfficialReadyGate();
        ready.onConnectionChanged(true);
        boolean claim = !gate.needsVerifiedEarlyPeer() && ready.claim(true, true);
        check(!claim && gate.needsVerifiedEarlyPeer(), "官方通知在线并非验证新MAC，旧地址完整不消费车控机会");
        VehicleIdentity verified = VehicleIdentity.fromConnectedPeer(NEW, NAME, first, known);
        check(verified != null, "唯一真实同MAC连接通过生产身份校验");
        targets.pinConnected(verified.address, 1600);
        gate.onVerifiedConnectedPeer();
        check(!gate.needsVerifiedEarlyPeer() && ready.claim(true, true), "真实地址晚到后立即补唯一官方就绪机会");
        check(!ready.claim(true, true), "重复身份采用不生成第二次就绪机会");
        gate.onOfficialDisconnected();
        check(!gate.needsVerifiedEarlyPeer(), "真正断连清理仅快路径会话门控");
        VehicleTargetSelector<VehicleBeacon> learned = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> learnedGate = new VehicleWakeGate<>(learned);
        VehicleBeacon own = named(OLD, -78, 1000, PREFIX);
        learned.observe(own, 1000);
        learned.select(known, false, 1000);
        learnedGate.claim(own, known, true, false, true, 1000);
        check(!learnedGate.needsVerifiedEarlyPeer(), "原已学MAC路径不新增验证等待，不影响稳定上锁会话");
    }

    /** 关闭、重学、官方就绪不能继续旧注入；没有任何接口可直接下发车控。 */
    private static void disabledOfficialReadyAndResetDoNotLeakAttempts() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleBeacon ad = named(NEW, -78, 1000, PREFIX);
        targets.observe(ad, 1000);
        check(gate.claim(ad, known(), false, false, true, 1000) == VehicleWakeGate.Result.DISABLED, "总开关关闭禁注入");
        check(gate.claim(ad, known(), true, true, true, 1000) == VehicleWakeGate.Result.OFFICIAL_READY, "官方在线禁抢连接");
        gate.claim(ad, known(), true, false, true, 1000);
        gate.onOfficialConnected(1100);
        check(gate.attempts() == 0 && gate.lastWakeAdvertisement() == null, "官方就绪结束注入会话");
        gate.onOfficialDisconnected();
        targets.clear();
        gate.clear();
        VehicleBeacon fresh = named(OTHER, -78, 2000, PREFIX);
        targets.observe(fresh, 2000);
        check(gate.claim(fresh, known(), true, false, true, 2000) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "重学/蓝牙重置后不会继承旧在途地址或节流");
    }

    /** 日志起点来自物理候选，命名断档和改名原因可见，读取快照不累积样本。 */
    private static void candidateDiagnosticsDescribeRealNameEvidence() {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleBeacon first = named(NEW, -78, 1000, PREFIX);
        targets.observe(first, 1013);
        check(targets.claimFirstObservation(first) && !targets.claimFirstObservation(first), "每个候选首次事件一次");
        check(targets.firstSeenAtMs(NEW) == 1000, "首广告采集时间不是回调时间");
        String initial = targets.observationDiagnostic(first, 1013);
        check(initial.contains("candidate_age_ms=13") && initial.contains("named_sightings=1")
                && initial.contains("named_span_ms=0") && initial.contains("name_reset=FIRST_NAME"), "首包诊断包括真实命名数和采集年龄");
        VehicleBeacon second = named(NEW, -78, 1600, PREFIX);
        targets.observe(second, 1600);
        check(targets.observationDiagnostic(second, 1600).contains("named_span_ms=600"), "跨度来自两条实际命名广播");
        VehicleBeacon gap = named(NEW, -78, 5000, PREFIX);
        targets.observe(gap, 5000);
        check(targets.observationDiagnostic(gap, 5000).contains("name_reset=NAME_GAP"), "命名断档明确记录重置原因");
        VehicleBeacon changed = beacon(NEW, "GeelyVehicle345", -78, 5300, PREFIX);
        targets.observe(changed, 5300);
        String changedDiagnostic = targets.observationDiagnostic(changed, 5300);
        check(changedDiagnostic.contains("name_reset=NAME_CHANGED")
                && changedDiagnostic.equals(targets.observationDiagnostic(changed, 5300)), "只读快照不改变样本或原因");
    }

    /** 自定义连接阈值只影响官方预热，不改变解锁、上锁或测距资格。 */
    private static void configuredConnectionThresholdOnlyGatesWakeup() {
        VehicleIdentity known = known();
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        gate.setConnectThreshold(-80.0f);
        check(gate.normalConnectMinRssi() == -80.0f && gate.longAbsenceConnectMinRssi() == -87.0f,
                "普通阈值可配，长时无车宽限保持固定7dB差");
        VehicleBeacon weak = named(NEW, -84, 1000, PREFIX);
        targets.observe(weak, 1000);
        check(gate.claim(weak, known, true, false, true, 1000) == VehicleWakeGate.Result.WEAK_SIGNAL,
                "低于配置连接阈值的广播不能唤醒银河");
        VehicleBeacon enough = named(NEW, -79, 2000, PREFIX);
        targets.observe(enough, 2000);
        check(gate.claim(enough, known, true, false, true, 2000) == VehicleWakeGate.Result.SEND_EARLY_NAME,
                "达到配置连接阈值后仅发起官方预热");
        check(!targets.authorizesRssi(known, enough), "连接预热仍不授予RSSI或车控资格");
    }

    /** 官方在线的稳定上锁回放夹入同名新MAC；只读预连接门控不能改变任何RSSI/驻留输出。 */
    private static void earlyNeighborDoesNotChangeLockPipeline() {
        RssiFilter baselineFilter = new RssiFilter();
        RssiFilter optimizedFilter = new RssiFilter();
        PassiveKeyLogic baselineLogic = new PassiveKeyLogic();
        baselineLogic.setThresholds(-75.0f, -82.0f, 4500L);
        PassiveKeyLogic optimizedLogic = new PassiveKeyLogic();
        optimizedLogic.setThresholds(-75.0f, -82.0f, 4500L);
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        VehicleIdentity known = known();
        long lockAt = -1L;
        for (long now = 1000; now <= 30000; now += 500) {
            int raw = now <= 10000 ? -60 : -85;
            VehicleBeacon own = named(OLD, raw, now, PREFIX);
            targets.observe(own, now);
            targets.select(known, true, now);
            VehicleBeacon neighbor = named(NEW, -50, now, CHANGED_PREFIX);
            targets.observe(neighbor, now);
            check(gate.claim(neighbor, known, true, true, true, now) == VehicleWakeGate.Result.OFFICIAL_READY,
                    "官方已在线时新候选绝不发起连接");
            check(!targets.authorizesRssi(known, neighbor), "同名邻车不能进入离开信号");
            float baseline = baselineFilter.update(raw, now);
            float optimized = optimizedFilter.update(raw, now);
            PassiveKeyLogic.Action expected = baselineLogic.onSample(baseline + 8f, baseline, now, true, false, true, true, "GATT");
            PassiveKeyLogic.Action actual = optimizedLogic.onSample(optimized + 8f, optimized, now, true, false, true, true, "GATT");
            check(Float.floatToIntBits(baseline) == Float.floatToIntBits(optimized) && expected == actual,
                    "每个原始GATT样本、未补偿平滑值和动作逐项相等");
            check(baselineLogic.diagnosticSnapshot(now).equals(optimizedLogic.diagnosticSnapshot(now)),
                    "驻留、6秒近场保护、最少样本和锁存完全一致");
            if (expected != PassiveKeyLogic.Action.NONE) {
                baselineLogic.onControlSent(expected, now);
                optimizedLogic.onControlSent(actual, now);
                if (expected == PassiveKeyLogic.Action.LOCK && lockAt < 0L) lockAt = now;
            }
        }
        check(lockAt == 17000, "上锁基线保持远场开始后7秒，口袋补偿不进入上锁距离");
    }

    /** 新建独立首包场景，避免失败冷却或上一个候选污染反例。 */
    private static VehicleWakeGate.Result firstResult(VehicleBeacon ad, VehicleIdentity known) {
        VehicleTargetSelector<VehicleBeacon> targets = new VehicleTargetSelector<>();
        VehicleWakeGate<VehicleBeacon> gate = new VehicleWakeGate<>(targets);
        targets.observe(ad, 1000);
        targets.select(known, false, 1000);
        return gate.claim(ad, known, true, false, true, 1000);
    }

    /** 合成已验证旧地址及完整厂商特征，所有名字/尾号均为测试数据。 */
    private static VehicleIdentity known() {
        return VehicleIdentity.from(beacon(OLD, NAME, -80, 100, PREFIX));
    }

    /** 合成新包携带完整名称，厂商前缀允许版本和状态字段变化。 */
    private static VehicleBeacon named(String address, int rssi, long now, String prefix) {
        return beacon(address, NAME, rssi, now, prefix);
    }

    /** 沿用生产解析器构建数据，不在测试中绕过尾号/厂商字段校验。 */
    private static VehicleBeacon beacon(String address, String name, int rssi, long now, String prefix) {
        return VehicleBeacon.parse(address, name, rssi, now, VehicleBeacon.hexToBytes(prefix), null, null, false);
    }

    /** 断言失败时给出对应预连接或测距隔离规则。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
