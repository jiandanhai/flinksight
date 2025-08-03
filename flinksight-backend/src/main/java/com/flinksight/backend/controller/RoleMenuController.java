package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色-菜单分配管理
 */
@RestController
@RequestMapping("/api/role-menu")
@RequiredArgsConstructor
public class RoleMenuController {

    private final RoleMenuService service;

    @PostMapping("/assign")
    public ApiResponse<RoleMenuDTO> assign(@RequestParam Long roleId, @RequestParam Long menuId) {
        return ApiResponse.ok(service.assignMenuToRole(roleId, menuId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long menuId) {
        return service.removeMenuFromRole(roleId, menuId);
    }

    @GetMapping("/role/{roleId}")
    public ApiResponse<PageResult<RoleMenuDTO>> findByRole(@PathVariable Long roleId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByRoleId(roleId,page,size));
    }

    @GetMapping("/menu/{menuId}")
    public ApiResponse<PageResult<RoleMenuDTO>> findByMenu(@PathVariable Long menuId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByMenuId(menuId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleMenuDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
