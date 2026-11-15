#!/usr/bin/env bash
set -euo pipefail

APP_DIR="$(cd "$(dirname "$0")/.." && pwd)"
TEST_ROOT="$(mktemp -d "${TMPDIR:-/tmp}/geely-apk-version.XXXXXX")"
TEST_ROOT="$(cd "$TEST_ROOT" && pwd)"
trap 'rm -rf "$TEST_ROOT"' EXIT
cp "$APP_DIR/build.sh" "$TEST_ROOT/build.sh"

# 命名必须来自XML属性；只打印路径时不得构建、签名或连接手机。
check_path() {
    local expected="$1"
    local actual
    actual="$(bash "$TEST_ROOT/build.sh" --print-apk-path)"
    if [ "$actual" != "$TEST_ROOT/$expected" ]; then
        echo "APK命名错误：预期 ${expected}，实际 ${actual}" >&2
        exit 1
    fi
    if [ -d "$TEST_ROOT/build" ]; then
        echo "只打印APK路径不应创建构建目录" >&2
        exit 1
    fi
}

# 缺失或损坏的版本配置要显式失败，不能生成无版本或碰巧沿用旧版本的APK。
check_invalid_manifest() {
    if bash "$TEST_ROOT/build.sh" --print-apk-path >"$TEST_ROOT/output" 2>"$TEST_ROOT/error"; then
        echo "无效版本配置不应成功生成APK路径" >&2
        exit 1
    fi
}

# 当前Manifest原样解析，包括-min后缀和构建号，未来只改Manifest即自动更新文件名。
cp "$APP_DIR/AndroidManifest.xml" "$TEST_ROOT/AndroidManifest.xml"
XMLLINT="${XMLLINT:-$(command -v xmllint)}"
NAME="$("$XMLLINT" --nonet --xpath "string(/*[local-name()='manifest']/@*[local-name()='versionName'])" "$TEST_ROOT/AndroidManifest.xml")"
CODE="$("$XMLLINT" --nonet --xpath "string(/*[local-name()='manifest']/@*[local-name()='versionCode'])" "$TEST_ROOT/AndroidManifest.xml")"
SAFE_NAME="$(printf '%s' "$NAME" | LC_ALL=C tr -c 'A-Za-z0-9._-' '-')"
check_path "GeelyPassiveKey-v${SAFE_NAME}-${CODE}.apk"

# 属性换序、命名空间前缀改变、注释中留旧版本都不能影响读取当前版本。
cat > "$TEST_ROOT/AndroidManifest.xml" <<'XML'
<!-- android:versionName="1.0-old" android:versionCode="1" -->
<manifest xmlns:a="http://schemas.android.com/apk/res/android" a:versionName="5.2.0-rc.1" package="test" a:versionCode="520"/>
XML
check_path "GeelyPassiveKey-v5.2.0-rc.1-520.apk"

# 版本名中的空白/斜杠不能创建额外目录；保留常见版本名字符。
cat > "$TEST_ROOT/AndroidManifest.xml" <<'XML'
<manifest xmlns:android="http://schemas.android.com/apk/res/android" android:versionName="5.3 beta/test" android:versionCode="530"/>
XML
check_path "GeelyPassiveKey-v5.3-beta-test-530.apk"

cat > "$TEST_ROOT/AndroidManifest.xml" <<'XML'
<manifest xmlns:android="http://schemas.android.com/apk/res/android" android:versionCode="531"/>
XML
check_invalid_manifest

cat > "$TEST_ROOT/AndroidManifest.xml" <<'XML'
<manifest xmlns:android="http://schemas.android.com/apk/res/android" android:versionName="5.3" android:versionCode="not-a-number"/>
XML
check_invalid_manifest

printf 'VersionNamingTest passed\n'
