CREATE TYPE VERIFICATION_STATUS AS ENUM ('TO_PROCESS', 'IN_PROGRESS', 'VERIFIED', 'VERIFICATION_FAILED');
CREATE TYPE CONTACT_TYPE AS ENUM ('EMAIL', 'MOBILE', 'URL', 'LANDLINE');
CREATE TYPE KYC_DOC_TYPE AS ENUM ('AADHAAR', 'DRIVING_LICENSE', 'PAN', 'PASSPORT', 'VOTER_ID');

CREATE TABLE IF NOT EXISTS "country" (
  id   SERIAL      NOT NULL PRIMARY KEY,
  name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "state" (
  id         SERIAL      NOT NULL PRIMARY KEY,
  country_id INTEGER     NOT NULL,
  name       VARCHAR(30) NOT NULL UNIQUE
);

CREATE INDEX idx_state_gin
  ON "state" USING GIN ("name", gin_trgm_ops);
CREATE INDEX idx_state
  ON "state" USING BTREE ("name");

CREATE TABLE IF NOT EXISTS "city" (
  id              SERIAL      NOT NULL PRIMARY KEY,
  name            VARCHAR(30) NOT NULL UNIQUE,
  state_id        INTEGER     NOT NULL,
  country_id      INTEGER     NOT NULL,
  is_active       BOOLEAN     NOT NULL         DEFAULT FALSE,
  created_at      TIMESTAMPTZ NOT NULL         DEFAULT (now() AT TIME ZONE 'UTC'),
  last_updated_at TIMESTAMPTZ NOT NULL         DEFAULT (now() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_city_gin
  ON "city" USING GIN ("name", gin_trgm_ops);

CREATE INDEX idx_city
  ON "city" USING BTREE (name);

CREATE TABLE IF NOT EXISTS "pincode" (
  id              SERIAL      NOT NULL PRIMARY KEY,
  code            VARCHAR(9),
  city_id         INTEGER     NOT NULL,
  state_id        INTEGER     NOT NULL,
  country_id      INTEGER     NOT NULL,
  is_active       BOOLEAN     NOT NULL         DEFAULT FALSE,
  created_at      TIMESTAMPTZ NOT NULL         DEFAULT (now() AT TIME ZONE 'UTC'),
  last_updated_at TIMESTAMPTZ NOT NULL         DEFAULT (now() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_pincode
  ON "pincode" USING BTREE (code, is_active);

CREATE TABLE IF NOT EXISTS "user" (
  id                        SERIAL                         NOT NULL PRIMARY KEY,
  username                  VARCHAR(32) UNIQUE             NOT NULL,
  primary_email             TEXT UNIQUE                    NOT NULL,
  primary_cell_number       VARCHAR(16),
  email_verification_status VERIFICATION_STATUS            NOT NULL DEFAULT 'NOT_VERIFIED',
  cell_verification_status  VERIFICATION_STATUS            NOT NULL DEFAULT 'NOT_VERIFIED',
  PASSWORD                  TEXT                           NOT NULL,
  is_active                 BOOLEAN DEFAULT TRUE           NOT NULL,
  created_at                TIMESTAMPTZ                    NOT NULL DEFAULT (now() AT TIME ZONE 'UTC'),
  last_updated_at           TIMESTAMPTZ                    NOT NULL DEFAULT (now() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_username
  ON "user" USING BTREE (username, is_active);
CREATE INDEX idx_primary_email
  ON "user" USING BTREE (primary_email, is_active);
CREATE INDEX idx_primary_cell
  ON "user" USING BTREE (primary_cell_number, is_active);

CREATE TABLE IF NOT EXISTS "address" (
  id              SERIAL               NOT NULL PRIMARY KEY,
  user_id         INTEGER              NOT NULL,
  city_id         INTEGER              NOT NULL,
  state_id        INTEGER              NOT NULL,
  line1           VARCHAR(32)          NOT NULL,
  line2           VARCHAR(32)          NOT NULL,
  line3           VARCHAR(32),
  landmark        VARCHAR(32),
  country         VARCHAR(32)          NOT NULL,
  pincode         INTEGER              NOT NULL,
  is_active       BOOLEAN DEFAULT TRUE NOT NULL,
  created_at      TIMESTAMPTZ          NOT NULL DEFAULT (now() AT TIME ZONE 'UTC'),
  last_updated_at TIMESTAMPTZ          NOT NULL DEFAULT (now() AT TIME ZONE 'UTC')
);

CREATE INDEX idx_user_id
  ON "address" (user_id, is_active);

CREATE TABLE IF NOT EXISTS "contact" (
  id              SERIAL UNIQUE              NOT NULL,
  user_id         INTEGER                    NOT NULL,
  contact         VARCHAR(32) UNIQUE         NOT NULL,
  contact_type    CONTACT_TYPE               NOT NULL,
  is_active       BOOLEAN DEFAULT TRUE       NOT NULL,
  created_at      TIMESTAMPTZ                NOT NULL DEFAULT (now() AT TIME ZONE 'UTC'),
  last_updated_at TIMESTAMPTZ                NOT NULL DEFAULT (now() AT TIME ZONE 'UTC')
);
