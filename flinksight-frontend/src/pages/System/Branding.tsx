/**
 * @file SaaS品牌自定义
 * @desc 支持运营侧更换LOGO/配色/登录页Banner等
 */
import React from "react";
import { Form, Input, Upload, Button, Card, message } from "antd";
import http from "@/api/http";

const Branding: React.FC = () => {
  const [form] = Form.useForm();

  const onFinish = async (values: any) => {
    await http.post("/settings/branding", values);
    message.success("品牌设置已更新");
  };

  return (
    <Card title="品牌与主题配置">
      <Form form={form} onFinish={onFinish} labelCol={{ span: 6 }} wrapperCol={{ span: 12 }}>
        <Form.Item label="平台名称" name="name">
          <Input />
        </Form.Item>
        <Form.Item label="LOGO" name="logo">
          <Upload action="/api/upload/logo" listType="picture">
            <Button>上传LOGO</Button>
          </Upload>
        </Form.Item>
        <Form.Item label="主色调" name="primaryColor">
          <Input type="color" />
        </Form.Item>
        <Form.Item wrapperCol={{ offset: 6 }}>
          <Button type="primary" htmlType="submit">保存</Button>
        </Form.Item>
      </Form>
    </Card>
  );
};
export default Branding;
