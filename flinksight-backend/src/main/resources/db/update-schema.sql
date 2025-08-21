-- 所有表含 tenant_id 字段，默认强租户隔离，安全高可用。
-- 逻辑删除（is_deleted）与时间戳，适配软删与数据同步/备份。
-- 字段注释齐全，数据类型考虑可扩展性（如TEXT存复杂JSON、ENUM建议用varchar以兼容未来扩展）。
-- 主键/唯一键、索引建议生产实际业务补全。
-- 适合直接Flyway/Liquibase等工具落地生产，后续可按需求细化分区、表空间等特性。

--  ================================
--  1. 租户相关表
--  ================================

--  1. 创建测试数据库（如果已存在则删除后重建）
DROP DATABASE IF EXISTS flinksight_test;
CREATE DATABASE flinksight_test CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

--  2. 删除旧的测试用户（避免冲突）
DROP USER IF EXISTS 'testuser'@'localhost';
DROP USER IF EXISTS 'testuser'@'%';

--  3. 创建新的测试用户
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'TestPass123!';
CREATE USER 'testuser'@'%' IDENTIFIED BY 'TestPass123!';

--  4. 授权用户访问数据库
GRANT ALL PRIVILEGES ON flinksight_test.* TO 'testuser'@'localhost';
GRANT ALL PRIVILEGES ON flinksight_test.* TO 'testuser'@'%';

--  5. 刷新权限
FLUSH PRIVILEGES;

--  6. 提示信息
SELECT '测试数据库 testdb 和用户 testuser 已初始化完成！' AS Info;

--  6. 使用测试数据库
USE flinksight_test;


CREATE TABLE IF NOT EXISTS alert
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    job_id BIGINT UNSIGNED  NOT NULL COMMENT '任务ID',
    level VARCHAR(16) COMMENT '报警级别',
    type VARCHAR(32) COMMENT '报警类型',
    message VARCHAR(255) COMMENT '报警内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态(0未处理1处理中2关闭)',
    handler_id BIGINT UNSIGNED  COMMENT '处理人',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '产生时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT pk_alert PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警事件表';



CREATE TABLE IF NOT EXISTS alert_history
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '报警历史ID',
    alert_id BIGINT UNSIGNED  COMMENT '报警ID',
    rule_id BIGINT UNSIGNED  COMMENT '报警规则ID',
    content TEXT COMMENT '报警内容',
    level INT COMMENT '报警级别',
    status INT COMMENT '处理状态',
    operator_id BIGINT UNSIGNED  COMMENT '操作人ID',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    operate_time DATETIME COMMENT '操作时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT pk_alert_history PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警历史表';



CREATE TABLE IF NOT EXISTS alert_rule
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    cluster_id BIGINT UNSIGNED  NOT NULL COMMENT '集群ID',
    metric_key VARCHAR(64) COMMENT '指标',
    threshold DOUBLE COMMENT '阈值',
    compare_op VARCHAR(8) COMMENT '比较符(>,<,=,!=等)',
    channel VARCHAR(32) COMMENT '通知方式',
    enable TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT pk_alert_rule PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警规则表';


CREATE TABLE IF NOT EXISTS api_access_log
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '日志ID',
    url VARCHAR(255) COMMENT '请求URL',
    http_method VARCHAR(16) COMMENT 'HTTP方法',
    params TEXT COMMENT '请求参数',
    status INT COMMENT '响应码',
    user_id BIGINT COMMENT '访问用户ID',
    tenant_id BIGINT COMMENT '租户ID',
    ip VARCHAR(64) COMMENT 'IP地址',
    access_time DATETIME COMMENT '访问时间',
    duration BIGINT COMMENT '耗时ms',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_time (access_time),
    CONSTRAINT pk_api_access_log PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API访问日志表';


CREATE TABLE IF NOT EXISTS api_key
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '密钥ID',
    name VARCHAR(64) COMMENT '密钥名称',
    api_key VARCHAR(128) COMMENT 'API密钥内容',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    user_id BIGINT UNSIGNED  COMMENT '关联用户ID',
    status TINYINT DEFAULT 0 COMMENT '密钥状态 0正常 1禁用',
    expire_time DATETIME COMMENT '过期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_tenant_user (tenant_id, user_id),
    UNIQUE KEY uk_api_key (api_key),
    CONSTRAINT pk_api_key PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API密钥表';


CREATE TABLE IF NOT EXISTS api_whitelist
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
    api_path VARCHAR(255) NOT NULL COMMENT 'API路径（支持Ant风格）',
    `description`  VARCHAR(255) COMMENT 'API描述',
    allowed_user_id BIGINT COMMENT '允许调用的用户ID',
    allowed_tenant_id BIGINT COMMENT '允许调用的租户ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1启用',
    creator VARCHAR(64) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_api_path (api_path),
    CONSTRAINT pk_api_whitelist PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API白名单';


CREATE TABLE IF NOT EXISTS audit_log
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    user_id BIGINT UNSIGNED  COMMENT '操作人ID',
    action VARCHAR(64) COMMENT '操作类型',
    target_type VARCHAR(32) COMMENT '对象类型',
    target_id BIGINT COMMENT '对象ID',
    ip VARCHAR(45) COMMENT 'IP地址',
    content VARCHAR(255) COMMENT '操作内容',
    operator VARCHAR(64) NOT NULL COMMENT '操作人用户名/ID',
    trace_id VARCHAR(64) COMMENT '全链路追踪ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_operator (operator),
    CONSTRAINT pk_audit_log PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作审计日志表';

