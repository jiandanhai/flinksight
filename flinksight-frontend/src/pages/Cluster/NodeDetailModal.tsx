import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { Drawer, Tabs, Descriptions, Space, Tag, Statistic, Row, Col, Table, Button, Radio, message, DatePicker } from 'antd';
import type { TabsProps } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import { useSearchParams, useParams } from 'react-router-dom';
import * as api from '@/api/modules';
import client from '@/api/client';
import dayjs, { Dayjs } from 'dayjs';

const { RangePicker } = DatePicker;

type Props = {
  id: number | string | null;
  open: boolean;
  onClose: () => void;
};

type KV = Record<string, any>;

const ui2apiPage = (uiPage: number) => Math.max(0, Number(uiPage) - 1);

function unpackPageLike(resp: any) {
  const root = resp?.data ?? resp;
  const outer = root?.data !== undefined ? root.data : root;
  const keys = ['data','records','list','rows','items','content','result'];

  if (Array.isArray(outer)) return { list: outer, total: outer.length };

  for (const k of keys) {
    const v = outer?.[k];
    if (Array.isArray(v)) {
      const total = Number(outer?.total ?? outer?.totalElements ?? outer?.totalCount ?? outer?.count ?? v.length) || v.length;
      return { list: v, total };
    }
  }

  const d = outer?.data;
  if (d && typeof d === 'object') {
    for (const k of keys) {
      const v = d?.[k];
      if (Array.isArray(v)) {
        const total = Number(d?.total ?? d?.totalElements ?? d?.totalCount ?? d?.count ?? v.length) || v.length;
        return { list: v, total };
      }
    }
  }

  if (outer && typeof outer === 'object') {
    for (const [, v] of Object.entries(outer)) {
      if (Array.isArray(v)) {
        const total = Number(outer?.total ?? outer?.totalElements ?? outer?.totalCount ?? outer?.count ?? (v as any[]).length) || (v as any[]).length;
        return { list: v as any[], total };
      }
    }
  }
  return { list: [] as any[], total: 0 };
}

