package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户DTO")
public class UserDTO implements Serializable {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "用户名/账号")
    private String username;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "SSO第三方唯一ID")
    private String ssoId;
    @Schema(description = "状态 1启用 0禁用")
    private Integer status;
    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "角色列表")
    private List<RoleDTO> roles;

    @Schema(description = "所属租户（多租户系统可选）")
    private TenantDTO tenant;

    // 选填：OAuth绑定信息
    private List<OauthAccountDTO> oauthAccounts;
}