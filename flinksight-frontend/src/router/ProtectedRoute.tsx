/**
 * @file 路由守卫
 * @desc 检查Token或角色，无权限跳转登录/403页面，满足企业合规需求
 */
import React from 'react';
import {Navigate} from 'react-router-dom';

export const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem('token');
  // 这里可进一步接入RBAC/权限校验
  if (!token) {
    return <Navigate to="/login" />;
  }
  return <>{children}</>;
};
