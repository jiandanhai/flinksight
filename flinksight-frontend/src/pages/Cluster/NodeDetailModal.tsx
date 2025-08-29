import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { Drawer, Tabs, Descriptions, Space, Tag, Statistic, Row, Col, Table, Button, Radio, message, DatePicker } from 'antd';
import type { TabsProps } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import { useSearchParams, useParams } from 'react-router-dom';

import {
  getNodes,
  getNodeMetricByAgg,
  nodeHealthHistory, // GET /api/node/nodes/health/{nodeId}
} from '@/api/modules';

type Props = { id: number | string | null; open: boolean; onClose: () => void; };
type KV = Record<string, any>;
const { RangePicker } = DatePicker;

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

/** 健康历史接口要求 LocalDateTime：yyyy-MM-dd'T'HH:mm:ss（无毫秒/无Z） */
function fmtLDT(d: Date) {
  const pad = (n: number) => String(n).padStart(2, '0');
  const y = d.getFullYear();
  const m = pad(d.getMonth() + 1);
  const day = pad(d.getDate());
  const hh = pad(d.getHours());
  const mm = pad(d.getMinutes());
  const ss = pad(d.getSeconds());
  return `${y}-${m}-${day}T${hh}:${mm}:${ss}`;
}

/** 尝试把 dayjs/moment/Date/字符串 转成 Date */
function toDate(x: any): Date | null {
  if (!x) return null;
  if (x instanceof Date) return x;
  if (typeof x?.toDate === 'function') return x.toDate();
  const ts = Date.parse(String(x));
  return Number.isFinite(ts) ? new Date(ts) : null;
}

