/**
 * @file MainLayout.tsx
 * @desc 商业级主框架布局，自动集成多级权限侧边菜单、国际化、退出、顶部栏，适合B端SaaS
 */
import React from 'react';
import {Outlet} from 'react-router-dom';
import SideMenu from './SideMenu'; // 动态权限/国际化多级菜单
import {useUser} from '../store/user';

const MainLayout: React.FC = () => {
  const { username, logout } = useUser();

  return (
    <div className="flex min-h-screen bg-gray-100">
      {/* 侧边栏 */}
      <aside className="w-64 bg-white shadow-lg flex flex-col">
        <div className="h-16 flex items-center justify-center text-2xl font-bold border-b">
          <img src="/logo.png" alt="Logo" className="h-8 mr-2" />
          Flinksight
        </div>
        <nav className="flex-1 p-4 overflow-y-auto">
          <SideMenu />
        </nav>
        <div className="p-4 border-t text-sm text-gray-400">
          <span className="block mb-2">© 2025 Flinksight</span>
          <button onClick={logout} className="text-blue-600 hover:underline">退出</button>
        </div>
      </aside>
      {/* 右侧内容区 */}
      <main className="flex-1 flex flex-col min-h-screen">
        {/* 顶部栏 */}
        <header className="h-16 bg-white shadow flex items-center px-8">
          <div className="flex-1 font-bold text-lg">Flinksight SaaS 实时监控平台</div>
          <div>
            <span className="text-gray-600">欢迎，{username}</span>
          </div>
        </header>
        {/* 页面内容区 */}
        <div className="flex-1 p-8 bg-gray-50">
          <Outlet />
        </div>
      </main>
      {/**页脚版权 */}
      <footer className="text-center text-xs text-gray-400 py-2 border-t">© 2025 Flinksight. All rights reserved.</footer>
    </div>
  );
};

export default MainLayout;
