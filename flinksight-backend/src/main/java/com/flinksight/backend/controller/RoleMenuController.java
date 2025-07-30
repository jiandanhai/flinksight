package com.flinksight.backend.controller;

import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 角色-菜单分配管理
 */
@RestController
@RequestMapping("/api/role-menu")
@RequiredArgsConstructor
public class RoleMenuController {

    private final RoleMenuService service;

    @PostMapping("/assign")
    public RoleMenuDTO assign(@RequestParam Long roleId, @RequestParam Long menuId) {
        return service.assignMenuToRole(roleId, menuId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long menuId) {
        return service.removeMenuFromRole(roleId, menuId);
    }

    @GetMapping("/role/{roleId}")
    public List<RoleMenuDTO> findByRole(@PathVariable Long roleId) {
        return service.findByRoleId(roleId);
    }

    @GetMapping("/menu/{menuId}")
    public List<RoleMenuDTO> findByMenu(@PathVariable Long menuId) {
        return service.findByMenuId(menuId);
    }

    @GetMapping("/{id}")
    public Optional<RoleMenuDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
