/**
 * @file 报警流规则列表
 * @desc 展示单报警流下所有规则，支持启停/编辑/删除/批量，自动对接API/types，权限、交互、注释齐全
 */
import React, {useEffect, useState} from 'react';
import {Button, message, Modal, Space, Table, Tag} from 'antd';
import  api  from 'src/api/gen/client';

import type {AlertRuleDTO} from '../../api/gen/data-contracts.ts';
import EditRuleModal from './EditRuleModal';
import {useUser} from '../../store/user';

interface Props {
  alertId: number;
  onRulesChange?: () => void;
}

const LEVEL_MAP = ['未知', '低', '中', '高', '致命'];

const RuleList: React.FC<Props> = ({ alertId, onRulesChange }) => {
  const [list, setList] = useState<AlertRuleDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const { role } = useUser();

  // 拉取规则列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAlertRuleByRule(alertId);
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [alertId]);

  // 启停
  async function handleEnable(rule: AlertRuleDTO) {
    await api.updateAlertRule(rule.id, { enabled: !rule.enabled });
    message.success(rule.enabled ? '已停用' : '已启用');
    fetch();
    onRulesChange && onRulesChange();
  }

  // 删除（支持批量）
  async function handleDelete(ids: number[]) {
    Modal.confirm({
      title: `确认删除${ids.length > 1 ? ids.length + '条' : ''}规则？`,
      onOk: async () => {
        if (ids.length === 1) {
          await api.deleteAlertRule(ids[0]);
        } else {
          await api.deleteAlertRules(ids);
        }
        message.success('删除成功');
        setSelectedRowKeys([]);
        fetch();
        onRulesChange && onRulesChange();
      }
    });
  }

  const canEdit = role !== 'user';

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
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[])
        }}
        columns={[
          { title: '名称', dataIndex: 'name' },
          { title: '表达式', dataIndex: 'expr' },
          { title: '等级', dataIndex: 'level', render: (v: number) => LEVEL_MAP[v] },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag>停用</Tag> },
          { title: '最近触发', dataIndex: 'lastFired', render: (v: string) => v ? new Date(v).toLocaleString() : '-' },
          {
            title: '操作',
            render: (_: any, r: AlertRule) => (
              <Space>
                <Button type="link" size="small" onClick={() => { setEditId(r.id); setModalVisible(true); }} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" onClick={() => handleEnable(r)} disabled={!canEdit}>
                  {r.enabled ? '停用' : '启用'}
                </Button>
                <Button type="link" size="small" danger onClick={() => handleDelete([r.id])} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
        pagination={false}
      />
      {modalVisible && (
        <EditRuleModal
          ruleId={editId}
          alertId={alertId}
          onOk={() => { setModalVisible(false); fetch(); onRulesChange && onRulesChange(); }}
          onCancel={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default RuleList;
