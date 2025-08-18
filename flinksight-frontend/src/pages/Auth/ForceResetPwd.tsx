import React, {useState} from 'react';
import api from '@/api/api-compat';

import {useNavigate} from 'react-router-dom';

/**
 * 强制修改密码页面（首次/安全要求）
 */
const ForceResetPwdPage: React.FC = () => {
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setMsg('');
    try {
      await api.forceResetPassword({ password });
      setMsg('密码修改成功，正在跳转...');
      setTimeout(() => navigate('/', { replace: true }), 1200);
    } catch (e: any) {
      setMsg(e?.message || '修改失败');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[340px]" onSubmit={handleSubmit}>
        <h2 className="font-bold text-xl mb-6 text-center">强制修改密码</h2>
        <input className="input mb-4" placeholder="新密码" type="password" required value={password} onChange={e => setPassword(e.target.value)} />
        <button className="btn-primary w-full" type="submit" disabled={loading}>提交</button>
        {msg && <div className="mt-4 text-center text-blue-600">{msg}</div>}
      </form>
    </div>
  );
};
export default ForceResetPwdPage;
