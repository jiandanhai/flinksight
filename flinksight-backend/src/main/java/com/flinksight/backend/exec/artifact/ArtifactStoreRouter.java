// backend/exec/artifact/ArtifactStoreRouter.java
package com.flinksight.backend.exec.artifact;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

//Store 路由器
@Component
public class ArtifactStoreRouter {
    private final List<ArtifactStore> stores;
    public ArtifactStoreRouter(List<ArtifactStore> stores){ this.stores = stores; }

    public Path fetch(String uri, Map<String, Object> ctx) throws Exception {
        return stores.stream()
                .filter(s -> s.supports(uri))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No ArtifactStore supports: " + uri))
                .fetch(uri, ctx);
    }
}
