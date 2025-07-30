/**
 * @file 业务健康分布
 * @desc 展示健康/异常/告警节点占比，可与首页聚合
 */
import React, { useEffect, useRef } from 'react';
import * as echarts from 'echarts';
import http from '@/api/http';

const HealthDist: React.FC = () => {
  const chartRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    http.get('/dashboard/health').then(res => {
      const d = res.data || { healthy: 0, warning: 0, error: 0 };
      if (chartRef.current) {
        const chart = echarts.init(chartRef.current);
        chart.setOption({
          title: { text: '业务健康分布', left: 'center', top: 10, textStyle: { fontSize: 16 } },
          tooltip: { trigger: 'item' },
          series: [{
            type: 'pie',
            radius: '70%',
            data: [
              { value: d.healthy, name: '健康' },
              { value: d.warning, name: '预警' },
              { value: d.error, name: '异常' }
            ],
            label: { formatter: '{b} {d}%' }
          }]
        });
      }
    });
    return () => { chartRef.current && echarts.dispose(chartRef.current); }
  }, []);

  return <div ref={chartRef} style={{ height: 260, width: '100%', background: '#fff', borderRadius: 12 }} />;
};

export default HealthDist;
