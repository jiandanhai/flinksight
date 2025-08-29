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
    Optional<RoleDTO> getRoleByCode(String code);
    /**
     * 编辑角色
     */
    RoleDTO update(RoleDTO dto);

    PageResult<RoleDTO> list(String keyword, String name, String code,
                             Integer page, Integer size);

}
