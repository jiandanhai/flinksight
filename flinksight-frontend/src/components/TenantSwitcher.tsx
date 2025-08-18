import React, {useEffect, useState} from 'react';
import {Select, Spin} from 'antd';
import api from '@/api/api-compat';

import {useTenant} from '../store/tenant';


/**租户选择器和多租户数据隔离 （全局，顶部栏/下拉）*/
const TenantSwitcher: React.FC = () => {
  const { tenantId, setTenantId } = useTenant();
  const [options, setOptions] = useState<{ label: string, value: string }[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getTenants().then(res => {
      setOptions((res.data || []).map((t: any) => ({ label: t.name, value: t.id })));
      setLoading(false);
    });
  }, []);

  if (loading) return <Spin />;

  return (
    <Select
      style={{ width: 160 }}
      value={tenantId}
      onChange={id => setTenantId(id)}
      options={options}
      placeholder="选择租户"
    />
  );
};
export default TenantSwitcher;
