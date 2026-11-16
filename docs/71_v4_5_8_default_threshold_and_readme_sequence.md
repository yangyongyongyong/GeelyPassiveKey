# 71. v4.5.8：默认触发参数调整与 README 时序图

版本：`4.5.8-min / versionCode 508`。

## 1. 项目默认参数

| 参数 | 默认值 | 语义 |
| --- | ---: | --- |
| 链接触发 | `≥ -95 dBm` | 达到后把真实广播交给银河，提前唤醒官方蓝牙钥匙 |
| 靠近解锁 | `≥ -75 dBm` | 达到后才形成靠近解锁资格 |
| 离开上锁 | `≤ -85 dBm` | 进入离开判定区间 |
| 离开确认时长 | `2.0 秒` | 远场条件需要连续保持的确认时间 |

链接触发不等于解锁：前者只负责更早唤醒银河，后者才决定是否请求解锁。

## 2. 默认值不会覆盖用户配置

- 新安装或清除应用数据后，首次使用上述默认值；
- 覆盖安装、自动更新和服务重建只读取 SharedPreferences，不写回默认值；
- 设置页“填入默认值”只填充输入草稿，用户再次点击“保存并生效”后才会修改配置；
- 现有用户已保存的四项参数保持不变。

## 3. README 与 Release

README 顶部直接展示 `docs/diagrams/unlock_sequence.png`，点击可进入时序与架构说明。

Release 正文由 `.github/workflows/android-release.yml` 从 `PassiveKeyDefaults.java` 动态提取，固定包含：

```text
蓝牙连接触发：≥ -95 dBm
靠近解锁：≥ -75 dBm
离开上锁：≤ -85 dBm
离开确认：2.0 秒
```

并注明升级安装保留用户配置。任一项解析失败时 Release 流水线失败。Actions 手动运行可不预先创建 tag：workflow 会从 `AndroidManifest.xml` 自动生成并创建对应标签。

## 4. 回归

- `PassiveKeyLogicTest` 校验项目默认值为 `-95 / -75 / -85 / 2000 ms`；
- `ThresholdSettingsFormTest` 校验 App 内默认值辅助说明包含新参数；
- `AndroidManifest.xml` 使用 `4.5.8-min / versionCode 508`，便于后续自动更新识别；
- 本地构建只编译 APK，不自动截图、点击或安装手机。
