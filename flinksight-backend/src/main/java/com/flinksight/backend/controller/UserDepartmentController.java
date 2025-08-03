package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-部门分配管理
 */
@RestController
@RequestMapping("/api/user-department")
@RequiredArgsConstructor
public class UserDepartmentController {

    private final UserDepartmentService service;

    @PostMapping("/assign")
    public ApiResponse<UserDepartmentDTO> assign(@RequestParam Long userId, @RequestParam Long departmentId) {
        return ApiResponse.ok(service.assignDepartmentToUser(userId, departmentId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long departmentId) {
        return service.removeDepartmentFromUser(userId, departmentId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserDepartmentDTO>> findByUser(@PathVariable Long userId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/department/{departmentId}")
    public ApiResponse<PageResult<UserDepartmentDTO>> findByDepartment(@PathVariable Long departmentId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(departmentId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDepartmentDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
