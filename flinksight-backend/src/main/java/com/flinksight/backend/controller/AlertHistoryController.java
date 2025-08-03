package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.AlertHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报警历史管理
 */
@RestController
@RequestMapping("/api/alert-history")
@RequiredArgsConstructor
public class AlertHistoryController {

    private final AlertHistoryService service;

    @PostMapping
    public ApiResponse<AlertHistoryDTO> create(@RequestBody AlertHistoryDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<AlertHistoryDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<AlertHistoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<AlertHistoryDTO>> findByTenantId(
            @PathVariable Long tenantId ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId, page,size));
    }

    @PutMapping
    public ApiResponse<AlertHistoryDTO> update(@RequestBody AlertHistoryDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
