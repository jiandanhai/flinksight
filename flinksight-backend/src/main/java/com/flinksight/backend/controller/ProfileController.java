package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ChangePasswordRequestDTO;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户扩展档案管理
 */
@Tag(name = "api",description = "用户扩展档案管理API")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final ProfileService service;

    @Operation(summary = "创建/更新用户档案", operationId = "createProfile")
    @PostMapping
    public ApiResponse<ProfileDTO> create(@RequestBody @Valid ProfileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "根据档案ID获取", operationId = "getProfile")
    @GetMapping("/id/{id}")
    public ApiResponse<ProfileDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "获取我的档案",operationId = "getMyProfile")
    @GetMapping("/me")
    public ApiResponse<ProfileDTO> getMine() {
        return ApiResponse.ok(service.getMyProfile());
    }


    @Operation(summary = "根据用户ID获取", operationId = "getProfileByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<ProfileDTO> getByUser(@PathVariable Long userId) {

        return ApiResponse.ok(service.getByUserId(userId));
    }

    @Operation(summary = "更新用户档案", operationId = "updateProfile")
    @PutMapping("/update")
    public ApiResponse<ProfileDTO> update(@RequestBody @Valid ProfileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "修改当前用户密码",operationId = "changePassword")
    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO req) {
        service.changePassword(req);
    }

    @Operation(summary = "删除用户档案", operationId = "deleteProfile")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }

    @Operation(summary = "管理员-分页查询本租户所有档案",operationId = "listProfiles")
    @GetMapping("/list")
    public ApiResponse<PageResult<ProfileDTO>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(page, size));
    }

    @Operation(summary = "管理员-软删除(按userId)",operationId = "deleteByUser")
    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteByUser(@PathVariable Long userId) {
        service.softDeleteByUserId(userId);
        return ApiResponse.ok(null);
    }
}
