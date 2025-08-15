// src/pages/dashboard/index.tsx
import React, { Suspense, useEffect, useState } from "react";
import KPIStats from './KPIStats';
import ClusterStatus from './ClusterStatus';
import JobFunnel from './JobFunnel';
import AlertTrend from './AlertTrend';
import HealthDist from './HealthDist';

// 使用 React.lazy 异步加载指标大屏
const MetricDashboard = React.lazy(() => import('./MetricDashboard'));

/**
 * Flinksight SaaS 仪表盘主页面
 * - 支持运营概览与指标大屏两大视图切换
 * - 确保布局完整、Tab 状态可记忆、页面加载流畅
 */
const DashboardPage: React.FC = () => {
  // 默认 Tab 视图
  const [tab, setTab] = useState<'overview' | 'metric'>('overview');

  useEffect(() => {
    document.title = (tab === 'overview' ? '运营总览' : '指标可视化大屏') + ' - Flinksight';
  }, [tab]);

  useEffect(() => {
    console.debug("[DashboardPage] 页面已加载，当前tab:", tab);
  }, [tab]);

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="flex items-center mb-6">
        <h1 className="font-bold text-2xl mr-8">
          {tab === 'overview' ? '运营总览' : '指标可视化大屏'}
        </h1>
        {/* Tab 标签切换按钮 */}
        <div className="flex space-x-4">
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'overview' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('overview')}
          >
            运营总览
          </button>
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'metric' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('metric')}
          >
            指标可视化大屏
          </button>
        </div>
      </div>

      {/* Tab 视图内容切换 */}
      {tab === 'overview' ? (
        <>
          <KPIStats />
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
            <ClusterStatus />
            <HealthDist />
            <JobFunnel />
          </div>
          <div className="mt-8">
            <AlertTrend />
          </div>
        </>
      ) : (
        <Suspense fallback={<div className="text-center py-32">指标大屏加载中...</div>}>
          <MetricDashboard />
        </Suspense>
      )}
    </div>
  );
};

export default DashboardPage;
