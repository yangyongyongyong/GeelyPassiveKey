#!/usr/bin/env bash
set -euo pipefail

APP_DIR="$(cd "$(dirname "$0")" && pwd)"
BUILD_TOOLS="${BUILD_TOOLS:-/opt/homebrew/share/android-commandlinetools/build-tools/36.0.0}"
ANDROID_JAR="${ANDROID_JAR:-/opt/homebrew/share/android-commandlinetools/platforms/android-36/android.jar}"
ADB="${ADB:-$(command -v adb || echo "$HOME/platform-tools/platform-tools/adb")}"
if [ -n "${JAVA_HOME:-}" ] && [ -d "$JAVA_HOME/bin" ]; then
    PATH="$JAVA_HOME/bin:$PATH"
elif [ -d "$HOME/Library/Java/JavaVirtualMachines/corretto-11.0.24/Contents/Home/bin" ]; then
    PATH="$HOME/Library/Java/JavaVirtualMachines/corretto-11.0.24/Contents/Home/bin:$PATH"
fi

BUILD_DIR="$APP_DIR/build/passive"
GEN_DIR="$BUILD_DIR/gen"
CLASSES_DIR="$BUILD_DIR/classes"
DEX_DIR="$BUILD_DIR/dex"
TEST_DIR="$BUILD_DIR/test-classes"
XMLLINT="${XMLLINT:-$(command -v xmllint || true)}"
if [ -z "$XMLLINT" ] || ! command -v "$XMLLINT" >/dev/null 2>&1; then
    echo "缺少 xmllint，无法从 AndroidManifest.xml 读取版本；可设置 XMLLINT 指定可执行文件" >&2
    exit 1
fi

# 按XML属性读取版本，避免靠文本行号或正则碰巧匹配到注释中的旧版本。
read_manifest_version() {
    "$XMLLINT" --nonet --xpath "string(/*[local-name()='manifest']/@*[local-name()='$1'])" \
        "$APP_DIR/AndroidManifest.xml"
}

VERSION_NAME="$(read_manifest_version versionName)"
VERSION_CODE="$(read_manifest_version versionCode)"
if [ -z "$VERSION_NAME" ] || [[ ! "$VERSION_CODE" =~ ^[0-9]+$ ]]; then
    echo "AndroidManifest.xml 必须提供非空 versionName 和数字 versionCode" >&2
    exit 1
fi
# 保留版本名常用字符，斜杠、空白等替换为短横线，防止文件名生成额外目录。
SAFE_VERSION_NAME="$(printf '%s' "$VERSION_NAME" | LC_ALL=C tr -c 'A-Za-z0-9._-' '-')"
APK_NAME="GeelyPassiveKey-v${SAFE_VERSION_NAME}-${VERSION_CODE}.apk"
APK="$APP_DIR/$APK_NAME"
REMOTE_APK="/data/local/tmp/$APK_NAME"

# 只打印预期产物路径，便于发布脚本和命名回归检查，不编译、不接触手机。
if [ "${1:-}" = "--print-apk-path" ] && [ "$#" -eq 1 ]; then
    printf '%s\n' "$APK"
    exit 0
elif [ "$#" -gt 0 ]; then
    echo "用法: bash build.sh [--print-apk-path]；仅构建请设置 SKIP_INSTALL=1" >&2
    exit 1
fi

echo "版本: $VERSION_NAME (versionCode=$VERSION_CODE)"

mkdir -p "$GEN_DIR" "$CLASSES_DIR" "$DEX_DIR" "$TEST_DIR"
find "$GEN_DIR" -type f -delete 2>/dev/null || true
find "$CLASSES_DIR" -type f -delete 2>/dev/null || true
find "$DEX_DIR" -type f -delete 2>/dev/null || true
find "$TEST_DIR" -type f -delete 2>/dev/null || true

echo "[1/6] 纯逻辑单测"
javac -source 11 -target 11 -d "$TEST_DIR" \
    "$APP_DIR/src/com/luca/geelykeyradar/PassiveKeyLogic.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/RssiFilter.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/RssiChartSeries.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/GattAuthFrameParser.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/PendingControl.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/NotificationEpoch.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/RotatingFileLog.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/DiagnosticText.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/DiagnosticCounters.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/DiagnosticLogStore.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleLearningState.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleBeacon.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleDiscoverySignature.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleParamCodec.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleIdentity.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleTargetSelector.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleWakeGate.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/VehicleScanPolicy.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/OfficialReadyGate.java" \
    "$APP_DIR/src/com/luca/geelykeyradar/ThresholdSettingsForm.java" \
    "$APP_DIR/tests/com/luca/geelykeyradar/"*Test.java
for TEST_CLASS in PassiveKeyLogicTest RssiPipelineTest RssiChartSeriesTest GattAuthFrameParserTest PendingControlTest NotificationEpochTest RotatingFileLogTest DiagnosticsTest VehicleLearningStateTest VehicleIdentityTest VehicleTargetSelectorTest VehicleWakeGateTest VehicleScanPolicyTest OfficialReadyGateTest ThresholdSettingsFormTest VehicleParamCodecTest; do
    java -ea -cp "$TEST_DIR" "com.luca.geelykeyradar.$TEST_CLASS"
