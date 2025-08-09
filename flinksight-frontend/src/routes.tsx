import React from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import AuthRoute from './layouts/AuthRoute';
import NotFound from './components/NotFound';
import Forbidden from './components/Forbidden';

// 页面组件
import DashboardPage from './pages/Dashboard';
import ClusterPage from './pages/Cluster';
import JobPage from './pages/Job';
import AlertPage from './pages/Alert';
import UserPage from './pages/User';
import SettingsPage from './pages/Settings';

import LoginPage from './pages/Auth/Login';
import RegisterPage from './pages/Auth/Register';
import ForgotPwdPage from './pages/Auth/ForgotPwd';
import ResetPwdPage from './pages/Auth/ResetPwd';
import ForceResetPwdPage from './pages/Auth/ForceResetPwd';

const Router: React.FC = () => (
  <Routes>
    {/* 认证模块 */}
    <Route path="/auth/login" element={<LoginPage />} />
    <Route path="/auth/register" element={<RegisterPage />} />
    <Route path="/auth/forgot" element={<ForgotPwdPage />} />
    <Route path="/auth/reset" element={<ResetPwdPage />} />
    <Route path="/auth/force-reset" element={<ForceResetPwdPage />} />
    {/* 权限路由 */}
    <Route element={<AuthRoute />}>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard/*" element={<DashboardPage />} />
        <Route path="/cluster/*" element={<ClusterPage />} />
        <Route path="/job/*" element={<JobPage />} />
        <Route path="/alert/*" element={<AlertPage />} />
        <Route path="/user/*" element={<UserPage />} />
        <Route path="/settings/*" element={<SettingsPage />} />
        <Route path="/403" element={<Forbidden />} />
      </Route>
    </Route>
    <Route path="*" element={<NotFound />} />
  </Routes>
);

export default Router;
