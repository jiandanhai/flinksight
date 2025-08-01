package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工单表
 */
@Getter
@Setter
@Entity
@Table(
        name = "ticket",
        indexes = {
                @Index(name = "idx_ticket_tenant", columnList = "tenant_id"),
                @Index(name = "idx_ticket_alert", columnList = "alert_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "工单表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "alert_id", nullable = false)
    @Schema(description = "关联报警事件ID")
    private Long alertId;

    @Column(name = "handler_id")
    @Schema(description = "处理人ID")
    private Long handlerId;

    @Column(nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "状态(0未处理1处理中2关闭)")
    private Integer status;

    @Column(length = 255)
    @Schema(description = "处理备注")
    private String note;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除")
    private Integer isDeleted = 0;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    // 推荐补充
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
