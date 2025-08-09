import React, {useState} from 'react';
import AlertList from './AlertList';
import RuleList from './RuleList';

/**
 * 报警中心主入口
 * - Tab聚合报警流与规则管理
 */
const AlertPage: React.FC = () => {
  const [tab, setTab] = useState<'alert' | 'rule'>('alert');

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">报警中心</h2>
      <div className="flex mb-6 border-b">
        <button
          className={`mr-6 pb-2 px-2 ${tab === 'alert' ? 'border-b-2 border-blue-600 font-bold' : 'text-gray-600'}`}
          onClick={() => setTab('alert')}
        >报警流</button>
        <button
          className={`pb-2 px-2 ${tab === 'rule' ? 'border-b-2 border-blue-600 font-bold' : 'text-gray-600'}`}
          onClick={() => setTab('rule')}
        >报警规则</button>
      </div>
      {tab === 'alert' && <AlertList />}
      {tab === 'rule' && <RuleList />}
    </div>
  );
};
export default AlertPage;
