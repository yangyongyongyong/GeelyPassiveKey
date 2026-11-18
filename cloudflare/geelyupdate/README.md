# GeelyPassiveKey Cloudflare 更新镜像

- Worker：`geelypassivekey-update`
- 域名：`https://geelyupdate.mq.lucadesign.uk`
- 模式：Cloudflare Worker 代理 GitHub Release，并使用边缘 Cache API
- 不启用 R2，不依赖本地电脑，不使用 Cloudflare Tunnel

## HTTP

```text
GET /update.json
GET /releases/<versionName>/<apkName>
GET /releases/<versionName>/<apkName>.sha256
GET /v1/health
GET /update.json?refresh=1   # 发布后强制刷新边缘清单
```

`update.json` 由 Worker 从 GitHub latest Release 拉取，并把 `apkUrl` 改写为 Cloudflare 版本化地址。第一请求回源 GitHub，后续由 Cloudflare 边缘缓存；APK 使用一年 immutable 缓存。

## 本地检查

```bash
npm install
npm run check
npm run deploy:dry
npm run dev
```
