package com.luca.geelykeyradar;

import java.math.BigDecimal;
import java.util.Locale;

/** 触发参数表单的纯Java解析与校验，不修改车控算法或既有配置。 */
final class ThresholdSettingsForm {
    static final float MIN_CONNECT_DBM = -115.0f;
    static final float MAX_CONNECT_DBM = -45.0f;
    static final float MIN_UNLOCK_DBM = -90.0f;
    static final float MAX_UNLOCK_DBM = -40.0f;
    static final float MIN_LOCK_DBM = -98.0f;
    static final float MAX_LOCK_DBM = -46.0f;
    static final float MIN_CONNECT_GAP_DBM = 5.0f;
    static final float MIN_GAP_DBM = 6.0f;
    static final double MIN_DWELL_SECONDS = 1.5;
    static final double MAX_DWELL_SECONDS = 20.0;

    static final class Result {
        final float connectDbm;
        final float unlockDbm;
        final float lockDbm;
        final long dwellMs;
        final String connectError;
        final String unlockError;
        final String lockError;
        final String dwellError;
        final boolean normalizedPositiveSignal;

        /** 同时保存四项错误，让弹窗逐字段反馈而不是保存部分配置。 */
        Result(float connectDbm, float unlockDbm, float lockDbm, long dwellMs,
               String connectError, String unlockError, String lockError, String dwellError,
               boolean normalizedPositiveSignal) {
            this.connectDbm = connectDbm;
            this.unlockDbm = unlockDbm;
            this.lockDbm = lockDbm;
            this.dwellMs = dwellMs;
            this.connectError = connectError;
            this.unlockError = unlockError;
            this.lockError = lockError;
            this.dwellError = dwellError;
            this.normalizedPositiveSignal = normalizedPositiveSignal;
        }

        /** 四项输入和缓冲关系全部合法后才允许保存。 */
        boolean isValid() {
            return connectError.isEmpty() && unlockError.isEmpty()
                    && lockError.isEmpty() && dwellError.isEmpty();
        }

        /** 输出真正将保存的负dBm与毫秒换算时长。 */
        String summary() {
            if (!isValid()) return "请填写四项设置，检查无误后再保存";
            return String.format(Locale.ROOT, "连接触发 ≥ %s dBm · 靠近解锁 ≥ %s dBm\n离开上锁 ≤ %s dBm · 连续弱信号 %s 秒",
                    number(connectDbm), number(unlockDbm), number(lockDbm), seconds(dwellMs));
        }
    }

    /** 兼容旧调用：未提供连接阈值时使用项目默认值。 */
    static Result validate(String unlockText, String lockText, String dwellText) {
        return validate(unlockText, lockText, dwellText, number(PassiveKeyDefaults.CONNECT_DBM));
    }

