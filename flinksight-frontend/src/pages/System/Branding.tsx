/**
 * @file SaaS品牌自定义
 * @desc 支持运营侧更换LOGO/配色/登录页Banner等
 */
import React from "react";
import { Button, Card, Form, Input, message, Upload } from "antd";
import  api  from 'src/api/gen/client';

const Branding: React.FC = () => {
  const [form] = Form.useForm();

  // 提交表单
  const onFinish = async (values: any) => {
    // 假设 openapi 里有 settingsControllerSetBranding 这样的方法
    await api.settingsControllerSetBranding(values);
    message.success("品牌设置已更新");
  };

  return (
    <Card title="品牌与主题配置">
      <Form
        form={form}
        onFinish={onFinish}
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 12 }}
      >
        <Form.Item label="平台名称" name="name">
          <Input />
        </Form.Item>
        <Form.Item label="LOGO" name="logo">
          <Upload
            action="/api/upload/logo"
            listType="picture"
            maxCount={1}
            // 可在onChange里处理上传结果并同步到form里
          >
            <Button>上传LOGO</Button>
          </Upload>
        </Form.Item>
        <Form.Item label="主色调" name="primaryColor">
          <Input type="color" />
        </Form.Item>
        <Form.Item wrapperCol={{ offset: 6 }}>
          <Button type="primary" htmlType="submit">
            保存
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default Branding;
