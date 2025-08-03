package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserTenantDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-租户关联管理
 */
@RestController
@RequestMapping("/api/user-tenant")
@RequiredArgsConstructor
public class UserTenantController {

    private final UserTenantService service;

    @PostMapping("/assign")
    public ApiResponse<UserTenantDTO> assign(@RequestParam Long userId, @RequestParam Long tenantId) {
        return ApiResponse.ok(service.assignTenantToUser(userId, tenantId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long tenantId) {
        return service.removeTenantFromUser(userId, tenantId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserTenantDTO>> findByUserId(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<UserTenantDTO>> findByTenantId(@PathVariable Long tenantId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserTenantDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
