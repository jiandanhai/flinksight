/**
 * @file 实时大屏监控
 * @desc 展示核心指标、报警、各子系统状态等（支持大屏展示）
 */
import React, { useEffect, useState } from 'react';
import { Card, Row, Col, Statistic } from 'antd';
import http from '@/api/http';

interface Metrics {
  alertCount: number;
  jobRunning: number;
  clusterHealthy: number;
  userCount: number;
}

const MonitorBoard: React.FC = () => {
  const [metrics, setMetrics] = useState<Metrics>({ alertCount: 0, jobRunning: 0, clusterHealthy: 0, userCount: 0 });

  useEffect(() => {
    // 实时拉取数据，可用WebSocket替换为推送
    http.get('/dashboard/metrics').then(res => setMetrics(res.data));
    const timer = setInterval(() => {
      http.get('/dashboard/metrics').then(res => setMetrics(res.data));
    }, 5000);
    return () => clearInterval(timer);
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
