# 36. v4.0 极简通用无感钥匙：只保留息屏靠近解锁 / 离开上锁

## 1. 目标

把原「银河钥匙雷达 v3.20.6」裁剪成一个功能极简、可通用分发的 App：

- 核心只保留：**息屏靠近解锁、离开上锁**，另提供两个用户主动点击的车控链路测试按钮；
- 不要求用户填写 VIN、MAC、车型或任何逆向参数；
- 不依赖电脑 adb、REA、私有数据或 root；
- 用户安装 App、用银河官方 App 连接一次车辆后，本 App 自动识别本车蓝牙身份；
- 保留原包名 `com.luca.geelykeyradar`，升级安装后通知监听授权通常可以继续沿用；若系统只保留授权但不再绑定监听，使用 App 内“修复通知监听”重新开关一次。

当前版本：`v4.2.2-min / versionCode 202`（已安装到手机）。通知快速补扫、单MiuiService、强信号匿名首包、GATT cmdId=5鉴权和PI缓存见 [56_v4_2_0_fast_auth_and_notification_fallback.md](56_v4_2_0_fast_auth_and_notification_fallback.md)；锁屏补扫提前停止修复、50ms/15s窗口和无通知权限边界见 [57_v4_2_1_lockscreen_fast_rescan.md](57_v4_2_1_lockscreen_fast_rescan.md)；长时无车后的弱信号提前连接与诊断见 [58_v4_2_2_long_absence_weak_connect.md](58_v4_2_2_long_absence_weak_connect.md)。阈值线对比度优化见 [55_v4_1_8_threshold_line_contrast.md](55_v4_1_8_threshold_line_contrast.md)。蓝牙信号实时曲线见 [54_v4_1_7_realtime_rssi_chart.md](54_v4_1_7_realtime_rssi_chart.md)。车机参数只读探针见 [53_v4_1_6_readonly_vehicle_param_probe.md](53_v4_1_6_readonly_vehicle_param_probe.md)。稳定VIN掩码发现见 [52_v4_1_5_stable_mask_discovery.md](52_v4_1_5_stable_mask_discovery.md)。首包预连接优化见 [50_v4_1_4_early_connection_latency.md](50_v4_1_4_early_connection_latency.md)。阈值设置UI见 [49_v4_1_3_threshold_dialog_ui.md](49_v4_1_3_threshold_dialog_ui.md)。主页版本与APK自动命名见 [48_v4_1_2_version_display_and_apk_naming.md](48_v4_1_2_version_display_and_apk_naming.md)。多车过滤与身份隔离见 [47_v4_1_1_multicar_filter_and_identity.md](47_v4_1_1_multicar_filter_and_identity.md)。后台锁定说明见 [46_v4_0_9_background_lock_guide.md](46_v4_0_9_background_lock_guide.md)，电池白名单反馈见 [45_v4_0_8_battery_whitelist_feedback.md](45_v4_0_8_battery_whitelist_feedback.md)，后台扫描与权限拆分见 [44_v4_0_7_background_scan_and_permissions.md](44_v4_0_7_background_scan_and_permissions.md)。

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
├── RssiChartSeries.java            # 最近60秒RSSI样本、断点与固定容量
├── RssiChartView.java              # 原生Canvas原始/平滑RSSI与阈值曲线
├── GattAuthFrameParser.java        # DK1.5明文头/cmdId=5鉴权成功帧
├── OfficialReadyGate.java         # 官方连接与真实身份交错时的一次就绪机会
├── PendingControl.java             # 唯一待发请求、固定期限、有限发送尝试
├── NotificationEpoch.java          # 拦截旧通知解析与迟到移除
├── RotatingFileLog.java            # 固定文件数及容量的轮转日志
├── DiagnosticLog.java              # 跨模块日志、进程退出与服务运行记录
├── DiagnosticLogStore.java         # 有界队列、固定容量、脱敏ZIP导出
├── DiagnosticText.java             # 新旧日志的身份与凭证脱敏
├── DiagnosticCounters.java         # 扫描与读取的累计/窗口计数
├── VehicleBeacon.java              # 纯Java广播解析、公开身份字段与物理MAC键
├── GeelyAdvertisement.java         # Android ScanRecord/ScanResult包装，不搜索原始子串
├── VehicleTargetSelector.java      # 单物理目标、观察预算、轮换确认与失败冷却
├── VehicleWakeGate.java            # 独立预连接、单个在途、同MAC节流与ACL握手保护
├── VehicleDiscoverySignature.java  # 官方V2稳定VIN掩码、尾号校验和厂商payload裁剪
├── VehicleParamCodec.java          # 车机只读回执解析与写操作硬白名单
├── VehicleParameterProbe.java      # AIDL自动读取PE/车况/标定原始回执并持久化
├── VehicleScanPolicy.java          # 学习/跟踪过滤计划、厂商掩码与扫描重建限频
├── VehicleIdentity.java            # 本车蓝牙身份与匹配规则
├── VehicleIdentityStore.java       # 身份持久化 + 两个自动开关
├── VehicleLearningState.java       # 地址学习完成资格、来源和权限/重连提示
├── GeelyBridge.java                # 最小化调用银河官方导出组件
├── KeyNotificationBridgeService.java # 读取官方通知状态 + 点击官方解锁/上锁按钮
├── ThresholdSettingsDialog.java   # 有标签/说明/单位/错误提示的原生阈值弹窗
├── ThresholdSettingsForm.java     # 表单校验、正数兼容、精确格式化与预览
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
- 因此所有模式都提供非空 `ScanFilter`；首次学习才使用 Company ID `0x01FE/0x06FE/0xFF15` 和 UUID `0xFDFD` 协议过滤。
- **学会本车后替换为本车 MAC、精确 `GeelyVehicle*` 名称、稳定 VIN 掩码，删除裸 Company ID/UUID 宽过滤。** 厂商规则优先使用 `FF 00 FF 00 FF FF`（vendor+antenna+VIN后两位），并用 `FF 00 00 00 FF FF`（vendor+VIN后两位）兜底天线变化；列表是 OR，不能只是把本车条件追加进去。名称/掩码用于发现 MAC 轮换，不代表所有通过硬件的匿名设备都可信。
- 厂商数据按 `ScanRecord` 的 AD 结构读取；裸 `FDFD + 任意非空名称`、在任意负载中搜索出的 `15FF` 子串不再作为车辆证据。编码只处理短前缀，避免逐包转换全部原始字节。

