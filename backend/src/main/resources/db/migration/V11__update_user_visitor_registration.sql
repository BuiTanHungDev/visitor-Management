
-- =====================================================
-- V11: Update User and Visitor for Family Registration
-- =====================================================

-- 1. Allow username to be NULL.
-- Family members can authenticate using email or phone.
-- Existing staff accounts retain their current usernames.

ALTER TABLE users
    ALTER COLUMN username DROP NOT NULL;


-- 2. Add contact verification timestamps.

ALTER TABLE users
    ADD COLUMN email_verified_at TIMESTAMPTZ,
    ADD COLUMN phone_verified_at TIMESTAMPTZ;


-- 3. Enforce unique phone numbers after normalization.
-- Ignore formatting characters such as spaces, dots and hyphens.
-- Existing data has been checked for normalized duplicates.

CREATE UNIQUE INDEX uk_users_phone_normalized
    ON users (
        regexp_replace(phone, '[^0-9]', '', 'g')
    )
    WHERE phone IS NOT NULL;


-- 4. Link a visitor profile to its login account.
-- Nullable: existing visitor profiles do not need an account.
-- Unique: one user can be linked to at most one visitor.
-- ON DELETE SET NULL: preserve visitor profile if user is deleted.

ALTER TABLE visitors
    ADD COLUMN user_id UUID;

ALTER TABLE visitors
    ADD CONSTRAINT uk_visitors_user_id
        UNIQUE (user_id);

ALTER TABLE visitors
    ADD CONSTRAINT fk_visitors_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL;
