package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业信息实体
 * 平台管理的逻辑作业元数据（调度模板/脚本定义/唯一标识）
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_info",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_jobinfo_jobname_tenant", columnNames = {"job_name", "tenant_id"}),
                @UniqueConstraint(name = "uk_jobinfo_traceid", columnNames = {"trace_id"})
        },
        indexes = {
                @Index(name = "idx_jobinfo_tenant", columnList = "tenant_id"),
                @Index(name = "idx_jobinfo_type", columnList = "job_type")
        }
)
@Schema(description = "作业定义表（元数据/调度模板/唯一标识）")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createdAt", "id"})
public class JobInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "作业ID")
    private Long id;

    @Column(name = "job_name", nullable = false, length = 128)
    @Schema(description = "作业名称")
    private String jobName;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "job_type", nullable = false, length = 32)
    @Schema(description = "作业类型")
    private String jobType;

    @Column(name = "project_code", length = 64)
    @Schema(description = "项目编码")
    private String projectCode;

    @Column(name = "operator", nullable = false, length = 64)
    @Schema(description = "操作人")
    private String operator;

    @Column(name = "source", nullable = false, length = 32)
    @Schema(description = "来源（平台/接口/导入等）")
    private String source;

    @Column(name = "trace_id", nullable = false, unique = true, length = 64)
    @Schema(description = "全局唯一 Trace ID")
    private String traceId;

    @Column(name = "remark", length = 256)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "register_at", nullable = false)
    @Schema(description = "注册时间戳")
    private Long registerAt;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
