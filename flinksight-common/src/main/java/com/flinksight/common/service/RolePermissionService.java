package com.flinksight.common.service;

import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RolePermissionService extends SoftDeleteService<RolePermissionDTO, Long> {
    RolePermissionDTO assignPermissionToRole(Long roleId, String permissionCode);
    boolean removePermissionFromRole(Long roleId, String permissionCode);
    PageResult<RolePermissionDTO> findByRoleId(Long roleId,int page, int size);
    PageResult<RolePermissionDTO> findByPermissionCode(String permissionCode,int page, int size);
    Optional<RolePermissionDTO> getById(Long id);
}
