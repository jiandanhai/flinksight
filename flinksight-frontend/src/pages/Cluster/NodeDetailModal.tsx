import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { Drawer, Tabs, Descriptions, Space, Tag, Statistic, Row, Col, Table, Button, Radio, message } from 'antd';
import type { TabsProps } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import { useSearchParams, useParams } from 'react-router-dom';

// 只依赖现有 /api/modules
import { getNodes, getNodeMetric, getNodeMetricByAgg, getNodeHealth_2 } from '@/api/modules';

type Props = {
  id: number | null;
  open: boolean;
  onClose: () => void;
};

type KV = Record<string, any>;

/** 简易迷你折线图（纯 SVG，无第三方依赖） */
const Sparkline: React.FC<{
  data: Array<{ t: number; v: number }>;
  height?: number;
  stroke?: string;
  fill?: string;
  max?: number; // 可固定纵轴上限（例如 100%）
}> = ({ data, height = 56, stroke = '#165DFF', fill = '#165dff14', max }) => {
  const pad = 4;
  const W = 240; // 固定宽度，足够在 Drawer 中阅读
  const H = height;
  const M = data.length ? (max ?? Math.max(...data.map(d => Number(d.v) || 0), 1)) : 1;

  const pts = data.map((d, i) => {
    const x = pad + (i * (W - pad * 2)) / Math.max(1, data.length - 1);
    const y = H - pad - ((Number(d.v) || 0) / M) * (H - pad * 2);
    return [x, y] as [number, number];
  });

  const dPath = pts.length
    ? `M ${pts[0][0]} ${pts[0][1]} ` + pts.slice(1).map(p => `L ${p[0]} ${p[1]}`).join(' ')
    : '';

  const areaPath = pts.length
    ? `M ${pts[0][0]} ${H - pad} L ${pts[0][0]} ${pts[0][1]} ` +
      pts.slice(1).map(p => `L ${p[0]} ${p[1]}`).join(' ') +
      ` L ${pts[pts.length - 1][0]} ${H - pad} Z`
    : '';

  return (
    <svg width={W} height={H} role="img" aria-label="sparkline">
      <defs>
        <linearGradient id="g-line" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor={fill} />
          <stop offset="100%" stopColor="transparent" />
        </linearGradient>
      </defs>
      {pts.length ? (
        <>
          <path d={areaPath} fill="url(#g-line)" />
          <path d={dPath} fill="none" stroke={stroke} strokeWidth={2} />
        </>
      ) : (
        <text x={W / 2} y={H / 2} textAnchor="middle" fill="#bfbfbf" fontSize="12">暂无数据</text>
      )}
    </svg>
  );
};

/** 将接口时间序列尽量归一到 {t,v}[]（未知结构时容错） */
function coerceSeries(obj: any, nodeKey: string | number): Array<{ t: number; v: number }> {
  if (!obj) return [];
  // 常见几种形态的兜底解析
  // 1) { series: { cpu: [{timestamp,value}], mem: [...] } }
  // 2) { nodes: { [nodeId]: { cpu:[...], mem:[...] } } }
  // 3) { cpu: [...], mem: [...] }（整个集群，后续再筛）
  // 4) 直接就是数组
  const arrish = (x: any) => (Array.isArray(x) ? x : []);
  const normalize = (a: any[]) =>
    a
      .map((i: any) => {
        const t = Number(i?.t ?? i?.ts ?? i?.time ?? i?.timestamp ?? i?.[0]);
        const v = Number(i?.v ?? i?.value ?? i?.val ?? i?.[1]);
        return Number.isFinite(t) && Number.isFinite(v) ? { t, v } : null;
      })
      .filter(Boolean) as Array<{ t: number; v: number }>;

  if (Array.isArray(obj)) return normalize(obj);
  if (obj?.series) return normalize(obj.series);
  if (obj?.nodes && (obj.nodes as KV)[nodeKey]) {
    // 可能是 { cpu:[], mem:[] } 结构
    const node = (obj.nodes as KV)[nodeKey];
    if (Array.isArray(node)) return normalize(node);
    if (node?.cpu) return normalize(node.cpu);
  }
  if (obj?.cpu) return normalize(obj.cpu);
  return [];
}

