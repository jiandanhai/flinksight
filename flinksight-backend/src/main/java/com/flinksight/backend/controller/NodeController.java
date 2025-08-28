package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

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

    /** 环图统计（与列表同口径） */
    @Operation(summary = "环图统计（与列表同口径）”）", description = "",operationId = "healthBuckets")
    @GetMapping("/nodes/{clusterId}/health-buckets")
    public ApiResponse<Map<String, Long>> healthBuckets(@PathVariable Long clusterId) {
        return ApiResponse.ok(service.healthBuckets(clusterId));
    }

    /** 节点健康历史（节点详情页/弹窗） */
    @Operation(summary = "节点健康历史（节点详情页/弹窗））”）", description = "",operationId = "nodeHealthHistory")
    @GetMapping("/nodes/health/{nodeId}")
    public ApiResponse<Map<String, Object>> nodeHealthHistory(@PathVariable Long nodeId,
                                                    @RequestParam String from,
                                                    @RequestParam String to,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "100") int size) {
        return ApiResponse.ok(service.nodeHealthHistory(nodeId, LocalDateTime.parse(from), LocalDateTime.parse(to), page, size));
    }

    @Operation(summary = "", description = "",operationId = "updateNode")
    @PutMapping("/update")
    public ApiResponse<NodeDTO> update(@RequestBody @Valid NodeDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }


    @Operation(summary = "查询节点健康（时间区间）",operationId = "getNodeHealthSeries")
    @GetMapping("/{nodeId}/health")
    public ApiResponse<NodeHealthResponseDTO> getNodeHealthSeries(
            @PathVariable Long nodeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ApiResponse.ok(service.getNodeHealthSeries(nodeId, from, to));
    }

    @Operation(summary = "", description = "",operationId = "deleteNode")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
