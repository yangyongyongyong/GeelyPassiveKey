# 50. v4.1.4：提前连接与可信测距分离，优化解锁及时性

日期：2026-11-13，Asia/Shanghai。版本：`4.1.4-min / 114`。

**范围：解锁及时性优先，不做功耗优化；上锁稳定，保持其输入、阈值、保护和车控发送链路不变。**本轮只修改本地代码、测试、文档和构建APK，未安装手机、未操作Git、未上传/发布。

## 1. 三份实车日志的依据

用户反馈：11月12日20:14在车后2–3米解锁，距离满意；20:36上锁稳定；11月13日10:43到车旁紧贴车辆约一秒后才解锁，期望3–5米处门已经打开。手机导出版本是`4.1.1-min / 111`；本地之前是`4.1.3-min / 113`，4.1.2/4.1.3仅增加版本显示、APK名称与阈值UI，核心仍对应手机。

| 可见阶段 | 11/12 解锁 | 11/13 解锁 |
| --- | ---: | ---: |
| 最早可见匹配 | 20:14:24.376，-85dBm | 10:43:00.528，-78dBm |
| 匹配 → 本App `WAKE_BEGIN` | 1539ms | **4044ms** |
| 本App `WAKE_BEGIN` → 官方就绪 | 3119ms | 869ms |
| 官方就绪 → 通知PI交付 | 24ms | **17ms** |
| 匹配 → 通知PI交付 | 4682ms | 4930ms |

今早首个可见匹配为`NON_PRIMARY_NAME`，新MAC与旧学习地址不同。10:43:04.554才成为`ROTATION_DISCOVERY`，此前等待4026ms；05.440官方就绪，05.458完成PI交付，05.499才收到本App首个GATT测距样本。该次前置等待不是-75阈值/滤波/500ms采样/通知发送。

**必须保留的证据边界：**

- 新MAC的NAME普通路径要求至少两条命名广播且跨度≥600ms；匿名HINT至少两条且跨度≥1500ms。时间自然过去不等于有了第二条实际广播。
- 命名证据间隔超过3秒会重新观察，稀疏`ScanResponse`可能拖长候选确认；旧`MATCH_RESULT`每5秒节流，不能恢复所有逐包或断言哪包实际触发条件。
- 今早同MAC底层ACL在10:43:01.319就已连接，比本App这次唤醒早3252ms。869ms不是完整从零开始的鉴权时间；ACL没有调用者包名，不能断言发起者是银河。
- 两次“最早可见匹配到交付”总耗时只差248ms，不能把今早全部4秒等待都宣称可回收。
- 距离来自用户反馈；日志没有3–5米/贴车时刻标记，RSSI不能直接换算米数；PI交付不是车辆实际开门回执。

证据文件：

- 原始对照报告：`/tmp/geely_unlock_20261113.ws0fjw/diagnosis.md`。
- 今早：`/tmp/geely_unlock_20261113.ws0fjw/GeelyPassiveKey-logs-20261113-104321/diagnostic.log`的1949–1964行，`events.log`的1708–1725行。
- 昨天解锁：`/tmp/geely_unlock_20261113.ws0fjw/GeelyPassiveKey-logs-20261112-201444/diagnostic.log`的2149–2161行，`events.log`的2023–2035行。
- 昨天上锁：`/tmp/geely_unlock_20261113.ws0fjw/GeelyPassiveKey-logs-20261112-203622/diagnostic.log`的2267–2281行，`events.log`的2058–2061行。

这些是本机临时保留的导出，不是入库的公开用户数据；文档只引用时间与指纹用途，不写真实MAC、VIN或钥匙凭证。

## 2. 改什么：首包先连接，但不先信任

原路径把“普通主目标确认”与“第一次交给官方连接”绑在一起：未选中主目标就在`NON_PRIMARY_*`分支返回，无法提前唤醒。

新路径：

```text
真实新鲜广播
  → 解析/年龄/重复检查不变
  → 候选首见日志（在普通选择前）
  → 已学完整本车名 + 当前包厂商/VIN线索合格？
      是：独立首包预连接门控 → 把原始ScanResult交给官方
      否：保留原NAME/HINT观察，不能首包盲连
  → 官方完成自己的连接/鉴权
  → 官方就绪 + 唯一可验证的实际连接
  → 才保存新MAC、固定测距目标、挂GATT
  → 原官方就绪一次机会/距离阈值/单队列车控
```

### 快速路径全部条件

1. 已保存合法本车地址及完整`GeelyVehicle*`名称，不只是一段手填尾号。
2. **当前`ScanRecord.getDeviceName()`**携带与已学名称相同的完整名称；`BluetoothDevice.getName()`缓存补名不能冒充本包命名证据。
3. Company ID一致、可识别vendor、六字节数据完整、VIN尾号与已学名称/前缀一致；版本和中间状态字节可变化。
4. 广播采集年龄≤3秒、RSSI≥-95，当前物理候选记录未过期/冷却，没有已知名称或尾号冲突。
5. 已学本车MAC不仍有新鲜广播，官方未连接；没有同MAC、当前在途目标或已学本车MAC的近期真实ACL握手。
6. 没有另一个未知目标正在尝试；所有路径共用单个在途候选、节流和预算。

