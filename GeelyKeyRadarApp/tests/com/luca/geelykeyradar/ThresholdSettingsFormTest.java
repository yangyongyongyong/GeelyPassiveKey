package com.luca.geelykeyradar;

/** 设置弹窗的字段解析回归，不调用蓝牙、车控或Android服务。 */
public final class ThresholdSettingsFormTest {
    /** 验证默认值、正数兼容、小数精度、边界、缓冲关系和无效输入。 */
    public static void main(String[] args) {
        defaultsAndPositiveCompatibility();
        boundsAndHysteresis();
        invalidInputsKeepSpecificErrors();
        fractionalSettingsDoNotLosePrecision();
        System.out.println("ThresholdSettingsFormTest passed");
    }

    /** 旧版正数自动取负兼容仍在，预览与保存值使用同一个校验结果。 */
    private static void defaultsAndPositiveCompatibility() {
        ThresholdSettingsForm.Result defaults = ThresholdSettingsForm.validate("-75", "-82", "4.5");
        check(defaults.isValid() && defaults.unlockDbm == -75 && defaults.lockDbm == -82 && defaults.dwellMs == 4500,
                "推荐值与旧版默认参数完全一致");
        ThresholdSettingsForm.Result positive = ThresholdSettingsForm.validate("75", "82", "4.5");
        check(positive.isValid() && positive.unlockDbm == -75 && positive.lockDbm == -82
                && positive.normalizedPositiveSignal, "输入正数仍自动转为负dBm");
        check(positive.summary().contains("-75") && positive.summary().contains("-82"), "预览显示实际负值");
        check(ThresholdSettingsForm.validate(" −75 ", "－82", "4.5").isValid(), "支持粘贴常见减号和空白");
    }

    /** 输入范围与现有服务一致，保存前指出越界和不足6dB缓冲，不静默截断用户数字。 */
    private static void boundsAndHysteresis() {
        check(ThresholdSettingsForm.validate("-90", "-98", "1.5").isValid(), "支持最弱信号及最短时长边界");
        check(ThresholdSettingsForm.validate("-40", "-46", "20").isValid(), "支持最强信号及最长时长边界");
        check(!ThresholdSettingsForm.validate("-91", "-98", "4.5").unlockError.isEmpty(), "解锁越界标在解锁字段");
        check(!ThresholdSettingsForm.validate("-75", "-99", "4.5").lockError.isEmpty(), "上锁越界标在上锁字段");
        ThresholdSettingsForm.Result gap = ThresholdSettingsForm.validate("-75", "-80", "4.5");
        check(!gap.isValid() && gap.lockError.contains("-81") && gap.unlockError.isEmpty(), "提示当前上锁至少需要-81");
        check(!ThresholdSettingsForm.validate("-75", "-82", "1.49").dwellError.isEmpty(), "不接受低于1.5秒");
        check(!ThresholdSettingsForm.validate("-75", "-82", "20.01").dwellError.isEmpty(), "不接受高于20秒");
    }

    /** 每项错误可同时展示，空值、非数值和无穷值不会保存或误转成合法零值。 */
    private static void invalidInputsKeepSpecificErrors() {
        ThresholdSettingsForm.Result empty = ThresholdSettingsForm.validate("", null, " ");
        check(!empty.isValid() && !empty.unlockError.isEmpty() && !empty.lockError.isEmpty()
                && !empty.dwellError.isEmpty(), "三个空字段分别反馈原因");
        for (String invalid : new String[]{"abc", "NaN", "Infinity", "-Infinity"}) {
            ThresholdSettingsForm.Result result = ThresholdSettingsForm.validate(invalid, invalid, invalid);
            check(!result.isValid() && !result.unlockError.isEmpty() && !result.lockError.isEmpty()
                    && !result.dwellError.isEmpty(), "非有限数字不应通过校验");
        }
        check(!ThresholdSettingsForm.validate("0", "-82", "4.5").isValid(), "零dBm不被兼容为负阈值");
        check(!ThresholdSettingsForm.validate("-75", "-82", "-4.5").isValid(), "时长不沿用信号取负兼容");
    }

    /** 原有小数阈值和毫秒时长打开/保存不丢精度，避免旧整数/一位小数展示意外改配置。 */
    private static void fractionalSettingsDoNotLosePrecision() {
        check(ThresholdSettingsForm.number(-75.25f).equals("-75.25"), "已有小数阈值完整显示");
        check(ThresholdSettingsForm.seconds(4567).equals("4.567"), "已有毫秒精度时长完整显示");
        ThresholdSettingsForm.Result result = ThresholdSettingsForm.validate("-75.25", "-82.5", "4.567");
        check(result.isValid() && result.unlockDbm == -75.25f && result.lockDbm == -82.5f && result.dwellMs == 4567,
                "格式化后保存仍是原值");
        check(ThresholdSettingsForm.validate("-75", "-82", "4.5678").dwellMs == 4568, "只在毫秒精度换算时四舍五入");
        String summary = ThresholdSettingsForm.configuredSummary(-75.25f, -82.5f, 4567);
        check(summary.contains("-75.25") && summary.contains("4.567") && summary.contains("\n")
                && !summary.contains("米"), "主页摘要显示真实精度，不伪造固定距离");
    }

    /** 断言失败时标明对应字段的设置语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
