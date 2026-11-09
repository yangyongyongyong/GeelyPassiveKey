package com.luca.geelykeyradar;

import java.util.Locale;

/**
 * 极简无感钥匙状态机。
 *
 * 只做两件事：
 * 1. 有效信号进入近场阈值 -> 触发一次解锁；
 * 2. 有效信号持续处于远场阈值 -> 触发一次上锁。
 *
 * 额外保留的安全约束：
 * - 冷启动/一直在远处时，不主动补发上锁；
 * - 蓝牙断连、无新样本时不使用旧 RSSI 误锁；
 * - 同一接近会话只解锁一次，必须真正离开并持续远场后才重新武装；
 * - 行车中不自动上锁，也不重新武装。
 */
final class PassiveKeyLogic {

    enum Action {
        NONE,
        UNLOCK,
        LOCK
    }

    static final float UNLOCK_DBM = -75.0f; // 实车验证最佳提前解锁阈值
    static final float LOCK_DBM = -82.0f;
    static final float REARM_HYSTERESIS_DBM = 4.0f;
    static final float NEAR_GUARD_DBM = -78.0f;
    static final long LOCK_DWELL_MS = 4500L;
    static final long NEAR_GUARD_MS = 6000L;
    static final long UNLOCK_COOLDOWN_MS = 8000L;
    static final long REARM_FAR_MS = 8000L;
    static final long REARM_DISCONNECTED_SILENCE_MS = 12000L;
    static final long SIGNAL_STALE_MS = 3000L;
    static final long SIGNAL_LOST_INHIBIT_MS = 10000L;
    static final long CONTROL_FAILURE_COOLDOWN_MS = 5000L;
    static final long RETURN_NEAR_CONFIRM_MS = 1000L;
    static final float EARLY_DEPARTURE_DROP_DBM = 6.0f;
    static final int MAX_REQUESTS_PER_ACTION = 2;
    static final int LOCK_MIN_SAMPLES = 3;

    private float unlockDbm = UNLOCK_DBM;
    private float lockDbm = LOCK_DBM;
    private long lockDwellMs = LOCK_DWELL_MS;

    private boolean approachLatched = false;
    private boolean manualLockLatched = false;
    private boolean hasBeenNear = false;
    private long lastUnlockAtMs = 0L;
    private long lockDwellStartMs = 0L;
    private int lockDwellSamples = 0;
    private long farSinceMs = 0L;
    private long lastSampleAtMs = 0L;
    private long lastNearAtMs = 0L;
    private long lastApproachAtMs = 0L;
    private long officialDisconnectedAtMs = 0L;
    private long lockInhibitUntilMs = 0L;
    private float lastDistanceDbm = Float.NaN;
    private Action pendingAction = Action.NONE;
    private int unlockRequests = 0;
    private int lockRequests = 0;
    private long controlRetryAfterMs = 0L;
    private boolean returnConfirmationRequired = false;
    private long returnNearStartMs = 0L;
    private int returnNearSamples = 0;
    private String lastSampleDecision = "NO_SAMPLE";
    private String lastOfficialDecision = "NO_OFFICIAL_EVENT";
    private String lastMaintenanceDecision = "NO_TICK";
    private String lastControlFeedback = "NONE";
    private long lastSampleDecisionAtMs = 0L;
    private long lastOfficialDecisionAtMs = 0L;
    private boolean departureArmed = false;
    private long earlyUnlockSentAtMs = 0L;
    private float earlyApproachPeakDbm = Float.NaN;
    private String measurementSource = "";

    /**
     * 动态更新自定义阈值（靠近解锁阈值、离开上锁阈值、离开确认毫秒数）。
     */
    synchronized void setThresholds(float unlockDbm, float lockDbm, long lockDwellMs) {
        this.unlockDbm = unlockDbm;
        this.lockDbm = Math.min(lockDbm, unlockDbm - 6.0f);
        this.lockDwellMs = Math.max(1500L, lockDwellMs);
        resetLockDwell();
    }

    /** 返回当前解锁阈值。 */
    synchronized float getUnlockDbm() {
        return unlockDbm;
    }

