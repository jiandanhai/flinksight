/**
 * 登录页：账号密码 + SSO 入口
 * - 默认走后端代理 /api/sso/sso-login（避免跨域、方便维护）
 * - 支持直连授权端点（VITE_SSO_LOGIN_URL 配成完整 URL 时）
 */
import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useUser } from '../../store/user';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useUser();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const redirectAfter = new URLSearchParams(location.search).get('redirect') || '/dashboard';

  // 账号密码
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      const resp = await fetch('/api/sso/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });
      const data = await resp.json();
      if (!resp.ok || !data?.token) throw new Error(data?.message || '用户名或密码错误');
      await login(data.token);
      navigate(redirectAfter, { replace: true });
    } catch (e: any) {
      setError(e?.message || '登录失败');
    }
  };

  // SSO 入口（默认后端代理模式）
  const goSSO = () => {
    const loginUrl = (import.meta.env.VITE_SSO_LOGIN_URL as string | undefined) || '/api/sso/sso-login';
    // 后端代理：/api/sso/sso-login?redirect=/dashboard
    window.location.href = `${loginUrl}?redirect=${encodeURIComponent(redirectAfter)}`;
  };

  return (
    <div className="login-page" style={{ maxWidth: 400, margin: '100px auto' }}>
      <h2>用户登录</h2>

      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="用户名"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
          style={{ display: 'block', width: '100%', margin: '10px 0' }}
        />
        <input
          type="password"
          placeholder="密码"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
          style={{ display: 'block', width: '100%', margin: '10px 0' }}
        />
        <button type="submit" style={{ width: '100%', padding: 10 }}>登录</button>
      </form>

      {error && <div style={{ color: 'red', marginTop: 10 }}>{error}</div>}

      <button style={{ width: '100%', marginTop: 20 }} onClick={goSSO}>
        SSO 登录
      </button>
    </div>
  );
};

export default LoginPage;