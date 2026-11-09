package com.luca.geelykeyradar;

public final class PassiveKeyLogicTest {

    /** 运行原有行为及新增的连续采样、发送反馈回归。 */
    public static void main(String[] args) {
        unlockOnlyOncePerApproach();
        awayLockNeedsDwellAndSamples();
        coldStartWeakSignalNeverLocks();
        signalLossDoesNotLock();
        drivingDoesNotSendUnlock();
        rearmAllowsNextApproachUnlock();
        drivingBlocksLockAndRearmUntilParked();
        middleZoneCancelsLockDwell();
        nearGuardAvoidsCabinFalseLock();
        officialConnectedFiresUnlockAndSilenceRearms();
        nearGuardAndDwellRunTogether();
        sampleGapRestartsDwell();
        duplicateSampleDoesNotCount();
        pocketCompensationDoesNotPostponeLock();
        failedLockRetainsSessionAndRetryIsBounded();
        pendingLockCancelledByReturnOrSignalLoss();
        pendingUnlockRespectsAllSwitches();
        lateOfficialConnectionDoesNotCreateAnotherAction();
        failedUnlockRearmsAfterLeaving();
        pocketJitterAfterLockDoesNotUnlockAgain();
        staleFarSignalDoesNotBlockOfficialConnection();
        failedUnlockBudgetRecoversAfterReturnWhileConnected();
        failedLockBudgetRecoversAfterReturnWhileConnected();
        completedDepartureRearmsNewOfficialApproach();
        freshFarReconnectAfterRearmStaysLocked();
        wiredSessionLocksAfterUnplugAndDeparture();
        signalLossRestartsReturnConfirmation();
        earlyOfficialUnlockDoesNotLockWhileApproaching();
        remainingAtEarlyUnlockDistanceDoesNotLock();
        abortedEarlyApproachStillLocksAfterRealWeakening();
        switchingRssiSourceDoesNotPretendDeparture();
        sourceSwitchRestartsContinuousFarDwell();
        manualUnlockIsNotAutomaticallyRepeated();
        manualEarlyUnlockDoesNotImmediatelyLock();
        manualLockRemainsLockedWhileNearby();
        manualLockRearmsAfterRealDeparture();
        manualLockSilenceRearmsNextApproach();
        manualUnlockOverridesManualLock();
        manualActionsInvalidateQueuedAutomaticAction();
        manualLockResetRestoresAutomaticControl();
        System.out.println("PassiveKeyLogicTest passed");
    }

