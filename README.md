# 银河无感钥匙 · GeelyPassiveKey

把吉利银河的「靠近解锁 / 远离上锁」重做一遍：**手机自己判断你在靠近还是离开，然后替你把银河 App 的蓝牙钥匙连上、把锁打开。**

车机里那个「无感进出」开关可以一直关着，本 App 不依赖它。

[⬇️ 下载 APK](https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest) · [v4.0 设计说明](docs/36_minimal_generic_passive_key.md) · [逆向知识库](docs/)

当前本地版本 **v4.2.2-min（已安装到手机）**：一次唤醒只走一个`MiuiService`主入口，1200ms无ACL才兼容兜底；通知监听窗口每50ms主动补扫银河通知并持续15秒，只有“已连接且官方按钮已经捕获”才停止，避免锁屏时先出现“未连接”通知就过早结束补扫；已学习身份下`-70dBm`稳定尾号匿名首包可立即交给官方。新增GATT只读观察，收到DK1.5 `cmdId=5 / CMD_SESSION_RSP15`即提前认定官方鉴权完成，并复用已捕获/私有缓存的官方解锁上锁PendingIntent。详见 [v4.2.2长时无车弱信号连接](docs/58_v4_2_2_long_absence_weak_connect.md)、[v4.2.1锁屏补扫修复](docs/57_v4_2_1_lockscreen_fast_rescan.md) 和 [v4.2.0提速与通知回退](docs/56_v4_2_0_fast_auth_and_notification_fallback.md)。v4.1.8的 [阈值线优化](docs/55_v4_1_8_threshold_line_contrast.md)、v4.1.7的 [蓝牙实时曲线](docs/54_v4_1_7_realtime_rssi_chart.md)、v4.1.6的 [车机参数只读探针](docs/53_v4_1_6_readonly_vehicle_param_probe.md)、v4.1.5的 [稳定掩码发现](docs/52_v4_1_5_stable_mask_discovery.md)、v4.1.4的 [首包预连接与可信测距分离](docs/50_v4_1_4_early_connection_latency.md) 继续保留。当前银河`1.56.1`仍不导出`DigitalKeyBgService`，车机参数AIDL显示不可用。本轮没有查询或更新GitHub Releases。后台锁定说明见 [v4.0.9说明](docs/46_v4_0_9_background_lock_guide.md)，电池白名单见 [v4.0.8说明](docs/45_v4_0_8_battery_whitelist_feedback.md)，后台扫描见 [v4.0.7说明](docs/44_v4_0_7_background_scan_and_permissions.md)。

---

## 一、解决什么毛病

银河原生的靠近解锁 / 远离上锁，实车用起来两个高频事故：

### 1. 罚站

人已经走到车门边，车不解锁。掏手机 → 打开银河 App → 等它连蓝牙钥匙 → 再点解锁。后面排队的人看着你，你在车门口站着。

### 2. 车旁就被锁

停好车绕到车尾插充电枪，或者去后备箱拿个东西，离开驾驶位没几步，车「咔」一声自己锁了。想再打开还得从头来一遍。

**根因**：车机侧的被动 PE 只有单点 RSSI 阈值，判定粗糙——没有「一次接近只解锁一次」，没有近场保护，也没有驻留时间。而且吉利车机**只有一个 BLE 连接槽位**：银河 App 一旦连上，车机立刻停发广播，纯靠广播 RSSI 的判定当场踩空，人自然就罚站了。

---

## 二、我们怎么做（核心）

**不需要车机开启「靠近解锁 / 远离上锁」。** 车机侧的 `0x0501` 无感 PE 总开关可以一直关着；本 App 不碰它，也不写任何车机参数。

本 App 只做两件事：

**① 自己识别** —— 首次扫描吉利车机的 BLE 协议特征，官方已连接且系统匹配连接唯一时学习身份；之后替换为本车窄过滤，只让一个可信物理MAC进入RSSI。匿名尾号/短前缀只用于发现，不直接改地址或测距。银河连上后改用连接态 `readRemoteRssi()` 继续判断。

**② 调用银河 App 的蓝牙钥匙** ——

