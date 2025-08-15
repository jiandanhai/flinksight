package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/** 用户 Token 版本状态（仅用于接口返回/业务传递） */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserTokenStateDTO", description = "用户令牌版本状态")
public class UserTokenStateDTO  implements Serializable {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "访问令牌 (JWT Access Token)")
    private String accessToken;

    @Schema(description = "访问令牌有效期（秒）")
    private long expiresIn;

    @Schema(description = "刷新令牌 (可选)")
    private String refreshToken;

    @Schema(description = "刷新令牌有效期（秒，可选）")
    private Long refreshExpiresIn;

    @Schema(description = "令牌类型，通常为 Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "当前 token 版本号（用于后端 tokenVersion 校验）")
    private Integer tokenVersion;

    @Schema(description = "最后更新时间")
    private Instant lastUpdated;
}
