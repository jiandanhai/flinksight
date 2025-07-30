/**
 * @file 大屏报警趋势图表
 * @desc 用于监控大屏，展示报警数随时间变化（可拓展为所有统计图）
 */
import React, { useEffect, useState } from 'react';
import { Line } from '@ant-design/charts'; // 推荐用antv，也支持echarts
import http from '@/api/http';

const AlertTrendChart: React.FC = () => {
  const [data, setData] = useState([]);
  useEffect(() => {
    http.get('/dashboard/alert-trend').then(res => setData(res.data || []));
  }, []);
  return (
    <Line
      data={data}
      xField="date"
      yField="count"
      smooth
      area
      point={{ size: 4 }}
      color="#FA541C"
      height={260}
      title={{ visible: true, text: '报警趋势（日）' }}
      xAxis={{ type: 'time', title: { text: '日期' } }}
      yAxis={{ title: { text: '报警数' } }}
    />
  );
};

export default AlertTrendChart;
