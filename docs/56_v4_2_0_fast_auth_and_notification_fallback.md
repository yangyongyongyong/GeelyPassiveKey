# 56. v4.2.0：通知补扫、单入口唤醒与GATT鉴权提前完成

日期：2026-11-15，Asia/Shanghai。版本：`4.2.0-min / versionCode 200`。

> 后续补丁：v4.2.1已修正“未连接通知出现就提前停止快速补扫”的问题，窗口改为50ms/15秒，并在通知已连接且按钮捕获后才停止。见 [57_v4_2_1_lockscreen_fast_rescan.md](57_v4_2_1_lockscreen_fast_rescan.md)。

用户要求优先解锁速度与稳定性，不考虑功耗，并要求同时实施：

1. 唤醒窗口主动补扫官方通知；
2. 合并重复`MiuiService`唤醒入口；
3. GATT只读观察官方DK1.5鉴权完成；
4. 额外加入强信号稳定尾号匿名首包提前唤醒。

本轮实现这些链路，但没有降低官方V2鉴权要求，也没有伪造车辆授权。`PendingIntent`仍是官方通知中的原按钮，只在实际鉴权完成后才会发送。

## 1. 唤醒后75ms主动补扫通知

正常日志中官方通知`postTime`到NotificationListener回调约248ms；旧逻辑在通知缺失时每1500ms才补扫一次。

新增：

```text
WAKE_REQUEST / ACL_CONNECTED
  → requestFastRescan(5000～8000ms)
  → 每75ms getActiveNotifications()
  → 找到银河id=5通知立即停止补扫
  → 复用现有parseGeelyNotification/NotificationEpoch
```

`FAST_RESCAN_START/EXTEND/STOP`写入诊断日志。它不新建发送队列，不重复点同一个`postTime`通知。

## 2. 单一MiuiService主入口

旧版一次WAKE同时发：

```text
MiuiService consumer action
MiuiService geometry action
MiuiReceiver
GLDkConnectionService
DigitalKeyBgService x2
```

当前银河1.56.1 Manifest只声明`MiuiService`的geometry action；MiuiReceiver/GLDk不存在，DigitalKeyBgService为`exported=false`。

v4.2.0：

- 主路径只发送一次`com.geely.geometry.miuiservice.action`；
- 1200ms仍无同MAC ACL时，才补发一次未声明的consumer action；
- 已有ACL、已官方就绪或已失败冷却时不补发；
- 删除死Receiver/GLDk/未导出服务的调用，减少重复`connectDevice`和异常。

## 3. 强信号稳定尾号匿名首包

已学习完整车辆身份时：

- RSSI `>= -70dBm`；
- Company/vendor/VIN2稳定掩码一致；
- 身份匹配为`HINT`；
- 无名称/尾号冲突、无候选冷却；

可把第一个匿名包交给官方预连接，日志原因`STABLE_HINT_HIGH_RSSI`。匿名HINT仍：

- 不获得RSSI；
- 不修改MAC；
- 不触发生车控；
- 由官方V2鉴权决定车辆是否有效。

## 4. GATT观察DK1.5鉴权完成

ACL出现后，本App附加自己的GATT client：

```text
Service UUID:       02362AFF-CF3A-11E1-EFDE-0002A5D5C51B
Notify UUID:        02362A13-CF3A-11E1-EFDE-0002A5D5C51B
CCCD:               00002902-0000-1000-8000-00805f9b34fb
```

只解析DK1.5通知帧前8字节：

```text
[0] magic = FE
[1] version
[2..3] len
[4..5] cmdId
[6..7] seq
```

收到`cmdId=5`（`CMD_SESSION_RSP15`）即认为官方V2鉴权完成：

```text
AUTH_SUCCESS_OBSERVED
→ 固定当前真实GATT地址
→ 复用同一client切换到500ms RSSI轮询
→ 通知主服务进入官方就绪判定
→ 立即尝试已排队解锁
```

payload不落盘、不解析钥匙字段，也不直接编码车控。

## 5. 锁屏通知慢、无当前通知监听时如何解锁

v4.2.0保留最近一次官方按钮token：

- 当前通知里的`PendingIntent`优先；
- 当前通知不可用时，使用内存中的上一次同token；
- 同时把`PendingIntent`序列化到应用私有`control_pendingintent_cache`；
- App进程/通知监听重建后尝试恢复；
- 只有GATT `cmdId=5`或官方通知明确已连接时才允许发送。

因此锁屏通知刷新慢、NotificationListener暂时掉绑时，只要此前至少成功解析过一次银河“解锁/上锁”按钮，GATT鉴权完成后可以直接复用该官方PendingIntent，不必等新通知。

**硬边界：**如果用户从未授予过通知使用权，手机永远没有获得过银河官方按钮token。当前公开Android API没有其他方法凭空生成该PendingIntent；此场景仍需授予一次通知使用权。已有token缓存只能解决“之后掉绑/锁屏慢”。

## 6. 主要文件

| 文件 | 变化 |
| --- | --- |
| `KeyNotificationBridgeService.java` | 75ms快速补扫、上次PI缓存/恢复、GATT就绪门控 |
| `PassiveKeyService.java` | GATT鉴权观察、cmdId=5就绪、单入口/延迟兜底、快速补扫触发 |
| `GeelyBridge.java` | 单一MiuiService主入口，删除当前无效重复路径 |
| `VehicleTargetSelector.java` | `-70dBm`稳定HINT匿名首包快速路径 |
| `VehicleWakeGate.java` | 新增`SEND_EARLY_HINT` |
| `GattAuthFrameParser.java` | DK1.5明文头和`CMD_SESSION_RSP15`解析 |
| `GattAuthFrameParserTest.java` | 短帧、错误magic、非Session响应拒绝 |

## 7. 回归与构建

16组Java测试＋1组APK命名回归通过，Android资源、Java、DEX、zipalign、签名构建通过。

新增/调整覆盖：

- 强信号稳定HINT匿名首包允许官方预连接，但`authorizesRssi=false`；
- 不足1500ms的普通匿名HINT仍不选择；
- `cmdId=5`鉴权成功帧解析；
- 其他cmdId、短帧和错误magic不误判；
- 原有解锁/上锁、RSSI、预连接、多车隔离回归保持通过。

产物：

```text
/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.2.0-min-200.apk
SHA-256: 47d7848be22a1d312eefec71e24ee21d482cef362a084e0f006cbdb145202e6d
```

签名证书仍为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`。

## 8. 实车需要重点验证

1. GATT附加是否影响官方V2：`AUTH_OBSERVE_START → AUTH_FRAME(cmd=5) → CONNECTED`；
2. cmdId=5到官方通知postTime的间隔，以及到PI发送的间隔；
3. 锁屏后通知监听是否仍在线；若掉绑，Pi缓存是否成功恢复；
4. 多吉利相邻时强信号稳定HINT是否误连，以及30秒冷却是否生效；
5. 单MiuiService主入口的ACL时间是否优于旧双入口；
6. 上锁链路保持原阈值、4.5秒驻留和6秒近场保护。
