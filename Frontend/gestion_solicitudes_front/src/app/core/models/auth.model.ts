export interface LoginRequest {
  correo: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface RegisterRequest {
  correo: string;
  password: string;
  nombre: string;
  role: 'ESTUDIANTE' | 'ADMINISTRATIVO' | 'DOCENTE';
  semestre?: number; // solo Estudiante
  tipoContrato?: 'PLANTA' | 'CATEDRATICO'; // Docente/Administrativo
  areaEncargada?: string; // solo Administrativo
}
