package com.flinksight.backend.controller;

import com.flinksight.backend.domain.RoleDataScope;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.service.RoleDataScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 角色-数据权限分配管理
 */
@RestController
@RequestMapping("/api/role-data-scope")
@RequiredArgsConstructor
public class RoleDataScopeController {

    private final RoleDataScopeService service;

    @PostMapping("/assign")
    public RoleDataScopeDTO assign(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return service.assignDataScopeToRole(roleId, dataScopeId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long roleId, @RequestParam Long dataScopeId) {
        return service.removeDataScopeFromRole(roleId, dataScopeId);
    }

    @GetMapping("/role/{roleId}")
    public List<RoleDataScopeDTO> findByRole(@PathVariable Long roleId) {
        return service.findByRoleId(roleId);
    }

    @GetMapping("/data-scope/{dataScopeId}")
    public List<RoleDataScopeDTO> findByDataScope(@PathVariable Long dataScopeId) {
        return service.findByDataScopeId(dataScopeId);
    }

    @GetMapping("/{id}")
    public Optional<RoleDataScopeDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
