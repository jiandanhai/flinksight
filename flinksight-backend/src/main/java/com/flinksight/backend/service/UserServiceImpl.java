package com.flinksight.backend.service;

import com.flinksight.backend.domain.Profile;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.UserRoleStructMapper;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.backend.repository.RolePermissionRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.repository.UserRoleRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.dto.UserRoleDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.service.UserService;
import com.flinksight.common.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户业务实现
 * User Service Impl
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private final UserStructMapper mapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User entity = mapper.toEntity(userDTO);
        entity.setIsDeleted(0);
        User saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<UserDTO> getUserById(Long userId) {
        return repository.findById(userId).map(mapper::toDTO).filter(e -> e.getIsDeleted() != null && e.getIsDeleted() == 0);
    }

    @Override
    public List<UserDTO> getUsersByTenant(Long tenantId, int page, int size) {
        return mapper.toDTOList(repository.findAllByTenantIdAndIsDeleted(tenantId, 0).stream().skip((long)page*size).limit(size).toList());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        // 仅允许修改部分字段
        Optional<UserDTO> oldOpt = repository.findById(userDTO.getId()).map(mapper::toDTO).filter(e -> e.getIsDeleted() != null && e.getIsDeleted() == 0);
        User entity = mapper.toEntity(userDTO);
        if(oldOpt.isPresent()) {
            UserDTO ud = oldOpt.get();
            entity.setEmail(ud.getEmail());
            entity.setPhone(ud.getPhone());
            entity.setStatus(ud.getStatus());
            entity.setIsDeleted(0);
            // ...其它字段
            return mapper.toDTO(repository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
    }

    @Override
    public boolean checkPassword(Long userId, String rawPwd) {
        Optional<User> userOpt = repository.findById(userId);
        return userOpt.isPresent() && PasswordUtil.matches(rawPwd, userOpt.get().getPassword());
    }

    /**
     * 查询用户所有权限（如所有角色下的权限code合集）
     */
    @Override
    public List<String> getAuthorities(Long userId) {
        Set<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);
        Set<String> authorities = new HashSet<>();
        for (Long roleId : roleIds) {
            authorities.addAll(rolePermissionRepository.findPermissionCodesByRoleId(roleId));
        }
        return new ArrayList<>(authorities);
    }

    @Override
    public boolean softDelete(Long userId) {
        Optional<UserDTO> opt = repository.findById(userId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里可以加多租户ID逻辑
        User user = repository.findByUsernameAndIsDeleted(username, 0)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 构造UserDetails，填充权限等
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER") // 这里可以查出角色/权限
        );
    }
}
