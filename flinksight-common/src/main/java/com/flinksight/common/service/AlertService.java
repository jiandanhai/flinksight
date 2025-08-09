package com.flinksight.common.service;

import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 报警事件业务接口
 * Alert Service
 */
public interface AlertService  extends SoftDeleteService<AlertDTO, Long> {
    AlertDTO createAlert(AlertDTO alert);
    Optional<AlertDTO> getAlertById(Long id);
    PageResult<AlertDTO> getAlertsByTenantAndStatus(Long tenantId, Integer status,int page, int size);
    PageResult<AlertDTO> getAlertsByJobAndStatus(Long jobId, Integer status,int page, int size);
    PageResult<AlertDTO> getAlertsByLevelAndStatus(String level, Integer status,int page, int size);
    AlertDTO updateAlert(AlertDTO alert);
}
