import React, { useState } from 'react';
import RoleList from './RoleList';
import RoleDetail from './RoleDetail';

/**
 * 角色管理主页面
 * - 包含角色列表与详情页切换
 */
const RolePage: React.FC = () => {
  const [selectedId, setSelectedId] = useState<number | null>(null);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">角色管理</h2>
      {!selectedId ? (
        <RoleList onSelect={id => setSelectedId(id)} />
      ) : (
        <RoleDetail id={selectedId} onBack={() => setSelectedId(null)} />
      )}
    </div>
  );
};
export default RolePage;
