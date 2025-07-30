// /src/types/auth.ts 认证、注册、找回密码、第三方登录相关全部类型

/**
 * 登录请求
 */
export interface LoginReq {
  username: string;
  password: string;
  rememberMe?: boolean;
}

/**
 * 登录返回
 */
export interface LoginResp {
  token: string;
  user: AuthUserInfo;
  expireAt: string;
}

/**
 * 认证用户信息（简化版）
 */
export interface AuthUserInfo {
  id: number;
  username: string;
  email?: string;
  phone?: string;
  roles: string[];
  tenantId?: number;
  avatar?: string;
  lastLoginTime?: string;
}

/**
 * 注册请求
 */
export interface RegisterReq {
  username: string;
  password: string;
  email?: string;
  phone?: string;
  code?: string; // 邮箱/短信验证码
}

/**
 * 找回密码请求
 */
export interface ResetPasswordReq {
  username: string;
  code: string;     // 验证码
  newPassword: string;
}

/**
 * 强制修改密码请求
 */
export interface ForceChangePasswordReq {
  oldPassword?: string; // 首次登录可不填
  newPassword: string;
}

/**
 * 第三方登录方式
 */
export type ThirdPartyProvider = 'dingding' | 'wechat' | 'ldap';

/**
 * 第三方登录请求
 */
export interface ThirdPartyLoginReq {
  provider: ThirdPartyProvider;
  code: string; // 第三方回调code
}

/**
 * 第三方登录返回
 */
export interface ThirdPartyLoginResp {
  token: string;
  user: AuthUserInfo;
  expireAt: string;
  needBind?: boolean; // 是否需要绑定账号
}
