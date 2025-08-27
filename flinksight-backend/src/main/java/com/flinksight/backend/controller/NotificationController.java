package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.IdListDTO;
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
        return service.getMyById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    // ====== 前端：notificationList() ======
    @Operation(summary = "消息列表（站内消息）",operationId = "listNotifications")
    @GetMapping("/list")
    public ApiResponse<PageResult<NotificationDTO>> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "0") int page,   // 前端若是 0-based 就保持一致；你也可接收 1-based 再 -1
            @RequestParam(defaultValue = "20") int size
    ) {
        var p = service.list(type, isRead, page,size);
        return ApiResponse.ok(p);
    }

    // ====== 前端：notificationMarkRead({ids}) ======
    @Operation(summary = "批量标记已读",operationId = "notificationMarkRead")
    @PostMapping("/mark-read")
    public int notificationMarkRead(@Valid @RequestBody IdListDTO body) {
        return service.markRead( body.getIds());
    }

    // ====== 前端：notificationDelete({ids}) ======
    @Operation(summary = "批量删除（软删）",operationId = "notificationDelete")
    @DeleteMapping
    public int notificationDelete(@Valid @RequestBody IdListDTO body) {
        return service.softDelete(body.getIds());
    }


    @Operation(summary = "", description = "",operationId = "getNotificationsByTenant")
    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<NotificationDTO>> findByTenant(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByTenantId(page,size));
    }

    @Operation(summary = "", description = "",operationId = "createNotification")
    @PutMapping("/update")
    public ApiResponse<NotificationDTO> update(@RequestBody @Valid NotificationDTO dto) {
        return ApiResponse.ok(service.createOrUpdate(dto));
    }

    @Operation(summary = "", description = "",operationId = "deleteNotification")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
