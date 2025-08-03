package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录历史管理
 */
@RestController
@RequestMapping("/api/login-history")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService service;

    @PostMapping
    public ApiResponse<LoginHistoryDTO> create(@RequestBody LoginHistoryDTO dto) {

        return ApiResponse.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<LoginHistoryDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<LoginHistoryDTO>> findByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
