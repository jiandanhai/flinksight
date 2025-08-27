// backend/exec/artifact/ArtifactStore.java
package com.flinksight.backend.exec.artifact;

import java.nio.file.Path;
import java.util.Map;

public interface ArtifactStore {
    /** 
     * 将 uri 对应的制品取到本地（或返回可直接被后续执行器使用的“可寻址地址”）
     * 约定：实现可根据需要把远程 jar 下载到临时目录，并返回本地路径。
     */
    Path fetch(String uri, Map<String, Object> ctx) throws Exception;

    /** 类型标识，例如 BINREPO / S3 / HDFS */
    String type();

    /** uri 判定（如 binrepo://、s3://、http(s)://…） */
    boolean supports(String uri);
}
