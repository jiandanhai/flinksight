package com.flinksight.common.service;

import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserPostService extends SoftDeleteService<UserPostDTO, Long> {
    UserPostDTO assignPostToUser(Long userId, Long postId);
    boolean removePostFromUser(Long userId, Long postId);
    Optional<UserPostDTO> getById(Long id);

    PageResult<UserPostDTO> list(Long userId, Long postId, int page, int size);
}
