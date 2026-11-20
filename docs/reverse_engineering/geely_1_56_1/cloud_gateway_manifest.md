# 银河 1.56.1 云端车况网关反编译清单

日期：2026-11-20

## 1. 来源

| 项目 | 值 |
| :--- | :--- |
| 官方 App | 吉利银河 `com.geelygalaxy.customer` |
| versionName | `1.56.1` |
| versionCode | `156010010` |
| APK SHA-256 | `6b5019e29b8fcc903b16863c5d4084bc9ce5f088718c8286ebea09da0c06b8fa` |
| 签名证书 SHA-256 | `ff1b5765cd9570446f1ae6a06169c1f108f12ff77549542e21e1eda3f5ed2ab1` |
| JADX | `1.5.7` |
| 脱壳方式 | Android 35 AOSP userdebug 模拟器，运行时提取 14 个真实 DEX |
| APK 本机路径 | `/tmp/geelygalaxy-1.56.1.apk` |
| DEX 本机路径 | `/tmp/geely-dex-out/classes0.dex` 至 `classes13.dex` |

原始 APK、DEX 和完整 JADX 树不进入 Git。

## 2. 相关 DEX

| DEX | SHA-256 | 本清单中的主要来源 |
| :--- | :--- | :--- |
| `classes1.dex` | `a53217a4d0ee5379376b0454d1fef9bf9228d95e69d60e1503096d6c2610a0ce` | `com.geely.rxhttp`、`com.geely.snc.login` |
| `classes11.dex` | `394411c7515bf19ebfaa6d0be9abba09bdf1956eaa05523308b458d4670158cf` | `com.alibaba.cloudapi.sdk` |
| `classes12.dex` | `28d643fee9defb7472c2bb5024ab1171d26a8c78819e2dda6b6bb1e2554e59f7` | USP 接口/模型、用户会话、网络执行器 |
| `classes13.dex` | `cf029c3a735e6d2f0919ec6d3efc262a3d7f22a63bda517eb077514f1e87ba65` | App 初始化、环境配置、native 方法声明 |

全部 14 个 DEX 的 SHA-256 见 [dex-sha256.txt](dex-sha256.txt)。

## 3. 收录范围

共收录：

- 53 个 Java 文件；
- 2 个官方 assets 环境配置；
- 约 1.3 MB。

目录：

```text
docs/reverse_engineering/geely_1_56_1/
├── cloud_gateway_manifest.md
└── decompiled/
    └── cloud_gateway/
        ├── README.md
        ├── SHA256SUMS.txt
        ├── assets/
        └── com/
```

### 3.1 Alibaba Cloud API SDK

来源：

```text
com/alibaba/cloudapi/sdk/
```

收录内容：

- `client/BaseApiClient.java`
- `client/HttpApiClient.java`
- `constant/HttpConstant.java`
- `constant/SdkConstant.java`
- `enums/HttpMethod.java`
- `enums/Scheme.java`
- `model/ApiHttpMessage.java`
- `model/ApiRequest.java`
- `model/HttpClientBuilderParams.java`
- `signature/HMacSHA1SignerFactory.java`
- `signature/HMacSHA256SignerFactory.java`
- `signature/ISignerFactory.java`
- `signature/ISinger.java`
- `signature/SignerFactoryManager.java`
- `util/ApiRequestMaker.java`
- `util/HttpCommonUtil.java`
- `util/SignUtil.java`

用途：

- 证明 `content-md5` 的生成方式；
- 证明 `date`、`x-ca-timestamp`、`x-ca-nonce`、`x-ca-key` 的生成；
- 证明 string-to-sign 的固定行顺序；
- 证明参与签名的 header 使用 `TreeMap` 字典序；
- 证明 HMAC-SHA256 + Base64 的最终输出。

### 3.2 Geely HTTP/Gateway 层

来源：

```text
com/geely/rxhttp/
```

收录内容：

- `ContentType.java`
- `GateWayOptions.java`
- `ISWHttpProxy.java`
- `SWAPIClientHost.java`
- `SWGatewayComponent.java`
- `SWHttpComponent.java`
- `base/Constants.java`
- `base/SWGateWayConfig.java`
- `impl/SimpleJsonRequest.java`
- `intereptor/AutoRefreshInterceptor.java`
- `intereptor/SessionInterceptor.java`

用途：

- `SWHttpComponent.initGateWay()` 明确写入：

  ```java
  new String[]{"token", "gl_user_id"}
  ```

- `SWAPIClientHost` 证明 `x-ca-appcode`、`token`、`x-requireToken` 和公共 header 的注入位置；
- `AutoRefreshInterceptor` 证明鉴权失败时会清空旧签名头并重新发起请求；
- `SimpleJsonRequest` 证明 `useGateway`、`requireToken`、header/body 的组织方式。

### 3.3 USP 车况接口

来源：

```text
com/geely/cockpit/services/controlservice/
```

收录内容：

- `bean/PlatformVehicleStatus.java`
- `bean/VehicleBatteryStatus.java`
- `bean/VehicleRunningStatus.java`
- `bean/VehicleTyreStatus.java`
- `bean/VehicleBasicStatus.java`
- `core/controlImpl/UspControlImpl.java`
- `core/response/BaseUspResult.java`
- `core/uspapi/UspApiConstantKt.java`
- `core/uspapi/UspApiService.java`
- `runtime/CurUserInfo.java`
- `runtime/CurUspBaseUrl.java`
- `runtime/CurVehicle.java`

