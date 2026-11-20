# 80. 官方云端车况网关可行性验证（只读）

日期：2026-11-20
研究对象：吉利银河官方 Android App `com.geelygalaxy.customer` 1.56.1
APK SHA-256：`6b5019e29b8fcc903b16863c5d4084bc9ce5f088718c8286ebea09da0c06b8fa`

## 1. 结论

官方云端车况接口真实存在，阿里云 API Gateway 签名链路也已独立复现；但当前不能把“云端车况”直接做成普通 APK 内无需用户授权的功能。

已完成验证：

- 可以定位车况请求的接口、请求体、客户端身份头和签名算法；
- 可以用占位会话构造出通过网关签名校验的请求；
- 缺少或使用无效会话时，服务端明确返回 `user-login-invalid-expired`；
- `PlatformVehicleStatus` 模型包含电量、电压、车速、里程和四轮胎压等字段。

当前唯一关键阻塞项是**有效官方账号会话**：

- `token`；
- `openUserId`；
- 完整 `vin`。

这三项属于账号/车辆私有数据，不能从公开接口获得，也不应硬编码、上传或写入仓库。

## 2. App 内完整调用链

反编译证据：

1. `SWCarModuleMainInitializer.initUspGateWay()` 从 `LDPProtectUtils` 的 native 方法读取网关代码、appKey 和 appSecret，并初始化网关；
2. `ParseEnvConfigUtils` 从 `CarEnvConfNew.json` 读取 `iov-service-galaxy.geely.com` 生产基址；
3. `UspControlImpl.getVehicleStatusGDPR$suspendImpl()` 组装车况请求；
4. `UspApiService.getPlatformVehicleStatus()` 使用 `PostHttpConfigImpl`，开启 `useGateWay(true)` 并添加 `x-ca-stage`；
5. `SWAPIClientHost` 注入 `x-ca-appcode`、`token` 以及 token interpolator 提供的公共头；
6. `SignUtil`、`ApiRequestMaker`、`HMacSHA256SignerFactory` 完成 string-to-sign、HMAC-SHA256 和 Base64；
7. `SWGatewayComponent` 通过阿里云 API SDK 发出请求；
8. `BaseUspResult<PlatformVehicleStatus>` 接住响应并映射为车况对象。

### 2.1 接口

```text
POST https://iov-service-galaxy.geely.com/vc/app/v1/vehicle/control/status
```

路径来源：

- 生产基址：`CarEnvConfNew.json` 的 `production.otaApiBaseUrl`；
- App 类型前缀：`RuntimeUtilsKt.isGeelyApp()` 为真时是 `api`，否则是 `vc/app`；
- 接口后缀：`API_POST_CONTROL_STATUS = /v1/vehicle/control/status`。

本仓库当前研究的是银河 App 的 `vc/app` 路径。吉利品牌 App 可能走 `api` 前缀。

### 2.2 请求体

来自 `UspControlImpl.getVehicleStatusGDPR$suspendImpl()`：

```json
{
  "clientType": 2,
  "dataTypeList": ["all"],
  "statusType": "local",
  "vin": "<完整VIN>",
  "openUserId": "<官方平台用户ID>"
}
```

字段来源：

- `vin`：`CurVehicle.getVin()`；
- `openUserId`：`CurUserInfo.getUserId()`；
- 其余三项为固定值。

## 3. 网关请求头

实测请求需要以下头：

| Header | 来源/作用 | 是否参与签名 |
| :--- | :--- | :--- |
| `accept` | SDK 根据请求方法生成 | 固定位置 |
| `content-md5` | `Base64(MD5(body))`，官方 SDK 截取前 24 个字符 | 固定位置 |
| `content-type` | JSON 请求 | 固定位置 |
| `date` | RFC 1123 GMT 时间 | 固定位置 |
| `host` | 请求域名 | 否 |
| `user-agent` | SDK 固定值 | 否 |
| `x-ca-appcode` | 网关 appCode | 是，`x-ca-*` |
| `x-ca-key` | 网关 appKey | 是，`x-ca-*` |
| `x-ca-nonce` | UUID | 是，`x-ca-*` |
| `x-ca-stage` | 生产环境为 `RELEASE` | 是，`x-ca-*` |
| `x-ca-timestamp` | 毫秒时间戳 | 是，`x-ca-*` |
| `x-ca-version` | 官方 SDK 写 `1` | 是，`x-ca-*` |
| `x-ca-signature-method` | `HmacSHA256` | 是，`x-ca-*` |
| `x-ca-signature-headers` | 参与签名的 header 名列表 | 由 SDK最后生成，不列入自身 |
| `x-ca-signature` | 最终签名 | 否 |
| `token` | 官方账号会话 | 是，extra header |
| `gl_user_id` | 官方用户标识头 | 是，extra header |
| `appId` | 官方客户端身份头，来自 `BuildConfig.APP_ID` | 否 |
| `tenantId` | 官方租户身份头，来自 `BuildConfig.TENANT_ID` | 否 |

