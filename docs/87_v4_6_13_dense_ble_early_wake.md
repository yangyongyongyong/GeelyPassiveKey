# 87. v4.6.13：密集车辆场景被动唤醒放宽与 ACL 初期避让优化

本轮基于 2026-11-26 vivo V2426A（iQOO 13、`4.6.12-min(612)`）无线 ADB 全天 4 次实机解锁快照（`09:45`、`12:21`、`19:40`、`19:55`）定位并优化。目标版本为 `4.6.13-min / 613`。

## 1. 实机日志核查

- 早/午两次解锁首包分别在 `-82 dBm` / `-78 dBm` 命中；晚间周围车辆特别多的两次解锁（`19:40` 与 `19:55`）首包均延迟到 `-66 dBm`（已贴近车门）才命中，且 4/4 次全部等待 `SCAN_RSP` 携带完整车名（`match_reason=NAME`），0/4 走被动 `0x01FE` 提前唤醒。
- 银河官方 App `1.56.1` 在 `ACL_CONNECTED` 后完成 V2 鉴权耗时稳定在 `3.7 ~ 4.0 秒`；`19:55` 因 `19:55:48` 撞上 60s 扫描重建空白（`3.95s`）叠加车多丢包，导致体感等待约 `9 ~ 10 秒`。
- 在官方 `1.56.1` 下 `ACL_CONNECTED` 后立即并发挂载的只读 `AUTH_OBSERVER`（`connectGatt + discoverServices + writeDescriptor`）4 次均未收到 `cmdId=5`，反而在 `09:45:31` 触发了一次测距超时重建。

## 2. 优化内容

- `VehicleTargetSelector`：纯被动稳定 `0x01FE` 帧（已通过 `known.stableDiscoveryMatches(ad)` 校验）首包唤醒门槛从 `-70 dBm` 放宽至 `-76 dBm`，连续 3 包同 MAC 稳定 `0x01FE` 放宽至 `-83 dBm`（`SEND_EARLY_HINT` 仍不改地址、不供给 RSSI）。
- `PassiveKeyService`：`ACL_CONNECTED` 后延迟 `1.5s` 再挂载只读 `AUTH_OBSERVER`（官方提前就绪则取消）；在 `promoteAuthObserverToRssi()` 刷新 `lastGattAttemptAtMs` 并避免观察期触发测距超时关断。
- `ScanHealthPolicy`：离车零回调退避周期由 `60s` 拉长至 `180s`，减少靠近车辆时撞上重扫空白的概率。
