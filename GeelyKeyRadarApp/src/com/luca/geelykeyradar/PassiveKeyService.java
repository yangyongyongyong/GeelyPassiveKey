package com.luca.geelykeyradar;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 极简无感钥匙前台服务。
 *
 * 只保留：
 * - 息屏 BLE 扫描 / 连接态 RSSI 读取；
 * - 自动学习本车蓝牙身份；
 * - 靠近一次解锁、持续远离上锁；
 * - 通过银河官方通知栏按钮完成车控及用户主动发起的手动链路测试。
 *
 * 不包含摇一摇、电源键、快捷磁贴、桌面组件、里程、哨兵、语音等功能。
 */
public class PassiveKeyService extends Service implements SensorEventListener {

    private static final String TAG = "PassiveKey";
    public static final String ACTION_START = "com.luca.geelykeyradar.action.START_PASSIVE_KEY";

    private static final String CHANNEL_ID = "passive_key_channel";
    private static final int NOTIFICATION_ID = 2027;
    private static final String ACTION_GATT_CONN_CHANGE =
            "android.bluetooth.gatt.profile.action.CONNECTION_STATE_CHANGED";
    private static final long CANDIDATE_TTL_MS = 20000L;
    private static final long GATT_RETRY_MS = 4500L;
    private static final long GATT_CONNECT_TIMEOUT_MS = 3500L;
    private static final long GATT_POLL_MS = 500L;
    private static final long SIGNAL_EXPIRE_UI_MS = 6000L;
    private static final long SCAN_RESTART_AFTER_MS = 45000L;
    private static final long SCAN_RESTART_THROTTLE_MS = 90000L;
    private static final long WAKE_LOCK_NEAR_MS = 5 * 60 * 1000L;
    private static final long WAKE_LOCK_BOOTSTRAP_MS = 45000L;
    private static final long WAKE_LOCK_TIMEOUT_MS = 15 * 60 * 1000L;
    private static final long DIAGNOSTIC_INTERVAL_MS = 5000L;
    private static final long HEARTBEAT_INTERVAL_MS = 30000L;
    private static final long MANUAL_CLICK_GAP_MS = 2000L;

    private static volatile boolean running = false;

    private final IBinder binder = new LocalBinder();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Map<String, GeelyAdvertisement> candidates = new ConcurrentHashMap<>();
    private final DiagnosticCounters diagnostics = new DiagnosticCounters();
    private final PendingControl pendingControl = new PendingControl();
    private final Runnable notificationStateListener = this::handleOfficialNotificationUpdate;

    private VehicleIdentityStore identityStore;
    private volatile VehicleIdentity identity;
    private volatile PassiveKeyLogic logic = new PassiveKeyLogic();
    private volatile RssiFilter rssiFilter = new RssiFilter();
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bleScanner;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private volatile boolean autoUnlock = true;
    private volatile boolean autoLock = true;
    private volatile boolean serviceEnabled = true;
    private volatile boolean blockWhenCharging = true;
    private volatile boolean wiredCharging = false;
    private volatile float unlockDbm = VehicleIdentityStore.DEFAULT_UNLOCK_DBM;
    private volatile float lockDbm = VehicleIdentityStore.DEFAULT_LOCK_DBM;
    private volatile long lockDwellMs = VehicleIdentityStore.DEFAULT_LOCK_DWELL_MS;
    private volatile boolean isScanning = false;
    private boolean screenOn = true;
    private volatile boolean proximityCovered = false;
    private volatile boolean geelyConnected = false;
    private volatile boolean gattConnected = false;
    private volatile boolean waitForDisconnectToLearn = false;
    private boolean receiverRegistered = false;

    private volatile long lastTargetAdvertAtMs = 0L;
    private long lastScreenOffAtMs = 0L;
    private long lastScanStartAtMs = 0L;
    private long lastScanRestartAtMs = 0L;
    private long lastScanRecoveryAtMs = 0L;
    private volatile long lastSampleAtMs = 0L;
    private long lastAcceptedScanNanos = 0L;
    private String lastAcceptedScanAddress = "";
    private long lastGattRssiAtMs = 0L;
    private long lastGattReadRequestedAtMs = 0L;
    private boolean gattReadPending = false;
    private long lastDiagnosticAtMs = 0L;
    private long lastHeartbeatAtMs = 0L;
    private long lastTickAtMs = 0L;
    private long lastScanCallbackAtMs = 0L;
    private long lastAcceptedAdvertAtMs = 0L;
    private long firstAdvertInSessionAtMs = 0L;
    private long firstWakeInSessionAtMs = 0L;
    private long lastDecisionLogAtMs = 0L;
    private long lastDecisionHeartbeatAtMs = 0L;
    private long lastPendingLogAtMs = 0L;
    private String lastDecisionReason = "";
    private String lastPendingWaitReason = "";
    private boolean signalMissingLogged = false;
    private int gattAttemptId = 0;
    private int lastGattStatus = -1;
    private int lastGattReadStatus = -1;
    private String serviceRun = "";
    private long lastRssiWarningAtMs = 0L;
    private boolean pendingProximityCovered = false;
    private volatile long lastWakeAttemptAtMs = 0L;
    private long lastGattAttemptAtMs = 0L;
    private volatile long lastGeelyConnectedAtMs = 0L;
    private volatile long lastNotificationUpdateAtMs = 0L;
    private long lastListenerRebindAtMs = 0L;
    private volatile long lastZoneLogAtMs = 0L;
    private volatile long lastLearnAttemptAtMs = 0L;
    private volatile int wakeAttempts = 0;
    private volatile int unlockCount = 0;
    private volatile int lockCount = 0;
    private int manualRequestSequence = 0;
    private long nextManualControlAtMs = 0L;
    private volatile String lastManualControlMessage = "请先在银河 App 连接蓝牙钥匙，再点击测试。";
    private volatile int lastRawRssi = -127;
    private float lastSmoothRssi = Float.NaN;
    private float lastApproachRssi = Float.NaN;
    private volatile String lastSource = "";
    private volatile String lastAction = "等待车辆";
    private volatile String lastZone = "等待车辆蓝牙信号";
    private volatile String lastZoneCategory = "";

    private volatile GeelyAdvertisement currentAd;
    private volatile GeelyAdvertisement lastWakeAd;
    private volatile BluetoothDevice lastObservedCarDevice;
    private volatile String lastObservedBleMac = "";
    private volatile long lastAclConnectedAtMs = 0L;
    private volatile long lastSelfGattCloseAtMs = 0L;
    private volatile String lastSelfGattAddress = "";
    private volatile String primaryCandidateKey = "";
    private volatile BluetoothGatt activeGatt;

    public final class LocalBinder extends Binder {
        public PassiveKeyService getService() {
            return PassiveKeyService.this;
        }
    }

    /**
     * 给界面轮询的只读状态快照。
     */
    public static final class Status {
        public boolean running;
        public boolean serviceEnabled;
        public boolean bluetoothOn;
        public boolean scanning;
        public boolean notificationAccess;
        public boolean geelyInstalled;
        public boolean geelyConnected;
        public boolean identityLearned;
        public boolean gattConnected;
        public boolean autoUnlock;
        public boolean autoLock;
        public boolean blockWhenCharging;
        public boolean wiredCharging;
        public boolean manualControlEnabled;
        public float unlockDbm;
        public float lockDbm;
        public long lockDwellMs;
        public int rssi = -127;
        public int unlockCount;
        public int lockCount;
        public long lastSampleAgeMs = -1L;
        public String vehicleName = "";
        public String vehicleAddress = "";
        public String zone = "";
        public String lastAction = "";
        public String source = "";
        public String buttonDiag = "";
        public String learningMessage = "";
        public String learningSource = "";
        public String manualControlMessage = "";
    }

    /** 返回前台服务实例是否仍处于运行状态。 */
    public static boolean isRunning() {
        return running;
    }

    /** 初始化前台扫描、通知就绪回调和有界日志。 */
    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        DiagnosticLog.initialize(this);
        serviceRun = DiagnosticLog.serviceStarted();
        createNotificationChannel();
        try {
            startForeground(NOTIFICATION_ID, buildNotification("正在启动"));
        } catch (Throwable t) {
            Log.w(TAG, "startForeground 延后等待权限: " + t.getMessage());
            DiagnosticLog.bridgeError("Service", "FOREGROUND_START_ERROR", t);
        }

