import React, {useState} from 'react';
import TenantList from './TenantList';
import TenantDetail from './TenantDetail';

/**
 * 租户管理主页面
 */
const TenantPage: React.FC = () => {
  const [selectedId, setSelectedId] = useState<number | null>(null);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">租户管理</h2>
      {!selectedId ? (
        <TenantList onSelect={id => setSelectedId(id)} />
      ) : (
        <TenantDetail id={selectedId} onBack={() => setSelectedId(null)} />
      )}
    </div>
  );
};
export default TenantPage;
