package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置/参数实体
 */
@Getter
@Setter
@Entity
@Table(
        name = "config",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_config_code", columnNames = {"code"})
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class Config implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "参数ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 128)
    @Schema(description = "参数编码")
    private String code;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "参数值")
    private String value;

    @Column(length = 255)
    @Schema(description = "参数说明")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