        identityStore = new VehicleIdentityStore(this);
        identity = identityStore.load();
        serviceEnabled = identityStore.isServiceEnabled();
        blockWhenCharging = identityStore.isBlockWhenChargingEnabled();
        autoUnlock = identityStore.isAutoUnlockEnabled();
        autoLock = identityStore.isAutoLockEnabled();
        unlockDbm = identityStore.getUnlockDbm();
        lockDbm = identityStore.getLockDbm();
        lockDwellMs = identityStore.getLockDwellMs();
        logic.setThresholds(unlockDbm, lockDbm, lockDwellMs);

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : BluetoothAdapter.getDefaultAdapter();
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        screenOn = powerManager == null || powerManager.isInteractive();
        if (!screenOn) {
            lastScreenOffAtMs = SystemClock.elapsedRealtime();
        }
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GeelyPassiveKey:radar");
            wakeLock.setReferenceCounted(false);
        }

        initProximitySensor();
        refreshWiredChargingState(null);
        registerSystemReceiver();
        KeyNotificationBridgeService.setStateListener(notificationStateListener);
        if (serviceEnabled) {
            ServiceWatchdogReceiver.schedule(this);
            GeelyBridge.bindKeepAlive(this);
            KeyNotificationBridgeService.ensureRebound(this);
            startBleScan();
        } else {
            lastZone = "无感进出服务已关闭";
            lastAction = "服务已由总开关暂停";
        }
        handler.post(tickRunnable);

        appendLog(String.format(Locale.US,
                "极简无感钥匙启动 (总开关=%s | 解锁≥%.0fdBm | 上锁≤%.0fdBm / %.1fs)",
                serviceEnabled ? "开" : "关", unlockDbm, lockDbm, lockDwellMs / 1000.0f));
        if (identity != null) {
            appendLog("已加载本车身份: " + identityDiagnostic());
        } else {
            appendLog("本车身份未学习：先靠近车辆，让银河 App 连接一次，本应用会自动记住");
        }
        DiagnosticLog.event("Service", "INITIAL_STATE", getDiagnosticSnapshot());
    }

    /** 记录启动来源和系统重建，区分启动命令与开关切换。 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DiagnosticLog.event("Lifecycle", "SERVICE_START_COMMAND", "run=" + serviceRun + " start_id=" + startId
                + " flags=" + flags + " null_intent=" + (intent == null)
                + " start_action=" + (intent == null ? "system_restart" : intent.getAction()));
        if (serviceEnabled && !isScanning) {
            startBleScan();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** 停止服务时撤销请求和通知回调，避免销毁后再触发车控。 */
    @Override
    public void onDestroy() {
        cancelPendingControl("服务停止");
        serviceEnabled = false;
        KeyNotificationBridgeService.clearStateListener(notificationStateListener);
        running = false;
        handler.removeCallbacksAndMessages(null);
        stopBleScan();
        closeGatt("服务停止");
        releaseWakeLock();
        if (sensorManager != null) {
            try {
                sensorManager.unregisterListener(this);
            } catch (Throwable ignored) {
            }
        }
        if (receiverRegistered) {
            try {
                unregisterReceiver(systemReceiver);
            } catch (Throwable ignored) {
            }
            receiverRegistered = false;
        }
        appendLog("极简无感钥匙停止");
        try {
            stopForeground(true);
        } catch (Throwable ignored) {
        }
        DiagnosticLog.serviceStopped(serviceRun);
        super.onDestroy();
    }

    /** 记录任务被划走，但保持原本前台服务生命周期行为。 */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        DiagnosticLog.event("Lifecycle", "TASK_REMOVED", "run=" + serviceRun + " " + signalDiagnostic(SystemClock.elapsedRealtime()));
        super.onTaskRemoved(rootIntent);
    }

    /** 记录系统内存压力而不改变扫描或车控策略。 */
    @Override
    public void onTrimMemory(int level) {
        DiagnosticLog.event("Lifecycle", "TRIM_MEMORY", "run=" + serviceRun + " level=" + level);
        super.onTrimMemory(level);
    }

    // ==================== UI 状态 ====================

    /** 返回官方连接与本应用测距通道相互独立的界面状态。 */
    public Status getStatus() {
        long now = SystemClock.elapsedRealtime();
        boolean signalFresh = lastSampleAtMs > 0L && (now - lastSampleAtMs) <= SIGNAL_EXPIRE_UI_MS;
        Status status = new Status();
        status.running = running;
        status.serviceEnabled = serviceEnabled;
        status.bluetoothOn = bluetoothAdapter != null && bluetoothAdapter.isEnabled();
        status.scanning = isScanning;
        status.notificationAccess = KeyNotificationBridgeService.isNotificationListenerEnabled(this);
        status.geelyInstalled = GeelyBridge.isGeelyInstalled(this);
        status.geelyConnected = geelyConnected;
        status.identityLearned = hasCompleteIdentity();
        status.gattConnected = gattConnected;
        status.autoUnlock = autoUnlock;
        status.autoLock = autoLock;
        status.blockWhenCharging = blockWhenCharging;
        status.wiredCharging = wiredCharging;
        status.manualControlEnabled = running && serviceEnabled && !isChargingBlocked()
                && !isDriving(now) && now >= nextManualControlAtMs;
        status.manualControlMessage = !serviceEnabled ? "服务总开关已关闭，手动与自动操作均已停止"
                : isChargingBlocked() ? "已插数据线/充电线，禁控保护中；拔线后再测试"
                : isDriving(now) ? "车辆行驶中，禁止手动测试"
                : (!KeyNotificationBridgeService.isListenerConnected()
                ? "系统已解绑通知监听，请点下方“修复通知监听”"
                : lastManualControlMessage);
        status.unlockDbm = unlockDbm;
        status.lockDbm = lockDbm;
        status.lockDwellMs = lockDwellMs;
        status.rssi = signalFresh ? lastRawRssi : -127;
        status.unlockCount = unlockCount;
        status.lockCount = lockCount;
        status.lastSampleAgeMs = lastSampleAtMs > 0L
                ? Math.max(0L, now - lastSampleAtMs) : -1L;
        status.vehicleName = identity != null ? identity.displayName() : "";
        status.vehicleAddress = identity != null ? identity.address : "";
        status.zone = !serviceEnabled ? "无感进出服务已关闭"
                : (isChargingBlocked() ? "已插充电线 · 禁止解闭锁"
                : (signalFresh ? lastZone : "不在车辆附近 (无信号)"));
        status.lastAction = lastAction;
        status.source = signalFresh ? lastSource : "";
        status.buttonDiag = KeyNotificationBridgeService.getLastButtonCaptureDiag();
        status.learningSource = identityStore.getLearningSource();
        status.learningMessage = learningMessage();
        return status;
    }

    /** 所有模块共用最近十条关键事件，界面容量保持固定。 */
    public String getLogText() {
        return DiagnosticLog.recentText();
    }

    /**
     * 清空内存与本地日志。
     */
    public void clearLogs() {
        DiagnosticLog.clear();
        appendLog("日志已清空");
    }

    /** 手动导出时只读取当前快照，ZIP在日志线程生成，不触发扫描、连接或车控。 */
    public Future<File> exportLogs(File destination) {
        return DiagnosticLog.export(destination, getDiagnosticSnapshot());
    }

    /** 返回可导出的完整状态，所有计时使用单调时间且身份只有短指纹。 */
    public String getDiagnosticSnapshot() {
        long now = SystemClock.elapsedRealtime();
        return "run=" + serviceRun + "\n" + environmentDiagnostic()
                + "\n" + signalDiagnostic(now) + "\n" + logic.diagnosticSnapshot(now)
                + "\n" + pendingControl.diagnosticSnapshot(now) + " wait_reason=" + lastPendingWaitReason
                + "\n" + scanDiagnostic(now, false) + "\n" + gattDiagnostic(now)
                + "\n" + KeyNotificationBridgeService.diagnosticSnapshot(now)
                + "\nlearning=" + learningMessage() + " learning_source=" + identityStore.getLearningSource()
                + " waiting_reconnect=" + waitForDisconnectToLearn
                + "\nmanual_req_id=" + manualRequestSequence + " manual_cooldown_left_ms="
                + Math.max(0L, nextManualControlAtMs - now) + " manual_result=" + lastManualControlMessage
                + "\n" + DiagnosticLog.health();
    }

    /** 用户主动测试当前官方连接，绕过距离条件但不绕过安全保护，只尝试发送一次。 */
    public String requestManualControl(PassiveKeyLogic.Action action) {
        long now = SystemClock.elapsedRealtime();
        int sequence = ++manualRequestSequence;
        String label = action == PassiveKeyLogic.Action.UNLOCK ? "手动解锁" : "手动上锁";
        DiagnosticLog.event("Control", "MANUAL_REQUEST", "manual_req_id=" + sequence + " action=" + action);
        refreshWiredChargingState(null);
        String blockedReason = manualControlBlockReason(action, now);
        if (!blockedReason.isEmpty()) {
            lastManualControlMessage = label + "未发送：" + blockedReason;
            lastAction = lastManualControlMessage;
            DiagnosticLog.event("Control", "MANUAL_BLOCKED", "manual_req_id=" + sequence
                    + " action=" + action + " reason=" + blockedReason + " " + signalDiagnostic(now));
            appendLog(lastManualControlMessage);
            updateNotificationIfNeeded(true);
            return lastManualControlMessage;
        }
        cancelPendingControl("用户发起" + label + "，取消自动待发请求");
        nextManualControlAtMs = now + MANUAL_CLICK_GAP_MS;
        KeyNotificationBridgeService.ControlResult result =
                KeyNotificationBridgeService.triggerManualControlViaNotification(action);
        if (result != KeyNotificationBridgeService.ControlResult.NOT_SENT) {
            logic.onManualControlSent(action, now);
        }
        if (result == KeyNotificationBridgeService.ControlResult.SENT) {
            if (action == PassiveKeyLogic.Action.UNLOCK) unlockCount++;
            else lockCount++;
            lastManualControlMessage = label + "指令已交给银河（未获取车锁回执），请观察车辆";
        } else if (result == KeyNotificationBridgeService.ControlResult.UNKNOWN) {
            lastManualControlMessage = label + "发送结果不确定，不会重发，请核实车锁";
        } else {
            lastManualControlMessage = label + "未发送：官方通知按钮不可用或已失效，请检查银河连接后重试";
        }
        lastAction = lastManualControlMessage;
        DiagnosticLog.event("Control", "MANUAL_RESULT", "manual_req_id=" + sequence + " action=" + action
                + " result=" + result + " " + KeyNotificationBridgeService.getLastControlDiagnostic());
        appendLog(lastManualControlMessage + " #" + sequence);
        updateNotificationIfNeeded(true);
        return lastManualControlMessage;
    }

    /** 返回手动测试被拒绝的具体原因；身份学习与RSSI不作为官方按钮测试的前置条件。 */
    private String manualControlBlockReason(PassiveKeyLogic.Action action, long now) {
        if (action != PassiveKeyLogic.Action.UNLOCK && action != PassiveKeyLogic.Action.LOCK) return "无效动作";
        if (Looper.myLooper() != Looper.getMainLooper()) return "只能由界面主线程发起";
        if (!running || !serviceEnabled) return "服务总开关已关闭";
        if (isChargingBlocked()) return "已插数据线/充电线，请拔线后测试";
        if (isDriving(now)) return "车辆行驶中，禁止操作车锁";
        if (!hasBluetoothPermission()) return "请先授予蓝牙权限";
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) return "请先开启手机蓝牙";
        if (!GeelyBridge.isGeelyInstalled(this)) return "请先安装银河 App";
        if (KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                && !KeyNotificationBridgeService.isListenerConnected()) {
            KeyNotificationBridgeService.requestImmediateRecovery(this);
            return "通知监听被系统解绑，请点下方“修复通知监听”，在系统页关闭再开启";
        }
        if (!KeyNotificationBridgeService.isNotificationListenerEnabled(this)) return "请先开启本应用的通知使用权";
        if (!KeyNotificationBridgeService.isGeelyNotificationPresent()) {
            return "通知监听已连接，但尚未读到银河钥匙通知；请保持银河连接并稍后重试";
        }
        if (!KeyNotificationBridgeService.isListenerConnected()) return "通知监听尚未就绪，请稍后重试";
        if (!isOfficialConnected()) return "银河蓝牙钥匙未连接，请先在银河 App 连接车辆";
        if (!KeyNotificationBridgeService.hasControlIntent(action)) return "未找到对应的银河通知按钮";
        if (now < nextManualControlAtMs) return "操作过于频繁，请等待两秒后重试";
        return "";
    }

    /** 更新解锁开关并立即撤销对应待发请求。 */
    public void setAutoUnlockEnabled(boolean enabled) {
        autoUnlock = enabled;
        identityStore.setAutoUnlockEnabled(enabled);
        if (!enabled && pendingControl.getAction() == PassiveKeyLogic.Action.UNLOCK) {
            cancelPendingControl("靠近自动解锁已关闭");
        }
        appendLog("靠近自动解锁: " + (enabled ? "开" : "关"));
    }

    /** 更新上锁开关并立即撤销对应待发请求。 */
    public void setAutoLockEnabled(boolean enabled) {
        autoLock = enabled;
        identityStore.setAutoLockEnabled(enabled);
        if (!enabled && pendingControl.getAction() == PassiveKeyLogic.Action.LOCK) {
            cancelPendingControl("远离自动上锁已关闭");
        }
        appendLog("远离自动上锁: " + (enabled ? "开" : "关"));
    }

    /**
     * 插充电线后禁止解闭锁开关（USB/AC 有线充电认为人在驾驶室或调试中，无线充电不拦截）。
     */
    public void setBlockWhenChargingEnabled(boolean enabled) {
        blockWhenCharging = enabled;
        identityStore.setBlockWhenChargingEnabled(enabled);
        if (isChargingBlocked()) {
            cancelPendingControl("插线禁控保护");
        }
        appendLog("插充电线禁止解闭锁: " + (enabled ? "开" : "关")
                + (wiredCharging ? " (当前已插线)" : ""));
        updateNotificationIfNeeded(true);
    }

    /**
     * 是否因插着 USB/AC 有线充电线而禁止操作车锁。
     */
    private boolean isChargingBlocked() {
        return blockWhenCharging && wiredCharging;
    }

    /**
     * 读取或更新当前是否处于 USB/AC 有线充电状态（无线充电不触发）。
     */
    private void refreshWiredChargingState(Intent batteryIntent) {
        try {
            Intent sticky = batteryIntent;
            if (sticky == null) {
                sticky = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
            if (sticky == null) return;
            int plugged = sticky.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean wired = (plugged & (BatteryManager.BATTERY_PLUGGED_AC | BatteryManager.BATTERY_PLUGGED_USB)) != 0;
            if (wired != wiredCharging) {
                wiredCharging = wired;
                if (wired && blockWhenCharging) {
                    cancelPendingControl("检测到有线充电");
                    appendLog("🔌 检测到已插充电线：已激活车内禁控保护，禁止自动解锁/上锁");
                } else if (!wired) {
                    appendLog("🔌 充电线已拔出：恢复正常无感解锁/上锁");
                }
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * 无感进出服务总开关：关闭时立即停止扫描、断开 GATT、取消一切重发队列，防止任何后台操作。
     */
    public void setServiceEnabled(boolean enabled) {
        serviceEnabled = enabled;
        identityStore.setServiceEnabled(enabled);
        cancelPendingControl("服务总开关变更");
        if (enabled) {
            logic.reset();
            rssiFilter = new RssiFilter();
            lastSampleAtMs = 0L;
            geelyConnected = false;
            lastAction = "服务已启用，等待车辆";
            lastZone = "不在车辆附近 (无信号)";
            appendLog("✓ 已开启无感进出总开关，恢复 BLE 扫描与自动解闭锁");
            GeelyBridge.bindKeepAlive(this);
            ServiceWatchdogReceiver.schedule(this);
            KeyNotificationBridgeService.ensureRebound(this);
            startBleScan();
        } else {
            ServiceWatchdogReceiver.cancel(this);
            stopBleScan();
            closeGatt("用户关闭无感进出总开关");
            releaseWakeLock();
            logic.reset();
            lastRawRssi = -127;
            lastSampleAtMs = 0L;
            lastZone = "无感进出服务已关闭";
            lastAction = "已手动停止无感进出服务";
            appendLog("🛑 已关闭无感进出总开关：已停止 BLE 扫描、GATT 与一切自动解闭锁操作");
        }
        updateNotificationIfNeeded(true);
    }

    /**
     * 更新自定义阈值（靠近解锁 dBm、离开上锁 dBm、离开确认毫秒数）。
     */
    public void updateThresholds(float newUnlockDbm, float newLockDbm, long newLockDwellMs) {
        float clampedUnlock = Math.max(-90.0f, Math.min(-40.0f, newUnlockDbm));
        float clampedLock = Math.max(-98.0f, Math.min(clampedUnlock - 6.0f, newLockDbm));
        long clampedDwell = Math.max(1500L, Math.min(20000L, newLockDwellMs));
        this.unlockDbm = clampedUnlock;
        this.lockDbm = clampedLock;
        this.lockDwellMs = clampedDwell;
        identityStore.saveThresholds(clampedUnlock, clampedLock, clampedDwell);
        logic.setThresholds(clampedUnlock, clampedLock, clampedDwell);
        cancelPendingControl("用户调整阈值，重新确认距离");
        appendLog(String.format(Locale.US,
                "✓ 已更新自定义阈值：靠近解锁 ≥ %.0f dBm | 离开上锁 ≤ %.0f dBm (连续 %.1fs)",
                clampedUnlock, clampedLock, clampedDwell / 1000.0f));
        updateNotificationIfNeeded(true);
    }

    /**
     * 允许用户手动填入车架号后 3 位（例如 "123"）辅助锁定本车，或留空完全走自动识别。
     */
    public void bindByVinSuffix(String vinTail3Input) {
        if (vinTail3Input == null) return;
        String digits = vinTail3Input.replaceAll("[^0-9A-Za-z]", "").trim();
        if (digits.length() > 3) {
            digits = digits.substring(digits.length() - 3);
        }
        if (digits.length() < 2) {
            appendLog("车架号尾号至少需要 2~3 位字符");
            return;
        }
        cancelPendingControl("用户更新本车身份");
        String tail3 = digits.length() == 3 ? digits : "";
        String tail2 = digits.substring(digits.length() - 2);
        String name = !tail3.isEmpty() ? ("GeelyVehicle" + tail3) : "";
        boolean sameHint = identity != null && (!tail3.isEmpty()
                ? tail3.equalsIgnoreCase(identity.vinTail3) : tail2.equalsIgnoreCase(identity.vinTail2));
        String addr = sameHint ? identity.address : "";
        if (!sameHint) {
            closeGatt("更新本车尾号匹配特征");
            currentAd = null;
            lastWakeAd = null;
            lastObservedCarDevice = null;
            lastObservedBleMac = "";
            candidates.clear();
            logic.reset();
            rssiFilter = new RssiFilter();
            lastSampleAtMs = 0L;
        }
        identity = new VehicleIdentity(addr, name, tail2, tail3, "", System.currentTimeMillis());
        identityStore.save(identity, VehicleLearningState.MANUAL);
        waitForDisconnectToLearn = false;
        appendLog("已设置本车匹配特征（不代表已获取地址）: " + identityDiagnostic());
        stopBleScan();
        startBleScan();
        updateNotificationIfNeeded(true);
    }

    /** 更换车辆前取消旧请求和测距状态。 */
    public void resetLearnedVehicle() {
        boolean connectedNow = isOfficialConnected();
        VehicleIdentity connectedEvidence = findIdentityFromConnectedGatt();
        cancelPendingControl("重新学习本车");
        identityStore.clear();
        identity = null;
        currentAd = null;
        lastWakeAd = null;
        lastObservedCarDevice = null;
        lastObservedBleMac = "";
        closeGatt("重新学习本车");
        candidates.clear();
        primaryCandidateKey = "";
        logic.reset();
        rssiFilter = new RssiFilter();
        wakeAttempts = 0;
        lastGeelyConnectedAtMs = 0L;
        lastLearnAttemptAtMs = 0L;
        waitForDisconnectToLearn = connectedNow && connectedEvidence == null;
        if (VehicleLearningState.canLearnConnected(hasBluetoothPermission(), connectedNow,
                waitForDisconnectToLearn, connectedEvidence != null)) {
            identity = connectedEvidence;
            identityStore.save(identity, VehicleLearningState.CONNECTED);
            waitForDisconnectToLearn = false;
            lastAction = "已从当前真实蓝牙连接重新学习本车";
        } else {
            lastAction = learningMessage();
        }
        DiagnosticLog.event("Learning", "RESET_REQUEST", "complete=" + hasCompleteIdentity()
                + " waiting_reconnect=" + waitForDisconnectToLearn + " reason=" + lastAction);
        appendLog(lastAction);
        stopBleScan();
        startBleScan();
        maybeLearnVehicle(SystemClock.elapsedRealtime());
        updateNotificationIfNeeded(true);
    }

    /** 手填尾号或旧名称不是完整本车地址，学习完成前不执行自动车控。 */
    private boolean hasCompleteIdentity() {
        return identity != null && VehicleLearningState.hasAddress(identity.address);
    }

    /** 反馈缺权限、等待地址及等待重新连接的具体状态。 */
    private String learningMessage() {
        return VehicleLearningState.message(serviceEnabled, hasBluetoothPermission(),
                KeyNotificationBridgeService.isNotificationListenerEnabled(this), hasCompleteIdentity(),
                identity != null, geelyConnected, waitForDisconnectToLearn);
    }

    // ==================== BLE 扫描 ====================

    private final Runnable restartScanRunnable = new Runnable() {
        @Override
        public void run() {
            startBleScan();
        }
    };

    private boolean hasBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /** 使用硬件过滤开启低延迟扫描，失败时保持过滤条件重试，避免息屏静默挂起。 */
    private void startBleScan() {
        if (!serviceEnabled) {
            return;
        }
        if (!hasBluetoothPermission()) {
            appendLog("缺少蓝牙扫描/连接权限，等待授权");
            diagnostics.increment("scan_permission_denied");
            return;
        }
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            appendLog("蓝牙未开启，等待蓝牙可用");
            return;
        }
        try {
            bleScanner = bluetoothAdapter.getBluetoothLeScanner();
            if (bleScanner == null) {
                handler.removeCallbacks(restartScanRunnable);
                handler.postDelayed(restartScanRunnable, 1000L);
                return;
            }
            if (isScanning) return;
            try {
                bleScanner.stopScan(scanCallback);
            } catch (Throwable ignored) {
            }
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                    .setReportDelay(0L)
                    .build();
            List<ScanFilter> filters = buildHardwareScanFilters();
            bleScanner.startScan(filters, settings, scanCallback);
            isScanning = true;
            lastScanStartAtMs = SystemClock.elapsedRealtime();
            DiagnosticLog.detail("Scan", "SCAN_START", "filters=" + (filters == null ? 0 : filters.size())
                    + " mode=LOW_LATENCY report_delay_ms=0 " + environmentDiagnostic());
            appendDiagnostic("开始硬件过滤低延迟扫描（支持息屏扫描与本车自动识别）");
            updateNotificationIfNeeded(true);
        } catch (SecurityException se) {
            appendLog("蓝牙扫描权限不足: " + se.getMessage());
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Scan", "SCAN_START_ERROR", t);
            appendLog("启动 BLE 扫描失败: " + t.getMessage());
            handler.removeCallbacks(restartScanRunnable);
            handler.postDelayed(restartScanRunnable, 2000L);
        }
    }

    /**
     * 构建硬件级 BLE ScanFilter 列表（OR 关系）。
     *
     * Android 在息屏时会挂起无 Filter 的扫描（Cannot start unfiltered scan in screen-off）。
     * 使用吉利/极氪通用广播特征作为硬件过滤，既能对所有吉利银河车型通用，又能保证息屏持续扫描。
     */
    private List<ScanFilter> buildHardwareScanFilters() {
        List<ScanFilter> filters = new ArrayList<>();
        try {
            // 1. 吉利/UAES V2 厂商广播 Company ID 0x01FE (510)
            filters.add(new ScanFilter.Builder().setManufacturerData(510, new byte[]{}).build());
            // 2. 吉利/极氪通用广播 Service UUID 0xFDFD
            filters.add(new ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid.fromString("0000fdfd-0000-1000-8000-00805f9b34fb"))
                    .build());
            // 3. 吉利/极氪 0x06FE (1790) 厂商数据兼容
            filters.add(new ScanFilter.Builder().setManufacturerData(1790, new byte[]{}).build());
            // 4. 若已学习本车名称或 MAC，加入精确硬件过滤
            VehicleIdentity id = this.identity;
            if (id != null) {
                if (!id.name.isEmpty()) {
                    filters.add(new ScanFilter.Builder().setDeviceName(id.name).build());
                }
                if (!id.address.isEmpty() && BluetoothAdapter.checkBluetoothAddress(id.address)) {
                    filters.add(new ScanFilter.Builder().setDeviceAddress(id.address).build());
                }
            }
        } catch (Throwable ignored) {
        }
        return filters.isEmpty() ? null : filters;
    }

    private void stopBleScan() {
        handler.removeCallbacks(restartScanRunnable);
        if (bleScanner != null && isScanning) {
            try {
                bleScanner.stopScan(scanCallback);
            } catch (Throwable ignored) {
            }
        }
        isScanning = false;
        DiagnosticLog.detail("Scan", "SCAN_STOP", scanDiagnostic(SystemClock.elapsedRealtime(), false));
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        /** 将平台回调串行投递到服务线程，排除重复和过期扫描结果。 */
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                handler.post(() -> onScanResult(callbackType, result));
                return;
            }
            diagnostics.increment("scan_callbacks");
            lastScanCallbackAtMs = SystemClock.elapsedRealtime();
            if (!serviceEnabled) {
                diagnostics.increment("scan_disabled");
                return;
            }
            GeelyAdvertisement ad = GeelyAdvertisement.from(result);
            if (ad == null) {
                diagnostics.increment("scan_not_geely");
                return;
            }
            long now = SystemClock.elapsedRealtime();
            long scanNanos = result.getTimestampNanos();
            long ageNanos = SystemClock.elapsedRealtimeNanos() - scanNanos;
            if (scanNanos <= 0L || ageNanos < 0L
                    || ageNanos > PassiveKeyLogic.SIGNAL_STALE_MS * 1000000L) {
                diagnostics.increment("scan_stale");
                return;
            }
            if (ad.address.equals(lastAcceptedScanAddress) && scanNanos <= lastAcceptedScanNanos) {
                diagnostics.increment("scan_duplicate");
                return;
            }
            pruneCandidates(now);
            candidates.put(ad.stableKey(), ad);

            if (identity != null) {
                if (identity.matches(ad)) {
                    lastAcceptedScanNanos = scanNanos;
                    lastAcceptedScanAddress = ad.address;
                    VehicleIdentity merged = identity.mergeWith(ad);
                    if (merged != identity) {
                        identity = merged;
                        identityStore.save(identity, VehicleLearningState.SCAN);
                        appendLog("已更新本车蓝牙特征: " + identityDiagnostic());
                    }
                    if (result.getDevice() != null) {
                        lastObservedCarDevice = result.getDevice();
                    }
                    if (!ad.address.isEmpty()) {
                        lastObservedBleMac = ad.address;
                    }
                    handleTargetAdvertisement(ad, now);
                } else {
                    diagnostics.increment("scan_other_vehicle");
                }
                return;
            }

            // 尚未学习本车时：如果只有单一吉利车广播且带明确车名或 01FE 帧，直接自动学习并接管！
            if (!waitForDisconnectToLearn
                    && (!ad.name.isEmpty() || !ad.vinTail2.isEmpty())
                    && candidates.size() == 1
                    && ad.rssi >= -88) {
                identity = VehicleIdentity.from(ad);
                identityStore.save(identity, VehicleLearningState.SCAN);
                if (result.getDevice() != null) {
                    lastObservedCarDevice = result.getDevice();
                }
                lastObservedBleMac = ad.address;
                appendLog("✓ 扫到本车特征广播，已自动锁定: " + identityDiagnostic());
            }

            GeelyAdvertisement primary = choosePrimaryCandidate(now);
            if (primary != null && primary.stableKey().equals(ad.stableKey())) {
                lastAcceptedScanNanos = scanNanos;
                lastAcceptedScanAddress = ad.address;
                if (result.getDevice() != null) {
                    lastObservedCarDevice = result.getDevice();
                }
                if (!ad.address.isEmpty()) {
                    lastObservedBleMac = ad.address;
                }
                handleTargetAdvertisement(ad, now);
            } else {
                diagnostics.increment("scan_non_primary");
            }

            if (geelyConnected || (lastGeelyConnectedAtMs > 0L && (now - lastGeelyConnectedAtMs) < 60000L)) {
                maybeLearnVehicle(now);
            }
        }

        /** 扫描失败时仅在缺少正常连接态样本时撤销驻留。 */
        @Override
        public void onScanFailed(int errorCode) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                handler.post(() -> onScanFailed(errorCode));
                return;
            }
            diagnostics.increment("scan_failures");
            DiagnosticLog.event("Scan", "SCAN_CALLBACK_ERROR", "code=" + errorCode
                    + " " + scanDiagnostic(SystemClock.elapsedRealtime(), false));
            if (errorCode == ScanCallback.SCAN_FAILED_ALREADY_STARTED) {
                isScanning = true;
                return;
            }
            isScanning = false;
            long now = SystemClock.elapsedRealtime();
            if (!gattConnected || (now - lastGattRssiAtMs) > PassiveKeyLogic.SIGNAL_STALE_MS) {
                logic.onSignalLost(now);
                cancelPendingControl("扫描失败且没有新鲜连接态信号");
            }
            long delay = (errorCode == ScanCallback.SCAN_FAILED_SCANNING_TOO_FREQUENTLY) ? 6500L : 1800L;
            appendLog("BLE 扫描失败 errorCode=" + errorCode + "，" + delay + "ms 后重试");
            handler.removeCallbacks(restartScanRunnable);
            handler.postDelayed(restartScanRunnable, delay);
        }
    };

    private void pruneCandidates(long now) {
        Iterator<Map.Entry<String, GeelyAdvertisement>> iterator = candidates.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, GeelyAdvertisement> entry = iterator.next();
            GeelyAdvertisement ad = entry.getValue();
            if (ad == null || (now - ad.seenAtMs) > CANDIDATE_TTL_MS) {
                iterator.remove();
            }
        }
    }

    private GeelyAdvertisement strongestCandidate(long now) {
        pruneCandidates(now);
        GeelyAdvertisement best = null;
        for (GeelyAdvertisement ad : candidates.values()) {
            if (ad == null) continue;
            if (best == null || ad.rssi > best.rssi
                    || (ad.rssi == best.rssi && ad.seenAtMs > best.seenAtMs)) {
                best = ad;
            }
        }
        return best;
    }

    /**
     * 身份尚未学习时固定一个主候选 12 秒。
     *
     * 附近同时有两辆吉利车时，不能把两辆车的 RSSI 混入同一个滤波器；先跟随最强的一辆，
     * 等官方 App 真正连上后再由连接事件决定哪一辆是本车。
     */
    private GeelyAdvertisement choosePrimaryCandidate(long now) {
        pruneCandidates(now);
        if (!primaryCandidateKey.isEmpty()) {
            GeelyAdvertisement current = candidates.get(primaryCandidateKey);
            if (current != null && (now - current.seenAtMs) <= 12000L) {
                return current;
            }
        }
        GeelyAdvertisement best = strongestCandidate(now);
        primaryCandidateKey = best != null ? best.stableKey() : "";
        return best;
    }

    /** 首帧先唤醒官方钥匙；连接态采样正常时不混入广播以免改变滤波频率。 */
    private void handleTargetAdvertisement(GeelyAdvertisement ad, long now) {
        diagnostics.increment("scan_accepted");
        lastAcceptedAdvertAtMs = now;
        if (lastTargetAdvertAtMs == 0L || now - lastTargetAdvertAtMs > 12000L) {
            firstAdvertInSessionAtMs = now;
            firstWakeInSessionAtMs = 0L;
            DiagnosticLog.event("Scan", "TARGET_FIRST_SEEN", "addr_id=" + DiagnosticLog.fingerprint(ad.address)
                    + " source=" + ad.sourceTag + " rssi=" + ad.rssi + " scan_age_ms="
                    + (SystemClock.elapsedRealtimeNanos() - ad.scanResult.getTimestampNanos()) / 1000000L
                    + " screen_on=" + screenOn);
        }
        currentAd = ad;
        lastTargetAdvertAtMs = now;
        refreshWakeLock(now);
        if (!geelyConnected) {
            maybeWakeGeely(ad, now);
        }
        if (!gattConnected || lastGattRssiAtMs == 0L
                || (now - lastGattRssiAtMs) > PassiveKeyLogic.SIGNAL_STALE_MS) {
            onFreshRssi(ad.rssi, "广播 " + ad.sourceTag, ad);
        } else {
            diagnostics.increment("scan_rssi_superseded_by_gatt");
        }
        if (identity == null && geelyConnected) {
            maybeLearnVehicle(now);
        }
    }

    // ==================== 唤醒官方 App ====================

    /** 仅使用近期真实广播提前唤醒，握手中的同一 ACL 不重复注入连接请求。 */
    private void maybeWakeGeely(GeelyAdvertisement ad, long now) {
        if (!serviceEnabled) return;
        if (ad == null || geelyConnected) return;
        if ((now - ad.seenAtMs) > PassiveKeyLogic.SIGNAL_STALE_MS) {
            diagnostics.increment("wake_ad_stale");
            return;
        }
        if (lastAclConnectedAtMs > 0L && (now - lastAclConnectedAtMs) < 5000L) {
            diagnostics.increment("wake_wait_acl_auth");
            return;
        }
        if (ad.rssi < -95) {
            diagnostics.increment("wake_weak_signal");
            return;
        }
        long throttle = ad.rssi >= -88 ? 1500L : 2500L;
        if ((now - lastWakeAttemptAtMs) < throttle) {
            diagnostics.increment("wake_throttled");
            return;
        }
        if (wakeAttempts >= 6 && (now - lastWakeAttemptAtMs) < 5000L) {
            diagnostics.increment("wake_backoff");
            return;
        }

        lastWakeAttemptAtMs = now;
        lastWakeAd = ad;
        wakeAttempts++;
        if (firstWakeInSessionAtMs == 0L) firstWakeInSessionAtMs = now;
        diagnostics.increment("wake_requests");
        DiagnosticLog.detail("Bridge", "WAKE_REQUEST", "attempt=" + wakeAttempts
                + " since_first_ad_ms=" + age(now, firstAdvertInSessionAtMs)
                + " addr_id=" + DiagnosticLog.fingerprint(ad.address) + " rssi=" + ad.rssi
                + " req_id=" + pendingControl.getSequence());
        GeelyBridge.wakeAndConnect(this, ad.scanResult, ad.carId());
        String message = "注入广播唤醒银河 #" + wakeAttempts + " · " + ad.rssi + "dBm";
        if (wakeAttempts == 1) appendLog(message);
        else appendDiagnostic(message);
    }

    private void maybeLearnVehicle(long now) {
        if (hasCompleteIdentity() || !serviceEnabled || !hasBluetoothPermission()) return;
        if ((now - lastLearnAttemptAtMs) < 10000L) return;
        lastLearnAttemptAtMs = now;

        // GATT 连接列表最接近“官方 App 真正连上的那辆车”，优先使用。
        VehicleIdentity gattIdentity = findIdentityFromConnectedGatt();
        if (gattIdentity != null) {
            identity = gattIdentity;
            identityStore.save(identity, VehicleLearningState.CONNECTED);
            waitForDisconnectToLearn = false;
            primaryCandidateKey = "";
            appendLog("✓ 已从系统 GATT 连接识别本车蓝牙: " + identityDiagnostic());
            updateNotificationIfNeeded(true);
            return;
        }
        if (waitForDisconnectToLearn && geelyConnected) {
            DiagnosticLog.detail("Learning", "WAIT_RECONNECT", "reason=" + learningMessage());
            return;
        }

        GeelyAdvertisement ad = null;
        if (lastWakeAd != null && (now - lastWakeAd.seenAtMs) <= 15000L
                && (identity == null || identity.matches(lastWakeAd))) {
            ad = lastWakeAd;
        }
        if (ad != null) {
            identity = VehicleIdentity.from(ad);
            identityStore.save(identity, VehicleLearningState.SCAN);
            primaryCandidateKey = "";
            appendLog("✓ 已自动识别本车蓝牙: " + identityDiagnostic());
            updateNotificationIfNeeded(true);
            return;
        }

        pruneCandidates(now);
        if (candidates.size() > 1) {
            appendLog("附近发现 " + candidates.size()
                    + " 辆吉利车，暂不自动绑定；请靠近本车，或手动点“重新学习本车”");
            return;
        }
        ad = strongestCandidate(now);
        if (ad != null && (identity == null || identity.matches(ad))) {
            identity = VehicleIdentity.from(ad);
            identityStore.save(identity, VehicleLearningState.SCAN);
            primaryCandidateKey = "";
            appendLog("✓ 已自动识别本车蓝牙: " + identityDiagnostic());
            updateNotificationIfNeeded(true);
            return;
        }

        VehicleIdentity observed = observedIdentityForDevice(lastObservedCarDevice, now);
        if (observed != null && lastAclConnectedAtMs > 0L && now - lastAclConnectedAtMs <= CANDIDATE_TTL_MS) {
            identity = observed;
            identityStore.save(identity, VehicleLearningState.ACL);
            primaryCandidateKey = "";
            appendLog("✓ 已从系统 BLE 连接事件自动识别本车: " + identityDiagnostic());
            updateNotificationIfNeeded(true);
            return;
        }

        appendLog("银河已连接，但暂未捕获本车广播；下次靠近会自动学习");
    }

    /** 从可见系统连接识别车辆，匿名设备缺少名称时继续查候选广播，不中断就绪处理。 */
    private VehicleIdentity findIdentityFromConnectedGatt() {
        if (!hasBluetoothPermission() || !isOfficialConnected()) return null;
        List<BluetoothDevice> devices = new ArrayList<>(GeelyBridge.getConnectedGattDevices(this));
        if (activeGatt != null && gattConnected) devices.add(activeGatt.getDevice());
        Map<String, VehicleIdentity> verified = new LinkedHashMap<>();
        for (BluetoothDevice device : devices) {
            VehicleIdentity candidate = observedIdentityForDevice(device, SystemClock.elapsedRealtime());
            if (candidate != null) verified.put(candidate.address.toUpperCase(Locale.ROOT), candidate);
        }
        String address = VehicleLearningState.uniqueAddress(verified.keySet());
        return verified.get(address);
    }

    /** 只用当前设备名称或近期广播验证，匿名地址及与手填尾号冲突的车辆不得自动绑定。 */
    private VehicleIdentity observedIdentityForDevice(BluetoothDevice device, long now) {
        if (device == null) return null;
        try {
            String address = device.getAddress();
            for (GeelyAdvertisement advertisement : candidates.values()) {
                if (advertisement != null && now - advertisement.seenAtMs <= CANDIDATE_TTL_MS
                        && address.equalsIgnoreCase(advertisement.address)
                        && (identity == null || identity.matches(advertisement))) return VehicleIdentity.from(advertisement);
            }
            String name = device.getName();
            if (!GeelyAdvertisement.isGeelyVehicleName(name)) return null;
            String tail3 = GeelyAdvertisement.vinTailFromName(name);
            String tail2 = tail3.length() >= 2 ? tail3.substring(tail3.length() - 2) : "";
            if (identity != null && ((!identity.name.isEmpty() && !identity.name.equalsIgnoreCase(name))
                    || (!identity.vinTail3.isEmpty() && !identity.vinTail3.equalsIgnoreCase(tail3))
                    || (!identity.vinTail2.isEmpty() && !identity.vinTail2.equalsIgnoreCase(tail2)))) return null;
            return new VehicleIdentity(address, name, tail2, tail3, "", System.currentTimeMillis());
        } catch (SecurityException denied) {
            return null;
        }
    }

    // ==================== RSSI 与状态机 ====================

    /** 接近补偿与离开测距分离，先更新距离再验证旧请求，避免回到车旁仍执行上锁。 */
    private void onFreshRssi(int rawDbm, String source, GeelyAdvertisement ad) {
        if (!serviceEnabled || !running) return;
        if (rawDbm <= -105 || rawDbm >= 0) {
            diagnostics.increment("rssi_invalid");
            return;
        }
        long now = SystemClock.elapsedRealtime();
        diagnostics.increment("rssi_accepted");
        if (signalMissingLogged) {
            DiagnosticLog.event("Signal", "SIGNAL_RECOVERED", "gap_ms=" + age(now, lastSampleAtMs)
                    + " rssi=" + rawDbm + " source=" + source);
            signalMissingLogged = false;
        }
        float smooth = rssiFilter.update(rawDbm, now);
        float effective = smooth + (proximityCovered ? 8.0f : 0.0f);

        lastRawRssi = rawDbm;
        lastSmoothRssi = smooth;
        lastApproachRssi = effective;
        lastSampleAtMs = now;
        lastSource = source;
        if (ad != null) currentAd = ad;

        boolean chargingBlocked = isChargingBlocked();
        boolean driving = isDriving(now) || chargingBlocked;
        PassiveKeyLogic.Action action = logic.onSample(
                effective,
                smooth,
                now,
                geelyConnected,
                driving,
                autoUnlock && !chargingBlocked && hasCompleteIdentity(),
                autoLock && !chargingBlocked && hasCompleteIdentity(),
                source.startsWith("GATT") ? "GATT" : "SCAN"
        );
        recordDecision(action, now);

        if (pendingControl.getAction() != PassiveKeyLogic.Action.NONE
                && !logic.isPendingActionValid(pendingControl.getAction(), now, driving, autoUnlock, autoLock)) {
            cancelPendingControl("信号、方向或禁控条件已变化");
        }
        if (action == PassiveKeyLogic.Action.UNLOCK) {
            handleUnlock(effective, source, ad);
        } else if (action == PassiveKeyLogic.Action.LOCK) {
            handleLock(smooth, source, ad);
        }

        String zone = zoneText(smooth, effective);
        String category = zoneCategory(smooth, effective);
        boolean categoryChanged = !category.equals(lastZoneCategory);
        lastZoneCategory = category;
        lastZone = zone;
        // 仅在信号真正跨越区间边界时记录一条日志，不在同一区间内无限刷屏
        if (categoryChanged && now - lastZoneLogAtMs >= 1000L) {
            lastZoneLogAtMs = now;
            appendDiagnostic(String.format(Locale.US,
                    "进入%s | 原始 %d dBm / 距离 %.1f dBm / 接近 %.1f dBm | %s%s",
                    category,
                    rawDbm,
                    smooth, effective,
                    geelyConnected ? "银河已连接" : "银河未连接",
                    driving ? " | 行驶中禁锁" : ""));
        }
        long sampleLogInterval = logic.getLockDwellStartMs() > 0L
                || pendingControl.getAction() != PassiveKeyLogic.Action.NONE ? 500L : DIAGNOSTIC_INTERVAL_MS;
        if ((now - lastDiagnosticAtMs) >= sampleLogInterval) {
            lastDiagnosticAtMs = now;
            DiagnosticLog.detail("Signal", "RSSI_SAMPLE", signalDiagnostic(now)
                    + " " + logic.diagnosticSnapshot(now));
        }
        processPendingControl(false);
        updateNotificationIfNeeded(false);
    }

    /** 优先使用真实远场判定，不让口袋补偿掩盖离开。 */
    private String zoneCategory(float distanceDbm, float approachDbm) {
        if (distanceDbm <= lockDbm) return "远离上锁区";
        if (approachDbm >= unlockDbm) return "靠近解锁区";
        return "车旁安全区";
    }

    /** 显示当前判定区域和连续远离确认进度。 */
    private String zoneText(float distanceDbm, float approachDbm) {
        if (isChargingBlocked()) {
            return "已插充电线 · 禁止解闭锁";
        }
        long now = SystemClock.elapsedRealtime();
        if (lastSampleAtMs == 0L || (now - lastSampleAtMs) > SIGNAL_EXPIRE_UI_MS) {
            return "不在车辆附近 (无信号)";
        }
        if (distanceDbm <= lockDbm) {
            long dwellStart = logic.getLockDwellStartMs();
            return String.format(Locale.US, "远离确认 %.1f / %.1fs (%.1f dBm)",
                    dwellStart == 0L ? 0f : (now - dwellStart) / 1000f,
                    lockDwellMs / 1000f, distanceDbm);
        }
        if (approachDbm >= unlockDbm) {
            return String.format(Locale.US, "靠近解锁区 (%.1f dBm)", approachDbm);
        }
        return String.format(Locale.US, "车旁安全区 (%.1f dBm)", distanceDbm);
    }

    /** 将解锁决策交给唯一的待发控制队列。 */
    private void handleUnlock(float effectiveDbm, String source, GeelyAdvertisement ad) {
        beginControl(PassiveKeyLogic.Action.UNLOCK, effectiveDbm, source);
    }

    /** 将上锁决策交给唯一的待发控制队列，发送前仍复核新鲜远场证据。 */
    private void handleLock(float effectiveDbm, String source, GeelyAdvertisement ad) {
        beginControl(PassiveKeyLogic.Action.LOCK, effectiveDbm, source);
    }

    /** 创建固定十二秒期限的请求；次数仅在通知按钮实际发送成功后累加。 */
    private void beginControl(PassiveKeyLogic.Action action, float effectiveDbm, String source) {
        long now = SystemClock.elapsedRealtime();
        if (!serviceEnabled || isChargingBlocked() || isDriving(now)) {
            DiagnosticLog.event("Control", "REQUEST_BLOCKED", "action=" + action + " " + signalDiagnostic(now));
            logic.onControlFailed(action, now);
            return;
        }
        pendingControl.begin(action, now);
        lastPendingWaitReason = "";
        lastPendingLogAtMs = 0L;
        String label = action == PassiveKeyLogic.Action.UNLOCK ? "解锁" : "上锁";
        lastAction = label + "等待银河钥匙就绪";
        appendLog(String.format(Locale.US,
                "★ %s请求 #%d · %.1f dBm · %s | %s", label, pendingControl.getSequence(),
                effectiveDbm, source, signalDiagnostic(now)));
        DiagnosticLog.event("Control", "REQUEST_CREATED", pendingControl.diagnosticSnapshot(now)
                + " source=" + source + " " + logic.diagnosticSnapshot(now));
        processPendingControl(true);
        updateNotificationIfNeeded(true);
    }

    /**
     * 单次执行解锁或上锁：仅点击已连接通知中的按钮。
     * 原 DK1.5 广播兜底无法确认接收结果，不能与通知点击并发重发，否则会再次造成二次上锁。
     */
    private KeyNotificationBridgeService.ControlResult dispatchControlOnce(PassiveKeyLogic.Action action) {
        return action == PassiveKeyLogic.Action.UNLOCK
                ? KeyNotificationBridgeService.triggerUnlockViaNotification()
                : KeyNotificationBridgeService.triggerLockViaNotification();
    }

    /**
     * 跨越吉利银河 V2 蓝牙 3.07 秒鉴权窗口的多阶护航重发。
     * 现统一为通知就绪立即尝试和每秒有界重试；不再同时维护通知待发队列与五个延迟任务。
     * 一旦某次成功点中通知栏按钮，立即终止，杜绝二次解闭锁。
     */
    private void processPendingControl(boolean justReady) {
        PassiveKeyLogic.Action action = pendingControl.getAction();
        if (action == PassiveKeyLogic.Action.NONE) return;
        long now = SystemClock.elapsedRealtime();
        if (pendingControl.isExpired(now)) {
            cancelPendingControl("等待银河就绪超时，未发送");
            return;
        }
        if (!running || !serviceEnabled || isChargingBlocked() || !hasCompleteIdentity()
                || !logic.isPendingActionValid(action, now, isDriving(now), autoUnlock, autoLock)) {
            cancelPendingControl("距离证据已失效或已进入禁控状态");
            return;
        }
        if (!geelyConnected) {
            recordPendingWait("WAIT_OFFICIAL", now);
            maybeWakeGeely(currentAd, now);
            return;
        }
        if (!KeyNotificationBridgeService.hasControlIntent(action)) {
            recordPendingWait("WAIT_BUTTON", now);
            return;
        }
        if (!pendingControl.claimAttempt(now, justReady)) {
            recordPendingWait("RETRY_INTERVAL_OR_LIMIT", now);
            return;
        }
        int sequence = pendingControl.getSequence();
        DiagnosticLog.event("Control", "SEND_ATTEMPT", pendingControl.diagnosticSnapshot(now)
                + " just_ready=" + justReady + " " + signalDiagnostic(now));
        KeyNotificationBridgeService.ControlResult result = dispatchControlOnce(action);
        DiagnosticLog.event("Control", "SEND_RESULT", pendingControl.diagnosticSnapshot(now)
                + " result=" + result + " " + KeyNotificationBridgeService.getLastControlDiagnostic());
        if (result == KeyNotificationBridgeService.ControlResult.NOT_SENT) {
            appendDiagnostic("通知按钮发送暂未成功 #" + sequence + " 尝试=" + pendingControl.getAttempts());
            if (pendingControl.isAttemptsExhausted()) cancelPendingControl("通知按钮两次发送失败");
            return;
        }
        if (!pendingControl.complete(sequence)) return;
        logic.onControlSent(action, now);
        String label = action == PassiveKeyLogic.Action.UNLOCK ? "解锁" : "上锁";
        if (result == KeyNotificationBridgeService.ControlResult.UNKNOWN) {
            lastAction = label + "发送结果不确定，已停止重发，请核实车锁";
            appendLog(lastAction + " #" + sequence + " | " + signalDiagnostic(now));
            updateNotificationIfNeeded(true);
            return;
        }
        if (action == PassiveKeyLogic.Action.UNLOCK) unlockCount++;
        else lockCount++;
        lastAction = label + "指令已交给银河（未获取车锁回执）";
        appendLog("✓ " + label + "发送完成 #" + sequence + " 等待="
                + pendingControl.getAgeMs(now) + "ms | " + signalDiagnostic(now));
        updateNotificationIfNeeded(true);
    }

    /** 统一撤销待发请求并反馈状态机；已经发送成功的动作不自动再发。 */
    private void cancelPendingControl(String reason) {
        PassiveKeyLogic.Action action = pendingControl.getAction();
        long now = SystemClock.elapsedRealtime();
        String request = pendingControl.diagnosticSnapshot(now);
        pendingControl.cancel();
        if (action == PassiveKeyLogic.Action.NONE) return;
        logic.onControlFailed(action, SystemClock.elapsedRealtime());
        String label = action == PassiveKeyLogic.Action.UNLOCK ? "解锁" : "上锁";
        lastAction = label + "未发送：" + reason;
        DiagnosticLog.event("Control", "REQUEST_CANCELLED", request + " reason=" + reason
                + " " + signalDiagnostic(now) + " " + logic.diagnosticSnapshot(now));
        appendLog(lastAction + " | " + signalDiagnostic(SystemClock.elapsedRealtime()));
        updateNotificationIfNeeded(true);
    }

    /** 仅采用本轮通知观测到的明确行驶状态。 */
    private boolean isDriving(long now) {
        String park = KeyNotificationBridgeService.getLastObservedParkStatus();
        long observedAt = KeyNotificationBridgeService.getLastObservedParkStatusAtMs();
        return "行驶中".equals(park)
                && observedAt > 0L
                && (now - observedAt) < 90000L;
    }

    // ==================== 官方连接状态 ====================

    /** 官方通知变化立即处理，GATT 挂载绝不代替官方鉴权就绪。 */
    private void handleOfficialNotificationUpdate() {
        if (!running || !serviceEnabled) return;
        boolean connected = isOfficialConnected();
        long now = SystemClock.elapsedRealtime();
        if (connected != geelyConnected) onGeelyConnectionChanged(connected, now);
        processPendingControl(true);
    }

    /** 仅官方连接状态边沿触发即时解锁，不被共享 GATT 的连接事件吞掉。 */
    private void onGeelyConnectionChanged(boolean connected, long now) {
        DiagnosticLog.event("Official", connected ? "CONNECTED" : "DISCONNECTED",
                "since_first_ad_ms=" + age(now, firstAdvertInSessionAtMs)
                        + " since_first_wake_ms=" + age(now, firstWakeInSessionAtMs)
                        + " " + signalDiagnostic(now) + " " + KeyNotificationBridgeService.diagnosticSnapshot(now));
        geelyConnected = connected;
        if (connected) {
            lastGeelyConnectedAtMs = now;
            lastGattAttemptAtMs = 0L;
            wakeAttempts = 0;
            appendLog("✓ 银河蓝牙钥匙已连接");
            maybeLearnVehicle(now);
            boolean blocked = isDriving(now) || isChargingBlocked();
            PassiveKeyLogic.Action action = logic.onOfficialConnected(now, blocked,
                    autoUnlock && !blocked && hasCompleteIdentity());
            DiagnosticLog.event("Decision", "OFFICIAL_READY_RESULT", "action=" + action
                    + " " + logic.diagnosticSnapshot(now));
            if (action == PassiveKeyLogic.Action.UNLOCK) {
                handleUnlock(lastRawRssi > -127 ? lastRawRssi : -60.0f, "银河蓝牙连接即刻解锁", currentAd);
            }
        } else {
            appendLog("银河蓝牙钥匙已断开");
            waitForDisconnectToLearn = false;
            lastAclConnectedAtMs = 0L;
            closeGatt("银河蓝牙已断开");
            logic.onOfficialDisconnected(now);
        }
        ensureGattAttached(now);
        updateNotificationIfNeeded(true);
    }

    /** 官方通知与系统蓝牙必须同时可用，蓝牙关闭期间旧通知不构成就绪。 */
    private boolean isOfficialConnected() {
        try {
            return bluetoothAdapter != null && bluetoothAdapter.isEnabled()
                    && KeyNotificationBridgeService.isGeelyBleConnected()
                    && KeyNotificationBridgeService.isGeelyNotificationPresent();
        } catch (SecurityException denied) {
            return false;
        }
    }

    // ==================== GATT 连接态 RSSI ====================

    private final Runnable gattConnectTimeoutRunnable = new Runnable() {
        /** 只终止本应用的超时挂载，保留官方连接状态供下一轮恢复。 */
        @Override
        public void run() {
            if (activeGatt != null && !gattConnected) {
                DiagnosticLog.event("Gatt", "CONNECT_TIMEOUT", gattDiagnostic(SystemClock.elapsedRealtime()));
                closeGatt("3.5s 未连上已建立的 ACL，释放连接尝试");
            }
        }
    };

    /** 官方就绪后共享已有连接，每条通道仅保留一个 RSSI 读取循环。 */
    private void ensureGattAttached(long now) {
        if (!serviceEnabled) return;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) return;
        if (activeGatt != null) {
            long lastProgress = Math.max(lastGattRssiAtMs, lastGattAttemptAtMs);
            if (gattConnected && (now - lastProgress) > SIGNAL_EXPIRE_UI_MS) {
                appendLog("连接态 RSSI 已停更，重建测距通道 | " + signalDiagnostic(now));
                logic.onSignalLost(now);
                cancelPendingControl("连接态信号停更");
                closeGatt("测距回调超时");
            }
            return;
        }
        if (lastGattAttemptAtMs > 0L && (now - lastGattAttemptAtMs) < GATT_RETRY_MS) return;

        // 严格限制挂载连接态 RSSI 的条件（防止在车外盲目 connectGatt 抢占车机唯一广播槽位）：
        // 旧版允许仅凭 ACL_CONNECTED 挂载，现在等待银河通知明确就绪，避免干扰 V2 鉴权。
        if (!geelyConnected) {
            return;
        }

        String address = resolveGattAddress();
        BluetoothDevice device = null;
        if (lastObservedCarDevice != null
                && (address.isEmpty() || address.equalsIgnoreCase(lastObservedCarDevice.getAddress()))) {
            device = lastObservedCarDevice;
            address = device.getAddress();
        }
        if (device == null) {
            if (address.isEmpty() || !BluetoothAdapter.checkBluetoothAddress(address)) return;
            device = bluetoothAdapter.getRemoteDevice(address);
        }
        try {
            lastGattAttemptAtMs = now;
            gattAttemptId++;
            lastSelfGattAddress = address;
            DiagnosticLog.event("Gatt", "CONNECT_START", "attempt_id=" + gattAttemptId
                    + " addr_id=" + DiagnosticLog.fingerprint(address) + " " + gattDiagnostic(now));
            activeGatt = device.connectGatt(this, false, gattCallback, BluetoothDevice.TRANSPORT_LE,
                    BluetoothDevice.PHY_LE_1M_MASK, handler);
            if (activeGatt == null) {
                appendLog("系统未创建 GATT 测距通道，稍后重试");
                return;
            }
            handler.removeCallbacks(gattConnectTimeoutRunnable);
            handler.postDelayed(gattConnectTimeoutRunnable, GATT_CONNECT_TIMEOUT_MS);
            appendLog("挂载连接态 RSSI 读取通道: " + address);
        } catch (Throwable t) {
            DiagnosticLog.bridgeError("Gatt", "CONNECT_EXCEPTION", t);
            appendLog("挂载 GATT RSSI 通道失败: " + t.getMessage());
        }
    }

    /** 只选择符合本车身份的连接设备，不把其他车辆的系统连接覆盖成本车地址。 */
    private String resolveGattAddress() {
        // 车机蓝牙地址可能轮换。先用系统 GATT 连接列表按名称/候选地址校正，
        // 再回退到已保存地址；否则地址变化后会一直挂不上 RSSI 通道。
        for (BluetoothDevice device : GeelyBridge.getConnectedGattDevices(this)) {
            if (device == null) continue;
            String address = device.getAddress();
            String name = "";
            try {
                String observedName = device.getName();
                name = observedName == null ? "" : observedName;
            } catch (Throwable ignored) {
            }
            if (!matchesKnownVehicle(device, SystemClock.elapsedRealtime())) continue;
            if (!name.isEmpty() && GeelyAdvertisement.isGeelyVehicleName(name)) {
                if (identity == null) {
                    String t3 = GeelyAdvertisement.vinTailFromName(name);
                    String t2 = t3.length() >= 2 ? t3.substring(t3.length() - 2) : "";
                    identity = new VehicleIdentity(address, name, t2, t3, "", System.currentTimeMillis());
                    identityStore.save(identity, VehicleLearningState.CONNECTED);
                } else if (identity.name.isEmpty() || name.equalsIgnoreCase(identity.name)) {
                    identity = identity.withAddress(address);
                    identityStore.save(identity, VehicleLearningState.CONNECTED);
                }
                lastObservedCarDevice = device;
                lastObservedBleMac = address;
                return address;
            }
            for (GeelyAdvertisement ad : candidates.values()) {
                if (ad != null && ad.address.equalsIgnoreCase(address)
                        && (identity == null || identity.matches(ad))) {
                    if (identity != null) {
                        identity = identity.withAddress(address);
                        identityStore.save(identity, VehicleLearningState.CONNECTED);
                    }
                    lastObservedCarDevice = device;
                    lastObservedBleMac = address;
                    return address;
                }
            }
        }
        if (!lastObservedBleMac.isEmpty()) return lastObservedBleMac;
        if (identity == null) return "";
        if (!identity.address.isEmpty()) return identity.address;
        return "";
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        /** 只接收当前 GATT 实例的状态，迟到的旧通道断连不能关闭新通道。 */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (gatt != activeGatt || !serviceEnabled || !running) {
                diagnostics.increment("gatt_old_state_callback");
                return;
            }
            lastGattStatus = status;
            DiagnosticLog.event("Gatt", "STATE_CALLBACK", "new_state=" + newState + " status=" + status
                    + " " + gattDiagnostic(SystemClock.elapsedRealtime()));
            if (newState == BluetoothProfile.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS) {
                handler.removeCallbacks(gattConnectTimeoutRunnable);
                gattConnected = true;
                lastGattRssiAtMs = 0L;
                gattReadPending = false;
                appendLog("✓ 已挂载车机 ACL，开始读取连接态 RSSI");
                handler.removeCallbacks(gattRssiPollRunnable);
                handler.post(gattRssiPollRunnable);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED || status != BluetoothGatt.GATT_SUCCESS) {
                handler.removeCallbacks(gattConnectTimeoutRunnable);
                gattConnected = false;
                handler.removeCallbacks(gattRssiPollRunnable);
                // 官方钥匙仍在线时，GATT 断开更像链路闪断，短暂禁止误锁；
                // 官方钥匙已断开则很可能是人已走远，让广播重新接管，避免无谓延迟上锁。
                if (KeyNotificationBridgeService.isGeelyBleConnected()) {
                    logic.onSignalLost(SystemClock.elapsedRealtime());
                }
                if (pendingControl.getAction() == PassiveKeyLogic.Action.LOCK) {
                    cancelPendingControl("GATT 测距连接断开");
                }
                closeGatt("GATT 连接断开，status=" + status);
            }
            updateNotificationIfNeeded(true);
        }

        /** 读取成功并且仍属于当前通道时才提交新鲜样本。 */
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            diagnostics.increment("gatt_read_callbacks");
            if (gatt != activeGatt || !gattConnected || !serviceEnabled || !running || !gattReadPending) {
                diagnostics.increment("gatt_read_ignored");
                return;
            }
            gattReadPending = false;
            lastGattReadStatus = status;
            long now = SystemClock.elapsedRealtime();
            if ((now - lastGattReadRequestedAtMs) > PassiveKeyLogic.SIGNAL_STALE_MS) {
                diagnostics.increment("gatt_read_stale");
                return;
            }
            if (status == BluetoothGatt.GATT_SUCCESS && rssi > -105 && rssi < 0) {
                diagnostics.increment("gatt_read_success");
                lastGattRssiAtMs = now;
                onFreshRssi(rssi, "GATT连接态", currentAd);
            } else {
                diagnostics.increment("gatt_read_error");
                appendDiagnostic("RSSI 读取失败，status=" + status + "，保持样本过期检查");
            }
        }
    };

    private final Runnable gattRssiPollRunnable = new Runnable() {
        /** 每五百毫秒至多一个在途读取，防止重复轮询制造虚假的连续样本。 */
        @Override
        public void run() {
            if (!serviceEnabled || activeGatt == null || !gattConnected) return;
            long now = SystemClock.elapsedRealtime();
            if (!gattReadPending) {
                try {
                    diagnostics.increment("gatt_read_requests");
                    lastGattReadRequestedAtMs = now;
                    gattReadPending = activeGatt.readRemoteRssi();
                    if (!gattReadPending) diagnostics.increment("gatt_read_request_rejected");
                } catch (Throwable error) {
                    diagnostics.increment("gatt_read_request_exception");
                    DiagnosticLog.bridgeError("Gatt", "READ_REQUEST_EXCEPTION", error);
                    appendDiagnostic("请求 RSSI 失败: " + error.getClass().getSimpleName());
                }
            }
            handler.postDelayed(this, GATT_POLL_MS);
        }
    };

    /** 先清除当前引用再关闭通道，拒绝其后到达的旧回调。 */
    private void closeGatt(String reason) {
        if (activeGatt != null) DiagnosticLog.event("Gatt", "CLOSE", "reason=" + reason
                + " " + gattDiagnostic(SystemClock.elapsedRealtime()));
        handler.removeCallbacks(gattConnectTimeoutRunnable);
        handler.removeCallbacks(gattRssiPollRunnable);
        gattConnected = false;
        gattReadPending = false;
        lastGattRssiAtMs = 0L;
        lastSelfGattCloseAtMs = SystemClock.elapsedRealtime();
        BluetoothGatt closingGatt = activeGatt;
        activeGatt = null;
        if (closingGatt != null) {
            try {
                closingGatt.disconnect();
            } catch (Throwable ignored) {
            }
            try {
                closingGatt.close();
            } catch (Throwable ignored) {
            }
        }
        if (closingGatt != null && !reason.isEmpty()) {
            appendLog("关闭 GATT: " + reason);
        }
    }

    // ==================== 息屏保活 ====================

    /** 首次收到本车广播即延长息屏保活，覆盖后续官方鉴权和远离确认。 */
    private void refreshWakeLock(long now) {
        if (wakeLock == null) return;
        boolean need = serviceEnabled && !screenOn && (
                geelyConnected
                        || gattConnected
                        || pendingControl.getAction() != PassiveKeyLogic.Action.NONE
                        || (lastTargetAdvertAtMs > 0L && (now - lastTargetAdvertAtMs) < WAKE_LOCK_NEAR_MS)
                        || (lastScreenOffAtMs > 0L && (now - lastScreenOffAtMs) < WAKE_LOCK_BOOTSTRAP_MS)
        );
        try {
            if (need) {
                if (!wakeLock.isHeld()) {
                    wakeLock.acquire(WAKE_LOCK_TIMEOUT_MS);
                    appendDiagnostic("息屏保活：已持有 PARTIAL_WAKE_LOCK");
                }
            } else if (wakeLock.isHeld()) {
                wakeLock.release();
                appendDiagnostic("息屏保活：已释放 PARTIAL_WAKE_LOCK");
            }
        } catch (Throwable ignored) {
        }
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            try {
                if (wakeLock.isHeld()) wakeLock.release();
            } catch (Throwable ignored) {
            }
        }
    }

    /** 扫描维护记录在独立诊断文件，不占据关键动作的日志容量。 */
    private void runScreenOffScanWatchdog(long now) {
        if (screenOn || !isScanning || geelyConnected) return;
        long lastSignalAt = Math.max(lastTargetAdvertAtMs, lastScanStartAtMs);
        if ((now - lastSignalAt) < SCAN_RESTART_AFTER_MS) return;
        if ((now - lastScanRestartAtMs) < SCAN_RESTART_THROTTLE_MS) return;
        lastScanRestartAtMs = now;
        appendDiagnostic("息屏扫描看门狗：45s 无车机广播，重建 BLE 扫描器");
        stopBleScan();
        startBleScan();
    }

    // ==================== 系统广播 / 传感器 / 定时 ====================

    private void initProximitySensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) return;
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /** 遮挡状态连续稳定八百毫秒才切换，只补偿接近信号。 */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.sensor == null) return;
        if (event.sensor.getType() != Sensor.TYPE_PROXIMITY) return;
        float maxRange = proximitySensor != null ? proximitySensor.getMaximumRange() : 5.0f;
        if (event.values == null || event.values.length == 0) return;
        boolean covered = event.values[0] < Math.min(3.0f, maxRange);
        if (covered != pendingProximityCovered) {
            pendingProximityCovered = covered;
            handler.removeCallbacks(applyProximityRunnable);
            handler.postDelayed(applyProximityRunnable, 800L);
        }
    }

    private final Runnable applyProximityRunnable = new Runnable() {
        /** 仅记录稳定后的遮挡变化，且不直接产生车控动作。 */
        @Override
        public void run() {
            if (proximityCovered == pendingProximityCovered) return;
            proximityCovered = pendingProximityCovered;
            appendDiagnostic(proximityCovered
                    ? "手机稳定遮挡，仅接近信号 +8dBm；远离信号不补偿" : "手机遮挡解除");
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void registerSystemReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            filter.addAction(ACTION_GATT_CONN_CHANGE);
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            filter.addAction(Intent.ACTION_POWER_CONNECTED);
            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            if (Build.VERSION.SDK_INT >= 33) {
                registerReceiver(systemReceiver, filter, Context.RECEIVER_EXPORTED);
            } else {
                registerReceiver(systemReceiver, filter);
            }
            receiverRegistered = true;
        } catch (Throwable ignored) {
        }
    }

    private final BroadcastReceiver systemReceiver = new BroadcastReceiver() {
        /** 使用单调时钟处理电源与蓝牙事件，端口变化立即撤销旧车控请求。 */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            String action = intent.getAction();
            long now = SystemClock.elapsedRealtime();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    appendLog("系统蓝牙已开启，重建扫描");
                    cancelPendingControl("系统蓝牙重新开启");
                    // 蓝牙重新开启后允许重新判定一次靠近；冷启动远处仍不会补发上锁。
                    logic.reset();
                    startBleScan();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    appendLog("系统蓝牙已关闭，暂停扫描并禁止误锁");
                    cancelPendingControl("系统蓝牙已关闭");
                    lastAclConnectedAtMs = 0L;
                    geelyConnected = false;
                    stopBleScan();
                    closeGatt("蓝牙关闭");
                    logic.onBluetoothReset(now);
                }
            } else if (ACTION_GATT_CONN_CHANGE.equals(action)) {
                handleGattProfileBroadcast(intent, now);
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleAclBroadcast(device, true, now);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                handleAclBroadcast(device, false, now);
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                refreshWiredChargingState(intent);
            } else if (Intent.ACTION_POWER_CONNECTED.equals(action)
                    || Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
                refreshWiredChargingState(null);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                DiagnosticLog.event("Power", "SCREEN_OFF", "run=" + serviceRun + " " + signalDiagnostic(now));
                screenOn = false;
                lastScreenOffAtMs = now;
                refreshWakeLock(now);
                if (!isScanning && bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    startBleScan();
                }
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                DiagnosticLog.event("Power", "SCREEN_ON", "run=" + serviceRun + " " + signalDiagnostic(now));
                screenOn = true;
                releaseWakeLock();
                if (!isScanning && bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    startBleScan();
                }
            }
            updateNotificationIfNeeded(true);
        }
    };

    /**
     * 处理系统级 BLE GATT 连接状态广播 (android.bluetooth.gatt.profile.action.CONNECTION_STATE_CHANGED)。
     *
     * 当银河 App 的 :dkservice 连接或断开车机 BLE 时，系统会广播：
     * device_type="ble", bd_addr="<车机当前MAC>", status=<状态>。
     * 地址轮换后仍须用已学习名称或新鲜广播验证身份；未知设备不能仅凭银河在线就冒充本车。
     */
    private void handleGattProfileBroadcast(Intent intent, long now) {
        if (!serviceEnabled) return;
        try {
            String devType = intent.getStringExtra("device_type");
            if (devType != null && !"ble".equalsIgnoreCase(devType)) return;
            String bdAddr = intent.getStringExtra("bd_addr");
            if (bdAddr == null || !BluetoothAdapter.checkBluetoothAddress(bdAddr)) return;
            // 忽略我们自己刚刚主动关闭/超时释放的 GATT 触发的回声广播，彻底杜绝自触发死循环！
            if (bdAddr.equalsIgnoreCase(lastSelfGattAddress) && (now - lastSelfGattCloseAtMs) < 5000L) {
                return;
            }
            // 只在银河通知栏处于已连接状态时，才采信该 BLE GATT 地址为本车新地址
            if (!KeyNotificationBridgeService.isGeelyBleConnected()) {
                return;
            }
            int state = intent.hasExtra(BluetoothProfile.EXTRA_STATE)
                    ? intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    : intent.getIntExtra("status", -1);
            if (state != BluetoothProfile.STATE_CONNECTED) return;
            BluetoothDevice dev = null;
            try {
                if (bluetoothAdapter != null) {
                    dev = bluetoothAdapter.getRemoteDevice(bdAddr);
                }
            } catch (Throwable ignored) {
            }
            String devName = "";
            try {
                if (dev != null && dev.getName() != null) {
                    devName = dev.getName();
                }
            } catch (Throwable ignored) {
            }
            if (!matchesKnownVehicle(dev, now)) return;
            lastObservedBleMac = bdAddr;
            if (dev != null) lastObservedCarDevice = dev;
            lastAclConnectedAtMs = now;

            if (!hasCompleteIdentity()) {
                String t3 = GeelyAdvertisement.vinTailFromName(devName);
                String t2 = t3.length() >= 2 ? t3.substring(t3.length() - 2) : "";
                identity = new VehicleIdentity(bdAddr, devName, t2, t3, "", System.currentTimeMillis());
                identityStore.save(identity, VehicleLearningState.GATT_EVENT);
                waitForDisconnectToLearn = false;
                appendLog("✓ 捕获系统 BLE GATT 车机地址: " + identityDiagnostic());
            } else if (identity != null && !bdAddr.equalsIgnoreCase(identity.address)) {
                if (devName.isEmpty() || identity.name.isEmpty() || devName.equalsIgnoreCase(identity.name)) {
                    identity = identity.withAddress(bdAddr);
                    identityStore.save(identity, VehicleLearningState.GATT_EVENT);
                    appendLog("✓ 捕获车机 BLE 新地址: " + identityDiagnostic());
                }
            }
            ensureGattAttached(now);
        } catch (Throwable ignored) {
        }
    }

    /**
     * 处理系统 ACL 连接/断开广播。
     */
    private void handleAclBroadcast(BluetoothDevice device, boolean connected, long now) {
        if (!serviceEnabled) return;
        if (device == null) return;
        String addr = device.getAddress();
        if (addr == null || addr.isEmpty()) return;
        String name = "";
        try {
            if (device.getName() != null) name = device.getName();
        } catch (Throwable ignored) {
        }
        if (!matchesKnownVehicle(device, now)) {
            diagnostics.increment("acl_other_vehicle");
            return;
        }
        if (!connected) {
            String currentAddress = activeGatt != null
                    ? activeGatt.getDevice().getAddress() : lastObservedBleMac;
            if (!currentAddress.isEmpty() && !addr.equalsIgnoreCase(currentAddress)) {
                diagnostics.increment("acl_old_address_disconnect");
                return;
            }
        }
        DiagnosticLog.event("Gatt", connected ? "ACL_CONNECTED" : "ACL_DISCONNECTED",
                "addr_id=" + DiagnosticLog.fingerprint(addr) + " " + gattDiagnostic(now));
        lastObservedCarDevice = device;
        lastObservedBleMac = addr;
        if (connected) {
            lastAclConnectedAtMs = now;
            if (!hasCompleteIdentity()) {
                String t3 = GeelyAdvertisement.vinTailFromName(name);
                String t2 = t3.length() >= 2 ? t3.substring(t3.length() - 2) : "";
                identity = new VehicleIdentity(addr, name, t2, t3, "", System.currentTimeMillis());
                identityStore.save(identity, VehicleLearningState.ACL);
                waitForDisconnectToLearn = false;
                appendLog("✓ 从 ACL 连接自动锁定本车: " + identityDiagnostic());
            } else if (identity != null && !addr.equalsIgnoreCase(identity.address)) {
                identity = identity.withAddress(addr);
                identityStore.save(identity, VehicleLearningState.ACL);
            }
            appendLog("✓ 车机底层 ACL 已连接: addr_id=" + DiagnosticLog.fingerprint(addr)
                    + " name_id=" + DiagnosticLog.fingerprint(name));
            ensureGattAttached(now);
        } else {
            appendLog("车机底层 ACL 已断开: addr_id=" + DiagnosticLog.fingerprint(addr)
                    + " name_id=" + DiagnosticLog.fingerprint(name));
            logic.onSignalLost(now);
            lastAclConnectedAtMs = 0L;
            cancelPendingControl("本车底层蓝牙已断开");
            closeGatt("底层 ACL 断开");
            lastRawRssi = -127;
            lastSampleAtMs = 0L;
            lastZone = "不在车辆附近 (无信号)";
        }
    }

    /** 校验系统连接设备确属本车，拒绝其他吉利车辆或没有身份依据的匿名地址。 */
    private boolean matchesKnownVehicle(BluetoothDevice device, long now) {
        if (device == null) return false;
        String address;
        String name;
        try {
            address = device.getAddress();
            String observedName = device.getName();
            name = observedName == null ? "" : observedName;
        } catch (SecurityException denied) {
            return false;
        }
        if (address == null || address.isEmpty()) return false;
        VehicleIdentity known = identity;
        if (known != null) {
            if (!known.name.isEmpty() && GeelyAdvertisement.isGeelyVehicleName(name)
                    && !known.name.equalsIgnoreCase(name)) return false;
            if (!known.address.isEmpty() && known.address.equalsIgnoreCase(address)) return true;
            if (!known.name.isEmpty() && known.name.equalsIgnoreCase(name)) return true;
            String tail = GeelyAdvertisement.vinTailFromName(name);
            if (!known.vinTail3.isEmpty() && known.vinTail3.equalsIgnoreCase(tail)) return true;
        } else if (GeelyAdvertisement.isGeelyVehicleName(name)) {
            return true;
        }
        for (GeelyAdvertisement advertisement : candidates.values()) {
            if (advertisement != null && now - advertisement.seenAtMs <= CANDIDATE_TTL_MS
                    && address.equalsIgnoreCase(advertisement.address)
                    && (known != null ? known.matches(advertisement) : candidates.size() == 1)) return true;
        }
        return false;
    }

    private final Runnable tickRunnable = new Runnable() {
        /** 维护官方状态、请求期限和测距通道；未收到样本绝不当作已经远离。 */
        @Override
        public void run() {
            long now = SystemClock.elapsedRealtime();
            recordHeartbeat(now);
            if (!serviceEnabled) {
                lastRawRssi = -127;
                lastZone = "无感进出服务已关闭";
                updateNotificationIfNeeded(false);
                handler.postDelayed(this, 1000L);
                return;
            }
            // 超过 6 秒无新鲜 RSSI 样本时，立即清除陈旧信号值，避免离开车辆后仍显示 -93 dBm
            if (lastSampleAtMs > 0L && (now - lastSampleAtMs) > SIGNAL_EXPIRE_UI_MS) {
                if (!signalMissingLogged) {
                    DiagnosticLog.event("Signal", "SIGNAL_EXPIRED", signalDiagnostic(now)
                            + " " + scanDiagnostic(now, false) + " " + gattDiagnostic(now));
                    signalMissingLogged = true;
                }
                lastRawRssi = -127;
                lastZone = "不在车辆附近 (无信号)";
                lastZoneCategory = "";
                lastSource = "";
            }
            if ((now - lastListenerRebindAtMs) >= 2000L) {
                lastListenerRebindAtMs = now;
                KeyNotificationBridgeService.recoverIfNeeded(PassiveKeyService.this);
            }
            if (serviceEnabled && !isScanning && hasBluetoothPermission()
                    && bluetoothAdapter != null && bluetoothAdapter.isEnabled()
                    && (now - lastScanRecoveryAtMs) >= 2000L) {
                lastScanRecoveryAtMs = now;
                startBleScan();
            }
            boolean notifConnected = isOfficialConnected();
            if (notifConnected != geelyConnected) {
                onGeelyConnectionChanged(notifConnected, now);
            }
            processPendingControl(false);
            if (logic.onTick(now, notifConnected, isDriving(now) || isChargingBlocked())) {
                appendLog("🔄 已离开车辆且蓝牙断开满 12s，已重新武装下一次靠近自动解锁");
            }
            ensureGattAttached(now);
            if (notifConnected && !hasCompleteIdentity()) {
                maybeLearnVehicle(now);
            }
            if (notifConnected && (lastSampleAtMs == 0L || (now - lastSampleAtMs) > SIGNAL_EXPIRE_UI_MS)
                    && (now - lastGeelyConnectedAtMs) > SIGNAL_EXPIRE_UI_MS
                    && (now - lastRssiWarningAtMs) >= 30000L) {
                lastRssiWarningAtMs = now;
                appendLog("银河钥匙已连接，但测距暂无新样本，无法判定远离 | " + signalDiagnostic(now));
            }
            refreshWakeLock(now);
            runScreenOffScanWatchdog(now);
            updateNotificationIfNeeded(false);
            handler.postDelayed(this, 500L);
        }
    };

    // ==================== 前台通知 / 日志 ====================

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "无感钥匙常驻服务",
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription("息屏靠近解锁 / 离开上锁");
        channel.setShowBadge(false);
        manager.createNotificationChannel(channel);
    }

    private Notification buildNotification(String text) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        return builder
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("银河无感钥匙")
                .setContentText(text == null ? "" : text)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
    }

    /** 按单调时间节流前台通知，避免系统时间调整打乱刷新。 */
    private void updateNotificationIfNeeded(boolean force) {
        long now = SystemClock.elapsedRealtime();
        if (!force && (now - lastNotificationUpdateAtMs) < 2000L) return;
        lastNotificationUpdateAtMs = now;
        String text;
        if (!serviceEnabled) {
            text = "无感进出服务已手动关闭";
        } else if (!KeyNotificationBridgeService.isNotificationListenerEnabled(this)) {
            text = "通知使用权未开启 · 打开App授权";
        } else if (!KeyNotificationBridgeService.isListenerConnected()) {
            text = "通知监听被系统解绑 · 点此打开App修复";
        } else if (!geelyConnected) {
            text = identity == null ? "正在学习本车蓝牙身份" : "等待银河蓝牙连接 · " + lastZone;
        } else {
            text = "银河已连接 · " + lastZone;
        }
        try {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(NOTIFICATION_ID, buildNotification(text));
            }
        } catch (Throwable ignored) {
        }
    }

    /** 关键事件由共用日志仓库脱敏并进入十条界面列表及固定容量文件。 */
    private void appendLog(String message) {
        DiagnosticLog.event("Service", "STATE", "run=" + serviceRun + " " + message);
    }

    /** 高频采样和扫描维护写入独立诊断轮转文件，不挤掉关键事件。 */
    private void appendDiagnostic(String message) {
        DiagnosticLog.detail("Service", "DETAIL", "run=" + serviceRun + " " + message);
    }

    /** 仅记录实际状态机返回的原因，变化时节流，长时间相同也定期保留证据。 */
    private void recordDecision(PassiveKeyLogic.Action action, long now) {
        String reason = logic.getLastSampleDecision();
        if (action != PassiveKeyLogic.Action.NONE || (!reason.equals(lastDecisionReason)
                && now - lastDecisionLogAtMs >= 1000L) || now - lastDecisionHeartbeatAtMs >= 30000L) {
            lastDecisionReason = reason;
            lastDecisionLogAtMs = now;
            lastDecisionHeartbeatAtMs = now;
            DiagnosticLog.detail("Decision", "SAMPLE_RESULT", "action=" + action
                    + " " + signalDiagnostic(now) + " " + logic.diagnosticSnapshot(now));
        }
    }

    /** 请求等待时记录具体阻塞阶段，每个原因首次出现和五秒心跳才落盘。 */
    private void recordPendingWait(String reason, long now) {
        if (reason.equals(lastPendingWaitReason) && now - lastPendingLogAtMs < 5000L) return;
        lastPendingWaitReason = reason;
        lastPendingLogAtMs = now;
        DiagnosticLog.detail("Control", "REQUEST_WAIT", "reason=" + reason + " "
                + pendingControl.diagnosticSnapshot(now) + " " + KeyNotificationBridgeService.diagnosticSnapshot(now));
    }

    /** 无信号时也保留低频心跳，Handler长时间未获调度会留下明确间隔。 */
    private void recordHeartbeat(long now) {
        if (lastTickAtMs > 0L && now - lastTickAtMs > 5000L) {
            DiagnosticLog.event("Lifecycle", "TICK_GAP", "gap_ms=" + (now - lastTickAtMs)
                    + " note=may_be_sleep_or_schedule_delay " + environmentDiagnostic());
        }
        lastTickAtMs = now;
        if (lastHeartbeatAtMs > 0L && now - lastHeartbeatAtMs < HEARTBEAT_INTERVAL_MS) return;
        lastHeartbeatAtMs = now;
        DiagnosticLog.detail("Health", "HEARTBEAT", environmentDiagnostic() + " " + signalDiagnostic(now)
                + " " + scanDiagnostic(now, true) + " " + gattDiagnostic(now)
                + " " + KeyNotificationBridgeService.diagnosticSnapshot(now)
                + " " + logic.diagnosticSnapshot(now) + " " + DiagnosticLog.health());
    }

    /** 观测权限、屏幕、省电与内存状态；读取失败只写未知，不改变开关或权限。 */
    private String environmentDiagnostic() {
        try {
            Runtime runtime = Runtime.getRuntime();
            return "run=" + serviceRun + " enabled=" + serviceEnabled + " auto_unlock=" + autoUnlock
                    + " auto_lock=" + autoLock + " wired=" + wiredCharging + " block_charging=" + blockWhenCharging
                    + " bluetooth_permission=" + hasBluetoothPermission()
                    + " bluetooth_on=" + (bluetoothAdapter != null && bluetoothAdapter.isEnabled())
                    + " notification_access=" + KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                    + " screen_on=" + screenOn + " wake_lock=" + (wakeLock != null && wakeLock.isHeld())
                    + " idle=" + (powerManager != null && powerManager.isDeviceIdleMode())
                    + " power_save=" + (powerManager != null && powerManager.isPowerSaveMode())
                    + " battery_exempt=" + (powerManager != null && powerManager.isIgnoringBatteryOptimizations(getPackageName()))
                    + " heap_used_kb=" + (runtime.totalMemory() - runtime.freeMemory()) / 1024L
                    + " identity_id=" + DiagnosticLog.fingerprint(identity == null ? "" : identity.stableKey())
                    + " learning_complete=" + hasCompleteIdentity()
                    + " learning_source=" + identityStore.getLearningSource()
                    + " learning_waiting_reconnect=" + waitForDisconnectToLearn;
        } catch (RuntimeException unavailable) {
            return "environment_error=" + unavailable.getClass().getSimpleName();
        }
    }

    /** 扫描漏斗计数既包含总量也包含窗口增量，零回调与有回调被过滤可以区分。 */
    private String scanDiagnostic(long now, boolean resetWindow) {
        return "scanning=" + isScanning + " scan_uptime_ms=" + age(now, lastScanStartAtMs)
                + " callback_age_ms=" + age(now, lastScanCallbackAtMs)
                + " accepted_ad_age_ms=" + age(now, lastAcceptedAdvertAtMs)
                + " candidates=" + candidates.size() + " primary_id=" + DiagnosticLog.fingerprint(primaryCandidateKey)
                + " " + diagnostics.snapshot(resetWindow);
    }

    /** 每次挂载和读取都能关联到同一attempt，拒绝与停更原因不再只存在Logcat中。 */
    private String gattDiagnostic(long now) {
        return "gatt_attempt=" + gattAttemptId + " gatt_active=" + (activeGatt != null)
                + " gatt_connected=" + gattConnected + " gatt_addr_id=" + DiagnosticLog.fingerprint(lastSelfGattAddress)
                + " gatt_attempt_age_ms=" + age(now, lastGattAttemptAtMs) + " gatt_status=" + lastGattStatus
                + " read_pending=" + gattReadPending + " read_request_age_ms=" + age(now, lastGattReadRequestedAtMs)
                + " read_success_age_ms=" + age(now, lastGattRssiAtMs) + " read_status=" + lastGattReadStatus;
    }

    /** 日志从源头只写身份指纹，任意自定义车名都不会被拼入文件。 */
    private String identityDiagnostic() {
        VehicleIdentity known = identity;
        if (known == null) return "identity=none";
        return "addr_id=" + DiagnosticLog.fingerprint(known.address)
                + " name_id=" + DiagnosticLog.fingerprint(known.name)
                + " identity_id=" + DiagnosticLog.fingerprint(known.stableKey())
                + " learning_source=" + identityStore.getLearningSource();
    }

    /** 未出现的事件用负一表示，零表示刚刚发生。 */
    private static long age(long now, long observedAt) {
        return observedAt <= 0L ? -1L : Math.max(0L, now - observedAt);
    }

    /** 记录动作判定所需的最小上下文，不包含 VIN、钥匙凭证或通知原文。 */
    private String signalDiagnostic(long now) {
        long dwellStart = logic.getLockDwellStartMs();
        return String.format(Locale.US,
                "raw=%d smooth=%.1f approach=%.1f pocket=%s age=%dms source=%s official=%s gatt=%s "
                        + "dwell=%dms samples=%d near=%s latched=%s wiredBlock=%s driving=%s pending=%s",
                lastRawRssi, lastSmoothRssi, lastApproachRssi, proximityCovered,
                lastSampleAtMs == 0L ? -1L : now - lastSampleAtMs, lastSource, geelyConnected, gattConnected,
                dwellStart == 0L ? 0L : now - dwellStart, logic.getLockDwellSamples(),
                logic.hasBeenNear(), logic.isApproachLatched(), isChargingBlocked(), isDriving(now),
                pendingControl.getAction());
    }
}
