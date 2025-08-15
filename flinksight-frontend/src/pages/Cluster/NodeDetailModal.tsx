/**
 * @file 节点明细弹窗
 * @desc 展示节点基础信息、健康历史、指标趋势等
 */
import React, {useEffect, useState} from "react";
import {Descriptions, Modal, Spin, Tag} from "antd";
import  api  from 'src/api/gen/client';

import type {NodeDTO, NodeMetricDTO} from '../../api/gen/data-contracts.ts';
import * as echarts from "echarts";

interface Props {
  id: number;
  open: boolean;
  onClose: () => void;
}

const NodeDetailModal: React.FC<Props> = ({ id, open, onClose }) => {
  const [data, setData] = useState<NodeDTO | null>(null);
  const [metrics, setMetrics] = useState<NodeMetricDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!open) return;
    setLoading(true);
    Promise.all([api.getNode(id), api.getNodeMetric(id)]).then(([res, mres]) => {
      setData(res.data);
      setMetrics(mres.data || []);
    }).finally(() => setLoading(false));
  }, [id, open]);

  useEffect(() => {
    if (!open || !metrics.length) return;
    const chart = echarts.init(document.getElementById('node-metric-chart') as HTMLDivElement);
    chart.setOption({
      title: { text: 'CPU/内存指标趋势', left: 'center' },
      tooltip: { trigger: 'axis' },
      legend: { data: ['CPU', '内存'] },
      xAxis: { type: 'category', data: metrics.map(m => m.time) },
      yAxis: { type: 'value' },
      series: [
        { name: 'CPU', type: 'line', data: metrics.map(m => m.cpu) },
        { name: '内存', type: 'line', data: metrics.map(m => m.mem) }
      ]
    });
    return () => { chart && echarts.dispose(chart); };
  }, [metrics, open]);

  if (loading || !data) return <Spin tip="加载中..." />;

  return (
    <Modal
      open={open}
      title={`节点明细：${data.name}`}
      onCancel={onClose}
      onOk={onClose}
      width={760}
      footer={null}
      destroyOnClose
      maskClosable
    >
      <Descriptions bordered column={2}>
        <Descriptions.Item label="节点名">{data.name}</Descriptions.Item>
        <Descriptions.Item label="IP">{data.ip}</Descriptions.Item>
        <Descriptions.Item label="集群">{data.clusterName}</Descriptions.Item>
        <Descriptions.Item label="角色">{data.role}</Descriptions.Item>
        <Descriptions.Item label="CPU">{data.cpuUsage}%</Descriptions.Item>
        <Descriptions.Item label="内存">{data.memUsage}%</Descriptions.Item>
        <Descriptions.Item label="健康">
          <Tag color={data.health === 'healthy' ? 'green' : data.health === 'warning' ? 'orange' : 'red'}>
            {data.health === 'healthy' ? '健康' : data.health === 'warning' ? '预警' : '异常'}
          </Tag>
        </Descriptions.Item>
        <Descriptions.Item label="状态">{data.enabled ? '启用' : '禁用'}</Descriptions.Item>
      </Descriptions>
      <div id="node-metric-chart" style={{ height: 280, marginTop: 32, background: '#fff' }} />
    </Modal>
  );
};

export default NodeDetailModal;
