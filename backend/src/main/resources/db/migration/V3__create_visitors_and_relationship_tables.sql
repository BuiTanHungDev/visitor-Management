
-- =====================================================
-- V3: VISITORS AND RELATIONSHIPS
-- Quản lý thân nhân và khách thăm
-- =====================================================

-- 1. VISITORS: Thông tin khách thăm
CREATE TABLE visitors (
    id UUID PRIMARY KEY,

    full_name VARCHAR(150) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20),

    phone VARCHAR(20),

    identity_document_type VARCHAR(30),
    identity_document_number VARCHAR(50),

    permanent_address TEXT,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_visitors_gender
        CHECK (
            gender IS NULL OR
            gender IN ('MALE', 'FEMALE', 'OTHER', 'UNKNOWN')
        ),

    CONSTRAINT chk_visitors_identity_type
        CHECK (
            identity_document_type IS NULL OR
            identity_document_type IN (
                'CCCD',
                'CMND',
                'PASSPORT',
                'OTHER'
            )
        ),

    CONSTRAINT chk_visitors_identity_info
        CHECK (
            (identity_document_number IS NULL
                AND identity_document_type IS NULL)
            OR
            (identity_document_number IS NOT NULL
                AND identity_document_type IS NOT NULL)
        ),

    CONSTRAINT chk_visitors_status
        CHECK (
            status IN ('ACTIVE', 'INACTIVE', 'BLOCKED')
        )
);

-- Không cho phép trùng số giấy tờ cùng loại
CREATE UNIQUE INDEX uk_visitors_identity_document
    ON visitors (
        identity_document_type,
        LOWER(identity_document_number)
    )
    WHERE identity_document_number IS NOT NULL;

CREATE INDEX idx_visitors_full_name
    ON visitors(LOWER(full_name));

CREATE INDEX idx_visitors_phone
    ON visitors(phone);


-- 2. VISITOR_SOLDIER_RELATIONSHIPS:
-- Mối quan hệ giữa khách thăm và quân nhân
CREATE TABLE visitor_soldier_relationships (
    id UUID PRIMARY KEY,

    visitor_id UUID NOT NULL,
    soldier_id UUID NOT NULL,

    relationship_type VARCHAR(30) NOT NULL,

    note VARCHAR(255),

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visitor_relationships_visitor
        FOREIGN KEY (visitor_id)
        REFERENCES visitors(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_visitor_relationships_soldier
        FOREIGN KEY (soldier_id)
        REFERENCES soldiers(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_visitor_soldier_relationship
        UNIQUE (visitor_id, soldier_id),

    CONSTRAINT chk_visitor_relationship_type
        CHECK (
            relationship_type IN (
                'PARENT',
                'SPOUSE',
                'CHILD',
                'SIBLING',
                'GRANDPARENT',
                'GRANDCHILD',
                'GUARDIAN',
                'OTHER'
            )
        ),

    CONSTRAINT chk_visitor_relationship_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_visitor_relationships_soldier
    ON visitor_soldier_relationships(soldier_id);

CREATE INDEX idx_visitor_relationships_visitor
    ON visitor_soldier_relationships(visitor_id);


-- =====================================================
-- END V3
-- =====================================================
