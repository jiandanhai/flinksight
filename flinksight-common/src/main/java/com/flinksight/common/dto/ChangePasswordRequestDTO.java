package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "修改密码请求")
public class ChangePasswordRequestDTO {

    @NotBlank
    @Schema(description = "原密码", example = "OldPass#123")
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 64)
    @Schema(description = "新密码（8-64位）", example = "NewPass#123")
    private String newPassword;

    @NotBlank
    @Schema(description = "确认新密码", example = "NewPass#123")
    private String confirmPassword;
}