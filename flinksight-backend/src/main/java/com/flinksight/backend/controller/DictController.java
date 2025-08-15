package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典管理
 */
@RestController
@Tag(name = "api", description = "数据字典管理管理")
@RequestMapping("/api/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService service;

    @Operation(summary = "", description = "",operationId = "createDict")
    @PostMapping
    public ApiResponse<DictDTO> create(@RequestBody DictDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getDict")
    @GetMapping("/{id}")
    public ApiResponse<DictDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getDictsByDictType")
    @GetMapping("/type/{dictType}")
    public ApiResponse<PageResult<DictDTO>> findByDictType(
            @PathVariable String dictType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByDictType(dictType,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateDict")
    @PutMapping
    public ApiResponse<DictDTO> update(@RequestBody DictDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteDict")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
