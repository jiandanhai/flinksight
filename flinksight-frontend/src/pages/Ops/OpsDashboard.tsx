/**
 * @file SaaS 运营大屏
 * @desc 平台新建/成功趋势、漏斗、实时告警等
 */
import React, { useEffect, useRef } from "react";
import { Card, Col, Progress, Row, Statistic } from "antd";
import * as echarts from "echarts";
import { opsGetKpi, opsGetFunnel } from "@/api/modules";

type KpiResp = { date: string[]; active: number[]; paid: number[] };
type FunnelResp = { steps: string[]; values: number[] };

const OpsDashboard: React.FC = () => {
  const kpiRef = useRef<HTMLDivElement>(null);
  const funnelRef = useRef<HTMLDivElement>(null);
  const kpiChart = useRef<echarts.ECharts | null>(null);
  const funnelChart = useRef<echarts.ECharts | null>(null);

  useEffect(() => {
    // KPI 趋势（新建数/成功数）
    opsGetKpi().then(res => {
      const d: KpiResp = res?.data ?? { date: [], active: [], paid: [] };
      if (kpiRef.current) {
        if (!kpiChart.current) {
          kpiChart.current = echarts.init(kpiRef.current);
        }
        kpiChart.current.setOption({
          title: { text: "新建数 / 成功数（按日）", left: "center" },
          tooltip: { trigger: "axis" },
          legend: { data: ["新建数", "成功数"] },
          xAxis: { type: "category", data: Array.isArray(d.date) ? d.date : [] },
          yAxis: { type: "value" },
          series: [
            { name: "新建数", data: Array.isArray(d.active) ? d.active : [], type: "line", smooth: true },
            { name: "成功数", data: Array.isArray(d.paid) ? d.paid : [], type: "bar" }
          ]
        });
      }
    });

    // 转化漏斗
    opsGetFunnel().then(res => {
      const d: FunnelResp = res?.data ?? { steps: [], values: [] };
      if (funnelRef.current) {
        if (!funnelChart.current) {
          funnelChart.current = echarts.init(funnelRef.current);
        }
        funnelChart.current.setOption({
          title: { text: "任务转化漏斗", left: "center" },
          series: [{
            name: "Funnel",
            type: "funnel",
            left: "10%",
            width: "80%",
            data: (Array.isArray(d.steps) ? d.steps : []).map((step: string, i: number) => ({
              name: step,
              value: (Array.isArray(d.values) ? d.values : [])[i] ?? 0
            }))
          }]
        });
      }
    });

    // 自适应
    const onResize = () => {
      kpiChart.current?.resize();
      funnelChart.current?.resize();
    };
    window.addEventListener("resize", onResize);

    return () => {
      window.removeEventListener("resize", onResize);
      if (kpiChart.current) { kpiChart.current.dispose(); kpiChart.current = null; }
      if (funnelChart.current) { funnelChart.current.dispose(); funnelChart.current = null; }
    };
  }, []);

  return (
    <div>
      <Row gutter={24}>
        <Col span={6}><Card><Statistic title="累计任务" value={1024} /></Card></Col>
        <Col span={6}><Card><Statistic title="昨日新建" value={335} /></Card></Col>
        <Col span={6}><Card><Statistic title="昨日成功" value={56} /></Card></Col>
        <Col span={6}><Card><Statistic title="本月报警" value={91} /></Card></Col>
      </Row>

      <Row gutter={24} style={{ marginTop: 32 }}>
        <Col span={12}>
          <Card title="新建/成功趋势">
            <div ref={kpiRef} style={{ height: 300 }} />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="转化漏斗">
            <div ref={funnelRef} style={{ height: 300 }} />
          </Card>
        </Col>
      </Row>

      <Card title="SaaS 平台运维概览" style={{ marginTop: 32 }}>
        <Progress percent={67} status="active" />
        {/* 可扩展：目标、日历、活动等 */}
      </Card>
    </div>
  );
};

export default OpsDashboard;
