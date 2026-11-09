# 36. v4.0 极简通用无感钥匙：只保留息屏靠近解锁 / 离开上锁

## 1. 目标

把原「银河钥匙雷达 v3.20.6」裁剪成一个功能极简、可通用分发的 App：

- 核心只保留：**息屏靠近解锁、离开上锁**，另提供两个用户主动点击的车控链路测试按钮；
- 不要求用户填写 VIN、MAC、车型或任何逆向参数；
- 不依赖电脑 adb、REA、私有数据或 root；
- 用户安装 App、用银河官方 App 连接一次车辆后，本 App 自动识别本车蓝牙身份；
- 保留原包名 `com.luca.geelykeyradar`，升级安装后通知监听授权通常可以继续沿用；若系统只保留授权但不再绑定监听，使用 App 内“修复通知监听”重新开关一次。

当前版本：`v4.0.9-min / versionCode 109`。后台锁定说明见 [46_v4_0_9_background_lock_guide.md](46_v4_0_9_background_lock_guide.md)，电池白名单反馈见 [45_v4_0_8_battery_whitelist_feedback.md](45_v4_0_8_battery_whitelist_feedback.md)，后台扫描与权限拆分见 [44_v4_0_7_background_scan_and_permissions.md](44_v4_0_7_background_scan_and_permissions.md)。

---

## 2. 删除的功能

以下 v3.x 功能已从新源码移除：

- 旧版手动解锁/上锁实现（v4.0.5仅重新增加两个复用当前通知链路的测试按钮）；
- 摇一摇解闭锁；
- 电源键三连、音量键、无障碍按键；
- 快捷设置磁贴；
- 桌面 4x2 组件、组件二次确认、组件设置；
- 总里程、胎压、行程记录、油电续航折扣；
- 哨兵模式、语音播报、提示音/震动；
- 车机侧 PE 参数读写、备份恢复；
- 锁态云端/本地仲裁、0x0121 锁态强一致；
- GPS 车速监控、A/C 充电判断、A2DP 行驶判定；
- 手动“一键唤醒/刷新锁态”等调试入口。

旧版源码与 APK 归档在：

```text
GeelyKeyRadarApp/legacy/v3.20.6/
```

---

## 3. 新代码结构

```text
GeelyKeyRadarApp/src/com/luca/geelykeyradar/
├── PassiveKeyService.java          # 前台服务：BLE 扫描、身份学习、RSSI、状态机、GATT、WakeLock
├── PassiveKeyLogic.java            # 纯逻辑：靠近一次解锁、远离驻留上锁、断连不误锁
├── RssiFilter.java                 # 三点中值 + 按时间计算的非对称 EMA
├── PendingControl.java             # 唯一待发请求、固定期限、有限发送尝试
├── NotificationEpoch.java          # 拦截旧通知解析与迟到移除
├── RotatingFileLog.java            # 固定文件数及容量的轮转日志
├── DiagnosticLog.java              # 跨模块日志、进程退出与服务运行记录
├── DiagnosticLogStore.java         # 有界队列、固定容量、脱敏ZIP导出
├── DiagnosticText.java             # 新旧日志的身份与凭证脱敏
├── DiagnosticCounters.java         # 扫描与读取的累计/窗口计数
├── GeelyAdvertisement.java         # 解析 0x01FE / 0xFDFD / GeelyVehicle 名称 / 15FF
├── VehicleIdentity.java            # 本车蓝牙身份与匹配规则
├── VehicleIdentityStore.java       # 身份持久化 + 两个自动开关
├── VehicleLearningState.java       # 地址学习完成资格、来源和权限/重连提示
├── GeelyBridge.java                # 最小化调用银河官方导出组件
├── KeyNotificationBridgeService.java # 读取官方通知状态 + 点击官方解锁/上锁按钮
├── MainActivity.java               # 极简配置页：状态、学习、手动测试、权限、开关、日志
└── BootReceiver.java               # 开机/更新自动拉起
```

