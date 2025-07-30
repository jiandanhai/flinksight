package com.flinksight.common.service;

import com.flinksight.common.dto.UserGroupDTO;

import java.util.List;
import java.util.Optional;

public interface UserGroupService extends SoftDeleteService<UserGroupDTO, Long> {
    UserGroupDTO assignGroupToUser(Long userId, Long groupId);
    boolean removeGroupFromUser(Long userId, Long groupId);
    List<UserGroupDTO> findByUserId(Long userId);
    List<UserGroupDTO> findByGroupId(Long groupId);
    Optional<UserGroupDTO> getById(Long id);
}
