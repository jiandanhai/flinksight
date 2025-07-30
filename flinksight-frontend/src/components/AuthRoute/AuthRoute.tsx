import React from 'react';
import { Outlet, Navigate } from 'react-router-dom';
import { useUser } from '../../store/user';


const SSO_LOGIN_URL = process.env.REACT_APP_SSO_LOGIN_URL || '/api/auth/sso-login'; // 环境变量

const AuthRoute: React.FC = () => {
  const { id } = useUser();
  if (id) return <Outlet />;
  // 未登录自动跳转到 SSO 登录  内判断未登录时自动跳转到后端SSO认证地址。
  //SSO_LOGIN_URL建议由.env注入，便于多环境切换。
  //SSO后端回跳需携带token，前端在/login-callback或/auth-callback路由处理登录（见下）。
  window.location.href = SSO_LOGIN_URL + `?redirect=${encodeURIComponent(window.location.href)}`;
  return null;
};
export default AuthRoute;
