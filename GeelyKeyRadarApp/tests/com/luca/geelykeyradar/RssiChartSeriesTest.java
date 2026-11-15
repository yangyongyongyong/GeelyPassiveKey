package com.luca.geelykeyradar;

import java.util.List;

/** 实时RSSI曲线回归：范围、断点、60秒窗口和固定容量。 */
public final class RssiChartSeriesTest {
    /** 执行样本范围、无信号断点、窗口裁剪与容量上限检查。 */
    public static void main(String[] args) {
        rangeAndGap();
        pruneAndCapacity();
        System.out.println("RssiChartSeriesTest passed");
    }

    /** 原始值参与范围统计，断点不参与最小/最大值且保留为无效样本。 */
    private static void rangeAndGap() {
        RssiChartSeries series = new RssiChartSeries();
        series.add(1000L, -70, -72.5f);
        series.add(1500L, -80, -78.0f);
        check(series.validCount(1500L) == 2, "两条有效样本");
        check(series.minDbm(1500L) == -80 && series.maxDbm(1500L) == -70, "原始范围统计");
        series.addGap(2000L);
        List<RssiChartSeries.Sample> samples = series.snapshot(2000L);
        check(samples.size() == 3 && !samples.get(2).valid && !samples.get(2).hasSmooth(), "断点不是-127真实信号");
        check(series.validCount(2000L) == 2 && series.minDbm(2000L) == -80, "断点不参与范围统计");
    }

    /** 60秒前样本被裁剪，长时间高频刷新仍受160点硬上限约束。 */
    private static void pruneAndCapacity() {
        RssiChartSeries series = new RssiChartSeries();
        series.add(1000L, -60, -61f);
        series.add(62001L, -70, -71f);
        List<RssiChartSeries.Sample> pruned = series.snapshot(62001L);
        check(pruned.size() == 1 && pruned.get(0).rawDbm == -70, "60秒前样本裁剪");
        for (int index = 0; index < 200; index++) {
            series.add(63000L + index * 200L, -60 - (index % 30), -65f);
        }
        check(series.snapshot(103000L).size() <= RssiChartSeries.MAX_SAMPLES, "曲线样本容量固定");
    }

    /** 断言失败指出具体曲线或采样边界。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
