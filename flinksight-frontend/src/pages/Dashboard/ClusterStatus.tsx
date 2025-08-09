import React, {useEffect, useState} from 'react';
import { api } from 'src/api/gen/client';

import type {ClusterStatusHistoryDTO} from '../../api/gen/data-contracts.ts';
import Loading from '../../components/Loading';

/**
 * 集群状态统计  集群状态分布卡片
 * - 展示健康/警告/异常集群数量  
 */
const ClusterStatus: React.FC = () => {
  const [dist, setDist] = useState<ClusterStatusHistoryDTO | null>(null);
  const [loading, setLoading] = useState(false);

  async function fetchDist() {
    setLoading(true);
    try {
      const data = await api.getHealthDistribution();
      setDist(data);
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { fetchDist(); }, []);

  if (loading || !dist) return <Loading />;
  return (
    <div className="bg-white rounded-xl shadow p-6">
      <h3 className="font-bold text-lg mb-4">集群健康分布</h3>
      <div className="flex space-x-6">
        <StatusBlock label="健康" color="green" value={dist.healthy} />
        <StatusBlock label="警告" color="yellow" value={dist.warning} />
        <StatusBlock label="异常" color="red" value={dist.critical} />
      </div>
    </div>
  );
};
function StatusBlock({ label, color, value }: { label: string, color: string, value: number }) {
  return (
    <div className={`flex flex-col items-center`}>
      <span className={`text-xl font-bold text-${color}-600`}>{value}</span>
      <span className="text-gray-500">{label}</span>
    </div>
  );
}
export default ClusterStatus;
