# 84. v4.6.10：基于 v4.6.9 日志的上报问题加固与 AVD 自动化验证

本轮基于 2026-11-24 近 24 小时 Cloudflare D1 中 6 份用户日志做代码加固。发布版本为 `4.6.10-min / 610`；输入日志全部来自 `4.6.9-min / 609`。

## 1. 六份日志对应问题

| 设备 | 用户反馈 | 日志结论 |
|---|---|---|
| OPPO PLG110 | 靠近 15 秒未解锁，亮屏后才解锁 | 后台扫描回调长期为 `-1`；首包虽已唤醒银河，ACL 未建立；亮屏后官方才连接并解锁 |
| OnePlus PLQ110 | 老是要重新监听 | `NotificationListenerService` 掉绑后每两秒 `requestRebind`，一分钟以上仍未恢复 |
| vivo V2426A | 回来重复解锁，车辆本来已解锁 | `RELOCK_RECOVERY_THRESHOLD_REACHED` 仅凭 RSSI 深谷回流补发第二次解锁，没有新鲜 `LOCKED` 证据 |
| vivo V2426A | 停车约 5 秒才解锁 | 首包到解锁约 4.51 秒，主要是银河官方 App 建连/鉴权耗时 |
| vivo V2059A | 只写“靠近解锁” | 身份未学习、ACL 未连接；不具备合法自动车控条件 |
| OPPO PLW110 | 靠近没开锁，显示没有信号 | 扫描回调持续停滞、零候选、零 RSSI；通知监听在线但无银河钥匙通知 |

## 2. 代码修复

### 扫描回调停滞

- 后台真实回调停滞阈值从 45 秒收紧为 10 秒；
- 亮屏状态 4 秒无回调即进入快速恢复；
- `ACTION_SCREEN_ON` 和 `ACTION_USER_PRESENT` 触发强制硬恢复，绕过 8 秒节流并立即重建 Adapter/Scanner；
- `SCAN_FAILED_APPLICATION_REGISTRATION_FAILED`、`SCAN_FAILED_INTERNAL_ERROR`、`SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES` 直接升级为硬恢复；
- 扫描过频仍保留 6.5 秒退避，避免持续触发系统限频。

### 亮屏补拉银河

- 官方服务保活绑定改为先 `bindService` 再投递 `ScanResult`；
- `bindService()` 返回 false 时不再错误地认为已经保活；
- 亮屏/解锁后，如果 30 秒内看到过本车广播、官方仍未连接且 ACL 尚未出现，补发一次真实 `ScanResult`；
- 补拉仍遵守 5 秒节流、ACL 检查和单目标边界。

### 通知监听恢复

- 重绑退避改为 `0 / 2 / 5 / 10 / 30 / 60 / 300` 秒；
- 持续 10 秒未恢复标记为 `needs_user_access_toggle`；
- 仍保留五分钟一次的廉价重试，不永久放弃；
- 已连接、已撤销通知使用权时不再继续请求重绑。

### 重复解锁

- `PassiveKeyLogic` 的 RSSI 深谷回流纠偏新增明确锁态入参；
- 只有 90 秒内的新鲜 `LOCKED` 官方通知/小组件文本才允许第二次解锁；
- `UNLOCKED`、`UNKNOWN`、空值或过期锁态均拒绝；
- 车机 `0x0159` 原生误锁纠偏路径保留，不受 RSSI 锁态门控影响。

## 3. 自动化测试

`GeelyKeyRadarApp/build.sh` 当前运行 **30 组纯 Java 测试＋1 组 APK 命名回归**。本轮新增：

- `ScanFailurePolicyTest`：错误码硬恢复分类与退避；
- `InteractiveWakePolicyTest`：亮屏补拉条件、广告年龄和节流；
- `NotificationRecoveryPolicyTest`：六分钟重绑次数上界、用户介入状态；
- `UnlockRecoveryPolicyTest`：只接受新鲜明确 `LOCKED`；
- `ScanHealthPolicyTest`：10 秒后台、4 秒亮屏、强制硬恢复与安全门控；
- `PassiveKeyLogicTest`：无锁态证据时禁止 RSSI 二次解锁。

AVD 集成冒烟：

```bash
KEEP_EMULATOR=1 bash GeelyKeyRadarApp/tests/emulator_smoke_test.sh
```

覆盖：

1. 使用隔离临时签名密钥构建 APK；
2. 仅允许在模拟器 serial 上执行，拒绝真实手机；
3. 安装并授予蓝牙/通知权限；
4. 开启 NotificationListener；
5. 验证 `PassiveKeyService` 前台运行且类型为 `connectedDevice`；
6. 撤销并恢复通知使用权，验证监听自动重绑；
7. 执行息屏/亮屏循环，验证交互恢复广播和进程存活；
8. 检查 App 进程无 `FATAL EXCEPTION`。

本机验证环境：Apple M2、24 GB、macOS 26.4.1、Android Emulator `37.2.12`、AVD `GeelyUnpack35`、Android 15 / API 35、arm64-v8a。AVD 冒烟已通过。

## 4. 模拟器边界

AVD 能覆盖 Android 生命周期、前台服务类型、通知监听授权/重绑、屏幕交互、安装升级和崩溃回归。

AVD 不能替代真实车机验证：

- AOSP 模拟器没有真实吉利 BLE 广播与 RSSI 环境；
- 仓库中没有可再分发的银河官方 APK，模拟器也没有登录账号、车辆和钥匙凭证；
- 官方 App 的 BLE 鉴权、通知按钮实际车控和车锁回执必须在真车环境确认。

因此最终验收仍是：纯 Java 回归 → AVD 冒烟 → 真机安装 → 银河连接 → 实车靠近/离开车控。
