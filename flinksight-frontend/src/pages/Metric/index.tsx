import React, { useMemo } from 'react';
import { Tabs } from 'antd';
import { useSearchParams } from 'react-router-dom';

import MetricList from './MetricList';
import MetricDashboard from './MetricDashboard';

const TAB_KEYS = ['list', 'dashboard'] as const;
type TabKey = typeof TAB_KEYS[number];

/**
 * 指标页父级容器：
 * - 一级菜单 /metrics 进入本页
 * - 内部 Tabs：指标管理（list）/ 指标大屏（dashboard）
 * - 支持外链直达：/metrics?tab=dashboard
 */
const MetricPage: React.FC = () => {
  const [sp, setSp] = useSearchParams();

  const activeKey: TabKey = useMemo(() => {
    const t = (sp.get('tab') || 'list').toLowerCase();
    return (TAB_KEYS as readonly string[]).includes(t) ? (t as TabKey) : 'list';
  }, [sp]);

  const onChange = (key: string) => {
    const next = new URLSearchParams(sp);
    next.set('tab', key);
    setSp(next, { replace: true });
  };

  return (
    <div className="p-4">
      <Tabs
        activeKey={activeKey}
        onChange={onChange}
        items={[
          {
            key: 'list',
            label: '指标管理',
            children: (
              <div className="bg-white rounded-xl shadow p-4">
                <MetricList
                  onSelect={(id) => {
                    const next = new URLSearchParams(sp);
                    next.set('tab', 'dashboard');
                    next.set('id', String(id));
                    setSp(next, { replace: true });
                  }}
                />
              </div>
            ),
          },
          {
            key: 'dashboard',
            label: '指标大屏',
            children: (
              <div className="bg-white rounded-xl shadow p-4">
                <MetricDashboard />
              </div>
            ),
          },
        ]}
      />
    </div>
  );
};

export default MetricPage;
