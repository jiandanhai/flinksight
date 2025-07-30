package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 登录历史日志实体
 */
@Data
@Entity
@Table(name = "login_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录历史表")
@Where(clause = "is_deleted=0")
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录IP")
    private String ip;

    @Schema(description = "登录方式")
    private String loginType;

    @Schema(description = "设备信息")
    private String deviceInfo;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
