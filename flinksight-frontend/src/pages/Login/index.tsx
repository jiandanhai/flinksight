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

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      const resp = await fetch('/sso/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        // 你的后端 DTO 用的是 account/password，这里对齐
        body: JSON.stringify({ account: username, password }),
      });
      const data = await resp.json();
      if (!resp.ok || !data?.data?.token) throw new Error(data?.message || '用户名或密码错误');

      const token = data.data.token;
      login(token);
      navigate('/dashboard', { replace: true });
    } catch (e: any) {
      setError(e?.message || '登录失败');
    }
  };

  const goSSO = () => {
    const loginUrl = import.meta.env.VITE_SSO_LOGIN_URL || '/sso/sso-login';
    // 注意：redirect 传完整 URL 或相对路径均可；后端会做 state 存证
    window.location.href = `${loginUrl}?redirect=${encodeURIComponent(redirectAfter)}`;
  };

  return (
    <div style={{ maxWidth: 420, margin: '12vh auto' }}>
      <h2>登录</h2>
      <form onSubmit={handleLogin}>
        <input value={username} onChange={e=>setUsername(e.target.value)} placeholder="用户名" />
        <input value={password} onChange={e=>setPassword(e.target.value)} placeholder="密码" type="password" />
        <button type="submit">本地登录</button>
      </form>
      {error && <div style={{ color: 'crimson', marginTop: 8 }}>{error}</div>}
      <hr/>
      <button onClick={goSSO} style={{ marginTop: 12 }}>SSO 登录</button>
    </div>
  );
};
export default LoginPage;
