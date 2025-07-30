--所有表含 tenant_id 字段，默认强租户隔离，安全高可用。
--逻辑删除（is_deleted）与时间戳，适配软删与数据同步/备份。
--字段注释齐全，数据类型考虑可扩展性（如TEXT存复杂JSON、ENUM建议用varchar以兼容未来扩展）。
--主键/唯一键、索引建议生产实际业务补全。
--适合直接Flyway/Liquibase等工具落地生产，后续可按需求细化分区、表空间等特性。

-- ================================
-- 1. 租户相关表
-- ================================

CREATE TABLE tenant (
                        id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID，主键自增',
                        name          VARCHAR(128) NOT NULL COMMENT '租户名称',
                        code          VARCHAR(64)  NOT NULL UNIQUE COMMENT '租户编码，平台唯一',
                        contact       VARCHAR(64)  DEFAULT NULL COMMENT '联系人姓名',
                        phone         VARCHAR(32)  DEFAULT NULL COMMENT '联系电话',
                        email         VARCHAR(64)  DEFAULT NULL COMMENT '邮箱',
                        status        TINYINT      DEFAULT 1 COMMENT '状态（1:启用，0:禁用）',
                        remark        VARCHAR(256) DEFAULT NULL COMMENT '备注',
                        is_deleted    TINYINT      DEFAULT 0 COMMENT '逻辑删除标记（0:正常，1:删除）',
                        created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        UNIQUE KEY uk_code(code),
                        KEY idx_status(status),
                        KEY idx_deleted(is_deleted)
) COMMENT='【多租户】租户基础信息表，平台核心隔离单元';

CREATE TABLE tenant_config (
                               id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                               tenant_id     BIGINT      NOT NULL COMMENT '租户ID，关联tenant表',
                               config_key    VARCHAR(64) NOT NULL COMMENT '配置项Key（唯一）',
                               config_value  TEXT        COMMENT '配置值（JSON格式）',
                               is_deleted    TINYINT     DEFAULT 0 COMMENT '逻辑删除标记',
                               created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               updated_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               UNIQUE KEY idx_tenant_key(tenant_id, config_key)
) COMMENT='租户自定义参数/功能开关/配置信息';

-- ================================
-- 2. 用户、角色、权限、关联表
-- ================================

CREATE TABLE user (
                      id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                      username   VARCHAR(64)  NOT NULL COMMENT '用户名，平台唯一',
                      password   VARCHAR(128) NOT NULL COMMENT '用户密码（加密）',
                      real_name  VARCHAR(64) COMMENT '用户真实姓名',
                      email      VARCHAR(64) COMMENT '邮箱',
                      phone      VARCHAR(32) COMMENT '手机号',
                      tenant_id  BIGINT      NOT NULL COMMENT '所属租户ID',
                      status     TINYINT DEFAULT 1 COMMENT '用户状态（1:启用，0:禁用）',
                      is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      UNIQUE KEY uk_username_tenant(username, tenant_id),
                      KEY idx_tenant(tenant_id),
                      KEY idx_status(status),
                      KEY idx_deleted(is_deleted)
) COMMENT='平台用户表，支持多租户';

CREATE TABLE role (
                      id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
                      code       VARCHAR(64) NOT NULL COMMENT '角色编码',
                      name       VARCHAR(64) NOT NULL COMMENT '角色名称',
                      tenant_id  BIGINT      NOT NULL COMMENT '租户ID',
                      remark     VARCHAR(256) COMMENT '角色备注说明',
                      is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      UNIQUE KEY uk_code(code),
                      KEY idx_tenant(tenant_id)
) COMMENT='角色表，用于权限分组';

