// src/pages/Dashboard/AlertTrend.tsx
import React, { useEffect, useRef, useState } from "react";
import * as echarts from "echarts";
import { getTenantId } from "@/utils/tenant";
import dayjs from "dayjs";
import { Card, Spin, Empty } from "antd";
import { dashboardStatisticsAlertTrend } from "../../api/modules";

const AlertTrend: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.ECharts | null>(null);
  const [loading, setLoading] = useState(false);
  const [hasData, setHasData] = useState(false);

  useEffect(() => {
    fetchTrend();
    return () => {
      chartInstance.current?.dispose();
      chartInstance.current = null;
    };
  }, []);

  const fetchTrend = async () => {
    setLoading(true);
    try {
      const tenantId = getTenantId();

      // 最近 7 天时间范围
      const to = dayjs().endOf("day").toISOString();
      const from = dayjs().subtract(6, "day").startOf("day").toISOString();

      const res = await dashboardStatisticsAlertTrend({ tenantId, from, to });
      const d = res.data || { times: [], total: [], fatal: [], warn: [] };

      const total = (d.total || []).reduce((a, b) => a + b, 0);
      setHasData(total > 0);

      if (!chartRef.current) return;
      if (!chartInstance.current) chartInstance.current = echarts.init(chartRef.current);

      chartInstance.current.setOption({
        title: {
          text: "告警趋势（最近7天）",
          left: "center",
          textStyle: { fontSize: 16 },
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["总告警", "致命", "预警"],
          bottom: 0,
        },
        grid: {
          top: 50,
          left: "3%",
          right: "4%",
          bottom: 50,
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: d.times,
          boundaryGap: false,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "总告警",
            type: "line",
            data: d.total,
            smooth: true,
            lineStyle: { width: 2, color: "#1890ff" },
            areaStyle: { color: "#e6f7ff" },
          },
          {
            name: "致命",
            type: "bar",
            data: d.fatal,
            itemStyle: { color: "#f5222d" },
          },
          {
            name: "预警",
            type: "bar",
            data: d.warn,
            itemStyle: { color: "#faad14" },
          },
        ],
      });
    } catch (err) {
      console.error("获取告警趋势失败", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="告警趋势（最近7天）" bordered={false}>
      <Spin spinning={loading}>
        {!hasData ? (
          <Empty description="暂无告警数据" />
        ) : (
          <div
            ref={chartRef}
            style={{
              height: 320,
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

export default AlertTrend;
