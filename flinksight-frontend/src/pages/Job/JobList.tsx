/**
 * @file 任务/作业管理页
 * @desc 支持任务查询、分页、批量启停、详情、编辑弹窗，权限自动校验
 */
import React, { useEffect, useState } from 'react';
import { Table, Button, Input, Tag, Space, Modal, message } from 'antd';
import { getJobs, updateJob, deleteJob, batchUpdateJobStatus } from '../../api/job';
import type { Job, JobQuery } from '../../types/job';
import EditJobModal from './EditJobModal';
import JobDetail from './JobDetail';
import { useUser } from '../../store/user';

const { Search } = Input;

const JobList: React.FC = () => {
  const [list, setList] = useState<Job[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<JobQuery>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin' || role === 'ops';

  // 拉取任务列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await getJobs({ ...query, page, size });
      setList(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [query, page, size]);

  // 搜索
  const handleSearch = (val: string) => {
    setQuery({ ...query, keyword: val });
    setPage(1);
  };

  // 编辑弹窗
  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  // 详情
  if (detailId) {
    return <JobDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该任务？',
      onOk: async () => {
        await deleteJob(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  // 批量启用/停用
  async function handleBatchEnable(enable: boolean) {
    await batchUpdateJobStatus(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已停用');
    setSelectedRowKeys([]);
    fetch();
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="任务名/关键字" allowClear enterButton onSearch={handleSearch} style={{ width: 320 }} />
        <Space>
          <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建任务</Button>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量停用</Button>
        </Space>
      </div>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[])
        }}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => { setPage(p); setSize(s); }
        }}
        columns={[
          {
            title: '任务名',
            dataIndex: 'name',
            render: (v, t) => <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId(t.id)}>{v}</span>
          },
          { title: '负责人', dataIndex: 'owner' },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">停用</Tag> },
          { title: '调度周期', dataIndex: 'cron' },
          { title: '上次运行', dataIndex: 'lastRun', render: v => v && new Date(v).toLocaleString() },
          { title: '运行结果', dataIndex: 'lastResult' },
          {
            title: '操作',
            render: (_: any, t: Job) => (
              <Space>
                <Button type="link" size="small" onClick={() => openModal(t.id)} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(t.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
      />
      {modalVisible && (
        <EditJobModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default JobList;
