package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotifyChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 通知渠道管理控制器
 */
@RestController
@Tag(name = "api", description = "通知渠道管理控制器API")
@RequestMapping("/api/notify/channel")
@RequiredArgsConstructor
@Validated
public class NotifyChannelController {

    private final NotifyChannelService notifyChannelService;

    @Operation(summary = "", description = "", operationId = "createNotifyChannel")
    @PostMapping("/create")
    public ApiResponse<NotifyChannelDTO> create(@Valid @RequestBody NotifyChannelDTO dto) {
        return ApiResponse.ok(notifyChannelService.create(dto));
    }

    @Operation(summary = "", description = "", operationId = "updateNotifyChannel")
    @PutMapping("/update")
    public ApiResponse<NotifyChannelDTO> update(@Valid @RequestBody NotifyChannelDTO dto) {
        return ApiResponse.ok(notifyChannelService.update(dto.getId(),dto));
    }

    @Operation(summary = "", description = "", operationId = "getNotifyChannel")
    @GetMapping("/get/{id}")
    public ApiResponse<Optional<NotifyChannelDTO>> getNotifyChannel(@PathVariable Long id) {
        return ApiResponse.ok(notifyChannelService.getMyById(id));
    }

    @Operation(summary = "通知渠道列表（分页）", description = "", operationId = "listNotifications")
    @GetMapping("/list")
    public ApiResponse<PageResult<NotifyChannelDTO>> list(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(notifyChannelService.list(page,size));
    }

    // ====== 前端：updateNotification(id, patch)（更新渠道，例如启停/改配置/改名称）=====
    @Operation(summary = "更新渠道（Patch，忽略空字段）")
    @PutMapping("/update/{id}")
    public NotifyChannelDTO updateNotification(@PathVariable Long id, @Valid @RequestBody NotifyChannelDTO patch) {

        return notifyChannelService.update(id, patch);
    }

    @Operation(summary = "", description = "", operationId = "deleteNotifyChannel")
    @PostMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        notifyChannelService.sDelete(id);
        return ApiResponse.ok(null);
    }
}
