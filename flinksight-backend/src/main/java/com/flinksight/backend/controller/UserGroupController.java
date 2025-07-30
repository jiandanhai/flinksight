package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserGroup;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户分组分配管理
 */
@RestController
@RequestMapping("/api/user-group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService service;

    @PostMapping("/assign")
    public UserGroupDTO assign(@RequestParam Long userId, @RequestParam Long groupId) {
        return service.assignGroupToUser(userId, groupId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long groupId) {
        return service.removeGroupFromUser(userId, groupId);
    }

    @GetMapping("/user/{userId}")
    public List<UserGroupDTO> findByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/group/{groupId}")
    public List<UserGroupDTO> findByGroup(@PathVariable Long groupId) {
        return service.findByGroupId(groupId);
    }

    @GetMapping("/{id}")
    public Optional<UserGroupDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
