import { Routes } from '@angular/router';

import { Login } from './features/security/pages/login/login';
import { Register } from './features/security/pages/register/register';
import { Dashboard } from './features/dashboard/pages/dashboard/dashboard';

import { PanelIaComponent } from './features/ia/pages/panel-ia/panel-ia';
import { HistorialComponent } from './features/historial/pages/historial-page/historial';

import { authGuard } from './features/security/guards/auth.guard';

export const routes: Routes = [

  {
    path:'',
    component:Login
  },

  {
    path:'register',
    component:Register
  },

  {
    path:'dashboard',
    component:Dashboard,
    canActivate:[authGuard]
  },

  {
    path:'ia',
    component:PanelIaComponent
  },

  {
    path:'historial',
    component:HistorialComponent
  },

  {
    path:'**',
    redirectTo:''
  }

];