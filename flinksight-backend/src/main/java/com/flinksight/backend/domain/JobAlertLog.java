package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_alert_log",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_job", columnList = "job_id"),
                @Index(name = "idx_alert_time", columnList = "alert_time")
        }
)
@Schema(description = "作业报警日志表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class JobAlertLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "作业ID")
    private Long jobId;

    @Column(name = "job_name", length = 128)
    @Schema(description = "作业名称")
    private String jobName;

    @Column(name = "alert_type", length = 64)
    @Schema(description = "报警类型")
    private String alertType;

    @Column(name = "alert_msg", columnDefinition = "TEXT")
    @Schema(description = "报警消息内容")
    private String alertMsg;

    @Column(name = "alert_time", nullable = false)
    @Schema(description = "报警时间")
    private LocalDateTime alertTime;

    @Column(length = 16)
    @Schema(description = "状态 (SENT/ACK)")
    private String status;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
