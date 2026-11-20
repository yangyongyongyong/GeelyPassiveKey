# 78. 银河 1.56.1 真脱壳、控制链路与无通知优化评估

日期：2026-11-20
目标：`com.geelygalaxy.customer` 1.56.1（versionCode `156010010`）
性质：运行时脱壳 + 关键代码分析 + 后续 App 优化设计；未在真实车辆上发送车控指令。

## 1. 结论先行

这次壳确实已经解开。通过 Android 35 AOSP root 模拟器、运行时冻结进程和 `/proc/<pid>/mem` 提取，成功拿到 14 个真实 `dex\n038`，并定位到以下关键类：

- `DigitalKeyBgService`：在 `classes1.dex`
- `MiuiService`、`FunType`：在 `classes12.dex`
- `WgProviderMiddle`、`WgBaseProvider`、`WgControlView`、`WgCarControl`、`WidgetEvent`、`WgConfig`、`CarControlBusiness`：在 `classes13.dex`

由此可以明确回答：

1. **当前 1.56.1 普通第三方 App 不能靠“直接发广播”控车**：`DigitalKeyBgService` 的通知接收器是动态注册，并在 Android 14+ 使用 `RECEIVER_NOT_EXPORTED`；`RemoteViewsClickReceiver`、`WgProviderMiddle` 的 Manifest `exported` 也都是 `false`。
2. **`DigitalKeyBgService` 确实有完整 AIDL 车控入口**：`DkBinder` 实现了 `doFeatureActionData`，但服务当前是 `exported=false`，普通 App 无法直接绑定。
3. **`MiuiService.onBind()` 仍是 `null`**，只接收 `setCarId + ScanResult` 并转给官方连接 SDK；它不是车控服务。
4. **通知栏 PendingIntent 仍是最稳的普通 App 车控 token**；但它不是唯一理论路径。官方桌面小组件 `WgProviderMiddle` 的 RemoteViews 中也带官方 PendingIntent，可通过 `AppWidgetHost` 获取。
5. **Widget 不是直接替代通知的等价方案**：`WgCarControl` 最终走 `CarControlBusiness.lock()`，而 `FunType.LOCK` 是官方 App 当前锁态的状态切换，不是显式“解锁/上锁”命令。没有新鲜锁态时，禁止用 Widget token 自动车控。

因此当前产品策略应为：**通知栏 PendingIntent 主路径保留；AppWidget 作为可选备用 token 源，不作为默认无通知替代；直接 AIDL/root 模式不进入普通发行版。**

## 2. 脱壳方法

### 2.1 环境

- Android 35 AOSP `default/arm64-v8a` userdebug 模拟器
- `adb root` 可用
- 不影响用户真机数据
- 使用 `frida` 不是必须；本次最终提取没有依赖 Frida 注入

### 2.2 关键步骤

1. 安装官方 APK 到 root 模拟器；
2. 启动 `GalaxySplashActivity`；
3. 约 1.2 秒后对官方进程执行 `SIGSTOP`，冻结壳和应用；
4. 读取 `/proc/<pid>/maps`，定位约 117 MB 的解密 `/dev/zero` 区域；
5. 从 `/proc/<pid>/mem` 导出该区域；
6. 在区域内扫描 DEX magic，按每个 DEX header 的 `file_size` 精确切成 14 个 DEX。

壳在运行时会触发 CPU 时间限制/`SIGXCPU`，目标进程约数秒后终止。先冻结再读内存是关键；不要在目标仍运行、且自身扫描器抢占 CPU 时做长时间内存读取。

### 2.3 本次产物

- 原始 APK SHA-256：`6b5019e29b8fcc903b16863c5d4084bc9ce5f088718c8286ebea09da0c06b8fa`
- 解密内存区：117,080,064 字节
- 真实 DEX：14 个
- 14 个 DEX 的 SHA-256、类映射和关键代码摘录：
  - [脱壳资料索引](reverse_engineering/geely_1_56_1/README.md)
  - [关键反编译代码](reverse_engineering/geely_1_56_1/key_code.md)
- [云端车况网关反编译清单](reverse_engineering/geely_1_56_1/cloud_gateway_manifest.md)
- [云端车况网关分析](80_cloud_vehicle_status_gateway.md)

不在仓库中提交约 112 MB 的内存区和完整 DEX 二进制；只保留可复核的哈希、类映射和关键代码片段。

## 3. 真实控制路径

### 3.1 通知栏路径：官方显式 PendingIntent

`DigitalKeyBgService` 内部接收通知控制：

```java
if (ACTION_NOTIFICATION_CONTROL.equals(intent.getAction()) && mDkService != null) {
    int cmd = intent.getIntExtra(INTENT_EXTRA_CMD, DkControlType.Lock.getAct());
    mDkService.doFeatureActionData("feat_dk_fast_ctrl", 272, cmd, listener);
}
```

对应动态注册：

