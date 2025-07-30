package com.flinksight.common.service;

import com.flinksight.common.dto.UserApiDTO;

import java.util.List;
import java.util.Optional;

public interface UserApiService extends SoftDeleteService<UserApiDTO, Long> {
    UserApiDTO assignApiToUser(Long userId, Long apiId);
    boolean removeApiFromUser(Long userId, Long apiId);
    List<UserApiDTO> findByUserId(Long userId);
    List<UserApiDTO> findByApiId(Long apiId);
    Optional<UserApiDTO> getById(Long id);
}