CREATE TABLE IF NOT EXISTS cluster
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '集群ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '所属租户ID',
    name VARCHAR(64) NOT NULL COMMENT '集群名称',
    type VARCHAR(32) NOT NULL COMMENT '类型(YARN/K8S/Standalone)',
    endpoint VARCHAR(128) NOT NULL COMMENT '集群访问地址',
    version VARCHAR(32) COMMENT '版本号',
    tags VARCHAR(100) COMMENT '标签',
    status INT NOT NULL DEFAULT 1 COMMENT '状态',
    remark VARCHAR(255) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME COMMENT '创建时间',
    UNIQUE KEY uk_name_tenant (name, tenant_id),
    INDEX idx_tenant (tenant_id),
    INDEX idx_type (type),
    INDEX idx_status (status),
    CONSTRAINT pk_cluster PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='集群表';


CREATE TABLE IF NOT EXISTS cluster_status_history
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    cluster_id BIGINT UNSIGNED  COMMENT '集群ID',
    collect_time DATETIME COMMENT '采集时间',
    active_node_count INT NOT NULL COMMENT '活跃节点数',
    cpu_usage DOUBLE COMMENT 'CPU使用率',
    memory_usage DOUBLE COMMENT '内存使用率',
    queue_load_json TEXT COMMENT '队列负载',
    extend_json TEXT COMMENT '资源池扩展',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_collect_time (collect_time),
    CONSTRAINT pk_cluster_status_history PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='集群状态采集历史';


CREATE TABLE IF NOT EXISTS config
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '参数ID',
    code VARCHAR(64) NOT NULL UNIQUE COMMENT '参数编码',
    value TEXT COMMENT '参数值',
    `description` VARCHAR(255) COMMENT '参数说明',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT pk_config PRIMARY KEY (id),
    UNIQUE KEY uk_config_code (code)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ========= 数据源/第三方 =========
CREATE TABLE IF NOT EXISTS  data_source
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '数据源ID',
    name VARCHAR(64) COMMENT '数据源名称',
    type VARCHAR(32) COMMENT '数据源类型',
    connect_info TEXT COMMENT '连接信息(JSON/DSN)',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    `description` VARCHAR(255) COMMENT '描述',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT pk_data_source PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源表';


CREATE TABLE IF NOT EXISTS dept_role
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
    dept_id BIGINT UNSIGNED  NOT NULL COMMENT '部门ID',
    role_id BIGINT UNSIGNED  NOT NULL COMMENT '角色ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_dept (dept_id),
    UNIQUE KEY uk_dept_role (dept_id, role_id),
    CONSTRAINT pk_dept_role PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门-角色关联表';


