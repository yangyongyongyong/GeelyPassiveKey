# 72. v4.5.9：Cloudflare D1 日志一键上报

版本：`4.5.9-min / versionCode 509`。

## 1. 用户操作

维护 → 诊断工具新增：

```text
上传排查日志给开发者
```

本车识别卡片新增：

```text
上报车型兼容问题
```

点击后：

1. 展示上传范围和隐私边界；
2. **必须填写异常现象**；
3. 普通排查直接生成脱敏完整诊断 ZIP；车型兼容上报先额外抓取 10 秒原始 BLE，再生成含 `compatibility_capture.txt` 的 ZIP；
4. 用户再次点击“上传”后才发起网络请求；
5. 成功后显示日志编号，可一键复制。

不会自动上传，也不会因为 App 启动、解锁、上锁或更新而上传。车型兼容按钮同样只是打开手动上报对话框，不会静默发送；提交时问题描述会加上 `【车型兼容】` 前缀，便于后台筛选。

## 2. 云端架构

- 域名：`https://geelylog.mq.lucadesign.uk`
- Worker：`geelypassivekey-log`
- D1：`geelypassivekey-logs`
- 不复用 R2，也不修改任何 Cloudflare Tunnel。
- ZIP 以 BLOB 写入 D1，元数据同表保存。
- 单包上限 `1.8 MB`，D1 单行上限为 `2 MB`。
- 默认保留 `90 天`，Worker 每日定时清理过期记录。
- 每个固定设备 UID 每天最多 10 次、每出口 IP 每天最多 100 次。

D1 表：

- `log_uploads`：编号、安装随机 ID、固定设备 UID、时间、App/Android/机型、用户描述、ZIP、SHA-256；
- `device_upload_rates` / `ip_upload_rates`：按固定 UID 和出口 IP 限流。

固定 UID 生成方式：

```text
SHA-256(
  GeelyPassiveKey | 包名 | Android ID |
  厂商 | 品牌 | 型号 | device | hardware | board | SoC
)[前20位十六进制]
→ GPK-XXXX-XXXX-XXXX-XXXX-XXXX
```

Android ID 仅参与本地哈希，不保存原文、不写日志、不上传。同一设备、用户和应用签名下 UID 固定；恢复出厂设置或更换签名后可能变化。

## 3. 隐私边界

普通诊断上传继续使用现有脱敏导出器；车型兼容上传会额外包含用户主动触发的 `compatibility_capture.txt` 原始 BLE 抓取：

- 每条日志和 ZIP summary 均含固定 `GPK-...` UID；
- 不包含 Android ID 原文；
- 不包含账号；
- 不包含 VIN；
- 普通诊断日志不包含完整蓝牙 MAC；车型兼容附件的原始地址属于显式例外；
- 不包含钥匙、令牌或鉴权密钥；
- 不包含系统通知正文；
- 普通诊断日志中的车辆地址和名称替换为本机指纹；
- 车型兼容附件按采集目的保留原始蓝牙地址、设备名、AD 字节、Manufacturer Data、Service UUID/Data 和当前可见 GATT。

App 另生成随机安装 UUID 用于区分重装，不读取序列号、IMEI 或手机号。用户填写的异常现象为必填项并会原样出现在上传记录中，因此界面明确提示不要填写敏感信息。

## 4. 管理检索

管理员接口需要独立 Bearer Token：

```http
GET /api/logs?limit=30
GET /api/logs?limit=30&uid=GPK-XXXX-XXXX-XXXX-XXXX-XXXX
GET /api/stats
GET /api/logs/<LOG_ID>/download
DELETE /api/logs/<LOG_ID>
```

日常排查可直接使用：

```bash
cloudflare/geelylog/admin.sh stats
cloudflare/geelylog/admin.sh list 30
cloudflare/geelylog/admin.sh list 30 GPK-XXXX-XXXX-XXXX-XXXX-XXXX
cloudflare/geelylog/admin.sh download <LOG_ID>
```

脚本从环境变量或本地忽略文件 `.dev.vars` 读取 Token，不回显密钥。Token 通过 Wrangler Secret 保存，不进入 APK、源码或 Git。后台源码和部署说明位于：

```text
cloudflare/geelylog/
```

## 5. Release 默认参数

本版继续使用：

```text
蓝牙连接触发：≥ -95 dBm
靠近解锁：≥ -75 dBm
离开上锁：≤ -85 dBm
离开确认：2.0 秒
```

升级安装保留用户已保存配置。
