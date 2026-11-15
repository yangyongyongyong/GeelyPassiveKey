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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1003;
    private static final ExecutorService LOG_EXPORT_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final AtomicBoolean LOG_EXPORT_BUSY = new AtomicBoolean();
    private static final int COLOR_BG = Color.rgb(8, 17, 31);
    private static final int COLOR_CARD = Color.rgb(17, 28, 46);
    private static final int COLOR_TEXT = Color.rgb(229, 231, 235);
    private static final int COLOR_MUTED = Color.rgb(148, 163, 184);
    private static final int COLOR_ACCENT = Color.rgb(56, 189, 248);
    private static final int COLOR_GREEN = Color.rgb(34, 197, 94);
    private static final int COLOR_RED = Color.rgb(248, 113, 113);

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
    private RssiChartView rssiChartView;
    private TextView rssiChartSummaryView;
    private TextView vehicleParamView;
    private TextView identityView;
    private TextView thresholdView;
    private TextView permissionView;
    private TextView logView;
    private Switch serviceMasterSwitch;
    private Switch autoUnlockSwitch;
    private Switch autoLockSwitch;
    private Switch blockChargingSwitch;
    private Button manualUnlockButton;
    private Button manualLockButton;
    private TextView manualControlView;
    private Button exportLogButton;
    private Button notificationPermissionButton;
    private Button batteryWhitelistButton;
    private String exportSnapshot = "service_snapshot=unavailable";
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
        }
        buildUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPassiveKeyService();
        bindPassiveKeyService();
        parameterProbe.start();
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

    /** 保持极简配置页，增加两个车控测试按钮和手动日志导出入口；标题区域显示实际版本。 */
    private void buildUi() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(COLOR_BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(48), dp(20), dp(32));
        scrollView.addView(root, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        ));

        TextView title = text("银河无感钥匙", 26, COLOR_TEXT, Typeface.BOLD);
        root.addView(title);
        TextView subtitle = text("只保留：息屏靠近解锁 / 离开上锁", 14, COLOR_MUTED, Typeface.NORMAL);
        LinearLayout.LayoutParams subtitleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subtitleLp.topMargin = dp(4);
        subtitleLp.bottomMargin = dp(6);
        root.addView(subtitle, subtitleLp);

        TextView versionView = text(installedVersionLabel(), 14, COLOR_ACCENT, Typeface.NORMAL);
        versionView.setTextIsSelectable(true);
        LinearLayout.LayoutParams versionLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        versionLp.bottomMargin = dp(18);
        root.addView(versionView, versionLp);

        LinearLayout statusCard = card();
        statusCard.addView(text("运行状态", 16, COLOR_ACCENT, Typeface.BOLD));
        statusView = text("", 14, COLOR_TEXT, Typeface.NORMAL);
        LinearLayout.LayoutParams bodyLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bodyLp.topMargin = dp(10);
        statusCard.addView(statusView, bodyLp);
        root.addView(statusCard, cardLp());

        LinearLayout rssiChartCard = card();
        rssiChartCard.addView(text("蓝牙信号实时曲线", 16, COLOR_ACCENT, Typeface.BOLD));
        rssiChartView = new RssiChartView(this);
        LinearLayout.LayoutParams chartLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(220));
        chartLp.topMargin = dp(8);
        rssiChartCard.addView(rssiChartView, chartLp);
        rssiChartSummaryView = text("等待服务绑定", 13, COLOR_TEXT, Typeface.NORMAL);
        LinearLayout.LayoutParams chartSummaryLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chartSummaryLp.topMargin = dp(8);
        rssiChartCard.addView(rssiChartSummaryView, chartSummaryLp);
        rssiChartCard.addView(text("蓝线：原始RSSI · 黄线：平滑RSSI · 绿/红虚线：当前解锁/上锁阈值；无信号处断开。",
                12, COLOR_MUTED, Typeface.NORMAL), chartSummaryLp);
        root.addView(rssiChartCard, cardLp());

        LinearLayout vehicleParamCard = card();
        vehicleParamCard.addView(text("车机参数（只读）", 16, COLOR_ACCENT, Typeface.BOLD));
        vehicleParamView = text("等待读取银河官方AIDL...", 13, COLOR_TEXT, Typeface.NORMAL);
        vehicleParamCard.addView(vehicleParamView, bodyLp);
        Button refreshVehicleParamButton = button("重新读取车机参数（只读）");
        refreshVehicleParamButton.setOnClickListener(v -> {
            parameterProbe.queryReadOnly();
            refreshUi();
        });
        vehicleParamCard.addView(refreshVehicleParamButton, buttonLp());
        LinearLayout.LayoutParams vehicleParamHintLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        vehicleParamHintLp.topMargin = dp(8);
        vehicleParamCard.addView(text(
                "只发送0x0501原生PE状态查询、0x0121车况查询和0x0131标定读取请求；记录0x0132原始回执。"
                        + "不修改PE开关、距离档位或任何车机参数。银河L6/DK1.5若不支持0x0131，会记录为无回执而不是猜值。",
                12, COLOR_MUTED, Typeface.NORMAL), vehicleParamHintLp);
        root.addView(vehicleParamCard, cardLp());

        LinearLayout manualCard = card();
        manualCard.addView(text("手动测试", 16, COLOR_ACCENT, Typeface.BOLD));
        LinearLayout manualButtons = new LinearLayout(this);
        manualButtons.setOrientation(LinearLayout.HORIZONTAL);
        manualUnlockButton = button("解锁");
        manualLockButton = button("上锁");
        manualUnlockButton.setOnClickListener(view -> requestManualControl(PassiveKeyLogic.Action.UNLOCK));
        manualLockButton.setOnClickListener(view -> requestManualControl(PassiveKeyLogic.Action.LOCK));
        LinearLayout.LayoutParams unlockButtonLp = new LinearLayout.LayoutParams(0, dp(48), 1f);
        unlockButtonLp.rightMargin = dp(6);
        LinearLayout.LayoutParams lockButtonLp = new LinearLayout.LayoutParams(0, dp(48), 1f);
        lockButtonLp.leftMargin = dp(6);
        manualButtons.addView(manualUnlockButton, unlockButtonLp);
        manualButtons.addView(manualLockButton, lockButtonLp);
        LinearLayout.LayoutParams manualButtonsLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(48));
        manualButtonsLp.topMargin = dp(10);
        manualCard.addView(manualButtons, manualButtonsLp);
        manualControlView = text("等待服务绑定", 13, COLOR_TEXT, Typeface.NORMAL);
        manualCard.addView(manualControlView, bodyLp);
        LinearLayout.LayoutParams manualHintLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        manualHintLp.topMargin = dp(8);
        manualCard.addView(text("测试银河当前连接的车辆；不受距离阈值或自动动作分开关限制，不排队重发。总开关、插线禁控与行驶保护仍生效。",
                12, COLOR_MUTED, Typeface.NORMAL), manualHintLp);
        root.addView(manualCard, cardLp());

        LinearLayout identityCard = card();
        identityCard.addView(text("本车识别", 16, COLOR_ACCENT, Typeface.BOLD));
        identityView = text("", 14, COLOR_TEXT, Typeface.NORMAL);
        identityCard.addView(identityView, bodyLp);

        TextView identityHint = text(
                "首次使用：先授权并保持本应用运行，再用银河连接车辆。银河已连接时车机可能停止广播；若系统未提供地址，先在银河断开蓝牙钥匙再连接一次。",
                12, COLOR_MUTED, Typeface.NORMAL
        );
        LinearLayout.LayoutParams hintLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        hintLp.topMargin = dp(10);
        identityCard.addView(identityHint, hintLp);

        Button bindSuffixButton = button("设置本车尾号匹配（可选）");
        bindSuffixButton.setOnClickListener(v -> promptBindByVinSuffix());
        identityCard.addView(bindSuffixButton, buttonLp());

        Button resetButton = button("重新学习本车");
        resetButton.setOnClickListener(v -> confirmResetIdentity());
        identityCard.addView(resetButton, buttonLp());
        root.addView(identityCard, cardLp());

        LinearLayout switchCard = card();
        switchCard.addView(text("服务与自动动作", 16, COLOR_ACCENT, Typeface.BOLD));
        serviceMasterSwitch = switchRow("无感进出服务总开关", "关闭后立即停止扫描与一切解闭锁操作");
        autoUnlockSwitch = switchRow("靠近自动解锁", "接近信号达到阈值时，只解锁一次");
        autoLockSwitch = switchRow("离开自动上锁", "持续远离达到确认秒数后上锁，断连不会误锁");
        blockChargingSwitch = switchRow("插数据线/充电线后禁止解闭锁", "USB/AC 有线充电时视为在车内或调试中，禁止自动与手动解闭锁");
        switchCard.addView(serviceMasterSwitch);
        switchCard.addView(autoUnlockSwitch);
        switchCard.addView(autoLockSwitch);
        switchCard.addView(blockChargingSwitch);
        serviceMasterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingSwitches) return;
                if (service != null) {
                    service.setServiceEnabled(isChecked);
                } else {
                    new VehicleIdentityStore(MainActivity.this).setServiceEnabled(isChecked);
                    if (isChecked) startPassiveKeyService();
                }
                refreshUi();
            }
        });
        autoUnlockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingSwitches) return;
                if (service != null) service.setAutoUnlockEnabled(isChecked);
            }
        });
        autoLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingSwitches) return;
                if (service != null) service.setAutoLockEnabled(isChecked);
            }
        });
        blockChargingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingSwitches) return;
                if (service != null) {
                    service.setBlockWhenChargingEnabled(isChecked);
                } else {
                    new VehicleIdentityStore(MainActivity.this).setBlockWhenChargingEnabled(isChecked);
                }
                refreshUi();
            }
        });

        thresholdView = text("", 13, COLOR_TEXT, Typeface.NORMAL);
        LinearLayout.LayoutParams threshLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        threshLp.topMargin = dp(8);
        switchCard.addView(thresholdView, threshLp);

        Button customThresholdBtn = button("自定义靠近/离开阈值");
        customThresholdBtn.setOnClickListener(v -> promptCustomThresholds());
        switchCard.addView(customThresholdBtn, buttonLp());
        root.addView(switchCard, cardLp());

        LinearLayout permissionCard = card();
        permissionCard.addView(text("权限与保活", 16, COLOR_ACCENT, Typeface.BOLD));
        permissionView = text("", 14, COLOR_TEXT, Typeface.NORMAL);
        permissionCard.addView(permissionView, bodyLp);

        Button btButton = button("授予蓝牙权限");
        btButton.setOnClickListener(v -> requestBluetoothPermissions());
        permissionCard.addView(btButton, buttonLp());

        notificationPermissionButton = button("授予系统通知权限");
        notificationPermissionButton.setOnClickListener(v -> requestNotificationPermission());
        permissionCard.addView(notificationPermissionButton, buttonLp());

        Button notificationButton = button("开启通知读取权限");
        notificationButton.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));
        permissionCard.addView(notificationButton, buttonLp());

        Button repairNotificationButton = button("修复通知监听");
        repairNotificationButton.setOnClickListener(v -> openNotificationListenerSettings());
        permissionCard.addView(repairNotificationButton, buttonLp());

        batteryWhitelistButton = button("加入电池白名单");
        batteryWhitelistButton.setOnClickListener(v -> requestBatteryWhitelist());
        permissionCard.addView(batteryWhitelistButton, buttonLp());

        Button lockBackgroundButton = button("锁定后台（最近任务说明）");
        lockBackgroundButton.setOnClickListener(v -> showBackgroundLockGuide());
        permissionCard.addView(lockBackgroundButton, buttonLp());

        Button backgroundButton = button("打开后台高耗电设置");
        backgroundButton.setOnClickListener(v -> openBackgroundHighUsageSettings());
        permissionCard.addView(backgroundButton, buttonLp());
        root.addView(permissionCard, cardLp());

        LinearLayout helpCard = card();
        helpCard.addView(text("使用说明", 16, COLOR_ACCENT, Typeface.BOLD));
        TextView help = text(
                "1. 安装并登录银河 App，完成蓝牙钥匙绑定。\n"
                        + "2. 靠近车辆，让银河 App 成功连接一次。\n"
                        + "3. 本应用自动识别本车蓝牙，不需要填写 VIN 或 MAC。\n"
                        + "4. 保持本应用后台运行；息屏放口袋，靠近解锁，离开上锁。\n"
                        + "5. 若系统清理后台，请在电池设置里允许本应用后台高耗电。",
                13, COLOR_TEXT, Typeface.NORMAL
        );
        LinearLayout.LayoutParams helpLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        helpLp.topMargin = dp(10);
        helpCard.addView(help, helpLp);
        root.addView(helpCard, cardLp());

        LinearLayout logCard = card();
        logCard.addView(text("最近 10 条关键日志 (最新在上)", 16, COLOR_ACCENT, Typeface.BOLD));
        logView = text("", 11, COLOR_MUTED, Typeface.NORMAL);
        logView.setTypeface(Typeface.MONOSPACE);
        logView.setLineSpacing(0f, 1.15f);
        logView.setMaxLines(12);
        LinearLayout.LayoutParams logLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        logLp.topMargin = dp(10);
        logCard.addView(logView, logLp);

        LinearLayout.LayoutParams logHintLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        logHintLp.topMargin = dp(10);
        logCard.addView(text("后台自动记录判定原因、连接耗时及重启信息，最多 4 MiB；导出时隐藏车辆地址与尾号。",
                12, COLOR_MUTED, Typeface.NORMAL), logHintLp);
        exportLogButton = button("导出排查日志（ZIP）");
        exportLogButton.setOnClickListener(view -> chooseLogExportDestination());
        logCard.addView(exportLogButton, buttonLp());

        Button clearLogBtn = button("清空日志");
        clearLogBtn.setOnClickListener(v -> {
            if (service != null) {
                service.clearLogs();
                refreshUi();
            }
        });
        logCard.addView(clearLogBtn, buttonLp());
        root.addView(logCard, cardLp());

        setContentView(scrollView);
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
        if (notificationPermissionButton != null) {
            notificationPermissionButton.setVisibility(notificationRuntimePermission
                    ? View.GONE : View.VISIBLE);
        }
        if (batteryWhitelistButton != null) {
            batteryWhitelistButton.setText(batteryWhitelisted
                    ? "电池白名单：已加入" : "加入电池白名单");
        }

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
                    store.getUnlockDbm(), store.getLockDbm(), store.getLockDwellMs()));
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
                    status.unlockDbm, status.lockDbm, status.lockDwellMs));
            logView.setText(service.getLogText());
        }

        permissionView.setText(
                "蓝牙权限：" + yesNo(btPermission) + "\n"
                        + "通知监听：" + notificationStatusText(notificationAccess, listenerConnected) + "\n"
                        + "系统通知权限：" + yesNo(notificationRuntimePermission) + "\n"
                        + (notificationAccess && !listenerConnected
                        ? "系统已授权但没有绑定监听，请点“修复通知监听”，在系统页关闭再开启本应用。\n"
                        : notificationAccess ? "" : "通知使用权是必需权限，否则无法读取和点击银河通知按钮。\n")
                        + "电池白名单：" + yesNo(batteryWhitelisted) + "\n"
                        + "银河 App：" + (geelyInstalled ? "已安装" : "未安装")
        );
        if (vehicleParamView != null) vehicleParamView.setText(VehicleParameterProbe.loadDisplay(this));
    }

    /** 每次界面刷新采样运行态原始/平滑RSSI；失效时绘制断点，阈值随用户配置实时更新。 */
    private void updateRssiChart(PassiveKeyService.Status status) {
        if (rssiChartView == null || rssiChartSummaryView == null) return;
        long now = android.os.SystemClock.elapsedRealtime();
        float unlock = status != null ? status.unlockDbm : new VehicleIdentityStore(this).getUnlockDbm();
        float lock = status != null ? status.lockDbm : new VehicleIdentityStore(this).getLockDbm();
        rssiChartView.setThresholds(unlock, lock);
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
        return connected ? "已授权 · 监听在线" : "已授权 · 系统未绑定";
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
        super.onSaveInstanceState(outState);
    }

    /** 在后台生成白名单ZIP并写入用户指定文件，日志写入与车控线程保持分离。 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_LOG_EXPORT) return;
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            DiagnosticLog.detail("Export", "EXPORT_CANCELLED", "");
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
     * 弹出自定义信号阈值设置框：允许自定义靠近解锁阈值(dBm)、离开上锁阈值(dBm)与离开确认秒数。
     * 独立弹窗固定展示字段含义、范围与推荐值；只在用户保存且三项校验通过后写配置。
     */
    private void promptCustomThresholds() {
        if (thresholdSettingsDialog != null && thresholdSettingsDialog.isShowing()) return;
        VehicleIdentityStore store = new VehicleIdentityStore(this);
        PassiveKeyService.Status status = service != null ? service.getStatus() : null;
        float unlockDbm = status != null ? status.unlockDbm : store.getUnlockDbm();
        float lockDbm = status != null ? status.lockDbm : store.getLockDbm();
        long dwellMs = status != null ? status.lockDwellMs : store.getLockDwellMs();
        thresholdSettingsDialog = ThresholdSettingsDialog.show(this, unlockDbm, lockDbm, dwellMs, (unlock, lock, dwell) -> {
            if (service != null) {
                service.updateThresholds(unlock, lock, dwell);
            } else {
                store.saveThresholds(unlock, lock, dwell);
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

    private LinearLayout card() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(16), dp(16), dp(16), dp(16));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COLOR_CARD);
        bg.setCornerRadius(dp(16));
        layout.setBackground(bg);
        return layout;
    }

    private LinearLayout.LayoutParams cardLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.bottomMargin = dp(14);
        return lp;
    }

    private Button button(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setTextColor(Color.rgb(8, 17, 31));
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(COLOR_ACCENT));
        return button;
    }

    private LinearLayout.LayoutParams buttonLp() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(46)
        );
        lp.topMargin = dp(10);
        return lp;
    }

    private Switch switchRow(String title, String detail) {
        Switch toggle = new Switch(this);
        toggle.setText(title + "\n" + detail);
        toggle.setTextSize(14);
        toggle.setTextColor(COLOR_TEXT);
        toggle.setGravity(Gravity.CENTER_VERTICAL);
        toggle.setPadding(0, dp(10), 0, dp(10));
        return toggle;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private String yesNo(boolean value) {
        return value ? "已开启" : "未开启";
    }
}
