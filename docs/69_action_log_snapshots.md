# 69. 解锁/上锁自动日志快照

日期：2026-11-15，Asia/Shanghai。当前源码版本：`4.5.5-min / versionCode 505`。

## 1. 行为

每次自动或手动解锁/上锁成功交给银河后，后台自动生成一个独立 ZIP：

```text
files/action-snapshots/
  GeelyPassiveKey-action-UNLOCK-yyyyMMdd-HHmmss-SSS.zip
  GeelyPassiveKey-action-LOCK-yyyyMMdd-HHmmss-SSS.zip
  ...
```

一个 ZIP 只覆盖动作交付时刻之前的最近 **3 分钟**，不是完整历史日志。

动作快照由 `PassiveKeyService` 在 `pendingControl.complete()` 且 `logic.onControlSent()` 之后触发；手动车控成功后也使用同一机制。生成失败只写诊断，不影响车控。

## 2. 文件内容

| 文件 | 内容 |
| --- | --- |
| `summary.txt` | 动作类型、请求号、三分钟窗口、四项触发配置、运行状态、日志健康 |
| `README.txt` | 快照边界、脱敏和100个文件限制说明 |
| `events.log` | 最近三分钟的关键事件 |
| `diagnostic.log` | 最近三分钟的采样、计数、等待细节 |

`summary.txt` 明确包含当前配置：

```text
connect_dbm=-95.0
unlock_dbm=-79.0
lock_dbm=-85.0
lock_dwell_ms=2000
snapshot_config_captured_wall_ms=...
```

四项均来自本次动作发生时的内存配置，不是从默认值重新生成。用户自定义参数会原样进入快照。

## 3. 三分钟过滤

`DiagnosticSnapshotWindow` 读取日志行首统一时间戳：

```text
yyyy-MM-dd'T'HH:mm:ss.SSS+0800
```

只接纳：

```text
动作时间 - 3 分钟 <= 日志时间 <= 动作时间
```

没有时间戳的续行跟随上一条已接纳日志。日志按轮转分段从旧到新扫描，但不导出窗口外的旧事件。

## 4. 100个上限

- 快照保存在应用私有目录 `files/action-snapshots`；
- 文件数最多 **100**；
- 超过上限后按文件最后修改时间和文件名排序，删除最老文件；
- 删除失败只累计 `io_errors`，不阻塞车控；
- 手动完整日志导出不会增加动作快照数量。

## 5. App内查看与导出

维护页新增：

```text
自动动作快照：N / 最多 100 个 · X KiB
导出全部动作快照（ZIP）
```

“导出全部动作快照”会把当前存在的最多100个动作 ZIP 流式打包到用户选择的位置；不读取或改写原快照。

原有“导出排查日志（ZIP）”仍保留，手动完整导出现在也会把四项配置写入 `summary.txt`。

## 6. 回归

`DiagnosticsTest` 新增验证：

1. 动作快照只包含三分钟窗口内事件；
2. `summary.txt` 包含连接/解锁/上锁/确认四项配置；
3. 快照 ZIP 文件白名单固定；
4. 超过100个后只保留最新100个；
5. 既有关键/诊断容量、脱敏和清空回归继续通过。

自动截图、自动点击和自动安装不做；APK 构建与逻辑回归单独执行。
