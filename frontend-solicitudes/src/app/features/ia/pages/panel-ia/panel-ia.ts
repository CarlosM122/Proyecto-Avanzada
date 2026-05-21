import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { IaService } from '../../services/ia.service';
import { SugerenciaIA } from '../../models/sugerencia-ia';

@Component({
 selector:'app-panel-ia',
 standalone:true,
 imports:[FormsModule],
 templateUrl:'./panel-ia.html',
 styleUrl:'./panel-ia.css'
})

export class PanelIaComponent{

 descripcion='';

 resultado?:SugerenciaIA;

 constructor(
  private iaService:IaService
 ){}

 analizar(){

    this.iaService
    .clasificar(this.descripcion)
    .subscribe({
      next:(data)=>{
          this.resultado=data;
      },
      error:(error)=>{
          console.log(error);
      }
    });

 }

}