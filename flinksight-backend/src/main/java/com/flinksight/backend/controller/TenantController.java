package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Tenant;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.service.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理接口
 */
@Tag(name = "租户管理", description = "Tenant Management API")
@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
@TenantRequired
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "新建租户", description = "Create new tenant")
    @PostMapping("/create")
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantDTO dto) {
        return ResponseEntity.ok(tenantService.createTenant(dto));
    }

    @Operation(summary = "根据ID查询租户", description = "Get tenant by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询所有租户", description = "Get all tenants")
    @GetMapping("/list")
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @Operation(summary = "更新租户信息", description = "Update tenant info")
    @PutMapping("/update")
    public ResponseEntity<TenantDTO> updateTenant(@RequestBody TenantDTO dto) {
        return ResponseEntity.ok(tenantService.updateTenant(dto));
    }

    @Operation(summary = "删除租户", description = "Delete tenant")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
