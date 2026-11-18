# 39. v4.0.2：日志增强与诊断包使用

版本：`4.0.2-min / versionCode 102`。本次补齐观测与手动导出，默认参数继续使用 `-75 / -82 dBm / 4.5s`，解闭锁判定、失败预算和保护策略沿用 v4.0.1。

## 1. 出现问题后怎么操作

1. 记下大概时间、解锁还是上锁、是否息屏/放口袋/插线，以及有没有听到车辆钥匙连接声。
2. 打开本应用，滑到最底部，点 **“导出排查日志（ZIP）”**。
3. 在系统文件选择器中保存。把保存的ZIP和发生时间提供给排查人员。

导出无需电脑、adb、root或存储权限，不会自动上传文件。系统文件选择器打开前会采集状态；返回时如果服务已重新绑定，会更新快照。`snapshot_captured_mono_ms`标明采集时间，和ZIP实际生成时间可能不同。

排查前不要清空日志或卸载应用。日志会轮转，建议问题发生后尽快导出；不能保证保存所有历史事件。界面只显示最近十条，并不代表后台只有十条。

## 2. 固定容量与写入策略

| 类型 | 文件 | 容量 |
| --- | --- | --- |
| 关键事件 | `passive_key.log`及`.1`—`.3` | 4份，每份256KiB，共1MiB |
| 诊断细节 | `passive_key_diagnostic.log`及`.1`—`.5` | 6份，每份512KiB，共3MiB |
| 界面 | 最近关键事件，最新在前 | 最多10条 |
| 内存写入队列 | 所有模块共用单一写入线程 | 最多256个任务，满时丢弃并记录计数 |

总日志上限 **4MiB**。文件达到上限时只淘汰最老分段，不整批删除历史。单行正文限制8192字符；过长信息截断并标记。日志与导出操作在后台执行，车控线程不等待磁盘。

- 常态RSSI约每5秒保存一次；上锁驻留或请求等待期间约每500ms记录判定现场。
- 判定原因变化及区间变化有节流；长期相同原因仍周期保留。
- 每30秒保存心跳和窗口计数，没有信号时也保存，便于识别无回调。
- `dropped`和`io_errors`记录未接纳或写入失败数量，非零表示现场可能不完整。
- 一键清空通过同一队列删除两组分段，并保留清空事件。身份脱敏盐和服务运行标记不随日志清空。
- 导出期间生成一个缓存ZIP，写到用户选择的文件后清理暂存。用户保存的文件由用户管理，不计入后台4MiB日志容量。

## 3. ZIP内容及脱敏

| 文件 | 用途 |
| --- | --- |
| `summary.txt` | App/银河版本、手机品牌型号和Android版本、当前配置、运行状态、日志健康计数 |
| `events.log` | 服务启动/结束、连接边沿、动作请求、发送尝试/结果、取消/超时、信号过期、异常 |
| `diagnostic.log` | 收包与过滤计数、RSSI、判定原因、等待阶段、GATT读取状态、通知解析和省电心跳 |
| `README.txt` | 包内字段释义及证据边界 |

分段按旧到新合并。ZIP在统一写入队列中生成，只包含已接纳并在导出任务之前完成的日志前缀。普通导出不会读取或打包SharedPreferences、身份盐、扫描原始字节、官方通知正文、银河账号或钥匙凭证。用户主动进行“车型兼容问题”上传时，会额外附带 `compatibility_capture.txt`，其中保留原始 BLE 广播与可见 GATT，用于协议兼容分析。

车辆地址和名称从日志源头写为本机加盐指纹`addr_id/name_id/identity_id`。导出时再次处理旧版本中的MAC、VIN特征和身份事件详情，包括自定义车名。相同地址在本机可关联；指纹只用于诊断，不能作为钥匙或车辆授权依据。设备品牌型号、版本、时间和数值状态会保留。

## 4. 关联时间与请求

每条新日志有完整日期、时区及以下前缀：

```text
2026-11-07T12:00:00.000+0800 sid=12ab34cd pid=12345 seq=80 mono_ms=100000 src=Control event=REQUEST_CREATED trace_run=ab12cd34 req_id=2 action=LOCK ...
```

