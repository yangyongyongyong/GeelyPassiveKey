package com.geely.snc.digitalkey.base.service;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.geely.snc.digitalkey.base.constant.BundleIdKt;
import com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService;
import com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener;
import com.geely.snc.digitalkey.base.dk.business.DkBusinessCenter;
import com.geely.snc.digitalkey.base.dk.business.IDkBleDataListener;
import com.geely.snc.digitalkey.base.dk.business.IDkBleFeatureKt;
import com.geely.snc.digitalkey.base.dk.business.feature.DkFeatureActionDataListener;
import com.geely.snc.digitalkey.base.dk.business.feature.control.DkControlType;
import com.geely.snc.digitalkey.base.dk.constant.DkBleConnectStatus;
import com.geely.snc.digitalkey.base.dk.constant.DkCommConstKt;
import com.geely.snc.digitalkey.base.dk.context.DkContext;
import com.geely.snc.digitalkey.base.dk.context.DkIntentKt;
import com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener;
import com.geely.snc.digitalkey.base.dk.security.DkSecurityCrypto;
import com.geely.snc.digitalkey.base.dk.security.PqcManager;
import com.geely.snc.digitalkey.base.env.BrandCo;
import com.geely.snc.digitalkey.base.env.ConnectMode;
import com.geely.snc.digitalkey.base.env.DigitalKeySdkEnv;
import com.geely.snc.digitalkey.base.env.DigitalKeyVersion;
import com.geely.snc.digitalkey.base.log.DkLogger;
import com.geely.snc.digitalkey.base.notification.DigitalKeyNotification;
import com.geely.snc.digitalkey.base.notification.DigitalKeyNotificationKt;
import com.geely.snc.digitalkey.base.utils.AppUtils;
import com.geely.snc.digitalkey.base.utils.DkPermissionUtilsKt;
import com.geely.snc.digitalkey.base.utils.MmkvUtils;
import com.geely.snc.digitalkey.base.utils.MmkvUtilsKt;
import com.geely.snc.digitalkey.sysble.util.SysBleLogImpl;
import com.geely.snc.log.alisls.BaseUtil;
import com.geely.snc.log.api.LogModuleConfig;
import com.geely.snc.longalive.base.GLWakeUpService;
import com.geely.snc.longalive.base.IGLPullAliveKt;
import com.geely.snc.security.api.GeelySecurityManager;
import com.geely.snc.security.api.IGeelySecurity;
import com.geely.snc.security.logListener;
import com.sensorsdata.analytics.android.sdk.aop.push.PushAutoTrackHelper;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.java_websocket.extensions.ExtensionRequestData;

