package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户分组分配管理
 */
@Tag(name = "api", description = "用户分组分配管理API")
@RestController
@RequestMapping("/api/user/group")
@RequiredArgsConstructor
@Validated
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

    @Operation(summary = "", description = "",operationId = "listUserGroups")
    @GetMapping("/list")
    public ApiResponse<PageResult<UserGroupDTO>> findByGroup(@RequestParam Long userId,
                                                            @RequestParam Long groupId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(userId,groupId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserGroup")
    @GetMapping("/id/{id}")
    public ApiResponse<UserGroupDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserGroup")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
