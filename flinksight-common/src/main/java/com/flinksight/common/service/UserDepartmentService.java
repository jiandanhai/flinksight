package com.flinksight.common.service;

import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserDepartmentService extends SoftDeleteService<UserDepartmentDTO, Long> {
    UserDepartmentDTO assignDepartmentToUser(Long userId, Long departmentId);
    boolean removeDepartmentFromUser(Long userId, Long departmentId);
    Optional<UserDepartmentDTO> getById(Long id);

    PageResult<UserDepartmentDTO> list(Long userId, Long departmentId, int page, int size);
}
