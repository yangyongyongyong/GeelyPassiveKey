# 59. 手机无线 ADB 固定 5555 端口与 Mac 多 server 排查

日期：2026-11-15，Asia/Shanghai。

## 1. 当前结果

手机 `V2426A / iQOO Neo10 Pro` 已固定：

```text
无线 ADB：192.168.199.143:5555
当前版本：v4.2.2-min / versionCode 202
```

手机端确认：

```text
getprop service.adb.tcp.port = 5555
TCP LISTEN *:5555
```

电脑端验证：

```bash
ADB_LIBUSB=0 ADB_MDNS_AUTO_CONNECT=0 \
  adb -H 127.0.0.1 -P 5055 connect 192.168.199.143:5555
```

连接后无线设备和 USB 设备可同时出现在列表中。

## 2. 本次为什么反复连不上

不是每次都需要这样配置。本次是 Mac 上同时残留多个 ADB server：

- `5037` 旧 server 只监听但不响应 client 握手；
- `5060` 旧 server 可响应但 USB transport 已失效；
- 旧进程持有 USB interface，启动新 server 时出现 `e00002c5 / Could not open interface`；
- 再启动一个 server 又出现 `Address already in use`。

清理全部 ADB server，只保留一个新端口后，USB 立即恢复为 `device`。

## 3. 固定 5555 的操作

在有线 ADB 已为 `device` 时执行：

```bash
ADB_LIBUSB=0 ADB_MDNS_AUTO_CONNECT=0 \
  adb -H 127.0.0.1 -P 5055 shell 'ip -4 addr show wlan0'
```

取得手机 Wi-Fi IPv4 后：

```bash
ADB_LIBUSB=0 ADB_MDNS_AUTO_CONNECT=0 \
  adb -H 127.0.0.1 -P 5055 tcpip 5555

ADB_LIBUSB=0 ADB_MDNS_AUTO_CONNECT=0 \
  adb -H 127.0.0.1 -P 5055 connect 192.168.199.143:5555
```

预期：

```text
restarting in TCP mode port: 5555
connected to 192.168.199.143:5555
192.168.199.143:5555 device
```

## 4. 以后电脑端固定使用单 server

本次采用的控制端口是 `5055`，避免继续使用卡死的 `5037/5060`：

```bash
export ADB_LIBUSB=0
export ADB_MDNS_AUTO_CONNECT=0
adb -L tcp:5055 start-server

adb -H 127.0.0.1 -P 5055 devices -l
adb -H 127.0.0.1 -P 5055 connect 192.168.199.143:5555
```

也可以直接封装：

```bash
alias adb5055='ADB_LIBUSB=0 ADB_MDNS_AUTO_CONNECT=0 adb -H 127.0.0.1 -P 5055'
```

常用：

```bash
adb5055 devices -l
adb5055 -s 192.168.199.143:5555 shell getprop ro.serialno
```

## 5. 手机重启后

`adb tcpip 5555` 是本次切换，不保证手机重启、关闭无线调试或重启 adbd 后仍保留。若 `5555` 拒绝连接：

1. 插 USB；
2. 等 `adb devices` 显示 `device`；
3. 再执行一次 `adb tcpip 5555`；
4. 拔线后重新 `adb connect IP:5555`。

若手机系统设置中有“无线调试/ADB over network”，优先确认它没有被切回随机端口。

## 6. 与 App 的关系

固定 `5555` 只影响开发调试连接，不改变：

- 极简无感钥匙蓝牙扫描；
- 银河 MiuiService 注入；
- GATT 鉴权观察；
- 通知 PendingIntent；
- 车型配置和日志。

验证前后 App 配置哈希保持一致：

```text
shared_prefs/passive_key_prefs.xml
4d2c8efc4b4116af3610e32ce0be6b733205923a62a6f3304fc70aae152b120a

shared_prefs/vehicle_param_readonly_prefs.xml
ab98fb278f2046e1b259c1d9a552be0d65b59f4be1dc9a2de2ae1249a48489e4
```
