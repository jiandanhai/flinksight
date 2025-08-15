package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiWhitelistDTO;
import com.flinksight.common.service.ApiWhitelistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 接口白名单管理
 */
@RestController
@Tag(name = "api", description = "接口白名单管理")
@RequestMapping("/api/api-whitelist")
@RequiredArgsConstructor
public class ApiWhitelistController {

    private final ApiWhitelistService service;

    @Operation(summary = "分页获取所有报警历史记录", operationId = "createApiWhitelist")
    @PostMapping
    public ApiResponse<ApiWhitelistDTO> create(@RequestBody ApiWhitelistDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "分页获取所有报警历史记录", operationId = "getApiWhitelist")
    @GetMapping("/{id}")
    public ApiResponse<ApiWhitelistDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "分页获取所有报警历史记录", operationId = "updateApiWhitelist")
    @PutMapping
    public ApiResponse<ApiWhitelistDTO> update(@RequestBody ApiWhitelistDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "分页获取所有报警历史记录", operationId = "deleteApiWhitelist")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
