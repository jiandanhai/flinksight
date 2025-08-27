package com.flinksight.backend.exec.flink;

import lombok.Data;

@Data
public class FlinkCommand {
    public enum Op { SUBMIT, STOP, SAVEPOINT, RESCALE, UPLOAD }

    private Op op;
    private String clusterBaseUrl;    // http://flink-jobmanager:8081
    private String jarId;             // /jars/{jarId}
    private String jobId;             // /jobs/{jobId}
    private String entryClass;
    private Integer parallelism;
    private String programArgs;
    private Boolean allowNonRestoredState;
    private Boolean drain;
    private String savepointDir;

    // 上传用
    private String uploadFileName;
    private String uploadFileBase64;
}