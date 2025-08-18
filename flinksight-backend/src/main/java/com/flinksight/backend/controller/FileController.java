package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文件管理
 */
@RestController
@Tag(name = "api", description = "文件管理")
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileService service;

    @Operation(summary = "", description = "",operationId = "createFile")
    @PostMapping("/create")
    public ApiResponse<FileDTO> create(@RequestBody  @Valid FileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getFile")
    @GetMapping("/id/{id}")
    public ApiResponse<FileDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllFiles")
    @GetMapping("/list")
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
    @PutMapping("/update")
    public ApiResponse<FileDTO> update(@RequestBody  @Valid FileDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteFile")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
