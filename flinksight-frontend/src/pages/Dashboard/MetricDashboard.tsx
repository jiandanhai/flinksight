// src/pages/Dashboard/MetricDashboard.tsx
import React, { useEffect, useState } from "react";
import { Card, Col, DatePicker, Empty, Row, Spin, message } from "antd";
import { Line } from "@ant-design/plots";
import dayjs, { Dayjs } from "dayjs";
import { getTenantId } from "@/utils/tenant";
import { dashboardStatisticsMetricSeries } from "../../api/modules";

const { RangePicker } = DatePicker;

interface MetricSeriesDTO {
  times: string[];
  values: number[];
}

const METRICS = [
  { key: "cpu", label: "CPU 使用率 (%)" },
  { key: "memory", label: "内存使用率 (%)" },
  { key: "load", label: "系统负载" },
  { key: "jobCount", label: "任务数" },
];

const MetricDashboard: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [metrics, setMetrics] = useState<Record<string, MetricSeriesDTO>>({});
  const [dateRange, setDateRange] = useState<[Dayjs, Dayjs]>([
    dayjs().subtract(6, "day"),
    dayjs(),
  ]);

  const fetchData = async () => {
    setLoading(true);
    const [from, to] = dateRange;
    const tenantId = getTenantId();

    try {
      const results: Record<string, MetricSeriesDTO> = {};

      await Promise.all(
        METRICS.map(async ({ key }) => {
          const res = await dashboardStatisticsMetricSeries({
            tenantId,
            metric: key,
            from: from.toISOString(),
            to: to.toISOString(),
          });
          results[key] = res.data || { times: [], values: [] };
        })
      );

      setMetrics(results);
    } catch (err) {
      console.error(err);
      message.error("获取指标数据失败");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [dateRange]);

  const renderChart = (key: string, label: string) => {
    const series = metrics[key];
    if (!series) return null;

    const data = series.times.map((time, i) => ({
      time,
      value: series.values[i] ?? 0,
    }));

    return (
      <Card title={label} style={{ marginBottom: 24 }}>
        {data.length === 0 ? (
          <Empty description="暂无数据" />
        ) : (
          <Line
            data={data}
            xField="time"
            yField="value"
            smooth
            xAxis={{ label: { rotate: Math.PI / 4 } }}
            yAxis={{ label: { formatter: (v: any) => `${v}` } }}
            height={260}
          />
        )}
      </Card>
    );
  };

  return (
    <Spin spinning={loading}>
      <div className="p-4">
        <Row justify="end" className="mb-4">
          <RangePicker
            value={dateRange}
            onChange={(dates) => dates && setDateRange(dates)}
            allowClear={false}
          />
        </Row>
        <Row gutter={[24, 24]}>
          {METRICS.map(({ key, label }) => (
            <Col span={12} key={key}>
              {renderChart(key, label)}
            </Col>
          ))}
        </Row>
      </div>
    </Spin>
  );
};

export default MetricDashboard;
