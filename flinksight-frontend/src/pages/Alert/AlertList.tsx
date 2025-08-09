/**
 * @file 报警流表格页面 - 企业级全功能增强
 * @desc 支持分页、批量、搜索、详情、新建/编辑/删除/权限、级别筛选、导出、状态管理、表头配置、列自定义
 */
import React, {useEffect, useMemo, useState} from 'react';
import {Button, Checkbox, Dropdown, Input, Menu, message, Modal, Select, Space, Table, Tag} from 'antd';
import { api } from 'src/api/gen/client';

import type {AlertDTO} from '../../api/gen/data-contracts.ts';
import EditAlertModal from './EditAlertModal';
import AlertDetail from './AlertDetail';
import Loading from '../../components/Loading';
import ExportButton from '../../components/common/ExportButton';
import {useUser} from '../../store/user';

const { Search } = Input;
const { Option } = Select;

// 报警等级常量映射
const ALERT_LEVEL = ['未知', '低', '中', '高', '致命'];
const ALERT_LEVEL_COLOR = ['', 'blue', 'orange', 'red', 'purple'];

// 默认表头配置
const DEFAULT_COLUMNS = [
  { key: 'name', title: '名称' },
  { key: 'level', title: '级别' },
  { key: 'status', title: '状态' },
  { key: 'createTime', title: '创建时间' },
  { key: 'op', title: '操作' },
];

const AlertList: React.FC = () => {
  const [alerts, setAlerts] = useState<AlertDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<AlertDTO>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  // 级别、状态筛选
  const [level, setLevel] = useState<number | undefined>(undefined);
  const [status, setStatus] = useState<number | undefined>(undefined);
  // 列自定义
  const [columnsSetting, setColumnsSetting] = useState<string[]>(DEFAULT_COLUMNS.map(col => col.key));

  /** 拉取报警流数据 */
  const fetchAlerts = async () => {
    setLoading(true);
    try {
      const res = await api.getAlertsByLevelAndStatus({ ...query, page, size, level, status });
      setAlerts(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetchAlerts(); }, [query, page, size, level, status]);

  /** 搜索报警流名称/规则关键字 */
  const handleSearch = (val: string) => {
    setQuery({ ...query, keyword: val });
    setPage(1);
  };

  /** 打开新建/编辑弹窗 */
  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  /** 详情页打开 */
  if (detailId) {
    return <AlertDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  /** 删除报警流（支持批量，含权限） */
  async function handleDelete(ids: number[]) {
    if (!ids.length) return;
    Modal.confirm({
      title: `确认删除${ids.length > 1 ? ids.length + '个' : ''}报警流？`,
      onOk: async () => {
        setLoading(true);
        try {
          if (ids.length === 1) {
            await api.deleteAlert(ids[0]);
          } else {
            await deleteAlerts(ids);
          }
          message.success('删除成功');
          setSelectedRowKeys([]);
          fetchAlerts();
        } finally {
          setLoading(false);
        }
      }
    });
  }

  /** 状态管理（激活/关闭切换，持久化） */
  async function handleStatusChange(alert: Alert) {
    await api.updateAlert(alert.id, { status: alert.status === 1 ? 0 : 1 });
    message.success(alert.status === 1 ? '已关闭' : '已激活');
    fetchAlerts();
  }

  /** 导出报警流 */
  async function handleExport() {
    await api.exportAlerts({ ...query, level, status });
    message.success('导出请求已提交');
  }

  /** 表头配置 - 列自定义 */
  const columnsConfig = useMemo(() => DEFAULT_COLUMNS.filter(c => columnsSetting.includes(c.key)), [columnsSetting]);
  const canEdit = role !== 'user';

  const menu = (
    <Menu>
      {DEFAULT_COLUMNS.map(col => (
        <Menu.Item key={col.key}>
          <Checkbox
            checked={columnsSetting.includes(col.key)}
            onChange={e =>
              setColumnsSetting(cols =>
                e.target.checked
                  ? [...cols, col.key]
                  : cols.filter(k => k !== col.key)
              )
            }
          >{col.title}</Checkbox>
        </Menu.Item>
      ))}
    </Menu>
  );

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="mb-4 flex flex-wrap justify-between items-center gap-2">
        <Space>
          <Search
            placeholder="报警流名称/规则关键字"
            allowClear enterButton onSearch={handleSearch}
            style={{ width: 220 }}
          />
          <Select
            placeholder="级别"
            allowClear
            value={level}
            onChange={v => setLevel(v)}
            style={{ width: 100 }}
          >
            {[1, 2, 3, 4].map(l => <Option value={l} key={l}>{ALERT_LEVEL[l]}</Option>)}
          </Select>
          <Select
            placeholder="状态"
            allowClear
            value={status}
            onChange={v => setStatus(v)}
            style={{ width: 100 }}
          >
            <Option value={1}>激活</Option>
            <Option value={0}>关闭</Option>
          </Select>
        </Space>
        <Space>
          <ExportButton onClick={handleExport} disabled={!alerts.length} />
          <Dropdown overlay={menu} trigger={['click']}><Button>表头配置</Button></Dropdown>
          <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建报警流</Button>
          <Button danger disabled={!selectedRowKeys.length} onClick={() => handleDelete(selectedRowKeys)}>批量删除</Button>
        </Space>
      </div>
      <Table
        rowKey="id"
        dataSource={alerts}
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
        columns={columnsConfig.map(col => {
          switch (col.key) {
            case 'name':
              return {
                title: '名称',
                dataIndex: 'name',
                render: (_: any, a: Alert) => (
                  <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId(a.id)}>{a.name}</span>
                ),
              };
            case 'level':
              return {
                title: '级别',
                dataIndex: 'level',
                render: (v: number) => <Tag color={ALERT_LEVEL_COLOR[v] || 'gray'}>{ALERT_LEVEL[v] || '未知'}</Tag>
              };
            case 'status':
              return {
                title: '状态',
                dataIndex: 'status',
                render: (_: any, a: Alert) => (
                  <Button
                    type={a.status === 1 ? 'link' : 'default'}
                    size="small"
                    onClick={() => handleStatusChange(a)}
                  >{a.status === 1 ? '激活' : '关闭'}</Button>
                )
              };
            case 'createTime':
              return {
                title: '创建时间',
                dataIndex: 'createTime',
                render: (v: string) => new Date(v).toLocaleString(),
              };
            case 'op':
              return {
                title: '操作',
                render: (_: any, a: Alert) => (
                  <Space>
                    <Button
                      type="link"
                      size="small"
                      onClick={() => openModal(a.id)}
                      disabled={!canEdit}
                    >编辑</Button>
                    <Button
                      type="link"
                      size="small"
                      danger
                      disabled={!canEdit}
                      onClick={() => handleDelete([a.id])}
                    >删除</Button>
                  </Space>
                )
              };
            default:
              return {};
          }
        })}
      />
      {/* 编辑弹窗 */}
      {modalVisible && (
        <EditAlertModal
          id={editId}
          onClose={() => setModalVisible(false)}
          onOk={fetchAlerts}
        />
      )}
      {loading && <Loading />}
    </div>
  );
};
export default AlertList;
