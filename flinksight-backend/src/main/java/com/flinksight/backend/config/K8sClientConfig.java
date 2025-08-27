// src/main/java/com/flinksight/backend/config/K8sClientConfig.java
package com.flinksight.backend.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class K8sClientConfig {

    /** 可选：通过配置或环境变量指定 kubeconfig 路径 */
    @Value("${k8s.kubeconfig:}")
    private String kubeconfigPath;

    /** 生成并注入 KubernetesClient；在容器里会自动用 ServiceAccount；本地优先用 kubeconfig */
    @Bean(destroyMethod = "close")
    public KubernetesClient kubernetesClient() throws Exception {
        Config cfg;
        if (StringUtils.hasText(kubeconfigPath)) {
            String yaml = Files.readString(Path.of(kubeconfigPath));
            cfg = Config.fromKubeconfig(yaml);
        } else {
            // 自动探测：KUBECONFIG / ~/.kube/config / InCluster
            cfg = Config.autoConfigure(null);
        }
        return new KubernetesClientBuilder().withConfig(cfg).build();
    }
}
