# 57. v4.2.1：锁屏通知补扫持续到按钮真正就绪

日期：2026-11-15，Asia/Shanghai。版本：`4.2.1-min / versionCode 201`。

## 1. 结论

v4.2.0 的 75ms 主动补扫存在一个停止条件缺陷：

```text
银河通知先以“未连接”状态出现
→ geelyNotificationPresent=true
→ 旧逻辑立即停止快速补扫
→ 锁屏/厂商ROM把随后“已连接”通知回调压后
→ 本App只能等普通 onNotificationPosted 或1.5秒低频补扫
```

v4.2.1改为只有同时满足下面三项才停止快速补扫：

1. 当前银河钥匙通知存在；
2. 通知明确进入“已连接”；
3. 当前通知已捕获至少一个可点击的官方解锁/上锁按钮。

快速补扫窗口从`75ms × 8s`提升为`50ms × 15s`。用户明确不把功耗作为本阶段优先项，先保证锁屏下的及时性与稳定性。

## 2. 关键代码变化

`KeyNotificationBridgeService`：

- `FAST_RESCAN_INTERVAL_MS = 50ms`；
- `FAST_RESCAN_WINDOW_MS = 15000ms`；
- 新增`fastRescanSatisfied()`：必须已连接且当前按钮已捕获；
- `FAST_RESCAN_STOP reason=official_control_ready`才表示真正可发送；
- `reason=window_expired`表示15秒内仍未得到可点击按钮，不能按“通知已就绪”解读。

`PassiveKeyService`在以下时点统一请求15秒快速窗口：

- 首次`WAKE_REQUEST`；
- 同MAC ACL建立；
- GATT收到DK1.5 `cmdId=5`。

## 3. 没有通知监听权限时到底会怎样

| 场景 | 是否还能自动解锁 | 原因 |
| --- | --- | --- |
| 通知监听在线 | 可以 | 快速补扫当前通知并提取官方PendingIntent |
| 监听在线但锁屏回调慢/漏回调 | 可以，v4.2.1重点修复 | 50ms直接读取`getActiveNotifications()`，不依赖回调到达 |
| 监听掉绑，但以前成功捕获过按钮 | 通常可以 | GATT `cmdId=5`后复用内存或私有缓存的上一次官方PendingIntent |
| 从未授予通知使用权，也没有历史PendingIntent | 不能 | Android没有公开API可凭空取得银河App内部创建的官方按钮token |
| 银河App通知被系统完全屏蔽，且没有历史PendingIntent | 不能 | 当前系统通知记录里没有可解析按钮；需恢复银河通知或先成功捕获一次 |
| 银河App通知被延迟显示，但有历史PendingIntent | 可以提前发送 | GATT鉴权完成后直接复用缓存，不等通知刷新 |

因此，“锁屏通知刷新慢”和“授权后监听偶发掉绑”已通过主动补扫、GATT完成观察和PendingIntent缓存移出主要等待路径；但“从第一次使用起就完全没有通知权限、也没有历史缓存”仍受Android权限模型限制，不是靠重试或固定睡眠能解决。

## 4. 回归与安装

- 16组Java测试＋1组APK命名回归通过；
- `zipalign -c -p 4`通过；
- `apksigner verify`通过，签名证书SHA-256仍为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`；
- APK：`/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.2.1-min-201.apk`；
- SHA-256：`186b2f1edf2c451866403afe8ad8db9cc6e6acc5f8aba14857314acb2e4d4680`；
- 已通过ADB覆盖安装到`V2426A`，系统确认`versionName=4.2.1-min / versionCode=201`；
- 首页确认显示`版本：v4.2.1-min（构建 201）`；
- 通知监听仍在线，后台白名单和AppOps已恢复；
- 安装前后`passive_key_prefs.xml`与`vehicle_param_readonly_prefs.xml`哈希一致，车型配置未变化。

## 5. 实车验收重点

按同一次接近记录：

```text
CANDIDATE_FIRST_SEEN
→ WAKE_REQUEST
→ ACL_CONNECTED
→ AUTH_OBSERVE_START
→ AUTH_FRAME cmd_id=5
→ AUTH_SUCCESS_OBSERVED
→ FAST_RESCAN_STOP reason=official_control_ready
→ SEND_RESULT result=SENT
```

分别比较：

- `WAKE_REQUEST → ACL_CONNECTED`：官方连接启动速度；
- `ACL_CONNECTED → AUTH_SUCCESS_OBSERVED`：官方V2鉴权耗时；
- `AUTH_SUCCESS_OBSERVED → SEND_RESULT`：本App按钮交付耗时；
- 若出现`FAST_RESCAN_STOP reason=window_expired`，检查是否未捕获按钮、银河通知被屏蔽或官方鉴权未完成；
- 若出现`PI_CACHE_RESTORED`但未发送，检查是否没有`cmdId=5`或当前动作被插线/行驶/开关拦截。
