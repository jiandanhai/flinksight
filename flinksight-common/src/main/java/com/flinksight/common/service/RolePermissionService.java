package com.flinksight.common.service;

import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RolePermissionService extends SoftDeleteService<RolePermissionDTO, Long> {
    RolePermissionDTO assignPermissionToRole(Long roleId, Long permissionId);
    boolean removePermissionFromRole(Long roleId, Long permissionId);
    PageResult<RolePermissionDTO> findByRoleId(Long roleId,int page, int size);
    PageResult<RolePermissionDTO> findByPermissionId(Long permissionId,int page, int size);
    Optional<RolePermissionDTO> getById(Long id);
}
