/**
 * @file 业务健康分布
 * @desc 展示健康/异常/告警节点占比，可与首页聚合
 */
import React, { useEffect, useRef } from 'react';
import * as echarts from 'echarts';
import { api } from 'src/api/gen/client';

const HealthDist: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.EChartsType>();

  useEffect(() => {
    // 推荐统一用后端定义的类型/字段
    api.dashboardControllerGetHealth().then(res => {
      const d = res.data || { healthyCount: 0, warningCount: 0, errorCount: 0 };
      if (chartRef.current) {
        if (!chartInstance.current) {
          chartInstance.current = echarts.init(chartRef.current);
        }
        chartInstance.current.setOption({
          title: {
            text: '业务健康分布',
            left: 'center',
            top: 10,
            textStyle: { fontSize: 16 }
          },
          tooltip: { trigger: 'item' },
          series: [{
            type: 'pie',
            radius: '70%',
            data: [
              { value: d.healthyCount, name: '健康' },
              { value: d.warningCount, name: '预警' },
              { value: d.errorCount, name: '异常' }
            ],
            label: { formatter: '{b} {d}%' }
          }]
        });
      }
    });
    return () => {
      if (chartInstance.current) {
        chartInstance.current.dispose();
        chartInstance.current = undefined;
      }
    };
  }, []);

  return (
    <div
      ref={chartRef}
      style={{
        height: 260,
        width: '100%',
        background: '#fff',
        borderRadius: 12
      }}
    />
  );
};

export default HealthDist;