    /** 返回当前上锁阈值。 */
    synchronized float getLockDbm() {
        return lockDbm;
    }

    /** 返回连续远场确认时长。 */
    synchronized long getLockDwellMs() {
        return lockDwellMs;
    }

    /**
     * 输入一条新鲜的有效 RSSI，返回本样本应执行的动作。
     *
     * @param effectiveDbm     经过滤波和口袋补偿后的有效信号
     * @param nowMs            当前时间
     * @param officialConnected 银河 App 的蓝牙钥匙是否已连接
     * @param driving          银河通知是否明确显示“行驶中”
     * @param autoUnlock       用户是否开启靠近自动解锁
     * @param autoLock         用户是否开启远离自动上锁
     */
    synchronized Action onSample(
            float effectiveDbm,
            long nowMs,
            boolean officialConnected,
            boolean driving,
            boolean autoUnlock,
            boolean autoLock
    ) {
        return onSample(effectiveDbm, effectiveDbm, nowMs, officialConnected, driving, autoUnlock, autoLock);
    }

    /** 解锁可以补偿口袋衰减；远离判定及近场保护始终使用未加补偿的滤波信号。 */
    synchronized Action onSample(
            float approachDbm,
            float distanceDbm,
            long nowMs,
            boolean officialConnected,
            boolean driving,
            boolean autoUnlock,
            boolean autoLock
    ) {
        return onSample(approachDbm, distanceDbm, nowMs, officialConnected, driving, autoUnlock, autoLock, "RSSI");
    }

