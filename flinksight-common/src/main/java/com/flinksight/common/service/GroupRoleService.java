package com.flinksight.common.service;

import com.flinksight.common.dto.GroupRoleDTO;

import java.util.List;
import java.util.Optional;

public interface GroupRoleService extends SoftDeleteService<GroupRoleDTO, Long> {
    GroupRoleDTO assignRoleToGroup(Long groupId, Long roleId);
    boolean removeRoleFromGroup(Long groupId, Long roleId);
    List<GroupRoleDTO> findByGroupId(Long groupId);
    List<GroupRoleDTO> findByRoleId(Long roleId);
    Optional<GroupRoleDTO> getById(Long id);
}
