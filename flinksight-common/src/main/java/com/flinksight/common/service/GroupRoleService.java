package com.flinksight.common.service;

import com.flinksight.common.dto.GroupRoleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface GroupRoleService extends SoftDeleteService<GroupRoleDTO, Long> {
    GroupRoleDTO assignRoleToGroup(Long groupId, Long roleId);
    boolean removeRoleFromGroup(Long groupId, Long roleId);
    PageResult<GroupRoleDTO> list(Long groupId,Long roleId,int page, int size);
    Optional<GroupRoleDTO> getById(Long id);
}
