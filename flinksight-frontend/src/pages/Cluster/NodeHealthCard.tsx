/**
 * @file 节点健康分布卡片
 * @desc 展示健康/预警/异常节点统计，支持 props 注入 stats 数据，自动刷新
 */
import React, { useEffect, useRef } from "react";
import * as echarts from "echarts";

/**
 * props:
 * stats: { healthy: number; warning: number; error: number }
 * -- 推荐从父组件传入（如集群详情拉取健康分布统计后传递）
 */
const NodeHealthCard: React.FC<{ stats: { healthy: number; warning: number; error: number } }> = ({ stats }) => {
  const chartRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!chartRef.current || !stats) return;
    const chart = echarts.init(chartRef.current);
    chart.setOption({
      title: { text: '节点健康分布', left: 'center', top: 12, textStyle: { fontSize: 16 } },
      tooltip: { trigger: 'item' },
      legend: { bottom: 0, data: ['健康', '预警', '异常'] },
      series: [{
        type: 'pie',
        radius: ['65%', '90%'],
        data: [
          { value: stats.healthy, name: '健康', itemStyle: { color: '#3CB371' } },
          { value: stats.warning, name: '预警', itemStyle: { color: '#FFD700' } },
          { value: stats.error, name: '异常', itemStyle: { color: '#FF6347' } }
        ],
        label: { formatter: '{b} {d}%' }
      }]
    });
    return () => { chart && echarts.dispose(chart); };
  }, [stats]);

  return <div ref={chartRef} style={{ height: 200, width: '100%', background: '#fff', borderRadius: 12 }} />;
};

export default NodeHealthCard;