- **蓝牙钥匙连接**：把扫到的车机广播注入银河官方 `MiuiService` / `DigitalKeyBgService`，官方钥匙 SDK 自己完成 V2 鉴权、自己连车。
- **蓝牙钥匙解锁 / 上锁**：找到银河常驻通知栏里的「解锁 / 上锁」按钮，通过 PendingIntent 或无法提取 PI 时的 View 点击发送一次；统一等待官方就绪，不再叠加广播与多套重发队列。

所以整条链路严格等效于：

```
你手动打开银河 App  →  等它连上蓝牙钥匙  →  下拉通知栏点「解锁」/「上锁」
```

只是这一串现在由本 App 在你息屏、手机在口袋里的状态下自动完成。

> **钥匙凭证、V2 鉴权、车控指令全部在银河官方 App 内完成。**
> 本 App 不生成钥匙、不伪造凭证、不读官方私有数据（SharedPreferences / MMKV / REA）、不需要 root、不需要 adb。

---

## 三、为什么这样反而不罚站

| 机制 | 作用 |
| :--- | :--- |
| **阈值解释清晰** | 每项有永久标签、作用说明、单位、范围与调节方向；非法输入就地提示，推荐值仅填草稿，保存后生效。 |
| **版本可见** | 主页标题下显示 `版本：v4.2.2-min（构建 202）`，从实际安装包读取，未来升级无需改界面字符串。 |
| **首包提前连接** | 已学完整本车名＋一致厂商/VIN线索不必等第二条命名广播；只是交给官方鉴权，不提前保存MAC或计RSSI，同地址正在ACL握手时不重复注入。 |
| **多车隔离** | 学习后替换协议宽过滤，明确身份冲突先拒绝；匿名弱线索只找车，不改MAC/混RSSI。 |
| **官方就绪即处理** | 官方钥匙通知变成「已连接」立即处理一次接近机会，已有请求仅完成它。GATT 测距已连不会提前冒充官方鉴权就绪。 |
| **通知监听恢复** | 每2秒请求系统重绑并补扫已有银河通知；系统仍拒绝无界面重绑时，常驻通知和权限卡明确显示“系统未绑定”，点“修复通知监听”在系统页关闭再开启即可。 |
| **一次接近只解锁一次** | 成功交付后不重复；锁后仍连着时须真实近场持续1秒才能再次解锁，口袋补偿与边界抖动不算返回。 |
| **近场保护** | 最近6秒还在默认 `-78 dBm` 以上则禁止发送上锁；保护与远离计时同时进行。 |
| **靠近阶段不反向锁车** | 官方连接可以提前解锁；实际进入过车旁后才按原阈值确认离开。没有走到车旁便转身时，需要同一采样来源的持续变弱证据，广播/GATT落差不算离开。 |
| **上锁要驻留** | 默认未补偿 RSSI `≤ -82 dBm` 连续4.5秒、至少3个新样本；进入缓冲带或采样断档即取消计时。 |
| **断连不误锁** | GATT 断开 / 蓝牙重启后 10~15 秒内禁止自动上锁。宁可不锁，也不在你车旁误锁。 |
| **行车不动作** | 银河通知明确显示「行驶中」时，不解锁也不上锁。 |
| **插线禁控** | 手机插着有线充电（例如在车里用 USB 供电）时默认禁止自动与手动解闭锁。 |

默认解锁阈值 `-75 dBm`（实车调过的提前解锁点）、上锁阈值 `-82 dBm`、驻留 `4.5 s`，都能在 App 里改。设置弹窗会说明“更负/更强”对触发时机的影响；RSSI 不对应固定米数。

手机遮挡稳定800ms后，接近信号补偿 `+8 dBm`，上锁信号不补偿。界面最多显示10条关键日志，后台轮转日志总容量上限 **4 MiB**，采样和关键动作分别保存。显示“指令已交付”代表已交给银河，未取得车锁执行回执。

遇到解锁慢或漏锁，先记下大概发生时间，再到 App 底部点 **“导出排查日志（ZIP）”** 保存文件。ZIP 包含版本、配置和完整保留日志；车辆地址和名称会替换为本机指纹，不自动上传。详见 [日志排查说明](docs/39_v4_0_2_diagnostic_logs.md)。

---

