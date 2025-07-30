package com.flinksight.common.service;

import com.flinksight.common.dto.RoleMenuDTO;

import java.util.List;
import java.util.Optional;

public interface RoleMenuService extends SoftDeleteService<RoleMenuDTO, Long> {
    RoleMenuDTO assignMenuToRole(Long roleId, Long menuId);
    boolean removeMenuFromRole(Long roleId, Long menuId);
    List<RoleMenuDTO> findByRoleId(Long roleId);
    List<RoleMenuDTO> findByMenuId(Long menuId);
    Optional<RoleMenuDTO> getById(Long id);
}
