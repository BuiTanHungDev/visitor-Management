
-- =====================================================
-- V6: VISIT EXECUTION AND GATE MANAGEMENT
-- Quản lý quá trình thăm, xác minh và ra/vào cổng
-- =====================================================


-- 1. VISIT_SESSIONS
-- Phiên thăm thực tế

CREATE TABLE visit_sessions (
    id UUID PRIMARY KEY,

    registration_id UUID NOT NULL UNIQUE,

    visit_date DATE NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',

    opened_by_user_id UUID,
    opened_at TIMESTAMPTZ,

    completed_at TIMESTAMPTZ,
    cancelled_at TIMESTAMPTZ,

    notes TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visit_sessions_registration
        FOREIGN KEY (registration_id)
        REFERENCES visit_registrations(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_sessions_opened_by
        FOREIGN KEY (opened_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_visit_sessions_status
        CHECK (
            status IN (
                'PLANNED',
                'IN_PROGRESS',
                'AWAITING_UNIT_CONFIRMATION',
                'AWAITING_EXIT',
                'COMPLETED',
                'CANCELLED',
                'INTERRUPTED'
            )
        ),

    CONSTRAINT chk_visit_sessions_completion
        CHECK (
            (status = 'COMPLETED' AND completed_at IS NOT NULL)
            OR
            (status <> 'COMPLETED' AND completed_at IS NULL)
        )
);

CREATE INDEX idx_visit_sessions_date
    ON visit_sessions(visit_date);

CREATE INDEX idx_visit_sessions_status
    ON visit_sessions(status);


-- 2. VISIT_PARTICIPANT_RECORDS
-- Theo dõi riêng từng khách trong phiên thăm

CREATE TABLE visit_participant_records (
    id UUID PRIMARY KEY,

    session_id UUID NOT NULL,
    registration_participant_id UUID NOT NULL UNIQUE,

    status VARCHAR(20) NOT NULL DEFAULT 'EXPECTED',

    entry_at TIMESTAMPTZ,
    entry_verified_by_user_id UUID,

    exit_at TIMESTAMPTZ,
    exit_verified_by_user_id UUID,

    denial_reason TEXT,
    notes TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visit_participant_records_session
        FOREIGN KEY (session_id)
        REFERENCES visit_sessions(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_participant_records_participant
        FOREIGN KEY (registration_participant_id)
        REFERENCES registration_participants(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_participant_records_entry_user
        FOREIGN KEY (entry_verified_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_visit_participant_records_exit_user
        FOREIGN KEY (exit_verified_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_visit_participant_records_status
        CHECK (
            status IN (
                'EXPECTED',
                'ENTERED',
                'EXITED',
                'DENIED',
                'NO_SHOW'
            )
        ),

    CONSTRAINT chk_visit_participant_records_entry
        CHECK (
            (status IN ('ENTERED', 'EXITED')
                AND entry_at IS NOT NULL)
            OR
            (status NOT IN ('ENTERED', 'EXITED')
                AND entry_at IS NULL)
        ),

    CONSTRAINT chk_visit_participant_records_exit
        CHECK (
            (status = 'EXITED' AND exit_at IS NOT NULL)
            OR
            (status <> 'EXITED' AND exit_at IS NULL)
        ),

    CONSTRAINT chk_visit_participant_records_time
        CHECK (
            exit_at IS NULL
            OR exit_at >= entry_at
        )
);

CREATE INDEX idx_visit_participant_records_session
    ON visit_participant_records(session_id);

CREATE INDEX idx_visit_participant_records_status
    ON visit_participant_records(status);


-- 3. GATE_VERIFICATION_LOGS
-- Nhật ký xác minh quân nhân và khách tại cổng

CREATE TABLE gate_verification_logs (
    id UUID PRIMARY KEY,

    session_id UUID NOT NULL,

    participant_record_id UUID,
    soldier_id UUID,

    subject_type VARCHAR(20) NOT NULL,
    event_type VARCHAR(30) NOT NULL,

    verification_result VARCHAR(20) NOT NULL,

    checked_by_user_id UUID NOT NULL,

    gate_code VARCHAR(50) NOT NULL DEFAULT 'MAIN_GATE',

    notes TEXT,

    checked_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_gate_logs_session
        FOREIGN KEY (session_id)
        REFERENCES visit_sessions(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_gate_logs_participant
        FOREIGN KEY (participant_record_id)
        REFERENCES visit_participant_records(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_gate_logs_soldier
        FOREIGN KEY (soldier_id)
        REFERENCES soldiers(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_gate_logs_user
        FOREIGN KEY (checked_by_user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_gate_logs_subject
        CHECK (
            (
                subject_type = 'VISITOR'
                AND participant_record_id IS NOT NULL
                AND soldier_id IS NULL
            )
            OR
            (
                subject_type = 'SOLDIER'
                AND soldier_id IS NOT NULL
                AND participant_record_id IS NULL
            )
        ),

    CONSTRAINT chk_gate_logs_event
        CHECK (
            event_type IN (
                'IDENTITY_VERIFICATION',
                'ENTRY',
                'EXIT'
            )
        ),

    CONSTRAINT chk_gate_logs_soldier_event
        CHECK (
            subject_type <> 'SOLDIER'
            OR event_type = 'IDENTITY_VERIFICATION'
        ),

    CONSTRAINT chk_gate_logs_result
        CHECK (
            verification_result IN (
                'VERIFIED',
                'REJECTED'
            )
        )
);

CREATE INDEX idx_gate_logs_session
    ON gate_verification_logs(session_id);

CREATE INDEX idx_gate_logs_participant
    ON gate_verification_logs(participant_record_id);

CREATE INDEX idx_gate_logs_soldier
    ON gate_verification_logs(soldier_id);

CREATE INDEX idx_gate_logs_checked_at
    ON gate_verification_logs(checked_at);


-- 4. UNIT_VISIT_CONFIRMATIONS
-- Xác nhận hoàn thành lượt thăm từ đơn vị

CREATE TABLE unit_visit_confirmations (
    id UUID PRIMARY KEY,

    session_id UUID NOT NULL,
    unit_id UUID NOT NULL,

    confirmation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    confirmed_by_user_id UUID,

    confirmation_notes TEXT,

    confirmed_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_unit_confirmations_session
        FOREIGN KEY (session_id)
        REFERENCES visit_sessions(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_unit_confirmations_unit
        FOREIGN KEY (unit_id)
        REFERENCES organization_units(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_unit_confirmations_user
        FOREIGN KEY (confirmed_by_user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_unit_visit_confirmation
        UNIQUE (session_id, unit_id),

    CONSTRAINT chk_unit_confirmation_status
        CHECK (
            confirmation_status IN (
                'PENDING',
                'CONFIRMED',
                'REJECTED'
            )
        ),

    CONSTRAINT chk_unit_confirmation_time
        CHECK (
            (confirmation_status = 'PENDING'
                AND confirmed_at IS NULL)
            OR
            (confirmation_status <> 'PENDING'
                AND confirmed_at IS NOT NULL)
        )
);

CREATE INDEX idx_unit_confirmations_session
    ON unit_visit_confirmations(session_id);

CREATE INDEX idx_unit_confirmations_unit
    ON unit_visit_confirmations(unit_id);

CREATE INDEX idx_unit_confirmations_status
    ON unit_visit_confirmations(confirmation_status);


-- =====================================================
-- END V6
-- =====================================================
