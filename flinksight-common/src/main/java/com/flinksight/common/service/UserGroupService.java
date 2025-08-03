package com.flinksight.common.service;

import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserGroupService extends SoftDeleteService<UserGroupDTO, Long> {
    UserGroupDTO assignGroupToUser(Long userId, Long groupId);
    boolean removeGroupFromUser(Long userId, Long groupId);
    PageResult<UserGroupDTO> findByUserId(Long userId,int page, int size);
    PageResult<UserGroupDTO> findByGroupId(Long groupId,int page, int size);
    Optional<UserGroupDTO> getById(Long id);
}