解析结果只保存广播里本来就公开的字段：

- BLE 地址；
- 广播名称；
- VIN 后两位/后三位；
- 厂商 ID 和 vendor/version/固定位/VIN 尾号的短前缀（通常6字节；旧15FF存储包含2字节Company ID）。前缀不是唯一车辆凭证。

**不保存 VIN 全码、账号、token、证书或吉利私有存储。**
运行时不读取 `/sdcard/Android/data/com.geelygalaxy.customer/`、官方 App 私有 SharedPreferences/MMKV 或 REA 导出的内存数据。

### 4.2 本车身份自动学习

学习时机与多通道来源（无需用户手工配置，也支持可选填入车架号后 3 位）：

1. **系统级 BLE GATT / ACL 广播监听**：监听 `android.bluetooth.gatt.profile.action.CONNECTION_STATE_CHANGED`（含 `bd_addr`）与 `ACTION_ACL_CONNECTED`，按已学习名称、尾号或近期广播校验本车。地址轮换但没有任何身份依据时不盲目绑定匿名设备；
2. **系统 `BluetoothManager.getConnectedDevices(GATT)`**：匹配 `GeelyVehicle*` 名称；
3. **首次观察而非第一包绑定**：物理 MAC 去重，先观察至少1.5秒/至少2条新鲜广播，再选择唯一主候选并把广播交给官方；不再仅凭当前 `candidates.size()==1` 自动记住第一辆。完成学习依赖官方通知已连接及系统唯一可验证的匹配 GATT 设备；
4. **本机旧版配置无感迁移**：若检测到本机旧版 SharedPreferences 已有车辆尾号/MAC，首次启动自动迁移；
5. **可选手动输入车架号后 3 位**：UI 提供可选按钮，输入 3 位尾号（如 `123`）即可直接生成 `GeelyVehicle123` + 尾号 `23` 过滤。

v4.0.4区分“匹配提示”与“真实地址已获得”。手动尾号和旧版迁移标明来源；更换尾号不能继承另一辆车的旧MAC。缺蓝牙权限时无法扫描或读取连接设备，缺通知使用权时本应用也无法确认银河状态。清空数据后需要重新授权。

重学先读取系统及本应用当前可验证的已连接设备，再清除旧状态并重建扫描。等待断连的标志不再挡住真实当前连接的识别。若系统没有提供地址、设备名称不可识别且没有近期广播，本应用明确等待银河断开再连接，不会仅凭“银河已连接”绑定匿名地址。

v4.1.1 的匹配与学习分级：