补充边界：`UspApiService.getPlatformVehicleStatus()` 的片段本身没有显式写 `appId` / `tenantId`，它们由官方公共 header/token interpolator 链路补齐；但本次实测表明，缺少它们时网关会直接返回 `B00001`。`UserRepositoryImpl.requestBuilder()` 是这两个字段名及其 `BuildConfig` 来源的直接静态证据，不能只依赖车况方法的局部代码判断。

`SWHttpComponent.initGateWay()` 明确把 extra signed headers 配置为：

```java
new String[]{"token", "gl_user_id"}
```

`UserRepositoryImpl.requestBuilder()` 明确为官方用户中心请求添加：

```java
addHeader("appId", BuildConfig.APP_ID)
addHeader("tenantId", BuildConfig.TENANT_ID)
setAppCode(BuildConfig.APP_CODE)
```

本次只记录字段名和来源，不记录这些客户端标识的真实值。

## 4. 签名复现

### 4.1 string-to-sign

SDK 的构造顺序为：

```text
HTTP_METHOD\n
accept\n
content-md5\n
content-type\n
date\n
<按字典序排列的 header:value\n>
path[?query]
```

参与 header 段的内容包括：

- 所有以 `x-ca-` 开头的 header；
- `token`；
- `gl_user_id`。

必须按 header 名做字典序排序。此前未排序时，网关返回：

```text
X-Ca-Error-Code: A403IS
Invalid Signature
```

改用 `TreeMap` 排序后签名通过。

### 4.2 HMAC

- 算法：HMAC-SHA256；
- 结果编码：Base64；
- `x-ca-signature` 写入最终结果。

### 4.3 content-md5

官方 `ApiRequestMaker` 的实现为：

```text
Base64(MD5(requestBody))
```

并只保留 Base64 文本的前 24 个字符。这个问题如果不按官方 SDK 处理，签名和 content-md5 都会对不上。

## 5. 实测响应

测试仅使用无效占位会话和占位 VIN，没有读取真实车辆，也没有发送任何车控指令。

### 5.1 缺少客户端身份头

```text
HTTP 200
{"code":"B00001","msg":"请求头header缺失appId","data":null,"success":false}
```

说明网关入口存在，但还需要官方客户端身份头。

### 5.2 身份头齐全、会话无效

```text
HTTP 200
{"code":"user-login-invalid-expired","msg":"","data":null,"traceId":"0","success":false}
```

这说明：

1. 接口可达；
2. `appId` / `tenantId` / `x-ca-appcode` 身份链路可被网关识别；
3. HMAC 签名已通过；
4. 服务端已进入账号会话校验阶段；
5. 只差有效官方会话和对应车辆权限。

该结果不等于“拿到任意 token 就能控制车辆”。token 仍必须与 `openUserId`、VIN、账号权限和服务端有效期匹配。

## 6. 返回模型中的目标字段

`PlatformVehicleStatus` 可包含：

### 6.1 电池/充电

- `vehicleBatteryStatus.stateOfCharge`
- `vehicleBatteryStatus.chargeLevel`
- `vehicleBatteryStatus.voltage`
- `vehicleBatteryStatus.isCharging`
- `vehicleBatteryStatus.isPluggedIn`
- `vehicleBatteryStatus.timeToFullyCharged`

### 6.2 行驶

- `vehicleRunningStatus.speed`
- `vehicleRunningStatus.odometer`
- `vehicleRunningStatus.tripMeter1`
- `vehicleRunningStatus.tripMeter2`

### 6.3 轮胎

- `vehicleTyreStatus.tyreStatusDriver`
- `vehicleTyreStatus.tyreStatusPassenger`
- `vehicleTyreStatus.tyreStatusDriverRear`
- `vehicleTyreStatus.tyreStatusPassengerRear`
- 四轮温度、预警和温度告警字段

### 6.4 基础车况

- `vehicleBasicStatus.odometer`
- `vehicleBasicStatus.distanceToEmpty`
- `vehicleBasicStatus.chargeLevel`
- `vehicleBasicStatus.chargeLevelPct`

注意：`voltage` 的真实语义、单位、是否为小电瓶电压，必须以真实响应 payload 为准。在拿到授权账号下的实际返回前，不能把它直接标成“12V 小电瓶电压”。

