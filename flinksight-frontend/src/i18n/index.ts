import zh from './zh';
import en from './en';

export const locales = { zh, en };
export type LocaleType = keyof typeof locales;

let currentLocale: LocaleType = 'zh';

export function t(key: string): string {
  return locales[currentLocale][key] || key;
}

export function setLocale(locale: LocaleType) {
  currentLocale = locale;
}

export function getLocale(): LocaleType {
  return currentLocale;
}
