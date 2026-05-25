import { Routes } from '@angular/router';

import { Login } from './features/security/pages/login/login';

import { Register } from './features/security/pages/register/register';

import { Dashboard } from './features/dashboard/pages/dashboard/dashboard';

import { authGuard } from './features/security/guards/auth.guard';

export const routes: Routes = [

  {
    path: '',
    component: Login
  },

  {
    path: 'register',
    component: Register
  },

  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuard]
  },

  {
    path: '**',
    redirectTo: ''
  }

];