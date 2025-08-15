package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotifyChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 通知渠道管理控制器
 */
@RestController
@Tag(name = "api", description = "通知渠道管理控制器API")
@RequestMapping("/api/notify-channel")
@RequiredArgsConstructor
public class NotifyChannelController {

    private final NotifyChannelService notifyChannelService;

    @Operation(summary = "", description = "", operationId = "createNotifyChannel")
    @PostMapping("/create")
    public ApiResponse<NotifyChannelDTO> create(@Valid @RequestBody NotifyChannelDTO dto) {
        return ApiResponse.ok(notifyChannelService.create(dto));
    }

    @Operation(summary = "", description = "", operationId = "updateNotifyChannel")
    @PostMapping("/update")
    public ApiResponse<NotifyChannelDTO> update(@Valid @RequestBody NotifyChannelDTO dto) {
        return ApiResponse.ok(notifyChannelService.update(dto));
    }

    @Operation(summary = "", description = "", operationId = "deleteNotifyChannel")
    @PostMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        notifyChannelService.delete(id);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "", description = "", operationId = "getNotifyChannel")
    @GetMapping("/get/{id}")
    public ApiResponse<NotifyChannelDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(notifyChannelService.getById(id));
    }

    @Operation(summary = "", description = "", operationId = "getNotifyChannelsByTenant")
    @GetMapping("/list")
    public ApiResponse<PageResult<NotifyChannelDTO>> list(@RequestParam Long tenantId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(notifyChannelService.listByTenant(tenantId,page,size));
    }
}
