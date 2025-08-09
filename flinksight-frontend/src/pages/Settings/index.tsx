/**
 * @file 设置中心主页
 * @desc 支持配置列表、通知渠道、租户参数等聚合Tab切换
 */
import React, {useState} from 'react';
import ConfigList from './ConfigList';
import NotificationConfig from './NotificationConfig';
import TenantConfig from './TenantConfig';
import SysParam from './SysParam';

const SettingsPage: React.FC = () => {
  const [tab, setTab] = useState<'config' | 'notify' | 'tenant' | 'sys'>('config');

  return (
    <div className="p-8">
      <h2 className="font-bold text-xl mb-6">系统设置</h2>
      <div className="flex mb-6 border-b">
        <button className={`mr-6 pb-2 ${tab === 'config' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('config')}>配置项</button>
        <button className={`mr-6 pb-2 ${tab === 'notify' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('notify')}>通知渠道</button>
        <button className={`mr-6 pb-2 ${tab === 'tenant' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('tenant')}>租户参数</button>
        <button className={`pb-2 ${tab === 'sys' ? 'border-b-2 border-blue-600' : ''}`} onClick={() => setTab('sys')}>系统参数</button>
      </div>
      {tab === 'config' && <ConfigList />}
      {tab === 'notify' && <NotificationConfig />}
      {tab === 'tenant' && <TenantConfig />}
      {tab === 'sys' && <SysParam />}
    </div>
  );
};

export default SettingsPage;
