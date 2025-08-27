package com.flinksight.common.service;

import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RoleDataScopeService extends SoftDeleteService<RoleDataScopeDTO, Long> {
    RoleDataScopeDTO assignDataScopeToRole(Long roleId, Long dataScopeId);
    boolean removeDataScopeFromRole(Long roleId, Long dataScopeId);
    Optional<RoleDataScopeDTO> getById(Long id);

    PageResult<RoleDataScopeDTO> list(Long roleId, Long dataScopeId, int page, int size);
}
