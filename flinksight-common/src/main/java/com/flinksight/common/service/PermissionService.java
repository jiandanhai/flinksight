package com.flinksight.common.service;

import com.flinksight.common.dto.PermissionDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 权限点管理服务
 * 只负责权限点（菜单/按钮/API等）的元数据定义、查找
 * 不负责业务授权关系
 */
public interface PermissionService extends SoftDeleteService<PermissionDTO, Long>  {

    /**
     * 创建权限点
     */
    PermissionDTO createPermission(PermissionDTO permission);

    /**
     * 根据主键查找权限点
     */
    Optional<PermissionDTO> getPermissionById(Long id);

    /**
     * 根据权限编码查找权限点
     */
    PermissionDTO  getPermissionByCode(String code);

    /**
     * 查询所有权限点
     */
    PageResult<PermissionDTO> list(int page, int size);

    /**
     * 检查租户是否有权注册新作业
     * 平台侧可自定义（如黑名单、停用租户）
     * 不通过时抛出异常
     */
    void checkTenantRegisterPermission(Long tenantId, String operator);

    /**
     * 校验用户是否拥有某平台级权限点（不涉及具体作业）
     */
    boolean userHasPermission(Long userId, Long tenantId,String permissionCode);
}
