package com.flinksight.backend.exec;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/** 默认执行器：不做任何实际动作，返回 OK。用于快速上线与本地开发 */
@Slf4j
@Component
public class NoopTemplateExecutor implements TemplateExecutor {
    @Override
    public String execute(String rendered, Map<String, Object> vars) {
        log.info("[NoopExecutor] rendered:\n{}", rendered);
        return "EXEC_OK";
    }
    @Override
    public String type() { return "NOOP"; }
}