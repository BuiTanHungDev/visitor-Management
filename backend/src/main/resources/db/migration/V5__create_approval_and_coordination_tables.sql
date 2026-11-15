
-- =====================================================
-- V5: APPROVAL AND COORDINATION MANAGEMENT
-- Quản lý phê duyệt và điều phối lượt thăm
-- =====================================================


-- 1. REGISTRATION_APPROVALS
-- Các bước phê duyệt đăng ký thăm

CREATE TABLE registration_approvals (
    id UUID PRIMARY KEY,

    registration_id UUID NOT NULL,

    workflow_round INTEGER NOT NULL DEFAULT 1,
    step_order INTEGER NOT NULL,

    step_code VARCHAR(50) NOT NULL,
    step_name VARCHAR(150) NOT NULL,

    assigned_role_id UUID,
    assigned_user_id UUID,

    decision VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    acted_by_user_id UUID,
    comments TEXT,

    assigned_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    acted_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_registration_approvals_registration
        FOREIGN KEY (registration_id)
        REFERENCES visit_registrations(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_registration_approvals_role
        FOREIGN KEY (assigned_role_id)
        REFERENCES roles(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_registration_approvals_assigned_user
        FOREIGN KEY (assigned_user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_registration_approvals_actor
        FOREIGN KEY (acted_by_user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_registration_approval_step
        UNIQUE (registration_id, workflow_round, step_order),

    CONSTRAINT chk_registration_approval_round
        CHECK (workflow_round > 0),

    CONSTRAINT chk_registration_approval_order
        CHECK (step_order > 0),

    CONSTRAINT chk_registration_approval_assignee
        CHECK (
            assigned_role_id IS NOT NULL
            OR assigned_user_id IS NOT NULL
        ),

    CONSTRAINT chk_registration_approval_decision
        CHECK (
            decision IN (
                'PENDING',
                'APPROVED',
                'REJECTED',
                'RETURNED',
                'SKIPPED'
            )
        ),

    CONSTRAINT chk_registration_approval_action_time
        CHECK (
            (decision = 'PENDING' AND acted_at IS NULL)
            OR
            (decision <> 'PENDING' AND acted_at IS NOT NULL)
        )
);

CREATE INDEX idx_registration_approvals_registration
    ON registration_approvals(registration_id);

CREATE INDEX idx_registration_approvals_assigned_role
    ON registration_approvals(assigned_role_id);

CREATE INDEX idx_registration_approvals_assigned_user
    ON registration_approvals(assigned_user_id);

CREATE INDEX idx_registration_approvals_decision
    ON registration_approvals(decision);


-- 2. VISIT_COORDINATIONS
-- Kế hoạch điều phối lượt thăm

CREATE TABLE visit_coordinations (
    id UUID PRIMARY KEY,

    registration_id UUID NOT NULL,

    coordination_type VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',

    created_by_user_id UUID,

    notes TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visit_coordinations_registration
        FOREIGN KEY (registration_id)
        REFERENCES visit_registrations(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visit_coordinations_creator
        FOREIGN KEY (created_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_visit_coordination_type
        CHECK (
            coordination_type IN ('DIRECT', 'ROUTE')
        ),

    CONSTRAINT chk_visit_coordination_status
        CHECK (
            status IN (
                'PLANNED',
                'IN_PROGRESS',
                'COMPLETED',
                'CANCELLED'
            )
        )
);

CREATE INDEX idx_visit_coordinations_registration
    ON visit_coordinations(registration_id);

CREATE INDEX idx_visit_coordinations_status
    ON visit_coordinations(status);


-- 3. VISIT_COORDINATION_STEPS
-- Các đơn vị tham gia điều phối

CREATE TABLE visit_coordination_steps (
    id UUID PRIMARY KEY,

    coordination_id UUID NOT NULL,
    unit_id UUID NOT NULL,

    step_order INTEGER NOT NULL,

    assigned_user_id UUID,

    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    notes TEXT,

    responded_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_coordination_steps_coordination
        FOREIGN KEY (coordination_id)
        REFERENCES visit_coordinations(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_coordination_steps_unit
        FOREIGN KEY (unit_id)
        REFERENCES organization_units(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_coordination_steps_user
        FOREIGN KEY (assigned_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT uk_coordination_step_order
        UNIQUE (coordination_id, step_order),

    CONSTRAINT chk_coordination_step_order
        CHECK (step_order > 0),

    CONSTRAINT chk_coordination_step_status
        CHECK (
            status IN (
                'PENDING',
                'CONFIRMED',
                'REJECTED',
                'COMPLETED',
                'SKIPPED'
            )
        )
);

CREATE INDEX idx_coordination_steps_coordination
    ON visit_coordination_steps(coordination_id);

CREATE INDEX idx_coordination_steps_unit
    ON visit_coordination_steps(unit_id);

CREATE INDEX idx_coordination_steps_status
    ON visit_coordination_steps(status);


-- 4. VISIT_REGISTRATION_STATUS_HISTORIES
-- Lịch sử thay đổi trạng thái đăng ký

CREATE TABLE visit_registration_status_histories (
    id UUID PRIMARY KEY,

    registration_id UUID NOT NULL,

    previous_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,

    changed_by_user_id UUID,

    change_reason TEXT,

    changed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_registration_status_histories_registration
        FOREIGN KEY (registration_id)
        REFERENCES visit_registrations(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_registration_status_histories_user
        FOREIGN KEY (changed_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_registration_status_histories_registration
    ON visit_registration_status_histories(registration_id);

CREATE INDEX idx_registration_status_histories_changed_at
    ON visit_registration_status_histories(changed_at);


-- 5. EXTEND REGISTRATION STATUS
-- Bổ sung trạng thái phục vụ quy trình phê duyệt và điều phối

ALTER TABLE visit_registrations
    DROP CONSTRAINT chk_visit_registration_status;

ALTER TABLE visit_registrations
    ADD CONSTRAINT chk_visit_registration_status
    CHECK (
        status IN (
            'DRAFT',
            'SUBMITTED',
            'UNDER_REVIEW',
            'RETURNED',
            'REJECTED',
            'APPROVED',
            'COORDINATING',
            'READY_FOR_VISIT',
            'CANCELLED'
        )
    );


-- =====================================================
-- END V5
-- =====================================================
