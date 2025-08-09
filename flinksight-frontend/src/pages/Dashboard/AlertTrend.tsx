import React, { useEffect, useRef } from "react";
import * as echarts from "echarts";
import { api } from 'src/api/gen/client';

const AlertTrend: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.ECharts | null>(null);

  useEffect(() => {
    // 获取数据
    api.dashboardControllerGetAlertTrend().then(res => {
      const d = res.data || { times: [], total: [], fatal: [], warn: [] };
      // 渲染图表
      if (chartRef.current) {
        if (!chartInstance.current) {
          chartInstance.current = echarts.init(chartRef.current);
        }
        chartInstance.current.setOption({
          title: { text: "告警趋势", left: "center" },
          tooltip: { trigger: "axis" },
          legend: { data: ["总告警", "致命", "预警"] },
          xAxis: { type: "category", data: d.times },
          yAxis: { type: "value" },
          series: [
            { name: "总告警", type: "line", data: d.total, smooth: true },
            { name: "致命", type: "bar", data: d.fatal },
            { name: "预警", type: "bar", data: d.warn }
          ]
        });
      }
    }).catch(e => {
      // 可以加全局提示/日志
      console.error("获取告警趋势失败", e);
    });

    // 卸载时销毁 ECharts 实例，防止内存泄漏
    return () => {
      if (chartInstance.current) {
        chartInstance.current.dispose();
        chartInstance.current = null;
      }
    };
  }, []);

  return (
    <div
      ref={chartRef}
      style={{
        height: 300,
        width: "100%",
        background: "#fff",
        borderRadius: 12
      }}
    />
  );
};

export default AlertTrend;
