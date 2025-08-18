// src/pages/Dashboard/JobFunnel.tsx
import React, { useEffect, useState } from "react";
import { Card, Spin, Empty, Pagination, Row } from "antd";
import { Bar } from "@ant-design/plots";
import { getTenantId } from "@/utils/tenant";
import { dashboardStatisticsJobFunnels } from "../../api/modules";
interface JobStatusDTO {
  status: string;
  count: number;
}

const colorMap: Record<string, string> = {
  "调度中": "#d46b08",
  "运行中": "#1890ff",
  "完成": "#52c41a",
  "失败": "#f5222d",
  "取消": "#8c8c8c",
};

const PAGE_SIZE = 10;

const JobFunnel: React.FC = () => {
  const [data, setData] = useState<JobStatusDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState<number>(1);
  const [total, setTotal] = useState<number>(0);

  const fetchData = async (page: number) => {
    setLoading(true);
    try {
      const res = await dashboardStatisticsJobFunnels({
        tenantId: getTenantId(),
        page,
        size: PAGE_SIZE,
      });
      setData(res?.data?.records || []);
      setTotal(res?.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(page);
  }, [page]);

  const config = {
    data,
    xField: "count",
    yField: "status",
    seriesField: "status",
    color: ({ status }: DTO.JobStatusDTO) => colorMap[status] || "#999",
    legend: false,
    label: {
      position: "right",
      style: {
        fill: "#595959",
        fontSize: 14,
      },
    },
    meta: {
      count: { alias: "数量" },
      status: { alias: "状态" },
    },
    height: 360,
  };

  return (
    <Card title="作业流程状态分布" bordered={false}>
      <Spin spinning={loading}>
        {data.length === 0 ? (
          <Empty description="暂无作业状态数据" />
        ) : (
          <>
            <Bar {...config} />
            <Row justify="end" className="mt-4">
              <Pagination
                current={page}
                pageSize={PAGE_SIZE}
                total={total}
                onChange={(p) => setPage(p)}
                showSizeChanger={false}
              />
            </Row>
          </>
        )}
      </Spin>
    </Card>
  );
};

export default JobFunnel;
