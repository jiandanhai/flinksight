package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobBatchUpdateStatusRequestDTO;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.dto.JobInfoDTO;
import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobRegisterService;
import com.flinksight.common.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final JobRegisterService jobRegisterService;
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

    @Operation(summary = "查询集群下所有任务", description = "Get jobs by tenant and cluster",operationId = "listJobs")
    @GetMapping("/listByCluster")
    public ApiResponse<PageResult<JobDTO>> list(
            @RequestParam Long clusterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobService.list(clusterId,page,size));
    }

    @Operation(summary = "更新任务信息", description = "Update job info",operationId = "updateJob")
    @PutMapping("/update")
    public ApiResponse<JobDTO> updateJob(@RequestBody @Valid JobDTO dto) {
        return ApiResponse.ok(jobService.updateJob(dto));
    }

    @Operation(summary = "批量更新任务状态")
    @PostMapping("/status-batch")
    public ApiResponse<Integer> batchUpdateStatus(@RequestBody @Valid JobBatchUpdateStatusRequestDTO req) {
        int updated = jobService.batchUpdateJobStatus(req);
        return ApiResponse.ok(updated);
    }

    @Operation(summary = "删除任务（软删）", description = "Soft delete job",operationId = "deleteJob")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteJob(@PathVariable Long id) {
        jobService.sDelete(id);
        return ApiResponse.ok(null);
    }

    /**
     * 自动注册作业，平台幂等/权限校验/多租户
     */
    @Operation(summary = "", description = "",operationId = "registerJob")
    @PostMapping("/register")
    public ApiResponse<JobInfoDTO> registerJob(@RequestBody @Valid JobRegisterRequestDTO req) {
        // （建议接口层可加租户/平台黑白名单防刷）
        JobInfoDTO job = jobRegisterService.register(req);
        return ApiResponse.ok(job);
    }
}
