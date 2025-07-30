package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 资源-标签关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resource_label", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_id", "label_id"}))
@Schema(description = "资源-标签关联表")
@Where(clause = "is_deleted=0")
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
    private Integer isDeleted;
}
