package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签登录历史表实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "label_login_history")
@Schema(description = "标签登录历史表")
@Where(clause = "is_deleted=0")
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
    @Schema(description = "登录IP地址")
    private String ipAddress;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
}