---

## 4. 通用化：如何在不读私有数据的情况下识别本车

### 4.1 广播识别

`GeelyAdvertisement` 兼容以下常见吉利数字钥匙广播特征：

| 特征 | 用途 |
| :--- | :--- |
| Company ID `0x01FE`（510） | UAES/吉利 V2 厂商数据；第 4~5 字节可提取 VIN 后两位 |
| 设备名 `GeelyVehicle<VIN后三位>` | 最稳定的跨车型身份之一 |
| Service UUID `0xFDFD` + 吉利设备名 | 部分车型/固件只暴露服务 UUID |
| 原始 `15FF` 厂商帧 | 老平台/V1 广播兜底 |

**息屏扫描关键点（参考开源项目 `borconi/openzeekr` Issue #21 与 AOSP `ScanManager`）**：
- Android 在屏幕熄灭时会直接挂起无 `ScanFilter` 的扫描（`Cannot start unfiltered scan in screen-off`）；
- 因此 `PassiveKeyService.buildHardwareScanFilters()` 默认使用通用硬件过滤（Company ID `0x01FE`、UUID `0xFDFD`、Company ID `0x06FE`，以及已学习的本车名称/MAC），既不需要写死任何车型参数，又能保证息屏状态下 BLE 扫描不被系统冻结。

解析结果只保存广播里本来就公开的字段：

- BLE 地址；
- 广播名称；
- VIN 后两位/后三位；
- 厂商数据前 8 字节签名。

**不保存 VIN 全码、账号、token、证书或吉利私有存储。**
运行时不读取 `/sdcard/Android/data/com.geelygalaxy.customer/`、官方 App 私有 SharedPreferences/MMKV 或 REA 导出的内存数据。

### 4.2 本车身份自动学习

学习时机与多通道来源（无需用户手工配置，也支持可选填入车架号后 3 位）：

1. **系统级 BLE GATT / ACL 广播监听**：监听 `android.bluetooth.gatt.profile.action.CONNECTION_STATE_CHANGED`（含 `bd_addr`）与 `ACTION_ACL_CONNECTED`，按已学习名称、尾号或近期广播校验本车。地址轮换但没有任何身份依据时不盲目绑定匿名设备；
2. **系统 `BluetoothManager.getConnectedDevices(GATT)`**：匹配 `GeelyVehicle*` 名称；
3. **近场单一吉利广播自动锁定**：未学习状态下若扫到唯一 `GeelyVehicle*` / `0x01FE` 广播（`>= -88 dBm`），直接自动学习并唤醒银河；
4. **本机旧版配置无感迁移**：若检测到本机旧版 SharedPreferences 已有车辆尾号/MAC，首次启动自动迁移；
5. **可选手动输入车架号后 3 位**：UI 提供可选按钮，输入 3 位尾号（如 `123`）即可直接生成 `GeelyVehicle123` + 尾号 `23` 过滤。

v4.0.4区分“匹配提示”与“真实地址已获得”。手动尾号和旧版迁移标明来源；更换尾号不能继承另一辆车的旧MAC。缺蓝牙权限时无法扫描或读取连接设备，缺通知使用权时本应用也无法确认银河状态。清空数据后需要重新授权。

重学先读取系统及本应用当前可验证的已连接设备，再清除旧状态并重建扫描。等待断连的标志不再挡住真实当前连接的识别。若系统没有提供地址、设备名称不可识别且没有近期广播，本应用明确等待银河断开再连接，不会仅凭“银河已连接”绑定匿名地址。

学习后优先按以下顺序匹配：

1. BLE 地址完全一致；
2. 广播名称完全一致；
3. VIN 后三位 + 厂商数据签名一致；
4. VIN 后两位 + 厂商数据签名一致。

车机蓝牙模块重启导致 MAC 轮换时，名称/VIN 尾号仍能命中；下一次广播出现会自动更新持久化地址。

### 4.3 为什么不需要完整 VIN