/** 纯 SVG sparkline 小图 */
const Sparkline: React.FC<{ data: Array<{ t: number; v: number }>; height?: number; stroke?: string; fill?: string; max?: number; }>
= ({ data, height = 56, stroke = '#165DFF', fill = '#165dff14', max }) => {
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
      {pts.length ? (<>
        <path d={areaPath} fill="url(#g-line)" />
        <path d={dPath} fill="none" stroke={stroke} strokeWidth={2} />
      </>) : (<text x={W/2} y={H/2} textAnchor="middle" fill="#bfbfbf" fontSize="12">暂无数据</text>)}
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
function mapEnabledTag(v: any) {
  const s = String(v ?? '').toUpperCase();
  const on = typeof v === 'boolean' ? v : s === 'ENABLED' || s === 'TRUE' || s === '1' || s === 'ON' || s === 'OK';
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

  // 指标范围（仍用顶部单选）
  const [metricRange, setMetricRange] = useState<'1h' | '6h' | '24h' | '7d'>('6h');
  const metricTime = useMemo(() => {
    const to = new Date();
    const from = new Date();
    if (metricRange === '1h') from.setHours(to.getHours() - 1);
    if (metricRange === '6h') from.setHours(to.getHours() - 6);
    if (metricRange === '24h') from.setDate(to.getDate() - 1);
    if (metricRange === '7d') from.setDate(to.getDate() - 7);
    return { fromISO: from.toISOString(), toISO: to.toISOString() };
  }, [metricRange]);

  // ✅ 健康记录范围：优先使用页面上的 RangePicker；未选择时默认近 30 天
  const [healthPicker, setHealthPicker] = useState<[any, any] | null>(null);
  const healthTime = useMemo(() => {
    if (healthPicker?.[0] && healthPicker?.[1]) {
      const f = toDate(healthPicker[0]); const t = toDate(healthPicker[1]);
      return {
        fromLDT: f ? fmtLDT(f) : undefined,
        toLDT: t ? fmtLDT(t) : undefined,
      };
    }
    const to = new Date();
    const from = new Date();
    from.setDate(to.getDate() - 30);
    return { fromLDT: fmtLDT(from), toLDT: fmtLDT(to) };
  }, [healthPicker]);

  const [cpuSeries, setCpuSeries] = useState<Array<{ t: number; v: number }>>([]);
  const [memSeries, setMemSeries] = useState<Array<{ t: number; v: number }>>([]);

  const [hRows, setHRows] = useState<any[]>([]);
  const [hTotal, setHTotal] = useState(0);
  const [hPage, setHPage] = useState(1);
  const hSize = 10;

  const fetchNode = useCallback(async () => {
    if (!id || !clusterId) return;
    setLoading(true);
    try {
      const resp = await getNodes({ clusterId }, { page: 0, size: 200 });
      const { list } = unpackPageLike(resp);
      const target = (list as any[]).find((n: any) => String(n?.id) === String(id)) || null;
      setNode(target);
    } catch (e: any) {
      message.error(e?.message || '加载节点失败');
    } finally {
      setLoading(false);
    }
  }, [id, clusterId]);

  const fetchMetric = useCallback(async () => {
    if (!id || !clusterId) return;
    try {
      const agg = await getNodeMetricByAgg({ clusterId }, { from: metricTime.fromISO, to: metricTime.toISO, agg: '1m' as any });
      const key = (node?.id ?? id) as any;
      const altKey = node?.name ?? node?.hostname ?? node?.ip;
      const c2 = coerceSeries((agg as any)?.data ?? agg, key);
      const c4 = altKey ? coerceSeries((agg as any)?.data ?? agg, altKey) : [];
      const cpu = [c2, c4].find(a => a.length) || [];
      setCpuSeries(cpu);
      setMemSeries(cpu);
    } catch {
      setCpuSeries([]); setMemSeries([]);
    }
  }, [id, clusterId, node, metricTime]);

  const fetchHealth = useCallback(async () => {
    if (!id) return;
    try {
      const params: any = { page: hPage, size: hSize };
      if (healthTime.fromLDT) params.from = healthTime.fromLDT;
      if (healthTime.toLDT)   params.to   = healthTime.toLDT;

      const res = await nodeHealthHistory({ nodeId: Number(id) }, params);
      const { list, total } = unpackPageLike(res);
      const rows = (list as any[]).map((r: any) => ({
        id: r.id,
        time: r.time ?? r.checkTime ?? r.timestamp ?? r.createdAt,
        level: r.healthStatus ?? r.health ?? r.level,
        message: r.message,
        source: r.source,
      }));
      setHRows(rows);
      setHTotal(Number(total) || rows.length);
    } catch {
      setHRows([]); setHTotal(0);
    }
  }, [id, hPage, hSize, healthTime]);

  useEffect(() => { if (open) fetchNode(); }, [open, fetchNode]);
  useEffect(() => { if (open && node) fetchMetric(); }, [open, node, fetchMetric]);
  useEffect(() => { if (open) fetchHealth(); }, [open, fetchHealth]);     // 打开时：用 RangePicker 的值(若未选用默认30天)
  useEffect(() => { if (open) fetchMetric(); }, [open, metricRange]);     // 切换指标范围
  useEffect(() => { if (open) { setHPage(1); fetchHealth(); } }, [open, healthPicker]); // RangePicker 变化重查

  const lastHeartbeatStr = useMemo(() => {
    const v = node?.lastHeartbeat ?? node?.heartbeatAt ?? node?.updatedAt ?? node?.healthTime;
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

      <Descriptions column={1} size="small" styles={{ label: { width: 92, color: '#595959' } }}>
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
        <Radio.Group value={metricRange} onChange={e => setMetricRange(e.target.value)} size="small">
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
      <div className="flex justify-between items-center mb-2">
        {/* ✅ 用页面时间选择器控制健康记录范围；未选时默认近30天 */}
        <RangePicker
          showTime
          allowClear
          value={healthPicker as any}
          onChange={(vals) => { setHPage(1); setHealthPicker(vals ? [vals[0], vals[1]] : null); }}
          style={{ width: 360 }}
          placeholder={['开始时间', '结束时间']}
        />
        <Button size="small" icon={<ReloadOutlined />} onClick={() => { setHPage(1); fetchHealth(); }} />
      </div>

      <Table
        size="small"
        rowKey={(r: any) =>
          String(r.id ?? r.time ?? r.checkTime ?? r.timestamp ?? r.createdAt ??
            `${r.level ?? ''}-${r.message ?? ''}-${r.source ?? ''}`)
        }
        dataSource={hRows}
        pagination={{ current: hPage, total: hTotal, pageSize: hSize, onChange: p => setHPage(p) }}
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
          { title: '级别', dataIndex: 'level', width: 120, render: (v: any) => mapHealthTag(v) },
          { title: '描述', dataIndex: 'message', ellipsis: true },
          { title: '来源', dataIndex: 'source', width: 140, ellipsis: true },
        ]}
        locale={{
          emptyText: '所选时间范围内无健康记录',
        }}
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

const CardLike: React.FC<{ title: React.ReactNode; children: React.ReactNode }> = ({ title, children }) => (
  <div style={{ border: '1px solid #f0f0f0', borderRadius: 10, padding: 12 }}>
    <div style={{ fontWeight: 600, marginBottom: 8 }}>{title}</div>
    {children}
  </div>
);
