package com.flinksight.common.service;

import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface UserGroupService extends SoftDeleteService<UserGroupDTO, Long> {
    UserGroupDTO assignGroupToUser(Long userId, Long groupId);
    boolean removeGroupFromUser(Long userId, Long groupId);
    Optional<UserGroupDTO> getById(Long id);

    PageResult<UserGroupDTO> list(Long userId, Long groupId, int page, int size);
}
