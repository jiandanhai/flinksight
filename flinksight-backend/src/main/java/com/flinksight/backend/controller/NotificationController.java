package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Notification;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 消息通知管理
 */
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public NotificationDTO create(@RequestBody NotificationDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<NotificationDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<NotificationDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<NotificationDTO> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<NotificationDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public NotificationDTO update(@RequestBody NotificationDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
