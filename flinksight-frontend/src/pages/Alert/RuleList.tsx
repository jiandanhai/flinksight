/**
 * 规则列表（关键日志 + 0-based 安全分页 + 正确解析 PageResult）
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, message, Modal, Space, Table, Tag } from 'antd';
import {
  listAlertRules,
  deleteAlertRule,
  deleteBatch,
  enableOne,
} from '@/api/modules';
import type { ColumnsType } from 'antd/es/table';
import type { AlertRuleDTO } from '@/api/dto';
import EditRuleModal from './EditRuleModal';
import { useUser } from '../../store/user';

interface Props {
  clusterId?: number;
  tenantId?: number;
  alertId?: number;
  onRulesChange?: () => void;
}

const LEVEL_MAP = ['未知', '低', '中', '高', '致命'];

const RuleList: React.FC<Props> = ({ clusterId, tenantId, alertId, onRulesChange }) => {
  const { role } = (useUser() as any) || {};
  const canEdit = useMemo(() => role !== 'user', [role]);

  // ---- 0-based 分页 ----
  const [pageIdx, setPageIdx] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [total, setTotal] = useState(0);

  // ---- 数据 ----
  const [list, setList] = useState<AlertRuleDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  /** 正确解析：SDK 已返回 PageResult 本体（含 total/page/size/pages/data[]） */
  function parsePageResult(tag: string, resp: any) {
    // 这一步非常关键：resp 就是 PageResult（不是 {success, data}）
    const hasPageShape = resp && typeof resp === 'object' && 'total' in resp && 'data' in resp;
    const rows = hasPageShape && Array.isArray(resp.data) ? resp.data : [];
    const totalVal = hasPageShape && typeof resp.total === 'number' ? resp.total : 0;

    console.groupCollapsed(`[RuleList] ${tag} (parsed)`);
    console.log('resp keys:', resp ? Object.keys(resp) : null);
    console.log('rows.length =', rows.length, 'total =', totalVal);
    if (rows.length) console.log('first row =', rows[0]);
    console.groupEnd();

    return { rows, total: totalVal };
  }

  async function fetchList() {
    setLoading(true);
    try {
      const base = { page: pageIdx, size: pageSize };
      console.groupCollapsed('[RuleList] fetch params');
      console.log({ clusterId, tenantId, ...base });
      console.groupEnd();

      let res: any;
      if (clusterId != null) {
        // SDK 已经把响应体解包成 PageResult
        res = await listAlertRules({ ...base, tenantId, clusterId } as any);
        const { rows, total } = parsePageResult('SDK listByTenantAndCluster', res);
        setList(rows as AlertRuleDTO[]);
        setTotal(total);
      } else {
        res = await listAlertRules(base as any);
        const { rows, total } = parsePageResult('SDK listByTenant', res);
        setList(rows as AlertRuleDTO[]);
        setTotal(total);
      }
    } catch (e) {
      console.error('[RuleList] fetch error', e);
      setList([]); setTotal(0);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchList(); /* eslint-disable-next-line */ }, [pageIdx, pageSize, clusterId]);

  async function handleEnable(rule: AlertRuleDTO) {
    const currentEnabled = (rule as any).enable === 1 || (rule as any).enable === true;
    console.log('[RuleList] enableOne params =', { id: rule.id, value: !currentEnabled });
    await enableOne({ id: rule.id as number, value: !currentEnabled } as any);
    message.success(currentEnabled ? '已停用' : '已启用');
    fetchList(); onRulesChange?.();
  }

  async function handleDelete(ids: number[]) {
    console.log('[RuleList] delete ids =', ids);
    Modal.confirm({
      title: `确认删除${ids.length > 1 ? ids.length + '条' : ''}规则？`,
      onOk: async () => {
        if (ids.length === 1) await deleteAlertRule(ids[0] as any);
        else await deleteBatch(ids as any);
        message.success('删除成功');
        setSelectedRowKeys([]); fetchList(); onRulesChange?.();
      },
    });
  }

  const columns: ColumnsType<AlertRuleDTO> = [
    { title: 'ID', dataIndex: 'id', width: 90 },
    { title: '名称', dataIndex: 'name' },
    { title: '指标Key', dataIndex: 'metricKey' },
    { title: '阈值', dataIndex: 'threshold' },
    { title: '比较符', dataIndex: 'compareOp' },
    { title: '通知渠道', dataIndex: 'channel' },
    {
      title: '状态',
      dataIndex: 'enable',
      render: (v: any) => (v === 1 || v === true ? <Tag color="green">启用</Tag> : <Tag>停用</Tag>),
    },
    { title: '创建时间', dataIndex: 'createdAt', render: (v?: string) => (v ? new Date(v).toLocaleString() : '-') },
    {
      title: '操作',
      width: 180,
      render: (_: any, r: AlertRuleDTO) => (
        <Space>
          <Button type="link" size="small"
            onClick={() => { setEditId(r.id as number); setModalVisible(true); }}
            disabled={!canEdit}
          >编辑</Button>
          <Button type="link" size="small" onClick={() => handleEnable(r)} disabled={!canEdit}>
            {(r as any).enable ? '停用' : '启用'}
          </Button>
          <Button type="link" size="small" danger onClick={() => handleDelete([r.id as number])} disabled={!canEdit}>
            删除
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between mb-2">
        <Button type="primary" onClick={() => { setEditId(null); setModalVisible(true); }} disabled={!canEdit}>
          新建规则
        </Button>
        <Button danger disabled={!selectedRowKeys.length} onClick={() => handleDelete(selectedRowKeys)}>
          批量删除
        </Button>
      </div>

      <Table<AlertRuleDTO>
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={columns}
        rowSelection={{ selectedRowKeys, onChange: (keys) => setSelectedRowKeys(keys as number[]) }}
        pagination={{
          current: pageIdx + 1,
          pageSize,
          total,
          showSizeChanger: true,
          onChange: (cur, ps) => {
            const nextIdx = Math.max(0, (cur ?? 1) - 1);
            console.log('[RuleList] onChange -> ', { current: cur, pageIdx: nextIdx, pageSize: ps });
            setPageIdx(nextIdx);
            setPageSize(ps ?? pageSize);
          },
        }}
      />

      {modalVisible && (
        <EditRuleModal
          ruleId={editId}
          alertId={alertId as number}
          onOk={() => { setModalVisible(false); fetchList(); onRulesChange?.(); }}
          onCancel={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};

export default RuleList;
