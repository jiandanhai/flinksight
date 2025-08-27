package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterService;
import com.flinksight.common.service.MetricDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 集群管理接口
 */
@Tag(name = "api", description = "集群管理")
@RestController
@RequestMapping("/api/cluster")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class ClusterController {

    private final ClusterService clusterService;
    private final MetricDashboardService metricDashboardService;

    @Operation(summary = "新建集群", description = "Create new cluster",operationId = "createCluster")
    @PostMapping("/create")
    public ApiResponse<ClusterDTO> createCluster(@RequestBody  @Valid ClusterDTO dto) {
        return ApiResponse.ok(clusterService.createCluster(dto));
    }

    @Operation(summary = "根据ID查询集群", description = "Get cluster by ID",operationId = "getCluster")
    @GetMapping("/id/{id}")
    public ApiResponse<ClusterDTO> getById(@PathVariable Long id) {
        return clusterService.getClusterById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询租户下所有集群", description = "Get clusters by tenant",operationId = "listClusters")
    @GetMapping("/list")
    public ApiResponse<PageResult<ClusterDTO>> getClustersByTenant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(clusterService.list(page,size));
    }

    @Operation(summary = "更新集群信息", description = "Update cluster info",operationId = "updateCluster")
    @PutMapping("/update/{id}")
    public ApiResponse<ClusterDTO> updateCluster(@PathVariable Long id, @RequestBody  @Valid ClusterDTO dto) {
        return ApiResponse.ok(clusterService.updateCluster(id,dto));
    }

    @Operation(summary = "启停集群（单条，幂等）",operationId = "enableOne")
    @PatchMapping("/enablement/{id}")
    public ApiResponse<ClusterDTO> enableOne(@PathVariable Long id, @RequestBody EnableReq body) {
        return ApiResponse.ok(clusterService.setClusterEnable(id, body.getEnable()));
    }

    @Operation(summary = "启停集群（批量，幂等）",operationId = "enableBatch")
    @PostMapping("/enablements")
    public ApiResponse<Void> enableBatch(@RequestBody EnableBatchReq body) {
        clusterService.setClusterEnableBatch(body.getIds(), body.getEnable());
        return ApiResponse.ok(null);
    }


    @Operation(summary = "删除集群（软删）", description = "Soft delete cluster",operationId = "deleteCluster")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteCluster(@PathVariable Long id) {
        clusterService.sDelete(id);
        return ApiResponse.ok(null);
    }


    /* --------------- Node --------------- */

    @Operation(summary = "创建节点",operationId = "createNode")
    @PostMapping("/nodes/create")
    public ApiResponse<NodeDTO> createNode(@Valid @RequestBody NodeDTO req) {
        return ApiResponse.ok(clusterService.createNode(req));
    }

    @Operation(summary = "批量创建节点",operationId = "batchCreateNode")
    @PostMapping("/nodes/batch-create")
    public ApiResponse<List<NodeDTO>> batchCreateNode(@Valid @RequestBody NodeBatchCreateReqDTO req) {
        return ApiResponse.ok(clusterService.batchAddNodes(req.getItems()));
    }

    @Operation(summary = "按集群分页查询节点",operationId = "getNodes")
    @GetMapping("/nodes/{clusterId}")
    public ApiResponse<PageResult<NodeDTO>> getNodes(
            @PathVariable Long clusterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.ok(clusterService.getNodesByCluster(clusterId, page,size));
    }


    @Operation(summary = "启停节点（单条，幂等）",operationId = "enableNode")
    @PatchMapping("/nodes/enablement/{clusterId}")
    public ApiResponse<NodeDTO> enableNode(@PathVariable Long nodeId, @RequestBody EnableReq body) {
        return ApiResponse.ok(clusterService.setNodeEnable(nodeId, body.getEnable()));
    }

    @Operation(summary = "启停节点（批量，幂等）",operationId = "enableNodeBatch")
    @PostMapping("/nodes/enablements")
    public ApiResponse<Void> enableNodeBatch(@RequestBody EnableBatchReq body) {
        clusterService.setNodeEnableBatch(body.getIds(), body.getEnable());
        return ApiResponse.ok(null);
    }

    @Operation(summary = "（集群维度）节点指标：CPU/内存/活跃节点", description = "agg=none/hour/day",operationId = "getNodeMetricByAgg")
    @GetMapping("/nodes/metric-agg/{clusterId}")
    public ApiResponse<NodeMetricResponseDTO> getNodeMetricByAgg(
            @PathVariable Long clusterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "none") String agg) {
        return ApiResponse.ok(metricDashboardService.getNodeMetric(clusterId, from, to, agg));
    }


    /* --------------- Health / Metrics --------------- */

    @Operation(summary = "查询节点健康（分页，按时间倒序）",operationId = "getNodeHealth")
    @GetMapping("/nodes/health/{nodeId}")
    public ApiResponse<PageResult<NodeHealthDTO>> getNodeHealth(
            @PathVariable Long nodeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(clusterService.getNodeHealth(nodeId, page, size));
    }

    @Operation(summary = "获取集群监控指标（最近一次或区间聚合）",operationId = "getNodeMetric")
    @GetMapping("/nodes/metric/{clusterId}")
    public ApiResponse<NodeMetricDTO> getNodeMetric(
            @PathVariable Long clusterId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {
        return ApiResponse.ok(clusterService.getNodeMetric(clusterId, from, to));
    }

    /* ---- request models ---- */
    @Data
    public static class EnableReq { @NotNull private Boolean enable; }
    @Data public static class EnableBatchReq { @NotEmpty private List<Long> ids; @NotNull private Boolean enable; }
}