```java
if (Build.VERSION.SDK_INT >= 34) {
    context.registerReceiver(mNotificationReceiver, filter, 4); // RECEIVER_NOT_EXPORTED
} else {
    context.registerReceiver(mNotificationReceiver, filter);
}
```

这正是当前 App 需要通过 `NotificationListenerService` 获取官方 Notification 中 PendingIntent 的原因：系统把“创建 token 的身份”和“发送指令的身份”绑定在一起，第三方无法凭空伪造一个带官方 UID 的 token。

### 3.2 `MiuiService` 路径：只连接，不控车

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
        mCarLinkInteractor.connectDevice(carId, JsonParserUtils.parcelable2String(scanResult));
        return 2;
    }
    throw new NullPointerException("小米扫描的vin码或者scanResult为空！");
}
```

`MiuiService` 不提供 `doFeatureActionData`；因此不能把“唤醒/连接”误认为“主动控车”。

### 3.3 `DigitalKeyBgService` 路径：有 AIDL，但当前不可直接绑定

`DigitalKeyBgService.onBind()`：

```java
return new DkBinder(this.mDkService);
```

`DkBinder` 中有：

```java
doFeatureActionData(String featureName, int action, String value, IDkBleDataListener listener)
```

以及 `connectVehicle`、`connectVehicleBle`、`getBleConnectStatus`、`isVehicleConnected` 等方法。

但当前 1.56.1 Manifest：

```text
com.geely.snc.digitalkey.base.service.DigitalKeyBgService exported=false
```

普通第三方 App 不能直接绑定它，也不能通过 ADB/Shizuku 之外的普通应用权限绕过 `exported` 限制。因此：

- 不作为普通 Release 主路径；
- 不把旧版 `exported=true` 的 AIDL 结论继续当作 1.56.1 事实；
- 未来如要做开发者专用版本，可单独评估 Shizuku/root 辅助，但不能污染普通用户安装包。

### 3.4 AppWidget 路径：可取官方 token，但 LOCK 是状态切换

`WidgetEvent` 会创建官方 PendingIntent：

```text
action = com.geely.galaxy.customer.widget.event.WIDGET_CLICK
extras = WIDGET_EVENT_FLAG=true, clickType=LOCK
component = com.geelygalaxy.customer/...WgProviderMiddle
```

确认按钮：

```text
action = com.geely.galaxy.customer.widget.event.CONFIRM
extras = WidgetConfirmType=android.appwidget.action.confirm.ok, clickType=LOCK
```

`WgControlView` 把该 PendingIntent 绑定到控件；最终 `WgCarControl.carControl()` 调用：

```java
CarControlBusiness.toCarControl(...FunType.LOCK..., SourceType.Widget)
```

真正的 `CarControlBusiness.lock()`：

```java
boolean funcState = ControlItemHelperKt.getFuncState(FunType.LOCK);
BasicLockParam param = new BasicLockParam(
    CommandType.Companion.toStop(funcState), false, null, 6, null);