## 7. root 方案分析

### 7.1 当前设备实测

- 型号：`V2426A`；
- `/system/bin/sh: su: inaccessible or not found`；
- `adb root` 被 production build 拒绝；
- 当前手机未 root，因此不能做 root 链路实测。

### 7.2 root 后能做什么

root 主要解决“读取官方 App 私有本地数据”的问题，理论链路为：

1. root 读取 `/data/user/0/com.geelygalaxy.customer/` 下的 MMKV/SharedPreferences/数据库；
2. 定位官方 token、`openUserId`、VIN 或车辆列表缓存；
3. 如果 token 是加密存储，需要在官方 App 进程内借助 Frida/动态插桩调用：
   - `SWUserSessionComponent` 或 `TokenProvider` 的 token getter；
   - `CurUserInfo.getUserId()`；
   - `CurVehicle.getVin()`；
4. 用本仓库已复现的网关签名请求云端只读状态。

### 7.3 root 不能做什么

root 不是绕过服务端登录的开关：

- token 过期仍然失效；
- 换账号、解绑车辆或权限变更会使旧 token 失效；
- root 无法凭空生成 `openUserId` 与 VIN 的合法绑定关系；
- 云端仍会校验账号、车辆权限、租户和风控；
- 不能把官方 native `.so`、appSecret、token 或 VIN 打进普通 Release。

因此 root 只能定位为**研究/高级诊断手段**，不能成为普通用户功能的依赖。公开版本不应要求 root，也不应读取官方 App 私有数据。

## 8. 可行接入方案

### 方案 A：官方 OAuth/二维码授权（首选评估）

如果官方 App 存在合法的第三方 OAuth/扫码授权入口，用户在官方 App 内主动确认授权，本项目仅获得短期、可撤销的只读 scope。

优点：

- 不读取官方私有文件；
- 不需要 root/无障碍；
- 会话生命周期和撤销由官方控制。

前置条件：必须确认官方是否开放 third-party client、scope、回调协议和车辆只读权限。

### 方案 B：用户手动导入短期会话（仅开发者/高级功能）

用户手动提供短期 `token`、`openUserId` 和 VIN，仅用于本机只读展示：

- 不持久化明文；
- 不自动上传；
- 不进入日志；
- 不参与自动解锁/上锁；
- 过期后由用户重新导入。

该方案不适合普通用户，也不能打包官方密钥。

### 方案 C：官方小组件过渡展示

现有官方小组件可提供部分电量、续航、里程和锁态信息，但不能提供完整四轮胎压和小电瓶电压。它可继续作为过渡展示，不能冒充完整云端车况。

## 9. 安全边界

明确不做：

- 不把官方 `appSecret`、`getUspAppSecret*()` 返回值、token、完整 VIN 或用户 ID 写入仓库、日志、Release 或诊断上传；
- 不把官方 native 库打进普通 Release；
- 不通过无障碍、截图或自动点击官方 App 窃取会话；
- 不把云端状态用于自动解锁/上锁决策。云端数据可能延迟、缓存或字段缺失，不能替代现有 BLE/通知桥接链路；
- 不在未确认车辆权限和会话有效性的情况下发送车控请求。

云端车况即使后续落地，也应是**只读展示**，不能成为无感钥匙安全联锁的一部分。

## 10. 反编译证据

轻量白名单已保存到仓库：

- [反编译清单](reverse_engineering/geely_1_56_1/cloud_gateway_manifest.md)
- [反编译目录说明](reverse_engineering/geely_1_56_1/decompiled/cloud_gateway/README.md)

重点文件：

- `com/alibaba/cloudapi/sdk/util/ApiRequestMaker.java`
- `com/alibaba/cloudapi/sdk/util/SignUtil.java`
- `com/geely/rxhttp/SWHttpComponent.java`
- `com/geely/rxhttp/SWAPIClientHost.java`
- `com/geely/cockpit/services/controlservice/core/controlImpl/UspControlImpl.java`
- `com/geely/cockpit/services/controlservice/core/uspapi/UspApiService.java`
- `com/geely/galaxy/uc/repository/UserRepositoryImpl.java`
- `com/geely/galaxy/car/init/SWCarModuleMainInitializer.java`

完整 APK、14 个 DEX、约 147 MB JADX 树和官方 native 库不进入 Git。

## 11. 下一步建议

优先评估方案 A。只有在官方授权入口可用后，才实现：

1. `CloudVehicleStatus` 纯解析模型；
2. 只读状态卡片；
3. 会话过期和字段缺失状态；
4. 云端状态仅展示、不参与自动车控；
5. 日志脱敏和会话不落盘测试。
