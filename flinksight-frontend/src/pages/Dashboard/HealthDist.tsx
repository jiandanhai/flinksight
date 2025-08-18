// src/pages/Dashboard/HealthDist.tsx
import React, { useEffect, useRef, useState } from "react";
import * as echarts from "echarts";
import { getTenantId } from "@/utils/tenant";
import { Card, Spin, Empty } from "antd";
import { dashboardStatisticsHealth } from "../../api/modules";

// 颜色配置
const COLOR_MAP = {
  健康: "#52c41a",
  预警: "#faad14",
  异常: "#f5222d",
};

const HealthDist: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.EChartsType>();
  const [loading, setLoading] = useState(false);
  const [hasData, setHasData] = useState(false);

  useEffect(() => {
    fetchHealthDist();
    return () => {
      chartInstance.current?.dispose();
      chartInstance.current = undefined;
    };
  }, []);

  const fetchHealthDist = async () => {
    setLoading(true);
    try {
      const res = await dashboardStatisticsHealth({ tenantId: getTenantId() });
      const d = res.data || { healthy: 0, warning: 0, critical: 0 };
      const pieData = [
        { value: d.healthy, name: "健康" },
        { value: d.warning, name: "预警" },
        { value: d.critical, name: "异常" },
      ];

      const total = pieData.reduce((sum, item) => sum + item.value, 0);
      setHasData(total > 0);

      if (!chartRef.current) return;
      if (!chartInstance.current) chartInstance.current = echarts.init(chartRef.current);

      chartInstance.current.setOption({
        title: {
          text: "业务健康分布",
          left: "center",
          top: 10,
          textStyle: { fontSize: 16 },
        },
        tooltip: { trigger: "item" },
        legend: {
          orient: "vertical",
          left: "left",
          data: ["健康", "预警", "异常"],
        },
        series: [
          {
            name: "健康分布",
            type: "pie",
            radius: "70%",
            data: pieData,
            label: {
              formatter: "{b}: {d}%",
            },
            color: [COLOR_MAP.健康, COLOR_MAP.预警, COLOR_MAP.异常],
          },
        ],
      });
    } catch (err) {
      console.error("获取业务健康分布失败", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="业务健康分布" bordered={false}>
      <Spin spinning={loading}>
        {!hasData ? (
          <Empty description="暂无健康分布数据" />
        ) : (
          <div
            ref={chartRef}
            style={{
              height: 300,
              width: "100%",
              background: "#fff",
              borderRadius: 12,
            }}
          />
        )}
      </Spin>
    </Card>
  );
};

export default HealthDist;