    /** 一次接近完成解锁后，不因持续近场而重复下发。 */
    private static void unlockOnlyOncePerApproach() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, false, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "首次进入近场应解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onSample(-60f, 1_500L, false, false, true, true) == PassiveKeyLogic.Action.NONE,
                "同一接近会话不得重复解锁");
        check(logic.onSample(-65f, 5_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "车内状态回写不得重复解锁");
    }

    /** 已完成解锁的会话只有在足量连续远场确认后才请求上锁。 */
    private static void awayLockNeedsDwellAndSamples() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onSample(-84f, 8_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "第 1 个远场样本不应立刻上锁");
        check(logic.onSample(-84f, 9_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "第 2 个远场样本不应立刻上锁");
        check(logic.onSample(-84f, 10_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "驻留未满 4.5s 不应上锁");
        check(logic.onSample(-84f, 12_600L, true, false, true, true) == PassiveKeyLogic.Action.LOCK,
                "连续远场且样本足够后应上锁");
    }

    /** 从未接近过车辆的远处样本不能补锁。 */
    private static void coldStartWeakSignalNeverLocks() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        for (int sampleIndex = 0; sampleIndex < 30; sampleIndex++) {
            check(logic.onSample(-90f, 1_000L + sampleIndex * 1_000L, false, false, true, true)
                    == PassiveKeyLogic.Action.NONE, "冷启动一直在远处不得补发上锁");
        }
    }

    /** 物理链路中断后的保护窗口不能利用旧信号上锁。 */
    private static void signalLossDoesNotLock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        logic.onSignalLost(2_000L);
        for (int sampleIndex = 0; sampleIndex < 10; sampleIndex++) {
            check(logic.onSample(-90f, 3_000L + sampleIndex * 1_000L, false, false, true, true)
                    == PassiveKeyLogic.Action.NONE, "断连后的抑制窗口内不得上锁");
        }
    }

    /** 行驶或插线禁控状态下只记录近车证据，不发解锁。 */
    private static void drivingDoesNotSendUnlock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-55f, 1_000L, true, true, true, true) == PassiveKeyLogic.Action.NONE,
                "行驶中不得下发解锁");
        check(logic.isApproachLatched(), "行驶中应占用本次接近会话");
        check(logic.onSample(-90f, 10_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "停车后刚开始远离不应立刻上锁");
    }

    /** 上一次解锁完成且真正离开后，可以再次接近解锁。 */
    private static void rearmAllowsNextApproachUnlock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, false, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "第一次接近解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        // 关闭远离上锁时，持续远场 8 秒后重新武装。
        for (int sampleIndex = 0; sampleIndex < 10; sampleIndex++) {
            logic.onSample(-90f, 10_000L + sampleIndex * 1_000L, false, false, true, false);
        }
        check(logic.onSample(-60f, 21_000L, false, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "真正离开后再次接近应重新解锁");
    }

    /** 行驶期间的弱信号不触发锁车，停车后重新累计远场。 */
    private static void drivingBlocksLockAndRearmUntilParked() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        for (int sampleIndex = 0; sampleIndex < 20; sampleIndex++) {
            check(logic.onSample(-90f, 10_000L + sampleIndex * 1_000L, true, true, true, true)
                    == PassiveKeyLogic.Action.NONE, "行驶中不得上锁");
        }
        // 停车后继续远场，仍然应允许上锁。
        boolean sawLock = false;
        for (int sampleIndex = 0; sampleIndex < 10; sampleIndex++) {
            if (logic.onSample(-90f, 31_000L + sampleIndex * 1_000L, true, false, true, true)
                    == PassiveKeyLogic.Action.LOCK) {
                sawLock = true;
                break;
            }
        }
        check(sawLock, "停车后持续远离应恢复正常上锁");
    }

    /** 回到安全区后，先前的上锁计时不能继续累计。 */
    private static void middleZoneCancelsLockDwell() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onSample(-84f, 8_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "远场开始驻留");
        check(logic.onSample(-70f, 9_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "回到缓冲带应取消驻留");
        check(logic.onSample(-84f, 10_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "重新开始远场驻留");
        check(logic.onSample(-84f, 11_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "驻留未满不得上锁");
    }

    /** 持续不足六秒的车内深衰落仍由近场保护拦截。 */
    private static void nearGuardAvoidsCabinFalseLock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onSample(-65f, 2_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "车内近场保持");
        // 车内偶发深衰落 6 秒内不应开始上锁。
        for (int sampleIndex = 0; sampleIndex < 5; sampleIndex++) {
            check(logic.onSample(-85f, 3_000L + sampleIndex * 1_000L, true, false, true, true)
                    == PassiveKeyLogic.Action.NONE, "近场保护窗口内不得上锁");
        }
    }

    /** 官方就绪立即产生请求，发送完成后真正离开才能重新武装。 */
    private static void officialConnectedFiresUnlockAndSilenceRearms() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        // 银河抢先连上车机导致广播停止时，onOfficialConnected 应立即触发一次解锁
        check(logic.onOfficialConnected(1_000L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "银河蓝牙刚连上时应立即触发解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onOfficialConnected(2_000L, false, true) == PassiveKeyLogic.Action.NONE,
                "同一接近会话不得重复触发");
        // 用户离开车辆且完全走出蓝牙范围（无新 RSSI 样本），银河断开满 12s 后应自动重新武装
        logic.onOfficialDisconnected(5_000L);
        check(!logic.onTick(10_000L, false, false), "未满 12s 不应提前重置");
        check(logic.onTick(18_000L, false, false), "断开且无近场信号满 12s 应自动重新武装");
        check(logic.onOfficialConnected(20_000L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "重新武装后再次靠近连接应允许再次解锁");
    }

    /** 六秒近场保护与四点五秒驻留同时累计，不再串行等十点五秒。 */
    private static void nearGuardAndDwellRunTogether() {
        PassiveKeyLogic logic = nearSession();
        for (long time = 1_500L; time < 7_000L; time += 500L) {
            check(logic.onSample(-86f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "保护未满六秒不得上锁");
        }
        check(logic.onSample(-86f, 7_000L, true, false, true, true) == PassiveKeyLogic.Action.LOCK,
                "六秒时保护和远场确认均已满足，应当请求上锁");
    }

    /** 中间三十秒没有样本，不能累计成连续远场驻留。 */
    private static void sampleGapRestartsDwell() {
        PassiveKeyLogic logic = nearSession();
        logic.onSample(-90f, 10_000L, true, false, true, true);
        logic.onSample(-90f, 10_500L, true, false, true, true);
        check(logic.onSample(-90f, 40_500L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "跨长时间断档的第三个样本不能上锁");
        check(logic.getLockDwellSamples() == 1 && logic.getLockDwellStartMs() == 40_500L,
                "断档后必须从当前样本重新计时");
        logic.onTick(44_000L, true, false);
        check(logic.getLockDwellStartMs() == 0L, "tick 也应使过期驻留失效");
    }

    /** 同一时间戳的重复回调不会增加远场样本数。 */
    private static void duplicateSampleDoesNotCount() {
        PassiveKeyLogic logic = nearSession();
        logic.onSample(-90f, 10_000L, true, false, true, true);
        for (int duplicate = 0; duplicate < 20; duplicate++) {
            logic.onSample(-90f, 10_000L, true, false, true, true);
        }
        check(logic.getLockDwellSamples() == 1, "重复回调不应制造连续样本");
    }

    /** 口袋里实际远场为负八十五时，接近补偿不能抵消上锁阈值。 */
    private static void pocketCompensationDoesNotPostponeLock() {
        PassiveKeyLogic logic = nearSession();
        PassiveKeyLogic.Action lastAction = PassiveKeyLogic.Action.NONE;
        for (long time = 1_500L; time <= 7_000L; time += 500L) {
            lastAction = logic.onSample(-77f, -85f, time, true, false, true, true);
        }
        check(lastAction == PassiveKeyLogic.Action.LOCK, "补偿只影响靠近，不应挡住真实远场上锁");
    }

    /** 未发送的上锁保留会话并有限补发，成功发送后不再重复。 */
    private static void failedLockRetainsSessionAndRetryIsBounded() {
        PassiveKeyLogic logic = nearSession();
        long firstLock = feedUntilLock(logic, 10_000L);
        check(logic.hasBeenNear() && logic.isApproachLatched(), "请求创建不等于上锁成功");
        check(logic.onSample(-90f, firstLock + 500L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "请求未完成时不能创建第二个请求");
        logic.onControlFailed(PassiveKeyLogic.Action.LOCK, firstLock + 500L);
        long secondLock = feedUntilLock(logic, firstLock + 1_000L);
        check(secondLock - firstLock >= PassiveKeyLogic.CONTROL_FAILURE_COOLDOWN_MS,
                "失败补发必须有冷却和新的连续样本");
        logic.onControlFailed(PassiveKeyLogic.Action.LOCK, secondLock);
        for (long time = secondLock + 500L; time < secondLock + 30_000L; time += 500L) {
            check(logic.onSample(-90f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "同一接近会话失败两次后不得无限创建上锁请求");
        }

        PassiveKeyLogic success = nearSession();
        long lockAt = feedUntilLock(success, 10_000L);
        success.onControlSent(PassiveKeyLogic.Action.LOCK, lockAt);
        for (long time = lockAt + 500L; time < lockAt + 30_000L; time += 500L) {
            check(success.onSample(-90f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "同一次离开成功发送后不得再次上锁");
        }
    }

    /** 等待上锁时用户返回、信号断档和蓝牙关闭都撤销发送资格。 */
    private static void pendingLockCancelledByReturnOrSignalLoss() {
        PassiveKeyLogic logic = nearSession();
        long lockAt = feedUntilLock(logic, 10_000L);
        check(logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockAt, false, true, true),
                "当前新鲜远场可以发送");
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockAt + 3_001L, false, true, true),
                "等待时样本过期不能补锁");
        logic.onSample(-70f, lockAt + 500L, true, false, true, true);
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockAt + 500L, false, true, true),
                "回到车旁不能继续发送旧上锁");
        logic.onBluetoothReset(lockAt + 600L);
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockAt + 600L, false, true, true),
                "蓝牙重置必须拦截旧上锁");
    }

    /** 等待鉴权的解锁在禁控、开关关闭或明确远场时必须停止。 */
    private static void pendingUnlockRespectsAllSwitches() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onSample(-60f, 1_000L, false, false, true, true);
        check(logic.isPendingActionValid(PassiveKeyLogic.Action.UNLOCK, 4_100L, false, true, true),
                "车机停广播鉴权期间可等待有限时间");
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.UNLOCK, 4_100L, true, true, true),
                "插线或行驶中不得发送解锁");
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.UNLOCK, 4_100L, false, false, true),
                "关闭解锁开关后不得发送");
        logic.onSample(-90f, 4_500L, false, false, true, true);
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.UNLOCK, 4_500L, false, true, true),
                "已经走远不能因晚到的鉴权事件解锁");
    }

    /** 官方鉴权晚于距离请求时，仅完成已有请求，不再产生另一个动作。 */
    private static void lateOfficialConnectionDoesNotCreateAnotherAction() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onSample(-60f, 1_000L, false, false, true, true);
        check(logic.onOfficialConnected(4_100L, false, true) == PassiveKeyLogic.Action.NONE,
                "已有待发解锁不应再创建一次");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 4_100L);
        check(logic.onOfficialConnected(4_500L, false, true) == PassiveKeyLogic.Action.NONE,
                "重复就绪通知不能二次解锁");
        long lockAt = feedUntilLock(logic, 10_000L);
        check(logic.onOfficialConnected(lockAt + 200L, false, true) == PassiveKeyLogic.Action.NONE,
                "上锁等待中不能因连接恢复反向解锁");
        check(logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockAt + 200L, false, true, true),
                "晚到连接通知不得重置已经成立的上锁驻留");
    }

    /** 解锁发送失败耗尽本轮预算后，真正离开仍能恢复下一次接近。 */
    private static void failedUnlockRearmsAfterLeaving() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, false, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "第一次请求");
        logic.onControlFailed(PassiveKeyLogic.Action.UNLOCK, 1_100L);
        check(logic.onSample(-60f, 6_100L, false, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "冷却后允许一次补发");
        logic.onControlFailed(PassiveKeyLogic.Action.UNLOCK, 6_200L);
        check(logic.onSample(-60f, 12_000L, false, false, true, false) == PassiveKeyLogic.Action.NONE,
                "本轮失败预算不能无限延长");
        logic.onOfficialDisconnected(13_000L);
        check(logic.onTick(26_000L, false, false), "真正离开后应重置失败预算");
        check(logic.onSample(-60f, 27_000L, false, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "下一次接近仍应解锁");
    }

    /** 已上锁后在远场边界来回抖动，不能被口袋补偿变成新的接近会话。 */
    private static void pocketJitterAfterLockDoesNotUnlockAgain() {
        PassiveKeyLogic logic = nearSession();
        long lockAt = feedUntilLock(logic, 10_000L);
        logic.onControlSent(PassiveKeyLogic.Action.LOCK, lockAt);
        for (int sampleIndex = 1; sampleIndex <= 40; sampleIndex++) {
            long time = lockAt + sampleIndex * 500L;
            float distance = sampleIndex % 2 == 0 ? -81f : -83f;
            check(logic.onSample(distance + 8f, distance, time, true, false, true, true)
                    == PassiveKeyLogic.Action.NONE, "锁后边界抖动不能反向解锁或重复上锁");
        }
        check(!logic.hasBeenNear(), "边界抖动不能重新制造近车证据");
        check(logic.onOfficialConnected(lockAt + 20_100L, false, true) == PassiveKeyLogic.Action.NONE,
                "锁后重复连接通知不能解除回程确认");
        check(logic.onSample(-67f, -75f, lockAt + 20_500L, true, false, true, true)
                == PassiveKeyLogic.Action.NONE, "真实近场也须先开始回程确认");
        check(logic.onSample(-67f, -75f, lockAt + 21_000L, true, false, true, true)
                == PassiveKeyLogic.Action.NONE, "回程尚未满一秒");
        check(logic.onSample(-67f, -75f, lockAt + 21_500L, true, false, true, true)
                == PassiveKeyLogic.Action.UNLOCK, "持续真实返回后应允许下一次解锁");
    }

    /** 官方先连使广播停止时，过期的远场记录不能阻止新的就绪事件。 */
    private static void staleFarSignalDoesNotBlockOfficialConnection() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onSample(-95f, 1_000L, false, false, true, true);
        check(logic.onOfficialConnected(2_000L, false, true) == PassiveKeyLogic.Action.NONE,
                "新鲜的远场仍要阻止远处解锁");
        check(logic.onOfficialConnected(4_001L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "过期远场不能永久吞掉新的官方连接就绪");
    }

    /** 已确认未发送的解锁耗尽预算后，车主远离再回到近场可恢复，即使官方始终连接。 */
    private static void failedUnlockBudgetRecoversAfterReturnWhileConnected() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onSample(-60f, 1_000L, true, false, true, false);
        logic.onControlFailed(PassiveKeyLogic.Action.UNLOCK, 1_100L);
        check(logic.onSample(-60f, 6_100L, true, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "允许一次补发");
        logic.onControlFailed(PassiveKeyLogic.Action.UNLOCK, 6_200L);
        for (long time = 7_000L; time <= 17_000L; time += 500L) {
            check(logic.onSample(-86f, time, true, false, true, false) == PassiveKeyLogic.Action.NONE,
                    "仍在远处不能直接刷新预算并发送");
        }
        logic.onSample(-65f, 17_500L, true, false, true, false);
        logic.onSample(-65f, 18_000L, true, false, true, false);
        check(logic.onSample(-65f, 18_500L, true, false, true, false) == PassiveKeyLogic.Action.UNLOCK,
                "真实返回后应恢复新一次接近的发送预算");
    }

    /** 上锁失败预算只有真实回到近场才重置，单纯继续站在远处不会无限重试。 */
    private static void failedLockBudgetRecoversAfterReturnWhileConnected() {
        PassiveKeyLogic logic = nearSession();
        long firstLock = feedUntilLock(logic, 10_000L);
        logic.onControlFailed(PassiveKeyLogic.Action.LOCK, firstLock);
        long secondLock = feedUntilLock(logic, firstLock + 500L);
        logic.onControlFailed(PassiveKeyLogic.Action.LOCK, secondLock);
        for (long time = secondLock + 500L; time <= secondLock + 10_000L; time += 500L) {
            check(logic.onSample(-90f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "站在远处不能再次发起上锁请求");
        }
        for (long time = secondLock + 10_500L; time <= secondLock + 11_500L; time += 500L) {
            check(logic.onSample(-65f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "上次解锁已送达，返回时无需再次解锁");
        }
        check(feedUntilLock(logic, secondLock + 12_000L) > secondLock + 12_000L,
                "再次真实离开应有新的上锁机会");
    }

    /** 真正离开且官方断连后属于新会话，不把上一轮锁后的回程保护永久带到下一次接近。 */
    private static void completedDepartureRearmsNewOfficialApproach() {
        PassiveKeyLogic logic = nearSession();
        long lockAt = feedUntilLock(logic, 10_000L);
        logic.onControlSent(PassiveKeyLogic.Action.LOCK, lockAt);
        logic.onOfficialDisconnected(lockAt + 1_000L);
        check(logic.onTick(lockAt + 13_000L, false, false), "断连且离开足够久应结束旧会话");
        check(logic.onOfficialConnected(lockAt + 14_000L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "新会话里官方先完成连接时仍应处理即时解锁");
    }

    /** 即使已重武装，官方重连时有新鲜远场证据也不能远处解锁。 */
    private static void freshFarReconnectAfterRearmStaysLocked() {
        PassiveKeyLogic logic = nearSession();
        long lockAt = feedUntilLock(logic, 10_000L);
        logic.onControlSent(PassiveKeyLogic.Action.LOCK, lockAt);
        logic.onOfficialDisconnected(lockAt + 1_000L);
        logic.onTick(lockAt + 13_000L, false, false);
        check(logic.onSample(-75f, -83f, lockAt + 13_500L, false, false, true, true)
                == PassiveKeyLogic.Action.NONE, "补偿后的边界信号不能作为新接近");
        check(logic.onOfficialConnected(lockAt + 13_700L, false, true) == PassiveKeyLogic.Action.NONE,
                "新鲜远场必须拦截重连即解锁");
    }

    /** 车内USB禁控只占用当前接近，拔线后靠新的远场样本正常锁车。 */
    private static void wiredSessionLocksAfterUnplugAndDeparture() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onOfficialConnected(1_000L, true, false) == PassiveKeyLogic.Action.NONE,
                "插线期间官方连接也不发送解锁");
        for (long time = 1_500L; time <= 30_000L; time += 500L) {
            check(logic.onSample(-52f, -60f, time, true, true, false, false)
                    == PassiveKeyLogic.Action.NONE, "插线车内样本不能产生车控");
        }
        check(logic.isApproachLatched() && logic.hasBeenNear(), "禁控期间仍保留近车会话");
        check(feedUntilLock(logic, 30_500L) >= 36_000L, "拔线离开后仍须满足保护与远场确认");
    }

    /** 回程近场确认过程中断链，不能跨断链累计为一秒连续返回。 */
    private static void signalLossRestartsReturnConfirmation() {
        PassiveKeyLogic logic = nearSession();
        long lockAt = feedUntilLock(logic, 10_000L);
        logic.onControlSent(PassiveKeyLogic.Action.LOCK, lockAt);
        logic.onSample(-65f, lockAt + 10_000L, true, false, true, true);
        logic.onSignalLost(lockAt + 10_400L);
        check(logic.onSample(-65f, lockAt + 11_000L, true, false, true, true)
                == PassiveKeyLogic.Action.NONE, "断链后应从新样本重新确认返回");
        check(logic.onSample(-65f, lockAt + 12_000L, true, false, true, true)
                == PassiveKeyLogic.Action.UNLOCK, "新的连续近场满足后可返回解锁");
    }

    /** 等价回放实车弱信号提前解锁后持续走近的窗口，不能在六秒保护结束时反向上锁。 */
    private static void earlyOfficialUnlockDoesNotLockWhileApproaching() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        long connectedAt = 10_000L;
        logic.onSample(-86f, connectedAt - 4_416L, false, false, true, true);
        check(logic.onOfficialConnected(connectedAt, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "官方鉴权就绪仍应提前解锁，不修改原有解锁距离");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, connectedAt + 5L);
        float[] approaching = {-89f, -89f, -88.5f, -88f, -87.5f, -87f, -86.5f,
                -86f, -85f, -84.5f, -84f, -83f, -82.5f};
        for (int sampleIndex = 0; sampleIndex < approaching.length; sampleIndex++) {
            long time = connectedAt + 51L + sampleIndex * 500L;
            check(logic.onSample(approaching[sampleIndex], time, true, false, true, true)
                    == PassiveKeyLogic.Action.NONE, "仍在走近、未到过车旁时不能把弱信号当成离开");
        }
        check(logic.onSample(-76f, connectedAt + 8_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "真正到达车旁后仍属于同一次接近，不能再次解锁");
        check(logic.onSample(-73f, connectedAt + 9_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "首次解锁后继续靠近不应再发第二次解锁");
        check(feedUntilLock(logic, connectedAt + 9_500L) <= connectedAt + 17_000L,
                "真正到过车旁后离开，应保持原有远离阈值和驻留响应");
    }

    /** 提前解锁后停在远距边界，不能仅因保护计时结束自动上锁。 */
    private static void remainingAtEarlyUnlockDistanceDoesNotLock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onOfficialConnected(1_000L, false, true);
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_005L);
        for (long time = 1_500L; time <= 31_000L; time += 500L) {
            check(logic.onSample(-86f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "提前解锁位置没有真实离开证据，不得重新锁车");
        }
    }

    /** 提前解锁但没有走到车旁便转身离开，仍可凭同源持续变弱确认远离，避免留下未锁车辆。 */
    private static void abortedEarlyApproachStillLocksAfterRealWeakening() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onOfficialConnected(1_000L, false, true);
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_005L);
        for (long time = 1_500L; time <= 12_000L; time += 500L) {
            float distance = time <= 3_000L ? -86f : -89f;
            check(logic.onSample(distance, distance, time, true, false, true, true, "GATT")
                    == PassiveKeyLogic.Action.NONE, "小幅波动没有进入近场，也不足以确认真正离开");
        }
        for (long time = 12_500L; time < 17_000L; time += 500L) {
            check(logic.onSample(-93f, -93f, time, true, false, true, true, "GATT")
                    == PassiveKeyLogic.Action.NONE, "同源明显变弱后仍须完整远场驻留");
        }
        check(logic.onSample(-93f, -93f, 17_000L, true, false, true, true, "GATT")
                == PassiveKeyLogic.Action.LOCK, "真实转身离开应仍能上锁");
        check(logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, 17_000L, false, true, true),
                "同源确认远离也必须在发送前可核验");
    }

    /** 广播负八十切到GATT负八十九是来源差异，不能作为提前解锁后的远离下降。 */
    private static void switchingRssiSourceDoesNotPretendDeparture() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-72f, -80f, 1_000L, false, false, true, true, "SCAN")
                == PassiveKeyLogic.Action.UNLOCK, "保留口袋补偿的提前解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_005L);
        logic.onSample(-72f, -80f, 1_500L, false, false, true, true, "SCAN");
        for (long time = 2_000L; time <= 22_000L; time += 500L) {
            check(logic.onSample(-81f, -89f, time, true, false, true, true, "GATT")
                    == PassiveKeyLogic.Action.NONE, "更换采样来源的落差不等于人已走远");
        }
        check(logic.diagnosticSnapshot(22_000L).contains("early_peak_dbm=-89.0"),
                "提前解锁阶段必须使用新采样来源自己的峰值");
    }

    /** 各通道的远场证据独立连续，切换通道时不能使用另一通道累计的旧驻留。 */
    private static void sourceSwitchRestartsContinuousFarDwell() {
        PassiveKeyLogic logic = nearSession();
        for (long time = 10_000L; time < 14_500L; time += 500L) {
            check(logic.onSample(-86f, -86f, time, true, false, true, true, "GATT")
                    == PassiveKeyLogic.Action.NONE, "首通道未满驻留时间");
        }
        check(logic.onSample(-86f, -86f, 14_500L, true, false, true, true, "SCAN")
                == PassiveKeyLogic.Action.NONE, "来源切换的第一帧不能继承旧驻留并上锁");
        check(logic.getLockDwellStartMs() == 14_500L && logic.getLockDwellSamples() == 1,
                "新的通道从当前样本开始确认");
        for (long time = 15_000L; time < 19_000L; time += 500L) {
            check(logic.onSample(-86f, -86f, time, true, false, true, true, "SCAN")
                    == PassiveKeyLogic.Action.NONE, "新通道仍须连续确认");
        }
        check(logic.onSample(-86f, -86f, 19_000L, true, false, true, true, "SCAN")
                == PassiveKeyLogic.Action.LOCK, "新通道满足原四点五秒后正常上锁");
    }

    /** 手动解锁计入本次靠近会话，重复通知与近场样本不能再自动解锁。 */
    private static void manualUnlockIsNotAutomaticallyRepeated() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        check(logic.onOfficialConnected(1_500L, false, true) == PassiveKeyLogic.Action.NONE,
                "手动解锁后官方连接通知不得重复解锁");
        for (long time = 2_000L; time <= 20_000L; time += 500L) {
            check(logic.onSample(-60f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "手动解锁后持续近场不得重复解锁");
        }
        check(logic.diagnosticSnapshot(20_000L).contains("feedback=MANUAL_FINISHED_UNLOCK"),
                "日志应区分手动交付与自动交付");
        feedUntilLock(logic, 21_000L);
    }

    /** 距离阈值之外手动解锁后，停留或继续靠近不应立刻反向自动上锁。 */
    private static void manualEarlyUnlockDoesNotImmediatelyLock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        for (long time = 1_500L; time <= 20_000L; time += 500L) {
            check(logic.onSample(-84f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "没有真实离开证据时，不得反向撤销用户手动解锁");
        }
    }

    /** 手动上锁后，即使人在车旁或官方仍连接，也不得自动重开或重复上锁。 */
    private static void manualLockRemainsLockedWhileNearby() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_000L);
        check(logic.onOfficialConnected(1_500L, false, true) == PassiveKeyLogic.Action.NONE,
                "手动上锁后的连接事件不能重新解锁");
        for (long time = 2_000L; time <= 30_000L; time += 500L) {
            check(logic.onSample(-60f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "人在车旁时保持手动上锁结果");
        }
        for (long time = 30_500L; time <= 45_000L; time += 500L) {
            check(logic.onSample(-90f, time, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "官方仍连接时，不能将手动上锁再重复发送");
        }
        check(logic.onSample(-60f, 45_500L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "同一连接会话返回车旁也不能撤销手动上锁");
        check(logic.diagnosticSnapshot(45_500L).contains("manual_lock_latched=true"),
                "导出日志应能解释手动上锁锁存");
    }

    /** 手动上锁后，真实远场连续八秒且官方断开，才恢复下一次自动靠近。 */
    private static void manualLockRearmsAfterRealDeparture() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_000L);
        logic.onSample(-60f, 2_000L, true, false, true, true);
        logic.onOfficialDisconnected(3_000L);
        for (long time = 4_000L; time <= 12_000L; time += 500L) {
            check(logic.onSample(-90f, time, false, false, true, true) == PassiveKeyLogic.Action.NONE,
                    "远离重新武装时不能补发手动上锁");
        }
        check(logic.diagnosticSnapshot(12_000L).contains("manual_lock_latched=false"),
                "真实离开后应清除手动锁存");
        check(logic.onOfficialConnected(20_000L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "下一次实际接近仍应正常自动解锁");
    }

    /** 手动上锁后自然离开至无信号，沿用原静默重新武装规则。 */
    private static void manualLockSilenceRearmsNextApproach() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_000L);
        logic.onSample(-60f, 2_000L, true, false, true, true);
        logic.onOfficialDisconnected(3_000L);
        check(!logic.onTick(13_500L, false, false), "静默不足十二秒不得恢复自动解锁");
        check(logic.onTick(14_000L, false, false), "实际离开并断连静默后应恢复");
        check(logic.onOfficialConnected(15_000L, false, true) == PassiveKeyLogic.Action.UNLOCK,
                "离开后的新官方连接应恢复原自动解锁");
    }

    /** 用户再点手动解锁可立即解除手动上锁锁存，随后正常监测远离。 */
    private static void manualUnlockOverridesManualLock() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_000L);
        logic.onSample(-60f, 2_000L, true, false, true, true);
        logic.onManualControlSent(PassiveKeyLogic.Action.UNLOCK, 3_000L);
        check(logic.onSample(-60f, 4_000L, true, false, true, true) == PassiveKeyLogic.Action.NONE,
                "手动解锁后不能重复自动解锁");
        check(logic.diagnosticSnapshot(4_000L).contains("manual_lock_latched=false"),
                "手动解锁应立即清除手动上锁锁存");
        feedUntilLock(logic, 10_000L);
    }

    /** 手动操作完成后，旧自动请求不再有效，不能延迟覆盖用户选择。 */
    private static void manualActionsInvalidateQueuedAutomaticAction() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先建立自动待发解锁");
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_500L);
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.UNLOCK, 1_600L, false, true, true),
                "旧自动解锁不能覆盖用户手动上锁");
        logic.onManualControlSent(PassiveKeyLogic.Action.UNLOCK, 2_000L);
        logic.onSample(-60f, 3_000L, true, false, true, true);
        long lockTime = feedUntilLock(logic, 9_000L);
        logic.onManualControlSent(PassiveKeyLogic.Action.UNLOCK, lockTime + 100L);
        check(!logic.isPendingActionValid(PassiveKeyLogic.Action.LOCK, lockTime + 200L, false, true, true),
                "旧自动上锁不能覆盖用户手动解锁");
    }

    /** 重学或重新启用服务时清除手动会话锁存，不永久影响自动功能。 */
    private static void manualLockResetRestoresAutomaticControl() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onManualControlSent(PassiveKeyLogic.Action.LOCK, 1_000L);
        logic.reset();
        check(logic.onSample(-60f, 2_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "重置后应恢复自动解锁");
    }

    /** 创建已完成一次解锁的近车会话，供远离场景复用。 */
    private static PassiveKeyLogic nearSession() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        check(logic.onSample(-60f, 1_000L, true, false, true, true) == PassiveKeyLogic.Action.UNLOCK,
                "先完成近车解锁");
        logic.onControlSent(PassiveKeyLogic.Action.UNLOCK, 1_000L);
        return logic;
    }

    /** 连续提供足量远场样本，最多二十秒内应生成一个上锁请求。 */
    private static long feedUntilLock(PassiveKeyLogic logic, long startMs) {
        for (long time = startMs; time <= startMs + 20_000L; time += 500L) {
            if (logic.onSample(-90f, time, true, false, true, true) == PassiveKeyLogic.Action.LOCK) return time;
        }
        throw new AssertionError("连续远场未生成上锁请求");
    }

    /** 断言失败时保留场景语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
