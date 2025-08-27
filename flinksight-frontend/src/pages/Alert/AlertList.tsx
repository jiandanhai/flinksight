/**
 * @file 报警流（Alert）列表 — 稳健分页解析修正版
 * - antd 1-based → 后端 0-based：向后端传 page-1
 * - status 必传：未选择时传 -1（表示“全部”，与后端约定一致）
 * - 解析 PageResult 时：仅在外层是 { success, data } 时才解壳；否则直接使用响应本体
 */

import React, { useEffect, useMemo, useState } from 'react';
import {
  Button,
  Checkbox,
  Dropdown,
  Input,
  message,
  Modal,
  Select,
  Space,
  Table,
  Tag,
} from 'antd';
import {
  listAlerts,
  deleteAlert,
  deleteBatch,
  updateAlert,
  exportAlerts,
} from '@/api/modules';
import type { AlertDTO } from '@/api/dto';
import EditAlertModal from './EditAlertModal';
import AlertDetail from './AlertDetail';
import Loading from '../../components/Loading';
import ExportButton from '../../components/common/ExportButton';
import { useUser } from '../../store/user';

const { Search } = Input;
const { Option } = Select;

// 级别显示
const LEVEL_LABEL = ['未知', '低', '中', '高', '致命'];
const LEVEL_COLOR = ['', 'blue', 'orange', 'red', 'purple'];

// 默认列
const DEFAULT_COLUMNS = [
  { key: 'name', title: '名称' },
  { key: 'level', title: '级别' },
  { key: 'status', title: '状态' },
  { key: 'createTime', title: '创建时间' },
  { key: 'op', title: '操作' },
];

/** 稳健解析：仅当外层是 {success, data} 时才“解壳”；否则直接用响应本体 */
function parsePageResult(tag: string, resp: any) {
  // 1) 判断是否为“包壳结构”
  const isWrapped = resp && typeof resp === 'object'
    && ('success' in resp || 'code' in resp || 'message' in resp)
    && 'data' in resp;

  // 2) pageObj：分页对象
  const pageObj = isWrapped ? resp.data : resp;

  // 3) 如果 pageObj 还是包了一层（极少数后端会再包一次）
  const page =
    pageObj && typeof pageObj === 'object' && ('total' in pageObj || 'records' in pageObj || 'data' in pageObj)
      ? pageObj
      : resp; // 兜底直接用 resp

  // 4) 解析 rows & total
  let rows: any[] = [];
  let total = 0;

  if (page && typeof page === 'object') {
    if ('total' in page && 'data' in page && Array.isArray(page.data)) {
      // 标准 PageResult：{ total, page, size, pages, data: [...] }
      rows = page.data;
      total = Number(page.total) || 0;
    } else if ('total' in page && 'records' in page && Array.isArray(page.records)) {
      // 另一种常见命名：{ total, records: [...] }
      rows = page.records;
      total = Number(page.total) || 0;
    } else if (Array.isArray(page)) {
      // 极端情况：直接返回数组（无分页）——把数组当作数据源
      rows = page;
      total = page.length;
    } else if ('content' in page && Array.isArray(page.content)) {
      rows = page.content;
      total = Number(page.totalElements ?? page.total ?? rows.length) || rows.length;
    } else if ('items' in page && Array.isArray(page.items)) {
      rows = page.items;
      total = Number(page.total ?? rows.length) || rows.length;
    } else if ('list' in page && Array.isArray(page.list)) {
      rows = page.list;
      total = Number(page.total ?? rows.length) || rows.length;
    } else if ('data' in page && Array.isArray(page.data)) {
      // 注意：只有当 page 自身带 total 时才是分页；否则当作“非分页数组”
      rows = page.data;
      total = Number(page.total ?? rows.length) || rows.length;
    }
  }

  console.groupCollapsed(`[AlertList] ${tag} · parsed`);
  console.log('isWrapped =', isWrapped);
  console.log('resp keys:', resp ? Object.keys(resp) : null);
  console.log('page keys:', page ? (Array.isArray(page) ? ['<array>'] : Object.keys(page)) : null);
  console.log('rows.length =', rows.length, ' total =', total);
  if (rows.length) console.log('first row =', rows[0]);
  console.groupEnd();

  return { rows, total };
}

