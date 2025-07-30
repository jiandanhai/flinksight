package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.service.UserDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户-部门分配管理
 */
@RestController
@RequestMapping("/api/user-department")
@RequiredArgsConstructor
public class UserDepartmentController {

    private final UserDepartmentService service;

    @PostMapping("/assign")
    public UserDepartmentDTO assign(@RequestParam Long userId, @RequestParam Long departmentId) {
        return service.assignDepartmentToUser(userId, departmentId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long departmentId) {
        return service.removeDepartmentFromUser(userId, departmentId);
    }

    @GetMapping("/user/{userId}")
    public List<UserDepartmentDTO> findByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/department/{departmentId}")
    public List<UserDepartmentDTO> findByDepartment(@PathVariable Long departmentId) {
        return service.findByDepartmentId(departmentId);
    }

    @GetMapping("/{id}")
    public Optional<UserDepartmentDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
