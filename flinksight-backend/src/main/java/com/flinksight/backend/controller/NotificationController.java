package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 消息通知管理
 */
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public ApiResponse<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationDTO> get(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @GetMapping
    public ApiResponse<PageResult<NotificationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }


    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<NotificationDTO>> findByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<NotificationDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @PutMapping
    public ApiResponse<NotificationDTO> update(@RequestBody NotificationDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
