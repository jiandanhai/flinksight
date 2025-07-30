package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.service.ClusterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ClusterDTO> createCluster(@RequestBody ClusterDTO dto) {
        return ResponseEntity.ok(clusterService.createOrUpdate(dto));
    }

    @Operation(summary = "根据ID查询集群", description = "Get cluster by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClusterDTO> getClusterById(@PathVariable Long id) {
        return clusterService.getClusterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询租户下所有集群", description = "Get clusters by tenant")
    @GetMapping("/list")
    public ResponseEntity<List<ClusterDTO>> getClustersByTenant(@RequestParam Long tenantId) {
        return ResponseEntity.ok(clusterService.getClustersByTenant(tenantId));
    }

    @Operation(summary = "更新集群信息", description = "Update cluster info")
    @PutMapping("/update")
    public ResponseEntity<ClusterDTO> updateCluster(@RequestBody ClusterDTO dto) {
        return ResponseEntity.ok(clusterService.createOrUpdate(dto));
    }

    @Operation(summary = "删除集群（软删）", description = "Soft delete cluster")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        clusterService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