/** 简易迷你折线图（纯 SVG） */
const Sparkline: React.FC<{
  data: Array<{ t: number; v: number }>;
  height?: number;
  stroke?: string;
  fill?: string;
  max?: number;
}> = ({ data, height = 56, stroke = '#165DFF', fill = '#165dff14', max }) => {
  const pad = 4, W = 240, H = height;
  const M = data.length ? (max ?? Math.max(...data.map(d => Number(d.v) || 0), 1)) : 1;
  const pts = data.map((d, i) => {
    const x = pad + (i * (W - pad * 2)) / Math.max(1, data.length - 1);
    const y = H - pad - ((Number(d.v) || 0) / M) * (H - pad * 2);
    return [x, y] as [number, number];
  });
  const dPath = pts.length ? `M ${pts[0][0]} ${pts[0][1]} ` + pts.slice(1).map(p => `L ${p[0]} ${p[1]}`).join(' ') : '';
  const areaPath = pts.length
    ? `M ${pts[0][0]} ${H - pad} L ${pts[0][0]} ${pts[0][1]} ` + pts.slice(1).map(p => `L ${p[0]} ${p[1]}`).join(' ') + ` L ${pts[pts.length - 1][0]} ${H - pad} Z`
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

function coerceSeries(obj: any, nodeKey: string | number): Array<{ t: number; v: number }> {
  if (!obj) return [];
  const normalize = (a: any[]) =>
    a.map((i: any) => {
      const t = Number(i?.t ?? i?.ts ?? i?.time ?? i?.timestamp ?? i?.[0]);
      const v = Number(i?.v ?? i?.value ?? i?.val ?? i?.[1]);
      return Number.isFinite(t) && Number.isFinite(v) ? { t, v } : null;
    }).filter(Boolean) as Array<{ t: number; v: number }>;

  if (Array.isArray(obj)) return normalize(obj);
  if (obj?.series) return normalize(obj.series);
  if (obj?.nodes && obj.nodes?.[nodeKey as any]) {
    const node = obj.nodes?.[nodeKey as any];
    if (Array.isArray(node)) return normalize(node);
    if (node?.cpu) return normalize(node.cpu);
  }
  if (obj?.cpu) return normalize(obj.cpu);
  return [];
}

function mapHealthTag(v: any) {
  const s = String(v ?? '').toLowerCase();
  if (['healthy','ok','online','健康'].includes(s)) return <Tag color="green">健康</Tag>;
  if (['warning','warn','degraded','预警'].includes(s)) return <Tag color="orange">预警</Tag>;
  if (s) return <Tag color="red">异常</Tag>;
  return <Tag>未知</Tag>;
}

/** 兼容布尔 / 数字 / 字符串枚举(ENABLED|DISABLED|0|1) */
function mapEnabledTag(v: any) {
  const s = String(v ?? '').toUpperCase();
  const on =
    typeof v === 'boolean' ? v
    : s === 'ENABLED' || s === 'TRUE' || s === '1' || s === 'ON' || s === 'OK';
  return on ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag>;
}

/** LocalDateTime 期望的本地时间格式（不带 Z、不带时区） */
const toLocal = (d: Dayjs) => d.format('YYYY-MM-DDTHH:mm:ss');

const oneHour = 60 * 60 * 1000;
const MAX_METRIC_RANGE_MS = 7 * 24 * oneHour;
const pickAgg = (rangeMs: number) => {
  if (rangeMs <= 6 * oneHour) return '1m';
  if (rangeMs <= 24 * oneHour) return '5m';
  return '30m';
};

const NodeDetailModal: React.FC<Props> = ({ id, open, onClose }) => {
  const [sp] = useSearchParams();
  const { id: rid } = useParams<{ id?: string }>();
  const clusterId = useMemo(() => {
    const q = Number(sp.get('detail') ?? sp.get('clusterId'));
    return Number.isFinite(q) ? q : Number(rid);
  }, [sp, rid]);

  const [node, setNode] = useState<KV | null>(null);
  const [loading, setLoading] = useState(false);

  // —— 指标 —— //
  const [cpuSeries, setCpuSeries] = useState<Array<{ t: number; v: number }>>([]);
  const [memSeries, setMemSeries] = useState<Array<{ t: number; v: number }>>([]);
  const [range, setRange] = useState<'1h' | '6h' | '24h' | '7d'>('6h');

  const metricRange = useMemo(() => {
    const now = Date.now();
    let fromMs = now;
    if (range === '1h')  fromMs = now - 1  * oneHour;
    if (range === '6h')  fromMs = now - 6  * oneHour;
    if (range === '24h') fromMs = now - 24 * oneHour;
    if (range === '7d')  fromMs = now - 7  * 24 * oneHour;
    const minFrom = now - MAX_METRIC_RANGE_MS;
    if (fromMs < minFrom) fromMs = minFrom;
    const dur = now - fromMs;
    return {
      from: new Date(fromMs).toISOString(),
      to:   new Date(now).toISOString(),
      agg:  pickAgg(dur),
    };
  }, [range]);

  // —— 健康明细 —— //
  const [hRange, setHRange] = useState<[Dayjs, Dayjs]>(() => [dayjs().subtract(7, 'day'), dayjs()]);
  const [hRows, setHRows] = useState<any[]>([]);
  const [hTotal, setHTotal] = useState(0);
  const [hPage, setHPage] = useState(1);
  const hSize = 10;

  /** 拉节点基础信息 */
  const fetchNode = useCallback(async () => {
    if (!id || !clusterId) return;
    setLoading(true);
    try {
      const resp = await (api as any).getNodes({ clusterId }, { page: 0, size: 200 });
      const { list } = unpackPageLike(resp);
      const target = (list as any[]).find((n: any) => String(n?.id) === String(id)) || null;
      setNode(target);
    } catch (e: any) {
      message.error(e?.message || '加载节点失败');
    } finally {
      setLoading(false);
    }
  }, [id, clusterId]);

  /** 拉指标 */
  const fetchMetric = useCallback(async () => {
    if (!id || !clusterId) return;
    try {
      const fnAgg = (api as any).getNodeMetricByAgg || (api as any).getClusterNodesMetricAgg;
      if (!fnAgg) { setCpuSeries([]); setMemSeries([]); return; }

      const res: any = await fnAgg(
        { clusterId },
        { from: metricRange.from, to: metricRange.to, agg: metricRange.agg }
      );

      const key   = (node?.id ?? id) as any;
      const alias = node?.name ?? node?.hostname ?? node?.ip;

      const a = coerceSeries((res?.data ?? res), key);
      const b = alias ? coerceSeries((res?.data ?? res), alias) : [];
      const cpu = [a, b].find(x => x.length) || [];
      setCpuSeries(cpu);
      setMemSeries(cpu);
    } catch {
      setCpuSeries([]); setMemSeries([]);
    }
  }, [id, clusterId, node, metricRange]);

  /** 健康明细历史 */
  const fetchHealth = useCallback(async () => {
    if (!id) return;
    try {
      const url = `/api/node/nodes/health/${id}`;
      const res: any = await client.get(url, {
        params: {
          from: toLocal(hRange[0]),
          to:   toLocal(hRange[1]),
          page: hPage,
          size: hSize,
        }
      });

      const { list, total } = unpackPageLike(res);
      const rows = (list as any[]).map((r: any) => ({
        id: r.id,
        time: r.checkTime ?? r.time ?? r.timestamp ?? r.createdAt,
        level: r.healthStatus ?? r.health ?? r.level,
        message: r.message,
        source: r.source,
        createdAt: r.createdAt,
      }));
      setHRows(rows);
      setHTotal(Number(total) || rows.length);
    } catch {
      setHRows([]); setHTotal(0);
    }
  }, [id, hRange, hPage]);

  // —— 生命周期 —— //
  useEffect(() => { if (open) fetchNode(); }, [open, fetchNode]);
  useEffect(() => { if (open && node) fetchMetric(); }, [open, node, fetchMetric]);
  useEffect(() => { if (open) fetchHealth(); }, [open, fetchHealth]);
  useEffect(() => { if (open) fetchMetric(); }, [open, range]);

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

      <Descriptions
        column={1}
        size="small"
        styles={{ label: { width: 92, color: '#595959' } }}
      >
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
      <div className="flex justify-between items-center mb-2 gap-2">
        <Radio.Group value={range} onChange={e => setRange(e.target.value)} size="small">
          <Radio.Button value="1h">近 1 小时</Radio.Button>
          <Radio.Button value="6h">近 6 小时</Radio.Button>
          <Radio.Button value="24h">近 24 小时</Radio.Button>
          <Radio.Button value="7d">近 7 天</Radio.Button>
        </Radio.Group>
        <div style={{ fontSize: 12, color: '#8c8c8c' }}>指标仅保留近 7 天，超出会自动截断</div>
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
      <div className="flex items-center justify-between mb-2">
        <Space size={8}>
          <span style={{ color: '#595959' }}>时间范围：</span>
          <RangePicker
            showTime
            allowClear={false}
            value={hRange}
            format="YYYY-MM-DD HH:mm:ss"
            onChange={(vals) => {
              if (!vals || vals.length !== 2 || !vals[0] || !vals[1]) return;
              setHRange([vals[0], vals[1]]);
              setHPage(1);
            }}
            onOk={(vals) => {
              if (!vals || vals.length !== 2 || !vals[0] || !vals[1]) return;
              setHRange([vals[0] as Dayjs, vals[1] as Dayjs]);
              setHPage(1);
            }}
          />
        </Space>
        <Button size="small" icon={<ReloadOutlined />} onClick={() => fetchHealth()}>刷新</Button>
      </div>

      <Table
        size="small"
        rowKey={(r: any) =>
          String(
            r.id ??
            r.time ??
            r.checkTime ??
            r.timestamp ??
            r.createdAt ??
            `${r.level ?? ''}-${r.message ?? ''}-${r.source ?? ''}`
          )
        }
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

const CardLike: React.FC<{ title: React.ReactNode; children: React.ReactNode }> = ({ title, children }) => {
  return (
    <div style={{ border: '1px solid #f0f0f0', borderRadius: 10, padding: 12 }}>
      <div style={{ fontWeight: 600, marginBottom: 8 }}>{title}</div>
      {children}
    </div>
  );
};
