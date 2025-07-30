package com.flinksight.common.service;

import com.flinksight.common.dto.UserDepartmentDTO;

import java.util.List;
import java.util.Optional;

public interface UserDepartmentService extends SoftDeleteService<UserDepartmentDTO, Long> {
    UserDepartmentDTO assignDepartmentToUser(Long userId, Long departmentId);
    boolean removeDepartmentFromUser(Long userId, Long departmentId);
    List<UserDepartmentDTO> findByUserId(Long userId);
    List<UserDepartmentDTO> findByDepartmentId(Long departmentId);
    Optional<UserDepartmentDTO> getById(Long id);
}
