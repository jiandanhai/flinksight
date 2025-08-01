package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源实体（支持JDBC/ES/CK等）
 */
@Getter
@Setter
@Entity
@Table(
        name = "data_source",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_type", columnList = "type")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据源表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class DataSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "数据源ID")
    private Long id;

    @Column(nullable = false, length = 128)
    @Schema(description = "数据源名称")
    private String name;

    @Column(nullable = false, length = 32)
    @Schema(description = "数据源类型，如mysql、es、ck")
    private String type;

    @Column(name = "connect_info", columnDefinition = "TEXT")
    @Schema(description = "连接信息(JSON/DSN)")
    private String connectInfo;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(length = 255)
    @Schema(description = "描述")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
