package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 标签表
 * 用于作业、用户、资源的分类与标记
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "label",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_label_tenant_name", columnNames = {"tenant_id", "name"})
        },
        indexes = {
                @Index(name = "idx_label_tenant_type", columnList = "tenant_id, type"),
                @Index(name = "idx_label_name", columnList = "name")
        }
)
@Schema(description = "标签表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @Schema(description = "标签名称")
    private String name;

    @Column(name = "color", length = 20)
    @Schema(description = "颜色，可选")
    private String color;

    @Column(name = "type", length = 20)
    @Schema(description = "标签类型，可选")
    private String type;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted = 0;
}
