package com.flinksight.backend.controller;

import com.flinksight.backend.domain.GroupRole;
import com.flinksight.common.dto.GroupRoleDTO;
import com.flinksight.common.service.GroupRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 组织-角色分配管理
 */
@RestController
@RequestMapping("/api/group-role")
@RequiredArgsConstructor
public class GroupRoleController {

    private final GroupRoleService service;

    @PostMapping("/assign")
    public GroupRoleDTO assign(@RequestParam Long groupId, @RequestParam Long roleId) {
        return service.assignRoleToGroup(groupId, roleId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long groupId, @RequestParam Long roleId) {
        return service.removeRoleFromGroup(groupId, roleId);
    }

    @GetMapping("/group/{groupId}")
    public List<GroupRoleDTO> findByGroup(@PathVariable Long groupId) {
        return service.findByGroupId(groupId);
    }

    @GetMapping("/role/{roleId}")
    public List<GroupRoleDTO> findByRole(@PathVariable Long roleId) {
        return service.findByRoleId(roleId);
    }

    @GetMapping("/{id}")
    public Optional<GroupRoleDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
