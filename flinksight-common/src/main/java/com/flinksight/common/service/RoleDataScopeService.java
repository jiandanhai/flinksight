package com.flinksight.common.service;

import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface RoleDataScopeService extends SoftDeleteService<RoleDataScopeDTO, Long> {
    RoleDataScopeDTO assignDataScopeToRole(Long roleId, Long dataScopeId);
    boolean removeDataScopeFromRole(Long roleId, Long dataScopeId);
    PageResult<RoleDataScopeDTO> findByRoleId(Long roleId,int page, int size);
    PageResult<RoleDataScopeDTO> findByDataScopeId(Long dataScopeId,int page, int size);
    Optional<RoleDataScopeDTO> getById(Long id);
}