    /** 同一测量来源内确认靠近和远离，广播切换到GATT的强度差不能当成人在离开。 */
    synchronized Action onSample(
            float approachDbm,
            float distanceDbm,
            long nowMs,
            boolean officialConnected,
            boolean driving,
            boolean autoUnlock,
            boolean autoLock,
            String source
    ) {
        if (!Float.isFinite(approachDbm) || !Float.isFinite(distanceDbm)
                || (lastSampleAtMs > 0L && nowMs <= lastSampleAtMs)) {
            return sampleDecision(Action.NONE, "INVALID_SAMPLE_OR_ORDER", nowMs);
        }
        if (lastSampleAtMs > 0L && (nowMs - lastSampleAtMs) > SIGNAL_STALE_MS) {
            resetLockDwell();
            farSinceMs = 0L;
            resetReturnConfirmation();
            earlyApproachPeakDbm = Float.NaN;
        }
        String currentSource = source == null ? "RSSI" : source;
        if (!currentSource.equals(measurementSource)) {
            measurementSource = currentSource;
            earlyApproachPeakDbm = Float.NaN;
            resetLockDwell();
            farSinceMs = 0L;
        }
        lastSampleAtMs = nowMs;
        lastDistanceDbm = distanceDbm;
        if (officialConnected) {
            officialDisconnectedAtMs = 0L;
        }
        float nearGuard = Math.max(lockDbm + 3.0f, Math.min(unlockDbm - 3.0f, NEAR_GUARD_DBM));
        if (distanceDbm >= nearGuard) {
            lastNearAtMs = nowMs;
            departureArmed = true;
        }
        if (manualLockLatched) {
            if (!officialConnected && !driving && distanceDbm <= lockDbm) {
                if (farSinceMs == 0L) farSinceMs = nowMs;
                if ((nowMs - farSinceMs) >= REARM_FAR_MS) {
                    rearm();
                    return sampleDecision(Action.NONE, "REARMED_AFTER_MANUAL_LOCK_DEPARTURE", nowMs);
                }
            } else {
                farSinceMs = 0L;
            }
            return sampleDecision(Action.NONE, "MANUAL_LOCK_LATCHED", nowMs);
        }
        if (hasBeenNear && earlyUnlockSentAtMs > 0L && !departureArmed) {
            earlyApproachPeakDbm = Float.isNaN(earlyApproachPeakDbm)
                    ? distanceDbm : Math.max(earlyApproachPeakDbm, distanceDbm);
        }
        if (returnConfirmationRequired && !confirmReturn(distanceDbm, nearGuard, nowMs, driving)) {
            return sampleDecision(Action.NONE, driving ? "DRIVING_OR_WIRED_BLOCK" : "RETURN_CONFIRMATION", nowMs);
        }

        if (approachDbm >= unlockDbm && distanceDbm > lockDbm) {
            lastApproachAtMs = nowMs;
            hasBeenNear = true;
            farSinceMs = 0L;
            resetLockDwell();

            // 行车中人在车内，不需要再发解锁指令；但标记本次会话已“接近”，
            // 停车下车后仍可正常进入远离上锁流程。
            if (driving) {
                approachLatched = true;
                return sampleDecision(Action.NONE, "DRIVING_OR_WIRED_BLOCK", nowMs);
            }
            if (!autoUnlock) {
                return sampleDecision(Action.NONE, "UNLOCK_DISABLED", nowMs);
            }
            if (approachLatched || pendingAction != Action.NONE
                    || unlockRequests >= MAX_REQUESTS_PER_ACTION || nowMs < controlRetryAfterMs) {
                return sampleDecision(Action.NONE, approachLatched ? "APPROACH_LATCHED"
                        : pendingAction != Action.NONE ? "CONTROL_PENDING"
                        : unlockRequests >= MAX_REQUESTS_PER_ACTION ? "UNLOCK_REQUEST_LIMIT"
                        : "CONTROL_RETRY_COOLDOWN", nowMs);
            }
            if (lastUnlockAtMs > 0L && (nowMs - lastUnlockAtMs) < UNLOCK_COOLDOWN_MS) {
                return sampleDecision(Action.NONE, "UNLOCK_COOLDOWN", nowMs);
            }

            approachLatched = true;
            lastUnlockAtMs = nowMs;
            pendingAction = Action.UNLOCK;
            unlockRequests++;
            return sampleDecision(Action.UNLOCK, "UNLOCK_THRESHOLD_REACHED", nowMs);
        }

        if (distanceDbm <= lockDbm) {
            // 冷启动就在远处，或本次会话从未接近过车辆：不要凭空补一条上锁指令。
            if (!hasBeenNear) {
                resetLockDwell();
                return sampleDecision(Action.NONE, "NO_NEAR_EVIDENCE", nowMs);
            }

            if (driving) {
                resetLockDwell();
                farSinceMs = 0L;
                return sampleDecision(Action.NONE, "DRIVING_OR_WIRED_BLOCK", nowMs);
            }

            if (nowMs < lockInhibitUntilMs) {
                resetLockDwell();
                farSinceMs = 0L;
                return sampleDecision(Action.NONE, "SIGNAL_LOSS_INHIBIT", nowMs);
            }
            if (!hasConfirmedDeparture()) {
                resetLockDwell();
                farSinceMs = 0L;
                return sampleDecision(Action.NONE, !autoLock ? "LOCK_DISABLED" : "APPROACH_NOT_FINISHED", nowMs);
            }
            // 防止人在车内时偶发多径深衰落直接触发上锁：必须先离开近场至少 6 秒。
            // 保护窗口与远场驻留同时计时，保护只拦截发送，不再反复清空连续远场证据。

            if (lockDwellStartMs == 0L) {
                lockDwellStartMs = nowMs;
                lockDwellSamples = 0;
            }
            if (farSinceMs == 0L) farSinceMs = nowMs;
            lockDwellSamples++;

            if (pendingAction == Action.NONE
                    && (unlockRequests >= MAX_REQUESTS_PER_ACTION || lockRequests >= MAX_REQUESTS_PER_ACTION)
                    && (nowMs - farSinceMs) >= REARM_FAR_MS) {
                returnConfirmationRequired = true;
                resetReturnConfirmation();
                resetLockDwell();
                return sampleDecision(Action.NONE, "RETURN_REQUIRED_AFTER_FAILURE", nowMs);
            }

            long elapsed = nowMs - lockDwellStartMs;
            if (autoLock
                    && pendingAction == Action.NONE
                    && lockRequests < MAX_REQUESTS_PER_ACTION
                    && nowMs >= controlRetryAfterMs
                    && lockDwellSamples >= LOCK_MIN_SAMPLES
                    && elapsed >= lockDwellMs
                    && (lastNearAtMs == 0L || (nowMs - lastNearAtMs) >= NEAR_GUARD_MS)) {
                pendingAction = Action.LOCK;
                lockRequests++;
                return sampleDecision(Action.LOCK, "LOCK_DWELL_READY", nowMs);
            }

            // 解锁后银河断开、但上锁命令没有成功送出时，也要允许下一次接近重新解锁。
            // 正常会话只有“银河已经断开”且持续远场才重新武装；失败预算耗尽则须确认远离再返回。
            if (hasBeenNear && !officialConnected && pendingAction == Action.NONE) {
                if ((nowMs - farSinceMs) >= REARM_FAR_MS) {
                    rearm();
                    return sampleDecision(Action.NONE, "REARMED_AFTER_DEPARTURE", nowMs);
                }
            }
            return sampleDecision(Action.NONE, !autoLock ? "LOCK_DISABLED"
                    : pendingAction != Action.NONE ? "CONTROL_PENDING"
                    : lockRequests >= MAX_REQUESTS_PER_ACTION ? "LOCK_REQUEST_LIMIT"
                    : nowMs < controlRetryAfterMs ? "CONTROL_RETRY_COOLDOWN"
                    : lockDwellSamples < LOCK_MIN_SAMPLES ? "LOCK_NEEDS_SAMPLES"
                    : elapsed < lockDwellMs ? "LOCK_DWELL_IN_PROGRESS" : "NEAR_GUARD", nowMs);
        }

        // 中间缓冲带：禁止上锁，并清除远离计时。
        resetLockDwell();
        farSinceMs = 0L;
        return sampleDecision(Action.NONE, "BUFFER_ZONE", nowMs);
    }

