# 58. v4.2.2：长时无车后的弱信号提前连接与重试诊断

日期：2026-11-15，Asia/Shanghai。版本：`4.2.2-min / versionCode 202`。

## 1. 结论

用户提出的“最近5分钟没有本车信号，首次出现大概率正在接近”可以作为连接预热的触发条件，但不能直接把所有弱包都当成可信距离。

v4.2.2采用以下分层：

| 场景 | 连接门槛 | 是否参与自动解锁/上锁 |
| --- | ---: | --- |
| 常规连接 | `≥ -95 dBm` | 仍按原身份、RSSI和状态机 |
| 最近至少5分钟无本车信号，首个已学MAC/完整名称强身份候选 | `≥ -102 dBm` | 不直接参与，仅交给官方连接/鉴权 |
| 匿名尾号 HINT | 仍然 `≥ -95 dBm` | 不参与RSSI |
| 解锁/上锁阈值 | 保持 `-75 / -82 dBm` | 完全不变 |

弱信号只负责“更早叫醒银河并建立蓝牙钥匙连接”，不会因为 `-102 dBm` 就直接发解锁。

## 2. 为什么不是直接把 -95 全局改成 -102

直接全局放宽会让停车场、墙后、邻车和手机姿态变化产生更多误唤醒：

- 官方 `MiuiService` 可能收到弱信号后尝试连接但失败；
- 多辆同品牌车辆时，弱候选可能占用单目标连接预算；
- 弱包不适合参与测距，不能拿来触发车控；
- 失败重试过多会干扰真正靠近时的连接。

因此限制为：

1. 已学习本车身份；
2. `MAC` 或完整名称/厂商/VIN 尾号匹配；
3. 至少5分钟没有本车信号；
4. 同一次长时无车会话最多使用一次弱信号宽限；
5. 仍受同MAC ACL 5秒保护、单目标12秒预算和30秒失败冷却。

## 3. 解锁请求等待期间的重试

现在如果 RSSI 已达到解锁条件，但银河尚未鉴权：

```text
REQUEST_CREATED
→ WAIT_OFFICIAL
→ WAKE_REQUEST / ACL
```

待发解锁会重置同一接近会话的12秒重试预算，让后续广播继续尝试建立官方连接；仍不会绕过：

- 总开关；
- 插线禁控；
- 行驶保护；
- 已学身份；
- 官方 PendingIntent / GATT 鉴权就绪；
- 两次发送失败上限。

## 4. 新增诊断日志

### `WEAK_CANDIDATE`

低于常规 `-95` 的连接门槛、但尚未进入候选的包：

```text
WEAK_CANDIDATE addr_id=... match_reason=ADDRESS rssi=-101 threshold_dbm=-95 sampled_mono_ms=...
```

用途：

- 区分“完全没收到广播”和“收到了但被弱信号门槛拒绝”；
- 统计用户接近车辆时第一次出现到底是 `-105/-102/-96/-95`；
- 评估是否还需要继续放宽，而不是盲目改门槛。

### `WAKE_ACL_WINDOW`

每次主唤醒后1.2秒诊断一次：

```text
WAKE_ACL_WINDOW wait_ms=1200 acl_seen=false connected=false
```

- `acl_seen=false`：第一次唤醒后没有建立同MAC ACL，随后才补发兼容action；
- `acl_seen=true`：第一次唤醒已进入握手，不再重复注入。

### `WAKE_REQUEST` 新增

```text
threshold_dbm=-95|-102
long_absence_ms=...
since_first_wake_ms=...
```

### 心跳新增

```text
absence_age_ms=...
last_vehicle_age_ms=...
weak_candidate=...
```

## 5. 对最近三次成功连接的日志结论

已留存的 v4.1.x/v4.2.0 日志中，3次成功连接均为：

```text
WAKE_REQUEST attempt=1
→ ACL_CONNECTED
```

首包 RSSI 分别为：

```text
-65 dBm
-68 dBm
-87 dBm
```

目前没有观察到“第一次失败、第二次才成功”的留存证据。因此本轮不能把罚站归因于第二次连接，只能把它列为待验证假设；新增 `WEAK_CANDIDATE` 和 `WAKE_ACL_WINDOW` 后，下一次实车日志可以直接判断。

## 6. 验收方法

测试前拔掉充电线。每次靠近记录：

```text
最近一次本车信号时间
→ WEAK_CANDIDATE rssi
→ WAKE_REQUEST threshold_dbm
→ WAKE_ACL_WINDOW acl_seen
→ ACL_CONNECTED
→ AUTH_SUCCESS_OBSERVED
→ SEND_RESULT
```

重点比较：

- `WEAK_CANDIDATE` 是否出现在 `-96 ~ -102`；
- 第一次 `WAKE_REQUEST` 后 `acl_seen` 是否为`false`；
- 是否需要第二次 `WAKE_REQUEST` 才能 `ACL_CONNECTED`；
- 长时无车后的弱连接是否把 `ACL_CONNECTED` 提前到距离车更远的位置；
- 上锁是否仍按原 `-82 dBm / 4.5s` 稳定执行。

## 7. 证据边界

- `-102 dBm` 是连接预热门槛，不是距离裁定，也不是解锁门槛。
- 合成测试只能证明门控和身份隔离，不证明实车在某个米数一定能连上。
- 当前留存日志的3次成功连接均为第一次唤醒成功，不代表以后不会出现第二次重试。
- 弱包没有进入RSSI授权，不会制造自动解锁或上锁资格。
