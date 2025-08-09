/**
 * @file 指标大屏
 * @desc 支持多指标聚合可视化、区间选择、实时刷新
 */
import React, { useEffect, useRef, useState } from "react";
import { Button, Card, Col, DatePicker, Row, Select } from "antd";
import * as echarts from "echarts";
import { Api } from "../../api/gen/api.ts";
import dayjs, { Dayjs } from "dayjs";

const { RangePicker } = DatePicker;

const metricsList = [
  { key: "cpu", name: "CPU使用率" },
  { key: "memory", name: "内存使用率" },
  { key: "throughput", name: "吞吐量" },
  { key: "delay", name: "延迟(ms)" }
];

const api = new Api();

const MetricDashboard: React.FC = () => {
  const [metric, setMetric] = useState<string>("cpu");
  const [range, setRange] = useState<[Dayjs, Dayjs]>([dayjs().subtract(1, "d"), dayjs()]);
  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.ECharts | null>(null);

  const fetchData = () => {
    api.metricsControllerGetData({
      metric,
      from: range[0].toISOString(),
      to: range[1].toISOString()
    }).then(res => {
      const data = res.data || { times: [], values: [] };
      if (chartRef.current) {
        if (!chartInstance.current) {
          chartInstance.current = echarts.init(chartRef.current);
        }
        chartInstance.current.setOption({
          title: { text: metricsList.find(m => m.key === metric)?.name, left: "center" },
          tooltip: { trigger: "axis" },
          xAxis: { type: "category", data: data.times },
          yAxis: { type: "value" },
          series: [{ data: data.values, type: "line", areaStyle: {} }]
        });
      }
    });
  };

  useEffect(() => {
    fetchData();
    return () => {
      if (chartInstance.current) {
        chartInstance.current.dispose();
        chartInstance.current = null;
      }
    };
    // eslint-disable-next-line
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
            onChange={v => v && setRange(v as [Dayjs, Dayjs])}
            allowClear={false}
          />
        </Col>
        <Col>
          <Button type="primary" onClick={fetchData}>刷新</Button>
        </Col>
      </Row>
      <div ref={chartRef} style={{ height: 400, background: "#fff" }} />
    </Card>
  );
};

export default MetricDashboard;
