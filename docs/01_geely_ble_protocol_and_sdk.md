# 01. 吉利银河 (`com.geelygalaxy.customer`) 脱壳、BLE 协议与 AIDL 逆向手册

本文档记录了对吉利银河 App (`com.geelygalaxy.customer`) 的脱壳提取路径、数字钥匙 SDK (`com.geely.snc.digitalkey` / `com.zeekr.sdk.snc.ble`) 核心类结构、BLE 广播协议、GATT 特征值、车机参数标定报文以及跨进程 AIDL/Notification 控制接口。

---

## 一、APK 加固壳分析与免 Root 脱壳方法

### 1. 壳特征
- 原始安装包 `/data/app/.../com.geelygalaxy.customer.../base.apk` 仅包含壳入口 `com.SecShell.SecShell.AW` 与 native 壳库 `libzky.so`（梆梆企业版加固），直接用 JADX 打开原始 APK 只能看到空壳类。
- **关键突破口**：该 APK 的 Manifest 中保留了 `android:debuggable="true"`（或在运行期将解密后的 22 个 DEX 缓存在应用私有数据目录中）。

### 2. 免 Root 提取真实 DEX 命令
通过 `run-as com.geelygalaxy.customer` 可以直接访问其私有目录下的已解密 DEX：
```bash
# 查看运行态解密后的 DEX 目录
adb shell "run-as com.geelygalaxy.customer ls -la /data/user/0/com.geelygalaxy.customer/.be89a562/1924833545/"

# 打包并导出全部 22 个真实 DEX 到本地电脑
adb shell "run-as com.geelygalaxy.customer tar -cf - -C /data/user/0/com.geelygalaxy.customer/.be89a562/1924833545 ." > /path/to/car_key_re/geely_dump/dex_dump.tar
tar -xf /path/to/car_key_re/geely_dump/dex_dump.tar -C /path/to/car_key_re/geely_dump/
```

### 3. 核心业务所在 DEX 索引表（含壳内子 APK / DEX）
吉利银河采用两层动态加载：除了进程私有目录下的 DEX 外，还在运行期解压出 `dk_core.dex`、`dk_icce.dex`、`dk_vivo.dex` 等数字钥匙子模块。快速检索时**严禁全文扫描**，直接对目标 DEX 使用 `grep -n` 定位类偏移后再局部读取：

| DEX 文件 | 核心包名 / 模块 | 关键类说明 |
| :--- | :--- | :--- |
| `dk_core.dex` | `com.geely.icc.common.business.alive.ble.*`、`com.geely.galaxy.car.weight.appwidget.*` | **V2 数字钥匙核心策略**：`VehicleV2BleStrategy`（V2 连接、3.07s 鉴权回调、`subConnectBle`）、`CarOrderBusiness`、`DigitalKey`、`WgProviderMiddle`（桌面小组件车控入口） |
| `dk_icce.dex` | `com.ecarx.blekey.thirdadapter.*` | **跨进程保活与直连适配器**：`MiuiService`（`exported=true`）、`MiuiReceiver`（`exported=true`）、`ThirdAdapterApi` |
| `classes2.dex` | `com.geely.snc.digitalkey.*` | V1 数字钥匙业务层：`DigitalKeyBgService`、`DigitalKeyManager`、`DkWalletManager`、`DkRemoteView`、`RemoteViewsClickReceiver` |
| `classes12.dex` | `com.zeekr.snc.service.*` | 跨进程服务接口层：`IDKService`、`IDKServiceCallback`、`DKData` |
| `classes19.dex` | `com.zeekr.sdk.snc.ble.*`、`com.init.ble.*` | 底层蓝牙协议栈：`ZeekrBleSdkManager`、`PendingIntentScanReceiver`、GATT 读写加密、RSSI 滤波与车机标定指令组装 |

---

## 二、车辆 BLE 广播帧格式（V2 实车 `0x01FE` 与 V1 `0xFF15` 双格式）

