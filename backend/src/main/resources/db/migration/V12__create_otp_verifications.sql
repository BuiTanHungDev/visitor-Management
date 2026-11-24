
-- =====================================================
-- V12: Create OTP verification table
-- =====================================================

CREATE TABLE otp_verifications (
    id UUID PRIMARY KEY,

    purpose VARCHAR(30) NOT NULL,

    channel VARCHAR(10) NOT NULL,

    destination VARCHAR(255) NOT NULL,

    code_hash VARCHAR(64) NOT NULL,

    expires_at TIMESTAMPTZ NOT NULL,

    attempts INTEGER NOT NULL DEFAULT 0,

    max_attempts INTEGER NOT NULL DEFAULT 5,

    sent_at TIMESTAMPTZ NOT NULL,

    verified_at TIMESTAMPTZ,

    consumed_at TIMESTAMPTZ,

    provider_message_id VARCHAR(100),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_otp_channel
        CHECK (channel IN ('EMAIL', 'SMS')),

    CONSTRAINT chk_otp_purpose
        CHECK (purpose IN ('REGISTRATION')),

    CONSTRAINT chk_otp_attempts
        CHECK (attempts >= 0 AND max_attempts > 0),

    CONSTRAINT chk_otp_expiration
        CHECK (expires_at > created_at OR verified_at IS NOT NULL)
);

-- Search OTP requests by destination and purpose.
CREATE INDEX idx_otp_destination_purpose_created
    ON otp_verifications (
        destination,
        purpose,
        created_at DESC
    );

-- Support cleanup of expired OTP records.
CREATE INDEX idx_otp_expires_at
    ON otp_verifications (expires_at);
