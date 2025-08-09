import React from 'react';
import {useUser} from '../../../store/user';
import { api } from 'src/api/gen/client';

import {useNavigate} from 'react-router-dom';

//退出/强制登出
const Header: React.FC = () => {
  const { username, logout: localLogout } = useUser();
  const navigate = useNavigate();
  const handleLogout = async () => {
    await api.logout();
    localLogout();
    navigate('/login');
  };
  return (
    <div className="flex items-center justify-end gap-6 h-16 px-8 bg-white">
      <span className="text-gray-600">欢迎，{username}</span>
      <button className="text-blue-600" onClick={handleLogout}>退出</button>
    </div>
  );
};
export default Header;
