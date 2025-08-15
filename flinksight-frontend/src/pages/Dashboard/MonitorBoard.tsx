import React, { useEffect, useState } from "react";
import { Card, Col, Row, Statistic, message } from "antd";
import api from "src/api/gen/client";
import { getTenantId } from "@/utils/tenant";

// 定义接口返回结构
interface MonitorMetricsDTO {
  alertCount: number;
  jobRunning: number;
  clusterHealthy: number;
  userCount: number;
}

const MonitorBoard: React.FC = () => {
  const [metrics, setMetrics] = useState<MonitorMetricsDTO>({
    alertCount: 0,
    jobRunning: 0,
    clusterHealthy: 0,
    userCount: 0,
  });

  const fetchMetrics = async () => {
    try {
      const res = await api.dashboardStatisticsMonitorMetrics({
        tenantId: getTenantId(),
      });
      setMetrics(res.data || metrics);
    } catch (err) {
      message.error("获取监控指标失败");
    }
  };

  useEffect(() => {
    fetchMetrics(); // 初始加载一次
    const timer = setInterval(fetchMetrics, 5000); // 每5秒自动刷新一次
    return () => clearInterval(timer); // 组件卸载时清理
  }, []);

  return (
    <Row gutter={24}>
      <Col span={6}>
        <Card>
          <Statistic title="告警总数" value={metrics.alertCount} />
        </Card>
      </Col>
      <Col span={6}>
        <Card>
          <Statistic title="运行中的作业" value={metrics.jobRunning} />
        </Card>
      </Col>
      <Col span={6}>
        <Card>
          <Statistic title="健康集群数" value={metrics.clusterHealthy} />
        </Card>
      </Col>
      <Col span={6}>
        <Card>
          <Statistic title="用户数" value={metrics.userCount} />
        </Card>
      </Col>
    </Row>
  );
};

export default MonitorBoard;
