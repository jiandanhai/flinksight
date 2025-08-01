package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 资源-标签关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "resource_label",
        uniqueConstraints = @UniqueConstraint(name = "uk_resource_label", columnNames = {"resource_id", "label_id"}),
        indexes = {
                @Index(name = "idx_resource_label_resource", columnList = "resource_id"),
                @Index(name = "idx_resource_label_label", columnList = "label_id")
        }
)
@Schema(description = "资源-标签关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class ResourceLabel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "resource_id", nullable = false)
    @Schema(description = "资源ID")
    private Long resourceId;

    @Column(name = "label_id", nullable = false)
    @Schema(description = "标签ID")
    private Long labelId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
