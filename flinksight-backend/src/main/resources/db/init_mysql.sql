-- ===========================
-- 初始化脚本（含外键/唯一约束，保证关联）
-- ===========================

-- ========== 租户 ==========
USE flinksight_test;

SET time_zone = '+08:00';

-- 关外键，避免依赖顺序问题
SET FOREIGN_KEY_CHECKS = 0;

-- 建议用 DELETE TABLE，防止外键问题
TRUNCATE TABLE alert_history;
TRUNCATE TABLE alert;
TRUNCATE TABLE alert_rule;
TRUNCATE TABLE job_alert_log;
TRUNCATE TABLE job_alert_rule;
TRUNCATE TABLE job_metric;
TRUNCATE TABLE job_log;
TRUNCATE TABLE job_diagnostic_log;
TRUNCATE TABLE job_dependency;
TRUNCATE TABLE job_instance;
TRUNCATE TABLE job_info;
TRUNCATE TABLE job;
TRUNCATE TABLE cluster_status_history;
TRUNCATE TABLE node_health;
TRUNCATE TABLE node;
TRUNCATE TABLE cluster;

TRUNCATE TABLE role_permission;
TRUNCATE TABLE user_role;
TRUNCATE TABLE user_tenant;
TRUNCATE TABLE oauth_account;
TRUNCATE TABLE sso_token;
TRUNCATE TABLE user_token_state;
TRUNCATE TABLE login_history;
TRUNCATE TABLE profile;
TRUNCATE TABLE `user`;

TRUNCATE TABLE permission;
TRUNCATE TABLE `role`;
TRUNCATE TABLE `menu`;

TRUNCATE TABLE tenant_config;
TRUNCATE TABLE tenant_resource;
TRUNCATE TABLE tenant;

-- 依次清空其余不依赖字段的表
TRUNCATE TABLE audit_log;
TRUNCATE TABLE config;
TRUNCATE TABLE sys_param;
TRUNCATE TABLE system_settings;
TRUNCATE TABLE api_access_log;
TRUNCATE TABLE api_key;
TRUNCATE TABLE api_whitelist;
TRUNCATE TABLE data_source;
TRUNCATE TABLE metric_dashboard;
TRUNCATE TABLE ticket;
TRUNCATE TABLE file;
TRUNCATE TABLE tag;
TRUNCATE TABLE notify_channel;
TRUNCATE TABLE org_node;
TRUNCATE TABLE label;
TRUNCATE TABLE resource;
TRUNCATE TABLE resource_label;
TRUNCATE TABLE resource_group;
TRUNCATE TABLE notification;
TRUNCATE TABLE label_login_history;
TRUNCATE TABLE ops_task;
-- 恢复外键
SET FOREIGN_KEY_CHECKS = 1;

START TRANSACTION;

