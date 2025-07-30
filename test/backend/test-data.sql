INSERT INTO user (tenant_id, username, password, status, is_deleted, create_time, update_time) VALUES
                                                                                                   (1, 'admin', '$2a$10$abc...', 1, 0, NOW(), NOW()),
                                                                                                   (1, 'ops', '$2a$10$abc...', 1, 0, NOW(), NOW());

INSERT INTO tenant (name, code, contact, status, create_time) VALUES
    ('演示租户', 'demo', 'test@acme.com', 1, NOW());

INSERT INTO role (name, code, desc) VALUES
    ('超级管理员', 'ADMIN', '全局最高权限');
