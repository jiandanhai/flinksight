package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Profile;
import com.flinksight.backend.domain.User;
import com.flinksight.backend.mapper.ProfileStructMapper;
import com.flinksight.backend.repository.ProfileRepository;
import com.flinksight.backend.repository.UserRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.ChangePasswordRequestDTO;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository repository;
    private final ProfileStructMapper profileStructMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ProfileDTO createOrUpdate(ProfileDTO dto) {
        Profile entity = null;

        // 先按 id 查；也可按 (tenantId, userId) 唯一键兜底
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null && dto.getTenantId() != null && dto.getUserId() != null) {
            entity = repository.findByTenantIdAndUserIdAndIsDeleted(dto.getTenantId(), dto.getUserId(),0);
        }

        if (entity == null) {
            // 新建
            entity = profileStructMapper.toEntity(dto);
            if (entity.getIsDeleted() == null) entity.setIsDeleted(0);
        } else {
            // 更新：只合并 dto 的非空字段；并忽略这些不可被覆盖的字段
            profileStructMapper.mergeIgnoreNullAndBlank(
                    dto, entity,
                    "id", "tenantId", "userId", "isDeleted", "createdAt", "updateTime"
            );
        }

        Profile saved = repository.save(entity);
        return profileStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ProfileDTO> getById(Long id) {
        return repository.findById(id).map(profileStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    @Transactional
    public ProfileDTO getMyProfile() {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Long userId = SecurityUtil.getCurrentUserId();
        return profileStructMapper.toDTO(repository.findByTenantIdAndUserIdAndIsDeleted(tenantId,userId, 0));
    }

    @Override
    public ProfileDTO getByUserId(Long userId) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        return profileStructMapper.toDTO(repository.findByTenantIdAndUserIdAndIsDeleted(tenantId,userId, 0));
    }


    @Override
    public PageResult<ProfileDTO> list(int page,int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Profile.class); // 统一 1→0
        Page<Profile> result = repository.findByTenantId(SecurityUtil.getCurrentTenantId(), pr);
        return PageHelpers.toPageResult(result, profileStructMapper::toDTO, true); // 返回
    }

    /** 修改当前用户密码 */
    @Transactional
    public void changePassword(ChangePasswordRequestDTO req) {
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("两次输入的新密码不一致");
        }
        // 不允许与原密码相同
        if (req.getOldPassword().equals(req.getNewPassword())) {
            throw new IllegalArgumentException("新密码不能与原密码相同");
        }
        // 强度校验（可按需放宽/加强）
        validatePasswordPolicy(req.getNewPassword());
        User user = userRepository.findByIdAndTenantIdAndIsDeleted(SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(),0);
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("原密码不正确");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /** 最低强度策略：长度≥8，且包含字母、数字、特殊字符三类中的至少两类（可按需调整） */
    private void validatePasswordPolicy(String pwd) {
        if (pwd.length() < 8 || pwd.length() > 64) {
            throw new IllegalArgumentException("密码长度须为 8-64 位");
        }
        Pattern letter = Pattern.compile("[A-Za-z]");
        Pattern digit = Pattern.compile("\\d");
        Pattern special = Pattern.compile("[^A-Za-z0-9]");

        int cls = 0;
        if (letter.matcher(pwd).find()) cls++;
        if (digit.matcher(pwd).find()) cls++;
        if (special.matcher(pwd).find()) cls++;

        if (cls < 2) {
            throw new IllegalArgumentException("密码需至少包含字母、数字、特殊字符三类中的两类");
        }
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<ProfileDTO> opt = repository.findById(id).map(profileStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ProfileDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(profileStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void softDeleteByUserId(Long userId) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Profile entity = repository.findByTenantIdAndUserIdAndIsDeleted(tenantId, userId,0);
        if (null != entity) {
            int n = repository.softDeleteByTenantAndUser(tenantId, userId);
        }
    }
}