对当前银河 1.56.1 反编译 `com.ecarx.blekey.thirdadapter.service.MiuiService` 后确认：

- `onStartCommand` 只要求 `setCarId` 非空、`ScanResult` 非空；
- `MiCarLinkInteractorImpl.connectDevice(carId, scanResultJson)` 把 `ScanResult` 交给官方钥匙 SDK；
- `BaseInteractor.checkDevice()` 默认返回 `true`，`carId` 只用于日志/回执标识；
- 真正的车辆身份校验、钥匙凭证和 V2 鉴权仍在银河官方 App 内完成。

因此本 App 传入广播名（如 `GeelyVehicle123`）作为非空 `carId` 即可，不需要、也不应该写死用户 VIN。

---

## 5. 运行流程

```text
BleScanner 扫描
  → 解析吉利广播
  → 身份未学习：固定一个主候选 12s，避免多车 RSSI 混流
  → RSSI ≥ -95dBm：把 ScanResult 注入官方 MiuiService / MiuiReceiver / GLDkConnectionService
  → 官方 App 完成 V2 鉴权并显示“已连接”
  → 本 App 自动学习本车身份
  → 官方通知就绪事件单独处理；GATT 已连不代替官方鉴权就绪
  → 仅官方就绪后挂载 GATT，500ms 至多一个在途 readRemoteRssi()
  → 接近 RSSI ≥ -75dBm：请求一次“解锁”
  → 未补偿距离 RSSI ≤ -82dBm 连续 4.5s / ≥3 新样本，且6s近场保护结束：请求“上锁”
```

当前车控路径（固定等待预算，实际只选择一种发送方式）：

```text
PassiveKeyService / PendingControl
  → 创建唯一请求，固定12秒超时
  → 官方就绪回调立即尝试；每次发送前核验距离、插线、行驶与开关
KeyNotificationBridgeService
  → inflate 官方通知 RemoteViews
  → 找到当前动作对应的 PendingIntent；无法提取PI时使用主线程 View.performClick()
  → 成功交付后停止重发；明确未发送时有限重试；结果未知时停止并显示核实提示
```

原 DK1.5 反转映射（Unlock=2、Lock=1）与约 3.07s 的 V2 鉴权结论仍保留备查。v4.0.1 自动控制不再把广播兜底、PI、View 和通知待发队列同时下发，以免重复解闭锁。每条请求至多两次明确未发送后的尝试，同一接近每个动作至多两条请求；持续站在远处不会无限补发。

**防罚站双触发机制**：
- 吉利车机只有 **1 个 BLE 连接槽位**，一旦银河 App 抢先连上车机，车机立即停止发送广播。如果此时还没达到 `-75 dBm`，单靠广播 RSSI 会因为“广播消失”而漏触发。
- 因此官方通知刚变为“已连接”（`onOfficialConnected`）时处理一次解锁机会。已有待发动作时仅完成它；新鲜远场、行驶、插线和锁后回程保护仍拦截即时解锁。
- 锁后官方连接仍保持时，必须未补偿信号回到近场并持续1秒才允许下一次接近；远场边界抖动和口袋补偿不能反复锁/解。真正断连且离开足够时间后按原静默规则重新武装。

通知按钮点击等于用户手动点击官方通知栏按钮，不依赖车机“靠近解锁”被动 PE 开关。

---

## 6. 状态机与安全约束

`PassiveKeyLogic` 的核心规则：

