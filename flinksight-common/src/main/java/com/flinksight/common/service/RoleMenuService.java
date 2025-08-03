package com.flinksight.common.service;

import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RoleMenuService extends SoftDeleteService<RoleMenuDTO, Long> {
    RoleMenuDTO assignMenuToRole(Long roleId, Long menuId);
    boolean removeMenuFromRole(Long roleId, Long menuId);
    PageResult<RoleMenuDTO> findByRoleId(Long roleId,int page, int size);
    PageResult<RoleMenuDTO> findByMenuId(Long menuId,int page, int size);
    Optional<RoleMenuDTO> getById(Long id);
}
