package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LoginHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录历史管理
 */
@Tag(name = "api", description = "登录历史管理API")
@RestController
@RequestMapping("/api/login-history")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService service;

    @Operation(summary = "", description = "",operationId = "createLoginHistory")
    @PostMapping
    public ApiResponse<LoginHistoryDTO> create(@RequestBody LoginHistoryDTO dto) {

        return ApiResponse.ok(service.create(dto));
    }

    @Operation(summary = "", description = "",operationId = "getLoginHistory")
    @GetMapping("/{id}")
    public ApiResponse<LoginHistoryDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getLoginHistorysByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<LoginHistoryDTO>> findByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "按租户查询登录历史",operationId = "getSsoLoginHistorysByTenant")
    @GetMapping("/tenant")
    public ApiResponse<PageResult<LoginHistoryDTO>> findByTenant(@RequestParam Long tenantId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "deleteLoginHistory")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }

    @Operation(summary = "统计用户成功登录次数",operationId = "countSuccessLoginByUser")
    @GetMapping("/count-success")
    public ApiResponse<Long> countUserSuccess(@RequestParam Long userId) {
        return ApiResponse.ok(service.countUserSuccessLogin(userId));
    }
}
