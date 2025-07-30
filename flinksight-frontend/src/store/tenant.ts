import { createContext, useContext, useState } from 'react';
export interface TenantStore {
  tenantId: string;
  setTenantId: (id: string) => void;
}
const TenantContext = createContext<TenantStore>({} as TenantStore);

export const TenantProvider: React.FC<{children: React.ReactNode}> = ({children}) => {
  const [tenantId, setTenantId] = useState(localStorage.getItem('tenantId') || '');
  const handleSet = (id: string) => {
    setTenantId(id); localStorage.setItem('tenantId', id); window.location.reload();
  };
  return (
    <TenantContext.Provider value={{ tenantId, setTenantId: handleSet }}>{children}</TenantContext.Provider>
  );
};
export const useTenant = () => useContext(TenantContext);
