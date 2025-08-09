package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 节点管理
 */
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService service;

    @Operation(summary = "", description = "",operationId = "createNode")
    @PostMapping
    public ApiResponse<NodeDTO> create(@RequestBody NodeDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getNode")
    @GetMapping("/{id}")
    public ApiResponse<NodeDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllNodes")
    @GetMapping
    public ApiResponse<PageResult<NodeDTO>> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getNodesByCluster")
    @GetMapping("/cluster/{clusterId}")
    public ApiResponse<PageResult<NodeDTO>> findByClusterId(
            @PathVariable Long clusterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByClusterId(clusterId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "updateNode")
    @PutMapping
    public ApiResponse<NodeDTO> update(@RequestBody NodeDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteNode")
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
