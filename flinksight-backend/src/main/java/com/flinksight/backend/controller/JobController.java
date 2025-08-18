package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务管理接口
 */
@Tag(name = "api", description = "Job任务管理")
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class    JobController {

    private final JobService jobService;

    @Operation(summary = "新建任务", description = "Create new job",operationId = "createJob")
    @PostMapping("/create")
    public ApiResponse<JobDTO> createJob(@RequestBody JobDTO dto) {
        return ApiResponse.ok(jobService.createJob(dto));
    }

    @Operation(summary = "根据ID查询任务", description = "Get job by ID",operationId = "getJob")
    @GetMapping("/{id}")
    public ApiResponse<JobDTO> getById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }
    @Operation(summary = "", operationId = "getAllJobs")
    @GetMapping
    public ApiResponse<PageResult<JobDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobService.getAll(page,size));
    }


    @Operation(summary = "查询租户下所有任务", description = "Get jobs by tenant",operationId = "getJobsByTenant")
    @GetMapping("/list")
    public ApiResponse<PageResult<JobDTO>> getJobsByTenant(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobService.getJobsByTenant(tenantId,page,size));
    }

    @Operation(summary = "查询集群下所有任务", description = "Get jobs by tenant and cluster",operationId = "getJobsByTenantAndCluster")
    @GetMapping("/listByCluster")
    public ApiResponse<PageResult<JobDTO>> getJobsByTenantAndCluster(
            @RequestParam Long tenantId,
            @RequestParam Long clusterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobService.getJobsByTenantAndCluster(tenantId,clusterId,page,size));
    }

    @Operation(summary = "更新任务信息", description = "Update job info",operationId = "updateJob")
    @PutMapping("/update")
    public ApiResponse<JobDTO> updateJob(@RequestBody JobDTO dto) {
        return ApiResponse.ok(jobService.updateJob(dto));
    }

    @Operation(summary = "删除任务（软删）", description = "Soft delete job",operationId = "deleteJob")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(@PathVariable Long id) {
        jobService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
