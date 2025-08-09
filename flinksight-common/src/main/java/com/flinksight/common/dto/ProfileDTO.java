package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户扩展档案DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户扩展档案DTO")
public class ProfileDTO  implements Serializable {
    private Long id;
    private Long userId;
    private String avatar;
    private String bio;
    private String extraJson;
    private Integer isDeleted;
    private LocalDateTime updateTime;
}
