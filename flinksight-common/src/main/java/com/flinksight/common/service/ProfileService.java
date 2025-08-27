package com.flinksight.common.service;

import com.flinksight.common.dto.ChangePasswordRequestDTO;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ProfileService extends SoftDeleteService<ProfileDTO, Long> {

    /** 管理员：Upsert 指定用户档案（同租户） */
    ProfileDTO createOrUpdate(ProfileDTO entity);

    Optional<ProfileDTO> getById(Long id);

    /** 获取“我的”档案（没有则返回默认并自动初始化） */
    ProfileDTO getMyProfile();

    /** 管理员：按 userId 获取档案（同租户） */
    ProfileDTO getByUserId(Long userId);

    /** 管理员：分页查询本租户所有档案 */
    PageResult<ProfileDTO> list(int page,int szie);

    /** 管理员：软删除（同租户） */
    void softDeleteByUserId(Long userId);

    void changePassword(ChangePasswordRequestDTO req);
}
