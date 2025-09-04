import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

/**
 * 方案B：保留后端中转登录
 * - 使用环境变量 VITE_SSO_LOGIN_URL（例如 http://localhost:8080/sso/sso-login）
 * - 本页挂载即硬跳转：/sso/sso-login?redirect=<目标页>
 * - 不渲染任何本地用户名/密码或 SSO 按钮
 */
const LoginPage: React.FC = () => {
  const { search } = useLocation();

  useEffect(() => {
    const ssoLogin = import.meta.env.VITE_SSO_LOGIN_URL || '/sso/sso-login';
    const redirectAfter = new URLSearchParams(search).get('redirect') || '/dashboard';
    const sep = ssoLogin.includes('?') ? '&' : '?';
    // 硬跳转到后端中转，后端再 302 到 Keycloak，并带 state/redirect 回调本前端
    window.location.replace(`${ssoLogin}${sep}redirect=${encodeURIComponent(redirectAfter)}`);
  }, [search]);

  // 兜底提示（通常一闪而过）
  return (
    <div style={{
      minHeight: '100vh',
      display: 'grid',
      placeItems: 'center',
      background: '#0b1220',
      color: '#e6e8ec',
      padding: 24
    }}>
      <div style={{ opacity: .9 }}>Redirecting to SSO…</div>
    </div>
  );
};

export default LoginPage;
