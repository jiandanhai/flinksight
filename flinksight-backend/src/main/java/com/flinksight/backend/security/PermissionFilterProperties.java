package com.flinksight.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一的权限过滤配置（唯一真源）
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.permission-filter")
public class PermissionFilterProperties {

    /** 是否启用 PermissionCheckFilter */
    private boolean enabled = true;

    /** 仅对这个前缀下的请求做权限校验；你的项目都是 /api 开头，这里就默认 "/api" */
    private String apiPrefix = "/api";

    /**
     * 白名单：支持两种写法
     * 1) "/public/**"  （方法不限）
     * 2) "GET /api/user/me"（限定方法）
     */
    private List<String> skip = new ArrayList<>();

    /**
     * 是否把 menu/menu-map.yaml 中的【一级/二级菜单 path】自动加入白名单
     * 会根据 apiPrefix 自动加前缀
     */
    private boolean includeMenuMap = true;
}
