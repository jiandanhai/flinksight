import React, { useState, useMemo } from 'react';
import { Tabs } from 'antd';
import type { TabsProps } from 'antd';
import AlertList from './AlertList';
import RuleList from './RuleList';

/**
 * 报警中心主入口（Tabs 版）
 * - 使用 antd Tabs：语义清晰、键盘可达、状态样式完善（优于手搓按钮）
 * - “报警规则”默认展示租户下所有规则（不依赖 alertId）
 */
const AlertPage: React.FC = () => {
  const [activeKey, setActiveKey] = useState<'alert' | 'rule'>('alert');

  const items = useMemo<TabsProps['items']>(
    () => [
      {
        key: 'alert',
        label: '报警流',
        children: <AlertList />,
      },
      {
        key: 'rule',
        label: '报警规则',
        // 不传 alertId：RuleList 将自动拉“租户规则列表”
        children: <RuleList />,
      },
    ],
    []
  );

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">报警中心</h2>

      <Tabs
        items={items}
        activeKey={activeKey}
        onChange={(k) => setActiveKey(k as 'alert' | 'rule')}
        size="large"
        tabBarGutter={24}
        // 下面两行是常见的视觉优化：更明显的选中态、更清晰的分割
        centered={false}
        animated
      />
    </div>
  );
};

export default AlertPage;