不是整段六字节前缀相等：日志显示同车名的广播前缀指纹可能随连接状态变化。整段精确相等会挡住本来能用的命名首包。不是只按Company ID或两位尾号放行：完整名也可能重名，所有字段都只是公开发现线索，最终官方钥匙仍负责鉴权。

匿名同尾号、只有名称没有厂商数据、厂商ID不一致、旧身份没有完整名称/地址、缓存名、过期、重复和弱包**不走快速路径**，普通发现兼容性仍在。

### 不授予的新权限

首包预连接不修改`VehicleIdentity`，不写偏好，不调用`onFreshRssi`，不向`PassiveKeyLogic`喂样本，不制造解锁/上锁请求，也不提前`connectGatt`抢车机槽位。普通候选确认后也仍然只有发现资格；身份采用仍需要官方就绪及系统唯一可验证的匹配连接。新首包路径额外标记`wake_wait_verified_peer`：即使旧保存地址完整，官方通知先就绪而本次实际连接尚未唯一可验证时，也先不消费就绪解锁机会、不交付车控、不回退旧MAC主动挂GATT；每秒重查真实连接证据，验证采用后立即继续。这只约束新增首包会话，不增加原已学MAC路径或稳定上锁会话的等待。

## 3. 防止重复连接与串车

新增纯Java`VehicleWakeGate`管理实际注入，与`VehicleTargetSelector`共用候选预算。

- **同地址确认不清旧状态：**首包已经发起后，后续600ms确认、名称断档或`TARGET_SWITCH`不再清掉同地址唤醒次数/节流/ACL保护。
- **ACL按物理地址记录：**同MAC或正在尝试的目标有真实ACL时，保留原5秒不重注入保护。无关设备ACL不造成全局延迟，旧地址断连不解除新地址保护；ACL只表示底层连接，不当作鉴权完成。
- **单未知目标：**更强同名邻车不能并行预连接一批MAC。没有ACL时，真正已验证的MAC重新出现可以优先恢复，避免被误命中的同名首包拖住十二秒；正在握手仍不抢槽位。
- **预算不续期：**未知候选最多12秒，从普通选择或更早的首包实际尝试算；持续广播/补名不重置。失败后30秒冷却，快速与普通路径都不能绕过。
- **预算边界ACL：**十二秒尝试到期释放候选，但若它刚进入ACL握手，仍保留剩余五秒保护，不立即换另一同名车打断鉴权。
- **在途证据保留：**容量仍32个，保留唯一预连接目标近期真实广播用于后续连接交叉校验，不被其他设备的候选洪水挤掉。
- **生命周期清理：**服务关闭、停止、重学、更新提示或蓝牙关闭清临时唤醒门控；官方就绪结束注入，不改测距和车控锁存。

十二秒/三十秒只控制未验证连接尝试，**不是十五秒后停止扫描或已建立测距**。扫描仍LOW_LATENCY、reportDelay=0；不实现省电窗口、RSSI伪造、固定3.2秒猜鉴权、双路六次车控重发。

## 4. 上锁及已有安全保护不变

以下源码逐字/哈希与本轮备份一致：

- `PassiveKeyLogic.java`、`RssiFilter.java`、`VehicleIdentity.java`、`VehicleIdentityStore.java`、`VehicleScanPolicy.java`。
- `PendingControl.java`、`OfficialReadyGate.java`、`GeelyBridge.java`、`KeyNotificationBridgeService.java`。
- `MainActivity.java`、`ThresholdSettingsDialog.java`、`ThresholdSettingsForm.java`。

服务的RSSI处理/阈值/请求创建/实际发送/读取轮询方法未改；在官方就绪消费、待发派送、首次GATT挂载入口只增加了**首包会话未验证则等待**的守卫，原已学MAC/稳定上锁会话该标志为false，不改距离有效性或锁存。保持默认解锁-75、上锁-82、4.5秒驻留、至少3新样本、6秒近场保护、未补偿上锁smooth、500ms单在途RSSI读取、手动锁存、回程确认、行驶/插线保护、成功一次交付和未知结果停止重发。

昨天稳定上锁基线：20:36:06.401起远场驻留，10.935达到4535ms/10样本，10.950交付PI，使用GATT未补偿平滑距离。本轮不据此调阈值或驻留。

## 5. 分段日志

增加`CANDIDATE_FIRST_SEEN`，确保候选出现早于普通主目标确认。`TARGET_FIRST_SEEN`仍表示进入普通处理，不当作全部首包起点。

- `advertised_name/early_reason`：是不是当前包完整名、为什么可/不可预连接。
- `candidate_age_ms/sightings/named_sightings/named_span_ms/name_reset`：真实命名样本数与跨度；`NAME_GAP/NAME_CHANGED`解释重新观察。
- `WAKE_REQUEST phase=SEND_EARLY_NAME/SEND_SELECTED`：区分首包快路径与原普通目标。`since_first_ad_ms`从实际地址首候选采集时间起算，选择确认不覆盖。
- `WAKE_WAIT`：按原因区分ACL握手、单目标占用与冷却；同原因5秒心跳、不同目标同样限频。
- `WAKE_BUDGET_EXPIRED`：未验证候选预算结束。
- 心跳/导出`wake_target_id/wake_reason/early_reason/wake_attempts/wake_attempt_age_ms/wake_last_age_ms/wake_wait_verified_peer`，官方边沿`wake_session_id`。

