# 银河 1.56.1 关键反编译代码

以下代码来自运行时提取的真实 DEX，使用 JADX 1.5.7 反编译。这里是关键路径摘录，不是官方源码，也不是可直接复用的 SDK。

## 1. DigitalKeyBgService 的通知控制入口

文件映射：`classes1.dex`
类：`com.geely.snc.digitalkey.base.service.DigitalKeyBgService`

```java
// mNotificationReceiver
if (Intrinsics.areEqual(
        DigitalKeyNotificationKt.ACTION_NOTIFICATION_CONTROL,
        intent.getAction())
        && (iDigitalKeyBleService = this.this$0.mDkService) != null) {
    int intExtra = intent.getIntExtra(
            DigitalKeyNotificationKt.INTENT_EXTRA_CMD,
            DkControlType.Lock.getAct());
    DkLogger.info$default(..., "DigitalKeyBgService notification click cmd value: " + intExtra, ...);
    iDigitalKeyBleService.doFeatureActionData(
            IDkBleFeatureKt.DK_FEATURE_FAST_CTRL,
            272,
            intExtra,
            new DkFeatureActionDataListener() {
                @Override
                public void onResult(int code, int action, int platform, byte[] info) {
                    DkLogger.info$default(..., "DigitalKeyBgService notification click action: " + action, ...);
                }
            });
}
```

动态注册：

```java
Context context = this.mContext;
if (Build.VERSION.SDK_INT >= 34) {
    context.registerReceiver(this.mNotificationReceiver, intentFilter, 4); // RECEIVER_NOT_EXPORTED
} else {
    context.registerReceiver(this.mNotificationReceiver, intentFilter);
}
```

AIDL 绑定入口：

```java
@Override
public IBinder onBind(Intent intent) {
    resetDkVersion(DigitalKeySdkEnv.INSTANCE.getDkVersion());
    return new DkBinder(this.mDkService);
}
```

结论：`DkBinder` 确实有 `doFeatureActionData`，但在当前 1.56.1 Manifest 中 `DigitalKeyBgService` 是 `exported=false`；普通第三方 App 不能直接绑定。通知按钮的 PendingIntent 是官方 App 自己创建的授权 token。

## 2. MiuiService 只负责连接

文件映射：`classes12.dex`
类：`com.ecarx.blekey.thirdadapter.service.MiuiService`

```java
@Override
public IBinder onBind(Intent intent) {
    return null;
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    String carId = intent.getStringExtra("setCarId");
    ScanResult scanResult = (ScanResult) intent.getParcelableExtra("ScanResult");
    if (!TextUtils.isEmpty(carId) && scanResult != null) {
        this.mCarLinkInteractor.connectDevice(
                carId,
                JsonParserUtils.parcelable2String(scanResult));
        return 2;
    }
    throw new NullPointerException("小米扫描的vin码或者scanResult为空！");
}
```

结论：`MiuiService` 是“连接/唤醒入口”，没有 `doFeatureActionData` 一类车控方法。

## 3. 小组件控制按钮

文件映射：`classes13.dex`

`WidgetEvent` 构造官方 PendingIntent：

```java
public final PendingIntent buildFunClickIntent(WidgetFunType widgetFunType) {
    return buildPendingIntent(
            WIDGET_CLICK,
            widgetFunType.ordinal() + 10000,
            MapsKt.mapOf(TuplesKt.to(WidgetFunType.BundleName, widgetFunType.name())));
}

public final PendingIntent buildFunConfirmIntent(WidgetFunType widgetFunType) {
    return buildPendingIntent(
            CONFIRM,
            20001,
            MapsKt.mapOf(
                TuplesKt.to(WidgetConfirmType.BundleName, WidgetConfirmType.CONFIRM.getBundleValue()),
                TuplesKt.to(WidgetFunType.BundleName, widgetFunType.name())));
}
```

常量：

```java
public static final String WIDGET_CLICK = "com.geely.galaxy.customer.widget.event.WIDGET_CLICK";
public static final String CONFIRM = "com.geely.galaxy.customer.widget.event.CONFIRM";
public static final String WidgetFunType.BundleName = "clickType";
public static final String WidgetConfirmType.BundleName = "WidgetConfirmType";
// WidgetConfirmType.CONFIRM = "android.appwidget.action.confirm.ok"
```

