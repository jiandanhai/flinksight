/**
 * @file 告警趋势
 * @desc 展示历史告警数量趋势，折线/柱状，自动数据刷新
 */
import React, { useEffect, useRef } from "react";
import * as echarts from "echarts";
import http from "@/api/http";

const AlertTrend: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    http.get('/dashboard/alert-trend').then(res => {
      const d = res.data || { times: [], total: [], fatal: [], warn: [] };
      if (chartRef.current) {
        const chart = echarts.init(chartRef.current);
        chart.setOption({
          title: { text: '告警趋势', left: 'center' },
          tooltip: { trigger: 'axis' },
          legend: { data: ['总告警', '致命', '预警'] },
          xAxis: { type: 'category', data: d.times },
          yAxis: { type: 'value' },
          series: [
            { name: '总告警', type: 'line', data: d.total, smooth: true },
            { name: '致命', type: 'bar', data: d.fatal },
            { name: '预警', type: 'bar', data: d.warn }
          ]
        });
      }
    });
    return () => { chartRef.current && echarts.dispose(chartRef.current); }
  }, []);

  return <div ref={chartRef} style={{ height: 300, width: '100%', background: '#fff', borderRadius: 12 }} />;
};

export default AlertTrend;
