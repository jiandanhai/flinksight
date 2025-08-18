package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceLabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 资源-标签关联管理
 */
@Tag(name = "api",description = "资源-标签关联管理API")
@RestController
@RequestMapping("/api/resource-label")
@RequiredArgsConstructor
@Validated
public class ResourceLabelController {

    private final ResourceLabelService service;

    @Operation(summary = "", description = "",operationId = "assignResourceLabel")
    @PostMapping("/assign")
    public ApiResponse<ResourceLabelDTO> assign(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return ApiResponse.ok(service.assignLabelToResource(resourceId, labelId));
    }

    @Operation(summary = "", description = "",operationId = "removeResourceLabel")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return service.removeLabelFromResource(resourceId, labelId);
    }

    @Operation(summary = "", description = "",operationId = "getResourceLabelsByResource")
    @GetMapping("/resource/{resourceId}")
    public ApiResponse<PageResult<ResourceLabelDTO>> findByResourceId(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByResourceId(resourceId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getResourceLabelsByLabel")
    @GetMapping("/label/{labelId}")
    public ApiResponse<PageResult<ResourceLabelDTO>> findByLabelId(
            @PathVariable Long labelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByLabelId(labelId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getResourceLabel")
    @GetMapping("/id/{id}")
    public ApiResponse<ResourceLabelDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteResourceLabel")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
