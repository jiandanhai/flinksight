package com.flinksight.backend.config;

import lombok.Getter; import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 集中配置
 */
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "sso.oauth2")
public class SsoProperties {
    private String authorizeUrl;
    private String tokenUrl;
    private String endSessionUrl;
    private String userinfoUrl;   // ← 新增
    private String clientId;
    private String clientSecret;  // ← 新增
    private String callbackUrl;

    public boolean userinfoEnabled() {
        return userinfoUrl != null && !userinfoUrl.isBlank();
    }
}