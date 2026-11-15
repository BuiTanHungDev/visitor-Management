
-- =====================================================
-- V7: NOTIFICATION AND AUDIT MANAGEMENT
-- Quản lý thông báo và nhật ký hoạt động
-- =====================================================


-- 1. NOTIFICATIONS
-- Nội dung và thông tin sự kiện thông báo

CREATE TABLE notifications (
    id UUID PRIMARY KEY,

    notification_code VARCHAR(100) NOT NULL,

    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,

    notification_type VARCHAR(50) NOT NULL,

    reference_type VARCHAR(50),
    reference_id UUID,

    created_by_user_id UUID,

    metadata JSONB,

    expires_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notifications_creator
        FOREIGN KEY (created_by_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_notifications_code
    ON notifications(notification_code);

CREATE INDEX idx_notifications_type
    ON notifications(notification_type);

CREATE INDEX idx_notifications_created_at
    ON notifications(created_at DESC);

CREATE INDEX idx_notifications_reference
    ON notifications(reference_type, reference_id);


-- 2. NOTIFICATION_RECIPIENTS
-- Người nhận và trạng thái thông báo

CREATE TABLE notification_recipients (
    id UUID PRIMARY KEY,

    notification_id UUID NOT NULL,
    user_id UUID NOT NULL,

    delivery_channel VARCHAR(20) NOT NULL DEFAULT 'IN_APP',

    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    sent_at TIMESTAMPTZ,
    delivered_at TIMESTAMPTZ,
    read_at TIMESTAMPTZ,

    error_message TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_recipients_notification
        FOREIGN KEY (notification_id)
        REFERENCES notifications(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_notification_recipients_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_notification_recipient
        UNIQUE (notification_id, user_id, delivery_channel),

    CONSTRAINT chk_notification_delivery_channel
        CHECK (
            delivery_channel IN (
                'IN_APP',
                'EMAIL',
                'SMS',
                'PUSH'
            )
        ),

    CONSTRAINT chk_notification_delivery_status
        CHECK (
            delivery_status IN (
                'PENDING',
                'SENT',
                'DELIVERED',
                'FAILED'
            )
        ),

    CONSTRAINT chk_notification_read_time
        CHECK (
            read_at IS NULL
            OR delivered_at IS NOT NULL
        )
);

CREATE INDEX idx_notification_recipients_user
    ON notification_recipients(user_id);

CREATE INDEX idx_notification_recipients_status
    ON notification_recipients(delivery_status);

CREATE INDEX idx_notification_recipients_unread
    ON notification_recipients(user_id, read_at)
    WHERE read_at IS NULL;


-- 3. AUDIT_LOGS
-- Nhật ký hoạt động và truy vết thay đổi dữ liệu

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,

    actor_user_id UUID,

    action_code VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,

    old_values JSONB,
    new_values JSONB,

    description TEXT,

    ip_address INET,
    user_agent TEXT,

    request_id VARCHAR(100),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_logs_actor
        FOREIGN KEY (actor_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_audit_logs_actor
    ON audit_logs(actor_user_id);

CREATE INDEX idx_audit_logs_action
    ON audit_logs(action_code);

CREATE INDEX idx_audit_logs_entity
    ON audit_logs(entity_type, entity_id);

CREATE INDEX idx_audit_logs_created_at
    ON audit_logs(created_at DESC);

CREATE INDEX idx_audit_logs_request
    ON audit_logs(request_id);


-- =====================================================
-- END V7
-- =====================================================
