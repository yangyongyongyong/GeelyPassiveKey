import {
  MAX_JSON_BYTES,
  bearerToken,
  errorResponse,
  jsonResponse,
  normalizeLogPayload,
  sanitizeText,
  sha256Hex,
  validDeviceUid
} from './validation.js';

const CORS_HEADERS = {
  'access-control-allow-origin': '*',
  'access-control-allow-methods': 'GET,POST,DELETE,OPTIONS',
  'access-control-allow-headers': 'content-type,authorization'
};

export default {
  async fetch(request, env) {
    const url = new URL(request.url);
    if (request.method === 'OPTIONS') return new Response(null, { status: 204, headers: CORS_HEADERS });
    try {
      if (request.method === 'GET' && url.pathname === '/') {
        return jsonResponse({
          ok: true,
          service: 'geelypassivekey-log',
          health: '/v1/health',
          upload: 'POST /v1/logs'
        }, 200, CORS_HEADERS);
      }
      if (request.method === 'GET' && url.pathname === '/v1/health') {
        const row = await env.DB.prepare('SELECT COUNT(*) AS count FROM log_uploads').first();
        return jsonResponse({ ok: true, service: 'geelypassivekey-log', records: Number(row?.count || 0) }, 200, CORS_HEADERS);
      }
      if (request.method === 'POST' && url.pathname === '/v1/logs') return uploadLog(request, env);
      if (url.pathname.startsWith('/api/')) return adminApi(request, env, url);
      return errorResponse('NOT_FOUND', 'Route not found', 404);
    } catch (error) {
      console.error('request_failed', error instanceof Error ? error.message : String(error));
      return errorResponse('INTERNAL_ERROR', 'Request failed', 500);
    }
  },

  async scheduled(_controller, env) {
    const retentionDays = boundedInt(env.RETENTION_DAYS, 90, 1, 3650);
    const cutoff = Date.now() - retentionDays * 86_400_000;
    const oldLogs = await env.DB.prepare('DELETE FROM log_uploads WHERE created_at < ?').bind(cutoff).run();
    const rateCutoff = utcDay(Date.now() - 14 * 86_400_000);
    await env.DB.batch([
      env.DB.prepare('DELETE FROM device_upload_rates WHERE day < ?').bind(rateCutoff),
      env.DB.prepare('DELETE FROM ip_upload_rates WHERE day < ?').bind(rateCutoff)
    ]);
    console.log('retention_cleanup', { deleted_logs: oldLogs.meta?.changes || 0, retention_days: retentionDays });
  }
};

