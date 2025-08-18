package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.OauthAccountDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OauthAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方账号授权管理接口
 */
@Tag(name = "api",description = "第三方账号授权管理接口API")   // 统一 tag，所有接口都归到同一个模块
@RestController
@RequestMapping("/sso/oauth-account")
@RequiredArgsConstructor
@Validated
public class OauthAccountController {

    private final OauthAccountService oauthAccountService;


    @Operation(summary = "绑定第三方账号",operationId = "bindSsoOauthAccount")
    @PostMapping("/bind")
    public ApiResponse<OauthAccountDTO> bind(@RequestBody OauthAccountDTO dto) {
        return ApiResponse.ok(oauthAccountService.bindAccount(dto));
    }

    @Operation(summary = "解绑第三方账号",operationId = "unbindSsoOauthAccount")
    @PostMapping("/unbind")
    public ApiResponse<Void> unbind(@RequestParam Long userId, @RequestParam String provider) {
        oauthAccountService.unbindAccount(userId, provider);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "查询当前用户所有已绑定账号",operationId = "getSsoOauthAccountsByUser")
    @GetMapping("/list")
    public ApiResponse<PageResult<OauthAccountDTO>> list(@RequestParam Long userId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(oauthAccountService.findByUserId(userId,page,size));
    }

    @Operation(summary = "根据平台openId查账号",operationId = "getSsoOauthAccountByOpenId")
    @GetMapping("/by-openid")
    public ApiResponse<OauthAccountDTO> byOpenid(@RequestParam String provider, @RequestParam String openid) {
        return oauthAccountService.findByProviderAndOpenid(provider, openid)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(ErrorCode.NOT_FOUND,"未找到账号"));
    }
}
