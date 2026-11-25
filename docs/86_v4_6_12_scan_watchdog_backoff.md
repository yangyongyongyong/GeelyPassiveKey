# 86. v4.6.12：修复 BLE 扫描看门狗离车过频重启引发的亮屏 6 秒空白

本轮基于 2026-11-25 晚间 vivo V2426A（iQOO 13、`4.6.10-min(610)`）无线 ADB 实机动作快照（`195436` / `193507`）定位并修复。发布版本为 `4.6.12-min / 612`。

## 1. 实机日志现象与根因

- 19:52:05 ~ 19:54:25 离车期间，因本车不在附近本来没有硬件过滤回调，`4.6.10-min` 的 10 秒看门狗且在 `HARD_RESET` 后清零 `consecutiveStaleScanRestarts`，导致后台每 8~10 秒持续执行 `stopBleScan()` + `startBleScan()`（150 秒内重启 18 次，30 秒内达 3~4 次，全天 `scan_start_budget_wait=36`）。
- 19:54:25.263 人已在车旁但未解锁、点亮屏幕时，`SCREEN_ON` 因 `INTERACTIVE_STALE_CALLBACK_MS = 4s` 立即打断 19:54:18.815 刚启动 6.4 秒的扫描并做第 4 次重建；重启后至 19:54:32.347 整整出现 **6.43 秒** 扫描空白，随后 `WAKE_REQUEST -> ACL_CONNECTED -> OFFICIAL_CONNECTED -> UNLOCK` 用时 4.12 秒，亮屏总等待达 **11.21 秒**。

## 2. 修复内容

- `ScanHealthPolicy`：后台首次停滞放宽至 `30s`（节流 `25s`）；连续 `RESTART + HARD_RESET` 仍零回调时进入 `60s` 离车退避；亮屏强制重建与交互检查门限调至 `8s`（含 `8s` 冷却），避免打断刚启动的新鲜扫描。
- `PassiveKeyService`：移除 `HARD_RESET` 与 `onScanFailed` 中对 `consecutiveStaleScanRestarts` 的清零，仅在真正收到 `ScanCallback` 或系统蓝牙关闭时清零。
- `ScanHealthPolicyTest`：新增离车 60 秒退避、6.4 秒新鲜扫描保护与 8 秒亮屏冷却回归测试。
