package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.domain.UserTokenState;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.UserStructMapper;
import com.flinksight.backend.repository.*;
import com.flinksight.backend.security.SecurityUser;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.UserDTO;
import com.flinksight.common.dto.UserTokenStateDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserService;
import com.flinksight.common.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
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

    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserTokenStateRepository tokenStateRepo;
    private final PermissionRepository permRepo;
    private final UserStructMapper userStructMapper;

    @Override
    public UserDTO getCurrentUserProfile() {
        SecurityUser currentUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userStructMapper.toDTO(currentUser.getUser());
    }

    @Override
    public UserDTO findByAccount(String account) {
        User user = userRepository.findByTenantIdAndUsernameAndIsDeleted(SecurityUtil.getCurrentTenantId(), account, 0);
        return userStructMapper.toDTO(user);
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        // 密码加密/唯一性校验省略
        User user = userStructMapper.toEntity(userDTO);
        user.setStatus(1);
        user.setIsDeleted(0);
        user.setCreatedAt(LocalDateTime.now());
        return userStructMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void updateProfile(UserDTO userDTO) {
        userRepository.findById(userDTO.getId()).ifPresent(user -> {
            user.setNickname(userDTO.getNickname());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setAvatar(userDTO.getAvatar());
            userRepository.save(user);
        });
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User entity = userStructMapper.toEntity(userDTO);
        entity.setIsDeleted(0);
        User saved = userRepository.save(entity);
        return userStructMapper.toDTO(saved);
    }

    @Override
    public Optional<UserDTO> getUserById(Long userId) {
        return userRepository.findById(userId).map(userStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<UserDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, User.class); // 统一 1→0
        Page<User> result = userRepository.findAllByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, userStructMapper::toDTO, true); //
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        // 仅允许修改部分字段
        Optional<UserDTO> oldOpt = userRepository.findById(userDTO.getId()).map(userStructMapper::toDTO).filter(e -> e.getIsDeleted() != null && e.getIsDeleted() == 0);
        User entity = userStructMapper.toEntity(userDTO);
        if(oldOpt.isPresent()) {
            UserDTO ud = oldOpt.get();
            userStructMapper.mergeIgnoreNullAndBlank(ud,entity);
            return userStructMapper.toDTO(userRepository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
    }

    @Override
    public boolean checkPassword(Long userId, String rawPwd) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && PasswordUtil.matches(rawPwd, userOpt.get().getPassword());
    }

    /**
     * 查询用户所有权限（如所有角色下的权限code合集）
     */
    @Override
    public List<String> getAuthorities(Long userId,Long tenantId) {
        final Set<String> authorities = new HashSet<>(permRepo.findCodesByUser(userId,tenantId));
        // 也可以在这里顺便 union 用户直赋的 permission（如果有 user_permission 表）
        //authorities.addAll(userPermissionRepository.findCodesByUserIdAndTenantId(userId, tenantId));

        // 过滤启用状态（如果你的 repo 没过滤 enabled，可在这里再保险过滤一次）
        //authorities = authorities.stream().filter(code -> permRepository.isEnabled(code, tenantId)).collect(Collectors.toSet());

        return new ArrayList<>(authorities);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<UserDTO> opt = userRepository.findById(id).map(userStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserDTO dto = opt.get();
            dto.setIsDeleted(1);
            userRepository.save(userStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里可以加多租户ID逻辑
        User user = userRepository.findByTenantIdAndUsernameAndIsDeleted(SecurityUtil.getCurrentTenantId(),username, 0);
        // 构造UserDetails，填充权限等
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER") // 这里可以查出角色/权限
        );
    }


    /**
     * 令牌版本号 +1：退出、改密、强制下线等场景调用。
     * 并发安全：乐观锁 + 最多3次重试。
     */
    @Override
    @Transactional
    public void bumpTokenVersion(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");

        int attempts = 0;
        while (true) {
            try {
                UserTokenState state = tokenStateRepo.findById(userId)
                        .orElseGet(() -> UserTokenState.builder()
                                .userId(userId)
                                .tokenVersion(1)  // 首次初始化为 1
                                .build());

                state.setTokenVersion(state.getTokenVersion() + 1);
                tokenStateRepo.saveAndFlush(state); // flush 以尽早触发版本校验
                return;
            } catch (OptimisticLockingFailureException e) {
                if (++attempts >= 3) {
                    throw e; // 超过重试上限，抛出给上层（留审计）
                }
                // 短暂自旋，继续重试
            }
        }
    }

    /**
     * 获取当前用户的令牌版本号。
     * 若无记录则初始化为 1 并返回（幂等）。
     */
    @Override
    @Transactional
    public int getTokenVersion(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");

        return tokenStateRepo.findById(userId)
                .map(UserTokenState::getTokenVersion)
                .orElseGet(() -> {
                    // 首次访问自动初始化为 1
                    UserTokenState init = UserTokenState.builder()
                            .userId(userId)
                            .tokenVersion(1)
                            .build();
                    tokenStateRepo.save(init);
                    return 1;
                });
    }

    @Override
    @Transactional
    public UserTokenStateDTO getUserTokenState(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        return tokenStateRepo.findById(userId)
                .map(s -> UserTokenStateDTO.builder()
                        .userId(s.getUserId())
                        .tokenVersion(s.getTokenVersion())
                        .lastUpdated(s.getUpdatedAt()) // 来自实体 @UpdateTimestamp
                        .build())
                .orElse(UserTokenStateDTO.builder()
                        .userId(userId)
                        .tokenVersion(1)
                        .lastUpdated(Instant.EPOCH) // 首次尚未初始化
                        .build());
    }
}
