package com.flinksight.common.service;

import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserRoleService extends SoftDeleteService<UserRoleDTO, Long> {
    UserRoleDTO assignRoleToUser(Long userId, Long roleId, Long tenantId);
    boolean removeRoleFromUser(Long userId, Long roleId);
    PageResult<UserRoleDTO> findRolesByUserId(Long userId,int page, int size);
    PageResult<UserRoleDTO> findUsersByRoleId(Long roleId,int page, int size);
    PageResult<UserRoleDTO> findByTenantId(Long tenantId,int page, int size);
    Optional<UserRoleDTO> getById(Long id);
}
