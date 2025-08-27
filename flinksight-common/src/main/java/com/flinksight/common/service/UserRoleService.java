package com.flinksight.common.service;

import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserRoleService extends SoftDeleteService<UserRoleDTO, Long> {
    /**
     * 用户分配角色
     */
    void assignRole(Long userId, Long roleId);
    UserRoleDTO assignRoleToUser(Long userId, Long roleId);
    /**
     * 取消用户角色
     */
    boolean removeRoleFromUser(Long userId, Long roleId);

    Optional<UserRoleDTO> getById(Long id);

    PageResult<UserRoleDTO> list(Long userId, Long roleId, int page, int size);

}
