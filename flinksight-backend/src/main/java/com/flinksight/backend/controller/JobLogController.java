package com.flinksight.backend.controller;

import com.flinksight.backend.domain.JobLog;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.service.JobLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务日志接口
 */
@Tag(name = "任务日志", description = "JobLog API")
@RestController
@RequestMapping("/api/joblog")
@RequiredArgsConstructor
@TenantRequired
public class JobLogController {

    private final JobLogService jobLogService;

    @Operation(summary = "新建任务日志", description = "Create job log")
    @PostMapping("/create")
    public ResponseEntity<JobLogDTO> createJobLog(@RequestBody JobLogDTO dto) {
        return ResponseEntity.ok(jobLogService.createJobLog(dto));
    }

    @Operation(summary = "根据ID查询日志", description = "Get log by ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobLogDTO> getJobLogById(@PathVariable Long id) {
        return jobLogService.getJobLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询任务日志", description = "Get logs by job")
    @GetMapping("/listByJob")
    public ResponseEntity<List<JobLogDTO>> getLogsByJob(
            @RequestParam Long jobId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(
                jobLogService.getLogsByJob(
                        jobId,
                        LocalDateTime.parse(start),
                        LocalDateTime.parse(end))
        );
    }

    @Operation(summary = "按级别查询租户日志", description = "Get logs by tenant and level")
    @GetMapping("/listByTenantLevel")
    public ResponseEntity<List<JobLogDTO>> getLogsByTenantAndLevel(
            @RequestParam Long tenantId,
            @RequestParam String level,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(
                jobLogService.getLogsByTenantAndLevel(
                        tenantId, level,
                        LocalDateTime.parse(start),
                        LocalDateTime.parse(end))
        );
    }

    @Operation(summary = "删除日志（软删）", description = "Soft delete job log")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobLog(@PathVariable Long id) {
        jobLogService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
