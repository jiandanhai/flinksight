package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_event", indexes = {
        @Index(name = "idx_ev_tenant_time", columnList = "tenant_id, created_at desc"),
        @Index(name = "idx_ev_job_time", columnList = "job_id, created_at desc"),
        @Index(name = "idx_ev_cluster_time", columnList = "cluster_id, created_at desc"),
        @Index(name = "idx_ev_type", columnList = "type"),
        @Index(name = "idx_ev_severity", columnList = "severity"),
        @Index(name = "idx_ev_trace", columnList = "trace_id")
})
@Schema(description = "作业事件表：运行事件/心跳/告警汇聚")
public class JobEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long tenantId;
    @Column
    private Long clusterId;
    @Column(nullable = false)
    private Long jobId;
    @Column(length = 128)
    private String jobName;
    @Column(length = 16)
    private String engine;
    @Column(length = 16)
    private String env;
    @Column(nullable = false, length = 64)
    private String type;
    @Column(nullable = false)
    private Short severity;
    @Column(length = 32)
    private String source;
    @Column(length = 64)
    private String traceId;
    @Column(name = "event_ts", nullable = false)
    private LocalDateTime eventTs;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(length = 128)
    private String kafkaTopic;
    @Column
    private Integer kafkaPartition;
    @Column
    private Long kafkaOffset;
    @Column(length = 128)
    private String dedupeKey;
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    @PrePersist
    public void pre() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (severity == null) severity = 0;
        if (source == null) source = "pipeline";
    }
}