import React from 'react';
import {useLocale} from '../store/locale';

const langs = [
  { key: 'zh', label: '中文' },
  { key: 'en', label: 'English' }
];

const LanguageSwitcher: React.FC = () => {
  const { locale, setLocale } = useLocale();

  return (
    <div className="flex items-center space-x-2">
      {langs.map(l => (
        <button
          key={l.key}
          className={`px-2 py-1 rounded ${locale === l.key ? 'bg-blue-500 text-white' : 'text-gray-700'}`}
          onClick={() => setLocale(l.key)}
        >{l.label}</button>
      ))}
    </div>
  );
};
export default LanguageSwitcher;
