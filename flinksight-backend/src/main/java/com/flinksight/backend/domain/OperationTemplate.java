package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准操作模板表
 */
@Data
@Entity
@Table(name = "operation_template")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "操作模板表")
@Where(clause = "is_deleted=0")
public class OperationTemplate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "模板ID")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板类型")
    private String type;

    @Schema(description = "模板内容")
    @Column(columnDefinition = "text")
    private String content;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