async function uploadLog(request, env) {
  const contentType = (request.headers.get('content-type') || '').toLowerCase();
  if (!contentType.startsWith('application/json')) {
    return errorResponse('UNSUPPORTED_MEDIA_TYPE', 'Content-Type must be application/json', 415);
  }
  const declaredLength = Number(request.headers.get('content-length') || 0);
  if (!Number.isFinite(declaredLength) || declaredLength <= 0) {
    return errorResponse('CONTENT_LENGTH_REQUIRED', 'Content-Length is required', 411);
  }
  if (declaredLength > MAX_JSON_BYTES) return errorResponse('BODY_TOO_LARGE', 'Request body is too large', 413);
  const bodyText = await request.text();
  if (new TextEncoder().encode(bodyText).length > MAX_JSON_BYTES) {
    return errorResponse('BODY_TOO_LARGE', 'Request body is too large', 413);
  }
  let raw;
  try {
    raw = JSON.parse(bodyText);
  } catch {
    return errorResponse('INVALID_JSON', 'Request body must be JSON');
  }
  let payload;
  try {
    payload = normalizeLogPayload(raw, boundedInt(env.MAX_ARCHIVE_BYTES, 1_800_000, 100_000, 1_800_000));
  } catch (error) {
    return errorResponse('INVALID_LOG_PACKAGE', error instanceof Error ? error.message : 'Invalid log package');
  }

  const now = Date.now();
  const day = utcDay(now);
  const ip = sanitizeText(request.headers.get('cf-connecting-ip'), 80) || 'unknown';
  const ipHash = (await sha256Hex(new TextEncoder().encode(`geelylog:${ip}`))).slice(0, 32);
  const deviceLimit = boundedInt(env.DEVICE_DAILY_LIMIT, 10, 1, 100);
  const ipLimit = boundedInt(env.IP_DAILY_LIMIT, 100, 1, 1000);
  const rate = await incrementRates(env.DB, payload.deviceUid, ipHash, day, now);
  if (rate.device > deviceLimit) {
    return errorResponse('DEVICE_RATE_LIMITED', `Each device UID can upload at most ${deviceLimit} logs per day`, 429);
  }
  if (rate.ip > ipLimit) {
    return errorResponse('IP_RATE_LIMITED', `This network can upload at most ${ipLimit} logs per day`, 429);
  }

  const actualSha256 = await sha256Hex(payload.archive);
  if (payload.archiveSha256 && payload.archiveSha256 !== actualSha256) {
    return errorResponse('CHECKSUM_MISMATCH', 'archiveSha256 does not match archiveBase64');
  }
  const id = crypto.randomUUID();
  const country = sanitizeText(request.cf?.country, 8);
  const archiveBuffer = payload.archive.buffer.slice(
    payload.archive.byteOffset,
    payload.archive.byteOffset + payload.archive.byteLength
  );

  await env.DB.prepare(`
    INSERT INTO log_uploads (
      id, install_id, device_uid, created_at, app_version, version_code, android_release,
      brand, model, note, archive_name, archive_size, archive_sha256, archive, ip_hash, country
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `).bind(
    id,
    payload.installId,
    payload.deviceUid,
    now,
    payload.appVersion,
    payload.versionCode,
    payload.androidRelease,
    payload.brand,
    payload.model,
    payload.note,
    payload.archiveName,
    payload.archive.length,
    actualSha256,
    archiveBuffer,
    ipHash,
    country
  ).run();

  console.log('log_uploaded', {
    id,
    install_id: payload.installId,
    device_uid: payload.deviceUid,
    bytes: payload.archive.length,
    sha256: actualSha256,
    version: payload.appVersion,
    country
  });
  return jsonResponse({
    ok: true,
    logId: id,
    deviceUid: payload.deviceUid,
    uploadedAt: now,
    bytes: payload.archive.length,
    sha256: actualSha256,
    retentionDays: boundedInt(env.RETENTION_DAYS, 90, 1, 3650)
  }, 201, CORS_HEADERS);
}

async function incrementRates(db, deviceUid, ipHash, day, now) {
  await db.batch([
    db.prepare(`
      INSERT INTO device_upload_rates (device_uid, day, count, updated_at)
      VALUES (?, ?, 1, ?)
      ON CONFLICT(device_uid, day) DO UPDATE SET count = count + 1, updated_at = excluded.updated_at
    `).bind(deviceUid, day, now),
    db.prepare(`
      INSERT INTO ip_upload_rates (ip_hash, day, count, updated_at)
      VALUES (?, ?, 1, ?)
      ON CONFLICT(ip_hash, day) DO UPDATE SET count = count + 1, updated_at = excluded.updated_at
    `).bind(ipHash, day, now)
  ]);
  const results = await db.batch([
    db.prepare('SELECT count FROM device_upload_rates WHERE device_uid = ? AND day = ?').bind(deviceUid, day),
    db.prepare('SELECT count FROM ip_upload_rates WHERE ip_hash = ? AND day = ?').bind(ipHash, day)
  ]);
  return {
    device: Number(results[0]?.results?.[0]?.count || 0),
    ip: Number(results[1]?.results?.[0]?.count || 0)
  };
}

