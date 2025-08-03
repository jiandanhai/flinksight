package com.flinksight.common.service;

import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface NotificationService extends SoftDeleteService<NotificationDTO, Long> {
    NotificationDTO createOrUpdate(NotificationDTO entity);
    Optional<NotificationDTO> getById(Long id);
    PageResult<NotificationDTO> getAll(int page, int size);
    PageResult<NotificationDTO> findByUserId(Long userId,int page, int size);
    PageResult<NotificationDTO> findByTenantId(Long tenantId,int page, int size);
}
