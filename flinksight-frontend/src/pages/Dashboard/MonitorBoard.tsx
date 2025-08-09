/**
 * @file 实时大屏监控
 * @desc 展示核心指标、报警、各子系统状态等（支持大屏展示）
 */
import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Statistic } from 'antd';
import { Api } from '../../api/gen/api.ts';

// 建议直接用 openapi 生成的类型（如 MetricsDTO），没有就临时 interface
interface Metrics {
  alertCount: number;
  jobRunning: number;
  clusterHealthy: number;
  userCount: number;
}

const api = new Api();

const MonitorBoard: React.FC = () => {
  const [metrics, setMetrics] = useState<Metrics>({
    alertCount: 0,
    jobRunning: 0,
    clusterHealthy: 0,
    userCount: 0
  });

  useEffect(() => {
    // 实时拉取数据（如后端支持 WebSocket，可优化为推送）
    const fetchData = () => {
      api.dashboardControllerGetMetrics().then(res => setMetrics(res.data || metrics));
    };
    fetchData();
    const timer = setInterval(fetchData, 5000);
    return () => clearInterval(timer);
    // eslint-disable-next-line
  }, []);

  return (
    <Row gutter={24}>
      <Col span={6}><Card><Statistic title="今日报警数" value={metrics.alertCount} /></Card></Col>
      <Col span={6}><Card><Statistic title="运行任务数" value={metrics.jobRunning} /></Card></Col>
      <Col span={6}><Card><Statistic title="健康集群数" value={metrics.clusterHealthy} /></Card></Col>
      <Col span={6}><Card><Statistic title="活跃用户" value={metrics.userCount} /></Card></Col>
    </Row>
  );
};

export default MonitorBoard;
