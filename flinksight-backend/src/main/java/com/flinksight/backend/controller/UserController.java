package com.flinksight.backend.controller;

import com.flinksight.backend.domain.User;
import com.flinksight.backend.security.rbac.OpPermission;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.service.OpAudit;
import com.flinksight.common.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口
 */
@Tag(name = "用户管理", description = "User Management API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@TenantRequired
public class UserController {

    private final UserService userService;

    @Operation(summary = "创建用户", description = "Create new user")
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @Operation(summary = "根据ID查询用户", description = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "分页查询用户", description = "Get user list by tenant with paging")
    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getUsersByTenant(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.getUsersByTenant(tenantId, page, size));
    }

    @Operation(summary = "软删除用户", description = "Soft delete user")
    @OpPermission("user:delete")
    @OpAudit(action = "DELETE_USER", targetType = "User", targetIdSpEL = "#id", contentSpEL = "'删除用户-' + #id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        if (userService.softDelete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "更新用户信息", description = "Update user info")
    @OpPermission("user:delete")
    @OpAudit(action = "UPDATE_USER", targetType = "User", targetIdSpEL = "#id", contentSpEL = "'更新用户-' + #id")
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    @Operation(summary = "校验密码", description = "Check user password")
    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestParam Long userId, @RequestParam String rawPwd) {
        return ResponseEntity.ok(userService.checkPassword(userId, rawPwd));
    }
}
