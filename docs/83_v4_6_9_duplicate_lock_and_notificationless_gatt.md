# 83. v4.6.9：禁止当前按钮重复上锁与通知缺失时的 GATT 学习

版本：`4.6.9-min / versionCode 609`

本次处理两份 `v4.6.8-min / 608` 用户日志。

## 1. vivo `V2426A`：锁车两次

日志编号：`b31d6f5b-4259-4327-816f-806cccb81bba`

时间线：

```text
20:10:53.718  首次上锁 transport=VIEW_CURRENT result=SENT
20:10:53.723  LOCK_RETRY_SCHEDULED token_age_ms=1086730
20:10:59.969  LOCK_RETRY_ATTEMPT
20:10:59.978  第二次上锁 transport=VIEW_CURRENT result=SENT
```

两次上锁间隔约 6.26 秒。

### 根因

v4.6.8 正确识别出通知快照约 18 分钟未更新，因此把 token 判为旧 token 并安排复核。

但第一次已经通过当前 RemoteViews 按钮的 `View.performClick()` 成功交付，这等价于点击当前官方通知按钮；不应再因“通知快照旧”而补发第二次。

### 修复

- 发送通道加入缓存来源判定；
- 只有实际使用 `PI_CACHED` / `VIEW_CACHED` 时才允许旧 token 上锁复核；
- `VIEW_CURRENT`、`PI_CURRENT`、Widget 和 `UNKNOWN` 均不进入复核；
- 复核仍最多一次，解锁仍永不自动重试。

## 2. Redmi `23049RAD8C`：银河搜不到

日志编号：`8035d718-be55-4461-a588-7d4405fd3806`

兼容抓取明确看到：

```text
GeelyAuto 广播观察 34 次
RSSI 范围 -105 ~ -91 dBm
系统 GATT 连接数 1
GATT 设备名 GeelyAuto
```

但应用状态一直为：

```text
identity=none
learning_complete=false
notification_present=false
official_connected=false
```

### 根因

旧代码中的 `findIdentityFromConnectedGatt()` 强制要求官方通知已连接。系统 GATT 已能看到唯一 `GeelyAuto`，但通知栏缺失时该方法直接返回空，导致本车身份永远无法完成学习。

### 修复

- 新增无通知 GATT 学习路径；
- 只接受当前 GATT 列表中唯一、明确命名为 `GeelyAuto` / `Geely_Auto` / 吉利车名的 LE 连接；
- 多个吉利连接时拒绝猜测；
- 只有普通耳机/手表等非吉利名称时拒绝；
- 通知在线时仍沿用原有的 GATT 增量、唯一非绑定 LE 和 ACL/注入候选路径。

## 3. 安全边界

- 不把任意唯一 GATT 设备都学习为车辆；
- 不读取官方 App 私钥、账号或车辆凭证；
- 无通知学习仍只保存公开的蓝牙连接身份；
- 当前按钮成功交付不等于车辆回执，但不再主动重复上锁；
- Android 14+ 的 PendingIntent 仍只保存在内存，不持久化。

## 4. 回归

- `ConnectedPeerSelectorTest` 新增无通知唯一吉利 GATT、普通设备拒绝和多吉利连接拒绝；
- `LockDeliveryPolicyTest` 新增当前 View/PI 成功不得复核、仅历史缓存 token 可复核；
- 全部纯 Java 测试、APK 命名测试、资源/Java 编译、D8、对齐和签名要求通过。
