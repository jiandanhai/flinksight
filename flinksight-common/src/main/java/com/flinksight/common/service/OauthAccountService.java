package com.flinksight.common.service;


import com.flinksight.common.dto.OauthAccountDTO;
import com.flinksight.common.model.PageResult;


import java.util.Optional;

public interface OauthAccountService {
    OauthAccountDTO bindAccount(OauthAccountDTO dto);

    void unbindAccount(Long userId, String provider);

    PageResult<OauthAccountDTO> findByUserId(Long userId, int page, int size);

    Optional<OauthAccountDTO> findByProviderAndOpenid(String provider, String openid);
}
