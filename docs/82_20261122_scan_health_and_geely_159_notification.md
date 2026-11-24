# 82. 2026-11-22 两组日志：扫描回调停滞与官方 1.59 通知控制

基线：`4.6.7-min / versionCode 607`

修复版本：`4.6.8-min / versionCode 608`

> 后续加固：`v4.6.10` 起后台扫描停滞窗口由 45 秒收紧为 10 秒，亮屏为 4 秒，并在亮屏/解锁时绕过节流执行一次硬重建；错误码 2/3/5 也会直接硬恢复。详见 [84](84_v4_6_10_feedback_hardening_and_avd.md)。

本次处理两份用户上报：

- 小米 `23116PN5BC`：`靠近未解锁`
- Redmi `24115RA8EC`：`吉利星愿，解锁和上锁都不灵`

两份设备均运行 Android 16，官方银河 App 均为 `1.59.0(159000047)`。

## 1. 靠近未解锁：扫描器只剩“运行中”状态

日志显示：

```text
scanning=true
scan_uptime_ms=528016
callback_age_ms=1462595
scan_callbacks=749(+0)
```

应用仍认为扫描在运行，但最后一次 BLE 扫描回调已过去约 24 分钟。旧扫描看门狗只有两个触发条件：

1. 必须处于息屏；
2. 45 秒没有车机广播。

用户亮屏后看门狗不再处理；同时 HyperOS 可能接受 `startScan()` 却不继续投递回调。仅重复启动同一扫描器也没有脱离“状态为 true、实际无回调”的假在线。

### 修复

- 新增 `ScanHealthPolicy`，只以本轮扫描启动后的真实 BLE 回调年龄判断健康度；
- 45 秒无任何扫描回调即重建扫描器，不再要求息屏或必须曾收到车机广播；
- 亮屏事件到达时立即复核扫描健康度，修复“息屏启动后静默无回调、亮屏仍不重建”；
- 90 秒重建节流，避免频繁 `stopScan/startScan`；
- 连续第二次仍无回调时，重新获取 `BluetoothManager`、`BluetoothAdapter` 与 `BluetoothLeScanner`；
- 任意有效 BLE 回调到达后清零停滞计数，并记录 `SCAN_HEALTH_RECOVERED`；
- 车型兼容原始抓取和官方钥匙已连接期间不打断正常链路。

## 2. 星愿解锁/上锁不灵：token 被判定为“当前”，实际通知已很旧

日志中所有自动/手动车控均显示：

```text
result=SENT
transport=PI
bridge_result=SENT
```

但同一通知自 19:48 左右发布后长期未更新，20:33 上锁时：

```text
notification_current=true
notification_current_age_ms=0
notification_snapshot_age_ms=720786
```

发送前重新解析旧通知，会把按钮捕获年龄重置为 0；v4.6.7 的旧 token 判定因此失效。`PendingIntent.send()` 返回成功也只代表 Android 接收投递，不能证明银河 1.59 执行了车控。

### 修复

- 当前通知中存在可点击 RemoteViews 按钮时，优先调用 `View.performClick()`；
- PendingIntent 顺序调整为：当前 PI → 历史 PI → 历史 View；
- 只有 Activity 类型 PendingIntent 在 Android 14+ 附加后台启动许可；广播/服务使用普通 `send()`；
- 日志新增安全的 PI 元数据：`activity/broadcast/service` 及其是否由官方 App 创建；
- 通知 token 年龄取“本次捕获年龄”和“通知发布时间年龄”的较大值；
- 重新读取旧通知不再把 token 伪装成新鲜；
- 自动上锁使用旧 token 时，会真正进入一次显式上锁复核；
- Android 14+ 不再尝试把其他应用的 PendingIntent marshall 到磁盘，避免每次解析产生 `PI_CACHE_WRITE_ERROR`；token 仅保存在内存中。

## 3. 边界

- 本次修复不能替代星愿车型的实车验证；
- `View.performClick()` 仍只表示官方按钮被触发，不是车辆锁控回执；
- 解锁继续保持“不自动重试”，结果未知时只提示用户核实；
- 不新增 AIDL/root/广播绕过，不读取官方私钥、账号或车辆凭证。

## 4. 回归

新增：

- `ScanHealthPolicyTest`
- `NotificationTransportPolicyTest`
- `LockDeliveryPolicyTest` 的通知快照年龄用例

要求全部纯 Java 测试、APK 命名测试、资源/Java 编译、D8、对齐和签名通过。