- `sid/pid`区分进程；同一进程内前台服务重建会生成新的`trace_run`。
- **用`sid + trace_run + req_id`关联一次请求**，不能只看`req_id`，因为服务重建后编号可能重用。
- `seq`是日志编号，不是车控次数。`wake_id`关联一次向官方组件注入广播的多个入口。
- `mono_ms`是本次开机单调时间，适合比较耗时。设备重启后重新计时；墙钟可受校时影响。
- 时间/年龄`-1`表示未观察到，不代表刚发生。历史段中没有这些字段的行来自旧版。

## 5. 关键事件和判定字段

| 范围 | 事件/字段 | 可以回答的问题 |
| --- | --- | --- |
| 扫描 | `SCAN_START/SCAN_CALLBACK_ERROR/CANDIDATE_FIRST_SEEN/TARGET_FIRST_SEEN`，`scan_callbacks/scan_not_geely/scan_stale/scan_duplicate/scan_other_vehicle/scan_non_primary/scan_accepted` | 没收到回调，还是回调被过滤？ |
| 原始信号 | `RSSI_SAMPLE`，`raw/smooth/approach/pocket/age/source` | 是原始弱信号、滤波、口袋补偿还是旧样本？ |
| GATT | `CONNECT_START/STATE_CALLBACK/CONNECT_TIMEOUT/CLOSE`，`gatt_attempt/gatt_addr_id/read_pending/read_request_age_ms/read_success_age_ms/read_status` | 通道没有连上，还是读取请求被拒绝/回调停更？ |
| 官方通知 | `LISTENER_CONNECTED/LISTENER_DISCONNECTED/REMOVED/PARSED/STATE_CLEARED`，`FAST_RESCAN_START/EXTEND/STOP`，`PI_CACHE_WRITTEN/RESTORED/ERROR` | 监听是否掉线、锁屏刷新是否慢、快速补扫为何结束、官方按钮是否已缓存？ |
| 唤醒官方 | `WAKE_REQUEST/WAKE_BEGIN/WAKE_SERVICE_RETURN/WAKE_SERVICE_ERROR/WAKE_BROADCAST_SENT/WAKE_FALLBACK_DUE` | 哪个入口被系统接纳或拒绝，1200ms无ACL后是否补发兼容入口？ |
| 鉴权观察 | `AUTH_OBSERVE_START/AUTH_OBSERVE_CONNECTED/AUTH_OBSERVER_READY/AUTH_FRAME/AUTH_SUCCESS_OBSERVED/AUTH_OBSERVE_TIMEOUT` | 只读GATT是否观察到最后一条DK1.5鉴权帧，何时提前确认官方鉴权完成？ |
| 车控 | `REQUEST_CREATED/REQUEST_WAIT/SEND_ATTEMPT/SEND_RESULT/REQUEST_CANCELLED` | 是否生成请求、等待什么、何时取消、通过哪条路径发送？ |
| 运行 | `PROCESS_START/SERVICE_BEGIN/SERVICE_START_COMMAND/SERVICE_END/PREVIOUS_PROCESS_EXIT/TICK_GAP/HEARTBEAT` | 应用重启、内存压力、调度长间隔或权限/省电变化？ |

扫描/GATT/通知计数形如`scan_callbacks=20(+3)`，分别表示进程累计20次、本窗口新增3次。导出快照不会推进计数窗口。

官方通知时间分开记录：

- `posted_callback_age_ms`：最近一次真实`onNotificationPosted`回调的年龄；启动时仅扫描已有通知则可以是`-1`。
- `notification_seen_age_ms`：最近通过回调或活动通知扫描看见它的年龄。
- `notification_parse_age_ms`：最近完成解析的年龄。
- `notification_post_ms`：系统通知自身的墙钟时间，不能直接当作蓝牙连接/断开时刻。

状态机原因来自实际执行的返回分支，诊断快照不会再次运行状态机：

