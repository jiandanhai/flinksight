package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "租户DTO")
public class TenantDTO implements Serializable {
    @Schema(description = "租户ID")
    private Long id;
    @Schema(description = "租户唯一编码")
    private String code;
    @Schema(description = "租户名称")
    private String name;
    @Schema(description = "联系人")
    private String contact;
    @Schema(description = "联系方式")
    private String contactInfo;
    @Schema(description = "状态 1启用 0禁用")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // 选填：租户下的用户列表（如有）
    private List<UserDTO> users;

    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
