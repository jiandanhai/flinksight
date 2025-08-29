package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 节点健康管理
 */
@Tag(name = "api", description = "节点健康管理API")
@RestController
@RequestMapping("/api/node/health")
@RequiredArgsConstructor
@Validated
public class NodeHealthController {

    private final NodeHealthService service;

    @Operation(summary = "", description = "",operationId = "getNodeHealth")
    @GetMapping("/id/{id}")
    public ApiResponse<NodeHealthDTO> getById(@PathVariable Long id) {
        return service.getLatestByNodeId(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "listNodeHealths")
    @GetMapping("/list")
    public ApiResponse<PageResult<NodeHealthDTO>> list(
            @RequestParam Long nodeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(nodeId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "deleteNodeHealth")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