-- 权限点表（可扩展MENU/BUTTON/API，支持多租户隔离）
CREATE TABLE permission (
                            id          BIGSERIAL PRIMARY KEY,                        -- 主键
                            code        VARCHAR(50) NOT NULL,                         -- 权限编码
                            name        VARCHAR(50) NOT NULL,                         -- 权限名称
                            desc        VARCHAR(100),                                 -- 权限描述
                            type        VARCHAR(16) NOT NULL,                         -- 权限类型
                            tenant_id   BIGINT NOT NULL,                              -- 租户ID
                            is_deleted  INTEGER NOT NULL DEFAULT 0,                   -- 软删除
                            CONSTRAINT uk_permission_code_tenant UNIQUE(code, tenant_id)
);

COMMENT ON TABLE permission IS '权限点表';
COMMENT ON COLUMN permission.id IS '主键';
COMMENT ON COLUMN permission.code IS '权限编码';
COMMENT ON COLUMN permission.name IS '权限名称';
COMMENT ON COLUMN permission.desc IS '权限描述';
COMMENT ON COLUMN permission.type IS '权限类型（MENU/BUTTON/API）';
COMMENT ON COLUMN permission.tenant_id IS '租户ID';
COMMENT ON COLUMN permission.is_deleted IS '软删除';


CREATE TABLE user_role (
                           id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                           user_id    BIGINT NOT NULL COMMENT '用户ID',
                           role_id    BIGINT NOT NULL COMMENT '角色ID',
                           is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           UNIQUE KEY uk_user_role(user_id, role_id),
                           KEY idx_user(user_id),
                           KEY idx_role(role_id)
) COMMENT='用户-角色 多对多关联';

CREATE TABLE role_permission (
                                 id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                                 role_id    BIGINT NOT NULL COMMENT '角色ID',
                                 permission_id BIGINT NOT NULL COMMENT '权限ID',
                                 is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 UNIQUE KEY uk_role_permission(role_id, permission_id),
                                 KEY idx_role(role_id),
                                 KEY idx_permission(permission_id)
) COMMENT='角色-权限 多对多关联';

-- ================================
-- 3. 计算集群、作业与作业状态
-- ================================

CREATE TABLE cluster (
                         id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '集群ID',
                         cluster_name  VARCHAR(128) NOT NULL COMMENT '集群名称',
                         type          VARCHAR(32)  NOT NULL COMMENT '集群类型（YARN/K8S/Standalone/Spark）',
                         endpoint      VARCHAR(256) NOT NULL COMMENT '接入地址/管理端点',
                         status        VARCHAR(32)  NOT NULL COMMENT '集群状态',
                         tenant_id     BIGINT       NOT NULL COMMENT '租户ID',
                         config        TEXT         COMMENT '集群配置信息（JSON）',
                         is_deleted    TINYINT      DEFAULT 0 COMMENT '逻辑删除',
                         created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         UNIQUE KEY uk_name_tenant(cluster_name, tenant_id),
                         KEY idx_type(type),
                         KEY idx_status(status),
                         KEY idx_tenant(tenant_id)
) COMMENT='Flink/Spark/Yarn等大数据计算集群注册表';

CREATE TABLE job (
                     id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '作业ID',
                     job_name     VARCHAR(128) NOT NULL COMMENT '作业名称',
                     cluster_id   BIGINT NOT NULL COMMENT '所属集群ID',
                     tenant_id    BIGINT NOT NULL COMMENT '所属租户ID',
                     owner_id     BIGINT COMMENT '作业负责人ID',
                     job_type     VARCHAR(32) NOT NULL COMMENT '作业类型（Streaming/Batch）',
                     status       VARCHAR(32) NOT NULL COMMENT '作业状态（RUNNING/FAILED/FINISHED）',
                     schedule_type VARCHAR(32) COMMENT '调度类型（手动/定时/自动）',
                     priority     INT DEFAULT 0 COMMENT '作业优先级',
                     entry_class  VARCHAR(256) COMMENT '主类名',
                     entry_params TEXT COMMENT '启动参数',
                     config       TEXT COMMENT '作业配置',
                     start_time   DATETIME COMMENT '启动时间',
                     end_time     DATETIME COMMENT '结束时间',
                     is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                     created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                     updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                     KEY idx_name_tenant(job_name, tenant_id),
                     KEY idx_cluster(cluster_id),
                     KEY idx_status(status),
                     KEY idx_tenant(tenant_id)
) COMMENT='Flink/Spark/批流作业表';

