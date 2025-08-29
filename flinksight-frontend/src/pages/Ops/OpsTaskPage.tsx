// /src/pages/ops/OpsTaskPage.tsx
import React, { useEffect, useMemo, useState } from 'react';
import { Card, Segmented, Input, Space, Button, Tag, Popconfirm, Popover, message } from 'antd';
import dayjs from 'dayjs';

import { listOpsTasks, deleteOpsTask, runOpsTask } from '@/api/modules';
import type { OpsTaskDTO } from '@/api/dto';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

/**
 * 运维自动化任务
 * - 修复：后端要求必传 status；默认 ALL
 * - 支持：keyword 模糊查询
 * - 美化：Card 容器、工具条、状态 Tag、时间格式、结果 Popover、删除二次确认
 */

const STATUS_OPTIONS = [
  { label: '全部', value: 'ALL' },
  { label: '运行中', value: 'RUNNING' },
  { label: '成功', value: 'SUCCESS' },
  { label: '失败', value: 'FAILED' },
  { label: '暂停', value: 'PAUSED' },
] as const;

const DEFAULT_PAGE = 1;
const DEFAULT_SIZE = 30;

const OpsTaskPage: React.FC = () => {
  const [tasks, setTasks] = useState<OpsTaskDTO[]>([]);
  const [total, setTotal] = useState(0);
  const [status, setStatus] = useState<string>('ALL');        // ✅ 必传
  const [keyword, setKeyword] = useState<string>('');         // 可选
  const [loading, setLoading] = useState(false);

  async function fetchTasks() {
    setLoading(true);
    try {
      const res = await listOpsTasks({
        page: DEFAULT_PAGE,
        size: DEFAULT_SIZE,
        status,                                 // ✅ 关键：后端必传
        keyword: keyword?.trim() || undefined,  // 可选
      });

      // —— 统一解包：兼容 {success,data:{items,total}} / {items,total} / 数组 —— //
      const body: any = (res && 'data' in res) ? res.data : res;
      if (body && body.success === false) {
        throw new Error(body.message || '请求失败');
      }
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
      setLoading(false);
    }
  }

  async function handleRun(id: number) {
    setLoading(true);
    try {
      await runOpsTask(id);             // ✅ 统一使用 modules 中的 runOpsTask
      message.success('任务已触发');
      fetchTasks();
    } catch (e: any) {
      message.error(e?.message || '触发失败');
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id: number) {
    setLoading(true);
    try {
      await deleteOpsTask(id);
      message.success('删除成功');
      fetchTasks();
    } catch (e: any) {
      message.error(e?.message || '删除失败');
    } finally {
      setLoading(false);
    }
  }

  // 首次 + status 变化时刷新；搜索通过按钮/回车触发
  useEffect(() => { fetchTasks(); /* eslint-disable-next-line */ }, [status]);

  const columns = useMemo(() => ([
    { key: 'name', title: '任务名', render: (t: OpsTaskDTO) => <span className="font-medium">{t.name || '-'}</span> },
    { key: 'script', title: '脚本', render: (t: OpsTaskDTO) => <code className="text-xs">{t.script || '-'}</code> },
    {
      key: 'status', title: '状态',
      render: (t: OpsTaskDTO) => {
        const s = t.status || 'UNKNOWN';
        const color =
          s === 'SUCCESS' ? 'success' :
          s === 'FAILED'  ? 'error'   :
          s === 'RUNNING' ? 'processing' :
          s === 'PAUSED'  ? 'warning' :
          'default';
        return <Tag color={color}>{s}</Tag>;
      }
    },
    { key: 'runBy', title: '执行人', render: (t: OpsTaskDTO) => t.runBy || '-' },
    {
      key: 'runAt', title: '执行时间',
      render: (t: OpsTaskDTO) => t.runAt ? dayjs(t.runAt).format('YYYY-MM-DD HH:mm:ss') : '-'
    },
    {
      key: 'result', title: '执行结果',
      render: (t: OpsTaskDTO) => (
        t.result
          ? <Popover content={<pre className="max-w-[520px] whitespace-pre-wrap">{t.result}</pre>} trigger="click">
              <Button type="link" size="small">查看</Button>
            </Popover>
          : '-'
      )
    },
    {
      key: 'op', title: '操作',
      render: (t: OpsTaskDTO) => (
        <Space size="small">
          <Button type="link" onClick={() => handleRun(t.id)}>执行</Button>
          <Popconfirm title="确认删除该任务？" onConfirm={() => handleDelete(t.id)}>
            <Button type="link" danger>删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ]), []);

  return (
    <div className="p-4 md:p-6">
      <Card
        className="shadow-sm"
        title={<div className="text-base md:text-lg font-semibold">运维自动化任务</div>}
        extra={
          <Space size="middle" wrap>
            <Segmented
              options={STATUS_OPTIONS as any}
              value={status}
              onChange={(v) => setStatus(v as string)}
            />
            <Input.Search
              placeholder="按任务名/脚本搜索"
              allowClear
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onSearch={() => fetchTasks()}
              style={{ width: 280 }}
            />
            <Button onClick={() => fetchTasks()}>刷新</Button>
            <Button type="primary">新建任务</Button>
          </Space>
        }
        bodyStyle={{ paddingTop: 16 }}
      >
        <PageTable<OpsTaskDTO>
          columns={columns}
          data={tasks}
          loading={loading}
          page={DEFAULT_PAGE}
          size={DEFAULT_SIZE}
          total={total}
          emptyText="暂无运维任务"
        />
      </Card>

      {loading && <Loading />}
    </div>
  );
};

export default OpsTaskPage;
