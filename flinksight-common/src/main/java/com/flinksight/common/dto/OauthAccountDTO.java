package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方授权账号DTO
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "第三方OAuth绑定账号DTO")
public class OauthAccountDTO implements Serializable {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "平台类型(如 wechat、github、google)")
    private String provider;
    @Schema(description = "平台openid")
    private String openid;
    @Schema(description = "平台unionid")
    private String unionid;
    @Schema(description = "access token")
    private String accessToken;
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
    @Schema(description = "绑定时间")
    private LocalDateTime createTime;
}