done

bash "$APP_DIR/tests/version_naming_test.sh"

echo "[2/6] 编译资源"
"$BUILD_TOOLS/aapt2" compile --dir "$APP_DIR/res" -o "$BUILD_DIR/res.zip"
"$BUILD_TOOLS/aapt2" link -I "$ANDROID_JAR" \
    --manifest "$APP_DIR/AndroidManifest.xml" \
    --java "$GEN_DIR" \
    -o "$BUILD_DIR/unsigned.apk" \
    "$BUILD_DIR/res.zip"

echo "[3/6] 编译 Java"
javac -source 11 -target 11 -cp "$ANDROID_JAR" -d "$CLASSES_DIR" \
    $(find "$GEN_DIR" -name "R.java") \
    $(find "$APP_DIR/src" -name "*.java")

echo "[4/6] 生成 DEX"
"$BUILD_TOOLS/d8" --lib "$ANDROID_JAR" --output "$DEX_DIR" \
    $(find "$CLASSES_DIR" -name "*.class")
(cd "$DEX_DIR" && zip -q -u "$BUILD_DIR/unsigned.apk" classes.dex)

echo "[5/6] 对齐与签名"
"$BUILD_TOOLS/zipalign" -f -p 4 "$BUILD_DIR/unsigned.apk" "$BUILD_DIR/aligned.apk"
if [ ! -f "$APP_DIR/build/debug.keystore" ]; then
    keytool -genkeypair -v \
        -keystore "$APP_DIR/build/debug.keystore" \
        -storepass android -keypass android \
        -alias androiddebugkey \
        -keyalg RSA -keysize 2048 -validity 10000 \
        -dname "CN=Android Debug,O=Android,C=US"
fi
"$BUILD_TOOLS/apksigner" sign \
    --ks "$APP_DIR/build/debug.keystore" \
    --ks-pass pass:android \
    --key-pass pass:android \
    --out "$APK" \
    "$BUILD_DIR/aligned.apk"
echo "APK: $APK"

if [ "${SKIP_INSTALL:-0}" = "1" ]; then
    echo "[6/6] 已跳过安装，APK 构建完成"
    exit 0
fi

echo "[6/6] 尝试安装到真机"
export ADB_LIBUSB=0
ADB_ARGS=()
if "$ADB" -P 5038 get-state >/dev/null 2>&1; then
    ADB_ARGS=(-P 5038)
elif [ -S /tmp/adb_claude.sock ] && ADB_SERVER_SOCKET=localfilesystem:/tmp/adb_claude.sock "$ADB" get-state >/dev/null 2>&1; then
    export ADB_SERVER_SOCKET=localfilesystem:/tmp/adb_claude.sock
elif "$ADB" get-state >/dev/null 2>&1; then
    ADB_ARGS=()
fi
if "$ADB" "${ADB_ARGS[@]}" get-state >/dev/null 2>&1; then
    "$ADB" "${ADB_ARGS[@]}" shell input keyevent KEYCODE_WAKEUP >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell wm dismiss-keyguard >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" push "$APK" "$REMOTE_APK" >/dev/null
    ("$ADB" "${ADB_ARGS[@]}" shell pm install -r -g "$REMOTE_APK") &
    PM_PID=$!
    # vivo/OriginOS 外部来源安装确认页（1260x2800）
    sleep 4.2
    "$ADB" "${ADB_ARGS[@]}" shell input tap 447 2478 >/dev/null 2>&1 || true
    sleep 0.8
    "$ADB" "${ADB_ARGS[@]}" shell input tap 630 2674 >/dev/null 2>&1 || true
    if ! wait "$PM_PID"; then
        echo "安装失败，APK 已生成；尚未切换手机上的版本" >&2
        exit 1
    fi
    "$ADB" "${ADB_ARGS[@]}" shell cmd notification disallow_listener \
        com.luca.geelykeyradar/com.luca.geelykeyradar.KeyNotificationBridgeService >/dev/null 2>&1 || true
    sleep 0.2
    "$ADB" "${ADB_ARGS[@]}" shell cmd notification allow_listener \
        com.luca.geelykeyradar/com.luca.geelykeyradar.KeyNotificationBridgeService >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell dumpsys deviceidle whitelist +com.luca.geelykeyradar >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell cmd appops set com.luca.geelykeyradar RUN_IN_BACKGROUND allow >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell cmd appops set com.luca.geelykeyradar RUN_ANY_IN_BACKGROUND allow >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell cmd appops set com.luca.geelykeyradar WAKE_LOCK allow >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell cmd appops set com.luca.geelykeyradar START_FOREGROUND allow >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell am set-standby-bucket com.luca.geelykeyradar active >/dev/null 2>&1 || true
    "$ADB" "${ADB_ARGS[@]}" shell am start -n com.luca.geelykeyradar/.MainActivity >/dev/null 2>&1 || true
    echo "✓ 已安装并启动极简无感钥匙"
else
    echo "当前未连接 USB 真机，APK 已生成"
fi
