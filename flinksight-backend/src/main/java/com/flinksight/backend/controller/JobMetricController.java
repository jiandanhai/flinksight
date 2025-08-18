package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobInstanceService;
import com.flinksight.common.service.JobMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务指标接口
 */
@Tag(name = "api", description = "Job任务指标API")
@RestController
@RequestMapping("/api/metric")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class JobMetricController {

    private final JobMetricService jobMetricService;
    private final JobInstanceService jobInstanceService;

    @Operation(summary = "新建任务指标", description = "Create job metric",operationId = "createMetric")
    @PostMapping("/create")
    public ApiResponse<JobMetricDTO> createMetric(@RequestBody JobMetricDTO dto) {
        return ApiResponse.ok(jobMetricService.createMetric(dto));
    }

    @Operation(summary = "根据ID查询指标", description = "Get metric by ID",operationId = "getMetric")
    @GetMapping("/{id}")
    public ApiResponse<JobMetricDTO> getById(@PathVariable Long id) {
        return jobMetricService.getMetricById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询任务的指标", description = "Get metrics by job",operationId = "getMetricsByJob")
    @GetMapping("/listByJob")
    public ApiResponse<PageResult<JobMetricDTO>> getMetricsByJob(
            @RequestParam Long jobId,
            @RequestParam String start, // ISO格式字符串
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobMetricService.getMetricsByJob(jobId,LocalDateTime.parse(start),LocalDateTime.parse(end),page,size));
    }

    @Operation(summary = "查询租户的某类型指标", description = "Get metrics by tenant and metricKey",operationId = "getMetricsByTenantAndMetricKey")
    @GetMapping("/listByTenant")
    public ApiResponse<PageResult<JobMetricDTO>> getMetricsByTenantAndMetric(
            @RequestParam Long tenantId,
            @RequestParam String metricKey,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobMetricService.getMetricsByTenantAndMetric(tenantId,metricKey,LocalDateTime.parse(start),LocalDateTime.parse(end),page,size));
    }

    @Operation(summary = "", description = "",operationId = "deleteMetric")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMetric(@PathVariable Long id) {
        jobMetricService.softDelete(id);
        return ApiResponse.ok(null);
    }

    /**
     * 获取租户下各作业状态计数（RUNNING/FAILED/SUCCESS等）
     */
    @Operation(summary = "", description = "",operationId = "getJobInstanceStatusCountByTenant")
    @GetMapping("/status-count/{tenantId}")
    public Map<Integer, Long> statusCount(@PathVariable Long tenantId) {
        return jobInstanceService.countStatusByTenantId(tenantId);
    }

    /**
     * 获取最近N个成功/失败作业
     */
    @Operation(summary = "", description = "",operationId = "getJobInstanceLastJobsByTenant")
    @GetMapping("/last-jobs/{tenantId}")
    public ApiResponse<PageResult<JobInstanceDTO>> lastJobs(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(jobInstanceService.findByTenantIdAndIsDeleted(tenantId,page,size));
    }
}
