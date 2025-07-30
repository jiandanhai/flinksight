package com.flinksight.common.service;

import com.flinksight.common.dto.AlertDTO;
import com.flinksight.common.dto.AlertRuleDTO;

import java.util.List;
import java.util.Optional;

/**
 * 报警事件业务接口
 * Alert Service
 */
public interface AlertService  extends SoftDeleteService<AlertDTO, Long> {
    AlertDTO createAlert(AlertDTO alert);
    Optional<AlertDTO> getAlertById(Long id);
    List<AlertDTO> getAlertsByTenantAndStatus(Long tenantId, Integer status);
    List<AlertDTO> getAlertsByJobAndStatus(Long jobId, Integer status);
    AlertDTO updateAlert(AlertDTO alert);
}
