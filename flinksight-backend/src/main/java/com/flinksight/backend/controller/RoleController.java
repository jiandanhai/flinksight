package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理接口
 */
@Tag(name = "api", description = "角色管理接口API")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "新建角色", description = "Create new role",operationId = "createRole")
    @PostMapping("/create")
    public ApiResponse<RoleDTO> createRole(@RequestBody @Valid RoleDTO dto) {
        return ApiResponse.ok(roleService.createRole(dto));
    }

    @Operation(summary = "根据ID查询角色", description = "Get role by ID",operationId = "getRole")
    @GetMapping("/id/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }


    @Operation(summary = "分页查询角色",operationId = "listRoles")
    @GetMapping("/list")
    public ApiResponse<PageResult<RoleDTO>> listRolesLegacy(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ApiResponse.ok(roleService.list(keyword, name, code, page, size));
    }

    @Operation(summary = "编辑角色",operationId = "updateRole")
    @PutMapping("/update")
    public ApiResponse<RoleDTO> update(@RequestBody @Valid RoleDTO dto) {
        return ApiResponse.ok(roleService.update(dto));
    }

    @Operation(summary = "删除角色", description = "Delete role by ID",operationId = "deleteRole")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.sDelete(id);
        return ApiResponse.ok(null);
    }
}
