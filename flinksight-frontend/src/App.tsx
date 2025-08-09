import React from 'react';
import Router from './router';
import { UserProvider } from './store/user';
// import { LocaleProvider } from './store/locale'; // 如有国际化

const App: React.FC = () => (
  <UserProvider>
    {/* <LocaleProvider> */}
      <Router />
    {/* </LocaleProvider> */}
  </UserProvider>
);

export default App;