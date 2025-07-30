/**
 * @file 个人中心聚合页
 * @desc 资料编辑、修改密码、登录日志，多Tab可扩展
 */
import React, { useState } from 'react';
import ProfileInfo from './ProfileInfo';
import ChangePassword from './ChangePassword';
import LoginHistory from './LoginHistory';
import { Card } from 'antd';

const ProfilePage: React.FC = () => {
  const [tab, setTab] = useState<'info' | 'password' | 'log'>('info');

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="flex items-center mb-6">
        <h2 className="font-bold text-2xl mr-8">个人中心</h2>
        <div className="flex space-x-4">
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'info' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('info')}
          >资料信息</button>
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'password' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('password')}
          >修改密码</button>
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'log' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('log')}
          >登录日志</button>
        </div>
      </div>
      <Card>
        {tab === 'info' && <ProfileInfo />}
        {tab === 'password' && <ChangePassword />}
        {tab === 'log' && <LoginHistory />}
      </Card>
    </div>
  );
};
export default ProfilePage;
