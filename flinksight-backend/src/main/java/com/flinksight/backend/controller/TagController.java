package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 标签管理
 */
@Tag(name = "api", description = "标签管理API")
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService service;

    @Operation(summary = "", description = "",operationId = "createTag")
    @PostMapping
    public ApiResponse<TagDTO> create(@RequestBody TagDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getTag")
    @GetMapping("/{id}")
    public ApiResponse<TagDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllTags")
    @GetMapping
    public ApiResponse<PageResult<TagDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getTagsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<TagDTO>> findByTenantId(@PathVariable Long tenantId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateTag")
    @PutMapping
    public ApiResponse<TagDTO> update(@RequestBody TagDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteTag")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
