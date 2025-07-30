package com.flinksight.backend.controller;

import com.flinksight.backend.domain.ApiAccessLog;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.service.ApiAccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API访问日志管理
 */
@RestController
@RequestMapping("/api/api-access-log")
@RequiredArgsConstructor
public class ApiAccessLogController {

    private final ApiAccessLogService service;

    @PostMapping
    public ApiAccessLogDTO create(@RequestBody ApiAccessLogDTO apiAccessLogDTO) {
        return service.createOrUpdate(apiAccessLogDTO);
    }

    @GetMapping("/{id}")
    public Optional<ApiAccessLogDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ApiAccessLogDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ApiAccessLogDTO> findByTenantId(@PathVariable Long tenantId) {
        return service.findByTenantId(tenantId);
    }

    @PutMapping
    public ApiAccessLogDTO update(@RequestBody ApiAccessLogDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
