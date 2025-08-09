package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录历史表
 * 记录用户每次登录行为，用于安全合规审计
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "login_history",
        indexes = {
                @Index(name = "idx_loginhis_user_time", columnList = "user_id, login_time"),
                @Index(name = "idx_loginhis_ip", columnList = "ip_address"),
                @Index(name = "idx_loginhis_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "登录历史表")
@SQLRestriction("is_deleted=0") // 软删除
public class LoginHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Column(name = "login_type", length = 32)
    @Schema(description = "登录方式（PASSWORD/SSO/OAUTH/SMS/LDAP/ADMIN等）")
    private String loginType;

    @Column(name = "provider", length = 32)
    @Schema(description = "第三方渠道/平台")
    private String provider;

    @Column(name = "ip_address", length = 64)
    @Schema(description = "登录IP")
    private String ipAddress;

    @Column(name = "device_info", length = 128)
    @Schema(description = "设备信息")
    private String deviceInfo;

    @Column(name = "login_time", nullable = false)
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Column(name = "success_flag", nullable = false)
    @Schema(description = "是否成功（1=成功 0=失败）")
    private Integer successFlag = 1;

    @Column(name = "fail_reason", length = 128)
    @Schema(description = "失败原因")
    private String failReason;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
