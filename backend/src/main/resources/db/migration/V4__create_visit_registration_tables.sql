
-- =====================================================
-- V4: VISIT REGISTRATION
-- Quản lý đăng ký thăm và người tham gia
-- =====================================================

-- 1. VISIT_REGISTRATIONS: Thông tin lượt đăng ký
CREATE TABLE visit_registrations (
    id UUID PRIMARY KEY,

    registration_code VARCHAR(50) NOT NULL UNIQUE,

    soldier_id UUID NOT NULL,
    registrant_visitor_id UUID NOT NULL,
    created_by_user_id UUID,

    registration_channel VARCHAR(20) NOT NULL,

    visit_date DATE NOT NULL,
    expected_arrival_time TIME NOT NULL,
    expected_departure_time TIME NOT NULL,

    purpose VARCHAR(500),
    notes TEXT,

    qr_token VARCHAR(100),

    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visit_registrations_soldier
        FOREIGN KEY (soldier_id)
        REFERENCES soldiers(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_registrations_registrant
        FOREIGN KEY (registrant_visitor_id)
        REFERENCES visitors(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_registrations_creator
        FOREIGN KEY (created_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_visit_registration_channel
        CHECK (
            registration_channel IN ('ONLINE', 'MANUAL')
        ),

    CONSTRAINT chk_visit_registration_status
        CHECK (
            status IN ('DRAFT', 'SUBMITTED', 'CANCELLED')
        ),

    CONSTRAINT chk_visit_registration_time
        CHECK (
            expected_departure_time > expected_arrival_time
        )
);

CREATE UNIQUE INDEX uk_visit_registrations_qr_token
    ON visit_registrations(qr_token)
    WHERE qr_token IS NOT NULL;

CREATE INDEX idx_visit_registrations_soldier
    ON visit_registrations(soldier_id);

CREATE INDEX idx_visit_registrations_registrant
    ON visit_registrations(registrant_visitor_id);

CREATE INDEX idx_visit_registrations_date_status
    ON visit_registrations(visit_date, status);

CREATE INDEX idx_visit_registrations_created_by
    ON visit_registrations(created_by_user_id);


-- 2. REGISTRATION_PARTICIPANTS:
-- Danh sách khách tham gia từng lượt đăng ký
CREATE TABLE registration_participants (
    id UUID PRIMARY KEY,

    registration_id UUID NOT NULL,
    visitor_id UUID NOT NULL,

    is_representative BOOLEAN NOT NULL DEFAULT FALSE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    notes VARCHAR(255),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_registration_participants_registration
        FOREIGN KEY (registration_id)
        REFERENCES visit_registrations(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_registration_participants_visitor
        FOREIGN KEY (visitor_id)
        REFERENCES visitors(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_registration_participant
        UNIQUE (registration_id, visitor_id),

    CONSTRAINT chk_registration_participant_status
        CHECK (
            status IN ('ACTIVE', 'CANCELLED')
        )
);

CREATE INDEX idx_registration_participants_registration
    ON registration_participants(registration_id);

CREATE INDEX idx_registration_participants_visitor
    ON registration_participants(visitor_id);

CREATE INDEX idx_registration_participants_status
    ON registration_participants(status);


-- =====================================================
-- END V4
-- =====================================================

