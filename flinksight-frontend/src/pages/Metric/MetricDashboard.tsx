/**
 * @file 指标可视化大屏（增强版）
 * - 兼容多种返回结构：{times,values} / {xAxis,series} / {rows|list|items}
 * - 所有传给 ECharts 的 data/source 均保证为数组
 * - 初始化图表仅一次；变更 metric/range 时不重复 init
 */
import React, { useEffect, useRef, useState } from "react";
import { Button, Card, Col, DatePicker, Row, Select } from "antd";
import * as echarts from "echarts";
import { listMetricDashboards } from "@/api/modules";
import dayjs, { Dayjs } from "dayjs";

const { RangePicker } = DatePicker;

const metricsList = [
  { key: "cpu", name: "CPU 使用率" },
  { key: "memory", name: "内存使用率" },
  { key: "throughput", name: "吞吐量" },
  { key: "delay", name: "延迟(ms)" },
];

// —— 工具：确保是数组
const toArr = <T,>(v: any): T[] => (Array.isArray(v) ? (v as T[]) : []);

// —— 工具：剥掉常见包裹层
const unwrap = (o: any) => o?.data ?? o?.result ?? o?.payload ?? o;

// —— 从任意返回体构建 ECharts option（内部保证只给数组）
function buildOptionFromResponse(raw: any, title: string): echarts.EChartsOption {
  const data: any = unwrap(unwrap(raw)) || {};

  // 形态 1：标准 xAxis + series
  let x = toArr<string>(data.xAxis ?? data.x ?? data.categories);
  let series = toArr<any>(data.series).map((s) => ({
    ...s,
    data: toArr<any>(s?.data),
  })).filter((s) => Array.isArray(s.data));

  // 形态 2：times + values（你原来的结构）
  if (series.length === 0 && (data.values || data.value || data.times)) {
    const values = toArr<number>(data.values ?? data.value);
    const times = toArr<any>(data.times ?? data.timestamps ?? []);
    series = [{ type: "line", areaStyle: {}, data: values }];
    if (x.length === 0) {
      x = times.length === values.length ? times : values.map((_, i) => i);
    }
  }

  // 形态 3：行数据 rows/list/items -> dataset
  if (series.length === 0) {
    const rows = toArr<any>(data.rows ?? data.list ?? data.items ?? data.data);
    if (rows.length > 0) {
      const dims = Object.keys(rows[0] || {});
      if (dims.length >= 2) {
        const [xKey, ...yKeys] = dims;
        const source = rows.map((r) => dims.map((k) => r?.[k]));
        return {
          title: { text: title, left: "center" },
          tooltip: { trigger: "axis" },
          dataset: { source: toArr<any[]>(source) }, // 2D 数组
          xAxis: { type: "category" },
          yAxis: { type: "value" },
          legend: { show: true },
          series: yKeys.map(() => ({ type: "line", smooth: true, areaStyle: {} })),
        };
      }
    }
  }

  // 如果 series 有值但 x 为空，尝试从第一条 series.data 推出 x
  if (series.length > 0 && x.length === 0) {
    const d0 = toArr<any>(series[0]?.data);
    if (d0.length > 0) {
      if (Array.isArray(d0[0])) {
        // [[x,y], ...]
        x = d0.map((p: any[]) => p?.[0]);
        series = series.map((s) => ({
          ...s,
          data: toArr<any>(s.data).map((p: any[]) => p?.[1]),
        }));
      } else if (typeof d0[0] === "object") {
        // [{name,value}] 或 {x,y}
        x = d0.map((p: any) => p?.name ?? p?.x);
        series = series.map((s) => ({
          ...s,
          data: toArr<any>(s.data).map((p: any) => p?.value ?? p?.y),
        }));
      } else {
        // 纯 y 数组，用索引做 x
        x = d0.map((_: any, i: number) => i);
      }
    }
  }

  return {
    title: { text: title, left: "center" },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: toArr<any>(x) },
    yAxis: { type: "value" },
    legend: { show: true },
    series: toArr<any>(series),
  };
}

const MetricDashboard: React.FC = () => {
  const [metric, setMetric] = useState<string>("cpu");
  const [range, setRange] = useState<[Dayjs, Dayjs]>([
    dayjs().subtract(1, "d"),
    dayjs(),
  ]);

  const chartRef = useRef<HTMLDivElement>(null);
  const chartInstance = useRef<echarts.ECharts | null>(null);

  // 仅一次初始化 / 释放
  useEffect(() => {
    if (chartRef.current && !chartInstance.current) {
      chartInstance.current = echarts.init(chartRef.current);
      const resize = () => chartInstance.current?.resize();
      window.addEventListener("resize", resize);
      return () => {
        window.removeEventListener("resize", resize);
        chartInstance.current?.dispose();
        chartInstance.current = null;
      };
    }
  }, []);

  // 拉取数据并渲染
  const fetchData = async () => {
    const title = metricsList.find((m) => m.key === metric)?.name || metric;
    try {
      const res = await listMetricDashboards({
        from: range[0].toISOString(),
        to: range[1].toISOString(),
      });

      const option = buildOptionFromResponse(res, title);
      const chart = chartInstance.current;
      if (!chart) return;

      chart.clear();

      // 判空兜底：没有 series 且没有 dataset
      const hasSeries =
        Array.isArray((option as any)?.series) &&
        ((option as any).series as any[]).length > 0;
      const hasDataset = !!(option as any)?.dataset;

      if (!hasSeries && !hasDataset) {
        chart.setOption(
          {
            title: { text: title, subtext: "暂无数据", left: "center" },
            xAxis: { type: "category", data: [] },
            yAxis: { type: "value" },
            series: [],
          },
          { notMerge: true }
        );
        return;
      }

      chart.setOption(option, { notMerge: true });
    } catch (err) {
      console.error("[metric-dashboard] fetch error:", err);
      chartInstance.current?.clear();
      chartInstance.current?.setOption(
        {
          title: { text: title, subtext: "加载失败", left: "center" },
        },
        { notMerge: true }
      );
    }
  };

  // metric / range 变化时刷新
  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [metric, range]);

  return (
    <Card title="指标可视化大屏">
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col>
          <Select value={metric} onChange={setMetric} style={{ width: 180 }}>
            {metricsList.map((m) => (
              <Select.Option value={m.key} key={m.key}>
                {m.name}
              </Select.Option>
            ))}
          </Select>
        </Col>
        <Col>
          <RangePicker
            value={range}
            onChange={(v) => v && setRange(v as [Dayjs, Dayjs])}
            allowClear={false}
          />
        </Col>
        <Col>
          <Button type="primary" onClick={fetchData}>
            刷新
          </Button>
        </Col>
      </Row>
      <div ref={chartRef} style={{ height: 400, background: "#fff" }} />
    </Card>
  );
};

export default MetricDashboard;
