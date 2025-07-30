/**
 * @file 登录页   普通登录页，兼容SSO跳转入口）
 * @desc 账号密码登录、SSO入口跳转，自动对接用户状态
 */
import React from 'react';
import { Form, Input, Button, message } from 'antd';
import { login, ssoLogin } from '../../api/auth';
import { useUser } from '../../store/user';
import { useNavigate } from 'react-router-dom';

const LoginPage: React.FC = () => {
  const { login: doLogin } = useUser();
  const navigate = useNavigate();

  const onFinish = async (values: { username: string; password: string }) => {
    try {
      const res = await login(values);
      doLogin(res.data.token, res.data.user);
      navigate('/');
    } catch (e: any) {
      message.error(e.message || '登录失败');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="bg-white p-8 shadow-xl rounded-xl w-96">
        <h2 className="mb-8 font-bold text-2xl text-center">平台登录</h2>
        <Form onFinish={onFinish} layout="vertical" autoComplete="off">
          <Form.Item name="username" label="账号" rules={[{ required: true, message: '请输入账号' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="password" label="密码" rules={[{ required: true, message: '请输入密码' }]}>
            <Input.Password />
          </Form.Item>
          <Button type="primary" htmlType="submit" block className="mb-4">登录</Button>
        </Form>
        <Button type="link" block onClick={() => ssoLogin(window.location.origin + '/login/sso-callback')}>
          使用企业SSO登录
        </Button>
      </div>
    </div>
  );
};
export default LoginPage;
