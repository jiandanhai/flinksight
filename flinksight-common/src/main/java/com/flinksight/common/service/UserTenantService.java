package com.flinksight.common.service;

import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserTenantService extends SoftDeleteService<UserTenantDTO, Long> {
    UserTenantDTO assignTenantToUser(Long userId, Long tenantId);
    boolean removeTenantFromUser(Long userId, Long tenantId);
    PageResult<UserTenantDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserTenantDTO> findByTenantId(Long tenantId,int page, int size);
    Optional<UserTenantDTO> getById(Long id);
}
