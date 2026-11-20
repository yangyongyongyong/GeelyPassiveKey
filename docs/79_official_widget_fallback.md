# 79. 官方小组件备用控制（可选）

日期：2026-11-20
状态：本地实现与构建通过；尚未在真车发送 Widget 车控。

## 1. 目标与边界

通知栏官方 `PendingIntent` 仍是主车控路径。本功能只解决“通知按钮暂时不可用，但官方小组件 token 可用”的备用场景。

- 默认关闭，不改变通知主路径；
- 不自动打开银河 App，不替用户选择车辆或点击控件；
- 不创建自有桌面组件，只通过 `AppWidgetHost` 宿主银河官方的 `WgProviderMiddle`；
- 不覆盖用户已保存的四项触发参数；
- Widget 的 `LOCK` 是官方状态切换，不是显式解锁/上锁命令。

## 2. 路由规则

发送前按以下顺序选择唯一 token 源：

```text
通知 token 可用
  -> NOTIFICATION（不再检查或发送 Widget）

通知 token 不可用
  -> Widget 未启用：不发送
  -> Widget token 未捕获：不发送
  -> 官方锁态未知或超过 45 秒：不发送
  -> 目标是 UNLOCK 且当前 LOCKED：允许 Widget 切换
  -> 目标是 LOCK 且当前 UNLOCKED：允许 Widget 切换
  -> 其他情况：不发送
```

通知 token 明确返回 `NOT_SENT` 时，才允许在同一轮尝试符合门控的 Widget token。通知返回 `SENT` 或 `UNKNOWN` 时绝不回退，避免双发导致反向车控。

## 3. Widget token 生命周期

- `AppWidgetHost` 使用固定 Host ID `0x4752` 和官方 provider：
  `com.geelygalaxy.customer/com.geely.galaxy.car.weight.appwidget.WgProviderMiddle`
- 首次绑定由用户点击“绑定官方小组件（系统确认）”，进入 Android 系统绑定页；
- 绑定后，用户点击“打开官方小组件配置”手动选择车辆和车锁控件；
- 从官方 `RemoteViews` 中提取 LOCK 图标对应的 `WIDGET_CLICK` PendingIntent；
- 发送点击后，最多在点击后 600ms/1600ms 检查一次新捕获的 `CONFIRM` token，旧 token 不得复用；
- 点击发送后不自动重发；因为二次确认是否出现不能同步得知，结果按 `UNKNOWN` 处理，服务不会冒险再发一次。

## 4. 安全门控

Widget 路径继续复用服务现有安全联锁：

- 服务总开关；
- 接近/离开距离状态机；
- GATT 已挂载与官方钥匙就绪；
- 插线禁控；
- 行车保护；
- 手动两秒防连点；
- 有身份学习与近场保护；
- 单次请求最多两次尝试，但 `UNKNOWN` 立即终止。

Widget 自身额外要求：官方锁态可来自通知文字或小组件门锁图标，时间戳不超过 45 秒；无锁态、锁态过期、目标与当前状态相同时拒绝。

小组件图标解码依据：官方 `WidgetUiStatus.parseWgControlStatus()` 对 `WidgetFunType.LOCK` 的 `onStatus` 赋值来自 `anyDoorUnLocked`；`WidgetFunTypeIcon` 在 `onStatus` 时返回 `car_wg_funicon_door_lock_on`，在关闭状态返回 `car_wg_funicon_door_lock_off_*`。因此图标资源可用于交叉确认新鲜锁态。

## 5. UI 状态

维护页“官方小组件备用控制（可选）”卡片显示：

- 是否启用；
- 官方 provider 绑定状态；
- 门锁 token 是否已捕获；
- 官方锁态及年龄；
- 绑定和配置入口。

顶部实时行为条在等待 Widget 时会显示：

- `等待官方小组件车锁 token`；
- `小组件备用等待新鲜车锁状态`；
- 实际发送时标注“官方小组件备用通道”。

## 6. 诊断与测试

日志新增：

```text
CONTROL_TOKEN_SOURCE
CONTROL_TOKEN_REJECTED
CONTROL_TOKEN_FALLBACK
Widget/BIND_PERMISSION_REQUEST
Widget/BOUND
Widget/REMOTEVIEWS_READY
Widget/CLICK_SENT
Widget/CLICK_CANCELED
Widget/CONFIRM_SENT
```

构建脚本加入 `WidgetTokenPolicyTest`，覆盖通知优先、锁态门控、过期/未知拒绝、Widget 未启用和 token 缺失。

验证命令：

```bash
ANDROID_KEYSTORE=/path/to/existing.keystore SKIP_INSTALL=1 \
  bash GeelyKeyRadarApp/build.sh
```

默认触发参数保持：

```text
连接触发：>= -95 dBm
靠近解锁：>= -75 dBm
离开上锁：<= -85 dBm
离开确认：2.0 s
```

这些默认值只用于全新安装或“填入默认值”草稿；覆盖升级保留用户已保存配置。
