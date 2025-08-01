package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统参数表
 */
@Getter
@Setter
@Entity
@Table(
        name = "sys_param",
        uniqueConstraints = @UniqueConstraint(name = "uk_sys_param_code", columnNames = "code"),
        indexes = {
                @Index(name = "idx_sys_param_type", columnList = "type"),
                @Index(name = "idx_sys_param_code", columnList = "code")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统参数表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class SysParam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "参数ID")
    private Long id;

    @Column(name = "name", length = 64)
    @Schema(description = "参数名称")
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 64)
    @Schema(description = "参数编码（全局唯一）")
    private String code;

    @Column(name = "value", columnDefinition = "text")
    @Schema(description = "参数值")
    private String value;

    @Column(name = "type", length = 32)
    @Schema(description = "参数类型")
    private String type;

    @Column(name = "description", length = 256)
    @Schema(description = "备注")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
