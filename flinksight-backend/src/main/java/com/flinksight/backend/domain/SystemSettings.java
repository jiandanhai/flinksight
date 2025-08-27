package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 全局系统设置表
 */
@Getter
@Setter
@Entity
@Table(
        name = "system_settings",
        uniqueConstraints = @UniqueConstraint(name = "uk_system_settings_code", columnNames = "code"),
        indexes = @Index(name = "idx_system_settings_code", columnList = "code")
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "全局系统设置表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"code", "id"})
public class SystemSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 64)
    @Schema(description = "设置项编码（唯一）")
    private String code;

    @Column(name = "value", columnDefinition = "text")
    @Schema(description = "设置项值")
    private String value;

    @Column(name = "description", length = 256)
    @Schema(description = "备注")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
