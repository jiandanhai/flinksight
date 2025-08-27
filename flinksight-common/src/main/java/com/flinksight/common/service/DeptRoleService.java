package com.flinksight.common.service;

import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface DeptRoleService extends SoftDeleteService<DeptRoleDTO, Long> {
    DeptRoleDTO assignRoleToDept(Long deptId, Long roleId);
    boolean removeRoleFromDept(Long deptId, Long roleId);
    PageResult<DeptRoleDTO> list(Long deptId,Long roleId,int page, int size);
    Optional<DeptRoleDTO> getById(Long id);
}
