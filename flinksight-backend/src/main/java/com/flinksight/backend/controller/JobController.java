package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Job;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.service.JobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务管理接口
 */
@Tag(name = "任务管理", description = "Job Management API")
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@TenantRequired
public class JobController {

    private final JobService jobService;

    @Operation(summary = "新建任务", description = "Create new job")
    @PostMapping("/create")
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO dto) {
        return ResponseEntity.ok(jobService.createJob(dto));
    }

    @Operation(summary = "根据ID查询任务", description = "Get job by ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询租户下所有任务", description = "Get jobs by tenant")
    @GetMapping("/list")
    public ResponseEntity<List<JobDTO>> getJobsByTenant(@RequestParam Long tenantId) {
        return ResponseEntity.ok(jobService.getJobsByTenant(tenantId));
    }

    @Operation(summary = "查询集群下所有任务", description = "Get jobs by tenant and cluster")
    @GetMapping("/listByCluster")
    public ResponseEntity<List<JobDTO>> getJobsByTenantAndCluster(@RequestParam Long tenantId, @RequestParam Long clusterId) {
        return ResponseEntity.ok(jobService.getJobsByTenantAndCluster(tenantId, clusterId));
    }

    @Operation(summary = "更新任务信息", description = "Update job info")
    @PutMapping("/update")
    public ResponseEntity<JobDTO> updateJob(@RequestBody JobDTO dto) {
        return ResponseEntity.ok(jobService.updateJob(dto));
    }

    @Operation(summary = "删除任务（软删）", description = "Soft delete job")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}
