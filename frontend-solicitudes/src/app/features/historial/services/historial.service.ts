import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Historial } from '../models/historial';

@Injectable({
  providedIn: 'root'
})
export class HistorialService {

  private api = 'http://localhost:8081/historial';

  constructor(private http: HttpClient) {}

  obtenerHistorial(id:number):Observable<Historial[]>{

    return this.http.get<Historial[]>(
      `${this.api}/${id}`
    );

  }
}