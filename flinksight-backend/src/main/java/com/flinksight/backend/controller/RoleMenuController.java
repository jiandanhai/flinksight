package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 角色-菜单分配管理
 */
@Tag(name = "api", description = "角色-菜单分配管理API")
@RestController
@RequestMapping("/api/role/menu")
@RequiredArgsConstructor
@Validated
public class RoleMenuController {

    private final RoleMenuService service;

    @Operation(summary = "", description = "",operationId = "assignRoleMenu")
    @PostMapping("/assign")
    public ApiResponse<RoleMenuDTO> assign(@RequestParam Long roleId, @RequestParam Long menuId) {
        return ApiResponse.ok(service.assignMenuToRole(roleId, menuId));
    }

    @Operation(summary = "", description = "",operationId = "removeRoleMenu")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long menuId) {
        return service.removeMenuFromRole(roleId, menuId);
    }

    @Operation(summary = "", description = "",operationId = "getRoleMenusByRole")
    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<RoleMenuDTO>> findByRole(@PathVariable Long roleId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getRoleMenusByMenu")
    @GetMapping("/menu/{menuId}")
    public ApiResponse<PageResult<RoleMenuDTO>> findByMenu(@PathVariable Long menuId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByMenuId(menuId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getRoleMenu")
    @GetMapping("/id/{id}")
    public ApiResponse<RoleMenuDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteRoleMenu")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
