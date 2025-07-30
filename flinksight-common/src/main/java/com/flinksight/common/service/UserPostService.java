package com.flinksight.common.service;

import com.flinksight.common.dto.UserPostDTO;

import java.util.List;
import java.util.Optional;

public interface UserPostService extends SoftDeleteService<UserPostDTO, Long> {
    UserPostDTO assignPostToUser(Long userId, Long postId);
    boolean removePostFromUser(Long userId, Long postId);
    List<UserPostDTO> findByUserId(Long userId);
    List<UserPostDTO> findByPostId(Long postId);
    Optional<UserPostDTO> getById(Long id);
}