## 四、核心仍只做两件事

- ✅ 息屏靠近解锁
- ✅ 离开上锁

仅额外提供「解锁 / 上锁」两个手动测试按钮，方便确认银河通知车控链路。摇一摇、电源键三连、快捷磁贴、桌面组件、里程、胎压、哨兵、语音、充电门控等旧功能仍然删除。旧版实现和它们的逆向记录仍留在 `docs/` 里备查。

手动测试先在银河 App 连接蓝牙钥匙，再点击本应用的按钮；不受RSSI阈值、身份学习状态或两个自动动作分开关限制，但服务总开关、插线禁控和行驶保护仍生效。每次点击只发送一次，不连接后补发、不自动重试，连续操作间隔至少2秒。界面和日志区分“未发送 / 已交给银河 / 结果不确定”；已交付不是车锁执行回执。手动上锁后不会因为仍站在车旁而自动重新解锁，真实离开后恢复下一次自动靠近，也可直接再点手动解锁。

不需要填完整VIN、MAC或车型：先授予本应用蓝牙权限和通知使用权、保持运行，再用银河连接一次车，捕获本车广播/连接事件。银河已经连接且车机停止广播、系统又未提供地址时，需要先在银河断开蓝牙钥匙再连接。填写尾号只建立匹配提示，不能推算MAC。

---

## 五、安装

1. 银河官方 App 已登录、蓝牙钥匙已绑定（凭证在官方 App 里，本 App 不碰）；
2. 装 [最新版本 APK](https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest)；本地构建文件名会包含版本和构建号；
3. 打开 App，授予：蓝牙权限、**通知使用权**、电池优化白名单；
4. 保持本应用运行，靠近车辆，让银河 App 成功连接一次；若银河已经连接而地址尚未识别，先在银河断开蓝牙钥匙再连接；
5. 日志出现下面三行就成了：

```text
✓ 已自动识别本车蓝牙: addr_id=... name_id=... identity_id=...
✓ 银河蓝牙钥匙已连接
✓ 已挂载车机 ACL，开始读取连接态 RSSI
```

之后保持后台运行，息屏放口袋即可。换车或换了蓝牙模块，点「重新学习本车」再连一次。

清空应用数据会撤销权限，需重新授予蓝牙权限与通知使用权。“已有本车配置”会标明旧配置/手填来源；只有获得真实蓝牙地址后才显示学习完成。重学按钮缺权限时直接引导授权，不会凭空生成地址。

**没有通知使用权**时 App 仍能扫描、显示信号，但点不到官方通知栏的解锁按钮——这个权限是必需的。系统显示“已授权”但状态为“系统未绑定”时，点 App 内“修复通知监听”，在系统页关闭再开启本应用通知使用权即可重新绑定。

- vivo / OriginOS：设置 → 电池 → 后台耗电管理 → 允许后台高耗电；i管家 → 自启动 → 允许。
- 其他 ROM 若息屏后暂停 BLE 扫描，本 App 会重建扫描器，但仍建议加白名单。

---

## 六、目录

