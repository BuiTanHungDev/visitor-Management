
-- =============================================
-- V13: Create RELATIVE role and permissions
-- =============================================

-- 1. Kiểm tra Permission cần thiết
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM permissions
        WHERE code = 'REGISTRATION_CREATE'
    ) THEN
        RAISE EXCEPTION
            'Permission REGISTRATION_CREATE does not exist';
    END IF;
END $$;

-- 2. Tạo Role RELATIVE nếu chưa tồn tại
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
    'RELATIVE',
    'Thân nhân',
    'Người thân đăng ký tài khoản và thực hiện đăng ký thăm khách',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1
    FROM roles
    WHERE code = 'RELATIVE'
);

-- 3. Gán Permission REGISTRATION_CREATE
INSERT INTO role_permissions (
    role_id,
    permission_id
)
SELECT
    r.id,
    p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'RELATIVE'
  AND p.code = 'REGISTRATION_CREATE'
  AND NOT EXISTS (
      SELECT 1
      FROM role_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );
