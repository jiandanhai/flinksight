package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO implements Serializable {
    private Long id;
    private String name;
    private String code;
    private String contact;
    private Integer status;
    private LocalDateTime createTime;
    @Schema(description = "软删除")
    private Integer isDeleted;
}
