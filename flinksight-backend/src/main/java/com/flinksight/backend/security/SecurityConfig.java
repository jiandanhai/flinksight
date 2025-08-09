package com.flinksight.backend.security;

import com.flinksight.backend.security.jwt.JwtAuthFilter;
import com.flinksight.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security 企业级安全配置
 * - 支持JWT、RBAC、多租户上下文、CORS、异常处理、注解权限
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 支持@PreAuthorize、@Secured
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserServiceImpl userServiceImpl;
    private final CorsConfigurationSource corsConfigurationSource;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Dao认证Provider，关联自定义UserDetailsService和密码加密
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceImpl); // 必须用实现了UserDetailsService的UserServiceImpl
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * API主入口安全配置（全部接口无Session，仅JWT，最小权限原则）
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        // ====== Swagger/OpenAPI/Knife4j文档全路径放行 ======
                        .requestMatchers(
                                "/swagger-ui.html",      // Swagger UI 主入口
                                "/swagger-ui/**",        // 新版 UI 静态资源
                                "/v3/api-docs/**",       // OpenAPI 文档接口
                                "/swagger-resources/**", // Swagger 静态资源
                                "/webjars/**",           // js/css/fonts等
                                "/doc.html",             // Knife4j 支持
                                // ====== 其它公共接口 ======
                                "/api/auth/**","/api/sso/**", "/actuator/**", "/health", "/public/**",
                                "/static/**", "/favicon.ico", "/assets/**"
                        ).permitAll()
                        // 其它接口需认证
                        .anyRequest().authenticated()
                )
                // 核心JWT过滤器，放在用户名密码认证过滤器之前
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, resp, ex1) -> {
                            resp.setContentType("application/json;charset=UTF-8");
                            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            resp.getWriter().write("{\"error\": \"未认证或Token失效\"}");
                        })
                        .accessDeniedHandler((req, resp, ex2) -> {
                            resp.setContentType("application/json;charset=UTF-8");
                            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            resp.getWriter().write("{\"error\": \"无权限访问该资源\"}");
                        })
                )
                .httpBasic(Customizer.withDefaults()) // 兼容Swagger文档Basic认证
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /*
     * 如需多租户/多前端场景，可配置额外SecurityFilterChain（可选）
     * 如后台管理独立端口、开放接口可做隔离
     */
    // @Bean
    // @Order(2)
    // public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
    //     // 自定义实现
    //     return http.build();
    // }
}
