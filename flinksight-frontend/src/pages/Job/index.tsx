import React, { useState } from 'react';
import JobList from './JobList';
import JobDetail from './JobDetail';

/**
 * 任务管理主入口页面
 * - 支持查看任务列表和单个任务详情
 * - 模块解耦，利于维护
 */
const JobPage: React.FC = () => {
  // 当前选中任务ID（null则展示列表）
  const [selectedId, setSelectedId] = useState<number | null>(null);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">任务管理</h2>
      {!selectedId ? (
        <JobList onSelect={id => setSelectedId(id)} />
      ) : (
        <JobDetail id={selectedId} onBack={() => setSelectedId(null)} />
      )}
    </div>
  );
};

export default JobPage;
