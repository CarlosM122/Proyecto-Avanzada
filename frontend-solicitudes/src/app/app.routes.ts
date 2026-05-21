import { Routes } from '@angular/router';

import { Login } from './features/security/pages/login/login';
import { Register } from './features/security/pages/register/register';

import { PanelIaComponent } from './features/ia/pages/panel-ia/panel-ia';
import { HistorialComponent } from './features/historial/pages/historial-page/historial';

export const routes: Routes = [

  {
    path:'',
    component: Login
  },

  {
    path:'register',
    component: Register
  },

  {
    path:'ia',
    component: PanelIaComponent
  },

  {
    path:'historial',
    component: HistorialComponent
  }

];