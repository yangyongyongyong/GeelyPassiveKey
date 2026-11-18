# GeelyPassiveKey 日志上报服务

- Worker：`geelypassivekey-log`
- 域名：`https://geelylog.mq.lucadesign.uk`
- 数据库：Cloudflare D1 `geelypassivekey-logs`
- 单包上限：1.8 MB，保留 90 天
- 手动上传，不自动上传；日志 ZIP 由 App 现有脱敏导出器生成
- 每台设备使用固定 `GPK-...` UID，异常现象必填
- 不复用、不修改任何 Cloudflare Tunnel

## App 接口

```http
POST /v1/logs
Content-Type: application/json
```

JSON 字段：

```json
{
  "schema": 1,
  "installId": "随机安装ID",
  "deviceUid": "GPK-XXXX-XXXX-XXXX-XXXX-XXXX",
  "note": "必填，至少4个字符的异常现象",
  "appVersion": "4.5.9-min",
  "versionCode": 509,
  "androidRelease": "16",
  "brand": "vivo",
  "model": "V2426A",
  "archiveName": "GeelyPassiveKey-logs-....zip",
  "archiveBase64": "ZIP Base64",
  "archiveSha256": "可选，服务端复核"
}
```

## 管理接口

项目提供便捷脚本，Token 从环境变量或本地 `.dev.vars` 读取，不会回显：

```bash
./admin.sh stats
./admin.sh list 30
./admin.sh list 30 GPK-XXXX-XXXX-XXXX-XXXX-XXXX
./admin.sh download <LOG_ID>
./admin.sh delete <LOG_ID>
```

也可以直接调用 HTTP API。需要 `ADMIN_TOKEN`：

```bash
curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  https://geelylog.mq.lucadesign.uk/api/logs?limit=30
curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  "https://geelylog.mq.lucadesign.uk/api/logs?limit=30&uid=GPK-XXXX-XXXX-XXXX-XXXX-XXXX"

curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  -o log.zip \
  https://geelylog.mq.lucadesign.uk/api/logs/<LOG_ID>/download

curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  https://geelylog.mq.lucadesign.uk/api/stats
```

## 本地验证

```bash
npm install
npm run db:migrate:local
npm test
npm run deploy:dry
npm run dev
```

## 线上迁移与部署

```bash
npm run db:migrate:remote
npm run deploy
```

`ADMIN_TOKEN` 只通过 `wrangler secret put ADMIN_TOKEN` 配置，不进入仓库。