在实车 `LB378LEZ0XX000123` (`GeelyVehicle123`) 抓包与 `dk_core.dex` 逆向中，确认吉利银河车型存在 **V2 (`0x01FE` 联电 UAES 方案)** 与 **V1 (`0xFF15` 极氪/早期方案)** 两种广播帧格式：

### 1. 实车 V2 广播格式（UAES V2，本车 `LB378LEZ0XX000123` 实测采用）
- **蓝牙设备名 (`mDeviceName`)**：`"GeelyVehicle123"`（`"GeelyVehicle"` + VIN 末 3 位 `"123"`）
- **Service UUID**：`0000fdfd-0000-1000-8000-00805f9b34fb` (`0xFDFD`)
- **Manufacturer Specific Data**：Company ID = **`0x01FE`（十进制 `510`）**，18 字节 Payload：
  ```text
  实测十进制字节: [6, 2, 0, 20, 52, 53, -83, 0, 75, 3, 60, 55, 60, 53, 53, 59, 62, 49]
  ├── payload[0] = 6        -> Vender = 6 (UAES 联合汽车电子)
  ├── payload[1] = 2        -> Version = 2 (V2 蓝牙钥匙协议)
  ├── payload[4..5] = 52,53 -> ASCII "45" (车架号 LB378LEZ0XX000123 末 2 位)
  └── 后续字节              -> 车辆状态与滚动校验字段
  ```
- **本车实测 MAC 地址演进**：
  - 当前实车 V2 BLE MAC：**`AA:BB:CC:11:22:33`**（历史曾观测到 `AA:BB:CC:77:88:99`、`AA:BB:CC:44:55:66`，车机蓝牙模块重启或重置后可能轮换静态随机 MAC，因此匹配逻辑必须以 `0x01FE` 厂商帧 + `0xFDFD` 服务 UUID + `GeelyVehicle123` 名称为主，MAC 为辅）。
- **单连接槽位特性（极其重要）**：
  - 车机 BLE 外设仅支持 **1 个 GATT 连接槽位**。一旦任何手机端进程（无论是银河 `VehicleV2BleStrategy` 还是第三方 App）与 `AA:BB:CC:11:22:33` 建立 GATT 连接，**车机立即停止发送一切 BLE 广播**，直到该连接断开才恢复广播。

### 2. V1 广播包字节结构（兼容保留）
```text
原始广播片段: 09 FF 15 FF 35 34 37 32 33 32 34 35
              │  │  └──┴── Company ID = 0xFF15 (小端序字节: 0x15, 0xFF)
              │  └──────── AD Type = 0xFF (Manufacturer Specific Data)
              └─────────── Length = 9 bytes (2 bytes Company ID + 7~8 bytes Payload)
```
- **Manufacturer ID (`companyId`)**：`0xFF15`（字节流前两位为 `15 FF`）。
- **Payload 内容**：车辆 VIN 后 8 位或末 2 位 ASCII 字符。

---

## 三、GATT 服务与特征值 UUID 对照表

逆向 `com.init.ble.a.a`、`com.vehicles.ble` 得到的吉利/极氪数字钥匙三代平台 GATT UUID：

| 平台代际 | Service UUID | Write Characteristic (手机$\rightarrow$车机) | Notify/Read Characteristic (车机$\rightarrow$手机) |
| :--- | :--- | :--- | :--- |
| **初代平台 (Legacy)** | `00001908-0000-1000-8000-00805f9b34fb` | `00002b11-0000-1000-8000-00805f9b34fb` | `00002b10-0000-1000-8000-00805f9b34fb` |
| **新平台 (New Platform)** | `0000889b-0000-1000-8000-00805f9b34fb` | `00008982-0000-1000-8000-00805f9b34fb` | `00008901-0000-1000-8000-00805f9b34fb` |
| **高阶平台 (High Platform)** | `00005801-0000-1000-8000-00805f9b34fb` | `00005803-0000-1000-8000-00805f9b34fb` | `00005802-0000-1000-8000-00805f9b34fb` |
| **CCCD 描述符** | - | - | `00002902-0000-1000-8000-00805f9b34fb` |

