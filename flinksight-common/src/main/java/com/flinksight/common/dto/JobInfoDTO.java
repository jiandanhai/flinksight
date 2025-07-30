package com.flinksight.common.dto;

import lombok.*;
import java.io.Serializable;

/**
 * 作业信息DTO，后端返回用
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfoDTO implements Serializable {
    private Long id;
    private String jobName;
    private Long tenantId;
    private String jobType;
    private String projectCode;
    private String operator;
    private String source;
    private String traceId;
    private String remark;
    private Long registerAt;
    private Integer isDeleted;
    private String createdAt;
    private String updatedAt;
}
