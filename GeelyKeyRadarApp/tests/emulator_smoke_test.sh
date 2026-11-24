#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SDK_ROOT="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-$HOME/Library/Android/sdk}}"
ADB="${ADB:-$SDK_ROOT/platform-tools/adb}"
EMULATOR="${EMULATOR:-$SDK_ROOT/emulator/emulator}"
AVD_NAME="${AVD_NAME:-GeelyUnpack35}"
KEEP_EMULATOR="${KEEP_EMULATOR:-0}"
SKIP_BUILD="${SKIP_BUILD:-0}"
STARTED_EMULATOR=0
BUILT_APK=""
BUILT_IDSIG=""
BACKUP_APK=""
BACKUP_IDSIG=""
HAD_APK=0
HAD_IDSIG=0

log() { printf '[AVD-SMOKE] %s\n' "$*"; }
fail() { printf '[AVD-SMOKE] FAIL: %s\n' "$*" >&2; exit 1; }

cleanup() {
    if [ -n "$BACKUP_APK" ] && [ -f "$BACKUP_APK" ] && [ -n "$BUILT_APK" ]; then
        cp -p "$BACKUP_APK" "$BUILT_APK" || true
    fi
    if [ -n "$BACKUP_IDSIG" ] && [ -f "$BACKUP_IDSIG" ] && [ -n "$BUILT_IDSIG" ]; then
        cp -p "$BACKUP_IDSIG" "$BUILT_IDSIG" || true
    fi
    if [ "$HAD_APK" = "0" ] && [ -n "$BUILT_APK" ] && [ -f "$BUILT_APK" ]; then
        find "$(dirname "$BUILT_APK")" -maxdepth 1 -name "$(basename "$BUILT_APK")" -type f -delete || true
    fi
    if [ "$HAD_IDSIG" = "0" ] && [ -n "$BUILT_IDSIG" ] && [ -f "$BUILT_IDSIG" ]; then
        find "$(dirname "$BUILT_IDSIG")" -maxdepth 1 -name "$(basename "$BUILT_IDSIG")" -type f -delete || true
    fi
    if [ "$STARTED_EMULATOR" = "1" ] && [ "$KEEP_EMULATOR" != "1" ]; then
        "$ADB" -s "$SERIAL" emu kill >/dev/null 2>&1 || true
    fi
}
trap cleanup EXIT

[ -x "$ADB" ] || fail "missing adb: $ADB"
[ -x "$EMULATOR" ] || fail "missing emulator: $EMULATOR"

if [ -n "${ADB_SERIAL:-}" ]; then
    SERIAL="$ADB_SERIAL"
else
    SERIAL="$("$ADB" devices | awk '/^emulator-[0-9]+[[:space:]]+device$/ {print $1; exit}')"
fi

if [ -z "${SERIAL:-}" ]; then
    [ -x "$EMULATOR" ] || fail "no running emulator and emulator binary is unavailable"
    log "starting AVD $AVD_NAME"
    EMULATOR_RUN_DIR="$(mktemp -d "${TMPDIR:-/tmp}/gpk-avd.XXXXXX")"
    LOG_FILE="$EMULATOR_RUN_DIR/emulator.log"
    "$EMULATOR" -avd "$AVD_NAME" -no-window -no-audio -no-boot-anim \
        -gpu swiftshader_indirect -no-snapshot-load -no-snapshot-save \
        >"$LOG_FILE" 2>&1 &
    STARTED_EMULATOR=1
    for _ in $(seq 1 120); do
        SERIAL="$("$ADB" devices | awk '/^emulator-[0-9]+[[:space:]]+device$/ {print $1; exit}')"
        [ -n "$SERIAL" ] && break
        sleep 1
    done
    [ -n "${SERIAL:-}" ] || fail "emulator did not connect; log=$LOG_FILE"
fi

export ADB_SERIAL="$SERIAL"
ADB_CMD=("$ADB" -s "$SERIAL")

