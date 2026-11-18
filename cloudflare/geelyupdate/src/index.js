const ORIGIN = 'https://geelyupdate.mq.lucadesign.uk';
const GITHUB_REPO = 'yangyongyongyong/GeelyPassiveKey';
const GITHUB_LATEST_MANIFEST = `https://github.com/${GITHUB_REPO}/releases/latest/download/update.json`;
const GITHUB_RELEASE_PREFIX = `https://github.com/${GITHUB_REPO}/releases/download/`;
const APK_CONTENT_TYPE = 'application/vnd.android.package-archive';
const APK_PATTERN = /^GeelyPassiveKey-v[A-Za-z0-9._-]{1,80}-\d+\.apk$/;
const FALLBACK_ETAG = 'W/"geelyupdate-unavailable"';

export default {
  async fetch(request, _env, ctx) {
    const url = new URL(request.url);
    if (request.method !== 'GET' && request.method !== 'HEAD') {
      return json({ ok: false, code: 'METHOD_NOT_ALLOWED', message: 'GET or HEAD required' }, 405);
    }
    if (url.pathname === '/') {
      return json({
        ok: true,
        service: 'geelypassivekey-update',
        mode: 'github-release-edge-cache',
        manifest: '/update.json',
        releases: '/releases/<versionName>/<apkName>',
        health: '/v1/health'
      });
    }
    if (url.pathname === '/v1/health') {
      return json({ ok: true, service: 'geelypassivekey-update', mode: 'github-release-edge-cache', r2: false });
    }
    if (url.pathname === '/update.json') return serveManifest(request, ctx);
    const route = parseReleasePath(url.pathname);
    if (route) return serveRelease(request, ctx, route);
    return json({ ok: false, code: 'NOT_FOUND', message: 'Object not found' }, 404);
  }
};

export function parseReleasePath(pathname) {
  const apk = /^\/releases\/([A-Za-z0-9._-]{1,40})\/(GeelyPassiveKey-v[A-Za-z0-9._-]{1,80}-\d+\.apk)$/.exec(pathname || '');
  if (apk) return { kind: 'apk', versionName: apk[1], name: apk[2] };
  const sha = /^\/releases\/([A-Za-z0-9._-]{1,40})\/(GeelyPassiveKey-v[A-Za-z0-9._-]{1,80}-\d+\.apk\.sha256)$/.exec(pathname || '');
  if (sha) return { kind: 'sha256', versionName: sha[1], name: sha[2] };
  return null;
}

export function rewriteManifest(raw, origin = ORIGIN) {
  if (!raw || typeof raw !== 'object') throw new Error('manifest is not an object');
  const versionName = String(raw.versionName || '');
  const versionCode = Number(raw.versionCode || 0);
  const apkName = String(raw.apkName || '');
  const upstreamUrl = String(raw.apkUrl || '');
  const sha256 = String(raw.sha256 || '');
  const size = Number(raw.size || 0);
  if (!/^[A-Za-z0-9._-]{1,40}$/.test(versionName)) throw new Error('invalid versionName');
  if (!Number.isSafeInteger(versionCode) || versionCode <= 0) throw new Error('invalid versionCode');
  if (!APK_PATTERN.test(apkName)) throw new Error('invalid apkName');
  if (!apkName.includes(`-${versionCode}.apk`)) throw new Error('apkName versionCode mismatch');
  if (!upstreamUrl.startsWith(GITHUB_RELEASE_PREFIX + `v${versionName}/`)) {
    throw new Error('upstream apkUrl is not this GitHub project');
  }
  if (!sha256.toLowerCase().startsWith('sha256:') || !/^sha256:[0-9a-f]{64}$/i.test(sha256)) {
    throw new Error('invalid sha256');
  }
  if (!Number.isSafeInteger(size) || size <= 0) throw new Error('invalid size');
  return {
    versionName,
    versionCode,
    apkName,
    apkUrl: `${origin}/releases/${versionName}/${apkName}`,
    sha256: sha256.toLowerCase(),
    size
  };
}

