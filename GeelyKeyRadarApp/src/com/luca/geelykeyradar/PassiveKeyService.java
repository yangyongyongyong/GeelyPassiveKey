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
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothStatusCodes;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
    private static final long CANDIDATE_TTL_MS = VehicleTargetSelector.CANDIDATE_TTL_MS;
    private static final long GATT_RETRY_MS = 4500L;
    private static final long GATT_CONNECT_TIMEOUT_MS = 3500L;
    private static final long GATT_POLL_MS = 500L;
    private static final long WAKE_FALLBACK_MS = 1200L;
    private static final long AUTH_OBSERVE_TIMEOUT_MS = 7000L;
    private static final String AUTH_SERVICE_UUID = "02362aff-cf3a-11e1-efde-0002a5d5c51b";
    private static final String AUTH_NOTIFY_UUID = "02362a13-cf3a-11e1-efde-0002a5d5c51b";
    private static final String CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb";
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
    private static final VehicleScanPolicy.RestartBudget SCAN_RESTART_BUDGET = new VehicleScanPolicy.RestartBudget();

    private final IBinder binder = new LocalBinder();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final VehicleTargetSelector<GeelyAdvertisement> targetSelector = new VehicleTargetSelector<>();
    private final VehicleWakeGate<GeelyAdvertisement> wakeGate = new VehicleWakeGate<>(targetSelector);
    private final DiagnosticCounters diagnostics = new DiagnosticCounters();
    private final PendingControl pendingControl = new PendingControl();
    private final OfficialReadyGate officialReadyGate = new OfficialReadyGate();
    private final LiveActivityTrace liveTrace = new LiveActivityTrace();
    private final Runnable notificationStateListener = this::handleOfficialNotificationUpdate;
    private final Runnable compatibilityCaptureTimeout = () -> finishCompatibilityCapture("duration_elapsed");

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
    private volatile float connectDbm = VehicleIdentityStore.DEFAULT_CONNECT_DBM;
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
    private long lastVehicleSignalAtMs = 0L;
    private long longAbsenceSessionStartedAtMs = 0L;
    private String wakeSessionAddress = "";
    private String lastWakeWaitKey = "";
    private long lastWakeWaitLogAtMs;
    private long lastHintFirstLogAtMs;
    private String lastWakeAttemptAddress = "";
    private long lastWakeAttemptAtMs = 0L;
    private GeelyAdvertisement lastWakeAttemptAd;
    private String lastWeakCandidateKey = "";
    private long lastWeakCandidateLogAtMs = 0L;
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
    private long lastGattAttemptAtMs = 0L;
    private volatile long lastGeelyConnectedAtMs = 0L;
    private volatile long lastNotificationUpdateAtMs = 0L;
    private long lastListenerRebindAtMs = 0L;
    private long lastUpdateAutoCheckAtMs = 0L;
    private volatile long lastZoneLogAtMs = 0L;
    private volatile long lastLearnAttemptAtMs = 0L;
    private volatile int unlockCount = 0;
    private volatile int lockCount = 0;
    private int manualRequestSequence = 0;
    private long nextManualControlAtMs = 0L;
    private volatile String lastManualControlMessage = "请先在银河 App 连接蓝牙钥匙，再点击测试。";
    private volatile int lastRawRssi = -127;
    private volatile PassiveKeyLogic.Action lastDispatchAction = PassiveKeyLogic.Action.NONE;
    private volatile long lastDispatchStartedAtMs = 0L;
    private volatile String lastControlResult = "NONE";
    private float lastSmoothRssi = Float.NaN;
    private float lastApproachRssi = Float.NaN;
    private volatile String lastSource = "";
    private volatile String lastAction = "等待车辆";
    private volatile String lastZone = "等待车辆蓝牙信号";
    private volatile String lastZoneCategory = "";

    private volatile GeelyAdvertisement currentAd;
    private volatile BluetoothDevice lastObservedCarDevice;
    private volatile String lastObservedBleMac = "";
    private volatile long lastSelfGattCloseAtMs = 0L;
    private volatile String lastSelfGattAddress = "";
    private String activeScanPlanKey = "";
    private String activeScanMode = "STOPPED";
    private String activeTargetAddress = "";
    private String lastHandledAdvertAddress = "";
    private long lastHandledAdvertNanos;
    private String lastMatchReason = "NONE";
    private long lastMatchLogAtMs;
    private long lastScanAgeMs = -1L;
    private long maxScanAgeMs;
    private long lastLearningWaitLogAtMs;
    private volatile BluetoothGatt activeGatt;
    private boolean authObservationActive = false;
    private boolean authObserverNotifyReady = false;
    private boolean gattAuthSuccessObserved = false;
    private String authObservationAddress = "";
    private long authObservationStartedAtMs = 0L;
    private long gattAuthSuccessObservedAtMs = 0L;
    private volatile int lastGattNotifyCmdId = -1;
    private volatile long lastGattNotifyCmdAtMs = 0L;
    private volatile long lastApproachLockNotifyAtMs = 0L;
    private CompatibilityCapture.Session compatibilityCaptureSession;
    private CompletableFuture<CompatibilityCapture.Result> compatibilityCaptureFuture;
    private Set<String> gattBaselineAddresses = new HashSet<>();
    private long lastGattBaselineAtMs = 0L;

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
        public boolean driving;
        public boolean wakeActive;
        public boolean aclRecent;
        public boolean autoUnlock;
        public boolean autoLock;
        public boolean blockWhenCharging;
        public boolean wiredCharging;
        public boolean manualControlEnabled;
        public float unlockDbm;
        public float lockDbm;
        public long lockDwellMs;
        public float connectDbm;
        public boolean usingDefaultThresholds = true;
        public int rssi = -127;
        public float smoothRssi = Float.NaN;
        public int unlockCount;
        public int lockCount;
        public long lastSampleAgeMs = -1L;
        public long wakeAgeMs = -1L;
        public long lastSentAgeMs = -1L;
        public long dispatchAgeMs = -1L;
        public long lockDwellElapsedMs = -1L;
        public int lockDwellSamples;
        public PassiveKeyLogic.Action pendingAction = PassiveKeyLogic.Action.NONE;
        public PassiveKeyLogic.Action lastSentAction = PassiveKeyLogic.Action.NONE;
        public PassiveKeyLogic.Action dispatchAction = PassiveKeyLogic.Action.NONE;
        public String lastControlResult = "NONE";
        public java.util.List<LiveActivityTrace.Entry> liveTrace = java.util.Collections.emptyList();
        public long liveTraceNowMs;
        public String pendingWaitReason = "";
        public String sampleDecision = "";
        public String vehicleName = "";
        public String vehicleAddress = "";
        public String zone = "";
        public String lastAction = "";
        public String source = "";
        public String authObserver = "";
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
        connectDbm = identityStore.getConnectDbm();
        wakeGate.setConnectThreshold(connectDbm);
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
        KeyNotificationBridgeService.setControlContext(this);
        KeyNotificationBridgeService.markGattAuthReady(false);
        if (serviceEnabled) {
            ServiceWatchdogReceiver.schedule(this);
            GeelyBridge.bindKeepAlive(this);
            KeyNotificationBridgeService.ensureRebound(this);
            UpdateManager.maybeAutoCheck(this);
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
        if (compatibilityCaptureSession != null) finishCompatibilityCapture("service_destroy");
        cancelPendingControl("服务停止");
        KeyNotificationBridgeService.markGattAuthReady(false);
        clearGattAuthObservation("service_destroy", false);
        serviceEnabled = false;
        KeyNotificationBridgeService.clearStateListener(notificationStateListener);
        running = false;
        handler.removeCallbacksAndMessages(null);
        stopBleScan();
        clearWakeState();
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
        boolean drivingNow = isDriving(now);
        String activeWakeAddress = wakeGate.activeAddress();
        long lockDwellStart = logic.getLockDwellStartMs();
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
        status.driving = drivingNow;
        status.wakeActive = !activeWakeAddress.isEmpty();
        status.aclRecent = !activeWakeAddress.isEmpty()
                && wakeGate.hasRecentAclHandshake(activeWakeAddress, now);
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
        status.connectDbm = connectDbm;
        status.usingDefaultThresholds = !identityStore.hasCustomThresholds();
        status.rssi = signalFresh ? lastRawRssi : -127;
        status.smoothRssi = signalFresh && Float.isFinite(lastSmoothRssi) ? lastSmoothRssi : Float.NaN;
        status.unlockCount = unlockCount;
        status.lockCount = lockCount;
        status.lastSampleAgeMs = lastSampleAtMs > 0L
                ? Math.max(0L, now - lastSampleAtMs) : -1L;
        status.wakeAgeMs = age(now, firstWakeInSessionAtMs);
        status.pendingAction = pendingControl.getAction();
        status.pendingWaitReason = lastPendingWaitReason;
        status.lastSentAction = logic.getLastSentAction();
        status.dispatchAction = lastDispatchAction;
        status.lastControlResult = lastControlResult;
        status.liveTrace = liveTrace.snapshot();
        status.liveTraceNowMs = now;
        status.lastSentAgeMs = age(now, logic.getLastSentActionAtMs());
        status.dispatchAgeMs = age(now, lastDispatchStartedAtMs);
        status.sampleDecision = logic.getLastSampleDecision();
        status.lockDwellSamples = logic.getLockDwellSamples();
        status.lockDwellElapsedMs = lockDwellStart == 0L ? -1L : Math.max(0L, now - lockDwellStart);
        status.lockDwellMs = lockDwellMs;
        status.vehicleName = identity != null ? identity.displayName() : "";
        status.vehicleAddress = identity != null ? identity.address : "";
        status.zone = !serviceEnabled ? "无感进出服务已关闭"
                : (isChargingBlocked() ? "已插充电线 · 禁止解闭锁"
                : (signalFresh ? lastZone : "不在车辆附近 (无信号)"));
        status.lastAction = lastAction;
        status.source = signalFresh ? lastSource : "";
        status.authObserver = gattAuthSuccessObserved ? "CMD5_OBSERVED"
                : authObserverNotifyReady ? "NOTIFY_READY"
                : authObservationActive ? "OBSERVING" : "IDLE";
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

    /** 手动导出动作快照合集；不修改留存文件。 */
    public Future<File> exportActionSnapshots(File destination) {
        return DiagnosticLog.exportActionSnapshots(destination, getDiagnosticSnapshot());
    }

    /** 返回自动快照数量与占用。 */
    public String getActionSnapshotHealth() {
        return DiagnosticLog.actionSnapshotHealth();
    }

    /** 手动导出时只读取当前快照，ZIP在日志线程生成，不触发扫描、连接或车控。 */
    public Future<File> exportLogs(File destination) {
        return DiagnosticLog.export(destination, getDiagnosticSnapshot());
    }

    /**
     * 用户主动上报车型兼容问题时，暂停普通过滤扫描并抓取全量 BLE 原始广播。
     * 这里只观察，不连接设备、不改身份、不执行车控；抓取结束后自动恢复原扫描。
     */
    public Future<CompatibilityCapture.Result> captureCompatibilityRaw(long durationMs) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return CompletableFuture.completedFuture(
                    CompatibilityCapture.unavailable("caller_not_main_thread"));
        }
        if (compatibilityCaptureFuture != null && compatibilityCaptureSession != null) {
            return compatibilityCaptureFuture;
        }
        CompletableFuture<CompatibilityCapture.Result> future = new CompletableFuture<>();
        compatibilityCaptureFuture = future;
        compatibilityCaptureSession = new CompatibilityCapture.Session(this, durationMs, "service_scan");
        cancelPendingControl("车型兼容原始抓取");
        DiagnosticLog.event("Compatibility", "CAPTURE_START",
                "duration_ms=" + Math.max(1_000L, Math.min(30_000L, durationMs))
                        + " mode=UNFILTERED_LOW_LATENCY");
        appendLog("开始 10 秒车型兼容原始 BLE 抓取，期间暂停普通车辆扫描");
        handler.post(this::startCompatibilityScan);
        return future;
    }

    private void startCompatibilityScan() {
        CompatibilityCapture.Session session = compatibilityCaptureSession;
        if (session == null) return;
        if (!hasBluetoothPermission()) {
            finishCompatibilityCapture("permission_denied");
            return;
        }
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            finishCompatibilityCapture("bluetooth_unavailable");
            return;
        }
        try {
            stopBleScan();
            bleScanner = bluetoothAdapter.getBluetoothLeScanner();
            if (bleScanner == null) {
                finishCompatibilityCapture("scanner_unavailable");
                return;
            }
            long now = SystemClock.elapsedRealtime();
            SCAN_RESTART_BUDGET.started(now);
            bleScanner.startScan(null, CompatibilityCapture.scanSettings(), scanCallback);
            activeScanPlanKey = "COMPATIBILITY_RAW";
            activeScanMode = "COMPATIBILITY_RAW";
            isScanning = true;
            lastScanStartAtMs = now;
            handler.removeCallbacks(compatibilityCaptureTimeout);
            handler.postDelayed(compatibilityCaptureTimeout,
                    Math.max(1_000L, Math.min(30_000L, session.durationMs())));
            DiagnosticLog.detail("Compatibility", "SCAN_START",
                    "filters=0 mode=UNFILTERED_LOW_LATENCY duration_ms=" + session.durationMs());
            updateNotificationIfNeeded(true);
        } catch (Throwable error) {
            session.addError("start_compatibility_scan", error);
            finishCompatibilityCapture("scan_start_failed");
        }
    }

    /** 完成抓取、交付结果并恢复原身份过滤扫描；任何失败都必须允许上传继续。 */
    private void finishCompatibilityCapture(String reason) {
        CompatibilityCapture.Session session;
        CompletableFuture<CompatibilityCapture.Result> future;
        synchronized (this) {
            session = compatibilityCaptureSession;
            future = compatibilityCaptureFuture;
            compatibilityCaptureSession = null;
            compatibilityCaptureFuture = null;
        }
        if (session == null) return;
        handler.removeCallbacks(compatibilityCaptureTimeout);
        stopBleScan();
        CompatibilityCapture.Result result = session.finish(reason, activeGatt);
        if (future != null) future.complete(result);
        DiagnosticLog.event("Compatibility", "CAPTURE_DONE", result.diagnosticSummary());
        appendLog("车型兼容原始抓取完成：" + result.deviceCount + " 个设备 / "
                + result.uniqueObservationCount + " 种广播");
        handler.postDelayed(() -> {
            if (running && serviceEnabled) startBleScan();
        }, 800L);
        updateNotificationIfNeeded(true);
    }

    /** 返回可导出的完整状态，所有计时使用单调时间且身份只有短指纹。 */
    public String getDiagnosticSnapshot() {
        long now = SystemClock.elapsedRealtime();
        return "run=" + serviceRun + "\n" + environmentDiagnostic()
                + "\n" + triggerConfigDiagnostic()
                + "\nlive_trace=" + liveTraceText(now)
                + "\n" + signalDiagnostic(now) + "\n" + logic.diagnosticSnapshot(now)
                + "\n" + pendingControl.diagnosticSnapshot(now) + " wait_reason=" + lastPendingWaitReason
                + "\n" + scanDiagnostic(now, false) + "\n" + gattDiagnostic(now)
                + " gatt_baseline_count=" + gattBaselineAddresses.size()
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
        lastDispatchAction = action;
        lastDispatchStartedAtMs = now;
        KeyNotificationBridgeService.ControlResult result =
                KeyNotificationBridgeService.triggerManualControlViaNotification(action);
        if (result != KeyNotificationBridgeService.ControlResult.NOT_SENT) {
            logic.onManualControlSent(action, now);
            lastControlResult = result.name();
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
            long downMs = KeyNotificationBridgeService.getListenerDownDurationMs();
            if (downMs < PermissionAudit.LISTENER_REBIND_GRACE_MS) {
                return "系统授权仍在，通知桥接正在自动重绑；如持续未恢复，请打开“授权与运行全检”";
            }
            return "系统授权仍在，但通知桥接未绑定；请打开“授权与运行全检”修复";
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
        if (!enabled && compatibilityCaptureSession != null) {
            finishCompatibilityCapture("service_disabled");
        }
        KeyNotificationBridgeService.markGattAuthReady(false);
        clearGattAuthObservation("service_switch", false);
        identityStore.setServiceEnabled(enabled);
        cancelPendingControl("服务总开关变更");
        clearWakeState();
        targetSelector.clear();
        if (enabled) {
            logic.reset();
            liveTrace.clear();
            rssiFilter = new RssiFilter();
            lastSampleAtMs = 0L;
            activeTargetAddress = "";
            geelyConnected = false;
            officialReadyGate.onConnectionChanged(false);
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
     * 更新四项触发参数：连接预热、靠近解锁、离开上锁和离开确认毫秒数。
     */
    public void updateThresholds(float newUnlockDbm, float newLockDbm, long newLockDwellMs,
                                 float newConnectDbm) {
        float safeUnlock = Float.isFinite(newUnlockDbm) ? newUnlockDbm : VehicleIdentityStore.DEFAULT_UNLOCK_DBM;
        float safeLock = Float.isFinite(newLockDbm) ? newLockDbm : VehicleIdentityStore.DEFAULT_LOCK_DBM;
        float safeConnect = Float.isFinite(newConnectDbm) ? newConnectDbm : VehicleIdentityStore.DEFAULT_CONNECT_DBM;
        float clampedUnlock = Math.max(-90.0f, Math.min(-40.0f, safeUnlock));
        float clampedLock = Math.max(-98.0f, Math.min(clampedUnlock - 6.0f, safeLock));
        long clampedDwell = Math.max(1500L, Math.min(20000L, newLockDwellMs));
        float clampedConnect = Math.max(-115.0f, Math.min(clampedUnlock - 5.0f, safeConnect));
        this.unlockDbm = clampedUnlock;
        this.lockDbm = clampedLock;
        this.lockDwellMs = clampedDwell;
        this.connectDbm = clampedConnect;
        identityStore.saveThresholds(clampedUnlock, clampedLock, clampedDwell, clampedConnect);
        wakeGate.setConnectThreshold(clampedConnect);
        logic.setThresholds(clampedUnlock, clampedLock, clampedDwell);
        cancelPendingControl("用户调整阈值，重新确认距离");
        appendLog(String.format(Locale.US,
                "✓ 已更新触发设置：连接触发 ≥ %.0f dBm | 靠近解锁 ≥ %.0f dBm | 离开上锁 ≤ %.0f dBm (连续 %.1fs)",
                clampedConnect, clampedUnlock, clampedLock, clampedDwell / 1000.0f));
        updateNotificationIfNeeded(true);
    }

    /**
     * 允许用户手动填入车架号后 3 位（例如 "123"）辅助锁定本车，或留空完全走自动识别。
     */
    public void bindByVinSuffix(String vinTail3Input) {
        if (vinTail3Input == null) return;
        String digits = vinTail3Input.replaceAll("[^0-9A-Za-z]", "").trim().toUpperCase(Locale.ROOT);
        if (digits.length() > 3) {
            digits = digits.substring(digits.length() - 3);
        }
        if (digits.length() < 2) {
            appendLog("车架号尾号至少需要 2~3 位字符");
            return;
        }
        cancelPendingControl("用户更新本车身份");
        KeyNotificationBridgeService.markGattAuthReady(false);
        clearGattAuthObservation("identity_hint_changed", false);
        String tail3 = digits.length() == 3 ? digits : "";
        String tail2 = digits.substring(digits.length() - 2);
        String name = !tail3.isEmpty() ? ("GeelyVehicle" + tail3) : "";
        boolean sameHint = identity != null && (!tail3.isEmpty()
                ? tail3.equalsIgnoreCase(identity.vinTail3) : tail2.equalsIgnoreCase(identity.vinTail2));
        String addr = sameHint ? identity.address : "";
        if (!sameHint) {
            closeGatt("更新本车尾号匹配特征");
            currentAd = null;
            lastObservedCarDevice = null;
            lastObservedBleMac = "";
            targetSelector.clear();
            logic.reset();
            rssiFilter = new RssiFilter();
            lastSampleAtMs = 0L;
        }
        targetSelector.clear();
        clearWakeState();
        activeTargetAddress = "";
        identity = new VehicleIdentity(addr, name, tail2, tail3,
                sameHint ? identity.mfgSignature : "",
                sameHint ? identity.manufacturerId : -1, System.currentTimeMillis());
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
        KeyNotificationBridgeService.markGattAuthReady(false);
        clearGattAuthObservation("reset_learning", false);
        identityStore.clear();
        identity = null;
        currentAd = null;
        lastObservedCarDevice = null;
        lastObservedBleMac = "";
        closeGatt("重新学习本车");
        targetSelector.clear();
        clearWakeState();
        activeTargetAddress = "";
        logic.reset();
        rssiFilter = new RssiFilter();
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
        /** 重试启动继续使用当前身份过滤，并共享启动限频预算。 */
        @Override
        public void run() {
            startBleScan();
        }
    };

    /** 同时核验扫描与连接权限，缺权限时不构造未经验证的设备身份。 */
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
            long now = SystemClock.elapsedRealtime();
            long delay = SCAN_RESTART_BUDGET.delayMs(now);
            if (delay > 0L) {
                diagnostics.increment("scan_start_budget_wait");
                handler.removeCallbacks(restartScanRunnable);
                handler.postDelayed(restartScanRunnable, delay);
                return;
            }
            VehicleScanPolicy.Plan plan = VehicleScanPolicy.build(identity);
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
            List<ScanFilter> filters = buildHardwareScanFilters(plan);
            SCAN_RESTART_BUDGET.started(now);
            bleScanner.startScan(filters, settings, scanCallback);
            activeScanPlanKey = plan.key;
            activeScanMode = plan.mode;
            isScanning = true;
            lastScanStartAtMs = SystemClock.elapsedRealtime();
            DiagnosticLog.detail("Scan", "SCAN_START", "filters=" + filters.size() + " filter_mode=" + plan.mode
                    + " mode=LOW_LATENCY report_delay_ms=0 " + environmentDiagnostic());
            appendDiagnostic("开始硬件过滤低延迟扫描（支持息屏扫描与本车自动识别） · " + plan.mode);
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
     * 吉利/极氪通用广播特征只用于首次学习；已学习后替换为本车地址、名称及窄厂商前缀。
     * 精确条件不能继续OR到协议级宽过滤中；转换失败也绝不回退成无Filter扫描。
     */
    private List<ScanFilter> buildHardwareScanFilters(VehicleScanPolicy.Plan plan) {
        List<ScanFilter> filters = new ArrayList<>();
        for (VehicleScanPolicy.Rule rule : plan.rules) {
            ScanFilter.Builder builder = new ScanFilter.Builder();
            if (rule.kind == VehicleScanPolicy.Kind.ADDRESS) {
                builder.setDeviceAddress(rule.value);
            } else if (rule.kind == VehicleScanPolicy.Kind.NAME) {
                builder.setDeviceName(rule.value);
            } else if (rule.kind == VehicleScanPolicy.Kind.SERVICE) {
                // 吉利/极氪通用广播 Service UUID 0xFDFD，仅学习模式才保留裸UUID规则。
                builder.setServiceUuid(ParcelUuid.fromString(rule.value));
            } else {
                // 吉利/UAES V2 Company ID 0x01FE (510)、0x06FE (1790)及15FF兼容。
                // 空数据仅学习时放行协议；跟踪时匹配完整前缀，手填提示使用VIN位掩码。
                builder.setManufacturerData(rule.manufacturerId, rule.data, rule.mask);
            }
            filters.add(builder.build());
        }
        if (filters.isEmpty()) throw new IllegalStateException("BLE过滤计划不得为空");
        return filters;
    }

    private final Runnable refreshScanFiltersRunnable = new Runnable() {
        /** 合并身份补全产生的重配置，等待启动预算时保留旧扫描，不制造扫描空窗。 */
        @Override
        public void run() {
            if (!running || !serviceEnabled || !isScanning) return;
            VehicleScanPolicy.Plan desired = VehicleScanPolicy.build(identity);
            if (desired.key.equals(activeScanPlanKey)) return;
            long delay = SCAN_RESTART_BUDGET.delayMs(SystemClock.elapsedRealtime());
            if (delay > 0L) {
                handler.postDelayed(this, delay);
                return;
            }
            DiagnosticLog.event("Scan", "FILTER_RECONFIGURE", "old_mode=" + activeScanMode
                    + " new_mode=" + desired.mode + " filters=" + desired.rules.size());
            stopBleScan();
            startBleScan();
        }
    };

    /** 仅在实际过滤内容变化时请求重建，不在每个广播回调中反复stop/start。 */
    private void requestScanFilterRefresh() {
        if (!running || !serviceEnabled || !isScanning) return;
        if (VehicleScanPolicy.build(identity).key.equals(activeScanPlanKey)) return;
        if (!handler.hasCallbacks(refreshScanFiltersRunnable)) {
            handler.postDelayed(refreshScanFiltersRunnable, 750L);
        }
    }

    /** 停止当前扫描以及尚未执行的重配置，不遗留后台重启请求。 */
    private void stopBleScan() {
        handler.removeCallbacks(restartScanRunnable);
        handler.removeCallbacks(refreshScanFiltersRunnable);
        if (bleScanner != null && isScanning) {
            try {
                bleScanner.stopScan(scanCallback);
            } catch (Throwable ignored) {
            }
        }
        isScanning = false;
        activeScanPlanKey = "";
        activeScanMode = "STOPPED";
        DiagnosticLog.detail("Scan", "SCAN_STOP", scanDiagnostic(SystemClock.elapsedRealtime(), false));
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        /** 先检查真实采集年龄再解析，按物理MAC去重，所有匹配路径共用唯一目标选择。 */
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                handler.post(() -> onScanResult(callbackType, result));
                return;
            }
            diagnostics.increment("scan_callbacks");
            long now = SystemClock.elapsedRealtime();
            lastScanCallbackAtMs = now;
            CompatibilityCapture.Session capture = compatibilityCaptureSession;
            if (capture != null) {
                diagnostics.increment("compatibility_scan_callbacks");
                capture.record(callbackType, result, System.currentTimeMillis(), now);
                return;
            }
            if (!serviceEnabled || !running) {
                diagnostics.increment("scan_disabled");
                return;
            }
            if (result == null) {
                diagnostics.increment("scan_not_geely");
                return;
            }
            long scanNanos = result.getTimestampNanos();
            long ageNanos = SystemClock.elapsedRealtimeNanos() - scanNanos;
            lastScanAgeMs = ageNanos / 1000000L;
            maxScanAgeMs = Math.max(maxScanAgeMs, lastScanAgeMs);
            if (scanNanos <= 0L || ageNanos < 0L
                    || ageNanos > PassiveKeyLogic.SIGNAL_STALE_MS * 1000000L) {
                diagnostics.increment("scan_stale");
                return;
            }
            GeelyAdvertisement ad = GeelyAdvertisement.from(result);
            if (ad == null) {
                diagnostics.increment("scan_not_geely");
                return;
            }
            if (!targetSelector.observe(ad, now)) {
                diagnostics.increment("scan_duplicate");
                return;
            }
            VehicleIdentity.Match match = identity == null ? VehicleIdentity.Match.HINT : identity.match(ad);
            if (match == VehicleIdentity.Match.NONE || match == VehicleIdentity.Match.CONFLICT) {
                diagnostics.increment("scan_other_vehicle");
                recordScanMatch(ad, match.name(), now);
                return;
            }
            recordFirstCandidate(ad, match, now);
            if (liveTrace.lastAt(LiveActivityTrace.Step.SCAN_FOUND) == 0L
                    || now - liveTrace.lastAt(LiveActivityTrace.Step.SCAN_FOUND) > 12000L) {
                liveTrace.add(LiveActivityTrace.Step.SCAN_FOUND, now, "RSSI " + ad.rssi + " dBm");
            }
            GeelyAdvertisement primary = choosePrimaryCandidate(now);
            if (primary == null || !primary.address.equalsIgnoreCase(ad.address)) {
                // 名称确认只控制普通发现；强本车线索首包可先交官方，不保存地址、不送RSSI。
                maybeWakeGeely(ad, now, ad.advertisedName);
                diagnostics.increment("scan_non_primary");
                recordScanMatch(ad, "NON_PRIMARY_" + match.name(), now);
                return;
            }

            // 尚未学习本车时不再凭当前回调candidates.size()==1直接自动学习并接管。
            // 先观察唯一主候选并交给官方鉴权；只有系统可见且已验证的真实连接能完成学习。
            boolean trusted = targetSelector.authorizesRssi(identity, ad);
            if (trusted) {
                updateIdentityFromAdvertisement(ad, match);
                lastObservedCarDevice = result.getDevice();
                lastObservedBleMac = ad.address;
            } else {
                diagnostics.increment("scan_discovery_only");
            }
            recordScanMatch(ad, trusted ? match.name() : "DISCOVERY_" + match.name(), now);
            handleTargetAdvertisement(ad, now);
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
            CompatibilityCapture.Session capture = compatibilityCaptureSession;
            if (capture != null) {
                capture.scanFailed(errorCode);
                finishCompatibilityCapture("scan_failed");
                return;
            }
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

    /**
     * 身份尚未学习时固定一个主候选最多12秒，预算从选中时起算而非最后收到广播。
     *
     * 附近同时有两辆吉利车时，不能把两辆车的 RSSI 混入同一个滤波器；先观察并尝试最强候选，
     * 等官方 App 真正连上后再由唯一可验证的连接决定本车；已学习路径同样不能多MAC混流。
     */
    // 已学身份的首包预连接与后续发现共用更早的尝试起点，不因目标确认而重置握手证据。
    private GeelyAdvertisement choosePrimaryCandidate(long now) {
        String previous = targetSelector.selectedAddress();
        String expiredWakeAddress = wakeGate.maintain(identity, now);
        if (!expiredWakeAddress.isEmpty()) {
            DiagnosticLog.event("Bridge", "WAKE_BUDGET_EXPIRED", "addr_id="
                    + DiagnosticLog.fingerprint(expiredWakeAddress) + " cooldown_ms="
                    + VehicleTargetSelector.FAILED_COOLDOWN_MS);
        }
        GeelyAdvertisement selected = targetSelector.select(identity, geelyConnected, now);
        String address = targetSelector.selectedAddress();
        if (!previous.equalsIgnoreCase(address)) {
            DiagnosticLog.event("Scan", "TARGET_SWITCH", "old_addr_id=" + DiagnosticLog.fingerprint(previous)
                    + " addr_id=" + DiagnosticLog.fingerprint(address) + " reason=" + targetSelector.lastReason());
            // 选择确认不能清掉同地址的首包唤醒、节流或ACL保护；门控按实际MAC管理预算。
        }
        return selected;
    }

    /** 只补全已验证同MAC字段；跨地址名称/尾号线索必须等实际连接，不从扫描直接换MAC。 */
    private void updateIdentityFromAdvertisement(GeelyAdvertisement ad, VehicleIdentity.Match match) {
        VehicleIdentity previous = identity;
        VehicleIdentity merged = previous.mergeWith(ad);
        if (merged == previous) return;
        identity = merged;
        identityStore.save(identity);
        DiagnosticLog.event("Learning", "IDENTITY_UPDATED", "reason=" + match
                + " old_addr_id=" + DiagnosticLog.fingerprint(previous.address) + " " + identityDiagnostic());
        appendLog("已更新本车蓝牙特征: " + identityDiagnostic());
        requestScanFilterRefresh();
    }

    /** 第一条候选事件在普通选择前记录；匿名提示限频，完整本车名不被五秒MATCH节流隐藏。 */
    private void recordFirstCandidate(GeelyAdvertisement ad, VehicleIdentity.Match match, long now) {
        if (!targetSelector.claimFirstObservation(ad)) return;
        if (match == VehicleIdentity.Match.HINT && lastHintFirstLogAtMs > 0L && now - lastHintFirstLogAtMs < 1000L) {
            diagnostics.increment("candidate_first_log_suppressed");
            return;
        }
        if (match == VehicleIdentity.Match.HINT) lastHintFirstLogAtMs = now;
        String earlyReason = targetSelector.earlyConnectionReason(identity, ad, ad.advertisedName, now);
        if (!geelyConnected && (wakeGate.activeAddress().isEmpty() || ad.address.equalsIgnoreCase(wakeGate.activeAddress()))
                && (match == VehicleIdentity.Match.ADDRESS || "FULL_NAME_VENDOR_TAIL".equals(earlyReason))) {
            noteWakeSession(ad);
        }
        DiagnosticLog.event("Scan", "CANDIDATE_FIRST_SEEN", "addr_id=" + DiagnosticLog.fingerprint(ad.address)
                + " match_reason=" + match + " sampled_mono_ms=" + ad.seenAtMs
                + " scan_age_ms=" + (now - ad.seenAtMs) + " rssi=" + ad.rssi
                + " advertised_name=" + ad.advertisedName + " early_reason=" + earlyReason
                + " " + targetSelector.observationDiagnostic(ad, now));
    }

    /** 匹配诊断每五秒最多一条，记录依据与采集年龄；停车场拒绝回调不逐包刷日志。 */
    private void recordScanMatch(GeelyAdvertisement ad, String reason, long now) {
        lastMatchReason = reason;
        if (lastMatchLogAtMs > 0L && now - lastMatchLogAtMs < DIAGNOSTIC_INTERVAL_MS) return;
        lastMatchLogAtMs = now;
        DiagnosticLog.detail("Scan", "MATCH_RESULT", "match_reason=" + reason
                + " addr_id=" + DiagnosticLog.fingerprint(ad.address)
                + " name_id=" + DiagnosticLog.fingerprint(ad.name)
                + " prefix_id=" + DiagnosticLog.fingerprint(ad.mfgSignature)
                + " company=" + ad.manufacturerId + " sampled_mono_ms=" + ad.seenAtMs
                + " scan_age_ms=" + (now - ad.seenAtMs) + " rssi=" + ad.rssi
                + " target_id=" + DiagnosticLog.fingerprint(targetSelector.selectedAddress())
                + " " + targetSelector.observationDiagnostic(ad, now)
                + " " + wakeGate.diagnosticSnapshot(now));
    }

    /** 物理目标切换后清除滤波和驻留，保留已交付/手动上锁锁存，不能制造二次解锁。 */
    private void resetMeasurementTarget(String address, long now) {
        if (address.equalsIgnoreCase(activeTargetAddress)) return;
        boolean hadSample = lastSampleAtMs > 0L;
        activeTargetAddress = address;
        if (hadSample) {
            cancelPendingControl("测距目标切换，撤销旧距离请求");
            logic.onMeasurementTargetChanged(now);
        }
        rssiFilter = new RssiFilter();
        lastSampleAtMs = 0L;
        lastRawRssi = -127;
        lastSmoothRssi = Float.NaN;
        lastApproachRssi = Float.NaN;
        lastSource = "";
    }

    /** 首帧先唤醒官方钥匙；连接态采样正常时不混入广播以免改变滤波频率。 */
    private void handleTargetAdvertisement(GeelyAdvertisement ad, long now) {
        boolean trusted = targetSelector.authorizesRssi(identity, ad);
        boolean newAdvertisement = !ad.address.equalsIgnoreCase(lastHandledAdvertAddress)
                || ad.seenAtNanos > lastHandledAdvertNanos;
        if (!newAdvertisement) {
            // 定时维护只能重试官方唤醒，不重复记收包或用同一广告伪造新的测距样本。
            if (!geelyConnected) maybeWakeGeely(ad, now);
            return;
        }
        lastHandledAdvertAddress = ad.address;
        lastHandledAdvertNanos = ad.seenAtNanos;
        if (trusted) {
            diagnostics.increment("scan_accepted");
            lastAcceptedAdvertAtMs = ad.seenAtMs;
            resetMeasurementTarget(ad.address, now);
        }
        if (lastTargetAdvertAtMs == 0L || now - lastTargetAdvertAtMs > 12000L) {
            // 会话起点由实际尝试地址的首候选确定，后续选中不能覆盖提前唤醒时序。
            DiagnosticLog.event("Scan", "TARGET_FIRST_SEEN", "addr_id=" + DiagnosticLog.fingerprint(ad.address)
                    + " source=" + ad.sourceTag + " rssi=" + ad.rssi + " scan_age_ms="
                    + (SystemClock.elapsedRealtimeNanos() - ad.scanResult.getTimestampNanos()) / 1000000L
                    + " trusted=" + trusted + " screen_on=" + screenOn);
        }
        currentAd = ad;
        lastTargetAdvertAtMs = ad.seenAtMs;
        refreshWakeLock(now);
        if (!geelyConnected) {
            maybeWakeGeely(ad, now);
        }
        if (!trusted) {
            // 候选只唤醒官方，不参与阈值、近场保护或身份学习；官方就绪后读取实际连接证据。
            if (geelyConnected) maybeLearnVehicle(now);
            return;
        }
        if (!gattConnected || lastGattRssiAtMs == 0L
                || (now - lastGattRssiAtMs) > PassiveKeyLogic.SIGNAL_STALE_MS) {
            onFreshRssi(ad.rssi, "广播 " + ad.sourceTag, ad);
        } else {
            diagnostics.increment("scan_rssi_superseded_by_gatt");
        }
    }

    // ==================== 唤醒官方 App ====================

    /** 仅使用近期真实广播提前唤醒，握手中的同一 ACL 不重复注入连接请求。 */
    // 普通主目标沿用原路径，非扫描调用不能冒充当前包完整车名资格。
    private void maybeWakeGeely(GeelyAdvertisement ad, long now) {
        maybeWakeGeely(ad, now, false);
    }

    /** 所有预连接共用单目标预算和按地址ACL保护；提前连接与可信测距严格分开。 */
    private void maybeWakeGeely(GeelyAdvertisement ad, long now, boolean advertisedName) {
        updateVehicleAbsenceState(now);
        VehicleWakeGate.Result result = wakeGate.claim(ad, identity, serviceEnabled && running,
                geelyConnected, advertisedName, now, true, longAbsenceSessionStartedAtMs);
        if (result != VehicleWakeGate.Result.SEND_SELECTED
                && result != VehicleWakeGate.Result.SEND_EARLY_NAME
                && result != VehicleWakeGate.Result.SEND_EARLY_HINT) {
            if (result == VehicleWakeGate.Result.DISABLED || result == VehicleWakeGate.Result.OFFICIAL_READY) return;
            String counter = result == VehicleWakeGate.Result.WAIT_ACL_AUTH ? "wake_wait_acl_auth"
                    : "wake_" + result.name().toLowerCase(Locale.ROOT);
            diagnostics.increment(counter);
            if (result == VehicleWakeGate.Result.WAIT_ACL_AUTH) noteWakeSession(ad);
            if (result == VehicleWakeGate.Result.WAIT_ACL_AUTH
                    || result == VehicleWakeGate.Result.CANDIDATE_IN_FLIGHT
                    || result == VehicleWakeGate.Result.CANDIDATE_COOLDOWN) {
                recordWakeWait(ad, result, now);
            }
            return;
        }
        boolean newWakeSession = firstWakeInSessionAtMs == 0L
                || !ad.address.equalsIgnoreCase(wakeSessionAddress);
        noteWakeSession(ad);
        if (newWakeSession) {
            liveTrace.clear();
            clearGattAuthObservation("new_wake_session", true);
            gattAuthSuccessObserved = false;
        }
        if (firstWakeInSessionAtMs == 0L) firstWakeInSessionAtMs = now;
        int attempts = wakeGate.attempts();
        diagnostics.increment("wake_requests");
        if (result == VehicleWakeGate.Result.SEND_EARLY_NAME) diagnostics.increment("wake_early_name");
        if (result == VehicleWakeGate.Result.SEND_EARLY_HINT) diagnostics.increment("wake_early_hint");
        DiagnosticLog.event("Bridge", "WAKE_REQUEST", "attempt=" + attempts + " phase=" + result
                + " since_first_ad_ms=" + age(now, firstAdvertInSessionAtMs)
                + " addr_id=" + DiagnosticLog.fingerprint(ad.address) + " rssi=" + ad.rssi
                + " req_id=" + pendingControl.getSequence()
                + " threshold_dbm=" + (ad.rssi < wakeGate.normalConnectMinRssi()
                        ? wakeGate.longAbsenceConnectMinRssi() : wakeGate.normalConnectMinRssi())
                + " long_absence_ms=" + age(now, longAbsenceSessionStartedAtMs)
                + " since_first_wake_ms=" + age(now, firstWakeInSessionAtMs)
                + " " + targetSelector.observationDiagnostic(ad, now));
        liveTrace.add(LiveActivityTrace.Step.WAKE_REQUEST, now, "RSSI " + ad.rssi + " dBm");
        lastWakeAttemptAddress = ad.address;
        lastWakeAttemptAtMs = now;
        lastWakeAttemptAd = ad;
        GeelyBridge.wakeAndConnect(this, ad.scanResult, ad.carId());
        KeyNotificationBridgeService.requestFastRescan(this, KeyNotificationBridgeService.FAST_RESCAN_WINDOW_MS);
        scheduleWakeFallback(ad, now);
        String message = result == VehicleWakeGate.Result.SEND_EARLY_NAME ? "首包完整名提前连接银河 #"
                : result == VehicleWakeGate.Result.SEND_EARLY_HINT
                        ? (ad.rssi < VehicleWakeGate.NORMAL_CONNECT_MIN_RSSI
                                ? "长时间无车弱信号提前连接银河 #" : "稳定尾号强信号提前连接银河 #")
                : "注入广播唤醒银河 #";
        message += attempts + " · " + ad.rssi + "dBm";
        if (attempts == 1) appendLog(message);
        else appendDiagnostic(message);
    }

    /** 1.2秒仍无同MAC ACL时补发一次兼容action；已有ACL绝不重复唤醒。 */
    private void scheduleWakeFallback(GeelyAdvertisement ad, long wakeAtMs) {
        if (ad == null) return;
        String address = ad.address;
        handler.postDelayed(() -> {
            long now = SystemClock.elapsedRealtime();
            boolean aclSeen = wakeGate.hasRecentAclHandshake(address, now);
            DiagnosticLog.event("Bridge", "WAKE_ACL_WINDOW", "addr_id=" + DiagnosticLog.fingerprint(address)
                    + " wait_ms=" + age(now, wakeAtMs) + " acl_seen=" + aclSeen
                    + " connected=" + geelyConnected);
            if (!running || !serviceEnabled || geelyConnected
                    || !address.equalsIgnoreCase(wakeGate.activeAddress())
                    || aclSeen) return;
            GeelyBridge.wakeAndConnectFallback(this, ad.scanResult, ad.carId());
            KeyNotificationBridgeService.requestFastRescan(this, KeyNotificationBridgeService.FAST_RESCAN_WINDOW_MS);
        }, WAKE_FALLBACK_MS);
    }

    /** 手动停用/重学或蓝牙重置统一清理唤醒和分段日志，不清除身份或已交付车控。 */
    private void clearWakeState() {
        wakeGate.clear();
        liveTrace.clear();
        wakeSessionAddress = "";
        lastWakeAttemptAddress = "";
        lastWakeAttemptAtMs = 0L;
        lastWakeAttemptAd = null;
        lastVehicleSignalAtMs = 0L;
        longAbsenceSessionStartedAtMs = 0L;
        firstAdvertInSessionAtMs = 0L;
        firstWakeInSessionAtMs = 0L;
        lastWakeWaitKey = "";
        lastWakeWaitLogAtMs = 0L;
        lastHintFirstLogAtMs = 0L;
    }

    /** 依据最后一次本车广播维护“长时间无车”会话；不把邻车或失效信号算作本车。 */
    private void updateVehicleAbsenceState(long now) {
        if (lastVehicleSignalAtMs <= 0L
                || now < lastVehicleSignalAtMs
                || now - lastVehicleSignalAtMs > VehicleWakeGate.LONG_ABSENCE_MS) {
            if (longAbsenceSessionStartedAtMs == 0L) longAbsenceSessionStartedAtMs = now;
        } else {
            longAbsenceSessionStartedAtMs = 0L;
        }
    }

    /** 弱包只记录漏斗和首尾样本，不进入候选、RSSI、身份或车控资格。 */
    private void recordWeakCandidate(GeelyAdvertisement ad, VehicleIdentity.Match match, long now, String source) {
        String key = ad.address + "|" + source;
        if (key.equals(lastWeakCandidateKey)
                && lastWeakCandidateLogAtMs > 0L && now - lastWeakCandidateLogAtMs < 5000L) return;
        lastWeakCandidateKey = key;
        lastWeakCandidateLogAtMs = now;
        DiagnosticLog.detail("Scan", "WEAK_CANDIDATE", "addr_id=" + DiagnosticLog.fingerprint(ad.address)
                + " match_reason=" + match + " rssi=" + ad.rssi + " threshold_dbm=" + wakeGate.normalConnectMinRssi()
                + " sampled_mono_ms=" + ad.seenAtMs + " source=" + source);
        diagnostics.increment("weak_candidate");
    }

    /** 用实际尝试/握手地址的候选采集时刻关联会话，后续名称确认不覆盖已有首唤醒时间。 */
    private void noteWakeSession(GeelyAdvertisement ad) {
        if (ad == null) return;
        long firstSeen = targetSelector.firstSeenAtMs(ad.address);
        if (!ad.address.equalsIgnoreCase(wakeSessionAddress)
                || (firstSeen > 0L && firstSeen != firstAdvertInSessionAtMs)
                || firstAdvertInSessionAtMs == 0L) {
            wakeSessionAddress = ad.address;
            firstAdvertInSessionAtMs = firstSeen > 0L ? firstSeen : ad.seenAtMs;
            firstWakeInSessionAtMs = 0L;
        }
    }

    /** 只记录握手、单目标占用和冷却的变化或五秒心跳，不逐包输出拒绝事件。 */
    private void recordWakeWait(GeelyAdvertisement ad, VehicleWakeGate.Result result, long now) {
        if (ad == null) return;
        String key = ad.address + "|" + result;
        if (key.equals(lastWakeWaitKey) && now - lastWakeWaitLogAtMs < DIAGNOSTIC_INTERVAL_MS) return;
        // 不同邻车轮番到来时同样限频，不能用地址变化绕过拒绝日志预算。
        if (lastWakeWaitLogAtMs > 0L && now - lastWakeWaitLogAtMs < 1000L) return;
        lastWakeWaitKey = key;
        lastWakeWaitLogAtMs = now;
        DiagnosticLog.detail("Bridge", "WAKE_WAIT", "reason=" + result
                + " addr_id=" + DiagnosticLog.fingerprint(ad.address)
                + " active_id=" + DiagnosticLog.fingerprint(wakeGate.activeAddress())
                + " " + targetSelector.observationDiagnostic(ad, now)
                + " " + wakeGate.diagnosticSnapshot(now));
    }

    /** 官方在线后优先采用唯一GATT/ACL真实连接；近期实际注入且由银河确认的候选作为受限兜底。 */
    private void maybeLearnVehicle(long now) {
        if (!serviceEnabled || !hasBluetoothPermission() || !isOfficialConnected()) return;
        if (lastLearnAttemptAtMs > 0L && now - lastLearnAttemptAtMs < 1000L) return;
        lastLearnAttemptAtMs = now;

        // GATT 连接列表最接近“官方 App 真正连上的那辆车”，优先使用。
        VehicleIdentity verified = findIdentityFromConnectedGatt();
        if (verified != null) {
            adoptConnectedIdentity(verified, VehicleLearningState.CONNECTED, now);
            return;
        }
        // 部分 ROM 不向第三方暴露系统 GATT 列表，但同一 MAC 的 ACL 事件仍可作为真实连接证据。
        VehicleIdentity aclVerified = findIdentityFromRecentAclPeer(now);
        if (aclVerified != null) {
            adoptConnectedIdentity(aclVerified, VehicleLearningState.ACL, now);
            return;
        }
        VehicleIdentity wakeVerified = findIdentityFromLatestInjectedPeer(now);
        if (wakeVerified != null) {
            adoptConnectedIdentity(wakeVerified, VehicleLearningState.WAKE_CONFIRMED, now);
            return;
        }
        if (!hasCompleteIdentity() && (lastLearningWaitLogAtMs == 0L || now - lastLearningWaitLogAtMs >= 10000L)) {
            lastLearningWaitLogAtMs = now;
            DiagnosticLog.detail("Learning", "WAIT_VERIFIED_PEER", "waiting_reconnect=" + waitForDisconnectToLearn
                    + " candidates=" + targetSelector.size(now));
            appendDiagnostic("银河已连接，但暂未获得唯一可验证的本车地址；不再根据上次注入广播猜测绑定");
        }
    }

    /** 采用已验证连接身份，保留原开关与阈值，地址更新同时固定测距目标并刷新窄过滤。 */
    private void adoptConnectedIdentity(VehicleIdentity verified, String source, long now) {
        VehicleIdentity previous = identity;
        boolean changed = previous == null || !previous.stableKey().equalsIgnoreCase(verified.stableKey());
        if (changed) {
            identity = verified;
            identityStore.save(identity, source);
            DiagnosticLog.event("Learning", "CONNECTED_IDENTITY", "reason=" + source
                    + " old_addr_id=" + DiagnosticLog.fingerprint(previous == null ? "" : previous.address)
                    + " " + identityDiagnostic());
            appendLog("✓ 已从真实蓝牙连接识别本车: " + identityDiagnostic());
        } else if (!source.equals(identityStore.getLearningSource())) {
            identityStore.save(identity, source);
        }
        waitForDisconnectToLearn = false;
        GeelyAdvertisement wakeEvidence = lastWakeAttemptAd != null
                && identity.address.equalsIgnoreCase(lastWakeAttemptAddress) ? lastWakeAttemptAd : null;
        lastWakeAttemptAddress = "";
        lastWakeAttemptAtMs = 0L;
        lastWakeAttemptAd = null;
        targetSelector.pinConnected(identity.address, now);
        wakeGate.onVerifiedConnectedPeer();
        resetMeasurementTarget(identity.address, now);
        lastObservedBleMac = identity.address;
        GeelyAdvertisement evidence = targetSelector.evidenceFor(identity.address, now);
        if (evidence == null) evidence = wakeEvidence;
        currentAd = evidence;
        if (bluetoothAdapter != null) lastObservedCarDevice = bluetoothAdapter.getRemoteDevice(identity.address);
        requestScanFilterRefresh();
        updateNotificationIfNeeded(changed);
        if (evidence != null && evidence.seenAtMs > 0L && evidence.seenAtMs <= now
                && now - evidence.seenAtMs <= PassiveKeyLogic.SIGNAL_STALE_MS) {
            onFreshRssi(evidence.rssi, "广播 " + evidence.sourceTag, evidence);
        }
        processOfficialReadyDecision(now);
    }

    /**
     * 部分 ROM 同时隐藏系统 GATT 和 ACL 事件。只有当最近一次实际注入的是近期
     * 吉利候选，且银河在 30 秒内报告已连接时，才采用该候选地址；不再限定车型名称。
     */
    private VehicleIdentity findIdentityFromLatestInjectedPeer(long now) {
        if (!hasBluetoothPermission() || !isOfficialConnected()) return null;
        if (lastWakeAttemptAddress.isEmpty() || lastWakeAttemptAtMs <= 0L
                || now < lastWakeAttemptAtMs || now - lastWakeAttemptAtMs > 30_000L) return null;
        GeelyAdvertisement evidence = lastWakeAttemptAd;
        if (evidence == null || !lastWakeAttemptAddress.equalsIgnoreCase(evidence.address)) return null;
        return VehicleIdentity.fromTrustedConnectedPeer(evidence.address, evidence.name, evidence, identity);
    }

    /**
     * GATT 列表不可见时的安全兜底：ACL 地址必须同时匹配当前主候选/唤醒目标，
     * 且该地址仍有近期广播证据；无VIN尾号的车型也可由此完成地址绑定。
     */
    private VehicleIdentity findIdentityFromRecentAclPeer(long now) {
        if (!hasBluetoothPermission() || !isOfficialConnected()) return null;
        BluetoothDevice device = lastObservedCarDevice;
        if (device == null) return null;
        try {
            String address = device.getAddress();
            String selected = targetSelector.selectedAddress();
            String active = wakeGate.activeAddress();
            if (!address.equalsIgnoreCase(selected) && !address.equalsIgnoreCase(active)) return null;
            GeelyAdvertisement evidence = targetSelector.evidenceFor(address, now);
            if (evidence == null) return null;
            return VehicleIdentity.fromTrustedConnectedPeer(address, device.getName(), evidence, identity);
        } catch (SecurityException denied) {
            return null;
        }
    }

    /**
     * 从系统真实 GATT 连接选择本车，不再用车型名称作为学习白名单。
     * 优先选择官方连接前后新增的唯一 LE 设备；名称仅在多个连接的唯一消歧时使用。
     */
    private VehicleIdentity findIdentityFromConnectedGatt() {
        if (!hasBluetoothPermission() || !isOfficialConnected()) return null;
        long now = SystemClock.elapsedRealtime();
        List<ConnectedPeerSelector.Peer> peers = currentGattPeers();
        ConnectedPeerSelector.Selection selection =
                ConnectedPeerSelector.select(peers, gattBaselineAddresses);
        DiagnosticLog.detail("Learning", "GATT_PEER_SELECTION", selection.diagnostic()
                + " addr_id=" + DiagnosticLog.fingerprint(selection.address));
        if (!selection.selected()) return null;
        BluetoothDevice device = findGattDevice(selection.address);
        if (device == null) return null;
        try {
            String address = device.getAddress();
            GeelyAdvertisement evidence = targetSelector.evidenceFor(address, now);
            return VehicleIdentity.fromTrustedConnectedPeer(address, device.getName(), evidence, identity);
        } catch (SecurityException denied) {
            return null;
        }
    }

    /** 去重收集 GATT/GATT_SERVER 与本应用主动挂载的唯一连接。 */
    private List<ConnectedPeerSelector.Peer> currentGattPeers() {
        Map<String, BluetoothDevice> devices = new LinkedHashMap<>();
        for (BluetoothDevice device : GeelyBridge.getConnectedGattDevices(this)) addGattDevice(devices, device);
        if (activeGatt != null && gattConnected) addGattDevice(devices, activeGatt.getDevice());
        List<ConnectedPeerSelector.Peer> result = new ArrayList<>();
        for (BluetoothDevice device : devices.values()) {
            if (device == null) continue;
            try {
                result.add(new ConnectedPeerSelector.Peer(device.getAddress(), device.getName(),
                        device.getType(), device.getBondState()));
            } catch (SecurityException ignored) {
            }
        }
        return result;
    }

    private static void addGattDevice(Map<String, BluetoothDevice> devices, BluetoothDevice device) {
        if (device == null) return;
        try {
            String address = device.getAddress();
            if (VehicleLearningState.hasAddress(address)) {
                devices.putIfAbsent(address.toUpperCase(Locale.ROOT), device);
            }
        } catch (SecurityException ignored) {
        }
    }

    private BluetoothDevice findGattDevice(String address) {
        if (address == null || address.isEmpty()) return null;
        for (BluetoothDevice device : GeelyBridge.getConnectedGattDevices(this)) {
            try {
                if (address.equalsIgnoreCase(device.getAddress())) return device;
            } catch (SecurityException ignored) {
            }
        }
        if (activeGatt != null && gattConnected) {
            try {
                if (address.equalsIgnoreCase(activeGatt.getDevice().getAddress())) return activeGatt.getDevice();
            } catch (SecurityException ignored) {
            }
        }
        return null;
    }

    /** 官方断开期间持续记录普通GATT设备，官方连接时用集合差找到新增车机。 */
    private void refreshGattBaseline(long now) {
        if (geelyConnected || now < lastGattBaselineAtMs) return;
        if (lastGattBaselineAtMs > 0L && now - lastGattBaselineAtMs < 2000L) return;
        lastGattBaselineAtMs = now;
        Set<String> addresses = new HashSet<>();
        for (ConnectedPeerSelector.Peer peer : currentGattPeers()) addresses.add(peer.address);
        if (!addresses.equals(gattBaselineAddresses)) {
            gattBaselineAddresses = addresses;
            DiagnosticLog.detail("Learning", "GATT_BASELINE", "count=" + addresses.size()
                    + " gatt_baseline=" + addresses.size());
        }
    }

    // ==================== RSSI 与状态机 ====================

    /** 接近补偿与离开测距分离，先更新距离再验证旧请求，避免回到车旁仍执行上锁。 */
    private void onFreshRssi(int rawDbm, String source, GeelyAdvertisement ad) {
        if (!serviceEnabled || !running || compatibilityCaptureSession != null) return;
        if (rawDbm <= -105 || rawDbm >= 0) {
            diagnostics.increment("rssi_invalid");
            return;
        }
        long now = SystemClock.elapsedRealtime();
        long sampledAt = source.startsWith("GATT") || ad == null ? now : ad.seenAtMs;
        if (sampledAt <= 0L || sampledAt > now || now - sampledAt > PassiveKeyLogic.SIGNAL_STALE_MS
                || (lastSampleAtMs > 0L && sampledAt <= lastSampleAtMs)) {
            diagnostics.increment("rssi_stale_or_duplicate");
            return;
        }
        diagnostics.increment("rssi_accepted");
        if (signalMissingLogged) {
            DiagnosticLog.event("Signal", "SIGNAL_RECOVERED", "gap_ms=" + age(now, lastSampleAtMs)
                    + " rssi=" + rawDbm + " source=" + source);
            signalMissingLogged = false;
        }
        float smooth = rssiFilter.update(rawDbm, sampledAt);
        float effective = smooth + (proximityCovered ? 8.0f : 0.0f);

        lastRawRssi = rawDbm;
        lastSmoothRssi = smooth;
        lastApproachRssi = effective;
        lastSampleAtMs = sampledAt;
        lastSource = source;
        if (ad != null) currentAd = ad;

        boolean chargingBlocked = isChargingBlocked();
        boolean driving = isDriving(now) || chargingBlocked;
        PassiveKeyLogic.Action action = logic.onSample(
                effective,
                smooth,
                sampledAt,
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
        liveTrace.add(action == PassiveKeyLogic.Action.UNLOCK
                        ? LiveActivityTrace.Step.UNLOCK_REQUESTED : LiveActivityTrace.Step.LOCK_REQUESTED,
                now, String.format(Locale.US, "%.1f dBm · %s", effectiveDbm, source));
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
            // 待发解锁要求本次会话优先完成官方连接；重试预算从当前等待重新起算，但ACL保护仍生效。
            wakeGate.renewForPendingUnlock(now);
            maybeWakeGeely(currentAd, now);
            return;
        }
        if (wakeGate.needsVerifiedEarlyPeer()) {
            // 仅新增首包路径等待本次唯一真实连接，不能沿用旧MAC把“通知在线”当作新身份验证。
            recordPendingWait("WAIT_VERIFIED_PEER", now);
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
        lastDispatchAction = action;
        lastDispatchStartedAtMs = now;
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
        lastControlResult = result.name();
        captureActionSnapshot(action, sequence);
        liveTrace.add(result == KeyNotificationBridgeService.ControlResult.UNKNOWN
                        ? LiveActivityTrace.Step.RESULT_UNKNOWN
                        : action == PassiveKeyLogic.Action.UNLOCK
                        ? LiveActivityTrace.Step.UNLOCK_SENT : LiveActivityTrace.Step.LOCK_SENT,
                now, "req_id=" + sequence + " transport=PI");
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

    /** 每次成功交付解锁或上锁后异步保存最近三分钟，失败只记诊断，不影响车控。 */
    private void captureActionSnapshot(PassiveKeyLogic.Action action, int sequence) {
        try {
            String name = action == PassiveKeyLogic.Action.UNLOCK ? "UNLOCK" : "LOCK";
            DiagnosticLog.captureActionSnapshot(name,
                    "action=" + action + " req_id=" + sequence + "\n" + getDiagnosticSnapshot());
            DiagnosticLog.event("DiagnosticSnapshot", "ACTION_SNAPSHOT_REQUESTED",
                    "action=" + action + " req_id=" + sequence + " window_ms="
                            + DiagnosticSnapshotWindow.WINDOW_MS + " max_files="
                            + DiagnosticSnapshotWindow.MAX_FILES);
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("DiagnosticSnapshot", "ACTION_SNAPSHOT_REQUEST_ERROR", error);
        }
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

    /** 官方就绪机会每次连接只消费一次；地址证据稍晚到达时补处理，但不伪造第二个连接边沿。 */
    private void processOfficialReadyDecision(long now) {
        if (!geelyConnected || wakeGate.needsVerifiedEarlyPeer()
                || !officialReadyGate.claim(hasCompleteIdentity(), serviceEnabled)) return;
        boolean blocked = isDriving(now) || isChargingBlocked();
        PassiveKeyLogic.Action action = logic.onOfficialConnected(now, blocked, autoUnlock && !blocked);
        DiagnosticLog.event("Decision", "OFFICIAL_READY_RESULT", "action=" + action
                + " " + logic.diagnosticSnapshot(now));
        if (action == PassiveKeyLogic.Action.UNLOCK) {
            handleUnlock(lastRawRssi > -127 ? lastRawRssi : -60.0f, "银河蓝牙连接即刻解锁", currentAd);
        }
    }

    /** 仅官方连接状态边沿触发即时解锁，不被共享 GATT 的连接事件吞掉。 */
    private void onGeelyConnectionChanged(boolean connected, long now) {
        DiagnosticLog.event("Official", connected ? "CONNECTED" : "DISCONNECTED",
                "since_first_ad_ms=" + age(now, firstAdvertInSessionAtMs)
                        + " since_first_wake_ms=" + age(now, firstWakeInSessionAtMs)
                        + " wake_session_id=" + DiagnosticLog.fingerprint(wakeSessionAddress)
                        + " " + signalDiagnostic(now) + " " + KeyNotificationBridgeService.diagnosticSnapshot(now));
        geelyConnected = connected;
        officialReadyGate.onConnectionChanged(connected);
        if (connected) {
            lastGeelyConnectedAtMs = now;
            lastGattAttemptAtMs = 0L;
            wakeGate.onOfficialConnected(now);
            promoteAuthObserverToRssi();
            liveTrace.add(LiveActivityTrace.Step.OFFICIAL_CONNECTED, now, "官方通知就绪");
            appendLog("✓ 银河蓝牙钥匙已连接");
            lastLearnAttemptAtMs = 0L;
            maybeLearnVehicle(now);
            processOfficialReadyDecision(now);
        } else {
            appendLog("银河蓝牙钥匙已断开");
            lastWakeAttemptAddress = "";
            lastWakeAttemptAtMs = 0L;
            lastWakeAttemptAd = null;
            KeyNotificationBridgeService.markGattAuthReady(false);
            clearGattAuthObservation("official_disconnected", false);
            waitForDisconnectToLearn = false;
            wakeGate.onOfficialDisconnected();
            closeGatt("银河蓝牙已断开");
            logic.onOfficialDisconnected(now);
        }
        ensureGattAttached(now);
        updateNotificationIfNeeded(true);
    }

    /** 官方通知与系统蓝牙必须同时可用，蓝牙关闭期间旧通知不构成就绪。 */
    private boolean isOfficialConnected() {
        try {
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) return false;
            if (KeyNotificationBridgeService.isGattAuthReady()) return true;
            return KeyNotificationBridgeService.isGeelyBleConnected()
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

    private final Runnable authObservationTimeoutRunnable = new Runnable() {
        /** 超时只结束实验性观察，不伪造鉴权成功或车控资格。 */
        @Override
        public void run() {
            if (!authObservationActive || gattAuthSuccessObserved || geelyConnected) return;
            DiagnosticLog.event("Gatt", "AUTH_OBSERVE_TIMEOUT", "age_ms="
                    + age(SystemClock.elapsedRealtime(), authObservationStartedAtMs)
                    + " addr_id=" + DiagnosticLog.fingerprint(authObservationAddress));
            clearGattAuthObservation("observe_timeout", false);
            closeGatt("鉴权观察超时");
        }
    };

    /** ACL已到且存在单在途目标时，提前挂载官方通知特征做只读鉴权观察。 */
    private void maybeStartAuthObserver(BluetoothDevice device, long now) {
        if (device == null || geelyConnected || gattAuthSuccessObserved || activeGatt != null) return;
        String address = device.getAddress();
        String active = wakeGate.activeAddress();
        boolean learnedAddress = identity != null && address.equalsIgnoreCase(identity.address);
        if (!address.equalsIgnoreCase(active) && !learnedAddress) return;
        if (!address.equalsIgnoreCase(active) && !wakeGate.hasRecentAclHandshake(address, now)) return;
        try {
            authObservationActive = true;
            authObserverNotifyReady = false;
            gattAuthSuccessObserved = false;
            gattAuthSuccessObservedAtMs = 0L;
            authObservationAddress = address;
            authObservationStartedAtMs = now;
            lastSelfGattAddress = address;
            lastGattAttemptAtMs = now;
            gattAttemptId++;
            activeGatt = device.connectGatt(this, false, gattCallback, BluetoothDevice.TRANSPORT_LE,
                    BluetoothDevice.PHY_LE_1M_MASK, handler);
            if (activeGatt == null) {
                clearGattAuthObservation("connect_null", false);
                return;
            }
            handler.removeCallbacks(authObservationTimeoutRunnable);
            handler.postDelayed(authObservationTimeoutRunnable, AUTH_OBSERVE_TIMEOUT_MS);
            DiagnosticLog.event("Gatt", "AUTH_OBSERVE_START", "attempt_id=" + gattAttemptId
                    + " addr_id=" + DiagnosticLog.fingerprint(address));
        } catch (Throwable error) {
            DiagnosticLog.bridgeError("Gatt", "AUTH_OBSERVE_CONNECT_ERROR", error);
            clearGattAuthObservation("connect_error", false);
        }
    }

    /** 清理提前观察状态；仅在ACL/蓝牙/身份真正失效时清除全局鉴权就绪。 */
    private void clearGattAuthObservation(String reason, boolean clearReady) {
        handler.removeCallbacks(authObservationTimeoutRunnable);
        boolean wasActive = authObservationActive;
        authObservationActive = false;
        authObserverNotifyReady = false;
        gattAuthSuccessObserved = false;
        authObservationAddress = "";
        authObservationStartedAtMs = 0L;
        if (clearReady) KeyNotificationBridgeService.markGattAuthReady(false);
        if (wasActive) DiagnosticLog.event("Gatt", "AUTH_OBSERVE_CLEAR", "reason=" + reason
                + " clear_ready=" + clearReady);
    }

    /** 在官方通知特征写入CCCD，只开启通知观察，不写任何车控数据。 */
    private boolean enableAuthObserverNotify(BluetoothGatt gatt) {
        BluetoothGattService service = gatt.getService(UUID.fromString(AUTH_SERVICE_UUID));
        BluetoothGattCharacteristic notify = service == null ? null : service.getCharacteristic(UUID.fromString(AUTH_NOTIFY_UUID));
        if (notify == null) {
            DiagnosticLog.event("Gatt", "AUTH_NOTIFY_MISSING", "addr_id="
                    + DiagnosticLog.fingerprint(authObservationAddress));
            return false;
        }
        boolean local = gatt.setCharacteristicNotification(notify, true);
        BluetoothGattDescriptor descriptor = notify.getDescriptor(UUID.fromString(CCCD_UUID));
        byte[] cccdValue = (notify.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0
                ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                : BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        boolean write = false;
        if (descriptor != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                write = gatt.writeDescriptor(descriptor, cccdValue)
                        == BluetoothStatusCodes.SUCCESS;
            } else {
                descriptor.setValue(cccdValue);
                write = gatt.writeDescriptor(descriptor);
            }
        }
        DiagnosticLog.event("Gatt", "AUTH_NOTIFY_ENABLE", "local=" + local + " cccd_write=" + write
                + " addr_id=" + DiagnosticLog.fingerprint(authObservationAddress));
        return local && write;
    }

    /** 解析DK1.5通知头：cmdId=5确认鉴权，cmdId=0x0159识别车机原生PE误锁。 */
    private void handleKeyNotifyFrame(BluetoothGatt gatt, byte[] value, long now) {
        GattAuthFrameParser.Frame frame = GattAuthFrameParser.parse(value);
        if (frame == null || gatt != activeGatt) return;
        lastGattNotifyCmdId = frame.cmdId;
        lastGattNotifyCmdAtMs = now;
        diagnostics.increment("gatt_notify_frames");

        if (frame.isApproachLockNotice()) {
            lastApproachLockNotifyAtMs = now;
            DiagnosticLog.event("Gatt", "NATIVE_APPROACH_LOCK_NOTICE", "cmd_id=" + frame.cmdId
                    + " seq=" + frame.sequence + " len=" + frame.length
                    + " addr_id=" + DiagnosticLog.fingerprint(gatt.getDevice().getAddress()));
            handler.post(this::handleNativeApproachLockOnMain);
            return;
        }

        DiagnosticLog.detail("Gatt", "AUTH_FRAME", "cmd_id=" + frame.cmdId + " seq=" + frame.sequence
                + " len=" + frame.length + " addr_id=" + DiagnosticLog.fingerprint(authObservationAddress));
        if (!frame.isDk15AuthSuccess() || gattAuthSuccessObserved) return;
        String address = gatt.getDevice().getAddress();
        if (!VehicleLearningState.hasAddress(authObservationAddress)
                || !address.equalsIgnoreCase(authObservationAddress)) return;
        gattAuthSuccessObserved = true;
        gattAuthSuccessObservedAtMs = now;
        authObservationActive = false;
        authObserverNotifyReady = false;
        handler.removeCallbacks(authObservationTimeoutRunnable);
        KeyNotificationBridgeService.markGattAuthReady(true);
        KeyNotificationBridgeService.requestFastRescan(this, KeyNotificationBridgeService.FAST_RESCAN_WINDOW_MS);
        DiagnosticLog.event("Gatt", "AUTH_SUCCESS_OBSERVED", "cmd_id=" + frame.cmdId
                + " addr_id=" + DiagnosticLog.fingerprint(address)
                + " since_acl_ms=" + age(now, authObservationStartedAtMs));
        targetSelector.pinConnected(address, now);
        promoteAuthObserverToRssi();
        if (!geelyConnected) onGeelyConnectionChanged(true, now);
        else processPendingControl(true);
    }

    /** 只处理状态机已确认仍在本车近场的原生PE落锁；所有前置安全条件在纯逻辑内复核。 */
    private void handleNativeApproachLockOnMain() {
        long now = SystemClock.elapsedRealtime();
        boolean blocked = isDriving(now) || isChargingBlocked();
        PassiveKeyLogic.Action action = logic.onNativeApproachLock(now, blocked, autoUnlock && !blocked);
        DiagnosticLog.event("Gatt", "NATIVE_APPROACH_LOCK_RESULT", "action=" + action
                + " " + logic.diagnosticSnapshot(now) + " " + signalDiagnostic(now));
        if (action == PassiveKeyLogic.Action.UNLOCK) {
            beginControl(PassiveKeyLogic.Action.UNLOCK,
                    lastRawRssi > -127 ? lastRawRssi : -60.0f, "车机PE原生误锁纠偏");
        }
    }

    /** 官方通知或GATT鉴权任一先就绪时，复用同一client切换到RSSI轮询。 */
    private void promoteAuthObserverToRssi() {
        if (activeGatt == null || !gattConnected) return;
        authObservationActive = false;
        authObserverNotifyReady = false;
        handler.removeCallbacks(authObservationTimeoutRunnable);
        gattReadPending = false;
        handler.removeCallbacks(gattRssiPollRunnable);
        handler.post(gattRssiPollRunnable);
    }

    /** 官方就绪后共享已有连接，每条通道仅保留一个 RSSI 读取循环。 */
    private void ensureGattAttached(long now) {
        if (!serviceEnabled) return;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) return;
        if (activeGatt != null) {
            long lastProgress = Math.max(lastGattRssiAtMs, lastGattAttemptAtMs);
            if (gattConnected && (now - lastProgress) > SIGNAL_EXPIRE_UI_MS) {
                appendLog("连接态 RSSI 已停更，重建测距通道 | " + signalDiagnostic(now));
                boolean freshBroadcast = lastSampleAtMs > 0L
                        && (now - lastSampleAtMs) <= PassiveKeyLogic.SIGNAL_STALE_MS
                        && lastSource.startsWith("广播");
                if (!freshBroadcast) logic.onSignalLost(now);
                PassiveKeyLogic.Action pending = pendingControl.getAction();
                // 新鲜广播已经证明人仍在车旁时，重建 GATT 不能把合格的解锁请求一起取消。
                // 上锁必须依赖连接态连续远场，没有新鲜 GATT 样本始终撤销。
                if (pending == PassiveKeyLogic.Action.LOCK
                        || (pending == PassiveKeyLogic.Action.UNLOCK && !freshBroadcast)) {
                    cancelPendingControl(freshBroadcast ? "连接态信号停更，等待重建" : "连接态信号停更");
                }
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

        // 新首包预连接必须先采用本次唯一实际连接；未验证时不回退旧MAC主动挂载。
        if (wakeGate.needsVerifiedEarlyPeer()) return;
        if (!hasCompleteIdentity()) return;
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

    /** 只选择唯一可验证的本车连接，不将系统列表中第一辆同品牌车覆盖成本车地址。 */
    private String resolveGattAddress() {
        // 车机蓝牙地址可能轮换。先用系统 GATT 连接列表按名称/同MAC候选证据校正，
        // 再回退到已保存地址；否则地址变化后会一直挂不上 RSSI 通道。
        VehicleIdentity verified = findIdentityFromConnectedGatt();
        if (verified != null) {
            adoptConnectedIdentity(verified, VehicleLearningState.CONNECTED, SystemClock.elapsedRealtime());
            return verified.address;
        }
        return identity != null && VehicleLearningState.hasAddress(identity.address) ? identity.address : "";
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
                if (authObservationActive && !geelyConnected) {
                    boolean discovering = gatt.discoverServices();
                    DiagnosticLog.event("Gatt", "AUTH_OBSERVE_CONNECTED", "discover=" + discovering
                            + " addr_id=" + DiagnosticLog.fingerprint(authObservationAddress));
                    appendLog("已挂载官方鉴权观察通道，等待cmdId=5");
                    updateNotificationIfNeeded(true);
                    return;
                }
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
                clearGattAuthObservation("gatt_disconnected", false);
                closeGatt("GATT 连接断开，status=" + status);
            }
            updateNotificationIfNeeded(true);
        }

        /** 服务发现后只开启官方通知特征，不读取或写入鉴权凭证。 */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (gatt != activeGatt || !authObservationActive) return;
            if (status != BluetoothGatt.GATT_SUCCESS) {
                DiagnosticLog.event("Gatt", "AUTH_SERVICE_DISCOVERY_FAILED", "status=" + status);
                clearGattAuthObservation("discovery_failed", false);
                closeGatt("鉴权服务发现失败");
                return;
            }
            enableAuthObserverNotify(gatt);
        }

        /** CCCD写入成功后才认为观察通道真正可用。 */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (gatt != activeGatt || !authObservationActive || descriptor == null
                    || !UUID.fromString(CCCD_UUID).equals(descriptor.getUuid())) return;
            authObserverNotifyReady = status == BluetoothGatt.GATT_SUCCESS;
            DiagnosticLog.event("Gatt", authObserverNotifyReady ? "AUTH_OBSERVER_READY" : "AUTH_CCCD_WRITE_FAILED",
                    "status=" + status + " addr_id=" + DiagnosticLog.fingerprint(authObservationAddress));
        }

        /** 只解析明文头cmdId，payload不落盘、不参与身份或车控编码。 */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (gatt != activeGatt || characteristic == null
                    || !UUID.fromString(AUTH_NOTIFY_UUID).equals(characteristic.getUuid())) return;
            handleKeyNotifyFrame(gatt, characteristic.getValue(), SystemClock.elapsedRealtime());
        }

        /** Android 13+新增带value重载，必须同样处理，否则新系统永远看不到鉴权帧。 */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value) {
            if (gatt != activeGatt || characteristic == null
                    || !UUID.fromString(AUTH_NOTIFY_UUID).equals(characteristic.getUuid())) return;
            handleKeyNotifyFrame(gatt, value, SystemClock.elapsedRealtime());
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
        if (authObservationActive) clearGattAuthObservation(reason, false);
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
                    clearWakeState();
                    clearGattAuthObservation("bluetooth_off", true);
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
            VehicleIdentity verified = findIdentityFromConnectedGatt();
            if (verified == null || !bdAddr.equalsIgnoreCase(verified.address)) return;
            lastObservedBleMac = bdAddr;
            if (dev != null) lastObservedCarDevice = dev;
            wakeGate.onAclConnected(bdAddr, now);
            adoptConnectedIdentity(verified, VehicleLearningState.GATT_EVENT, now);
            // 捕获车机新地址时也必须来自唯一的真实连接，不能凭匿名尾号直接覆盖。
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
            wakeGate.onAclConnected(addr, now);
            KeyNotificationBridgeService.requestFastRescan(this, KeyNotificationBridgeService.FAST_RESCAN_WINDOW_MS);
            maybeStartAuthObserver(device, now);
            // ACL先于官方鉴权到达时只记录握手事实；官方就绪后再学习唯一可见GATT设备。
            if (isOfficialConnected()) {
                VehicleIdentity verified = findIdentityFromConnectedGatt();
                if (verified != null) {
                    if (addr.equalsIgnoreCase(verified.address)) {
                        adoptConnectedIdentity(verified, VehicleLearningState.ACL, now);
                    }
                } else {
                    VehicleIdentity aclVerified = findIdentityFromRecentAclPeer(now);
                    if (aclVerified != null && addr.equalsIgnoreCase(aclVerified.address)) {
                        adoptConnectedIdentity(aclVerified, VehicleLearningState.ACL, now);
                    }
                }
            }
            liveTrace.add(LiveActivityTrace.Step.ACL_CONNECTED, now, "addr_id=" + DiagnosticLog.fingerprint(addr));
            appendLog("✓ 车机底层 ACL 已连接: addr_id=" + DiagnosticLog.fingerprint(addr)
                    + " name_id=" + DiagnosticLog.fingerprint(name));
            ensureGattAttached(now);
        } else {
            appendLog("车机底层 ACL 已断开: addr_id=" + DiagnosticLog.fingerprint(addr)
                    + " name_id=" + DiagnosticLog.fingerprint(name));
            logic.onSignalLost(now);
            wakeGate.onAclDisconnected(addr);
            if (addr.equalsIgnoreCase(authObservationAddress)) clearGattAuthObservation("acl_disconnected", true);
            cancelPendingControl("本车底层蓝牙已断开");
            closeGatt("底层 ACL 断开");
            lastRawRssi = -127;
            lastSampleAtMs = 0L;
            lastZone = "不在车辆附近 (无信号)";
        }
    }

    /** ACL/GATT设备先按强身份校验；未学习时只能跟随当前唯一主候选，不能接管任意吉利连接。 */
    private boolean matchesKnownVehicle(BluetoothDevice device, long now) {
        if (device == null) return false;
        try {
            String address = device.getAddress();
            String name = device.getName();
            if (!VehicleLearningState.hasAddress(address)) return false;
            if (identity == null) return address.equalsIgnoreCase(targetSelector.selectedAddress());
            String tail3 = VehicleBeacon.vinTailFromName(name);
            if (identity.conflicts(name, "", tail3)) return false;
            if (address.equalsIgnoreCase(identity.address) || identity.matchesName(name)) return true;
            GeelyAdvertisement evidence = targetSelector.evidenceFor(address, now);
            if (evidence == null) return false;
            if (identity.matches(evidence)) return true;
            // 已经发起的单目标NAME候选遇到无缓存设备名ACL，也保留握手保护；不因此学习或供给RSSI。
            VehicleIdentity.Match activeMatch = targetSelector.connectionMatch(identity, evidence, now);
            return address.equalsIgnoreCase(wakeGate.activeAddress())
                    && (activeMatch == VehicleIdentity.Match.NAME || activeMatch == VehicleIdentity.Match.HINT);
        } catch (SecurityException denied) {
            return false;
        }
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
                if (liveTrace.lastAt(LiveActivityTrace.Step.SCAN_FOUND) > 0L) {
                    liveTrace.add(LiveActivityTrace.Step.SCAN_LOST, now, "SIGNAL_EXPIRED");
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
            if ((now - lastUpdateAutoCheckAtMs) >= 60000L) {
                lastUpdateAutoCheckAtMs = now;
                UpdateManager.maybeAutoCheck(PassiveKeyService.this);
            }
            if (serviceEnabled && !isScanning && hasBluetoothPermission()
                    && bluetoothAdapter != null && bluetoothAdapter.isEnabled()
                    && (now - lastScanRecoveryAtMs) >= 2000L) {
                lastScanRecoveryAtMs = now;
                startBleScan();
            }
            targetSelector.prune(now);
            boolean notifConnected = isOfficialConnected();
            if (!notifConnected) refreshGattBaseline(now);
            if (notifConnected != geelyConnected) {
                onGeelyConnectionChanged(notifConnected, now);
            }
            if (!notifConnected) {
                GeelyAdvertisement primary = choosePrimaryCandidate(now);
                GeelyAdvertisement lastWake = wakeGate.lastWakeAdvertisement();
                if (primary != null && (lastWake == null || !lastWake.address.equalsIgnoreCase(primary.address))) {
                    handleTargetAdvertisement(primary, now);
                }
            }
            requestScanFilterRefresh();
            processPendingControl(false);
            if (logic.onTick(now, notifConnected, isDriving(now) || isChargingBlocked())) {
                appendLog("🔄 已离开车辆且蓝牙断开满 12s，已重新武装下一次靠近自动解锁");
            }
            ensureGattAttached(now);
            if (notifConnected && (!hasCompleteIdentity() || wakeGate.needsVerifiedEarlyPeer())) {
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
        boolean permissionNeedsAttention = !KeyNotificationBridgeService.isNotificationListenerEnabled(this)
                || !KeyNotificationBridgeService.isListenerConnected();
        Intent intent = new Intent(this,
                permissionNeedsAttention ? PermissionGuideActivity.class : MainActivity.class);
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
            long downMs = KeyNotificationBridgeService.getListenerDownDurationMs();
            text = downMs < PermissionAudit.LISTENER_REBIND_GRACE_MS
                    ? "通知桥接正在恢复 · 授权仍然保留"
                    : "通知桥接未绑定 · 点此打开App修复";
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

    /** 导出时保留最近实时阶段，便于确认页面短暂显示是否与日志一致。 */
    private String liveTraceText(long now) {
        StringBuilder result = new StringBuilder();
        for (LiveActivityTrace.Entry entry : liveTrace.snapshot()) {
            if (result.length() > 0) result.append(" -> ");
            result.append(entry.step.name()).append('@').append(Math.max(0L, now - entry.atMs)).append("ms");
        }
        return result.length() == 0 ? "none" : result.toString();
    }

    /** 四项触发配置单独成行，动作快照和手动导出都能直接解析到当时的用户设置。 */
    private String triggerConfigDiagnostic() {
        return String.format(Locale.US,
                "connect_dbm=%.1f unlock_dbm=%.1f lock_dbm=%.1f dwell_ms=%d",
                connectDbm, unlockDbm, lockDbm, lockDwellMs);
    }

    /** 观测权限、屏幕、省电与内存状态；读取失败只写未知，不改变开关或权限。 */
    private String environmentDiagnostic() {
        try {
            Runtime runtime = Runtime.getRuntime();
            return "run=" + serviceRun + " enabled=" + serviceEnabled + " auto_unlock=" + autoUnlock
                    + " auto_lock=" + autoLock + " connect_dbm=" + connectDbm
                + " wired=" + wiredCharging + " block_charging=" + blockWhenCharging
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
                + " candidates=" + targetSelector.size(now) + " primary_id=" + DiagnosticLog.fingerprint(targetSelector.selectedAddress())
                + " target_age_ms=" + targetSelector.selectionAgeMs(now) + " target_reason=" + targetSelector.lastReason()
                + " filter_mode=" + activeScanMode + " match_reason=" + lastMatchReason
                + " scan_age_ms=" + lastScanAgeMs + " max_scan_age_ms=" + maxScanAgeMs
                + " wake_target_id=" + DiagnosticLog.fingerprint(wakeGate.activeAddress())
                + " absence_age_ms=" + age(now, longAbsenceSessionStartedAtMs)
                + " last_vehicle_age_ms=" + age(now, lastVehicleSignalAtMs)
                + " " + wakeGate.diagnosticSnapshot(now) + " " + diagnostics.snapshot(resetWindow);
    }

    /** 每次挂载和读取都能关联到同一attempt，拒绝与停更原因不再只存在Logcat中。 */
    private String gattDiagnostic(long now) {
        return "gatt_attempt=" + gattAttemptId + " gatt_active=" + (activeGatt != null)
                + " gatt_connected=" + gattConnected + " gatt_addr_id=" + DiagnosticLog.fingerprint(lastSelfGattAddress)
                + " gatt_attempt_age_ms=" + age(now, lastGattAttemptAtMs) + " gatt_status=" + lastGattStatus
                + " auth_observe=" + (gattAuthSuccessObserved ? "CMD5_OBSERVED"
                : authObserverNotifyReady ? "NOTIFY_READY" : authObservationActive ? "OBSERVING" : "IDLE")
                + " auth_cmd5_age_ms=" + age(now, gattAuthSuccessObservedAtMs)
                + " notify_cmd=" + lastGattNotifyCmdId + " notify_age_ms=" + age(now, lastGattNotifyCmdAtMs)
                + " approach_lock_notify_age_ms=" + age(now, lastApproachLockNotifyAtMs)
                + " read_pending=" + gattReadPending + " read_request_age_ms=" + age(now, lastGattReadRequestedAtMs)
                + " read_success_age_ms=" + age(now, lastGattRssiAtMs) + " read_status=" + lastGattReadStatus;
    }

    /** 日志从源头只写身份指纹，任意自定义车名都不会被拼入文件。 */
    private String identityDiagnostic() {
        VehicleIdentity known = identity;
        if (known == null) return "identity=none";
        return "addr_id=" + DiagnosticLog.fingerprint(known.address)
                + " name_id=" + DiagnosticLog.fingerprint(known.name)
                + " identity_id=" + DiagnosticLog.fingerprint(known.stableKey()) + " company=" + known.manufacturerId
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
