# 52. v4.1.5：稳定 VIN 掩码发现，缩短本车进入官方连接链路前的等待

日期：2026-11-15，Asia/Shanghai。版本：`4.1.5-min / versionCode 115`。

**范围：只优化发现阶段。**保留v4.1.4的首包预连接、可信地址判断、RSSI滤波、解锁/上锁阈值、驻留、近场保护、通知按钮和单队列车控。本轮修改本地代码、测试、文档并构建APK；随后已在用户明确要求下覆盖安装到当前vivo手机。**尚未做实车解锁效果验证，未执行Git命令、未提交或推送**。

## 1. 为什么改

v4.1.4在已学习车辆上仍把6字节厂商前缀逐位全等匹配：

```text
FF FF FF FF FF FF
```

同一辆车的广播中，version、antenna、devices等字节可能随固件/连接状态变化。任何动态字节变化都会在Android `ScanFilter`阶段被丢弃，`ScanCallback`根本收不到，后续名称确认、首包预连接和官方鉴权都无从开始。11月14日日志已出现同一实际连接MAC的厂商前缀指纹从`b0a493f9c3d5`变为`e36b48d581bc`，说明整段6字节不是稳定身份。

REA对银河历史V2实现的只读分析显示，官方发现过滤采用稀疏VIN掩码：四字节VIN分支优先保留`vendor + antenna + VIN后两位`，两字节VIN分支只保留VIN后两位。可直接借鉴的是**发现层选定位**，不是把VIN掩码当成授权凭证。

## 2. v4.1.5实现

已学习车辆、Company ID一致且尾号一致时，硬件计划同时保留两条厂商规则：

| 规则 | 掩码 | 匹配位 |
| --- | --- | --- |
| 优先稳定掩码 | `FF 00 FF 00 FF FF` | `P0 vendor`、`P2 antenna`、`P4..P5 VIN后两位` |
| 天线变化兜底 | `FF 00 00 00 FF FF` | `P0 vendor`、`P4..P5 VIN后两位` |

当前样本布局：

```text
P0 vendor      06
P1 version     02 / 03（允许变化）
P2 antenna     00（优先稳定，变化时走兜底）
P3 devices     14 / 99（允许变化）
P4..P5 VIN2    45
```

软件HINT也使用同一稳定掩码判断，避免硬件放行后又被整段前缀全等拒绝。Company ID仍严格一致；完整`GeelyVehicle*`名称、VIN后两位冲突和已学习地址规则继续保持原优先级。

## 3. 发现与授权边界

稳定掩码只把匿名轮换MAC送入**发现候选**，没有任何新的可信权限：

- 仍是`VehicleIdentity.Match.HINT`，不是`ADDRESS`；
- `matches()`仍只接受已验证地址，`authorizesRssi()`不会为HINT放行；
- `mergeWith()`不会因跨MAC HINT修改已学习地址或厂商签名；
- 不喂`RssiFilter`，不创建解锁/上锁请求，不提前`connectGatt`；
- 匿名HINT仍需至少2个样本、跨度至少1500ms、RSSI≥-95，普通选择后才交给官方；
- 匿名HINT不能走`SEND_EARLY_NAME`；当前实际`ScanRecord`带完整本车名称的v4.1.4首包快路径保持不变；
- 身份采用仍要求官方就绪及系统唯一可验证的实际连接，最终V2鉴权和车控仍在银河官方App内完成。

没有恢复裸`0x01FE/0x06FE/0xFF15`全品牌宽扫，也没有加入服务UUID宽规则。其他99个VIN尾号仍被厂商掩码排除；同尾号但Company ID、完整名称或三字节尾号冲突时，软件继续拒绝HINT。

## 4. 文件

| 文件 | 变化 |
| --- | --- |
| `VehicleDiscoverySignature.java` | 新增稳定掩码、VIN尾号一致性、旧15FF前缀裁剪与掩码比较 |
| `VehicleIdentity.java` | 稳定掩码命中最多返回HINT；可信地址/RSSI语义不变 |
| `VehicleScanPolicy.java` | 已学习厂商规则从单项全等改为优先与兜底两项掩码；地址/名称规则保留 |
| `VehicleIdentityTest.java` | 版本/devices/antenna变化、不同尾号/Company/名称及HINT不改地址 |
| `VehicleScanPolicyTest.java` | 四项TRACKING计划、动态字段放行、其他99尾号排除、旧15FF双规则 |
| `VehicleTargetSelectorTest.java` | 稳定掩码HINT进入候选但`authorizesRssi=false`，不覆盖地址 |
| `VehicleWakeGateTest.java` | 匿名HINT首包不预连接、不足1.5秒不选择、P2变化仅走普通单目标官方路径 |

