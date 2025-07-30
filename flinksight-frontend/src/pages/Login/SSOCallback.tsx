/**
 * @file SSO回调处理页
 * @desc 解析token并写入用户上下文，全自动跳转首页
 */
import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { ssoCallback } from '../../api/auth';
import { useUser } from '../../store/user';

const SSOCallback: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useUser();
  const { search } = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(search);
    const token = params.get('token');
    if (token) {
      ssoCallback(token).then(res => {
        login(res.data.token, res.data.user);
        navigate('/', { replace: true });
      });
    } else {
      navigate('/login?err=missing_token');
    }
    // eslint-disable-next-line
  }, []);

  return <div>正在登录，请稍候...</div>;
};
export default SSOCallback;