| 规则 | 说明 |
| :--- | :--- |
| 服务总开关 | 支持在 App 内一键关闭「无感进出服务总开关」，关闭后立即停止扫描、断开 GATT、取消所有待发重发任务，也禁止手动测试 |
| 插充电线禁控 | 默认开启「插数据线/充电线后禁止解闭锁」：检测到 USB/AC 有线充电时（无线充电不拦截），禁止一切自动与手动解锁、上锁 |
| 解锁（支持自定义） | 默认接近 RSSI ≥ `-75 dBm`，支持口袋补偿，一次接近成功交付后不再重复。RSSI 与实际距离受遮挡和车型影响 |
| 上锁（支持自定义） | 默认未补偿滤波 RSSI ≤ `-82 dBm`，连续 `4.5s` 且至少 3 个新样本；近场保护与驻留并行。发送前重新核验距离和禁控条件 |
| 无信号过期清零 | 超过 6 秒无新 RSSI 样本立即回退显示 `-- · 不在车辆附近 (无信号)`，不再残留断连前的最后一条弱信号（如 `-93 dBm`） |
| 固定日志容量 | UI 最近10条；关键事件4份×256KiB、诊断6份×512KiB，合计上限4MiB；有界异步队列，支持清空全部日志分段 |
| 日志导出 | 底部“导出排查日志（ZIP）”，通过系统文件选择器保存；含版本、状态、关键事件与诊断，身份脱敏，不自动上传 |
| 学习未完成 | 只有尾号/名称但无实际蓝牙地址时，不视为学习完成，也不执行自动车控 |
| 近场保护 | 默认最近6秒出现 `-78 dBm` 以上信号则禁止发送上锁，但连续远场可以同时计时，避免额外串行等待 |
| 靠近与离开阶段 | 提前解锁不自动获得上锁资格；未补偿RSSI真正进入现有近场保护区后才启用正常远离判定。若提前解锁后直接转身离开，须同源信号比本阶段峰值下降至少6dB并满足原远场驻留 |
| 冷启动 | 从未接近过车辆时，一直在远处不会补发上锁 |
| 断连 | GATT/扫描失败立即清空驻留，并禁止自动上锁 10 秒 |
| 蓝牙关闭 | 禁止自动上锁 15 秒，避免把蓝牙端口重启当成“人已走远” |
| 行车 | 银河通知明确显示“行驶中”时，不上锁、不重新武装；也不下发解锁 |
| 重新武装 | 正常会话按持续远场加断连/静默规则；上锁后要求确认真实返回。失败预算耗尽时允许“远场8秒→真实近场1秒”恢复，不依赖官方必须先断连 |
| 采样断档 | 超过3秒无新样本，驻留失效；重复时间戳、乱序或过期扫描不累计为新鲜样本 |
| 发送状态 | 仅通知发送成功累加“指令已交付”次数；这不是车辆执行回执。失败/取消/超时/结果未知均明确更新最近动作 |
| 通知监听恢复 | 监听未授权时提示开启；已授权但系统未绑定则每2秒请求重绑，10秒仍失败在常驻通知和权限卡提示“系统未绑定”，用户点“修复通知监听”在系统页关闭再开启；连接后每1.5秒补扫缺失的现有银河通知。不做会杀进程的组件禁用 |
| 手动链路测试 | 仅在官方已连接且按钮可用时单次发送；不要求RSSI或身份学习，不受自动动作分开关限制，仍复核总开关、插线与行驶保护。无连接不排队、失败不自动重试，2秒防连点 |
| 手动上锁锁存 | 同一接近会话不再自动解锁或重复上锁；真实离开并断连后恢复下一次自动靠近，手动解锁可立即解除锁存 |

滤波：三点中值抑制单帧尖峰，按真实样本时间间隔计算 EMA（接近时间常数350ms、远离750ms），消除原低增益 Kalman 与 EMA 串联滞后。遮挡状态连续稳定800ms才改变，`+8dBm` 仅用于接近；上锁和近场保护使用未加补偿的信号。

息屏保活：

- `connectedDevice` 前台服务；
- 屏幕关闭且最近 5 分钟见过车辆/已连接时持有 `PARTIAL_WAKE_LOCK`；
- 屏幕关闭后 45 秒无广播、银河未连接时重建一次 BLE 扫描器（90 秒节流）；
- 通知监听服务掉线时每2秒请求系统重绑；系统长期拒绝无界面重绑时由“修复通知监听”引导用户重新授权；
- 扫描器未注册时服务会自动重试；覆盖安装/开机由接收器和2分钟看门狗尝试拉起服务，用户关闭总开关时取消看门狗；
- 开机、应用更新自动拉起。

