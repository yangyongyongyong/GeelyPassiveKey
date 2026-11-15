package com.luca.geelykeyradar;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

/** 日志观测、容量、脱敏和导出回归，不需要手机或官方钥匙权限。 */
public final class DiagnosticsTest {
    /** 检验纯观测不会影响原车控行为，诊断包只包含允许的日志。 */
    public static void main(String[] args) throws Exception {
        redactionHidesIdentifiers();
        counterWindowsRemainBounded();
        diagnosticSnapshotDoesNotMutateLogic();
        reasonsMatchActualBranches();
        exportIsConsistentAndRedacted();
        actionSnapshotUsesThreeMinuteWindowAndKeepsConfiguration();
        actionSnapshotPrunesOldestAtOneHundred();
        processMarkerDistinguishesUncleanStop();
        System.out.println("DiagnosticsTest passed");
    }

    /** 地址指纹可关联但无法直接识别车辆，敏感键值和异常正文不能进入导出。 */
    private static void redactionHidesIdentifiers() {
        DiagnosticText text = new DiagnosticText("local-test-salt");
        String value = text.sanitize("addr=AA:BB:CC:11:22:33 vehicle=GeelyVehicle123 VIN尾号 123 token=private-value\nnext");
        check(!value.contains("AA:BB:CC:11:22:33") && !value.contains("GeelyVehicle123")
                && !value.contains("VIN尾号 123") && !value.contains("private-value") && !value.contains("\n"),
                "车辆身份及令牌必须脱敏，日志必须保持单行");
        check(text.fingerprint("AA:BB:CC:11:22:33").equals(text.fingerprint("aa:bb:cc:11:22:33")),
                "同一地址大小写差异仍应可关联");
        check(!text.fingerprint("AA:BB:CC:11:22:33").equals(new DiagnosticText("other-salt").fingerprint("AA:BB:CC:11:22:33")),
                "不同本机盐应生成不同指纹");
        check(!DiagnosticText.errorSummary(new IllegalStateException("token=private-value")).contains("private-value"),
                "异常摘要不得包含正文里的凭证");
        check(!text.sanitize("已加载本车身份: Luca EV · AA:BB:CC:11:22:33").contains("Luca EV"),
                "旧版身份日志里的任意自定义车名也必须脱敏");
        check(!text.sanitize("车机底层 ACL 已连接: 我的车 · AA:BB:CC:11:22:33").contains("我的车"),
                "旧版 ACL 文案不能泄露中文自定义车名");
        check(text.sanitize("已加载本车身份: addr_id=abcdef name_id=123456 identity_id=789abc")
                .contains("addr_id=abcdef name_id=123456"), "新版结构化指纹字段应保持可关联");
    }

    /** 累计值和窗口增量可区分零回调与被过滤，动态名称也不能无限扩容。 */
    private static void counterWindowsRemainBounded() {
        DiagnosticCounters counters = new DiagnosticCounters();
        counters.increment("scan_callbacks");
        counters.increment("scan_stale");
        check(counters.snapshot(true).contains("scan_callbacks=1(+1)"), "首个窗口记录新增值");
        check(counters.snapshot(false).contains("scan_callbacks=1(+0)"), "新窗口无回调应为零新增");
        counters.increment("scan_callbacks");
        check(counters.snapshot(false).contains("scan_callbacks=2(+1)"), "累计和增量分别正确");
        for (int index = 0; index < 1000; index++) counters.increment("unexpected_" + index);
        check(counters.snapshot(false).split(" ").length <= 65, "计数器命名空间必须有上限");
    }

    /** 在相同样本之间反复查询诊断，动作和状态应与没有查询的实例完全一致。 */
    private static void diagnosticSnapshotDoesNotMutateLogic() {
        PassiveKeyLogic observed = new PassiveKeyLogic();
        PassiveKeyLogic control = new PassiveKeyLogic();
        for (int index = 0; index < 100; index++) {
            long now = 1_000L + index * 500L;
            float distance = index < 20 || index > 60 ? -65f : -87f;
            PassiveKeyLogic.Action expected = control.onSample(distance, now, true, false, true, true);
            PassiveKeyLogic.Action actual = observed.onSample(distance, now, true, false, true, true);
            String snapshot = observed.diagnosticSnapshot(now);
            check(snapshot.equals(observed.diagnosticSnapshot(now)), "诊断读取本身不能修改计时或状态");
            check(actual == expected, "诊断观测不能改变解闭锁输出");
            if (actual != PassiveKeyLogic.Action.NONE) {
                observed.onControlSent(actual, now);
                control.onControlSent(expected, now);
            }
            check(observed.isApproachLatched() == control.isApproachLatched()
                    && observed.getLockDwellSamples() == control.getLockDwellSamples(), "观测前后业务状态一致");
        }
    }

