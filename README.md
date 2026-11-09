# 银河无感钥匙 · GeelyPassiveKey

把吉利银河的「靠近解锁 / 远离上锁」重做一遍：**手机自己判断你在靠近还是离开，然后替你把银河 App 的蓝牙钥匙连上、把锁打开。**

车机里那个「无感进出」开关可以一直关着，本 App 不依赖它。

[⬇️ 下载 APK](https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest) · [v4.0 设计说明](docs/36_minimal_generic_passive_key.md) · [逆向知识库](docs/)

当前版本 **v4.0.9-min**：新增“锁定后台（最近任务说明）”入口和后台高耗电设置跳转，详见 [后台锁定说明](docs/46_v4_0_9_background_lock_guide.md)。电池白名单见 [v4.0.8说明](docs/45_v4_0_8_battery_whitelist_feedback.md)，后台扫描见 [v4.0.7说明](docs/44_v4_0_7_background_scan_and_permissions.md)。

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

**① 自己识别** —— 扫吉利车机的 BLE 广播（Company ID `0x01FE`、`GeelyVehicle<VIN后三位>`、`FDFD` 等特征），滤波后按 RSSI 判断靠近 / 远离；银河连上后改用连接态 `readRemoteRssi()` 继续判断。

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
| **官方就绪即处理** | 官方钥匙通知变成「已连接」立即处理一次接近机会，已有请求仅完成它。GATT 测距已连不会提前冒充官方鉴权就绪。 |
| **通知监听恢复** | 每2秒请求系统重绑并补扫已有银河通知；系统仍拒绝无界面重绑时，常驻通知和权限卡明确显示“系统未绑定”，点“修复通知监听”在系统页关闭再开启即可。 |
| **一次接近只解锁一次** | 成功交付后不重复；锁后仍连着时须真实近场持续1秒才能再次解锁，口袋补偿与边界抖动不算返回。 |
| **近场保护** | 最近6秒还在默认 `-78 dBm` 以上则禁止发送上锁；保护与远离计时同时进行。 |
| **靠近阶段不反向锁车** | 官方连接可以提前解锁；实际进入过车旁后才按原阈值确认离开。没有走到车旁便转身时，需要同一采样来源的持续变弱证据，广播/GATT落差不算离开。 |
| **上锁要驻留** | 默认未补偿 RSSI `≤ -82 dBm` 连续4.5秒、至少3个新样本；进入缓冲带或采样断档即取消计时。 |
| **断连不误锁** | GATT 断开 / 蓝牙重启后 10~15 秒内禁止自动上锁。宁可不锁，也不在你车旁误锁。 |
| **行车不动作** | 银河通知明确显示「行驶中」时，不解锁也不上锁。 |
| **插线禁控** | 手机插着有线充电（例如在车里用 USB 供电）时默认禁止自动与手动解闭锁。 |

默认解锁阈值 `-75 dBm`（实车调过的提前解锁点）、上锁阈值 `-82 dBm`、驻留 `4.5 s`，都能在 App 里改。

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
2. 装 [GeelyPassiveKey.apk](https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest)；
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
│   │   ├── GeelyAdvertisement.java             # 吉利广播解析
│   │   ├── VehicleIdentity.java                # 本车蓝牙身份匹配
│   │   ├── VehicleIdentityStore.java           # 身份持久化 + 阈值开关
│   │   ├── VehicleLearningState.java           # 学习资格、来源与具体等待状态
│   │   ├── RssiFilter.java                     # 三点中值 + 按时间计算的 EMA
│   │   ├── PendingControl.java                 # 唯一待发请求和有限重试
│   │   ├── NotificationEpoch.java              # 旧通知和移除事件隔离
│   │   ├── RotatingFileLog.java                # 固定容量轮转日志
│   │   ├── DiagnosticLog.java                  # 跨模块日志与进程退出记录
│   │   ├── DiagnosticLogStore.java             # 有界写入队列与一致快照导出
│   │   ├── DiagnosticText.java                 # 车辆身份与凭证脱敏
│   │   ├── DiagnosticCounters.java             # 扫描、读取和过滤计数
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
bash GeelyKeyRadarApp/build.sh
```

先跑状态机、RSSI完整流程、待发请求、通知版本、日志轮转、诊断导出和学习资格 **7组测试**，再 `aapt2` → `javac` → `d8` → `apksigner`。检测到真机时尝试安装；`SKIP_INSTALL=1` 可只构建。产物：`GeelyKeyRadarApp/GeelyPassiveKey.apk`。

本地发布压缩包包含当前极简版源码、资源、测试、文档和签名APK；不包含Git目录、构建缓存、签名私钥、本机配置、运行日志、旧版源码归档及历史界面mockups。重新构建时脚本会生成新的调试签名；若要覆盖已安装版本，需要沿用本机原签名密钥。

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
4. **多辆吉利车相邻**：未学习时只让一个主候选进 RSSI 流；即使注入错车，官方 SDK 也不会鉴权通过，本 App 会重试。
5. **ROM 差异**：部分 ROM 息屏会暂停 BLE 扫描，建议按第五节加后台白名单。
6. **非 vivo 手机的 5 秒远距连接延迟坑（已记录，暂未改代码）**：银河 `AbsBleStrategy.connectBle(ScanResult)` 对 `isPhoneVIVO()` 直接发起连接，而对非 vivo 手机会检查 `ScanResult.getRssi() >= min_conn_rssi`，弱信号时放入 `connectDelayThread` 延迟 `5000ms` 再连。后续若在非 vivo 机型上遇到远距唤醒偏慢，可在注入 `MiuiService` 时将 `ScanResult` 副本的 `rssi` 改写为 `-55` 绕过（详见 [docs/36_minimal_generic_passive_key.md](docs/36_minimal_generic_passive_key.md) 第 10 节）。

---

## 免责

仅供自己车辆个人使用。仓库内逆向内容为学习记录，不含钥匙凭证或账号信息。车控有风险，请自行确认安全边界。