| `sample_reason/official_reason` | 含义 |
| --- | --- |
| `UNLOCK_THRESHOLD_REACHED/OFFICIAL_READY_UNLOCK/LOCK_DWELL_READY` | 本次已产生对应动作请求 |
| `APPROACH_LATCHED` | 本次接近已占用解锁机会，防止重复 |
| `UNLOCK_DISABLED/LOCK_DISABLED` | 对应自动开关关闭 |
| `DRIVING_OR_WIRED_BLOCK` | 行驶或插线禁控，结合`driving/wiredBlock`区分 |
| `NO_NEAR_EVIDENCE` | 从未接近的远处会话不能补锁 |
| `SIGNAL_LOSS_INHIBIT/NEAR_GUARD` | 断连保护或近期近场保护仍有效 |
| `LOCK_NEEDS_SAMPLES/LOCK_DWELL_IN_PROGRESS` | 新样本数或连续确认时间尚不足 |
| `CONTROL_PENDING/CONTROL_RETRY_COOLDOWN/UNLOCK_COOLDOWN` | 已有请求或冷却中 |
| `UNLOCK_REQUEST_LIMIT/LOCK_REQUEST_LIMIT/RETURN_REQUIRED_AFTER_FAILURE` | 本会话失败预算耗尽，等待真实离开再返回 |
| `RETURN_CONFIRMATION/FRESH_FAR_SIGNAL/BUFFER_ZONE` | 回程未确认、新鲜远场或处于安全缓冲带 |
| `APPROACH_NOT_FINISHED`（v4.0.3起） | 虽然已提前解锁，但还没有真正进入车旁，也没有同源持续变弱的离开证据，上锁被拦截 |
| `INVALID_SAMPLE_OR_ORDER` | 无效或乱序样本 |

同时保留`dwell_ms/dwell_samples/near_guard_left_ms/inhibit_left_ms/retry_left_ms/unlock_requests/lock_requests/return_required/return_ms/return_samples`及原因年龄，避免把旧原因误认作当前判断。

v4.0.3新增`departure_armed/early_unlock_age_ms/early_peak_dbm/departure_drop_dbm/rssi_source`，区分真正到过车旁、尚在提前接近，以及广播/GATT采样来源切换。相关故障与修复见文档40。

## 6. 证据边界与复盘顺序

v4.1.4先按大概时间找到`CANDIDATE_FIRST_SEEN`（普通选择前），再对照`WAKE_REQUEST → ACL_CONNECTED → Official CONNECTED → REQUEST_* → SEND_*`；旧版只有`TARGET_FIRST_SEEN`，该事件可能晚于最早候选出现；离开问题结合`RSSI_SAMPLE`和`sample_reason`查看驻留是否成立。

- 官方服务调用返回`accepted=true`只表示系统接纳启动请求，广播也没有鉴权回执。
- `SENT`表示本应用已通过通知按钮交给银河；本版本没有车锁执行回执。`UNKNOWN`不能当作发送成功或确定失败。
- `TICK_GAP`可能来自休眠或调度延迟，不能单独认定被系统冻结。
- `previous_clean_stop=false`只表示没有记录到正常停止；可结合Android11以上的最近三次本应用退出原因分析。退出历史未返回不证明从未退出。
- 日志轮转、队列拒绝、进程突停都可能丢失记录，没有日志不等于没有事件。

## 7. 验证

`SKIP_INSTALL=1 bash GeelyKeyRadarApp/build.sh`运行六组测试并生成签名APK。`DiagnosticsTest`覆盖：只读快照不改变动作、原因与分支一致、计数空间有界、ZIP白名单、旧日志和自定义车名脱敏、4MiB容量、清空顺序、服务未正常结束标记。车控原有五组测试继续运行。

Android系统文件选择器与厂商后台行为需要真机验证；自动测试不模拟真实车辆鉴权或锁车回执。

### 本次验证结果（2026-11-07）

- 六组测试全部通过，APK完成编译、对齐和签名验证。
- 已通过ADB覆盖安装到连接的vivo手机，系统包信息确认`versionCode=102 / 4.0.2-min`。
- 安装前后对照偏好，车辆身份、两个自动开关和`-75/-82/4500ms`参数保持一致。
- 手机处于USB供电且插线禁控开启；启动后确认两组日志文件及服务运行标记已生成。
- ZIP一致性、白名单、容量和脱敏经离线测试验证。随后ADB连接中断，尚未完成系统文件选择器保存ZIP的真机操作验证。