    /**
     * 信号断连或扫描失败时调用：不使用旧样本完成上锁驻留。
     */
    synchronized void onSignalLost(long nowMs) {
        resetLockDwell();
        resetReturnConfirmation();
        earlyApproachPeakDbm = Float.NaN;
        farSinceMs = 0L;
        lockInhibitUntilMs = nowMs + SIGNAL_LOST_INHIBIT_MS;
    }

    /**
     * 蓝牙关闭/重启后短暂禁止自动上锁，避免把端口重启当成“人已走远”。
     */
    synchronized void onBluetoothReset(long nowMs) {
        resetLockDwell();
        resetReturnConfirmation();
        earlyApproachPeakDbm = Float.NaN;
        farSinceMs = 0L;
        lockInhibitUntilMs = nowMs + 15000L;
    }

    /**
     * 当银河官方蓝牙钥匙刚刚从“未连接”变为“已连接”时调用。
     *
     * 为什么需要这个入口：
     * 吉利车机只有 1 个 BLE 连接槽位。一旦银河 App（或 vivo 车联网）抢先连上车机，
     * 车机立即停止发送 BLE 广播；如果此时本应用还没来得及采到 >= -63dBm 的广播样本，
     * 单靠 RSSI 阈值就会因为“广播突然消失”而漏掉本次靠近解锁（造成车主在车脸前罚站）。
     *
     * 因此：当银河蓝牙钥匙刚连上（必然发生在车旁蓝牙范围内），且本次接近会话尚未解锁过时，
     * 直接触发一次解锁并闩锁本次接近会话，后续由连接态 GATT RSSI 负责监测离开自动上锁。
     */
    synchronized Action onOfficialConnected(long nowMs, boolean driving, boolean autoUnlock) {
        officialDisconnectedAtMs = 0L;
        if (manualLockLatched) {
            return officialDecision(Action.NONE, "MANUAL_LOCK_LATCHED", nowMs);
        }
        if (pendingAction != Action.NONE || returnConfirmationRequired) {
            return officialDecision(Action.NONE, pendingAction != Action.NONE
                    ? "CONTROL_PENDING" : "RETURN_CONFIRMATION", nowMs);
        }
        boolean freshFar = lastSampleAtMs > 0L && (nowMs - lastSampleAtMs) <= SIGNAL_STALE_MS
                && lastDistanceDbm <= lockDbm;
        if (freshFar) {
            return officialDecision(Action.NONE, "FRESH_FAR_SIGNAL", nowMs);
        }
        hasBeenNear = true;
        if (!approachLatched) {
            lastNearAtMs = nowMs;
            lastApproachAtMs = nowMs;
            farSinceMs = 0L;
            resetLockDwell();
        }

        if (driving) {
            approachLatched = true;
            return officialDecision(Action.NONE, "DRIVING_OR_WIRED_BLOCK", nowMs);
        }
        if (!autoUnlock || approachLatched || unlockRequests >= MAX_REQUESTS_PER_ACTION
                || nowMs < controlRetryAfterMs) {
            return officialDecision(Action.NONE, !autoUnlock ? "UNLOCK_DISABLED"
                    : approachLatched ? "APPROACH_LATCHED"
                    : unlockRequests >= MAX_REQUESTS_PER_ACTION ? "UNLOCK_REQUEST_LIMIT"
                    : "CONTROL_RETRY_COOLDOWN", nowMs);
        }
        if (lastUnlockAtMs > 0L && (nowMs - lastUnlockAtMs) < UNLOCK_COOLDOWN_MS) {
            return officialDecision(Action.NONE, "UNLOCK_COOLDOWN", nowMs);
        }
        approachLatched = true;
        lastUnlockAtMs = nowMs;
        pendingAction = Action.UNLOCK;
        unlockRequests++;
        return officialDecision(Action.UNLOCK, "OFFICIAL_READY_UNLOCK", nowMs);
    }

