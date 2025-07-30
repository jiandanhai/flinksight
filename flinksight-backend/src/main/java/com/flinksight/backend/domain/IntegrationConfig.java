package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方集成配置
 */
@Data
@Entity
@Table(name = "integration_config")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "第三方集成配置表")
@Where(clause = "is_deleted=0")
public class IntegrationConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "集成名称")
    private String name;

    @Schema(description = "集成类型")
    private String type;

    @Schema(description = "配置参数(JSON)")
    @Column(columnDefinition = "text")
    private String configJson;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "启用状态")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
