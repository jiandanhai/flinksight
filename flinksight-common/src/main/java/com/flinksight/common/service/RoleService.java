package com.flinksight.common.service;

import com.flinksight.common.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

/**
 * 角色业务接口
 * Role Service
 */
public interface RoleService extends SoftDeleteService<RoleDTO, Long> {
    RoleDTO createRole(RoleDTO role);
    Optional<RoleDTO> getRoleById(Long roleId);
    RoleDTO getRoleByCode(String code);
    List<RoleDTO> getAllRoles();
}
