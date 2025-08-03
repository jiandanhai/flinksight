package com.flinksight.common.service;

import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserDepartmentService extends SoftDeleteService<UserDepartmentDTO, Long> {
    UserDepartmentDTO assignDepartmentToUser(Long userId, Long departmentId);
    boolean removeDepartmentFromUser(Long userId, Long departmentId);
    PageResult<UserDepartmentDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserDepartmentDTO> findByDepartmentId(Long departmentId,int page, int size);
    Optional<UserDepartmentDTO> getById(Long id);
}
