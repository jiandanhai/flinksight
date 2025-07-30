/**
 * @file 鉴权路由/SSO自动拦截
 * @desc 未登录时优先自动跳SSO，回调自动写入token/user，权限自动校验
 */
import React from 'react';
import { Outlet, Navigate, useLocation } from 'react-router-dom';
import { useUser } from '../../store/user';

const SSO_LOGIN_URL = process.env.REACT_APP_SSO_LOGIN_URL || '/api/auth/sso-login';

const AuthRoute: React.FC = () => {
  const { id, token } = useUser();
  const { pathname, search } = useLocation();

  if (id && token) return <Outlet />;
  // 如果URL带token参数（SSO回跳），自动处理
  const urlParams = new URLSearchParams(search);
  const ssoToken = urlParams.get('token');
  if (ssoToken) {
    // 跳转SSO回调页面
    return <Navigate to={`/login/sso-callback?token=${ssoToken}`} replace />;
  }
  // 未登录，跳SSO
  window.location.href = `${SSO_LOGIN_URL}?redirect=${encodeURIComponent(window.location.href)}`;
  return null;
};
export default AuthRoute;
