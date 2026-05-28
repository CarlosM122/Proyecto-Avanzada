import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {
  Solicitud,
  SolicitudRequest,
  ClasificacionRequest,
  Page,
  Estado,
  TipoSolicitud,
  Prioridad,
} from '../models/solicitud.model';

const API = 'http://localhost:8081';

@Injectable({ providedIn: 'root' })
export class SolicitudService {
  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Solicitud>>(`${API}/solicitudes`, { params });
  }

  obtener(id: number) {
    return this.http.get<Solicitud>(`${API}/solicitudes/${id}`);
  }

  crear(body: SolicitudRequest) {
    return this.http.post<Solicitud>(`${API}/solicitudes`, body);
  }

  clasificar(id: number, body: ClasificacionRequest) {
    return this.http.patch<Solicitud>(`${API}/solicitudes/${id}/clasificar`, body);
  }

  asignarResponsable(id: number, responsableId: number) {
    return this.http.patch<Solicitud>(`${API}/solicitudes/${id}/asignacion`, responsableId);
  }

  atender(id: number, anotacion: string) {
    return this.http.patch<Solicitud>(`${API}/solicitudes/${id}/atender`, anotacion);
  }

  cerrar(id: number, anotacion: string) {
    return this.http.post<Solicitud>(`${API}/solicitudes/${id}/cerrar`, anotacion);
  }

  buscar(filtros: {
    estado?: Estado;
    tipo?: TipoSolicitud;
    prioridad?: Prioridad;
    responsableId?: number;
  }) {
    let params = new HttpParams();
    if (filtros.estado) params = params.set('estado', filtros.estado);
    if (filtros.tipo) params = params.set('tipo', filtros.tipo);
    if (filtros.prioridad) params = params.set('prioridad', filtros.prioridad);
    if (filtros.responsableId) params = params.set('responsableId', filtros.responsableId);
    return this.http.get<Solicitud[]>(`${API}/solicitudes/buscar`, { params });
  }
}
