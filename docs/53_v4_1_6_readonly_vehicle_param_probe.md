# 53. v4.1.6：车机参数只读探针与自动记录

日期：2026-11-15，Asia/Shanghai。版本：`4.1.6-min / versionCode 116`。

**范围：只读展示和记录，不提供任何车机写入入口。**本轮保留v4.1.5的稳定VIN掩码、首包预连接、解锁/上锁状态机、RSSI滤波和通知车控；新增打开App自动绑定银河官方AIDL、读取车机参数并持久化。未开放PE开关写入、`0x0170`距离档位写入或`0x0195`标定数据写入。

### 当前银河1.56.1运行验证（2026-11-15）

在手机上安装v4.1.7后实测，当前银河`1.56.1`的Manifest明确包含：

```text
com.geely.snc.digitalkey.base.service.DigitalKeyBgService
android:exported="false"
```

普通第三方App绑定会被系统抛出`SecurityException`，因此**当前版本无法通过该AIDL读取PE/车况/标定参数**。v4.1.7已改为绑定前检查`ServiceInfo.exported`，检测到未导出时显示“银河1.56.1未导出DigitalKeyBgService（只读AIDL被系统限制）”，不再反复尝试。历史v3.20.6在旧版银河上有效，不代表当前版本仍可用。

## 1. REA补充结论：哪些值能读，哪些不能假装能读

历史`v3.20.6`的`GeelyAidlBridge`在绑定`DigitalKeyBgService`后自动发送：

```text
feat_dk_configuration / 5392 / 2      # 0x0501 原生PE状态查询
feat_dk_fast_ctrl / 272 / 0           # 0x0121 车况只读查询
```

数据回调接口为：

```java
IDkBleDataListener.onResult(int errorCode, int platform, int action, byte[] payload)
```

`action=5392`回执至少4字节：`byte[0]`请求类型、`byte[1]`真实PE状态（0关闭/1开启）、`byte[2..3]`结果码。该值表示**车机原生无感PE总开关**，不是距离或RSSI阈值。

本轮对历史银河模块的REA只读检查还确认了三个边界：

1. `DkBleConfigImpl.doAction`在DK1.5上明确处理`5392`；`5393/5394/5395`只在DK2执行。
2. `DkBleCalibrationImpl.doAction`当前只分发`400/402/405/406/409`，**没有看到305的读取分发**。因此`0x0131`读取请求在银河L6/DK1.5上可能没有回执，不能把“已发送”当成“已读到阈值”。
3. `DkCalibrationInfo`包含calibrationMode及small/big/self/ext系数串、hash和refresh标志，不是单一`-75dBm`之类的阈值字段。距离/RSSI门限可能位于车机内部标定模型，不能从现有AIDL回执直接反推一个数值。

`0x0170`距离挡位在银河L6/DK1.5上仍不支持；早期1~5挡UI是假设挡位，不能恢复成写入开关。

## 2. 本版具体实现

新增`VehicleParameterProbe`，App打开并`onResume`时自动绑定官方`DigitalKeyBgService`，绑定成功后延迟350ms发送三类**只读**请求：

| Feature | Action | Value | 用途 |
| --- | ---: | ---: | --- |
| `feat_dk_configuration` | `5392` (`0x1510`) | `2` | 查询`0x0501`原生PE状态 |
| `feat_dk_fast_ctrl` | `272` (`0x0110`) | `0` | 查询`0x0121`车况帧 |
| `feat_calibration` | `305` (`0x0131`) | `0` | 请求标定参数读取；L6/DK1.5可能无回执 |

同时注册只读回调：

- `5392`：PE状态与原始回执；
- `306` (`0x0132`)：标定原始回执；
- `289` (`0x0121`)：车况原始帧；
- `345` (`0x0159`)：车机原生靠近闭锁事件原始帧（仅记录，不据此放行车控）。

结果保存到私有SharedPreferences `vehicle_param_readonly_prefs`，并在诊断日志写入`READ_REQUEST/PE_SWITCH_RESULT/CALIBRATION_RESULT/VEHICLE_STATUS_RESULT/READ_TIMEOUT`。原始帧最多展示64字节摘要，不保存完整VIN、MAC或钥匙凭证。

## 3. 只读安全门

发送方法不是“调用方自觉只读”，而是统一经过`VehicleParamCodec.isReadOnlyRequest(feature, action, value)`：

允许：

```text
feat_dk_configuration / 5392 / 2
feat_dk_fast_ctrl / 272 / 0
feat_calibration / 305 / 0
```

明确拒绝：

```text
5392 / 0 或 1       # PE关闭/开启
405 / 任意值        # 0x0195标定数据发送
368 / 任意值        # 0x0170距离档位
其他未登记action
```

代码中没有调用`doFeatureActionBytesData`，没有写`Parcel`字节帧路径，也没有在界面提供修改开关。查询超时只记录“无回执”，不重试、不猜测、不切换成写请求。

## 4. 界面

主页新增“车机参数（只读）”卡片，显示：

- AIDL绑定状态、SDK版本、官方BLE状态码；
- `0x0501` PE开关真实状态和原始回执；
- `0x0131→0x0132`标定读取状态与原始回执；
- `0x0121`车况帧状态与原始摘要；
- 查询时间、最后回执时间、最近错误；
- “写入能力：未启用（本页只读）”。

如果车机不返回`0x0132`，界面明确显示“未收到0x0132回执（L6/DK1.5可能不支持0x0131读取）”，不会把PE开关或推测值冒充距离阈值。

## 5. 测试与构建

新增`VehicleParamCodecTest`，覆盖：

- `0x0501`开启、关闭和非零结果码；
- 空回执、单字节回执拒绝；
- 只读白名单：PE查询`5392=2`放行，`5392=0/1`、`405`、`368`拒绝；
- 原始帧十六进制摘要长度上限。

完整构建结果：14组Java测试＋1组APK命名回归通过；资源、Java、DEX、zipalign、签名验证通过。

产物：

```text
/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.1.6-min-116.apk
SHA-256: afaa73c979040e11ef082190e77f53f7ac181bde5c483e1de77b2f7651ea8ffb
```

签名证书仍为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`。

## 6. 安装与下一步实车观察

本版应覆盖安装后打开App，等待AIDL绑定完成，再查看“车机参数（只读）”卡片。重点记录：

1. `0x0501`是否返回真实开关状态；
2. `0x0132`是否有原始回执；
3. 若没有回执，说明L6/DK1.5当前AIDL未暴露阈值读取，不能据此尝试盲写；
4. 后续即使要做写入实验，也必须另建显式二次确认、原值备份、恢复入口和回滚验证，不能复用本版只读白名单。

本版不改变任何车机设置，也不把通知栏按钮或AIDL查询当成修改成功。
