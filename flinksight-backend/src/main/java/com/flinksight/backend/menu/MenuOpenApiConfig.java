package com.flinksight.backend.menu;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.PathItem;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class MenuOpenApiConfig {

    private static final String MENU_YAML = "menu/menu-map.yaml";
    private static final String MENU_PREFIX = "/api/__menu__/";

    @Bean
    public MenuMap menuMap() {
        try (InputStream in = new ClassPathResource(MENU_YAML).getInputStream()) {
            return new Yaml().loadAs(in, MenuMap.class);
        } catch (Exception e) {
            throw new IllegalStateException("加载 " + MENU_YAML + " 失败: " + e.getMessage(), e);
        }
    }

    @Bean
    public OpenApiCustomizer injectMenuFromYaml(MenuMap map) {   // ✅ 返回类型
        return openApi -> {
            if (map == null || map.getTops() == null || map.getTops().isEmpty()) return;

            Paths paths = openApi.getPaths();
            if (paths == null) { paths = new Paths(); openApi.setPaths(paths); }

            for (MenuMap.Top top : map.getTops()) {
                String topPath = MENU_PREFIX + top.getCode();
                paths.addPathItem(topPath, pathItemFor(top, null));

                if (top.getChildren() != null) {
                    for (MenuMap.Item child : top.getChildren()) {
                        String url = MENU_PREFIX + top.getCode();
                        String suffix = safeSuffix(top.getCode(), child.getPath());
                        if (!suffix.isBlank()) url = url + "/" + suffix;
                        paths.addPathItem(url, pathItemFor(top, child));
                    }
                }
            }
        };
    }

    private static String safeSuffix(String topCode, String childPath) {
        if (childPath == null) return "";
        String normalized = childPath.startsWith("/") ? childPath.substring(1) : childPath;
        if (normalized.startsWith(topCode + "/")) {
            return normalized.substring(topCode.length() + 1);
        }
        return normalized.replace("/", "-");
    }

    private static PathItem pathItemFor(MenuMap.Top top, MenuMap.Item child) {
        Operation op = new Operation();
        op.setSummary((child == null ? top.getTitleZh() : child.getTitleZh()) + "（菜单）");

        Map<String, Object> xm = new LinkedHashMap<>();
        if (child == null) {
            xm.put("code",       top.getCode());
            xm.put("titleZh",    top.getTitleZh());
            xm.put("titleEn",    emptyToNull(top.getTitleEn()));
            xm.put("path",       top.getPath());
            xm.put("icon",       emptyToNull(top.getIcon()));
            xm.put("order",      top.getOrder());
            xm.put("permission", top.getPerm());
        } else {
            xm.put("code",       child.getCode());
            xm.put("parentCode", top.getCode());
            xm.put("titleZh",    child.getTitleZh());
            xm.put("titleEn",    emptyToNull(child.getTitleEn()));
            xm.put("path",       child.getPath());
            xm.put("order",      child.getOrder());
            xm.put("permission", child.getPerm());
        }
        Map<String, Object> ext = new LinkedHashMap<>();
        ext.put("x-menu", xm);
        op.setExtensions(ext);

        ApiResponses responses = new ApiResponses();
        responses.addApiResponse("204", new ApiResponse().description("No Content"));
        op.setResponses(responses);

        PathItem item = new PathItem();
        item.setGet(op);
        return item;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
