package com.flinksight.common.service;

import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserPostService extends SoftDeleteService<UserPostDTO, Long> {
    UserPostDTO assignPostToUser(Long userId, Long postId);
    boolean removePostFromUser(Long userId, Long postId);
    PageResult<UserPostDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserPostDTO> findByPostId(Long postId,int page, int size);
    Optional<UserPostDTO> getById(Long id);
}
