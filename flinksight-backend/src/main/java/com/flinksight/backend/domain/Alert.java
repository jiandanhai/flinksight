package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警事件实体
 * Alert Entity
 */
@Getter
@Setter
@Entity
@Table(name = "`alert`") // ⚡ 防止关键字冲突
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警事件表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class Alert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "任务ID")
    private Long jobId;

    @Column(length = 16)
    @Schema(description = "报警级别")
    private String level;

    @Column(length = 32)
    @Schema(description = "报警类型")
    private String type;

    @Column(length = 255)
    @Schema(description = "报警内容")
    private String message;

    @Column(nullable = false)
    @Schema(description = "状态(0未处理1处理中2关闭)")
    private Integer status = 0; // ⚡ 默认值放到 Java，不要 columnDefinition

    @Column(name = "handler_id")
    @Schema(description = "处理人")
    private Long handlerId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "产生时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
