import React, { useState } from 'react';
import ClusterList from './ClusterList';
import ClusterDetail from './ClusterDetail';

/**
 * 集群管理主入口页面
 * - 支持 Tab 或选中集群后查看详情
 * - 严格模块解耦，利于维护和扩展
 */
const ClusterPage: React.FC = () => {
  // 当前选中集群ID（null为未选中，仅展示列表）
  const [selectedId, setSelectedId] = useState<number | null>(null);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">集群管理</h2>
      {/* 列表和详情切换 */}
      {!selectedId ? (
        <ClusterList onSelect={id => setSelectedId(id)} />
      ) : (
        <ClusterDetail id={selectedId} onBack={() => setSelectedId(null)} />
      )}
    </div>
  );
};

export default ClusterPage;
