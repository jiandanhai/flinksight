import React, {useState} from 'react';
import { api } from 'src/api/gen/client';

import {useNavigate} from 'react-router-dom';
import type {LoginDTO} from '../../api/gen/data-contracts.ts';

/**
 * 登录页面
 * - 支持用户名密码登录
 * - 登录后自动跳转首页
 */
const LoginPage: React.FC = () => {
  const [form, setForm] = useState<LoginDTO>({ username: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [errMsg, setErrMsg] = useState('');
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setErrMsg('');
    try {
      await api.login(form);
      navigate('/', { replace: true });
    } catch (e: any) {
      setErrMsg(e?.message || '登录失败');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[340px]" onSubmit={handleSubmit}>
        <h2 className="font-bold text-xl mb-6 text-center">登录</h2>
        <input className="input mb-4" placeholder="用户名" required value={form.username} onChange={e => setForm(f => ({ ...f, username: e.target.value }))} />
        <input className="input mb-4" placeholder="密码" type="password" required value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
        {errMsg && <div className="text-red-500 mb-2">{errMsg}</div>}
        <button className="btn-primary w-full" type="submit" disabled={loading}>登录</button>
        <div className="mt-4 flex justify-between text-sm">
          <a className="text-blue-600" href="/auth/register">注册账号</a>
          <a className="text-blue-600" href="/auth/forgot">忘记密码</a>
        </div>
      </form>
    </div>
  );
};
export default LoginPage;
