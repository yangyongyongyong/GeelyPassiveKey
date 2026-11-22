package com.luca.geelykeyradar;

/** 为异步通知解析提供版本号，移除和新通知会使旧解析任务失效。 */
final class NotificationEpoch {
    private long generation = 0L;
    private String latestKey = "";
    private long latestPostTime = -1L;
    private boolean removed = false;

    /** 接受较新的通知快照，拒绝移除后再次到达的旧快照。 */
    synchronized long begin(String key, long postTime) {
        if (latestKey.equals(key)
                && (postTime < latestPostTime || (removed && postTime <= latestPostTime))) return -1L;
        latestKey = key;
        latestPostTime = postTime;
        removed = false;
        return ++generation;
    }

    /** 移除只针对同一条且不早于当前快照的通知，迟到的旧移除不影响新快照。 */
    synchronized long remove(String key, long postTime) {
        if (!latestKey.isEmpty() && (!latestKey.equals(key) || postTime < latestPostTime)) return -1L;
        latestKey = key;
        latestPostTime = postTime;
        removed = true;
        return ++generation;
    }

    /** 监听断线时撤销所有在途解析，重绑后允许读取同一条仍然存在的通知。 */
    synchronized long invalidate() {
        latestKey = "";
        latestPostTime = -1L;
        removed = false;
        return ++generation;
    }

    /** 清理状态时撤销已排队的解析，同时保留移除记录，旧快照不能重新提交。 */
    synchronized void cancelPending() {
        generation++;
    }

    /** 当前活动通知列表为空时标记已移除，拦截此前排队的旧快照。 */
    synchronized void removeCurrent() {
        removed = true;
        generation++;
    }

    /** 只有最新有效版本可以提交解析或清理结果。 */
    synchronized boolean isCurrent(long token) {
        return token >= 0L && token == generation;
    }

    /** 当前通知的墙钟发布时间，仅用于计算 token 与通知快照年龄。 */
    synchronized long getLatestPostTime() {
        return latestPostTime;
    }

    /** 只读观测版本和移除状态，不暴露原始通知key。 */
    synchronized String diagnosticSnapshot() {
        return "notification_epoch=" + generation + " notification_post_ms=" + latestPostTime
                + " notification_removed=" + removed;
    }
}
