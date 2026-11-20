package com.ecarx.blekey.thirdadapter.service;

import android.app.Service;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.ecarx.blekey.thirdadapter.api.ThirdAdapterApi;
import com.ecarx.blekey.thirdadapter.base.api.AliveConstants;
import com.ecarx.blekey.thirdadapter.base.notification.NotificationHelper;
import com.ecarx.blekey.thirdadapter.base.notification.NotifyConfig;
import com.ecarx.blekey.thirdadapter.base.utils.AliveLogUtil;
import com.ecarx.blekey.thirdadapter.basedata.CarInfoRepositoryImpl;
import com.ecarx.blekey.thirdadapter.basedata.ConnectDeviceRepositoryImpl;
import com.ecarx.blekey.thirdadapter.basedomain.RetryWithDelay;
import com.ecarx.blekey.thirdadapter.miuidata.impl.AuthorizaMIProtocolImpl;
import com.ecarx.blekey.thirdadapter.miuidata.impl.BindMiRepositoryImpl;
import com.ecarx.blekey.thirdadapter.miuidata.impl.MiConnectDeviceProtocolImpl;
import com.ecarx.blekey.thirdadapter.miuidata.impl.MiPhoneProtocolImpl;
import com.ecarx.blekey.thirdadapter.miuidomain.impl.MiCarLinkInteractorImpl;
import com.ecarx.blekey.thirdadapter.oppodata.utils.Constants;
import com.ecarx.blekey.thirdadapter.report.EVENT;
import com.ecarx.blekey.thirdadapter.report.OPERATORCODE;
import com.ecarx.blekey.thirdadapter.report.ReportFactory;
import com.ecarx.blekey.thirdadapter.report.bean.ConnectDeviceOperator;
import com.ecarx.blekey.thirdadapter.utils.JsonParserUtils;
import com.ecarx.blekey.thirdadapter.utils.cache.DefaultStorage;
import com.sensorsdata.analytics.android.sdk.aop.push.PushAutoTrackHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;