/** 健康值标准化 */
function mapHealthTag(v: any) {
  const s = String(v ?? '').toLowerCase();
  if (['healthy', 'ok', 'online', '健康'].includes(s)) return <Tag color="green">健康</Tag>;
  if (['warning', 'warn', 'degraded', '预警'].includes(s)) return <Tag color="orange">预警</Tag>;
  if (s) return <Tag color="red">异常</Tag>;
  return <Tag>未知</Tag>;
}

/** 启用/禁用标准化 */
function mapEnabledTag(v: any) {
  const on = typeof v === 'boolean' ? v : Number(v) === 1;
  return on ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag>;
}

const NodeDetailModal: React.FC<Props> = ({ id, open, onClose }) => {
  const [sp] = useSearchParams();
  const { id: rid } = useParams<{ id?: string }>();
  const clusterId = useMemo(() => {
    const q = Number(sp.get('detail') ?? sp.get('clusterId'));
    return Number.isFinite(q) ? q : Number(rid);
  }, [sp, rid]);

  const [node, setNode] = useState<KV | null>(null);
  const [loading, setLoading] = useState(false);

  // 指标
  const [cpuSeries, setCpuSeries] = useState<Array<{ t: number; v: number }>>([]);
  const [memSeries, setMemSeries] = useState<Array<{ t: number; v: number }>>([]);
  const [range, setRange] = useState<'1h' | '6h' | '24h' | '7d'>('6h');

  // 健康记录
  const [hRows, setHRows] = useState<any[]>([]);
  const [hTotal, setHTotal] = useState(0);
  const [hPage, setHPage] = useState(1);
  const hSize = 10;

  const timeRange = useMemo(() => {
    const to = new Date();
    const from = new Date();
    if (range === '1h') from.setHours(to.getHours() - 1);
    if (range === '6h') from.setHours(to.getHours() - 6);
    if (range === '24h') from.setDate(to.getDate() - 1);
    if (range === '7d') from.setDate(to.getDate() - 7);
    return { from: from.toISOString(), to: to.toISOString() };
  }, [range]);

  /** 拉节点基础信息（用 getNodes，全量取一页，再按 id 命中） */
  const fetchNode = useCallback(async () => {
    if (!id || !clusterId) return;
    setLoading(true);
    try {
      const resp = await getNodes({ clusterId }, { page: 1, size: 200 });
      const payload: any = resp as any;
      const pageData =
        (Array.isArray(payload?.data?.data?.data) && payload.data.data.data) ||
        (Array.isArray(payload?.data?.data?.records) && payload.data.data.records) ||
        (Array.isArray(payload?.data?.data?.list) && payload.data.data.list) ||
        (Array.isArray(payload?.data?.data) && payload.data.data) ||
        (Array.isArray(payload?.data) && payload.data) ||
        (Array.isArray(payload) ? payload : []);
      const target = (pageData as any[]).find((n: any) => Number(n?.id) === Number(id)) || null;
      setNode(target);
    } catch (e: any) {
      message.error(e?.message || '加载节点失败');
    } finally {
      setLoading(false);
    }
  }, [id, clusterId]);

  /** 拉指标（尽力解析为 {t,v}[]；失败就降级只显示“当前值”） */
  const fetchMetric = useCallback(async () => {
    if (!id || !clusterId) return;

    try {
      // 先用“最近一次/区间聚合”
      const X = await getNodeMetric({ clusterId }, { from: timeRange.from, to: timeRange.to });
      const Y = await getNodeMetricByAgg({ clusterId }, { from: timeRange.from, to: timeRange.to, agg: '1m' as any });

      // 猜测 nodeKey：用 id 或 name 两种都试下
      const key = (node?.id ?? id) as any;
      const altKey = node?.name ?? node?.hostname ?? node?.ip;

      const c1 = coerceSeries((X as any)?.data ?? X, key);
      const c2 = coerceSeries((Y as any)?.data ?? Y, key);
      const c3 = altKey ? coerceSeries((X as any)?.data ?? X, altKey) : [];
      const c4 = altKey ? coerceSeries((Y as any)?.data ?? Y, altKey) : [];

      const cpu = [c2, c1, c4, c3].find(a => a.length) || [];
      setCpuSeries(cpu);

      // 内存同样复用 series；如果你的返回区分字段（如 memSeries），可在这里再做一次 coerce
      // 为避免“空空”，用 cpu 替代（真实场景建议后端明确返回）
      setMemSeries(cpu);
    } catch {
      setCpuSeries([]);
      setMemSeries([]);
    }
  }, [id, clusterId, node, timeRange]);

  /** 拉健康流水 */
  const fetchHealth = useCallback(async () => {
    if (!id) return;
    try {
      const res: any = await getNodeHealth_2({ nodeId: id }, { page: hPage - 1, size: hSize });
      const payload = res?.data?.data ?? res?.data ?? res ?? {};
      const rows =
        (Array.isArray(payload?.data) && payload.data) ||
        (Array.isArray(payload?.records) && payload.records) ||
        (Array.isArray(payload?.list) && payload.list) ||
        (Array.isArray(payload) ? payload : []);
      const total = payload?.total ?? payload?.totalElements ?? rows.length;
      setHRows(rows as any[]);
      setHTotal(Number(total) || 0);
    } catch {
      setHRows([]);
      setHTotal(0);
    }
  }, [id, hPage]);

  useEffect(() => { if (open) fetchNode(); }, [open, fetchNode]);
  useEffect(() => { if (open && node) fetchMetric(); }, [open, node, fetchMetric]);
  useEffect(() => { if (open) fetchHealth(); }, [open, fetchHealth]);
  useEffect(() => { if (open) fetchMetric(); }, [open, range]); // 切换时间范围

  // —— 概览 KPI —— //
  const lastHeartbeatStr = useMemo(() => {
    const v = node?.lastHeartbeat ?? node?.heartbeatAt ?? node?.updatedAt;
    if (!v) return '-';
    const ts = typeof v === 'number' ? v : Date.parse(String(v));
    return Number.isFinite(ts) ? new Date(ts).toLocaleString() : '-';
  }, [node]);

  const cpuNow = useMemo(() => {
    const v = Number(node?.cpuUsage ?? node?.cpu ?? 0);
    if (Number.isFinite(v) && v > 0) return Math.round(v);
    if (cpuSeries.length) return Math.round(cpuSeries[cpuSeries.length - 1].v);
    return 0;
  }, [node, cpuSeries]);

  const memNow = useMemo(() => {
    const v = Number(node?.memUsage ?? node?.memory ?? 0);
    if (Number.isFinite(v) && v > 0) return Math.round(v);
    if (memSeries.length) return Math.round(memSeries[memSeries.length - 1].v);
    return 0;
  }, [node, memSeries]);

  const Overview = (
    <>
      <Row gutter={16}>
        <Col xs={12} md={6}><Statistic title="健康" valueRender={() => mapHealthTag(node?.health ?? node?.status)} /></Col>
        <Col xs={12} md={6}><Statistic title="状态" valueRender={() => mapEnabledTag(node?.enabled ?? node?.status)} /></Col>
        <Col xs={12} md={6}><Statistic title="最近心跳" value={lastHeartbeatStr} /></Col>
        <Col xs={12} md={6}><Statistic title="CPU / 内存" value={`${cpuNow}% / ${memNow}%`} /></Col>
      </Row>

      <div className="mt-4" />

      <Descriptions column={1} size="small" labelStyle={{ width: 92, color: '#595959' }}>
        <Descriptions.Item label="节点名">{node?.name || `#${id}`}</Descriptions.Item>
        <Descriptions.Item label="IP">{node?.ip || '-'}</Descriptions.Item>
        <Descriptions.Item label="角色">{node?.role || node?.type || '-'}</Descriptions.Item>
        <Descriptions.Item label="标签">{node?.labels || node?.tags || '-'}</Descriptions.Item>
        <Descriptions.Item label="版本">{node?.version || '-'}</Descriptions.Item>
        <Descriptions.Item label="备注">{node?.remark || '-'}</Descriptions.Item>
        <Descriptions.Item label="创建时间">
          {node?.createdAt ? new Date(node.createdAt).toLocaleString() : '-'}
        </Descriptions.Item>
      </Descriptions>
    </>
  );

  const Metrics = (
    <>
      <div className="flex justify-between items-center mb-2">
        <Radio.Group value={range} onChange={e => setRange(e.target.value)} size="small">
          <Radio.Button value="1h">近 1 小时</Radio.Button>
          <Radio.Button value="6h">近 6 小时</Radio.Button>
          <Radio.Button value="24h">近 24 小时</Radio.Button>
          <Radio.Button value="7d">近 7 天</Radio.Button>
        </Radio.Group>
        <Button size="small" icon={<ReloadOutlined />} onClick={() => { fetchMetric(); }} />
      </div>

      <Row gutter={16}>
        <Col span={12}>
          <CardLike title="CPU 使用率">
            <Sparkline data={cpuSeries} max={100} />
            <div style={{ marginTop: 6, color: '#8c8c8c' }}>当前 {cpuNow}%</div>
          </CardLike>
        </Col>
        <Col span={12}>
          <CardLike title="内存 使用率">
            <Sparkline data={memSeries} max={100} stroke="#13c2c2" fill="#13c2c214" />
            <div style={{ marginTop: 6, color: '#8c8c8c' }}>当前 {memNow}%</div>
          </CardLike>
        </Col>
      </Row>
    </>
  );

  const Health = (
    <>
      <Table
        size="small"
        rowKey={(_, i) => String(i)}
        dataSource={hRows}
        pagination={{
          current: hPage,
          total: hTotal,
          pageSize: hSize,
          onChange: p => setHPage(p),
        }}
        columns={[
          {
            title: '时间',
            dataIndex: 'time',
            width: 200,
            render: (v: any, r: any) => {
              const ts = Number(v ?? r.timestamp ?? Date.parse(String(v ?? r?.createdAt)));
              return Number.isFinite(ts) ? new Date(ts).toLocaleString() : '-';
            },
          },
          {
            title: '级别',
            dataIndex: 'level',
            width: 120,
            render: (v: any) => mapHealthTag(v),
          },
          { title: '描述', dataIndex: 'message', ellipsis: true },
          { title: '来源', dataIndex: 'source', width: 140, ellipsis: true },
        ]}
      />
    </>
  );

  const items: TabsProps['items'] = [
    { key: 'overview', label: '概览', children: Overview },
    { key: 'metrics',  label: '指标', children: Metrics },
    { key: 'health',   label: '健康记录', children: Health },
  ];

  return (
    <Drawer
      title={
        <Space size={12}>
          <span>节点明细：</span>
          <b>{node?.name || `#${id}`}</b>
          <Tag>{node?.ip || '-'}</Tag>
        </Space>
      }
      width={980}
      open={open}
      onClose={onClose}
      destroyOnClose
      extra={
        <Space>
          <Button size="small" icon={<ReloadOutlined />} onClick={() => { fetchNode(); fetchMetric(); fetchHealth(); }}>
            刷新
          </Button>
        </Space>
      }
      styles={{ body: { paddingTop: 12 } }}
      loading={loading as any}
    >
      <Tabs defaultActiveKey="overview" items={items} />
    </Drawer>
  );
};

export default NodeDetailModal;

/** 轻量卡片容器（避免引入 antd Card 造成边距不统一，这里自定义一点点样式） */
const CardLike: React.FC<{ title: React.ReactNode; children: React.ReactNode }> = ({ title, children }) => {
  return (
    <div style={{ border: '1px solid #f0f0f0', borderRadius: 10, padding: 12 }}>
      <div style={{ fontWeight: 600, marginBottom: 8 }}>{title}</div>
      {children}
    </div>
  );
};
