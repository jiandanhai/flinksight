import React, { useEffect, useState } from "react";
import { Card, Col, Row, Statistic, Spin } from "antd";
import { AlertOutlined, CheckCircleOutlined, CloseCircleOutlined, ClockCircleOutlined } from "@ant-design/icons";
import { dashboardKpiStatisticsSummary } from "../../api/modules";
interface KPIData {
  todayAlerts: number;
  successJobs: number;
  failedJobs: number;
  avgLatency: number;
}

const KPIStats: React.FC = () => {
  const [data, setData] = useState<KPIData | null>(null);
  const [loading, setLoading] = useState(false);

  // 加载 KPI 数据
  const fetchKPI = async () => {
    try {
      setLoading(true);
      const res = await dashboardKpiStatisticsSummary(); // 对应后端接口 /api/dashboard/kpi
      if (res?.data) {
        setData(res.data);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKPI();
  }, []);

  return (
    <Spin spinning={loading}>
      <Row gutter={16}>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="今日告警数"
              value={data?.todayAlerts ?? 0}
              prefix={<AlertOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="成功作业数"
              value={data?.successJobs ?? 0}
              valueStyle={{ color: "#3f8600" }}
              prefix={<CheckCircleOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="失败作业数"
              value={data?.failedJobs ?? 0}
              valueStyle={{ color: "#cf1322" }}
              prefix={<CloseCircleOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="平均响应耗时 (ms)"
              value={data?.avgLatency ?? 0}
              precision={1}
              prefix={<ClockCircleOutlined />}
            />
          </Card>
        </Col>
      </Row>
    </Spin>
  );
};

export default KPIStats;
