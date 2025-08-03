package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 集群管理接口
 */
@Tag(name = "集群管理", description = "Cluster Management API")
@RestController
@RequestMapping("/api/cluster")
@RequiredArgsConstructor
@TenantRequired
public class ClusterController {

    private final ClusterService clusterService;

    @Operation(summary = "新建集群", description = "Create new cluster")
    @PostMapping("/create")
    public ApiResponse<ClusterDTO> createCluster(@RequestBody ClusterDTO dto) {
        return ApiResponse.ok(clusterService.createOrUpdate(dto));
    }

    @Operation(summary = "根据ID查询集群", description = "Get cluster by ID")
    @GetMapping("/{id}")
    public ApiResponse<ClusterDTO> getClusterById(@PathVariable Long id) {
        return clusterService.getClusterById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询租户下所有集群", description = "Get clusters by tenant")
    @GetMapping("/list")
    public ApiResponse<PageResult<ClusterDTO>> getClustersByTenant(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(clusterService.getClustersByTenant(tenantId,page,size));
    }

    @Operation(summary = "更新集群信息", description = "Update cluster info")
    @PutMapping("/update")
    public ApiResponse<ClusterDTO> updateCluster(@RequestBody ClusterDTO dto) {
        return ApiResponse.ok(clusterService.createOrUpdate(dto));
    }

    @Operation(summary = "删除集群（软删）", description = "Soft delete cluster")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCluster(@PathVariable Long id) {
        clusterService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
