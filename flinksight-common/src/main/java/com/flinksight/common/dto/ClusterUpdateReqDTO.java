package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/** 部分字段可选；MapStruct IGNORE null 不覆盖 */
@Data
@Schema(description = "更新集群请求（部分字段可选）")
public class ClusterUpdateReqDTO {
    private String type;
    private String endpoint;
    private String version;
    private String tags;
    private String remark;
    private Map<String,String> extras;
}