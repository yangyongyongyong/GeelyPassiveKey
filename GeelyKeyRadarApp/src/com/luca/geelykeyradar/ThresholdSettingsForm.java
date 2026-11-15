package com.luca.geelykeyradar;

import java.math.BigDecimal;
import java.util.Locale;

/** 阈值表单的纯Java解析与校验，只拦截无效输入，不修改车控算法或既有配置。 */
final class ThresholdSettingsForm {
    static final float MIN_UNLOCK_DBM = -90.0f;
    static final float MAX_UNLOCK_DBM = -40.0f;
    static final float MIN_LOCK_DBM = -98.0f;
    static final float MAX_LOCK_DBM = -46.0f;
    static final float MIN_GAP_DBM = 6.0f;
    static final double MIN_DWELL_SECONDS = 1.5;
    static final double MAX_DWELL_SECONDS = 20.0;

    static final class Result {
        final float unlockDbm;
        final float lockDbm;
        final long dwellMs;
        final String unlockError;
        final String lockError;
        final String dwellError;
        final boolean normalizedPositiveSignal;

        /** 同时保存三项错误，让弹窗逐字段反馈而不是自动关闭后只弹Toast。 */
        Result(float unlockDbm, float lockDbm, long dwellMs, String unlockError,
               String lockError, String dwellError, boolean normalizedPositiveSignal) {
            this.unlockDbm = unlockDbm;
            this.lockDbm = lockDbm;
            this.dwellMs = dwellMs;
            this.unlockError = unlockError;
            this.lockError = lockError;
            this.dwellError = dwellError;
            this.normalizedPositiveSignal = normalizedPositiveSignal;
        }

        /** 三项输入和解锁/上锁之间的缓冲关系全部合法后才允许保存。 */
        boolean isValid() {
            return unlockError.isEmpty() && lockError.isEmpty() && dwellError.isEmpty();
        }

        /** 输出真正将保存的负dBm与毫秒换算时长，正数兼容不会令预览和保存值不同。 */
        String summary() {
            if (!isValid()) return "请填写三项设置，检查无误后再保存";
            return String.format(Locale.ROOT, "解锁 ≥ %s · 上锁 ≤ %s dBm\n连续弱信号 %s 秒后确认",
                    number(unlockDbm), number(lockDbm), seconds(dwellMs));
        }
    }

    /** 按现有服务支持的范围校验；越界明确提示，不偷偷截断或在未绑定服务时保存不同值。 */
    static Result validate(String unlockText, String lockText, String dwellText) {
        double rawUnlock = parse(unlockText);
        double rawLock = parse(lockText);
        double dwellSeconds = parse(dwellText);
        // 兼容用户输入正数 70 自动转为 -70，界面预览也同步显示实际负dBm。
        double unlock = rawUnlock > 0.0 ? -rawUnlock : rawUnlock;
        double lock = rawLock > 0.0 ? -rawLock : rawLock;
        String unlockError = "";
        String lockError = "";
        String dwellError = "";
        if (!Double.isFinite(unlock)) {
            unlockError = "请输入靠近信号，例如 -75";
        } else if (unlock < MIN_UNLOCK_DBM || unlock > MAX_UNLOCK_DBM) {
            unlockError = "靠近信号范围为 -90 至 -40 dBm";
        }
        if (!Double.isFinite(lock)) {
            lockError = "请输入离开信号，例如 -82";
        } else if (lock < MIN_LOCK_DBM || lock > MAX_LOCK_DBM) {
            lockError = "离开信号范围为 -98 至 -46 dBm";
        } else if (unlockError.isEmpty() && lock > unlock - MIN_GAP_DBM) {
            lockError = "需比解锁信号低至少 6 dB；当前请填 "
                    + number((float) (unlock - MIN_GAP_DBM)) + " 或更负的值";
        }
        if (!Double.isFinite(dwellSeconds)) {
            dwellError = "请输入连续确认秒数，例如 4.5";
        } else if (dwellSeconds < MIN_DWELL_SECONDS || dwellSeconds > MAX_DWELL_SECONDS) {
            dwellError = "确认时长范围为 1.5 至 20 秒";
        }
        return new Result((float) unlock, (float) lock,
                dwellError.isEmpty() ? Math.round(dwellSeconds * 1000.0) : 0L,
                unlockError, lockError, dwellError, rawUnlock > 0.0 || rawLock > 0.0);
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
    static String configuredSummary(float unlockDbm, float lockDbm, long dwellMs) {
        return "靠近解锁 ≥ " + number(unlockDbm) + " dBm\n离开上锁 ≤ " + number(lockDbm)
                + " dBm · 连续 " + seconds(dwellMs) + " 秒";
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
