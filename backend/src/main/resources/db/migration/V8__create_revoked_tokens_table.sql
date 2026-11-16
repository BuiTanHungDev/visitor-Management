
CREATE TABLE revoked_tokens (
    jti UUID PRIMARY KEY,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_revoked_tokens_expires_at
    ON revoked_tokens (expires_at);
