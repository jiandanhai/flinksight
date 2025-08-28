// 只依赖现有 /api/modules，不新增任何模块
import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { Button, Card, Col, Row, Tabs, message, Tooltip } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useNavigate, useSearchParams, useParams } from 'react-router-dom';
import type { TabsProps } from 'antd';
import { getCluster, getNodes } from '@/api/modules';
import { useUser } from '@/store/user';
import NodeList from './NodeList';
import NodeStatusPanel from './NodeStatusPanel';
import ExpandNodeModal from './ExpandNodeModal';

type Health = { healthy: number; warning: number; error: number };

/** —— 色板（与 antd 体系协调，同时考虑色弱可区分） —— */
const COLORS = {
  healthy: { solid: '#52c41a', g1: '#52c41a', g2: '#73d13d' },
  warning: { solid: '#faad14', g1: '#faad14', g2: '#ffc53d' },
  error:   { solid: '#ff4d4f', g1: '#ff4d4f', g2: '#ff7875' },
};

/** 权限工具：优先 perms，再回退到角色 */
function hasPerm(userInfo: any, permCode: string, roleFallback: string[] = ['admin', 'ops']) {
  const role = String(userInfo?.role || userInfo?.roleCode || '').toLowerCase();
  const perms: string[] = Array.isArray(userInfo?.perms) ? userInfo!.perms : [];
  return (perms?.includes(permCode)) || roleFallback.includes(role);
}

/** 将极坐标转笛卡尔 / 生成圆弧 path */
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

/** —— 分段环形图（可与图例联动高亮） —— */
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

/** 将节点行聚合为 {healthy, warning, error} */
function toBuckets(rows: any[]): Health {
  let healthy = 0, warning = 0, error = 0;
  const norm = (v: any) => String(v ?? '').trim().toLowerCase();
  rows.forEach((r) => {
    const hv = norm(r?.health), sv = norm(r?.status);
    if (hv) { if (['healthy','ok','online','健康'].includes(hv)) healthy++; else if (['warning','warn','degraded','预警'].includes(hv)) warning++; else error++; return; }
    if (sv) { if (['healthy','ok','online'].includes(sv)) healthy++; else if (['warning','warn','degraded'].includes(sv)) warning++; else error++; return; }
    if (r?.enabled === true || Number(r?.status) === 1) healthy++; else error++;
  });
  return { healthy, warning, error };
}

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

  // 🔗 参数同步（仅在本页生效，不改你的 ClusterSwitcher）
  // 目标：如果只有 clusterId 或只有 detail，就把另一项也自动补齐，保持双兼容，且不会死循环。
  useEffect(() => {
    const next = new URLSearchParams(sp);
    let changed = false;
    if (clusterIdParam && clusterIdParam !== detailParam) {
      next.set('detail', clusterIdParam); changed = true;
    }
    if (!clusterIdParam && detailParam) {
      next.set('clusterId', detailParam); changed = true;
    }
    if (changed) setSp(next, { replace: true });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [clusterIdParam, detailParam]);

  // 详情
  const fetchDetail = async () => {
    if (!Number.isFinite(id)) return;
    setLoading(true);
    try {
      const res = await getCluster({ id: id as number });
      setCluster((res as any)?.data ?? res);
    } catch (e: any) {
      message.error(e?.message || '加载集群详情失败');
    } finally { setLoading(false); }
  };

  // 健康分布（按节点列表聚合）
  const fetchHealth = async () => {
    if (!Number.isFinite(id)) return;
    try {
      const resp = await getNodes({ clusterId: id as number }, { page: 1, size: 200 });
      const payload = (resp as any)?.data?.data ?? (resp as any)?.data ?? resp;
      const rows =
        (Array.isArray(payload?.data)    && payload.data) ||
        (Array.isArray(payload?.records) && payload.records) ||
        (Array.isArray(payload?.list)    && payload.list) ||
        (Array.isArray(payload) ? payload : []);
      setHealth(toBuckets(Array.isArray(rows) ? rows : []));
    } catch { setHealth({ healthy: 0, warning: 0, error: 0 }); }
  };

  useEffect(() => { fetchDetail(); fetchHealth(); }, [id]);          // id 变更自动刷新
  useEffect(() => { if (activeTab === 'status') fetchHealth(); }, [activeTab]);

  const basicInfo = useMemo(
    () => [
      ['类型', cluster?.type || '-'],
      ['地址', cluster?.endpoint || '-'],
      ['版本', cluster?.version || '-'],
      ['标签', cluster?.tags || '-'],
      ['备注', cluster?.remark || '-'],
      ['健康状态', cluster?.status === 1 ? <span className="text-green-600">在线</span> : <span className="text-gray-400">下线</span>],
      [
        '创建时间',
        cluster?.createdAt
          ? (Number.isFinite(cluster.createdAt)
              ? new Date(cluster.createdAt)
              : new Date(Date.parse(cluster.createdAt))
            ).toString() === 'Invalid Date'
              ? '-'
              : new Date(cluster.createdAt).toLocaleString()
          : '-',
      ],
    ],
    [cluster]
  );

  const onTabChange = useCallback((k: string) => {
    const next = new URLSearchParams(sp);
    next.set('tab', k);
    setSp(next, { replace: true });
  }, [sp, setSp]);

  if (!Number.isFinite(id)) {
    return (
      <div className="p-6">
        <Button type="link" onClick={() => navigate('/cluster/list')}>← 返回列表</Button>
        <div className="mt-6 text-red-500">缺少 detail/clusterId 参数。</div>
      </div>
    );
  }

  const items: TabsProps['items'] = [
    { key: 'nodes',  label: '节点明细', children: <NodeList clusterId={id as number} /> },
    { key: 'status', label: '节点状态', children: <NodeStatusPanel clusterId={id as number} onHealthChange={(h) => setHealth(h)} /> },
  ];

  // 扩容权限与弹窗（不动你的切换器 & 其它页面）
  const canExpand = hasPerm(useUser().userInfo, 'CLUSTER_EDIT', ['admin','ops']);
  const [expandOpen, setExpandOpen] = useState(false);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <Button type="link" onClick={() => navigate('/cluster/list')}>← 返回列表</Button>
        <Tooltip title={canExpand ? '' : '无扩容权限（需要 CLUSTER_EDIT 或管理员/运维）'}>
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
