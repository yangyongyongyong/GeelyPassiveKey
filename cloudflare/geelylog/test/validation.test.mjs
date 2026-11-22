import test from 'node:test';
import assert from 'node:assert/strict';
import {
  MAX_ARCHIVE_BYTES,
  decodeBase64,
  looksLikeZip,
  normalizeLogPayload,
  safeArchiveName,
  sanitizeText,
  sha256Hex,
  validDeviceUid,
  validInstallId
} from '../src/validation.js';

const zipBytes = Uint8Array.from([0x50, 0x4b, 0x03, 0x04, ...new Uint8Array(64)]);
const zipBase64 = Buffer.from(zipBytes).toString('base64');

test('validates install ids', () => {
  assert.equal(validInstallId('12345678-1234-1234-1234-123456789abc'), true);
  assert.equal(validInstallId('short'), false);
  assert.equal(validInstallId('contains spaces 12345678'), false);
  assert.equal(validDeviceUid('GPK-ABCD-EF01-2345-6789-ABCD'), true);
  assert.equal(validDeviceUid('GPK-ABCD'), false);
});

test('normalizes and verifies a zip payload', async () => {
  const payload = normalizeLogPayload({
    schema: 1,
    installId: '12345678-1234-1234-1234-123456789abc',
    deviceUid: 'GPK-ABCD-EF01-2345-6789-ABCD',
    note: '  靠近未解锁  ',
    appVersion: '4.5.9-min',
    versionCode: 509,
    androidRelease: '16',
    brand: 'vivo',
    model: 'V2426A',
    archiveName: '../../危险 日志.zip',
    archiveBase64: zipBase64
  });
  assert.equal(payload.note, '靠近未解锁');
  assert.equal(payload.deviceUid, 'GPK-ABCD-EF01-2345-6789-ABCD');
  assert.equal(payload.archiveName, '-----.zip');
  assert.equal(payload.appVersion, '4.5.9-min');
  assert.equal(payload.versionCode, 509);
  assert.equal(payload.archive.length, zipBytes.length);
  assert.match(await sha256Hex(payload.archive), /^[0-9a-f]{64}$/);
});

test('rejects malformed and oversized archives', () => {
  const base = {
    schema: 1,
    installId: '12345678-1234-1234-1234-123456789abc',
    deviceUid: 'GPK-ABCD-EF01-2345-6789-ABCD',
    note: '测试异常现象',
    appVersion: '4.6.7-min',
    versionCode: 607,
    archiveBase64: Buffer.from('not-a-zip-but-long-enough').toString('base64')
  };
  assert.throws(() => normalizeLogPayload(base), /not a ZIP/);
  assert.throws(() => normalizeLogPayload({ ...base, deviceUid: 'BAD' }), /deviceUid/);
  assert.throws(() => normalizeLogPayload({ ...base, note: '' }), /note/);
  assert.throws(() => normalizeLogPayload({ ...base, archiveBase64: '***' }), /invalid/);
  assert.throws(() => normalizeLogPayload({ ...base, archiveBase64: zipBase64, appVersion: '', versionCode: 0 }),
    /appVersion and versionCode/);
  const oversized = new Uint8Array(MAX_ARCHIVE_BYTES + 1);
  oversized.set([0x50, 0x4b, 0x03, 0x04]);
  assert.throws(() => normalizeLogPayload({
    ...base,
    archiveBase64: Buffer.from(oversized).toString('base64')
  }), /too large/);
});

test('decodes base64 and recognizes zip signatures', () => {
  assert.equal(looksLikeZip(decodeBase64(zipBase64)), true);
  assert.equal(looksLikeZip(Uint8Array.from([1, 2, 3])), false);
});

test('sanitizes note and archive names', () => {
  assert.equal(sanitizeText('a\u0000b\n c', 20), 'ab c');
  assert.equal(safeArchiveName('GeelyPassiveKey-logs.zip'), 'GeelyPassiveKey-logs.zip');
  assert.equal(safeArchiveName('../../evil.sh'), 'GeelyPassiveKey-logs.zip');
  assert.equal(safeArchiveName('../../evil.zip'), 'evil.zip');
});
