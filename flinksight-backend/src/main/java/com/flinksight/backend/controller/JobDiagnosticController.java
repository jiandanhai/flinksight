package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobDiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 任务链路与日志API
 */
@RestController
@RequestMapping("/api/job-diagnostic")
@RequiredArgsConstructor
public class JobDiagnosticController {

    private final JobDiagnosticService service;

    @GetMapping("/logs/{jobId}")
    public ApiResponse<PageResult<JobDiagnosticLogDTO>> getLogsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getLogsByJob(jobId,page,size));
    }

    @GetMapping("/logs-level/{level}")
    public ApiResponse<PageResult<JobDiagnosticLogDTO>> getLogsByLevel(
            @PathVariable String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getLogsByLevel(level,page,size));
    }
}
