import { Routes } from '@angular/router';

import { Login } from './features/security/pages/login/login';

import { Register } from './features/security/pages/register/register';

export const routes: Routes = [

  {
    path: '',
    component: Login
  },

  {
    path: 'register',
    component: Register
  }

];