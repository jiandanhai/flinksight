package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口白名单DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "接口白名单DTO")
public class ApiWhitelistDTO  implements Serializable {
    private Long id;
    private String path;
    private String method;
    private String description;
    private Integer isDeleted;
}
