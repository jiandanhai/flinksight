package com.flinksight.backend.security;

import com.flinksight.backend.config.CorsProperties;
import com.flinksight.backend.security.jwt.JwtAuthFilter;
import com.flinksight.backend.security.token.TokenGuardFilter;
import com.flinksight.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 支持@PreAuthorize、@Secured
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsProperties corsProperties;
    private final JwtAuthFilter jwtAuthFilter;
    private final TokenGuardFilter tokenGuardFilter;
    private final UserServiceImpl userServiceImpl;
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

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http,
                                              HandlerMappingIntrospector introspector,
                                              CorsConfigurationSource corsConfigurationSource) throws Exception {

        // MVC 感知的路径匹配器（对齐 Spring MVC 的 PathPattern/ServletPath 规则）
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

        // 关键：限定自定义过滤器的触发路径（保留原有 JWT/TokenGuard 逻辑）
        jwtAuthFilter.setRequestMatcher(mvc.pattern("/api/**"));

        http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        // ====== Swagger/OpenAPI/Knife4j文档全路径放行 ======
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/doc.html",
                                "/static/**", "/favicon.ico", "/assets/**",
                                "/actuator/**", "/health", "/public/**"
                        ).permitAll()

                        // SSO 桥接端点保留（不再挂 Keycloak 过滤器）
                        .requestMatchers("/sso/sso-login", "/sso/callback", "/sso/login", "/sso/register").permitAll()
                        // 若只做 IdP 退出跳转，可将下一行改为 permitAll()
                        .requestMatchers("/sso/logout").permitAll()

                        // 业务 API 保护（JWT）
                        .requestMatchers("/api/**").authenticated()

                        // 其他拒绝（按需调整）
                        .anyRequest().denyAll()
                )
                // 在 JwtAuthFilter 前挂 TokenGuardFilter
                .addFilterBefore(tokenGuardFilter, UsernamePasswordAuthenticationFilter.class)
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
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        config.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        config.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        config.setExposedHeaders(Arrays.asList(corsProperties.getExposedHeaders().split(",")));
        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        log.info("#[CORS SecurityConfig] config => {}", config);
        return source;
    }
}