---

## 7. 权限

必需：

- 蓝牙扫描/连接（Android 12+：`BLUETOOTH_SCAN`、`BLUETOOTH_CONNECT`；Android 11 及以下：精确位置）；
- 通知使用权（系统设置 → 通知使用权 → 银河无感钥匙）；
- 电池优化白名单。

建议：

- OriginOS/vivo：设置 → 电池 → 后台耗电管理 → 允许后台高耗电；
- i管家 → 自启动 → 允许本应用。

没有通知使用权时，App 仍能扫描和显示信号，但无法点击官方通知栏的解锁/上锁按钮。

“已授权 · 系统未绑定”表示系统仍保留授权记录，但本应用的监听实例没有恢复；点“修复通知监听”进入系统详情页，关闭再开启一次即可。部分vivo/OriginOS在低内存回收或覆盖安装后会进入该状态，属于系统监听绑定行为，不是银河App未连接。

---

## 8. 首次使用

1. 安装并登录银河官方 App，确认蓝牙钥匙已绑定；
2. 安装 `GeelyPassiveKey.apk`；
3. 打开 App，授予蓝牙权限、通知使用权、电池白名单；
4. 靠近车辆，让银河官方 App 成功连接一次；
5. 本 App 日志出现：

```text
✓ 已自动识别本车蓝牙: addr_id=... name_id=... identity_id=...
✓ 银河蓝牙钥匙已连接
✓ 已挂载车机 ACL，开始读取连接态 RSSI
```

6. 之后保持本 App 后台运行即可。息屏放口袋，靠近解锁，离开上锁。

换车或更换蓝牙模块后，点「重新学习本车」，再用银河 App 连接一次。

---

## 9. 构建与测试

```bash
bash /path/to/car_key_re/GeelyKeyRadarApp/build.sh
```

输出：

```text
/path/to/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey.apk
```

脚本先运行 `PassiveKeyLogicTest`、`RssiPipelineTest`、`PendingControlTest`、`NotificationEpochTest`、`RotatingFileLogTest`、`DiagnosticsTest`、`VehicleLearningStateTest`，再编译资源、Java、DEX、签名，并在检测到真机时尝试安装。仅构建可使用 `SKIP_INSTALL=1 bash GeelyKeyRadarApp/build.sh`。

实车调试：

```bash
adb logcat -s PassiveDiagnostics PassiveKey GeelyBridge KeyNotifBridge
adb shell run-as com.luca.geelykeyradar cat files/passive_key.log
adb shell run-as com.luca.geelykeyradar cat files/passive_key.log.1
adb shell run-as com.luca.geelykeyradar cat files/passive_key_diagnostic.log
```

单测覆盖：

- 一次接近只解锁一次；
- 远离驻留/样本数不足不上锁；
- 冷启动远处不补锁；
- 断连不误锁；
- 真正离开后重新武装；
- 行车中不上锁、不发解锁；
- 中间缓冲带取消上锁倒计时；
- 车内近场保护窗口内不误锁。
- 不同采样频率的原始信号→滤波→动作完整回放、口袋上锁与手持一致；
- 锁后边界抖动不解锁、发送失败有限恢复、旧通知与迟到移除隔离；
- 重试期限/次数上限、单次发送完成、日志容量与诊断隔离。
- 官方提前解锁后仍在接近/停留不误锁、未完成接近便转身可锁、广播/GATT切换不累计旧驻留。
- 手动解锁不重复或立即反向锁车、手动上锁不被近场自动撤销、真实离开后恢复、手动操作使旧自动待发失效。
- 通知监听掉绑状态、2秒重绑节流、监听连接后补扫已存在银河通知。
- 蓝牙权限与系统通知权限拆分、锁屏扫描心跳、服务看门狗恢复。
- 原因字段与实际返回分支一致、只读快照不改变动作、服务未正常结束标记、旧日志脱敏和ZIP白名单。

