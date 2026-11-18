export const MAX_ARCHIVE_BYTES = 1_800_000;
export const MAX_JSON_BYTES = 2_600_000;
export const MAX_NOTE_CHARS = 800;
export const MIN_NOTE_CHARS = 4;
const INSTALL_ID = /^[A-Za-z0-9][A-Za-z0-9-]{15,63}$/;
const DEVICE_UID = /^GPK-[0-9A-F]{4}(?:-[0-9A-F]{4}){4}$/;

export function jsonResponse(value, status = 200, extraHeaders = {}) {
  return new Response(JSON.stringify(value), {
    status,
    headers: {
      'content-type': 'application/json; charset=utf-8',
      'cache-control': 'no-store',
      ...extraHeaders
    }
  });
}

export function errorResponse(code, message, status = 400, details = undefined) {
  return jsonResponse({ ok: false, code, message, ...(details === undefined ? {} : { details }) }, status);
}

export function sanitizeText(value, maxChars) {
  return String(value ?? '')
    .replace(/[\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F]/g, '')
    .replace(/\s+/g, ' ')
    .trim()
    .slice(0, maxChars);
}

export function validInstallId(value) {
  return typeof value === 'string' && INSTALL_ID.test(value.trim());
}

export function validDeviceUid(value) {
  return typeof value === 'string' && DEVICE_UID.test(value.trim());
}

export function decodeBase64(value) {
  if (typeof value !== 'string' || value.length === 0) throw new Error('archiveBase64 is required');
  const normalized = value.replace(/\s+/g, '');
  if (normalized.length % 4 !== 0 || !/^[A-Za-z0-9+/]*={0,2}$/.test(normalized)) {
    throw new Error('archiveBase64 is invalid');
  }
  let binary;
  try {
    binary = atob(normalized);
  } catch {
    throw new Error('archiveBase64 is invalid');
  }
  const output = new Uint8Array(binary.length);
  for (let index = 0; index < binary.length; index += 1) {
    output[index] = binary.charCodeAt(index);
  }
  return output;
}

export function looksLikeZip(bytes) {
  return bytes instanceof Uint8Array && bytes.length >= 22
    && bytes[0] === 0x50 && bytes[1] === 0x4b
    && (bytes[2] === 0x03 || bytes[2] === 0x05 || bytes[2] === 0x07)
    && (bytes[3] === 0x04 || bytes[3] === 0x06 || bytes[3] === 0x08);
}

export async function sha256Hex(bytes) {
  const digest = await crypto.subtle.digest('SHA-256', bytes);
  return [...new Uint8Array(digest)].map((value) => value.toString(16).padStart(2, '0')).join('');
}

export function bearerToken(request) {
  const header = request.headers.get('authorization') || '';
  const match = /^Bearer\s+(.+)$/i.exec(header);
  return match ? match[1].trim() : '';
}

export function safeArchiveName(value) {
  const name = String(value ?? '').replace(/^.*[\\/]/, '').replace(/[^A-Za-z0-9._-]/g, '-').slice(0, 120);
  return name.toLowerCase().endsWith('.zip') ? name : 'GeelyPassiveKey-logs.zip';
}

export function normalizeLogPayload(raw, maxArchiveBytes = MAX_ARCHIVE_BYTES) {
  if (!raw || typeof raw !== 'object' || Array.isArray(raw)) throw new Error('JSON object is required');
  if (raw.schema !== 1) throw new Error('unsupported schema');
  const installId = sanitizeText(raw.installId, 64);
  if (!validInstallId(installId)) throw new Error('installId is invalid');
  const deviceUid = sanitizeText(raw.deviceUid, 32);
  if (!validDeviceUid(deviceUid)) throw new Error('deviceUid is invalid');
  const note = sanitizeText(raw.note, MAX_NOTE_CHARS);
  if (note.length < MIN_NOTE_CHARS) throw new Error('note must describe the problem in at least 4 characters');
  const archive = decodeBase64(raw.archiveBase64);
  if (archive.length > maxArchiveBytes) throw new Error('archive is too large');
  if (!looksLikeZip(archive)) throw new Error('archive is not a ZIP file');
  return {
    installId,
    deviceUid,
    note,
    appVersion: sanitizeText(raw.appVersion, 40) || 'unknown',
    versionCode: Number.isSafeInteger(Number(raw.versionCode)) ? Number(raw.versionCode) : 0,
    androidRelease: sanitizeText(raw.androidRelease, 20) || 'unknown',
    brand: sanitizeText(raw.brand, 40) || 'unknown',
    model: sanitizeText(raw.model, 60) || 'unknown',
    archiveName: safeArchiveName(raw.archiveName),
    archiveSha256: sanitizeText(raw.archiveSha256, 64).toLowerCase(),
    archive
  };
}
