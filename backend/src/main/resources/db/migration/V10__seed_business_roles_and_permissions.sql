
-- =============================================
-- V10: Seed Business Roles and Permissions
-- =============================================

-- 1. Tạo các Role nghiệp vụ
INSERT INTO roles (
    id,
    code,
    name,
    description,
    is_system,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    r.code,
    r.name,
    r.description,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM (
    VALUES
        (
            'COMMANDER',
            'Chỉ huy',
            'Quản lý và giám sát hoạt động thăm khách'
        ),
        (
            'REGISTRATION_OFFICER',
            'Cán bộ tiếp nhận',
            'Tiếp nhận và quản lý đăng ký thăm'
        ),
        (
            'APPROVER',
            'Cán bộ phê duyệt',
            'Phê duyệt hoặc từ chối đăng ký thăm'
        ),
        (
            'COORDINATOR',
            'Cán bộ điều phối',
            'Tổ chức và điều phối lượt thăm'
        ),
        (
            'GATE_OFFICER',
            'Cán bộ kiểm soát cổng',
            'Xác minh thân nhân tại cổng'
        )
) AS r(code, name, description)
WHERE NOT EXISTS (
    SELECT 1
    FROM roles existing
    WHERE existing.code = r.code
);

-- 2. Gán Permission cho COMMANDER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'COMMANDER'
AND p.code IN (
    'USER_VIEW',
    'ORGANIZATION_VIEW',
    'SOLDIER_VIEW',
    'VISITOR_VIEW',
    'REGISTRATION_VIEW',
    'APPROVAL_VIEW',
    'COORDINATION_VIEW',
    'GATE_VIEW',
    'NOTIFICATION_VIEW',
    'AUDIT_VIEW'
)
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);

-- 3. Gán Permission cho REGISTRATION_OFFICER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'REGISTRATION_OFFICER'
AND p.code IN (
    'ORGANIZATION_VIEW',
    'SOLDIER_VIEW',
    'VISITOR_VIEW',
    'VISITOR_CREATE',
    'VISITOR_UPDATE',
    'REGISTRATION_VIEW',
    'REGISTRATION_CREATE',
    'REGISTRATION_UPDATE',
    'NOTIFICATION_VIEW'
)
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);

-- 4. Gán Permission cho APPROVER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'APPROVER'
AND p.code IN (
    'ORGANIZATION_VIEW',
    'SOLDIER_VIEW',
    'VISITOR_VIEW',
    'REGISTRATION_VIEW',
    'APPROVAL_VIEW',
    'APPROVAL_APPROVE',
    'APPROVAL_REJECT',
    'COORDINATION_VIEW',
    'NOTIFICATION_VIEW'
)
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);

-- 5. Gán Permission cho COORDINATOR
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'COORDINATOR'
AND p.code IN (
    'ORGANIZATION_VIEW',
    'SOLDIER_VIEW',
    'VISITOR_VIEW',
    'REGISTRATION_VIEW',
    'COORDINATION_VIEW',
    'COORDINATION_CREATE',
    'COORDINATION_UPDATE',
    'GATE_VIEW',
    'NOTIFICATION_VIEW'
)
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);

-- 6. Gán Permission cho GATE_OFFICER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'GATE_OFFICER'
AND p.code IN (
    'SOLDIER_VIEW',
    'VISITOR_VIEW',
    'REGISTRATION_VIEW',
    'COORDINATION_VIEW',
    'GATE_VIEW',
    'GATE_VERIFY',
    'NOTIFICATION_VIEW'
)
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);
