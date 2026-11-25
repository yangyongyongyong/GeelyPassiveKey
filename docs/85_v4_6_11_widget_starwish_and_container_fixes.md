# 85. v4.6.11：小组件跨版本识别、星愿抓包/名称兼容与跨容器拦截

本轮基于 2026-11-25 Cloudflare D1 中 4 份 `4.6.10-min(610)` 新上传诊断日志完成定位与修复，发布版本为 `4.6.11-min / 611`。

## 1. 四份日志对应问题与根因

| 日志 ID | 设备 / 系统 | 用户反馈 | 根因定位 |
|---|---|---|---|
| `e121e2b4` | OnePlus PLK110 / Android 17 (SDK 37) | 小组件不识别 | `WidgetTokenBridge` 仅靠反射读取 `mLastInflatedRemoteViews` / `mRemoteViews` 失败（卡在 `waiting_remoteviews`）；硬编码 1.56.1 资源 ID（银河已升至 1.59.0）；未先查 `getAppWidgetInfo(widgetId)` 导致 `BOUND id=15..23` 持续自增泄漏 |
| `c2914c4b` | OPPO PHZ110 / Android 16 | 【车型兼容】吉利星愿 扫描不到车 | 10 秒兼容原始抓包期间 `requestScanFilterRefresh()` 未拦截 `compatibilityCaptureSession != null`，约 1 秒即触发 `FILTER_RECONFIGURE` 打断无过滤抓包；且通过 GATT 学到 `name="GeelyAuto"`、无厂商签名时，硬件计划只放行 `GeelyAuto` 未同时放行 `Geely_Auto` |
| `34ba8a00` | HUAWEI ALN-AL00 / Android 16 (卓易通) | 【车型兼容】吉利星愿 | Android 容器内无 `com.geelygalaxy.customer`（`official_app=unavailable`），`maybeWakeGeely()` 未做前置拦截，导致 47 轮无效 `WAKE_REQUEST` -> `WAKE_UNAVAILABLE` -> `FAST_RESCAN` 循环；同时兼容抓包也被中途重配置打断 |
| `c629cd4c` | vivo V2426A / Android 16 | 【车型兼容】通知使用权必须重新开关 | 进程此前为 `USER_REQUESTED` 退出，OriginOS 锁死静默 `requestRebind()`；代码侧 `requestImmediateRecovery()` 绕过 `NotificationRecoveryPolicy` 冷却，切页/亮屏/手动点击同秒连发 `manual_request` |

## 2. 代码修复

### 官方小组件识别与 ID 复用（`WidgetTokenBridge` / `WidgetRemoteViewsSpec`）

- 在自定义 `AppWidgetHost` / `AppWidgetHostView` 中覆写 `updateAppWidget(RemoteViews)` 公开回调直接捕获 `RemoteViews`，保留原有私有字段反射作兜底。
- 新增 `WidgetRemoteViewsSpec`，保留 1.56.1 静态 ID，同时运行时通过 `createPackageContext(GEELY_PKG, CONTEXT_IGNORE_SECURITY)` 按资源名动态解析 `wg_view_fun_status`、`wg_view_fun_loading`、`wg_middle_confirm_btn_ok`、`wg_btn_setting`、`wg_ll_app_addition` 与三态锁图标 ID。
- 绑定前先核查 `appWidgetManager.getAppWidgetInfo(widgetId)`，复用已有绑定并清理 host 下多余孤立 `widgetId`。

### 兼容原始抓包保护与星愿名称兼容（`VehicleScanPolicy` / `PassiveKeyService`）

- 新增 `VehicleScanPolicy.shouldRefreshScanFilters(...)`：只要 `compatibilityCaptureSession != null` 即阻止后台过滤重配置，`startCompatibilityScan()` 启动时立即移除待执行的 `refreshScanFiltersRunnable`。
- 当已学身份名称满足 `VehicleBeacon.isGeelyAutoName(name)` 时，硬件扫描过滤计划同时加入 `GeelyAuto` 与 `Geely_Auto` 两条名称规则（不改变 `GeelyVehicleXXX` 多车隔离规则）。

### 未安装银河 / 跨容器保护与监听恢复冷却（`InteractiveWakePolicy` / `NotificationRecoveryPolicy` / `KeyNotificationBridgeService`）

- `PassiveKeyService.maybeWakeGeely()` 与 `retryOfficialWakeOnInteractive()` 前置检查 `GeelyBridge.isGeelyInstalled(this)` 与 `InteractiveWakePolicy.shouldWake(...)`，缺失时仅按 60 秒限频记录 `WAKE_UNAVAILABLE`，不再触发 `wakeGate` 或 `FAST_RESCAN`；界面补充鸿蒙卓易通/隐私空间同容器提示。
- `KeyNotificationBridgeService.requestImmediateRecovery()` 接入 `NotificationRecoveryPolicy.shouldRequestImmediateRebind(..., false)`（5 秒冷却），引导页按钮显式重试使用 `requestUserTriggeredRecovery()`（1.5 秒防抖），并修正引导页“每 2 秒请求重绑”的旧文案。

## 3. 自动化测试与本地 AVD 模拟

- 纯 Java 回归新增 `WidgetRemoteViewsSpecTest`，并扩充 `VehicleScanPolicyTest`、`InteractiveWakePolicyTest`、`NotificationRecoveryPolicyTest`，合计 **31 组纯 Java 测试 + 1 组 APK 命名回归**。
- 本地 Apple M2 Mac 可继续通过 `GeelyKeyRadarApp/tests/emulator_smoke_test.sh` 启动 `GeelyUnpack35`（Android 15 / API 35 / `arm64-v8a`）完成隔离测试密钥构建、安装、前台服务类型核查、通知监听重绑循环、息屏/亮屏生命周期和崩溃检查。真机 BLE 广播、官方 App 登录与实车门锁控制仍需实机与实车验证。
