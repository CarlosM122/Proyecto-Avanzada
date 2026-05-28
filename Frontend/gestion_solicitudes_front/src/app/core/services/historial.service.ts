import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Historial } from '../models/historial';

const API = 'http://localhost:8081';

@Injectable({ providedIn: 'root' })
export class HistorialService {
  private http = inject(HttpClient);

  obtener(solicitudId: number) {
    return this.http.get<Historial[]>(`${API}/historial/${solicitudId}`);
  }
}
