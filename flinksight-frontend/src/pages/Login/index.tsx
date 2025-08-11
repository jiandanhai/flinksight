import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useUser } from '../../store/user';
import { setApiToken } from '../../api/gen/client';  // 引入 setApiToken

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useUser();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const redirectAfter = new URLSearchParams(location.search).get('redirect') || '/dashboard';

  // 账号密码登录处理
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
      
      // 登录成功，存储 token
      await login(data.token);
      
      // 设置 token 到 Api 客户端
      setApiToken(data.token); // 确保 token 在所有后续请求中生效

      // 将 Token 存储在 sessionStorage 和 localStorage 中
      sessionStorage.setItem('authToken', data.token);
      localStorage.setItem('authToken', data.token);

      // 在成功登录后，重定向
      navigate(redirectAfter, { replace: true });
    } catch (e: any) {
      setError(e?.message || '登录失败');
    }
  };

  // SSO 登录入口（默认后端代理模式）
  const goSSO = () => {
    const loginUrl = (import.meta.env.VITE_SSO_LOGIN_URL as string | undefined) || '/api/sso/sso-login';
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
