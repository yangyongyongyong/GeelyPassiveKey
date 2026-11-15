package com.luca.geelykeyradar;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 动作快照的时间窗口和文件命名纯逻辑，不依赖 Android。 */
final class DiagnosticSnapshotWindow {
    static final long WINDOW_MS = 3 * 60 * 1000L;
    static final int MAX_FILES = 100;
    private static final Pattern LOG_TIMESTAMP = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3})([+-]\\d{4})\\b");
    private static final DateTimeFormatter FILE_TIME = DateTimeFormatter
            .ofPattern("yyyyMMdd-HHmmss-SSS", Locale.ROOT);
    private static final DateTimeFormatter ISO_TIME = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ROOT);

    /** 解析本应用统一日志前缀；不能解析时保守保留，避免丢掉续行。 */
    static boolean lineIsWithin(String line, long cutoffWallMs, long exportedWallMs) {
        if (line == null || line.isEmpty()) return false;
        Matcher match = LOG_TIMESTAMP.matcher(line);
        if (!match.find()) return true;
        try {
            long lineMs = OffsetDateTime.parse(match.group(1) + match.group(2), ISO_TIME)
                    .toInstant().toEpochMilli();
            return lineMs >= cutoffWallMs && lineMs <= exportedWallMs + 1000L;
        } catch (RuntimeException ignored) {
            return true;
        }
    }

    /** 独立文件名包含本地日期和毫秒，稳定排序后可直接删除最老文件。 */
    static String fileName(String action, long wallMs) {
        String safeAction = action == null ? "CONTROL" : action.replaceAll("[^A-Za-z0-9_-]", "_");
        return "GeelyPassiveKey-action-" + safeAction + "-"
                + FILE_TIME.format(Instant.ofEpochMilli(wallMs).atZone(ZoneId.systemDefault())) + ".zip";
    }

    /** 窗口元数据固定写入快照，便于确认快照确实覆盖最近三分钟。 */
    static String summary(long wallMs, long monoMs, String action) {
        return "schema=2 snapshot_type=CONTROL_ACTION action=" + action
                + " window_ms=" + WINDOW_MS + " window_start_wall_ms=" + (wallMs - WINDOW_MS)
                + " exported_wall_ms=" + wallMs + " exported_mono_ms=" + monoMs
                + " max_files=" + MAX_FILES + "\n";
    }

    private DiagnosticSnapshotWindow() {
    }
}
