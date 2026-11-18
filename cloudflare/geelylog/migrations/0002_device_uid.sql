ALTER TABLE log_uploads ADD COLUMN device_uid TEXT NOT NULL DEFAULT '';

CREATE INDEX IF NOT EXISTS idx_log_uploads_device_uid
  ON log_uploads(device_uid, created_at DESC);

CREATE TABLE IF NOT EXISTS device_upload_rates (
  device_uid TEXT NOT NULL,
  day TEXT NOT NULL,
  count INTEGER NOT NULL,
  updated_at INTEGER NOT NULL,
  PRIMARY KEY (device_uid, day)
);

CREATE INDEX IF NOT EXISTS idx_device_upload_rates_day
  ON device_upload_rates(day);