const AlertList: React.FC = () => {
  const { role } = useUser();
  const canEdit = role !== 'user';

  // antd 分页是 1-based
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);

  const [alerts, setAlerts] = useState<AlertDTO[]>([]);
  const [loading, setLoading] = useState(false);

  const [query, setQuery] = useState<Partial<AlertDTO>>({});
  const [level, setLevel] = useState<number | undefined>(undefined);
  const [status, setStatus] = useState<number | undefined>(undefined); // 未选时传 -1 给后端

  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);

  // 表头配置
  const [columnsSetting, setColumnsSetting] = useState<string[]>(
    DEFAULT_COLUMNS.map((c) => c.key),
  );

  // —— 拉取列表 —— //
  const fetchAlerts = async () => {
    setLoading(true);
    try {
      const req = {
        ...query,
        status: status ?? -1,          // 关键：后端需要始终带上 status；-1 表示全部
        level,
        page: Math.max(0, page - 1),   // 关键：后端 0-based
        size,
      };
      console.log('[AlertList] fetch params', req);

      const res = await listAlerts(req as any);
      console.log('[AlertList] raw resp', res);

      // 处理失败返回
      if (res && typeof res === 'object' && 'success' in res && res.success === false) {
        message.error((res as any)?.message || '获取数据失败');
        setAlerts([]);
        setTotal(0);
        return;
      }

      const { rows, total } = parsePageResult('listAlerts', res);
      setAlerts(rows as AlertDTO[]);
      setTotal(total);
    } catch (e) {
      console.error('[AlertList] fetch error', e);
      message.error('请求失败，请看控制台日志');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAlerts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, size, level, status, JSON.stringify(query)]);

  // —— 搜索 —— //
  const handleSearch = (val: string) => {
    setQuery((q) => ({ ...q, keyword: val }));
    setPage(1);
  };

  // —— 详情 —— //
  if (detailId) {
    return <AlertDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  // —— 删除（批量） —— //
  async function handleDelete(ids: number[]) {
    if (!ids.length) return;
    Modal.confirm({
      title: `确认删除${ids.length > 1 ? ids.length + '个' : ''}报警流？`,
      onOk: async () => {
        setLoading(true);
        try {
          if (ids.length === 1) await deleteAlert(ids[0] as any);
          else await deleteBatch(ids as any);
          message.success('删除成功');
          setSelectedRowKeys([]);
          fetchAlerts();
        } finally {
          setLoading(false);
        }
      },
    });
  }

  // —— 状态切换 —— //
  async function handleStatusChange(a: AlertDTO) {
    const next = a.status === 1 ? 0 : 1;
    await updateAlert(a.id as any, { status: next } as any);
    message.success(next === 1 ? '已激活' : '已关闭');
    fetchAlerts();
  }

  // —— 导出 —— //
  async function handleExport() {
    const p = { ...query, level, status: status ?? -1 };
    console.log('[AlertList] export params', p);
    await exportAlerts(p as any);
    message.success('导出请求已提交');
  }

  // —— 表头配置菜单 —— //
  const menuItems: MenuProps['items'] = DEFAULT_COLUMNS.map((col) => ({
    key: col.key,
    label: (
      <Checkbox
        checked={columnsSetting.includes(col.key)}
        onChange={(e) =>
          setColumnsSetting((cols) =>
            e.target.checked
              ? [...cols, col.key]
              : cols.filter((k) => k !== col.key),
          )
        }
      >
        {col.title}
      </Checkbox>
    ),
  }));

  const columnsConfig = useMemo(
    () => DEFAULT_COLUMNS.filter((c) => columnsSetting.includes(c.key)),
    [columnsSetting],
  );

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      {/* 工具栏 */}
      <div className="mb-4 flex flex-wrap justify-between items-center gap-2">
        <Space wrap>
          <Search
            placeholder="报警流名称/规则关键字"
            allowClear
            enterButton
            onSearch={handleSearch}
            style={{ width: 240 }}
          />
          <Select
            placeholder="级别"
            allowClear
            value={level}
            onChange={(v) => { setLevel(v); setPage(1); }}
            style={{ width: 120 }}
          >
            {[1, 2, 3, 4].map((l) => (
              <Option key={l} value={l}>
                {LEVEL_LABEL[l]}
              </Option>
            ))}
          </Select>
          <Select
            placeholder="状态（未选=全部）"
            allowClear
            value={status}
            onChange={(v) => { setStatus(v); setPage(1); }}
            style={{ width: 140 }}
          >
            <Option value={1}>激活(1)</Option>
            <Option value={0}>关闭(0)</Option>
          </Select>
        </Space>
        <Space wrap>
          <ExportButton onClick={handleExport} disabled={!alerts.length} />
          <Dropdown menu={{ items: menuItems }}>
            <Button>表头配置</Button>
          </Dropdown>
          <Button
            type="primary"
            onClick={() => { setEditId(null); setModalVisible(true); }}
            disabled={!canEdit}
          >
            新建报警流
          </Button>
          <Button
            danger
            disabled={!selectedRowKeys.length}
            onClick={() => handleDelete(selectedRowKeys)}
          >
            批量删除
          </Button>
        </Space>
      </div>

      {/* 表格 */}
      <Table<AlertDTO>
        rowKey="id"
        dataSource={alerts}
        loading={loading}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[]),
        }}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => {
            setPage(p);
            setSize(s || size);
          },
        }}
        columns={columnsConfig.map((col) => {
          switch (col.key) {
            case 'name':
              return {
                title: '名称',
                dataIndex: 'name',
                render: (_: any, a: AlertDTO) => (
                  <span
                    className="text-blue-600 cursor-pointer"
                    onClick={() => setDetailId(a.id as number)}
                  >
                    {a.name}
                  </span>
                ),
              };
            case 'level':
              return {
                title: '级别',
                dataIndex: 'level',
                render: (v: number) => (
                  <Tag color={LEVEL_COLOR[v] || 'gray'}>
                    {LEVEL_LABEL[v] || '未知'}
                  </Tag>
                ),
              };
            case 'status':
              return {
                title: '状态',
                dataIndex: 'status',
                render: (_: any, a: AlertDTO) => (
                  <Button
                    type={a.status === 1 ? 'link' : 'default'}
                    size="small"
                    onClick={() => handleStatusChange(a)}
                  >
                    {a.status === 1 ? '已激活' : '关闭'}
                  </Button>
                ),
              };
            case 'createTime':
              return {
                title: '创建时间',
                dataIndex: 'createdAt', // 如果后端字段是 createdAt，请改为 'createdAt'
                render: (v: any) => (v ? new Date(v).toLocaleString() : '-'),
              };
            case 'op':
              return {
                title: '操作',
                render: (_: any, a: AlertDTO) => (
                  <Space>
                    <Button
                      type="link"
                      size="small"
                      onClick={() => { setEditId(a.id as number); setModalVisible(true); }}
                      disabled={!canEdit}
                    >
                      编辑
                    </Button>
                    <Button
                      type="link"
                      size="small"
                      danger
                      onClick={() => handleDelete([a.id as number])}
                      disabled={!canEdit}
                    >
                      删除
                    </Button>
                  </Space>
                ),
              };
            default:
              return {} as any;
          }
        })}
      />

      {/* 弹窗 */}
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