CREATE TABLE job_status_history (
                                    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                                    job_id       BIGINT NOT NULL COMMENT '作业ID',
                                    status       VARCHAR(32) NOT NULL COMMENT '变更后状态',
                                    message      VARCHAR(512) COMMENT '状态变更描述',
                                    changed_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '状态变更时间',
                                    operator     VARCHAR(64) COMMENT '操作人',
                                    audit_source VARCHAR(64) COMMENT '操作来源',
                                    is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                    KEY idx_job(job_id),
                                    KEY idx_status(status)
) COMMENT='作业运行状态变更历史';

-- ================================
-- 4. 指标/报警/链路审计/日志
-- ================================

CREATE TABLE metrics_event (
                               id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                               job_id       BIGINT NOT NULL COMMENT '作业ID',
                               metric_key   VARCHAR(64) NOT NULL COMMENT '指标Key',
                               metric_value VARCHAR(128) NOT NULL COMMENT '指标值',
                               event_time   TIMESTAMP NOT NULL COMMENT '指标采集时间',
                               tenant_id    BIGINT NOT NULL COMMENT '租户ID',
                               notify_channels VARCHAR(128) COMMENT '通知渠道（如wechat,sms,email）',
                               auto_recover TINYINT DEFAULT 0 COMMENT '是否自动恢复',
                               priority     INT DEFAULT 0 COMMENT '任务优先级',
                               audit_at     TIMESTAMP COMMENT '审计时间',
                               operator     VARCHAR(64) COMMENT '操作人',
                               audit_source VARCHAR(64) COMMENT '审计来源',
                               is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                               created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               KEY idx_job(job_id),
                               KEY idx_tenant(tenant_id),
                               KEY idx_time(event_time)
) COMMENT='作业指标/报警事件流表，支持实时多通道推送';

CREATE TABLE job_alert_log (
                               id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报警日志ID',
                               job_id       BIGINT NOT NULL COMMENT '作业ID',
                               alert_rule_id BIGINT COMMENT '报警规则ID',
                               alert_type   VARCHAR(64) NOT NULL COMMENT '报警类型',
                               alert_level  VARCHAR(32) NOT NULL COMMENT '报警等级',
                               alert_msg    VARCHAR(512) NOT NULL COMMENT '报警内容',
                               status       VARCHAR(32) COMMENT '报警处理状态',
                               handle_result VARCHAR(512) COMMENT '报警处理结果',
                               tenant_id    BIGINT NOT NULL COMMENT '租户ID',
                               operator     VARCHAR(64) COMMENT '操作人',
                               audit_source VARCHAR(64) COMMENT '操作来源',
                               created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '报警生成时间',
                               updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               KEY idx_job(job_id),
                               KEY idx_alert_rule(alert_rule_id),
                               KEY idx_tenant(tenant_id)
) COMMENT='作业报警记录（历史）';

CREATE TABLE alert_rule (
                            id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报警规则ID',
                            name         VARCHAR(128) NOT NULL COMMENT '规则名称',
                            rule_type    VARCHAR(64)  NOT NULL COMMENT '规则类型（指标/异常/自愈）',
                            rule_expr    TEXT         NOT NULL COMMENT '规则表达式（JSON/DSL）',
                            tenant_id    BIGINT       NOT NULL COMMENT '租户ID',
                            status       TINYINT      DEFAULT 1 COMMENT '启用状态',
                            is_deleted   TINYINT      DEFAULT 0 COMMENT '逻辑删除',
                            created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            KEY idx_type(rule_type),
                            KEY idx_tenant(tenant_id)
) COMMENT='报警规则配置表';

