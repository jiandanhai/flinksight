package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 作业实例管理API
 */
@RestController
@RequestMapping("/api/job-instance")
@RequiredArgsConstructor
public class JobInstanceController {

    private final JobInstanceService service;

    @PostMapping("/create")
    public ApiResponse<JobInstanceDTO> create(@RequestBody JobInstanceDTO dto) {

        return ApiResponse.ok(service.createJob(dto));
    }

    @PostMapping("/update-status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return service.updateJobStatus(id, status);
    }

    @GetMapping("/tenant/{tenantId}")
    public ApiResponse<PageResult<JobInstanceDTO>> listByTenant(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.listByTenant(tenantId,page,size));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<PageResult<JobInstanceDTO>> listByStatus(
            @PathVariable Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.listByStatus(status,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<JobInstanceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
