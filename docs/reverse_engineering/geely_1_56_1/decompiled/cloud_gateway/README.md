# 银河 1.56.1 云端车况网关反编译节选

本目录只保留 2026-11-20 对吉利银河 1.56.1 云端车况、网关签名、官方会话和返回模型有直接证据价值的 JADX 输出。

这些文件是**分析材料**，不是可独立编译工程。它们依赖官方 App 中大量未收录的类、Kotlin 运行时、混淆包和 native 库。

## 证据链

### 1. 请求如何构造

- `com/geely/cockpit/services/controlservice/core/controlImpl/UspControlImpl.java`
  - `getVehicleStatusGDPR$suspendImpl()`
  - 组装 `clientType`、`dataTypeList`、`statusType`、`vin`、`openUserId`
- `com/geely/cockpit/services/controlservice/core/uspapi/UspApiService.java`
  - `getPlatformVehicleStatus()`
  - 开启 `useGateWay(true)`，添加 `x-ca-stage`
- `com/geely/cockpit/services/controlservice/core/uspapi/UspApiConstantKt.java`
  - `/v1/vehicle/control/status` 路径常量

### 2. 环境与密钥入口

- `com/geely/galaxy/car/init/SWCarModuleMainInitializer.java`
  - `initUspGateWay()`
- `com/geely/galaxy/car/init/ParseEnvConfigUtils.java`
  - 读取 `CarEnvConf.json` 与 `CarEnvConfNew.json`
- `com/geely/galaxy/car/utils/LDPProtectUtils.java`
  - 只有 `getUspAppid*`、`getUspAppSecret*`、`getUspGateway*` 等 native 方法声明

### 3. 网关签名与 header

- `com/alibaba/cloudapi/sdk/util/ApiRequestMaker.java`
  - `date`、`x-ca-timestamp`、`x-ca-nonce`、`content-md5`、`x-ca-key` 等
- `com/alibaba/cloudapi/sdk/util/SignUtil.java`
  - string-to-sign、`TreeMap` 字典序、`x-ca-signature-headers`
- `com/alibaba/cloudapi/sdk/signature/HMacSHA256SignerFactory.java`
- `com/geely/rxhttp/SWHttpComponent.java`
  - extra signed headers：`token`、`gl_user_id`
- `com/geely/rxhttp/SWAPIClientHost.java`
  - `x-ca-appcode`、token 注入、API SDK 请求组装

### 4. 官方账号会话

- `com/geely/galaxy/uc/repository/UserRepositoryImpl.java`
  - `requestBuilder()` 添加 `appId`、`tenantId`、`appCode`
- `com/geely/galaxy/uc/http/TokenProvider.java`
- `com/geely/galaxy/uc/http/SWUserSessionComponent.java`
- `com/geely/galaxy/uc/http/GalaxyUserServiceApi.java`
- `com/geely/rxhttp/intereptor/SessionInterceptor.java`
- `com/geely/rxhttp/intereptor/AutoRefreshInterceptor.java`

### 5. 车况返回模型

- `com/geely/cockpit/services/controlservice/bean/PlatformVehicleStatus.java`
- `com/geely/cockpit/services/controlservice/bean/VehicleBatteryStatus.java`
- `com/geely/cockpit/services/controlservice/bean/VehicleRunningStatus.java`
- `com/geely/cockpit/services/controlservice/bean/VehicleTyreStatus.java`
- `com/geely/cockpit/services/controlservice/bean/VehicleBasicStatus.java`

### 6. 环境配置

- `assets/CarEnvConf.json`
- `assets/CarEnvConfNew.json`

仅包含官方 App 的测试、UAT、生产基址，不包含账号 token 或车辆标识。

## 目录结构

```text
cloud_gateway/
├── README.md
├── SHA256SUMS.txt
├── assets/
│   ├── CarEnvConf.json
│   └── CarEnvConfNew.json
└── com/
    ├── alibaba/cloudapi/sdk/...
    └── geely/...
```

## 已省略内容

出于安全和仓库体积考虑，以下内容没有收录：

- 官方 APK；
- 14 个原始 DEX；
- 约 147 MB 的完整 JADX 输出；
- 官方 native `.so`；
- `com.geely.galaxy.usercenter.BuildConfig` 中 `APP_ID`、`TENANT_ID`、`APP_CODE` 的真实值；
- `LDPProtectUtils` native 返回的 appKey/appSecret；
- 任何账号 token、`openUserId`、VIN、MMKV 或 SharedPreferences 数据。

因此，不能用本目录自行还原出官方密钥或账号会话。

## 结论

本目录足以复现：

- 车况接口路径；
- 请求体结构；
- header 签名规则；
- HMAC-SHA256 流程；
- 官方会话注入位置；
- 返回模型字段。

但缺少合法官方会话时，服务端会返回 `user-login-invalid-expired`。root 只能辅助读取官方 App 私有会话，不能绕过服务端账号和车辆权限校验。

完整分析见 [docs/80_cloud_vehicle_status_gateway.md](../../../../80_cloud_vehicle_status_gateway.md)。
