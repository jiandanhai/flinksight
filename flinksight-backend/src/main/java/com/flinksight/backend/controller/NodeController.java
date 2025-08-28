package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 节点管理
 */
@Tag(name = "api", description = "节点管理API")
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
@Validated
public class NodeController {

    private final NodeService service;

    @Operation(summary = "", description = "",operationId = "createNode")
    @PostMapping
    public ApiResponse<NodeDTO> create(@RequestBody @Valid NodeDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getNode")
    @GetMapping("/id/{id}")
    public ApiResponse<NodeDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "listNodes")
    @GetMapping("/list")
    public ApiResponse<PageResult<NodeDTO>> list(
            @RequestParam Long clusterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(clusterId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateNode")
    @PutMapping("/update")
    public ApiResponse<NodeDTO> update(@RequestBody @Valid NodeDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }


    @Operation(summary = "查询节点健康（时间区间）",operationId = "getNodeHealth")
    @GetMapping("/{nodeId}/health")
    public ApiResponse<NodeHealthResponseDTO> getNodeHealth(
            @PathVariable Long nodeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ApiResponse.ok(service.getNodeHealth(nodeId, from, to));
    }

    @Operation(summary = "", description = "",operationId = "deleteNode")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
