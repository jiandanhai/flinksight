package com.flinksight.common.service;

import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 审计日志业务接口
 * AuditLog Service
 */
public interface AuditLogService  extends SoftDeleteService<AuditLogDTO, Long> {
    AuditLogDTO createAuditLog(AuditLogDTO log);
    Optional<AuditLogDTO> getAuditLogById(Long id);
    PageResult<AuditLogDTO> getLogsByTenantAndUser(Long tenantId, Long userId,int page, int size);
    /**
     * 配置变更审计日志
     * @param configType 配置类型（如 NACOS、SPARK_JOB、GLOBAL 等）
     * @param dataId 配置唯一标识（如nacos的dataId或作业id等）
     * @param tenantId 租户ID
     * @param operator 操作人
     * @param config 配置内容
     * @param traceId 链路追踪ID
     */
    void logConfigChange(String configType, String dataId, Long tenantId, String operator, String config, String traceId);
}
