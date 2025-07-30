import React, { useEffect, useState } from 'react';
import { getDashboardSummary } from '../../api/dashboard';
import type { DashboardSummary } from '../../types/dashboard';
import Loading from '../../components/Loading';

/**
 * KPI统计组件
 * - 展示核心数据：集群数、任务数、报警数、活跃任务、健康度等
 */
const KPIStats: React.FC = () => {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [loading, setLoading] = useState(false);

  async function fetchSummary() {
    setLoading(true);
    try {
      const data = await getDashboardSummary();
      setSummary(data);
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { fetchSummary(); }, []);

  if (loading || !summary) return <Loading />;
  return (
    <div className="grid grid-cols-5 gap-6">
      <KPIItem label="集群数" value={summary.totalClusters} />
      <KPIItem label="任务数" value={summary.totalJobs} />
      <KPIItem label="报警数" value={summary.totalAlerts} />
      <KPIItem label="今日活跃" value={summary.activeJobs} />
      <KPIItem label="健康度" value={`${summary.healthyRate}%`} />
    </div>
  );
};

// 单个KPI项
function KPIItem({ label, value }: { label: string, value: any }) {
  return (
    <div className="bg-white rounded-xl shadow p-6 flex flex-col items-center">
      <span className="text-2xl font-bold text-blue-600">{value}</span>
      <span className="text-gray-500 mt-2">{label}</span>
    </div>
  );
}
export default KPIStats;
