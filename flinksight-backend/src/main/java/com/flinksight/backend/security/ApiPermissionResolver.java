    package com.flinksight.backend.security;

    import lombok.Data;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.core.io.ClassPathResource;
    import org.springframework.stereotype.Component;
    import org.springframework.util.AntPathMatcher;
    import org.yaml.snakeyaml.Yaml;

    import jakarta.annotation.PostConstruct;
    import jakarta.servlet.http.HttpServletRequest;
    import java.io.InputStream;
    import java.util.*;

    /**
     * 解析器（加载规则并解析请求→权限码）
     * 根据 security/api-perm-rules.yaml 将 (method, uri) 解析为权限码 {RESOURCE}_{ACTION}
     * Controller 零侵入：不需要在方法上写 @PreAuthorize
     */
    @Slf4j
    @Component
    public class ApiPermissionResolver {
        private static final String RULES = "security/api-perm-rules.yaml";
        private final AntPathMatcher matcher = new AntPathMatcher();

        @Data
        public static class SuffixRule {
            private List<List<String>> bySuffix; // [[ "list", "VIEW" ], ...]
            private String _default;             // 映射的默认动作（VIEW/CREATE/UPDATE/DELETE/…）
            public String getDefault() { return _default; } // 兼容 YAML 字段名
            public void setDefault(String d) { this._default = d; }
        }
        @Data
        public static class Rule {
            private String pattern;                 // e.g. /api/user/**
            private List<String> methods;           // e.g. [GET, POST]
            private Map<String, SuffixRule> mapping;// method -> SuffixRule
        }
        @Data
        public static class RuleFile {
            private List<Rule> rules;
            private Map<String, String> _default;   // method -> ACTION
            public Map<String,String> getDefault() { return _default; }
            public void setDefault(Map<String,String> d) { this._default = d; }
        }

        private RuleFile conf;

        @PostConstruct
        public void load() {
            try (InputStream in = new ClassPathResource(RULES).getInputStream()) {
                this.conf = new Yaml().loadAs(in, RuleFile.class);
                int cnt = (conf != null && conf.getRules() != null) ? conf.getRules().size() : 0;
                log.info("[perm] loaded api rules: {}", cnt);
            } catch (Exception e) {
                throw new IllegalStateException("Load "+RULES+" failed", e);
            }
        }

        /** 返回需要的权限码，如 USER_VIEW；若不需鉴权返回 Optional.empty() */
        public Optional<String> resolve(HttpServletRequest req) {
            final String method = req.getMethod().toUpperCase(Locale.ROOT);
            final String path = req.getRequestURI();

            if (conf == null) return Optional.empty();
            final List<Rule> rules = (conf.getRules() != null) ? conf.getRules() : Collections.emptyList();

            for (Rule r : rules) {
                if (r.getMethods()==null || r.getPattern()==null) continue;
                if (!r.getMethods().stream().anyMatch(m -> method.equalsIgnoreCase(m))) continue;
                if (!matcher.match(r.getPattern(), path)) continue;

                String action = resolveAction(method, path, r.getMapping());
                String resource = extractResource(path);
                if (resource != null && action != null) {
                    return Optional.of(resource + "_" + action);
                }
            }

            // 全局默认兜底
            Map<String,String> def = conf.getDefault();
            if (def != null) {
                String act = def.get(method);
                String res = extractResource(path);
                if (act != null && res != null) {
                    return Optional.of(res + "_" + act);
                }
            }
            return Optional.empty();
        }

        private String extractResource(String path) {
            // /api/{resource}/... -> {resource}.toUpperCase()
            String[] segs = Arrays.stream(path.split("/"))
                    .filter(s -> s != null && !s.isEmpty()).toArray(String[]::new);
            if (segs.length >= 2 && "api".equals(segs[0])) {
                return segs[1].toUpperCase(Locale.ROOT);
            }
            return null;
        }

        private String resolveAction(String method, String path, Map<String, SuffixRule> mapping) {
            if (mapping == null) return null;
            SuffixRule sr = mapping.get(method);
            if (sr == null) return null;

            String last = lastLiteral(path);
            if (last != null && sr.getBySuffix() != null) {
                for (List<String> pair : sr.getBySuffix()) {
                    if (pair.size() == 2 && last.equalsIgnoreCase(pair.get(0))) {
                        return pair.get(1).toUpperCase(Locale.ROOT);
                    }
                }
            }
            String d = sr.getDefault();
            return d != null ? d.toUpperCase(Locale.ROOT) : null;
        }

        private String lastLiteral(String path) {
            String[] segs = Arrays.stream(path.split("/"))
                    .filter(s -> s != null && !s.isEmpty()).toArray(String[]::new);
            for (int i = segs.length - 1; i >= 0; i--) {
                String s = segs[i];
                if (!s.startsWith("{")) return s;
            }
            return null;
        }
    }
