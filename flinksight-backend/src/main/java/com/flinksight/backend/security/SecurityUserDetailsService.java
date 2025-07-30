package com.flinksight.backend.security;

import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.domain.RolePermission;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.domain.UserRole;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.backend.repository.RolePermissionRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private UserRoleRepository userRoleRepository;
    @Autowired private RolePermissionRepository rolePermissionRepository;
    @Autowired private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里建议username格式：tenantId:username 以支持多租户
        String[] parts = username.split(":");
        if (parts.length != 2) throw new UsernameNotFoundException("Invalid format, should be tenantId:username");

        Long tenantId = Long.parseLong(parts[0]);
        String uname = parts[1];

        User user = userRepository.findByUsernameAndTenantId(uname, tenantId)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        List<Long> roleIds = userRoleRepository.findByUserId(user.getId())
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());

        Set<String> permissionCodes = new HashSet<>();
        for (Long roleId : roleIds) {
            List<Long> permIds = rolePermissionRepository.findByRoleId(roleId)
                    .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
            for (Long permId : permIds) {
                permissionRepository.findById(permId)
                    .map(Permission::getCode).ifPresent(permissionCodes::add);
            }
        }
        return new SecurityUser(user, new ArrayList<>(permissionCodes));
    }
}
