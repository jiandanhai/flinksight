import React, {useState} from 'react';
import  api  from 'src/api/gen/client';


/**
 * 找回密码页面
 * - 支持邮箱验证码找回
 */
const ForgotPwdPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setMsg('');
    try {
      await api.forgotPassword({ email });
      setMsg('已发送重置邮件，请查收。');
    } catch (e: any) {
      setMsg(e?.message || '发送失败');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[340px]" onSubmit={handleSubmit}>
        <h2 className="font-bold text-xl mb-6 text-center">找回密码</h2>
        <input className="input mb-4" placeholder="注册邮箱" required type="email" value={email} onChange={e => setEmail(e.target.value)} />
        <button className="btn-primary w-full" type="submit" disabled={loading}>发送重置邮件</button>
        {msg && <div className="mt-4 text-center text-blue-600">{msg}</div>}
        <div className="mt-4 text-center text-sm">
          <a className="text-blue-600" href="/auth/login">返回登录</a>
        </div>
      </form>
    </div>
  );
};
export default ForgotPwdPage;
