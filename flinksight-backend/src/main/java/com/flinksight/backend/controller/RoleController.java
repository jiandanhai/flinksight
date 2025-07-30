package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口
 */
@Tag(name = "角色管理", description = "Role Management API")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@TenantRequired
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "新建角色", description = "Create new role")
    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO dto) {
        return ResponseEntity.ok(roleService.createRole(dto));
    }

    @Operation(summary = "根据ID查询角色", description = "Get role by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询所有角色", description = "Get all roles")
    @GetMapping("/list")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "删除角色", description = "Delete role by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
