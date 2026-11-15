package com.luca.geelykeyradar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 授权与运行全检。
 *
 * 结构参考 dsh-go 的系统权限引导：一次只处理一项；解释用途、系统路径和当前
 * 真实状态；去系统设置后返回即复检；完成后给出一页式结果汇总。这里不是把
 * 一组按钮堆在一起，而是把“必要权限 / 保活建议 / 运行条件”分开判断。
 */
public class PermissionGuideActivity extends Activity {
    private static final int REQUEST_BLUETOOTH = 2101;
    private static final int REQUEST_NOTIFICATIONS = 2102;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2103;
    private static final int COLOR_BG = Color.rgb(8, 13, 18);
    private static final int COLOR_CARD = Color.rgb(17, 24, 32);
    private static final int COLOR_CARD_SOFT = Color.rgb(24, 34, 45);
    private static final int COLOR_TEXT = Color.rgb(232, 238, 245);
    private static final int COLOR_MUTED = Color.rgb(156, 171, 186);
    private static final int COLOR_ACCENT = Color.rgb(102, 211, 250);
    private static final int COLOR_ACCENT_SOFT = Color.rgb(16, 59, 76);
    private static final int COLOR_GREEN = Color.rgb(101, 214, 163);
    private static final int COLOR_GREEN_SOFT = Color.rgb(20, 59, 43);
    private static final int COLOR_AMBER = Color.rgb(247, 198, 91);
    private static final int COLOR_AMBER_SOFT = Color.rgb(58, 47, 20);
    private static final int COLOR_RED = Color.rgb(255, 154, 168);
    private static final int COLOR_RED_SOFT = Color.rgb(68, 31, 38);
    private static final long DONE_HOLD_MS = 750L;

    private enum GuideStep {
        BLUETOOTH("蓝牙扫描与连接",
                "先从系统获取附近的设备访问权，再确认手机蓝牙已开启。",
                "设置 → 应用 → 银河无感钥匙 → 权限 → 附近的设备",
                R.drawable.ic_permission_bluetooth),
        NOTIFICATIONS("系统通知权限",
                "常驻通知用于显示扫描和通知桥接状态；系统提醒也需要这条权限。",
                "设置 → 应用 → 银河无感钥匙 → 通知",
                R.drawable.ic_permission_notifications),
        LISTENER("通知使用权",
                "读取银河蓝牙钥匙的连接状态，并复用官方通知里的解锁/上锁按钮。",
                "设置 → 特殊应用权限 → 通知使用权 → 银河无感钥匙通知桥接",
                R.drawable.ic_permission_listener),
        BATTERY("电池白名单",
                "锁屏后减少被省电策略冻结、断网或清理的概率。",
                "设置 → 电池 → 后台耗电管理 → 银河无感钥匙",
                R.drawable.ic_permission_battery),
        VENDOR_BACKGROUND("厂商后台与自启动",
                "OriginOS 的后台高耗电和自启动没有可读的统一 API，需要你手动确认一次。",
                "i管家 → 自启动 → 银河无感钥匙；设置 → 电池 → 后台耗电管理 → 允许",
                R.drawable.ic_permission_background);

        final String title;
        final String description;
        final String path;
        final int iconRes;

        GuideStep(String title, String description, String path, int iconRes) {
            this.title = title;
            this.description = description;
            this.path = path;
            this.iconRes = iconRes;
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (!resumed) return;
            refreshUi();
            handler.postDelayed(this, 1000L);
        }
    };

