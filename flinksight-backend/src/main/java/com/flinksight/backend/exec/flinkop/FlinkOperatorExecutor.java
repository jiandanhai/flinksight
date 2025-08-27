// backend/exec/flinkop/FlinkOperatorExecutor.java
package com.flinksight.backend.exec.flinkop;

import com.flinksight.backend.exec.TemplateExecutor;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

//执行器实现（Fabric8 方式加载 YAML） KubeConfig/ServiceAccount 自动发现；若需指定，注入自定义 KubernetesClient Bean 即可
@Component
@RequiredArgsConstructor
public class FlinkOperatorExecutor implements TemplateExecutor {

    private final KubernetesClient k8s;

    @Override
    public String execute(String rendered, Map<String, Object> vars) throws Exception {
        // rendered 为 YAML（可能包含多个文档）
        try (InputStream is = new ByteArrayInputStream(rendered.getBytes(StandardCharsets.UTF_8))) {
            List<HasMetadata> items = k8s.load(is).items();
            for (HasMetadata item : items) {
                String ns = item.getMetadata() != null ? item.getMetadata().getNamespace() : null;
                if (ns == null || ns.isBlank()) {
                    ns = (String) vars.getOrDefault("namespace", "default");
                }
                // 存在则替换，不存在则创建
                k8s.resource(item).inNamespace(ns).createOrReplace();
            }
        }
        return "{\"result\":\"OK\"}";
    }

    @Override
    public String type() { return "FLINK_OP"; }
}
