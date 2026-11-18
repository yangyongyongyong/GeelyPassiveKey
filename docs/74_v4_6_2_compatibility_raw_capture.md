# 74. v4.6.2：车型兼容原始 BLE 抓取

版本：`4.6.2-min / versionCode 602`；该能力随 `4.6.3-min / versionCode 603` 正式发布。

## 1. 解决的问题

v4.6.1 及之前的“上报车型兼容问题”只上传已有诊断日志。未识别的 `Geely_Auto` 等广播通常在 `GeelyAdvertisement.from()` 返回 `null` 后只增加 `scan_not_geely`，原始名称、地址、AD 字节、厂商数据和 Service Data 都不会进入日志，无法据此判断兼容分支。

## 2. 用户操作

维护 → 本车识别 → **上报车型兼容问题**：

1. 必须填写车型与异常现象；
2. 点击上传后，前台服务暂停普通身份过滤扫描；
3. 以 `SCAN_MODE_LOW_LATENCY` 进行 10 秒无过滤 BLE 扫描；
4. 抓取结束后自动恢复原扫描；
5. ZIP 增加 `compatibility_capture.txt` 并上传；
6. 普通“上传排查日志给开发者”不触发原始抓取，行为保持不变。

抓取期间只观察广播，不连接任何新设备、不改本车身份、不执行自动解解锁。进入抓取时会取消尚未交付的自动车控请求，避免扫描切换期间误发。

## 3. 原始附件内容

`compatibility_capture.txt` 包含：

- 抓取开始/结束墙钟与单调时间、实际持续时长；
- Android、厂商、品牌、型号、时区和蓝牙适配器能力；
- 每个设备地址、系统设备名、广播设备名；
- 每条唯一广播的最新/最小/最大/首次 RSSI；
- `connectable`、Tx Power、Advertising SID、PHY、Data Status、设备类型、绑定状态和设备类别；
- `ScanRecord.getBytes()` 完整十六进制；
- 按 AD length/type 拆分的原始结构，包括 Flags、名称、UUID、Tx Power、Appearance、Service Data 和 Manufacturer Data；
- Company ID 小端解码与完整厂商 payload；
- ScanRecord Service UUID、Service Data 和 Manufacturer Data 映射；
- 当前 `BluetoothManager` 可见的 GATT/GATT_SERVER 连接；
- 当前本应用已挂载 GATT 的 service、characteristic、descriptor UUID、properties 与 permissions；
- 系统已绑定设备列表；
- 扫描失败 code、异常阶段和错误摘要。

文件保留原始蓝牙地址与设备名，这是兼容排查所需的刻意行为；普通日志正文仍使用既有本机指纹。

## 4. 容量和隐私边界

- 最多记录 256 个设备；
- 最多保留 1,500 种唯一广播结构；
- 文本超过 1,200,000 字符时在行边界截断并明确标记；
- 相同地址、相同 AD 字节和相同扫描元数据的重复广播只累计次数与 RSSI，不重复写文件；
- 附件不改变原有 D1 元数据表结构；兼容附件随 ZIP 一起受 90 天保留和上传大小限制；
- 上传仍只能由用户手动触发。

## 5. 验证

- `CompatibilityAdDecoderTest` 覆盖空口名称、`0x01FE` 厂商数据、16-bit Service Data、Flags 和损坏 AD 长度；
- `DiagnosticsTest` 验证兼容附件原样进入 ZIP、不会被二次脱敏，且禁止覆盖固定日志文件；
- 全部原有纯逻辑测试继续通过；
- `SKIP_INSTALL=1` 完成资源编译、Java 编译、DEX、对齐和签名；
- 本轮未推送、未创建 Release、未安装到手机。
