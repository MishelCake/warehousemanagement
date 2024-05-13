INSERT INTO roles (role_id, role)
SELECT 1, 'SYSTEM_ADMIN'
WHERE NOT EXISTS (
        SELECT 1 FROM roles WHERE role_id = 1 AND role = 'SYSTEM_ADMIN'
    );

INSERT INTO roles (role_id, role)
SELECT 2, 'WAREHOUSE_MANAGER'
WHERE NOT EXISTS (
        SELECT 1 FROM roles WHERE role_id = 2 AND role = 'WAREHOUSE_MANAGER'
    );

INSERT INTO roles (role_id, role)
SELECT 3, 'CLIENT'
WHERE NOT EXISTS (
        SELECT 1 FROM roles WHERE role_id = 3 AND role = 'CLIENT'
    );

INSERT INTO users (user_id, created_date, email, enabled, name, password, surname, role_id, address, phone_number, postal_code)
 SELECT 1, '2024-05-10 14:41:04', 'admin@test.com', true, 'Administrator', '$2a$12$lfTkwbZsC59fG8SwIDDxVOCbU44RtxXCPJ8JYv9Jzuoyj1KTr.jv.', 'admin', 1, 'string', 'string', 1015
WHERE NOT EXISTS(SELECT 1 FROM users WHERE user_id = 1);
INSERT INTO users (user_id, created_date, email, enabled, name, password, surname, role_id, address, phone_number, postal_code)
SELECT 2, '2024-05-10 15:41:26.669551', 'manager@test.com', true, 'Manager', '$2a$10$iCc6QXSJMJfp4cRcEYuSsOTG0rhCxswGjZ45uJfDc/9bpYCOH781e', 'string', 2, 'string', 'string', 1005
WHERE NOT EXISTS(SELECT 1 FROM users WHERE user_id = 2);
INSERT INTO users (user_id, created_date, email, enabled, name, password, surname, role_id, address, phone_number, postal_code)
SELECT 3, '2024-05-10 14:41:04', 'mishelcake1@gmail.com', true, 'Mishel', '$2a$10$bwm/8ty68pU1xY.GOuQvc.ncA.iOK.vwRd8d1pVJItH9F8Xf5AwAm', 'Cake', 3, 'Tirana', 'string', 1020
WHERE NOT EXISTS(SELECT 1 FROM users WHERE user_id = 3);

