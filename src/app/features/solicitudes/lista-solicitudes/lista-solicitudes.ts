import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { AuthService } from '../../../core/services/auth.service';
import { Solicitud, Estado, TipoSolicitud, Prioridad } from '../../../core/models/solicitud.model';

@Component({
  selector: 'app-lista-solicitudes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './lista-solicitudes.html',
  styleUrl: './lista-solicitudes.css',
})
export class ListaSolicitudes implements OnInit {
  private solicitudService = inject(SolicitudService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  // ── Estado ──
  solicitudes = signal<Solicitud[]>([]);
  loading = signal(true);
  error = signal('');

  // ── Filtros ──
  filtroEstado = signal<string>('');
  filtroTipo = signal<string>('');
  filtroPrioridad = signal<string>('');

  // ── Modal crear solicitud ──
  modalAbierto = signal(false);
  loadingCrear = signal(false);
  errorCrear = signal('');

  // ── Modal crear usuario ──
  modalCrearUsuario = signal(false);
  loadingCrearUsuario = signal(false);
  errorCrearUsuario = signal('');
  rolSeleccionado = signal<string>('');

  // ── Computed ──
  solicitudesFiltradas = computed(() => {
    return this.solicitudes().filter((s) => {
      const matchEstado = !this.filtroEstado() || s.estado === this.filtroEstado();
      const matchTipo = !this.filtroTipo() || s.tipoSolicitud === this.filtroTipo();
      const matchPrioridad = !this.filtroPrioridad() || s.prioridad === this.filtroPrioridad();
      return matchEstado && matchTipo && matchPrioridad;
    });
  });

  totalSolicitudes = computed(() => this.solicitudes().length);

  userName = computed(() => {
    const token = this.authService.token();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.nombre ?? payload.sub ?? 'Usuario';
    } catch {
      return 'Usuario';
    }
  });

  userInitial = computed(() => this.userName().charAt(0).toUpperCase());

  userRole = computed(() => {
    const role = this.authService.getRole();
    const labels: Record<string, string> = {
      ROL_ESTUDIANTE: 'Estudiante',
      ROL_ADMINISTRATIVO: 'Administrativo',
      ROL_DOCENTE: 'Docente',
    };
    return role ? (labels[role] ?? role) : '';
  });

  canCreate = computed(() => this.authService.getRole() === 'ROL_ESTUDIANTE');
  isAdmin = computed(() => this.authService.getRole() === 'ROL_ADMINISTRATIVO');
  esDocente = computed(() => this.rolSeleccionado() === 'DOCENTE');
  esAdminForm = computed(() => this.rolSeleccionado() === 'ADMINISTRATIVO');

  // ── Formularios ──
  solicitudForm: FormGroup = this.fb.group({
    tipoSolicitud: ['', Validators.required],
    origen: ['', Validators.required],
    descripcion: ['', Validators.required],
  });

