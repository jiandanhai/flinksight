// src/main/java/com/flinksight/backend/exec/flink/FlinkTemplateExecutor.java
package com.flinksight.backend.exec.flink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flinksight.backend.exec.TemplateExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlinkTemplateExecutor implements TemplateExecutor {

    private final ObjectMapper mapper;
    private final FlinkRestClient client;

    @Override
    public String execute(String rendered, Map<String, Object> vars) throws Exception {
        FlinkCommand cmd = mapper.readValue(rendered, FlinkCommand.class);
        validate(cmd);

        switch (cmd.getOp()) {
            case SUBMIT -> {
                String jobId = client.submitJar(
                        cmd.getClusterBaseUrl(), cmd.getJarId(),
                        cmd.getEntryClass(), cmd.getParallelism(),
                        cmd.getProgramArgs(), cmd.getAllowNonRestoredState()
                );
                return "{\"result\":\"OK\",\"jobId\":\"" + jobId + "\"}";
            }
            case STOP -> {
                client.stopJob(
                        cmd.getClusterBaseUrl(), cmd.getJobId(),
                        Boolean.TRUE.equals(cmd.getDrain()),
                        cmd.getSavepointDir()
                );
                return "{\"result\":\"OK\"}";
            }
            case SAVEPOINT -> {
                String resp = client.triggerSavepoint(
                        cmd.getClusterBaseUrl(), cmd.getJobId(),
                        cmd.getSavepointDir(), Boolean.TRUE.equals(cmd.getDrain())
                );
                return resp; // Flink 会返回触发 operation 的 JSON
            }
            case RESCALE -> {
                if (cmd.getParallelism() == null || cmd.getParallelism() <= 0) {
                    throw new IllegalArgumentException("parallelism required for RESCALE");
                }
                client.rescale(cmd.getClusterBaseUrl(), cmd.getJobId(), cmd.getParallelism());
                return "{\"result\":\"OK\"}";
            }
            case UPLOAD -> {
                String r = client.uploadJar(
                        cmd.getClusterBaseUrl(),
                        cmd.getUploadFileName(),
                        cmd.getUploadFileBase64()
                );
                return r; // 返回包含 jarId 的 JSON
            }
            default -> throw new UnsupportedOperationException("Unsupported op: " + cmd.getOp());
        }
    }

    @Override
    public String type() { return "FLINK"; }

    private void validate(FlinkCommand c) {
        if (c.getOp() == null) throw new IllegalArgumentException("op required");
        if (c.getClusterBaseUrl() == null || c.getClusterBaseUrl().isBlank())
            throw new IllegalArgumentException("clusterBaseUrl required");
        // 按操作类型校验
        switch (c.getOp()) {
            case SUBMIT -> {
                if (isBlank(c.getJarId())) throw new IllegalArgumentException("jarId required");
                if (isBlank(c.getEntryClass())) throw new IllegalArgumentException("entryClass required");
            }
            case STOP, SAVEPOINT, RESCALE -> {
                if (isBlank(c.getJobId())) throw new IllegalArgumentException("jobId required");
            }
            case UPLOAD -> {
                if (isBlank(c.getUploadFileName()) || isBlank(c.getUploadFileBase64()))
                    throw new IllegalArgumentException("uploadFileName & uploadFileBase64 required");
            }
        }
    }
    private static boolean isBlank(String s){ return s==null || s.isBlank(); }
}
