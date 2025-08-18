package com.flinksight.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 跨域处理
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/public/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST");

        // 打印调试信息
        System.out.println("CORS mapping applied to /**");
    }
}