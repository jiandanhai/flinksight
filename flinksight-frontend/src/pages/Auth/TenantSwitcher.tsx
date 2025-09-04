/**
 * @file 多租户切换/自注册（后端中转 + Keycloak）
 * @desc SaaS租户自服务入口，支持切换/新建
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, Form, Input, message, Modal, Select, Spin, Empty } from 'antd';
import api from '@/api/api-compat';
import { useUser } from '@/context/UserContext';

interface Tenant {
  id: number;
  name: string;
}

type Props = { visible: boolean; onClose: () => void };

const TenantSwitcher: React.FC<Props> = ({ visible, onClose }) => {
  const { user } = useUser() as any; // 若上下文里包含 currentTenant，可用于 defaultValue
  const [list, setList] = useState<Tenant[]>([]);
  const [selected, setSelected] = useState<number | undefined>(undefined);
  const [showNew, setShowNew] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [switching, setSwitching] = useState(false);
  const [creating, setCreating] = useState(false);
  const [form] = Form.useForm();

  // 当前租户名称（用于 UI 提示）
  const currentTenantName = useMemo(() => {
    const id = user?.tenantId ?? user?.currentTenantId;
    return list.find(i => i.id === id)?.name;
  }, [list, user]);

  // 拉取当前用户的可切换租户列表
  useEffect(() => {
    if (!visible) return;
    (async () => {
      try {
        setFetching(true);
        const res = await api.tenantControllerListSelf();
        const data: Tenant[] = res?.data || [];
        setList(data);
        // 默认选中当前租户（若有）
        const currentId = user?.tenantId ?? user?.currentTenantId;
        if (currentId) setSelected(currentId);
      } catch (err: any) {
        message.error(err?.message || '获取租户列表失败');
      } finally {
        setFetching(false);
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [visible]);

  // 切换租户
  const handleSwitch = async () => {
    if (!selected) return message.warning('请先选择要切换的租户');
    try {
      setSwitching(true);
      await api.tenantControllerSwitch({ tenantId: selected });
      message.success('切换成功');

      // 优先尝试更新全局请求头/上下文，避免整页刷新
      try {
        // 如果你的 api 客户端是 axios，可在此处更新默认 Header
        // (api as any).defaults.headers.common['X-Tenant-Id'] = String(selected);

        // 如果 useUser 提供了 setTenant / setUser，可在这里更新（按你项目实际命名修改）
        // const { setTenant } = useUser() as any;
        // setTenant?.(selected);

        // 通知父组件关闭
        onClose();
        // 若你的页面依赖 SWR/ReactQuery，可在此触发全局缓存失效
        // queryClient.invalidateQueries();

        // 如果没有全局状态或查询库，作为兜底再刷新
        if (!('requestIdleCallback' in window)) {
          window.location.reload();
        } else {
          (window as any).requestIdleCallback(() => window.location.reload());
        }
      } catch {
        window.location.reload();
      }
    } catch (err: any) {
      message.error(err?.message || '切换失败');
    } finally {
      setSwitching(false);
    }
  };

  // 新建租户
  const handleCreate = async (values: any) => {
    try {
      setCreating(true);
      await api.tenantControllerCreate(values);
      message.success('新租户已创建');

      // 创建成功后刷新列表并选中新租户（若后端返回ID可直接切换）
      setShowNew(false);
      form.resetFields();

      // 重新拉取列表
      setFetching(true);
      const res = await api.tenantControllerListSelf();
      const data: Tenant[] = res?.data || [];
      setList(data);
      setFetching(false);

      // 如果后端返回了新租户ID，更推荐：直接调用 switch 接口并完成刷新
      // await api.tenantControllerSwitch({ tenantId: newId }); window.location.reload();
    } catch (err: any) {
      message.error(err?.message || '创建租户失败');
    } finally {
      setCreating(false);
    }
  };

  return (
    <Modal
      open={visible}
      title="切换 / 新建租户"
      onCancel={onClose}
      footer={
        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
          <Button onClick={onClose} disabled={switching || creating}>取消</Button>
          <Button type="primary" onClick={handleSwitch} loading={switching} disabled={!selected}>
            切换到选中租户
          </Button>
        </div>
      }
      destroyOnClose
      maskClosable={false}
      centered
    >
      <Spin spinning={fetching}>
        <div style={{ marginBottom: 8, color: 'rgba(0,0,0,.55)' }}>
          当前租户：{currentTenantName || '（未选择）'}
        </div>

        {list.length > 0 ? (
          <Select
            showSearch
            allowClear
            style={{ width: '100%', marginBottom: 12 }}
            placeholder="选择要切换的租户"
            value={selected}
            options={list.map(i => ({ label: i.name, value: i.id }))}
            optionFilterProp="label"
            onChange={(val) => setSelected(val)}
          />
        ) : (
          <Empty description="暂无可用租户，您可以先新建一个" style={{ marginBottom: 12 }} />
        )}

        <Button type="link" onClick={() => setShowNew(v => !v)} style={{ paddingLeft: 0 }}>
          {showNew ? '取消创建' : '我要新建租户'}
        </Button>

        {showNew && (
          <Form
            form={form}
            onFinish={handleCreate}
            layout="vertical"
            style={{ marginTop: 8 }}
          >
            <Form.Item label="租户名" name="name" rules={[{ required: true, message: '请输入租户名' }]}>
              <Input placeholder="例如：上海数据团队 / ACME" />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={creating}>
                立即创建
              </Button>
            </Form.Item>
          </Form>
        )}
      </Spin>
    </Modal>
  );
};

export default TenantSwitcher;
