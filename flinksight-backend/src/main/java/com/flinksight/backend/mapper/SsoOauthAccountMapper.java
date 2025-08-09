package com.flinksight.backend.mapper;


import com.flinksight.backend.domain.OauthAccount;
import com.flinksight.common.dto.OauthAccountDTO;
import org.mapstruct.Mapper;

/**
 * 第三方授权账号 Mapper
 */
@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface SsoOauthAccountMapper  extends GenericMapper<OauthAccountDTO, OauthAccount> {}
