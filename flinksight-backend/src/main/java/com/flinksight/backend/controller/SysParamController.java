package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 系统参数管理
 */
@RestController
@RequestMapping("/api/sys-param")
@RequiredArgsConstructor
public class SysParamController {

    private final SysParamService service;

    @PostMapping
    public ApiResponse<SysParamDTO> create(@RequestBody SysParamDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<SysParamDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<SysParamDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @PutMapping
    public ApiResponse<SysParamDTO> update(@RequestBody SysParamDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