/* JADX INFO: loaded from: /tmp/geely-dex-out/classes12.dex */
public class MiuiService extends Service {
    private MiCarLinkInteractorImpl mCarLinkInteractor;
    private DefaultStorage mDefaultStorage;
    private Disposable mDisposable;
    private RetryWithDelay mRetryWithDelay;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.TypeCastException */
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) throws TypeCastException {
        PushAutoTrackHelper.onServiceStartCommand(this, intent, i, i2);
        try {
            AliveLogUtil.i(AliveConstants.SDK_TAG, "MiuiService onStartCommand");
            stg();
            String stringExtra = intent.getStringExtra("setCarId");
            ScanResult scanResult = (ScanResult) intent.getParcelableExtra("ScanResult");
            AliveLogUtil.i(AliveConstants.SDK_TAG, "小米服务发现设备： " + stringExtra + ", scResult: " + scanResult + ",current thread:" + Thread.currentThread().getName());
            if (!TextUtils.isEmpty(stringExtra) && scanResult != null) {
                if (scanResult.getClass().getClassLoader() == null) {
                    AliveLogUtil.i(AliveConstants.SDK_TAG, "MiuiService onStartCommand classLoader is null");
                }
                this.mCarLinkInteractor.connectDevice(stringExtra, JsonParserUtils.parcelable2String(scanResult));
                ReportFactory.INSTANCE.getInstance().reportEvent(EVENT.ON_CAR_FOUND, OPERATORCODE.CODE_ON_CARFOUND, scanResult.toString(), true);
                ReportFactory.INSTANCE.getInstance().reportEvent(EVENT.ALIVE_CONNECT_DEVICE, OPERATORCODE.CODE_ON_CONNECT_DEVICE1, scanResult.toString(), false);
                ConnectDeviceOperator connectDeviceOperator = new ConnectDeviceOperator();
                connectDeviceOperator.setConnectMode(2);
                ReportFactory.INSTANCE.getInstance().reportEvent(EVENT.ALIVE_CONNECT_DEVICE, OPERATORCODE.CODE_ON_CONNECT_DEVICE2, JsonParserUtils.object2String(connectDeviceOperator), false);
                ReportFactory.INSTANCE.getInstance().reportEvent(EVENT.ALIVE_UNLOCK, OPERATORCODE.CODE_ALIVE_UNLOCK1, scanResult.toString(), false);
                ReportFactory.INSTANCE.getInstance().reportEvent(EVENT.ALIVE_UNLOCK, OPERATORCODE.CODE_ALIVE_UNLOCK2, "", false);
                return 2;
            }
            throw new NullPointerException("小米扫描的vin码或者scanResult为空！");
        } catch (NullPointerException e) {
            AliveLogUtil.e(AliveConstants.SDK_TAG, "MiuiService onStartCommand error" + e.getLocalizedMessage());
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.TypeCastException */
    @Override // android.app.Service
    public void onCreate() throws TypeCastException {
        try {
            super.onCreate();
            this.mDefaultStorage = new DefaultStorage(this, DefaultStorage.SP_NAME);
            AliveLogUtil.i(AliveConstants.SDK_TAG, "MiuiService onCreate");
            stg();
        } catch (Exception e) {
            AliveLogUtil.e(AliveConstants.SDK_TAG, "MiuiService onCreate error" + e.getLocalizedMessage());
        }
        this.mCarLinkInteractor = new MiCarLinkInteractorImpl(new ConnectDeviceRepositoryImpl(), new CarInfoRepositoryImpl(this), new AuthorizaMIProtocolImpl(this), new BindMiRepositoryImpl(this), new MiConnectDeviceProtocolImpl(this), new MiPhoneProtocolImpl(this));
        RetryWithDelay retryWithDelay = new RetryWithDelay(100, 50, "小米保活服务等待注册配置信息");
        this.mRetryWithDelay = retryWithDelay;
        retryWithDelay.isStopRetry = false;
        Observable.create(new ObservableOnSubscribe<Boolean>() { // from class: com.ecarx.blekey.thirdadapter.service.MiuiService.2
            public void subscribe(ObservableEmitter<Boolean> observableEmitter) throws Exception {
                boolean zRegisterConfig = MiuiService.this.mCarLinkInteractor.registerConfig();
                AliveLogUtil.i(AliveConstants.SDK_TAG, "Register config to mi: " + zRegisterConfig + ",current thread:" + Thread.currentThread().getName());
                if (zRegisterConfig) {
                    observableEmitter.onNext(Boolean.valueOf(zRegisterConfig));
                } else {
                    observableEmitter.onError(new Throwable("Register config to mi failure!"));
                }
            }
        }).retryWhen(this.mRetryWithDelay).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() { // from class: com.ecarx.blekey.thirdadapter.service.MiuiService.1
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
                MiuiService.this.mDisposable = disposable;
            }

            public void onNext(Boolean bool) {
                if (MiuiService.this.mDisposable != null && !MiuiService.this.mDisposable.isDisposed()) {
                    MiuiService.this.mDisposable.dispose();
                }
                AliveLogUtil.i("MiuiService Register config to mi success!");
            }

            public void onError(Throwable th) {
                if (MiuiService.this.mDisposable != null && !MiuiService.this.mDisposable.isDisposed()) {
                    MiuiService.this.mDisposable.dispose();
                }
                AliveLogUtil.i("MiuiService Register config to mi failure, stop register!");
            }
        });
        ThirdAdapterApi.getInstance(this).getMiuiAdapter().refreshConfigSet();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        RetryWithDelay retryWithDelay = this.mRetryWithDelay;
        if (retryWithDelay != null) {
            retryWithDelay.isStopRetry = true;
        }
        AliveLogUtil.i(AliveConstants.SDK_TAG, stackLog("MiuiService onDestroy"));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: kotlin.TypeCastException */
    private void stg() throws TypeCastException {
        String string = this.mDefaultStorage.getString(Constants.MI_SERVICE_NOTIFY);
        AliveLogUtil.i(AliveConstants.SDK_TAG, "The notifyConfig of output is:" + string);
        NotifyConfig notifyConfig = !TextUtils.isEmpty(string) ? (NotifyConfig) JsonParserUtils.string2Object(string, NotifyConfig.class) : null;
        if (notifyConfig == null) {
            notifyConfig = new NotifyConfig();
            notifyConfig.setNotifyId(NotificationHelper.ID);
            notifyConfig.setTitle("");
            notifyConfig.setContent("数字钥匙服务启动");
        }
        AliveLogUtil.i(AliveConstants.SDK_TAG, "MiuiService updateNotification");
        NotificationHelper.updateNotification(notifyConfig, this);
    }

    private String stackLog(String str) {
        StringBuilder sb = new StringBuilder(str + "\n");
        try {
            sb.append("调用堆栈日志：\n" + Log.getStackTraceString(new Throwable()));
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("打印堆栈日志异常：" + e.getMessage());
        }
        return sb.toString();
    }
}
