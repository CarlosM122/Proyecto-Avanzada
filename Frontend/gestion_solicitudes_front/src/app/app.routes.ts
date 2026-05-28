import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { ListaSolicitudes } from './features/solicitudes/lista-solicitudes/lista-solicitudes';
import { authGuard } from './core/guards/auth-guard';
import { DetalleSolicitud } from './features/solicitudes/detalle-solicitud/detalle-solicitud';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'solicitudes', pathMatch: 'full' },
      { path: 'solicitudes', component: ListaSolicitudes },
      { path: 'solicitudes/:id', component: DetalleSolicitud },
    ],
  },
  { path: '**', redirectTo: 'login', pathMatch: 'full' },
];
