# 88. v4.6.14：兼容吉利汽车与吉利银河双官方端及多车无感车库 UI 重构

本轮参考本地改造版 `GeelyPassiveKey-src-dual-613.zip` 的吉利汽车（`com.geely.consumer`）核心适配结论，在保留上游全部安全边界、防误锁与诊断能力基础上，重构实现双品牌兼容、用户选车型与多车无感解锁。目标版本为 `4.6.14-min / 614`。

## 1. 双官方客户端兼容架构

| 品牌 | 官方包名 | MiuiService Action | 小组件 Provider |
|---|---|---|---|
| 吉利汽车 | `com.geely.consumer` | `com.geely.consumer.miuiservice.action` | `com.geely.consumer.carcontrol.widget.appwidget.Wg_Middle` |
| 吉利银河 | `com.geelygalaxy.customer` | `com.geely.geometry.miuiservice.action` | `com.geely.galaxy.car.weight.appwidget.WgProviderMiddle` |

- `AndroidManifest.xml` `<queries>` 同时声明双包名与双 action，确保 Android 11+ 包可见性。
- `OfficialBrandPolicy` + `OfficialTarget` 负责品牌解析、优先级与 `AUTO` 双端兜底唤醒；首次在 `AUTO` 模式下连接成功时会自动为该车记录实际连接的品牌。
- `KeyNotificationBridgeService` 按通知来源包名独立解析与缓存 `notif_layout_lock_unlock`、`notif_tv_lock`、`notif_tv_status_conn`、`notif_tv_status_disconn`、`notif_tv_status_no`、`notif_tv_unlock` 与 `Context`，并屏蔽双端共存时另一家“未连接”通知对当前已连接车辆状态的覆盖。

## 2. 多车无感车库（`VehicleProfile` / `VehicleGarage` / `VehicleIdentityStore`）

- 升级安装时自动将原有单车身份迁移为车库第 1 台档案，完整保留四项触发阈值与所有开关。
- 多车模式下，`VehicleScanPolicy.buildMulti()` 合并所有启用车辆的精确 `ADDRESS` / `NAME` / 稳定 `MANUFACTURER` 规则（`MULTI_TRACKING`），不退化为宽过滤。
- `PassiveKeyService` 在未连接且空闲时按 `ADDRESS > NAME > HINT` 匹配最合适车辆，切换车辆时重置测距滤波器与状态机，避免不同车辆 RSSI 混流。

## 3. UI 重构与自动化测试

- `MainActivity` 顶部与关于页统一为“吉利无感钥匙”；概览页新增“车型与多车车库”卡片（品牌切换条、车库列表、添加车型弹窗含常见吉利/银河车型快捷选项、多车自动/单车锁定切换）；维护页新增“双品牌兼容状态”卡片。
- 新增 `OfficialBrandPolicyTest`、`VehicleGarageTest`，纯 Java 测试扩充至 **33 组 + 1 组 APK 命名回归**，并通过 `GeelyUnpack35` AVD 冒烟验证。
