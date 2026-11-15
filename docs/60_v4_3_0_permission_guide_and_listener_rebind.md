# 60. v4.3.0：授权与运行全检、通知监听重绑与签名防误换

日期：2026-11-15，Asia/Shanghai。版本：`4.3.0-min / versionCode 300`。

本轮处理两个问题：

1. 覆盖安装后，手机设置里仍显示通知使用权已允许，但 App 报“系统未绑定”；
2. 原权限卡只是多个同色按钮的堆叠，无法说明每项权限的作用、系统路径和真实状态。

## 1. 先排除“证书变化”

读取手机已安装 APK 后确认，当前安装包签名证书 SHA-256 仍是：

```text
1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff
```

该证书与 v4.0.9 之后文档记录一致，因此本次现象**不是证书变化导致的通知使用权丢失**。

覆盖安装/强行停止后的系统实态：

```text
settings secure enabled_notification_listeners:
  com.luca.geelykeyradar/...KeyNotificationBridgeService

dumpsys notification:
  Allowed notification listeners:
    com.luca.geelykeyradar/...KeyNotificationBridgeService
  All notification listeners enabled for current profiles:
    com.luca.geelykeyradar/...KeyNotificationBridgeService
  Live notification listeners:
    <不含本应用>
```

结论：vivo OriginOS 保留了“已允许”的授权记录，但安装/强停后没有立即把 `NotificationListenerService` 绑定到新进程。用户在系统页关闭再开启，会强制系统重新建链，所以看起来“重新授权后正常”。

真机日志中，应用持续调用 `NotificationListenerService.requestRebind()` 后约 8.5 秒恢复：

```text
13:59:30.387 LISTENER_DOWN
13:59:32.412 LISTENER_REBIND_REQUEST down_ms=2026
13:59:34.482 LISTENER_REBIND_REQUEST down_ms=4096
13:59:36.544 LISTENER_REBIND_REQUEST down_ms=6158
13:59:38.608 LISTENER_REBIND_REQUEST down_ms=8221
13:59:38.956 LISTENER_CONNECTED
```

因此修复目标不是“重新申请一个并不存在的权限”，而是：

- 明确区分“未授权”和“授权仍在、监听未绑定”；
- 覆盖安装后立即主动请求重绑；
- 在 10 秒自动重绑窗口内不要把那句话写成“权限被解绑”；
- 10 秒后仍失败时，引导用户到准确组件页关闭再开启一次；
- 常驻通知、主页和全检页使用同一套状态语义。

## 2. 通知监听的新状态机

`PermissionAudit` 将通知监听状态拆成：

```text
LISTENER:
  未授权                   → 待处理，去开通知使用权
  已授权 + 监听在线         → 已就绪
  已授权 + 掉绑 < 10秒      → 正在重连（授权仍在）
  已授权 + 掉绑 ≥ 10秒      → 待处理，打开系统页修复
```

`MY_PACKAGE_REPLACED` 广播、`MainActivity.onResume()`、全检页 `onResume()` 都会立即触发一次重绑请求。常驻通知在观察窗口内改为“通知桥接正在恢复 · 授权仍然保留”，超过窗口才提示“通知桥接未绑定 · 点此打开App修复”。

通知/车控手动测试的阻塞原因也同步改为：

```text
系统授权仍在，通知桥接正在自动重绑
```

不再把“系统未绑定”简写成“权限被解绑”。

## 3. 授权引导页重构

新增 `PermissionGuideActivity`，结构参考 dsh-go 的系统权限引导：

- 一次只展示一项；
- 顶部 5 段进度条和 `n / 5`；
- 每项包含用途、系统路径、真实状态、说明卡片；
- 主按钮执行该系统步骤，次按钮查看修复步骤或稍后处理；
- 从系统页返回后自动复检；
- 状态从“未完成”变为“已就绪”时显示 750ms 对勾并进入下一项；
- 最后进入汇总页，按“必要权限与运行 / 保活建议”分组；
- 未完成行可点击继续处理，不会把厂商开关伪装成可自动读取。

五项引导按顺序为：

1. 蓝牙扫描与连接；
2. 系统通知权限；
3. 通知使用权；
4. 电池白名单；
5. 厂商后台与自启动。

全检另外检查：

- 手机蓝牙是否开启；
- 银河 App 是否安装；
- 无感服务总开关与服务进程是否运行；
- 通知监听是否真正在线。

“厂商后台与自启动”没有公开统一读取 API，因此只会显示“无法自动读取 · 需手动确认”；用户点击“我已设置”后才记录为已确认。

## 4. 主页调整

主页原先“权限与保活”卡片只有一组同色按钮。现在改为顶部“一键全检”摘要：

- 显示必要项和保活建议的完成数；
- 最多列出三项待处理内容；
- 通知监听掉绑时明确显示“授权仍在，系统正在重连”；
- 主按钮直接打开授权引导页。

底层扫描、GATT、RSSI、车控队列和阈值状态机未改变。

## 5. 构建脚本：禁止静默生成新签名

旧脚本在 `build/debug.keystore` 缺失时会自动生成新密钥，存在下一次构建悄悄换签名、导致覆盖安装失败或特殊权限失配的风险。

现在：

- 缺少签名密钥直接失败；
- 必须通过 `ANDROID_KEYSTORE` 指向原密钥；
- 仅显式设置 `ALLOW_NEW_DEBUG_KEYSTORE=1` 才允许创建新密钥；
- 构建后打印证书 SHA-256；
- 真机安装前读取已安装 APK 的签名并比较；
- 不一致时拒绝自动安装，不卸载、不清数据；
- 可用 `ADB_SERIAL` 指定无线调试设备；
- 可用 `ALLOW_SIGNATURE_CHANGE=1` 显式覆盖，但脚本仍不会自动卸载。

## 6. 验证

- 17 组纯 Java 回归通过，新增 `PermissionAuditTest` 覆盖：
  - 通知监听 10 秒重绑窗口；
  - “授权仍在”不能被计为已就绪；
  - Android 12 及以下通知权限兼容；
  - 必要项与建议项计数、顺序；
  - 厂商后台只能人工确认。
- `aapt2`、`javac`、`d8`、`zipalign`、`apksigner` 全部通过。
- Pixel 8 / Android 16 模拟器实际安装验证：
  - 主页全检摘要正确显示必要项和保活建议；
  - 逐项引导、状态胶囊、系统路径、修复说明显示正常；
  - 电池系统弹窗允许后返回，自动显示完成并进入下一项；
  - 汇总页正确区分已就绪、待处理和需手动确认。
- 真机签名核对：脚本正确识别手机已装证书 `1365f73e…a4ff` 与当前测试构建签名不一致，并在安装前退出，未改动手机数据。

本轮没有更新 GitHub Releases，也没有在真机覆盖安装 v4.3.0。
