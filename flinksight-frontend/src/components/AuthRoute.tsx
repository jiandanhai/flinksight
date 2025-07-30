// src/components/AuthRoute.tsx
import React from 'react';
import { Navigate } from 'react-router-dom';

/**
 * 登录鉴权路由包装
 * 检查localStorage的token，未登录跳转login
 */
const AuthRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" replace />;
};

export default AuthRoute;