    /** 原因由真实返回分支产生，官方事件原因不被后续心跳覆盖。 */
    private static void reasonsMatchActualBranches() {
        PassiveKeyLogic logic = new PassiveKeyLogic();
        logic.onSample(-90f, 1_000L, false, false, true, true);
        check("NO_NEAR_EVIDENCE".equals(logic.getLastSampleDecision()), "冷启动远处不补锁应有可检索原因");
        logic.onSample(-65f, 1_500L, false, false, false, true);
        check("UNLOCK_DISABLED".equals(logic.getLastSampleDecision()), "关闭自动解锁应记录该分支");
        logic.onOfficialConnected(2_000L, true, false);
        logic.onTick(3_000L, true, true);
        check("DRIVING_OR_WIRED_BLOCK".equals(logic.getLastOfficialDecision()), "tick不能覆盖官方就绪被拦截的原因");
        check(logic.diagnosticSnapshot(3_000L).contains("unlock_requests=0/2"), "快照应含失败预算");
    }

    /** 压测轮转并导出旧版含身份日志，ZIP不得包含任何偏好或身份盐文件。 */
    private static void exportIsConsistentAndRedacted() throws Exception {
        Path directory = Files.createTempDirectory("diagnostic-export-test-");
        DiagnosticLogStore store = new DiagnosticLogStore(directory.toFile(), "testsid", 123, "salt");
        try {
            Files.writeString(directory.resolve("private-prefs.xml"), "token=should-not-be-exported");
            Files.writeString(directory.resolve("passive_key.log.3"), "old AA:BB:CC:11:22:33 GeelyVehicle123\n"
                    + "12:00:00 已加载本车身份: Luca EV · AA:BB:CC:11:22:33\n");
            for (int index = 0; index < 20; index++) store.log(true, "Test", "EVENT", "number=" + index, 1_000L + index, 10L + index);
            check(store.recentText().split("\n").length == 10, "界面最多十条事件");
            check(!store.recentText().contains("number=9 ") && store.recentText().contains("number=19"), "应保留最新事件");
            String detail = "data=" + "a".repeat(7000);
            for (int index = 0; index < 520; index++) {
                store.log(false, "Test", "DETAIL", detail, 2_000L + index, 100L + index);
                if (index % 80 == 79) store.flush().get(10L, TimeUnit.SECONDS);
            }
            store.flush().get(10L, TimeUnit.SECONDS);
            File archive = store.export(directory.resolve("result.zip").toFile(), "token=private-value\nstate=ok", 99_000L, 80_000L)
                    .get(10L, TimeUnit.SECONDS);
            try (ZipFile zip = new ZipFile(archive)) {
                Set<String> names = new HashSet<>();
                zip.stream().forEach(entry -> names.add(entry.getName()));
                check(names.equals(Set.of("summary.txt", "README.txt", "events.log", "diagnostic.log")), "仅白名单日志可以进入ZIP");
                String events = new String(zip.getInputStream(zip.getEntry("events.log")).readAllBytes(), StandardCharsets.UTF_8);
                check(events.contains("number=19") && events.contains("sid=testsid"), "导出应包含先前所有已接纳关键事件");
                check(!events.contains("AA:BB:CC:11:22:33") && !events.contains("GeelyVehicle123")
                        && !events.contains("Luca EV"), "旧版本日志也必须再次脱敏");
                String summary = new String(zip.getInputStream(zip.getEntry("summary.txt")).readAllBytes(), StandardCharsets.UTF_8);
                check(summary.contains("state=ok") && !summary.contains("private-value"), "摘要要保留状态并脱敏");
            }
            long logBytes;
            try (Stream<Path> paths = Files.list(directory)) {
                logBytes = paths.filter(path -> path.getFileName().toString().startsWith("passive_key"))
                        .mapToLong(path -> path.toFile().length()).sum();
            }
            check(logBytes <= 4L * 1024 * 1024, "全部日志总量不得超过4MiB");
            store.clear(100_000L, 81_000L);
            store.flush().get(10L, TimeUnit.SECONDS);
            check(Files.readString(directory.resolve("passive_key.log")).contains("LOGS_CLEARED"), "清空后保留清空标记");
            check(!Files.exists(directory.resolve("passive_key.log.3"))
                    && !Files.exists(directory.resolve("passive_key_diagnostic.log")), "清空应移除所有日志历史分段");
        } finally {
            store.close();
            deleteTemporary(directory);
        }
    }