`WgControlView` 把 `WidgetFunType.LOCK` 的 PendingIntent 绑定到状态图标：

```java
remoteViews2.setOnClickPendingIntent(
        R.id.wg_view_fun_status,
        WidgetEvent.INSTANCE.buildFunClickIntent(funTypeByPosition));
remoteViews2.setOnClickPendingIntent(
        R.id.wg_view_fun_loading,
        WidgetEvent.INSTANCE.buildFunClickIntent(funTypeByPosition));
```

小组件图标同时携带锁态。`WidgetUiStatus.parseWgControlStatus()` 对 LOCK 的 `onStatus` 使用车辆门锁状态：

```java
case 1:
    zAnyDoorUnLocked = PlatformVehicleDoorBizExtendKt.anyDoorUnLocked(vehicleStatus);
    break;
...
arrayList.add(new WgFunStatus(widgetFunType, zAnyDoorUnLocked, zTrunkOpen));
```

`WidgetFunTypeIcon.getFunIcon()` 在 `onStatus=true` 时使用 `car_wg_funicon_door_lock_on`，否则使用 `car_wg_funicon_door_lock_off_black/white`。结合 `onStatus = anyDoorUnLocked`，可判定 `door_lock_on` 图标对应 `UNLOCKED`，两个 `door_lock_off_*` 图标对应 `LOCKED`；因此可从 RemoteViews 的 `mImageViewResource` 值反推最新锁态，作为通知文字锁态的独立交叉来源。

`WgCarControl` 最终调用官方车控业务：

```java
public final void carControl(RemoteViews remoteViews, WidgetFunType carControlType) {
    updateControlView(remoteViews, () ->
        CarControlBusiness.toCarControl$default(
            CarControlBusiness.INSTANCE,
            carControlType.getBinFunType(),
            null,
            null,
            SourceType.Widget,
            6,
            null));
}
```

## 4. 重要：Widget 的 LOCK 是状态切换，不是显式解锁/上锁

文件映射：`classes13.dex`
类：`com.geely.galaxy.car.vehicle.carcontrol.CarControlBusiness`

```java
private final Object lock(SourceType sourceType, Continuation<? super Unit> continuation) {
    boolean funcState = ControlItemHelperKt.getFuncState(FunType.LOCK);
    BasicLockParam basicLockParam = new BasicLockParam(
            CommandType.Companion.toStop(funcState),
            false,
            null,
            6,
            null);
    ...
    return ICarControlApi.DefaultImpls.controlDoor$default(
            CarControlManager.INSTANCE.getOneInstance(),
            basicLockParam,
            null,
            new CarControlCallBack(
                    FunType.LOCK,
                    funcState,
                    basicLockParam.getCmdType(),
                    ...),
            continuation,
            2,
            null);
}
```

`funcState` 是官方 App 当前的 `FunType.LOCK` 状态，`CommandType.toStop(funcState)` 是切换语义。Widget 只提供一个 LOCK 控件，不提供独立的 UNLOCK 控件。因此：

- Widget token 不能在不读取官方车辆锁态的情况下当作显式“解锁/上锁”命令；
- 仅在丢锁/丢解锁场景作为通知 token 的备用路径；
- 发送前必须有新鲜、可信的锁态，并确保目标状态与当前状态相反。

## 5. 结论

| 路径 | 当前 1.56.1 可用性 | 命令语义 |
| :--- | :--- | :--- |
| 通知栏官方 PendingIntent | 需要通知使用权，但当前 App 已支持并缓存 | 官方通知按钮的显式命令 |
| `DigitalKeyBgService` AIDL | `exported=false`，普通 App 不能直接绑定 | 若能绑定，`doFeatureActionData` 精确动作 |
| `MiuiService` | `exported=true` | 只连接，不直接控车 |
| 官方 AppWidget `WgProviderMiddle` | 需要 AppWidgetHost 配置；可取官方 PendingIntent | LOCK 控件是状态切换，需锁态门控 |
| 直接 BLE 协议 | 不建议 | 依赖官方私钥和车辆唯一连接槽 |