## v4.1.1 多车排查新增字段

当前构建运行11组测试；本页上方的六组测试/安装结果属于2026-11-07历史版本。

- `FILTER_RECONFIGURE` 与 `filter_mode=LEARNING/HINT_DISCOVERY/TRACKING`：确认是否真正从协议广扫切换为本车窄过滤；`scan_start_budget_wait` 表示主动限频，不是已停止工作的证据。
- `MATCH_RESULT`：`match_reason`、广告 `addr_id/name_id/prefix_id`、`company`、`sampled_mono_ms` 和 `scan_age_ms`，每5秒最多一条，不记录原始广播或VIN全文。
- `TARGET_SWITCH`：目标地址指纹及原因；`target_age_ms` 从选择时刻起算，未知候选12秒预算不会被持续广播续期。`candidates` 是近期物理MAC候选数（最大32），仍不能简单等同实际车辆数。
- `IDENTITY_UPDATED/CONNECTED_IDENTITY`：地址变化是否来自 `ADDRESS` 同MAC补全或唯一可验证的连接；跨地址 `NAME/HINT` 都不得直接合并身份。
- `scan_discovery_only`：只尝试官方鉴权，未用于RSSI/自动车控；`max_scan_age_ms` 可帮助识别回调排队；`rssi_stale_or_duplicate` 表示迟到/重复样本未计入驻留。
- `MEASUREMENT_TARGET_CHANGED`：物理测距目标切换，旧距离与驻留已撤销，但已发送/手动上锁锁存仍保留。

`scan_accepted` 从v4.1.1起仅统计可信单目标广告；候选发现包单独记录，不能直接与旧版未学习期间的接受率比较。详细规则与实车验收见 [47_v4_1_1_multicar_filter_and_identity.md](47_v4_1_1_multicar_filter_and_identity.md)。


## v4.1.4 预连接分段字段

- `CANDIDATE_FIRST_SEEN`：每个物理候选首次出现（先于普通主目标选择），记录`match_reason/sampled_mono_ms/scan_age_ms/rssi/advertised_name/early_reason`；匿名提示首见最多每秒一条，其余计入`suppressed`，不全面逐包刷日志。
- `advertised_name=true`表示当前`ScanRecord`携带精确名称；系统缓存设备名仍可以参与原普通发现，但不冒充新鲜命名广播首包资格。
- `early_reason=FULL_NAME_VENDOR_TAIL`仅代表可提前交给官方的公开线索，不代表真实地址已学、RSSI可信或已鉴权。`NO_LEARNED_ADDRESS/NO_FRESH_FULL_NAME/VENDOR_OR_TAIL_MISMATCH/LEARNED_ADDRESS_FRESH`说明不能走首包路径。
- `candidate_age_ms/sightings/named_sightings/named_span_ms/name_reset`附在候选、匹配与实际唤醒事件中；`NAME_GAP`为命名间隔超过3秒重置，`NAME_CHANGED`为同MAC车名变化，跨度取实际命名采集时间，不是Handler等待时间。
- `WAKE_REQUEST phase=SEND_EARLY_NAME`为普通名称确认前的请求，`SEND_SELECTED`为原主目标路径。`since_first_ad_ms`从实际尝试地址的首候选采集时间算，稍后名称确认不覆盖起点；后续`WAKE_BEGIN`仍是实际桥接调用阶段。
- `WAKE_WAIT reason=WAIT_ACL_AUTH`表示同MAC/当前在途目标已有真实ACL，五秒内不再注入；**不是鉴权完成**。`CANDIDATE_IN_FLIGHT/CANDIDATE_COOLDOWN`分别是单目标占用和失败冷却。
- `WAKE_BUDGET_EXPIRED`为未验证预连接十二秒预算结束，进入三十秒候选冷却；不结束全局扫描或已验证GATT监测。
- 心跳/导出增加`wake_target_id/wake_reason/early_reason/wake_attempts/wake_attempt_age_ms/wake_last_age_ms/wake_wait_verified_peer`；官方边沿增加`wake_session_id`。所有地址仍只记录指纹。
- `wake_wait_verified_peer=true`仅表示新增首包会话的实际物理地址待验证，旧保存MAC完整不绕过；`REQUEST_WAIT reason=WAIT_VERIFIED_PEER`说明等待这一证据，不是RSSI阈值慢。真实连接验证采用后继续，官方断连/服务重置清理。
- `wake_early_name`统计提前请求，`wake_wait_acl_auth`统计握手等待。`TARGET_FIRST_SEEN`仍表示进入普通主目标处理，不得用它替代首候选事件计算预连接等待。

