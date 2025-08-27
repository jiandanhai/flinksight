package com.flinksight.common.service;

import com.flinksight.common.dto.RolePermissionDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RolePermissionService extends SoftDeleteService<RolePermissionDTO, Long> {
    RolePermissionDTO assignPermissionToRole(Long roleId, String permissionCode);
    boolean removePermissionFromRole(Long roleId, String permissionCode);
    Optional<RolePermissionDTO> getById(Long id);


    PageResult<RolePermissionDTO> list(Long roleId, String permissionCode, int page, int size);
}