CREATE TABLE audit_log (
                           id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                           action       VARCHAR(64) NOT NULL COMMENT '操作类型（如删除、导出、变更）',
                           target_type  VARCHAR(64) NOT NULL COMMENT '目标对象类型（如User、Job）',
                           target_id    VARCHAR(64) COMMENT '目标对象ID',
                           content      TEXT COMMENT '操作内容详情',
                           operator     VARCHAR(64) NOT NULL COMMENT '操作人',
                           audit_source VARCHAR(64) NOT NULL COMMENT '操作来源',
                           tenant_id    BIGINT      NOT NULL COMMENT '租户ID',
                           audit_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '审计时间',
                           is_deleted   TINYINT     DEFAULT 0 COMMENT '逻辑删除',
                           KEY idx_action(action),
                           KEY idx_tenant(tenant_id)
) COMMENT='运维/安全全链路审计日志表';

-- ================================
-- 5. 标签、字典、API白名单
-- ================================

CREATE TABLE label (
                       id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
                       code         VARCHAR(64) NOT NULL COMMENT '标签编码，平台唯一',
                       name         VARCHAR(64) NOT NULL COMMENT '标签名',
                       color        VARCHAR(32) COMMENT '标签颜色',
                       tenant_id    BIGINT NOT NULL COMMENT '租户ID',
                       remark       VARCHAR(256) COMMENT '备注说明',
                       is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                       created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       UNIQUE KEY uk_code_tenant(code, tenant_id),
                       KEY idx_tenant(tenant_id)
) COMMENT='标签表（用于作业、资源等自定义标记）';

CREATE TABLE dict (
                      id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典ID',
                      dict_type    VARCHAR(64) NOT NULL COMMENT '字典类型',
                      dict_key     VARCHAR(64) NOT NULL COMMENT '字典项Key',
                      dict_value   VARCHAR(128) NOT NULL COMMENT '字典项值',
                      sort         INT DEFAULT 0 COMMENT '排序值',
                      remark       VARCHAR(256) COMMENT '备注说明',
                      tenant_id    BIGINT NOT NULL COMMENT '租户ID',
                      is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                      created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      UNIQUE KEY uk_type_key_tenant(dict_type, dict_key, tenant_id),
                      KEY idx_tenant(tenant_id)
) COMMENT='数据字典表（支持多租户）';

CREATE TABLE api_whitelist (
                               id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                               api_path     VARCHAR(256) NOT NULL COMMENT 'API接口路径',
                               tenant_id    BIGINT NOT NULL COMMENT '租户ID',
                               enabled      TINYINT DEFAULT 1 COMMENT '是否启用（1:启用,0:禁用）',
                               remark       VARCHAR(256) COMMENT '备注',
                               created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               KEY idx_api(tenant_id, api_path)
) COMMENT='API白名单配置表';

-- ================================
-- 6. 节点健康、Tag补充表
-- ================================

CREATE TABLE node_health (
                             id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                             node_id      BIGINT      NOT NULL COMMENT '节点ID',
                             status       VARCHAR(32) NOT NULL COMMENT '节点状态',
                             metrics      TEXT        COMMENT '指标详情（JSON）',
                             report_time  TIMESTAMP   NOT NULL COMMENT '上报时间',
                             tenant_id    BIGINT      NOT NULL COMMENT '租户ID',
                             is_deleted   TINYINT     DEFAULT 0 COMMENT '逻辑删除',
                             created_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             KEY idx_node(node_id),
                             KEY idx_tenant(tenant_id)
) COMMENT='集群节点健康状态监控表';

CREATE TABLE tag (
                     id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                     name         VARCHAR(64) NOT NULL COMMENT '标签名称',
                     color        VARCHAR(32) COMMENT '颜色',
                     remark       VARCHAR(256) COMMENT '备注',
                     tenant_id    BIGINT      NOT NULL COMMENT '租户ID',
                     is_deleted   TINYINT DEFAULT 0 COMMENT '逻辑删除',
                     created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                     KEY idx_tenant(tenant_id)
) COMMENT='业务标签表（可用于分类、分组）';


