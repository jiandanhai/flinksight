package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * API密钥管理
 */
@RestController
@Tag(name = "api", description = "秘钥管理管理")
@RequestMapping("/api/api/key")
@RequiredArgsConstructor
@Validated
public class ApiKeyController {

    private final ApiKeyService service;

    @Operation(summary = "", operationId = "createApiKey")
    @PostMapping("/create")
    public ApiKeyDTO create(@RequestBody  @Valid ApiKeyDTO dto) {
        return service.createOrUpdate(dto);
    }

    @Operation(summary = "", operationId = "getApiKey")
    @GetMapping("/id/{id}")
    public ApiResponse<ApiKeyDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", operationId = "listApiKeys")
    @GetMapping("/list")
    public ApiResponse<PageResult<ApiKeyDTO>> list(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(page,size));
    }

    @Operation(summary = "", operationId = "updateApiKey")
    @PutMapping("/update")
    public ApiResponse<ApiKeyDTO> update(@RequestBody  @Valid ApiKeyDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", operationId = "deleteApiKey")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
