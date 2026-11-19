# 76. v4.6.4：星愿真实 GATT 名 GeelyAuto

版本：`4.6.4-min / versionCode 604`。

## 1. 用户日志证据

日志 ID：`beaecae0-0e3d-4771-9c15-db1a615e006d`。

普通诊断包记录同一设备 UID 在 13 分钟前另一次兼容上传：

```text
dea9c31f-fbef-4263-8ca3-866a2dbfbfc1
```

该兼容包中的原始 GATT 快照显示：

```text
GATT count=2
GATT device addr=C2:A0:18:AF:24:3B name="OPPO Band 243B"
GATT device addr=7E:AE:C8:5F:AB:1A name="GeelyAuto"
```

同一时刻 App 状态仍为：

```text
identity_id=none
learning_complete=false
official_connected=true
gatt_attempt=0
```

手动解锁日志为：

```text
MANUAL_REQUEST action=UNLOCK
MANUAL_RESULT result=SENT transport=PI
```

说明银河通知桥接正常，故障只在自动本车身份学习。

## 2. 根因

v4.6.3 只接受：

```text
Geely_Auto
```

实际系统 GATT 名称是：

```text
GeelyAuto
```

因此 `findIdentityFromConnectedGatt()` 虽然看到了唯一星愿连接，仍被名称门控拒绝，无法保存地址，也不会进入 RSSI 和靠近解锁状态机。

兼容抓取期间车辆已被银河连接，车机停止广播，10 秒扫描只看到 6 个其他 BLE 设备，没有星愿广播；但 GATT 快照已经提供足够的真实连接证据。

## 3. 修复

- `isGeelyAutoName()` 同时接受 `Geely_Auto` 与 `GeelyAuto`。
- 首次学习硬件过滤同时放行两种名称。
- `fromConnectedPeer()` 允许官方唯一 GATT 中的 `GeelyAuto` 直接生成本车身份。
- 已学习的 `GeelyAuto` 地址轮换后，新的真实 GATT 仍可更新地址。
- 两种名称仍只属于弱型号线索，不能仅凭同名广播直接供 RSSI 或车控。

## 4. 验证

- `VehicleIdentityTest` 覆盖 `GeelyAuto` GATT 直接学习和地址轮换。
- `VehicleScanPolicyTest` 验证两种名称都进入首次学习过滤。
- 原有星愿 `Geely_Auto`、多车隔离、RSSI 和车控测试继续运行。
