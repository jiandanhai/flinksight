// backend/exec/artifact/BinaryRepoArtifactStore.java
package com.flinksight.backend.exec.artifact;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

//自有仓库实现（HTTP + Token 示例）
@Component
@RequiredArgsConstructor
public class BinaryRepoArtifactStore implements ArtifactStore {

    private final RestTemplate rt = new RestTemplate();

    @Override
    public Path fetch(String uri, Map<String, Object> ctx) throws Exception {
        // 约定：uri 形如 binrepo://group/artifact/version/file.jar
        if (!supports(uri)) throw new IllegalArgumentException("Unsupported uri: " + uri);
        String base = (String) ctx.getOrDefault("repoBase", "https://repo.example.com");
        String token = (String) ctx.getOrDefault("repoToken", "");

        // 将自定义 scheme 映射到真实 http(s) 地址
        String real = base + "/api/v1/artifacts/" + uri.replaceFirst("^binrepo://", "");
        HttpHeaders headers = new HttpHeaders();
        if (!token.isBlank()) headers.setBearerAuth(token);
        ResponseEntity<byte[]> resp = rt.exchange(URI.create(real), HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
        if (!resp.getStatusCode().is2xxSuccessful()) throw new RuntimeException("repo fetch failed: " + resp.getStatusCode());

        Path tmp = Files.createTempFile("artifact-", "-" + real.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
        try (OutputStream os = Files.newOutputStream(tmp)) {
            StreamUtils.copy(resp.getBody(), os);
        }
        return tmp;
    }

    @Override public String type() { return "BINREPO"; }
    @Override public boolean supports(String uri) { return uri != null && uri.startsWith("binrepo://"); }
}
