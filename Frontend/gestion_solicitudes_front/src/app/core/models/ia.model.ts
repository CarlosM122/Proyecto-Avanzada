export interface SugerenciaIA {
  categoria: string;
  prioridad: string;
  confianza: number;
  explicacion: string;
}

export interface ResumenIA {
  resumen: string;
  generadoPor: string;
}