    /**
     * 银河蓝牙断开时记录断开时刻并清空驻留计时。
     */
    synchronized void onOfficialDisconnected(long nowMs) {
        if (officialDisconnectedAtMs == 0L) {
            officialDisconnectedAtMs = nowMs;
        }
        resetLockDwell();
        resetReturnConfirmation();
        earlyApproachPeakDbm = Float.NaN;
    }

    /**
     * 周期性维护状态（每 500ms 调用一次）：
     * 当人离开车辆超出蓝牙距离后，手机往往不再收到任何 BLE 样本（而不是持续收到 -85dBm 样本）。
     * 若银河已断开且连续 12 秒无近场信号，自动释放 approachLatched，重新武装下一次靠近自动解锁。
     *
     * @return 若本次调用重新武装了靠近解锁，返回 true
     */
    synchronized boolean onTick(long nowMs, boolean officialConnected, boolean driving) {
        if (lastSampleAtMs > 0L && (nowMs - lastSampleAtMs) > SIGNAL_STALE_MS) {
            resetLockDwell();
        }
        if (officialConnected) {
            officialDisconnectedAtMs = 0L;
            lastMaintenanceDecision = "OFFICIAL_CONNECTED";
            return false;
        }
        if (driving || (!approachLatched && !hasBeenNear && !returnConfirmationRequired)
                || pendingAction != Action.NONE) {
            lastMaintenanceDecision = driving ? "DRIVING_OR_WIRED_BLOCK"
                    : pendingAction != Action.NONE ? "CONTROL_PENDING" : "NO_ACTIVE_SESSION";
            return false;
        }
        long lastApproachEvidence = Math.max(lastNearAtMs, lastApproachAtMs);
        boolean noRecentNearSignal = lastApproachEvidence == 0L
                || (nowMs - lastApproachEvidence) >= REARM_DISCONNECTED_SILENCE_MS;
        boolean disconnectedLongEnough = (officialDisconnectedAtMs == 0L)
                || ((nowMs - officialDisconnectedAtMs) >= REARM_FAR_MS);
        boolean signalGoneLongEnough = (lastSampleAtMs > 0L)
                && ((nowMs - lastSampleAtMs) >= REARM_DISCONNECTED_SILENCE_MS);
        if (noRecentNearSignal && (disconnectedLongEnough || signalGoneLongEnough)) {
            rearm();
            lastMaintenanceDecision = "REARMED_AFTER_SILENCE";
            return true;
        }
        lastMaintenanceDecision = "WAITING_DEPARTURE_CONFIRMATION";
        return false;
    }

