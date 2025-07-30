package com.flinksight.backend.controller;

import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.service.JobDiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务链路与日志API
 */
@RestController
@RequestMapping("/api/job-diagnostic")
@RequiredArgsConstructor
public class JobDiagnosticController {

    private final JobDiagnosticService service;

    @GetMapping("/logs/{jobId}")
    public List<JobDiagnosticLogDTO> getLogsByJob(@PathVariable Long jobId) {
        return service.getLogsByJob(jobId);
    }

    @GetMapping("/logs-level/{level}")
    public List<JobDiagnosticLogDTO> getLogsByLevel(@PathVariable String level) {
        return service.getLogsByLevel(level);
    }
}
