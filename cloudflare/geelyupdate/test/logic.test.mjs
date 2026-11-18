import test from 'node:test';
import assert from 'node:assert/strict';
import { parseReleasePath, rewriteManifest } from '../src/index.js';

const versionName = '4.6.1-min';
const versionCode = 601;
const apkName = 'GeelyPassiveKey-v4.6.1-min-601.apk';
const upstream = `https://github.com/yangyongyongyong/GeelyPassiveKey/releases/download/v${versionName}/${apkName}`;

test('parses versioned APK and checksum paths', () => {
  const apk = parseReleasePath(`/releases/${versionName}/${apkName}`);
  assert.equal(apk.kind, 'apk');
  assert.equal(apk.versionName, versionName);
  const sha = parseReleasePath(`/releases/${versionName}/${apkName}.sha256`);
  assert.equal(sha.kind, 'sha256');
});

test('rejects traversal and unversioned paths', () => {
  assert.equal(parseReleasePath('/releases/../evil.apk'), null);
  assert.equal(parseReleasePath('/releases/4.6.1-min/GeelyPassiveKey.apk'), null);
  assert.equal(parseReleasePath(`/releases/${versionName}/${apkName}/extra`), null);
});

test('rewrites valid GitHub manifest to Cloudflare URL', () => {
  const manifest = rewriteManifest({
    versionName,
    versionCode,
    apkName,
    apkUrl: upstream,
    sha256: `sha256:${'a'.repeat(64)}`,
    size: 206129
  }, 'https://geelyupdate.mq.lucadesign.uk');
  assert.equal(manifest.apkUrl, `https://geelyupdate.mq.lucadesign.uk/releases/${versionName}/${apkName}`);
  assert.equal(manifest.versionCode, versionCode);
});

test('rejects foreign manifests and mismatched APK names', () => {
  assert.throws(() => rewriteManifest({
    versionName,
    versionCode,
    apkName,
    apkUrl: `https://evil.example/v${versionName}/${apkName}`,
    sha256: `sha256:${'a'.repeat(64)}`,
    size: 123
  }), /GitHub project/);
  assert.throws(() => rewriteManifest({
    versionName,
    versionCode,
    apkName: 'GeelyPassiveKey-v4.6.1-min-999.apk',
    apkUrl: upstream,
    sha256: `sha256:${'a'.repeat(64)}`,
    size: 123
  }), /versionCode/);
});
