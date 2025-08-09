package com.flinksight.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 第三方授权账号绑定表
 * 支持多平台（如微信、钉钉、飞书、Github、企业微信、Google等）账号与本地用户的绑定
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oauth_account", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_provider", columnList = "provider"),
        @Index(name = "idx_unionid", columnList = "unionid"),
        @Index(name = "idx_openid", columnList = "openid")
})
public class OauthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 本地用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 平台类型（如 wechat、dingding、feishu、github、google...） */
    @Column(name = "provider", length = 32, nullable = false)
    private String provider;

    /** 第三方平台openId */
    @Column(name = "openid", length = 64, nullable = false)
    private String openid;

    /** 第三方平台unionId（如有） */
    @Column(name = "unionid", length = 64)
    private String unionid;

    /** 授权Token（敏感，建议加密或不落库，仅调试用） */
    @Column(name = "access_token", length = 256)
    private String accessToken;

    /** Token过期时间 */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /** 授权时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /** 软删除 0=正常 1=删除 */
    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;
}
