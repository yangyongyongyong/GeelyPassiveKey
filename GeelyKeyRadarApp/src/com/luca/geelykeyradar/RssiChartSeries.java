package com.luca.geelykeyradar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/** 保留最近60秒RSSI样本的纯Java环形序列，供界面曲线和回归测试共用。 */
final class RssiChartSeries {
    static final long WINDOW_MS = 60000L;
    static final int MAX_SAMPLES = 160;

    /** 单个曲线点同时保留原始值、平滑值和有效性，空档不会伪装成-127dBm。 */
    static final class Sample {
        final long timeMs;
        final int rawDbm;
        final float smoothDbm;
        final boolean valid;

        /** 保存单调时间和两条曲线值；原始值越界时整点标记为断档。 */
        Sample(long timeMs, int rawDbm, float smoothDbm) {
            this.timeMs = timeMs;
            this.rawDbm = rawDbm;
            this.smoothDbm = smoothDbm;
            this.valid = rawDbm > -105 && rawDbm < 0;
        }

        /** 平滑值只有有限且位于可显示范围时才画第二层曲线。 */
        boolean hasSmooth() {
            return valid && Float.isFinite(smoothDbm) && smoothDbm > -105f && smoothDbm < 0f;
        }
    }

    private final ArrayDeque<Sample> samples = new ArrayDeque<>();

    /** 添加一条实时样本；同一毫秒重复刷新只替换末点，不制造虚假高频数据。 */
    synchronized void add(long nowMs, int rawDbm, float smoothDbm) {
        prune(nowMs);
        if (!samples.isEmpty() && samples.peekLast().timeMs == nowMs) samples.removeLast();
        samples.addLast(new Sample(nowMs, rawDbm, smoothDbm));
        while (samples.size() > MAX_SAMPLES) samples.removeFirst();
    }

    /** 无服务或无新鲜信号时添加快照断点，曲线中间保持空白而不是拉一条假线。 */
    synchronized void addGap(long nowMs) {
        prune(nowMs);
        if (!samples.isEmpty() && samples.peekLast().timeMs == nowMs) samples.removeLast();
        samples.addLast(new Sample(nowMs, -127, Float.NaN));
        while (samples.size() > MAX_SAMPLES) samples.removeFirst();
    }

    /** 返回按时间升序的只读副本，绘图线程不会修改内部环形队列。 */
    synchronized List<Sample> snapshot(long nowMs) {
        prune(nowMs);
        return new ArrayList<>(samples);
    }

    /** 返回窗口内有效原始样本数。 */
    synchronized int validCount(long nowMs) {
        int count = 0;
        for (Sample sample : snapshot(nowMs)) if (sample.valid) count++;
        return count;
    }

    /** 返回窗口内原始RSSI最小值；没有有效样本时返回0。 */
    synchronized int minDbm(long nowMs) {
        int result = Integer.MAX_VALUE;
        for (Sample sample : snapshot(nowMs)) if (sample.valid) result = Math.min(result, sample.rawDbm);
        return result == Integer.MAX_VALUE ? 0 : result;
    }

    /** 返回窗口内原始RSSI最大值；没有有效样本时返回0。 */
    synchronized int maxDbm(long nowMs) {
        int result = Integer.MIN_VALUE;
        for (Sample sample : snapshot(nowMs)) if (sample.valid) result = Math.max(result, sample.rawDbm);
        return result == Integer.MIN_VALUE ? 0 : result;
    }

    /** 清空曲线，仅用于服务重置或页面明确重置。 */
    synchronized void clear() {
        samples.clear();
    }

    /** 删除60秒前和过多的样本，保证长时间前台页面内存固定。 */
    private void prune(long nowMs) {
        while (!samples.isEmpty() && nowMs - samples.peekFirst().timeMs > WINDOW_MS) samples.removeFirst();
        while (samples.size() > MAX_SAMPLES) samples.removeFirst();
    }
}
