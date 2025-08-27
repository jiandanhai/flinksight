package com.flinksight.common.service;

import com.flinksight.common.dto.AuditLogDTO;

/**
 * 审计日志业务接口
 * AuditLog Service
 */
public interface AuditLogService  extends AuditLogWriter, AuditLogQuery,SoftDeleteService<AuditLogDTO, Long> {}
