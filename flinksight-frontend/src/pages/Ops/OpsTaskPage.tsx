// /src/pages/ops/OpsTaskPage.tsx
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Card, Segmented, Input, Space, Button, Tag, Popconfirm, Popover, Drawer, message } from 'antd';
import dayjs from 'dayjs';
import client from '@/api/client';
import { listOpsTasks, deleteOpsTask, runOpsTask } from '@/api/modules';
import type { OpsTaskDTO } from '@/api/dto';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

const STATUS_OPTIONS = [
  { label: '全部', value: 'ALL' },
  { label: '运行中', value: 'RUNNING' },
  { label: '成功', value: 'SUCCESS' },
  { label: '失败', value: 'FAILED' },
  { label: '暂停', value: 'PAUSED' },
] as const;

const DEFAULT_PAGE = 1;
const DEFAULT_SIZE = 30;

type TemplateDetail = {
  id: number;
  name: string;
  type?: string;
  content?: string;
  createTime?: string;
};

function extractPage<T = any>(resp: any) {
  const payload = resp?.data?.data ?? resp?.data ?? resp;
  const list: T[] = Array.isArray(payload)
    ? payload
    : Array.isArray(payload?.items) ? payload.items
    : Array.isArray(payload?.data)  ? payload.data
    : Array.isArray(payload?.list)  ? payload.list
    : [];
  const totalRaw =
    payload?.total ?? payload?.totalElements ?? payload?.totalCount ?? (Array.isArray(list) ? list.length : 0);
  return { list, total: Number(totalRaw) || 0, success: payload?.success, messageText: payload?.message };
}

