# 61. v4.4.0：应用内自动更新与 GitHub Release 流水线

日期：2026-11-15，Asia/Shanghai。版本：`4.4.0-min / versionCode 400`。

本版把“发布新版本”和“手机更新”连成一条完整链路：

```text
推送 v* 标签
  → GitHub Actions 用发布密钥编译、签名、校验
  → 创建 GitHub Release 并上传版本化 APK + SHA-256 + update.json
  → 旧版 App 每小时检查 latest Release 的 update.json
  → 自动下载并校验
  → 通知用户点开系统安装器
  → 也可在 App 内手动检查/安装
```

## 1. 手机端更新设置

主页新增“应用更新”卡片：

- “自动检查更新（每 1 小时）”默认开启；
- 自动模式发现有更高 versionCode 时，自动下载 APK；
- 下载完成后发“应用更新”通知；
- 通知点击进入 App 的安装确认页；
- “立即检查更新”始终可手动触发；
- 已下载后按钮变为“安装 vX”，再次点击打开系统安装器。

检查周期固定为 `3,600,000 ms`。打开主页、前台服务创建以及前台服务运行期间每 60 秒都会调用一次轻量调度器，但真正访问 GitHub 最多每小时一次。检查失败或无网络不会无限重试；用户可随时手动检查。

更新地址是稳定的 Release 资源地址：

```text
https://github.com/yangyongyongyong/GeelyPassiveKey/releases/latest/download/update.json
```

不使用 GitHub REST API，因此不受匿名 API 每小时 60 次的限流影响。`update.json` 与 APK 在同一次 Release 中发布。

### Android 的限制

普通应用不能在后台静默覆盖安装自己。自动更新实现到：

- 自动检查；
- 自动下载；
- 自动校验；
- 通知用户。

最后一步必须由 Android 系统安装器确认。是否需要“安装未知应用”权限取决于 ROM；App 在需要时打开本应用的授权页，授权返回后继续安装。

## 2. 更新包校验

下载流程不只信任文件名，依次检查：

1. 只接受 GitHub Actions 生成的 `GeelyPassiveKey-v<versionName>-<versionCode>.apk`；
2. 远端 versionCode 必须大于已安装版本；
3. 下载总量上限 50 MiB；
4. `update.json` 的 `sha256` 必须与本地 SHA-256 完全一致；
5. `PackageManager` 解析 APK 后，包名必须为 `com.luca.geelykeyradar`；
6. APK 的 versionCode 必须与 Release 文件名一致；
7. APK 的签名证书集合必须与当前安装包完全一致；
8. 任一步失败都删除下载文件，不打开安装器。

更新 APK 放在应用私有缓存 `cache/updates/`，通过只读 `UpdateApkProvider` 临时授权给系统安装器。Provider 不对外开放，只允许读取该目录内的单个文件。

## 3. GitHub Actions

新增：

```text
.github/workflows/android-release.yml
```

触发方式：

- 推送 `v*` 标签；
- 或在 Actions 页面手动运行并填写已有标签。

流水线执行：

1. 校验标签与 `AndroidManifest.xml` 的 `versionName` 一致；
2. 安装 JDK 17、Android 36 平台和 Build Tools 36.0.0；
3. 从 Base64 Secret 还原发布 keystore；
4. 运行现有 18 组 Java 测试＋APK 命名回归；
5. `aapt2` → `javac` → `d8` → `zipalign` → `apksigner`；
6. 输出签名证书 SHA-256；
7. 生成 APK 的 `.sha256` 文件；
8. 生成 `update.json`（versionName/versionCode/APK URL/size/SHA-256）；
9. 创建/更新 GitHub Release，上传 APK、`.sha256` 和 `update.json`。

`update.json` 结构：

```json
{
  "versionName": "4.4.0-min",
  "versionCode": 400,
  "apkName": "GeelyPassiveKey-v4.4.0-min-400.apk",
  "apkUrl": "https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/v4.4.0-min/GeelyPassiveKey-v4.4.0-min-400.apk",
  "sha256": "sha256:…64位十六进制…",
  "size": 173124
}
```

### 必须配置的仓库 Secrets

