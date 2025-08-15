package com.flinksight.backend.security.jwt;

import com.flinksight.common.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long accessTtlMs;
    private final long clockSkewSeconds;
    private final String issuer;
    private final String audience;

    public JwtUtil(
            @Value("${security.jwt.secret:ChangeMeToYourSecretKey-ChangeMeToYourSecretKey}") String secret,
            @Value("${security.jwt.access-ttl-ms:86400000}") long accessTtlMs,            // 1天
            @Value("${security.jwt.clock-skew-seconds:60}") long clockSkewSeconds,        // 容忍时钟偏差
            @Value("${security.jwt.issuer:flinksight}") String issuer,
            @Value("${security.jwt.audience:api}") String audience
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlMs = accessTtlMs;
        this.clockSkewSeconds = clockSkewSeconds;
        this.issuer = issuer;
        this.audience = audience;
    }

    /* ===== 兼容你原有三个重载 ===== */
    public String generateToken(UserDTO user) {
        return generateAccessToken(
                user.getId(), user.getUsername(), user.getTenantId(), null,
                Map.of("roles", user.getRoles())
        );
    }
    public String generateToken(Long userId, String username, Long tenantId) {
        return generateAccessToken(userId, username, tenantId, null, null);
    }
    public String generateToken(Long userId, String username, Long tenantId, Set<String> roles) {
        return generateAccessToken(userId, username, tenantId, null,
                roles == null ? null : Map.of("roles", roles));
    }

    /* ===== 新的核心签发（写入 tokenVersion，可选） ===== */
    public String generateAccessToken(Long userId,
                                      String username,
                                      Long tenantId,
                                      Integer tokenVersion,
                                      Map<String, ?> extraClaims) {
        long now = System.currentTimeMillis();
        JwtBuilder b = Jwts.builder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTtlMs))
                .claim("userId", userId)
                .claim("tenantId", tenantId);
        if (tokenVersion != null) b.claim("tokenVersion", tokenVersion);
        if (extraClaims != null && !extraClaims.isEmpty()) extraClaims.forEach(b::claim);

        // 0.11.x 写法
        return b.signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    /* ===== 校验 & 解析（0.11.x） ===== */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .setAllowedClockSkewSeconds(clockSkewSeconds)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Long getUserIdFromToken(String token) {
        Number n = getClaim(token, c -> (Number) c.get("userId"));
        return n == null ? null : n.longValue();
    }

    public Long getTenantIdFromToken(String token) {
        Number n = getClaim(token, c -> (Number) c.get("tenantId"));
        return n == null ? null : n.longValue();
    }

    public Integer getTokenVersionFromToken(String token) {
        Number n = getClaim(token, c -> (Number) c.get("tokenVersion"));
        return n == null ? null : n.intValue();
    }

    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(clockSkewSeconds)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}
