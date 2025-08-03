package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ResourceLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 资源-标签关联管理
 */
@RestController
@RequestMapping("/api/resource-label")
@RequiredArgsConstructor
public class ResourceLabelController {

    private final ResourceLabelService service;

    @PostMapping("/assign")
    public ApiResponse<ResourceLabelDTO> assign(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return ApiResponse.ok(service.assignLabelToResource(resourceId, labelId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return service.removeLabelFromResource(resourceId, labelId);
    }

    @GetMapping("/resource/{resourceId}")
    public ApiResponse<PageResult<ResourceLabelDTO>> findByResourceId(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByResourceId(resourceId,page,size));
    }

    @GetMapping("/label/{labelId}")
    public ApiResponse<PageResult<ResourceLabelDTO>> findByLabelId(
            @PathVariable Long labelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByLabelId(labelId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ResourceLabelDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
