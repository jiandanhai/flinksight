/**
 * @file 指标可视化大屏
 * @desc 支持多指标图表、聚合、自定义周期
 */
import React, { useEffect, useRef, useState } from "react";
import { Card, Select, DatePicker, Button, Row, Col } from "antd";
import * as echarts from "echarts";
import http from "@/api/http";
import dayjs from "dayjs";

const { RangePicker } = DatePicker;

const metricsList = [
  { key: "cpu", name: "CPU 使用率" },
  { key: "memory", name: "内存使用率" },
  { key: "throughput", name: "吞吐量" },
  { key: "delay", name: "延迟(ms)" },
];

const MetricDashboard: React.FC = () => {
  const [metric, setMetric] = useState<string>("cpu");
  const [range, setRange] = useState<[any, any]>([dayjs().subtract(1, "d"), dayjs()]);
  const chartRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    // 拉取数据并渲染
    if (!chartRef.current) return;
    http.get("/metrics/data", {
      params: { metric, from: range[0].toISOString(), to: range[1].toISOString() }
    }).then(res => {
      const data = res.data || { times: [], values: [] };
      const chart = echarts.init(chartRef.current!);
      chart.setOption({
        title: { text: metricsList.find(m => m.key === metric)?.name, left: "center" },
        tooltip: { trigger: "axis" },
        xAxis: { type: "category", data: data.times },
        yAxis: { type: "value" },
        series: [{ data: data.values, type: "line", areaStyle: {} }]
      });
    });
    return () => { chartRef.current && echarts.dispose(chartRef.current); }
  }, [metric, range]);

  return (
    <Card title="指标可视化大屏">
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col>
          <Select value={metric} onChange={setMetric} style={{ width: 180 }}>
            {metricsList.map(m => <Select.Option value={m.key} key={m.key}>{m.name}</Select.Option>)}
          </Select>
        </Col>
        <Col>
          <RangePicker
            value={range}
            onChange={v => setRange(v as [any, any])}
            allowClear={false}
          />
        </Col>
        <Col>
          <Button type="primary" onClick={() => window.location.reload()}>刷新</Button>
        </Col>
      </Row>
      <div ref={chartRef} style={{ height: 400, background: "#fff" }} />
    </Card>
  );
};
export default MetricDashboard;
