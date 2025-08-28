/**
 * @file 节点扩容弹窗（权限能力版）
 * @desc 支持单节点/CSV 批量扩容；默认按 hasPerm 判定，也可由父组件 canEdit 强制指定
 */
import React, { useState } from "react";
import { Button, Form, Input, message, Modal, Switch, Upload, Tooltip } from "antd";
import { UploadOutlined, InfoCircleOutlined } from "@ant-design/icons";
import type { NodeDTO } from '@/api/dto';
import { useUser } from "@/store/user";
import { createNode, batchCreateNode } from "@/api/modules";

/** 通用权限判定：优先 perms，再回退到 role */
function hasPerm(userInfo: any, permCode: string, roleFallback: string[] = ['admin', 'ops']) {
  const role = String(userInfo?.role || userInfo?.roleCode || '').toLowerCase();
  const perms: string[] = Array.isArray(userInfo?.perms) ? userInfo!.perms : [];
  return (perms?.includes(permCode)) || roleFallback.includes(role);
}

interface Props {
  open: boolean;
  onOk?: (result?: { success: number; fail: number; failList?: any[] }) => void;
  onClose: () => void;
  clusterId?: number;
  /** 父组件可强制指定是否可编辑；不传则按 hasPerm 计算 */
  canEdit?: boolean;
}

const ExpandNodeModal: React.FC<Props> = ({ open, onOk, onClose, clusterId, canEdit: canEditProp }) => {
  const [form] = Form.useForm();
  const [batchMode, setBatchMode] = useState(false);
  const [uploading, setUploading] = useState(false);
  const { userInfo } = useUser();

  // 默认以 CLUSTER_EDIT 能力为准；无显式传入时走默认规则
  const canEditDefault = hasPerm(userInfo, 'CLUSTER_EDIT', ['admin', 'ops']);
  const canEdit = typeof canEditProp === 'boolean' ? canEditProp : canEditDefault;

  // 单节点提交
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (clusterId) (values as any).clusterId = clusterId;
      await createNode(values as NodeDTO);
      message.success("节点扩容成功");
      onOk?.({ success: 1, fail: 0 });
      onClose();
      form.resetFields();
    } catch {
      /* 表单校验会自行提示 */
    }
  };

  // CSV 批量导入
  const handleBatchUpload = async (info: any) => {
    setUploading(true);
    try {
      const file = info.file?.originFileObj;
      if (!file) { setUploading(false); return; }
      const reader = new FileReader();
      reader.onload = async (e: any) => {
        const content = String(e.target.result || '');
        const lines = content.split(/\r?\n/).map(l => l.trim()).filter(Boolean);
        if (lines.length < 2) { message.error("CSV 至少包含一行数据"); setUploading(false); return; }
        const headers = lines[0].split(",");
        const idx = (k: string) => headers.indexOf(k);
        if (!['name','ip','role'].every(k => idx(k) >= 0)) {
          message.error("CSV 缺少字段：name, ip, role"); setUploading(false); return;
        }
        const rows: NodeDTO[] = lines.slice(1).map(line => {
          const arr = line.split(",");
          const n: any = {
            name: arr[idx('name')],
            ip: arr[idx('ip')],
            role: arr[idx('role')],
            clusterName: idx('clusterName') >= 0 ? arr[idx('clusterName')] : '',
            enabled: true,
          };
          if (clusterId) n.clusterId = clusterId;
          return n;
        }).filter(n => n.name && n.ip && n.role);

        if (!rows.length) { message.error("无有效节点数据"); setUploading(false); return; }

        const res = await batchCreateNode(rows);
        const d = (res as any)?.data ?? res;
        message.success(`批量扩容完成：成功 ${d?.success ?? rows.length} 台，失败 ${d?.fail ?? 0} 台`);
        onOk?.(d);
        setUploading(false);
        onClose();
      };
      reader.readAsText(file);
    } catch (e: any) {
      message.error(`批量导入失败：${e?.message || '未知错误'}`);
      setUploading(false);
    }
  };

  return (
    <Modal
      open={open}
      title="节点扩容"
      onCancel={onClose}
      onOk={batchMode ? undefined : handleSubmit}
      okButtonProps={{ disabled: batchMode || !canEdit, 'aria-disabled': batchMode || !canEdit }}
      cancelButtonProps={{ disabled: uploading }}
      destroyOnClose
      maskClosable={false}
      footer={
        batchMode
          ? null
          : [
              <Button key="cancel" onClick={onClose} disabled={uploading}>取消</Button>,
              <Tooltip key="ok-tip" title={canEdit ? '' : '无扩容权限，仅管理员/运维或具备 CLUSTER_EDIT 能力的用户可操作'}>
                <Button key="ok" type="primary" onClick={handleSubmit} disabled={!canEdit || uploading}>
                  确认扩容
                </Button>
              </Tooltip>,
            ]
      }
    >
      <div className="mb-4">
        <Switch
          checkedChildren="批量导入"
          unCheckedChildren="单节点"
          checked={batchMode}
          onChange={setBatchMode}
          disabled={uploading}
          aria-label="批量导入开关"
        />
        <span className="ml-4 text-gray-500">
          {batchMode ? (
            <>
              支持 CSV 导入，示例：<code>name,ip,role,clusterName</code>
              <Tooltip
                title={
                  <>
                    <div>CSV 首行为表头，后续为节点：</div>
                    <pre style={{ margin: 0 }}>
                      name,ip,role,clusterName{"\n"}
                      worker01,192.168.1.10,worker,集群A{"\n"}
                      master01,192.168.1.11,master,集群A
                    </pre>
                  </>
                }
              >
                <InfoCircleOutlined className="ml-1" />
              </Tooltip>
            </>
          ) : "单节点信息手动录入"}
        </span>
      </div>

      {batchMode ? (
        <Upload
          accept=".csv"
          beforeUpload={() => false}
          showUploadList={false}
          customRequest={handleBatchUpload}
          disabled={uploading || !canEdit}
        >
          <Tooltip title={canEdit ? '' : '无扩容权限'}>
            <Button icon={<UploadOutlined />} loading={uploading} disabled={uploading || !canEdit}>
              {uploading ? "上传中..." : "上传 CSV 批量导入"}
            </Button>
          </Tooltip>
        </Upload>
      ) : (
        <Form form={form} layout="vertical" autoComplete="off">
          <Form.Item name="name" label="节点名" rules={[{ required: true, message: "请输入节点名" }]}>
            <Input placeholder="worker01" disabled={!canEdit} />
          </Form.Item>
          <Form.Item
            name="ip"
            label="节点 IP"
            rules={[
              { required: true, message: "请输入节点 IP" },
              { pattern: /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/, message: "IP 格式不正确" },
            ]}
          >
            <Input placeholder="192.168.1.100" disabled={!canEdit} />
          </Form.Item>
          <Form.Item name="role" label="角色" rules={[{ required: true, message: "请输入节点角色" }]}>
            <Input placeholder="worker/master" disabled={!canEdit} />
          </Form.Item>
          <Form.Item name="clusterName" label="集群名" required={false}>
            <Input placeholder="集群A" disabled={!canEdit} />
          </Form.Item>
        </Form>
      )}

      {!canEdit && <div className="text-red-500 mt-3">无扩容权限，仅管理员/运维或具备 CLUSTER_EDIT 能力的账户可操作</div>}
    </Modal>
  );
};

export default ExpandNodeModal;
