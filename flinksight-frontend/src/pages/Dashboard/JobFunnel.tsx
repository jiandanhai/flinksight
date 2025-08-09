import React, {useEffect, useState} from 'react';
import { api } from 'src/api/gen/client';

import type {JobFunnelDTO} from '../../api/gen/data-contracts.ts';
import Loading from '../../components/Loading';

/**
 * 业务转化漏斗分析  业务漏斗分析图
 * - 展示各阶段业务流转统计
 */
const JobFunnel: React.FC = () => {
  const [funnel, setFunnel] = useState<JobFunnelDTO | null>(null);
  const [loading, setLoading] = useState(false);

  async function fetchFunnel() {
    setLoading(true);
    try {
      const data = await api.getJobFunnelsByTenant();
      setFunnel(data);
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { fetchFunnel(); }, []);

  if (loading || !funnel) return <Loading />;
  return (
    <div className="bg-white rounded-xl shadow p-6">
      <h3 className="font-bold text-lg mb-4">业务漏斗</h3>
      <div className="flex flex-col space-y-3">
        {funnel.stages.map((s, i) => (
          <div key={i} className="flex items-center">
            <div className="w-32">{s.name}</div>
            <div className="flex-1 mx-3 h-4 rounded-full bg-gray-100 relative">
              <div
                className="absolute top-0 left-0 h-4 rounded-full bg-blue-500"
                style={{ width: `${s.percent}%` }}
              />
            </div>
            <div className="w-12 text-right">{s.value}</div>
            <div className="w-12 text-gray-400 text-xs text-right">{s.percent}%</div>
          </div>
        ))}
      </div>
    </div>
  );
};
export default JobFunnel;