以下源码与修改前备份逐字一致：`PassiveKeyLogic.java`、`RssiFilter.java`、`PendingControl.java`、`NotificationEpoch.java`、`GeelyBridge.java`、`KeyNotificationBridgeService.java`、`OfficialReadyGate.java`。因此上锁判定、信号平滑、通知发送和车控队列没有随本轮改变。

## 5. 回归与构建

以下验证全部通过：

- 13组Java回归＋1组APK命名回归；
- 状态机、RSSI完整流程、待发队列、通知版本、日志轮转、诊断导出、学习资格；
- 身份冲突、候选隔离、预连接门控、官方就绪门控；
- 稳定四字节VIN掩码与两字节VIN兜底；
- Android资源、Java、DEX、zipalign和APK签名。

构建命令：

```bash
SKIP_INSTALL=1 JAVA_HOME=/Users/luca/Library/Java/JavaVirtualMachines/corretto-11.0.24/Contents/Home \
  bash /Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/build.sh
```

产物：

```text
/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.1.5-min-115.apk
SHA-256: 48e05ea9fdaa7a0201b9a84cc4628731eddd08627c79c795f4f2a53c712bd934
```

签名证书SHA-256仍为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`，keystore SHA-256保持`e6dd3c9ee8b67212182db67a4765cf2d9f83753afc3b0d5d431b9816d9015ad6`。

### 手机覆盖安装验收（2026-11-15）

- 使用`pm install -r -g`覆盖安装，未卸载、未清数据；安装前后`passive_key_prefs.xml`与`diagnostic_log.xml`的SHA-256完全一致。
- 系统包信息为`versionCode=115 / versionName=4.1.5-min`，应用页面显示`版本：v4.1.5-min（构建 115）`。
- 前台服务运行中，本车身份保留；新进程日志出现`filters=4 filter_mode=TRACKING mode=LOW_LATENCY`。
- 通知使用权保留并重新绑定，最新心跳`listener_connected=true / listener_recovery=connected`；后台白名单、后台运行和前台服务AppOps均已恢复。
- 安装前配置备份位于`/tmp/geely_phone_pre_v4_1_5_20261115_012651/`。实车慢/快、3–5米目标与上锁稳定性仍需下一轮现场日志判断。

## 6. 证据边界与实车验收

**本轮不能保证3–5米一定开门。**稳定掩码只解决“同车动态字节导致本App看不到广播”的软件风险；实际射频距离、系统调度、银河V2鉴权和车机响应仍需实车验证。两字节VIN兜底也扩大了发现碰撞面，因此明确禁止HINT进入可信测距和持久化身份。

后续实车测试保持现有配置、签名和阈值，不卸载、不清数据。按以下路线测试并及时导出ZIP：

1. 同MAC、版本/devices变化：确认`FILTER_RECONFIGURE`为TRACKING，`MATCH_RESULT`能更早出现HINT/NAME/ADDRESS；
2. antenna变化：确认本车仍进入候选，但匿名包只出现`scan_discovery_only`，不产生RSSI；
3. 多辆吉利相邻：确认其他99个尾号、不同Company和明确冲突名称未混入；
4. 对比`首候选 → WAKE_REQUEST → ACL → 官方就绪 → PI交付`各段，而不是只看最终门开；
5. 复测上锁：确认离开阈值、4.5秒驻留、6秒近场保护及稳定发送不变。

REA证据与报告：

- `/Users/luca/Workspace/car_key_re/investigations/20261115_galaxy_rea/findings-summary.md`
- `/Users/luca/Workspace/car_key_re/investigations/20261115_galaxy_rea/findings-scan.md`
- `/Users/luca/Workspace/car_key_re/investigations/20261115_galaxy_rea/findings-adapter.md`
- 官方mask：`ev_465736d7fd0b5fe730bad52cb45b610fd5ea4d42c5404345b9d30ece234e98f0`
- payload布局：`ev_0f5ffff7dc7da552676f4a53ec0ee408a2af9032b963bc9508f2cc37ed09e01d`
- 扫描配置：`ev_b62e1d90418f5ab33a1ece76e18bb54b5586702eb305c0e35540c25e74c36222`
- `MiuiService.onBind`返回null：`ev_d0c956e1fc065d65ba0a8929a7beb2e396c350a8484499b1dc15715008dfc67f`

`MiuiService.onBind()`返回`null`的结论仍有效：本轮没有把Service bind、私有回调或轮询当作官方就绪通道，通知监听链路保持不变。