```text
.
├── GeelyKeyRadarApp/
│   ├── build.sh                                # 单测 → 编译 → 签名 → 安装
│   ├── AndroidManifest.xml
│   ├── res/
│   ├── src/com/luca/geelykeyradar/
│   │   ├── PassiveKeyService.java              # 前台服务：扫描、学习、RSSI、GATT、保活
│   │   ├── PassiveKeyLogic.java                # 状态机：一次解锁、驻留上锁、断连不误锁
│   │   ├── GeelyBridge.java                    # 调官方 MiuiService / 通知控制兜底
│   │   ├── KeyNotificationBridgeService.java   # 读官方通知、点解锁/上锁按钮
│   │   ├── VehicleBeacon.java                  # 纯Java广播解析和公开身份字段
│   │   ├── GeelyAdvertisement.java             # Android结构化ScanRecord包装
│   │   ├── VehicleTargetSelector.java          # 单物理MAC、轮换确认和候选预算
│   │   ├── VehicleWakeGate.java                # 独立预连接门控、单个在途与按MAC的ACL保护
│   │   ├── VehicleDiscoverySignature.java      # 官方V2稳定VIN掩码与厂商payload裁剪
│   │   ├── VehicleParamCodec.java              # 车机只读回执解码与写操作硬白名单
│   │   ├── VehicleParameterProbe.java          # AIDL只读PE/车况/标定查询与持久化
│   │   ├── VehicleScanPolicy.java              # 硬件过滤计划与扫描重建限频
│   │   ├── VehicleIdentity.java                # 本车蓝牙身份匹配
│   │   ├── VehicleIdentityStore.java           # 身份持久化 + 阈值开关
│   │   ├── VehicleLearningState.java           # 学习资格、来源与具体等待状态
│   │   ├── RssiFilter.java                     # 三点中值 + 按时间计算的 EMA
│   │   ├── RssiChartSeries.java                # 最近60秒曲线样本与无信号断点
│   │   ├── RssiChartView.java                  # 原生Canvas原始/平滑RSSI与阈值曲线
│   │   ├── GattAuthFrameParser.java            # DK1.5明文头与cmdId=5鉴权完成解析
│   │   ├── OfficialReadyGate.java              # 地址晚到的单次官方就绪机会
│   │   ├── PendingControl.java                 # 唯一待发请求和有限重试
│   │   ├── NotificationEpoch.java              # 旧通知和移除事件隔离
│   │   ├── RotatingFileLog.java                # 固定容量轮转日志
│   │   ├── DiagnosticLog.java                  # 跨模块日志与进程退出记录
│   │   ├── DiagnosticLogStore.java             # 有界写入队列与一致快照导出
│   │   ├── DiagnosticText.java                 # 车辆身份与凭证脱敏
│   │   ├── DiagnosticCounters.java             # 扫描、读取和过滤计数
│   │   ├── ThresholdSettingsDialog.java         # 原生阈值设置卡片、输入、说明与错误反馈
│   │   ├── ThresholdSettingsForm.java           # 纯Java设置校验、预览与小数精度
│   │   ├── MainActivity.java                   # 极简配置页
│   │   └── BootReceiver.java                   # 开机 / 更新自动拉起
│   └── tests/                                  # 纯逻辑单测
└── docs/                                       # 逆向知识库 + 版本演进记录
```