---

## 四、车机端距离标定协议与公式 (`com.init.ble.f.c`)

在 `classes19.dex` 的 `com.init.ble.f.c` 类中，逆向出了吉利向车机写入蓝牙 RSSI 标定参数的明文逻辑：

### 1. 车机标定报文格式
```java
byte[] payload = new byte[] {
    64,                         // 0x40: 命令字 (设置车机 BLE 迎宾/闭锁标定参数)
    1,                          // 0x01: 子命令版本
    20,                         // 0x14: 基准常量 (20)
    (byte) rssiAdjust,          // 手机天线 RSSI 校准偏移量 (默认约 0 ~ 2)
    (byte) unlockInterval,      // 解锁距离档位/阈值分量 (默认约 60 ~ 64)
    (byte) lockInterval,        // 闭锁迟滞增量 (原厂往往仅 4 ~ 6 dB，导致 3 米误锁！)
    (byte) rssiWindow           // 车机端滤波滑窗大小 (默认 3 ~ 5)
};
```

### 2. 车机端闭锁判定公式（源码日志还原）
`com.init.ble.f.c` 中的调试日志字符串明确打印了车机固件内部的闭锁区间计算方式：
$$\text{lockRange} = \text{unlockInterval} + \text{lockInterval} - \text{rssiAdjust}$$
- **原厂缺陷**：当 `unlockInterval` 对应约 1.5~2 米（如 `-62 dBm`），而 `lockInterval` 仅设为 `4~6 dB` 时，`lockRange` 落在 `-66 ~ -68 dBm`。人在车旁 3 米处只要身体转身挡住手机，信号瞬时跌破 `-70 dBm`，车机立刻判定离开并触发闭锁！

---

## 五、`DigitalKeyBgService` 本地 AIDL 接口 (`IDigitalKeyBleService`) 全集

吉利银河的数字钥匙后台服务运行在独立进程 `com.geelygalaxy.customer:dkservice` 中：
- **Service 类名**：`com.geely.snc.digitalkey.base.service.DigitalKeyBgService`（`exported="true"`）
- **绑定 Action**：`com.zeekr.snc.service.action.DK_START` / `com.geely.pendintent.scanresult`（包名 `com.geelygalaxy.customer`）
- **保活伴生 Service**：`com.ecarx.blekey.thirdadapter.service.MiuiService`（`exported="true"`，Action: `com.geely.geometry.miuiservice.action`）
- **AIDL 接口描述符**：
  - 主服务：`com.geely.snc.digitalkey.base.dk.IDigitalKeyBleService`
  - 连接回调：`com.geely.snc.digitalkey.base.dk.IDigitalKeyConnectionListener`
  - 数据回调：`com.geely.snc.digitalkey.base.dk.business.IDkBleDataListener`

### 1. `IDigitalKeyBleService` Transact 方法表 (`Stub` 序号 `1..22`)

