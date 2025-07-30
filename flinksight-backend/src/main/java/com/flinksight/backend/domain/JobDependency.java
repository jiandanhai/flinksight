package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;

/**
 * 作业依赖关系表
 */
@Data
@Entity
@Table(name = "job_dependency")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业依赖关系表")
@Where(clause = "is_deleted=0")
public class JobDependency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "作业ID")
    private Long jobId;

    @Schema(description = "依赖的作业ID")
    private Long dependencyJobId;

    @Schema(description = "依赖类型（前置、后置等）")
    private String type;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
