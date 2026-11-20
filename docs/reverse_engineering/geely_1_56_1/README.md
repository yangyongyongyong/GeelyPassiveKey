# 银河 1.56.1 脱壳资料索引

本目录存放 2026-11-20 对吉利银河官方 App `com.geelygalaxy.customer` 1.56.1 的运行时脱壳与关键反编译结果的轻量索引。不在仓库内提交约 112 MB 的原始内存区和约 115 MB 的 DEX 二进制，避免仓库膨胀。

## 源 APK

| 项目 | 值 |
| :--- | :--- |
| APK | `/tmp/geelygalaxy-1.56.1.apk` |
| 包名 | `com.geelygalaxy.customer` |
| versionName | `1.56.1` |
| versionCode | `156010010` |
| APK SHA-256 | `6b5019e29b8fcc903b16863c5d4084bc9ce5f088718c8286ebea09da0c06b8fa` |
| 签名证书 SHA-256 | `ff1b5765cd9570446f1ae6a06169c1f108f12ff77549542e21e1eda3f5ed2ab1` |
| 可执行文件 | `classes.dex` 为 KiwiVM/kiwic 加固壳，不能直接用 JADX 看到业务类 |

## 运行时提取

使用 Android 35 AOSP `default/arm64-v8a` userdebug 模拟器，官方 App 在启动后约 1 秒完成真实 DEX 加载。随后对官方进程执行 `SIGSTOP`，读取其 `maps` 中约 117,080,064 字节的 `/dev/zero` 解密区；该区域包含 14 个 `dex\n038` 头。根据 DEX header 的 `file_size` 精确切出 14 个 DEX。

关键点：壳内部会通过 CPU 时间限制/`SIGXCPU` 终止进程；必须先 `kill -STOP` 冻结，再读 `/proc/<pid>/mem`，不要在目标进程运行期间做大规模扫描。

## 14 个真实 DEX 的 SHA-256

| DEX | SHA-256 |
| :--- | :--- |
| `classes0.dex` | `83f3fd991ba7a1bf2fe878fb6beacf75b20502e396854c9cccdd436c62255b86` |
| `classes1.dex` | `a53217a4d0ee5379376b0454d1fef9bf9228d95e69d60e1503096d6c2610a0ce` |
| `classes2.dex` | `2e62e974c7ff54b20ac498c982ee6d89d4cf51e20cc996a4913f1f3738f02d0d` |
| `classes3.dex` | `6def8b08e0a6a004df0910332916c578f64d0e58378a2830f48dbc253902b59c` |
| `classes4.dex` | `207cda9131187bd720561223b33d70fc83ca5bd102c96f694c145b2dd84aa597` |
| `classes5.dex` | `38f14cca6e53183fba53374b893d43845b70f6478443a17bed9a32dc592da9d8` |
| `classes6.dex` | `cadb575374a82fbdbb8e7162876c3f558949d84b80f60b530d65d791ad92ba5b` |
| `classes7.dex` | `30958b8275b9f5bd67cb4db1dd8e524fc29c540181a8894ef02f429d2aa7ad92` |
| `classes8.dex` | `143c374ef12f51529939faf082d756b305bf7708dc6917837dbe5d2dbaf66a0b` |
| `classes9.dex` | `7d05a8e9e13b9e1d08955c452dcbb3b5c5c51004e6fca9ff1012f3cb31bd9693` |
| `classes10.dex` | `4ad21e7b897614d1e646be2384843ad6b46057568551cc7f87fedddee3473a31` |
| `classes11.dex` | `394411c7515bf19ebfaa6d0be9abba09bdf1956eaa05523308b458d4670158cf` |
| `classes12.dex` | `28d643fee9defb7472c2bb5024ab1171d26a8c78819e2dda6b6bb1e2554e59f7` |
| `classes13.dex` | `cf029c3a735e6d2f0919ec6d3efc262a3d7f22a63bda517eb077514f1e87ba65` |

## 目标类映射

| DEX | 关键类 |
| :--- | :--- |
| `classes1.dex` | `com.geely.snc.digitalkey.base.service.DigitalKeyBgService`、`DkRemoteView`、`DigitalKeyNotification` |
| `classes12.dex` | `com.ecarx.blekey.thirdadapter.service.MiuiService`、`com.geely.cockpit.services.controlservice.service.FunType` |
| `classes13.dex` | `com.geely.galaxy.car.weight.appwidget.WgProviderMiddle`、`WgBaseProvider`、`WgControlView`、`WgCarControl`、`WidgetEvent`、`WgConfig`、`WgConfigSettingActivity`、`CarControlBusiness` |
| `classes11.dex` | `com.alibaba.cloudapi.sdk` 网关签名 SDK：`SignUtil`、`ApiRequestMaker`、`HMacSHA256SignerFactory` |
| `classes12.dex` | `com.geely.cockpit.services.controlservice` 车况接口/模型、`com.geely.galaxy.uc` 会话、`com.geely.galaxy.network` 执行器 |

`RemoteViewsClickReceiver` 在 Manifest 中仍可见，但本次 14 个已加载 DEX 中没有找到其类体；这不改变它的 Manifest 导出状态结论，后续必须以运行时类加载结果为准。

## 关键反编译摘要

- [key_code.md](key_code.md)：关键方法摘录和语义结论
- [cloud_gateway_manifest.md](cloud_gateway_manifest.md)：云端车况网关、签名、会话和返回模型的白名单反编译清单
- [decompiled/cloud_gateway/](decompiled/cloud_gateway/)：本次新增的 53 个云端网关相关 JADX 节选及环境配置
- [dex-sha256.txt](dex-sha256.txt)：14 个 DEX 的 SHA-256
- [decompiled/](decompiled/)：本次保留的关键 JADX 输出；这些是分析材料，不是可直接编译的工程源码

完整分析、证据边界和优化路线见 [78](../../78_geely_1_56_1_unpack_and_control_channels.md)。

云端车况网关的接口、签名、root 边界和接入方案见 [80](../../80_cloud_vehicle_status_gateway.md)。
