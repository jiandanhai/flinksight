package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 集群实体
 * Cluster Entity
 */
@Data
@Entity
@Table(name = "cluster")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "集群表")
@Where(clause = "is_deleted=0")
public class Cluster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "集群ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Column(nullable = false, length = 64)
    @Schema(description = "集群名称")
    private String name;

    @Column(nullable = false, length = 32)
    @Schema(description = "类型(YARN/K8S/Standalone)")
    private String type;

    @Column(nullable = false, length = 128)
    @Schema(description = "集群访问地址")
    private String endpoint;

    @Column(length = 32)
    @Schema(description = "版本号")
    private String version;

    @Column(length = 100)
    @Schema(description = "标签")
    private String tags;

    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @Schema(description = "状态")
    private Integer status;

    @Column(length = 255)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
