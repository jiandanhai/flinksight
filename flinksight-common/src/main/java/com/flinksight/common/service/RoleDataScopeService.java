package com.flinksight.common.service;

import com.flinksight.common.dto.RoleDataScopeDTO;

import java.util.List;
import java.util.Optional;

public interface RoleDataScopeService extends SoftDeleteService<RoleDataScopeDTO, Long> {
    RoleDataScopeDTO assignDataScopeToRole(Long roleId, Long dataScopeId);
    boolean removeDataScopeFromRole(Long roleId, Long dataScopeId);
    List<RoleDataScopeDTO> findByRoleId(Long roleId);
    List<RoleDataScopeDTO> findByDataScopeId(Long dataScopeId);
    Optional<RoleDataScopeDTO> getById(Long id);
}