CREATE TABLE IF NOT EXISTS dict
(
    id BIGINT UNSIGNED   AUTO_INCREMENT COMMENT '主键',
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_key VARCHAR(50) NOT NULL COMMENT '字典项KEY',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典项VALUE',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(255) COMMENT '描述',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_dict_type (dict_type),
    UNIQUE KEY uk_dict_type_key (dict_type, dict_key),
    CONSTRAINT pk_dict PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';


CREATE TABLE IF NOT EXISTS file
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '文件ID',
    name VARCHAR(128) COMMENT '文件名',
    url VARCHAR(255) COMMENT '存储路径/URL',
    type VARCHAR(64) COMMENT '文件类型',
    size BIGINT COMMENT '文件大小（字节）',
    user_id BIGINT UNSIGNED  COMMENT '上传用户ID',
    tenant_id BIGINT UNSIGNED  COMMENT '所属租户ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    INDEX idx_user (tenant_id),
    CONSTRAINT pk_file PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';


CREATE TABLE IF NOT EXISTS group_role
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
    group_id BIGINT UNSIGNED  NOT NULL COMMENT '组织ID',
    role_id BIGINT UNSIGNED  NOT NULL COMMENT '角色ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_role (role_id),
    INDEX idx_group (group_id),
    UNIQUE KEY uk_group_role (group_id, role_id),
    CONSTRAINT pk_group_role PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织-角色关联表';

CREATE TABLE IF NOT EXISTS integration_config
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(64) COMMENT '集成名称',
    type VARCHAR(32) COMMENT '集成类型',
    config_json TEXT COMMENT '配置参数(JSON)',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    status TINYINT COMMENT '启用状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_type (type),
    CONSTRAINT pk_integration_config PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方集成配置表';


CREATE TABLE IF NOT EXISTS job
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '任务ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '所属租户ID',
    cluster_id BIGINT UNSIGNED  NOT NULL COMMENT '所属集群ID',
    job_name VARCHAR(64) NOT NULL COMMENT '任务名',
    job_type VARCHAR(32) COMMENT '类型 (streaming/batch)',
    status VARCHAR(16) COMMENT '状态 (运行/异常/已停止等)',
    owner_id BIGINT UNSIGNED  COMMENT '负责人ID',
    start_time DATETIME COMMENT '启动时间',
    end_time DATETIME COMMENT '结束时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    UNIQUE KEY uk_job_name_tenant (job_name, tenant_id),
    INDEX idx_tenant (tenant_id),
    INDEX idx_cluster (cluster_id),
    INDEX idx_status (status),
    CONSTRAINT pk_job PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';


CREATE TABLE IF NOT EXISTS  job_alert_log
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    job_id BIGINT UNSIGNED  COMMENT '作业ID',
    job_name VARCHAR(128) COMMENT '作业名称',
    alert_type VARCHAR(64) COMMENT '报警类型',
    alert_msg VARCHAR(512) COMMENT '报警内容',
    alert_time DATETIME COMMENT '报警时间',
    status VARCHAR(32) COMMENT '状态 SENT/ACK',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_alert_time (alert_time),
    CONSTRAINT pk_job_alert_log PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业报警日志表';

CREATE TABLE IF NOT EXISTS job_alert_rule
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
    rule_name VARCHAR(128) COMMENT '规则名称',
    job_type VARCHAR(32) COMMENT '作业类型',
    alert_type VARCHAR(32) COMMENT '报警类型',
    condition_json TEXT COMMENT '阈值/表达式JSON',
    target VARCHAR(128) COMMENT '通知目标',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_job_type (job_type),
    INDEX idx_alert_type (alert_type),
    CONSTRAINT pk_job_alert_rule PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业报警规则表';


CREATE TABLE IF NOT EXISTS job_dependency
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    job_id BIGINT UNSIGNED  COMMENT '作业ID',
    dependency_job_id BIGINT UNSIGNED  COMMENT '依赖的作业ID',
    type VARCHAR(32) COMMENT '依赖类型',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_dependency (dependency_job_id),
    CONSTRAINT pk_job_dependency PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业依赖关系表';


CREATE TABLE IF NOT EXISTS job_diagnostic_log
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    job_id BIGINT UNSIGNED  COMMENT '作业ID',
    job_name VARCHAR(128) COMMENT '作业名称',
    log_time DATETIME COMMENT '日志时间',
    level VARCHAR(16) COMMENT '日志级别',
    content TEXT COMMENT '日志内容',
    trace_id VARCHAR(64) COMMENT 'traceId',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    INDEX idx_trace (trace_id),
    INDEX idx_level (level),
    INDEX idx_log_time (log_time),
    INDEX idx_job (job_id),
    CONSTRAINT pk_job_diagnostic_log PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业诊断日志表';

-- ========= 作业主表、实例、依赖、诊断 =========
CREATE TABLE IF NOT EXISTS job_info
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    job_name VARCHAR(128) NOT NULL COMMENT '作业名称',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    job_type VARCHAR(32) NOT NULL COMMENT '作业类型',
    project_code VARCHAR(64) COMMENT '项目编码',
    operator VARCHAR(64) NOT NULL COMMENT '操作人',
    source VARCHAR(32) NOT NULL COMMENT '来源',
    trace_id VARCHAR(64) NOT NULL UNIQUE COMMENT '注册幂等全局唯一ID',
    remark VARCHAR(256) COMMENT '备注',
    register_at BIGINT NOT NULL COMMENT '注册时间戳',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_jobinfo_jobname_tenant (job_name, tenant_id),
    UNIQUE KEY uk_jobinfo_traceid (trace_id),
    INDEX idx_jobinfo_tenant (tenant_id),
    INDEX idx_jobinfo_type (job_type),
    CONSTRAINT pk_job_info PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业定义表';

CREATE TABLE IF NOT EXISTS job_instance
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    job_id BIGINT UNSIGNED  NOT NULL COMMENT '作业定义ID',
    job_name VARCHAR(128) NOT NULL COMMENT '作业名称快照',
    engine_type VARCHAR(32) NOT NULL COMMENT '作业类型',
    cluster_id BIGINT UNSIGNED  NOT NULL COMMENT '运行集群ID',
    instance_code VARCHAR(128) NOT NULL COMMENT '实例唯一标识',
    status INT NOT NULL DEFAULT 0 COMMENT '运行状态',
    trigger_type VARCHAR(32) NOT NULL COMMENT '调度触发类型',
    start_time DATETIME COMMENT '启动时间',
    end_time DATETIME COMMENT '结束时间',
    config_json TEXT COMMENT '运行参数/快照',
    result_json TEXT COMMENT '结果/产物',
    fail_reason VARCHAR(512) COMMENT '失败原因',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '自动重试次数',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    operator VARCHAR(64) COMMENT '操作人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_jobinst_jobid (job_id),
    INDEX idx_jobinst_status (status),
    INDEX idx_jobinst_tenant (tenant_id),
    UNIQUE KEY uk_instance_code (instance_code),
    CONSTRAINT pk_job_instance PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业实例表';

CREATE TABLE IF NOT EXISTS job_log
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    job_id BIGINT UNSIGNED  NOT NULL COMMENT '任务ID',
    level VARCHAR(16) COMMENT '日志级别 (INFO/WARN/ERROR)',
    content TEXT COMMENT '日志内容',
    log_time DATETIME NOT NULL COMMENT '日志产生时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
    INDEX idx_joblog_tenant (tenant_id),
    INDEX idx_joblog_jobid (job_id),
    INDEX idx_joblog_level (level),
    INDEX idx_joblog_time (log_time),
    CONSTRAINT pk_job_log PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务日志表';

-- ========= 作业指标/报警/告警历史/规则 =========
CREATE TABLE IF NOT EXISTS job_metric
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    job_id BIGINT UNSIGNED  NOT NULL COMMENT '任务ID',
    metric_key VARCHAR(64) NOT NULL COMMENT '指标类型（如cpu、mem、lag等）',
    metric_value DOUBLE COMMENT '指标值',
    metric_time DATETIME NOT NULL COMMENT '采集时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 (0=正常, 1=删除)',
    INDEX idx_jobmetric_tenant (tenant_id),
    INDEX idx_jobmetric_jobid (job_id),
    INDEX idx_jobmetric_key (metric_key),
    INDEX idx_jobmetric_time (metric_time),
    INDEX idx_jobmetric_jobkeytime (job_id, metric_key, metric_time),
    CONSTRAINT pk_job_metric PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务指标表';


CREATE TABLE `job_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_id` BIGINT UNSIGNED NOT NULL COMMENT '作业ID',          -- 匹配 job.id
  `tenant_id` BIGINT UNSIGNED NOT NULL COMMENT '租户ID',        -- 匹配 permission.tenant_id
  `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID（业务标识）',   -- 逻辑关联 user（不外键）
  `permission_code` VARCHAR(50) NOT NULL COMMENT '权限编码',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0=正常 1=删除',
  PRIMARY KEY (`id`),
    -- 唯一约束：同一作业 + 同一用户 + 同一权限 只能出现一次
    CONSTRAINT `uk_job_user_permission` UNIQUE (`job_id`, `user_id`, `permission_code`),
    -- 复合外键：permission(code, tenant_id) 才是唯一键
    CONSTRAINT `fk_jobperm_perm_tenant`
        FOREIGN KEY (`permission_code`, `tenant_id`)
            REFERENCES `permission` (`code`, `tenant_id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    -- 外键到 job
     CONSTRAINT `fk_jobperm_job`
         FOREIGN KEY (`job_id`)
             REFERENCES `job` (`id`)
             ON DELETE CASCADE ON UPDATE CASCADE,
    -- 索引
    INDEX `idx_jobperm_job` (`job_id`),
    INDEX `idx_jobperm_user` (`user_id`),
    INDEX `idx_jobperm_perm` (`permission_code`),
    INDEX `idx_jobperm_tenant` (`tenant_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci
  COMMENT='作业-用户-权限三元组';



CREATE TABLE IF NOT EXISTS  label
(
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '标签名称',
    color VARCHAR(20) COMMENT '颜色，可选',
    type VARCHAR(20) COMMENT '标签类型，可选',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0=正常 1=删除',
    UNIQUE KEY uk_label_tenant_name (tenant_id, name),
    INDEX idx_label_tenant_type (tenant_id, type),
    INDEX idx_label_name (name),
    CONSTRAINT pk_label PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';


CREATE TABLE IF NOT EXISTS label_login_history (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
   label_id BIGINT UNSIGNED  NOT NULL COMMENT '标签ID',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   login_time DATETIME NOT NULL COMMENT '登录时间',
   ip_address VARCHAR(64) COMMENT '登录IP地址 (支持IPv6)',
   tenant_id BIGINT NOT NULL COMMENT '租户ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0=正常 1=删除',
   INDEX idx_llh_tenant_user_time (tenant_id, user_id, login_time),
   INDEX idx_llh_label_time (label_id, login_time),
   INDEX idx_loginhis_ip(ip_address),
   INDEX idx_loginhis_user_time(user_id, login_time),
   CONSTRAINT pk_label_login_history PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签登录历史表';

CREATE TABLE IF NOT EXISTS  `login_history` (
                                 `id` BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                                 `user_id` BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
                                 `tenant_id` BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
                                 `login_type` VARCHAR(32) DEFAULT NULL COMMENT '登录方式',
                                 `provider` VARCHAR(32) DEFAULT NULL COMMENT '三方渠道/平台',
                                 `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP',
                                 `device_info` VARCHAR(128) DEFAULT NULL COMMENT '设备信息',
                                 `login_time` DATETIME NOT NULL COMMENT '登录时间',
                                 `success_flag` TINYINT DEFAULT 1 NOT NULL COMMENT '1成功 0失败',
                                 `fail_reason` VARCHAR(128) DEFAULT NULL COMMENT '失败原因',
                                 `is_deleted` TINYINT DEFAULT 0 NOT NULL COMMENT '0=正常 1=删除',
                                 INDEX idx_loginhis_user_time (user_id, login_time),
                                 INDEX idx_loginhis_ip (ip_address),
                                 CONSTRAINT pk_login_history PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE IF NOT EXISTS metric_dashboard (
  id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '大盘ID',
  name VARCHAR(64) NOT NULL COMMENT '名称',
  description VARCHAR(256) COMMENT '描述',
  tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
  config TEXT COMMENT '大盘配置(JSON)',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
  create_time DATETIME COMMENT '创建时间',
  UNIQUE KEY uk_dashboard_tenant_name (tenant_id, name),
  INDEX idx_dashboard_tenant (tenant_id),
  INDEX idx_dashboard_name (name),
  INDEX idx_dashboard_ctime (create_time),
  CONSTRAINT pk_metric_dashboard PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标大盘';

CREATE TABLE IF NOT EXISTS node (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '节点ID',
   name VARCHAR(64) NOT NULL COMMENT '节点名称',
   type VARCHAR(32) COMMENT '节点类型',
   ip VARCHAR(64) NOT NULL COMMENT '节点IP',
   cluster_id BIGINT UNSIGNED  NOT NULL COMMENT '关联集群ID',
   status INT NOT NULL COMMENT '节点状态',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   create_time DATETIME COMMENT '注册时间',
   UNIQUE KEY uk_node_cluster_name (cluster_id, name),
   UNIQUE KEY uk_node_ip (ip),
   INDEX idx_node_cluster (cluster_id),
   INDEX idx_node_status (status),
   CONSTRAINT pk_node PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节点表';



CREATE TABLE IF NOT EXISTS node_health (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   node_id BIGINT UNSIGNED  NOT NULL COMMENT '节点ID',
   health_status VARCHAR(16) NOT NULL COMMENT '健康状态（HEALTHY/UNHEALTHY/WARNING）',
   check_time DATETIME NOT NULL COMMENT '健康检测时间',
   message VARCHAR(256) COMMENT '状态描述',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0=正常 1=删除',
   INDEX idx_nh_tenant_node_time (tenant_id, node_id, check_time),
   INDEX idx_nh_health_status (health_status),
   CONSTRAINT pk_node_health PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节点健康状态表';


CREATE TABLE IF NOT EXISTS notification (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '通知ID',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '接收用户ID',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   title VARCHAR(128) NOT NULL COMMENT '通知标题',
   content TEXT COMMENT '通知内容',
   type VARCHAR(32) COMMENT '通知类型',
   is_read TINYINT NOT NULL DEFAULT 0 COMMENT '已读标志 0=未读 1=已读',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   create_time DATETIME NOT NULL COMMENT '发送时间',
   INDEX idx_notify_user_tenant_read (user_id, tenant_id, is_read),
   INDEX idx_notify_time (create_time),
   UNIQUE KEY uk_user_tenant (user_id, tenant_id),
   CONSTRAINT pk_notification PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';


CREATE TABLE IF NOT EXISTS operation_template (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '模板ID',
   name VARCHAR(128) NOT NULL COMMENT '模板名称',
   type VARCHAR(32) COMMENT '模板类型',
   content TEXT COMMENT '模板内容',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   create_time DATETIME COMMENT '创建时间',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   UNIQUE KEY uk_optemplate_tenant_type_name (tenant_id, type, name),
   INDEX idx_optemplate_tenant_type (tenant_id, type),
   CONSTRAINT pk_operation_template PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作模板表';


CREATE TABLE IF NOT EXISTS permission (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '权限ID',
   code VARCHAR(50) NOT NULL COMMENT '权限编码(全局唯一，如JOB_OWNER/ADMIN/VIEWER)',
   name VARCHAR(50) NOT NULL COMMENT '权限名称',
   `description` VARCHAR(100) COMMENT '权限描述',
   type VARCHAR(16) NOT NULL COMMENT '权限类型(MENU/BUTTON/API)',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID，多租户隔离',
   enabled TINYINT NOT NULL DEFAULT 0 COMMENT '启停标记 0=禁用 1=启用',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标记 0=正常 1=删除',
   UNIQUE KEY uk_permission_code_tenant (code, tenant_id),
   INDEX idx_permission_type (type),
   INDEX idx_permission_tenant (tenant_id),
   CONSTRAINT pk_permission PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限点表';


-- 菜单表（仅结构元数据，不含权限字段）
CREATE TABLE IF NOT EXISTS `menu` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                      `parent_id` BIGINT NULL COMMENT '父ID，NULL为根',
                                      `menu_key` VARCHAR(64) NOT NULL COMMENT '前端唯一key',
                                      `path` VARCHAR(255) NOT NULL COMMENT '前端路由',
                                      `title_zh` VARCHAR(128) NOT NULL,
                                      `title_en` VARCHAR(128) NOT NULL,
                                      `icon` VARCHAR(64) NULL,
                                      `order_num` INT NULL,
                                      `required_code` VARCHAR(128) NULL COMMENT '显示该菜单所需的权限码（为空=默认不显示）',
                                      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0=正常 1=删除',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_menu_key` (`menu_key`),
                                      KEY `idx_parent` (`parent_id`),
                                      KEY `idx_order` (`order_num`),
                                      KEY `idx_required_code` (`required_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单';

CREATE TABLE IF NOT EXISTS profile (
  id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
  user_id BIGINT UNSIGNED  NOT NULL UNIQUE COMMENT '用户ID',
  real_name VARCHAR(64) COMMENT '真实姓名',
  avatar_url VARCHAR(256) COMMENT '头像URL',
  gender INT COMMENT '性别',
  department VARCHAR(64) COMMENT '部门',
  position VARCHAR(64) COMMENT '岗位',
  signature VARCHAR(128) COMMENT '个性签名',
  phone VARCHAR(32) COMMENT '手机',
  email VARCHAR(64) COMMENT '邮箱',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
  create_time DATETIME COMMENT '创建时间',
  UNIQUE KEY uk_profile_user_id (user_id),
  CONSTRAINT pk_profile PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户个人信息表';


CREATE TABLE IF NOT EXISTS `resource` (
    id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '资源ID',
    name VARCHAR(128) NOT NULL COMMENT '资源名称',
    type VARCHAR(32) COMMENT '资源类型',
    path VARCHAR(256) COMMENT '资源路径/URL',
    tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '归属租户ID',
    `description` VARCHAR(256) COMMENT '描述',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
    create_time DATETIME COMMENT '创建时间',
    UNIQUE KEY uk_resource_tenant_name (tenant_id, name),
    INDEX idx_resource_tenant_type (tenant_id, type),
    CONSTRAINT pk_resource PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源管理表';


CREATE TABLE IF NOT EXISTS resource_group (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '分组ID',
   name VARCHAR(128) NOT NULL COMMENT '分组名称',
   type VARCHAR(32) COMMENT '分组类型',
   parent_id BIGINT UNSIGNED  COMMENT '父分组ID',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   `description` VARCHAR(256) COMMENT '描述',
   create_time DATETIME COMMENT '创建时间',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   UNIQUE KEY uk_resourcegroup_tenant_name (tenant_id, name),
   INDEX idx_resourcegroup_tenant_type (tenant_id, type),
   INDEX idx_resourcegroup_parent (parent_id),
   CONSTRAINT pk_resource_group PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源分组表';


CREATE TABLE IF NOT EXISTS resource_label (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   resource_id BIGINT UNSIGNED  NOT NULL COMMENT '资源ID',
   label_id BIGINT UNSIGNED  NOT NULL COMMENT '标签ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_resource_label (resource_id, label_id),
   INDEX idx_resource_label_resource (resource_id),
   INDEX idx_resource_label_label (label_id),
   CONSTRAINT pk_resource_label PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源-标签关联表';


CREATE TABLE IF NOT EXISTS `role` (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '角色ID',
   code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
   name VARCHAR(50) NOT NULL COMMENT '角色名称',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID，多租户隔离',
   remark VARCHAR(100) COMMENT '角色描述',
   created_at DATETIME COMMENT '创建时间',
   updated_at DATETIME COMMENT '更新时间',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标记 0=正常 1=删除',
   INDEX idx_tenant (tenant_id),
   UNIQUE KEY uk_code (code),
   CONSTRAINT pk_role PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS role_data_scope (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   role_id BIGINT UNSIGNED  NOT NULL COMMENT '角色ID',
   data_scope_id BIGINT UNSIGNED  NOT NULL COMMENT '数据范围ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_role_data_scope (role_id, data_scope_id),
   INDEX idx_role_data_scope_role (role_id),
   INDEX idx_role_data_scope_scope (data_scope_id),
   CONSTRAINT pk_role_data_scope PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-数据权限范围关联表';


CREATE TABLE IF NOT EXISTS role_menu (
  id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
  role_id BIGINT UNSIGNED  NOT NULL COMMENT '角色ID',
  menu_id BIGINT UNSIGNED  NOT NULL COMMENT '菜单ID',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
  UNIQUE KEY uk_role_menu (role_id, menu_id),
  INDEX idx_role_menu_role (role_id),
  INDEX idx_role_menu_menu (menu_id),
  CONSTRAINT pk_role_menu PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';


-- 角色-权限关联（以权限码为外键，兼容多租户）
CREATE TABLE IF NOT EXISTS role_permission (
    id               BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键',
    role_id          BIGINT UNSIGNED NOT NULL      COMMENT '角色ID',
    permission_code  VARCHAR(50)     NOT NULL      COMMENT '权限编码(与permission.code一致)',
    tenant_id        BIGINT UNSIGNED NOT NULL      COMMENT '租户ID',
    is_deleted       TINYINT         NOT NULL DEFAULT 0 COMMENT '软删除 0=正常 1=删除',
    CONSTRAINT pk_role_permission PRIMARY KEY (id),
    -- 唯一：同一角色下，同一权限码只能出现一次（是否带tenant_id看你们想不想允许“跨租户同code”的重复；
    -- 若role已归属唯一tenant，(role_id, permission_code) 足够；保守些可三列都加）
    UNIQUE KEY uk_role_permission (role_id, permission_code),
    KEY idx_rp_role      (role_id),
    KEY idx_rp_perm_code (permission_code),
    KEY idx_rp_tenant    (tenant_id),
    -- 外键：role
    CONSTRAINT fk_rp_role
    FOREIGN KEY (role_id) REFERENCES role(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    -- 外键：permission (复合外键 -> 依赖 permission 上的 (code, tenant_id) 唯一键)
    CONSTRAINT fk_rp_permission
    FOREIGN KEY (permission_code, tenant_id)
    REFERENCES permission(code, tenant_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    -- 可选：租户存在性约束（如果有 tenant 表）
    CONSTRAINT fk_rp_tenant
    FOREIGN KEY (tenant_id) REFERENCES tenant(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联（按权限码）';



CREATE TABLE IF NOT EXISTS sys_param (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '参数ID',
   name VARCHAR(64) COMMENT '参数名称',
   code VARCHAR(64) NOT NULL UNIQUE COMMENT '参数编码（全局唯一）',
   value TEXT COMMENT '参数值',
   type VARCHAR(32) COMMENT '参数类型',
   `description` VARCHAR(256) COMMENT '备注',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   update_time DATETIME COMMENT '更新时间',
   UNIQUE KEY uk_sys_param_code (code),
   INDEX idx_sys_param_type (type),
   INDEX idx_sys_param_code (code),
   CONSTRAINT pk_sys_param PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';



CREATE TABLE IF NOT EXISTS system_settings (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
   code VARCHAR(64) NOT NULL UNIQUE COMMENT '设置项编码（唯一）',
   value TEXT COMMENT '设置项值',
   `description` VARCHAR(256) COMMENT '备注',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   UNIQUE KEY uk_system_settings_code (code),
   INDEX idx_system_settings_code (code),
   CONSTRAINT pk_system_settings PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全局系统设置表';



CREATE TABLE IF NOT EXISTS tag (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '标签ID',
   name VARCHAR(64) NOT NULL COMMENT '标签名',
   color VARCHAR(32) COMMENT '标签颜色',
   type VARCHAR(32) COMMENT '标签类型',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   create_time DATETIME COMMENT '创建时间',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除标志 0=正常 1=删除',
   UNIQUE KEY uk_tag_name_tenant (name, tenant_id),
   INDEX idx_tag_tenant (tenant_id),
   INDEX idx_tag_name (name),
   CONSTRAINT pk_tag PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';


CREATE TABLE IF NOT EXISTS  `tenant` (
                          `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '租户ID',
                          `code` VARCHAR(64) NOT NULL UNIQUE COMMENT '租户编码',
                          `name` VARCHAR(128) NOT NULL COMMENT '租户名称',
                          `contact` VARCHAR(100) DEFAULT NULL COMMENT '联系人',
                          `contact_info` VARCHAR(128) DEFAULT NULL COMMENT '联系人方式（邮箱/电话）',
                          `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
                          `status` TINYINT DEFAULT 1 NOT NULL COMMENT '状态 1启用 0禁用',
                          `is_deleted` TINYINT DEFAULT 0 NOT NULL COMMENT '软删除 0=正常 1=删除',
                          `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          UNIQUE KEY uk_tenant_code (code),
                          INDEX idx_tenant_code (code),
                          INDEX idx_tenant_status (status),
                          CONSTRAINT pk_tenant PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';



CREATE TABLE IF NOT EXISTS tenant_config (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   config_key VARCHAR(64) NOT NULL COMMENT '配置项Key',
   config_value TEXT COMMENT '配置值',
   `description` VARCHAR(256) COMMENT '说明',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   create_time DATETIME COMMENT '创建时间',
   update_time DATETIME COMMENT '更新时间',
   UNIQUE KEY uk_tenant_config_key (tenant_id, config_key),
   INDEX idx_tenant_config_tenant (tenant_id),
   INDEX idx_tenant_config_key (config_key),
   CONSTRAINT pk_tenant_config PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户配置表';


CREATE TABLE IF NOT EXISTS tenant_resource (
   id BIGINT AUTO_INCREMENT COMMENT '主键',
   tenant_id BIGINT NOT NULL COMMENT '租户ID',
   resource_id BIGINT NOT NULL COMMENT '资源ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_tenant_resource (tenant_id, resource_id),
   INDEX idx_tenant_resource_tenant (tenant_id),
   INDEX idx_tenant_resource_resource (resource_id),
   CONSTRAINT pk_tenant_resource PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户-资源关联表';


CREATE TABLE IF NOT EXISTS ticket (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
   tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
   alert_id BIGINT UNSIGNED  NOT NULL COMMENT '关联报警事件ID',
   handler_id BIGINT UNSIGNED  COMMENT '处理人ID',
   status TINYINT NOT NULL DEFAULT 0 COMMENT '状态(0未处理1处理中2关闭)',
   note VARCHAR(255) COMMENT '处理备注',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
   update_time DATETIME COMMENT '更新时间',
   create_time DATETIME COMMENT '创建时间',
   INDEX idx_ticket_tenant (tenant_id),
   INDEX idx_ticket_alert (alert_id),
   CONSTRAINT pk_ticket PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';


CREATE TABLE IF NOT EXISTS `user` (
                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `tenant_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '租户ID，多租户隔离',
                        `username` VARCHAR(50) NOT NULL COMMENT '登录名/用户名（唯一）',
                        `password` VARCHAR(128) NOT NULL COMMENT '密码Hash',
                        `nickname` VARCHAR(128) DEFAULT NULL COMMENT '昵称',
                        `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                        `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                        `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                        `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
                        `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0=正常 1=已删除',
                        `sso_id` VARCHAR(128) DEFAULT NULL COMMENT '三方SSO唯一标识/开放平台id（可选）',
                        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                        `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        CONSTRAINT pk_user PRIMARY KEY (id),
                        UNIQUE KEY `uk_username` (`username`),
                        UNIQUE KEY `uk_sso_id` (`sso_id`),
                        KEY `idx_tenant_id` (`tenant_id`),
                        KEY `idx_email` (`email`),
                        KEY `idx_phone` (`phone`),
                        KEY `idx_status` (`status`),
                        CONSTRAINT `fk_user_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`)
                            ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';



CREATE TABLE IF NOT EXISTS user_api (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   api_id BIGINT UNSIGNED  NOT NULL COMMENT 'API ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_user_api (user_id, api_id),
   INDEX idx_user_api_user (user_id),
   INDEX idx_user_api_api (api_id),
   CONSTRAINT pk_user_api PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-API权限关联表';



CREATE TABLE IF NOT EXISTS user_department (
   id BIGINT AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   department_id BIGINT UNSIGNED  NOT NULL COMMENT '部门ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_user_department (user_id, department_id),
   INDEX idx_user_dept_user (user_id),
   INDEX idx_user_dept_dept (department_id),
   CONSTRAINT pk_user_department PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-部门关联表';


CREATE TABLE IF NOT EXISTS user_group (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   group_id BIGINT UNSIGNED  NOT NULL COMMENT '分组ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_user_group (user_id, group_id),
   INDEX idx_user_group_user (user_id),
   INDEX idx_user_group_group (group_id),
   CONSTRAINT pk_user_group PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-分组关联表';


CREATE TABLE IF NOT EXISTS user_post (
   id BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   post_id BIGINT UNSIGNED  NOT NULL COMMENT '岗位ID',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   UNIQUE KEY uk_user_post (user_id, post_id),
   INDEX idx_user_post_user (user_id),
   INDEX idx_user_post_post (post_id),
   CONSTRAINT pk_user_post PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-岗位关联表';

CREATE TABLE IF NOT EXISTS user_role (
   id BIGINT AUTO_INCREMENT COMMENT '主键',
   user_id BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
   role_id BIGINT UNSIGNED  NOT NULL COMMENT '角色ID',
   tenant_id BIGINT UNSIGNED  COMMENT '租户ID',
   assign_time DATETIME COMMENT '分配时间',
   is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0正常 1删除',
   INDEX idx_user_role_user (user_id),
   INDEX idx_user_role_role (role_id),
   INDEX idx_user_role_tenant (tenant_id),
   UNIQUE KEY uk_user_role (user_id, role_id),
   CONSTRAINT pk_user_role PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

CREATE TABLE IF NOT EXISTS `user_tenant` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `user_id` BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
                               `tenant_id` BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
                               `role` VARCHAR(32) DEFAULT NULL COMMENT '授权角色，如OWNER/ADMIN/USER，支持扩展',
                               `is_default` TINYINT DEFAULT 0 NOT NULL COMMENT '是否默认租户（1=默认 0=非默认）',
                               `is_deleted` TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除 0正常 1删除',
                               `create_time` DATETIME NOT NULL COMMENT '创建时间',
                               `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
                               CONSTRAINT pk_user_tenant PRIMARY KEY (id),
                               UNIQUE KEY `uk_user_tenant` (`user_id`,`tenant_id`),
                               KEY `idx_user_tenant_user` (`user_id`),
                               KEY `idx_user_tenant_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-租户关联表';


CREATE TABLE IF NOT EXISTS ops_task (
 id BIGINT AUTO_INCREMENT COMMENT '主键ID',
 tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
 name VARCHAR(128) NOT NULL COMMENT '任务名称',
 type VARCHAR(32) NOT NULL COMMENT '任务类型（如backup/expand/upgrade等）',
 status VARCHAR(16) NOT NULL COMMENT '任务执行状态（PENDING/RUNNING/SUCCESS/FAILED等）',
 description VARCHAR(255) COMMENT '任务描述',
 created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 executed_at DATETIME COMMENT '执行时间',
 is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0=正常 1=删除',
 PRIMARY KEY (id),
 INDEX idx_tenant (tenant_id),
 INDEX idx_status (status),
 INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运维自动化任务表';

CREATE TABLE IF NOT EXISTS notify_channel (
  id BIGINT AUTO_INCREMENT COMMENT '主键ID',
  type VARCHAR(32) NOT NULL COMMENT '渠道类型(email/dingding/wechat/sms等)',
  config TEXT NOT NULL COMMENT '配置内容(JSON字符串)',
  name VARCHAR(128) NOT NULL COMMENT '渠道名称',
  enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用 0禁用 1启用',
  tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0正常 1删除',
  PRIMARY KEY (id),
  INDEX idx_tenant (tenant_id),
  INDEX idx_enabled (enabled),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知渠道表';

CREATE TABLE IF NOT EXISTS org_node (
  id BIGINT AUTO_INCREMENT COMMENT '节点ID',
  parent_id BIGINT UNSIGNED  DEFAULT NULL COMMENT '父节点ID',
  name VARCHAR(128) NOT NULL COMMENT '节点名称',
  type VARCHAR(32) COMMENT '节点类型(公司/部门/组等)',
  sort_order INT DEFAULT 0 COMMENT '排序序号',
  tenant_id BIGINT UNSIGNED  NOT NULL COMMENT '租户ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除 0正常 1删除',
  PRIMARY KEY (id),
  INDEX idx_tenant (tenant_id),
  INDEX idx_parent (parent_id),
  INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构树节点表';

-- ===========================
-- SSO 第三方授权账号表（如微信、钉钉、GitHub登录）
-- ===========================
CREATE TABLE IF NOT EXISTS  oauth_account (
                                   id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                   user_id BIGINT UNSIGNED NOT NULL COMMENT '本地用户ID',
                                   provider VARCHAR(32) NOT NULL COMMENT '平台类型(如 wechat/dingding/github/google)',
                                   openid VARCHAR(64) NOT NULL COMMENT '第三方平台 openid',
                                   unionid VARCHAR(64) DEFAULT NULL COMMENT '第三方平台 unionid',
                                   access_token VARCHAR(256) DEFAULT NULL COMMENT '授权token',
                                   expire_time DATETIME DEFAULT NULL COMMENT 'Token过期时间',
                                   create_time DATETIME NOT NULL COMMENT '授权时间',
                                   update_time DATETIME DEFAULT NULL COMMENT '更新时间',
                                   is_deleted INT NOT NULL DEFAULT 0 COMMENT '软删除 0正常 1删除',
                                   KEY idx_user_id (user_id),
                                   KEY idx_provider (provider),
                                   KEY idx_openid (openid),
                                   KEY idx_unionid (unionid),
                                   CONSTRAINT fk_oauth_user FOREIGN KEY (user_id) REFERENCES user(id)
) COMMENT='第三方授权账号绑定表';

-- ===========================
-- SSO 会话/Token表（可选，便于集群JWT黑名单/Token续签等）
-- ===========================
CREATE TABLE IF NOT EXISTS  sso_token (
                           id              BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                           user_id         BIGINT UNSIGNED  NOT NULL COMMENT '用户ID',
                           token           VARCHAR(512) NOT NULL COMMENT 'Token字符串',
                           issued_at       DATETIME NOT NULL COMMENT '签发时间',
                           expires_at      DATETIME NOT NULL COMMENT '过期时间',
                           revoked         INT DEFAULT 0 COMMENT '是否已吊销',
                           create_time     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           INDEX idx_token_user (user_id),
                           INDEX idx_token_value (token(128)),
                           CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SSO会话Token记录表';

CREATE TABLE IF NOT EXISTS user_token_state (
                                                user_id       BIGINT UNSIGNED NOT NULL,
                                                token_version INT NOT NULL DEFAULT 1,
                                                updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                PRIMARY KEY (user_id),
                                                CONSTRAINT fk_user_token_state_user FOREIGN KEY (user_id)
                                                    REFERENCES `user` (id)
                                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户 Token 状态表';
-- ===========================
-- 初始化/演示数据可按需补充
-- ===========================
































































































































































