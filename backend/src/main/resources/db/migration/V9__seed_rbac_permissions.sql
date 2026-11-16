
-- =============================================
-- V9: Seed RBAC Permissions
-- =============================================

-- 1. Tạo danh mục Permission
INSERT INTO permissions (
    id,
    code,
    name,
    module,
    description
)
SELECT
    gen_random_uuid(),
    p.code,
    p.name,
    p.module,
    p.description
FROM (
    VALUES
        -- USER
        ('USER_VIEW', 'Xem tài khoản', 'USER', 'Xem danh sách và thông tin tài khoản'),
        ('USER_CREATE', 'Tạo tài khoản', 'USER', 'Tạo tài khoản mới'),
        ('USER_UPDATE', 'Cập nhật tài khoản', 'USER', 'Cập nhật thông tin tài khoản'),
        ('USER_DELETE', 'Xóa tài khoản', 'USER', 'Xóa tài khoản'),

        -- ORGANIZATION
        ('ORGANIZATION_VIEW', 'Xem đơn vị', 'ORGANIZATION', 'Xem thông tin đơn vị'),
        ('ORGANIZATION_CREATE', 'Tạo đơn vị', 'ORGANIZATION', 'Tạo đơn vị mới'),
        ('ORGANIZATION_UPDATE', 'Cập nhật đơn vị', 'ORGANIZATION', 'Cập nhật thông tin đơn vị'),
        ('ORGANIZATION_DELETE', 'Xóa đơn vị', 'ORGANIZATION', 'Xóa đơn vị'),

        -- SOLDIER
        ('SOLDIER_VIEW', 'Xem quân nhân', 'SOLDIER', 'Xem thông tin quân nhân'),
        ('SOLDIER_CREATE', 'Tạo hồ sơ quân nhân', 'SOLDIER', 'Tạo hồ sơ quân nhân mới'),
        ('SOLDIER_UPDATE', 'Cập nhật quân nhân', 'SOLDIER', 'Cập nhật hồ sơ quân nhân'),
        ('SOLDIER_DELETE', 'Xóa hồ sơ quân nhân', 'SOLDIER', 'Xóa hồ sơ quân nhân'),

        -- VISITOR
        ('VISITOR_VIEW', 'Xem thân nhân', 'VISITOR', 'Xem thông tin thân nhân'),
        ('VISITOR_CREATE', 'Tạo hồ sơ thân nhân', 'VISITOR', 'Tạo hồ sơ thân nhân mới'),
        ('VISITOR_UPDATE', 'Cập nhật thân nhân', 'VISITOR', 'Cập nhật hồ sơ thân nhân'),
        ('VISITOR_DELETE', 'Xóa hồ sơ thân nhân', 'VISITOR', 'Xóa hồ sơ thân nhân'),

        -- REGISTRATION
        ('REGISTRATION_VIEW', 'Xem đăng ký thăm', 'REGISTRATION', 'Xem đăng ký thăm'),
        ('REGISTRATION_CREATE', 'Tạo đăng ký thăm', 'REGISTRATION', 'Tạo đăng ký thăm mới'),
        ('REGISTRATION_UPDATE', 'Cập nhật đăng ký thăm', 'REGISTRATION', 'Cập nhật đăng ký thăm'),
        ('REGISTRATION_DELETE', 'Xóa đăng ký thăm', 'REGISTRATION', 'Xóa đăng ký thăm'),

        -- APPROVAL
        ('APPROVAL_VIEW', 'Xem phê duyệt', 'APPROVAL', 'Xem hồ sơ chờ phê duyệt'),
        ('APPROVAL_APPROVE', 'Phê duyệt đăng ký', 'APPROVAL', 'Phê duyệt đăng ký thăm'),
        ('APPROVAL_REJECT', 'Từ chối đăng ký', 'APPROVAL', 'Từ chối đăng ký thăm'),

        -- COORDINATION
        ('COORDINATION_VIEW', 'Xem điều phối', 'COORDINATION', 'Xem thông tin điều phối lượt thăm'),
        ('COORDINATION_CREATE', 'Tạo điều phối', 'COORDINATION', 'Tạo kế hoạch điều phối'),
        ('COORDINATION_UPDATE', 'Cập nhật điều phối', 'COORDINATION', 'Cập nhật kế hoạch điều phối'),
        ('COORDINATION_DELETE', 'Xóa điều phối', 'COORDINATION', 'Xóa kế hoạch điều phối'),

        -- GATE
        ('GATE_VIEW', 'Xem xác minh cổng', 'GATE', 'Xem thông tin xác minh tại cổng'),
        ('GATE_VERIFY', 'Xác minh tại cổng', 'GATE', 'Thực hiện xác minh người thăm'),

        -- NOTIFICATION
        ('NOTIFICATION_VIEW', 'Xem thông báo', 'NOTIFICATION', 'Xem thông báo'),
        ('NOTIFICATION_CREATE', 'Tạo thông báo', 'NOTIFICATION', 'Tạo thông báo mới'),

        -- AUDIT
        ('AUDIT_VIEW', 'Xem nhật ký', 'AUDIT', 'Xem nhật ký hoạt động hệ thống')

) AS p(code, name, module, description)
WHERE NOT EXISTS (
    SELECT 1
    FROM permissions existing
    WHERE existing.code = p.code
);

-- 2. Gán toàn bộ Permission cho ADMIN
INSERT INTO role_permissions (
    role_id,
    permission_id
)
SELECT
    r.id,
    p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
AND NOT EXISTS (
    SELECT 1
    FROM role_permissions rp
    WHERE rp.role_id = r.id
      AND rp.permission_id = p.id
);
