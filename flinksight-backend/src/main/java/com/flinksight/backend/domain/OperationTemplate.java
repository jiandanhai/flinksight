package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准操作模板表
 * 支持多租户自动化、脚本、调度模板管理
 */
@Getter
@Setter
@Entity
@Table(
        name = "operation_template",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_optemplate_tenant_type_name", columnNames = {"tenant_id", "type", "name"})
        },
        indexes = {
                @Index(name = "idx_optemplate_tenant_type", columnList = "tenant_id, type")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "操作模板表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class OperationTemplate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "模板ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    @Schema(description = "模板名称")
    private String name;

    @Column(name = "type", length = 32)
    @Schema(description = "模板类型")
    private String type;

    @Column(name = "content", columnDefinition = "text")
    @Schema(description = "模板内容")
    private String content;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
