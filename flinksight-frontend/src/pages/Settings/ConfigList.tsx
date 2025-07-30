/**
 * @file 平台参数配置
 * @desc 全局参数、业务开关、通知通道等配置中心，支持动态调整
 */
import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, Switch, message } from 'antd';
import http from '@/api/http';

interface ConfigItem {
  id: number;
  key: string;
  value: string;
  desc?: string;
  enable: boolean;
}

const ConfigList: React.FC = () => {
  const [list, setList] = useState<ConfigItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editing, setEditing] = useState<ConfigItem | null>(null);
  const [form] = Form.useForm();

  const fetchList = async () => {
    setLoading(true);
    try {
      const res = await http.get('/config');
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchList(); }, []);

  const handleAddOrEdit = async (values: any) => {
    if (editing) {
      await http.put(`/config/${editing.id}`, values);
      message.success('修改成功');
    } else {
      await http.post('/config', values);
      message.success('添加成功');
    }
    setModalVisible(false);
    fetchList();
    form.resetFields();
    setEditing(null);
  };

  return (
    <div>
      <Button type="primary" onClick={() => { setEditing(null); setModalVisible(true); }}>新建参数</Button>
      <Table
        rowKey="id"
        columns={[
          { title: '参数Key', dataIndex: 'key' },
          { title: '参数值', dataIndex: 'value' },
          { title: '描述', dataIndex: 'desc' },
          {
            title: '开关', dataIndex: 'enable', render: (val, record) =>
              <Switch checked={val} onChange={async (checked) => {
                await http.patch(`/config/${record.id}`, { enable: checked });
                fetchList();
              }} />
          },
          {
            title: '操作', render: (_, record) => (
              <Button size="small" onClick={() => { setEditing(record); setModalVisible(true); }}>编辑</Button>
            )
          }
        ]}
        dataSource={list}
        loading={loading}
        style={{ marginTop: 24 }}
      />
      <Modal
        title={editing ? '编辑参数' : '新建参数'}
        visible={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
      >
        <Form form={form} onFinish={handleAddOrEdit} layout="vertical">
          <Form.Item label="参数Key" name="key" rules={[{ required: true }]}>
            <Input disabled={!!editing} />
          </Form.Item>
          <Form.Item label="参数值" name="value" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label="描述" name="desc"><Input /></Form.Item>
          <Form.Item label="开关" name="enable" valuePropName="checked">
            <Switch />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ConfigList;
