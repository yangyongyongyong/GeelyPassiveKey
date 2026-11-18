# 73. v4.6.1：Cloudflare 更新镜像

版本：`4.6.1-min / versionCode 601`。

## 1. 目标

不启用 R2，也不依赖本地 Mac 或 Cloudflare Tunnel，使用 Cloudflare Worker 代理 GitHub Release，并把版本化 APK 缓存在 Cloudflare 边缘。

```text
App
  → 优先请求 https://geelyupdate.mq.lucadesign.uk/update.json
  → Worker 从 GitHub latest Release 读取 update.json
  → 把 apkUrl 改写为 Cloudflare 版本化地址
  → 用户从 Cloudflare 下载 APK
  → Worker 首次回源 GitHub，后续使用边缘缓存
  → 校验 SHA-256、包名、versionCode、签名
  → 通知用户打开 Android 系统安装器

Cloudflare Worker 不可用
  → App 自动回退到 GitHub latest Release 的 update.json
```

## 2. 为什么不用本地内网穿透

技术上可以创建一条独立 Tunnel 指向本地静态服务器，但不适合作为公开更新源：

- 本地 Mac 必须 24 小时在线；
- Mac 睡眠、重启、断电或网络变化都会导致更新中断；
- 每个用户的 APK 下载都消耗家庭网络上行；
- 需要长期维护 Tunnel 进程、认证和本机安全；
- 同一 Cloudflare 账户已有其他电脑正在使用 Tunnel，隔离和维护成本更高。

本方案不使用 R2，不访问本地电脑，不修改任何现有 Tunnel。Cloudflare Worker 回源 GitHub，APK 通过边缘缓存分发。

## 3. Cloudflare 结构

```text
geelyupdate.mq.lucadesign.uk
        │
  Worker: geelypassivekey-update
        ├── GET /update.json
        ├── GET /releases/<versionName>/<apk>
        ├── GET /releases/<versionName>/<apk>.sha256
        └── GET /v1/health
        │
  Cloudflare Cache API
        ├── latest manifest: 5 分钟
        ├── versioned APK: 一年 immutable
        └── SHA-256 文件: 一天 immutable
```

Worker 只允许本项目固定 GitHub Release 前缀，不接受任意上游 URL，防止 SSRF。

## 4. GHA 发布与预热

带版本标签或手动运行 Android Release 后：

1. 编译、签名、校验 APK；
2. 发布 GitHub Release；
3. 请求 Cloudflare `/update.json?refresh=1`，绕过旧清单缓存并触发 Worker 拉取、改写最新清单；
4. 从 Cloudflare 下载完整 APK；
5. 比对 Cloudflare 镜像 SHA-256 与本次构建 SHA-256；
6. 失败则流水线失败，避免发布一个不可用的更新镜像。

该步骤不需要额外 Cloudflare Secret；GitHub Actions 只访问公开的 Cloudflare 更新域名。

## 5. App 安全校验

Cloudflare 与 GitHub 两个清单都只能返回：

- `GeelyPassiveKey-v<versionName>-<versionCode>.apk`；
- 本项目 Cloudflare `/releases/` 路径或本项目 GitHub Release 路径；
- 清单版本名、构建号、文件名一致。

下载后继续验证：

1. 总大小不超过 50 MiB；
2. SHA-256 与清单一致；
3. APK 包名为 `com.luca.geelykeyradar`；
4. versionCode 与清单一致；
5. APK 签名证书与当前安装包一致；
6. 任一失败删除下载文件，不打开系统安装器。

## 6. Android 限制

普通侧载 App 不能静默覆盖自身：

```text
自动检查 → 自动下载 → 自动校验 → 通知 → 用户确认系统安装
```

只有 root、Device Owner/MDM、系统签名或应用商店分发才可能实现真正无确认安装。

## 7. Release 默认参数

```text
蓝牙连接触发：≥ -95 dBm
靠近解锁：≥ -75 dBm
离开上锁：≤ -85 dBm
离开确认：2.0 秒
```

以上仍为全新安装默认值；升级安装保留用户已保存配置。
