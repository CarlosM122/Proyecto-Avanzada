import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SugerenciaIA, ResumenIA } from '../models/ia.model';

const API = 'http://localhost:8081';

@Injectable({ providedIn: 'root' })
export class IaService {
  private http = inject(HttpClient);

  clasificar(descripcion: string) {
    return this.http.post<SugerenciaIA>(`${API}/ia/clasificar`, descripcion, {
      headers: { 'Content-Type': 'text/plain' },
    });
  }

  resumen(id: number) {
    return this.http.get<ResumenIA>(`${API}/ia/resumen/${id}`);
  }
}
