# 55. v4.1.8：加粗实时曲线阈值线

日期：2026-11-15，Asia/Shanghai。版本：`4.1.8-min / versionCode 118`。

用户反馈实时曲线里的绿色解锁线和红色上锁线偏细。v4.1.7原实现为1dp虚线，在1260宽屏截图缩放后不够醒目。

v4.1.8只调整`RssiChartView`视觉：

- 阈值虚线由1dp加粗到2dp；
- 虚线段从6dp/4dp调整为8dp/5dp；
- 在2dp虚线下面增加6dp、透明度48的柔和光晕；
- 阈值标签字号由10sp提高到11sp；
- 原始/平滑RSSI曲线、阈值数值、采样逻辑和车控状态机均不变。

手机验收：vivo V2426A已覆盖安装`4.1.8-min / 118`，页面截图确认绿色/红色阈值线明显更宽，通知监听恢复为`listener_connected=true`，扫描保持`TRACKING`。

构建结果：15组Java测试＋1组APK命名回归通过，zipalign和签名验证通过。

```text
/Users/luca/Workspace/car_key_re/GeelyKeyRadarApp/GeelyPassiveKey-v4.1.8-min-118.apk
SHA-256: 9ae414df7debfd37a92e65cc7fc2cf47012ecf49b0362c1c85631b56eb12a990
```

签名证书仍为`1365f73e3a3dc9f0685e73c7607708246de806f9363d8337c50a0bf0b292a4ff`。
