import React from 'react';
import {Outlet,useLocation} from 'react-router-dom';
import {useUser} from '../../store/user';


const SSO_LOGIN_URL = import.meta.env.VITE_SSO_LOGIN_URL || '/api/sso/sso-login';
const ALLOWLIST = new Set<string>(['/login', '/login/sso-callback']); // ✅ 回调白名单

const AuthRoute: React.FC = () => {
  const { token  } = useUser();
  const location   = useLocation();
  // 回调页&登录页放行
  if (ALLOWLIST.has(location.pathname)) return <Outlet />;

  // 已登录放行
  if (token) return <Outlet />;

  // 未登录：跳转后端 SSO，带当前完整地址
  const redirect = encodeURIComponent(window.location.href);
  window.location.href = `${SSO_LOGIN_URL}?redirect=${redirect}`;
  return null;
};
export default AuthRoute;
