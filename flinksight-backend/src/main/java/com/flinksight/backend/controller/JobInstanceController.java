package com.flinksight.backend.controller;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.service.JobInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 作业实例管理API
 */
@RestController
@RequestMapping("/api/job-instance")
@RequiredArgsConstructor
public class JobInstanceController {

    private final JobInstanceService service;

    @PostMapping("/create")
    public JobInstanceDTO create(@RequestBody JobInstanceDTO dto) {
        return service.createJob(dto);
    }

    @PostMapping("/update-status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam String status) {
        return service.updateJobStatus(id, status);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<JobInstanceDTO> listByTenant(@PathVariable Long tenantId) {
        return service.listByTenant(tenantId);
    }

    @GetMapping("/status/{status}")
    public List<JobInstanceDTO> listByStatus(@PathVariable String status) {
        return service.listByStatus(status);
    }

    @GetMapping("/{id}")
    public Optional<JobInstanceDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
