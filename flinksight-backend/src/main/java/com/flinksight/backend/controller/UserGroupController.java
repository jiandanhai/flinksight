package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户分组分配管理
 */
@RestController
@RequestMapping("/api/user-group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService service;

    @PostMapping("/assign")
    public ApiResponse<UserGroupDTO> assign(@RequestParam Long userId, @RequestParam Long groupId) {
        return ApiResponse.ok(service.assignGroupToUser(userId, groupId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long groupId) {
        return service.removeGroupFromUser(userId, groupId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserGroupDTO>> findByUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/group/{groupId}")
    public ApiResponse<PageResult<UserGroupDTO>> findByGroup(@PathVariable Long groupId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByGroupId(groupId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserGroupDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