| Transaction Code | 方法名 | 用途说明 |
| :---: | :--- | :--- |
| `1` | `getVersion()` | 获取数字钥匙 SDK 版本 |
| `2` | `setPairMode(int)` | 设置配对模式 |
| `3` | `updateContextInfo(...)` | 更新用户/车辆上下文 |
| `4` | `setAutoConnect(String vin, boolean auto)` | **开启/关闭后台自动重连车机蓝牙** |
| `5` | `setConnectionListener(IDigitalKeyConnectionListener)` | 注册蓝牙连接状态监听 |
| `6` | `scanVehicle(String vin)` | **主动触发扫描指定 VIN 车辆 BLE** |
| `7` | `stopScanVehicle(String vin)` | 停止扫描车辆 |
| `8` | `connectVehicle(String vin)` | **强制触发后台连接指定 VIN 车辆 BLE（绕过 `piScanMacCache` 死锁的核心方法）** |
| `9` | `connectVehicleBle(String vin, String mac)` | 指定 VIN + MAC 直接发起 BLE 连接 |
| `10` | `connectPiScanResult(String vin)` | 模拟 `PendingIntent` 扫到车后的连接流程 |
| `11` | `disconnectVehicle(String vin)` | 断开车辆 BLE |
| `12` | `isVehicleConnected(String vin)` | 查询当前车辆是否已连上并完成蓝牙钥匙鉴权 |
| `13` | `getBleConnectStatus(String vin)` | 获取连接状态码（`0`=未连接, `1`=扫描/连接中, `2`=已连接, `3`=已鉴权就绪） |
| `14..18` | `hasVehiclePaired` / `isGeelyOneVersion` / `hasDigitalKey` / `getUsedDkId` / `getAntiAbuse` | 钥匙元数据查询 |
| `19` | `doFeatureActionData(String feat, int action, String jsonOrParam, IDkBleDataListener cb)` | 执行 JSON/整型 Feature 动作（如快速解闭锁、档位读写） |
| `20` | `doFeatureActionBytesData(String feat, int action, byte[] data, IDkBleDataListener cb)` | 执行原始字节数组 Feature 动作（如标定数据下发） |
| `21` | `addFearureListener(String feat, IDkBleDataListener cb)` | 监听车机主动上报事件（注意吉利原厂拼写即为 `Fearure`） |
| `22` | `removeFearureListener(String feat, IDkBleDataListener cb)` | 移除车机事件监听 |

### 2. 核心 `Feature Name` 与 `Action ID` 映射表

| Feature 常量 | Action ID (`int`) | 十六进制 | 含义与作用 |
| :--- | :---: | :---: | :--- |
| `"feat_dk_fast_ctrl"` | `272` | `0x0110` | **本地蓝牙快速车控 (`CMD_A2V_CONTROL`)**：`CTRL_UNLOCK = 1`（解锁）、`CTRL_LOCK = 2`（闭锁）、`CTRL_CAR_LOCATOR = 3`、`CTRL_TRUNK_UNLOCK = 6`、`CTRL_WINDOW_UP = 12`、`CTRL_WINDOW_DOWN = 13` |
| `"feat_calibration"` | `305` | `0x0131` | 历史协议名称为`CMD_A2V_CALIB_REQ`；当前分析的`DkBleCalibrationImpl.doAction`未见到305分发，不能假定L6/DK1.5会返回 |
| `"feat_calibration"` | `368` | `0x0170` | 设置DK2迎宾/闭锁距离档位；银河L6/DK1.5入口不执行，不应在L6上恢复写入UI |
| `"feat_pair_coef"` | `370` | `0x0172` | 手机型号射频衰减补偿系数 (`ACT_PHONECOEF`) |
| `"feat_calibration"` | `400` | `0x0190` | 启动车机蓝牙自标定 (`CMD_A2V_CALIBRATION_START`) |
| `"feat_calibration"` | `405` | `0x0195` | 向车机发送底层 RSSI 标定字节帧 (`CMD_A2V_SEND_CALIBRATION`) |
| `"feat_dk_configuration"` | `406` | `0x0196` | 读取/切换 PE 无感进入模式 (`CMD_A2V_PE_MODE_REQ`) |
| `"feat_calibration"` | `409` | `0x0199` | 刷新极氪/银河平台标定模型 (`CMD_A2V_CALIBRATION_MODEL_ZEEKR`) |
| `"feat_dk_configuration"` | `5392` | `0x1510` | PE 1.5 总开关 (`ACT_PE15_SWITCH`) |
| `"feat_dk_configuration"` | `5393` | `0x1511` | **靠近自动解锁开关 (`ACT_APPROACH_UNLOCK`)** |
| `"feat_dk_configuration"` | `5394` | `0x1512` | **离开自动上锁开关 (`ACT_WALKAWAY_LOCK`)** |
| `"feat_dk_configuration"` | `5395` | `0x1513` | 蓝牙断开策略配置 (`ACT_DISCONNECT`) |

