CREATE TABLE IF NOT EXISTS log_uploads (
  id TEXT PRIMARY KEY,
  install_id TEXT NOT NULL,
  created_at INTEGER NOT NULL,
  app_version TEXT NOT NULL,
  version_code INTEGER NOT NULL,
  android_release TEXT NOT NULL,
  brand TEXT NOT NULL,
  model TEXT NOT NULL,
  note TEXT NOT NULL DEFAULT '',
  archive_name TEXT NOT NULL,
  archive_size INTEGER NOT NULL,
  archive_sha256 TEXT NOT NULL,
  archive BLOB NOT NULL,
  ip_hash TEXT NOT NULL,
  country TEXT NOT NULL DEFAULT ''
);

CREATE INDEX IF NOT EXISTS idx_log_uploads_created_at
  ON log_uploads(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_log_uploads_install_id
  ON log_uploads(install_id, created_at DESC);

CREATE TABLE IF NOT EXISTS install_upload_rates (
  install_id TEXT NOT NULL,
  day TEXT NOT NULL,
  count INTEGER NOT NULL,
  updated_at INTEGER NOT NULL,
  PRIMARY KEY (install_id, day)
);

CREATE TABLE IF NOT EXISTS ip_upload_rates (
  ip_hash TEXT NOT NULL,
  day TEXT NOT NULL,
  count INTEGER NOT NULL,
  updated_at INTEGER NOT NULL,
  PRIMARY KEY (ip_hash, day)
);

CREATE INDEX IF NOT EXISTS idx_install_upload_rates_day
  ON install_upload_rates(day);

CREATE INDEX IF NOT EXISTS idx_ip_upload_rates_day
  ON ip_upload_rates(day);
