package com.flinksight.common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private String name;
    private String type; // YARN/K8S/Standalone
    private String endpoint;
    private String version;
    private String tags;
    private String apiEndpoint;
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
