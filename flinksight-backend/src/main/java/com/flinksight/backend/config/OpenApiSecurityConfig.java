package com.flinksight.backend.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

/**
 * 全局声明：所有 OpenAPI 接口默认使用 Bearer(JWT) 鉴权
 */
@OpenAPIDefinition(
    security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
    name = "BearerAuth",
    type = HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@Configuration
public class OpenApiSecurityConfig {}