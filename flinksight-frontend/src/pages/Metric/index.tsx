import React, { useState } from 'react';
import MetricList from './MetricList';
import MetricDetail from './MetricDetail';

/**
 * 指标管理主页面
 */
const MetricPage: React.FC = () => {
  const [selectedId, setSelectedId] = useState<number | null>(null);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">指标管理</h2>
      {!selectedId ? (
        <MetricList onSelect={id => setSelectedId(id)} />
      ) : (
        <MetricDetail id={selectedId} onBack={() => setSelectedId(null)} />
      )}
    </div>
  );
};
export default MetricPage;
