import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { menus } from '../routes/menuConfig';
import type { RootState } from '../store';

const Sidebar: React.FC = () => {
  const location = useLocation();
  const { menu } = useSelector((state: RootState) => state.menu);
  return (
    <aside className="w-56 bg-gray-50 border-r h-full shadow-sm">
      <nav>
        <ul>
          {(menu || menus).map(item => (
            <li key={item.path} className={location.pathname.startsWith(item.path) ? 'bg-blue-50' : ''}>
              <Link to={item.path} className="block py-3 px-6 text-base text-gray-800 hover:bg-blue-100 transition">{item.name}</Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
};
export default Sidebar;
