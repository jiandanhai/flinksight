package com.flinksight.common.service;

import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.dto.UserTokenStateDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务接口
 * User Service
 */
public interface UserService extends SoftDeleteService<UserDTO, Long>  {

    /**
     * 获取当前用户
     * @return
     */
    UserDTO getCurrentUserProfile();

    UserDTO findByAccount(String account);

    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户对象
     */
    Optional<UserDTO> getUserById(Long userId);

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 每页条数
     * @return 用户列表
     */
    PageResult<UserDTO> list(int page, int size);


    /**
     * 修改用户信息
     * @param user 用户实体
     * @return 更新后用户
     */
    UserDTO updateUser(UserDTO user);

    /**
     * 获取用户所有权限（权限码/角色码）
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 权限码集合
     */
    List<String> getAuthorities(Long userId,Long tenantId);


    // in com.flinksight.common.service.UserService
    void bumpTokenVersion(Long userId);

    int getTokenVersion(Long userId);
    /** 查询用户Token版本状态（用于接口/调试/审计） */
    UserTokenStateDTO getUserTokenState(Long userId);

}
