package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-部门分配管理
 */
@Tag(name = "api", description = "用户-部门分配管理API")
@RestController
@RequestMapping("/api/user/department")
@RequiredArgsConstructor
@Validated
public class UserDepartmentController {

    private final UserDepartmentService service;

    @Operation(summary = "", description = "",operationId = "assignUserDepartment")
    @PostMapping("/assign")
    public ApiResponse<UserDepartmentDTO> assign(@RequestParam Long userId, @RequestParam Long departmentId) {
        return ApiResponse.ok(service.assignDepartmentToUser(userId, departmentId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserDepartment")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long departmentId) {
        return service.removeDepartmentFromUser(userId, departmentId);
    }

    @Operation(summary = "", description = "",operationId = "getUserDepartmentsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserDepartmentDTO>> findByUser(@PathVariable Long userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserDepartmentsByDepartment")
    @GetMapping("/department/{departmentId}")
    public ApiResponse<PageResult<UserDepartmentDTO>> findByDepartment(@PathVariable Long departmentId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(departmentId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserDepartment")
    @GetMapping("/id/{id}")
    public ApiResponse<UserDepartmentDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserDepartment")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
