package com.flinksight.common.service;

import com.flinksight.common.dto.UserRoleDTO;

import java.util.List;
import java.util.Optional;

public interface UserRoleService extends SoftDeleteService<UserRoleDTO, Long> {
    UserRoleDTO assignRoleToUser(Long userId, Long roleId, Long tenantId);
    boolean removeRoleFromUser(Long userId, Long roleId);
    List<UserRoleDTO> findRolesByUserId(Long userId);
    List<UserRoleDTO> findUsersByRoleId(Long roleId);
    List<UserRoleDTO> findByTenantId(Long tenantId);
    Optional<UserRoleDTO> getById(Long id);
}