    /** 按现有服务支持的范围校验四项设置；越界明确提示，不偷偷截断。 */
    static Result validate(String unlockText, String lockText, String dwellText, String connectText) {
        double rawUnlock = parse(unlockText);
        double rawLock = parse(lockText);
        double dwellSeconds = parse(dwellText);
        double rawConnect = parse(connectText);
        double unlock = rawUnlock > 0.0 ? -rawUnlock : rawUnlock;
        double lock = rawLock > 0.0 ? -rawLock : rawLock;
        double connect = rawConnect > 0.0 ? -rawConnect : rawConnect;
        String connectError = "";
        String unlockError = "";
        String lockError = "";
        String dwellError = "";
        if (!Double.isFinite(unlock)) {
            unlockError = "请输入靠近信号，例如 -79";
        } else if (unlock < MIN_UNLOCK_DBM || unlock > MAX_UNLOCK_DBM) {
            unlockError = "靠近信号范围为 -90 至 -40 dBm";
        }
        if (!Double.isFinite(lock)) {
            lockError = "请输入离开信号，例如 -85";
        } else if (lock < MIN_LOCK_DBM || lock > MAX_LOCK_DBM) {
            lockError = "离开信号范围为 -98 至 -46 dBm";
        } else if (unlockError.isEmpty() && lock > unlock - MIN_GAP_DBM) {
            lockError = "需比解锁信号低至少 6 dB；当前请填 "
                    + number((float) (unlock - MIN_GAP_DBM)) + " 或更负的值";
        }
        if (!Double.isFinite(dwellSeconds)) {
            dwellError = "请输入连续确认秒数，例如 2";
        } else if (dwellSeconds < MIN_DWELL_SECONDS || dwellSeconds > MAX_DWELL_SECONDS) {
            dwellError = "确认时长范围为 1.5 至 20 秒";
        }
        if (!Double.isFinite(connect)) {
            connectError = "请输入蓝牙连接触发信号，例如 -95";
        } else if (connect < MIN_CONNECT_DBM || connect > MAX_CONNECT_DBM) {
            connectError = "连接触发范围为 -115 至 -45 dBm";
        } else if (unlockError.isEmpty() && connect > unlock - MIN_CONNECT_GAP_DBM) {
            connectError = "应至少比解锁信号低 5 dB；当前请填 "
                    + number((float) (unlock - MIN_CONNECT_GAP_DBM)) + " 或更负的值";
        }
        return new Result((float) connect, (float) unlock, (float) lock,
                dwellError.isEmpty() ? Math.round(dwellSeconds * 1000.0) : 0L,
                connectError, unlockError, lockError, dwellError,
                rawConnect > 0.0 || rawUnlock > 0.0 || rawLock > 0.0);
    }

    /** 支持数字键盘与常见中文/数学减号，空值、NaN和无穷值统一归为无效输入。 */
    private static double parse(String value) {
        if (value == null || value.trim().isEmpty()) return Double.NaN;
        try {
            return Double.parseDouble(value.trim().replace('−', '-').replace('－', '-'));
        } catch (NumberFormatException invalid) {
            return Double.NaN;
        }
    }

    /** 主页分行展示实际配置，不附固定米数，不丢失小数或毫秒精度。 */
    static String configuredSummary(float unlockDbm, float lockDbm, long dwellMs, float connectDbm) {
        return "连接触发 ≥ " + number(connectDbm) + " dBm\n靠近解锁 ≥ " + number(unlockDbm)
                + " dBm\n离开上锁 ≤ " + number(lockDbm)
                + " dBm · 连续 " + seconds(dwellMs) + " 秒";
    }

    /** 兼容尚未持有连接阈值的旧调用。 */
    static String configuredSummary(float unlockDbm, float lockDbm, long dwellMs) {
        return configuredSummary(unlockDbm, lockDbm, dwellMs, PassiveKeyDefaults.CONNECT_DBM);
    }

    /** 只作为App内辅助提示；填充后仍须用户明确保存，不能自动覆盖已有配置。 */
    static String defaultsSummary() {
        return "连接触发 ≥ " + number(PassiveKeyDefaults.CONNECT_DBM)
                + " dBm\n靠近解锁 ≥ " + number(PassiveKeyDefaults.UNLOCK_DBM)
                + " dBm\n离开上锁 ≤ " + number(PassiveKeyDefaults.LOCK_DBM)
                + " dBm · 连续 " + seconds(PassiveKeyDefaults.LOCK_DWELL_MS) + " 秒";
    }

    /** 显示完整已有阈值，避免旧弹窗使用整数格式而丢掉用户的小数配置。 */
    static String number(float value) {
        return Float.isFinite(value) ? new BigDecimal(Float.toString(value)).stripTrailingZeros().toPlainString() : "";
    }

    /** 毫秒按小数秒精确展示，已有1毫秒精度配置不会因打开再保存被四舍五入到0.1秒。 */
    static String seconds(long milliseconds) {
        return BigDecimal.valueOf(milliseconds, 3).stripTrailingZeros().toPlainString();
    }
}
