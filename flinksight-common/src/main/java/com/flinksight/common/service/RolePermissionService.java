package com.flinksight.common.service;

import com.flinksight.common.dto.RolePermissionDTO;

import java.util.List;
import java.util.Optional;

public interface RolePermissionService extends SoftDeleteService<RolePermissionDTO, Long> {
    RolePermissionDTO assignPermissionToRole(Long roleId, Long permissionId);
    boolean removePermissionFromRole(Long roleId, Long permissionId);
    List<RolePermissionDTO> findByRoleId(Long roleId);
    List<RolePermissionDTO> findByPermissionId(Long permissionId);
    Optional<RolePermissionDTO> getById(Long id);
}
