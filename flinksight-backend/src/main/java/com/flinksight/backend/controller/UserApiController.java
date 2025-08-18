package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-API权限分配管理
 */
@Tag(name = "api", description = "用户-API权限分配管理")
@RestController
@RequestMapping("/api/user-api")
@RequiredArgsConstructor
@Validated
public class UserApiController {

    private final UserApiService service;

    @Operation(summary = "", description = "",operationId = "assignUserApi")
    @PostMapping("/assign")
    public ApiResponse<UserApiDTO> assign(@RequestParam Long userId, @RequestParam Long apiId) {
        return ApiResponse.ok(service.assignApiToUser(userId, apiId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserApi")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long apiId) {
        return service.removeApiFromUser(userId, apiId);
    }

    @Operation(summary = "", description = "",operationId = "getUserApisByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserApiDTO>> findByUser(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserApisByApi")
    @GetMapping("/api/{apiId}")
    public ApiResponse<PageResult<UserApiDTO>> findByApi(@PathVariable Long apiId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByApiId(apiId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserApi")
    @GetMapping("/id/{id}")
    public ApiResponse<UserApiDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserApi")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
