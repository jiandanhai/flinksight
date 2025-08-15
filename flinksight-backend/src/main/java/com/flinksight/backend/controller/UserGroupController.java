package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户分组分配管理
 */
@Tag(name = "api", description = "用户分组分配管理API")
@RestController
@RequestMapping("/api/user-group")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService service;

    @Operation(summary = "", description = "",operationId = "assignUserGroup")
    @PostMapping("/assign")
    public ApiResponse<UserGroupDTO> assign(@RequestParam Long userId, @RequestParam Long groupId) {
        return ApiResponse.ok(service.assignGroupToUser(userId, groupId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserGroup")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long groupId) {
        return service.removeGroupFromUser(userId, groupId);
    }

    @Operation(summary = "", description = "",operationId = "getUserGroupsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserGroupDTO>> findByUser(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserGroupsByGroup")
    @GetMapping("/group/{groupId}")
    public ApiResponse<PageResult<UserGroupDTO>> findByGroup(@PathVariable Long groupId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByGroupId(groupId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserGroup")
    @GetMapping("/{id}")
    public ApiResponse<UserGroupDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserGroup")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
