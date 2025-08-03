package com.flinksight.common.service;

import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 角色业务接口
 * Role Service
 */
public interface RoleService extends SoftDeleteService<RoleDTO, Long> {
    RoleDTO createRole(RoleDTO role);
    Optional<RoleDTO> getRoleById(Long roleId);
    RoleDTO getRoleByCode(String code);
    PageResult<RoleDTO> getAllRoles(int page, int size);
}
