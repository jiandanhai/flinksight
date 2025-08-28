import React from 'react';
import { useNavigate } from 'react-router-dom';
import ClusterList from './ClusterList';
import ClusterDetail from './ClusterDetail';
import { useCluster } from '@/context/ClusterContext';

/**
 * 集群管理主入口页面
 * - 点击列表项：设置为“当前集群”并跳到 /job?clusterId=xxx
 * - 若你仍然需要“右侧详情”的交互，可以把 onSelect 改成 setSelectedId
 */
const ClusterPage: React.FC = () => {
  const nav = useNavigate();
  const { setCluster } = useCluster();

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">集群管理</h2>
      <ClusterList
        onSelect={(id, name) => {
          setCluster({ id, name });
          nav(`/job?clusterId=${id}`);
        }}
      />
      {/* 如需保留详情交互，可改回：
      const [selectedId, setSelectedId] = useState<number|null>(null);
      {!selectedId ? <ClusterList onSelect={...}/> : <ClusterDetail id={selectedId} onBack={()=>setSelectedId(null)} />}
      */}
    </div>
  );
};

export default ClusterPage;
