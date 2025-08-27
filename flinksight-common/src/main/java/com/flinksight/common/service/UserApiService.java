package com.flinksight.common.service;

import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserApiService extends SoftDeleteService<UserApiDTO, Long> {
    UserApiDTO assignApiToUser(Long userId, Long apiId);
    boolean removeApiFromUser(Long userId, Long apiId);
    Optional<UserApiDTO> getById(Long id);

    PageResult<UserApiDTO> list(Long userId, Long apiId, int page, int size);
}
