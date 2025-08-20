package com.flinksight.backend.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// 利用现有 MenuMap，把顶级和子级 path 汇总成白名单
@Component
@RequiredArgsConstructor
public class MenuWhitelistFromMap {
    private final MenuMap menuMap;

    public List<String> patterns(String base) { // base 可传 "/api" 或 "/"
        if (menuMap == null || menuMap.getTops()==null) return List.of();
        String b = (base==null || "/".equals(base)) ? "" : base;
        List<String> out = new ArrayList<>();
        for (var top : menuMap.getTops()) {
            if (top.getPath()!=null) {
                out.add((b + norm(top.getPath())));
                out.add((b + norm(top.getPath()) + "/**"));
            }
            if (top.getChildren()!=null) {
                for (var c : top.getChildren()) {
                    if (c.getPath()!=null) {
                        out.add((b + norm(c.getPath())));
                        out.add((b + norm(c.getPath()) + "/**"));
                    }
                }
            }
        }
        return out.stream().distinct().toList();
    }
    private String norm(String p){ return p.startsWith("/")? p : ("/"+p); }
}
