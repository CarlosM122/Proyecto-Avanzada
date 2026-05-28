import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SugerenciaIA } from '../models/sugerencia-ia';
import { ResumenIA } from '../models/resumen-ia';

@Injectable({
  providedIn:'root'
})
export class IaService {

  private api="http://localhost:8081/ia";

  constructor(private http:HttpClient){}

  clasificar(descripcion:string):Observable<SugerenciaIA>{
    return this.http.post<SugerenciaIA>(
      `${this.api}/clasificar`,
      descripcion,
      {
        headers:{
          'Content-Type':'text/plain'
        }
      }
    );
  }

  resumen(id:number):Observable<ResumenIA>{
    return this.http.get<ResumenIA>(
      `${this.api}/resumen/${id}`
    );
  }
}