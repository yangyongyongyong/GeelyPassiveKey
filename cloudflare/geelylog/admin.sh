#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
BASE_URL="${GEELY_LOG_BASE_URL:-https://geelylog.mq.lucadesign.uk}"
TOKEN="${ADMIN_TOKEN:-}"
if [ -z "$TOKEN" ] && [ -f "$ROOT/.dev.vars" ]; then
  TOKEN="$(sed -n 's/^ADMIN_TOKEN=//p' "$ROOT/.dev.vars" | head -1)"
fi
if [ -z "$TOKEN" ]; then
  echo "缺少 ADMIN_TOKEN：设置环境变量或写入 cloudflare/geelylog/.dev.vars" >&2
  exit 2
fi
command="${1:-stats}"
case "$command" in
  stats)
    curl -fsS --max-time 30 -A 'GeelyPassiveKey-admin' \
      -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/stats"
    printf '\n'
    ;;
  list)
    limit="${2:-30}"
    uid="${3:-}"
    query="limit=$limit"
    if [ -n "$uid" ]; then
      query="$query&uid=$uid"
    fi
    curl -fsS --max-time 30 -A 'GeelyPassiveKey-admin' \
      -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/logs?$query"
    printf '\n'
    ;;
  download)
    id="${2:?用法: admin.sh download <LOG_ID> [OUTPUT.zip]}"
    output="${3:-GeelyPassiveKey-$id.zip}"
    curl -fsS --max-time 60 -A 'GeelyPassiveKey-admin' \
      -H "Authorization: Bearer $TOKEN" \
      "$BASE_URL/api/logs/$id/download" -o "$output"
    printf 'saved=%s\n' "$output"
    ;;
  delete)
    id="${2:?用法: admin.sh delete <LOG_ID>}"
    curl -fsS --max-time 30 -A 'GeelyPassiveKey-admin' \
      -H "Authorization: Bearer $TOKEN" -X DELETE "$BASE_URL/api/logs/$id"
    printf '\n'
    ;;
  *)
    echo "用法: admin.sh {stats|list [limit] [UID]|download <id> [out.zip]|delete <id>}" >&2
    exit 1
    ;;
esac
