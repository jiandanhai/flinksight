package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ApiWhitelistDTO;
import com.flinksight.common.service.ApiWhitelistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 接口白名单管理
 */
@RestController
@RequestMapping("/api/api-whitelist")
@RequiredArgsConstructor
public class ApiWhitelistController {

    private final ApiWhitelistService service;

    @PostMapping
    public ApiResponse<ApiWhitelistDTO> create(@RequestBody ApiWhitelistDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ApiWhitelistDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @PutMapping
    public ApiResponse<ApiWhitelistDTO> update(@RequestBody ApiWhitelistDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
