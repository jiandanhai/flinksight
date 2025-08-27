package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobInstanceService;
import com.flinksight.common.service.JobMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/job/metric")
@RequiredArgsConstructor
@TenantRequired
@Validated
public class JobMetricController {

    private final JobMetricService jobMetricService;
    private final JobInstanceService jobInstanceService;

    @Operation(summary = "新建任务指标", description = "Create job metric",operationId = "createMetric")
    @PostMapping("/create")
    public ApiResponse<JobMetricDTO> createMetric(@RequestBody @Valid JobMetricDTO dto) {
        return ApiResponse.ok(jobMetricService.createMetric(dto));
    }

    @Operation(summary = "根据ID查询指标", description = "Get metric by ID",operationId = "getMetric")
    @GetMapping("/id/{id}")
    public ApiResponse<JobMetricDTO> getById(@PathVariable Long id) {
        return jobMetricService.getMetricById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "查询租户的某类型指标", description = "Get metrics by tenant and metricKey",operationId = "listJobMetrics")
    @GetMapping("/list")
    public ApiResponse<PageResult<JobMetricDTO>> list(
            @RequestParam Long jobId,
            @RequestParam String metricKey,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(jobMetricService.list(jobId,metricKey,LocalDateTime.parse(start),LocalDateTime.parse(end),page,size));
    }

    @Operation(summary = "", description = "",operationId = "deleteMetric")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteMetric(@PathVariable Long id) {
        jobMetricService.sDelete(id);
        return ApiResponse.ok(null);
    }

    /**
     * 获取租户下各作业状态计数（RUNNING/FAILED/SUCCESS等）
     */
    @Operation(summary = "", description = "",operationId = "getJobInstanceStatusCountByTenant")
    @GetMapping("/status-count")
    public Map<Integer, Long> statusCount() {
        return jobInstanceService.countStatusByTenantId();
    }

}