1. **冲突优先**：明确不同 `GeelyVehicle*` 名称或尾号先拒绝，MAC 相同也不能绕过。
2. **同 MAC**：新鲜广播可进入唯一测距目标；同 MAC 的名称补全不增加物理候选数。
3. **完整名称轮换**：旧目标停更超过3秒、新地址至少2条同名广播且跨600ms、官方未连接时，才选择新的单一发现候选，真实连接确认后更新地址；名称尾号可能重名，跨MAC不直接测距。
4. **匿名尾号/短前缀（HINT）**：只限制找车并尝试官方鉴权。即使尾号和短前缀都相同，也不直接更新地址、不进入 RSSI。只填尾号同样不是完成学习。
5. **连接确认**：官方已就绪，系统匹配连接按地址去重后只有一个，并且连接名称/同 MAC 广播不冲突，才允许采用匿名轮换地址。多个匹配连接拒绝任取第一辆，不能仅凭通知在线或 `lastWakeAd` 猜测绑定。

候选以物理 MAC 为键，容量32，20秒过期，定时清理。未知主候选最多尝试12秒（从选中时起算，持续广播不续期），失败后冷却30秒再试；已学习且新鲜的本车 MAC 不受该未知候选预算限制。官方已有连接时固定已验证目标，不因邻车广播抢地址。

**v4.1.4新增独立预连接门控：**已保存合法本车MAC与完整名称，当前`ScanRecord`确实携带同一个完整名称、Company ID/vendor/VIN尾号一致，广播新鲜且RSSI≥-95时，可在普通名称确认前将首包交给官方。版本/中间状态字节允许变化，完整六字节前缀不是唯一凭证；缓存`BluetoothDevice.getName()`、匿名HINT、只有名称而无厂商数据、手填未学地址均不走快速路径。旧本车MAC仍新鲜或官方在线时不抢邻车。

**v4.1.5补齐发现过滤：**已学习车辆的厂商规则不再要求六字节全等。版本/devices变化走优先掩码，antenna变化走两字节VIN兜底；软件HINT使用同一掩码，但Company ID仍严格一致、冲突优先。任何稳定掩码匿名包都只进入候选，仍需至少2条样本/1500ms观察且不获得RSSI，不修改已学地址，不生成车控。详见 [52_v4_1_5_stable_mask_discovery.md](52_v4_1_5_stable_mask_discovery.md)。

**v4.1.6新增车机参数只读探针：**设计上绑定银河官方`DigitalKeyBgService`，发送`5392=2`（`0x0501` PE状态）、`272=0`（`0x0121`车况）和`305=0`（`0x0131`标定读取请求），记录`0x0132`等原始回执。发送前统一经过硬白名单，PE开/关、`0x0195`标定写入和`0x0170`距离档位均被拒绝。当前手机银河`1.56.1`实际Manifest将`DigitalKeyBgService`设为`exported=false`，普通第三方无法绑定，界面明确显示AIDL不可用；不会把PE开关或推测值冒充RSSI阈值。详见 [53_v4_1_6_readonly_vehicle_param_probe.md](53_v4_1_6_readonly_vehicle_param_probe.md)。

**v4.1.7新增实时RSSI曲线：**页面每500ms取现有运行态快照，绘制最近60秒原始RSSI（蓝）、平滑RSSI（黄）和当前解锁/上锁阈值（绿/红虚线）。无新鲜信号时曲线断开，不把`-127`画成真实弱信号；最多保留160点，不新增后台线程或扫描请求。曲线只用于观察，不参与解锁/上锁判定。详见 [54_v4_1_7_realtime_rssi_chart.md](54_v4_1_7_realtime_rssi_chart.md)。

**v4.2.0提速与通知回退：**WAKE后高频补扫官方通知；只发送一次MiuiService主入口，1200ms无ACL才兼容兜底；已学习身份下强信号稳定尾号匿名首包可提前连接。ACL后附加只读GATT观察，DK1.5收到`cmdId=5/CMD_SESSION_RSP15`即确认官方鉴权完成，并复用已捕获或私有缓存的官方PendingIntent，不必等锁屏通知刷新。若用户从未授予通知使用权且没有历史PI缓存，仍无法获得官方按钮token。v4.2.1进一步改为每50ms补扫、持续15秒，并且只有通知已连接且当前按钮捕获后才停止；单纯出现“未连接”通知不会提前结束补扫。详见 [57_v4_2_1_lockscreen_fast_rescan.md](57_v4_2_1_lockscreen_fast_rescan.md) 与 [56_v4_2_0_fast_auth_and_notification_fallback.md](56_v4_2_0_fast_auth_and_notification_fallback.md)。

