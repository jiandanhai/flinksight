package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户表
 */
@Getter
@Setter
@Entity
@Table(
        name = "tenant",
        uniqueConstraints = @UniqueConstraint(name = "uk_tenant_code", columnNames = "code"),
        indexes = {
                @Index(name = "idx_tenant_code", columnList = "code"),
                @Index(name = "idx_tenant_status", columnList = "status")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "租户表")
@SQLRestriction("is_deleted=0")
public class Tenant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "租户ID")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 64)
    @Schema(description = "租户编码（唯一）")
    private String code;

    @Column(name = "name", nullable = false, length = 128)
    @Schema(description = "租户名称")
    private String name;

    @Column(name = "contact", length = 100)
    @Schema(description = "联系人")
    private String contact;

    @Column(name = "contact_info", length = 128)
    @Schema(description = "联系人方式（邮箱/电话）")
    private String contactInfo; // 可选，兼容SsoTenant

    @Column(name = "remark", length = 255)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "status", nullable = false, columnDefinition = "tinyint default 1")
    @Schema(description = "状态 1启用 0禁用")
    private Integer status = 1;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
