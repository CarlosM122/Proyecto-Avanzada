import { Usuario } from './usuario.model';

export type Estado = 'REGISTRADA' | 'CLASIFICADA' | 'EN_ATENCION' | 'ATENDIDA' | 'CERRADA';
export type Prioridad = 'ALTA' | 'MEDIA' | 'BAJA';
export type TipoSolicitud =
  | 'REGISTRO_ASIGNATURAS'
  | 'HOMOLOGACION'
  | 'CANCELACION_ASIGNATURAS'
  | 'SOLICITUD_CUPOS'
  | 'CONSULTA_ACADEMICA';
export type OrigenSolicitud = 'CSU' | 'PRESENCIAL' | 'CORREO' | 'SAC' | 'TELEFONICO';

export interface Solicitud {
  id: number;
  solicitante: Usuario;
  descripcion: string;
  estado: Estado;
  prioridad: Prioridad;
  tipoSolicitud: TipoSolicitud;
  fecha: string;
  justificacionPrioridad: string;
  responsable?: Usuario;
}

export interface SolicitudRequest {
  descripcion: string;
  tipoSolicitud: TipoSolicitud;
  origen: OrigenSolicitud;
  idSolicitante?: number;
}

export interface ClasificacionRequest {
  prioridad: Prioridad;
  justificacion: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
