import React, { useState } from 'react';
import { useLocation, useNavigate, Outlet } from 'react-router-dom';

const TabPages: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [tabs, setTabs] = useState<string[]>([location.pathname]);

  React.useEffect(() => {
    if (!tabs.includes(location.pathname)) setTabs(tabs => [...tabs, location.pathname]);
  }, [location.pathname, tabs]);

  const closeTab = (tab: string) => setTabs(tabs.filter(t => t !== tab));

  return (
    <div>
      <div className="flex border-b">
        {tabs.map(tab => (
          <div
            key={tab}
            className={`px-4 py-2 cursor-pointer ${tab === location.pathname ? 'bg-white font-bold' : 'bg-gray-100'} border-r`}
            onClick={() => navigate(tab)}
          >
            {tab}
            <span className="ml-2 text-gray-400" onClick={e => {e.stopPropagation(); closeTab(tab);}}>×</span>
          </div>
        ))}
      </div>
      <div className="bg-white rounded-b-xl p-6">
        <Outlet />
      </div>
    </div>
  );
};
export default TabPages;