复盘需区分“首候选→本App注入”“底层ACL→官方就绪”“官方就绪→通知交付”。ACL没有发起进程身份、公开字段不是唯一车辆凭证，通知交付也不是车锁执行回执；缺失逐包/站位标记时不能宣称精确还原广播或RSSI米数。详见文档50。

## v4.1.5 稳定掩码发现排查

v4.1.5没有新增日志字段，但`TRACKING`的厂商过滤从六字节全等改为两个稳定掩码：`FF 00 FF 00 FF FF`优先、`FF 00 00 00 FF FF`兜底antenna变化。日志解释应同步调整：

- `FILTER_RECONFIGURE filter_mode=TRACKING`仍表示已学习窄过滤；不再从“六字节前缀是否完全相同”判断同车。相同尾号但version/devices变化可通过优先掩码，antenna变化可通过兜底掩码。
- `MATCH_RESULT match_reason=HINT`可能来自地址轮换的稳定掩码；`prefix_id`变化不等于另一辆车。只有Company ID、尾号、车名冲突或真实连接验证才能否定身份。
- `scan_discovery_only`仍表示匿名HINT未用于RSSI；`scan_other_vehicle`减少不代表其他吉利消失，也可能只是未落入本车VIN掩码。
- `WAKE_REQUEST phase=SEND_EARLY_NAME`仍要求当前`ScanRecord`完整本车名；匿名稳定掩码包只能走两条样本/1500ms后的`SEND_SELECTED`，不能因为过滤放行就提前连接。
- `scan_stale/scan_duplicate/rssi_stale_or_duplicate`含义不变；HINT不能进入`RSSI_SAMPLE`，也不能修改已学MAC。若看到该现象应按身份隔离回归处理。

复盘仍按`首候选 → WAKE_REQUEST → WAKE_BEGIN → ACL → Official CONNECTED → REQUEST_* → SEND_*`分层，详见 [52_v4_1_5_stable_mask_discovery.md](52_v4_1_5_stable_mask_discovery.md)。

## v4.1.6 车机参数只读探针

新日志源`VehicleParam`只记录只读AIDL查询和原始回执摘要，不参与解锁/上锁判定：

- `READ_REQUEST`：一次查询同时发送`5392=2` PE状态、`272=0`车况、`305=0`标定读取请求；字段`write_actions=none`明确表示没有写操作。
- `PE_SWITCH_RESULT`：`0x0501`回执的request/state/result/platform与最多64字节raw摘要。state=1开启、0关闭，**不是RSSI或距离阈值**。
- `CALIBRATION_RESULT`：收到`0x0132`时记录原始回执摘要；若5秒内没有回执，记录`READ_TIMEOUT`并在界面显示L6/DK1.5可能不支持`0x0131`读取。
- `VEHICLE_STATUS_RESULT`：`0x0121`车况原始帧摘要；仍以现有通知/逻辑判断为准，不因该帧到达放行车控。
- `APPROACH_LOCK_EVENT`：若车机主动上报`0x0159`，记录原始摘要，不据此发送上锁或改变状态机。
- `AIDL_CONNECTED/AIDL_DISCONNECTED/AIDL_BINDING_DIED`：只表示官方`DigitalKeyBgService`绑定状态，不等于蓝牙钥匙已鉴权或车辆可控。

持久化位置为应用私有`SharedPreferences("vehicle_param_readonly_prefs")`，界面卡片“车机参数（只读）”展示最近值与时间；导出ZIP仍按原有脱敏规则处理。不要根据`PE_SWITCH_RESULT`推断距离阈值，也不要在未备份原值与验证回滚前尝试任何写入。详见 [53_v4_1_6_readonly_vehicle_param_probe.md](53_v4_1_6_readonly_vehicle_param_probe.md)。

