package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.SysParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统参数管理
 */
@Tag(name = "api", description = "系统参数管理API")
@RestController
@RequestMapping("/api/sys-param")
@RequiredArgsConstructor
@Validated
public class SysParamController {

    private final SysParamService service;

    @Operation(summary = "", description = "",operationId = "createSysParam")
    @PostMapping
    public ApiResponse<SysParamDTO> create(@RequestBody SysParamDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getSysParam")
    @GetMapping("/{id}")
    public ApiResponse<SysParamDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllSysParams")
    @GetMapping
    public ApiResponse<PageResult<SysParamDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateSysParam")
    @PutMapping
    public ApiResponse<SysParamDTO> update(@RequestBody SysParamDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteSysParam")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
