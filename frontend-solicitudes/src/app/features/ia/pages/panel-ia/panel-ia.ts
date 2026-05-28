import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IaService } from '../../services/ia.service';
import { SugerenciaIA } from '../../models/sugerencia-ia';

@Component({
  selector:'app-panel-ia',
  standalone:true,
  imports:[
    CommonModule,
    FormsModule
  ],
  templateUrl:'./panel-ia.html',
  styleUrl:'./panel-ia.css'
})

export class PanelIaComponent{

  descripcion:string='';

  resultado?:SugerenciaIA;

  constructor(
    private iaService:IaService
  ){}

  analizar():void{

    if(!this.descripcion.trim()){
      return;
    }

    this.iaService
      .clasificar(this.descripcion)
      .subscribe({

        next:(data)=>{
          this.resultado=data;
        },

        error:(error)=>{
          console.error(
            'Error al consultar IA:',
            error
          );
        }

      });

  }

}