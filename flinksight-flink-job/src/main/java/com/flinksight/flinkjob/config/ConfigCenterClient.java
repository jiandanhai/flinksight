package com.flinksight.flinkjob.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import java.util.Properties;

/**
 * 平台Nacos配置中心客户端
 * 支持参数自动热加载
 */
public class ConfigCenterClient {
    private static ConfigService configService;

    static {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", "nacos-server:8848");
            configService = NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            throw new RuntimeException("Nacos连接失败", e);
        }
    }

    public static String get(String dataId) {
        try {
            return configService.getConfig(dataId, "DEFAULT_GROUP", 5000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
