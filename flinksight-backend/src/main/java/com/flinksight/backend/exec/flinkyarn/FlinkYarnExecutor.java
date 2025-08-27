// backend/exec/flinkyarn/FlinkYarnExecutor.java
package com.flinksight.backend.exec.flinkyarn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flinksight.backend.exec.TemplateExecutor;
import com.flinksight.backend.exec.artifact.ArtifactStoreRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明
 * 通过 ArtifactStoreRouter.fetch() 把 binrepo://... 拉到本地临时文件，再由 CLI 提交。
 * 需要后端主机具备 flink 命令与 HADOOP_CONF_DIR，适合 YARN Per-Job。
 * 也可把 CLI 调用放到“运维 Agent”节点，通过 RPC 远程执行（扩展点：抽象 CommandRunner）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlinkYarnExecutor implements TemplateExecutor {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ArtifactStoreRouter storeRouter;

    @Override
    public String execute(String rendered, Map<String, Object> vars) throws Exception {
        Cmd cmd = mapper.readValue(rendered, Cmd.class);
        validate(cmd);

        Map<String, String> env = new HashMap<>();
        if (cmd.getExtraEnvs() != null) cmd.getExtraEnvs().forEach((k,v)-> env.put(k, String.valueOf(v)));
        if (cmd.getHadoopConfDir()!=null) env.put("HADOOP_CONF_DIR", cmd.getHadoopConfDir());

        switch (cmd.getOp()) {
            case "SUBMIT" -> {
                // 仓库拉取 JAR -> 本地临时路径
                Path jar = storeRouter.fetch(cmd.getArtifactUri(), vars);
                List<String> argv = new ArrayList<>();
                argv.add(cmd.getFlinkHome()+"/bin/flink");
                argv.add("run-application");
                argv.add("-t"); argv.add("yarn-application");
                if (cmd.getQueue()!=null) { argv.add("-Dyarn.application.queue="+cmd.getQueue()); }
                if (cmd.getParallelism()!=null) { argv.add("-p"); argv.add(String.valueOf(cmd.getParallelism())); }
                if (cmd.getJobName()!=null) { argv.add("-Djob.name="+cmd.getJobName()); }
                argv.add(jar.toString());
                if (cmd.getMainClass()!=null) { argv.add("--class"); argv.add(cmd.getMainClass()); }
                if (cmd.getProgramArgs()!=null && !cmd.getProgramArgs().isBlank()) {
                    argv.addAll(Arrays.asList(cmd.getProgramArgs().split("\\s+")));
                }
                String out = run(argv, env);
                String appId = parseAppId(out);
                return "{\"result\":\"OK\",\"applicationId\":\""+appId+"\"}";
            }
            case "STOP" -> {
                List<String> argv = List.of(
                        cmd.getFlinkHome()+"/bin/flink", "stop", "-t", "yarn-application", cmd.getJobId()
                );
                run(argv, env);
                return "{\"result\":\"OK\"}";
            }
            case "SAVEPOINT" -> {
                List<String> argv = new ArrayList<>(List.of(
                        cmd.getFlinkHome()+"/bin/flink", "savepoint", "-t", "yarn-application", cmd.getJobId()
                ));
                if (cmd.getSavepointDir()!=null) { argv.add(cmd.getSavepointDir()); }
                String out = run(argv, env);
                return "{\"result\":\"OK\",\"savepoint\":\""+out+"\"}";
            }
            case "RESCALE" -> {
                Objects.requireNonNull(cmd.getParallelism(), "parallelism required for RESCALE");
                List<String> argv = List.of(
                        cmd.getFlinkHome()+"/bin/flink", "modify", "-t", "yarn-application",
                        cmd.getJobId(), "--parallelism", String.valueOf(cmd.getParallelism())
                );
                run(argv, env);
                return "{\"result\":\"OK\"}";
            }
            default -> throw new IllegalArgumentException("Unsupported op: " + cmd.getOp());
        }
    }

    @Override public String type() { return "FLINK_YARN"; }

    private static String run(List<String> argv, Map<String,String> env) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(argv);
        pb.redirectErrorStream(true);
        Map<String,String> pEnv = pb.environment();
        pEnv.putAll(env);
        Process p = pb.start();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line; while ((line = br.readLine()) != null) { sb.append(line).append('\n'); }
        }
        int code = p.waitFor();
        String out = sb.toString();
        if (code != 0) throw new RuntimeException("flink cli failed("+code+"): \n"+out);
        return out;
    }

    private static String parseAppId(String out) {
        // 简易解析：从输出中抓 application_xxx
        Matcher m = Pattern.compile("application_\\d+_\\d+").matcher(out);
        return m.find() ? m.group(0) : "";
    }

    private static void validate(Cmd c) {
        Objects.requireNonNull(c.getOp(), "op required");
        Objects.requireNonNull(c.getFlinkHome(), "flinkHome required");
        if ("SUBMIT".equals(c.getOp())) {
            Objects.requireNonNull(c.getArtifactUri(), "artifactUri required");
        } else {
            Objects.requireNonNull(c.getJobId(), "jobId required");
        }
    }

    /* ===== 内部命令模型 ===== */
    @lombok.Data public static class Cmd {
        private String op;
        private String flinkHome;
        private String hadoopConfDir;
        private Map<String,Object> extraEnvs;
        private String queue;
        private Integer parallelism;
        private String jobName;
        private String mainClass;
        private String artifactUri;
        private String programArgs;
        private String jobId;
        private String savepointDir;
    }
}
