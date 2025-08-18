import React, {useState} from 'react';
import api from '@/api/api-compat';

import {useNavigate, useSearchParams} from 'react-router-dom';
import type {UserDTO} from '@/api/dto';

/**
 * 重置密码页面（通过邮箱token）
 */
const ResetPwdPage: React.FC = () => {
  const [params] = useSearchParams();
  const [form, setForm] = useState<DTO.UserDTO>({ token: params.get('token') || '', password: '' });
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setMsg('');
    try {
      await api.resetPassword(form);
      setMsg('密码重置成功，正在跳转登录...');
      setTimeout(() => navigate('/auth/login', { replace: true }), 1200);
    } catch (e: any) {
      setMsg(e?.message || '重置失败');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[340px]" onSubmit={handleSubmit}>
        <h2 className="font-bold text-xl mb-6 text-center">重置密码</h2>
        <input className="input mb-4" placeholder="新密码" type="password" required value={form.password} onChange={e => setForm(f => ({ ...f, password: e.target.value }))} />
        <button className="btn-primary w-full" type="submit" disabled={loading}>提交</button>
        {msg && <div className="mt-4 text-center text-blue-600">{msg}</div>}
        <div className="mt-4 text-center text-sm">
          <a className="text-blue-600" href="/auth/login">返回登录</a>
        </div>
      </form>
    </div>
  );
};
export default ResetPwdPage;
