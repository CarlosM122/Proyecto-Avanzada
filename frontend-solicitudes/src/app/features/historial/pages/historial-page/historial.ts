import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HistorialService } from '../../services/historial.service';
import { Historial } from '../../models/historial';

@Component({
  selector:'app-historial',
  standalone:true,
  imports:[CommonModule],
  templateUrl:'./historial.html',
  styleUrl:'./historial.css'
})

export class HistorialComponent implements OnInit{

  historial:Historial[]=[];

  idSolicitud:number=1;

  constructor(
    private historialService:HistorialService
  ){}

  ngOnInit():void{

    this.historialService
    .obtenerHistorial(this.idSolicitud)
    .subscribe({

      next:(data)=>{

        this.historial = [...data];

      },

      error:(err)=>{

        console.error(
          "Error al cargar historial:",
          err
        );

      }

    });

  }

}