APK 不入库（`.gitignore` 排除 `*.apk`），从 [Releases](https://github.com/yangyongyongyong/GeelyPassiveKey/releases) 下载。

---

## 七、文档

当前版本看 **36**；查协议看 **01**，查误锁根因看 **03**，查完整链路看 **18**。

| 文档 | 内容 |
| :--- | :--- |
| [36](docs/36_minimal_generic_passive_key.md) | **v4.0 极简版**：为什么不需要车机 PE 开关、通用身份学习、状态机与安全约束 |
| [37](docs/37_20261107_slow_unlock_missed_lock_diagnosis.md) | 2026-11-07慢解锁/漏锁排查：已验证缺陷、离线复现及日志证据边界 |
| [38](docs/38_v4_0_1_response_and_control_safety.md) | **v4.0.1响应优化**：信号回放对比、通知与GATT隔离、单队列发送、回归与实车验证事项 |
| [39](docs/39_v4_0_2_diagnostic_logs.md) | **v4.0.2日志增强**：导出操作、日志容量、事件字段、未动作原因与故障复盘步骤 |
| [40](docs/40_v4_0_3_approach_relock_guard.md) | **v4.0.3靠近误锁修复**：提前解锁与离开资格分开、同源轨迹确认、实车日志时间线和回归 |
| [41](docs/41_v4_0_4_vehicle_learning.md) | **v4.0.4本车学习**：清空数据后的权限、已连接无广播、尾号提示与来源区分、重学流程 |
| [42](docs/42_v4_0_5_manual_control_test.md) | **v4.0.5手动测试**：两个按钮、单次通知车控、禁控保护、反馈日志和手动上锁锁存 |
| [43](docs/43_v4_0_6_notification_listener_recovery.md) | **v4.0.6监听恢复**：低内存/覆盖安装掉绑、requestRebind失效证据、一键修复、已有通知补扫 |
| [44](docs/44_v4_0_7_background_scan_and_permissions.md) | **v4.0.7后台扫描**：权限弹窗拆分、扫描自动恢复、服务看门狗、锁屏验证 |
| [45](docs/45_v4_0_8_battery_whitelist_feedback.md) | **v4.0.8电池白名单**：已加入状态、可见跳转、失败回退 |
| [46](docs/46_v4_0_9_background_lock_guide.md) | **v4.0.9后台锁定**：最近任务锁定说明、后台高耗电入口、任务保留 |
| [47](docs/47_v4_1_1_multicar_filter_and_identity.md) | **v4.1.1多车过滤**：窄过滤、弱身份隔离、MAC轮换、真实连接学习、候选硬预算、回归与实车验收 |
| [48](docs/48_v4_1_2_version_display_and_apk_naming.md) | **v4.1.2版本可见**：主页版本/构建号、Manifest驱动的APK名称、无副作用路径查询和命名回归 |
| [49](docs/49_v4_1_3_threshold_dialog_ui.md) | **v4.1.3阈值UI**：三项用途/单位/范围、调节方向、就地校验、推荐草稿、滚动和键盘操作 |
| [50](docs/50_v4_1_4_early_connection_latency.md) | **v4.1.4解锁及时性**：三份实车日志对照、首包预连接与可信测距分离、稀疏名称回归、ACL保护及上锁不变 |
| [52](docs/52_v4_1_5_stable_mask_discovery.md) | **v4.1.5稳定掩码发现**：动态version/antenna/devices、官方V2稀疏位、HINT隔离与上锁不变 |
| [53](docs/53_v4_1_6_readonly_vehicle_param_probe.md) | **v4.1.6车机参数只读**：AIDL自动读取PE/车况/标定回执、持久化、写操作白名单与L6/DK1.5边界 |
| [54](docs/54_v4_1_7_realtime_rssi_chart.md) | **v4.1.7实时曲线**：最近60秒原始/平滑RSSI、动态阈值线、无信号断点与固定容量 |
| [55](docs/55_v4_1_8_threshold_line_contrast.md) | **v4.1.8阈值线优化**：2dp阈值虚线、6dp柔和光晕、11sp标签与实机确认 |
| [56](docs/56_v4_2_0_fast_auth_and_notification_fallback.md) | **v4.2.0提速与通知回退**：单MiuiService、强HINT首包、GATT cmdId=5与PendingIntent缓存 |
| [57](docs/57_v4_2_1_lockscreen_fast_rescan.md) | **v4.2.1锁屏补扫修复**：50ms/15s持续补扫、按钮就绪才停止、无通知权限边界与安装验证 |
| [58](docs/58_v4_2_2_long_absence_weak_connect.md) | **v4.2.2长时无车弱信号连接**：-102预热、单次宽限、等待官方期间重试预算与弱包漏斗日志 |
| [01](docs/01_geely_ble_protocol_and_sdk.md) | 银河脱壳、`0x01FE` / `FF15` 广播格式、GATT UUID、AIDL、通知栏控件 ID |
| [02](docs/02_vivo_wallet_and_iccoa_key.md) | vivo 钱包 / ICCOA 开卡流程与测距公式 |
| [03](docs/03_root_causes_3m_lock_and_disconnect.md) | **3 米误锁、偶现不自动连蓝牙的源码级根因** |
| [06](docs/06_real_car_field_test_and_v2_ble_findings.md) | 实车联调：V2 3.07s 鉴权窗口、单槽位 ACL、5 米信号阶梯 |
| [18](docs/18_phone_key_workflow.md) | **手机钥匙完整链路图**（含 `docs/diagrams/` 流程图） |
| [07](docs/07_recent_reverse_findings_and_v2_2_features.md) | 逆向发现合集：DK1.5 枚举反转、车机 `0x0501` 开关真相、车辆状态枚举 |
| [08](docs/08_threshold_dwell_and_screen_off.md) | 阈值 + 驻留状态机，锁屏失效根因与保活 |
| [23](docs/23_v3_13_cabin_rssi_stabilizer.md) | 车内多径 RSSI 稳定化（Kalman / 中值 / 限速） |
| [26](docs/26_v3_16_ble_loss_no_false_lock.md) | 断连不误锁的取舍 |
| [30](docs/30_v3_20_1_own_car_ble_identity.md) · [34](docs/34_v3_20_5_target_duplicate_isolation.md) | 本车身份识别、多车隔离 |
| [35](docs/35_v3_20_6_approach_unlock_single_shot.md) | 一次接近只解锁一次 |
| [04](docs/04_geely_key_radar_app_architecture.md) · [05](docs/05_vivo_originos6_adb_optimization.md) · [09](docs/09_v2_4_shake_state_and_ui.md)–[29](docs/29_v3_19_accessibility_odometer_tire.md) | 旧版 v2.x–v3.x 功能与踩坑存档（摇一摇、组件、磁贴、里程、充电门控等，v4.0 已删除） |

---

## 八、构建

```bash
SKIP_INSTALL=1 bash GeelyKeyRadarApp/build.sh
```

先跑状态机、RSSI完整流程、曲线序列、GATT鉴权帧、待发请求、通知版本、日志轮转、诊断导出、学习资格、身份匹配、单目标选择、硬件过滤和官方就绪门控、预连接门控、阈值表单校验及车机只读回执 **16组Java测试＋1组APK命名回归**，再 `aapt2` → `javac` → `d8` → `apksigner`。上例仅构建，不操作手机；显式去掉 `SKIP_INSTALL=1` 才尝试原有安装流程。产物：`GeelyKeyRadarApp/GeelyPassiveKey-v4.2.2-min-202.apk`。后续命名统一为 `GeelyPassiveKey-v<versionName>-<versionCode>.apk`，不再生成无版本别名。

仅查询本次版本的预期产物路径（不编译、不安装）：

```bash
bash GeelyKeyRadarApp/build.sh --print-apk-path
```

本地发布压缩包包含当前极简版源码、资源、测试、文档和签名APK；不包含Git目录、构建缓存、签名私钥、本机配置、运行日志、旧版源码归档及历史界面mockups。缺少本地密钥时脚本才会生成调试签名；覆盖已安装版本必须沿用原签名密钥。v4.0.9的旧签名与本机密钥不同，v4.1.1已在用户授权下备份、重新安装并恢复数据。今后沿用现有密钥即可覆盖v4.1.1及后续同签名版本，不需再次卸载或清数据；详情见文档47。

调试：

```bash
adb logcat -s PassiveDiagnostics PassiveKey GeelyBridge KeyNotifBridge
adb shell run-as com.luca.geelykeyradar cat files/passive_key.log
```

---

## 九、已知边界

1. **必须有银河官方 App**：凭证、鉴权、车控按钮全在官方 App 里，本 App 只是「雷达 + 代点」。
2. **依赖官方通知栏控件**：按资源 ID + 「解锁/上锁」文本双路径查找，官方大改版后可能需要更新。
3. **首次要连一次车**：官方 App 被系统冻结时，先手动打开一次完成学习。
4. **多辆吉利车相邻**：未学习/匿名弱线索只尝试一个主候选，不进入RSSI；12秒预算后切换，失败MAC冷却30秒。已学习仅让唯一可信物理目标测距；稳定VIN掩码只扩大发现，不改变“地址+真实连接”的可信边界，同尾号、同短前缀不能当作唯一身份。
5. **ROM 差异**：部分 ROM 息屏会暂停 BLE 扫描，建议按第五节加后台白名单。
6. **非 vivo 手机的 5 秒远距连接延迟坑（已记录，暂未改代码）**：银河 `AbsBleStrategy.connectBle(ScanResult)` 对 `isPhoneVIVO()` 直接发起连接，而对非 vivo 手机会检查 `ScanResult.getRssi() >= min_conn_rssi`，弱信号时放入 `connectDelayThread` 延迟 `5000ms` 再连。后续若在非 vivo 机型上遇到远距唤醒偏慢，可在注入 `MiuiService` 时将 `ScanResult` 副本的 `rssi` 改写为 `-55` 绕过（详见 [docs/36_minimal_generic_passive_key.md](docs/36_minimal_generic_passive_key.md) 第 10 节）。

---

## 免责

仅供自己车辆个人使用。仓库内逆向内容为学习记录，不含钥匙凭证或账号信息。车控有风险，请自行确认安全边界。
