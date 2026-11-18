package com.luca.geelykeyradar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** 进程内共用的有界日志仓库，所有桥接事件也落盘，并能导出一致的诊断快照。 */
final class DiagnosticLogStore {
    static final int EVENT_BYTES = 256 * 1024;
    static final int EVENT_FILES = 4;
    static final int DETAIL_BYTES = 512 * 1024;
    static final int DETAIL_FILES = 6;
    static final int MAX_UI_LINES = 10;
    static final String ACTION_SNAPSHOT_DIR = "action-snapshots";

    private final File directory;
    private final File actionSnapshotDirectory;
    private final String session;
    private final int pid;
    private final DiagnosticText redactor;
    private final RotatingFileLog events;
    private final RotatingFileLog details;
    private final ArrayDeque<String> recent = new ArrayDeque<>();
    private final AtomicLong sequence = new AtomicLong();
    private final AtomicLong dropped = new AtomicLong();
    private final AtomicLong ioErrors = new AtomicLong();
    private final AtomicBoolean exportBusy = new AtomicBoolean();
    private final ThreadPoolExecutor writer = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(256));

    /** 创建两组固定容量日志，沿用旧文件名以保留升级前的现场。 */
    DiagnosticLogStore(File directory, String session, int pid, String salt) {
        this.directory = directory;
        this.actionSnapshotDirectory = new File(directory, ACTION_SNAPSHOT_DIR);
        this.session = session;
        this.pid = pid;
        redactor = new DiagnosticText(salt);
        events = new RotatingFileLog(directory, "passive_key.log", EVENT_BYTES, EVENT_FILES);
        details = new RotatingFileLog(directory, "passive_key_diagnostic.log", DETAIL_BYTES, DETAIL_FILES);
    }

    /** 生成可检索前缀并异步落盘；排队满时丢弃并计数，绝不阻塞车控主线程。 */
    String log(boolean important, String source, String event, String fields, long wallMs, long monoMs) {
        String line = format(source, event, fields, wallMs, monoMs);
        if (important) {
            synchronized (recent) {
                recent.addLast(line);
                while (recent.size() > MAX_UI_LINES) recent.removeFirst();
            }
        }
        enqueue(() -> append(important ? events : details, line));
        return line;
    }

    /** 使用完整日期、墙钟时区、单调时间和进程会话区分重启及系统时间变化。 */
    private String format(String source, String event, String fields, long wallMs, long monoMs) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ROOT).format(new Date(wallMs));
        return timestamp + " sid=" + session + " pid=" + pid + " seq=" + sequence.incrementAndGet()
                + " mono_ms=" + monoMs + " src=" + source + " event=" + event + " " + redactor.sanitize(fields);
    }

    /** 返回脱敏后的最近十条关键事件，最新在前。 */
    String recentText() {
        synchronized (recent) {
            if (recent.isEmpty()) return "暂无日志";
            StringBuilder result = new StringBuilder();
            Iterator<String> iterator = recent.descendingIterator();
            while (iterator.hasNext()) {
                if (result.length() > 0) result.append('\n');
                result.append(iterator.next());
            }
            return result.toString();
        }
    }

    /** 将容量、丢弃及写入错误一起导出，不能把缺失的日志误认作没有事件。 */
    String health() {
        return "log_cap_bytes=" + (EVENT_BYTES * EVENT_FILES + DETAIL_BYTES * DETAIL_FILES)
                + " queue=" + writer.getQueue().size() + "/256 dropped=" + dropped.get()
                + " io_errors=" + ioErrors.get() + " export_busy=" + exportBusy.get();
    }

    /** 返回本机稳定身份指纹，不暴露原始车辆信息。 */
    String fingerprint(String value) {
        return redactor.fingerprint(value);
    }

    /** 把服务上一次是否正常停止写入启动日志；未停止仅是线索，不等于已确认崩溃。 */
    void serviceStarted(String run, long wallMs, long monoMs) {
        enqueue(() -> {
            Properties previous = readMarker();
            String fields = "trace_run=" + run + " run=" + run + " previous_run=" + previous.getProperty("run", "none")
                    + " previous_clean_stop=" + previous.getProperty("clean", "unknown")
                    + " previous_started_ms=" + previous.getProperty("started", "0")
                    + " note=unclean_is_not_proof_of_crash";
            append(events, format("Lifecycle", "SERVICE_BEGIN", fields, wallMs, monoMs));
            previous.setProperty("run", run);
            previous.setProperty("started", Long.toString(wallMs));
            previous.setProperty("clean", "false");
            writeMarker(previous);
        });
    }

    /** 只有当前服务run能够标记正常结束，不覆盖新实例的启动标记。 */
    void serviceStopped(String run, long wallMs, long monoMs) {
        log(true, "Lifecycle", "SERVICE_END", "trace_run=" + run + " run=" + run, wallMs, monoMs);
        enqueue(() -> {
            Properties marker = readMarker();
            if (!run.equals(marker.getProperty("run"))) return;
            marker.setProperty("clean", "true");
            writeMarker(marker);
        });
    }

    /** 将完整清空操作排在已有写入之后，并保留本次清空事件。 */
    void clear(long wallMs, long monoMs) {
        if (!enqueue(() -> {
            try {
                events.clear();
                details.clear();
                append(events, format("User", "LOGS_CLEARED", "", wallMs, monoMs));
            } catch (IOException error) {
                ioErrors.incrementAndGet();
            }
        })) return;
        synchronized (recent) {
            recent.clear();
        }
    }

    /** 在同一写入队列中生成ZIP；只读取白名单日志，不打包偏好、身份盐或其他应用数据。 */
    Future<File> export(File destination, String snapshot, long wallMs, long monoMs) {
        return export(destination, snapshot, wallMs, monoMs, java.util.Collections.emptyMap());
    }

    /** 在固定诊断文件之外加入显式采集附件；附件只允许兼容性抓取白名单名称。 */
    Future<File> export(File destination, String snapshot, long wallMs, long monoMs,
                        Map<String, String> extraTexts) {
        if (!exportBusy.compareAndSet(false, true)) throw new IllegalStateException("已有日志导出进行中");
        try {
            Map<String, String> extras = extraTexts == null
                    ? java.util.Collections.emptyMap() : new java.util.LinkedHashMap<>(extraTexts);
            return writer.submit(() -> {
                try {
                    try (OutputStream output = new FileOutputStream(destination);
                         ZipOutputStream zip = new ZipOutputStream(output)) {
                        addText(zip, "summary.txt", "schema=2 exported_wall_ms=" + wallMs + " exported_mono_ms=" + monoMs
                                + "\n" + triggerConfigSummary(snapshot, wallMs)
                                + "sid=" + session + " pid=" + pid + "\n" + health() + "\n" + sanitizeLines(snapshot));
                        addText(zip, "README.txt", "银河无感钥匙诊断包\n"
                                + "events.log 是关键事件，diagnostic.log 是计数、采样和等待细节；每组按旧分段到新分段合并。\n"
                                + "sid/pid 区分进程；mono_ms 比较同一次开机的耗时；墙钟可能因时间校准改变。\n"
                                + "用 sid + trace_run + req_id 关联请求；SERVICE_BEGIN 的 previous_clean_stop=false 不能单独证明崩溃。\n"
                                + "计数是累计(+本窗口新增)，dropped/io_errors 非零说明日志可能丢失；轮转不保证保留全部历史。\n"
                                + "SENT 是交给银河，不是车锁执行回执；UNKNOWN 不能算成功或失败。\n"
                                + "普通诊断日志中的蓝牙地址/车辆名称已替换为本机指纹。\n"
                                + (extras.isEmpty() ? "" : "compatibility_capture.txt 是用户主动兼容上报时采集的原始 BLE 数据，"
                                + "刻意保留地址、名称、广播字节、厂商数据、UUID 与当前可见 GATT 信息。\n"));
                        addLogs(zip, "events.log", "passive_key.log", EVENT_FILES, EVENT_BYTES);
                        addLogs(zip, "diagnostic.log", "passive_key_diagnostic.log", DETAIL_FILES, DETAIL_BYTES);
                        for (Map.Entry<String, String> entry : extras.entrySet()) {
                            String name = entry.getKey();
                            if (!safeExtraName(name)) throw new IllegalArgumentException("不允许的日志附件名称");
                            addText(zip, name, entry.getValue() == null ? "" : entry.getValue());
                        }
                    }
                    return destination;
                } catch (Exception error) {
                    if (destination.exists()) destination.delete();
                    throw error;
                } finally {
                    exportBusy.set(false);
                }
            });
        } catch (RuntimeException rejected) {
            exportBusy.set(false);
            dropped.incrementAndGet();
            throw rejected;
        }
    }

    /** 兼容性附件只允许固定前缀，避免调用方覆盖 summary/README 或写入任意路径。 */
    private static boolean safeExtraName(String name) {
        return name != null && name.matches("compatibility_[a-z0-9_]{1,48}\\.txt");
    }

    /**
     * 动作交付给银河后异步截取最近三分钟。ZIP只写到应用私有的 action-snapshots，
     * 并在同一写入队列中完成，确保快照前缀一致；不阻塞车控线程。
     */
    Future<File> captureActionSnapshot(String action, String snapshot, long wallMs, long monoMs) {
        File[] initial = actionSnapshotDirectory.listFiles((dir, name) ->
                name.startsWith("GeelyPassiveKey-action-") && name.endsWith(".zip"));
        if (initial == null) initial = new File[0];
        return writer.submit(() -> {
            if (!actionSnapshotDirectory.exists() && !actionSnapshotDirectory.mkdirs()) {
                throw new IOException("无法创建动作快照目录");
            }
            File destination = new File(actionSnapshotDirectory,
                    DiagnosticSnapshotWindow.fileName(action, wallMs));
            try (OutputStream output = new FileOutputStream(destination);
                 ZipOutputStream zip = new ZipOutputStream(output)) {
                String triggerConfig = triggerConfigSummary(snapshot, wallMs);
                addText(zip, "summary.txt", DiagnosticSnapshotWindow.summary(wallMs, monoMs, action)
                        + triggerConfig
                        + "sid=" + session + " pid=" + pid + "\n" + health() + "\n" + sanitizeLines(snapshot));
                addText(zip, "README.txt", "银河无感钥匙动作诊断快照\n"
                        + "本快照仅覆盖动作交付时刻之前的最近三分钟；不是完整历史导出。\n"
                        + "触发配置（连接/解锁/上锁/确认）已写入 summary.txt，四个参数均可在 App 内自定义。\n"
                        + "SENT 是交给银河，不是车锁执行回执；UNKNOWN 不能算成功或失败。\n"
                        + "地址和车辆名称已脱敏；最多保留最近100个动作快照。\n");
                addLogsWithin(zip, "events.log", "passive_key.log", EVENT_FILES,
                        wallMs, wallMs - DiagnosticSnapshotWindow.WINDOW_MS);
                addLogsWithin(zip, "diagnostic.log", "passive_key_diagnostic.log", DETAIL_FILES,
                        wallMs, wallMs - DiagnosticSnapshotWindow.WINDOW_MS);
            } catch (Exception error) {
                destination.delete();
                throw error;
            } finally {
                pruneActionSnapshots();
            }
            return destination;
        });
    }

    /** 从实时服务快照抽取四项当前配置，保证动作ZIP自身即可解释触发参数。 */
    private static String triggerConfigSummary(String snapshot, long wallMs) {
        String source = snapshot == null ? "" : snapshot;
        String captured = "\nsnapshot_config_captured_wall_ms=" + wallMs + "\n";
        java.util.regex.Matcher match = java.util.regex.Pattern
                .compile("(?m)^connect_dbm=([^\\s]+) unlock_dbm=([^\\s]+) "
                        + "lock_dbm=([^\\s]+) dwell_ms=([^\\s]+)$")
                .matcher(source);
        if (match.find()) return captured + "connect_dbm=" + match.group(1)
                + " unlock_dbm=" + match.group(2) + " lock_dbm=" + match.group(3)
                + " lock_dwell_ms=" + match.group(4) + "\n";
        return captured + "connect_dbm=" + configValue(source, "connect_dbm")
                + " unlock_dbm=" + configValue(source, "unlock_dbm")
                + " lock_dbm=" + configValue(source, "lock_dbm")
                + " lock_dwell_ms=" + configValue(source, "dwell_ms") + "\n";
    }

    /** 优先取单行字段，缺失时明确写 unavailable，不猜测配置。 */
    private static String configValue(String snapshot, String... keys) {
        for (String key : keys) {
            java.util.regex.Matcher match = java.util.regex.Pattern
                    .compile("(?:^|\\s)" + java.util.regex.Pattern.quote(key) + "=([^\\s]+)")
                    .matcher(snapshot);
            if (match.find()) return match.group(1);
        }
        return "unavailable";
    }

    /** 只保留最近100个动作ZIP；按最后修改时间排序，删除最老文件。 */
    private void pruneActionSnapshots() {
        File[] files = actionSnapshotDirectory.listFiles((dir, name) ->
                name.startsWith("GeelyPassiveKey-action-") && name.endsWith(".zip"));
        if (files == null || files.length <= DiagnosticSnapshotWindow.MAX_FILES) return;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified)
                .thenComparing(File::getName));
        int removeCount = files.length - DiagnosticSnapshotWindow.MAX_FILES;
        for (int index = 0; index < removeCount; index++) {
            if (!files[index].delete()) ioErrors.incrementAndGet();
        }
    }

    /** 返回动作快照数量和总字节，供界面区分“自动快照”和手动完整导出。 */
    String actionSnapshotHealth() {
        File[] files = actionSnapshotDirectory.listFiles((dir, name) ->
                name.startsWith("GeelyPassiveKey-action-") && name.endsWith(".zip"));
        int count = files == null ? 0 : files.length;
        long bytes = 0L;
        if (files != null) for (File file : files) bytes += file.length();
        return "action_snapshots=" + count + "/" + DiagnosticSnapshotWindow.MAX_FILES
                + " action_snapshot_bytes=" + bytes;
    }

    /** 导出全部动作快照为一个ZIP，仅用于手动排查；不改变100个快照留存。 */
    Future<File> exportActionSnapshots(File destination, String snapshot, long wallMs, long monoMs) {
        return writer.submit(() -> {
            File[] files = actionSnapshotDirectory.listFiles((dir, name) ->
                    name.startsWith("GeelyPassiveKey-action-") && name.endsWith(".zip"));
            if (files == null) files = new File[0];
            Arrays.sort(files, Comparator.comparingLong(File::lastModified)
                    .thenComparing(File::getName));
            try (OutputStream output = new FileOutputStream(destination);
                 ZipOutputStream zip = new ZipOutputStream(output)) {
                addText(zip, "summary.txt", "schema=2 snapshot_type=ACTION_HISTORY"
                        + " exported_wall_ms=" + wallMs + " exported_mono_ms=" + monoMs
                        + " count=" + files.length + " max_files=" + DiagnosticSnapshotWindow.MAX_FILES
                        + "\n" + health() + "\n" + sanitizeLines(snapshot));
                addText(zip, "README.txt", "银河无感钥匙动作快照合集\n"
                        + "每个内层 ZIP 对应一次解锁或上锁动作前最近三分钟。\n"
                        + "最多保留100个；本导出只读取现存快照，不创建或删除快照。\n");
                for (File file : files) addFile(zip, "action-snapshots/" + file.getName(), file);
            } catch (Exception error) {
                if (destination.exists()) destination.delete();
                throw error;
            }
            return destination;
        });
    }

    /** 仅合并窗口内的时间戳行；续行跟随上一条已接纳日志。 */
    private void addLogsWithin(ZipOutputStream zip, String entry, String base, int count,
                               long exportedWallMs, long cutoffWallMs) throws IOException {
        zip.putNextEntry(new ZipEntry(entry));
        for (int index = count - 1; index >= 0; index--) {
            File file = new File(directory, index == 0 ? base : base + "." + index);
            if (!file.isFile()) continue;
            byte[] header = ("--- " + file.getName() + " bytes=" + file.length() + " ---\n")
                    .getBytes(StandardCharsets.UTF_8);
            zip.write(header);
            try (BufferedReader input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                boolean accepted = false;
                while ((line = input.readLine()) != null) {
                    if (line.matches("^\\d{4}-.*")) {
                        accepted = DiagnosticSnapshotWindow.lineIsWithin(line, cutoffWallMs, exportedWallMs);
                    }
                    if (!accepted) continue;
                    zip.write((redactor.sanitize(line) + "\n").getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        zip.closeEntry();
    }

    /** 测试或显式关闭时等待所有已接纳日志写完，运行中服务停止不关闭进程共享仓库。 */
    void close() throws InterruptedException {
        writer.shutdown();
        if (!writer.awaitTermination(10L, TimeUnit.SECONDS)) writer.shutdownNow();
    }

    /** 包内测试通过屏障验证异步操作顺序。 */
    Future<?> flush() {
        return writer.submit(() -> { });
    }

    /** 队列拒绝只累计丢弃数，不把日志失败传播到车控。 */
    private boolean enqueue(Runnable operation) {
        try {
            writer.execute(operation);
            return true;
        } catch (RuntimeException rejected) {
            dropped.incrementAndGet();
            return false;
        }
    }

    /** 写入失败记录健康计数，避免递归调用日志。 */
    private void append(RotatingFileLog destination, String line) {
        try {
            destination.append(line);
        } catch (IOException error) {
            ioErrors.incrementAndGet();
        }
    }

    /** 读取本应用的服务运行标记，损坏时显式回落到unknown。 */
    private Properties readMarker() {
        Properties marker = new Properties();
        File file = new File(directory, "diagnostic_service.properties");
        if (!file.exists()) return marker;
        try (FileInputStream input = new FileInputStream(file)) {
            marker.load(input);
        } catch (IOException error) {
            ioErrors.incrementAndGet();
        }
        return marker;
    }

    /** 标记只记录随机run和时间，不存业务凭证。 */
    private void writeMarker(Properties marker) {
        try (FileOutputStream output = new FileOutputStream(new File(directory, "diagnostic_service.properties"))) {
            marker.store(output, null);
        } catch (IOException error) {
            ioErrors.incrementAndGet();
        }
    }

    /** 汇总快照逐行脱敏，保留可读换行。 */
    private String sanitizeLines(String value) {
        StringBuilder result = new StringBuilder();
        for (String line : value.split("\\R")) result.append(redactor.sanitize(line)).append('\n');
        return result.toString();
    }

    /** 以流方式加入一个已有ZIP，避免把单个动作快照整体读入内存。 */
    private void addFile(ZipOutputStream zip, String entry, File file) throws IOException {
        zip.putNextEntry(new ZipEntry(entry));
        try (FileInputStream input = new FileInputStream(file)) {
            input.transferTo(zip);
        }
        zip.closeEntry();
    }

    /** 将纯文本加入ZIP。 */
    private void addText(ZipOutputStream zip, String name, String value) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(value.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    /** 合并固定分段并再次脱敏旧版本日志，限制每个源文件读取量。 */
    private void addLogs(ZipOutputStream zip, String entry, String base, int count, int maxBytes) throws IOException {
        zip.putNextEntry(new ZipEntry(entry));
        for (int index = count - 1; index >= 0; index--) {
            File file = new File(directory, index == 0 ? base : base + "." + index);
            if (!file.isFile()) continue;
            zip.write(("--- " + file.getName() + " bytes=" + file.length() + " ---\n").getBytes(StandardCharsets.UTF_8));
            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                int copied = 0;
                while ((line = input.readLine()) != null) {
                    byte[] bytes = (redactor.sanitize(line) + "\n").getBytes(StandardCharsets.UTF_8);
                    if (copied + bytes.length > maxBytes) {
                        zip.write("[source truncated at export limit]\n".getBytes(StandardCharsets.UTF_8));
                        break;
                    }
                    zip.write(bytes);
                    copied += bytes.length;
                }
            }
        }
        zip.closeEntry();
    }
}
