package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 指标大盘
 */
@Data
@Entity
@Table(name = "metric_dashboard")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "指标大盘")
@Where(clause = "is_deleted=0")
public class MetricDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "大盘ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "配置JSON")
    @Column(columnDefinition = "text")
    private String config;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