-- 作业主表，支持多租户、幂等、链路追踪、权限关联
-- 作业定义表（元数据/调度模板/唯一标识）
CREATE TABLE job_info (
                          id              BIGSERIAL PRIMARY KEY,                        -- 主键ID
                          job_name        VARCHAR(128) NOT NULL,                        -- 作业名称
                          tenant_id       BIGINT NOT NULL,                              -- 租户ID
                          job_type        VARCHAR(32) NOT NULL,                         -- 作业类型（FLINK/SPARK/ETL/SQL等）
                          project_code    VARCHAR(64),                                  -- 项目编码/归属
                          operator        VARCHAR(64) NOT NULL,                         -- 注册/管理操作人
                          source          VARCHAR(32) NOT NULL,                         -- 来源（平台/接口等）
                          trace_id        VARCHAR(64) NOT NULL UNIQUE,                  -- 注册幂等全局唯一ID
                          remark          VARCHAR(256),                                 -- 备注
                          register_at     BIGINT NOT NULL,                              -- 注册时间戳
                          is_deleted      INTEGER NOT NULL DEFAULT 0,                   -- 软删除
                          created_at      TIMESTAMP NOT NULL DEFAULT now(),             -- 创建时间
                          updated_at      TIMESTAMP NOT NULL DEFAULT now(),             -- 更新时间
                          CONSTRAINT uk_jobinfo_jobname_tenant UNIQUE(job_name, tenant_id)
);

COMMENT ON TABLE job_info IS '作业定义表（元数据/调度模板）';
COMMENT ON COLUMN job_info.id IS '主键ID';
COMMENT ON COLUMN job_info.job_name IS '作业名称';
COMMENT ON COLUMN job_info.tenant_id IS '租户ID';
COMMENT ON COLUMN job_info.job_type IS '作业类型（FLINK/SPARK/ETL/SQL等）';
COMMENT ON COLUMN job_info.project_code IS '项目编码/归属';
COMMENT ON COLUMN job_info.operator IS '注册/管理操作人';
COMMENT ON COLUMN job_info.source IS '来源（平台/接口等）';
COMMENT ON COLUMN job_info.trace_id IS '注册幂等全局唯一ID';
COMMENT ON COLUMN job_info.remark IS '备注';
COMMENT ON COLUMN job_info.register_at IS '注册时间戳';
COMMENT ON COLUMN job_info.is_deleted IS '软删除';
COMMENT ON COLUMN job_info.created_at IS '创建时间';
COMMENT ON COLUMN job_info.updated_at IS '更新时间';

-- 权限表举例：作业与用户、角色关联（略，可参考 user_job_perm/job_role_perm 结构）

-- 作业实例表（每次运行、调度、补数均一条）
CREATE TABLE job_instance (
                              id              BIGSERIAL PRIMARY KEY,                        -- 主键ID
                              job_id          BIGINT NOT NULL,                              -- 作业定义ID，外键
                              job_name        VARCHAR(128) NOT NULL,                        -- 作业名称快照
                              engine_type     VARCHAR(32) NOT NULL,                         -- 引擎类型（FLINK/SPARK）
                              cluster_id      BIGINT NOT NULL,                              -- 运行集群ID
                              instance_code   VARCHAR(128) NOT NULL,                        -- 实例唯一标识/traceId
                              status          VARCHAR(32) NOT NULL,                         -- 运行状态
                              trigger_type    VARCHAR(32) NOT NULL,                         -- 触发类型
                              start_time      TIMESTAMP,                                    -- 启动时间
                              end_time        TIMESTAMP,                                    -- 结束时间
                              config_json     TEXT,                                         -- 运行参数/快照
                              result_json     TEXT,                                         -- 结果/产物
                              fail_reason     VARCHAR(512),                                 -- 失败原因
                              retry_count     INTEGER NOT NULL DEFAULT 0,                   -- 自动重试次数
                              is_deleted      INTEGER NOT NULL DEFAULT 0,                   -- 软删除
                              tenant_id       BIGINT NOT NULL,                              -- 租户ID
                              operator        VARCHAR(64),                                  -- 操作人
                              created_at      TIMESTAMP,                                    -- 创建时间
                              updated_at      TIMESTAMP                                     -- 更新时间
);

