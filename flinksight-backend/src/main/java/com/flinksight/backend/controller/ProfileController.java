package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户扩展档案管理
 */
@Tag(name = "api",description = "用户扩展档案管理API")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @Operation(summary = "创建用户档案", operationId = "createProfile")
    @PostMapping
    public ApiResponse<ProfileDTO> create(@RequestBody ProfileDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "根据档案ID获取", operationId = "getProfile")
    @GetMapping("/{id}")
    public ApiResponse<ProfileDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "根据用户ID获取", operationId = "getProfileByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<ProfileDTO> getByUserId(@PathVariable Long userId) {

        return ApiResponse.ok(service.getByUserId(userId));
    }

    @Operation(summary = "更新用户档案", operationId = "updateProfile")
    @PutMapping
    public ApiResponse<ProfileDTO> update(@RequestBody ProfileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "删除用户档案", operationId = "deleteProfile")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
