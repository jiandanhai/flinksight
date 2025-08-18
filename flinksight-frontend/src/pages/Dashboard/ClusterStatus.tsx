import React, { useEffect, useRef, useState } from "react";
import * as echarts from "echarts";
import { getTenantId } from "@/utils/tenant";
import { Card, Col, Row, Statistic } from "antd";
import dayjs from "dayjs";
import { dashboardStatisticsClusterHealthMetrics,dashboardClusterTrend } from "../../api/modules";

// DTO 类型定义（如已生成可删除）
interface ClusterHealthMetricsDTO {
  totalClusters: number;
  totalActiveNodes: number;
  avgCpuUsage: number;
  avgMemoryUsage: number;
  statTime: string;
}

interface ClusterStatusTrendDTO {
  times: string[];
  cpuUsageList: number[];
  memoryUsageList: number[];
}

const ClusterStatus: React.FC = () => {
  const [metrics, setMetrics] = useState<ClusterHealthMetricsDTO | null>(null);
  const [trend, setTrend] = useState<ClusterStatusTrendDTO | null>(null);

  const pieChartRef = useRef<HTMLDivElement>(null);
  const lineChartRef = useRef<HTMLDivElement>(null);
  const pieInstance = useRef<echarts.EChartsType>();
  const lineInstance = useRef<echarts.EChartsType>();

  // 获取静态指标
  const fetchMetrics = async () => {
    const res = await dashboardStatisticsClusterHealthMetrics({ tenantId: getTenantId() });
    setMetrics(res.data);
    if (res.data && pieChartRef.current) {
      if (!pieInstance.current) {
        pieInstance.current = echarts.init(pieChartRef.current);
      }
      pieInstance.current.setOption({
        title: { text: "集群健康分布", left: "center", textStyle: { fontSize: 16 } },
        tooltip: { trigger: "item" },
        series: [
          {
            type: "pie",
            radius: "70%",
            label: { formatter: "{b}: {d}%" },
            data: [
              { value: Math.round(res.data.totalClusters * 0.6), name: "健康" }, // 示例计算
              { value: Math.round(res.data.totalClusters * 0.3), name: "预警" },
              { value: Math.round(res.data.totalClusters * 0.1), name: "异常" },
            ],
          },
        ],
      });
    }
  };

  // 获取24小时趋势
  const fetchTrend = async () => {
    const res = await dashboardClusterTrend({
      tenantId: getTenantId(),
      from: dayjs().subtract(3, "day").toISOString(),
      to: dayjs().endOf("day").toISOString(),
    });
    setTrend(res.data);
    if (res.data && lineChartRef.current) {
      if (!lineInstance.current) {
        lineInstance.current = echarts.init(lineChartRef.current);
      }
      lineInstance.current.setOption({
        title: { text: "集群状态趋势", left: "center", textStyle: { fontSize: 16 } },
        tooltip: { trigger: "axis" },
        legend: { data: ["CPU使用率", "内存使用率"] },
        xAxis: { type: "category", data: res.data.times },
        yAxis: { type: "value", axisLabel: { formatter: "{value}%" } },
        series: [
          { name: "CPU使用率", type: "line", smooth: true, data: res.data.cpuUsageList },
          { name: "内存使用率", type: "line", smooth: true, data: res.data.memoryUsageList },
        ],
      });
    }
  };

  useEffect(() => {
    fetchMetrics();
    fetchTrend();
    return () => {
      pieInstance.current?.dispose();
      lineInstance.current?.dispose();
      pieInstance.current = undefined;
      lineInstance.current = undefined;
    };
  }, []);

  return (
    <div>
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={6}>
          <Card><Statistic title="集群总数" value={metrics?.totalClusters ?? 0} /></Card>
        </Col>
        <Col span={6}>
          <Card><Statistic title="活跃节点数" value={metrics?.totalActiveNodes ?? 0} /></Card>
        </Col>
        <Col span={6}>
          <Card><Statistic title="平均CPU使用率" value={`${metrics?.avgCpuUsage?.toFixed(2) ?? 0}%`} /></Card>
        </Col>
        <Col span={6}>
          <Card><Statistic title="平均内存使用率" value={`${metrics?.avgMemoryUsage?.toFixed(2) ?? 0}%`} /></Card>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <div ref={pieChartRef} style={{ height: 300, background: "#fff", borderRadius: 12 }} />
        </Col>
        <Col span={12}>
          <div ref={lineChartRef} style={{ height: 300, background: "#fff", borderRadius: 12 }} />
        </Col>
      </Row>
    </div>
  );
};

export default ClusterStatus;
