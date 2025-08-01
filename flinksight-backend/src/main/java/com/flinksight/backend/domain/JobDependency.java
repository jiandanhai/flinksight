package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 作业依赖关系表
 */
@Getter
@Setter
@Entity
@Table(
        name = "job_dependency",
        indexes = {
                @Index(name = "idx_job", columnList = "job_id"),
                @Index(name = "idx_dependency", columnList = "dependency_job_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业依赖关系表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class JobDependency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "作业ID")
    private Long jobId;

    @Column(name = "dependency_job_id", nullable = false)
    @Schema(description = "依赖的作业ID")
    private Long dependencyJobId;

    @Column(length = 32)
    @Schema(description = "依赖类型（前置、后置等）")
    private String type;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
