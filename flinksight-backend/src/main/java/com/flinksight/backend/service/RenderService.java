package com.flinksight.backend.service;

import com.flinksight.common.model.JobSpec;
import com.flinksight.common.utils.Jsons;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 将 JobSpec 渲染为具体的 K8s CRD YAML（FlinkDeployment / SparkApplication）。
 * 使用 Mustache 模板，确保与 CI/GitOps 兼容（可审计）。
 */
@Service
@RequiredArgsConstructor
public class RenderService {
    private final MustacheFactory mf = new DefaultMustacheFactory();

    private Map<String, Object> ctx(JobSpec spec, String name) {
        Map<String, Object> map = Jsons.toMap(spec);
        map.put("name", name);
        return map;
    }

    public String renderJob(String name, JobSpec spec) {
        String tpl = spec.getEngine() == JobSpec.Engine.FLINK
                ? "/templates/flink/flinkdeployment.yaml.tpl"
                : "/templates/spark/sparkapplication.yaml.tpl";
        return render(tpl, ctx(spec, name));
    }

    public String render(String tplPath, Map<String, Object> ctx) {
        try (Reader in = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(tplPath)), StandardCharsets.UTF_8);
             StringWriter out = new StringWriter()) {
            Mustache m = mf.compile(in, tplPath);
            m.execute(out, ctx).flush();
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Render failed: " + tplPath, e);
        }
    }
}