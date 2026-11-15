# 48. v4.1.2：主页版本显示与版本化 APK 名称

版本：`4.1.2-min / versionCode 112`。2026-11-12本地修改，保留v4.1.1的蓝牙、身份和车控逻辑，只增加可见版本与构建命名。

## 1. App内的版本号

主页标题与副标题下增加一行：

```text
版本：v4.1.2-min（构建 112）
```

- 使用`PackageManager.getPackageInfo(getPackageName(), 0)`查询实际安装包，读取`versionName`和`getLongVersionCode()`；不写死当前版本。
- 页面创建时读取一次，未来只升级Manifest版本就会展示新值，无需同步修改UI字符串。
- 名称空缺或读取失败时显示“未知/读取失败”，不影响服务、扫描、权限或车控。
- 使用原界面主题的高对比文字，独立一行可自动换行，并可选择复制；不把版本藏在日志或系统设置中。

实现位于`MainActivity.installedVersionLabel()`和标题布局；包名、权限、数据结构不改变。

## 2. 打包文件名

统一命名：

```text
GeelyPassiveKey-v<versionName>-<versionCode>.apk
```

本次产物：

```text
GeelyKeyRadarApp/GeelyPassiveKey-v4.1.2-min-112.apk
```

`build.sh`使用`xmllint --nonet --xpath`按XML属性读取`AndroidManifest.xml`的版本名和构建号，不依赖属性顺序、文本行号或注释中的版本字符串。macOS内置`xmllint`；其他环境需安装或通过`XMLLINT=/path/to/xmllint`指定。

- 未提供版本名/构建号、构建号不是数字或Manifest损坏时立即失败，不悄悄生成无版本APK。
- 保留字母、数字、点、短横线和下划线；版本名中的空白、斜杠等替换为短横线，防止产物落入意外目录。
- 签名输出、终端显示和ADB暂存/安装都引用同一个版本化文件名。
- 不生成`GeelyPassiveKey.apk`旧别名。已有旧版APK已按实际版本重命名保留为`GeelyPassiveKey-v4.1.1-min-111.apk`，未删除旧产物。
- 仍沿用现有本地签名密钥，不生成替代密钥。

只查询路径，不构建或操作手机：

```bash
bash GeelyKeyRadarApp/build.sh --print-apk-path
```

仅构建：

```bash
SKIP_INSTALL=1 JAVA_HOME=/Users/luca/Library/Java/JavaVirtualMachines/corretto-11.0.24/Contents/Home \
  bash GeelyKeyRadarApp/build.sh
```

## 3. 测试

保留11组Java回归，并加入`tests/version_naming_test.sh`。该Shell回归复制构建脚本到临时目录，只调用`--print-apk-path`，不编译、不签名、不连接手机：

1. 当前Manifest文件名与包内版本一致；
2. 属性顺序变化、XML命名空间前缀变化和注释中的旧版本不影响结果；
3. 版本名有空白/斜杠时不会生成额外目录；
4. 缺少版本名、非法构建号明确失败；
5. 查询路径不创建构建目录。

完整APK通过`aapt2 dump badging`核对版本、`apksigner verify`验证签名和`zipalign -c`核对对齐。版本显示由实际安装包读取，需在安装后检查主页UI文本与布局。

## 4. 更新注意

手机v4.1.1已在上一轮用户授权下重装并恢复配置，现与本地密钥签名一致。v4.1.2无需卸载，可保留数据覆盖更新。原车辆身份、自动开关和`-75/-82 dBm / 4.5s`参数不修改；原有插线/行驶保护保持。

本次不修改任何蓝牙扫描、鉴权、通知车控或测距算法。未发布GitHub；版本化本地产物不代表已经创建远端Release。

## 5. API依据

已通过Context7查询Android官方`PackageInfo`版本API：`getLongVersionCode()`为API28起推荐的完整版本代码读取方式，项目minSdk29可直接使用。

- https://developer.android.com/reference/android/content/pm/PackageInfo