    /**
     * 用户点击“重新学习本车”或更换车辆时重置。
     */
    synchronized void reset() {
        approachLatched = false;
        manualLockLatched = false;
        hasBeenNear = false;
        lastUnlockAtMs = 0L;
        lastSampleAtMs = 0L;
        lastNearAtMs = 0L;
        lastApproachAtMs = 0L;
        officialDisconnectedAtMs = 0L;
        lockInhibitUntilMs = 0L;
        farSinceMs = 0L;
        lastDistanceDbm = Float.NaN;
        pendingAction = Action.NONE;
        unlockRequests = 0;
        lockRequests = 0;
        controlRetryAfterMs = 0L;
        returnConfirmationRequired = false;
        lastSampleDecision = "RESET";
        lastOfficialDecision = "RESET";
        lastMaintenanceDecision = "RESET";
        lastControlFeedback = "NONE";
        lastSampleDecisionAtMs = 0L;
        lastOfficialDecisionAtMs = 0L;
        resetDepartureEvidence();
        resetReturnConfirmation();
        resetLockDwell();
    }

    /** 发送前重新核验样本、方向及开关，禁止旧上锁请求在用户走回车旁后生效。 */
    synchronized boolean isPendingActionValid(Action action, long nowMs, boolean driving,
                                               boolean autoUnlock, boolean autoLock) {
        if (action == Action.NONE || pendingAction != action || driving) return false;
        if (action == Action.UNLOCK) {
            return autoUnlock && !(lastSampleAtMs > 0L && (nowMs - lastSampleAtMs) <= SIGNAL_STALE_MS
                    && lastDistanceDbm <= lockDbm);
        }
        return autoLock && hasBeenNear && hasConfirmedDeparture() && nowMs >= lockInhibitUntilMs
                && lastSampleAtMs > 0L && (nowMs - lastSampleAtMs) <= SIGNAL_STALE_MS
                && lastDistanceDbm <= lockDbm && lockDwellSamples >= LOCK_MIN_SAMPLES
                && lockDwellStartMs > 0L && (nowMs - lockDwellStartMs) >= lockDwellMs
                && (lastNearAtMs == 0L || (nowMs - lastNearAtMs) >= NEAR_GUARD_MS);
    }

    /** 官方通知按钮已成功发送后才结束上锁会话；发送成功不代表已收到车锁执行回执。 */
    synchronized void onControlSent(Action action, long nowMs) {
        if (pendingAction != action || action == Action.NONE) return;
        lastControlFeedback = "FINISHED_" + action;
        pendingAction = Action.NONE;
        controlRetryAfterMs = 0L;
        if (action == Action.UNLOCK) {
            earlyUnlockSentAtMs = nowMs;
            earlyApproachPeakDbm = Float.NaN;
            farSinceMs = 0L;
            resetLockDwell();
        }
        if (action == Action.LOCK) {
            approachLatched = false;
            hasBeenNear = false;
            farSinceMs = 0L;
            lastUnlockAtMs = nowMs;
            resetLockDwell();
            returnConfirmationRequired = true;
            resetReturnConfirmation();
            resetDepartureEvidence();
        }
    }

    /** 同步手动交付结果；手动上锁后保持本次会话锁存，避免人在车旁又被自动解锁。 */
    synchronized void onManualControlSent(Action action, long nowMs) {
        if (action != Action.UNLOCK && action != Action.LOCK) return;
        pendingAction = Action.NONE;
        approachLatched = true;
        manualLockLatched = action == Action.LOCK;
        hasBeenNear = action == Action.UNLOCK;
        lastUnlockAtMs = nowMs;
        lastNearAtMs = 0L;
        lastApproachAtMs = nowMs;
        farSinceMs = 0L;
        unlockRequests = 0;
        lockRequests = 0;
        controlRetryAfterMs = 0L;
        returnConfirmationRequired = false;
        resetDepartureEvidence();
        resetReturnConfirmation();
        resetLockDwell();
        if (action == Action.UNLOCK) earlyUnlockSentAtMs = nowMs;
        lastControlFeedback = "MANUAL_FINISHED_" + action;
    }

