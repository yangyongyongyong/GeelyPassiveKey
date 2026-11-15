package com.luca.geelykeyradar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 极简配置页。
 *
 * 只显示运行状态、本车识别、自动开关、手动链路测试、必需权限和最近日志。
 */
public class MainActivity extends Activity {

    private static final int REQUEST_BT_PERMISSIONS = 1001;
    private static final int REQUEST_LOG_EXPORT = 1002;
    private static final int REQUEST_ACTION_SNAPSHOT_EXPORT = 1004;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1003;
    private static final ExecutorService LOG_EXPORT_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final AtomicBoolean LOG_EXPORT_BUSY = new AtomicBoolean();
    private static final int TAB_OVERVIEW = 0;
    private static final int TAB_CONTROLS = 1;
    private static final int TAB_MAINTENANCE = 2;
    private static final int COLOR_BG = Color.rgb(8, 13, 18);
    private static final int COLOR_CARD = Color.rgb(17, 24, 32);
    private static final int COLOR_SURFACE_VARIANT = Color.rgb(24, 34, 45);
    private static final int COLOR_SURFACE_HIGH = Color.rgb(30, 42, 54);
    private static final int COLOR_OUTLINE = Color.rgb(43, 60, 74);
    private static final int COLOR_TEXT = Color.rgb(232, 238, 245);
    private static final int COLOR_MUTED = Color.rgb(156, 171, 186);
    private static final int COLOR_PRIMARY = Color.rgb(102, 211, 250);
    private static final int COLOR_ON_PRIMARY = Color.rgb(5, 45, 59);
    private static final int COLOR_PRIMARY_CONTAINER = Color.rgb(16, 59, 76);
    private static final int COLOR_ON_PRIMARY_CONTAINER = Color.rgb(186, 237, 255);
    private static final int COLOR_ACCENT = COLOR_PRIMARY;
    private static final int COLOR_GREEN = Color.rgb(101, 214, 163);
    private static final int COLOR_GREEN_CONTAINER = Color.rgb(20, 59, 43);
    private static final int COLOR_RED = Color.rgb(255, 154, 168);
    private static final int COLOR_RED_CONTAINER = Color.rgb(68, 31, 38);
    private static final int COLOR_AMBER = Color.rgb(247, 198, 91);
    private static final int COLOR_AMBER_CONTAINER = Color.rgb(58, 47, 20);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refreshUi();
            handler.postDelayed(this, 500L);
        }
    };

    private PassiveKeyService service;
    private boolean bound = false;
    private boolean binding = false;
    private boolean settingSwitches = false;

    private TextView statusView;
    private TextView topStatusChip;
    private LinearLayout liveActivityCard;
    private TextView liveActivityBadgeView;
    private TextView liveActivityTitleView;
    private TextView liveActivityDetailView;
    private TextView heroTitleView;
    private TextView heroDetailView;
    private TextView heroSignalView;
    private TextView heroSignalUnitView;
    private TextView heroZoneView;
    private TextView overviewVehicleTitleView;
    private TextView overviewVehicleDetailView;
    private TextView overviewVehicleMetaView;
    private TextView overviewHealthTitleView;
    private TextView overviewHealthDetailView;
    private LinearLayout overviewHealthCard;
    private RssiChartView rssiChartView;
    private TextView rssiChartSummaryView;
    private TextView vehicleParamView;
    private TextView identityView;
    private TextView thresholdView;
    private TextView thresholdSourceView;
    private TextView thresholdDefaultsView;
    private TextView permissionView;
    private TextView updateStatusView;
    private TextView logView;
    private Switch serviceMasterSwitch;
    private Switch autoUnlockSwitch;
    private Switch autoLockSwitch;
    private Switch blockChargingSwitch;
    private Button manualUnlockButton;
    private Button manualLockButton;
    private TextView manualControlView;
    private Button exportLogButton;
    private Button exportActionSnapshotsButton;
    private TextView actionSnapshotStatusView;
    private Button permissionGuideButton;
    private Button updateActionButton;
    private Switch autoUpdateSwitch;
    private ScrollView mainScrollView;
    private LinearLayout overviewPage;
    private LinearLayout controlsPage;
    private LinearLayout maintenancePage;
    private LinearLayout topAppBar;
    private LinearLayout bottomNavigationBar;
    private LinearLayout overviewTab;
    private LinearLayout controlsTab;
    private LinearLayout maintenanceTab;
    private FrameLayout overviewTabIconContainer;
    private FrameLayout controlsTabIconContainer;
    private FrameLayout maintenanceTabIconContainer;
    private ImageView overviewTabIcon;
    private ImageView controlsTabIcon;
    private ImageView maintenanceTabIcon;
    private TextView overviewTabLabel;
    private TextView controlsTabLabel;
    private TextView maintenanceTabLabel;
    private View maintenanceBadge;
    private int activeTab = TAB_OVERVIEW;
    private boolean settingUpdateSwitch;
    private boolean pendingInstallAfterPermission;
    private String exportSnapshot = "service_snapshot=unavailable";
    private String actionSnapshotExportSnapshot = "service_snapshot=unavailable";
    private java.util.List<LiveActivityTrace.Entry> currentLiveTrace = java.util.Collections.emptyList();
    private Dialog thresholdSettingsDialog;
    private VehicleParameterProbe parameterProbe;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            if (binder instanceof PassiveKeyService.LocalBinder) {
                service = ((PassiveKeyService.LocalBinder) binder).getService();
            }
            bound = true;
            binding = false;
            refreshUi();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            bound = false;
            binding = false;
            refreshUi();
        }
    };

    /** 初始化界面及日志仓库，恢复尚未完成的导出快照。 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiagnosticLog.initialize(this);
        parameterProbe = new VehicleParameterProbe(this);
        if (savedInstanceState != null) {
            exportSnapshot = savedInstanceState.getString("diagnostic_snapshot", exportSnapshot);
            actionSnapshotExportSnapshot = savedInstanceState.getString("action_snapshot_export", actionSnapshotExportSnapshot);
        }
        buildUi();
        configureEdgeToEdge();
        if (savedInstanceState != null) {
            setTab(savedInstanceState.getInt("active_tab", TAB_OVERVIEW));
        }
        handler.postDelayed(() -> handleUpdateIntent(getIntent()), 350L);
    }

    /** Android 15/16 默认 edge-to-edge；统一由窗口 insets 给顶栏和底栏留安全区。 */
    private void configureEdgeToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Api30Insets.configure(getWindow());
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().requestApplyInsets();
    }

    /** API 30 类型引用隔离，避免 Android 10 在加载 MainActivity 时解析新类。 */
    private static final class Api30Insets {
        static void configure(Window window) {
            window.setDecorFitsSystemWindows(false);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            android.view.WindowInsetsController controller =
                    window.getDecorView().getWindowInsetsController();
            if (controller != null) {
                controller.setSystemBarsAppearance(0,
                        android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                                | android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
            }
        }

        static int[] systemBars(WindowInsets insets) {
            Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
            return new int[]{bars.top, bars.bottom};
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPassiveKeyService();
        bindPassiveKeyService();
        if (KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                && !KeyNotificationBridgeService.isListenerConnected()) {
            KeyNotificationBridgeService.ensureRebound(this);
            KeyNotificationBridgeService.requestImmediateRecovery(this);
        }
        parameterProbe.start();
        if (pendingInstallAfterPermission && UpdateManager.canInstallPackages(this)) {
            pendingInstallAfterPermission = false;
            handler.postDelayed(this::promptInstallUpdate, 250L);
        }
        UpdateManager.maybeAutoCheck(this);
        handleUpdateIntent(getIntent());
        handler.removeCallbacks(refreshRunnable);
        handler.post(refreshRunnable);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(refreshRunnable);
        if (bound || binding) {
            try {
                unbindService(connection);
            } catch (Throwable ignored) {
            }
            bound = false;
            binding = false;
            service = null;
        }
        super.onPause();
    }

    /** 页面销毁时关闭所属设置窗口，不保留旧Activity或尚未保存的输入。 */
    @Override
    protected void onDestroy() {
        if (thresholdSettingsDialog != null) {
            thresholdSettingsDialog.dismiss();
            thresholdSettingsDialog = null;
        }
        if (parameterProbe != null) parameterProbe.stop();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleUpdateIntent(intent);
    }

    /** 从实际安装包读取版本名和构建号；界面显示不硬编码，读取失败不影响服务。 */
    private String installedVersionLabel() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName == null ? "" : info.versionName.trim();
            String displayName = versionName.isEmpty() ? "未知"
                    : (versionName.startsWith("v") ? versionName : "v" + versionName);
            return "版本：" + displayName + "（构建 " + info.getLongVersionCode() + "）";
        } catch (PackageManager.NameNotFoundException | RuntimeException unavailable) {
            return "版本：读取失败";
        }
    }

    /**
     * Material 3 信息架构：固定顶部栏 + 三页底部导航。
     * 概览只放当前状态与快捷动作，控制页放自动动作，维护页放权限、更新和诊断。
     */
    private void buildUi() {
        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setBackgroundColor(COLOR_BG);

        topAppBar = buildTopAppBar();
        screen.addView(topAppBar, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        mainScrollView = new ScrollView(this);
        mainScrollView.setFillViewport(true);
        mainScrollView.setClipToPadding(false);
        mainScrollView.setBackgroundColor(COLOR_BG);
        LinearLayout pageRoot = new LinearLayout(this);
        pageRoot.setOrientation(LinearLayout.VERTICAL);
        pageRoot.setPadding(dp(18), dp(2), dp(18), dp(24));

        overviewPage = page();
        controlsPage = page();
        maintenancePage = page();
        controlsPage.setVisibility(View.GONE);
        maintenancePage.setVisibility(View.GONE);
        pageRoot.addView(overviewPage);
        pageRoot.addView(controlsPage);
        pageRoot.addView(maintenancePage);

        buildOverviewContent();
        buildControlsContent();
        buildMaintenanceContent();

        mainScrollView.addView(pageRoot, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));
        screen.addView(mainScrollView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        bottomNavigationBar = buildBottomNavigation();
        screen.addView(bottomNavigationBar, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(screen);
        screen.setOnApplyWindowInsetsListener((view, insets) -> {
            int top;
            int bottom;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                int[] bars = Api30Insets.systemBars(insets);
                top = bars[0];
                bottom = bars[1];
            } else {
                top = insets.getSystemWindowInsetTop();
                bottom = insets.getSystemWindowInsetBottom();
            }
            topAppBar.setPadding(dp(20), top + dp(8), dp(20), dp(8));
            bottomNavigationBar.setPadding(0, 0, 0, bottom);
            return insets;
        });
        screen.requestApplyInsets();
        setTab(TAB_OVERVIEW);
    }

    private LinearLayout buildTopAppBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.VERTICAL);
        bar.setPadding(dp(20), dp(8), dp(20), dp(8));

        LinearLayout titleRow = new LinearLayout(this);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titlesLp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        titles.addView(text("银河无感钥匙", 22, COLOR_TEXT, Typeface.BOLD));
        TextView subtitle = text("无感进出 · 靠近解锁 / 离开上锁", 12, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams subtitleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subtitleLp.topMargin = dp(2);
        titles.addView(subtitle, subtitleLp);
        titleRow.addView(titles, titlesLp);

        topStatusChip = text("启动中", 12, COLOR_ON_PRIMARY_CONTAINER, Typeface.BOLD);
        topStatusChip.setGravity(Gravity.CENTER);
        topStatusChip.setPadding(dp(12), dp(7), dp(12), dp(7));
        topStatusChip.setBackground(roundedBackground(COLOR_PRIMARY_CONTAINER, 999));
        topStatusChip.setContentDescription("应用运行状态");
        topStatusChip.setOnClickListener(v -> setTab(TAB_MAINTENANCE));
        titleRow.addView(topStatusChip);
        bar.addView(titleRow, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        liveActivityCard = new LinearLayout(this);
        liveActivityCard.setOrientation(LinearLayout.HORIZONTAL);
        liveActivityCard.setGravity(Gravity.CENTER_VERTICAL);
        liveActivityCard.setPadding(dp(14), dp(10), dp(12), dp(10));
        liveActivityCard.setMinimumHeight(dp(62));
        liveActivityCard.setClickable(true);
        liveActivityCard.setFocusable(true);
        liveActivityCard.setOnClickListener(v -> setTab(TAB_OVERVIEW));
        liveActivityCard.setContentDescription("现在正在等待车辆蓝牙信号");
        liveActivityCard.setBackground(rippleBackground(COLOR_SURFACE_VARIANT, COLOR_OUTLINE, 16,
                Color.argb(28, 255, 255, 255)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        copy.addView(text("现在正在 · 实时行为", 11, COLOR_MUTED, Typeface.BOLD));
        liveActivityTitleView = text("正在连接服务", 15, COLOR_TEXT, Typeface.BOLD);
        liveActivityTitleView.setAccessibilityHeading(true);
        liveActivityTitleView.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        LinearLayout.LayoutParams liveTitleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        liveTitleLp.topMargin = dp(3);
        copy.addView(liveActivityTitleView, liveTitleLp);
        liveActivityDetailView = text("正在启动扫描与通知桥接", 12, COLOR_MUTED, Typeface.NORMAL);
        liveActivityDetailView.setMaxLines(2);
        liveActivityDetailView.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        LinearLayout.LayoutParams liveDetailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        liveDetailLp.topMargin = dp(2);
        copy.addView(liveActivityDetailView, liveDetailLp);
        liveActivityCard.addView(copy, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        liveActivityBadgeView = text("启动", 11, COLOR_ON_PRIMARY_CONTAINER, Typeface.BOLD);
        liveActivityBadgeView.setGravity(Gravity.CENTER);
        liveActivityBadgeView.setPadding(dp(10), dp(5), dp(10), dp(5));
        liveActivityBadgeView.setBackground(roundedBackground(COLOR_PRIMARY_CONTAINER, 999));
        LinearLayout.LayoutParams badgeLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        badgeLp.leftMargin = dp(10);
        liveActivityCard.addView(liveActivityBadgeView, badgeLp);

        LinearLayout.LayoutParams liveCardLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        liveCardLp.topMargin = dp(10);
        bar.addView(liveActivityCard, liveCardLp);
        return bar;
    }

    private void buildOverviewContent() {
        addSection(overviewPage, "实时状态");
        LinearLayout hero = heroCard();
        hero.addView(text("当前连接", 12, COLOR_PRIMARY, Typeface.BOLD));
        heroTitleView = text("正在连接服务", 25, COLOR_TEXT, Typeface.BOLD);
        LinearLayout.LayoutParams heroTitleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        heroTitleLp.topMargin = dp(8);
        hero.addView(heroTitleView, heroTitleLp);

        LinearLayout signalRow = new LinearLayout(this);
        signalRow.setGravity(Gravity.BOTTOM);
        heroSignalView = text("无信号", 18, COLOR_TEXT, Typeface.BOLD);
        heroSignalView.setGravity(Gravity.BOTTOM);
        signalRow.addView(heroSignalView);
        heroSignalUnitView = text("dBm", 14, COLOR_MUTED, Typeface.NORMAL);
        heroSignalUnitView.setVisibility(View.GONE);
        LinearLayout.LayoutParams unitLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        unitLp.leftMargin = dp(6);
        unitLp.bottomMargin = dp(7);
        signalRow.addView(heroSignalUnitView, unitLp);
        heroZoneView = text("等待服务绑定", 13, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams signalRowLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        signalRowLp.topMargin = dp(10);
        hero.addView(signalRow, signalRowLp);
        LinearLayout.LayoutParams zoneLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        zoneLp.topMargin = dp(4);
        hero.addView(heroZoneView, zoneLp);

        heroDetailView = text("服务启动后会自动扫描车辆", 13, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams heroDetailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        heroDetailLp.topMargin = dp(8);
        hero.addView(heroDetailView, heroDetailLp);
        addDivider(hero);
        serviceMasterSwitch = switchToggle();
        addSwitchRow(hero, "自动运行", "关闭后停止扫描与所有自动解闭锁动作",
                serviceMasterSwitch, true);
        serviceMasterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (settingSwitches) return;
            if (service != null) {
                service.setServiceEnabled(isChecked);
            } else {
                new VehicleIdentityStore(MainActivity.this).setServiceEnabled(isChecked);
                if (isChecked) startPassiveKeyService();
            }
            refreshUi();
        });
        overviewPage.addView(hero, cardLp());

        addSection(overviewPage, "快捷控制");
        LinearLayout manualCard = card();
        manualCard.addView(text("打开前请先在银河 App 连接车辆", 12, COLOR_MUTED, Typeface.NORMAL));
        LinearLayout manualButtons = new LinearLayout(this);
        manualButtons.setOrientation(LinearLayout.HORIZONTAL);
        manualUnlockButton = filledButton("解锁");
        manualLockButton = tonalButton("上锁");
        manualUnlockButton.setOnClickListener(view -> requestManualControl(PassiveKeyLogic.Action.UNLOCK));
        manualLockButton.setOnClickListener(view -> requestManualControl(PassiveKeyLogic.Action.LOCK));
        LinearLayout.LayoutParams unlockButtonLp = new LinearLayout.LayoutParams(0, dp(50), 1f);
        unlockButtonLp.rightMargin = dp(5);
        LinearLayout.LayoutParams lockButtonLp = new LinearLayout.LayoutParams(0, dp(50), 1f);
        lockButtonLp.leftMargin = dp(5);
        manualButtons.addView(manualUnlockButton, unlockButtonLp);
        manualButtons.addView(manualLockButton, lockButtonLp);
        LinearLayout.LayoutParams manualButtonsLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(50));
        manualButtonsLp.topMargin = dp(14);
        manualCard.addView(manualButtons, manualButtonsLp);
        manualControlView = text("等待服务绑定", 13, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams manualStatusLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        manualStatusLp.topMargin = dp(12);
        manualCard.addView(manualControlView, manualStatusLp);
        overviewPage.addView(manualCard, cardLp());

        addSection(overviewPage, "本车");
        LinearLayout vehicleCard = card();
        LinearLayout vehicleHeader = new LinearLayout(this);
        vehicleHeader.setGravity(Gravity.CENTER_VERTICAL);
        overviewVehicleTitleView = text("尚未识别本车", 18, COLOR_TEXT, Typeface.BOLD);
        vehicleHeader.addView(overviewVehicleTitleView, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        TextView vehicleBadge = text("本车", 11, COLOR_ON_PRIMARY_CONTAINER, Typeface.BOLD);
        vehicleBadge.setPadding(dp(9), dp(4), dp(9), dp(4));
        vehicleBadge.setBackground(roundedBackground(COLOR_PRIMARY_CONTAINER, 999));
        vehicleHeader.addView(vehicleBadge);
        vehicleCard.addView(vehicleHeader);
        overviewVehicleDetailView = text("保持应用运行，让银河连接车辆一次即可学习", 13, COLOR_MUTED,
                Typeface.NORMAL);
        LinearLayout.LayoutParams vehicleDetailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        vehicleDetailLp.topMargin = dp(8);
        vehicleCard.addView(overviewVehicleDetailView, vehicleDetailLp);
        overviewVehicleMetaView = text("", 12, COLOR_PRIMARY, Typeface.NORMAL);
        LinearLayout.LayoutParams vehicleMetaLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        vehicleMetaLp.topMargin = dp(8);
        vehicleCard.addView(overviewVehicleMetaView, vehicleMetaLp);
        overviewPage.addView(vehicleCard, cardLp());

        addSection(overviewPage, "系统状态");
        overviewHealthCard = clickableCard();
        LinearLayout healthHeader = new LinearLayout(this);
        healthHeader.setGravity(Gravity.CENTER_VERTICAL);
        overviewHealthTitleView = text("正在检查系统状态", 16, COLOR_TEXT, Typeface.BOLD);
        healthHeader.addView(overviewHealthTitleView, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        healthHeader.addView(text("›", 28, COLOR_MUTED, Typeface.NORMAL));
        overviewHealthCard.addView(healthHeader);
        overviewHealthDetailView = text("", 13, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams healthDetailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        healthDetailLp.topMargin = dp(8);
        overviewHealthCard.addView(overviewHealthDetailView, healthDetailLp);
        overviewHealthCard.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PermissionGuideActivity.class)));
        overviewPage.addView(overviewHealthCard, cardLp());
    }

    private void buildControlsContent() {
        addSection(controlsPage, "自动动作");
        LinearLayout automationCard = card();
        autoUnlockSwitch = switchToggle();
        autoLockSwitch = switchToggle();
        blockChargingSwitch = switchToggle();
        autoUnlockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (settingSwitches) return;
            if (service != null) service.setAutoUnlockEnabled(isChecked);
        });
        autoLockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (settingSwitches) return;
            if (service != null) service.setAutoLockEnabled(isChecked);
        });
        blockChargingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (settingSwitches) return;
            if (service != null) {
                service.setBlockWhenChargingEnabled(isChecked);
            } else {
                new VehicleIdentityStore(MainActivity.this).setBlockWhenChargingEnabled(isChecked);
            }
            refreshUi();
        });
        addSwitchRow(automationCard, "靠近自动解锁", "接近信号达到阈值时，只解锁一次",
                autoUnlockSwitch, false);
        addSwitchRow(automationCard, "离开自动上锁", "持续远离达到确认时间后上锁，断连不会误锁",
                autoLockSwitch, false);
        addSwitchRow(automationCard, "插线禁控", "有线充电时禁止自动与手动解闭锁",
                blockChargingSwitch, true);
        controlsPage.addView(automationCard, cardLp());

        addSection(controlsPage, "距离判定");
        LinearLayout thresholdCard = card();
        thresholdCard.addView(text("当前触发参数", 16, COLOR_TEXT, Typeface.BOLD));
        thresholdView = text("", 14, COLOR_TEXT, Typeface.NORMAL);
        LinearLayout.LayoutParams thresholdLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        thresholdLp.topMargin = dp(10);
        thresholdCard.addView(thresholdView, thresholdLp);
        thresholdSourceView = text("", 12, COLOR_PRIMARY, Typeface.BOLD);
        LinearLayout.LayoutParams sourceLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sourceLp.topMargin = dp(8);
        thresholdCard.addView(thresholdSourceView, sourceLp);
        thresholdDefaultsView = text("项目默认（仅辅助填充，不会自动应用）：\n"
                + ThresholdSettingsForm.defaultsSummary(), 12, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams defaultsLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        defaultsLp.topMargin = dp(10);
        thresholdCard.addView(thresholdDefaultsView, defaultsLp);
        Button customThresholdBtn = tonalButton("调整连接 / 解锁 / 上锁参数");
        customThresholdBtn.setOnClickListener(v -> promptCustomThresholds());
        thresholdCard.addView(customThresholdBtn, buttonLp());
        thresholdCard.addView(text("连接阈值只提前唤醒银河，不会直接解锁；RSSI 不对应固定米数，调整后立即生效。",
                12, COLOR_MUTED, Typeface.NORMAL), supportLp());
        controlsPage.addView(thresholdCard, cardLp());

        addSection(controlsPage, "安全边界");
        LinearLayout safetyCard = card();
        safetyCard.addView(text("宁可少动作，也不在车旁误锁", 15, COLOR_TEXT, Typeface.BOLD));
        safetyCard.addView(text(
                "服务总开关关闭、插线禁控、行驶保护或官方钥匙未就绪时，不会发送车锁命令。"
                        + "手动操作也受这些保护约束。",
                13, COLOR_MUTED, Typeface.NORMAL), supportLp());
        controlsPage.addView(safetyCard, cardLp());
    }

    private void buildMaintenanceContent() {
        addSection(maintenancePage, "授权与系统");
        LinearLayout permissionCard = card();
        permissionCard.addView(text("授权与运行全检", 17, COLOR_TEXT, Typeface.BOLD));
        permissionView = text("正在读取权限与运行状态...", 13, COLOR_MUTED, Typeface.NORMAL);
        permissionCard.addView(permissionView, supportLp());
        permissionGuideButton = tonalButton("打开授权与运行全检");
        permissionGuideButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PermissionGuideActivity.class)));
        permissionCard.addView(permissionGuideButton, buttonLp());
        maintenancePage.addView(permissionCard, cardLp());

        addSection(maintenancePage, "本车识别");
        LinearLayout identityCard = card();
        identityCard.addView(text("本车身份", 16, COLOR_TEXT, Typeface.BOLD));
        identityView = text("", 13, COLOR_MUTED, Typeface.NORMAL);
        identityCard.addView(identityView, supportLp());
        Button bindSuffixButton = outlinedButton("设置本车尾号匹配（可选）");
        bindSuffixButton.setOnClickListener(v -> promptBindByVinSuffix());
        identityCard.addView(bindSuffixButton, buttonLp());
        Button resetButton = dangerOutlinedButton("重新学习本车");
        resetButton.setOnClickListener(v -> confirmResetIdentity());
        identityCard.addView(resetButton, buttonLp());
        maintenancePage.addView(identityCard, cardLp());

        addSection(maintenancePage, "应用维护");
        LinearLayout updateCard = card();
        updateCard.addView(text("应用更新", 17, COLOR_TEXT, Typeface.BOLD));
        updateStatusView = text("读取更新状态...", 13, COLOR_MUTED, Typeface.NORMAL);
        updateCard.addView(updateStatusView, supportLp());
        autoUpdateSwitch = switchToggle();
        settingUpdateSwitch = true;
        autoUpdateSwitch.setChecked(UpdateManager.isAutoEnabled(this));
        settingUpdateSwitch = false;
        autoUpdateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (settingUpdateSwitch) return;
            UpdateManager.setAutoEnabled(this, isChecked);
            if (isChecked) UpdateManager.maybeAutoCheck(this);
            refreshUi();
        });
        addDivider(updateCard);
        addSwitchRow(updateCard, "自动检查更新", "默认每 1 小时检查；有新版自动下载",
                autoUpdateSwitch, false);
        updateActionButton = tonalButton("立即检查更新");
        updateActionButton.setOnClickListener(v -> handleUpdateAction());
        updateCard.addView(updateActionButton, buttonLp());
        updateCard.addView(text(
                "只从 GitHub Releases 下载。安装前校验 SHA-256、包名、构建号和当前签名。",
                12, COLOR_MUTED, Typeface.NORMAL), supportLp());
        maintenancePage.addView(updateCard, cardLp());

        addSection(maintenancePage, "诊断工具");
        LinearLayout rssiChartCard = card();
        rssiChartCard.addView(text("蓝牙信号实时曲线", 16, COLOR_TEXT, Typeface.BOLD));
        rssiChartView = new RssiChartView(this);
        LinearLayout.LayoutParams chartLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(190));
        chartLp.topMargin = dp(10);
        rssiChartCard.addView(rssiChartView, chartLp);
        rssiChartSummaryView = text("等待服务绑定", 12, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams chartSummaryLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chartSummaryLp.topMargin = dp(8);
        rssiChartCard.addView(rssiChartSummaryView, chartSummaryLp);
        rssiChartCard.addView(text("蓝线原始 RSSI · 黄线平滑 RSSI · 绿/红虚线为解锁/上锁阈值",
                11, COLOR_MUTED, Typeface.NORMAL), supportLp());
        maintenancePage.addView(rssiChartCard, cardLp());

        LinearLayout runtimeCard = card();
        runtimeCard.addView(text("运行详情", 16, COLOR_TEXT, Typeface.BOLD));
        statusView = text("", 12, COLOR_MUTED, Typeface.NORMAL);
        statusView.setLineSpacing(dp(2), 1.08f);
        runtimeCard.addView(statusView, supportLp());
        maintenancePage.addView(runtimeCard, cardLp());

        LinearLayout vehicleParamCard = card();
        vehicleParamCard.addView(text("车机参数（只读）", 16, COLOR_TEXT, Typeface.BOLD));
        vehicleParamView = text("等待读取银河官方 AIDL...", 12, COLOR_MUTED, Typeface.NORMAL);
        vehicleParamCard.addView(vehicleParamView, supportLp());
        Button refreshVehicleParamButton = outlinedButton("重新读取车机参数");
        refreshVehicleParamButton.setOnClickListener(v -> {
            parameterProbe.queryReadOnly();
            refreshUi();
        });
        vehicleParamCard.addView(refreshVehicleParamButton, buttonLp());
        vehicleParamCard.addView(text("只读查询 PE 状态、车况和标定；不修改任何车机参数。",
                11, COLOR_MUTED, Typeface.NORMAL), supportLp());
        maintenancePage.addView(vehicleParamCard, cardLp());

        LinearLayout logCard = card();
        logCard.addView(text("最近关键日志", 16, COLOR_TEXT, Typeface.BOLD));
        logView = text("", 10, COLOR_MUTED, Typeface.NORMAL);
        logView.setTypeface(Typeface.MONOSPACE);
        logView.setLineSpacing(0f, 1.18f);
        logView.setMaxLines(12);
        logCard.addView(logView, supportLp());
        exportLogButton = tonalButton("导出排查日志（ZIP）");
        exportLogButton.setContentDescription("手动导出当前完整日志；解锁和上锁还会自动保存最近三分钟快照");
        exportLogButton.setOnClickListener(view -> chooseLogExportDestination());
        logCard.addView(exportLogButton, buttonLp());
        actionSnapshotStatusView = text("自动动作快照：读取中", 12, COLOR_MUTED, Typeface.NORMAL);
        logCard.addView(actionSnapshotStatusView, supportLp());
        exportActionSnapshotsButton = outlinedButton("导出全部动作快照（ZIP）");
        exportActionSnapshotsButton.setOnClickListener(view -> chooseActionSnapshotExportDestination());
        exportActionSnapshotsButton.setContentDescription("导出最多100个自动保存的解锁或上锁前三分钟快照");
        logCard.addView(exportActionSnapshotsButton, buttonLp());
        Button clearLogBtn = outlinedButton("清空日志");
        clearLogBtn.setOnClickListener(v -> {
            if (service != null) {
                service.clearLogs();
                refreshUi();
            }
        });
        logCard.addView(clearLogBtn, buttonLp());
        maintenancePage.addView(logCard, cardLp());

        addSection(maintenancePage, "关于");
        LinearLayout aboutCard = card();
        aboutCard.addView(text("银河无感钥匙", 15, COLOR_TEXT, Typeface.BOLD));
        TextView version = text(installedVersionLabel(), 12, COLOR_MUTED, Typeface.NORMAL);
        version.setTextIsSelectable(true);
        aboutCard.addView(version, supportLp());
        maintenancePage.addView(aboutCard, cardLp());
    }

    private LinearLayout buildBottomNavigation() {
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setBackgroundColor(COLOR_BG);
        View divider = new View(this);
        divider.setBackgroundColor(COLOR_OUTLINE);
        outer.addView(divider, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER);
        bar.setPadding(dp(10), dp(6), dp(10), dp(8));

        NavItemView overview = navItem(R.drawable.ic_nav_overview, "概览");
        NavItemView controls = navItem(R.drawable.ic_nav_controls, "控制");
        NavItemView maintenance = navItem(R.drawable.ic_nav_maintenance, "维护");
        overviewTab = overview.container;
        overviewTabIconContainer = overview.iconContainer;
        overviewTabIcon = overview.icon;
        overviewTabLabel = overview.label;
        controlsTab = controls.container;
        controlsTabIconContainer = controls.iconContainer;
        controlsTabIcon = controls.icon;
        controlsTabLabel = controls.label;
        maintenanceTab = maintenance.container;
        maintenanceTabIconContainer = maintenance.iconContainer;
        maintenanceTabIcon = maintenance.icon;
        maintenanceTabLabel = maintenance.label;
        maintenanceBadge = maintenance.badge;
        overviewTab.setOnClickListener(v -> setTab(TAB_OVERVIEW));
        controlsTab.setOnClickListener(v -> setTab(TAB_CONTROLS));
        maintenanceTab.setOnClickListener(v -> setTab(TAB_MAINTENANCE));
        bar.addView(overviewTab, new LinearLayout.LayoutParams(0, dp(58), 1f));
        bar.addView(controlsTab, new LinearLayout.LayoutParams(0, dp(58), 1f));
        bar.addView(maintenanceTab, new LinearLayout.LayoutParams(0, dp(58), 1f));
        outer.addView(bar);
        return outer;
    }

    private NavItemView navItem(int iconRes, String labelText) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER);
        container.setClickable(true);
        container.setFocusable(true);
        container.setContentDescription(labelText);
        container.setPadding(dp(8), dp(5), dp(8), dp(5));

        FrameLayout iconHolder = new FrameLayout(this);
        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        FrameLayout.LayoutParams iconLp = new FrameLayout.LayoutParams(dp(24), dp(24));
        iconLp.gravity = Gravity.CENTER;
        iconHolder.addView(icon, iconLp);
        View badge = new View(this);
        badge.setBackground(roundedBackground(COLOR_RED, 999));
        FrameLayout.LayoutParams badgeLp = new FrameLayout.LayoutParams(dp(8), dp(8));
        badgeLp.gravity = Gravity.END | Gravity.TOP;
        badge.setVisibility(View.GONE);
        iconHolder.addView(badge, badgeLp);
        container.addView(iconHolder, new LinearLayout.LayoutParams(dp(58), dp(32)));

        TextView label = text(labelText, 11, COLOR_MUTED, Typeface.BOLD);
        label.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams labelLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        labelLp.topMargin = dp(3);
        container.addView(label, labelLp);
        return new NavItemView(container, iconHolder, icon, label, badge);
    }

    private void setTab(int tab) {
        activeTab = tab;
        overviewPage.setVisibility(tab == TAB_OVERVIEW ? View.VISIBLE : View.GONE);
        controlsPage.setVisibility(tab == TAB_CONTROLS ? View.VISIBLE : View.GONE);
        maintenancePage.setVisibility(tab == TAB_MAINTENANCE ? View.VISIBLE : View.GONE);
        styleNavItem(overviewTab, overviewTabIconContainer, overviewTabIcon, overviewTabLabel,
                tab == TAB_OVERVIEW);
        styleNavItem(controlsTab, controlsTabIconContainer, controlsTabIcon, controlsTabLabel,
                tab == TAB_CONTROLS);
        styleNavItem(maintenanceTab, maintenanceTabIconContainer, maintenanceTabIcon, maintenanceTabLabel,
                tab == TAB_MAINTENANCE);
        if (mainScrollView != null) mainScrollView.scrollTo(0, 0);
    }

    private void styleNavItem(LinearLayout item, FrameLayout iconContainer,
                              ImageView icon, TextView label, boolean selected) {
        item.setBackground(null);
        iconContainer.setBackground(selected
                ? roundedBackground(COLOR_PRIMARY_CONTAINER, 999) : null);
        icon.setColorFilter(selected ? COLOR_PRIMARY : COLOR_MUTED);
        label.setTextColor(selected ? COLOR_PRIMARY : COLOR_MUTED);
    }

    private static final class NavItemView {
        final LinearLayout container;
        final FrameLayout iconContainer;
        final ImageView icon;
        final TextView label;
        final View badge;

        NavItemView(LinearLayout container, FrameLayout iconContainer,
                    ImageView icon, TextView label, View badge) {
            this.container = container;
            this.iconContainer = iconContainer;
            this.icon = icon;
            this.label = label;
            this.badge = badge;
        }
    }

    private LinearLayout page() {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return page;
    }

    private void addSection(LinearLayout parent, String title) {
        TextView label = text(title, 13, COLOR_PRIMARY, Typeface.BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(18);
        lp.bottomMargin = dp(8);
        parent.addView(label, lp);
    }

    /** 刷新独立的官方连接、测距状态和已交付次数，不把尝试次数当作锁车成功。 */
    private void refreshUi() {
        if (exportLogButton != null) exportLogButton.setEnabled(!LOG_EXPORT_BUSY.get());
        PassiveKeyService.Status status = service != null ? service.getStatus() : null;
        updateRssiChart(status);
        refreshManualControls(status);
        boolean notificationAccess = KeyNotificationBridgeService.isNotificationListenerEnabled(this);
        boolean listenerConnected = KeyNotificationBridgeService.isListenerConnected();
        boolean notificationRuntimePermission = hasNotificationRuntimePermission();
        boolean batteryWhitelisted = isBatteryWhitelisted();
        boolean geelyInstalled = GeelyBridge.isGeelyInstalled(this);
        boolean btPermission = hasBluetoothPermissions();
        refreshLiveActivity(status, notificationAccess, listenerConnected);
        refreshActionSnapshotUi();
        PermissionAudit.Result audit = PermissionAudit.evaluate(PermissionStatus.read(this, status));
        UpdateManager.Snapshot update = UpdateManager.snapshot(this);
        permissionView.setText(permissionCardText(audit));
        permissionGuideButton.setText(audit.essentialReady()
                ? "查看授权与运行全检"
                : "立即处理 " + audit.pendingRequired() + " 项");
        refreshUpdateUi(update);
        refreshOverview(status, audit, update);

        if (status == null) {
            statusView.setText("服务：未绑定\n"
                    + "蓝牙权限：" + yesNo(btPermission) + "\n"
                    + "通知监听：" + notificationStatusText(notificationAccess, listenerConnected) + "\n"
                    + "系统通知权限：" + yesNo(notificationRuntimePermission) + "\n"
                    + "电池白名单：" + yesNo(batteryWhitelisted) + "\n"
                    + "银河 App：" + (geelyInstalled ? "已安装" : "未安装"));
            identityView.setText("等待服务启动");
            VehicleIdentityStore store = new VehicleIdentityStore(this);
            thresholdView.setText(ThresholdSettingsForm.configuredSummary(
                    store.getUnlockDbm(), store.getLockDbm(), store.getLockDwellMs(),
                    store.getConnectDbm()));
            thresholdSourceView.setText("当前配置：" + (store.hasCustomThresholds()
                    ? "用户自定义" : "项目默认"));
        } else {
            statusView.setText(
                    "服务总开关：" + (status.serviceEnabled ? "开启 (运行中)" : "已关闭 (完全停止操作)") + "\n"
                            + "蓝牙：" + (status.bluetoothOn ? "已开启" : "未开启")
                            + (status.scanning ? " · 扫描中" : "") + "\n"
                            + "银河钥匙：" + (status.geelyConnected ? "已连接" : "未连接") + "\n"
                            + "鉴权观察：" + status.authObserver + "\n"
                            + "连接态 RSSI：" + (status.gattConnected ? "已挂载" : "未挂载") + "\n"
                            + "有线充电：" + (status.wiredCharging
                                    ? (status.blockWhenCharging ? "已插线 (禁控保护中)" : "已插线 (未禁控)")
                                    : "未插线") + "\n"
                            + "信号：" + (status.rssi > -127 ? status.rssi + " dBm" : "--")
                            + " · " + status.zone + "\n"
                            + "通知按钮：" + status.buttonDiag + "\n"
                            + "最近动作：" + status.lastAction + "\n"
                            + "指令已交付（解锁/上锁）：" + status.unlockCount + " / " + status.lockCount
            );

            if (status.identityLearned) {
                boolean observed = !VehicleLearningState.UNKNOWN.equals(status.learningSource)
                        && !VehicleLearningState.LEGACY.equals(status.learningSource)
                        && !VehicleLearningState.MANUAL.equals(status.learningSource);
                identityView.setText(
                        (observed ? "已学习：" : "已有本车配置：") + status.vehicleName + "\n"
                                + "地址：" + (status.vehicleAddress.isEmpty() ? "--" : status.vehicleAddress)
                                + "\n来源：" + VehicleLearningState.sourceLabel(status.learningSource)
                                + "\n" + status.learningMessage
                );
            } else {
                identityView.setText("尚未获取本车蓝牙地址\n"
                        + (status.vehicleName.isEmpty() ? "" : "待匹配：" + status.vehicleName + "\n")
                        + status.learningMessage);
            }

            settingSwitches = true;
            serviceMasterSwitch.setChecked(status.serviceEnabled);
            autoUnlockSwitch.setChecked(status.autoUnlock);
            autoLockSwitch.setChecked(status.autoLock);
            blockChargingSwitch.setChecked(status.blockWhenCharging);
            autoUnlockSwitch.setEnabled(status.serviceEnabled);
            autoLockSwitch.setEnabled(status.serviceEnabled);
            blockChargingSwitch.setEnabled(status.serviceEnabled);
            settingSwitches = false;

            thresholdView.setText(ThresholdSettingsForm.configuredSummary(
                    status.unlockDbm, status.lockDbm, status.lockDwellMs, status.connectDbm));
            thresholdSourceView.setText("当前配置：" + (status.usingDefaultThresholds
                    ? "项目默认" : "用户自定义"));
            logView.setText(service.getLogText());
        }

        if (vehicleParamView != null) vehicleParamView.setText(VehicleParameterProbe.loadDisplay(this));
    }

    /** 更新卡只显示当前真实状态；自动检查默认开启，周期固定为一小时。 */
    private void refreshUpdateUi(UpdateManager.Snapshot update) {
        if (updateStatusView == null || updateActionButton == null || autoUpdateSwitch == null) return;
        settingUpdateSwitch = true;
        autoUpdateSwitch.setChecked(UpdateManager.isAutoEnabled(this));
        settingUpdateSwitch = false;
        String checked = update.lastCheckAtMs <= 0L ? "尚未检查"
                : "上次检查：" + new SimpleDateFormat("MM-dd HH:mm", Locale.ROOT)
                .format(new Date(update.lastCheckAtMs));
        updateStatusView.setText(update.status + "\n" + checked);
        if (update.checking) {
            updateActionButton.setText("正在检查更新…");
            updateActionButton.setEnabled(false);
        } else if (update.downloading) {
            updateActionButton.setText("正在下载更新…");
            updateActionButton.setEnabled(false);
        } else if (update.updateReady) {
            updateActionButton.setText("安装 v" + update.downloadedVersionName);
            updateActionButton.setEnabled(true);
        } else {
            updateActionButton.setText("立即检查更新");
            updateActionButton.setEnabled(true);
        }
    }

    /** 顶部跨页面实时行为条：由服务阶段映射，不使用最近日志文本猜测。 */
    private void refreshLiveActivity(PassiveKeyService.Status status, boolean notificationAccess,
                                     boolean listenerConnected) {
        if (liveActivityCard == null) return;
        LiveActivity.Input input = new LiveActivity.Input();
        input.running = status != null && status.running;
        input.serviceEnabled = status != null && status.serviceEnabled;
        input.bluetoothOn = status != null && status.bluetoothOn;
        input.notificationAccess = notificationAccess;
        input.listenerConnected = listenerConnected;
        if (status == null) {
            currentLiveTrace = java.util.Collections.emptyList();
            renderLiveActivity(LiveActivity.resolve(input));
            return;
        }
        input.chargingBlocked = status.wiredCharging && status.blockWhenCharging;
        input.driving = status.driving;
        input.identityComplete = status.identityLearned;
        input.scanning = status.scanning;
        input.geelyConnected = status.geelyConnected;
        input.gattConnected = status.gattConnected;
        input.wakeActive = status.wakeActive;
        input.aclRecent = status.aclRecent;
        input.signalFresh = status.rssi > -127 && status.rssi < 0 && status.lastSampleAgeMs >= 0L
                && status.lastSampleAgeMs <= 3000L;
        input.pendingUnlock = status.pendingAction == PassiveKeyLogic.Action.UNLOCK;
        input.pendingLock = status.pendingAction == PassiveKeyLogic.Action.LOCK;
        input.rssi = status.rssi;
        input.connectDbm = status.connectDbm;
        input.wakeAgeMs = status.wakeAgeMs;
        input.lastSentAgeMs = status.lastSentAgeMs;
        input.dispatchAgeMs = status.dispatchAgeMs;
        input.lastSentAction = status.lastSentAction;
        input.dispatchAction = status.dispatchAction;
        input.lastControlResult = status.lastControlResult;
        input.pendingWaitReason = status.pendingWaitReason;
        input.sampleDecision = status.sampleDecision;
        input.zone = status.zone;
        input.lockDwellElapsedMs = status.lockDwellElapsedMs;
        input.lockDwellMs = status.lockDwellMs;
        input.lockDwellSamples = status.lockDwellSamples;
        input.trace = status.liveTrace;
        currentLiveTrace = status.liveTrace;
        input.traceNowMs = status.liveTraceNowMs;
        renderLiveActivity(LiveActivity.resolve(input));
    }

    /** 用同一套颜色语义渲染进度、成功、保护和错误，保持顶部状态稳定可读。 */
    private void renderLiveActivity(LiveActivity.Result activity) {
        if (liveActivityCard == null || activity == null) return;
        int accent;
        int container;
        int badgeText;
        switch (activity.tone) {
            case SUCCESS:
                accent = COLOR_GREEN;
                container = COLOR_GREEN_CONTAINER;
                badgeText = COLOR_GREEN;
                break;
            case WARNING:
                accent = COLOR_AMBER;
                container = COLOR_AMBER_CONTAINER;
                badgeText = COLOR_AMBER;
                break;
            case ERROR:
                accent = COLOR_RED;
                container = COLOR_RED_CONTAINER;
                badgeText = COLOR_RED;
                break;
            case PROGRESS:
                accent = COLOR_PRIMARY;
                container = COLOR_PRIMARY_CONTAINER;
                badgeText = COLOR_ON_PRIMARY_CONTAINER;
                break;
            case IDLE:
            default:
                accent = COLOR_OUTLINE;
                container = COLOR_SURFACE_HIGH;
                badgeText = COLOR_MUTED;
                break;
        }
        if (!activity.title.contentEquals(liveActivityTitleView.getText())) {
            liveActivityTitleView.setText(activity.title);
        }
        String trace = LiveActivity.traceText(currentLiveTrace);
        String detail = trace.isEmpty() ? activity.detail : activity.detail + "\n最近：" + trace;
        if (!detail.contentEquals(liveActivityDetailView.getText())) {
            liveActivityDetailView.setText(detail);
        }
        if (!activity.badge.contentEquals(liveActivityBadgeView.getText())) {
            liveActivityBadgeView.setText(activity.badge);
        }
        liveActivityBadgeView.setTextColor(badgeText);
        liveActivityBadgeView.setBackground(roundedBackground(container, 999));
        liveActivityCard.setBackground(rippleBackground(COLOR_SURFACE_VARIANT, accent, 16,
                Color.argb(28, 255, 255, 255)));
        String description = "现在正在：" + activity.title + "。" + activity.detail;
        if (!trace.isEmpty()) description += "。最近：" + trace;
        if (!description.contentEquals(liveActivityCard.getContentDescription())) {
            liveActivityCard.setContentDescription(description);
        }
    }

    /** Material 3 首页只显示“现在是否正常”，诊断细节留在维护页。 */
    private void refreshOverview(PassiveKeyService.Status status, PermissionAudit.Result audit,
                                 UpdateManager.Snapshot update) {
        if (heroTitleView == null) return;
        boolean freshSignal = status != null && status.rssi > -127 && status.rssi < 0
                && status.lastSampleAgeMs >= 0L && status.lastSampleAgeMs <= 3000L;
        if (status == null) {
            heroTitleView.setText("正在连接服务");
            renderHeroSignal(false, 0);
            heroZoneView.setText("等待服务绑定");
            heroDetailView.setText("正在启动扫描与通知桥接");
        } else if (!status.serviceEnabled) {
            heroTitleView.setText("无感服务已暂停");
            renderHeroSignal(false, 0);
            heroZoneView.setText("自动解闭锁已关闭");
            heroDetailView.setText("打开下方“自动运行”即可恢复扫描");
        } else if (!status.bluetoothOn) {
            heroTitleView.setText("手机蓝牙未开启");
            renderHeroSignal(false, 0);
            heroZoneView.setText("无法发现车辆");
            heroDetailView.setText("请先打开手机蓝牙，再等待车辆广播");
        } else if (status.geelyConnected) {
            heroTitleView.setText("银河钥匙已连接");
            renderHeroSignal(freshSignal, status.rssi);
            heroZoneView.setText(status.zone == null || status.zone.isEmpty()
                    ? "连接态测距中" : displayZone(status.zone));
            heroDetailView.setText(status.gattConnected
                    ? "连接态 RSSI 已挂载 · 自动动作保持运行"
                    : "银河已连接，正在建立本应用测距通道");
        } else {
            heroTitleView.setText(status.scanning ? "正在等待车辆" : "服务正在恢复扫描");
            renderHeroSignal(freshSignal, status.rssi);
            heroZoneView.setText(status.zone == null || status.zone.isEmpty()
                    ? "附近暂无车辆广播" : displayZone(status.zone));
            heroDetailView.setText("自动解锁" + (status.autoUnlock ? "开" : "关")
                    + " · 自动上锁" + (status.autoLock ? "开" : "关")
                    + (status.wiredCharging && status.blockWhenCharging ? " · 插线禁控中" : ""));
        }

        if (status != null && status.identityLearned) {
            overviewVehicleTitleView.setText(status.vehicleName == null || status.vehicleName.isEmpty()
                    ? "本车已识别" : status.vehicleName);
            boolean observed = !VehicleLearningState.UNKNOWN.equals(status.learningSource)
                    && !VehicleLearningState.LEGACY.equals(status.learningSource)
                    && !VehicleLearningState.MANUAL.equals(status.learningSource);
            overviewVehicleDetailView.setText(observed ? "已学习本车蓝牙身份" : "已有本车配置");
            overviewVehicleMetaView.setText(String.format(Locale.ROOT,
                    "连接 ≥ %.0f · 解锁 ≥ %.0f · 上锁 ≤ %.0f dBm / %.1fs · %s",
                    status.connectDbm, status.unlockDbm, status.lockDbm, status.lockDwellMs / 1000f,
                    status.usingDefaultThresholds ? "项目默认" : "自定义"));
        } else {
            overviewVehicleTitleView.setText("尚未识别本车");
            overviewVehicleDetailView.setText("保持应用运行，让银河连接车辆一次");
            overviewVehicleMetaView.setText(status == null ? "等待服务启动" : status.learningMessage);
        }

        if (audit.essentialReady()) {
            overviewHealthTitleView.setText("系统就绪");
            overviewHealthDetailView.setText(audit.summaryLine() + " · 点击查看全检详情");
        } else {
            overviewHealthTitleView.setText(audit.headline());
            overviewHealthDetailView.setText(audit.summaryLine() + " · 点击逐项处理");
        }

        String chip;
        int chipText;
        int chipBackground;
        if (!audit.essentialReady()) {
            chip = "需要处理";
            chipText = COLOR_RED;
            chipBackground = COLOR_RED_CONTAINER;
        } else if (status == null) {
            chip = "连接中";
            chipText = COLOR_ON_PRIMARY_CONTAINER;
            chipBackground = COLOR_PRIMARY_CONTAINER;
        } else if (!status.serviceEnabled) {
            chip = "已暂停";
            chipText = COLOR_AMBER;
            chipBackground = COLOR_AMBER_CONTAINER;
        } else if (!status.bluetoothOn) {
            chip = "蓝牙关闭";
            chipText = COLOR_AMBER;
            chipBackground = COLOR_AMBER_CONTAINER;
        } else if (status.geelyConnected) {
            chip = "钥匙已连接";
            chipText = COLOR_GREEN;
            chipBackground = COLOR_GREEN_CONTAINER;
        } else {
            chip = "自动运行";
            chipText = COLOR_ON_PRIMARY_CONTAINER;
            chipBackground = COLOR_PRIMARY_CONTAINER;
        }
        topStatusChip.setText(chip);
        topStatusChip.setTextColor(chipText);
        topStatusChip.setBackground(roundedBackground(chipBackground, 999));

        if (maintenanceBadge != null) {
            if (!audit.essentialReady()) {
                maintenanceBadge.setBackground(roundedBackground(COLOR_RED, 999));
                maintenanceBadge.setVisibility(View.VISIBLE);
            } else if (update.updateReady) {
                maintenanceBadge.setBackground(roundedBackground(COLOR_PRIMARY, 999));
                maintenanceBadge.setVisibility(View.VISIBLE);
            } else {
                maintenanceBadge.setVisibility(View.GONE);
            }
        }
    }

    private void renderHeroSignal(boolean fresh, int rssi) {
        if (fresh) {
            heroSignalView.setText(String.valueOf(rssi));
            heroSignalView.setTextSize(42);
            heroSignalUnitView.setVisibility(View.VISIBLE);
        } else {
            heroSignalView.setText("无信号");
            heroSignalView.setTextSize(18);
            heroSignalUnitView.setVisibility(View.GONE);
        }
    }

    private String displayZone(String zone) {
        if (zone == null) return "";
        return zone.replace(" (无信号)", "").replace("（无信号）", "").trim();
    }

    /** 有已校验更新时优先安装，否则立即发起一次手动检查。 */
    private void handleUpdateAction() {
        UpdateManager.Snapshot update = UpdateManager.snapshot(this);
        if (update.updateReady) {
            promptInstallUpdate();
            return;
        }
        if (update.checking || update.downloading) {
            Toast.makeText(this, "更新任务正在进行，请稍候", Toast.LENGTH_SHORT).show();
            return;
        }
        if (UpdateManager.checkNow(this, true)) {
            Toast.makeText(this, "正在检查 GitHub Releases…", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, UpdateManager.snapshot(this).status, Toast.LENGTH_LONG).show();
        }
        refreshUi();
    }

    /** 通知点击和手动按钮都先确认一次，避免后台任务直接弹出安装器。 */
    private void promptInstallUpdate() {
        UpdateManager.Snapshot update = UpdateManager.snapshot(this);
        if (!update.updateReady) {
            Toast.makeText(this, "当前没有已下载的更新包", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("安装 v" + update.downloadedVersionName)
                .setMessage("新版本 APK 已通过 SHA-256、包名、构建号和签名校验。\n\n"
                        + "下一步会打开 Android 系统安装器；覆盖安装不会清除现有车辆配置和授权。")
                .setNegativeButton("稍后", null)
                .setPositiveButton("开始安装", (dialog, which) -> openUpdateInstaller())
                .show();
    }

    private void openUpdateInstaller() {
        if (!UpdateManager.canInstallPackages(this)) {
            pendingInstallAfterPermission = true;
            new AlertDialog.Builder(this)
                    .setTitle("需要安装未知应用权限")
                    .setMessage("Android 要求由你允许本应用打开系统安装器。授权后返回本页会继续安装。")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("去授权", (dialog, which) -> {
                        try {
                            startActivity(UpdateManager.installPermissionIntent(this));
                        } catch (Throwable unavailable) {
                            Toast.makeText(this, "无法打开安装权限设置，正在打开应用详情",
                                    Toast.LENGTH_LONG).show();
                            openAppDetails();
                        }
                    })
                    .show();
            return;
        }
        if (!UpdateManager.openInstaller(this)) {
            Toast.makeText(this, "无法打开系统安装器，请确认下载文件仍然存在", Toast.LENGTH_LONG).show();
        }
        refreshUi();
    }

    /** 更新通知带一次性标记；打开后立即消费，防止每次onResume重复弹窗。 */
    private void handleUpdateIntent(Intent intent) {
        if (intent == null || !intent.getBooleanExtra(UpdateManager.EXTRA_OPEN_UPDATE, false)) return;
        intent.removeExtra(UpdateManager.EXTRA_OPEN_UPDATE);
        setIntent(intent);
        setTab(TAB_MAINTENANCE);
        handler.postDelayed(() -> {
            if (UpdateManager.hasDownloadedUpdate(this)) promptInstallUpdate();
            else Toast.makeText(this, "更新包已不存在，请重新检查更新", Toast.LENGTH_LONG).show();
        }, 200L);
    }

    /** 主页只显示结论和最需要处理的几项，完整逐项状态在授权引导页。 */
    private String permissionCardText(PermissionAudit.Result audit) {
        StringBuilder text = new StringBuilder(audit.headline())
                .append("\n").append(audit.summaryLine());
        int shown = 0;
        for (PermissionAudit.Row row : audit.rows) {
            if (row.isReady() || shown >= 3) continue;
            text.append("\n• ").append(row.item.title).append("：");
            if (row.item == PermissionAudit.Item.LISTENER && row.isWaiting()) {
                text.append("授权仍在，系统正在重连");
            } else {
                text.append(row.statusLabel());
            }
            shown++;
        }
        return text.toString();
    }

    /** 每次界面刷新采样运行态原始/平滑RSSI；失效时绘制断点，阈值随用户配置实时更新。 */
    private void updateRssiChart(PassiveKeyService.Status status) {
        if (rssiChartView == null || rssiChartSummaryView == null) return;
        long now = android.os.SystemClock.elapsedRealtime();
        float unlock = status != null ? status.unlockDbm : new VehicleIdentityStore(this).getUnlockDbm();
        float lock = status != null ? status.lockDbm : new VehicleIdentityStore(this).getLockDbm();
        float connect = status != null ? status.connectDbm : new VehicleIdentityStore(this).getConnectDbm();
        rssiChartView.setThresholds(unlock, lock, connect);
        boolean fresh = status != null && status.rssi > -105 && status.rssi < 0
                && status.lastSampleAgeMs >= 0L && status.lastSampleAgeMs <= 3000L;
        if (fresh) {
            rssiChartView.addSample(now, status.rssi, status.smoothRssi);
            String smooth = Float.isFinite(status.smoothRssi)
                    ? String.format(Locale.ROOT, "%.1f", status.smoothRssi) : "--";
            rssiChartSummaryView.setText(String.format(Locale.ROOT,
                    "当前：原始%d dBm · 平滑%s dBm · %s\n%s · 来源：%s · 样本年龄%dms",
                    status.rssi, smooth, status.zone, rssiChartView.summary(now),
                    status.source == null || status.source.isEmpty() ? "未知" : status.source,
                    status.lastSampleAgeMs));
        } else {
            rssiChartView.addGap(now);
            rssiChartSummaryView.setText("当前无新鲜RSSI信号 · " + rssiChartView.summary(now)
                    + (status == null ? "\n等待服务绑定" : "\n" + status.zone));
        }
    }

    /** 根据总开关、插线、行驶及防连点状态刷新测试按钮，不主动执行车控。 */
    private void refreshManualControls(PassiveKeyService.Status status) {
        boolean enabled = status != null && status.manualControlEnabled;
        manualUnlockButton.setEnabled(enabled);
        manualLockButton.setEnabled(enabled);
        manualUnlockButton.setAlpha(enabled ? 1f : 0.4f);
        manualLockButton.setAlpha(enabled ? 1f : 0.4f);
        manualControlView.setText(status == null ? "等待服务绑定，暂不能测试" : status.manualControlMessage);
    }

    /** 仅在用户点击时向已绑定服务发起一次测试，并立即展示实际交付结果。 */
    private void requestManualControl(PassiveKeyLogic.Action action) {
        String message = service == null ? "服务尚未绑定，请稍后重试" : service.requestManualControl(action);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        refreshUi();
    }

    /** 直接打开本应用的通知使用权详情页，用户关闭再开启即可让系统重新绑定监听。 */
    private void openNotificationListenerSettings() {
        Intent detail = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS);
        detail.putExtra(Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                new ComponentName(this, KeyNotificationBridgeService.class).flattenToString());
        try {
            startActivity(detail);
            Toast.makeText(this, "请关闭再开启本应用的通知使用权，然后返回", Toast.LENGTH_LONG).show();
        } catch (Throwable unavailable) {
            try {
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                Toast.makeText(this, "请在通知使用权列表中关闭再开启本应用", Toast.LENGTH_LONG).show();
            } catch (Throwable ignored) {
                Toast.makeText(this, "无法打开系统通知使用权设置", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** 区分系统是否授权与监听实例是否真正在线，避免把“已授权”误显示为可用。 */
    private String notificationStatusText(boolean accessGranted, boolean connected) {
        if (!accessGranted) return "未授权";
        if (connected) return "已授权 · 监听在线";
        long downMs = KeyNotificationBridgeService.getListenerDownDurationMs();
        return downMs < PermissionAudit.LISTENER_REBIND_GRACE_MS
                ? "已授权 · 正在重连" : "已授权 · 监听未绑定";
    }

    /** 展示最多100个自动动作快照的当前数量与占用，不读取ZIP正文。 */
    private void refreshActionSnapshotUi() {
        if (actionSnapshotStatusView == null) return;
        if (service == null) {
            actionSnapshotStatusView.setText("自动动作快照：等待服务绑定");
            if (exportActionSnapshotsButton != null) exportActionSnapshotsButton.setEnabled(false);
            return;
        }
        String health = service.getActionSnapshotHealth();
        actionSnapshotStatusView.setText("自动动作快照：" + formatActionSnapshotHealth(health));
        if (exportActionSnapshotsButton != null) {
            exportActionSnapshotsButton.setEnabled(!LOG_EXPORT_BUSY.get());
        }
    }

    /** 将内部健康字段转成用户可读的数量和约占用大小。 */
    private static String formatActionSnapshotHealth(String health) {
        String count = "0/100";
        long bytes = 0L;
        for (String field : health == null ? new String[0] : health.split("\\s+")) {
            if (field.startsWith("action_snapshots=")) {
                count = field.substring("action_snapshots=".length());
            } else if (field.startsWith("action_snapshot_bytes=")) {
                try {
                    bytes = Long.parseLong(field.substring("action_snapshot_bytes=".length()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String size = bytes >= 1024L * 1024L
                ? String.format(Locale.ROOT, "%.1f MiB", bytes / (1024.0 * 1024.0))
                : String.format(Locale.ROOT, "%.0f KiB", bytes / 1024.0);
        return count.replace("/", " / 最多 ") + " 个 · " + size;
    }

    /** 选择动作快照合集导出目标；每个内层ZIP对应一次解锁或上锁。 */
    private void chooseActionSnapshotExportDestination() {
        if (LOG_EXPORT_BUSY.get()) return;
        actionSnapshotExportSnapshot = "snapshot_captured_mono_ms="
                + android.os.SystemClock.elapsedRealtime() + "\n"
                + (service == null ? "service_snapshot=unavailable" : service.getDiagnosticSnapshot());
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/zip");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_TITLE, "GeelyPassiveKey-action-snapshots-"
                + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(new Date()) + ".zip");
        try {
            startActivityForResult(intent, REQUEST_ACTION_SNAPSHOT_EXPORT);
        } catch (RuntimeException unavailable) {
            DiagnosticLog.bridgeError("Export", "ACTION_SNAPSHOT_FILE_PICKER_ERROR", unavailable);
            Toast.makeText(this, "无法打开系统文件保存器", Toast.LENGTH_LONG).show();
        }
    }

    /** 把动作快照合集写入用户选择位置，不删除或修改原有100个快照。 */
    private void exportActionSnapshots(Uri destination, String snapshot) {
        if (!LOG_EXPORT_BUSY.compareAndSet(false, true)) return;
        Context appContext = getApplicationContext();
        LOG_EXPORT_EXECUTOR.execute(() -> {
            File temporary = null;
            String message;
            try {
                temporary = File.createTempFile("passive-key-actions-", ".zip", appContext.getCacheDir());
                File ready = DiagnosticLog.exportActionSnapshots(temporary, snapshot).get();
                try (FileInputStream input = new FileInputStream(ready);
                     OutputStream output = appContext.getContentResolver().openOutputStream(destination, "wt")) {
                    if (output == null) throw new java.io.IOException("输出文件不可用");
                    byte[] buffer = new byte[16384];
                    int count;
                    while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
                }
                DiagnosticLog.event("Export", "ACTION_SNAPSHOTS_SAVED", "archive_bytes=" + ready.length());
                message = "动作快照合集已保存";
            } catch (Exception error) {
                DiagnosticLog.bridgeError("Export", "ACTION_SNAPSHOTS_EXPORT_FAILED", error);
                message = "动作快照导出失败，请重试";
            } finally {
                if (temporary != null && temporary.exists() && !temporary.delete()) {
                    DiagnosticLog.detail("Export", "CACHE_CLEANUP_FAILED", "temporary_archive=true");
                }
                LOG_EXPORT_BUSY.set(false);
            }
            String finalMessage = message;
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(appContext, finalMessage, Toast.LENGTH_LONG).show());
        });
        refreshUi();
    }

    /** 用户点击后通过系统文件选择器选择本地保存位置，无需存储权限，也不自动发送给他人。 */
    private void chooseLogExportDestination() {
        if (LOG_EXPORT_BUSY.get()) return;
        exportSnapshot = "snapshot_captured_mono_ms=" + android.os.SystemClock.elapsedRealtime() + "\n"
                + (service == null ? "service_snapshot=unavailable" : service.getDiagnosticSnapshot());
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/zip");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_TITLE, "GeelyPassiveKey-logs-"
                + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(new Date()) + ".zip");
        try {
            startActivityForResult(intent, REQUEST_LOG_EXPORT);
        } catch (RuntimeException unavailable) {
            DiagnosticLog.bridgeError("Export", "FILE_PICKER_ERROR", unavailable);
            Toast.makeText(this, "无法打开系统文件保存器", Toast.LENGTH_LONG).show();
        }
    }

    /** 保留导出快照的采集时刻，切换文件选择器时不丢失状态上下文。 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("diagnostic_snapshot", exportSnapshot);
        outState.putString("action_snapshot_export", actionSnapshotExportSnapshot);
        outState.putInt("active_tab", activeTab);
        super.onSaveInstanceState(outState);
    }

    /** 在后台生成白名单ZIP并写入用户指定文件，日志写入与车控线程保持分离。 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_LOG_EXPORT && requestCode != REQUEST_ACTION_SNAPSHOT_EXPORT) return;
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            DiagnosticLog.detail("Export", requestCode == REQUEST_ACTION_SNAPSHOT_EXPORT
                    ? "ACTION_SNAPSHOT_EXPORT_CANCELLED" : "EXPORT_CANCELLED", "");
            return;
        }
        if (requestCode == REQUEST_ACTION_SNAPSHOT_EXPORT) {
            String snapshot = service == null ? actionSnapshotExportSnapshot
                    : "snapshot_captured_mono_ms=" + android.os.SystemClock.elapsedRealtime()
                    + "\n" + service.getDiagnosticSnapshot();
            exportActionSnapshots(data.getData(), snapshot);
            return;
        }
        if (!LOG_EXPORT_BUSY.compareAndSet(false, true)) return;
        Context appContext = getApplicationContext();
        Uri destination = data.getData();
        String snapshot = service == null ? exportSnapshot
                : "snapshot_captured_mono_ms=" + android.os.SystemClock.elapsedRealtime() + "\n" + service.getDiagnosticSnapshot();
        LOG_EXPORT_EXECUTOR.execute(() -> {
            File temporary = null;
            String message;
            try {
                temporary = File.createTempFile("passive-key-diagnostics-", ".zip", appContext.getCacheDir());
                Future<File> archive = DiagnosticLog.export(temporary, snapshot);
                File ready = archive.get();
                try (FileInputStream input = new FileInputStream(ready);
                     OutputStream output = appContext.getContentResolver().openOutputStream(destination, "wt")) {
                    if (output == null) throw new java.io.IOException("输出文件不可用");
                    byte[] buffer = new byte[16384];
                    int count;
                    while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
                }
                DiagnosticLog.event("Export", "EXPORT_SAVED", "archive_bytes=" + ready.length());
                message = "排查日志已保存，可将 ZIP 提供给排查人员";
            } catch (Exception error) {
                DiagnosticLog.bridgeError("Export", "EXPORT_FAILED", error);
                message = "日志导出失败，请重试（已有日志仍保留）";
            } finally {
                if (temporary != null && temporary.exists() && !temporary.delete()) {
                    DiagnosticLog.detail("Export", "CACHE_CLEANUP_FAILED", "temporary_archive=true");
                }
                LOG_EXPORT_BUSY.set(false);
            }
            String finalMessage = message;
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(appContext, finalMessage, Toast.LENGTH_LONG).show());
        });
        refreshUi();
    }

    private void startPassiveKeyService() {
        try {
            Intent intent = new Intent(this, PassiveKeyService.class);
            intent.setAction(PassiveKeyService.ACTION_START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } catch (Throwable ignored) {
        }
    }

    private void bindPassiveKeyService() {
        if (bound || binding) return;
        binding = true;
        try {
            bindService(
                    new Intent(this, PassiveKeyService.class),
                    connection,
                    Context.BIND_AUTO_CREATE
            );
        } catch (Throwable ignored) {
            binding = false;
        }
    }

    private boolean hasBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /** 清空数据后通过系统对话框重新申请蓝牙权限，不替用户静默授予。 */
    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (Build.VERSION.SDK_INT >= 33) {
                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                }, REQUEST_BT_PERMISSIONS);
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                }, REQUEST_BT_PERMISSIONS);
            }
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_BT_PERMISSIONS);
        }
    }

    /** 系统通知权限单独申请，不和蓝牙权限混在同一个弹窗里。 */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) return;
        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                REQUEST_NOTIFICATION_PERMISSION);
    }

    /** 判断 Android 13+ 的运行时通知权限是否已授予；旧系统视为已具备。 */
    private boolean hasNotificationRuntimePermission() {
        return Build.VERSION.SDK_INT < 33
                || checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** 权限结果明确反馈；扫描权限恢复后启动扫描，通知使用权仍须单独开启。 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BT_PERMISSIONS) {
            startPassiveKeyService();
            Toast.makeText(this, hasBluetoothPermissions()
                    ? (KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                    ? "蓝牙权限已恢复，可以重新学习本车" : "蓝牙权限已恢复，请再开启通知使用权")
                    : "蓝牙权限尚未授予，无法扫描和学习本车地址", Toast.LENGTH_LONG).show();
            refreshUi();
        } else if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            Toast.makeText(this, hasNotificationRuntimePermission()
                    ? "系统通知权限已授予" : "系统通知权限未授予，可稍后在这里补授权",
                    Toast.LENGTH_LONG).show();
            refreshUi();
        }
    }

    private boolean isBatteryWhitelisted() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            return powerManager != null && powerManager.isIgnoringBatteryOptimizations(getPackageName());
        } catch (Throwable ignored) {
            return false;
        }
    }

    private void requestBatteryWhitelist() {
        if (isBatteryWhitelisted()) {
            Toast.makeText(this, "本应用已在电池白名单中，打开系统电池优化设置供你查看",
                    Toast.LENGTH_LONG).show();
            try {
                startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            } catch (Throwable ignored) {
                Toast.makeText(this, "当前系统没有可打开的电池优化设置页", Toast.LENGTH_LONG).show();
            }
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable t) {
            try {
                startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            } catch (Throwable ignored) {
                Toast.makeText(this, "当前系统无法打开电池白名单页面，请在应用详情中手动设置",
                        Toast.LENGTH_LONG).show();
                openAppDetails();
            }
        }
    }

    /** OriginOS 没有普通应用可调用的最近任务锁定API，这里给出可直接照做的系统操作说明。 */
    private void showBackgroundLockGuide() {
        new AlertDialog.Builder(this)
                .setTitle("锁定应用后台")
                .setMessage("Android/OriginOS 没有允许普通 App 自动锁定最近任务卡片的公开接口。\n\n"
                        + "请按下面操作：\n"
                        + "1. 从屏幕底部上滑并停留，打开最近任务；\n"
                        + "2. 找到“银河无感钥匙”卡片；\n"
                        + "3. 长按卡片，或点卡片右上角菜单，选择“锁定”；\n"
                        + "4. 再确认自启动和后台高耗电已允许。\n\n"
                        + "锁定后系统清理后台时更不容易杀掉本应用。")
                .setNegativeButton("关闭", null)
                .setPositiveButton("打开后台设置", (dialog, which) -> openBackgroundHighUsageSettings())
                .show();
    }

    /** 优先打开系统电池优化页，失败后回退到应用详情，避免按钮静默无响应。 */
    private void openBackgroundHighUsageSettings() {
        try {
            startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            Toast.makeText(this, "请在系统页允许本应用后台高耗电", Toast.LENGTH_LONG).show();
        } catch (Throwable unavailable) {
            Toast.makeText(this, "系统未提供后台高耗电页，打开应用详情", Toast.LENGTH_LONG).show();
            openAppDetails();
        }
    }

    private void openAppDetails() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable ignored) {
        }
    }

    /** 尾号只设置扫描匹配提示，不宣称已识别车辆或获得MAC。 */
    private void promptBindByVinSuffix() {
        final EditText input = new EditText(this);
        input.setHint("例如 123（留空则走自动识别）");
        input.setSingleLine(true);
        new AlertDialog.Builder(this)
                .setTitle("设置本车尾号匹配")
                .setMessage("尾号仅用来匹配本车蓝牙名称，不能推算蓝牙地址。地址仍须实际扫描或连接读取；银河已连且停止广播时，填尾号也不会直接得到地址。")
                .setView(input)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    String text = input.getText() != null ? input.getText().toString().trim() : "";
                    if (!text.isEmpty() && service != null) {
                        service.bindByVinSuffix(text);
                        refreshUi();
                    }
                })
                .show();
    }

    /**
     * 弹出自定义触发设置框：连接预热、靠近解锁、离开上锁阈值与离开确认秒数。
     * 独立弹窗固定展示字段含义、范围与推荐值；只在用户保存且四项校验通过后写配置。
     */
    private void promptCustomThresholds() {
        if (thresholdSettingsDialog != null && thresholdSettingsDialog.isShowing()) return;
        VehicleIdentityStore store = new VehicleIdentityStore(this);
        PassiveKeyService.Status status = service != null ? service.getStatus() : null;
        float unlockDbm = status != null ? status.unlockDbm : store.getUnlockDbm();
        float lockDbm = status != null ? status.lockDbm : store.getLockDbm();
        long dwellMs = status != null ? status.lockDwellMs : store.getLockDwellMs();
        float connectDbm = status != null ? status.connectDbm : store.getConnectDbm();
        thresholdSettingsDialog = ThresholdSettingsDialog.show(this, unlockDbm, lockDbm, dwellMs, connectDbm,
                (unlock, lock, dwell, connect) -> {
            if (service != null) {
                service.updateThresholds(unlock, lock, dwell, connect);
            } else {
                store.saveThresholds(unlock, lock, dwell, connect);
            }
            Toast.makeText(this, "自定义阈值已保存并立即生效", Toast.LENGTH_SHORT).show();
            refreshUi();
        });
    }

    /** 重学先检查两类权限，缺失时给出实际入口，避免清空后看起来毫无反应。 */
    private void confirmResetIdentity() {
        if (!hasBluetoothPermissions()) {
            Toast.makeText(this, "先授予蓝牙扫描/连接权限，再重新学习", Toast.LENGTH_LONG).show();
            requestBluetoothPermissions();
            return;
        }
        if (!KeyNotificationBridgeService.isNotificationListenerEnabled(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("需要通知使用权")
                    .setMessage("开启本应用的通知使用权后，才能确认银河蓝牙钥匙的连接状态并学习本车。")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("去开启", (dialog, which) ->
                            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)))
                    .show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("重新学习本车")
                .setMessage("清除现有车辆身份并重新扫描。若银河已连接，会先尝试读取当前真实连接；系统未提供地址时，请保持本应用运行，在银河先断开蓝牙钥匙再连接。")
                .setNegativeButton("取消", null)
                .setPositiveButton("开始学习", (dialog, which) -> {
                    if (service != null) {
                        service.resetLearnedVehicle();
                    } else {
                        new VehicleIdentityStore(this).clear();
                        startPassiveKeyService();
                    }
                    Toast.makeText(this, service == null ? "已清除旧身份，正在等待服务扫描"
                            : service.getStatus().learningMessage, Toast.LENGTH_LONG).show();
                    refreshUi();
                })
                .show();
    }

    private TextView text(String value, int sp, int color, int style) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setTypeface(Typeface.DEFAULT, style);
        return view;
    }

    private LinearLayout.LayoutParams supportLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.topMargin = dp(9);
        return lp;
    }

    private LinearLayout card() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(18), dp(18), dp(18), dp(18));
        layout.setBackground(roundedBackground(COLOR_CARD, 20));
        return layout;
    }

    private LinearLayout heroCard() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(20), dp(20), dp(14));
        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(15, 39, 50), COLOR_CARD}
        );
        bg.setCornerRadius(dp(24));
        bg.setStroke(dp(1), Color.rgb(35, 67, 82));
        layout.setBackground(bg);
        return layout;
    }

    private LinearLayout clickableCard() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(18), dp(18), dp(18), dp(18));
        layout.setClickable(true);
        layout.setFocusable(true);
        layout.setBackground(rippleBackground(COLOR_CARD, COLOR_OUTLINE, 20,
                Color.argb(34, 255, 255, 255)));
        return layout;
    }

    private GradientDrawable roundedBackground(int color, int radiusDp) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(dp(radiusDp));
        return background;
    }

    private RippleDrawable rippleBackground(int fillColor, int strokeColor, int radiusDp,
                                            int rippleColor) {
        GradientDrawable content = roundedBackground(fillColor, radiusDp);
        if (strokeColor != Color.TRANSPARENT) content.setStroke(dp(1), strokeColor);
        GradientDrawable mask = roundedBackground(Color.WHITE, radiusDp);
        return new RippleDrawable(ColorStateList.valueOf(rippleColor), content, mask);
    }

    private void addDivider(LinearLayout parent) {
        View divider = new View(this);
        divider.setBackgroundColor(COLOR_OUTLINE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
        lp.topMargin = dp(2);
        lp.bottomMargin = dp(2);
        parent.addView(divider, lp);
    }

    private Switch switchToggle() {
        Switch toggle = new Switch(this);
        toggle.setText("");
        toggle.setShowText(false);
        toggle.setMinWidth(dp(48));
        toggle.setMinimumWidth(dp(48));
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
        };
        toggle.setTrackTintList(new ColorStateList(states,
                new int[]{COLOR_PRIMARY_CONTAINER, COLOR_OUTLINE}));
        toggle.setThumbTintList(new ColorStateList(states,
                new int[]{COLOR_PRIMARY, COLOR_MUTED}));
        return toggle;
    }

    private void addSwitchRow(LinearLayout parent, String title, String detail,
                              Switch toggle, boolean last) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setMinimumHeight(dp(66));
        row.setPadding(dp(4), dp(10), dp(4), dp(10));
        row.setClickable(true);
        row.setFocusable(true);
        row.setBackground(rippleBackground(Color.TRANSPARENT, Color.TRANSPARENT,
                12, Color.argb(28, 255, 255, 255)));

        LinearLayout copy = new LinearLayout(this);
        copy.setOrientation(LinearLayout.VERTICAL);
        TextView titleView = text(title, 15, COLOR_TEXT, Typeface.BOLD);
        TextView detailView = text(detail, 12, COLOR_MUTED, Typeface.NORMAL);
        detailView.setLineSpacing(dp(1), 1.05f);
        copy.addView(titleView);
        LinearLayout.LayoutParams detailLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        detailLp.topMargin = dp(3);
        copy.addView(detailView, detailLp);
        row.addView(copy, new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout.LayoutParams toggleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        toggleLp.leftMargin = dp(12);
        row.addView(toggle, toggleLp);
        toggle.setContentDescription(title + "，" + detail);
        row.setOnClickListener(v -> {
            if (toggle.isEnabled()) toggle.setChecked(!toggle.isChecked());
        });
        parent.addView(row);
        if (!last) addDivider(parent);
    }

    private Button filledButton(String label) {
        return styledButton(label, COLOR_PRIMARY, COLOR_ON_PRIMARY,
                Color.TRANSPARENT, Color.argb(50, 0, 0, 0));
    }

    private Button tonalButton(String label) {
        return styledButton(label, COLOR_PRIMARY_CONTAINER, COLOR_ON_PRIMARY_CONTAINER,
                Color.TRANSPARENT, Color.argb(38, 255, 255, 255));
    }

    private Button outlinedButton(String label) {
        return styledButton(label, Color.TRANSPARENT, COLOR_PRIMARY,
                COLOR_OUTLINE, Color.argb(32, 255, 255, 255));
    }

    private Button dangerOutlinedButton(String label) {
        return styledButton(label, Color.TRANSPARENT, COLOR_RED,
                COLOR_RED, Color.argb(32, 255, 154, 168));
    }

    private Button styledButton(String label, int fillColor, int textColor,
                                int strokeColor, int rippleColor) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(textColor);
        button.setGravity(Gravity.CENTER);
        button.setMinHeight(dp(50));
        button.setMinimumHeight(dp(50));
        button.setPadding(dp(16), 0, dp(16), 0);
        button.setStateListAnimator(null);
        button.setBackground(rippleBackground(fillColor, strokeColor, 16, rippleColor));
        return button;
    }

    private LinearLayout.LayoutParams cardLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.bottomMargin = dp(4);
        return lp;
    }

    private LinearLayout.LayoutParams buttonLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50)
        );
        lp.topMargin = dp(12);
        return lp;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private String yesNo(boolean value) {
        return value ? "已开启" : "未开启";
    }

}
