import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UsuarioResumen } from '../models/usuario.model';

const API = 'http://localhost:8081';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);

  obtenerResponsables() {
    return this.http.get<UsuarioResumen[]>(`${API}/usuarios/responsables`);
  }
}
