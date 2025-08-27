package com.flinksight.common.service;

import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RoleMenuService extends SoftDeleteService<RoleMenuDTO, Long> {
    RoleMenuDTO assignMenuToRole(Long roleId, Long menuId);
    boolean removeMenuFromRole(Long roleId, Long menuId);
    Optional<RoleMenuDTO> getById(Long id);

    PageResult<RoleMenuDTO> list(Long roleId, Long menuId, int page, int size);
}
