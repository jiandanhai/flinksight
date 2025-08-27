package com.flinksight.backend.exec;

import java.util.Map;

/** 执行器接口：按模板类型执行渲染后的内容 */
public interface TemplateExecutor {
    /**
     * @param rendered 渲染后的文本（已替换 ${var}）
     * @param vars     原始变量（可能用于上下文）
     * @return 返回执行结果（建议 JSON/文本摘要）
     * @throws Exception 执行失败抛异常，AOP 会记录 FAIL
     */
    String execute(String rendered, Map<String, Object> vars) throws Exception;

    /** 执行器类型标识，如 SHELL/HTTP/SQL/K8S */
    String type();
}