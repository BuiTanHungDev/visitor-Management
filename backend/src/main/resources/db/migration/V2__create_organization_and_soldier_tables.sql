
-- =====================================================
-- V2: ORGANIZATION AND SOLDIER MANAGEMENT
-- Quản lý đơn vị và quân nhân
-- =====================================================

-- 1. ORGANIZATION_UNITS: Cơ cấu tổ chức đơn vị
CREATE TABLE organization_units (
    id UUID PRIMARY KEY,

    parent_unit_id UUID,

    unit_code VARCHAR(50) NOT NULL,
    unit_name VARCHAR(150) NOT NULL,
    unit_type VARCHAR(50) NOT NULL,

    description VARCHAR(255),

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_organization_units_parent
        FOREIGN KEY (parent_unit_id)
        REFERENCES organization_units(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_organization_units_status
        CHECK (status IN ('ACTIVE', 'INACTIVE')),

    CONSTRAINT chk_organization_units_parent
        CHECK (parent_unit_id IS NULL OR parent_unit_id <> id)
);

CREATE UNIQUE INDEX uk_organization_units_code_lower
    ON organization_units (LOWER(unit_code));

CREATE INDEX idx_organization_units_parent
    ON organization_units(parent_unit_id);


-- 2. SOLDIERS: Thông tin quân nhân
CREATE TABLE soldiers (
    id UUID PRIMARY KEY,

    user_id UUID UNIQUE,
    unit_id UUID NOT NULL,

    service_number VARCHAR(50) NOT NULL,
    full_name VARCHAR(150) NOT NULL,

    rank_name VARCHAR(100),
    position_title VARCHAR(150),

    joined_at DATE,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_soldiers_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_soldiers_unit
        FOREIGN KEY (unit_id)
        REFERENCES organization_units(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_soldiers_status
        CHECK (
            status IN (
                'ACTIVE',
                'INACTIVE',
                'TRANSFERRED',
                'DISCHARGED'
            )
        )
);

CREATE UNIQUE INDEX uk_soldiers_service_number_lower
    ON soldiers (LOWER(service_number));

CREATE INDEX idx_soldiers_unit_id
    ON soldiers(unit_id);

CREATE INDEX idx_soldiers_full_name_lower
    ON soldiers(LOWER(full_name));


-- =====================================================
-- END V2
-- =====================================================
