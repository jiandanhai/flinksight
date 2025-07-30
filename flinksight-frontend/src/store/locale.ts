import { createContext, useContext, useState } from 'react';

export interface LocaleStore {
  locale: string;
  setLocale: (locale: string) => void;
}
const LocaleContext = createContext<LocaleStore>({} as LocaleStore);

export const LocaleProvider: React.FC<{children: React.ReactNode}> = ({ children }) => {
  const [locale, setLocale] = useState<string>(localStorage.getItem('locale') || 'zh');

  const handleSetLocale = (l: string) => {
    setLocale(l);
    localStorage.setItem('locale', l);
    window.location.reload(); // 如需无刷新切换可配合react-intl/i18next等
  };

  return (
    <LocaleContext.Provider value={{ locale, setLocale: handleSetLocale }}>
      {children}
    </LocaleContext.Provider>
  );
};

export const useLocale = () => useContext(LocaleContext);
