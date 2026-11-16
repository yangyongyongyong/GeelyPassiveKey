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
        ThresholdSettingsForm.Result defaults = ThresholdSettingsForm.validate("-75", "-85", "2", "-95");
        check(defaults.isValid() && defaults.unlockDbm == -75 && defaults.lockDbm == -85
                && defaults.dwellMs == 2000 && defaults.connectDbm == -95,
                "默认值与当前实车参数一致");
        ThresholdSettingsForm.Result positive = ThresholdSettingsForm.validate("75", "85", "2", "95");
        check(positive.isValid() && positive.unlockDbm == -75 && positive.lockDbm == -85
                && positive.connectDbm == -95 && positive.normalizedPositiveSignal, "输入正数仍自动转为负dBm");
        check(positive.summary().contains("-75") && positive.summary().contains("-85")
                && positive.summary().contains("-95"), "预览显示实际负值");
        check(ThresholdSettingsForm.validate(" −79 ", "－85", "2", " −95 ").isValid(), "支持粘贴常见减号和空白");
        String defaultsHelp = ThresholdSettingsForm.defaultsSummary();
        check(defaultsHelp.contains("-95") && defaultsHelp.contains("-75")
                && defaultsHelp.contains("-85") && defaultsHelp.contains("2"),
                "App内默认值辅助说明必须包含四项当前默认参数");
    }

    /** 输入范围与现有服务一致，保存前指出越界和不足6dB缓冲，不静默截断用户数字。 */
    private static void boundsAndHysteresis() {
        check(ThresholdSettingsForm.validate("-90", "-98", "1.5", "-115").isValid(), "支持最弱信号及最短时长边界");
        check(ThresholdSettingsForm.validate("-40", "-46", "20", "-45").isValid(), "支持最强信号及最长时长边界");
        check(!ThresholdSettingsForm.validate("-91", "-98", "4.5", "-95").unlockError.isEmpty(), "解锁越界标在解锁字段");
        check(!ThresholdSettingsForm.validate("-79", "-99", "4.5", "-95").lockError.isEmpty(), "上锁越界标在上锁字段");
        ThresholdSettingsForm.Result gap = ThresholdSettingsForm.validate("-79", "-80", "4.5", "-95");
        check(!gap.isValid() && gap.lockError.contains("-85") && gap.unlockError.isEmpty(), "提示当前上锁至少需要-85");
        ThresholdSettingsForm.Result connectGap = ThresholdSettingsForm.validate("-79", "-85", "2", "-80");
        check(!connectGap.isValid() && connectGap.connectError.contains("-84"), "连接触发至少比解锁低5dB");
        check(!ThresholdSettingsForm.validate("-79", "-85", "1.49", "-95").dwellError.isEmpty(), "不接受低于1.5秒");
        check(!ThresholdSettingsForm.validate("-79", "-85", "20.01", "-95").dwellError.isEmpty(), "不接受高于20秒");
    }

    /** 每项错误可同时展示，空值、非数值和无穷值不会保存或误转成合法零值。 */
    private static void invalidInputsKeepSpecificErrors() {
        ThresholdSettingsForm.Result empty = ThresholdSettingsForm.validate("", null, " ", "");
        check(!empty.isValid() && !empty.connectError.isEmpty() && !empty.unlockError.isEmpty()
                && !empty.lockError.isEmpty() && !empty.dwellError.isEmpty(), "四个空字段分别反馈原因");
        for (String invalid : new String[]{"abc", "NaN", "Infinity", "-Infinity"}) {
            ThresholdSettingsForm.Result result = ThresholdSettingsForm.validate(invalid, invalid, invalid, invalid);
            check(!result.isValid() && !result.connectError.isEmpty() && !result.unlockError.isEmpty()
                    && !result.lockError.isEmpty() && !result.dwellError.isEmpty(), "非有限数字不应通过校验");
        }
        check(!ThresholdSettingsForm.validate("0", "-85", "2", "-95").isValid(), "零dBm不被兼容为负阈值");
        check(!ThresholdSettingsForm.validate("-79", "-85", "-2", "-95").isValid(), "时长不沿用信号取负兼容");
    }

    /** 原有小数阈值和毫秒时长打开/保存不丢精度，避免旧整数/一位小数展示意外改配置。 */
    private static void fractionalSettingsDoNotLosePrecision() {
        check(ThresholdSettingsForm.number(-75.25f).equals("-75.25"), "已有小数阈值完整显示");
        check(ThresholdSettingsForm.seconds(4567).equals("4.567"), "已有毫秒精度时长完整显示");
        ThresholdSettingsForm.Result result = ThresholdSettingsForm.validate("-79.25", "-85.5", "2.567", "-95.5");
        check(result.isValid() && result.unlockDbm == -79.25f && result.lockDbm == -85.5f
                && result.dwellMs == 2567 && result.connectDbm == -95.5f, "格式化后保存仍是原值");
        check(ThresholdSettingsForm.validate("-79", "-85", "2.5678", "-95").dwellMs == 2568,
                "只在毫秒精度换算时四舍五入");
        String summary = ThresholdSettingsForm.configuredSummary(-79.25f, -85.5f, 2567, -95.5f);
        check(summary.contains("-79.25") && summary.contains("2.567") && summary.contains("-95.5")
                && summary.contains("\n") && !summary.contains("米"), "主页摘要显示真实精度，不伪造固定距离");
    }

    /** 断言失败时标明对应字段的设置语义。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
