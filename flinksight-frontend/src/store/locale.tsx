import React, { createContext, useContext, useState, PropsWithChildren } from 'react';

export interface LocaleStore {
  locale: string;
  setLocale: (locale: string) => void;
}

const LocaleContext = createContext<LocaleStore>({} as LocaleStore);

export const LocaleProvider: React.FC<PropsWithChildren> = ({ children }) => {
  const [locale, setLocale] = useState<string>(localStorage.getItem('locale') || 'zh');

  const handleSetLocale = (l: string) => {
    setLocale(l);
    localStorage.setItem('locale', l);
    // 如需无刷新切换建议引入 i18next 或 react-intl
    window.location.reload();
  };

  return (
    <LocaleContext.Provider value={{ locale, setLocale: handleSetLocale }}>
      {children}
    </LocaleContext.Provider>
  );
};

export const useLocale = (): LocaleStore => useContext(LocaleContext);
