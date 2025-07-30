package com.flinksight.common.service;

import com.flinksight.common.dto.DeptRoleDTO;

import java.util.List;
import java.util.Optional;

public interface DeptRoleService extends SoftDeleteService<DeptRoleDTO, Long> {
    DeptRoleDTO assignRoleToDept(Long deptId, Long roleId);
    boolean removeRoleFromDept(Long deptId, Long roleId);
    List<DeptRoleDTO> findByDeptId(Long deptId);
    List<DeptRoleDTO> findByRoleId(Long roleId);
    Optional<DeptRoleDTO> getById(Long id);
}
