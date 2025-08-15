package com.flinksight.common.service;

public interface TokenVersionService {
    /** 撤销结果状态 */
    enum Result {
        SUCCESS,            // 已撤销
        ALREADY_REVOKED,    // 原本就处于撤销状态（幂等）
        NOT_FOUND,          // 令牌不可识别或无主体
        UNSUPPORTED,        // 实现不支持该操作
        ERROR               // 发生错误
    }

    /** 撤销请求（可携带审计信息） */
    class RevocationRequest {
        public final String token;        // access 或 refresh
        public final Long userId;         // 可为空，实现在内部解析
        public final String reason;       // 业务原因（安全、用户退出等）
        public final String operator;     // 谁发起的（username/system）
        public final String deviceId;     // 设备ID（可选）
        public final boolean refreshToken;// 是否刷新令牌

        public RevocationRequest(String token, Long userId, String reason,
                                 String operator, String deviceId, boolean refreshToken) {
            this.token = token; this.userId = userId; this.reason = reason;
            this.operator = operator; this.deviceId = deviceId; this.refreshToken = refreshToken;
        }
    }

    /** 通用撤销（支持 access/refresh） */
    Result revoke(RevocationRequest request);

    /** 快捷：撤销 access token */
    default Result revokeAccess(String accessToken, Long userId, String reason, String operator) {
        return revoke(new RevocationRequest(accessToken, userId, reason, operator, null, false));
    }

    /** 快捷：撤销 refresh token */
    default Result revokeRefresh(String refreshToken, Long userId, String reason, String operator) {
        return revoke(new RevocationRequest(refreshToken, userId, reason, operator, null, true));
    }

    /** 是否已撤销（黑名单实现使用） */
    boolean isRevoked(String token);

    /** 是否有效（版本号实现使用：tokenVersion 比对） */
    boolean isValid(String accessToken, Long userId);

    /** 可选：按 jti 精确撤销（若 Token 含 jti） */
    default Result revokeByJti(String jti, Long userId, String reason, String operator) {
        return Result.UNSUPPORTED;
    }
}