关键证据：

```java
map.put("clientType", 2);
map.put("dataTypeList", CollectionsKt.mutableListOf(new String[]{"all"}));
map.put("statusType", "local");
map.put("vin", vin);
map.put("openUserId", CurUserInfo.getUserId());
```

`UspApiService.getPlatformVehicleStatus()`：

```java
new PostHttpConfigImpl.Builder()
    .setCustomUrl(getBASE_USP_URL_NEW() + getAPI_POST_CONTROL_STATUS())
    .setContentType(ContentType.Json)
    .useGateWay(true)
    .setAppCode(getUSP_GATEWAY_CODE_NEW())
```

并添加：

```java
appCode.addHeader("x-ca-stage", isNotProd() ? "PRE" : "RELEASE");
```

### 3.4 网络执行器

来源：

```text
com/geely/galaxy/network/
```

收录内容：

- `HttpExecutor.java`
- `impl/GetHttpConfigImpl.java`
- `impl/HttpExecutorImpl.java`
- `impl/PostHttpConfigImpl.java`

用途：

- 证明网关初始化和请求执行入口；
- 证明 POST JSON 请求的 URL、host、path、scheme 解析；
- 证明 `initGateWay(appKey, appSecret, appCode)` 最终转发到 `SWHttpComponent`。

### 3.5 登录与会话

来源：

```text
com/geely/snc/login/
com/geely/galaxy/uc/http/
com/geely/galaxy/uc/repository/
```

收录内容：

- `com/geely/snc/login/GLLoginServiceImpl.java`
- `com/geely/snc/login/GLLoginServiceImplKt.java`
- `com/geely/galaxy/uc/http/GalaxyUserServiceApi.java`
- `com/geely/galaxy/uc/http/SWUserSessionComponent.java`
- `com/geely/galaxy/uc/http/TokenProvider.java`
- `com/geely/galaxy/uc/repository/UserRepositoryImpl.java`

用途：

- 证明官方 token 由 `SWTokenContract` / `ITokenProtocol` 提供；
- 证明请求会动态读取 token，而不是把 token 写死在 APK；
- 证明 `UserRepositoryImpl.requestBuilder()` 添加客户端身份头：

  ```java
  .addHeader("appId", BuildConfig.APP_ID)
  .addHeader("tenantId", BuildConfig.TENANT_ID)
  .setAppCode(BuildConfig.APP_CODE)
  ```

本清单只记录字段名，**不复制 `BuildConfig` 中的真实值**。

### 3.6 App 初始化与 native 声明

来源：

```text
com/geely/galaxy/car/init/
com/geely/galaxy/car/utils/
```

收录内容：

- `com/geely/galaxy/car/init/ParseEnvConfigUtils.java`
- `com/geely/galaxy/car/init/SWCarModuleMainInitializer.java`
- `com/geely/galaxy/car/utils/LDPProtectUtils.java`

用途：

- 证明 `CarEnvConfNew.json` 的 `otaApiBaseUrl` 是网关基址；
- 证明 `initUspGateWay()` 读取：
  - `LDPProtectUtils.getUspGateway()`
  - `LDPProtectUtils.getUspGatewayNew()`
  - `LDPProtectUtils.getUspAppid()`
  - `LDPProtectUtils.getUspAppSecret()`
  - `LDPProtectUtils.getUspAppidNew()`
  - `LDPProtectUtils.getUspAppSecretNew()`
- 证明这些值是 native 方法返回值，真实值不在 Java 代码中。

### 3.7 assets

- `assets/CarEnvConf.json`
- `assets/CarEnvConfNew.json`

仅包含 dev/sit/uat/tra/release/production 基址。生产车况基址位于 `CarEnvConfNew.json`：

```text
https://iov-service-galaxy.geely.com/
```

## 4. 文件校验

收录文件 SHA-256 见：

[SHA256SUMS.txt](decompiled/cloud_gateway/SHA256SUMS.txt)

## 5. 明确未收录

- 完整 APK；
- 14 个原始 DEX；
- 约 147 MB 的完整 JADX 树；
- 官方 native `.so`；
- `com.geely.galaxy.usercenter.BuildConfig` 的 `APP_ID` / `TENANT_ID` / `APP_CODE` 真实值；
- `LDPProtectUtils` 的 native 返回值；
- 任何 token、`openUserId`、VIN、MMKV、SharedPreferences 或用户数据；
- 临时探针 APK、探针源码和提取脚本。

## 6. 使用限制

- 本目录是 JADX 反编译节选，不是官方源码；
- JADX 对 Kotlin 协程、内联类和泛型的还原存在误差；
- 类依赖不完整，不能直接编译；
- 代码仅用于兼容性研究、接口分析和安全边界确认；
- 任何云端接入都必须使用合法、可撤销、与车辆权限匹配的会话；
- 云端车况不得用于自动解锁/上锁决策。

完整说明见：

- [decompiled/cloud_gateway/README.md](decompiled/cloud_gateway/README.md)
- [docs/80_cloud_vehicle_status_gateway.md](../../80_cloud_vehicle_status_gateway.md)
