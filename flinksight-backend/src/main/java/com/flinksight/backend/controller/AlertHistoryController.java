package com.flinksight.backend.controller;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.service.AlertHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 报警历史管理
 */
@RestController
@RequestMapping("/api/alert-history")
@RequiredArgsConstructor
public class AlertHistoryController {

    private final AlertHistoryService service;

    @PostMapping
    public AlertHistoryDTO create(@RequestBody AlertHistoryDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<AlertHistoryDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<AlertHistoryDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<AlertHistoryDTO> findByTenantId(@PathVariable Long tenantId) {

        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public AlertHistoryDTO update(@RequestBody AlertHistoryDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