  usuarioForm: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    role: ['', Validators.required],
    tipoContrato: ['', Validators.required],
    areaEncargada: [''],
  });

  ngOnInit() {
    this.cargarSolicitudes();
  }

  cargarSolicitudes() {
    this.loading.set(true);
    this.error.set('');
    this.solicitudService.listar(0, 100).subscribe({
      next: (page) => {
        this.solicitudes.set(page.content);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las solicitudes.');
        this.loading.set(false);
      },
    });
  }

  // ── Filtros ──
  filtrarEstado(event: Event) {
    this.filtroEstado.set((event.target as HTMLSelectElement).value);
  }
  filtrarTipo(event: Event) {
    this.filtroTipo.set((event.target as HTMLSelectElement).value);
  }
  filtrarPrioridad(event: Event) {
    this.filtroPrioridad.set((event.target as HTMLSelectElement).value);
  }
  resetFiltros() {
    this.filtroEstado.set('');
    this.filtroTipo.set('');
    this.filtroPrioridad.set('');
  }

  // ── Stats ──
  countByEstado(estado: string): number {
    return this.solicitudes().filter((s) => s.estado === estado).length;
  }

  // ── Modal solicitud ──
  abrirModal() {
    this.solicitudForm.reset();
    this.errorCrear.set('');
    this.modalAbierto.set(true);
  }
  cerrarModal() {
    this.modalAbierto.set(false);
  }

  crearSolicitud() {
    if (this.solicitudForm.invalid) {
      this.solicitudForm.markAllAsTouched();
      return;
    }
    this.loadingCrear.set(true);
    this.errorCrear.set('');
    this.solicitudService.crear(this.solicitudForm.value).subscribe({
      next: (nueva) => {
        this.solicitudes.update((list) => [nueva, ...list]);
        this.loadingCrear.set(false);
        this.cerrarModal();
      },
      error: () => {
        this.errorCrear.set('No se pudo crear la solicitud.');
        this.loadingCrear.set(false);
      },
    });
  }

  // ── Modal crear usuario ──
  abrirModalUsuario() {
    this.usuarioForm.reset();
    this.errorCrearUsuario.set('');
    this.rolSeleccionado.set('');
    this.modalCrearUsuario.set(true);
  }

  cerrarModalUsuario() {
    this.modalCrearUsuario.set(false);
  }

  onRolChange(event: Event) {
    const rol = (event.target as HTMLSelectElement).value;
    this.rolSeleccionado.set(rol);
    if (rol !== 'ADMINISTRATIVO') {
      this.usuarioForm.get('areaEncargada')?.setValue('');
    }
  }

  crearUsuario() {
    if (this.esAdminForm() && !this.usuarioForm.get('areaEncargada')?.value) {
      this.usuarioForm.get('areaEncargada')?.setErrors({ required: true });
    }

    if (this.usuarioForm.invalid) {
      this.usuarioForm.markAllAsTouched();
      return;
    }

    this.loadingCrearUsuario.set(true);
    this.errorCrearUsuario.set('');

    const formValue = this.usuarioForm.value;
    const body: any = {
      nombre: formValue.nombre,
      correo: formValue.correo,
      password: formValue.password,
      role: formValue.role,
      tipoContrato: formValue.tipoContrato,
    };

    if (formValue.areaEncargada) {
      body.areaEncargada = formValue.areaEncargada;
    }

    this.authService.crearUsuario(body).subscribe({
      next: () => {
        this.loadingCrearUsuario.set(false);
        this.cerrarModalUsuario();
      },
      error: (err) => {
        this.loadingCrearUsuario.set(false);
        if (err.status === 400) {
          this.errorCrearUsuario.set('El correo ya está registrado.');
        } else {
          this.errorCrearUsuario.set('No se pudo crear el usuario.');
        }
      },
    });
  }

  // ── Detalle ──
  verDetalle(id: number) {
    this.router.navigate(['/solicitudes', id]);
  }

  // ── Logout ──
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // ── Formatters ──
  formatEstado(estado: Estado): string {
    const map: Record<string, string> = {
      REGISTRADA: 'Registrada',
      CLASIFICADA: 'Clasificada',
      EN_ATENCION: 'En atención',
      ATENDIDA: 'Atendida',
      CERRADA: 'Cerrada',
    };
    return map[estado] ?? estado;
  }

  formatTipo(tipo: TipoSolicitud): string {
    const map: Record<string, string> = {
      REGISTRO_ASIGNATURAS: 'Registro',
      HOMOLOGACION: 'Homologación',
      CANCELACION_ASIGNATURAS: 'Cancelación',
      SOLICITUD_CUPOS: 'Cupos',
      CONSULTA_ACADEMICA: 'Consulta',
    };
    return map[tipo] ?? tipo;
  }

  toast = signal<{ mensaje: string; tipo: 'exito' | 'error' } | null>(null);

  mostrarToast(mensaje: string, tipo: 'exito' | 'error' = 'exito') {
    this.toast.set({ mensaje, tipo });
    setTimeout(() => this.toast.set(null), 3000);
  }
}
