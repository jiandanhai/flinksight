package com.flinksight.backend.service;

import io.fabric8.kubernetes.api.model.HasMetadata;import io.fabric8.kubernetes.client.KubernetesClient;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;import java.nio.charset.StandardCharsets;import java.util.List;
/**
 * K8s 客户端封装：负责将 YAML（CRD）应用到目标集群。
 * 使用 server-side apply，具备幂等与声明式特性，便于回滚与审计。
 */
@Service @RequiredArgsConstructor
public class K8sClientFacade {
  private final KubernetesClient client;
  public void applyYaml(String yaml){
    List<HasMetadata> list = client.load(new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8))).items();
    client.resourceList(list).serverSideApply();
  }
}