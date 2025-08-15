package com.flinksight.backend.service;

import com.flinksight.common.service.TokenVersionService;
import com.flinksight.common.service.UserService;
import com.flinksight.backend.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenVersionServiceImpl implements TokenVersionService {

    private final UserService userService; // 你已实现 bumpTokenVersion / getTokenVersion
    private final JwtUtil jwtUtil;

    @Override
    public Result revoke(RevocationRequest req) {
        // 按用户维度通杀所有旧 token
        Long uid = req.userId != null ? req.userId : jwtUtil.getUserIdFromToken(req.token);
        if (uid == null) return Result.NOT_FOUND;
        try {
            userService.bumpTokenVersion(uid);
            return Result.SUCCESS;
        } catch (Exception e) {
            return Result.ERROR;
        }
    }

    @Override
    public boolean isRevoked(String token) {
        // 版本号策略不维护黑名单
        return false;
    }

    @Override
    public boolean isValid(String accessToken, Long userId) {
        if (userId == null) return true;
        Integer tv = jwtUtil.getTokenVersionFromToken(accessToken);
        if (tv == null) return true; // 为兼容旧 token，不硬拦
        int current = userService.getTokenVersion(userId);
        return tv == current;
    }
}
