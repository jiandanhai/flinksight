package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * 登录历史表
 * 记录用户每次登录行为，用于安全合规审计
 */
@Getter
@Setter
@Entity
@Table(
        name = "login_history",
        indexes = {
                @Index(name = "idx_loginhis_user_time", columnList = "user_id, login_time"),
                @Index(name = "idx_loginhis_ip", columnList = "ip_address")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录历史表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "ip_address", length = 64)
    @Schema(description = "登录IP")
    private String ipAddress;

    @Column(name = "login_type", length = 32)
    @Schema(description = "登录方式")
    private String loginType;

    @Column(name = "device_info", length = 128)
    @Schema(description = "设备信息")
    private String deviceInfo;

    @Column(name = "login_time", nullable = false)
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
