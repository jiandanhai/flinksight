package com.flinksight.common.service;

import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserPermissionService extends SoftDeleteService<UserPermissionDTO, Long> {
    UserPermissionDTO assignPermissionToUser(Long userId, Long permissionId);
    boolean removePermissionFromUser(Long userId, Long permissionId);
    PageResult<UserPermissionDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserPermissionDTO> findByPermissionId(Long permissionId,int page, int size);
    Optional<UserPermissionDTO> getById(Long id);
}