| Secret | 必填 | 说明 |
| :--- | :--- | :--- |
| `ANDROID_KEYSTORE_BASE64` | 是 | 原发布 keystore 的 Base64 内容 |
| `ANDROID_KEYSTORE_PASS` | 是 | keystore 密码 |
| `ANDROID_KEY_ALIAS` | 是 | 签名别名 |
| `ANDROID_KEY_PASS` | 否 | key 密码；留空时沿用 keystore 密码 |
| `ANDROID_CERT_SHA256` | 推荐 | 期望证书 SHA-256，流水线发现不一致直接失败 |

Base64 生成示例：

```bash
base64 < path/to/release.keystore | tr -d '\n'
```

不要把 keystore 或密码提交到仓库。生成 Secret 后，后续所有更新必须继续使用这把密钥；换密钥会被 App 的签名一致性检查拒绝，也会导致旧版无法覆盖安装。

### 发布流程

先把 `AndroidManifest.xml` 的 `versionName/versionCode` 改成新版本，提交后推送与版本名相同的标签，例如：

```bash
git tag v4.4.0-min
git push origin v4.4.0-min
```

标签和 Manifest 不一致时流水线会失败，不会生成错误版本 Release。

## 4. App 更新状态

更新状态会持久化到 `app_update_prefs`：

- 自动检查开关，默认 `true`；
- 上次尝试检查时间；
- 上次成功时间；
- 最近状态文案；
- 已下载 APK 的路径、版本名、versionCode 和 digest。

主页每 500ms 只读取本地状态，不会因此访问网络。网络请求和 APK 下载都在单独后台线程执行，不阻塞蓝牙扫描、通知解析或车控队列。

## 5. 验证

- 18 组 Java 测试＋APK 命名回归通过；新增 `UpdatePolicyTest` 覆盖：
  - 一小时检查窗口；
  - 拒绝旧的无版本 `GeelyPassiveKey.apk`；
  - 版本号比较；
  - 文件名清理；
  - GitHub `sha256:` digest 比较。
- Android 资源、Java、DEX、zipalign、v3 APK 签名通过。
- Pixel 8 / Android 16 模拟器验证：
  - 首次安装后自动检查默认开启；
  - GitHub Release 资源实连并正确显示“最新 Release 尚未上传 update.json”；
  - 使用临时本地 Release 完成 `400 → 401` 自动下载；
  - GitHub SHA-256、包名、versionCode 和签名校验通过；
  - 产生更新通知；
  - App 内出现“安装 v4.4.1-min”；
  - `UpdateApkProvider` 成功把私有 APK 交给 Android 系统安装器；
  - 系统安装器显示“Update this app?”。
- 模拟器最后一步被 Play Protect 的安装验证策略拦截，属于测试环境策略；App 到系统安装器的链路已经完成。真实发布时由同一签名密钥和用户系统安装确认决定最终安装。

## 6. 本次真机升级与新的签名基线

2026-11-15 已在真机完成升级：

- 升级前完整备份旧 APK、`shared_prefs`、全部日志、通知监听和后台授权记录；
- 原 `1365f73e…b292a4ff` 签名私钥不在本机，无法原地覆盖；
- 使用现有稳定测试密钥重新安装 `4.4.0-min / 400`；
- 恢复应用数据后，`shared_prefs/passive_key_prefs.xml` 的 SHA-256 与升级前完全一致；
- 重新授予蓝牙扫描/连接、系统通知、通知监听、电池白名单、后台/前台服务及安装未知应用权限；
- 实机确认必要项 `6/6`、通知监听在线、无感服务前台运行。

因此手机当前安装包的签名基线已经变为：

```text
279948c5d7401188fb72c218e954fc088b8d9cd5eb1aece6dd077b19c08145c4
```

后续 GitHub Actions 必须使用与当前手机相同的 keystore；建议将 `ANDROID_CERT_SHA256` 设置为上面的值。若仍使用旧的 `1365f73e…` 密钥发布，手机端会因签名不一致拒绝安装。

本次使用的签名密钥副本、旧 APK、旧数据归档和安装后 APK 校验记录保存在本机忽略目录：

```text
GeelyKeyRadarApp/build/upgrade-backup-20261115-151411/
```

这些文件包含私钥或应用数据，绝不能提交到仓库或上传到公开 Release。原签名私钥如果以后找回，也可以重新走一次备份、卸载、安装和恢复流程切回，但普通覆盖更新不能跨签名。
