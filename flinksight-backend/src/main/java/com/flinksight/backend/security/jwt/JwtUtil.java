package com.flinksight.backend.security.jwt;

import com.flinksight.common.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {
    // 建议配置到 application.yaml，勿写死生产环境
    private static final String SECRET = "ChangeMeToYourSecretKey-ChangeMeToYourSecretKey";
    private static final long EXPIRE_MS = 86400000L; // 1天

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /** 登录/SSO签发 JWT：把 tenantId 一并写进去 */
    public String generateToken(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())           // List<String> 或 逗号串都OK
                .claim("tenantId", user.getTenantId())     // 👈 关键：写入租户ID
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Long userId, String username,Long tenantId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("tenantId", tenantId) // 👈 关键：在 token 里写入租户 ID
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MS))
                .signWith(key)
                .compact();
    }

    public String generateToken(Long userId, String username,Long tenantId, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("tenantId", tenantId) // 👈 关键：在 token 里写入租户 ID
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MS))
                .signWith(key)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Object val = getAllClaims(token).get("userId");
        if (val == null) return null;
        try {
            return (val instanceof Number) ? ((Number) val).longValue()
                    : Long.parseLong(val.toString().trim());
        } catch (NumberFormatException e) {
            return null; // 容错：不抛错，交给后续兜底通道
        }
    }

    public Long getTenantIdFromToken(String token) {
        Object val = getAllClaims(token).get("tenantId");
        if (val == null) return null;
        try {
            return (val instanceof Number) ? ((Number) val).longValue()
                    : Long.parseLong(val.toString().trim());
        } catch (NumberFormatException e) {
            return null; // 容错：不抛错，交给后续兜底通道
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
