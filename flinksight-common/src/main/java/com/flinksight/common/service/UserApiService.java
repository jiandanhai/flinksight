package com.flinksight.common.service;

import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserApiService extends SoftDeleteService<UserApiDTO, Long> {
    UserApiDTO assignApiToUser(Long userId, Long apiId);
    boolean removeApiFromUser(Long userId, Long apiId);
    PageResult<UserApiDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserApiDTO> findByApiId(Long apiId,int page, int size);
    Optional<UserApiDTO> getById(Long id);
}
