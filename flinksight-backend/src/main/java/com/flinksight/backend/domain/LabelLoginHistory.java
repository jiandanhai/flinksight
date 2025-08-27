package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签登录历史表
 * 记录用户与标签相关的登录行为，用于安全审计
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "label_login_history",
        indexes = {
                @Index(name = "idx_llh_tenant_user_time", columnList = "tenant_id, user_id, login_time"),
                @Index(name = "idx_llh_label_time", columnList = "label_id, login_time")
        }
)
@Schema(description = "标签登录历史表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"loginTime", "id"})
public class LabelLoginHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "label_id", nullable = false)
    @Schema(description = "标签ID")
    private Long labelId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "login_time", nullable = false)
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Column(name = "ip_address", length = 64)
    @Schema(description = "登录IP地址 (支持IPv6)")
    private String ipAddress;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除 0=正常 1=删除")
    private Integer isDeleted = 0;
}
