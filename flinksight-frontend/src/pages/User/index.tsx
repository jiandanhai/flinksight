import React, {useState} from 'react';
import UserList from './UserList';
import RoleList from './RoleList';
import Profile from './Profile';
// import { useSearchParams } from 'react-router-dom'; // 如需URL同步Tab

/**
 * 用户模块主页面
 * 聚合Tab切换：用户管理、角色管理、个人资料
 */
const UserPage: React.FC = () => {
  // 可选：用URL同步Tab
  // const [params, setParams] = useSearchParams();
  // const [tab, setTab] = useState<'user' | 'role' | 'profile'>(params.get('tab') as any || 'user');
  const [tab, setTab] = useState<'user' | 'role' | 'profile'>('user');

  return (
    <div className="p-6">
      <h2 className="font-bold text-lg mb-4">用户与权限管理</h2>
      <div className="flex mb-6 border-b">
        <button className={`mr-6 pb-2 ${tab === 'user' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('user')}>用户列表</button>
        <button className={`mr-6 pb-2 ${tab === 'role' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('role')}>角色管理</button>
        <button className={`pb-2 ${tab === 'profile' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('profile')}>个人资料</button>
      </div>
      {/* Tab页面聚合 */}
      {tab === 'user' && <UserList />}
      {tab === 'role' && <RoleList />}
      {tab === 'profile' && <Profile />}
    </div>
  );
};

export default UserPage;