    private LinearLayout content;
    private ScrollView scrollView;
    private final List<View> progressSegments = new ArrayList<>();
    private TextView progressCounter;
    private SharedPreferences prefs;
    private boolean resumed;
    private GuideStep activeStep;
    private GuideStep lastRenderedStep;
    private GuideStep doneStep;
    private long doneUntilMs;
    private String lastRenderSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PermissionStatus.PREFS, Context.MODE_PRIVATE);
        Window window = getWindow();
        window.setStatusBarColor(COLOR_BG);
        window.setNavigationBarColor(COLOR_BG);
        if (Build.VERSION.SDK_INT >= 26) {
            window.getDecorView().setSystemUiVisibility(0);
        }
        String saved = prefs.getString(PermissionStatus.KEY_ACTIVE_STEP, "");
        activeStep = parseStep(saved);
        if (savedInstanceState != null) {
            activeStep = parseStep(savedInstanceState.getString("active_step", saved));
        }
        buildShell();
        refreshUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
        startPassiveServiceIfEnabled();
        if (KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                && !KeyNotificationBridgeService.isListenerConnected()) {
            KeyNotificationBridgeService.ensureRebound(this);
            KeyNotificationBridgeService.requestImmediateRecovery(this);
        }
        handler.removeCallbacks(ticker);
        handler.post(ticker);
    }

    @Override
    protected void onPause() {
        resumed = false;
        handler.removeCallbacks(ticker);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (activeStep != null) outState.putString("active_step", activeStep.name());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATIONS) {
            prefs.edit().putBoolean(PermissionStatus.KEY_NOTIFICATION_REQUESTED, true).apply();
        }
        handler.postDelayed(this::refreshUi, 250L);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handler.postDelayed(this::refreshUi, 300L);
    }

    /** 固定顶栏 + 可滚动内容；进度条与 dsh-go 的逐项引导保持一致。 */
    private void buildShell() {
        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setBackgroundColor(COLOR_BG);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(16), dp(10), dp(16), dp(8));
        TextView back = text("‹", 34, COLOR_TEXT, Typeface.NORMAL);
        back.setGravity(Gravity.CENTER);
        back.setContentDescription("返回");
        back.setOnClickListener(v -> finish());
        top.addView(back, new LinearLayout.LayoutParams(dp(44), dp(48)));

        LinearLayout center = new LinearLayout(this);
        center.setOrientation(LinearLayout.HORIZONTAL);
        center.setGravity(Gravity.CENTER);
        progressSegments.clear();
        for (int i = 0; i < GuideStep.values().length; i++) {
            View segment = new View(this);
            LinearLayout.LayoutParams segmentLp = new LinearLayout.LayoutParams(0, dp(3), 1f);
            if (i > 0) segmentLp.leftMargin = dp(6);
            segment.setBackground(rounded(Color.rgb(48, 64, 83), 2));
            center.addView(segment, segmentLp);
            progressSegments.add(segment);
        }
        LinearLayout.LayoutParams centerLp = new LinearLayout.LayoutParams(0, dp(20), 1f);
        centerLp.leftMargin = dp(10);
        centerLp.rightMargin = dp(10);
        top.addView(center, centerLp);

        progressCounter = text("全检", 13, COLOR_MUTED, Typeface.BOLD);
        progressCounter.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        top.addView(progressCounter, new LinearLayout.LayoutParams(dp(62), dp(48)));
        screen.addView(top);

        scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setClipToPadding(false);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(22), dp(10), dp(22), dp(30));
        scrollView.addView(content, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));
        screen.addView(scrollView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        setContentView(screen);
    }

    private void refreshUi() {
        if (!resumed) return;
        PermissionAudit.Result result = PermissionAudit.evaluate(
                PermissionStatus.read(this, null));

        long now = SystemClock.elapsedRealtime();
        if (doneStep != null) {
            if (now < doneUntilMs) {
                String signature = "done:" + doneStep.name();
                if (!signature.equals(lastRenderSignature)) {
                    lastRenderSignature = signature;
                    renderStep(doneStep, result, true);
                }
                return;
            }
            doneStep = null;
            activeStep = null;
            lastRenderedStep = null;
            lastRenderSignature = null;
        }

        if (lastRenderedStep != null && stepState(lastRenderedStep, result) == PermissionAudit.State.READY) {
            doneStep = lastRenderedStep;
            doneUntilMs = now + DONE_HOLD_MS;
            lastRenderSignature = "done:" + doneStep.name();
            renderStep(doneStep, result, true);
            handler.removeCallbacks(ticker);
            handler.postDelayed(ticker, DONE_HOLD_MS + 40L);
            return;
        }

        GuideStep next = resolveNext(result);
        if (next == null) {
            activeStep = null;
            lastRenderedStep = null;
            prefs.edit().remove(PermissionStatus.KEY_ACTIVE_STEP).apply();
            String signature = "summary:" + resultSignature(result);
            if (signature.equals(lastRenderSignature)) return;
            lastRenderSignature = signature;
            renderSummary(result);
        } else {
            activeStep = next;
            lastRenderedStep = next;
            prefs.edit().putString(PermissionStatus.KEY_ACTIVE_STEP, next.name()).apply();
            String signature = "step:" + next.name() + ":" + stepState(next, result)
                    + ":" + resultSignature(result);
            if (signature.equals(lastRenderSignature)) return;
            lastRenderSignature = signature;
            renderStep(next, result, false);
        }
    }

    private String resultSignature(PermissionAudit.Result result) {
        StringBuilder signature = new StringBuilder();
        for (PermissionAudit.Row row : result.rows) {
            signature.append(row.item.name()).append('=').append(row.state.name()).append(';');
        }
        return signature.toString();
    }

    private GuideStep resolveNext(PermissionAudit.Result result) {
        Set<String> skipped = skippedSteps();
        if (activeStep != null && !skipped.contains(activeStep.name())
                && stepState(activeStep, result) != PermissionAudit.State.READY) {
            return activeStep;
        }
        for (GuideStep step : GuideStep.values()) {
            if (!skipped.contains(step.name())
                    && stepState(step, result) != PermissionAudit.State.READY) {
                return step;
            }
        }
        return null;
    }

    private PermissionAudit.State stepState(GuideStep step, PermissionAudit.Result result) {
        switch (step) {
            case BLUETOOTH: {
                PermissionAudit.Row permission = result.row(PermissionAudit.Item.BLUETOOTH_PERMISSION);
                PermissionAudit.Row enabled = result.row(PermissionAudit.Item.BLUETOOTH_ON);
                return permission != null && enabled != null && permission.isReady() && enabled.isReady()
                        ? PermissionAudit.State.READY : PermissionAudit.State.ACTION_REQUIRED;
            }
            case NOTIFICATIONS:
                return stateOf(result.row(PermissionAudit.Item.NOTIFICATIONS));
            case LISTENER:
                return stateOf(result.row(PermissionAudit.Item.LISTENER));
            case BATTERY:
                return stateOf(result.row(PermissionAudit.Item.BATTERY));
            case VENDOR_BACKGROUND:
                return stateOf(result.row(PermissionAudit.Item.VENDOR_BACKGROUND));
            default:
                return PermissionAudit.State.ACTION_REQUIRED;
        }
    }

    private PermissionAudit.State stateOf(PermissionAudit.Row row) {
        return row == null ? PermissionAudit.State.ACTION_REQUIRED : row.state;
    }

    /** 单项引导页：大标题、用途、系统路径、真实状态、一个主操作和次要入口。 */
    private void renderStep(GuideStep step, PermissionAudit.Result result, boolean done) {
        content.removeAllViews();
        scrollToTop();
        PermissionAudit.State state = stepState(step, result);
        int position = step.ordinal() + 1;
        updateProgress(step, result, done ? null : step);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout tile = iconTile(done ? R.drawable.ic_permission_done : step.iconRes,
                done ? COLOR_GREEN_SOFT : COLOR_ACCENT_SOFT,
                done ? COLOR_GREEN : COLOR_ACCENT, 58);
        header.addView(tile);
        LinearLayout.LayoutParams spacerLp = new LinearLayout.LayoutParams(0, 1, 1f);
        header.addView(new View(this), spacerLp);
        header.addView(text(String.format(Locale.ROOT, "%d / %d", position, GuideStep.values().length),
                13, COLOR_MUTED, Typeface.BOLD));
        content.addView(header);
        content.addView(space(22));

        content.addView(text(step.title, 27, COLOR_TEXT, Typeface.BOLD));
        content.addView(space(7));
        content.addView(text(step.description, 15, COLOR_MUTED, Typeface.NORMAL));
        content.addView(space(9));
        content.addView(text("位置：" + step.path, 12, COLOR_ACCENT, Typeface.NORMAL));
        content.addView(space(18));

        if (done) {
            content.addView(infoCard("已完成", "已读取到最新状态，正在进入下一项。",
                    COLOR_GREEN_SOFT, COLOR_GREEN));
            return;
        }

        String status;
        int statusColor;
        int statusBg;
        if (state == PermissionAudit.State.WAITING) {
            status = "已授权 · 正在重连";
            statusColor = COLOR_AMBER;
            statusBg = COLOR_AMBER_SOFT;
        } else if (state == PermissionAudit.State.MANUAL) {
            status = "无法自动读取 · 需手动确认";
            statusColor = COLOR_AMBER;
            statusBg = COLOR_AMBER_SOFT;
        } else if (state == PermissionAudit.State.READY) {
            status = "已就绪";
            statusColor = COLOR_GREEN;
            statusBg = COLOR_GREEN_SOFT;
        } else {
            status = "待处理";
            statusColor = COLOR_RED;
            statusBg = COLOR_RED_SOFT;
        }
        content.addView(statusPill(status, statusColor, statusBg));
        content.addView(space(14));

        if (step == GuideStep.LISTENER) {
            content.addView(listenerExplanation(result));
        } else if (step == GuideStep.BLUETOOTH) {
            PermissionAudit.Row permission = result.row(PermissionAudit.Item.BLUETOOTH_PERMISSION);
            PermissionAudit.Row enabled = result.row(PermissionAudit.Item.BLUETOOTH_ON);
            if (permission != null && !permission.isReady()) {
                content.addView(infoCard("为什么需要",
                        "Android 12 以后，扫描附近蓝牙设备与读取连接态 RSSI 都需要“附近的设备”权限。",
                        COLOR_CARD_SOFT, COLOR_TEXT));
            } else if (enabled != null && !enabled.isReady()) {
                content.addView(infoCard("蓝牙当前关闭",
                        "权限已经具备，只需在系统弹窗中打开蓝牙。",
                        COLOR_CARD_SOFT, COLOR_TEXT));
            }
        } else if (step == GuideStep.NOTIFICATIONS) {
            boolean requested = prefs.getBoolean(PermissionStatus.KEY_NOTIFICATION_REQUESTED, false);
            if (requested && state != PermissionAudit.State.READY) {
                content.addView(infoCard("系统不再弹窗？",
                        "Android 只允许有限次权限弹窗。若已经拒绝过，请从应用通知设置页手动开启。",
                        COLOR_CARD_SOFT, COLOR_TEXT));
            }
        } else if (step == GuideStep.VENDOR_BACKGROUND) {
            content.addView(infoCard("vivo / OriginOS",
                    "需要确认两处：后台高耗电允许，自启动允许。不同系统版本入口名称可能略有差异。\n\n"
                            + "厂商开关没有公开读取接口，所以这里只能记录“已手动确认”，不会伪装成自动检测成功。",
                    COLOR_CARD_SOFT, COLOR_TEXT));
        }
        content.addView(space(24));

        Button primary = primaryButton(primaryLabel(step, result));
        primary.setOnClickListener(v -> runPrimary(step, result));
        content.addView(primary);

        LinearLayout secondary = new LinearLayout(this);
        secondary.setOrientation(LinearLayout.HORIZONTAL);
        secondary.setGravity(Gravity.CENTER);
        if (step == GuideStep.LISTENER) {
            addSecondary(secondary, "查看修复步骤", v -> showListenerHelp(result));
            addSecondary(secondary, "稍后处理", v -> skipStep(step));
        } else if (step == GuideStep.VENDOR_BACKGROUND) {
            addSecondary(secondary, "我已设置", v -> {
                PermissionStatus.setVendorBackgroundConfirmed(this, true);
                refreshUi();
            });
            addSecondary(secondary, "稍后处理", v -> skipStep(step));
        } else {
            addSecondary(secondary, "稍后处理", v -> skipStep(step));
        }
        if (secondary.getChildCount() > 0) {
            LinearLayout.LayoutParams secondaryLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
            secondaryLp.topMargin = dp(8);
            content.addView(secondary, secondaryLp);
        }
    }

    private View listenerExplanation(PermissionAudit.Result result) {
        PermissionAudit.Row row = result.row(PermissionAudit.Item.LISTENER);
        if (row != null && row.isReady()) {
            return infoCard("监听在线", "能读取银河通知，也能复用官方解锁/上锁按钮。",
                    COLOR_GREEN_SOFT, COLOR_GREEN);
        }
        if (row != null && row.isWaiting()) {
            return infoCard("授权没有被撤销",
                    "系统设置里的允许记录仍在，只是 NotificationListenerService 尚未绑定。"
                            + "覆盖安装、强行停止或低内存回收后很常见。App 正在每 2 秒请求系统重绑，"
                            + "通常会在几秒内自动恢复。",
                    COLOR_AMBER_SOFT, COLOR_AMBER);
        }
        if (KeyNotificationBridgeService.isNotificationListenerEnabled(this)) {
            return infoCard("授权还在，监听未绑定",
                    "“已允许”只代表系统保留了授权记录，不代表当前进程已经收到通知。"
                            + "如果自动重试超过 10 秒仍未恢复，请在系统页把“银河无感钥匙通知桥接”"
                            + "关闭一次再打开；这不是重新授权，而是强制 vivo 重新绑定服务。",
                    COLOR_RED_SOFT, COLOR_RED);
        }
        return infoCard("需要开启通知使用权",
                "请在系统页找到“银河无感钥匙通知桥接”并允许。"
                        + "本应用只读取银河通知的连接状态和解锁/上锁按钮，不读取或保存账号凭证。",
                COLOR_CARD_SOFT, COLOR_TEXT);
    }

    /** 汇总页：必要项、保活建议和运行条件一次看清，点击未完成项可继续处理。 */
    private void renderSummary(PermissionAudit.Result result) {
        content.removeAllViews();
        scrollToTop();
        updateProgress(null, result, null);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.addView(iconTile(result.essentialReady() ? R.drawable.ic_permission_done
                        : R.drawable.ic_permission_listener,
                result.essentialReady() ? COLOR_GREEN_SOFT : COLOR_ACCENT_SOFT,
                result.essentialReady() ? COLOR_GREEN : COLOR_ACCENT, 58));
        content.addView(header);
        content.addView(space(22));
        content.addView(text(result.headline(), 27, COLOR_TEXT, Typeface.BOLD));
        content.addView(space(7));
        content.addView(text(result.essentialReady()
                        ? "必要项已通过。建议继续确认保活设置，减少息屏后被系统清理。"
                        : "按顺序处理未完成项。每次从系统页返回后，本页都会自动重新读取真实状态。",
                15, COLOR_MUTED, Typeface.NORMAL));
        content.addView(space(12));
        content.addView(statusPill(result.summaryLine(),
                result.essentialReady() ? COLOR_GREEN : COLOR_AMBER,
                result.essentialReady() ? COLOR_GREEN_SOFT : COLOR_AMBER_SOFT));
        content.addView(space(20));

        content.addView(sectionTitle("必要权限与运行"));
        content.addView(space(8));
        LinearLayout requiredCard = card();
        for (PermissionAudit.Item item : PermissionAudit.Item.values()) {
            if (!item.isRequired()) continue;
            addAuditRow(requiredCard, result.row(item));
        }
        content.addView(requiredCard);
        content.addView(space(18));

        content.addView(sectionTitle("保活建议"));
        content.addView(space(8));
        LinearLayout recommendedCard = card();
        for (PermissionAudit.Item item : PermissionAudit.Item.values()) {
            if (item.isRequired()) continue;
            addAuditRow(recommendedCard, result.row(item));
        }
        content.addView(recommendedCard);
        content.addView(space(22));

        Button primary = primaryButton(nextGuideStep(result) == null ? "重新全检" : "继续处理");
        primary.setOnClickListener(v -> {
            GuideStep next = nextGuideStep(result);
            if (next == null) {
                refreshUi();
            } else {
                activeStep = next;
                lastRenderedStep = null;
                doneStep = null;
                refreshUi();
            }
        });
        content.addView(primary);

        LinearLayout secondary = new LinearLayout(this);
        secondary.setOrientation(LinearLayout.HORIZONTAL);
        secondary.setGravity(Gravity.CENTER);
        if (!skippedSteps().isEmpty()) {
            addSecondary(secondary, "恢复跳过项", v -> {
                prefs.edit().remove(PermissionStatus.KEY_SKIPPED_STEPS).apply();
                lastRenderedStep = null;
                activeStep = null;
                lastRenderSignature = null;
                refreshUi();
            });
        }
        addSecondary(secondary, "完成", v -> finish());
        LinearLayout.LayoutParams secondaryLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        secondaryLp.topMargin = dp(8);
        content.addView(secondary, secondaryLp);
    }

    private void addAuditRow(LinearLayout parent, PermissionAudit.Row row) {
        if (row == null) return;
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setPadding(dp(2), dp(10), dp(2), dp(10));
        if (!row.isReady()) {
            item.setClickable(true);
            item.setFocusable(true);
            item.setBackground(rounded(Color.rgb(25, 40, 62), 12));
            item.setPadding(dp(12), dp(10), dp(12), dp(10));
            item.setOnClickListener(v -> handleAuditRow(row));
        }

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView mark = text(row.isReady() ? "✓" : row.isWaiting() ? "…" : "!",
                15, row.isReady() ? COLOR_GREEN : row.isWaiting() ? COLOR_AMBER : COLOR_RED,
                Typeface.BOLD);
        mark.setGravity(Gravity.CENTER);
        mark.setBackground(rounded(row.isReady() ? COLOR_GREEN_SOFT
                : row.isWaiting() ? COLOR_AMBER_SOFT : COLOR_RED_SOFT, 10));
        top.addView(mark, new LinearLayout.LayoutParams(dp(30), dp(30)));
        TextView title = text(row.item.title, 15, COLOR_TEXT, Typeface.BOLD);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        titleLp.leftMargin = dp(11);
        top.addView(title, titleLp);
        TextView status = text(row.statusLabel(), 12,
                row.isReady() ? COLOR_GREEN : row.isWaiting() ? COLOR_AMBER : COLOR_RED,
                Typeface.BOLD);
        top.addView(status);
        item.addView(top);

        TextView detail = text(row.item.description, 12, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams detailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        detailLp.leftMargin = dp(41);
        detailLp.topMargin = dp(4);
        item.addView(detail, detailLp);
        parent.addView(item, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    private void handleAuditRow(PermissionAudit.Row row) {
        switch (row.item) {
            case BLUETOOTH_PERMISSION:
            case BLUETOOTH_ON:
                activate(GuideStep.BLUETOOTH);
                break;
            case NOTIFICATIONS:
                activate(GuideStep.NOTIFICATIONS);
                break;
            case LISTENER:
                activate(GuideStep.LISTENER);
                break;
            case BATTERY:
                activate(GuideStep.BATTERY);
                break;
            case VENDOR_BACKGROUND:
                activate(GuideStep.VENDOR_BACKGROUND);
                break;
            case SERVICE:
                new VehicleIdentityStore(this).setServiceEnabled(true);
                startPassiveServiceIfEnabled();
                toast("已开启无感服务总开关，正在等待服务恢复");
                handler.postDelayed(this::refreshUi, 500L);
                break;
            case GEELY_APP:
                if (GeelyBridge.isGeelyInstalled(this)) {
                    try {
                        startActivity(getPackageManager().getLaunchIntentForPackage(GeelyBridge.GEELY_PKG));
                    } catch (Throwable unavailable) {
                        toast("无法打开银河 App");
                    }
                } else {
                    toast("请先安装并登录银河 App");
                }
                break;
            default:
                break;
        }
    }

    private void activate(GuideStep step) {
        activeStep = step;
        lastRenderedStep = null;
        doneStep = null;
        lastRenderSignature = null;
        prefs.edit().putString(PermissionStatus.KEY_ACTIVE_STEP, step.name()).apply();
        refreshUi();
    }

    private GuideStep nextGuideStep(PermissionAudit.Result result) {
        Set<String> skipped = skippedSteps();
        for (GuideStep step : GuideStep.values()) {
            if (!skipped.contains(step.name())
                    && stepState(step, result) != PermissionAudit.State.READY) {
                return step;
            }
        }
        return null;
    }

    private String primaryLabel(GuideStep step, PermissionAudit.Result result) {
        switch (step) {
            case BLUETOOTH:
                return PermissionStatus.hasBluetoothPermissions(this) ? "打开蓝牙" : "授予蓝牙权限";
            case NOTIFICATIONS:
                return prefs.getBoolean(PermissionStatus.KEY_NOTIFICATION_REQUESTED, false)
                        ? "打开通知设置" : "允许系统通知";
            case LISTENER: {
                PermissionAudit.Row row = result.row(PermissionAudit.Item.LISTENER);
                if (row != null && row.isWaiting()) return "立即重试绑定";
                return KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                        ? "修复通知绑定" : "开启通知使用权";
            }
            case BATTERY:
                return PermissionStatus.isBatteryWhitelisted(this) ? "打开电池设置" : "请求电池白名单";
            case VENDOR_BACKGROUND:
                return "打开应用设置";
            default:
                return "去设置";
        }
    }

    private void runPrimary(GuideStep step, PermissionAudit.Result result) {
        switch (step) {
            case BLUETOOTH:
                requestBluetoothOrEnable();
                break;
            case NOTIFICATIONS:
                requestNotificationPermission();
                break;
            case LISTENER: {
                PermissionAudit.Row row = result.row(PermissionAudit.Item.LISTENER);
                if (row != null && row.isWaiting()) {
                    KeyNotificationBridgeService.requestImmediateRecovery(this);
                    toast("已立即请求系统重新绑定通知桥接");
                    handler.postDelayed(this::refreshUi, 500L);
                } else {
                    openNotificationListenerSettings();
                }
                break;
            }
            case BATTERY:
                requestBatteryWhitelist();
                break;
            case VENDOR_BACKGROUND:
                openAppDetails();
                break;
            default:
                break;
        }
    }

    private void requestBluetoothOrEnable() {
        if (!PermissionStatus.hasBluetoothPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                }, REQUEST_BLUETOOTH);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_BLUETOOTH);
            }
            return;
        }
        try {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
        } catch (Throwable unavailable) {
            openBluetoothSettings();
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) {
            refreshUi();
            return;
        }
        boolean requested = prefs.getBoolean(PermissionStatus.KEY_NOTIFICATION_REQUESTED, false);
        if (!requested || shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATIONS);
        } else {
            openNotificationSettings();
        }
    }

    private void requestBatteryWhitelist() {
        if (PermissionStatus.isBatteryWhitelisted(this)) {
            try {
                startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            } catch (Throwable ignored) {
                openAppDetails();
            }
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable unavailable) {
            openBatterySettings();
        }
    }

    /** 直接定位到本组件；不同 ROM 不支持详情页时回退到通知使用权列表。 */
    private void openNotificationListenerSettings() {
        prefs.edit().putString(PermissionStatus.KEY_ACTIVE_STEP, GuideStep.LISTENER.name()).apply();
        Intent detail = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS);
        detail.putExtra(Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                new ComponentName(this, KeyNotificationBridgeService.class).flattenToString());
        try {
            startActivity(detail);
            toast("请关闭再开启“银河无感钥匙通知桥接”");
        } catch (Throwable unavailable) {
            try {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                toast("请在通知使用权列表中关闭再开启本应用");
            } catch (Throwable ignored) {
                toast("无法打开系统通知使用权设置");
            }
        }
    }

    private void showListenerHelp(PermissionAudit.Result result) {
        PermissionAudit.Row row = result.row(PermissionAudit.Item.LISTENER);
        boolean boundButDown = row != null && !row.isReady()
                && KeyNotificationBridgeService.isNotificationListenerEnabled(this);
        String message;
        if (boundButDown) {
            message = "系统里看到“已允许”并不等于监听在线。\n\n"
                    + "1. 打开本应用的“通知使用权”详情页；\n"
                    + "2. 找到“银河无感钥匙通知桥接”；\n"
                    + "3. 把开关关闭，等 1 秒，再打开；\n"
                    + "4. 返回本页，等待状态变成“已授权 · 监听在线”。\n\n"
                    + "这一步只是让 vivo 重新绑定服务，不会清数据，也不是重新授权。";
        } else {
            message = "1. 打开“通知使用权”；\n"
                    + "2. 找到“银河无感钥匙通知桥接”；\n"
                    + "3. 打开开关；\n"
                    + "4. 返回本页，会自动复检。\n\n"
                    + "该权限只用于读取银河通知的连接状态和解锁/上锁按钮。";
        }
        new AlertDialog.Builder(this)
                .setTitle("通知桥接怎么修复")
                .setMessage(message)
                .setNegativeButton("关闭", null)
                .setPositiveButton("打开系统页", (dialog, which) -> openNotificationListenerSettings())
                .show();
    }

    private void skipStep(GuideStep step) {
        Set<String> skipped = skippedSteps();
        skipped.add(step.name());
        prefs.edit().putStringSet(PermissionStatus.KEY_SKIPPED_STEPS, skipped).apply();
        if (activeStep == step) activeStep = null;
        lastRenderedStep = null;
        doneStep = null;
        lastRenderSignature = null;
        refreshUi();
    }

    private Set<String> skippedSteps() {
        return new HashSet<>(prefs.getStringSet(PermissionStatus.KEY_SKIPPED_STEPS,
                java.util.Collections.emptySet()));
    }

    private void updateProgress(GuideStep active, PermissionAudit.Result result, GuideStep current) {
        if (progressCounter == null || progressSegments.isEmpty()) return;
        Set<String> skipped = skippedSteps();
        for (int i = 0; i < GuideStep.values().length; i++) {
            GuideStep step = GuideStep.values()[i];
            PermissionAudit.State state = stepState(step, result);
            int color;
            if (state == PermissionAudit.State.READY) {
                color = COLOR_GREEN;
            } else if (skipped.contains(step.name())) {
                color = Color.rgb(75, 91, 111);
            } else if (step == active || step == current) {
                color = COLOR_ACCENT;
            } else {
                color = Color.rgb(48, 64, 83);
            }
            progressSegments.get(i).setBackground(rounded(color, 2));
        }
        if (active == null) {
            progressCounter.setText(result.essentialReady() ? "已就绪" : "全检");
        } else {
            progressCounter.setText(String.format(Locale.ROOT, "%d / %d",
                    active.ordinal() + 1, GuideStep.values().length));
        }
    }

    private void startPassiveServiceIfEnabled() {
        if (!new VehicleIdentityStore(this).isServiceEnabled()) return;
        try {
            Intent service = new Intent(this, PassiveKeyService.class);
            service.setAction(PassiveKeyService.ACTION_START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(service);
            } else {
                startService(service);
            }
        } catch (Throwable ignored) {
        }
    }

    private void openNotificationSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        } catch (Throwable unavailable) {
            openAppDetails();
        }
    }

    private void openBluetoothSettings() {
        try {
            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        } catch (Throwable ignored) {
            openAppDetails();
        }
    }

    private void openBatterySettings() {
        try {
            startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
        } catch (Throwable ignored) {
            openAppDetails();
        }
    }

    private void openAppDetails() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable ignored) {
            toast("无法打开应用设置");
        }
    }

    private TextView sectionTitle(String value) {
        return text(value, 14, COLOR_ACCENT, Typeface.BOLD);
    }

    private LinearLayout card() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(14), dp(6), dp(14), dp(6));
        layout.setBackground(rounded(COLOR_CARD, 18));
        return layout;
    }

    private View infoCard(String title, String body, int background, int tint) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(14), dp(15), dp(14));
        card.setBackground(rounded(background, 16));
        card.addView(text(title, 15, tint, Typeface.BOLD));
        TextView bodyView = text(body, 13, COLOR_TEXT, Typeface.NORMAL);
        bodyView.setLineSpacing(dp(2), 1.08f);
        LinearLayout.LayoutParams bodyLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bodyLp.topMargin = dp(7);
        card.addView(bodyView, bodyLp);
        return card;
    }

    private TextView statusPill(String label, int textColor, int background) {
        TextView pill = text(label, 13, textColor, Typeface.BOLD);
        pill.setGravity(Gravity.CENTER_VERTICAL);
        pill.setPadding(dp(12), dp(7), dp(12), dp(7));
        pill.setBackground(rounded(background, 999));
        return pill;
    }

    private LinearLayout iconTile(int iconRes, int background, int tint, int sizeDp) {
        LinearLayout tile = new LinearLayout(this);
        tile.setGravity(Gravity.CENTER);
        tile.setBackground(rounded(background, 16));
        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        icon.setColorFilter(tint);
        tile.addView(icon, new LinearLayout.LayoutParams(dp(28), dp(28)));
        tile.setLayoutParams(new LinearLayout.LayoutParams(dp(sizeDp), dp(sizeDp)));
        return tile;
    }

    private Button primaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(15);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setAllCaps(false);
        button.setTextColor(Color.rgb(7, 22, 34));
        button.setBackgroundTintList(ColorStateList.valueOf(COLOR_ACCENT));
        button.setMinHeight(dp(50));
        button.setPadding(dp(18), 0, dp(18), 0);
        return button;
    }

    private void addSecondary(LinearLayout row, String label, View.OnClickListener listener) {
        TextView action = text(label, 14, COLOR_MUTED, Typeface.NORMAL);
        action.setGravity(Gravity.CENTER);
        action.setClickable(true);
        action.setFocusable(true);
        action.setOnClickListener(listener);
        action.setPadding(dp(8), 0, dp(8), 0);
        row.addView(action, new LinearLayout.LayoutParams(0, dp(48), 1f));
    }

    private TextView text(String value, int sp, int color, int style) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.DEFAULT, style);
        return view;
    }

    private View space(int dp) {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(1, dp(dp)));
        return view;
    }

    private GradientDrawable rounded(int color, int radiusDp) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(dp(radiusDp));
        return background;
    }

    private GuideStep parseStep(String name) {
        if (name == null) return null;
        try {
            return GuideStep.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void scrollToTop() {
        if (scrollView == null) return;
        scrollView.post(() -> scrollView.scrollTo(0, 0));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
