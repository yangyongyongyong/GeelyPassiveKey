package com.luca.geelykeyradar;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/** 日志上报纯逻辑回归，不发起网络请求。 */
public final class LogUploadPolicyTest {
    public static void main(String[] args) {
        installIdsAndNotes();
        archivesAndJson();
        System.out.println("LogUploadPolicyTest passed");
    }

    private static void installIdsAndNotes() {
        String id = LogUploadPolicy.newInstallId();
        check(LogUploadPolicy.validInstallId(id), "随机安装ID格式正确");
        check(!LogUploadPolicy.validInstallId("short"), "短ID被拒绝");
        String uid1 = LogUploadPolicy.deriveDeviceUid("hardware-a|model-x");
        String uid2 = LogUploadPolicy.deriveDeviceUid("hardware-a|model-x");
        String uid3 = LogUploadPolicy.deriveDeviceUid("hardware-b|model-x");
        check(LogUploadPolicy.validDeviceUid(uid1) && uid1.equals(uid2), "同一硬件材料生成固定UID");
        check(!uid1.equals(uid3), "不同硬件材料生成不同UID");
        check(!LogUploadPolicy.validIssueDescription("短"), "异常现象过短被拒绝");
        check(LogUploadPolicy.validIssueDescription("靠近未解锁"), "正常异常现象通过");
        String note = LogUploadPolicy.sanitizeNote("  靠近\u0000未解锁\n第二行  ");
        check(note.equals("靠近未解锁\n第二行"), "问题描述保留中文与换行并移除控制字符");
        String longNote = "x".repeat(LogUploadPolicy.MAX_NOTE_CHARS + 20);
        check(LogUploadPolicy.sanitizeNote(longNote).length() == LogUploadPolicy.MAX_NOTE_CHARS,
                "问题描述限制长度");
    }

    private static void archivesAndJson() {
        byte[] zip = new byte[128];
        zip[0] = 0x50; zip[1] = 0x4B; zip[2] = 0x03; zip[3] = 0x04;
        LogUploadPolicy.validateArchive(zip);
        byte[] sha = new byte[32];
        Arrays.fill(sha, (byte) 0x1a);
        String digest = "1a".repeat(32);
        String deviceUid = LogUploadPolicy.deriveDeviceUid("test-device");
        String json = LogUploadPolicy.buildRequestJson(
                "12345678-1234-1234-1234-123456789abc", deviceUid,
                "他说\"未解锁\"\\测试", "4.5.9-min", 509, "16", "vivo", "V2426A",
                "log.zip", zip, digest);
        check(json.contains("\"schema\":1"), "请求固定schema");
        check(json.contains("\"deviceUid\":\"" + deviceUid + "\""), "请求包含固定日志UID");
        check(json.contains("他说\\\"未解锁\\\"\\\\测试"), "JSON引号和反斜杠被转义");
        check(json.contains("\"archiveBase64\":\"UEsDBA"), "ZIP以Base64发送");
        check(!json.contains("\u0000"), "请求不含控制字符");
        check(LogUploadPolicy.isZip(zip), "ZIP魔数校验");
        try {
            LogUploadPolicy.buildRequestJson(
                    "12345678-1234-1234-1234-123456789abc", deviceUid,
                    "", "4.5.9-min", 509, "16", "vivo", "V2426A",
                    "log.zip", zip, digest);
            throw new AssertionError("空异常现象必须拒绝");
        } catch (IllegalArgumentException expected) {
            check(expected.getMessage().contains("异常现象"), "异常现象为必填");
        }
        try {
            LogUploadPolicy.validateArchive(new byte[32]);
            throw new AssertionError("非ZIP必须拒绝");
        } catch (IllegalArgumentException expected) {
            check(expected.getMessage().contains("ZIP"), "非ZIP错误清晰");
        }
        byte[] large = new byte[LogUploadPolicy.MAX_ARCHIVE_BYTES + 1];
        large[0] = 0x50; large[1] = 0x4B; large[2] = 0x03; large[3] = 0x04;
        try {
            LogUploadPolicy.validateArchive(large);
            throw new AssertionError("超大ZIP必须拒绝");
        } catch (IllegalArgumentException expected) {
            check(expected.getMessage().contains("1.8 MB"), "超大日志提示上限");
        }
        check(new String(LogUploadPolicy.utf8("中文"), StandardCharsets.UTF_8).equals("中文"),
                "UTF-8编码稳定");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
