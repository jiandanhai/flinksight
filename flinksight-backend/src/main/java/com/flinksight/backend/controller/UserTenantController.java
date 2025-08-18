package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-租户关联管理
 */
@Tag(name = "api", description = "用户-租户关联管理API")
@RestController
@RequestMapping("/api/user-tenant")
@RequiredArgsConstructor
@Validated
public class UserTenantController {

    private final UserTenantService service;

    @Operation(summary = "", description = "",operationId = "assignUserTenant")
    @PostMapping("/assign")
    public ApiResponse<UserTenantDTO> assign(@RequestParam Long userId, @RequestParam Long tenantId) {
        return ApiResponse.ok(service.assignTenantToUser(userId, tenantId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserTenant")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long tenantId,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "20") int size) {
        return service.removeTenantFromUser(userId, tenantId);
    }

    @Operation(summary = "", description = "",operationId = "getUserTenantsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserTenantDTO>> findByUserId(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserTenantsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<UserTenantDTO>> findByTenantId(@PathVariable Long tenantId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserTenant")
    @GetMapping("/id/{id}")
    public ApiResponse<UserTenantDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserTenant")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
