package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private String type; // MENU/BUTTON/API
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted;
}
