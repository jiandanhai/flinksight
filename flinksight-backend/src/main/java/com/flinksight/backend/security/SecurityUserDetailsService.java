package com.flinksight.backend.security;

import com.flinksight.backend.domain.User;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private PermissionRepository permRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里建议username格式：tenantId:username 以支持多租户
        String[] parts = username.split(":");
        if (parts.length != 2) throw new UsernameNotFoundException("Invalid format, should be tenantId:username");

        Long tenantId = Long.parseLong(parts[0]);
        String uname = parts[1];

        User user = userRepository.findByUsernameAndTenantId(uname, tenantId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        final Set<String> authorities = new HashSet<>(permRepo.findCodesByUser(user.getId(), tenantId));
        return new SecurityUser(user, new ArrayList<>(authorities));
    }
}
