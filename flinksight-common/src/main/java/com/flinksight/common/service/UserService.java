package com.flinksight.common.service;

import com.flinksight.common.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务接口
 * User Service
 */
public interface UserService extends SoftDeleteService<UserDTO, Long>  {

    /**
     * 新建用户
     * @param user 用户实体
     * @return 保存后的用户
     */
    UserDTO createUser(UserDTO user);

    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户对象
     */
    Optional<UserDTO> getUserById(Long userId);

    /**
     * 分页查询用户
     * @param tenantId 租户ID
     * @param page 页码
     * @param size 每页条数
     * @return 用户列表
     */
    List<UserDTO> getUsersByTenant(Long tenantId, int page, int size);


    /**
     * 修改用户信息
     * @param user 用户实体
     * @return 更新后用户
     */
    UserDTO updateUser(UserDTO user);

    /**
     * 校验用户密码
     * @param userId 用户ID
     * @param rawPwd 明文密码
     * @return 是否匹配
     */
    boolean checkPassword(Long userId, String rawPwd);

    /**
     * 获取用户所有权限（权限码/角色码）
     * @param userId 用户ID
     * @return 权限码集合
     */
    List<String> getAuthorities(Long userId);
}