只用脱敏指纹与关键事件。匿名首见最多每秒一条，其余累计抑制计数，不开启所有逐包原始字段落盘。字段细节见文档39。

## 6. 回归与构建

13组Java回归＋1组Shell命名回归全部通过，Android资源、Java、DEX、APK对齐/签名验证通过。新增`VehicleWakeGateTest`覆盖：

- 新MAC完整名/厂商状态变化的-78首包，在采集后13ms即可请求；不保存身份、不选RSSI目标。
- **合成稀疏名称**：首命名后匿名补包，3秒后命名证据重置，再跨626ms第二条命名，普通选择4026ms；预连接请求13ms。早期ACL继续保护，后续不重复注入。
- 匿名/缓存名/手填未学/缺厂商/不同Company/冲突全名/过期/重复/低于-95拒绝快路径。
- 已学地址首包无新增延迟，旧地址新鲜时重名邻车不抢；可信MAC恢复优先及在途证据32容量保留。
- 同MAC名称确认后节流不重置、先到ACL/无关ACL/旧地址迟到断连的保护正确。
- 单未知在途、12秒共用预算、30秒冷却、广播消失再出现不提前恢复，以及服务关闭/官方就绪/重置。
- 十二秒预算结束但ACL刚连接时仍保护剩余窗口；首包路径通知先到、物理连接未验证时不凭旧MAC车控，真实证据晚到仍补处理唯一就绪机会。
- 夹入强同名新MAC的稳定上锁GATT回放：每个原始RSSI、滤波值、状态机诊断和动作与无预连接的基线逐项相等。

原RSSI全链路回归输出与修改前完全一致：

| 采样间隔 | 解锁跟随 | 口袋解锁 | 上锁跟随 | 口袋上锁 |
| --- | ---: | ---: | ---: | ---: |
| 250ms | 0.75s | 0.50s | 7.00s | 7.00s |
| 500ms | 1.00s | 1.00s | 7.00s | 7.00s |
| 1000ms | 2.00s | 2.00s | 8.00s | 8.00s |

这是固定测试信号的回放耗时，**不是车锁实际响应时间或距离标定**。

构建（不安装）：

```bash
SKIP_INSTALL=1 JAVA_HOME=/Users/luca/Library/Java/JavaVirtualMachines/corretto-11.0.24/Contents/Home \
  bash /Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/build.sh
```

产物：`/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.1.4-min-114.apk`。

界面从实际安装包读取版本，安装后自动显示`v4.1.4-min（构建114）`；APK名称从Manifest生成，无需额外改UI字符串。继续沿用现有签名，证书SHA-256为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`，密钥未替换。

本轮文件备份与修改前/后测试结果：`/tmp/geely-preconnect-v414.7g8h6o2p/`。未执行Git命令。

## 7. 后续实车验收

**代码已减少“首次预连接必须等后续命名确认”的人为等待；未安装、未实车验证，不能保证一定省4秒或已经达到3–5米。**若系统不给本包名称/厂商数据，仍会走普通发现；官方已主动握手时优先等真实就绪而不是反复重连。

获准安装后保持现有配置/签名，不卸载、不清数据、不改上锁参数。在原场地分别做MAC未轮换、MAC轮换、多辆吉利相邻、口袋遮挡测试。测试者记录：

1. 到距车约5米/3米的时刻；
2. 车锁实际打开时刻、是否到门边还有等待；
3. 靠近路线/车前后位置/手机遮挡；
4. 上锁是否仍稳定；随后立即导出ZIP。

对照`首候选 → 首次实际注入 → ACL → 官方就绪 → PI交付`分段，重点看首包是否走`SEND_EARLY_NAME`、是否已有ACL而等待、官方是否提前就绪。目标是**3–5米门已开**，因此要给官方连接/鉴权留提前量，不是到3米才发起连接。

## 8. API依据

本轮通过Context7查询Android官方资料：

- `ScanResult.getTimestampNanos()`返回从本次开机起的广播观察时间；使用真实采集时间，不能改成Handler处理时刻。
- `SystemClock.elapsedRealtime()/elapsedRealtimeNanos()`为包含深度休眠的单调开机时间，用于年龄/预算。
- `ACTION_ACL_CONNECTED`仅表示远端低层ACL连接，带设备字段，Android12+读取需要`BLUETOOTH_CONNECT`；不携带应用鉴权结果，也不能据此确定调用者包名。

来源：

- https://developer.android.com/reference/android/bluetooth/le/ScanResult
- https://developer.android.com/reference/android/os/SystemClock
- https://developer.android.com/reference/android/bluetooth/BluetoothDevice#ACTION_ACL_CONNECTED
