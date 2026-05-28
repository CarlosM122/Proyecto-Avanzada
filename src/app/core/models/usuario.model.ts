export interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  telefono?: string;
}

export interface UsuarioResumen {
  id: number;
  nombre: string;
  correo: string;
  role: string;
}
