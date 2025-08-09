package com.flinksight.common.service;

import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserRoleService extends SoftDeleteService<UserRoleDTO, Long> {
    /**
     * 用户分配角色
     */
    void assignRole(Long userId, Long roleId);
    UserRoleDTO assignRoleToUser(Long userId, Long roleId, Long tenantId);
    /**
     * 取消用户角色
     */
    boolean removeRoleFromUser(Long userId, Long roleId);
    /**
     * 查询用户所有角色
     */
    PageResult<UserRoleDTO> findRolesByUserId(Long userId,int page, int size);
    /**
     * 查询角色下所有用户
     */
    PageResult<UserRoleDTO> findUsersByRoleId(Long roleId,int page, int size);
    PageResult<UserRoleDTO> findByTenantId(Long tenantId,int page, int size);
    Optional<UserRoleDTO> getById(Long id);

}
