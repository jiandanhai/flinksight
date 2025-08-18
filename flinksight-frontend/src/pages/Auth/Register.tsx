import React, {useState} from 'react';
import api from '@/api/api-compat';

import {useNavigate} from 'react-router-dom';
import type {UserDTO} from '@/api/dto';

/**
 * 注册页面
 * - 支持注册后自动跳转登录
 */
const RegisterPage: React.FC = () => {
  const [form, setForm] = useState<DTO.UserDTO>({ username: '', password: '', email: '' });
  const [loading, setLoading] = useState(false);
  const [errMsg, setErrMsg] = useState('');
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setErrMsg('');
    try {
      await api.createUser(form);
      setSuccess(true);
      setTimeout(() => navigate('/auth/login', { replace: true }), 1200);
    } catch (e: any) {
      setErrMsg(e?.message || '注册失败');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[340px]" onSubmit={handleSubmit}>
        <h2 className="font-bold text-xl mb-6 text-center">注册</h2>
        <input className="input mb-4" placeholder="用户名" required value={form.username} onChange={e => setForm(f => ({ ...f, username: e.target.value }))} />
        <input className="input mb-4" placeholder="密码" type="password" required value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
        <input className="input mb-4" placeholder="邮箱" required type="email" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
        {errMsg && <div className="text-red-500 mb-2">{errMsg}</div>}
        <button className="btn-primary w-full" type="submit" disabled={loading}>注册</button>
        {success && <div className="text-green-600 mt-3 text-center">注册成功，自动跳转...</div>}
        <div className="mt-4 text-center text-sm">
          已有账号？<a className="text-blue-600" href="/auth/login">立即登录</a>
        </div>
      </form>
    </div>
  );
};
export default RegisterPage;
