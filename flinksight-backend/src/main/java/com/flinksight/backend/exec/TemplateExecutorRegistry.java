package com.flinksight.backend.exec;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 执行器注册表：按 type 路由到对应执行器 以后新增执行器只需 @Component 实现 TemplateExecutor 并返回对应 type() 即自动注册。*/
@Component
@RequiredArgsConstructor
public class TemplateExecutorRegistry {

    private final Map<String, TemplateExecutor> registry = new ConcurrentHashMap<>();

    public TemplateExecutorRegistry(List<TemplateExecutor> executors) {
        executors.forEach(ex -> registry.put(ex.type().toUpperCase(), ex));
    }

    public TemplateExecutor resolve(String type) {
        if (type == null) return null;
        return registry.get(type.toUpperCase());
    }
}