```

所以 Widget 的 LOCK 控件是“根据官方当前锁态切换”，不是独立的显式解锁/上锁值。若官方锁态未知或过期，贸然发送可能造成反向动作。

## 4. 控制通道边界矩阵

| 路径 | 普通 App 可用性 | 1.56.1 限制 | 命令语义 | 结论 |
| :--- | :--- | :--- | :--- | :--- |
| 通知栏官方 PendingIntent | 可用，但需通知使用权 | 需捕获并缓存官方 token | 通知按钮的显式 cmd | **主路径保留** |
| `MiuiService` | 可用 | `onBind=null` | 只连接/唤醒 | 只用于连接 |
| `DigitalKeyBgService` AIDL | 不可直接绑定 | `exported=false` | 精确 `doFeatureActionData` | 普通版不采用 |
| `WgProviderMiddle` AppWidget | 可宿主化 | 需 AppWidgetHost/配置、官方 token | LOCK 为状态切换 | **可选备用，需锁态门控** |
| 直接 BLE | 技术门槛极高 | 私钥、单连接槽、协议风险 | 精确但不可安全复用 | 不采用 |

## 5. 下一步优化方案

### P0：保持现有通知主路径不被破坏

- 不删除通知监听、PendingIntent 缓存、GATT `cmdId=5` 就绪门控、ACL 保护、近场保护和用户参数保留逻辑。
- 本次只增加“控制 token 来源”抽象，不改变现有发送时机与四项默认参数。

### P1：抽象 `OfficialControlToken`

新增统一模型，建议字段：

```text
ControlCommand: UNLOCK / LOCK
ControlTokenSource: NOTIFICATION / WIDGET
ControlTokenSemantics: EXPLICIT / TOGGLE
createdAtElapsedMs
token: PendingIntent
```

- 通知 token 包装为 `EXPLICIT`；
- Widget token 包装为 `TOGGLE`；
- 调度器只在 token 语义、当前锁态、目标动作三者一致时发送；
- 日志新增 `CONTROL_TOKEN_SOURCE`、`CONTROL_TOKEN_REJECTED`、`WIDGET_STATE_UNKNOWN`，便于后续实车排查。

### P2：增加可选 `WidgetTokenBridge`

在维护/授权页增加“官方小组件备用控制（可选）”入口：

1. 用标准 `AppWidgetHost` 绑定 `WgProviderMiddle`；
2. 在受控设置页展示官方小组件的配置/选中区域，用户手动完成一次车辆/控件配置；
3. 从官方 RemoteViews 中提取 `WIDGET_CLICK`、`CONFIRM` 的官方 PendingIntent；
4. 仅保存私有缓存，不读取官方 App 私钥和车辆凭证；
5. 配置完成后再监控 RemoteViews 更新，刷新 token。

优先使用官方 `WgProviderMiddle` 的 `WIDGET_CLICK(clickType=LOCK)` 以及必要时的 `CONFIRM(confirm.ok, clickType=LOCK)`；不要把 Widget 的单个“车锁”按钮当成显式解锁按钮。

### P3：Widget 发送门控

Widget 备用路径必须同时满足：

- 官方 App 已登录且能拿到当前车辆；
- 官方小组件已经配置出有效控制控件；
- 当前锁态新鲜、可信，且与目标动作相反；
- 官方 BLE 连接/鉴权已就绪；
- 不在近场保护、插线禁控、行车门控、手动锁存等安全联锁内；
- 同一轮只允许一个 token 源发送，避免通知与 Widget 双发。

具体规则：

- 目标是 `UNLOCK`：只有已知当前为 `LOCKED`，才允许 Widget LOCK 切换；
- 目标是 `LOCK`：只有已知当前为 `UNLOCKED`，才允许 Widget LOCK 切换；
- 当前锁态未知、超过有效期、与官方状态冲突：Widget 路径拒绝发送，并记录原因；
- 通知 token 仍优先；Widget 只作为 fallback。

### P4：UI 与用户感知

- 维护页显示两个独立状态：
  - `通知按钮：已授权/监听在线/已缓存 token`
  - `官方小组件：未配置/已就绪/需要重新选择车辆`
- 顶部实时状态增加来源说明：
  - `等待官方通知按钮`
  - `小组件备用 token 已就绪`
  - `小组件锁态未知，暂不发送`
- 不自动打开银河 App 配置页，不替用户点控件；只有用户主动点击入口后才打开配置页，车辆选择必须由用户手动完成。
- 所有新增状态只做辅助显示，不改变用户已保存的四项触发参数。

### P5：测试与验收

先做离线/模拟器测试，再上真车：

1. 通知 token 与 Widget token 的语义单测；
2. 锁态为 `LOCKED` 时只允许 Widget UNLOCK，锁态为 `UNLOCKED` 时只允许 Widget LOCK；
3. 锁态未知、过期、GATT 未就绪、近场保护、插线禁控时全部拒绝；
4. 通知 token 缺失、Widget token 存在时的模拟解锁/上锁；
5. 通知 token 恢复后优先级仍为通知；
6. 覆盖安装后用户四项参数不变；
7. 真车验证只发单次受控命令，避免通知 + Widget 重复车控。

### 当前实现状态（2026-11-20，本地构建）

- 已新增 `OfficialControlToken`、`WidgetTokenPolicy`、`WidgetTokenBridge`；
- 通知 token 优先，Widget 仅在显式启用、token 已捕获、锁态新鲜且与目标相反时可选；
- Widget RemoteViews 的门锁图标同时作为独立锁态来源；通知文字和图标按观测时间取最新；
- `WgProviderMiddle` 的 LOCK 点击后最多尝试一次点击后新捕获的 CONFIRM token，不重发点击；
- 维护页新增“官方小组件备用控制（可选）”卡片，绑定和车辆选择均需用户手动完成；
- 构建脚本已加入 Widget 路由单测；本轮未安装真机、未发送真实车控，也未 push 或 Release。

## 6. 本轮不做什么

- 不把 `112 MB` DEX 或 `117 MB` 内存镜像提交到 Git；
- 不把 `DigitalKeyBgService` AIDL 直接硬塞进普通发行版本；
- 不把 Widget 的 `LOCK` 当作显式解锁/上锁；
- 不在没有新鲜锁态时用 Widget 自动车控；
- 不修改当前四项默认参数，也不覆盖用户已保存配置；
- 未经明确要求不 push、不创建 Release。

## 7. 证据入口

- [脱壳资料索引](reverse_engineering/geely_1_56_1/README.md)
- [关键反编译代码](reverse_engineering/geely_1_56_1/key_code.md)
- [云端车况网关反编译清单](reverse_engineering/geely_1_56_1/cloud_gateway_manifest.md)
- [云端车况网关分析](80_cloud_vehicle_status_gateway.md)
- 原 APK：`/tmp/geelygalaxy-1.56.1.apk`
- 真实 DEX（本机临时）：`/tmp/geely-dex-out/classes0.dex` 至 `classes13.dex`
- 关键反编译临时输出：`/tmp/geely-jadx-generated.iZBlI3/`
