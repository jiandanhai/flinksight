package com.flinksight.backend.security.jwt;

import com.flinksight.backend.security.UserPrincipal;
import com.flinksight.common.dto.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long accessTtlMs;          // 访问令牌有效期（毫秒）
    private final long clockSkewSeconds;     // 时钟偏差（秒）
    private final String issuer;
    private final String audience;

    public JwtUtil(
            @Value("${security.jwt.secret:ChangeMeToYourSecretKey-ChangeMeToYourSecretKey}") String secret,
            @Value("${security.jwt.access-ttl-ms:86400000}") long accessTtlMs,            // 1 天（毫秒）
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

    /** 推荐主入口：从 UserPrincipal 签发业务 JWT */
    public String generateToken(UserPrincipal p) {
        long nowMs = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",   p.userId());                 // ✅ 统一键名
        claims.put("tenantId", p.tenantId());
        claims.put("username", p.username());               // 便于快速读取
        claims.put("ssoId",    p.ssoId());                  // 额外提供 ssoId
        claims.put("roles",    new ArrayList<>(p.permissions()));

        return Jwts.builder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(p.username())                   // ✅ 与过滤器保持一致：subject=username
                .setIssuedAt(new Date(nowMs))
                .setExpiration(new Date(nowMs + accessTtlMs)) // ✅ 毫秒
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /* ===== 兼容你原有三个重载（保持签名不变） ===== */
    public String generateToken(UserDTO user) {
        return generateAccessToken(
                user.getId(), user.getUsername(), user.getTenantId(), null,
                Map.of("roles", user.getRoles(), "ssoId", user.getSsoId())
        );
    }

    public String generateToken(Long userId, String username, Long tenantId) {
        return generateAccessToken(userId, username, tenantId, null, null);
    }

    public String generateToken(Long userId, String username, Long tenantId, Set<String> roles) {
        Map<String, Object> extra = (roles == null) ? null : Map.of("roles", roles);
        return generateAccessToken(userId, username, tenantId, null, extra);
    }

    /* ===== 核心签发（毫秒 TTL；subject 统一为 username） ===== */
    public String generateAccessToken(Long userId,
                                      String username,
                                      Long tenantId,
                                      Integer tokenVersion,
                                      Map<String, ?> extraClaims) {
        long nowMs = System.currentTimeMillis();
        JwtBuilder b = Jwts.builder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(username)                         // ✅ subject=用户名
                .setIssuedAt(new Date(nowMs))
                .setExpiration(new Date(nowMs + accessTtlMs)) // ✅ 毫秒
                .claim("userId", userId)
                .claim("tenantId", tenantId)
                .claim("username", username);                 // 冗余一份，读取方便
        if (tokenVersion != null) b.claim("tokenVersion", tokenVersion);
        if (extraClaims != null && !extraClaims.isEmpty()) extraClaims.forEach(b::claim);
        return b.signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    /* ===== 校验 & 解析 ===== */
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
        return getClaim(token, Claims::getSubject); // subject=用户名
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

    /** 可选：需要的话也可以直接取 ssoId */
    public String getSsoIdFromToken(String token) {
        return getClaim(token, c -> (String) c.get("ssoId"));
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
