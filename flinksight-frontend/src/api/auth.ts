// /src/api/auth.ts
import http from './http';
import type {
  LoginReq,
  LoginResp,
  RegisterReq,
  ResetPasswordReq,
  ForceChangePasswordReq,
  ThirdPartyLoginReq,
  ThirdPartyLoginResp,
  AuthUserInfo,
} from '../types/auth';

/** 普通账号密码登录 */
export const login = (data: LoginReq) =>
  http.post<LoginResp>('/auth/login', data);

/** SSO单点登录(跳转后端SSO入口) */
export const ssoLogin = (redirect: string) =>
  window.location.href = `/api/auth/sso-login?redirect=${encodeURIComponent(redirect)}`;

/** SSO回调token登录（携带token拉取用户信息） */
export const ssoCallback = (token: string) =>
  http.post<LoginResp>('/auth/sso-callback', { token });

/** 退出/强制退出 */
export const logout = () =>
  http.post('/auth/logout');

/** 获取当前登录用户信息 */
export const getCurrentUser = () =>
  http.get<AuthUserInfo>('/auth/me');


/** 用户注册 */
export const register = (data: RegisterReq) =>
  http.post('/auth/register', data);

/** 找回密码/重置密码 */
export const resetPassword = (data: ResetPasswordReq) =>
  http.post('/auth/reset-password', data);

/** 首次/强制修改密码 */
export const forceChangePassword = (data: ForceChangePasswordReq) =>
  http.post('/auth/force-change-password', data);

/** 第三方登录（钉钉、微信、LDAP等） */
export const thirdPartyLogin = (data: ThirdPartyLoginReq) =>
  http.post<ThirdPartyLoginResp>('/auth/third-party-login', data);

/** 获取第三方登录跳转URL（页面前端可直接window.location） */
export const getThirdPartyLoginUrl = (provider: string, redirectUri: string) =>
  http.get<{ url: string }>(`/auth/third-party-url`, { params: { provider, redirectUri } });

/** 发送注册/找回密码验证码（邮箱/短信） */
export const sendAuthCode = (type: 'register' | 'reset', to: string) =>
  http.post('/auth/send-code', { type, to });

