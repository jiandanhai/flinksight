package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserApi;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户-API权限分配管理
 */
@RestController
@RequestMapping("/api/user-api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiService service;

    @PostMapping("/assign")
    public UserApiDTO assign(@RequestParam Long userId, @RequestParam Long apiId) {
        return service.assignApiToUser(userId, apiId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long apiId) {
        return service.removeApiFromUser(userId, apiId);
    }

    @GetMapping("/user/{userId}")
    public List<UserApiDTO> findByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/api/{apiId}")
    public List<UserApiDTO> findByApi(@PathVariable Long apiId) {
        return service.findByApiId(apiId);
    }

    @GetMapping("/{id}")
    public Optional<UserApiDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