-- ========= 5) 告警 / 告警历史 / 规则 / 工单 =========
INSERT INTO alert (id, tenant_id, job_id, level, type, message, status, handler_id, is_deleted, created_at, updated_at)
VALUES
    (61001, 1, 30001, 'WARN',  'CPUHigh',    'CPU usage > 85%', 0, 1002, 0, NOW() - INTERVAL 20 MINUTE, NOW()),
    (61002, 1, 30002, 'FATAL', 'JobFailed',  'exit code 1',     0, 1001, 0, NOW() - INTERVAL 23 HOUR,  NOW()),
    (61011, 2, 30011, 'WARN',  'MEMHigh',    'Memory > 90%',    1, 2001, 0, NOW() - INTERVAL 30 MINUTE,NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO alert_history (id, alert_id, rule_id, content, level, status, operator_id, tenant_id, operate_time, is_deleted, created_at)
VALUES
    (62001, 61001, 60001, '触发CPU规则', 2, 0, 1002, 1, NOW() - INTERVAL 18 MINUTE, 0, NOW()),
    (62011, 61011, 60011, '触发MEM规则', 2, 1, 2001, 2, NOW() - INTERVAL 20 MINUTE, 0, NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO alert_rule (id, tenant_id, cluster_id, metric_key, threshold, compare_op, channel, enable, is_deleted, created_at)
VALUES
    (60001, 1, 10001, 'cpu', 0.85, '>',  'email', 1, 0, NOW()),
    (60011, 2, 20001, 'mem', 0.90, '>',  'ding',  1, 0, NOW())
ON DUPLICATE KEY UPDATE threshold=VALUES(threshold);


-- ========= 作业主数据（job / job_info / job_instance / 依赖 / 诊断 / 日志 / 指标） =========
-- 运行作业表（与 cluster/tenant 绑定）
INSERT INTO job (id, tenant_id, cluster_id, job_name, job_type, status, owner_id, start_time, is_deleted, created_at, updated_at)
VALUES
    (30001, 1, 10001, 'payment-etl', 'streaming', 1, 1001, NOW() - INTERVAL 2 HOUR, 0, NOW(), NOW()),
    (30002, 1, 10001, 'order-sync',  'batch',     2,  1002, NOW() - INTERVAL 1 DAY, 0, NOW(), NOW()),
    (30011, 2, 20001, 'beta-etl',    'streaming', 1, 2001, NOW() - INTERVAL 3 HOUR, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- 作业定义（job_info）与实例（job_instance）
INSERT INTO job_info (id, job_name, tenant_id, job_type, project_code, operator, source, trace_id, remark, register_at, is_deleted, created_at, updated_at)
VALUES
    (40001, 'payment-etl', 1, 'streaming', 'acme-pay', 'alice', 'ui', 'trace-pay-0001', '支付ETL', UNIX_TIMESTAMP()*1000, 0, NOW(), NOW()),
    (40002, 'order-sync',  1, 'batch',     'acme-ord', 'bob',   'ui', 'trace-ord-0001', '订单同步', UNIX_TIMESTAMP()*1000, 0, NOW(), NOW()),
    (40011, 'beta-etl',    2, 'streaming', 'beta-etl', 'charlie','api','trace-beta-0001','Beta流水', UNIX_TIMESTAMP()*1000, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE remark=VALUES(remark);

INSERT INTO job_instance (id, job_id, job_name, engine_type, cluster_id, instance_code, status, trigger_type, start_time, end_time, tenant_id, operator, is_deleted, created_at, updated_at)
VALUES
    (41001, 40001, 'payment-etl', 'flink', 10001, 'inst-pay-001', 1, 'SCHEDULE', NOW() - INTERVAL 120 MINUTE, NULL, 1, 'alice', 0, NOW(), NOW()),
    (41002, 40002, 'order-sync',  'flink', 10001, 'inst-ord-001', 2, 'MANUAL',   NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 23 HOUR, 1, 'bob',   0, NOW(), NOW()),
    (41011, 40011, 'beta-etl',    'spark', 20001, 'inst-beta-001',1, 'SCHEDULE', NOW() - INTERVAL 180 MINUTE, NULL, 2, 'charlie',0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO job_alert_log (id, tenant_id, job_id, job_name, alert_type, alert_msg, alert_time, status, is_deleted)
VALUES
    (64001, 1, 30001, 'payment-etl', 'BackPressure', 'ratio=0.35', NOW() - INTERVAL 15 MINUTE, 'SENT', 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);
INSERT INTO job_alert_rule (id, tenant_id, rule_name, job_type, alert_type, condition_json, target, is_deleted)
VALUES
    (63001, 1, 'FlinkBackPressure', 'streaming', 'BackPressure', '{"ratio":">0.3"}', 'oncall@acme', 0)
ON DUPLICATE KEY UPDATE target=VALUES(target);

-- 指标（CPU/MEM/吞吐…）
INSERT INTO job_metric (id, tenant_id, job_id, metric_key, metric_value, metric_time, is_deleted)
VALUES
    (45001, 1, 30001, 'cpu', 0.72, NOW() - INTERVAL 5 MINUTE, 0),
    (45002, 1, 30001, 'mem', 0.81, NOW() - INTERVAL 5 MINUTE, 0),
    (45003, 1, 30001, 'throughput', 12500, NOW() - INTERVAL 5 MINUTE, 0),
    (45011, 2, 30011, 'cpu', 0.48, NOW() - INTERVAL 5 MINUTE, 0)
ON DUPLICATE KEY UPDATE metric_value=VALUES(metric_value);
INSERT INTO job_log (id, tenant_id, job_id, level, content, log_time, is_deleted)
VALUES
    (44001, 1, 30001, 'INFO',  'job running...', NOW() - INTERVAL 10 MINUTE, 0),
    (44002, 1, 30002, 'ERROR', 'exit code 1',   NOW() - INTERVAL 23 HOUR,    0),
    (44011, 2, 30011, 'INFO',  'beta running',  NOW() - INTERVAL 20 MINUTE,  0)
ON DUPLICATE KEY UPDATE level=VALUES(level);

-- 诊断日志 / 运行日志
INSERT INTO job_diagnostic_log (id, job_id, job_name, log_time, level, content, trace_id, is_deleted)
VALUES
    (43001, 40001, 'payment-etl', NOW() - INTERVAL 30 MINUTE, 'INFO',  'checkpoint completed', 'trace-pay-0001', 0),
    (43002, 40002, 'order-sync',  NOW() - INTERVAL 25 HOUR,  'ERROR', 'NullPointerException at step-3', 'trace-ord-0001', 0)
ON DUPLICATE KEY UPDATE level=VALUES(level);
-- 依赖（演示：order-sync 依赖 payment-etl）
INSERT INTO job_dependency (id, job_id, dependency_job_id, type, is_deleted)
VALUES (42001, 40002, 40001, 'AFTER_SUCCESS', 0)
ON DUPLICATE KEY UPDATE type=VALUES(type);

-- ========= 集群 / 节点 =========
INSERT INTO cluster (id, tenant_id, name, type, endpoint, version, tags, status, remark, is_deleted, created_at)
VALUES
    (10001, 1, 'acme-k8s-prod', 'K8S', 'https://k8s.acme.local', '1.28', 'prod,critical', 1, '生产', 0, NOW()),
    (10002, 1, 'acme-k8s-dev',  'K8S', 'https://k8s-dev.acme.local', '1.27', 'dev', 1, '开发', 0, NOW()),
    (20001, 2, 'beta-yarn',     'YARN', 'yarn://beta.cluster', '3.3.6', 'etl', 1, 'ETL集群', 0, NOW())
ON DUPLICATE KEY UPDATE remark=VALUES(remark);

INSERT INTO cluster_status_history (id, cluster_id, collect_time, active_node_count, cpu_usage, memory_usage, queue_load_json, extend_json, is_deleted)
VALUES
    (52001, 10001, NOW() - INTERVAL 5 MINUTE, 2, 0.62, 0.71, '{"q1":0.7}', NULL, 0),
    (52002, 20001, NOW() - INTERVAL 5 MINUTE, 1, 0.45, 0.50, '{"default":0.4}', NULL, 0)
ON DUPLICATE KEY UPDATE active_node_count=VALUES(active_node_count);

INSERT INTO node (id, name, type, ip, cluster_id, status, is_deleted, create_time)
VALUES
    (50001, 'acme-node-1', 'worker', '10.0.0.11', 10001, 1, 0, NOW()),
    (50002, 'acme-node-2', 'worker', '10.0.0.12', 10001, 1, 0, NOW()),
    (50003, 'beta-node-1', 'nm',     '10.10.0.21',20001, 1, 0, NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO node_health (id, tenant_id, node_id, health_status, check_time, message, is_deleted)
VALUES
    (51001, 1, 50001, 'HEALTHY',   NOW() - INTERVAL 1 MINUTE, 'ok', 0),
    (51002, 1, 50002, 'WARNING',   NOW() - INTERVAL 1 MINUTE, 'mem high', 0),
    (51003, 2, 50003, 'HEALTHY',   NOW() - INTERVAL 1 MINUTE, 'ok', 0)
ON DUPLICATE KEY UPDATE health_status=VALUES(health_status);

-- =========  基础：租户 =========
INSERT INTO tenant (id, code, name, contact, contact_info, remark, status, is_deleted, create_time)
VALUES
    (1, 'acme', 'Acme Data', 'Alice', 'alice@acme.example', '演示租户A', 1, 0, NOW()),
    (2, 'beta', 'Beta Corp', 'Bob', 'ops@beta.example', '演示租户B', 1, 0, NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO tenant_config (id, tenant_id, config_key, config_value, description, is_deleted, create_time, update_time)
VALUES (91001, 1, 'feature.realtime', 'true', '实时功能开关', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE config_value=VALUES(config_value);

INSERT INTO tenant_resource (id, tenant_id, resource_id, is_deleted)
VALUES (92001, 1, 75101, 0)
ON DUPLICATE KEY UPDATE is_deleted=0;

-- 用户（演示：主要走 SSO，password 未做加密，仅占位）
-- ========= 用户 / 角色 / 权限 =========
INSERT INTO `user` (id, tenant_id, username, `password`, nickname, avatar, email, phone, status, is_deleted, sso_id, created_at, updated_at)
VALUES
    (1001, 1, 'alice',  'Admin123!', 'Alice', NULL, 'alice@acme.example',  '13800000001', 1, 0, 'kc-sub-alice',  NOW(), NOW()),
    (1002, 1, 'bob',    'User123!',  'Bob',   NULL, 'bob@acme.example',    '13800000002', 1, 0, 'kc-sub-bob',    NOW(), NOW()),
    (2001, 2, 'charlie','Admin123!', 'Charlie',NULL, 'charlie@beta.example','13800000003', 1, 0, 'kc-sub-charlie',NOW(), NOW())
ON DUPLICATE KEY UPDATE nickname=VALUES(nickname);


-- 角色（注意：role.code 全局唯一）
INSERT INTO `role` (id, code, name, tenant_id, remark, created_at, is_deleted)
VALUES
    (101, 'T1_ADMIN', '租户管理员', 1, 'Acme 管理员', NOW(), 0),
    (102, 'T1_USER',  '普通用户',   1, 'Acme 普通用户', NOW(), 0),
    (201, 'T2_ADMIN', '租户管理员', 2, 'Beta 管理员', NOW(), 0),
    (202, 'T2_USER',  '普通用户',   2, 'Beta 普通用户', NOW(), 0)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 顶层与子级菜单初始化（与前端 MENUS 常量一致）
-- 显式指定 id 以便 parent_id 引用
INSERT INTO `menu`
(`id`,`parent_id`,`menu_key`,`path`,`title_zh`,`title_en`,`icon`,`order_num`,`required_code`,`is_deleted`)
VALUES
    (1 , NULL, 'dashboard', '/dashboard',        '总览',     'Dashboard', 'dashboard', 10, 'DASHBOARD_VIEW', 0),
    (2 , NULL, 'cluster',   '/cluster',          '集群',     'Clusters',  'cluster',   20, 'CLUSTER_VIEW',   0),
    (3 , 2   , 'node',      '/cluster/node',     '节点',     'Nodes',     'node',      21, 'CLUSTER_VIEW',   0),
    (4 , NULL, 'job',       '/job',              '任务',     'Jobs',      'job',       30, 'JOB_MANAGE',     0),
    (5 , NULL, 'alert',     '/alert',            '报警',     'Alerts',    'alert',     40, 'ALERT_HANDLE',   0),
    (6 , 5   , 'rule',      '/alert/rule',       '规则管理',  'Rules',     'rule',      41, 'ALERT_HANDLE',   0),
    (7 , NULL, 'settings',  '/settings',         '系统',     'System',    'setting',   50, 'SYSTEM_ADMIN',   0),
    (8 , 7   , 'user',      '/settings/user',    '用户',     'Users',     'user',      51, 'USER_ADMIN',     0),
    (9 , 7   , 'role',      '/settings/role',    '角色',     'Roles',     'role',      52, 'ROLE_ADMIN',     0),
    (10, 7   , 'tenant',    '/settings/tenant',  '租户',     'Tenant',    'tenant',    53, 'TENANT_ADMIN',   0),
    (11, NULL, 'ops',       '/ops',              '运维',     'Operation', 'ops',       60, 'OPS_ACCESS',     0),
    (12, NULL, 'mfa',       '/mfa',              '子系统',   'SubSystem', 'appstore',  70, 'SUBSYSTEM_ACCESS', 0),
    (13, NULL, 'microapp',  '/microapp',         '外部应用', 'MicroApp',  'app',       80, NULL,             0);

-- 说明：
-- 1) dashboard/job/alert(含rule) 会在用户权限包含 ['DASHBOARD_VIEW','JOB_MANAGE','ALERT_HANDLE'] 时显示
-- 2) cluster/settings/ops/mfa 等需要对应权限码才显示
-- 3) microapp 设为 NULL → 公共可见（与当前过滤逻辑一致）
-- 设置自增游标到最大 id 之后，避免后续插入冲突
ALTER TABLE `menu` AUTO_INCREMENT = 1000;

-- 用户-角色
INSERT INTO user_role (id, user_id, role_id, tenant_id, assign_time, is_deleted)
VALUES
    (7001, 1001, 101, 1, NOW(), 0),
    (7002, 1002, 102, 1, NOW(), 0),
    (7003, 2001, 201, 2, NOW(), 0)
ON DUPLICATE KEY UPDATE is_deleted=0;

INSERT INTO profile (id, user_id, real_name, avatar_url, gender, department, position, signature, phone, email, is_deleted, create_time)
VALUES
    (74001, 1001, 'Alice Zhang', NULL, 2, '数据平台', '负责人', 'Keep it simple', '13800000001', 'alice@acme.example', 0, NOW())
ON DUPLICATE KEY UPDATE real_name=VALUES(real_name);

-- 用户-租户（跨租户场景：alice 也被授权到 beta，默认租户为 acme）
INSERT INTO user_tenant (id, user_id, tenant_id, role, is_default, is_deleted, create_time)
VALUES
    (8001, 1001, 1, 'OWNER', 1, 0, NOW()),
    (8002, 1001, 2, 'ADMIN', 0, 0, NOW()),
    (8003, 1002, 1, 'USER',  1, 0, NOW()),
    (8004, 2001, 2, 'OWNER', 1, 0, NOW())
ON DUPLICATE KEY UPDATE role=VALUES(role), is_default=VALUES(is_default);

-- 权限（按租户隔离）
INSERT INTO permission (id, code, name, description, type, tenant_id, enabled, is_deleted)
VALUES
    (301, 'DASHBOARD_VIEW', '查看大盘', '访问指标大盘', 'API', 1, 1, 0),
    (302, 'JOB_MANAGE',     '管理作业', '新建/停止/下发作业', 'API', 1, 1, 0),
    (303, 'ALERT_HANDLE',   '处理告警', '告警确认与工单流转', 'API', 1, 1, 0),
    (304, 'DASHBOARD_VIEW', '查看大盘', '访问指标大盘', 'API', 2, 1, 0),
    (305, 'ALERT_HANDLE',   '处理告警', '告警确认与工单流转', 'API', 2, 1, 0)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 角色-权限
INSERT INTO role_permission (id, role_id, permission_id, tenant_id, is_deleted)
VALUES
    (5001, 101, 301, 1, 0), (5002, 101, 302, 1, 0), (5003, 101, 303, 1, 0),
    (5004, 102, 301, 1, 0),
    (5005, 201, 304, 2, 0), (5006, 201, 305, 2, 0),
    (5007, 202, 304, 2, 0)
ON DUPLICATE KEY UPDATE is_deleted=0;

-- 你的 schema 里没有 department/post/menu/data_scope/api 等主表，这些关联表可暂略或自建主表后再插


INSERT INTO oauth_account (id, user_id, provider, openid, unionid, access_token, create_time, is_deleted)
VALUES
    (72001, 1001, 'keycloak', 'kc-sub-alice', NULL, 'AT-ALICE', NOW(), 0)
ON DUPLICATE KEY UPDATE access_token=VALUES(access_token);

INSERT INTO sso_token (id, user_id, token, issued_at, expires_at, revoked, create_time)
VALUES
    (73001, 1001, 'jwt-demo-alice', NOW() - INTERVAL 1 HOUR, NOW() + INTERVAL 23 HOUR, 0, NOW())
ON DUPLICATE KEY UPDATE revoked=VALUES(revoked);

INSERT INTO user_token_state (user_id, token_version, updated_at)
VALUES (1001, 1, NOW())
ON DUPLICATE KEY UPDATE token_version=VALUES(token_version);

-- =========  登录 / SSO / 用户档案 =========
INSERT INTO login_history (id, user_id, tenant_id, login_type, provider, ip_address, device_info, login_time, success_flag, is_deleted)
VALUES
    (71001, 1001, 1, 'SSO', 'keycloak', '127.0.0.1', 'Chrome macOS', NOW() - INTERVAL 1 HOUR, 1, 0),
    (71002, 1002, 1, 'PASSWORD', NULL, '127.0.0.1', 'Chrome macOS', NOW() - INTERVAL 2 DAY, 1, 0),
    (71011, 2001, 2, 'SSO', 'keycloak', '127.0.0.1', 'Chrome macOS', NOW() - INTERVAL 30 MINUTE, 1, 0)
ON DUPLICATE KEY UPDATE success_flag=VALUES(success_flag);

-- =========  审计 / 配置 / 数据源 / 白名单 / API Key / 访问日志 =========
INSERT INTO audit_log (id, tenant_id, user_id, action, target_type, target_id, ip, content, operator, trace_id, created_at, is_deleted)
VALUES
    (79001, 1, 1001, 'LOGIN', 'USER', 1001, '127.0.0.1', 'SSO 登录成功', 'alice', 'trace-login-1', NOW(), 0)
ON DUPLICATE KEY UPDATE content=VALUES(content);

INSERT INTO config (id, code, value, description, is_deleted, updated_at)
VALUES (80001, 'site.title', 'Flinksight Demo', '站点标题', 0, NOW())
ON DUPLICATE KEY UPDATE value=VALUES(value);

INSERT INTO sys_param (id, name, code, value, type, description, is_deleted, update_time)
VALUES (81001, '默认并行度', 'flink.default.parallelism', '4', 'NUMBER', 'Flink 默认并行度', 0, NOW())
ON DUPLICATE KEY UPDATE value=VALUES(value);

INSERT INTO system_settings (id, code, value, description, is_deleted)
VALUES (82001, 'global.theme', '{"primary":"#1677ff"}', '全局主题', 0)
ON DUPLICATE KEY UPDATE value=VALUES(value);

INSERT INTO api_key (id, name, api_key, tenant_id, user_id, status, expire_time, created_at, is_deleted)
VALUES (85001, 'alice-dev', 'AK-ALICE-DEV-001', 1, 1001, 0, NOW() + INTERVAL 30 DAY, NOW(), 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO api_whitelist (id, api_path, description, allowed_user_id, allowed_tenant_id, status, creator, create_time, updater, update_time, is_deleted)
VALUES (84001, '/api/**', '开发环境放行', 1001, 1, 1, 'system', NOW(), 'system', NOW(), 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);

INSERT INTO data_source (id, name, type, connect_info, tenant_id, description, is_deleted, created_at)
VALUES (83001, 'acme-mysql', 'mysql', '{"url":"jdbc:mysql://mysql:3306/dw","user":"demo"}', 1, '数仓库', 0, NOW())
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- =========  大盘 / 指标配置 & 监控 =========
INSERT INTO metric_dashboard (id, name, description, tenant_id, config, is_deleted, create_time)
VALUES
    (70001, 'Acme-核心指标', 'CPU/MEM/吞吐/延迟', 1,
     '{"widgets":[{"metric":"cpu"},{"metric":"memory"},{"metric":"throughput"},{"metric":"delay"}]}', 0, NOW()),
    (70011, 'Beta-核心指标', 'Beta 总览', 2, '{"widgets":[{"metric":"cpu"}]}', 0, NOW())
ON DUPLICATE KEY UPDATE description=VALUES(description);

INSERT INTO ticket (id, tenant_id, alert_id, handler_id, status, note, is_deleted, update_time, create_time)
VALUES
    (65001, 1, 61001, 1002, 1, '处理中，观察10分钟', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- =========  文件 =========
INSERT INTO `file` (id, name, url, type, size, user_id, tenant_id, is_deleted, created_at)
VALUES (93001, 'logo.png', '/files/logo.png', 'image/png', 20480, 1001, 1, 0, NOW())
ON DUPLICATE KEY UPDATE url=VALUES(url);

-- =========  标签 / 租户配置 / 资源授权 =========
INSERT INTO tag (id, name, color, type, tenant_id, create_time, is_deleted)
VALUES (90001, '核心', '#1677ff', 'biz', 1, NOW(), 0)
ON DUPLICATE KEY UPDATE color=VALUES(color);

INSERT INTO notify_channel (id, type, config, name, enabled, tenant_id, created_at, is_deleted)
VALUES (88001, 'email', '{"smtp":"smtp.example.com","from":"noreply@acme.com"}', 'Acme 邮件', 1, 1, NOW(), 0)
ON DUPLICATE KEY UPDATE enabled=VALUES(enabled);

INSERT INTO org_node (id, parent_id, name, type, sort_order, tenant_id, created_at, is_deleted)
VALUES
    (89001, NULL, 'Acme', 'company', 0, 1, NOW(), 0),
    (89002, 89001, '数据平台部', 'dept', 10, 1, NOW(), 0)
ON DUPLICATE KEY UPDATE name=VALUES(name);



INSERT INTO label (id, name, color, type, tenant_id, is_deleted)
VALUES (76001, '高优', '#ff4d4f', 'priority', 1, 0)
ON DUPLICATE KEY UPDATE color=VALUES(color);

INSERT INTO `resource` (id, name, type, path, tenant_id, description, is_deleted, create_time)
VALUES (75101, 'payment-udf.jar', 'JAR', '/res/udf/payment-udf.jar', 1, '支付UDF包', 0, NOW())
ON DUPLICATE KEY UPDATE description=VALUES(description);

INSERT INTO resource_label (id, resource_id, label_id, is_deleted)
VALUES (76101, 75101, 76001, 0)
ON DUPLICATE KEY UPDATE is_deleted=0;

-- ========= 资源 / 标签 / 通知 =========
INSERT INTO `resource_group` (id, name, type, parent_id, tenant_id, description, create_time, is_deleted)
VALUES (75001, 'Acme-脚本', 'SCRIPT', NULL, 1, '脚本资源', NOW(), 0)
ON DUPLICATE KEY UPDATE description=VALUES(description);

INSERT INTO notification (id, user_id, tenant_id, title, content, type, is_read, is_deleted, create_time)
VALUES
    (77001, 1001, 1, '欢迎使用', '你已成功登录 Acme', 'INFO', 0, 0, NOW())
ON DUPLICATE KEY UPDATE is_read=VALUES(is_read);

INSERT INTO label_login_history (id, label_id, user_id, login_time, ip_address, tenant_id, is_deleted)
VALUES (78001, 76001, 1001, NOW() - INTERVAL 55 MINUTE, '127.0.0.1', 1, 0)
ON DUPLICATE KEY UPDATE ip_address=VALUES(ip_address);

INSERT INTO api_access_log (id, url, http_method, params, status, user_id, tenant_id, ip, access_time, duration, is_deleted)
VALUES (86001, '/api/dashboard/summary', 'GET', '{"tenantId":1}', 200, 1001, 1, '127.0.0.1', NOW() - INTERVAL 5 MINUTE, 42, 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- ========= 运维 / 通道 / 组织 =========
INSERT INTO ops_task (id, tenant_id, name, type, status, description, created_at, executed_at, is_deleted)
VALUES (87001, 1, '按日备份', 'backup', 'SUCCESS', 'S3 归档', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, 0)
ON DUPLICATE KEY UPDATE status=VALUES(status);

COMMIT;
