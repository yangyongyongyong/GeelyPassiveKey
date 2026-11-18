# 75. v4.6.3：星愿兼容与最高版本更新

版本：`4.6.3-min / versionCode 603`。

## 1. 星愿反馈

用户反馈吉利星愿的蓝牙钥匙名称为 `Geely_Auto`，手动解锁可以成功，但靠近自动解锁不工作。此现象与 2026-11-18 日志一致：

- 官方通知栏手动链路可用；
- `identity_id=none`、`learning_complete=false`；
- 普通 `GeelyVehicle...` 候选均未形成本车身份；
- App 因未学习地址，不进入自动 RSSI 与靠近解锁状态机。

## 2. 兼容策略

1. 首次学习扫描增加 `Geely_Auto` 精确名称过滤；
2. `Geely_Auto` 可以作为发现候选并唤醒银河；
3. 它不被当作唯一身份，不能直接供 RSSI、自动解锁或自动上锁；
4. 优先使用银河连接后的唯一系统 GATT 地址；
5. GATT 列表不可见时，使用同一地址的真实 ACL 事件；
6. 部分 ROM 同时隐藏 GATT 和 ACL 时，只有在最近 30 秒内实际注入了 `Geely_Auto` 候选、且银河随后报告连接时，才采用该候选地址；
7. 绑定后立即将近期同地址广播作为第一笔可信 RSSI 样本，减少连接成功后的额外等待。

该策略不会因为停车场中存在多台 `Geely_Auto` 就直接把第一台车绑定为本车。

## 3. 更新链路

旧逻辑按 Cloudflare 优先，只要 Cloudflare 返回成功就停止比较；Cloudflare 边缘缓存较旧时，用户可能先收到旧版本，下一轮再收到新版本。

v4.6.3 改为：

- Cloudflare 清单使用 `?refresh=1` 绕过边缘旧缓存；
- 同时读取 Cloudflare 与 GitHub 清单；
- 按 `versionCode` 选择最高版本；
- 下载、SHA-256、包名、构建号和签名校验保持不变；
- 只会直接下载当前已知最高版本，不会刻意为每个中间版本安装一次。

## 4. 默认触发参数

每次 Release 必须展示以下四项默认值，且升级安装保留用户已保存的配置：

```text
蓝牙连接触发：≥ -95 dBm
靠近解锁：≥ -75 dBm
离开上锁：≤ -85 dBm
离开确认：2.0 秒
```

## 5. 验证

- `VehicleIdentityTest` 验证 `Geely_Auto` 可发现、跨 MAC 仅为 HINT、真实连接才采用新地址；
- `VehicleTargetSelectorTest` 验证 `Geely_Auto` 可进入官方唤醒候选但不能在未学习时供 RSSI；
- `VehicleScanPolicyTest` 验证首次学习硬件过滤放行 `Geely_Auto`；
- `UpdatePolicyTest` 验证多渠道选择最高 `versionCode`；
- 全部 Java 单测、APK 命名回归、签名与打包流程通过后，由 GitHub Actions 发布 Release。
