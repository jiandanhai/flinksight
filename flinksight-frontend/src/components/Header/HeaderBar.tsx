import React from 'react';
import { useAuth } from '../hooks/useAuth';
import logo from '../assets/logo.png';

const HeaderBar: React.FC = () => {
  const { user } = useAuth();
  return (
    <header className="flex items-center h-16 px-6 shadow bg-white border-b z-10">
      <img src={logo} alt="logo" className="h-10 w-10 mr-4 rounded-xl" />
      <span className="text-xl font-bold tracking-wide">Flinksight</span>
      <div className="flex-1" />
      <span className="text-gray-600 mr-6">{user?.username}</span>
    </header>
  );
};
export default HeaderBar;
