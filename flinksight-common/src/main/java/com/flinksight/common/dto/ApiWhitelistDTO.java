package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 接口白名单DTO
 */
@Data
@Schema(description = "接口白名单DTO")
public class ApiWhitelistDTO {
    private Long id;
    private String path;
    private String method;
    private String description;
    private Integer isDeleted;
}
