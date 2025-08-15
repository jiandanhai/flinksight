package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Job业务统计接口
 */
@Tag(name = "api", description = "Job业务统计接口API")
@RestController
@RequestMapping("/api/job-statistics")
@RequiredArgsConstructor
public class JobStatisticsController {

    private final JobStatisticsService jobStatisticsService;

    /**
     * 获取Job漏斗统计，支持分页
     */
    @Operation(
            summary = "Job漏斗统计",
            description = "Job漏斗统计",
            operationId = "getJobFunnelsByTenant"
    )
    @GetMapping("/funnel")
    public ApiResponse<PageResult<JobFunnelDTO>> getJobFunnel(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(jobStatisticsService.getJobFunnel(tenantId, page, size));
    }
}
