package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文件管理
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @Operation(summary = "", description = "",operationId = "createFile")
    @PostMapping
    public ApiResponse<FileDTO> create(@RequestBody FileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getFile")
    @GetMapping("/{id}")
    public ApiResponse<FileDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllFiles")
    @GetMapping
    public ApiResponse<PageResult<FileDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getFilesByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<FileDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateFile")
    @PutMapping
    public ApiResponse<FileDTO> update(@RequestBody FileDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteFile")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
