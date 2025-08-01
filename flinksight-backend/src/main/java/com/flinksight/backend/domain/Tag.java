package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签表
 */
@Getter
@Setter
@Entity
@Table(
        name = "tag",
        uniqueConstraints = @UniqueConstraint(name = "uk_tag_name_tenant", columnNames = {"name", "tenant_id"}),
        indexes = {
                @Index(name = "idx_tag_tenant", columnList = "tenant_id"),
                @Index(name = "idx_tag_name", columnList = "name")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "标签ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "标签名")
    private String name;

    @Column(name = "color", length = 32)
    @Schema(description = "标签颜色")
    private String color;

    @Column(name = "type", length = 32)
    @Schema(description = "标签类型")
    private String type;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
