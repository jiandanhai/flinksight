package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 任务日志接口
 */
@Tag(name = "api", description = "Job任务日志API")
@RestController
@RequestMapping("/api/job/log")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class JobLogController {

    private final JobLogService jobLogService;

    @Operation(summary = "新建任务日志", description = "Create job log",operationId = "createJobLog")
    @PostMapping("/create")
    public ApiResponse<JobLogDTO> createJobLog(@RequestBody @Valid JobLogDTO dto) {
        return ApiResponse.ok(jobLogService.createJobLog(dto));
    }

    @Operation(summary = "根据ID查询日志", description = "Get log by ID",operationId = "getJobLog")
    @GetMapping("/id/{id}")
    public ApiResponse<JobLogDTO> getById(@PathVariable Long id) {
        return jobLogService.getJobLogById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询任务日志", description = "Get logs by job",operationId = "getJobLogsByJob")
    @GetMapping("/listByJob")
    public ApiResponse<PageResult<JobLogDTO>> getLogsByJob(
            @RequestParam Long jobId,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobLogService.getLogsByJob(jobId,LocalDateTime.parse(start),LocalDateTime.parse(end),page,size));
    }

    @Operation(summary = "按级别查询租户日志", description = "Get logs by tenant and level",operationId = "getJobLogsByTenantAndLevel")
    @GetMapping("/listByTenantLevel")
    public ApiResponse<PageResult<JobLogDTO>> getLogsByTenantAndLevel(
            @RequestParam Long tenantId,
            @RequestParam String level,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobLogService.getLogsByTenantAndLevel(tenantId,level,LocalDateTime.parse(start),LocalDateTime.parse(end),page,size));
    }

    @Operation(summary = "删除日志（软删）", description = "Soft delete job log",operationId = "deleteJobLog")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteJobLog(@PathVariable Long id) {
        jobLogService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