/* JADX INFO: compiled from: DigitalKeyBgService.kt */
/* JADX INFO: loaded from: /tmp/geely-dex-out/classes1.dex */
@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\u0018\u00002\u00020\u0001:\u0001*B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\bH\u0014J\u0010\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\b\u0010\u0017\u001a\u00020\u0013H\u0002J\b\u0010\u0018\u001a\u00020\u0013H\u0002J\b\u0010\u0019\u001a\u00020\u0013H\u0002J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u0013H\u0016J\b\u0010\u001f\u001a\u00020\u0013H\u0016J\"\u0010 \u001a\u00020!2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020!H\u0016J\u0012\u0010$\u001a\u00020\u00102\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u0010\u0010%\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\b\u0010&\u001a\u00020\u0013H\u0002J\u0010\u0010'\u001a\u00020\u00132\u0006\u0010(\u001a\u00020!H\u0002J\b\u0010)\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcom/geely/snc/digitalkey/base/service/DigitalKeyBgService;", "Landroid/app/Service;", "()V", "TAG", ExtensionRequestData.EMPTY_VALUE, "mConnReceiver", "Landroid/content/BroadcastReceiver;", "mContext", "Landroid/content/Context;", "mDkService", "Lcom/geely/snc/digitalkey/base/dk/IDigitalKeyBleService;", "mNotificationReceiver", "mWakeUpRunnable", "Ljava/lang/Runnable;", "mWalletReceiver", "needInitMmkv", ExtensionRequestData.EMPTY_VALUE, "piScanMacCache", "attachBaseContext", ExtensionRequestData.EMPTY_VALUE, "newBase", "callStartForeground", "isConnect", "initKeepAlive", "initNotification", "initSecurityAndLog", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", ExtensionRequestData.EMPTY_VALUE, "flags", "startId", "onUnbind", "refreshNotification", "registerBroadcast", "resetDkVersion", "version", "unregisterBroadcast", "DkBinder", "digitalkey_base_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
public final class DigitalKeyBgService extends Service {
    private Context mContext;
    private IDigitalKeyBleService mDkService;
    private final String TAG = "DigitalKeyBgService";
    private boolean needInitMmkv = true;
    private String piScanMacCache = ExtensionRequestData.EMPTY_VALUE;
    private final Runnable mWakeUpRunnable = new Runnable() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            DigitalKeyBgService.m783mWakeUpRunnable$lambda1(this.f$0);
        }
    };
    private final BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService$mNotificationReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            IDigitalKeyBleService iDigitalKeyBleService;
            PushAutoTrackHelper.onBroadcastReceiver(this, context, intent);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(intent, "intent");
            try {
                if (Intrinsics.areEqual(DigitalKeyNotificationKt.ACTION_NOTIFICATION_CONTROL, intent.getAction()) && (iDigitalKeyBleService = this.this$0.mDkService) != null) {
                    int intExtra = intent.getIntExtra(DigitalKeyNotificationKt.INTENT_EXTRA_CMD, DkControlType.Lock.getAct());
                    DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService notification click cmd value: ", Integer.valueOf(intExtra)), 1, null);
                    iDigitalKeyBleService.doFeatureActionData(IDkBleFeatureKt.DK_FEATURE_FAST_CTRL, 272, intExtra, new DkFeatureActionDataListener() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService$mNotificationReceiver$1$onReceive$1$1
                        @Override // com.geely.snc.digitalkey.base.dk.business.feature.DkFeatureActionDataListener, com.geely.snc.digitalkey.base.dk.business.IDkBleDataListener
                        public void onResult(int code, int action, int platform, byte[] info) {
                            Intrinsics.checkNotNullParameter(info, "info");
                            DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService notification click action: ", Integer.valueOf(action)), 1, null);
                        }
                    });
                }
            } catch (RemoteException e) {
                DkLogger.error$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("NotificationControl:", e.getMessage()), 1, null);
            }
        }
    };
    private final BroadcastReceiver mConnReceiver = new BroadcastReceiver() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService$mConnReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            PushAutoTrackHelper.onBroadcastReceiver(this, context, intent);
            DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("mConnReceiver action: ", intent == null ? null : intent.getAction()), 1, null);
            if (Intrinsics.areEqual(DkIntentKt.ACTION_DK_BLE_CONNECTED, intent == null ? null : intent.getAction())) {
                this.this$0.refreshNotification(true);
                return;
            }
            if (Intrinsics.areEqual(DkIntentKt.ACTION_DK_BLE_DISCONNECTED, intent != null ? intent.getAction() : null)) {
                this.this$0.refreshNotification(false);
                this.this$0.piScanMacCache = ExtensionRequestData.EMPTY_VALUE;
            }
        }
    };
    private final BroadcastReceiver mWalletReceiver = new BroadcastReceiver() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService$mWalletReceiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            PushAutoTrackHelper.onBroadcastReceiver(this, context, intent);
            DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("GLWakeUpService WalletReceiver: ", intent == null ? null : intent.getAction()), 1, null);
            if (Intrinsics.areEqual(IGLPullAliveKt.ACTION_WALLET_BIND, intent == null ? null : intent.getAction())) {
                GLWakeUpService.INSTANCE.stopKeepAlive();
                return;
            }
            if (Intrinsics.areEqual(IGLPullAliveKt.ACTION_WALLET_UNBIND, intent != null ? intent.getAction() : null)) {
                GLWakeUpService.INSTANCE.startKeepAlive();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: mWakeUpRunnable$lambda-1, reason: not valid java name */
    public static final void m783mWakeUpRunnable$lambda1(DigitalKeyBgService digitalKeyBgService) {
        Intrinsics.checkNotNullParameter(digitalKeyBgService, "this$0");
        try {
            if (digitalKeyBgService.mContext == null) {
                digitalKeyBgService.mContext = digitalKeyBgService.getApplicationContext();
            }
            if (DkPermissionUtilsKt.checkBlePermission(digitalKeyBgService.mContext)) {
                DkLogger.INSTANCE.info(DkCommConstKt.TAG_COMMON, "--->Service execute dkHandler: " + digitalKeyBgService.mDkService + "; isbind:" + GLWakeUpService.INSTANCE.isWalletBind());
                if (GLWakeUpService.INSTANCE.isWalletBind()) {
                    GLWakeUpService.INSTANCE.stopKeepAlive();
                    return;
                }
                IDigitalKeyBleService iDigitalKeyBleService = digitalKeyBgService.mDkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                boolean zIsAutoConnect = DkBusinessCenter.INSTANCE.isAutoConnect();
                boolean z = iDigitalKeyBleService.getBleConnectStatus() == DkBleConnectStatus.STATUS_DISCONNECTED.getStatus();
                String vehicleVin = DkBusinessCenter.INSTANCE.getVehicleVin();
                DkLogger.INSTANCE.info(DkCommConstKt.TAG_COMMON, "Service isAutoConnect: " + zIsAutoConnect + ", isNeedConnect: " + z + " ,vin: " + vehicleVin);
                if (zIsAutoConnect && z && !TextUtils.isEmpty(vehicleVin)) {
                    DkLogger.INSTANCE.releaseLog(Intrinsics.stringPlus("DigitalKeyBgService keepalive autoConnect: ", vehicleVin));
                    iDigitalKeyBleService.connectVehicle(vehicleVin, ConnectMode.AUTO.getValue());
                    return;
                }
                return;
            }
            DkLogger.INSTANCE.info(DkCommConstKt.TAG_COMMON, "--->permission denied");
        } catch (Exception e) {
            DkLogger.error$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("Service except: ", e.getMessage()), 1, null);
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.i(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onCreate: ", getApplicationContext()));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onCreate: ", getApplicationContext()), 1, null);
        this.mContext = getApplicationContext();
        DkContext.INSTANCE.setSdkVersion(DigitalKeySdkEnv.INSTANCE.getVersion());
        AppUtils appUtils = AppUtils.INSTANCE;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
        this.needInitMmkv = appUtils.hasMmkvMode(applicationContext);
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService meta data MMKV is init:", Boolean.valueOf(this.needInitMmkv)), 1, null);
        initSecurityAndLog();
        if (this.mDkService == null) {
            DkBusinessCenter dkBusinessCenter = DkBusinessCenter.INSTANCE;
            Context applicationContext2 = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext2, "applicationContext");
            this.mDkService = dkBusinessCenter.init(applicationContext2);
        }
        PqcManager.INSTANCE.initPqc(this.mContext);
        initKeepAlive();
        initNotification();
        registerBroadcast();
    }

    @Override // android.app.Service, android.content.ContextWrapper
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService attachBaseContext: ", getApplicationContext()), 1, null);
        Context applicationContext = getApplicationContext();
        this.mContext = applicationContext;
        if (applicationContext == null) {
            return;
        }
        AppUtils appUtils = AppUtils.INSTANCE;
        Context applicationContext2 = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext2, "applicationContext");
        this.needInitMmkv = appUtils.hasMmkvMode(applicationContext2);
        MmkvUtils.INSTANCE.initMMKV(applicationContext, this.needInitMmkv);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        String address;
        PushAutoTrackHelper.onServiceStartCommand(this, intent, flags, startId);
        Log.i(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onStartCommand intent: ", intent));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onStartCommand intent: ", intent), 1, null);
        IDigitalKeyBleService iDigitalKeyBleService = this.mDkService;
        callStartForeground(iDigitalKeyBleService != null && iDigitalKeyBleService.getBleConnectStatus() == DkBleConnectStatus.STATUS_CONNECTED.getStatus());
        resetDkVersion(MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_VERSION, DigitalKeyVersion.VERSION_20.getValue()));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("GLWakeUpService isWalletBind:", Boolean.valueOf(GLWakeUpService.INSTANCE.isWalletBind())), 1, null);
        if (!GLWakeUpService.INSTANCE.isWalletBind()) {
            GLWakeUpService.INSTANCE.startKeepAlive();
        }
        if (intent != null) {
            DkLogger.INSTANCE.info(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onStartCommand intentAct:", intent.getAction()));
            if (Intrinsics.areEqual(DkCommConstKt.PENDINGINTENT_SCAN, intent.getAction())) {
                List<ScanResult> list = (List) intent.getSerializableExtra("android.bluetooth.le.extra.LIST_SCAN_RESULT");
                List list2 = list;
                if (!(list2 == null || list2.isEmpty())) {
                    for (ScanResult scanResult : list) {
                        BluetoothDevice device = scanResult.getDevice();
                        if (device == null || (address = device.getAddress()) == null) {
                            address = ExtensionRequestData.EMPTY_VALUE;
                        }
                        if (!Intrinsics.areEqual(address, this.piScanMacCache)) {
                            this.piScanMacCache = address;
                            DkLogger.INSTANCE.info(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onStartCommand BleScan PiscanResult:", scanResult));
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(BundleIdKt.CONNECTBLE_SCANRESULT, scanResult);
                            IDigitalKeyBleService iDigitalKeyBleService2 = this.mDkService;
                            if (!(iDigitalKeyBleService2 != null && iDigitalKeyBleService2.connectPiScanResult(bundle))) {
                                this.piScanMacCache = ExtensionRequestData.EMPTY_VALUE;
                            }
                        } else {
                            DkLogger.INSTANCE.info(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onStartCommand BleScan Repeat PiscanResult:", scanResult));
                        }
                    }
                }
            }
        }
        return 1;
    }

    private final void resetDkVersion(int version) {
        DkBusinessCenter.INSTANCE.setVersion(version);
    }

    private final void callStartForeground(boolean isConnect) {
        Notification notificationCreate = DigitalKeyNotification.INSTANCE.create(this.mContext, isConnect, DigitalKeySdkEnv.INSTANCE.getBrand(MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_BRAND, 0)));
        try {
            DkLogger.info$default(DkLogger.INSTANCE, null, "callStartForeground: isConnect=" + isConnect + ", " + notificationCreate, 1, null);
            if (Build.VERSION.SDK_INT < 29) {
                startForeground(5, notificationCreate);
            } else if (notificationCreate != null) {
                startForeground(5, notificationCreate, 16);
            }
        } catch (Exception e) {
            DkLogger.error$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("---->callStartForeground() : ", e.getMessage()), 1, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void refreshNotification(boolean isConnect) {
        BrandCo brand = DigitalKeySdkEnv.INSTANCE.getBrand(MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_BRAND, 0));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("refreshNotification brandCo:", brand), 1, null);
        DigitalKeyNotification.INSTANCE.refresh(this.mContext, isConnect, brand);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        int i = MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_STOP_SERVICE, 0);
        Log.i(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onDestroy isUnbind: ", Integer.valueOf(i)));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onDestroy isUnbind: ", Integer.valueOf(i)), 1, null);
        if (i == 1) {
            GLWakeUpService.INSTANCE.stopKeepAlive();
            GLWakeUpService.INSTANCE.destroyKeepAlive();
        }
        unregisterBroadcast();
        try {
            IDigitalKeyBleService iDigitalKeyBleService = this.mDkService;
            if (iDigitalKeyBleService == null) {
                return;
            }
            iDigitalKeyBleService.disconnectVehicle(ExtensionRequestData.EMPTY_VALUE);
            this.mDkService = null;
            DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onDestroy killprocess: ", this.mDkService), 1, null);
            Process.killProcess(Process.myPid());
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0082  */
    private final void initSecurityAndLog() {
        String string = MmkvUtils.INSTANCE.getString(MmkvUtilsKt.MMKV_DK_OPERATE, ExtensionRequestData.EMPTY_VALUE);
        String string2 = MmkvUtils.INSTANCE.getString(MmkvUtilsKt.MMKV_DK_LICENSE, ExtensionRequestData.EMPTY_VALUE);
        String string3 = MmkvUtils.INSTANCE.getString(MmkvUtilsKt.MMKV_DK_AREATYPE, ExtensionRequestData.EMPTY_VALUE);
        String string4 = MmkvUtils.INSTANCE.getString(MmkvUtilsKt.MMKV_DK_ENV, ExtensionRequestData.EMPTY_VALUE);
        DkLogger.INSTANCE.info("DK Security", Intrinsics.stringPlus("license:", string2));
        try {
            IGeelySecurity companion = GeelySecurityManager.Companion.getInstance();
            Context applicationContext = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
            companion.initSecuritySdk(applicationContext, string2, true);
        } catch (Exception e) {
            DkLogger.INSTANCE.error(this.TAG, Intrinsics.stringPlus("initLog error: ", e.getMessage()));
        }
        try {
            BrandCo brandCo = MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_BRAND, 6) == 6 ? BrandCo.ZEEKR : BrandCo.GEELY;
            String string5 = MmkvUtils.INSTANCE.getString(MmkvUtilsKt.MMKV_DK_VIN, ExtensionRequestData.EMPTY_VALUE);
            if (string == null) {
                string = "common";
            } else {
                if (!(string.length() > 0)) {
                    string = "common";
                }
            }
            Context context = this.mContext;
            if (context != null) {
                DkLogger.INSTANCE.initLog(context, brandCo);
                DkLogger.INSTANCE.initModuleConfig(new LogModuleConfig.Builder().moduleName(DkLogger.INSTANCE.getMODULE_NAME()).operator(Intrinsics.stringPlus(string, ExtensionRequestData.EMPTY_VALUE)).vin(Intrinsics.stringPlus(string5, ExtensionRequestData.EMPTY_VALUE)).sdkVersion(DkContext.INSTANCE.isDkVersion20() ? "DK20 Service" : "DK15 Service").areaType(Intrinsics.stringPlus(string3, ExtensionRequestData.EMPTY_VALUE)).envType(Intrinsics.stringPlus(string4, ExtensionRequestData.EMPTY_VALUE)).build());
            }
            DkLogger.INSTANCE.releaseLog("DKSDK version:1.1.10.3");
            GeelySecurityManager.Companion.getInstance().initLog(new logListener() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService.initSecurityAndLog.2
                public void logSecurity(String tag, String msg) {
                    Intrinsics.checkNotNullParameter(tag, BaseUtil.TAG);
                    Intrinsics.checkNotNullParameter(msg, "msg");
                    DkLogger.info$default(DkLogger.INSTANCE, null, "Security " + tag + ": " + msg, 1, null);
                }
            });
            SysBleLogImpl.getInstance().setLogCommon(new SysBleLogImpl.ISysBleLogCommon() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService.initSecurityAndLog.3
                @Override // com.geely.snc.digitalkey.sysble.util.SysBleLogImpl.ISysBleLogCommon
                public void ilog_info(String tag, String msg) {
                    DkLogger.info$default(DkLogger.INSTANCE, null, "Sysble " + ((Object) tag) + ": " + ((Object) msg), 1, null);
                }
            });
        } catch (Exception e2) {
            Log.e("DigitalKeyBgService", Intrinsics.stringPlus("initLog error: ", e2.getMessage()));
        }
        DkContext.INSTANCE.setDeviceIdCache(DkSecurityCrypto.INSTANCE.getDeviceID());
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("initSecurity deviceIdCache:", DkContext.INSTANCE.getDeviceIdCache()), 1, null);
    }

    private final void initKeepAlive() {
        int i = MmkvUtils.INSTANCE.getInt(MmkvUtilsKt.MMKV_DK_BRAND, BrandCo.ZEEKR.getCompany());
        GLWakeUpService gLWakeUpService = GLWakeUpService.INSTANCE;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "this.applicationContext");
        gLWakeUpService.init(applicationContext, this.mWakeUpRunnable, i, this.needInitMmkv);
    }

    private final void initNotification() {
        try {
            IDigitalKeyBleService iDigitalKeyBleService = this.mDkService;
            if (iDigitalKeyBleService == null) {
                return;
            }
            iDigitalKeyBleService.setConnectionListener("DkBgServiceNotification", new DigitalKeyConnectionListener() { // from class: com.geely.snc.digitalkey.base.service.DigitalKeyBgService.initNotification.1
                @Override // com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener, com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener
                public void onConnectFail(int code, String exception) {
                    DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DkBgServiceNotification onConnectFail: ", Integer.valueOf(code)), 1, null);
                }

                @Override // com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener, com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener
                public void onConnectFailExtra(int code, String exception, int refreshMode) {
                    DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DkBgServiceNotification onConnectFailExtra: ", Integer.valueOf(code)), 1, null);
                }

                @Override // com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener, com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener
                public void onConnected(String vin) {
                    DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DkBgServiceNotification onConnected: ", vin), 1, null);
                }

                @Override // com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener, com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener
                public void onConnecting() {
                    DkLogger.info$default(DkLogger.INSTANCE, null, "DkBgServiceNotification onConnecting ", 1, null);
                }

                @Override // com.geely.snc.digitalkey.base.dk.listener.DigitalKeyConnectionListener, com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener
                public void onDisconnected() {
                    DkLogger.info$default(DkLogger.INSTANCE, null, "DkBgServiceNotification onDisconnected ", 1, null);
                }
            });
        } catch (Exception unused) {
        }
    }

    private final void registerBroadcast() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DigitalKeyNotificationKt.ACTION_NOTIFICATION_CONTROL);
            if (Build.VERSION.SDK_INT >= 34) {
                Context context = this.mContext;
                if (context != null) {
                    context.registerReceiver(this.mNotificationReceiver, intentFilter, 4);
                }
            } else {
                Context context2 = this.mContext;
                if (context2 != null) {
                    context2.registerReceiver(this.mNotificationReceiver, intentFilter);
                }
            }
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction(DkIntentKt.ACTION_DK_BLE_CONNECTED);
            intentFilter2.addAction(DkIntentKt.ACTION_DK_BLE_DISCONNECTED);
            if (Build.VERSION.SDK_INT >= 34) {
                Context context3 = this.mContext;
                if (context3 != null) {
                    context3.registerReceiver(this.mConnReceiver, intentFilter2, 4);
                }
            } else {
                Context context4 = this.mContext;
                if (context4 != null) {
                    context4.registerReceiver(this.mConnReceiver, intentFilter2);
                }
            }
            IntentFilter intentFilter3 = new IntentFilter();
            intentFilter3.addAction(IGLPullAliveKt.ACTION_WALLET_BIND);
            intentFilter3.addAction(IGLPullAliveKt.ACTION_WALLET_UNBIND);
            if (Build.VERSION.SDK_INT >= 34) {
                Context context5 = this.mContext;
                if (context5 == null) {
                    return;
                }
                context5.registerReceiver(this.mWalletReceiver, intentFilter3, 4);
                return;
            }
            Context context6 = this.mContext;
            if (context6 == null) {
                return;
            }
            context6.registerReceiver(this.mWalletReceiver, intentFilter3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void unregisterBroadcast() {
        try {
            Context context = this.mContext;
            if (context != null) {
                context.unregisterReceiver(this.mNotificationReceiver);
            }
            Context context2 = this.mContext;
            if (context2 != null) {
                context2.unregisterReceiver(this.mConnReceiver);
            }
            Context context3 = this.mContext;
            if (context3 == null) {
                return;
            }
            context3.unregisterReceiver(this.mWalletReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.i(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onBind: ", intent));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onBind: ", intent), 1, null);
        resetDkVersion(DigitalKeySdkEnv.INSTANCE.getDkVersion());
        return new DkBinder(this.mDkService);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        Log.i(this.TAG, Intrinsics.stringPlus("DigitalKeyBgService onUnbind: ", intent));
        DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("DigitalKeyBgService onUnbind: ", intent), 1, null);
        return super.onUnbind(intent);
    }

    /* JADX INFO: compiled from: DigitalKeyBgService.kt */
    @Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0012\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u0012\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\u001a\u0010\u0011\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\nH\u0016J\u001c\u0010\u0014\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\u0012\u0010\u0015\u001a\u00020\u00062\b\u0010\u0012\u001a\u0004\u0018\u00010\bH\u0016J.\u0010\u0016\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\fH\u0016J,\u0010\u001a\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\n2\b\u0010\u0019\u001a\u0004\u0018\u00010\fH\u0016J\b\u0010\u001c\u001a\u00020\nH\u0016J\b\u0010\u001d\u001a\u00020\nH\u0016J\b\u0010\u001e\u001a\u00020\bH\u0016J\b\u0010\u001f\u001a\u00020\nH\u0016J\u0018\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u0016J\b\u0010\"\u001a\u00020\u000eH\u0016J\b\u0010#\u001a\u00020\u000eH\u0016J\u0012\u0010$\u001a\u00020\u000e2\b\u0010%\u001a\u0004\u0018\u00010\bH\u0016J\u0018\u0010&\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010'\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u000eH\u0016J\u0010\u0010)\u001a\u00020\u00062\u0006\u0010*\u001a\u00020\u000eH\u0016J\u001c\u0010+\u001a\u00020\u00062\b\u0010,\u001a\u0004\u0018\u00010\b2\b\u0010-\u001a\u0004\u0018\u00010.H\u0016J\u0010\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\nH\u0016J\b\u00101\u001a\u00020\u0006H\u0016J \u00102\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\b2\u0006\u00103\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u00064"}, d2 = {"Lcom/geely/snc/digitalkey/base/service/DigitalKeyBgService$DkBinder;", "Lcom/geely/snc/digitalkey/base/dk/IDigitalKeyBleService$Stub;", "dkService", "Lcom/geely/snc/digitalkey/base/dk/IDigitalKeyBleService;", "(Lcom/geely/snc/digitalkey/base/dk/IDigitalKeyBleService;)V", "addFearureListener", ExtensionRequestData.EMPTY_VALUE, "featureName", ExtensionRequestData.EMPTY_VALUE, "action", ExtensionRequestData.EMPTY_VALUE, "dataListener", "Lcom/geely/snc/digitalkey/base/dk/business/IDkBleDataListener;", "connectPiScanResult", ExtensionRequestData.EMPTY_VALUE, "args", "Landroid/os/Bundle;", "connectVehicle", "vin", "connectMode", "connectVehicleBle", "disconnectVehicle", "doFeatureActionBytesData", "data", ExtensionRequestData.EMPTY_VALUE, "actionListener", "doFeatureActionData", "value", "getAntiAbuse", "getBleConnectStatus", "getUsedDkId", "getVersion", "hasDigitalKey", "userId", "hasVehiclePaired", "isGeelyOneVersion", "isVehicleConnected", "dkid", "removeFearureListener", "scanVehicle", "autoConnect", "setAutoConnect", "isAuto", "setConnectionListener", "key", "listener", "Lcom/geely/snc/digitalkey/base/dk/IDigitalKeyConnectionListener;", "setPairMode", "mode", "stopScanVehicle", "updateContextInfo", "vehiclePlatform", "digitalkey_base_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
    public static final class DkBinder extends IDigitalKeyBleService.Stub {
        private final IDigitalKeyBleService dkService;

        public DkBinder(IDigitalKeyBleService iDigitalKeyBleService) {
            this.dkService = iDigitalKeyBleService;
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public int getVersion() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                Integer numValueOf = iDigitalKeyBleService == null ? null : Integer.valueOf(iDigitalKeyBleService.getVersion());
                if (numValueOf != null) {
                    return numValueOf.intValue();
                }
                return DigitalKeyVersion.VERSION_20.getValue();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, getVersion()", 1, null);
                return DigitalKeyVersion.VERSION_20.getValue();
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void updateContextInfo(String userId, String vehiclePlatform, String vin) {
            Intrinsics.checkNotNullParameter(userId, "userId");
            Intrinsics.checkNotNullParameter(vehiclePlatform, "vehiclePlatform");
            Intrinsics.checkNotNullParameter(vin, "vin");
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService != null) {
                    iDigitalKeyBleService.updateContextInfo(userId, vehiclePlatform, vin);
                }
                DkLogger.INSTANCE.initModuleConfig(new LogModuleConfig.Builder().moduleName(DkLogger.INSTANCE.getMODULE_NAME()).sdkVersion(DkContext.INSTANCE.isDkVersion20() ? "DK20 Service" : "DK15 Service").uid(Intrinsics.stringPlus(userId, ExtensionRequestData.EMPTY_VALUE)).vin(Intrinsics.stringPlus(vin, ExtensionRequestData.EMPTY_VALUE)).build());
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, updateContextInfo()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void setAutoConnect(boolean isAuto) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.setAutoConnect(isAuto);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, setAutoConnect()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void setConnectionListener(String key, IDigitalKeyConnectionListener listener) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.setConnectionListener(key, listener);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, setConnectionListener()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void scanVehicle(boolean autoConnect) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.scanVehicle(autoConnect);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, scanVehicle()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void stopScanVehicle() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.stopScanVehicle();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, stopScanVehicle()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean connectVehicle(String vin, int connectMode) {
            DkLogger.INSTANCE.initModuleConfig(new LogModuleConfig.Builder().moduleName(DkLogger.INSTANCE.getMODULE_NAME()).vin(Intrinsics.stringPlus(vin, ExtensionRequestData.EMPTY_VALUE)).build());
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return false;
                }
                return iDigitalKeyBleService.connectVehicle(vin, connectMode);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, connectVehicle()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean connectVehicleBle(String vin, Bundle args) {
            if (args == null) {
                return false;
            }
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return false;
                }
                return iDigitalKeyBleService.connectVehicleBle(vin, args);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, connectVehicleBle()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean connectPiScanResult(Bundle args) {
            if (args == null) {
                return false;
            }
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return false;
                }
                return iDigitalKeyBleService.connectPiScanResult(args);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, connectPiScanResult()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void disconnectVehicle(String vin) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.disconnectVehicle(vin);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, disconnectVehicle()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean isVehicleConnected(String dkid) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                return iDigitalKeyBleService != null && iDigitalKeyBleService.isVehicleConnected(dkid);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, isVehicleConnected()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public int getBleConnectStatus() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return 0;
                }
                return iDigitalKeyBleService.getBleConnectStatus();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, getBleConnectStatus()", 1, null);
                return 0;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean hasVehiclePaired() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                return iDigitalKeyBleService != null && iDigitalKeyBleService.hasVehiclePaired();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, hasVehiclePaired()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean isGeelyOneVersion() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                return iDigitalKeyBleService != null && iDigitalKeyBleService.isGeelyOneVersion();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, isGeelyOneVersion()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public boolean hasDigitalKey(String userId, String vin) {
            Intrinsics.checkNotNullParameter(userId, "userId");
            Intrinsics.checkNotNullParameter(vin, "vin");
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                return iDigitalKeyBleService != null && iDigitalKeyBleService.hasDigitalKey(userId, vin);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, hasDigitalKey()", 1, null);
                return false;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public String getUsedDkId() {
            String usedDkId;
            try {
                return (this.dkService == null || (usedDkId = getUsedDkId()) == null) ? ExtensionRequestData.EMPTY_VALUE : usedDkId;
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, getUsedDkId()", 1, null);
                return ExtensionRequestData.EMPTY_VALUE;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public int getAntiAbuse() {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return 0;
                }
                return iDigitalKeyBleService.getAntiAbuse();
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, getAntiAbuse", 1, null);
                return 0;
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void doFeatureActionData(String featureName, int action, int value, IDkBleDataListener actionListener) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.doFeatureActionData(featureName, action, value, actionListener);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, doFeatureActionData()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void doFeatureActionBytesData(String featureName, int action, byte[] data, IDkBleDataListener actionListener) {
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.doFeatureActionBytesData(featureName, action, data, actionListener);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, doFeatureActionBytesData()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void addFearureListener(String featureName, int action, IDkBleDataListener dataListener) {
            Intrinsics.checkNotNullParameter(featureName, "featureName");
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.addFearureListener(featureName, action, dataListener);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, setFearureListener()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void removeFearureListener(String featureName, int action) {
            Intrinsics.checkNotNullParameter(featureName, "featureName");
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.removeFearureListener(featureName, action);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, setFearureListener()", 1, null);
            }
        }

        @Override // com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService
        public void setPairMode(int mode) {
            DkLogger.info$default(DkLogger.INSTANCE, null, Intrinsics.stringPlus("Service setPairMode: ", Integer.valueOf(mode)), 1, null);
            try {
                IDigitalKeyBleService iDigitalKeyBleService = this.dkService;
                if (iDigitalKeyBleService == null) {
                    return;
                }
                iDigitalKeyBleService.setPairMode(mode);
            } catch (Exception unused) {
                DkLogger.info$default(DkLogger.INSTANCE, null, "DigitalKeyBgService DeadObjectException is caught, setPairMode()", 1, null);
            }
        }
    }
}
