package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 消息通知管理
 */
@RestController
@Tag(name = "api", description = "消息通知管理API")
@RequestMapping("/api/notify")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService service;

    @Operation(summary = "", description = "",operationId = "createNotification")
    @PostMapping
    public ApiResponse<NotificationDTO> create(@RequestBody @Valid NotificationDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "getNotification")
    @GetMapping("/id/{id}")
    public ApiResponse<NotificationDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "getAllNotifications")
    @GetMapping("/list")
    public ApiResponse<PageResult<NotificationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAll(page,size));
    }

    @Operation(summary = "", description = "",operationId = "getNotificationsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<NotificationDTO>> findByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getNotificationsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<NotificationDTO>> findByTenantId(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(tenantId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "createNotification")
    @PutMapping("/update")
    public ApiResponse<NotificationDTO> update(@RequestBody @Valid NotificationDTO dto) {

        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteNotification")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
