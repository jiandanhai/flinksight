/**
 * @file 运维与运营聚合页面
 * @desc Tab聚合 1) SaaS运营大屏 2) 运维自动化任务
 */
import React, {useState} from 'react';
import OpsDashboard from './OpsDashboard';
import OpsTaskPage from './OpsTaskPage';

const OpsIndexPage: React.FC = () => {
  // Tab：dashboard=运营大屏，task=运维自动化，未来可拓展
  const [tab, setTab] = useState<'dashboard' | 'task'>('dashboard');

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      {/* Tab切换 */}
      <div className="flex items-center mb-8">
        <h2 className="font-bold text-2xl mr-8">运维与运营中心</h2>
        <div className="flex space-x-4">
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'dashboard' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('dashboard')}
          >
            SaaS运营大屏
          </button>
          <button
            className={`px-4 py-1 rounded-t-md ${tab === 'task' ? 'bg-white shadow font-bold' : 'bg-gray-100 text-gray-500'}`}
            onClick={() => setTab('task')}
          >
            运维自动化
          </button>
        </div>
      </div>
      {/* Tab内容 */}
      {tab === 'dashboard' && <OpsDashboard />}
      {tab === 'task' && <OpsTaskPage />}
    </div>
  );
};
export default OpsIndexPage;
