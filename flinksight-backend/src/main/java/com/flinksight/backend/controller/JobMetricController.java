package com.flinksight.backend.controller;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.domain.JobMetric;
import com.flinksight.backend.repository.JobInstanceRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.service.JobInstanceService;
import com.flinksight.common.service.JobMetricService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务指标接口
 */
@Tag(name = "任务指标", description = "JobMetric API")
@RestController
@RequestMapping("/api/metric")
@RequiredArgsConstructor
@TenantRequired
public class JobMetricController {

    private final JobMetricService jobMetricService;
    private final JobInstanceService jobInstanceService;

    @Operation(summary = "新建任务指标", description = "Create job metric")
    @PostMapping("/create")
    public ResponseEntity<JobMetricDTO> createMetric(@RequestBody JobMetricDTO dto) {
        return ResponseEntity.ok(jobMetricService.createMetric(dto));
    }

    @Operation(summary = "根据ID查询指标", description = "Get metric by ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobMetricDTO> getMetricById(@PathVariable Long id) {
        return jobMetricService.getMetricById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "查询任务的指标", description = "Get metrics by job")
    @GetMapping("/listByJob")
    public ResponseEntity<List<JobMetricDTO>> getMetricsByJob(
            @RequestParam Long jobId,
            @RequestParam String start, // ISO格式字符串
            @RequestParam String end) {
        return ResponseEntity.ok(
                jobMetricService.getMetricsByJob(jobId,
                        LocalDateTime.parse(start),
                        LocalDateTime.parse(end))
        );
    }

    @Operation(summary = "查询租户的某类型指标", description = "Get metrics by tenant and metricKey")
    @GetMapping("/listByTenant")
    public ResponseEntity<List<JobMetricDTO>> getMetricsByTenantAndMetric(
            @RequestParam Long tenantId,
            @RequestParam String metricKey,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(
                jobMetricService.getMetricsByTenantAndMetric(
                        tenantId, metricKey,
                        LocalDateTime.parse(start),
                        LocalDateTime.parse(end))
        );
    }

    @Operation(summary = "删除指标（软删）", description = "Soft delete metric")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        jobMetricService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取租户下各作业状态计数（RUNNING/FAILED/SUCCESS等）
     */
    @GetMapping("/status-count/{tenantId}")
    public Map<Integer, Long> statusCount(@PathVariable Long tenantId) {
        List<JobInstanceDTO> jobs = jobInstanceService.findByTenantIdAndIsDeleted(tenantId);
        return jobs.stream().collect(Collectors.groupingBy(JobInstanceDTO::getStatus, Collectors.counting()));
    }

    /**
     * 获取最近N个成功/失败作业
     */
    @GetMapping("/last-jobs/{tenantId}")
    public List<JobInstanceDTO> lastJobs(@PathVariable Long tenantId, @RequestParam(defaultValue = "10") int limit) {
        List<JobInstanceDTO> jobs = jobInstanceService.findByTenantIdAndIsDeleted(tenantId);
        return jobs.stream()
                .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
