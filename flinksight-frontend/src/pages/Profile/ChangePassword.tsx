import React from 'react';
import {Button, Form, Input, message} from 'antd';
import { getMyProfile ,updateProfile,changePassword} from '@/api/modules';


/**
 * 修改密码
 */
const ChangePassword: React.FC = () => {
  const [form] = Form.useForm();

  const handleFinish = async (values: any) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error('两次新密码输入不一致');
      return;
    }
    await api.changePassword({ oldPassword: values.oldPassword, newPassword: values.newPassword });
    message.success('密码修改成功');
    form.resetFields();
  };

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={handleFinish}
      className="max-w-md"
    >
      <Form.Item label="当前密码" name="oldPassword" rules={[{ required: true, message: '请输入当前密码' }]}>
        <Input.Password />
      </Form.Item>
      <Form.Item label="新密码" name="newPassword" rules={[{ required: true, message: '请输入新密码' }]}>
        <Input.Password />
      </Form.Item>
      <Form.Item label="确认新密码" name="confirmPassword" rules={[{ required: true, message: '请确认新密码' }]}>
        <Input.Password />
      </Form.Item>
      <Button type="primary" htmlType="submit">修改密码</Button>
    </Form>
  );
};
export default ChangePassword;
