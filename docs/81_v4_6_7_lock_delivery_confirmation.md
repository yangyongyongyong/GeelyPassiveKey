# 81. v4.6.7：上锁交付确认与日志版本固化

版本：`4.6.7-min / versionCode 607`

## 1. 用户日志结论

日志编号：`1b1efa5b-18f8-431e-9d75-a33bd8180992`

日志显示自动上锁触发本身正常：

```text
15:29:47.354 进入远离上锁区 -85.3 dBm
15:29:49.359 创建上锁请求 #27，连续远场 2009 ms
15:29:49.364 通过官方通知 PendingIntent 发送“上锁”
15:29:49.365 result=SENT
15:30:19.726 银河蓝牙钥匙断开
```

权限、服务总开关、自动上锁和四项参数均正常。

问题出在交付 token：

```text
lock_button=NONE
cached_lock=PI
notification_post_ms=14:10:09
lock_attempt=15:29:49
```

应用复用了约 79 分 40 秒前的通知按钮 PendingIntent。`PendingIntent.send()` 返回成功只表示 Android 接收了投递，不代表银河 App 或车辆执行成功。

## 2. 修复策略

### 2.1 发送前重读当前通知

自动或手动车控前调用当前活动通知重扫：

- 优先使用当前通知 RemoteViews 里的按钮；
- 历史 PendingIntent 继续保留为最后备用；
- 当前按钮与历史缓存的捕获时间分别记录；
- 日志输出：

```text
notification_current=true|false
notification_current_age_ms=...
notification_fallback=true|false
notification_fallback_age_ms=...
notification_snapshot_age_ms=...
stale_notification_token=true|false
```

### 2.2 旧 token 上锁只复核一次

仅在以下条件同时满足时安排一次上锁复核：

- 本次是自动上锁；
- 官方通知 token 明确交付；
- 当前通知没有按钮，使用超过 60 秒或年龄未知的历史 token；
- 尚未观察到官方“已上锁”状态；
- 用户仍开启自动上锁。

6 秒后重新检查：

- 若已观察到 `LOCKED`，记录“车辆已确认上锁”；
- 若仍远场、官方仍连接、信号新鲜且未进入禁控，执行一次显式“上锁”复核；
- 不满足条件则放弃补发并记录原因；
- 最多一次，不建立无限重试队列。

解锁不使用该策略。解锁失败或结果未知时不得自动重试。

### 2.3 已交付不等于已确认

日志和顶部状态区分：

- `上锁指令已交付，等待车辆确认`
- `车辆已确认上锁`
- `已执行一次显式上锁复核，仍未获得车锁回执`
- `上锁复核结果不确定，已停止重发`

新增实时轨迹：

- `LOCK_CONFIRMING`
- `LOCK_CONFIRMED`

## 3. 日志版本固化

为防止用错代码版本排查，v4.6.7 起：

1. 每一行本地日志都带：

   ```text
   app=4.6.7-min(607)
   ```

2. 诊断 ZIP 固定新增 `build_info.txt`：

   ```text
   schema=1 package=com.luca.geelykeyradar app_version=4.6.7-min(607)
   version_source=PackageManager
   ```

3. `summary.txt` 顶部固定写入：

   ```text
   schema=3 app_version=4.6.7-min(607)
   ```

4. 动作快照同样包含 `build_info.txt` 和 `app_version`。
5. 上传文件名包含版本，例如：

   ```text
   passive-key-upload-v4.6.7-min-607-...zip
   ```

6. 上传元数据继续包含 `appVersion`、`versionCode`，上传成功提示也显示版本。
7. App 端在缺少有效版本时直接拒绝上传；geelylog Worker 同时拒绝 `appVersion` 为空/`unknown` 或 `versionCode <= 0` 的日志包。

版本来源统一为 Android `PackageManager`，不使用硬编码字符串。

## 4. 安全边界

- 不重试解锁；
- 结果未知时不自动回退到第二个 token 源；
- 补发前重新验证服务开关、自动上锁开关、行驶/充电禁控、官方连接、信号新鲜度和远场阈值；
- 用户重新靠近车辆时不得补发上锁；
- 官方小组件锁态确认可作为 `LOCKED` 证据；
- `SENT` 永远不等于车辆执行回执。

## 5. 回归

构建脚本新增：

- `LockDeliveryPolicyTest`
- 日志版本文件与逐行版本断言
- 上锁确认/确认成功实时轨迹测试

要求全部纯 Java 测试、APK 命名回归、资源编译、Java 编译、D8、对齐和签名通过。

## 6. 默认触发参数

```text
蓝牙连接触发：≥ -95 dBm
靠近解锁：≥ -75 dBm
离开上锁：≤ -85 dBm
离开确认：2.0 秒
```

以上为全新安装默认值；升级安装会保留用户已保存的配置。