async function serveManifest(request, ctx) {
  const cache = caches.default;
  const cacheKey = new Request(`${ORIGIN}/update.json`, { method: 'GET' });
  const forceRefresh = new URL(request.url).searchParams.has('refresh');
  let response = forceRefresh ? undefined : await cacheMatch(cache, cacheKey);
  if (!response) {
    let upstream;
    try {
      upstream = await fetch(GITHUB_LATEST_MANIFEST, {
        headers: { 'user-agent': 'GeelyPassiveKey-update-worker/1.0', accept: 'application/json' },
        cf: { cacheTtl: 60, cacheEverything: true }
      });
    } catch (error) {
      console.error('manifest_fetch_failed', toString(error));
      return json({ ok: false, code: 'UPSTREAM_UNAVAILABLE', message: 'GitHub manifest unavailable' }, 503);
    }
    if (!upstream.ok) {
      return json({ ok: false, code: 'UPSTREAM_ERROR', message: `GitHub manifest returned ${upstream.status}` }, 502);
    }
    let manifest;
    try {
      manifest = rewriteManifest(await upstream.json(), new URL(request.url).origin);
    } catch (error) {
      console.error('manifest_invalid', toString(error));
      return json({ ok: false, code: 'INVALID_MANIFEST', message: 'Upstream update.json is invalid' }, 502);
    }
    const body = JSON.stringify(manifest, null, 2);
    const etag = await etagFor(body);
    response = new Response(body, {
      status: 200,
      headers: {
        'content-type': 'application/json; charset=utf-8',
        'cache-control': 'public, max-age=300, must-revalidate',
        etag,
        'x-content-type-options': 'nosniff',
        'x-robots-tag': 'noindex',
        'access-control-allow-origin': '*'
      }
    });
    cachePut(ctx, cache, cacheKey, response);
  }
  return conditionalResponse(request, response);
}

async function serveRelease(request, ctx, route) {
  if (route.kind === 'apk' && !APK_PATTERN.test(route.name)) {
    return json({ ok: false, code: 'INVALID_APK', message: 'Invalid APK name' }, 400);
  }
  const cache = caches.default;
  const url = new URL(request.url);
  const cacheKey = new Request(`${url.origin}${url.pathname}`, { method: 'GET' });
  let response = await cacheMatch(cache, cacheKey);
  if (!response) {
    const upstreamUrl = `${GITHUB_RELEASE_PREFIX}v${route.versionName}/${route.name}`;
    let upstream;
    try {
      upstream = await fetch(upstreamUrl, {
        method: request.method,
        headers: { 'user-agent': 'GeelyPassiveKey-update-worker/1.0' },
        redirect: 'follow'
      });
    } catch (error) {
      console.error('release_fetch_failed', { url: upstreamUrl, error: toString(error) });
      return json({ ok: false, code: 'UPSTREAM_UNAVAILABLE', message: 'GitHub release unavailable' }, 503);
    }
    if (!upstream.ok) {
      return json({ ok: false, code: 'UPSTREAM_ERROR', message: `GitHub release returned ${upstream.status}` }, route.kind === 'apk' ? 502 : 404);
    }
    const contentType = route.kind === 'apk' ? APK_CONTENT_TYPE : 'text/plain; charset=utf-8';
    const cacheSeconds = route.kind === 'apk' ? 31_536_000 : 86_400;
    const headers = new Headers({
      'content-type': contentType,
      'cache-control': `public, max-age=${cacheSeconds}, immutable`,
      etag: await etagFor(`${route.versionName}|${route.name}|${upstream.headers.get('etag') || ''}`),
      'x-content-type-options': 'nosniff',
      'x-robots-tag': 'noindex',
      'access-control-allow-origin': '*'
    });
    const length = upstream.headers.get('content-length');
    if (length) headers.set('content-length', length);
    if (route.kind === 'apk') headers.set('content-disposition', `attachment; filename="${route.name}"`);
    response = new Response(request.method === 'HEAD' ? null : upstream.body, { status: 200, headers });
    if (request.method === 'GET') cachePut(ctx, cache, cacheKey, response);
  }
  return conditionalResponse(request, response);
}

async function cacheMatch(cache, key) {
  try {
    return await cache.match(key);
  } catch (error) {
    console.error('cache_match_failed', toString(error));
    return undefined;
  }
}

function cachePut(ctx, cache, key, response) {
  try {
    ctx.waitUntil(cache.put(key, response.clone()).catch((error) => {
      console.error('cache_put_failed', toString(error));
    }));
  } catch (error) {
    console.error('cache_schedule_failed', toString(error));
  }
}

function conditionalResponse(request, response) {
  const etag = response.headers.get('etag') || FALLBACK_ETAG;
  if (request.headers.get('if-none-match') === etag) {
    return new Response(null, {
      status: 304,
      headers: {
        etag,
        'cache-control': response.headers.get('cache-control') || 'public, max-age=300'
      }
    });
  }
  if (request.method === 'HEAD') {
    return new Response(null, { status: response.status, headers: response.headers });
  }
  return response;
}

async function etagFor(value) {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(value));
  const hex = [...new Uint8Array(digest)].map((byte) => byte.toString(16).padStart(2, '0')).join('');
  return `"gpk-${hex.slice(0, 24)}"`;
}

function toString(error) {
  return error instanceof Error ? error.message : String(error);
}

function json(value, status = 200) {
  return new Response(JSON.stringify(value), {
    status,
    headers: {
      'content-type': 'application/json; charset=utf-8',
      'cache-control': 'no-store',
      'x-content-type-options': 'nosniff',
      'x-robots-tag': 'noindex'
    }
  });
}
