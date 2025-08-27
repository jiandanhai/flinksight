package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobDiagnosticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 任务链路与日志API
 */
@RestController
@Tag(name = "api", description = "任务链路与日志API")
@RequestMapping("/api/job/diagnostic")
@RequiredArgsConstructor
@Validated
public class JobDiagnosticController {

    private final JobDiagnosticService service;

    @Operation(summary = "", description = "",operationId = "listJobDiagnosticLogs")
    @GetMapping("/list")
    public ApiResponse<PageResult<JobDiagnosticLogDTO>> list(
            @RequestParam Long jobId,
            @RequestParam String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(jobId,level,page,size));
    }
}
