package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClusterType {
    YARN("YARN", "Yarn集群"),
    K8S("K8S", "Kubernetes集群"),
    STANDALONE("Standalone", "独立部署");

    private final String code;
    private final String desc;
}