提前连接不保存新MAC、不参与RSSI/阈值/近场保护；后续身份采用仍走上述真实连接验证。新增首包会话若官方通知先到但本次物理连接证据未验证，则不凭旧保存MAC消费就绪车控机会或挂GATT，每秒复核唯一实际连接，采用后继续；原已学MAC/稳定上锁会话不加该等待。所有发现共用一个在途目标和12秒预算（首包实际尝试早于选择时从更早时刻算）、30秒失败冷却。目标确认不清除同MAC节流或ACL五秒握手保护；没有ACL时真正已验证的MAC可以抢回唯一尝试通道。仅停止过期预连接尝试，不停止扫描或已验证连接态测距。

身份补全后只在过滤内容变化时合并重配置（750ms），所有扫描启动共用“最多4次/30秒、间隔至少1秒”预算；等预算时保留旧扫描。物理测距目标切换清除旧滤波和驻留，但不清掉已交付/手动上锁锁存，避免重复或反向车控。

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
  → 身份未学习：观察1.5s / ≥2样本，选一个主候选，12s硬预算/失败冷却30s
  → 身份已学习：本车窄过滤；完整本车名＋厂商/VIN首包可独立提前连接
  → 普通NAME/HINT候选保留原观察；不保存未验证新MAC、不供给RSSI
  → RSSI ≥ -95dBm且无同MAC ACL握手保护：把真实 ScanResult 注入官方 MiuiService / MiuiReceiver / GLDkConnectionService
  → 官方 App 完成 V2 鉴权并显示“已连接”
  → 本 App 用官方就绪 + 唯一可验证的系统连接完成身份学习
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
2. 安装对应版本的 APK（当前本地构建为 `GeelyPassiveKey-v4.2.2-min-202.apk`）；
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
/path/to/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.2.2-min-202.apk
```

脚本先运行 `PassiveKeyLogicTest`、`RssiPipelineTest`、`RssiChartSeriesTest`、`GattAuthFrameParserTest`、`PendingControlTest`、`NotificationEpochTest`、`RotatingFileLogTest`、`DiagnosticsTest`、`VehicleLearningStateTest`、`VehicleIdentityTest`、`VehicleTargetSelectorTest`、`VehicleWakeGateTest`、`VehicleScanPolicyTest`、`OfficialReadyGateTest`、`ThresholdSettingsFormTest`、`VehicleParamCodecTest`这16组Java测试，以及 `tests/version_naming_test.sh`命名回归，再编译资源、Java、DEX、签名，并在检测到真机时尝试安装。仅构建可使用 `SKIP_INSTALL=1 bash GeelyKeyRadarApp/build.sh`。

自定义阈值窗口采用三项解释卡片，单位/范围/推荐值永久可见；非法值不关闭弹窗，推荐值不直接写配置，保存前统一校验，详见文档49。

APK名从Manifest的 `versionName/versionCode` 自动生成，未来只调整版本配置；界面从实际安装包读取同一版本。`bash GeelyKeyRadarApp/build.sh --print-apk-path` 仅查询输出路径，不触发构建/安装。详情见文档48。

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
- 多车身份冲突/匿名尾号与短前缀拒绝、MAC与ScanResponse去重、地址轮换确认、候选硬预算/失败冷却/容量、学习到跟踪过滤切换、重建限频，以及两条弱邻车夹入时的完整RSSI回放。
- 稳定四字节VIN与两字节VIN兜底、动态version/antenna/devices、其他99个尾号排除、Company/名称冲突，以及匿名HINT不获得RSSI/不覆盖地址。

新增预连接回归覆盖首包-78、名称稀疏/状态前缀变化、缓存名/匿名/弱/过期/重复拒绝、单个在途、可信MAC优先、选择后同MAC节流/ACL不重置、预算/冷却及上锁逐样本不变。合成稀疏名称案例：普通选择4026ms，早期请求13ms；这是源码时序，不是保证实车省4秒，详见文档50。

日常排查优先在 App 中导出 ZIP；固定分段容量、事件字段和原因释义见文档39。

---

## 10. 已知边界

1. **必须有银河官方 App**：车钥匙凭证、V2 鉴权和通知栏按钮都来自官方 App；本 App 只做雷达和桥接。
2. **通知栏控件依赖官方版本**：当前按资源 ID `0x7f0a10c7/0x7f0a10cb` + 文本“解锁/上锁”双路径查找；官方大版本改版后可能需要更新 ID。
3. **首次学习需要官方 App 连接一次**：如果官方 App 在后台已经被系统冻结，先手动打开一次。
4. **多辆吉利车相邻**：未学习/匿名弱线索只尝试唯一候选，不参与RSSI；错候选12秒预算后切换并冷却，官方SDK仍负责最终车辆鉴权。已学习后采用本车稳定VIN掩码和单MAC测距；稳定掩码只扩大发现，同名/同尾号不是全局唯一身份，系统匹配连接有歧义时不盲绑。
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
