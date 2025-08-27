package com.flinksight.sparkjob.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.flinksight.common.security.PermissionChecker;
import com.flinksight.common.service.AuditLogWriter;
import com.flinksight.common.tenant.TenantContextHolder;
import com.flinksight.common.utils.JsonUtil;
import com.flinksight.common.utils.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Nacos多租户配置中心服务
 * 支持动态热加载，变更链路追踪、权限校验
 */
@Slf4j
@Service
public class DynamicConfigService {

    private final ConfigService configService;
    private final AuditLogWriter auditLogWriter; // ===============注入Bean，而非静态调用

    /**
     * 构造函数，初始化 Nacos 配置服务（支持多环境、权限）
     * @param nacosServerAddr Nacos服务器地址
     * @param auditLogWriter 注入审计服务Bean
     */
    @Autowired
    public DynamicConfigService(String nacosServerAddr, AuditLogWriter auditLogWriter) throws Exception {
        Properties properties = new Properties();
        properties.put("serverAddr", nacosServerAddr);
        this.configService = NacosFactory.createConfigService(properties);
        this.auditLogWriter = auditLogWriter;
    }

    /**
     * 获取多租户配置内容
     */
    public String getConfig(String dataId, String group, Long tenantId, String operator) throws Exception {
        // 权限与多租户校验
        if (!PermissionChecker.hasConfigReadPermission(operator, tenantId)) {
            throw new SecurityException("无权访问租户配置");
        }
        return configService.getConfig(dataId, group, 3000);
    }

    /**
     * 注册配置变更监听器，支持多租户、链路追踪
     * @param dataId    配置ID 配置项唯一ID
     * @param group     分组
     * @param tenantId  租户ID
     * @param operator  当前操作人
     * @param configType 配置业务类型（如 alarm_rule）配置类型，如 "nacos", "spark-udf", "alarm-rule" 等
     * @param onConfigUpdate 配置回调（已解析对象）
     */
    public <T> void addChangeListener(
            String dataId, String group, Long tenantId, String operator,
            String configType, // <== 必须加configType
            Class<T> configClass,
            java.util.function.Consumer<T> onConfigUpdate
    ) throws Exception {
        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() { return null; }
            @Override
            public void receiveConfigInfo(String config) {
                String traceId = TraceUtil.getOrCreateTraceId();
                try {
                    // 多租户上下文
                    TenantContextHolder.setTenantId(tenantId);
                    // 权限校验
                    if (!PermissionChecker.hasConfigWritePermission(operator, tenantId)) {
                        log.warn("无权变更租户{}配置, 操作人:{}", tenantId, operator);
                        return;
                    }
                    // JSON反序列化
                    T parsedConfig = JsonUtil.fromJson(config, configClass);

                    // 回调通知业务服务层
                    try {
                        onConfigUpdate.accept(parsedConfig);
                    } catch (Exception ex) {
                        log.error("onConfigUpdate回调异常，已忽略。", ex);
                    }

                    // 审计日志埋点（对象建议存json）
                    auditLogWriter.logConfigChange(
                            configType, dataId, tenantId, operator, config, traceId
                    );
                    log.info("[Nacos] 配置热更新生效: dataId={}, group={}, 租户={}, traceId={}", dataId, group, tenantId, traceId);

                } catch (Exception e) {
                    log.error("[Nacos] 配置热加载处理异常: dataId={}, traceId={}", dataId, traceId, e);
                } finally {
                    TenantContextHolder.clear();
                }
            }
        });
    }
}
