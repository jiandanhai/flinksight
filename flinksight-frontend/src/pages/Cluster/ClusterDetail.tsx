/**
 * @file 集群详情：基础信息 / 节点健康分布 / 节点 & 节点状态 Tabs
 * 说明：
 * - 仅保留“安全动作”，默认只展示节点扩容（RBAC: admin/ops），不提供启停/扩容集群等危险操作。
 * - 健康统计来自 getNodes，使用严格 HEALTHY/WARNING/其他→ERROR 桶，与后端字段对齐。
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, Card, Col, Row, Tabs, Tooltip } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';

import NodeList from './NodeList';
import NodeStatusPanel from './NodeStatusPanel';
import ExpandNodeModal from './ExpandNodeModal';
import { useUser } from '@/store/user';
import { getCluster, getNodes } from '@/api/modules';

type Health = { healthy: number; warning: number; error: number; };

const COLORS = {
  healthy: { g1: '#52c41a', g2: '#73d13d', solid: '#52c41a' },
  warning: { g1: '#faad14', g2: '#ffc53d', solid: '#faad14' },
  error:   { g1: '#ff4d4f', g2: '#ff7875', solid: '#ff4d4f' },
};

// —— 与列表/节点页保持一致的“分页容错抽取” —— //
function unpackPageLike(resp: any) {
  const root  = resp?.data ?? resp;
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

// 严格：只按 row.health 聚合（HEALTHY/WARNING/其它→异常）
function toBucketsStrict(rows: any[]): Health {
  let healthy = 0, warning = 0, error = 0;
  rows.forEach(r => {
    const hv = String(r?.health ?? '').trim().toUpperCase();
    if      (hv === 'HEALTHY') healthy++;
    else if (hv === 'WARNING') warning++;
    else                       error++;
  });
  return { healthy, warning, error };
}

// —— Donut 绘图工具 —— //
function polar(cx: number, cy: number, r: number, angle: number) {
  const rad = (angle - 90) * (Math.PI / 180);
  return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
}
function arcPath(cx: number, cy: number, r: number, startDeg: number, endDeg: number) {
  const s = polar(cx, cy, r, endDeg);
  const e = polar(cx, cy, r, startDeg);
  const largeArc = endDeg - startDeg <= 180 ? 0 : 1;
  return `M ${s.x} ${s.y} A ${r} ${r} 0 ${largeArc} 0 ${e.x} ${e.y}`;
}
const Donut: React.FC<{
  healthy: number; warning: number; error: number;
  size?: number; thickness?: number;
  externalHover?: 'healthy' | 'warning' | 'error' | null;
  onHoverChange?: (h: 'healthy' | 'warning' | 'error' | null) => void;
}> = ({ healthy, warning, error, size = 148, thickness = 16, externalHover, onHoverChange }) => {
  const total = Math.max(0, Number(healthy) + Number(warning) + Number(error));
  const cx = size / 2, cy = size / 2, r = size / 2 - thickness / 2;
  const [innerHover, setInnerHover] = useState<null | 'healthy' | 'warning' | 'error'>(null);
  const hover = externalHover ?? innerHover;

  const raw = [
    { key: 'healthy' as const, val: healthy, color: 'url(#g-healthy)', title: '健康' },
    { key: 'warning' as const, val: warning, color: 'url(#g-warning)', title: '预警' },
    { key: 'error'   as const, val: error,   color: 'url(#g-error)',   title: '异常' },
  ].filter(s => s.val > 0);

  let acc = 0; const gap = total > 1 ? 4 : 0;
  const segs = total === 0 ? [] : raw.map(s => {
    const deg = (s.val / total) * 360, start = acc + gap / 2, end = acc + deg - gap / 2; acc += deg;
    return { ...s, start, end };
  });
  const ordered = hover ? [...segs.filter(s => s.key !== hover), ...segs.filter(s => s.key === hover)] : segs;

  const center = useMemo(() => {
    if (!total) return { l1: '0', l2: '节点' };
    const map: any = { healthy: '健康', warning: '预警', error: '异常' };
    if (!hover) return { l1: String(total), l2: '节点' };
    const val = hover === 'healthy' ? healthy : hover === 'warning' ? warning : error;
    const pct = Math.round((val / total) * 100);
    return { l1: String(val), l2: `${map[hover]} · ${pct}%` };
  }, [hover, healthy, warning, error, total]);

  return (
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <defs>
          <linearGradient id="g-healthy" x1="0" y1="0" x2="1" y2="1"><stop offset="0%" stopColor={COLORS.healthy.g1}/><stop offset="100%" stopColor={COLORS.healthy.g2}/></linearGradient>
          <linearGradient id="g-warning" x1="0" y1="0" x2="1" y2="1"><stop offset="0%" stopColor={COLORS.warning.g1}/><stop offset="100%" stopColor={COLORS.warning.g2}/></linearGradient>
          <linearGradient id="g-error"   x1="0" y1="0" x2="1" y2="1"><stop offset="0%" stopColor={COLORS.error.g1}/><stop offset="100%" stopColor={COLORS.error.g2}/></linearGradient>
        </defs>
        <circle cx={cx} cy={cy} r={r} fill="none" stroke="#f5f5f5" strokeWidth={thickness} />
        <g transform={`rotate(-90, ${cx}, ${cy})`}>
          {ordered.map(seg => (
              <path
                  key={seg.key}
                  d={arcPath(cx, cy, r, seg.start, seg.end)}
                  fill="none"
                  stroke={seg.color}
                  strokeWidth={hover===seg.key ? thickness + 3 : thickness}
                  strokeLinecap="round"
                  onMouseEnter={() => { setInnerHover(seg.key); onHoverChange?.(seg.key); }}
                  onMouseLeave={() => { setInnerHover(null);     onHoverChange?.(null);     }}
              >
                <title>{`${seg.title}：${seg.val}（${Math.round((seg.val/Math.max(1,total))*100)}%）`}</title>
              </path>
          ))}
        </g>
        <g textAnchor="middle" dominantBaseline="central">
          <text x={cx} y={cy - 6} fontSize={Math.round(size*0.2)} fill="#1f1f1f">{center.l1}</text>
          <text x={cx} y={cy + 12} fontSize={Math.round(size*0.11)} fill="#8c8c8c">{center.l2}</text>
        </g>
      </svg>
  );
};

const ClusterDetail: React.FC = () => {
  const [sp, setSp] = useSearchParams();
  const navigate = useNavigate();
  const { id: idFromParams } = useParams<{ id?: string }>();
  const { userInfo } = useUser();

  // 兼容 query(detail/clusterId) 与 /:id
  const clusterIdParam = sp.get('clusterId');
  const detailParam    = sp.get('detail');
  const _qId = Number(detailParam ?? clusterIdParam);
  const id = Number.isFinite(_qId) ? _qId : Number(idFromParams);

  const activeTab = sp.get('tab') || 'nodes';

  const [cluster, setCluster] = useState<any>(null);
  const [health, setHealth] = useState<Health>({ healthy: 0, warning: 0, error: 0 });
  const [loading, setLoading] = useState(false);
  const [legendHover, setLegendHover] = useState<null | 'healthy' | 'warning' | 'error'>(null);
  const [expandOpen, setExpandOpen] = useState(false);

  const canExpand = ['admin', 'ops'].includes(userInfo?.role || '');

  const fetchCluster = async () => {
    if (!id) return;
    const resp = await getCluster(id);
    const data = resp?.data ?? resp;
    setCluster(data || {});
  };

  const fetchHealth = async () => {
    if (!id) return;
    setLoading(true);
    try {
      const resp = await getNodes({ clusterId: id, page: 0, size: 200 }); // 拉一页足够展示健康概览
      const { list } = unpackPageLike(resp);
      setHealth(toBucketsStrict(list || []));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchCluster(); fetchHealth(); /* eslint-disable-next-line */ }, [id]);

  const onTabChange = (k: string) => {
    const next = new URLSearchParams(sp);
    next.set('tab', k);
    setSp(next, { replace: true });
  };

  const basicInfo: Array<[string, React.ReactNode]> = useMemo(() => {
    const d = cluster || {};
    return [
      ['名称', d.name || '-'],
      ['类型', d.type || '-'],
      ['版本', d.version || '-'],
      ['连接', d.endpoint || '-'],
      ['状态', (d.enabled ?? d.status) ? '启用' : '停用'],
      ['创建时间', d.createdAt ? new Date(typeof d.createdAt === 'number' ? d.createdAt : Date.parse(d.createdAt)).toLocaleString() : '-'],
      ['备注', d.remark || '-'],
    ];
  }, [cluster]);

  const items = useMemo(() => ([
    { key: 'nodes',  label: '节点',     children: <NodeList clusterId={id as number} /> },
    { key: 'status', label: '节点状态', children: <NodeStatusPanel clusterId={id as number} /> },
  ]), [id]);

  return (
      <div className="p-6">
        <div className="flex justify-between items-center mb-4">
          <Button type="link" onClick={() => navigate('/cluster/list')}>← 返回列表</Button>
          <Tooltip title={canExpand ? '' : '无扩容权限（需要 admin/ops 角色）'}>
            <Button type="primary" icon={<PlusOutlined />} onClick={() => setExpandOpen(true)} disabled={!canExpand}>
              节点扩容
            </Button>
          </Tooltip>
        </div>

        <Row gutter={16}>
          <Col span={8}>
            <Card loading={loading} title={<b>基础信息</b>}>
              <div className="space-y-2">
                {basicInfo.map(([k, v]) => (
                    <div key={String(k)}>
                      <b className="mr-2">{k}：</b>
                      <span>{v as any}</span>
                    </div>
                ))}
              </div>
            </Card>

            <Card className="mt-4" title={<b>节点健康分布</b>}>
              <div className="flex items-center gap-4">
                <Donut
                    healthy={health.healthy}
                    warning={health.warning}
                    error={health.error}
                    externalHover={legendHover}
                    onHoverChange={(h) => setLegendHover(h)}
                />
                <div className="space-y-1">
                  {[
                    { key: 'healthy' as const, name: '健康', count: health.healthy, color: COLORS.healthy.solid },
                    { key: 'warning' as const, name: '预警', count: health.warning, color: COLORS.warning.solid },
                    { key: 'error'   as const, name: '异常', count: health.error,   color: COLORS.error.solid },
                  ].map(item => (
                      <div
                          key={item.key}
                          onMouseEnter={() => setLegendHover(item.key)}
                          onMouseLeave={() => setLegendHover(null)}
                          style={{ display: 'flex', alignItems: 'center', gap: 8, cursor: 'default' }}
                          title={`${item.name}：${item.count}`}
                      >
                    <span style={{
                      width: 10, height: 10, borderRadius: 2, background: item.color,
                      boxShadow: legendHover === item.key ? `0 0 0 4px ${item.color}22` : undefined,
                      transition: 'box-shadow .2s ease',
                    }}/>
                        <span style={{ minWidth: 32, color: '#595959' }}>{item.name}</span>
                        <span style={{ fontWeight: 600 }}>{item.count}</span>
                      </div>
                  ))}
                </div>
              </div>
            </Card>
          </Col>

          <Col span={16}>
            <Card>
              <Tabs activeKey={activeTab} onChange={onTabChange} items={items} />
            </Card>
          </Col>
        </Row>

        <ExpandNodeModal
            open={expandOpen}
            clusterId={id as number}
            canEdit={canExpand}
            onClose={() => setExpandOpen(false)}
            onOk={() => { setExpandOpen(false); fetchHealth(); }}
        />
      </div>
  );
};

export default ClusterDetail;
