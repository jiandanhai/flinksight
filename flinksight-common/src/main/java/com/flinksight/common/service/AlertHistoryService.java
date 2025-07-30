package com.flinksight.common.service;

import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface AlertHistoryService extends SoftDeleteService<RoleDTO, Long> {
    AlertHistoryDTO createOrUpdate(AlertHistoryDTO entity);
    Optional<AlertHistoryDTO> getById(Long id);
    List<AlertHistoryDTO> findByTenantId(Long tenantId);
    List<AlertHistoryDTO> getAll();
}
