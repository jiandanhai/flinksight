// src/utils/tenant.ts
export const getTenantId = () =>
  Number(localStorage.getItem('FS_TENANT_ID') ?? '1');
