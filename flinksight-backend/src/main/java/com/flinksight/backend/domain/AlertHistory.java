package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警历史实体
 */
@Data
@Entity
@Table(name = "alert_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警历史表")
@Where(clause = "is_deleted=0")
public class AlertHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报警历史ID")
    private Long id;

    @Schema(description = "报警ID")
    private Long alertId;

    @Schema(description = "报警规则ID")
    private Long ruleId;

    @Schema(description = "报警内容")
    @Column(columnDefinition = "text")
    private String content;

    @Schema(description = "报警级别")
    private Integer level;

    @Schema(description = "处理状态")
    private Integer status;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