## v4.1.7 实时曲线读数

主页“蓝牙信号实时曲线”每500ms读取现有`Status.rssi / Status.smoothRssi / lastSampleAgeMs / source`，不新增日志事件，也不调用任何扫描或读取API。曲线蓝线是原始RSSI，黄线是`RssiFilter`平滑值，绿色/红色虚线是当前解锁/上锁阈值；无新鲜样本时曲线断开。

对照日志时，曲线当前值应与最近一条`Signal`事件的`raw/smooth/approach`及样本年龄一致。曲线尖峰不等于车机实际距离变化，必须结合`source=广播/GATT`、遮挡、手机姿态和官方就绪阶段判断。曲线本身不改变状态机，不能把“曲线已达到阈值”当作车控已发送或车锁已执行。详见 [54_v4_1_7_realtime_rssi_chart.md](54_v4_1_7_realtime_rssi_chart.md)。

## v4.2.1 锁屏快速补扫与新日志

- `FAST_RESCAN_START`：唤醒、ACL或`cmdId=5`后开始高频`getActiveNotifications()`，当前窗口为50ms一查、最长15秒。
- `FAST_RESCAN_EXTEND`：新的唤醒/GATT事件延长已有窗口，不创建第二套按钮队列。
- `FAST_RESCAN_STOP reason=official_control_ready`：通知存在、官方已连接且当前解锁/上锁按钮已经捕获，快速补扫真正完成。
- `FAST_RESCAN_STOP reason=window_expired`：15秒内仍未满足上述条件，不能当作成功；要结合通知权限、银河通知是否被屏蔽和`AUTH_*`判断。
- `WAKE_FALLBACK_DUE`：主`MiuiService`请求后1200ms仍没有同MAC ACL，才补发一次兼容action；已有ACL时不得出现该事件。
- `AUTH_OBSERVE_START/AUTH_FRAME/AUTH_SUCCESS_OBSERVED`：ACL后只读附加GATT观察；`cmd_id=5`表示DK1.5 Session响应。`auth_cmd5_age_ms`是最近一次观察年龄。
- `STABLE_HINT_HIGH_RSSI`：已学完整身份、强信号且稳定厂商/VIN尾号一致的首个匿名包提前交给官方；仍不是RSSI或车控资格。
- `PI_CACHE_WRITTEN/RESTORED/RESTORE_ERROR`：官方PendingIntent是否写入私有缓存、进程/监听重建后是否恢复、格式失效是否清除。
- 心跳/诊断快照增加`auth_observe`、`auth_cmd5_age_ms`、`gatt_auth_ready`、`cached_unlock`、`cached_lock`；`cached_*`表示上一次可复用按钮，不表示当前通知仍在。

复盘锁屏慢解锁时，应优先确认：`FAST_RESCAN_START`是否存在、结束原因是什么、`AUTH_SUCCESS_OBSERVED`是否早于通知刷新，以及`SEND_RESULT`前是否出现`PI_CACHE_RESTORED`。如果用户从未授权通知使用权且没有历史缓存，仍不能凭空生成官方按钮token。

## v4.2.2 长时无车弱信号连接日志

- `WEAK_CANDIDATE`：低于常规-95连接门槛的包只记录漏斗，不进入RSSI或车控；关键字段`addr_id/match_reason/rssi/threshold_dbm/sampled_mono_ms`。
- `WAKE_REQUEST threshold_dbm=-95|-102`：`-102`只在至少5分钟没有本车信号、首个已学MAC/完整名称强身份候选中使用一次。
- `WAKE_ACL_WINDOW acl_seen=false`：第一次唤醒后1.2秒没有同MAC ACL，随后才补发兼容action；`true`表示首次已进入握手。
- `since_first_wake_ms`：同一唤醒会话内第二次及后续尝试距第一次实际唤醒的时间。
- 心跳/扫描快照新增`absence_age_ms/last_vehicle_age_ms/weak_candidate`，用于判断是没收到包还是被弱信号门槛拒绝。
- 待发解锁处于`WAIT_OFFICIAL`时会刷新同一会话的重试预算，仍受请求12秒期限、插线/行驶保护和ACL 5秒保护。
