package com.flinksight.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI apiDoc() {
        return new OpenAPI().info(new Info().title("Flinksight API").version("1.0")
                .description("FlinkSpark 实时监控SaaS平台 API 文档"));
    }
}