async function adminApi(request, env, url) {
  const expected = env.ADMIN_TOKEN || '';
  if (!expected || !constantTimeEqual(bearerToken(request), expected)) {
    return errorResponse('UNAUTHORIZED', 'Admin bearer token is required', 401);
  }
  if (request.method === 'GET' && url.pathname === '/api/logs') {
    const limit = Math.max(1, Math.min(100, Number(url.searchParams.get('limit') || 30)));
    const uid = sanitizeText(url.searchParams.get('uid'), 32).toUpperCase();
    if (uid && !validDeviceUid(uid)) return errorResponse('INVALID_UID', 'uid filter is invalid');
    const rows = uid
      ? await env.DB.prepare(`
          SELECT id, install_id, device_uid, created_at, app_version, version_code, android_release,
                 brand, model, note, archive_name, archive_size, archive_sha256, country
          FROM log_uploads
          WHERE device_uid = ?
          ORDER BY created_at DESC
          LIMIT ?
        `).bind(uid, limit).all()
      : await env.DB.prepare(`
          SELECT id, install_id, device_uid, created_at, app_version, version_code, android_release,
                 brand, model, note, archive_name, archive_size, archive_sha256, country
          FROM log_uploads
          ORDER BY created_at DESC
          LIMIT ?
        `).bind(limit).all();
    return jsonResponse({ ok: true, logs: (rows.results || []).map(adminRow) }, 200, CORS_HEADERS);
  }
  if (request.method === 'GET' && url.pathname === '/api/stats') {
    const row = await env.DB.prepare(`
      SELECT COUNT(*) AS count, COALESCE(SUM(archive_size), 0) AS bytes, MAX(created_at) AS latest_at
      FROM log_uploads
    `).first();
    return jsonResponse({
      ok: true,
      count: Number(row?.count || 0),
      bytes: Number(row?.bytes || 0),
      latestAt: Number(row?.latest_at || 0)
    }, 200, CORS_HEADERS);
  }
  const download = /^\/api\/logs\/([0-9a-f-]{36})\/download$/i.exec(url.pathname);
  if (request.method === 'GET' && download) {
    const row = await env.DB.prepare('SELECT archive_name, archive FROM log_uploads WHERE id = ?')
      .bind(download[1]).first();
    if (!row) return errorResponse('NOT_FOUND', 'Log not found', 404);
    const archive = blobBytes(row.archive);
    return new Response(archive, {
      status: 200,
      headers: {
        ...CORS_HEADERS,
        'content-type': 'application/zip',
        'content-length': String(archive.byteLength),
        'content-disposition': `attachment; filename="${sanitizeText(row.archive_name, 120) || 'log.zip'}"`,
        'cache-control': 'no-store'
      }
    });
  }
  const remove = /^\/api\/logs\/([0-9a-f-]{36})$/i.exec(url.pathname);
  if (request.method === 'DELETE' && remove) {
    const result = await env.DB.prepare('DELETE FROM log_uploads WHERE id = ?').bind(remove[1]).run();
    return jsonResponse({ ok: true, deleted: Number(result.meta?.changes || 0) }, 200, CORS_HEADERS);
  }
  return errorResponse('NOT_FOUND', 'Admin route not found', 404);
}


function blobBytes(value) {
  if (value instanceof ArrayBuffer) return new Uint8Array(value);
  if (ArrayBuffer.isView(value)) return new Uint8Array(value.buffer, value.byteOffset, value.byteLength);
  if (Array.isArray(value)) return Uint8Array.from(value);
  if (typeof value === 'string') {
    const bytes = new Uint8Array(value.length);
    for (let index = 0; index < value.length; index += 1) bytes[index] = value.charCodeAt(index) & 0xff;
    return bytes;
  }
  throw new TypeError(`Unsupported D1 BLOB value: ${typeof value}`);
}

function adminRow(row) {
  return {
    id: row.id,
    installId: row.install_id,
    deviceUid: row.device_uid,
    createdAt: Number(row.created_at),
    appVersion: row.app_version,
    versionCode: Number(row.version_code),
    androidRelease: row.android_release,
    brand: row.brand,
    model: row.model,
    note: row.note,
    archiveName: row.archive_name,
    archiveSize: Number(row.archive_size),
    sha256: row.archive_sha256,
    country: row.country
  };
}

function constantTimeEqual(left, right) {
  const a = String(left || '');
  const b = String(right || '');
  let difference = a.length ^ b.length;
  const length = Math.max(a.length, b.length);
  for (let index = 0; index < length; index += 1) {
    difference |= (a.charCodeAt(index) || 0) ^ (b.charCodeAt(index) || 0);
  }
  return difference === 0;
}

function boundedInt(value, fallback, min, max) {
  const parsed = Number.parseInt(String(value ?? ''), 10);
  if (!Number.isFinite(parsed)) return fallback;
  return Math.max(min, Math.min(max, parsed));
}

function utcDay(timestamp) {
  return new Date(timestamp).toISOString().slice(0, 10);
}