### 3. 车机上报 Listener ID (`LISTENER_*`)

| Listener ID | 常量名 | 触发时机 |
| :---: | :--- | :--- |
| `289` (`0x0121`) | `LISTENER_VEHICLE_STATUS` | 车辆门锁/车窗/电源状态变化上报 |
| `306` (`0x0132`) | `LISTENER_CALIB` | 车机端标定参数回报 |
| `345` (`0x0159`) | `LISTENER_APPROACH_LOCK` | **车机触发无感闭锁通知 (`CMD_V2A_APPROACH_LOCK_NOTIFY`)**——若此时手机仍在 3 米缓冲带内，即可精准识别为 3 米误锁！ |
| `4384` (`0x1120`) | `LISTENER_VEHICLE_RESULT` | 车控指令执行结果回执 |

---

## 六、吉利常驻通知栏 (`id = 5`) 控件 ID 与 `PendingIntent` 桥接机制

### 1. 为什么要研究通知栏 `RemoteViews`？
在 Android 14 ~ 16 (API 34~36) 上，`DigitalKeyBgService` 内部注册通知栏点击广播 `mNotificationReceiver` 时使用了 **`Context.RECEIVER_NOT_EXPORTED` (flag = `4`)**。
这意味着外部 App 直接调用 `sendBroadcast(new Intent("com.geely.snc.digitalkey.notify_click_action"))` 会被系统静默丢弃！
但是，吉利塞进常驻通知栏（`NotificationId = 5`）`RemoteViews` 里的 **`PendingIntent` 是由吉利自身签名创建的**，通过 `NotificationListenerService` 提取出该 `PendingIntent` 并调用 `pendingIntent.send()`，拥有与用户在下拉通知栏亲手点击「解锁 / 闭锁」按钮 **100% 相同的系统特权和 0 延迟响应**。

### 2. `com.geely.snc.digitalkey.base.R$id` 通知栏资源 ID 精确表（由 `DkRemoteView` 反编译确认）

| 资源名称 | 十六进制 ID | 十进制 ID | 用途与状态解析规则 |
| :--- | :---: | :---: | :--- |
| `notif_layout_lock_unlock` | `0x7f0a10c6` | `2131366086` | 解锁/闭锁按钮父容器（`View.VISIBLE` = 蓝牙已连上，`GONE` = 蓝牙未连接） |
| `notif_tv_lock` | `0x7f0a10c7` | `2131366087` | **「上锁」按钮**（绑定 `PendingIntent` $\rightarrow$ `DkControlType.Lock = 2`） |
| `notif_tv_status_conn` | `0x7f0a10c8` | `2131366088` | **「蓝牙钥匙已连接」状态文本**（`View.VISIBLE (0)` 代表吉利已连上车机 BLE） |
| `notif_tv_status_disconn` | `0x7f0a10c9` | `2131366089` | **「蓝牙钥匙未连接」状态文本**（`View.VISIBLE (0)` 代表吉利当前未连上车机 BLE） |
| `notif_tv_status_no` | `0x7f0a10ca` | `2131366090` | 无可用钥匙提示文本 |
| `notif_tv_unlock` | `0x7f0a10cb` | `2131366091` | **「解锁」按钮**（绑定 `PendingIntent` $\rightarrow$ `DkControlType.Unlock = 1`） |

---

## 七、吉利银河 App 导出的跨进程组件 (`exported="true"`) 与 V2 控制/唤醒链路

