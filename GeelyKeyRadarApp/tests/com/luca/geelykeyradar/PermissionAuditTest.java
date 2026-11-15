package com.luca.geelykeyradar;

/** 授权全检的纯逻辑回归，不读取真实权限，也不触发系统页面。 */
public final class PermissionAuditTest {
    public static void main(String[] args) {
        readyInputKeepsOptionalManualVisible();
        listenerNeedsGraceThenAction();
        oldAndroidNotificationPermissionIsReady();
        requiredOrderAndCountsAreStable();
        System.out.println("PermissionAuditTest passed");
    }

    private static PermissionAudit.Input readyInput() {
        PermissionAudit.Input input = new PermissionAudit.Input();
        input.sdkInt = 36;
        input.bluetoothPermission = true;
        input.bluetoothOn = true;
        input.notificationRuntimePermission = true;
        input.listenerGranted = true;
        input.listenerConnected = true;
        input.geelyInstalled = true;
        input.serviceEnabled = true;
        input.serviceRunning = true;
        input.batteryWhitelisted = true;
        input.vendorBackgroundConfirmed = true;
        return input;
    }

    private static void readyInputKeepsOptionalManualVisible() {
        PermissionAudit.Input input = readyInput();
        input.vendorBackgroundConfirmed = false;
        PermissionAudit.Result result = PermissionAudit.evaluate(input);
        check(result.essentialReady(), "厂商后台未确认不应阻塞必要项");
        check(result.requiredReady == 6 && result.requiredTotal == 6, "六项必要检查均应就绪");
        check(result.recommendedReady == 1 && result.recommendedTotal == 2, "建议项统计应保留手动项");
        check(result.row(PermissionAudit.Item.VENDOR_BACKGROUND).state == PermissionAudit.State.MANUAL,
                "厂商后台没有可读API时必须标记手动确认");
        check(result.headline().contains("必要项已就绪"), "必要项完成但建议项未完成时使用精确标题");
    }

    private static void listenerNeedsGraceThenAction() {
        PermissionAudit.Input input = readyInput();
        input.listenerConnected = false;
        input.listenerDownMs = PermissionAudit.LISTENER_REBIND_GRACE_MS - 1L;
        PermissionAudit.Result waiting = PermissionAudit.evaluate(input);
        PermissionAudit.Row row = waiting.row(PermissionAudit.Item.LISTENER);
        check(row.state == PermissionAudit.State.WAITING, "观察窗口内应显示正在重连");
        check("正在重连".equals(row.statusLabel()), "等待态不能被描述成权限被撤销");
        check(!waiting.essentialReady(), "等待监听重绑时必要项仍未完成");

        input.listenerDownMs = PermissionAudit.LISTENER_REBIND_GRACE_MS;
        PermissionAudit.Result action = PermissionAudit.evaluate(input);
        check(action.row(PermissionAudit.Item.LISTENER).state == PermissionAudit.State.ACTION_REQUIRED,
                "超过观察窗口后才提示修复绑定");
        check("待处理".equals(action.row(PermissionAudit.Item.LISTENER).statusLabel()), "修复态文案稳定");
    }

    private static void oldAndroidNotificationPermissionIsReady() {
        PermissionAudit.Input input = readyInput();
        input.sdkInt = 32;
        input.notificationRuntimePermission = false;
        PermissionAudit.Result result = PermissionAudit.evaluate(input);
        check(result.row(PermissionAudit.Item.NOTIFICATIONS).isReady(),
                "Android 12及以下没有POST_NOTIFICATIONS运行时权限");
    }

    private static void requiredOrderAndCountsAreStable() {
        PermissionAudit.Input input = readyInput();
        input.bluetoothPermission = false;
        input.notificationRuntimePermission = false;
        input.serviceEnabled = false;
        PermissionAudit.Result result = PermissionAudit.evaluate(input);
        check(result.pendingRequired() == 3, "缺失的必要项必须全部计数");
        check(result.nextRequired != null && result.nextRequired.item == PermissionAudit.Item.BLUETOOTH_PERMISSION,
                "引导顺序应先蓝牙权限");
        check(result.rows.size() == 8, "八项检查必须保持稳定顺序与数量");
        check(result.summaryLine().contains("必要项 3/6"), "摘要必须来自实际评估结果");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
