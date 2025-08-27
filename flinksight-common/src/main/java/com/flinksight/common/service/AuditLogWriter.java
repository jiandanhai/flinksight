package com.flinksight.common.service;

import com.flinksight.common.dto.AuditLogDTO;

/**
 * 写入端口：用于“上报/创建审计日志”的最小集合。
 * 外部模块（spark-job、flink-job、第三方工具）仅依赖此接口。
 */
public interface AuditLogWriter {

    /**
     * 创建一条通用审计日志（推荐用于后端内部或强场景的批量写入）。
     */
    AuditLogDTO createAuditLog(AuditLogDTO log);

    /**
     * 上报“配置变更”事件（给作业侧等外部系统简洁上报用）。
     * @param configType  配置类型（如 'job-conf' / 'alert-rule'）
     * @param dataId      数据标识（如 jobId / ruleId）
     * @param tenantId    租户
     * @param operator    操作者展示名/账号
     * @param config      变更后的配置（JSON 字符串或文本）
     * @param traceId     链路追踪ID（可空）
     */
    void logConfigChange(String configType, String dataId, Long tenantId,
                         String operator, String config, String traceId);
}