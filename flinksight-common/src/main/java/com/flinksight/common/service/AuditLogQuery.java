package com.flinksight.common.service;

import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.dto.AuditLogQueryDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 查询/管理端口：仅后端使用。
 */
public interface AuditLogQuery {

    Optional<AuditLogDTO> getAuditLogById(Long id);

    /** 分页查询（统一 PageResult<T> 输出） */
    PageResult<AuditLogDTO> list(AuditLogQueryDTO q);

}