    /** 动作快照只含截止前3分钟且 summary 必须明确写出用户四项配置。 */
    private static void actionSnapshotUsesThreeMinuteWindowAndKeepsConfiguration() throws Exception {
        Path directory = Files.createTempDirectory("action-snapshot-test-");
        DiagnosticLogStore store = new DiagnosticLogStore(directory.toFile(), "snap", 7, "salt");
        try {
            long now = 1_794_735_600_000L;
            store.log(true, "Test", "OLD", "value=old", now - 181_000L, 1L);
            store.log(true, "Test", "RECENT", "value=recent", now - 60_000L, 2L);
            store.log(false, "Test", "DETAIL_RECENT", "value=detail", now - 30_000L, 3L);
            store.flush().get(10L, TimeUnit.SECONDS);
            String snapshot = "connect_dbm=-82.0 unlock_dbm=-75.0 lock_dbm=-85.0 dwell_ms=2000";
            File archive = store.captureActionSnapshot("UNLOCK", snapshot, now, 100L)
                    .get(10L, TimeUnit.SECONDS);
            try (ZipFile zip = new ZipFile(archive)) {
                Set<String> names = new HashSet<>();
                zip.stream().forEach(entry -> names.add(entry.getName()));
                check(names.equals(Set.of("summary.txt", "README.txt", "events.log", "diagnostic.log")),
                        "动作快照必须保持固定白名单");
                String summary = new String(zip.getInputStream(zip.getEntry("summary.txt"))
                        .readAllBytes(), StandardCharsets.UTF_8);
                check(summary.contains("window_ms=180000")
                                && summary.contains("connect_dbm=-82.0")
                                && summary.contains("unlock_dbm=-75.0")
                                && summary.contains("lock_dbm=-85.0")
                                && summary.contains("lock_dwell_ms=2000"),
                        "动作快照必须写明窗口和四项当时配置");
                String events = new String(zip.getInputStream(zip.getEntry("events.log"))
                        .readAllBytes(), StandardCharsets.UTF_8);
                check(events.contains("RECENT") && !events.contains("value=old"),
                        "动作事件日志不能导出三分钟以前的事件");
            }
        } finally {
            store.close();
            deleteTemporary(directory);
        }
    }

    /** 动作快照数量不得超过100，超过后删除最老文件。 */
    private static void actionSnapshotPrunesOldestAtOneHundred() throws Exception {
        Path directory = Files.createTempDirectory("action-snapshot-prune-test-");
        DiagnosticLogStore store = new DiagnosticLogStore(directory.toFile(), "prune", 8, "salt");
        try {
            long base = 1_794_735_600_000L;
            for (int index = 0; index < DiagnosticSnapshotWindow.MAX_FILES + 5; index++) {
                store.captureActionSnapshot("UNLOCK", "connect_dbm=-82 unlock_dbm=-75 lock_dbm=-85 dwell_ms=2000",
                        base + index, index).get(10L, TimeUnit.SECONDS);
            }
            File[] files = new File(directory.toFile(), DiagnosticLogStore.ACTION_SNAPSHOT_DIR)
                    .listFiles((dir, name) -> name.endsWith(".zip"));
            check(files != null && files.length == DiagnosticSnapshotWindow.MAX_FILES,
                    "动作快照必须限制为100个");
        } finally {
            store.close();
            deleteTemporary(directory);
        }
    }

    /** 未执行正常停止的旧服务在下次启动时留下线索，正常停止后应为true。 */
    private static void processMarkerDistinguishesUncleanStop() throws Exception {
        Path directory = Files.createTempDirectory("diagnostic-marker-test-");
        DiagnosticLogStore first = new DiagnosticLogStore(directory.toFile(), "process1", 1, "salt");
        DiagnosticLogStore second = null;
        try {
            first.serviceStarted("run1", 1_000L, 10L);
            first.flush().get(10L, TimeUnit.SECONDS);
            first.close();
            second = new DiagnosticLogStore(directory.toFile(), "process2", 2, "salt");
            second.serviceStarted("run2", 2_000L, 20L);
            second.serviceStopped("run2", 3_000L, 30L);
            second.serviceStarted("run3", 4_000L, 40L);
            second.flush().get(10L, TimeUnit.SECONDS);
            String events = Files.readString(directory.resolve("passive_key.log"));
            check(events.contains("previous_run=run1 previous_clean_stop=false"), "未正常结束应可追溯");
            check(events.contains("previous_run=run2 previous_clean_stop=true"), "正常结束应与异常中止区分");
        } finally {
            first.close();
            if (second != null) second.close();
            deleteTemporary(directory);
        }
    }

    /** 仅清理本测试创建的临时目录。 */
    private static void deleteTemporary(Path directory) throws Exception {
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toArray(Path[]::new)) Files.deleteIfExists(path);
        }
    }

    /** 失败时保留预期的诊断约束。 */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
