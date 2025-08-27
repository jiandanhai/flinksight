package com.flinksight.common.service;

import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;
import java.util.Optional;

public interface NotificationService extends SoftDeleteService<NotificationDTO, Long> {
    NotificationDTO createOrUpdate(NotificationDTO entity);

    Optional<NotificationDTO> getMyById(Long id);

    PageResult<NotificationDTO> findByTenantId(int page, int size);

    PageResult<NotificationDTO> list(String type, Integer isRead, int page,int size);

    int markRead(List<Long> ids);

    int softDelete(List<Long> ids);
}
