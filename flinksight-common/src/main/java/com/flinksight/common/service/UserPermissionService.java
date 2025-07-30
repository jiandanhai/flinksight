package com.flinksight.common.service;

import com.flinksight.common.dto.UserPermissionDTO;

import java.util.List;
import java.util.Optional;

public interface UserPermissionService extends SoftDeleteService<UserPermissionDTO, Long> {
    UserPermissionDTO assignPermissionToUser(Long userId, Long permissionId);
    boolean removePermissionFromUser(Long userId, Long permissionId);
    List<UserPermissionDTO> findByUserId(Long userId);
    List<UserPermissionDTO> findByPermissionId(Long permissionId);
    Optional<UserPermissionDTO> getById(Long id);
}