日常排查优先在 App 中导出 ZIP；固定分段容量、事件字段和原因释义见文档39。

---

## 10. 已知边界

1. **必须有银河官方 App**：车钥匙凭证、V2 鉴权和通知栏按钮都来自官方 App；本 App 只做雷达和桥接。
2. **通知栏控件依赖官方版本**：当前按资源 ID `0x7f0a10c7/0x7f0a10cb` + 文本“解锁/上锁”双路径查找；官方大版本改版后可能需要更新 ID。
3. **首次学习需要官方 App 连接一次**：如果官方 App 在后台已经被系统冻结，先手动打开一次。
4. **多辆吉利车相邻**：身份未学习时只让一个主候选进入 RSSI 流；官方 App 的钥匙 SDK 仍会做最终车辆校验。若注入错车，官方 App 不会鉴权成功，本 App 会在主候选变化后重试。
5. **ROM 差异**：部分 ROM 息屏会暂停 BLE 扫描；App 会重建扫描器，但仍建议按第 7 节设置后台高耗电。
6. **非 vivo 手机的 5 秒远距连接延迟坑（已定位，暂未改代码）**：
   - **逆向位置**：银河 `dk_core.dex` 中的 `com.geely.icc.common.business.alive.ble.AbsBleStrategy.connectBle(ScanResult, Function1)`：
     ```java
     if (PhoneModelUtil.Companion.isPhoneVIVO()) {
         subConnectBle(scanResult, back); // vivo 手机直接连，不判断 RSSI
         return;
     }
     int min_conn_rssi = aliveStrategy != null ? aliveStrategy.getMIN_CONN_RSSI() : -1000;
     if ((scanResult != null ? scanResult.getRssi() : min_conn_rssi) >= min_conn_rssi) {
         subConnectBle(scanResult, back);
         return;
     }
     // 非 vivo 手机且 RSSI 低于阈值时，会被丢进 connectDelayThread 延迟 5000ms (5秒) 再发起连接！
     ```
   - **现象**：在小米 / OPPO / 华为 / 三星等非 vivo 手机上，远距离扫到弱信号广播注入 `MiuiService` 时，若 `ScanResult.getRssi()` 低于银河内部门槛，银河会故意延迟 **5 秒** 才调用 `subConnectBle`。
   - **后续优化方案（待需要时启用）**：在 `GeelyBridge.wakeAndConnect()` 向银河 `MiuiService` 注入 `ScanResult` 前，构造一个 `rssi = -55`（强信号）的 `ScanResult` 副本传给银河（本 App 自身距离状态机仍使用真实 `result.getRssi()`），即可让所有品牌手机都走 0 秒直连分支。
   - **各品牌 ROM 保活设置对照**：
     - **vivo (OriginOS)**：设置 → 电池 → 后台耗电管理 → 允许后台高耗电；i管家 → 自启动 → 允许
     - **小米 / 红米 (HyperOS)**：应用设置 → 省电策略 → 无限制；开启「自启动」
     - **OPPO / 一加 (ColorOS)**：应用详情 → 耗电管理 → 允许完全后台行为 + 允许自启动
     - **华为 / 荣耀**：应用启动管理 → 关闭自动管理 → 手动开启「自启动 / 后台活动 / 关联启动」
     - **三星 (One UI)**：应用详情 → 电池 → 不受限制

---

## 11. 与旧版的关系

- 包名不变：`com.luca.geelykeyradar`；
- 通知监听组件名不变：`KeyNotificationBridgeService`，升级安装可保留授权；
- 旧版源码/资源/APK 在 `GeelyKeyRadarApp/legacy/v3.20.6/`；
- 旧版文档 `docs/01` ~ `docs/35` 仍保留，作为逆向知识与排查参考；
- 新版只覆盖 `GeelyKeyRadarApp/src`、`res`、`AndroidManifest.xml`、`build.sh`。