    /** 发送失败或条件失效时撤销待发标记，保留近车证据，并限制同一会话的补发次数。 */
    synchronized void onControlFailed(Action action, long nowMs) {
        if (pendingAction != action || action == Action.NONE) return;
        lastControlFeedback = "NOT_SENT_" + action;
        pendingAction = Action.NONE;
        controlRetryAfterMs = nowMs + CONTROL_FAILURE_COOLDOWN_MS;
        if (action == Action.UNLOCK) {
            approachLatched = false;
            lastUnlockAtMs = 0L;
        } else {
            resetLockDwell();
        }
    }

    /** 真正离开并断连后结束旧会话，下一次官方连接仍可快速解锁；回程确认只约束未结束的连接会话。 */
    private void rearm() {
        approachLatched = false;
        manualLockLatched = false;
        hasBeenNear = false;
        farSinceMs = 0L;
        pendingAction = Action.NONE;
        unlockRequests = 0;
        lockRequests = 0;
        controlRetryAfterMs = 0L;
        returnConfirmationRequired = false;
        resetDepartureEvidence();
        resetReturnConfirmation();
        resetLockDwell();
    }

    /** 锁后或失败预算耗尽后，未补偿近场持续一秒才接受真实返回，阈值附近抖动不算新接近。 */
    private boolean confirmReturn(float distanceDbm, float nearGuard, long nowMs, boolean driving) {
        if (driving || distanceDbm < nearGuard) {
            resetReturnConfirmation();
            return false;
        }
        if (returnNearStartMs == 0L) returnNearStartMs = nowMs;
        returnNearSamples++;
        if (returnNearSamples < 2 || (nowMs - returnNearStartMs) < RETURN_NEAR_CONFIRM_MS) return false;
        returnConfirmationRequired = false;
        unlockRequests = 0;
        lockRequests = 0;
        controlRetryAfterMs = 0L;
        farSinceMs = 0L;
        if (!approachLatched) lastUnlockAtMs = 0L;
        resetReturnConfirmation();
        return true;
    }

    /** 样本断档或尚未进入真实近场时清空回程确认。 */
    private void resetReturnConfirmation() {
        returnNearStartMs = 0L;
        returnNearSamples = 0;
    }

    /** 到过真实近场才能按原阈值锁车；提前解锁后直接转身离开，须同源信号持续比峰值弱六dB。 */
    private boolean hasConfirmedDeparture() {
        return departureArmed || (earlyUnlockSentAtMs > 0L && Float.isFinite(earlyApproachPeakDbm)
                && earlyApproachPeakDbm - lastDistanceDbm >= EARLY_DEPARTURE_DROP_DBM);
    }

    /** 会话结束后清除真实近场和提前解锁阶段，不能继承上一轮测距证据。 */
    private void resetDepartureEvidence() {
        departureArmed = false;
        earlyUnlockSentAtMs = 0L;
        earlyApproachPeakDbm = Float.NaN;
        measurementSource = "";
    }

    /** 返回本次靠近是否已经占用解锁机会。 */
    synchronized boolean isApproachLatched() {
        return approachLatched;
    }

    /** 返回是否有允许远离上锁的近车证据。 */
    synchronized boolean hasBeenNear() {
        return hasBeenNear;
    }

    /** 返回最后一个有效样本时间。 */
    synchronized long getLastSampleAtMs() {
        return lastSampleAtMs;
    }

    /** 返回持续远场的起始时间。 */
    synchronized long getFarSinceMs() {
        return farSinceMs;
    }

    /** 返回上锁驻留起点，零表示当前未驻留。 */
    synchronized long getLockDwellStartMs() {
        return lockDwellStartMs;
    }

    /** 返回最后一次近场保护样本的时间。 */
    synchronized long getLastNearAtMs() {
        return lastNearAtMs;
    }

    /** 返回当前连续远场样本数。 */
    synchronized int getLockDwellSamples() {
        return lockDwellSamples;
    }

    /** 返回最近真实执行过的样本分支；只读诊断不重新执行状态机。 */
    synchronized String getLastSampleDecision() {
        return lastSampleDecision;
    }