const OpsTaskPage: React.FC = () => {
  const [tasks, setTasks] = useState<OpsTaskDTO[]>([]);
  const [total, setTotal] = useState(0);
  const [status, setStatus] = useState<string>('ALL');
  const [keyword, setKeyword] = useState<string>('');
  const [loading, setLoading] = useState(false);

  // —— 仅用于强制 rc-table 在可见后重算列宽 —— //
  const [tableKey, setTableKey] = useState(0);
  const wrapRef = useRef<HTMLDivElement | null>(null);
  useEffect(() => {
    // 首次挂载后异步 bump 一次，避免隐藏容器初始化拿到 0 宽
    const t = setTimeout(() => setTableKey(k => k + 1), 0);
    return () => clearTimeout(t);
  }, []);
  useEffect(() => {
    if (!wrapRef.current || !('ResizeObserver' in window)) return;
    const ro = new ResizeObserver(() => setTableKey(k => k + 1));
    ro.observe(wrapRef.current);
    return () => ro.disconnect();
  }, []);

  // 模板抽屉
  const [tplOpen, setTplOpen] = useState(false);
  const [tplLoading, setTplLoading] = useState(false);
  const [tpl, setTpl] = useState<TemplateDetail | null>(null);

  const fetchTasks = async () => {
    setLoading(true);
    try {
      const resp = await listOpsTasks({
        page: DEFAULT_PAGE,
        size: DEFAULT_SIZE,
        status,
        keyword: keyword?.trim() || undefined,
      });
      const { list, total, success, messageText } = extractPage<OpsTaskDTO>(resp);
      if (success === false) {
        message.error(messageText || '任务列表加载失败');
        setTasks([]); setTotal(0);
      } else {
        setTasks(Array.isArray(list) ? list : []);
        setTotal(total);
      }
    } catch (e: any) {
      message.error(e?.message || '任务列表加载失败');
      setTasks([]); setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchTasks(); /* eslint-disable-line */ }, [status]);

  const handleRun = async (id?: number) => {
    if (!id) return;
    setLoading(true);
    try { await runOpsTask(id); message.success('任务已触发'); fetchTasks(); }
    catch (e: any) { message.error(e?.message || '触发失败'); }
    finally { setLoading(false); }
  };

  const handleDelete = async (id?: number) => {
    if (!id) return;
    setLoading(true);
    try { await deleteOpsTask(id); message.success('删除成功'); fetchTasks(); }
    catch (e: any) { message.error(e?.message || '删除失败'); }
    finally { setLoading(false); }
  };

  const openTemplateDetail = async (templateId?: number | null) => {
    if (!templateId) return;
    setTplOpen(true); setTplLoading(true);
    try {
      const resp = await client.get(`/api/ops/template/${templateId}`, { withCredentials: true });
      const data = resp?.data?.data ?? resp?.data ?? resp;
      setTpl(data || null);
    } catch (e: any) {
      message.error(e?.message || '获取模板详情失败');
    } finally { setTplLoading(false); }
  };

  // —— 列定义：百分比宽度 + 左对齐 + 省略，合计约 100%，消除右侧空白 —— //
  const columns = useMemo(() => ([
    {
      key: 'name',
      title: '任务名',
      align: 'left' as const,
      width: '22%',
      ellipsis: true,
      render: (t: any) => <span className="font-medium">{t.taskName || t.name || '-'}</span>,
    },
    {
      key: 'templateName',
      title: '模板名',
      align: 'left' as const,
      width: '22%',
      ellipsis: true,
      render: (t: any) =>
        t.templateName ? (
          <Button type="link" onClick={() => openTemplateDetail(t.templateId)}>{t.templateName}</Button>
        ) : '-',
    },
    {
      key: 'templateType',
      title: '模板类型',
      align: 'left' as const,
      width: '14%',
      ellipsis: true,
      render: (t: any) => t.templateType || '-',
    },
    {
      key: 'status',
      title: '状态',
      align: 'left' as const,
      width: '10%',
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
      align: 'left' as const,
      width: '18%',
      ellipsis: true,
      render: (t: any) => {
        const ts = t.executedAt || t.runAt;
        return ts ? dayjs(ts).format('YYYY-MM-DD HH:mm:ss') : '-';
      },
    },
    {
      key: 'result',
      title: '执行结果',
      align: 'left' as const,
      width: '8%',
      render: (t: any) =>
        t.result || t.descriptionSummary ? (
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
      align: 'left' as const,
      width: '6%',
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
      {/* 本页作用域内的“吃满宽度”，不改 table-layout，不加 !important */}
      <style>{`
        .ops-fit .ant-table-wrapper,
        .ops-fit .ant-table-container,
        .ops-fit .ant-table,
        .ops-fit .ant-table-content { width: 100%; }
      `}</style>

      <Card className="shadow-sm ops-fit" bodyStyle={{ paddingTop: 12 }}>
        <div className="mb-3 flex items-center justify-between gap-3 flex-wrap">
          <Segmented options={STATUS_OPTIONS as any} value={status} onChange={(v) => setStatus(v as string)} />
          <Space size="middle" wrap>
            <Input.Search
              placeholder="按任务名/模板名/模板类型搜索"
              allowClear
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onSearch={() => fetchTasks()}
              style={{ width: 300 }}
            />
            <Button onClick={() => fetchTasks()}>刷新</Button>
            <Button type="primary">新建任务</Button>
          </Space>
        </div>

        <div ref={wrapRef} style={{ width: '100%' }}>
          <PageTable<OpsTaskDTO>
            key={tableKey}
            columns={columns as any}
            data={Array.isArray(tasks) ? tasks : []}
            loading={!!loading}
            page={DEFAULT_PAGE}
            size={DEFAULT_SIZE}
            total={typeof total === 'number' ? total : 0}
            emptyText="暂无运维任务"
          />
        </div>
      </Card>

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
            <pre style={{ maxHeight: 420, overflow: 'auto', background: '#0a0a0a', color: '#eaeaea', padding: 12, borderRadius: 8, fontSize: 12, lineHeight: 1.5 }}>
              {(tpl.content || '').slice(0, 4000) || '(无内容)'}
            </pre>
          </div>
        ) : '未找到模板'}
      </Drawer>

      {loading && <Loading />}
    </div>
  );
};

export default OpsTaskPage;
