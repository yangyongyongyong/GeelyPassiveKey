package com.luca.geelykeyradar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/** 记录短生命周期的实时行为轨迹，避免页面500ms轮询错过发现、唤醒和鉴权阶段。 */
final class LiveActivityTrace {
    static final int MAX_STEPS = 8;
    static final long SUMMARY_VISIBLE_MS = 30000L;

    enum Step {
        SCAN_FOUND("发现", "发现本车蓝牙广播"),
        WAKE_REQUEST("唤醒", "已请求银河连接蓝牙钥匙"),
        ACL_CONNECTED("链路", "车机蓝牙链路已建立"),
        OFFICIAL_CONNECTED("鉴权", "银河蓝牙钥匙已连接"),
        UNLOCK_REQUESTED("解锁", "已确认靠近，准备发送解锁"),
        LOCK_REQUESTED("上锁", "已确认离开，准备发送上锁"),
        UNLOCK_SENT("已发送", "解锁指令已交给银河"),
        LOCK_SENT("已发送", "上锁指令已交给银河"),
        LOCK_CONFIRMING("确认", "上锁已交付，等待车辆确认"),
        LOCK_CONFIRMED("已确认", "车辆已确认上锁"),
        RESULT_UNKNOWN("待核实", "车控发送结果不确定"),
        SCAN_LOST("断开", "车辆蓝牙信号已丢失");

        final String badge;
        final String label;

        Step(String badge, String label) {
            this.badge = badge;
            this.label = label;
        }
    }

    static final class Entry {
        final Step step;
        final long atMs;
        final String detail;

        Entry(Step step, long atMs, String detail) {
            this.step = step;
            this.atMs = atMs;
            this.detail = detail == null ? "" : detail;
        }
    }

    private final ArrayDeque<Entry> entries = new ArrayDeque<>();

    /** 只追加新事件，不做内容去重；同一阶段可以留下真实重试或重新连接的时间。 */
    synchronized void add(Step step, long nowMs, String detail) {
        if (step == null) return;
        entries.addLast(new Entry(step, nowMs, detail));
        while (entries.size() > MAX_STEPS) entries.removeFirst();
    }

    /** 只清除新会话前的旧轨迹，不改变车控状态或日志。 */
    synchronized void clear() {
        entries.clear();
    }

    /** 返回该阶段最近一次发生时间，不存在时为零。 */
    synchronized long lastAt(Step step) {
        long latest = 0L;
        for (Entry entry : entries) if (entry.step == step) latest = entry.atMs;
        return latest;
    }

    /** 复制最近步骤，供界面渲染和纯逻辑测试使用。 */
    synchronized List<Entry> snapshot() {
        return new ArrayList<>(entries);
    }

    /** 判断是否仍属于短阶段，超过30秒后回到稳定状态，避免把旧动作当成当前行为。 */
    synchronized boolean hasRecent(Step step, long nowMs) {
        long at = lastAt(step);
        return at > 0L && nowMs >= at && nowMs - at <= SUMMARY_VISIBLE_MS;
    }
}
