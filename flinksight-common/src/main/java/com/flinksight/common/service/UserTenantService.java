package com.flinksight.common.service;

import com.flinksight.common.dto.UserTenantDTO;

import java.util.List;
import java.util.Optional;

public interface UserTenantService extends SoftDeleteService<UserTenantDTO, Long> {
    UserTenantDTO assignTenantToUser(Long userId, Long tenantId);
    boolean removeTenantFromUser(Long userId, Long tenantId);
    List<UserTenantDTO> findByUserId(Long userId);
    List<UserTenantDTO> findByTenantId(Long tenantId);
    Optional<UserTenantDTO> getById(Long id);
}
