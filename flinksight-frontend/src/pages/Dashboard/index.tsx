import React, {Suspense, useEffect, useState} from "react"; 
import KPIStats from './KPIStats';
import ClusterStatus from './ClusterStatus';
import JobFunnel from './JobFunnel';
import AlertTrend from './AlertTrend';
import HealthDist from './HealthDist';

// 懒加载指标可视化大屏（仅切换到时加载，首屏更快）
const MetricDashboard = React.lazy(() => import('./MetricDashboard'));

/**
 * 运营总览与指标大屏聚合页面
 * - Tab切换运营总览/指标趋势大屏
 * - 保证所有主卡片原样保留，支持扩展其它Tab
 */
const DashboardPage: React.FC = () => {
  // Tab: overview = 运营总览，metric = 指标大屏，可继续扩展其它Tab
  const [tab, setTab] = useState<'overview' | 'metric'>('overview');

  useEffect(() => {
    document.title = (tab === 'overview' ? '运营总览' : '指标可视化大屏') + ' - Flinksight';
  }, [tab]);

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="flex items-center mb-6">
        <h1 className="font-bold text-2xl mr-8">
          {tab === 'overview' ? '运营总览' : '指标可视化大屏'}
        </h1>
        {/* Tab导航，可继续添加其它Tab */}
        <div className="flex space-x-4">
          <button
            className={px-4 py-1 rounded-t-md ${tab === 'overview' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}}
            onClick={() => setTab('overview')}
          >
            运营总览
          </button>
          <button
            className={px-4 py-1 rounded-t-md ${tab === 'metric' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}}
            onClick={() => setTab('metric')}
          >
            指标可视化大屏
          </button>
        </div>
      </div>
      {tab === 'overview' && (
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
      )}
      {tab === 'metric' && (
        <Suspense fallback={<div className="text-center py-32">指标大屏加载中...</div>}>
          <MetricDashboard />
        </Suspense>
      )}
    </div>
  );
};

export default DashboardPage;