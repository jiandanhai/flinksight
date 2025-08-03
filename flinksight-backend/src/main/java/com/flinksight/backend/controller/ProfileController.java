package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户扩展档案管理
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ApiResponse<ProfileDTO> create(@RequestBody ProfileDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProfileDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<ProfileDTO> getByUserId(@PathVariable Long userId) {

        return ApiResponse.ok(service.getByUserId(userId));
    }

    @PutMapping
    public ApiResponse<ProfileDTO> update(@RequestBody ProfileDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