    /** 返回最近官方就绪事件走过的分支，避免定时tick覆盖这条原因。 */
    synchronized String getLastOfficialDecision() {
        return lastOfficialDecision;
    }

    /** 一次性读取判定原因、预算和各保护窗口，调用不会修改车控状态。 */
    synchronized String diagnosticSnapshot(long nowMs) {
        float nearGuard = Math.max(lockDbm + 3f, Math.min(unlockDbm - 3f, NEAR_GUARD_DBM));
        long dwellElapsed = lockDwellStartMs == 0L ? 0L : Math.max(0L, nowMs - lockDwellStartMs);
        return String.format(Locale.ROOT,
                "sample_reason=%s sample_reason_age_ms=%d official_reason=%s official_reason_age_ms=%d "
                        + "maintenance=%s feedback=%s distance=%.1f unlock_dbm=%.1f lock_dbm=%.1f near_guard_dbm=%.1f "
                        + "dwell_ms=%d/%d dwell_samples=%d near_guard_left_ms=%d inhibit_left_ms=%d "
                        + "unlock_cooldown_left_ms=%d retry_left_ms=%d unlock_requests=%d/%d lock_requests=%d/%d "
                        + "return_required=%s return_ms=%d return_samples=%d near_age_ms=%d approach_age_ms=%d "
                        + "far_ms=%d disconnected_ms=%d has_near=%s latched=%s manual_lock_latched=%s logic_pending=%s "
                        + "departure_armed=%s early_unlock_age_ms=%d early_peak_dbm=%.1f departure_drop_dbm=%.1f rssi_source=%s",
                lastSampleDecision, age(nowMs, lastSampleDecisionAtMs),
                lastOfficialDecision, age(nowMs, lastOfficialDecisionAtMs), lastMaintenanceDecision,
                lastControlFeedback, lastDistanceDbm, unlockDbm, lockDbm, nearGuard,
                dwellElapsed, lockDwellMs, lockDwellSamples,
                lastNearAtMs == 0L ? 0L : Math.max(0L, NEAR_GUARD_MS - (nowMs - lastNearAtMs)),
                Math.max(0L, lockInhibitUntilMs - nowMs),
                lastUnlockAtMs == 0L ? 0L : Math.max(0L, UNLOCK_COOLDOWN_MS - (nowMs - lastUnlockAtMs)),
                Math.max(0L, controlRetryAfterMs - nowMs), unlockRequests, MAX_REQUESTS_PER_ACTION,
                lockRequests, MAX_REQUESTS_PER_ACTION, returnConfirmationRequired,
                returnNearStartMs == 0L ? 0L : Math.max(0L, nowMs - returnNearStartMs), returnNearSamples,
                age(nowMs, lastNearAtMs), age(nowMs, lastApproachAtMs),
                farSinceMs == 0L ? 0L : Math.max(0L, nowMs - farSinceMs),
                age(nowMs, officialDisconnectedAtMs), hasBeenNear, approachLatched, manualLockLatched, pendingAction,
                departureArmed, age(nowMs, earlyUnlockSentAtMs), earlyApproachPeakDbm,
                earlyApproachPeakDbm - lastDistanceDbm, measurementSource);
    }

    /** 仅标记当前样本真实返回的结果和分支，不改变原返回动作。 */
    private Action sampleDecision(Action action, String reason, long nowMs) {
        lastSampleDecision = reason;
        lastSampleDecisionAtMs = nowMs;
        return action;
    }

    /** 仅标记官方事件真实返回的结果和分支，不改变原返回动作。 */
    private Action officialDecision(Action action, String reason, long nowMs) {
        lastOfficialDecision = reason;
        lastOfficialDecisionAtMs = nowMs;
        return action;
    }

    /** 未观测到的时间统一输出负一，避免被误认为零毫秒之前刚发生。 */
    private static long age(long nowMs, long timestampMs) {
        return timestampMs == 0L ? -1L : Math.max(0L, nowMs - timestampMs);
    }

    /** 清空驻留证据，禁止跨中断累计样本。 */
    private void resetLockDwell() {
        lockDwellStartMs = 0L;
        lockDwellSamples = 0;
    }
}
