import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Card, Segmented, Input, Space, Button, Tag, Popconfirm, Popover, Drawer, message } from 'antd';
import dayjs from 'dayjs';
import client from '@/api/client';

import { listOpsTasks, deleteOpsTask, runOpsTask } from '@/api/modules';
import type { OpsTaskDTO } from '@/api/dto';
import PageTable from '../../components/PageTable';
// import Loading from '../../components/Loading'; // 表格自带 loading，避免全屏遮罩

const STATUS_OPTIONS = [
  { label: '全部', value: 'ALL' },
  { label: '运行中', value: 'RUNNING' },
  { label: '成功', value: 'SUCCESS' },
  { label: '失败', value: 'FAILED' },
  { label: '暂停', value: 'PAUSED' },
] as const;

const DEFAULT_PAGE = 1;
const DEFAULT_SIZE = 30;
const TABLE_MIN_WIDTH = 900;

type TemplateDetail = {
  id: number;
  name: string;
  type?: string;
  content?: string;
  createTime?: string;
};

const OpsTaskPage: React.FC = () => {
  const [tasks, setTasks] = useState<OpsTaskDTO[]>([]);
  const [total, setTotal] = useState(0);
  const [status, setStatus] = useState<string>('ALL');
  const [keyword, setKeyword] = useState<string>('');
  const [loading, setLoading] = useState(false);

  // —— 重复请求防抖&并发保护（解决严格模式/多处触发导致的二次请求） —— //
  const lastKeyRef = useRef<string>('');
  const inflightRef = useRef<boolean>(false);

  // 模板详情 Drawer
  const [tplOpen, setTplOpen] = useState(false);
  const [tplLoading, setTplLoading] = useState(false);
  const [tpl, setTpl] = useState<TemplateDetail | null>(null);

  async function fetchTasks(force = false) {
    const k = `${status}|${keyword.trim()}|${DEFAULT_PAGE}|${DEFAULT_SIZE}`;
    if (!force) {
      if (lastKeyRef.current === k) return;     // 同参去重
      if (inflightRef.current) return;          // 正在请求中，避免并发
    }
    lastKeyRef.current = k;
    inflightRef.current = true;

    setLoading(true);
    try {
      const res = await listOpsTasks({
        page: DEFAULT_PAGE,
        size: DEFAULT_SIZE,
        status,
        keyword: keyword.trim() || undefined,
      });
      const body: any = (res && 'data' in res) ? res.data : res;
      if (body && body.success === false) throw new Error(body.message || '请求失败');

      const items: OpsTaskDTO[] = Array.isArray(body)
        ? body
        : (body?.items ?? body?.data ?? []);
      const ttl = body?.total ?? items?.length ?? 0;

      setTasks(items || []);
      setTotal(ttl);
    } catch (e: any) {
      message.error(e?.message || '任务列表加载失败');
      setTasks([]);
      setTotal(0);
    } finally {
      inflightRef.current = false;
      setLoading(false);
    }
  }

  async function handleRun(id: number) {
    if (!id) return;
    setLoading(true);
    try {
      await runOpsTask(id);
      message.success('任务已触发');
      await fetchTasks(true);
    } catch (e: any) {
      message.error(e?.message || '触发失败');
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id: number) {
    if (!id) return;
    setLoading(true);
    try {
      await deleteOpsTask(id);
      message.success('删除成功');
      await fetchTasks(true);
    } catch (e: any) {
      message.error(e?.message || '删除失败');
    } finally {
      setLoading(false);
    }
  }

  async function openTemplateDetail(templateId?: number | null) {
    if (!templateId) return;
    setTplOpen(true);
    setTplLoading(true);
    try {
      const resp = await client.get(`/api/ops/template/${templateId}`, { withCredentials: true });
      const data = resp?.data?.data ?? resp?.data;
      setTpl(data);
    } catch (e: any) {
      message.error(e?.message || '获取模板详情失败');
    } finally {
      setTplLoading(false);
    }
  }

  // 只有一个 effect：依赖 status（首次挂载也会跑一次），其余主动触发用 fetchTasks(true)
  useEffect(() => {
    fetchTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [status]);

  const columns = useMemo(() => ([
    {
      key: 'name',
      title: '任务名',
      width: 220,
      ellipsis: true,
      render: (t: any) => <span className="font-medium">{t.taskName || t.name || '-'}</span>,
    },
    {
      key: 'templateName',
      title: '模板名',
      width: 220,
      ellipsis: true,
      render: (t: any) =>
        t.templateName
          ? <Button type="link" onClick={() => openTemplateDetail(t.templateId)}>{t.templateName}</Button>
          : '-',
    },
    {
      key: 'templateType',
      title: '模板类型',
      width: 140,
      ellipsis: true,
      render: (t: any) => t.templateType || '-',
    },
    {
      key: 'status',
      title: '状态',
      width: 110,
      render: (t: any) => {
        const s = t.status || 'UNKNOWN';
        const color =
          s === 'SUCCESS' ? 'success' :
          s === 'FAILED'  ? 'error'   :
          s === 'RUNNING' ? 'processing' :
          s === 'PAUSED'  ? 'warning' : 'default';
        return <Tag color={color}>{s}</Tag>;
      },
    },
    {
      key: 'runAt',
      title: '执行时间',
      width: 180,
      render: (t: any) => {
        const ts = t.executedAt || t.runAt;
        return ts ? dayjs(ts).format('YYYY-MM-DD HH:mm:ss') : '-';
      },
    },
    {
      key: 'result',
      title: '执行结果',
      width: 120,
      render: (t: any) =>
        t.result || t.descriptionSummary
          ? (
            <Popover
              content={<pre className="max-w-[520px] whitespace-pre-wrap">{t.result ?? t.descriptionSummary}</pre>}
              trigger="click"
            >
              <Button type="link" size="small">查看</Button>
            </Popover>
          ) : '-',
    },
    {
      key: 'op',
      title: '操作',
      width: 160,
      render: (t: any) => (
        <Space size="small" wrap>
          <Button type="link" onClick={() => handleRun(t.taskId || t.id)}>执行</Button>
          <Popconfirm title="确认删除该任务？" onConfirm={() => handleDelete(t.taskId || t.id)}>
            <Button type="link" danger>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]), []);

  return (
    <div className="p-4 md:p-6">
      {/* 顶部工具条 */}
      <div className="mb-3 flex items-center justify-between gap-3 flex-wrap">
        <Space size="middle" wrap>
          <Segmented
            options={STATUS_OPTIONS as any}
            value={status}
            onChange={(v) => setStatus(v as string)}
          />
          <Input.Search
            placeholder="按任务名/模板名/模板类型搜索"
            allowClear
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onSearch={() => fetchTasks(true)}
            style={{ width: 300 }}
          />
        </Space>
        <Space size="middle" wrap>
          <Button onClick={() => fetchTasks(true)}>刷新</Button>
          <Button type="primary">新建任务</Button>
        </Space>
      </div>

      <Card className="shadow-sm" styles={{ body: { paddingTop: 12 } }}>
        {/* 让表格自己滚动，避免表头/内容错位（需要 PageTable 透传 scroll 和 tableLayout） */}
        <PageTable
          columns={columns as any}
          data={tasks}
          loading={loading}
          page={DEFAULT_PAGE}
          size={DEFAULT_SIZE}
          total={total}
          emptyText="暂无运维任务"
          tableLayout="fixed"      // PageTable 内部请转成 Table.tableLayout
          scrollX={TABLE_MIN_WIDTH} // PageTable 内部请转成 Table.scroll={{ x: TABLE_MIN_WIDTH }}
        />
      </Card>

      {/* 模板详情 Drawer */}
      <Drawer
        title={tpl?.name ? `模板：${tpl.name}` : '模板详情'}
        open={tplOpen}
        onClose={() => setTplOpen(false)}
        width={680}
      >
        {tplLoading ? '加载中…' : tpl ? (
          <div className="space-y-3">
            <div>类型：{tpl.type || '-'}</div>
            <div>创建时间：{tpl.createTime ? dayjs(tpl.createTime).format('YYYY-MM-DD HH:mm:ss') : '-'}</div>
            <div style={{ marginTop: 8 }}>脚本预览：</div>
            <pre
              style={{
                maxHeight: 420, overflow: 'auto',
                background: '#0a0a0a', color: '#eaeaea',
                padding: 12, borderRadius: 8, fontSize: 12, lineHeight: 1.5,
              }}
            >
              {(tpl.content || '').slice(0, 4000) || '(无内容)'}
            </pre>
          </div>
        ) : '未找到模板'}
      </Drawer>

      {/* 表格已经有 loading，这里不要再叠全屏遮罩以免“卡住”的观感 */}
      {/* {loading && <Loading />} */}
    </div>
  );
};

export default OpsTaskPage;
