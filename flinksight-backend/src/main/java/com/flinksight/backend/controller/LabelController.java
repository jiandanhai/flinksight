package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 标签管理
 */
@RestController
@RequestMapping("/api/label")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService service;

    @PostMapping
    public ApiResponse<LabelDTO> create(@RequestBody LabelDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<LabelDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<LabelDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<LabelDTO> update(@RequestBody LabelDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
