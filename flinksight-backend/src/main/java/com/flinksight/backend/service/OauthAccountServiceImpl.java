package com.flinksight.backend.service;


import com.flinksight.backend.domain.OauthAccount;
import com.flinksight.backend.mapper.SsoOauthAccountMapper;
import com.flinksight.backend.repository.SsoOauthAccountRepository;
import com.flinksight.common.dto.OauthAccountDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OauthAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthAccountServiceImpl implements OauthAccountService {

    private final SsoOauthAccountRepository repository;
    private final SsoOauthAccountMapper ssoOauthAccountMapper;

    @Override
    @Transactional
    public OauthAccountDTO bindAccount(OauthAccountDTO dto) {
        OauthAccount entity = ssoOauthAccountMapper.toEntity(dto);
        entity.setCreateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        return ssoOauthAccountMapper.toDTO(repository.save(entity));
    }

    /**
     * 解绑第三方账户，企业可做风控日志、合规通知等
     * @param userId 用户ID
     * @param provider 平台类型（如 wechat, dingtalk）
     */
    @Override
    @Transactional
    public void unbindAccount(Long userId, String provider) {
        int affected = repository.markUnbindByUserIdAndProvider(userId, provider);
        // 可选: 记录解绑操作日志、异步通知风控中心等
        if (affected == 0) {
            throw new IllegalStateException("解绑失败，未找到绑定记录或已解绑");
        }
    }

    @Override
    public PageResult<OauthAccountDTO> findByUserId(Long userId, int page, int size) {
        Page<OauthAccount> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<OauthAccountDTO> dtoPage = result.map(ssoOauthAccountMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<OauthAccountDTO> findByProviderAndOpenid(String provider, String openid) {
        return repository.findByProviderAndOpenidAndIsDeleted(provider, openid, 0)
                .map(ssoOauthAccountMapper::toDTO);
    }
}
