/**
 * @file SaaS 运营大屏
 * @desc 平台活跃、付费、漏斗、实时告警等
 */
import React, { useEffect, useRef } from "react";
import { Card, Col, Progress, Row, Statistic } from "antd";
import * as echarts from "echarts";
import api from '@/api/api-compat';

const OpsDashboard: React.FC = () => {
  const kpiRef = useRef<HTMLDivElement>(null);
  const funnelRef = useRef<HTMLDivElement>(null);
  const kpiChart = useRef<echarts.ECharts | null>(null);
  const funnelChart = useRef<echarts.ECharts | null>(null);

  useEffect(() => {
    // KPI 趋势
    api.opsControllerGetKpi().then(res => {
      const d = res.data || { date: [], active: [], paid: [] };
      if (kpiRef.current) {
        if (!kpiChart.current) {
          kpiChart.current = echarts.init(kpiRef.current);
        }
        kpiChart.current.setOption({
          title: { text: "日活/付费趋势", left: "center" },
          tooltip: { trigger: "axis" },
          legend: { data: ["日活", "付费"] },
          xAxis: { type: "category", data: d.date },
          yAxis: { type: "value" },
          series: [
            { name: "日活", data: d.active, type: "line", smooth: true },
            { name: "付费", data: d.paid, type: "bar" }
          ]
        });
      }
    });
    // 漏斗
    api.opsControllerGetFunnel().then(res => {
      const d = res.data || { steps: [], values: [] };
      if (funnelRef.current) {
        if (!funnelChart.current) {
          funnelChart.current = echarts.init(funnelRef.current);
        }
        funnelChart.current.setOption({
          title: { text: "平台漏斗", left: "center" },
          series: [{
            name: 'Funnel',
            type: 'funnel',
            left: '10%',
            width: '80%',
            data: d.steps.map((step: string, i: number) => ({
              name: step,
              value: d.values[i]
            }))
          }]
        });
      }
    });
    return () => {
      if (kpiChart.current) { kpiChart.current.dispose(); kpiChart.current = null; }
      if (funnelChart.current) { funnelChart.current.dispose(); funnelChart.current = null; }
    };
  }, []);

  return (
    <div>
      <Row gutter={24}>
        <Col span={6}><Card><Statistic title="注册用户" value={1024} /></Card></Col>
        <Col span={6}><Card><Statistic title="昨日活跃" value={335} /></Card></Col>
        <Col span={6}><Card><Statistic title="昨日付费" value={56} /></Card></Col>
        <Col span={6}><Card><Statistic title="本月报警" value={91} /></Card></Col>
      </Row>
      <Row gutter={24} style={{ marginTop: 32 }}>
        <Col span={12}>
          <Card title="KPI趋势">
            <div ref={kpiRef} style={{ height: 300 }} />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="转化漏斗">
            <div ref={funnelRef} style={{ height: 300 }} />
          </Card>
        </Col>
      </Row>
      <Card title="SaaS 平台运营情况" style={{ marginTop: 32 }}>
        <Progress percent={67} status="active" />
        {/* 还可扩展运营目标、日历、活动等 */}
      </Card>
    </div>
  );
};
export default OpsDashboard;
