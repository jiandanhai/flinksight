package com.flinksight.backend.security;

import com.flinksight.backend.domain.User;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于把数据库用户 + 权限装配成 UserPrincipal（不再实现 UserDetailsService）
 */
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService {

    private final UserRepository userRepository;
    private final PermissionRepository permRepo;

    /**
     * 兼容你原来的“tenantId:username”复合用户名格式，产出 UserPrincipal
     */
    public UserPrincipal loadPrincipalByCompositeUsername(String composite) {
        String[] parts = composite.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("invalid format, should be tenantId:username");
        }
        Long tenantId = Long.parseLong(parts[0]);
        String uname = parts[1];

        User user = userRepository.findByUsernameAndTenantId(uname, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        Set<String> authorities = new HashSet<>(permRepo.findCodesByUser(user.getId(), tenantId));
        return new UserPrincipal(
                user.getId(),
                user.getSsoId(),
                tenantId,
                user.getUsername(),
                authorities
        );
    }

    /**
     * 常用装载方式：根据 userId + tenantId 产出 UserPrincipal
     */
    public UserPrincipal loadPrincipalById(Long userId, Long tenantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        // 如果有成员关系校验，可在此验证 user 是否属于 tenantId

        Set<String> authorities = new HashSet<>(permRepo.findCodesByUser(user.getId(), tenantId));
        return new UserPrincipal(
                user.getId(),
                user.getSsoId(),
                tenantId != null ? tenantId : user.getTenantId(),
                user.getUsername(),
                authorities
        );
    }
}