// 只依赖现有 /api/modules，不新增任何模块
import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { Button, Card, Col, Row, Tabs, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
// import { useNavigate, useSearchParams } from 'react-router-dom';              // [KEEP]
import { useNavigate, useSearchParams, useParams } from 'react-router-dom';      // [FIX] 兼容 /:id 路由入参
import type { TabsProps } from 'antd';
// import { getCluster, getNodeHealth } from '@/api/modules';                    // [KEEP]
import { getCluster, getNodes } from '@/api/modules';                             // [FIX] 使用 OpenAPI 的正确方法
import NodeList from './NodeList';
import NodeStatusPanel from './NodeStatusPanel'; // [KEEP]

type Health = { healthy: number; warning: number; error: number };

// ====== 轻量无依赖环形图（SVG）======
const Donut: React.FC<{ healthy: number; warning: number; error: number }> = ({ healthy, warning, error }) => {
  const total = Math.max(0, Number(healthy) + Number(warning) + Number(error));
  const r = 46; const sw = 12; const C = 2 * Math.PI * r;
  const segs = total === 0
    ? [{ val: 1, color: '#d9d9d9' }]
    : [{ val: healthy, color: '#52c41a' }, { val: warning, color: '#faad14' }, { val: error, color: '#ff4d4f' }];
  let offset = 0;
  return (
    <svg width="120" height="120" viewBox="0 0 120 120" style={{ display: 'block' }}>
      <g transform="translate(60,60) rotate(-90)">
        {segs.map((s, i) => {
          const len = (s.val / (total || 1)) * C;
          const dash = `${len} ${C - len}`;
          const el = (
            <circle key={i} r={r} cx="0" cy="0" fill="none" stroke={s.color} strokeWidth={sw}
                    strokeDasharray={dash} strokeDashoffset={-offset}/>
          );
          offset += len;
          return el;
        })}
        <circle r={r} cx="0" cy="0" fill="none" stroke="#f0f0f0" strokeWidth={1} />
      </g>
    </svg>
  );
};

// [FIX] 把节点数组归并为 {healthy, warning, error}
function toBuckets(rows: any[]): Health {
  let healthy = 0, warning = 0, error = 0;
  rows.forEach((r) => {
    const hv = (r?.health ?? r?.status ?? '').toString().toLowerCase();
    const enabled = r?.enabled === true || r?.status === 1;
    if (hv === 'healthy' || hv === 'online' || enabled) healthy++;
    else if (hv === 'warning' || hv === 'degraded') warning++;
    else error++;
  });
  return { healthy, warning, error };
}

const ClusterDetail: React.FC = () => {
  const [sp, setSp] = useSearchParams();
  const navigate = useNavigate();
  const { id: idFromParams } = useParams<{ id?: string }>();              // [FIX]

  // ✅ 从 query 里拿 id（兼容 detail/clusterId 两个名字）
  // const id = Number(sp.get('detail') ?? sp.get('clusterId'));           // [KEEP]
  const _qId = Number(sp.get('detail') ?? sp.get('clusterId'));            // [FIX]
  const id = Number.isFinite(_qId) ? _qId : Number(idFromParams);          // [FIX] query 取不到时兜底用 :id

  const activeTab = sp.get('tab') || 'nodes';

  const [cluster, setCluster] = useState<any>(null);
  const [health, setHealth] = useState<Health>({ healthy: 0, warning: 0, error: 0 });
  const [loading, setLoading] = useState(false);

  const fetchDetail = async () => {
    if (!Number.isFinite(id)) return;                                      // [FIX]
    setLoading(true);
    try {
      // const res = await getCluster(id as number);                        // [KEEP] 错误：应为对象入参
      const res = await getCluster({ id: id as number });                   // [FIX] 正确用法
      setCluster((res as any)?.data ?? res);
    } catch (e: any) {
      message.error(e?.message || '加载集群详情失败');
    } finally {
      setLoading(false);
    }
  };

  // [FIX] 用 getNodes 拉一页数据做“节点健康分布”的聚合（后端没有集群维度健康接口时走这里）
  const fetchHealth = async () => {
    if (!Number.isFinite(id)) return;
    try {
      const query: any = { page: 1, size: 200 };                             // 取一页足够展示分布；要全量可放大 size
      const resp = await getNodes({ clusterId: id as number }, query);
      const payload = (resp as any)?.data?.data ?? (resp as any)?.data ?? resp;
      const rows =
        (Array.isArray(payload?.data)    && payload.data) ||
        (Array.isArray(payload?.records) && payload.records) ||
        (Array.isArray(payload?.list)    && payload.list) ||
        (Array.isArray(payload) ? payload : []);
      setHealth(toBuckets(Array.isArray(rows) ? rows : []));
    } catch {
      setHealth({ healthy: 0, warning: 0, error: 0 });
    }
  };

  useEffect(() => {
    fetchDetail();
    fetchHealth(); // [KEEP] 首次拉一次分布
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  // [FIX] 切换到“节点状态”Tab 时也刷新分布（保证左侧饼图立即更新）
  useEffect(() => {
    if (activeTab === 'status') fetchHealth();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab]);

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

  const onTabChange = useCallback((k: string) => {                          // [FIX]
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
    // 子组件把“当前页健康统计”回传，作为上述聚合的补充（切到“节点状态”也会驱动左侧饼图）
    { key: 'status', label: '节点状态', children: <NodeStatusPanel clusterId={id as number} onHealthChange={(h) => setHealth(h)} /> },
  ];

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <Button type="link" onClick={() => navigate('/cluster/list')}>← 返回列表</Button>
        <Button icon={<PlusOutlined />} disabled>节点扩容（占位）</Button>
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
            <div className="flex items-center space-x-4">
              <Donut healthy={health.healthy} warning={health.warning} error={health.error} />
              <div className="space-y-1">
                <div><span className="inline-block w-3 h-3 mr-2" style={{background:'#52c41a'}} />健康 {health.healthy}</div>
                <div><span className="inline-block w-3 h-3 mr-2" style={{background:'#faad14'}} />预警 {health.warning}</div>
                <div><span className="inline-block w-3 h-3 mr-2" style={{background:'#ff4d4f'}} />异常 {health.error}</div>
              </div>
            </div>
          </Card>
        </Col>

        <Col span={16}>
          <Card>
            {/* 旧实现保留为注释 // [KEEP]
            <Tabs
              activeKey={activeTab}
              destroyOnClose
              onChange={(k) => {
                const next = new URLSearchParams(sp);
                next.set('tab', k);
                setSp(next, { replace: true });
              }}
              items={items}
            />
            */}
            <Tabs activeKey={activeTab} onChange={onTabChange} items={items} /> {/* [FIX] */}
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default ClusterDetail;
