/**
 * @file 系统设置页
 * @desc 系统参数、报警配置等全量管理，支持自动表单同步
 */
import React, {useEffect, useState} from 'react';
import {Button, Card, Form, Input, message} from 'antd';
import { api } from 'src/api/gen/client';


const SettingsPage: React.FC = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.getAllSettings().then(res => form.setFieldsValue(res.data));
  }, [form]);

  const handleSubmit = async () => {
    setLoading(true);
    try {
      await api.updateSetting(form.getFieldsValue());
      message.success('设置已保存');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="系统设置" className="max-w-xl mx-auto">
      <Form form={form} layout="vertical" onFinish={handleSubmit}>
        <Form.Item name="alarmThreshold" label="报警阈值(%)" rules={[{ required: true }]}>
          <Input type="number" min={0} max={100} />
        </Form.Item>
        <Form.Item name="notifyEmail" label="通知邮箱">
          <Input />
        </Form.Item>
        <Form.Item name="adminContact" label="管理员联系方式">
          <Input />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>保存设置</Button>
        </Form.Item>
      </Form>
    </Card>
  );
};
export default SettingsPage;
