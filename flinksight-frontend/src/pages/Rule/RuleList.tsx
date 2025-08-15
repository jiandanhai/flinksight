/**
 * @file 告警规则管理页
 * @desc 全量管理所有告警规则，支持筛选、编辑、批量删除、启停、类型/级别筛选
 */
import React, {useEffect, useState} from 'react';
import {Button, Input, message, Modal, Select, Space, Table, Tag} from 'antd';
import  api  from 'src/api/gen/client';

import type {AlertRuleDTO} from '../../api/gen/data-contracts.ts';
import EditRuleModal from './EditRuleModal';
import {useUser} from '../../store/user';

const { Search } = Input;
const { Option } = Select;

const RuleList: React.FC = () => {
  const [list, setList] = useState<AlertRuleDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<AlertRuleDTO>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const { role } = useUser();

  // 拉取规则列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllRules({ ...query, page, size });
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

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该规则？',
      onOk: async () => {
        await api.deleteRule(id);
        message.success('已删除');
        fetch();
      }
    });
  }

  // 批量启用/停用
  async function handleBatchEnable(enable: boolean) {
    await api.batchUpdateRuleStatus(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已停用');
    setSelectedRowKeys([]);
    fetch();
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="规则名/关键字" allowClear enterButton onSearch={handleSearch} style={{ width: 280 }} />
        <Space>
          <Select
            style={{ width: 120 }}
            placeholder="级别筛选"
            allowClear
            onChange={level => setQuery({ ...query, level })}
          >
            <Option value={1}>致命</Option>
            <Option value={2}>高</Option>
            <Option value={3}>中</Option>
            <Option value={4}>低</Option>
          </Select>
          <Button type="primary" onClick={() => openModal()}>新建规则</Button>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length}>批量停用</Button>
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
          { title: '规则名', dataIndex: 'name' },
          { title: '级别', dataIndex: 'level', render: (v: number) => ['低', '中', '高', '致命'][v - 1] },
          { title: '条件', dataIndex: 'expr' },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag> },
          {
            title: '操作',
            render: (_: any, r: Rule) => (
              <Space>
                <Button type="link" size="small" onClick={() => openModal(r.id)}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(r.id)}>删除</Button>
              </Space>
            )
          }
        ]}
      />
      {modalVisible && (
        <EditRuleModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default RuleList;
