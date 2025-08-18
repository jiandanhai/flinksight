package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典管理
 */
@RestController
@Tag(name = "api", description = "数据字典管理管理")
@RequestMapping("/api/dict")
@RequiredArgsConstructor
@Validated
public class DictController {

    private final DictService service;

    @Operation(summary = "", description = "",operationId = "createDict")
    @PostMapping("/create")
    public ApiResponse<DictDTO> create(@RequestBody  @Valid DictDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getDict")
    @GetMapping("/id/{id}")
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
    @PutMapping("/update")
    public ApiResponse<DictDTO> update(@RequestBody  @Valid DictDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteDict")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