由于吉利银河主服务 `DigitalKeyBgService` 在新版本 Manifest 中设为 `exported="false"`，外部独立 App 必须通过吉利银河暴露的 **`exported="true"` 且无特殊权限限制**的组件进行后台唤醒、蓝牙直连与车控触发：

### 1. 导出组件清单（逆向自 `AndroidManifest.xml`）

| 组件类型 | 完整类名 | Action / 调用参数 | 核心作用 |
| :--- | :--- | :--- | :--- |
| `Service` | `com.ecarx.blekey.thirdadapter.service.MiuiService` | Action: `com.geely.consumer.miuiservice.action` / `com.geely.geometry.miuiservice.action`<br>Extras: `"setCarId"` (VIN `String`), `"ScanResult"` (`android.bluetooth.le.ScanResult`) | **V2 蓝牙直连核心入口**：调用 `ThirdAdapterApi.connectBle(scanResult)` $\rightarrow$ `VehicleV2BleStrategy.subConnectBle(scanResult)` $\rightarrow$ `CarOrderBusiness.connectBle(scanResult)` |
| `Receiver` | `com.ecarx.blekey.thirdadapter.receiver.MiuiReceiver` | Action: `com.geely.consumer.miuiservice.action`<br>Extras: `"setCarId"`, `"ScanResult"` | 广播方式触发 V2 蓝牙直连与进程唤醒 |
| `Service` | `com.geely.lib.dk.ble.GLDkConnectionService` | Extra: `"setCarId"` (VIN `String`) | 唤醒底层 BLE 连接管理服务 |
| `Receiver` | `com.geely.snc.digitalkey.notify.RemoteViewsClickReceiver` | Action: `com.geely.cockpit.ble.RemoteViewClick`<br>Extras: `"com.extra.cmd"` / `"ctrlType"` (`1`=解锁, `2`=上锁) | 导出的通知栏远程视图点击接收器 |
| `Receiver` | `com.geely.galaxy.car.weight.appwidget.WgProviderMiddle` | Action: `com.geely.galaxy.customer.widget.event.CONFIRM`<br>Extras: `"WidgetConfirmType"="android.appwidget.action.confirm.ok"`, `"clickType"="LOCK"`, `"carControlType"` (`1`/`2`) | 导出的桌面小组件二次确认控车入口（内部调用 `WidgetCarControlHelper.handleCommand`） |

### 2. `VehicleV2BleStrategy` 鉴权握手时序特征（实测 3.07 秒）
在 `dk_core.dex` 的 `com.geely.icc.common.business.alive.ble.VehicleV2BleStrategy` 中：
- `subConnectBle(ScanResult)`（偏移 `0x5f066c`）校验 `ScanResult` 中的 `0x01FE` 厂商帧（`vender=6, version=2` 且 VIN 末两位匹配 `"45"`）后，调用 `fastConnectBle(ScanResult)`（偏移 `0x5efafc`）。
- 若距离上次断开不足 `2000ms`（`lastDisConnectTime`），`fastConnectBle` 会通过 `HandlerUtils.runOnWorkThread` 延迟 `2000 - elapsed` 毫秒后再发起连接。
- 从 `connect real start V2` 到 GATT 建链、MTU 协商、UAES V2 双向密钥鉴权完成并回调 `VehicleV2BleStrategy$initListener$1.isConnectDevice(true)`（日志打印 `v2 isConnectDevice true latest CONNECT`，车机发出钥匙连接提示音），实测耗时约 **3.07 秒**（例如 `12:57:25.27` $\rightarrow$ `12:57:28.347`）。
- **工程启示**：在车机完成这 3.07 秒 V2 鉴权之前下发的任何 BLE 解锁指令都会因为通道尚未就绪而被丢弃；因此独立控车 App 必须在触发解锁后覆盖 **`+0s, +1.2s, +2.5s, +3.6s, +4.8s, +6.2s`** 全时段重发窗口，确保在 3.07 秒车机响起连接声后的第一时间（`+3.6s`）完成解锁。

