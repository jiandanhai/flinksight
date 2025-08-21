--按“租户+日期”对大表（如 job_metric, job_log, audit_log）分区
--job_metric 按月分区 & 租户字段分区键（MySQL支持INTERVAL分区）

CREATE TABLE job_metric (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            tenant_id BIGINT NOT NULL,
                            job_id BIGINT NOT NULL,
                            metric_key VARCHAR(64) NOT NULL,
                            value DOUBLE,
                            ts DATETIME NOT NULL,
                            is_deleted INT DEFAULT 0,
                            KEY idx_job_metric_tenant_job (tenant_id, job_id),
                            KEY idx_job_metric_metric (metric_key, ts)
)
    PARTITION BY RANGE (TO_DAYS(ts)) (
  PARTITION p202407 VALUES LESS THAN (TO_DAYS('2024-08-01')),
  PARTITION p202408 VALUES LESS THAN (TO_DAYS('2024-09-01')),
  PARTITION p202409 VALUES LESS THAN (TO_DAYS('2024-10-01')),
  PARTITION pmax    VALUES LESS THAN MAXVALUE
);

-- 分库分表设计建议与DDL（如按租户ID分库）
--假设你每1万个租户一个库，tenant_id % 10 决定落哪个物理库，表结构一致。例如：flinksight_0、flinksight_1 ... flinksight_9  建议采用ShardingSphere或MyCAT等中间件自动分库分表。

-- 在 flinksight_0 数据库
CREATE TABLE user_0 LIKE flinksight.user;
CREATE TABLE user_1 LIKE flinksight.user;
...
CREATE TABLE user_9 LIKE flinksight.user;
-- 插入时由后端分库分表中间件控制，路由如：user_{tenant_id % 10}

--索引优化脚本建议 常用索引示例（业务高频查询字段）
-- 用户登录、租户隔离
CREATE INDEX idx_user_tenant_username ON user (tenant_id, username);

-- 任务指标查询（租户/任务/指标/时间段）
CREATE INDEX idx_job_metric_query ON job_metric (tenant_id, job_id, metric_key, ts);

-- 任务日志查询
CREATE INDEX idx_job_log_query ON job_log (tenant_id, job_id, ts);

-- 工单、报警事件查询
CREATE INDEX idx_ticket_alert ON ticket (tenant_id, alert_id, status);

-- 审计日志按租户/操作人查
CREATE INDEX idx_auditlog_tenant_user ON audit_log (tenant_id, user_id, create_time);


-- 测试用全量假数据批量导入SQL
-- 100个测试租户
INSERT INTO tenant (name, code, contact, status)
SELECT CONCAT('测试租户', n), CONCAT('t', n), CONCAT('user', n, '@demo.com'), 1 FROM (
                                                                                     SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
                                                                                     UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
                                                                                 ) t1, (
                                                                                     SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
                                                                                     UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
                                                                                 ) t2 LIMIT 100;

-- 1000个用户
INSERT INTO user (tenant_id, username, password, email, phone, status, is_deleted)
SELECT FLOOR(1+RAND()*100), CONCAT('user', n), '$2a$10$Vr8OL6XTwC1nGlp/MNw3Xezn8YSh7fFsZcBhwcZJ.NJuz2ykNJH6W', CONCAT('user', n, '@test.com'), '13900000000', 1, 0
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 1000);

-- 500个集群
INSERT INTO cluster (tenant_id, name, type, endpoint, version, tags, status, remark, is_deleted)
SELECT FLOOR(1+RAND()*100), CONCAT('集群', n), 'YARN', CONCAT('http://node', n, ':8081'), '1.17', 'test', 1, '演示数据', 0
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 500);

-- 1000条任务
INSERT INTO job (tenant_id, cluster_id, name, type, status, owner_id, is_deleted, create_time, update_time)
SELECT FLOOR(1+RAND()*100), FLOOR(1+RAND()*500), CONCAT('任务', n), 'streaming', 'created', FLOOR(1+RAND()*1000), 0, NOW(), NOW()
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 1000);

-- 1万条任务指标
INSERT INTO job_metric (tenant_id, job_id, metric_key, value, ts, is_deleted)
SELECT FLOOR(1+RAND()*100), FLOOR(1+RAND()*1000), 'cpu', ROUND(RAND()*100,2), DATE_SUB(NOW(), INTERVAL FLOOR(RAND()*60) DAY), 0
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 10000);

-- 1万条任务日志
INSERT INTO job_log (tenant_id, job_id, level, content, ts, is_deleted)
SELECT FLOOR(1+RAND()*100), FLOOR(1+RAND()*1000), 'info', CONCAT('测试日志', n), DATE_SUB(NOW(), INTERVAL FLOOR(RAND()*60) DAY), 0
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 10000);

-- 1000条报警事件
INSERT INTO alert (tenant_id, job_id, level, type, message, status, is_deleted, create_time, update_time)
SELECT FLOOR(1+RAND()*100), FLOOR(1+RAND()*1000), 'high', '资源', '测试报警', 0, 0, NOW(), NOW()
FROM (SELECT @row:=@row+1 as n FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1, (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2, (SELECT @row:=0) t0 LIMIT 1000);

--说明与建议
--分区表DDL适合大体量指标/日志/审计等表，分区字段一般选时间（如ts）和租户ID，可极大优化查询和归档。
--分库分表需在后端代码层面用ShardingSphere等中间件控制，DB侧只需提前创建好对应分库分表即可。
--索引优化一定要根据实际业务查询重点字段来调整。
--测试假数据SQL可复制粘贴执行（数量可调，建议先小规模测试）。
