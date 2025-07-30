package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源表（如对象存储、HDFS、S3等）
 */
@Data
@Entity
@Table(name = "resource")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源管理表")
@Where(clause = "is_deleted=0")
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "资源ID")
    private Long id;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "资源类型")
    private String type;

    @Schema(description = "资源路径/URL")
    private String path;

    @Schema(description = "归属租户ID")
    private Long tenantId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
