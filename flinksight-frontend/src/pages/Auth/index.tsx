import React from 'react';
import { Navigate } from 'react-router-dom';

/**
 * 认证模块主入口
 * - 仅做路由转发到登录页
 */
const AuthIndex: React.FC = () => <Navigate to="/auth/login" replace />;
export default AuthIndex;