COMMENT ON TABLE job_instance IS '作业实例表（每次运行调度补数）';
COMMENT ON COLUMN job_instance.id IS '主键ID';
COMMENT ON COLUMN job_instance.job_id IS '作业定义ID';
COMMENT ON COLUMN job_instance.job_name IS '作业名称快照';
COMMENT ON COLUMN job_instance.engine_type IS '引擎类型';
COMMENT ON COLUMN job_instance.cluster_id IS '运行集群ID';
COMMENT ON COLUMN job_instance.instance_code IS '实例唯一标识/traceId';
COMMENT ON COLUMN job_instance.status IS '运行状态';
COMMENT ON COLUMN job_instance.trigger_type IS '触发类型';
COMMENT ON COLUMN job_instance.start_time IS '启动时间';
COMMENT ON COLUMN job_instance.end_time IS '结束时间';
COMMENT ON COLUMN job_instance.config_json IS '运行参数/快照';
COMMENT ON COLUMN job_instance.result_json IS '结果/产物';
COMMENT ON COLUMN job_instance.fail_reason IS '失败原因';
COMMENT ON COLUMN job_instance.retry_count IS '自动重试次数';
COMMENT ON COLUMN job_instance.is_deleted IS '软删除';
COMMENT ON COLUMN job_instance.tenant_id IS '租户ID';
COMMENT ON COLUMN job_instance.operator IS '操作人';
COMMENT ON COLUMN job_instance.created_at IS '创建时间';
COMMENT ON COLUMN job_instance.updated_at IS '更新时间';

-- 作业-用户-权限三元组表（支持细粒度授权、批量管理）
CREATE TABLE job_permission (
                                id            BIGSERIAL PRIMARY KEY,                   -- 主键
                                job_id        BIGINT NOT NULL,                         -- 作业ID
                                tenant_id     BIGINT NOT NULL,                         -- 租户ID
                                user_id       VARCHAR(64) NOT NULL,                    -- 用户ID
                                permission_id BIGINT NOT NULL                          -- 权限ID
);

COMMENT ON TABLE job_permission IS '作业-用户-权限三元组表';
COMMENT ON COLUMN job_permission.id IS '主键';
COMMENT ON COLUMN job_permission.job_id IS '作业ID';
COMMENT ON COLUMN job_permission.tenant_id IS '租户ID';
COMMENT ON COLUMN job_permission.user_id IS '用户ID';
COMMENT ON COLUMN job_permission.permission_id IS '权限ID';




-- 新增测试租户
INSERT INTO tenant (name, code, contact, status) VALUES ('演示租户', 'demo', 'admin@demo.com', 1);

-- 新增基础角色
INSERT INTO role (name, code, `desc`) VALUES ('管理员', 'ADMIN', '系统管理员');
INSERT INTO role (name, code, `desc`) VALUES ('普通用户', 'USER', '普通操作员');

-- 新增超级管理员用户（密码需加密，这里示例为明文admin123）
INSERT INTO user (tenant_id, username, password, email, phone, status, is_deleted)
VALUES (1, 'admin', '$2a$10$Vr8OL6XTwC1nGlp/MNw3Xezn8YSh7fFsZcBhwcZJ.NJuz2ykNJH6W', 'admin@demo.com', '13800000000', 1, 0);
-- 说明：password字段存储的是加密后的admin123（BCrypt: $2a$10$Vr8OL6XTwC1nGlp/MNw3Xezn8YSh7fFsZcBhwcZJ.NJuz2ykNJH6W）

-- 用户角色关联（超级管理员为管理员）
INSERT INTO user_role (user_id, role_id, tenant_id) VALUES (1, 1, 1);



