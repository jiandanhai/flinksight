package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeHealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 节点健康管理
 */
@RestController
@RequestMapping("/api/node-health")
@RequiredArgsConstructor
public class NodeHealthController {

    private final NodeHealthService service;

    @GetMapping("/{id}")
    public ApiResponse<NodeHealthDTO> get(@PathVariable Long id) {
        return service.getLatestByNodeId(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping("/node/{nodeId}")
    public ApiResponse<PageResult<NodeHealthDTO>> findByNodeId(
            @PathVariable Long nodeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getByNodeId(nodeId,page,size));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
