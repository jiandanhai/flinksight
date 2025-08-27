package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiWhitelistDTO;
import com.flinksight.common.service.ApiWhitelistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 接口白名单管理
 */
@RestController
@Tag(name = "api", description = "接口白名单管理")
@RequestMapping("/api/api-whitelist")
@RequiredArgsConstructor
@Validated
public class ApiWhitelistController {

    private final ApiWhitelistService service;

    @Operation(summary = "", operationId = "createApiWhitelist")
    @PostMapping("/create")
    public ApiResponse<ApiWhitelistDTO> create(@RequestBody  @Valid ApiWhitelistDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", operationId = "getApiWhitelist")
    @GetMapping("/id/{id}")
    public ApiResponse<ApiWhitelistDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", operationId = "updateApiWhitelist")
    @PutMapping("/update")
    public ApiResponse<ApiWhitelistDTO> update(@RequestBody  @Valid ApiWhitelistDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", operationId = "deleteApiWhitelist")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
