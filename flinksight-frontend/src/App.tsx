import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import Router from './router';
import { AuthContext } from './hooks/useAuth'; // 真实场景用Provider实现

/**
 * 顶层组件
 * - 包裹路由、Provider
 * - 支持国际化、主题、权限等
 */
const App: React.FC = () => {
  // 临时写死isLogin，生产用持久化Token
  const isLogin = !!localStorage.getItem('token');
  const user = { username: 'admin' }; // 示例
  return (
    <AuthContext.Provider value={{ isLogin, user }}>
      <BrowserRouter>
        <Router />
      </BrowserRouter>
    </AuthContext.Provider>
  );
};
export default App;
