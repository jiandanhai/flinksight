/**
 * @file 平台参数配置
 * @desc 全局参数、业务开关、通知通道等配置中心，支持动态调整
 */
import React, { useEffect, useState } from 'react';
import { Button, Form, Input, message, Modal, Switch, Table } from 'antd';
import { } from '@/api/modules';

// 建议后端openapi生成ConfigItemDTO类型
// interface ConfigItem { ... }

const ConfigList: React.FC = () => {
  const [list, setList] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editing, setEditing] = useState<any | null>(null);
  const [form] = Form.useForm();

  // 拉取配置参数列表
  const fetchList = async () => {
    setLoading(true);
    try {
      // 根据后端接口命名（举例configControllerList）
      const res = await listConfigs();
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchList(); }, []);

  // 新增或编辑参数
  const handleAddOrEdit = async (values: any) => {
    if (editing) {
      // 编辑：假设接口为 configControllerUpdate
      await configUpdate({ id: editing.id, ...values });
      message.success('修改成功');
    } else {
      // 新增：假设接口为 configControllerCreate
      await configCreate(values);
      message.success('添加成功');
    }
    setModalVisible(false);
    fetchList();
    form.resetFields();
    setEditing(null);
  };

  // 开关切换
  const handleSwitch = async (checked: boolean, record: any) => {
    await configUpdate({ id: record.id, enable: checked });
    fetchList();
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
              <Switch checked={val} onChange={checked => handleSwitch(checked, record)} />
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
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        destroyOnClose
      >
        <Form form={form} onFinish={handleAddOrEdit} layout="vertical" initialValues={editing || {}}>
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