log "waiting for $SERIAL boot"
"${ADB_CMD[@]}" wait-for-device
for _ in $(seq 1 120); do
    [ "$("${ADB_CMD[@]}" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && break
    sleep 2
done
[ "$("${ADB_CMD[@]}" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] \
    || fail "emulator boot timeout"
[ "$("${ADB_CMD[@]}" shell getprop ro.kernel.qemu 2>/dev/null | tr -d '\r')" = "1" ] \
    || fail "refusing destructive smoke test on a non-emulator serial: $SERIAL"

if [ "$SKIP_BUILD" != "1" ]; then
    TMP_DIR="$(mktemp -d "${TMPDIR:-/tmp}/gpk-avd-build.XXXXXX")"
    TEST_KEYSTORE="$TMP_DIR/emulator-debug.keystore"
    BUILT_APK="$("$ROOT/build.sh" --print-apk-path)"
    BUILT_IDSIG="$BUILT_APK.idsig"
    if [ -f "$BUILT_APK" ]; then
        cp -p "$BUILT_APK" "$TMP_DIR/original-apk"
        BACKUP_APK="$TMP_DIR/original-apk"
        HAD_APK=1
    fi
    if [ -f "$BUILT_IDSIG" ]; then
        cp -p "$BUILT_IDSIG" "$TMP_DIR/original-idsig"
        BACKUP_IDSIG="$TMP_DIR/original-idsig"
        HAD_IDSIG=1
    fi
    log "building with isolated emulator-only debug key"
    if ! (
        cd "$ROOT"
        ANDROID_KEYSTORE="$TEST_KEYSTORE" ALLOW_NEW_DEBUG_KEYSTORE=1 SKIP_INSTALL=1 \
            ADB_SERIAL="$SERIAL" bash build.sh
    ) >"$TMP_DIR/build.log" 2>&1; then
        cat "$TMP_DIR/build.log" >&2
        fail "build failed"
    fi
    APK="$TMP_DIR/$(basename "$BUILT_APK")"
    cp "$BUILT_APK" "$APK"
    # 临时签名产物不得覆盖或残留为仓库中的发布候选。
    if [ "$HAD_APK" = "1" ]; then
        cp -p "$TMP_DIR/original-apk" "$BUILT_APK"
        BACKUP_APK=""
    else
        find "$(dirname "$BUILT_APK")" -maxdepth 1 -name "$(basename "$BUILT_APK")" -type f -delete
    fi
    if [ "$HAD_IDSIG" = "1" ]; then
        cp -p "$TMP_DIR/original-idsig" "$BUILT_IDSIG"
        BACKUP_IDSIG=""
    else
        find "$(dirname "$BUILT_IDSIG")" -maxdepth 1 -name "$(basename "$BUILT_IDSIG")" -type f -delete
    fi
else
    APK="$("$ROOT/build.sh" --print-apk-path)"
fi
[ -f "$APK" ] || fail "APK not found: $APK"

log "installing $APK"
"${ADB_CMD[@]}" uninstall com.luca.geelykeyradar >/dev/null 2>&1 || true
"${ADB_CMD[@]}" install -r -g "$APK" >/dev/null

for permission in BLUETOOTH_SCAN BLUETOOTH_CONNECT POST_NOTIFICATIONS; do
    "${ADB_CMD[@]}" shell pm grant com.luca.geelykeyradar "android.permission.$permission" \
        >/dev/null 2>&1 || true
done
"${ADB_CMD[@]}" shell cmd notification allow_listener \
    com.luca.geelykeyradar/com.luca.geelykeyradar.KeyNotificationBridgeService >/dev/null

"${ADB_CMD[@]}" logcat -c
"${ADB_CMD[@]}" shell input keyevent KEYCODE_WAKEUP >/dev/null 2>&1 || true
"${ADB_CMD[@]}" shell wm dismiss-keyguard >/dev/null 2>&1 || true
"${ADB_CMD[@]}" shell am start -W -n com.luca.geelykeyradar/.MainActivity >/dev/null
sleep 5

PID="$("${ADB_CMD[@]}" shell pidof com.luca.geelykeyradar 2>/dev/null | tr -d '\r')"
[ -n "$PID" ] || fail "app process did not stay alive"
SERVICES="$("${ADB_CMD[@]}" shell dumpsys activity services com.luca.geelykeyradar)"
printf '%s\n' "$SERVICES" | grep -q 'com.luca.geelykeyradar/.PassiveKeyService' \
    || fail "PassiveKeyService was not started"
printf '%s\n' "$SERVICES" | grep -q 'isForeground=true' \
    || fail "PassiveKeyService is not a foreground service"
printf '%s\n' "$SERVICES" | grep -Eq 'types=0x0*10|foregroundServiceType=connectedDevice' \
    || fail "PassiveKeyService is missing connectedDevice foreground type"

LISTENERS="$("${ADB_CMD[@]}" shell dumpsys notification --noredact)"
printf '%s\n' "$LISTENERS" | grep -q 'com.luca.geelykeyradar/com.luca.geelykeyradar.KeyNotificationBridgeService' \
    || fail "notification listener is not enabled"

log "cycling notification listener access to exercise rebind"
LISTENER_COMPONENT="com.luca.geelykeyradar/com.luca.geelykeyradar.KeyNotificationBridgeService"
"${ADB_CMD[@]}" shell cmd notification disallow_listener "$LISTENER_COMPONENT" >/dev/null
sleep 2
"${ADB_CMD[@]}" shell cmd notification allow_listener "$LISTENER_COMPONENT" >/dev/null
sleep 8
LISTENERS_AFTER="$("${ADB_CMD[@]}" shell dumpsys notification --noredact)"
printf '%s\n' "$LISTENERS_AFTER" | grep -q "ComponentInfo{$LISTENER_COMPONENT}" \
    || fail "notification listener did not rebind after access cycle"

log "cycling screen to exercise interactive scan recovery"
"${ADB_CMD[@]}" shell input keyevent KEYCODE_SLEEP >/dev/null 2>&1 || true
sleep 6
"${ADB_CMD[@]}" shell input keyevent KEYCODE_WAKEUP >/dev/null 2>&1 || true
"${ADB_CMD[@]}" shell wm dismiss-keyguard >/dev/null 2>&1 || true
sleep 6

LOGS="$("${ADB_CMD[@]}" logcat -d -v brief)"
printf '%s\n' "$LOGS" | grep -q 'event=SCREEN_ON' \
    || fail "screen-on recovery broadcast was not observed"
if printf '%s\n' "$LOGS" | grep -q 'FATAL EXCEPTION'; then
    printf '%s\n' "$LOGS" | grep -A30 -B5 'FATAL EXCEPTION' >&2 || true
    fail "app produced a fatal exception"
fi

PID_AFTER="$("${ADB_CMD[@]}" shell pidof com.luca.geelykeyradar 2>/dev/null | tr -d '\r')"
[ -n "$PID_AFTER" ] || fail "app process died after screen cycle"

log "PASS device=$SERIAL api=$("${ADB_CMD[@]}" shell getprop ro.build.version.sdk | tr -d '\r') pid=$PID_AFTER"
log "scope: install, foreground service/type, notification-listener binding, screen-cycle lifecycle, crash check"
log "real Geely BLE advertising, official App login and vehicle control still require a physical phone/car"
