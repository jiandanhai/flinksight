package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源分组实体
 */
@Data
@Entity
@Table(name = "resource_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源分组表")
@Where(clause = "is_deleted=0")
public class ResourceGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分组ID")
    private Long id;

    @Schema(description = "分组名称")
    private String name;

    @Schema(description = "分组类型")
    private String type;

    @Schema(description = "父分组ID")
    private Long parentId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
