package com.flinksight.common.service;

import com.flinksight.common.dto.NotificationDTO;

import java.util.List;
import java.util.Optional;

public interface NotificationService extends SoftDeleteService<NotificationDTO, Long> {
    NotificationDTO createOrUpdate(NotificationDTO entity);
    Optional<NotificationDTO> getById(Long id);
    List<NotificationDTO> getAll();
    List<NotificationDTO> findByUserId(Long userId);
    List<NotificationDTO> findByTenantId(Long tenantId);
}
